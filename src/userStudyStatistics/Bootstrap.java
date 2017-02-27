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

/*
 * Do a bootstrap hypothesis test.
 */
public class Bootstrap {
	
	static private boolean debug = false;
	
	private int iterations = 100000;
	private Random randGenerate = new Random();
	private ArrayList<JSONObject> dataset = new ArrayList<JSONObject>();
	private Map<String,Map<String,Integer>> datasetByQuestion = new HashMap<String,Map<String,Integer>>();
	
	
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
				this.dataset.add(questions);
			}
		}
	}
	
	private void encodeDatasetByQuestion(){
		for(JSONObject questions:this.dataset){
			for(String questionID: questions.keySet()){
				if(!this.datasetByQuestion.containsKey(questionID)){
					HashMap<String,Integer> emptyEntry = new HashMap<String,Integer>();
					emptyEntry.put("M0_CORRECT", 0);
					emptyEntry.put("M0_WRONG", 0);
					emptyEntry.put("M0_TOTALTIME", 0);
					emptyEntry.put("M1_CORRECT", 0);
					emptyEntry.put("M1_WRONG", 0);
					emptyEntry.put("M1_TOTALTIME", 0);
					emptyEntry.put("M2_CORRECT", 0);
					emptyEntry.put("M2_WRONG", 0);
					emptyEntry.put("M2_TOTALTIME", 0);
					this.datasetByQuestion.put(questionID, emptyEntry);
				}
				JSONObject question = questions.getJSONObject(questionID);
				String key = "";
				if(question.getInt("method")==0){
					key += "M0"; 	
				}
				else if(question.getInt("method")==1){
					key += "M1";
				}
				else if(question.getInt("method")==2){
					key += "M2";
				}
				
				if(question.getInt("correctSolution") == Integer.parseInt(question.getString("answer"))){
					key += "_CORRECT";
				}
				else{
					key += "_WRONG";
				}
				int oldValue = this.datasetByQuestion.get(questionID).get(key);
				this.datasetByQuestion.get(questionID).put(key, oldValue+1);
				int olTimeValue = this.datasetByQuestion.get(questionID).get(key.substring(0,2)+"_TOTALTIME");
				this.datasetByQuestion.get(questionID).put(key.substring(0,2)+"_TOTALTIME", olTimeValue+question.getInt("time"));
			}
		}
		if(debug){
			for(int i=1; i<=19; i++){
				if(i==1||i==2||i==9||i==10){
					continue;
				}
				String questionID = ""+i;
				System.out.println("==============="+questionID+"===============");
				Map<String,Integer> questionStat = this.datasetByQuestion.get(questionID);
				
				System.out.println("M0_CORRECT"+":"+questionStat.get("M0_CORRECT"));
				System.out.println("M0_WRONG"+":"+questionStat.get("M0_WRONG"));
				System.out.println("M0_TOTALTIME"+":"+questionStat.get("M0_TOTALTIME"));
				System.out.println("M1_CORRECT"+":"+questionStat.get("M1_CORRECT"));
				System.out.println("M1_WRONG"+":"+questionStat.get("M1_WRONG"));
				System.out.println("M1_TOTALTIME"+":"+questionStat.get("M1_TOTALTIME"));
				System.out.println("M2_CORRECT"+":"+questionStat.get("M2_CORRECT"));
				System.out.println("M2_WRONG"+":"+questionStat.get("M2_WRONG"));
				System.out.println("M2_TOTALTIME"+":"+questionStat.get("M2_TOTALTIME"));
			}
		}
	}

	
	public Bootstrap(final File directory) throws IOException{
		this.loadDataset(directory);
		this.encodeDatasetByQuestion();
	}
	
	
	/*
	 * 01: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).[Sample by participants]
	 */
	public double computePvalueHypothesis01(){
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//Simulate new data via re-sampling
			ArrayList<JSONObject> sampleSet = new ArrayList<JSONObject>();
			for(int j=0;j<this.dataset.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(this.dataset.size());
				sampleSet.add(this.dataset.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M0_CORRECT", 0);
			statistics.put("M0_WRONG", 0);
			statistics.put("M2_CORRECT", 0);
			statistics.put("M2_WRONG", 0);
			for(int j=0; j<sampleSet.size();j++){
				JSONObject oneUser = sampleSet.get(j);
				for(String questionID: oneUser.keySet()){
					JSONObject question = oneUser.getJSONObject(questionID);
					String key = "";
					if(question.getInt("method")==1){
						continue;
					}
					if(question.getInt("method")==0){
						key += "M0"; 	
					}
					else if(question.getInt("method")==2){
						key += "M2";
					}
					if(question.getInt("correctSolution") == Integer.parseInt(question.getString("answer"))){
						key += "_CORRECT";
					}
					else{
						key += "_WRONG";
					}
					int oldValue = statistics.get(key);
					statistics.put(key, oldValue+1);
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
	 * 02: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).[Sample by participants]
	 */
	public double computePvalueHypothesis02(){
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//Simulate new data via re-sampling
			ArrayList<JSONObject> sampleSet = new ArrayList<JSONObject>();
			for(int j=0;j<this.dataset.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(this.dataset.size());
				sampleSet.add(this.dataset.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M1_CORRECT", 0);
			statistics.put("M1_WRONG", 0);
			statistics.put("M2_CORRECT", 0);
			statistics.put("M2_WRONG", 0);
			for(int j=0; j<sampleSet.size();j++){
				JSONObject oneUser = sampleSet.get(j);
				for(String questionID: oneUser.keySet()){
					JSONObject question = oneUser.getJSONObject(questionID);
					String key = "";
					if(question.getInt("method")==0){
						continue;
					}
					if(question.getInt("method")==1){
						key += "M1"; 	
					}
					else if(question.getInt("method")==2){
						key += "M2";
					}
					if(question.getInt("correctSolution") == Integer.parseInt(question.getString("answer"))){
						key += "_CORRECT";
					}
					else{
						key += "_WRONG";
					}
					int oldValue = statistics.get(key);
					statistics.put(key, oldValue+1);
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
	 * 03: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method0 (original code).[Sample by participants]
	 */
	public double computePvalueHypothesis03(){
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//Simulate new data via re-sampling
			ArrayList<JSONObject> sampleSet = new ArrayList<JSONObject>();
			for(int j=0;j<this.dataset.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(this.dataset.size());
				sampleSet.add(this.dataset.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M0_SUM", 0);
			statistics.put("M0_COUNT",0);
			statistics.put("M2_SUM", 0);
			statistics.put("M2_COUNT",0);
			for(int j=0; j<sampleSet.size();j++){
				JSONObject oneUser = sampleSet.get(j);
				for(String questionID: oneUser.keySet()){
					JSONObject question = oneUser.getJSONObject(questionID);
					String key = "";
					if(question.getInt("method")==1){
						continue;
					}
					if(question.getInt("method")==0){
						key += "M0"; 	
					}
					else if(question.getInt("method")==2){
						key += "M2";
					}
					int oldSum = statistics.get(key+"_SUM");
					statistics.put(key+"_SUM", oldSum + question.getInt("time"));
					int oldCount = statistics.get(key+"_COUNT");
					statistics.put(key+"_COUNT", oldCount+1);
				}
			}
			double time0 = (double)(statistics.get("M0_SUM"))/(double)(statistics.get("M0_COUNT"));
			double time2 = (double)(statistics.get("M2_SUM"))/(double)(statistics.get("M2_COUNT"));
			if(time2 - time0 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 04: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).[Sample by participants]
	 */
	public double computePvalueHypothesis04(){
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//Simulate new data via re-sampling
			ArrayList<JSONObject> sampleSet = new ArrayList<JSONObject>();
			for(int j=0;j<this.dataset.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(this.dataset.size());
				sampleSet.add(this.dataset.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M1_SUM", 0);
			statistics.put("M1_COUNT",0);
			statistics.put("M2_SUM", 0);
			statistics.put("M2_COUNT",0);
			for(int j=0; j<sampleSet.size();j++){
				JSONObject oneUser = sampleSet.get(j);
				for(String questionID: oneUser.keySet()){
					JSONObject question = oneUser.getJSONObject(questionID);
					String key = "";
					if(question.getInt("method")==0){
						continue;
					}
					if(question.getInt("method")==1){
						key += "M1"; 	
					}
					else if(question.getInt("method")==2){
						key += "M2";
					}
					int oldSum = statistics.get(key+"_SUM");
					statistics.put(key+"_SUM", oldSum + question.getInt("time"));
					int oldCount = statistics.get(key+"_COUNT");
					statistics.put(key+"_COUNT", oldCount+1);
				}
			}
			double time1 = (double)(statistics.get("M1_SUM"))/(double)(statistics.get("M1_COUNT"));
			double time2 = (double)(statistics.get("M2_SUM"))/(double)(statistics.get("M2_COUNT"));
			if(time2 - time1 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 05: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).[Sample by questions]
	 */
	public double computePvalueHypothesis05(){
		ArrayList<String> questionIDSet = new ArrayList<String>();
		JSONObject sampelQuestion = this.dataset.get(0);
		for(String id:sampelQuestion.keySet()){
			questionIDSet.add(id);
		}
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//Simulate new data via re-sampling
			ArrayList<String> sampleSet = new ArrayList<String>();
			for(int j=0;j<questionIDSet.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(questionIDSet.size());
				sampleSet.add(questionIDSet.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M0_CORRECT", 0);
			statistics.put("M0_WRONG", 0);
			statistics.put("M2_CORRECT", 0);
			statistics.put("M2_WRONG", 0);
			for(int j=0;j<sampleSet.size();j++){
				Map<String,Integer> currentQuestion = this.datasetByQuestion.get(sampleSet.get(j));
				statistics.put("M0_CORRECT",statistics.get("M0_CORRECT")+currentQuestion.get("M0_CORRECT"));
				statistics.put("M0_WRONG",statistics.get("M0_WRONG")+currentQuestion.get("M0_WRONG"));
				statistics.put("M2_CORRECT",statistics.get("M2_CORRECT")+currentQuestion.get("M2_CORRECT"));
				statistics.put("M2_WRONG",statistics.get("M2_WRONG")+currentQuestion.get("M2_WRONG"));
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
	 * 06: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).[Sample by questions]
	 */
	public double computePvalueHypothesis06(){
		ArrayList<String> questionIDSet = new ArrayList<String>();
		JSONObject sampelQuestion = this.dataset.get(0);
		for(String id:sampelQuestion.keySet()){
			questionIDSet.add(id);
		}
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//Simulate new data via re-sampling
			ArrayList<String> sampleSet = new ArrayList<String>();
			for(int j=0;j<questionIDSet.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(questionIDSet.size());
				sampleSet.add(questionIDSet.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M1_CORRECT", 0);
			statistics.put("M1_WRONG", 0);
			statistics.put("M2_CORRECT", 0);
			statistics.put("M2_WRONG", 0);
			for(int j=0;j<sampleSet.size();j++){
				Map<String,Integer> currentQuestion = this.datasetByQuestion.get(sampleSet.get(j));
				statistics.put("M1_CORRECT",statistics.get("M1_CORRECT")+currentQuestion.get("M1_CORRECT"));
				statistics.put("M1_WRONG",statistics.get("M1_WRONG")+currentQuestion.get("M1_WRONG"));
				statistics.put("M2_CORRECT",statistics.get("M2_CORRECT")+currentQuestion.get("M2_CORRECT"));
				statistics.put("M2_WRONG",statistics.get("M2_WRONG")+currentQuestion.get("M2_WRONG"));
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
	 * 07: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method0 (original code).[Sample by questions]
	 */
	public double computePvalueHypothesis07(){
		ArrayList<String> questionIDSet = new ArrayList<String>();
		JSONObject sampelQuestion = this.dataset.get(0);
		for(String id:sampelQuestion.keySet()){
			questionIDSet.add(id);
		}
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//Simulate new data via re-sampling
			ArrayList<String> sampleSet = new ArrayList<String>();
			for(int j=0;j<questionIDSet.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(questionIDSet.size());
				sampleSet.add(questionIDSet.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M0_TOTAL", 0);
			statistics.put("M0_COUNT", 0);
			statistics.put("M2_TOTAL", 0);
			statistics.put("M2_COUNT", 0);
			for(int j=0;j<sampleSet.size();j++){
				Map<String,Integer> currentQuestion = this.datasetByQuestion.get(sampleSet.get(j));
				statistics.put("M0_TOTAL",statistics.get("M0_TOTAL")+currentQuestion.get("M0_TOTALTIME"));
				statistics.put("M0_COUNT",statistics.get("M0_COUNT")+(currentQuestion.get("M0_CORRECT")+currentQuestion.get("M0_WRONG")));
				statistics.put("M2_TOTAL",statistics.get("M2_TOTAL")+currentQuestion.get("M2_TOTALTIME"));
				statistics.put("M2_COUNT",statistics.get("M2_COUNT")+(currentQuestion.get("M2_CORRECT")+currentQuestion.get("M2_WRONG")));
			}
			
			double time0 = (double)(statistics.get("M0_TOTAL"))/(double)(statistics.get("M0_COUNT"));
			double time2 = (double)(statistics.get("M2_TOTAL"))/(double)(statistics.get("M2_COUNT"));
			if(time2 - time0 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	/*
	 * 08: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).[Sample by questions]
	 */
	public double computePvalueHypothesis08(){
		ArrayList<String> questionIDSet = new ArrayList<String>();
		JSONObject sampelQuestion = this.dataset.get(0);
		for(String id:sampelQuestion.keySet()){
			questionIDSet.add(id);
		}
		int count = 0;
		for(int i=0; i<this.iterations;i++){
			//Simulate new data via re-sampling
			ArrayList<String> sampleSet = new ArrayList<String>();
			for(int j=0;j<questionIDSet.size();j++){
				//sample with replacement
				int index = this.randGenerate.nextInt(questionIDSet.size());
				sampleSet.add(questionIDSet.get(index));
			}
			//Check if the null hypothesis hold
			HashMap<String,Integer> statistics = new HashMap<String,Integer>();
			statistics.put("M1_TOTAL", 0);
			statistics.put("M1_COUNT", 0);
			statistics.put("M2_TOTAL", 0);
			statistics.put("M2_COUNT", 0);
			for(int j=0;j<sampleSet.size();j++){
				Map<String,Integer> currentQuestion = this.datasetByQuestion.get(sampleSet.get(j));
				statistics.put("M1_TOTAL",statistics.get("M1_TOTAL")+currentQuestion.get("M1_TOTALTIME"));
				statistics.put("M1_COUNT",statistics.get("M1_COUNT")+(currentQuestion.get("M1_CORRECT")+currentQuestion.get("M1_WRONG")));
				statistics.put("M2_TOTAL",statistics.get("M2_TOTAL")+currentQuestion.get("M2_TOTALTIME"));
				statistics.put("M2_COUNT",statistics.get("M2_COUNT")+(currentQuestion.get("M2_CORRECT")+currentQuestion.get("M2_WRONG")));
			}
			
			double time1 = (double)(statistics.get("M1_TOTAL"))/(double)(statistics.get("M1_COUNT"));
			double time2 = (double)(statistics.get("M2_TOTAL"))/(double)(statistics.get("M2_COUNT"));
			if(time2 - time1 > 0.00001){
				count++;
			}
		}
		return (double)(count)/(double)(this.iterations);
	}
	
	
	
	public static void main(String[] args) throws Exception{
		File directory = new File("userStudyStat/survey");
		Bootstrap stat = new Bootstrap(directory);
		System.out.println("01: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).[Sample by participants]");
		System.out.println("p-value: "+stat.computePvalueHypothesis01());
		System.out.println("02: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).[Sample by participants]");
		System.out.println("p-value: "+stat.computePvalueHypothesis02());
		System.out.println("03: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method0 (original code).[Sample by participants]");
		System.out.println("p-value: "+stat.computePvalueHypothesis03());
		System.out.println("04: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).[Sample by participants]");
		System.out.println("p-value: "+stat.computePvalueHypothesis04());
		System.out.println("05: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method0 (original code).[Sample by questions]");
		System.out.println("p-value: "+stat.computePvalueHypothesis05());
		System.out.println("06: Hypothesis_0: accuracy of method2 (our approach) is lower than or equal to that of method1 (naive approach).[Sample by questions]");
		System.out.println("p-value: "+stat.computePvalueHypothesis06());
		System.out.println("07: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method0 (original code).[Sample by questions]");
		System.out.println("p-value: "+stat.computePvalueHypothesis07());
		System.out.println("08: Hypothesis_0: react time of method2 (our approach) is larger than or equal to that of method1 (naive approach).[Sample by questions]");
		System.out.println("p-value: "+stat.computePvalueHypothesis08());
	}
}
