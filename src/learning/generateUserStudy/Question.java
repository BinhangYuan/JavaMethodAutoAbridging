package learning.generateUserStudy;

import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV5;
import ilpSolver.NaiveBinaryIPSolver;

public abstract class Question {
	protected LearningBinaryIPSolverV5 buildSolver(JSONObject obj) throws Exception{
		String filePath = obj.getString("file_path");
		String fileName = obj.getString("file_name");
		String methodName = obj.getString("method");
		int pos = obj.getInt("pos");
		return EncoderUtils.encodeSolverV5(filePath, fileName, methodName, pos);
	}
	
	protected NaiveBinaryIPSolver buildNaiveSolver(JSONObject obj) throws Exception{
		String filePath = obj.getString("file_path");
		String fileName = obj.getString("file_name");
		String methodName = obj.getString("method");
		int pos = obj.getInt("pos");
		return EncoderUtils.encodeNaiveSolver(filePath, fileName, methodName, pos);
	}
}
