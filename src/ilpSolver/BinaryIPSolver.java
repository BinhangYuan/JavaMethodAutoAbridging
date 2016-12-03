package ilpSolver;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LPWizard;
import scpsolver.problems.LinearProgram;
import statementGraph.DependencePair;

public class BinaryIPSolver {
	LinearProgram lp;
	List<DependencePair> dependenceConstraints;
	List<Integer> lineCostConstraints;
	int targetLineCount = -1;
	int variableNum = 0;
	
	public BinaryIPSolver(){
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
		System.out.println("Line cost Constraints List:");
		for(Integer i: this.lineCostConstraints){
			System.out.println(i);
		}
		System.out.println("Variable counts:"+ this.variableNum);
	}
	
	public void setTargetLineCount(int count){
		this.targetLineCount = count;
	}
	
	public void solve(){
		//Object function: 
		double [] objectFunc = new double[this.variableNum];
		Arrays.fill(objectFunc, 1.0);
		this.lp = new LinearProgram(objectFunc);
		//Line count constraints:
		double [] constraint0 = new double[this.variableNum];
		for(int i=0; i<constraint0.length; i++){
			constraint0[i] = this.lineCostConstraints.get(i);
		}
		this.lp.addConstraint( new LinearSmallerThanEqualsConstraint(constraint0, this.targetLineCount, "c_"));
		//dependence constraints:
		for(int i=0; i<this.dependenceConstraints.size();i++){
			DependencePair pair = this.dependenceConstraints.get(i);
			double[] implyConstraint = new double[this.variableNum];
			Arrays.fill(implyConstraint, 0);
			implyConstraint[pair.sourceIndex] = 1.0;
			implyConstraint[pair.destIndex] = -1.0;
			System.out.println("<"+pair.sourceIndex+","+pair.destIndex+">");
			for(int j=0;j<implyConstraint.length;j++){
				System.out.print(implyConstraint[j]+" ");
			}
			System.out.println();
			this.lp.addConstraint( new LinearBiggerThanEqualsConstraint(implyConstraint, 0, "c"+i));
		}
		//Some setup
		lp.setMinProblem(false);
		for(int i=0;i<this.variableNum;i++){
			this.lp.setBinary(i);
		}
		LinearProgramSolver solver  = SolverFactory.newDefault(); 
		double[] sol = solver.solve(lp);
		for(int i=0;i<sol.length;i++){
			System.out.println(sol[i]);
		}
	}
}
