package ilpSolver;
import scpsolver.problems.LPWizard;

public class TestSolver {
	public static void main(String[] args) {
		LPWizard lpw = new LPWizard(); 
		lpw.plus("x1",1.0).plus("x2",1.0); 
		lpw.addConstraint("c1",4.0,">=").plus("x1",3.0).plus("x2",4.0).setAllVariablesBoolean(); 
		lpw.setMinProblem(false); 
		System.out.println(lpw.solve());
	}
}
