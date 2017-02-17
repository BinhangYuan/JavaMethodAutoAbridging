package learning.generateUserStudy;

import org.eclipse.core.runtime.Assert;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV5;
import ilpSolver.NaiveBinaryIPSolver;
import learning.v5.NaiveBayesTextClassifierV5;

public class Type3Question extends Question {
	private LearningBinaryIPSolverV5 solver;
	
	private NaiveBinaryIPSolver naiveSolver;
	
	
	public Type3Question(JSONObject input) throws Exception{
		JSONObject code = input.getJSONObject("code");
		this.solver = this.buildSolver(code);
		this.naiveSolver = this.buildNaiveSolver(code);
	}
	
	public void setParas(double[] para){
		this.solver.setParameters(para);
	}
	
	public void setCodeTargetLineCount(int lines){
		this.solver.setTargetLineCount(lines);
		this.naiveSolver.setTargetLineCount(lines);
	}
	
	public void setTargetLineCounts(double rate){
		Assert.isTrue(rate>0 && rate<1);
		int originalLines = this.solver.originalProgramLineCount();
		int targetLine = (int)(originalLines * rate);
		Assert.isTrue(targetLine>=1);
		this.solver.setTargetLineCount(targetLine);
		this.naiveSolver.setTargetLineCount(targetLine);
	}
	
	public void setTextClassifierPrediction(NaiveBayesTextClassifierV5 textClassifier) throws Exception{
		this.solver.setTextClassifierResults(textClassifier.predictForATestProgram(this.solver));;
	}
	
	public JSONObject computeOutput(int method){
		JSONObject result = new JSONObject();
		if(method==EncoderUtils.ORIGINAL){
			result.put("code", this.solver.originalProgram2String());
		}
		else if(method==EncoderUtils.NAIVEMETHOD){
			result.put("code", this.naiveSolver.outputSolveResult());
		}
		else if(method==EncoderUtils.MYMETHOD){
			result.put("code", this.solver.outputSolveResult());
		}
		result.put("type", "T3");
		result.put("method", method);
		return result;
	}
}
