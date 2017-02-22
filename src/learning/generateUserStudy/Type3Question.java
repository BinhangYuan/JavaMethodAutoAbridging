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
	
	public JSONObject computeOutput(){
		JSONObject result = new JSONObject();
		
		result.put("code_original", this.solver.originalProgram2String());
		
		int originalLines = this.solver.originalProgramLineCount();
		JSONObject naiveCode = new JSONObject();
		this.naiveSolver.setTargetLineCount(15);
		naiveCode.put("r15", this.naiveSolver.outputSolveResult());
		this.naiveSolver.setTargetLineCount(30);
		naiveCode.put("r30", this.naiveSolver.outputSolveResult());
		this.naiveSolver.setTargetLineCount(originalLines/2);
		naiveCode.put("r50%", this.naiveSolver.outputSolveResult());
		result.put("code_native", naiveCode);
		
		JSONObject myMethodCode = new JSONObject();
		this.solver.setTargetLineCount(15);
		myMethodCode.put("r15", this.solver.outputSolveResult());
		this.solver.setTargetLineCount(30);
		myMethodCode.put("r30", this.solver.outputSolveResult());
		this.solver.setTargetLineCount(originalLines/2);
		myMethodCode.put("r50%", this.solver.outputSolveResult());
		result.put("code_mymethod", myMethodCode);
		result.put("type", "T3");
		result.put("method", 1);
		return result;
	}
}
