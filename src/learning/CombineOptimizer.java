package learning;

public class CombineOptimizer {
	
	/*
	 * Combine these two optimizer may be a bad idea, but keep the code here for now.
	 */
	public static void main(String[] args) throws Exception {
		ParamILS paramILSmodel = new ParamILS();
		paramILSmodel.initTraining("src/learning/labeling/labels.json");
		paramILSmodel.training();
		
		FiniteDifferenceGradientDescent fdgdmodel = new FiniteDifferenceGradientDescent();
		fdgdmodel.initTraining("src/learning/labeling/labels.json");
		fdgdmodel.setParameters(paramILSmodel.getParameters());
		fdgdmodel.training();
	}
}
