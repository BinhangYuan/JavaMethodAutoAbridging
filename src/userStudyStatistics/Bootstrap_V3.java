package userStudyStatistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;

import org.eclipse.core.runtime.Assert;
import org.json.JSONObject;

import statementGraph.ASTParserUtils;

public class Bootstrap_V3 {
	static private boolean debug = false;
	
	private int iterations = 10000;
	private Random randGenerate = new Random();
	private ArrayList<JSONObject> datasetRaw = new ArrayList<JSONObject>();
	private ArrayList<Map<String,ArrayList<JSONObject>>> databyUser = new ArrayList<Map<String,ArrayList<JSONObject>>>();
	
	private HashSet<String> task1Set = new HashSet<String>(SimpleStatistics_V1.task1);
	private HashSet<String> task2Set = new HashSet<String>(SimpleStatistics_V1.task2);
	
	
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
			ArrayList<JSONObject> task1_m0_Array = new ArrayList<JSONObject>();
			ArrayList<JSONObject> task1_m1_Array = new ArrayList<JSONObject>();
			ArrayList<JSONObject> task1_m2_Array = new ArrayList<JSONObject>();
			ArrayList<JSONObject> task2_m0_Array = new ArrayList<JSONObject>();
			ArrayList<JSONObject> task2_m1_Array = new ArrayList<JSONObject>();
			ArrayList<JSONObject> task2_m2_Array = new ArrayList<JSONObject>();
			for(int i=1; i<=19; i++){
				String questionID = ""+i;
				if(!this.task1Set.contains(questionID) && !this.task2Set.contains(questionID)){
					continue;
				}
				JSONObject currentQuestion = new JSONObject();
				for(String key:questions.getJSONObject(questionID).keySet()){
					currentQuestion.put(key, questions.getJSONObject(questionID).get(key));
				}
				currentQuestion.put("isCorrect", currentQuestion.getInt("correctSolution") == Integer.parseInt(currentQuestion.getString("answer")));
				currentQuestion.remove("correctSolution");
				currentQuestion.remove("answer");
				if(this.task1Set.contains(questionID)){
					if(currentQuestion.getInt("method")==0){
						task1_m0_Array.add(currentQuestion);
					}
					else if(currentQuestion.getInt("method")==1){
						task1_m1_Array.add(currentQuestion);
					}
					else if(currentQuestion.getInt("method")==2){
						task1_m2_Array.add(currentQuestion);
					}
				}
				else if(this.task2Set.contains(questionID)){
					if(currentQuestion.getInt("method")==0){
						task2_m0_Array.add(currentQuestion);
					}
					else if(currentQuestion.getInt("method")==1){
						task2_m1_Array.add(currentQuestion);
					}
					else if(currentQuestion.getInt("method")==2){
						task2_m2_Array.add(currentQuestion);
					}
				}
			}
			currentUser.put("task1_m0", task1_m0_Array);
			currentUser.put("task1_m1", task1_m1_Array);
			currentUser.put("task1_m2", task1_m2_Array);
			currentUser.put("task2_m0", task2_m0_Array);
			currentUser.put("task2_m1", task2_m1_Array);
			currentUser.put("task2_m2", task2_m2_Array);
			if(debug){
				System.out.println(currentUser.get("task1").size());
				System.out.println(currentUser.get("task2").size());
			}
			this.databyUser.add(currentUser);
		}
	}
	
	
	public Bootstrap_V3(final File directory) throws IOException{
		if(debug){
			System.out.println(this.task1Set.size());
			System.out.println(this.task2Set.size());
		}
		this.loadDataset(directory);
		this.encodeDatasetByUser();
	}
	
	
	/*
	 * 01: Hypothesis_0: For task1, accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).
	 */
	public double computePvalueHypothesis01(){
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
				ArrayList<JSONObject> task1_m0 = sampleUser.get(j).get("task1_m0");
				ArrayList<JSONObject> task1_m2 = sampleUser.get(j).get("task1_m2");
				//Then re-sample the question example by sampling question with replacement
				for(int k=0;k<task1_m0.size();k++){
					int index = this.randGenerate.nextInt(task1_m0.size());
					JSONObject current = task1_m0.get(index);
					if(current.getBoolean("isCorrect")){
						statistics.put("M0_CORRECT", statistics.get("M0_CORRECT")+1);
					}
					else{
						statistics.put("M0_WRONG", statistics.get("M0_WRONG")+1);
					}
				}
				for(int k=0;k<task1_m2.size();k++){
					int index = this.randGenerate.nextInt(task1_m2.size());
					JSONObject current = task1_m2.get(index);
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
	 * 02: Hypothesis_0: For task2, accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).
	 */
	public double computePvalueHypothesis02(){
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
				ArrayList<JSONObject> task2_m0 = sampleUser.get(j).get("task2_m0");
				ArrayList<JSONObject> task2_m2 = sampleUser.get(j).get("task2_m2");
				//Then re-sample the question example by sampling question with replacement
				for(int k=0;k<task2_m0.size();k++){
					int index = this.randGenerate.nextInt(task2_m0.size());
					JSONObject current = task2_m0.get(index);
					if(current.getBoolean("isCorrect")){
						statistics.put("M0_CORRECT", statistics.get("M0_CORRECT")+1);
					}
					else{
						statistics.put("M0_WRONG", statistics.get("M0_WRONG")+1);
					}
				}
				for(int k=0;k<task2_m2.size();k++){
					int index = this.randGenerate.nextInt(task2_m2.size());
					JSONObject current = task2_m2.get(index);
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
	 * 03: Hypothesis_0: For task1, accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).
	 */
	public double computePvalueHypothesis03(){
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
				ArrayList<JSONObject> task1_m1 = sampleUser.get(j).get("task1_m1");
				ArrayList<JSONObject> task1_m2 = sampleUser.get(j).get("task1_m2");
				//Then re-sample the question example by sampling question with replacement
				for(int k=0;k<task1_m1.size();k++){
					int index = this.randGenerate.nextInt(task1_m1.size());
					JSONObject current = task1_m1.get(index);
					if(current.getBoolean("isCorrect")){
						statistics.put("M1_CORRECT", statistics.get("M1_CORRECT")+1);
					}
					else{
						statistics.put("M1_WRONG", statistics.get("M1_WRONG")+1);
					}
				}
				for(int k=0;k<task1_m2.size();k++){
					int index = this.randGenerate.nextInt(task1_m2.size());
					JSONObject current = task1_m2.get(index);
					if(current.getBoolean("isCorrect")){
						statistics.put("M2_CORRECT", statistics.get("M2_CORRECT")+1);
					}
					else{
						statistics.put("M2_WRONG", statistics.get("M2_WRONG")+1);
					}
				}
			}
			double accuracy0 = (double)(statistics.get("M1_CORRECT"))/(double)(statistics.get("M1_WRONG")+statistics.get("M1_CORRECT"));
			double accuracy2 = (double)(statistics.get("M2_CORRECT"))/(double)(statistics.get("M2_WRONG")+statistics.get("M2_CORRECT"));
			if(accuracy0 - accuracy2 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	/*
	 * 04: Hypothesis_0: For task2, accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).
	 */
	public double computePvalueHypothesis04(){
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
				ArrayList<JSONObject> task2_m1 = sampleUser.get(j).get("task2_m1");
				ArrayList<JSONObject> task2_m2 = sampleUser.get(j).get("task2_m2");
				//Then re-sample the question example by sampling question with replacement
				for(int k=0;k<task2_m1.size();k++){
					int index = this.randGenerate.nextInt(task2_m1.size());
					JSONObject current = task2_m1.get(index);
					if(current.getBoolean("isCorrect")){
						statistics.put("M1_CORRECT", statistics.get("M1_CORRECT")+1);
					}
					else{
						statistics.put("M1_WRONG", statistics.get("M1_WRONG")+1);
					}
				}
				for(int k=0;k<task2_m2.size();k++){
					int index = this.randGenerate.nextInt(task2_m2.size());
					JSONObject current = task2_m2.get(index);
					if(current.getBoolean("isCorrect")){
						statistics.put("M2_CORRECT", statistics.get("M2_CORRECT")+1);
					}
					else{
						statistics.put("M2_WRONG", statistics.get("M2_WRONG")+1);
					}
				}
			}
			double accuracy0 = (double)(statistics.get("M1_CORRECT"))/(double)(statistics.get("M1_WRONG")+statistics.get("M1_CORRECT"));
			double accuracy2 = (double)(statistics.get("M2_CORRECT"))/(double)(statistics.get("M2_WRONG")+statistics.get("M2_CORRECT"));
			if(accuracy0 - accuracy2 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 05: Hypothesis_0: For task 1, react time of method2 (our approach) is larger than or equal to that of method0 (original code).
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
			statistics.put("M0_TOTALTIME", 0);
			statistics.put("M0_COUNT", 0);
			statistics.put("M2_TOTALTIME", 0);
			statistics.put("M2_COUNT", 0);
			for(int j=0; j<sampleUser.size();j++){
				ArrayList<JSONObject> task1_m0 = sampleUser.get(j).get("task1_m0");
				ArrayList<JSONObject> task1_m2 = sampleUser.get(j).get("task1_m2");
				//Then re-sample the question example by sampling question with replacement
				for(int k=0;k<task1_m0.size();k++){
					int index = this.randGenerate.nextInt(task1_m0.size());
					JSONObject current = task1_m0.get(index);
					statistics.put("M0_TOTALTIME", statistics.get("M0_TOTALTIME")+current.getInt("time"));
					statistics.put("M0_COUNT", statistics.get("M0_COUNT")+1);
				}
				for(int k=0;k<task1_m2.size();k++){
					int index = this.randGenerate.nextInt(task1_m2.size());
					JSONObject current = task1_m2.get(index);
					statistics.put("M2_TOTALTIME", statistics.get("M2_TOTALTIME")+current.getInt("time"));
					statistics.put("M2_COUNT", statistics.get("M2_COUNT")+1);
				}
			}
			double time0 = (double)(statistics.get("M0_TOTALTIME"))/(double)(statistics.get("M0_COUNT"));
			double time2 = (double)(statistics.get("M2_TOTALTIME"))/(double)(statistics.get("M2_COUNT"));
			if(time2 - time0 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	/*
	 * 06: Hypothesis_0: For task 2, react time of method2 (our approach) is larger than or equal to that of method0 (original code).
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
			statistics.put("M0_TOTALTIME", 0);
			statistics.put("M0_COUNT", 0);
			statistics.put("M2_TOTALTIME", 0);
			statistics.put("M2_COUNT", 0);
			for(int j=0; j<sampleUser.size();j++){
				ArrayList<JSONObject> task2_m0 = sampleUser.get(j).get("task2_m0");
				ArrayList<JSONObject> task2_m2 = sampleUser.get(j).get("task2_m2");
				//Then re-sample the question example by sampling question with replacement
				for(int k=0;k<task2_m0.size();k++){
					int index = this.randGenerate.nextInt(task2_m0.size());
					JSONObject current = task2_m0.get(index);
					statistics.put("M0_TOTALTIME", statistics.get("M0_TOTALTIME")+current.getInt("time"));
					statistics.put("M0_COUNT", statistics.get("M0_COUNT")+1);
				}
				for(int k=0;k<task2_m2.size();k++){
					int index = this.randGenerate.nextInt(task2_m2.size());
					JSONObject current = task2_m2.get(index);
					statistics.put("M2_TOTALTIME", statistics.get("M2_TOTALTIME")+current.getInt("time"));
					statistics.put("M2_COUNT", statistics.get("M2_COUNT")+1);
				}
			}
			double time0 = (double)(statistics.get("M0_TOTALTIME"))/(double)(statistics.get("M0_COUNT"));
			double time2 = (double)(statistics.get("M2_TOTALTIME"))/(double)(statistics.get("M2_COUNT"));
			if(time2 - time0 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 07: Hypothesis_0: For task 1, react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).
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
			statistics.put("M1_TOTALTIME", 0);
			statistics.put("M1_COUNT", 0);
			statistics.put("M2_TOTALTIME", 0);
			statistics.put("M2_COUNT", 0);
			for(int j=0; j<sampleUser.size();j++){
				ArrayList<JSONObject> task1_m1 = sampleUser.get(j).get("task1_m1");
				ArrayList<JSONObject> task1_m2 = sampleUser.get(j).get("task1_m2");
				//Then re-sample the question example by sampling question with replacement
				for(int k=0;k<task1_m1.size();k++){
					int index = this.randGenerate.nextInt(task1_m1.size());
					JSONObject current = task1_m1.get(index);
					statistics.put("M1_TOTALTIME", statistics.get("M1_TOTALTIME")+current.getInt("time"));
					statistics.put("M1_COUNT", statistics.get("M1_COUNT")+1);
				}
				for(int k=0;k<task1_m2.size();k++){
					int index = this.randGenerate.nextInt(task1_m2.size());
					JSONObject current = task1_m2.get(index);
					statistics.put("M2_TOTALTIME", statistics.get("M2_TOTALTIME")+current.getInt("time"));
					statistics.put("M2_COUNT", statistics.get("M2_COUNT")+1);
				}
			}
			double time1 = (double)(statistics.get("M1_TOTALTIME"))/(double)(statistics.get("M1_COUNT"));
			double time2 = (double)(statistics.get("M2_TOTALTIME"))/(double)(statistics.get("M2_COUNT"));
			if(time2 - time1 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	/*
	 * 08: Hypothesis_0: For task 2, react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).
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
				ArrayList<JSONObject> task2_m1 = sampleUser.get(j).get("task2_m1");
				ArrayList<JSONObject> task2_m2 = sampleUser.get(j).get("task2_m2");
				//Then re-sample the question example by sampling question with replacement
				for(int k=0;k<task2_m1.size();k++){
					int index = this.randGenerate.nextInt(task2_m1.size());
					JSONObject current = task2_m1.get(index);
					statistics.put("M1_TOTALTIME", statistics.get("M1_TOTALTIME")+current.getInt("time"));
					statistics.put("M1_COUNT", statistics.get("M1_COUNT")+1);
				}
				for(int k=0;k<task2_m2.size();k++){
					int index = this.randGenerate.nextInt(task2_m2.size());
					JSONObject current = task2_m2.get(index);
					statistics.put("M2_TOTALTIME", statistics.get("M2_TOTALTIME")+current.getInt("time"));
					statistics.put("M2_COUNT", statistics.get("M2_COUNT")+1);
				}
			}
			double time1 = (double)(statistics.get("M1_TOTALTIME"))/(double)(statistics.get("M1_COUNT"));
			double time2 = (double)(statistics.get("M2_TOTALTIME"))/(double)(statistics.get("M2_COUNT"));
			if(time2 - time1 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	
	public static void main(String[] args) throws Exception{
		File directory = new File("userStudyStat/survey");
		Bootstrap_V3 stat = new Bootstrap_V3(directory);
		
		System.out.println("01: Hypothesis_0: For task 1, accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).");
		System.out.println("p-value: "+stat.computePvalueHypothesis01());
		
		System.out.println("02: Hypothesis_0: For task 2, accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).");
		System.out.println("p-value: "+stat.computePvalueHypothesis02());
		
		System.out.println("03: Hypothesis_0: For task 1, accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).");
		System.out.println("p-value: "+stat.computePvalueHypothesis03());
		
		System.out.println("04: Hypothesis_0: For task 2, accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).");
		System.out.println("p-value: "+stat.computePvalueHypothesis04());
		
		System.out.println("05: Hypothesis_0: For task 1, react time of method2 (our approach) is larger than or equal to that of method0 (original code).");
		System.out.println("p-value: "+stat.computePvalueHypothesis05());
		
		System.out.println("06: Hypothesis_0: For task 2, react time of method2 (our approach) is larger than or equal to that of method0 (original code).");
		System.out.println("p-value: "+stat.computePvalueHypothesis06());
		
		System.out.println("07: Hypothesis_0: For task 1, react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).");
		System.out.println("p-value: "+stat.computePvalueHypothesis07());
		
		System.out.println("08: Hypothesis_0: For task 2, react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).");
		System.out.println("p-value: "+stat.computePvalueHypothesis08());
	}
}
