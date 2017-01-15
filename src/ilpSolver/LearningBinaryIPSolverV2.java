package ilpSolver;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;
import statementGraph.ConstraintAndFeatureEncoderV2;
import statementGraph.DependencePair;
import statementGraph.graphNode.StatementWrapper;

public class LearningBinaryIPSolverV2 {
	ConstraintAndFeatureEncoderV2 encoder;
	LinearProgram lp;
	List<DependencePair> dependenceConstraints;
	List<Integer> lineCostConstraints;
	List<Integer> statementType;
	Map<Integer,Integer> typeMap;
	double[] parameters;
	int targetLineCount = -1;
	int variableNum = 0;
	boolean debug = false;
	
	public LearningBinaryIPSolverV2(ConstraintAndFeatureEncoderV2 encoder){
		this.encoder = encoder;
		this.dependenceConstraints = new LinkedList<DependencePair>();
	}
	
	public void setDependenceConstraints(List<DependencePair> astConstraints, List<DependencePair> cfgConstraints, List<DependencePair> ddgConstraints){
		this.dependenceConstraints.clear();
		this.dependenceConstraints.addAll(astConstraints);
		this.dependenceConstraints.addAll(cfgConstraints);
		this.dependenceConstraints.addAll(ddgConstraints);
	}
	
	public void setLineCostConstraints(List<Integer> lineCostConstraints){
		this.lineCostConstraints = lineCostConstraints;
		this.variableNum = this.lineCostConstraints.size();
		if(debug){
			System.out.println("Line cost Constraints List:");
			for(Integer i: this.lineCostConstraints){
				System.out.println(i);
			}
			System.out.println("Variable counts:"+ this.variableNum);
		}
	}
	
	public void setTargetLineCount(int count){
		this.targetLineCount = count;
	}
	
	public void setParameters(double [] para){
		this.parameters = para;
	}
	
	public void setTypeMap(Map<Integer,Integer> map){
		this.typeMap = map;
	}
	
	public void setStatementType(List<Integer> types){
		this.statementType = types;
	}
	
	//For this version, we only encode the type, so the implementation is simple.
	private double computeStatementWeight(int type){
		return this.parameters[this.typeMap.get(type)];
	}
	
	public boolean[] solve(){
		//Object function: 
		double [] objectFunc = new double[this.variableNum];
		for(int i = 0; i < this.variableNum; i++){
			objectFunc[i] = computeStatementWeight(this.statementType.get(i));
		}
		this.lp = new LinearProgram(objectFunc);
		//Line count constraints:
		double [] constraint0 = new double[this.variableNum];
		for(int i=0; i<constraint0.length; i++){
			constraint0[i] = this.lineCostConstraints.get(i);
		}
		this.lp.addConstraint(new LinearSmallerThanEqualsConstraint(constraint0, this.targetLineCount, "c_"));
		//dependence constraints:
		for(int i=0; i<this.dependenceConstraints.size();i++){
			DependencePair pair = this.dependenceConstraints.get(i);
			double[] implyConstraint = new double[this.variableNum];
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
		for(int i=0;i<this.variableNum;i++){
			this.lp.setBinary(i);
		}
		LinearProgramSolver solver  = SolverFactory.newDefault(); 
		double[] solution = solver.solve(lp);
		boolean [] binarySolution = new boolean[this.variableNum];
		for(int i=0; i<this.variableNum; i++){
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
