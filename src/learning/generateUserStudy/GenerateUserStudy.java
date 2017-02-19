package learning.generateUserStudy;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import learning.v6.NaiveBayesTextClassifierV6;
import statementGraph.ASTParserUtils;

public class GenerateUserStudy {
	private double [] paras;
	private JSONArray questions;
	private NaiveBayesTextClassifierV6 textClassifier;
	
	
	private ArrayList<Type1Question> type1questions = new ArrayList<Type1Question>();
	private ArrayList<Type2Question> type2questions = new ArrayList<Type2Question>();
	private ArrayList<Type3Question> type3questions = new ArrayList<Type3Question>();
	
	
	public GenerateUserStudy(final File input) throws Exception{
		final File textClassifierLoader = new File("webDemo/result/NaiveBayesTextWordDistribution.json");
		this.textClassifier = new NaiveBayesTextClassifierV6(textClassifierLoader);
		
		String iputString = ASTParserUtils.readFileToString(input.getAbsolutePath());
		JSONObject obj = new JSONObject(iputString);
		JSONArray paraJSONArray = obj.getJSONArray("para");
		this.paras = new double[paraJSONArray.length()];
		for(int i=0;i<paras.length;i++){
			this.paras[i] = paraJSONArray.getDouble(i);
		}
		this.questions = obj.getJSONArray("questions");
		for(int i=0;i<questions.length();i++){
			JSONObject currentQuestion = this.questions.getJSONObject(i);
			if(currentQuestion.getString("type").equals("T1")){
				Type1Question t1question = new Type1Question(currentQuestion);
				t1question.setParas(this.paras);
				t1question.setTextClassifierPrediction(this.textClassifier);
				this.type1questions.add(t1question);
			}
			else if(currentQuestion.getString("type").equals("T2")){
				Type2Question t2question = new Type2Question(currentQuestion);
				t2question.setParas(this.paras);
				t2question.setTextClassifierPrediction(this.textClassifier);
				this.type2questions.add(t2question);
			}
			else if(currentQuestion.getString("type").equals("T3")){
				Type3Question t3question = new Type3Question(currentQuestion);
				t3question.setParas(this.paras);
				t3question.setTextClassifierPrediction(this.textClassifier);
				this.type3questions.add(t3question);
			}
			else{
				throw new Exception("Unexpected type "+currentQuestion.getString("type"));
			}
		}
		this.setTargetLineCounts(0.5);
	}
	
	private void setTargetLineCounts(double rate){
		for(Type1Question t1q:this.type1questions){
			t1q.setTargetLineCounts(rate);
		}
		for(Type2Question t2q:this.type2questions){
			t2q.setTargetLineCounts(rate);
		}
		for(Type3Question t3q:this.type3questions){
			t3q.setTargetLineCounts(rate);
		}
	}
	
	
	/*
	 * This is only used for debug and check whether the output is decent.
	 */
	public void generateWholeCaseForVerification() throws Exception{
		JSONObject questions = new JSONObject();
		int i = 1;
		for(Type1Question q1:this.type1questions){
			questions.put(""+i, q1.computeOutput(EncoderUtils.ORIGINAL));
			i++;
			questions.put(""+i, q1.computeOutput(EncoderUtils.NAIVEMETHOD));
			i++;
			questions.put(""+i, q1.computeOutput(EncoderUtils.MYMETHOD));
			i++;
		}
		for(Type2Question q2:this.type2questions){
			questions.put(""+i, q2.computeOutput(EncoderUtils.ORIGINAL));
			i++;
			questions.put(""+i, q2.computeOutput(EncoderUtils.NAIVEMETHOD));
			i++;
			questions.put(""+i, q2.computeOutput(EncoderUtils.MYMETHOD));
			i++;
		}
		for(Type3Question q3:this.type3questions){
			questions.put(""+i, q3.computeOutput(EncoderUtils.ORIGINAL));
			i++;
			questions.put(""+i, q3.computeOutput(EncoderUtils.NAIVEMETHOD));
			i++;
			questions.put(""+i, q3.computeOutput(EncoderUtils.MYMETHOD));
			i++;
		}
		JSONObject result = new JSONObject();
		result.put("questions", questions);
		FileWriter file = new FileWriter("userStudy/BackEnd/questionAll.json");
		result.write(file);
		file.close();
	}
	
	
	public void generateAllCasesForStudy(){
		
	}
	
	
	public static void main(String[] args) throws Exception{
		final File input = new File("src/learning/generateUserStudy/studyCase.json");
		GenerateUserStudy studyCase = new GenerateUserStudy(input);
		studyCase.generateWholeCaseForVerification();
	}
}
