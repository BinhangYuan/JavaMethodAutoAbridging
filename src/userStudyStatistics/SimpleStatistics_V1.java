package userStudyStatistics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.json.JSONObject;

import statementGraph.ASTParserUtils;

public class SimpleStatistics_V1 {
	public static ArrayList<String> task1 = new ArrayList<String>(Arrays.asList(new String[]{"3","4","5","6","7","8"}));
	public static ArrayList<String> task2 = new ArrayList<String>(Arrays.asList(new String[]{"11","12","13","14","15","16","17","18","19"}));
	
	private static boolean debug = false;
	
	private Map<String,Map<String,Integer>> task1AnswerCount = new HashMap<String,Map<String,Integer>>();
	private Map<String,Map<String,Integer>> task1ReactTime = new HashMap<String,Map<String,Integer>>();
	
	private Map<String,Map<String,Integer>> task2AnswerCount = new HashMap<String,Map<String,Integer>>();
	private Map<String,Map<String,Integer>> task2ReactTime = new HashMap<String,Map<String,Integer>>();
	
	private Map<String,Map<String,String>> surveyQuestion = new HashMap<String,Map<String,String>>();
	private Map<String,Map<String,Integer>> surveyResult = new HashMap<String,Map<String,Integer>>();
	
	private Map<String,Integer> expert = new HashMap<String,Integer>();
	
	private ArrayList<JSONObject> raw = new ArrayList<JSONObject>();
	
	private void initSurveyQuestion(){
		Map<String,String> question0 = new HashMap<String,String>();
		question0.put("title", "Which version of the code tended to make it easier to write your description?");
		question0.put("suveryQ0Option0", "Non-truncated code");
		question0.put("suveryQ0Option1", "Naive Approach");
		question0.put("suveryQ0Option2", "Final Approach");
		this.surveyQuestion.put("suveryQ0", question0);
		Map<String,Integer> question0Stat = new HashMap<String,Integer>();
		question0Stat.put("suveryQ0Option0", 0);
		question0Stat.put("suveryQ0Option1", 0);
		question0Stat.put("suveryQ0Option2", 0);
		this.surveyResult.put("suveryQ0", question0Stat);
		
		Map<String,String> question1 = new HashMap<String,String>();
		question1.put("title", "Which truncated level is easiest to use for naive approach?");
		question1.put("suveryQ1Option0", "To around 15 lines");
		question1.put("suveryQ1Option1", "To around 30 lines");
		question1.put("suveryQ1Option2", "To 50\\% of the original lines");
		this.surveyQuestion.put("suveryQ1", question1);
		Map<String,Integer> question1Stat = new HashMap<String,Integer>();
		question1Stat.put("suveryQ1Option0", 0);
		question1Stat.put("suveryQ1Option1", 0);
		question1Stat.put("suveryQ1Option2", 0);
		this.surveyResult.put("suveryQ1", question1Stat);
		
		Map<String,String> question2 = new HashMap<String,String>();
		question2.put("title", "Which truncated level is easiest to use for CodeAbstract?");
		question2.put("suveryQ2Option0", "To around 15 lines");
		question2.put("suveryQ2Option1", "To around 30 lines");
		question2.put("suveryQ2Option2", "To 50\\% of the original lines");
		this.surveyQuestion.put("suveryQ2", question2);
		Map<String,Integer> question2Stat = new HashMap<String,Integer>();
		question2Stat.put("suveryQ2Option0", 0);
		question2Stat.put("suveryQ2Option1", 0);
		question2Stat.put("suveryQ2Option2", 0);
		this.surveyResult.put("suveryQ2", question2Stat);
		
		Map<String,String> question3 = new HashMap<String,String>();
		question3.put("title", "Which abridged code misses more necessary statements to know the high level functionality of this method?");
		question3.put("suveryQ3Option0", "Naive Approach");
		question3.put("suveryQ3Option1", "Final Approach");
		this.surveyQuestion.put("suveryQ3", question3);
		Map<String,Integer> question3Stat = new HashMap<String,Integer>();
		question3Stat.put("suveryQ3Option0", 0);
		question3Stat.put("suveryQ3Option1", 0);
		this.surveyResult.put("suveryQ3", question3Stat);
		
		Map<String,String> question4 = new HashMap<String,String>();
		question4.put("title", "Which abridged code contains more unnecessary statements to know the high level functionality of this method?");
		question4.put("suveryQ4Option0", "Naive Approach");
		question4.put("suveryQ4Option1", "Final Approach");
		this.surveyQuestion.put("suveryQ4", question4);
		Map<String,Integer> question4Stat = new HashMap<String,Integer>();
		question4Stat.put("suveryQ4Option0", 0);
		question4Stat.put("suveryQ4Option1", 0);
		this.surveyResult.put("suveryQ4", question4Stat);
	}
	
	
	private void computeExpertStat(){
		this.expert.put("Expert", 0);
		this.expert.put("Proficient", 0);
		this.expert.put("Competent", 0);
		this.expert.put("Advanced Beginner", 0);
		this.expert.put("Novice", 0);
		for(JSONObject input:this.raw){
			JSONObject visitor = input.getJSONObject("visitor");
			String level = visitor.getString("level");
			this.expert.put(level, this.expert.get(level)+1);
		}
	}
	
	
	public SimpleStatistics_V1(final File directory) throws IOException{
		Assert.isTrue(directory.isDirectory());
		this.initSurveyQuestion();
		for(final File file: directory.listFiles()){
			Assert.isTrue(!file.isDirectory());
			if(file.getName().endsWith(".json")){
				String str = ASTParserUtils.readFileToString(file.getAbsolutePath());
				if(debug){
					System.out.println(str);
				}
				JSONObject input = new JSONObject(str);
				this.raw.add(input);
			}
		}
		this.computeAnswerCount();
		if(debug){
			System.out.println();
			System.out.println();
		}
		this.computeAverageTime();
		if(debug){
			System.out.println();
			System.out.println();
		}
		this.computeExpertStat();
		this.computeSurvey();
		this.computeOverallTime();
	}
	
	private void computeAnswerCount(){
		HashMap<String,Integer> task1SumEntry = new HashMap<String,Integer>();
		task1SumEntry.put("M0_CORRECT", 0);
		task1SumEntry.put("M0_WRONG", 0);
		task1SumEntry.put("M1_CORRECT", 0);
		task1SumEntry.put("M1_WRONG", 0);
		task1SumEntry.put("M2_CORRECT", 0);
		task1SumEntry.put("M2_WRONG", 0);
		this.task1AnswerCount.put("sum", task1SumEntry);
		HashMap<String,Integer> task2SumEntry = new HashMap<String,Integer>();
		task2SumEntry.put("M0_CORRECT", 0);
		task2SumEntry.put("M0_WRONG", 0);
		task2SumEntry.put("M1_CORRECT", 0);
		task2SumEntry.put("M1_WRONG", 0);
		task2SumEntry.put("M2_CORRECT", 0);
		task2SumEntry.put("M2_WRONG", 0);
		this.task2AnswerCount.put("sum", task2SumEntry);
		for(JSONObject input:this.raw){
			JSONObject answers = input.getJSONObject("answers");
			for(String questionID: answers.keySet()){
				if(answers.getJSONObject(questionID).get("type").equals("T1") && !answers.getJSONObject(questionID).getBoolean("practice")){
					if(!this.task1AnswerCount.containsKey(questionID)){
						HashMap<String,Integer> emptyEntry = new HashMap<String,Integer>();
						emptyEntry.put("M0_CORRECT", 0);
						emptyEntry.put("M0_WRONG", 0);
						emptyEntry.put("M1_CORRECT", 0);
						emptyEntry.put("M1_WRONG", 0);
						emptyEntry.put("M2_CORRECT", 0);
						emptyEntry.put("M2_WRONG", 0);
						this.task1AnswerCount.put(questionID, emptyEntry);
					}
					JSONObject question = answers.getJSONObject(questionID);
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
					int oldValue = this.task1AnswerCount.get(questionID).get(key);
					this.task1AnswerCount.get(questionID).put(key, oldValue+1);
					int oldSumValue = this.task1AnswerCount.get("sum").get(key);
					this.task1AnswerCount.get("sum").put(key, oldSumValue+1);
				}
				else if(answers.getJSONObject(questionID).get("type").equals("T2") && !answers.getJSONObject(questionID).getBoolean("practice")){
					if(!this.task2AnswerCount.containsKey(questionID)){
						HashMap<String,Integer> emptyEntry = new HashMap<String,Integer>();
						emptyEntry.put("M0_CORRECT", 0);
						emptyEntry.put("M0_WRONG", 0);
						emptyEntry.put("M1_CORRECT", 0);
						emptyEntry.put("M1_WRONG", 0);
						emptyEntry.put("M2_CORRECT", 0);
						emptyEntry.put("M2_WRONG", 0);
						this.task2AnswerCount.put(questionID, emptyEntry);
					}
					JSONObject question = answers.getJSONObject(questionID);
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
					int oldValue = this.task2AnswerCount.get(questionID).get(key);
					this.task2AnswerCount.get(questionID).put(key, oldValue+1);
					int oldSumValue = this.task2AnswerCount.get("sum").get(key);
					this.task2AnswerCount.get("sum").put(key, oldSumValue+1);
				}
			}
		}
		if(debug){
			System.out.println("Task1 summary");
			for(String questionID:task1){
				System.out.println("==============="+questionID+"===============");
				Map<String,Integer> questionStat = this.task1AnswerCount.get(questionID);
				System.out.println("M0_CORRECT"+":"+questionStat.get("M0_CORRECT"));
				System.out.println("M0_WRONG"+":"+questionStat.get("M0_WRONG"));
				System.out.println("M1_CORRECT"+":"+questionStat.get("M1_CORRECT"));
				System.out.println("M1_WRONG"+":"+questionStat.get("M1_WRONG"));
				System.out.println("M2_CORRECT"+":"+questionStat.get("M2_CORRECT"));
				System.out.println("M2_WRONG"+":"+questionStat.get("M2_WRONG"));
			}
			System.out.println("===============sum===============");
			System.out.println("M0_CORRECT"+":"+this.task1AnswerCount.get("sum").get("M0_CORRECT"));
			System.out.println("M0_WRONG"+":"+this.task1AnswerCount.get("sum").get("M0_WRONG"));
			System.out.println("Correct rate:"+(double)(this.task1AnswerCount.get("sum").get("M0_CORRECT"))/(this.task1AnswerCount.get("sum").get("M0_CORRECT")+this.task1AnswerCount.get("sum").get("M0_WRONG")));
			System.out.println("M1_CORRECT"+":"+this.task1AnswerCount.get("sum").get("M1_CORRECT"));
			System.out.println("M1_WRONG"+":"+this.task1AnswerCount.get("sum").get("M1_WRONG"));
			System.out.println("Correct rate:"+(double)(this.task1AnswerCount.get("sum").get("M1_CORRECT"))/(this.task1AnswerCount.get("sum").get("M1_CORRECT")+this.task1AnswerCount.get("sum").get("M1_WRONG")));
			System.out.println("M2_CORRECT"+":"+this.task1AnswerCount.get("sum").get("M2_CORRECT"));
			System.out.println("M2_WRONG"+":"+this.task1AnswerCount.get("sum").get("M2_WRONG"));
			System.out.println("Correct rate:"+(double)(this.task1AnswerCount.get("sum").get("M2_CORRECT"))/(this.task1AnswerCount.get("sum").get("M2_CORRECT")+this.task1AnswerCount.get("sum").get("M2_WRONG")));		
		
			System.out.println("Task2 summary");
			for(String questionID:task2){
				System.out.println("==============="+questionID+"===============");
				Map<String,Integer> questionStat = this.task2AnswerCount.get(questionID);
				System.out.println("M0_CORRECT"+":"+questionStat.get("M0_CORRECT"));
				System.out.println("M0_WRONG"+":"+questionStat.get("M0_WRONG"));
				System.out.println("M1_CORRECT"+":"+questionStat.get("M1_CORRECT"));
				System.out.println("M1_WRONG"+":"+questionStat.get("M1_WRONG"));
				System.out.println("M2_CORRECT"+":"+questionStat.get("M2_CORRECT"));
				System.out.println("M2_WRONG"+":"+questionStat.get("M2_WRONG"));
			}
			System.out.println("===============sum===============");
			System.out.println("M0_CORRECT"+":"+this.task2AnswerCount.get("sum").get("M0_CORRECT"));
			System.out.println("M0_WRONG"+":"+this.task2AnswerCount.get("sum").get("M0_WRONG"));
			System.out.println("Correct rate:"+(double)(this.task2AnswerCount.get("sum").get("M0_CORRECT"))/(this.task2AnswerCount.get("sum").get("M0_CORRECT")+this.task2AnswerCount.get("sum").get("M0_WRONG")));
			System.out.println("M1_CORRECT"+":"+this.task2AnswerCount.get("sum").get("M1_CORRECT"));
			System.out.println("M1_WRONG"+":"+this.task2AnswerCount.get("sum").get("M1_WRONG"));
			System.out.println("Correct rate:"+(double)(this.task2AnswerCount.get("sum").get("M1_CORRECT"))/(this.task2AnswerCount.get("sum").get("M1_CORRECT")+this.task2AnswerCount.get("sum").get("M1_WRONG")));
			System.out.println("M2_CORRECT"+":"+this.task2AnswerCount.get("sum").get("M2_CORRECT"));
			System.out.println("M2_WRONG"+":"+this.task2AnswerCount.get("sum").get("M2_WRONG"));
			System.out.println("Correct rate:"+(double)(this.task2AnswerCount.get("sum").get("M2_CORRECT"))/(this.task2AnswerCount.get("sum").get("M2_CORRECT")+this.task2AnswerCount.get("sum").get("M2_WRONG")));
		}
	}
	
	private void computeOverallTime(){
		int totalTimeTask1 = 0;
		int totalTimeTask2 = 0;
		for(JSONObject input:this.raw){
			JSONObject answers = input.getJSONObject("answers");
			for(String questionID: answers.keySet()){
				if(answers.getJSONObject(questionID).get("type").equals("T1")){
					JSONObject question = answers.getJSONObject(questionID);
					totalTimeTask1 += question.getInt("time");
				}
				else if(answers.getJSONObject(questionID).get("type").equals("T2")){
					JSONObject question = answers.getJSONObject(questionID);
					totalTimeTask2 += question.getInt("time");
				}
			}
		}
		System.out.println("Overall time:"+(totalTimeTask1+totalTimeTask2)/this.raw.size());
		System.out.println("Task1 overall time:"+(totalTimeTask1)/this.raw.size());
		System.out.println("Task2 overall time:"+(totalTimeTask2)/this.raw.size());
	}
	
	private void computeAverageTime(){
		HashMap<String,Integer> task1SumEntry = new HashMap<String,Integer>();
		task1SumEntry.put("M0", 0);
		task1SumEntry.put("M1", 0);
		task1SumEntry.put("M2", 0);
		this.task1ReactTime.put("sum",task1SumEntry);
		HashMap<String,Integer> task2SumEntry = new HashMap<String,Integer>();
		task2SumEntry.put("M0", 0);
		task2SumEntry.put("M1", 0);
		task2SumEntry.put("M2", 0);
		this.task2ReactTime.put("sum",task2SumEntry);
		for(JSONObject input:this.raw){
			JSONObject answers = input.getJSONObject("answers");
			for(String questionID: answers.keySet()){
				if(answers.getJSONObject(questionID).get("type").equals("T1") && !answers.getJSONObject(questionID).getBoolean("practice")){
					if(!this.task1ReactTime.containsKey(questionID)){
						HashMap<String,Integer> emptyEntry = new HashMap<String,Integer>();
						emptyEntry.put("M0", 0);
						emptyEntry.put("M1", 0);
						emptyEntry.put("M2", 0);
						this.task1ReactTime.put(questionID, emptyEntry);
					}
					JSONObject question = answers.getJSONObject(questionID);
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
					int oldValue = this.task1ReactTime.get(questionID).get(key);
					int newValue = oldValue + question.getInt("time");
					this.task1ReactTime.get(questionID).put(key, newValue);
					int oldSumValue = this.task1ReactTime.get("sum").get(key);
					int newSumValue = oldSumValue + question.getInt("time");
					this.task1ReactTime.get("sum").put(key, newSumValue);
				}
				else if(answers.getJSONObject(questionID).get("type").equals("T2") && !answers.getJSONObject(questionID).getBoolean("practice")){
					if(!this.task2ReactTime.containsKey(questionID)){
						HashMap<String,Integer> emptyEntry = new HashMap<String,Integer>();
						emptyEntry.put("M0", 0);
						emptyEntry.put("M1", 0);
						emptyEntry.put("M2", 0);
						this.task2ReactTime.put(questionID, emptyEntry);
					}
					JSONObject question = answers.getJSONObject(questionID);
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
					int oldValue = this.task2ReactTime.get(questionID).get(key);
					int newValue = oldValue + question.getInt("time");
					this.task2ReactTime.get(questionID).put(key, newValue);
					int oldSumValue = this.task2ReactTime.get("sum").get(key);
					int newSumValue = oldSumValue + question.getInt("time");
					this.task2ReactTime.get("sum").put(key, newSumValue);
				}
			}
		}
		
		for(String questionID: task1){
			Map<String,Integer> questionStatReactTime = this.task1ReactTime.get(questionID);
			Map<String,Integer> questionStatAnswerCount = this.task1AnswerCount.get(questionID);
			if(debug){
				System.out.println("==============="+questionID+"(total)===============");
				System.out.println("M0"+":"+questionStatReactTime.get("M0"));
				System.out.println("M1"+":"+questionStatReactTime.get("M1"));
				System.out.println("M2"+":"+questionStatReactTime.get("M2"));
			}
			
			int average0 = (questionStatAnswerCount.get("M0_CORRECT")+questionStatAnswerCount.get("M0_WRONG"))==0?0:questionStatReactTime.get("M0")/(questionStatAnswerCount.get("M0_CORRECT")+questionStatAnswerCount.get("M0_WRONG"));
			int average1 = (questionStatAnswerCount.get("M1_CORRECT")+questionStatAnswerCount.get("M1_WRONG"))==0?0:questionStatReactTime.get("M1")/(questionStatAnswerCount.get("M1_CORRECT")+questionStatAnswerCount.get("M1_WRONG"));
			int average2 = (questionStatAnswerCount.get("M2_CORRECT")+questionStatAnswerCount.get("M2_WRONG"))==0?0:questionStatReactTime.get("M2")/(questionStatAnswerCount.get("M2_CORRECT")+questionStatAnswerCount.get("M2_WRONG"));
			questionStatReactTime.put("M0", average0);
			questionStatReactTime.put("M1", average1);
			questionStatReactTime.put("M2", average2);
			if(debug){
				System.out.println("==============="+questionID+"(Average)===============");
				System.out.println("M0"+":"+questionStatReactTime.get("M0"));
				System.out.println("M1"+":"+questionStatReactTime.get("M1"));
				System.out.println("M2"+":"+questionStatReactTime.get("M2"));
			}
		}
		int task1SumAverage0 = this.task1ReactTime.get("sum").get("M0")/(this.task1AnswerCount.get("sum").get("M0_CORRECT") + this.task1AnswerCount.get("sum").get("M0_CORRECT"));
		int task1SumAverage1 = this.task1ReactTime.get("sum").get("M1")/(this.task1AnswerCount.get("sum").get("M1_CORRECT") + this.task1AnswerCount.get("sum").get("M1_CORRECT"));
		int task1SumAverage2 = this.task1ReactTime.get("sum").get("M2")/(this.task1AnswerCount.get("sum").get("M2_CORRECT") + this.task1AnswerCount.get("sum").get("M2_CORRECT"));
		this.task1ReactTime.get("sum").put("M0", task1SumAverage0);
		this.task1ReactTime.get("sum").put("M1", task1SumAverage1);
		this.task1ReactTime.get("sum").put("M2", task1SumAverage2);
		if(debug){
			System.out.println("=============== sum (Average)===============");
			System.out.println("M0"+":"+this.task1ReactTime.get("sum").get("M0"));
			System.out.println("M1"+":"+this.task1ReactTime.get("sum").get("M1"));
			System.out.println("M2"+":"+this.task1ReactTime.get("sum").get("M2"));
		}
		
		for(String questionID: task2){
			Map<String,Integer> questionStatReactTime = this.task2ReactTime.get(questionID);
			Map<String,Integer> questionStatAnswerCount = this.task2AnswerCount.get(questionID);
			if(debug){
				System.out.println("==============="+questionID+"(total)===============");
				System.out.println("M0"+":"+questionStatReactTime.get("M0"));
				System.out.println("M1"+":"+questionStatReactTime.get("M1"));
				System.out.println("M2"+":"+questionStatReactTime.get("M2"));
			}
			
			int average0 = (questionStatAnswerCount.get("M0_CORRECT")+questionStatAnswerCount.get("M0_WRONG"))==0?0:questionStatReactTime.get("M0")/(questionStatAnswerCount.get("M0_CORRECT")+questionStatAnswerCount.get("M0_WRONG"));
			int average1 = (questionStatAnswerCount.get("M1_CORRECT")+questionStatAnswerCount.get("M1_WRONG"))==0?0:questionStatReactTime.get("M1")/(questionStatAnswerCount.get("M1_CORRECT")+questionStatAnswerCount.get("M1_WRONG"));
			int average2 = (questionStatAnswerCount.get("M2_CORRECT")+questionStatAnswerCount.get("M2_WRONG"))==0?0:questionStatReactTime.get("M2")/(questionStatAnswerCount.get("M2_CORRECT")+questionStatAnswerCount.get("M2_WRONG"));
			questionStatReactTime.put("M0", average0);
			questionStatReactTime.put("M1", average1);
			questionStatReactTime.put("M2", average2);
			if(debug){
				System.out.println("==============="+questionID+"(Average)===============");
				System.out.println("M0"+":"+questionStatReactTime.get("M0"));
				System.out.println("M1"+":"+questionStatReactTime.get("M1"));
				System.out.println("M2"+":"+questionStatReactTime.get("M2"));
			}
		}
		int task2SumAverage0 = this.task2ReactTime.get("sum").get("M0")/(this.task2AnswerCount.get("sum").get("M0_CORRECT") + this.task2AnswerCount.get("sum").get("M0_CORRECT"));
		int task2SumAverage1 = this.task2ReactTime.get("sum").get("M1")/(this.task2AnswerCount.get("sum").get("M1_CORRECT") + this.task2AnswerCount.get("sum").get("M1_CORRECT"));
		int task2SumAverage2 = this.task2ReactTime.get("sum").get("M2")/(this.task2AnswerCount.get("sum").get("M2_CORRECT") + this.task2AnswerCount.get("sum").get("M2_CORRECT"));
		this.task2ReactTime.get("sum").put("M0", task2SumAverage0);
		this.task2ReactTime.get("sum").put("M1", task2SumAverage1);
		this.task2ReactTime.get("sum").put("M2", task2SumAverage2);
		if(debug){
			System.out.println("=============== sum (Average)===============");
			System.out.println("M0"+":"+this.task2ReactTime.get("sum").get("M0"));
			System.out.println("M1"+":"+this.task2ReactTime.get("sum").get("M1"));
			System.out.println("M2"+":"+this.task2ReactTime.get("sum").get("M2"));
		}	
	}
	
	private void computeSurvey(){
		for(JSONObject input:this.raw){
			JSONObject survey = input.getJSONObject("survey");
			for(String key: survey.keySet()){
				if(debug){
					System.out.println(key);
					System.out.println(survey.getString(key));
					for(String options: this.surveyResult.get(key).keySet()){
						System.out.println(options);
					}
				}
				Assert.isNotNull(this.surveyResult.get(key));
				Assert.isNotNull(this.surveyResult.get(key).get(survey.getString(key)));
				int oldValue = this.surveyResult.get(key).get(survey.getString(key));
				this.surveyResult.get(key).put(survey.getString(key), oldValue+1);
			}
		}
	}
	
	
	public String task1Count2LatexTable(){
		String result = new String();
		result +="\\begin{table*}[t]\n";
		result +="\\centering";
		result +="\\begin{tabular}{|*{13}{c|}}\n";
		result +="\\hline\n";
		result += "& \\multicolumn{4}{ |c| }{ Original Code} & \\multicolumn{4}{ |c| }{ Naive Approach}  & \\multicolumn{4}{ |c| }{ Final Approach}\\\\ \\hline\n";
		result +="No & Correct & Wrong & Total & Rate & Correct & Wrong & Total & Rate & Correct & Wrong & Total & Rate\\\\ \\hline\n";
	    int i = 0;
	    for(String index:task1){
	    	int correct0 = this.task1AnswerCount.get(index).get("M0_CORRECT");
	    	int wrong0 = this.task1AnswerCount.get(index).get("M0_WRONG");
	    	int total0 = correct0+wrong0;
	    	double rate0 = (double)(correct0)/(double)(total0);
	    	int correct1 = this.task1AnswerCount.get(index).get("M1_CORRECT");
	    	int wrong1 = this.task1AnswerCount.get(index).get("M1_WRONG");
	    	int total1 = correct1+wrong1;
	    	double rate1 = (double)(correct1)/(double)(total1);
	    	int correct2 = this.task1AnswerCount.get(index).get("M2_CORRECT");
	    	int wrong2 = this.task1AnswerCount.get(index).get("M2_WRONG");
	    	int total2 = correct2+wrong2;
	    	double rate2 = (double)(correct2)/(double)(total2);
	    	result += (""+(++i)+" & "+correct0+" & "+wrong0+" & "+total0+" & "+String.format("%.3g",rate0)+" & "+correct1+" & "+wrong1+" & "+total1+" & "+String.format("%.3g",rate1)+" & "+correct2+" & "+wrong2+" & "+total2+" & "+String.format("%.3g",rate2)+"\\\\ \n");
	    }
	    result +="\\hline\n";
	    int correct0sum = this.task1AnswerCount.get("sum").get("M0_CORRECT");
    	int wrong0sum = this.task1AnswerCount.get("sum").get("M0_WRONG");
    	int total0sum = correct0sum+wrong0sum;
    	double rate0sum = (double)(correct0sum)/(double)(total0sum);
    	int correct1sum = this.task1AnswerCount.get("sum").get("M1_CORRECT");
    	int wrong1sum = this.task1AnswerCount.get("sum").get("M1_WRONG");
    	int total1sum = correct1sum+wrong1sum;
    	double rate1sum = (double)(correct1sum)/(double)(total1sum);
    	int correct2sum = this.task1AnswerCount.get("sum").get("M2_CORRECT");
    	int wrong2sum = this.task1AnswerCount.get("sum").get("M2_WRONG");
    	int total2sum = correct2sum+wrong2sum;
    	double rate2sum = (double)(correct2sum)/(double)(total2sum);
    	result += ("Total & "+correct0sum+" & "+wrong0sum+" & "+total0sum+" & "+String.format("%.3g",rate0sum)+" & "+correct1sum+" & "+wrong1sum+" & "+total1sum+" & "+String.format("%.3g",rate1sum)+" & "+correct2sum+" & "+wrong2sum+" & "+total2sum+" & "+String.format("%.3g",rate2sum)+"\\\\ \n");
    	result +="\\hline\n";
	    result +="\\end{tabular}\n";
	    result +="\\caption{Correct rate of Task 1.}\n";
	    result +="\\end{table*}\n";
		return result;
	}
	
	
	public String task2Count2LatexTable(){
		String result = new String();
		result +="\\begin{table*}[t]\n";
		result +="\\centering";
		result +="\\begin{tabular}{|*{13}{c|}}\n";
		result +="\\hline\n";
		result += "& \\multicolumn{4}{ |c| }{ Original Code} & \\multicolumn{4}{ |c| }{ Naive Approach}  & \\multicolumn{4}{ |c| }{ Final Approach}\\\\ \\hline\n";
	    result +="No & Correct & Wrong & Total & Rate & Correct & Wrong & Total & Rate & Correct & Wrong & Total & Rate\\\\ \\hline\n";
	    int i = 0;
	    for(String index:task2){
	    	int correct0 = this.task2AnswerCount.get(index).get("M0_CORRECT");
	    	int wrong0 = this.task2AnswerCount.get(index).get("M0_WRONG");
	    	int total0 = correct0+wrong0;
	    	double rate0 = (double)(correct0)/(double)(total0);
	    	int correct1 = this.task2AnswerCount.get(index).get("M1_CORRECT");
	    	int wrong1 = this.task2AnswerCount.get(index).get("M1_WRONG");
	    	int total1 = correct1+wrong1;
	    	double rate1 = (double)(correct1)/(double)(total1);
	    	int correct2 = this.task2AnswerCount.get(index).get("M2_CORRECT");
	    	int wrong2 = this.task2AnswerCount.get(index).get("M2_WRONG");
	    	int total2 = correct2+wrong2;
	    	double rate2 = (double)(correct2)/(double)(total2);
	    	result += (""+(++i)+" & "+correct0+" & "+wrong0+" & "+total0+" & "+String.format("%.3g",rate0)+" & "+correct1+" & "+wrong1+" & "+total1+" & "+String.format("%.3g",rate1)+" & "+correct2+" & "+wrong2+" & "+total2+" & "+String.format("%.3g",rate2)+"\\\\ \n");
	    }
	    result +="\\hline\n";
	    int correct0sum = this.task2AnswerCount.get("sum").get("M0_CORRECT");
    	int wrong0sum = this.task2AnswerCount.get("sum").get("M0_WRONG");
    	int total0sum = correct0sum+wrong0sum;
    	double rate0sum = (double)(correct0sum)/(double)(total0sum);
    	int correct1sum = this.task2AnswerCount.get("sum").get("M1_CORRECT");
    	int wrong1sum = this.task2AnswerCount.get("sum").get("M1_WRONG");
    	int total1sum = correct1sum+wrong1sum;
    	double rate1sum = (double)(correct1sum)/(double)(total1sum);
    	int correct2sum = this.task2AnswerCount.get("sum").get("M2_CORRECT");
    	int wrong2sum = this.task2AnswerCount.get("sum").get("M2_WRONG");
    	int total2sum = correct2sum+wrong2sum;
    	double rate2sum = (double)(correct2sum)/(double)(total2sum);
    	result += ("Total & "+correct0sum+" & "+wrong0sum+" & "+total0sum+" & "+String.format("%.3g",rate0sum)+" & "+correct1sum+" & "+wrong1sum+" & "+total1sum+" & "+String.format("%.3g",rate1sum)+" & "+correct2sum+" & "+wrong2sum+" & "+total2sum+" & "+String.format("%.3g",rate2sum)+"\\\\ \n");
    	result +="\\hline\n";
	    result +="\\end{tabular}\n";
	    result +="\\caption{Correct rate of Task 2.}\n";
	    result +="\\end{table*}\n";
		return result;
	}
	
	
	public String task1Time2LatexTable(){
		String result = new String();
		result +="\\begin{table*}[t]\n";
		result +="\\centering";
		result +="\\begin{tabular}{|*{4}{c|}}\n";
		result +="\\hline\n";
		result += "No & Original Code & Naive Approach & Final Approach \\\\ \n";
		result +="\\hline\n";
		int i = 0;
		for(String index:task1){
	    	int t0 = this.task1ReactTime.get(index).get("M0");
	    	int t1 = this.task1ReactTime.get(index).get("M1");
	    	int t2 = this.task1ReactTime.get(index).get("M2");
	    	result += (""+(++i)+" & "+t0+" & "+t1+" & "+t2+"\\\\ \n");
	    }
	    result +="\\hline\n";
	    int t0sum = this.task1ReactTime.get("sum").get("M0");
    	int t1sum = this.task1ReactTime.get("sum").get("M1");
    	int t2sum = this.task1ReactTime.get("sum").get("M2");
    	result += ("Average"+" & "+t0sum+" & "+t1sum+" & "+t2sum+"\\\\ \n");result +="\\hline\n";
	    result +="\\end{tabular}\n";
	    result +="\\caption{Time of completion for Task 1.}\n";
	    result +="\\end{table*}\n";
		return result;
	}
	
	
	public String task2Time2LatexTable(){
		String result = new String();
		result +="\\begin{table*}[t]\n";
		result +="\\centering";
		result +="\\begin{tabular}{|*{4}{c|}}\n";
		result +="\\hline\n";
		result += "No & Original Code & Naive Approach & Final Approach \\\\ \n";
		result +="\\hline\n";
		int i = 0;
		for(String index:task2){
	    	int t0 = this.task2ReactTime.get(index).get("M0");
	    	int t1 = this.task2ReactTime.get(index).get("M1");
	    	int t2 = this.task2ReactTime.get(index).get("M2");
	    	result += (""+(++i)+" & "+t0+" & "+t1+" & "+t2+"\\\\ \n");
	    }
	    result +="\\hline\n";
	    int t0sum = this.task2ReactTime.get("sum").get("M0");
    	int t1sum = this.task2ReactTime.get("sum").get("M1");
    	int t2sum = this.task2ReactTime.get("sum").get("M2");
    	result += ("Average"+" & "+t0sum+" & "+t1sum+" & "+t2sum+"\\\\ \n");result +="\\hline\n";
	    result +="\\end{tabular}\n";
	    result +="\\caption{Time of completion for Task 2.}\n";
	    result +="\\end{table*}\n";
		return result;
	}
	
	
	public String expertiseTOString(){
		String result = "";
		if(this.expert.get("Expert")!=0){
			result += ((""+this.expert.get("Expert"))+" evaluate themselves as expert, ");
		}
		if(this.expert.get("Proficient")!=0){
			result += ((""+this.expert.get("Proficient"))+" evaluate themselves as proficient, ");
		}
		if(this.expert.get("Competent")!=0){
			result += ((""+this.expert.get("Competent"))+" evaluate themselves as competent, ");
		}
		if(this.expert.get("Advanced Beginner")!=0){
			result += ((""+this.expert.get("Advanced Beginner"))+" evaluate themselves as advanced Beginner, ");
		}
		if(this.expert.get("Novice")!=0){
			result += ((""+this.expert.get("Novice"))+" evaluate themselves as novice,");
		}
		return result;
	}
	
	public String survey2LatexTable() throws Exception{
		//double totol = (double)this.raw.size();
		String result = new String();
		result +="\\begin{table*}[t]\n";
		result +="\\centering\n";
		result +="\\begin{tabular}{|*{3}{p{3.7cm} | p{1.1cm} |}}\n";
		result +="\\hline\n";
		for(int i=0;i<5;i++){
			String questionKey = "suveryQ"+i;
			result += ("\\multicolumn{6}{ |l| }{"+this.surveyQuestion.get(questionKey).get("title")+"} \\\\ \n");
			result +="\\hline\n";
			int count = this.surveyResult.get("suveryQ"+i).keySet().size();
			if(count==3){
				for(int j = 0;j<3;j++){
					String option = "suveryQ"+i+"Option"+j;
					result += (this.surveyQuestion.get(questionKey).get(option)+" & "+this.surveyResult.get(questionKey).get(option)+(j==2?"":" & "));
					count++;
				}
			}
			else if(count==2){
				for(int j = 0;j<2;j++){
					String option = "suveryQ"+i+"Option"+j;
					result += ("\\multicolumn{2}{ |l| }{"+this.surveyQuestion.get(questionKey).get(option)+"} & "+this.surveyResult.get(questionKey).get(option)+(j==1?"":" & "));
				}
			}
			else{
				throw new Exception("Unexpected number of options: "+count);
			}
			result += "\\\\ \n";
			result +="\\hline\n";
		}
		result +="\\end{tabular}\n";
	    result +="\\caption{Survey of participants' feedback for Task 3.}\n";
	    result +="\\end{table*}\n";
		return result;
	}
	
	
	public static void main(String[] args) throws Exception{
		System.out.println("----------------------------------------------------------------");
		File directory = new File("userStudyStat/survey");
		SimpleStatistics_V1 stat = new SimpleStatistics_V1(directory);
		System.out.println(stat.expertiseTOString());
		System.out.println("----------------------------------------------------------------");
		System.out.println(stat.task1Count2LatexTable());
		System.out.println();
		//System.out.println("----------------------------------------------------------------");
		System.out.println(stat.task1Time2LatexTable());
		System.out.println();
		//System.out.println("----------------------------------------------------------------");
		System.out.println(stat.task2Count2LatexTable());
		System.out.println();
		//System.out.println("----------------------------------------------------------------");
		System.out.println(stat.task2Time2LatexTable());
		System.out.println();
		//System.out.println("----------------------------------------------------------------");
		System.out.println(stat.survey2LatexTable());
		System.out.println("----------------------------------------------------------------");
	}
}
