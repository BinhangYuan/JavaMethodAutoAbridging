package learning.generateUserStudy;

import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV5;
import ilpSolver.NaiveBinaryIPSolver;

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
