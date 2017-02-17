package learning.generateUserStudy;

import org.json.JSONArray;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV5;
import ilpSolver.NaiveBinaryIPSolver;


public class Type2Question extends Question {
	private LearningBinaryIPSolverV5 solverA;
	private LearningBinaryIPSolverV5 solverB;
	
	private NaiveBinaryIPSolver naiveSolverA;
	private NaiveBinaryIPSolver naiveSolverB;
	
	private String docA;
	private String docB;
	
	public Type2Question(JSONObject input) throws Exception{
		JSONObject codeA = input.getJSONObject("codeA");
		this.solverA = this.buildSolver(codeA);
		this.naiveSolverA = this.buildNaiveSolver(codeA);
		this.docA = codeA.getString("docA");
		JSONObject codeB = input.getJSONObject("codeB");
		this.solverB = this.buildSolver(codeB);
		this.naiveSolverB = this.buildNaiveSolver(codeB);
		this.docB = codeB.getString("docB");
	}
	
	public void setParas(double[] para){
		this.solverA.setParameters(para);
		this.solverB.setParameters(para);
	}
	
	public void setCodeATargetLineCount(int lines){
		this.solverA.setTargetLineCount(lines);
		this.naiveSolverA.setTargetLineCount(lines);
	}
	
	public void setCodeBTargetLineCount(int lines){
		this.solverB.setTargetLineCount(lines);
		this.naiveSolverB.setTargetLineCount(lines);
	}
	
	public JSONObject computeOutput(int method){
		JSONObject result = new JSONObject();
		if(method==EncoderUtils.ORIGINAL){
			result.put("codeA", this.solverA.originalProgram2String());
			result.put("codeB", this.solverB.originalProgram2String());
		}
		else if(method==EncoderUtils.NAIVEMETHOD){
			result.put("codeA", this.naiveSolverA.outputSolveResult());
			result.put("codeB", this.naiveSolverB.outputSolveResult());
		}
		else if(method==EncoderUtils.MYMETHOD){
			result.put("codeA", this.solverA.outputSolveResult());
			result.put("codeB", this.solverB.outputSolveResult());
		}
		result.put("docA", this.docA);
		result.put("docB", this.docB);
		result.put("type", "T2");
		result.put("method", method);
		JSONArray alternatives = new JSONArray();
		alternatives.put("Method A mactches Description A, Mehtod B mactches Description B.");
		alternatives.put("Method A mactches Description B, Mehtod A mactches Description B.");
		alternatives.put("Impossible to decide.");
		result.put("Alternatives", alternatives);
		return result;
	}
}
