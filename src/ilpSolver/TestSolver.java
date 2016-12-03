package ilpSolver;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LPWizard;
import scpsolver.problems.LinearProgram;

public class TestSolver {
	public static void test1(){
		LPWizard lpw = new LPWizard(); 
		lpw.plus("x1",1.0).plus("x2",1.0); 
		lpw.addConstraint("c1",4.0,">=").plus("x1",3.0).plus("x2",4.0).setAllVariablesBoolean(); 
		lpw.setMinProblem(false); 
		System.out.println(lpw.solve());
	}
	
	public static void test2(){
		LinearProgram lp = new LinearProgram(new double[]{5.0,10.0}); 
		lp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{3.0,1.0}, 8.0, "c1")); 
		lp.addConstraint(new LinearBiggerThanEqualsConstraint(new double[]{0.0,4.0}, 4.0, "c2")); 
		lp.addConstraint(new LinearSmallerThanEqualsConstraint(new double[]{2.0,0.0}, 2.0, "c3")); 
		lp.setMinProblem(true); 
		//lp.setBinary(1);
		LinearProgramSolver solver  = SolverFactory.newDefault(); 
		double[] sol = solver.solve(lp);
	}
	
	public static void main(String[] args) {
		test2();
	}
}
