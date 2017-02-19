package learning.generateUserStudy;

import org.eclipse.core.runtime.Assert;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV6;
import ilpSolver.NaiveBinaryIPSolver;
import learning.v6.NaiveBayesTextClassifierV6;

public class Type3Question extends Question {
	private LearningBinaryIPSolverV6 solver;
	
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
	
	public void setTextClassifierPrediction(NaiveBayesTextClassifierV6 textClassifier) throws Exception{
		this.solver.setTextClassifierResults(textClassifier.predictForATestProgram(this.solver));;
	}
	
	public JSONObject computeOutput(int method){
		JSONObject result = new JSONObject();
		if(method==EncoderUtils.ORIGINAL){
			result.put("code", this.solver.originalProgram2String());
		}
		else if(method==EncoderUtils.NAIVEMETHOD){
			JSONObject code = new JSONObject();
			this.naiveSolver.setTargetLineCount(10);
			code.put("r10", this.naiveSolver.outputSolveResult());
			this.naiveSolver.setTargetLineCount(20);
			code.put("r20", this.naiveSolver.outputSolveResult());
			int originalLines = this.solver.originalProgramLineCount();
			this.naiveSolver.setTargetLineCount(originalLines/2);
			code.put("r50%", this.naiveSolver.outputSolveResult());
			result.put("code", code);
		}
		else if(method==EncoderUtils.MYMETHOD){
			JSONObject code = new JSONObject();
			this.solver.setTargetLineCount(10);
			code.put("r10", this.solver.outputSolveResult());
			this.solver.setTargetLineCount(20);
			code.put("r20", this.solver.outputSolveResult());
			int originalLines = this.solver.originalProgramLineCount();
			this.solver.setTargetLineCount(originalLines/2);
			code.put("r50%", this.solver.outputSolveResult());
			result.put("code", code);
		}
		result.put("type", "T3");
		result.put("method", method);
		return result;
	}
}
