package learning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;

import ilpSolver.LearningBinaryIPSolverV2;
import statementGraph.graphNode.StatementWrapper;
import textExtractor.TextUtils;

public class NaiveBayesTextClassifier {
	private Map<LearningBinaryIPSolverV2,ManualLabel> trainingSet;
	
	private Set<String> vocabulary;
	private Map<String,Integer> positiveWordCounts;
	private Map<String,Integer> negativeWordCounts;
	private int positiveItemTotal;
	private int negativeItemTotal;
	private int positiveWordTotal;
	private int negativeWordTotal;
	
	public NaiveBayesTextClassifier(Map<LearningBinaryIPSolverV2,ManualLabel> trainingSet) throws Exception{
		this.trainingSet = trainingSet;
		this.vocabulary = new HashSet<String>();
		this.positiveWordCounts = new HashMap<String,Integer>();
		this.negativeWordCounts = new HashMap<String,Integer>();
		this.positiveItemTotal = 0;
		this.negativeItemTotal = 0;
		this.positiveWordTotal = 0;
		this.negativeWordTotal = 0;
		this.LearnNaiveBayesText();
	}
	
	
	private void LearnNaiveBayesText() throws Exception{
		for(LearningBinaryIPSolverV2 solver: this.trainingSet.keySet()){
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
	
	public boolean predictNaiveBayesFlag(StatementWrapper item) throws Exception{
		return this.predictNaiveBayesPositive_Log(item)>this.predictNaiveBayesNegative_Log(item);
	}
}
