/*
 * Binary solver is from http://scpsolver.org/
 */

package ilpSolver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;

import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;
import statementGraph.constraintAndFeatureEncoder.ConstraintAndFeatureEncoderV4;
import statementGraph.constraintAndFeatureEncoder.DependencePair;
import statementGraph.graphNode.StatementWrapper;

public class LearningBinaryIPSolverV4 {
	public static int PARALENGTH = StatementWrapper.statementsLabelSet.size()+ StatementWrapper.parentStatementsLabelSet.size() +2;
	
	private ConstraintAndFeatureEncoderV4 encoder;
	private LinearProgram lp;
	private List<DependencePair> astDependenceConstraints;
	private List<DependencePair> cfgDependenceConstraints;
	private List<DependencePair> ddgDependenceConstraints;
	private List<Integer> lineCostConstraints;
	private List<Integer> statementType;
	private List<Integer> parentStatementType;
	private List<Boolean> textClassifierResults;
	private Map<Integer,Integer> typeMap;
	private Map<Integer,Integer> parentTypeMap;
	
	/*
	 * The most important thing in this class
	 * [0,statementTypeLength-1], weight of each type;
	 * [statementTypeLength, statmentTypeLength+StatementParentTypeLegnth-1], weight of each parent type
	 * statementTypeLength+StatementParentTypeLegnth: weight for text classifier result;
	 * statementTypeLength+StatementParentTypeLegnth+1: penalty weight for ddg constraints.
	 */
	double[] parameters;
	int targetLineCount = -1;
	int statementCount = 0;
	boolean debug = false;
	
	public LearningBinaryIPSolverV4(ConstraintAndFeatureEncoderV4 encoder){
		this.encoder = encoder;
		this.astDependenceConstraints = new LinkedList<DependencePair>();
		this.cfgDependenceConstraints = new LinkedList<DependencePair>();
		this.ddgDependenceConstraints = new LinkedList<DependencePair>();
	}
	
	public void setDependenceConstraints(List<DependencePair> astConstraints, List<DependencePair> cfgConstraints, List<DependencePair> ddgConstraints){
		this.astDependenceConstraints.clear();
		this.astDependenceConstraints.addAll(astConstraints);
		this.cfgDependenceConstraints.clear();
		this.cfgDependenceConstraints.addAll(cfgConstraints);
		this.ddgDependenceConstraints.clear();
		this.ddgDependenceConstraints.addAll(ddgConstraints);
	}
	
	public void setLineCostConstraints(List<Integer> lineCostConstraints){
		this.lineCostConstraints = lineCostConstraints;
		this.statementCount = this.lineCostConstraints.size();
		if(debug){
			System.out.println("Line cost Constraints List:");
			for(Integer i: this.lineCostConstraints){
				System.out.println(i);
			}
			System.out.println("Statement counts:"+ this.statementCount);
		}
	}
	
	public void setTargetLineCount(int count){
		this.targetLineCount = count;
	}
	
	public void setParameters(double [] para){
		Assert.isTrue(para.length == PARALENGTH);
		this.parameters = para;
	}
	
	public void setTypeMap(Map<Integer,Integer> map){
		this.typeMap = map;
	}
	
	public void setParentTypeMap(Map<Integer,Integer> map){
		this.parentTypeMap = map;
	}
	
	public void setStatementType(List<Integer> types){
		this.statementType = types;
	}
	
	public void setParementStatementType(List<Integer> parentTypes){
		this.parentStatementType = parentTypes;
	}
	
	public void setTextClassifierResults(List<Boolean> predicts){
		this.textClassifierResults = predicts;
	}
	
	//For this version, we encode the type, parent type and text feature, so the implementation is simple.
	private double computeStatementWeight(int index){
		//Weight of statementType;
		int type = this.statementType.get(index);
		double result = this.parameters[this.typeMap.get(type)];
		//Weight of parent statement type;
		int parentType = this.parentStatementType.get(index);
		result += this.parameters[this.typeMap.size()+this.parentTypeMap.get(parentType)];
		//Weight of text classifier;		
		result += this.textClassifierResults.get(index)?this.parameters[this.typeMap.size()+this.parentTypeMap.size()]:0;
		return result;
	}
	
	//For now, just a simple soft constraint weight;
	private double computeDDGConstraintsPenalty(DependencePair pair){
		return this.parameters[this.typeMap.size()+this.parentTypeMap.size()+1];
	}
	
	public boolean[] solve(){
		//Object function: 
		double [] objectFunc = new double[this.statementCount];
		for(int i = 0; i < this.statementCount; i++){
			objectFunc[i] = computeStatementWeight(i);
		}
		//soft constraint on DDG dependence constraints:
		for(DependencePair pair : this.ddgDependenceConstraints){
			double cost = computeDDGConstraintsPenalty(pair);
			objectFunc[pair.sourceIndex] += cost;
			objectFunc[pair.destIndex] -= cost;
		}
		this.lp = new LinearProgram(objectFunc);
		//Line count constraints:
		double [] constraint0 = new double[this.statementCount];
		for(int i=0; i<constraint0.length; i++){
			constraint0[i] = this.lineCostConstraints.get(i);
		}
		this.lp.addConstraint(new LinearSmallerThanEqualsConstraint(constraint0, this.targetLineCount, "c_"));
		//hard constraint on AST dependence constraints:
		for(int i=0; i<this.astDependenceConstraints.size();i++){
			DependencePair pair = this.astDependenceConstraints.get(i);
			double[] implyConstraint = new double[this.statementCount];
			Arrays.fill(implyConstraint, 0);
			implyConstraint[pair.sourceIndex] = 1.0;
			implyConstraint[pair.destIndex] = -1.0;
			if(debug){
				System.out.println("<"+pair.sourceIndex+","+pair.destIndex+">");
				for(int j=0;j<implyConstraint.length;j++){
					System.out.print(implyConstraint[j]+" ");
				}
				System.out.println();
			}
			this.lp.addConstraint( new LinearBiggerThanEqualsConstraint(implyConstraint, 0, "c"+i));
		}
		//Some setup
		lp.setMinProblem(false);
		for(int i=0;i<this.statementCount;i++){
			this.lp.setBinary(i);
		}
		LinearProgramSolver solver  = SolverFactory.newDefault(); 
		double[] solution = solver.solve(lp);
		boolean [] binarySolution = new boolean[this.statementCount];
		for(int i=0; i<this.statementCount; i++){
			binarySolution[i] = Math.abs(solution[i]-1.0)<0.00001;
		}
		if(debug){
			for(int i=0;i<binarySolution.length;i++){
				System.out.println(binarySolution[i]);
			}
		}
		return binarySolution;
	}
	
	
	public String outputSolveResult(){
		return this.encoder.compressedProgram2String(this.solve());
	}
	
	
	public String outputLabeledResult(boolean [] labels){
		return this.encoder.compressedProgram2String(labels);
	}
	
	
	public String originalProgram2String(){
		return this.encoder.originProgram2String();
		
	}
	
	public List<StatementWrapper> getStatementWrapperList(){
		return this.encoder.getStatementWrapperList();
	}
}
