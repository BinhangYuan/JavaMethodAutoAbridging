package learning.generateUserStudy;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.json.JSONArray;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV6;
import ilpSolver.NaiveBinaryIPSolver;
import learning.v6.NaiveBayesTextClassifierV6;

public class Type1Question extends Question{
	private boolean isPractice;
	
	public boolean getIsPractice(){
		return this.isPractice;
	}
	
	private ArrayList<LearningBinaryIPSolverV6> solvers = new ArrayList<LearningBinaryIPSolverV6>();
	private ArrayList<NaiveBinaryIPSolver> naiveSolvers = new ArrayList<NaiveBinaryIPSolver>();
	
	private String doc;
	
	private int randExchange = EncoderUtils.randGenerate.nextInt(5);
	
	public Type1Question(JSONObject input) throws Exception{
		JSONArray codes = input.getJSONArray("codes");
		for(int i=0;i<codes.length();i++){
			JSONObject code = codes.getJSONObject(i);
			this.solvers.add(this.buildSolver(code));
			this.naiveSolvers.add(this.buildNaiveSolver(code));
		}
		this.doc = input.getString("doc");
		this.isPractice = input.getBoolean("practice");
	}
	
	public void setParas(double[] para){
		for(LearningBinaryIPSolverV6 solver:this.solvers){
			solver.setParameters(para);
		}
	}
	
	public void setTextClassifierPrediction(NaiveBayesTextClassifierV6 textClassifier) throws Exception{
		for(LearningBinaryIPSolverV6 solver:this.solvers){
			solver.setTextClassifierResults(textClassifier.predictForATestProgram(solver));;
		}
	}
	
	public void setTargetLineCounts(int line){
		for(LearningBinaryIPSolverV6 solver:this.solvers){
			solver.setTargetLineCount(line);
		}
		for(NaiveBinaryIPSolver solver:this.naiveSolvers){
			solver.setTargetLineCount(line);
		}
	}
	
	public void setTargetLineCounts(double rate){
		Assert.isTrue(rate>0 && rate<1);
		for(int i=0; i < this.solvers.size(); i++){
			int originalLines = this.solvers.get(i).originalProgramLineCount();
			int targetLine = (int)(originalLines * rate);
			Assert.isTrue(targetLine>=1);
			this.solvers.get(i).setTargetLineCount(targetLine);
			this.naiveSolvers.get(i).setTargetLineCount(targetLine);
		}
	}
	
	public JSONObject computeOutput(int method){
		JSONObject result = new JSONObject();
		JSONArray codes = new JSONArray();
		if(method==EncoderUtils.ORIGINAL){
			for(int i=0;i<this.solvers.size();i++){
				if(i==0){
					codes.put(this.solvers.get(this.randExchange).originalProgram2String());
				}
				else if(i==this.randExchange){
					codes.put(this.solvers.get(0).originalProgram2String());
				}
				else{
					codes.put(this.solvers.get(i).originalProgram2String());
				}
			}
		}
		else if(method==EncoderUtils.NAIVEMETHOD){
			for(int i=0;i<this.naiveSolvers.size();i++){
				if(i==0){
					codes.put(this.naiveSolvers.get(this.randExchange).outputSolveResult());
				}
				else if(i==this.randExchange){
					codes.put(this.naiveSolvers.get(0).outputSolveResult());
				}
				else{
					codes.put(this.naiveSolvers.get(i).outputSolveResult());
				}
			}
		}
		else if(method==EncoderUtils.MYMETHOD){
			for(int i=0;i<this.solvers.size();i++){
				if(i==0){
					codes.put(this.solvers.get(this.randExchange).outputSolveResult());
				}
				else if(i==this.randExchange){
					codes.put(this.solvers.get(0).outputSolveResult());
				}
				else{
					codes.put(this.solvers.get(i).outputSolveResult());
				}
			}
		}
		result.put("codes", codes);
		result.put("doc", this.doc);
		result.put("type", "T1");
		result.put("method", method);
		result.put("correctSolution",randExchange);
		result.put("practice", this.isPractice);
		return result;
	}
}
