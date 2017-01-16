package learning;

import learning.v1.FiniteDifferenceGradientDescentV1;
import learning.v1.ParamILSV1;

public class CombineOptimizer {
	
	/*
	 * Combine these two optimizer may be a bad idea, but keep the code here for now.
	 */
	public static void main(String[] args) throws Exception {
		ParamILSV1 paramILSmodel = new ParamILSV1();
		paramILSmodel.initTraining("src/learning/labeling/labels.json");
		paramILSmodel.training();
		
		FiniteDifferenceGradientDescentV1 fdgdmodel = new FiniteDifferenceGradientDescentV1();
		fdgdmodel.initTraining("src/learning/labeling/labels.json");
		fdgdmodel.setParameters(paramILSmodel.getParameters());
		fdgdmodel.training();
	}
}
