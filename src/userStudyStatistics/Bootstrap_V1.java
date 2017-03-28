package userStudyStatistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.core.runtime.Assert;
import org.json.JSONObject;

import statementGraph.ASTParserUtils;

public class Bootstrap_V1 {
	static private boolean debug = false;
	
	private int iterations = 1000;
	private Random randGenerate = new Random();
	private ArrayList<JSONObject> datasetRaw = new ArrayList<JSONObject>();
	private Map<String,ArrayList<JSONObject>> datasetByQuestion = new HashMap<String,ArrayList<JSONObject>>();
	private ArrayList<Map<String,ArrayList<JSONObject>>> databyUser = new ArrayList<Map<String,ArrayList<JSONObject>>>();
	
	
	private void loadDataset(final File directory) throws IOException{
		Assert.isTrue(directory.isDirectory());
		for(final File file: directory.listFiles()){
			Assert.isTrue(!file.isDirectory());
			if(file.getName().endsWith(".json")){
				String str = ASTParserUtils.readFileToString(file.getAbsolutePath());
				if(debug){
					System.out.println(str);
				}
				JSONObject input = new JSONObject(str);
				JSONObject questions = input.getJSONObject("answers");
				//Keep task1 and task2.
				questions.remove("1");
				questions.remove("2");
				questions.remove("9");
				questions.remove("10");
				questions.remove("20");
				questions.remove("21");
				questions.remove("22");
				questions.remove("23");
				questions.remove("24");
				if(debug){
					for(String key:questions.keySet()){
						System.out.print(key+" ");
					}
					System.out.println();
				}
				this.datasetRaw.add(questions);
			}
		}
	}
	
	private void encodeDatasetByUser(){
		for(JSONObject questions:this.datasetRaw){
			HashMap<String,ArrayList<JSONObject>> currentUser = new HashMap<String,ArrayList<JSONObject>>();
			ArrayList<JSONObject> m0Array = new ArrayList<JSONObject>();
			ArrayList<JSONObject> m1Array = new ArrayList<JSONObject>();
			ArrayList<JSONObject> m2Array = new ArrayList<JSONObject>();
			for(int i=1; i<=19; i++){
				if(i==1||i==2||i==9||i==10){
					continue;
				}
				String questionID = ""+i;
				JSONObject currentQuestion = new JSONObject();
				for(String key:questions.getJSONObject(questionID).keySet()){
					currentQuestion.put(key, questions.getJSONObject(questionID).get(key));
				}
				currentQuestion.put("isCorrect", currentQuestion.getInt("correctSolution") == Integer.parseInt(currentQuestion.getString("answer")));
				currentQuestion.remove("correctSolution");
				currentQuestion.remove("answer");
				if(currentQuestion.getInt("method")==0){
					m0Array.add(currentQuestion);
				}
				else if(currentQuestion.getInt("method")==1){
					m1Array.add(currentQuestion);
				}
				else if(currentQuestion.getInt("method")==2){
					m2Array.add(currentQuestion);
				}
			}
			currentUser.put("M0", m0Array);
			currentUser.put("M1", m1Array);
			currentUser.put("M2", m2Array);
			this.databyUser.add(currentUser);
		}
	}
	
	private void encodeDatasetByQuestion(){
		for(int i=1; i<=19; i++){
			if(i==1||i==2||i==9||i==10){
				continue;
			}
			String questionID = ""+i;
			this.datasetByQuestion.put(questionID+"_M0", new ArrayList<JSONObject>());
			this.datasetByQuestion.put(questionID+"_M1", new ArrayList<JSONObject>());
			this.datasetByQuestion.put(questionID+"_M2", new ArrayList<JSONObject>());
		}
		
		for(JSONObject questions:this.datasetRaw){
			for(String questionID: questions.keySet()){
				JSONObject question = new JSONObject();
				for(String key:questions.getJSONObject(questionID).keySet()){
					question.put(key, questions.getJSONObject(questionID).get(key));
				}
				question.put("isCorrect", question.getInt("correctSolution") == Integer.parseInt(question.getString("answer")));
				question.remove("correctSolution");
				question.remove("answer");
				this.datasetByQuestion.get(questionID+"_M"+question.getInt("method")).add(question);
			}
		}
		if(debug){
			for(int i=1; i<=19; i++){
				if(i==1||i==2||i==9||i==10){
					continue;
				}
				String questionID = ""+i;
				System.out.println("==============="+questionID+"===============");
				ArrayList<JSONObject> questionM0 = this.datasetByQuestion.get(questionID+"_M0");				
				System.out.println("M0 sample count:"+":"+questionM0.size());
				ArrayList<JSONObject> questionM1 = this.datasetByQuestion.get(questionID+"_M1");				
				System.out.println("M1 sample count:"+":"+questionM1.size());
				ArrayList<JSONObject> questionM2 = this.datasetByQuestion.get(questionID+"_M2");				
				System.out.println("M2 sample count:"+":"+questionM2.size());
			}
		}
	}

	
	public Bootstrap_V1(final File directory) throws IOException{
		this.loadDataset(directory);
		this.encodeDatasetByUser();
		this.encodeDatasetByQuestion();
	}
	
	/*
	 * 01: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).
	 * [First sample questions, then sample in each type category.]
	 */
	public double computePvalueHypothesis01(){
		ArrayList<String> questionIDSet = new ArrayList<String>();
		for(int i=1; i<=19; i++){
			if(i==1||i==2||i==9||i==10){
				continue;
			}
			questionIDSet.add(""+i);
		}
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//First re-sample 15 questions by sampling 15 times with replacement.
			ArrayList<String> sampleSet = new ArrayList<String>();
			for(int j=0;j<questionIDSet.size();j++){
				int index = this.randGenerate.nextInt(questionIDSet.size());
				sampleSet.add(questionIDSet.get(index));
			}
			
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M0_CORRECT", 0);
			statistics.put("M0_WRONG", 0);
			statistics.put("M2_CORRECT", 0);
			statistics.put("M2_WRONG", 0);
			
			for(int j=0; j<sampleSet.size();j++){
				ArrayList<JSONObject> m0Array = this.datasetByQuestion.get(sampleSet.get(j)+"_M0");
				ArrayList<JSONObject> m2Array = this.datasetByQuestion.get(sampleSet.get(j)+"_M2");
				
				//Then re-sample the question example by sampling question with replacement for method 0.
				for(int k=0; k<m0Array.size();k++){
					int index = this.randGenerate.nextInt(m0Array.size());
					JSONObject current = m0Array.get(index);
					if(current.getBoolean("isCorrect")){
						statistics.put("M0_CORRECT", statistics.get("M0_CORRECT")+1);
					}
					else{
						statistics.put("M0_WRONG", statistics.get("M0_WRONG")+1);
					}
				}
				
				//Then re-sample the question example by sampling question with replacement for method 2.
				for(int k=0; k<m2Array.size();k++){
					int index = this.randGenerate.nextInt(m2Array.size());
					JSONObject current = m2Array.get(index);
					if(current.getBoolean("isCorrect")){
						statistics.put("M2_CORRECT", statistics.get("M2_CORRECT")+1);
					}
					else{
						statistics.put("M2_WRONG", statistics.get("M2_WRONG")+1);
					}
				}
			}
			
			//Check if the null hypothesis hold
			double accuracy0 = (double)(statistics.get("M0_CORRECT"))/(double)(statistics.get("M0_WRONG")+statistics.get("M0_CORRECT"));
			double accuracy2 = (double)(statistics.get("M2_CORRECT"))/(double)(statistics.get("M2_WRONG")+statistics.get("M2_CORRECT"));
			if(accuracy0 - accuracy2 > 0.00001){
				count++;
			}
			
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 02: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).
	 * [First sample questions, then sample in each type category.]
	 */
	public double computePvalueHypothesis02(){
		ArrayList<String> questionIDSet = new ArrayList<String>();
		for(int i=1; i<=19; i++){
			if(i==1||i==2||i==9||i==10){
				continue;
			}
			questionIDSet.add(""+i);
		}
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//First re-sample 15 questions by sampling 15 times with replacement.
			ArrayList<String> sampleSet = new ArrayList<String>();
			for(int j=0;j<questionIDSet.size();j++){
				int index = this.randGenerate.nextInt(questionIDSet.size());
				sampleSet.add(questionIDSet.get(index));
			}
			
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M1_CORRECT", 0);
			statistics.put("M1_WRONG", 0);
			statistics.put("M2_CORRECT", 0);
			statistics.put("M2_WRONG", 0);
			
			for(int j=0; j<sampleSet.size();j++){
				ArrayList<JSONObject> m1Array = this.datasetByQuestion.get(sampleSet.get(j)+"_M1");
				ArrayList<JSONObject> m2Array = this.datasetByQuestion.get(sampleSet.get(j)+"_M2");
				
				//Then re-sample the question example by sampling question with replacement for method 1.
				for(int k=0; k<m1Array.size();k++){
					int index = this.randGenerate.nextInt(m1Array.size());
					JSONObject current = m1Array.get(index);
					if(current.getBoolean("isCorrect")){
						statistics.put("M1_CORRECT", statistics.get("M1_CORRECT")+1);
					}
					else{
						statistics.put("M1_WRONG", statistics.get("M1_WRONG")+1);
					}
				}
				
				//Then re-sample the question example by sampling question with replacement for method 2.
				for(int k=0; k<m2Array.size();k++){
					int index = this.randGenerate.nextInt(m2Array.size());
					JSONObject current = m2Array.get(index);
					if(current.getBoolean("isCorrect")){
						statistics.put("M2_CORRECT", statistics.get("M2_CORRECT")+1);
					}
					else{
						statistics.put("M2_WRONG", statistics.get("M2_WRONG")+1);
					}
				}
			}
			
			//Check if the null hypothesis hold
			double accuracy1 = (double)(statistics.get("M1_CORRECT"))/(double)(statistics.get("M1_WRONG")+statistics.get("M1_CORRECT"));
			double accuracy2 = (double)(statistics.get("M2_CORRECT"))/(double)(statistics.get("M2_WRONG")+statistics.get("M2_CORRECT"));
			if(accuracy1 - accuracy2 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 03: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method0 (original code).
	 * [First sample questions, then sample in each type category.]
	 */
	public double computePvalueHypothesis03(){
		ArrayList<String> questionIDSet = new ArrayList<String>();
		for(int i=1; i<=19; i++){
			if(i==1||i==2||i==9||i==10){
				continue;
			}
			questionIDSet.add(""+i);
		}
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//First re-sample 15 questions by sampling 15 times with replacement.
			ArrayList<String> sampleSet = new ArrayList<String>();
			for(int j=0;j<questionIDSet.size();j++){
				int index = this.randGenerate.nextInt(questionIDSet.size());
				sampleSet.add(questionIDSet.get(index));
			}
			
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M0_TOTALTIME", 0);
			statistics.put("M0_COUNT", 0);
			statistics.put("M2_TOTALTIME", 0);
			statistics.put("M2_COUNT", 0);
			
			for(int j=0; j<sampleSet.size();j++){
				ArrayList<JSONObject> m0Array = this.datasetByQuestion.get(sampleSet.get(j)+"_M0");
				ArrayList<JSONObject> m2Array = this.datasetByQuestion.get(sampleSet.get(j)+"_M2");
				
				//Then re-sample the question example by sampling question with replacement for method 1.
				for(int k=0; k<m0Array.size();k++){
					int index = this.randGenerate.nextInt(m0Array.size());
					JSONObject current = m0Array.get(index);
					statistics.put("M0_TOTALTIME", statistics.get("M0_TOTALTIME")+current.getInt("time"));
					statistics.put("M0_COUNT", statistics.get("M0_COUNT")+1);
				}
				
				//Then re-sample the question example by sampling question with replacement for method 1.
				for(int k=0; k<m2Array.size();k++){
					int index = this.randGenerate.nextInt(m2Array.size());
					JSONObject current = m2Array.get(index);
					statistics.put("M2_TOTALTIME", statistics.get("M2_TOTALTIME")+current.getInt("time"));
					statistics.put("M2_COUNT", statistics.get("M2_COUNT")+1);
				}
			}
			
			//Check if the null hypothesis hold
			double time0 = (double)(statistics.get("M0_TOTALTIME"))/(double)(statistics.get("M0_COUNT"));
			double time2 = (double)(statistics.get("M2_TOTALTIME"))/(double)(statistics.get("M2_COUNT"));
			if(time2 - time0 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 04: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).
	 * [First sample questions, then sample in each type category.]
	 */
	public double computePvalueHypothesis04(){
		ArrayList<String> questionIDSet = new ArrayList<String>();
		for(int i=1; i<=19; i++){
			if(i==1||i==2||i==9||i==10){
				continue;
			}
			questionIDSet.add(""+i);
		}
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//First re-sample 15 questions by sampling 15 times with replacement.
			ArrayList<String> sampleSet = new ArrayList<String>();
			for(int j=0;j<questionIDSet.size();j++){
				int index = this.randGenerate.nextInt(questionIDSet.size());
				sampleSet.add(questionIDSet.get(index));
			}
			
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M1_TOTALTIME", 0);
			statistics.put("M1_COUNT", 0);
			statistics.put("M2_TOTALTIME", 0);
			statistics.put("M2_COUNT", 0);
			
			for(int j=0; j<sampleSet.size();j++){
				ArrayList<JSONObject> m1Array = this.datasetByQuestion.get(sampleSet.get(j)+"_M1");
				ArrayList<JSONObject> m2Array = this.datasetByQuestion.get(sampleSet.get(j)+"_M2");
				
				//Then re-sample the question example by sampling question with replacement for method 1.
				for(int k=0; k<m1Array.size();k++){
					int index = this.randGenerate.nextInt(m1Array.size());
					JSONObject current = m1Array.get(index);
					statistics.put("M1_TOTALTIME", statistics.get("M1_TOTALTIME")+current.getInt("time"));
					statistics.put("M1_COUNT", statistics.get("M1_COUNT")+1);
				}
				
				//Then re-sample the question example by sampling question with replacement for method 1.
				for(int k=0; k<m2Array.size();k++){
					int index = this.randGenerate.nextInt(m2Array.size());
					JSONObject current = m2Array.get(index);
					statistics.put("M2_TOTALTIME", statistics.get("M2_TOTALTIME")+current.getInt("time"));
					statistics.put("M2_COUNT", statistics.get("M2_COUNT")+1);
				}
			}
			
			//Check if the null hypothesis hold
			double time1 = (double)(statistics.get("M1_TOTALTIME"))/(double)(statistics.get("M1_COUNT"));
			double time2 = (double)(statistics.get("M2_TOTALTIME"))/(double)(statistics.get("M2_COUNT"));
			if(time2 - time1 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 05: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).
	 * [First sample users, then sample in each type category.]
	 */
	public double computePvalueHypothesis05(){
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//First re-sample 20 users by sampling 20 times with replacement.
			ArrayList<Map<String,ArrayList<JSONObject>>> sampleUser = new ArrayList<Map<String,ArrayList<JSONObject>>>();
			for(int j=0;j<this.databyUser.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(this.databyUser.size());
				sampleUser.add(this.databyUser.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M0_CORRECT", 0);
			statistics.put("M0_WRONG", 0);
			statistics.put("M2_CORRECT", 0);
			statistics.put("M2_WRONG", 0);
			for(int j=0; j<sampleUser.size();j++){
				Map<String,ArrayList<JSONObject>> oneUser = sampleUser.get(j);
				ArrayList<JSONObject> m0Array = oneUser.get("M0");
				ArrayList<JSONObject> m2Array = oneUser.get("M2");
				
				//Then re-sample the question example by sampling question with replacement for method 0.
				for(int k=0;k<m0Array.size();k++){
					int index = this.randGenerate.nextInt(m0Array.size());
					JSONObject current = m0Array.get(index);
					Assert.isTrue(current.getInt("method")==0);
					if(current.getBoolean("isCorrect")){
						statistics.put("M0_CORRECT", statistics.get("M0_CORRECT")+1);
					}
					else{
						statistics.put("M0_WRONG", statistics.get("M0_WRONG")+1);
					}
				}
				
				//Then re-sample the question example by sampling question with replacement for method 2.
				for(int k=0;k<m2Array.size();k++){
					int index = this.randGenerate.nextInt(m2Array.size());
					JSONObject current = m2Array.get(index);
					Assert.isTrue(current.getInt("method")==2);
					if(current.getBoolean("isCorrect")){
						statistics.put("M2_CORRECT", statistics.get("M2_CORRECT")+1);
					}
					else{
						statistics.put("M2_WRONG", statistics.get("M2_WRONG")+1);
					}
				}
			}
			double accuracy0 = (double)(statistics.get("M0_CORRECT"))/(double)(statistics.get("M0_WRONG")+statistics.get("M0_CORRECT"));
			double accuracy2 = (double)(statistics.get("M2_CORRECT"))/(double)(statistics.get("M2_WRONG")+statistics.get("M2_CORRECT"));
			if(accuracy0 - accuracy2 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 06: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).
	 * [First sample users, then sample in each type category.]
	 */
	public double computePvalueHypothesis06(){
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//First re-sample 20 users by sampling 20 times with replacement.
			ArrayList<Map<String,ArrayList<JSONObject>>> sampleUser = new ArrayList<Map<String,ArrayList<JSONObject>>>();
			for(int j=0;j<this.databyUser.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(this.databyUser.size());
				sampleUser.add(this.databyUser.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M1_CORRECT", 0);
			statistics.put("M1_WRONG", 0);
			statistics.put("M2_CORRECT", 0);
			statistics.put("M2_WRONG", 0);
			for(int j=0; j<sampleUser.size();j++){
				Map<String,ArrayList<JSONObject>> oneUser = sampleUser.get(j);
				ArrayList<JSONObject> m1Array = oneUser.get("M1");
				ArrayList<JSONObject> m2Array = oneUser.get("M2");
				
				//Then re-sample the question example by sampling question with replacement for method 1.
				for(int k=0;k<m1Array.size();k++){
					int index = this.randGenerate.nextInt(m1Array.size());
					JSONObject current = m1Array.get(index);
					Assert.isTrue(current.getInt("method")==1);
					if(current.getBoolean("isCorrect")){
						statistics.put("M1_CORRECT", statistics.get("M1_CORRECT")+1);
					}
					else{
						statistics.put("M1_WRONG", statistics.get("M1_WRONG")+1);
					}
				}
				
				//Then re-sample the question example by sampling question with replacement for method 2.
				for(int k=0;k<m2Array.size();k++){
					int index = this.randGenerate.nextInt(m2Array.size());
					JSONObject current = m2Array.get(index);
					Assert.isTrue(current.getInt("method")==2);
					if(current.getBoolean("isCorrect")){
						statistics.put("M2_CORRECT", statistics.get("M2_CORRECT")+1);
					}
					else{
						statistics.put("M2_WRONG", statistics.get("M2_WRONG")+1);
					}
				}
			}
			double accuracy1 = (double)(statistics.get("M1_CORRECT"))/(double)(statistics.get("M1_WRONG")+statistics.get("M1_CORRECT"));
			double accuracy2 = (double)(statistics.get("M2_CORRECT"))/(double)(statistics.get("M2_WRONG")+statistics.get("M2_CORRECT"));
			if(accuracy1 - accuracy2 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 07: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method0 (original code).
	 * [First sample users, then sample in each type category.]
	 */
	public double computePvalueHypothesis07(){
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//First re-sample 20 users by sampling 20 times with replacement.
			ArrayList<Map<String,ArrayList<JSONObject>>> sampleUser = new ArrayList<Map<String,ArrayList<JSONObject>>>();
			for(int j=0;j<this.databyUser.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(this.databyUser.size());
				sampleUser.add(this.databyUser.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M0_TOTALTIME", 0);
			statistics.put("M0_COUNT", 0);
			statistics.put("M2_TOTALTIME", 0);
			statistics.put("M2_COUNT", 0);
			for(int j=0; j<sampleUser.size();j++){
				Map<String,ArrayList<JSONObject>> oneUser = sampleUser.get(j);
				ArrayList<JSONObject> m0Array = oneUser.get("M0");
				ArrayList<JSONObject> m2Array = oneUser.get("M2");
				
				//Then re-sample the question example by sampling question with replacement for method 0.
				for(int k=0;k<m0Array.size();k++){
					int index = this.randGenerate.nextInt(m0Array.size());
					JSONObject current = m0Array.get(index);
					Assert.isTrue(current.getInt("method")==0);
					statistics.put("M0_TOTALTIME", statistics.get("M0_TOTALTIME")+current.getInt("time"));
					statistics.put("M0_COUNT", statistics.get("M0_COUNT")+1);
				}
				
				//Then re-sample the question example by sampling question with replacement for method 2.
				for(int k=0;k<m2Array.size();k++){
					int index = this.randGenerate.nextInt(m2Array.size());
					JSONObject current = m2Array.get(index);
					Assert.isTrue(current.getInt("method")==2);
					statistics.put("M2_TOTALTIME", statistics.get("M2_TOTALTIME")+current.getInt("time"));
					statistics.put("M2_COUNT", statistics.get("M2_COUNT")+1);
				}
			}
			if(debug){
				System.out.println("M0_TOTALTIME: "+statistics.get("M0_TOTALTIME"));
				System.out.println("M2_TOTALTIME: "+statistics.get("M2_TOTALTIME"));
				System.out.println("M0_COUNT: "+statistics.get("M0_COUNT"));
				System.out.println("M2_COUNT: "+statistics.get("M2_COUNT"));
			}
			double time0 = (double)(statistics.get("M0_TOTALTIME"))/(double)(statistics.get("M0_COUNT"));
			double time2 = (double)(statistics.get("M2_TOTALTIME"))/(double)(statistics.get("M2_COUNT"));
			if(time0 - time2 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 08: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).
	 * [First sample users, then sample in each type category.]
	 */
	public double computePvalueHypothesis08(){
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//First re-sample 20 users by sampling 20 times with replacement.
			ArrayList<Map<String,ArrayList<JSONObject>>> sampleUser = new ArrayList<Map<String,ArrayList<JSONObject>>>();
			for(int j=0;j<this.databyUser.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(this.databyUser.size());
				sampleUser.add(this.databyUser.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M1_TOTALTIME", 0);
			statistics.put("M1_COUNT", 0);
			statistics.put("M2_TOTALTIME", 0);
			statistics.put("M2_COUNT", 0);
			for(int j=0; j<sampleUser.size();j++){
				Map<String,ArrayList<JSONObject>> oneUser = sampleUser.get(j);
				ArrayList<JSONObject> m1Array = oneUser.get("M1");
				ArrayList<JSONObject> m2Array = oneUser.get("M2");
				
				//Then re-sample the question example by sampling question with replacement for method 1.
				for(int k=0;k<m1Array.size();k++){
					int index = this.randGenerate.nextInt(m1Array.size());
					JSONObject current = m1Array.get(index);
					Assert.isTrue(current.getInt("method")==1);
					statistics.put("M1_TOTALTIME", statistics.get("M1_TOTALTIME")+current.getInt("time"));
					statistics.put("M1_COUNT", statistics.get("M1_COUNT")+1);
				}
				
				//Then re-sample the question example by sampling question with replacement for method 2.
				for(int k=0;k<m2Array.size();k++){
					int index = this.randGenerate.nextInt(m2Array.size());
					JSONObject current = m2Array.get(index);
					Assert.isTrue(current.getInt("method")==2);
					statistics.put("M2_TOTALTIME", statistics.get("M2_TOTALTIME")+current.getInt("time"));
					statistics.put("M2_COUNT", statistics.get("M2_COUNT")+1);
				}
			}
			if(debug){
				System.out.println("M1_TOTALTIME: "+statistics.get("M1_TOTALTIME"));
				System.out.println("M2_TOTALTIME: "+statistics.get("M2_TOTALTIME"));
				System.out.println("M1_COUNT: "+statistics.get("M1_COUNT"));
				System.out.println("M2_COUNT: "+statistics.get("M2_COUNT"));
			}
			double time1 = (double)(statistics.get("M1_TOTALTIME"))/(double)(statistics.get("M1_COUNT"));
			double time2 = (double)(statistics.get("M2_TOTALTIME"))/(double)(statistics.get("M2_COUNT"));
			if(time1 - time2 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	public static void main(String[] args) throws Exception{
		File directory = new File("userStudyStat/survey");
		Bootstrap_V1 stat = new Bootstrap_V1(directory);
		System.out.println("01: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).");
		System.out.println("[First sample questions, then sample in each type category.]");
		System.out.println("p-value: "+stat.computePvalueHypothesis01());
		System.out.println("02: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).");
		System.out.println("[First sample questions, then sample in each type category.]");
		System.out.println("p-value: "+stat.computePvalueHypothesis02());
		System.out.println("03: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method0 (original code).");
		System.out.println("[First sample questions, then sample in each type category.]");
		System.out.println("p-value: "+stat.computePvalueHypothesis03());
		System.out.println("04: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).");
		System.out.println("[First sample questions, then sample in each type category.]");
		System.out.println("p-value: "+stat.computePvalueHypothesis04());
		
		System.out.println("05: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).");
		System.out.println("[First sample users, then sample in each type category.]");
		System.out.println("p-value: "+stat.computePvalueHypothesis05());
		System.out.println("06: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).");
		System.out.println("[First sample users, then sample in each type category.]");
		System.out.println("p-value: "+stat.computePvalueHypothesis06());
		System.out.println("07: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method0 (original code).");
		System.out.println("[First sample users, then sample in each type category.]");
		System.out.println("p-value: "+stat.computePvalueHypothesis07());
		System.out.println("08: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).");
		System.out.println("[First sample users, then sample in each type category.]");
		System.out.println("p-value: "+stat.computePvalueHypothesis08());	
	}	
}