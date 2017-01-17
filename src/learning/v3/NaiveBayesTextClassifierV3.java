package learning.v3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Assert;
import org.json.JSONArray;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV3;
import learning.ManualLabel;
import statementGraph.graphNode.StatementWrapper;
import textExtractor.TextUtils;

public class NaiveBayesTextClassifierV3 {
	private Map<LearningBinaryIPSolverV3,ManualLabel> trainingSet;
	private Logger trainlogger;
	
	private Set<String> vocabulary;
	private Map<String,Integer> positiveWordCounts;
	private Map<String,Integer> negativeWordCounts;
	private int positiveItemTotal;
	private int negativeItemTotal;
	private int positiveWordTotal;
	private int negativeWordTotal;
	
	
	public NaiveBayesTextClassifierV3(Map<LearningBinaryIPSolverV3,ManualLabel> trainingSet, Logger logger) throws Exception{
		this.trainingSet = trainingSet;
		this.trainlogger = logger;
		this.vocabulary = new HashSet<String>();
		this.positiveWordCounts = new HashMap<String,Integer>();
		this.negativeWordCounts = new HashMap<String,Integer>();
		this.positiveItemTotal = 0;
		this.negativeItemTotal = 0;
		this.positiveWordTotal = 0;
		this.negativeWordTotal = 0;
	}
	
	
	public void LearnNaiveBayesText() throws Exception{
		this.trainlogger.info("Start training text naive bayes");
		for(LearningBinaryIPSolverV3 solver: this.trainingSet.keySet()){
			List<StatementWrapper> statements = solver.getStatementWrapperList();
			boolean[] labels = trainingSet.get(solver).getBooleanLabels();
			Assert.isTrue(statements.size() == labels.length);
			for(int i=0; i<labels.length;i++){
				if(labels[i]){
					this.positiveItemTotal++;
					LinkedList<String> words = TextUtils.parseStatement(statements.get(i));
					for(String word:words){
						if(!this.vocabulary.contains(word)){
							this.vocabulary.add(word);
						}
						if(!this.positiveWordCounts.containsKey(word)){
							this.positiveWordCounts.put(word, 0);
						}
						this.positiveWordCounts.put(word, this.positiveWordCounts.get(word)+1);
						this.positiveWordTotal++;
					}
				}
				else{
					this.negativeItemTotal++;
					LinkedList<String> words = TextUtils.parseStatement(statements.get(i));
					for(String word:words){
						if(!this.vocabulary.contains(word)){
							this.vocabulary.add(word);
						}
						if(!this.negativeWordCounts.containsKey(word)){
							this.negativeWordCounts.put(word, 0);
						}
						this.negativeWordCounts.put(word, this.negativeWordCounts.get(word)+1);
						this.negativeWordTotal++;
					}
				}
			}
		}
		this.trainlogger.info("Training text naive bayes done");
		this.outputWordDistribution2JsonFile();
	}
	
	
	private double positiveWordContionalProbiblity(String word){
		if(this.positiveWordCounts.containsKey(word)){
			return (double)(1+this.positiveWordCounts.get(word))/(double)(this.positiveWordTotal+this.vocabulary.size());
		}
		else{
			return 1.0/(double)(this.positiveWordTotal+this.vocabulary.size());
		}
	}
	
	
	private double negativeWordContionalProbiblity(String word){
		if(this.negativeWordCounts.containsKey(word)){
			return (double)(1+this.negativeWordCounts.get(word))/(double)(this.negativeWordTotal+this.vocabulary.size());
		}
		else{
			return 1.0/(double)(this.negativeWordTotal+this.vocabulary.size());
		}
	}
	
	
	private double predictNaiveBayesPositive_Log(StatementWrapper item) throws Exception{
		double result = Math.log((double)this.positiveItemTotal/(double)(this.positiveItemTotal+this.negativeItemTotal));
		for(String word:TextUtils.parseStatement(item)){
			result += Math.log(this.positiveWordContionalProbiblity(word)); 
		}
		return result;
	}
	
	
	private double predictNaiveBayesNegative_Log(StatementWrapper item) throws Exception{
		double result = Math.log((double)this.negativeItemTotal/(double)(this.positiveItemTotal+this.negativeItemTotal));
		for(String word:TextUtils.parseStatement(item)){
			result += Math.log(this.negativeWordContionalProbiblity(word)); 
		}
		return result;
	}
	
	private boolean predictNaiveBayesStatementFlag(StatementWrapper item) throws Exception{
		return this.predictNaiveBayesPositive_Log(item)>this.predictNaiveBayesNegative_Log(item);
	}
	
	
	public void predictNaiveBayesText() throws Exception{
		this.trainlogger.info("Start predicting");
		int truePositive = 0;
		int trueNegative = 0;
		int falsePositive = 0;
		int falseNegative = 0;
		for(LearningBinaryIPSolverV3 solver: this.trainingSet.keySet()){
			List<StatementWrapper> statements = solver.getStatementWrapperList();
			boolean[] labels = trainingSet.get(solver).getBooleanLabels();
			Assert.isTrue(statements.size() == labels.length);
			List<Boolean> predicts = new LinkedList<Boolean>();
			for(int i = 0; i<statements.size();i++){
				StatementWrapper item = statements.get(i);
				boolean predict = this.predictNaiveBayesStatementFlag(item);
				predicts.add(predict);
				if(labels[i] && predict){
					truePositive++;
				}
				else if(labels[i] && !predict){
					falseNegative++;
				}
				else if(!labels[i] && predict){
					falsePositive++;
				}
				else{
					trueNegative++;
				}
			}
			solver.setTextClassifierResults(predicts);
		}
		String statics = "TP:"+truePositive+" TN:"+trueNegative+" FP:"+falsePositive+" FN:"+falseNegative; 
		this.trainlogger.info(statics);
	}
	
	
	public void outputWordDistribution2JsonFile() throws IOException{
		FileWriter file = new FileWriter("log/NaiveBayesTextWordDistribution.json");
		JSONObject obj = new JSONObject();
		JSONArray positive = new JSONArray();
		for(String word:this.positiveWordCounts.keySet()){
			JSONObject temp = new JSONObject();
			temp.put("word",word);
			temp.put("count", this.positiveWordCounts.get(word));
			positive.put(temp);
		}
		obj.put("positive", positive);
		
		JSONArray negative = new JSONArray();
		for(String word:this.negativeWordCounts.keySet()){
			JSONObject temp = new JSONObject();
			temp.put("word",word);
			temp.put("count", this.negativeWordCounts.get(word));
			negative.put(temp);
		}
		obj.put("negative", negative);
		
		obj.write(file);
		file.close();
	}
}
