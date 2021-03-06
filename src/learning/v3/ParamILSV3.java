/*
 * This file refers to the method introduced in http://www.cs.ubc.ca/labs/beta/Projects/ParamILS/algorithms.html
 * Some implementation refers to their ruby source code.
 */
package learning.v3;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Assert;
import org.json.JSONArray;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV3;
import learning.LearningHelper;
import learning.ManualLabel;
import statementGraph.ASTParserUtils;
import statementGraph.constraintAndFeatureEncoder.ConstraintAndFeatureEncoderV3;
import statementGraph.graphNode.StatementWrapper;

public class ParamILSV3 extends AbstractOptimizerV3{
	static double[] candidate = {1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,11.0,12.0};
	private Random randGenerate = new Random();
	
	private HashMap<String,Double> visitedCandidates = new HashMap<String,Double>();
	private int iterations = 0;
	private int maxIterations = 100000;
	private int paraLength;
	private int paraR = 10;
	private int paraS = 3;
	private double restartProb = 0.01;
	private String bestStateHash = null;
	
	private Logger trainlogger = Logger.getLogger("learning.ParamILS");
	private LinkedList<Double> trainingCostRecordIterations;
	private LinkedList<Double> trainingCostRecordMin;
	private NaiveBayesTextClassifierV3 textClassifier;
	
	
	public String getBestStateHash(){
		return this.bestStateHash;
	}
	
	
	public double getLowestObjectiveFunctionValue(){
		return this.visitedCandidates.get(this.bestStateHash);
	}
	
	
	@Override
	protected double objectiveFunction(double [] paras){
		double cost = 0;
		double n = (double)this.trainingSet.keySet().size();
		for(LearningBinaryIPSolverV3 solver: this.trainingSet.keySet()){
			Assert.isNotNull(paras);
			solver.setParameters(paras);
			cost += this.computeDistance.distanceBetweenSets(solver.solve(),this.trainingSet.get(solver).getBooleanLabels());
		}
		return cost/n;
	}
	
	
	public ParamILSV3(){
		super();
		this.paraLength = StatementWrapper.statementsLabelSet.size()+ StatementWrapper.parentStatementsLabelSet.size() +1;
	}
	
	
	private boolean visit(double[] state){
		String stateHash = LearningHelper.hashKeyDoubleArray2String(state);
		if(this.visitedCandidates.containsKey(stateHash)){
			return false;
		}
		else{
			double cost = this.objectiveFunction(state);
			this.visitedCandidates.put(stateHash, cost);
			
			this.trainingCostRecordIterations.add(cost);
			this.trainlogger.info("Training process: Iteration "+this.iterations +" with cost value: "+ cost+" best cost so far:"+this.visitedCandidates.get(this.bestStateHash));
			this.trainlogger.info("Para:"+LearningHelper.outputDoubleArray2String(state));
			
			this.iterations++;
			if(this.bestStateHash==null || cost < this.visitedCandidates.get(this.bestStateHash)){
				this.bestStateHash = stateHash;
				this.parameters = state;
			}
			
			this.trainingCostRecordMin.add(this.visitedCandidates.get(this.bestStateHash));
			return true;
		}
	}
	
	
	private boolean isBetter(double[] state1, double[] state2){
		Assert.isTrue(this.visitedCandidates.containsKey(LearningHelper.hashKeyDoubleArray2String(state1)));
		double cost1 = this.visitedCandidates.get(LearningHelper.hashKeyDoubleArray2String(state1));
		Assert.isTrue(this.visitedCandidates.containsKey(LearningHelper.hashKeyDoubleArray2String(state2)));
		double cost2 = this.visitedCandidates.get(LearningHelper.hashKeyDoubleArray2String(state2));
		return cost1 < cost2;
	}
	
	
	private double[] randomState(){
		double[] state = new double [this.paraLength];
		for(int i=0;i<state.length;i++){
			state[i] = candidate[this.randGenerate.nextInt(candidate.length)];
		}
		return state;
	}
	
	
	private double[] perturbation(double[]state){
		double [] newState = new double[state.length];
		System.arraycopy(state, 0, newState, 0, newState.length);
		for(int i=0; i<this.paraS; i++){
			ArrayList<LinkedList<Double>> neighbours = this.getNeighbours(newState);
			if(neighbours.size()>0){
				int randomIndex = this.randGenerate.nextInt(neighbours.size());
				LinkedList<Double> neighbourList = neighbours.get(randomIndex);
				Assert.isTrue(neighbours.size()!=0);
				for(int j = 0; j < neighbourList.size(); j++){
					newState[j] = neighbourList.get(j);
				}
			}
		}
		return newState;
	}
	
	
	private ArrayList<LinkedList<Double>> getNeighbours(double [] currentState){
		ArrayList<LinkedList<Double>> neighbours = new ArrayList<LinkedList<Double>>();
		for(int i=0; i<currentState.length; i++){
			for(int j=0; j<candidate.length;j++){
				if(Math.abs(currentState[i]-candidate[j])>0.001){ //Not the current value;
					double[] tempNeighbour = new double[currentState.length];
					System.arraycopy(currentState, 0, tempNeighbour, 0, currentState.length);
					tempNeighbour[i] = candidate[j];
					if(!this.visitedCandidates.containsKey(LearningHelper.hashKeyDoubleArray2String(tempNeighbour))){
						LinkedList<Double> tempNeighbourList = new LinkedList<Double>();
						for(double value:tempNeighbour){
							tempNeighbourList.add(value);
						}
						neighbours.add(tempNeighbourList);
					}
				}
			}
		}
		return neighbours;
	}
	
	
	private boolean have2Stop(){
		if(this.iterations >= this.maxIterations){
			return true;
		}
		return false;
	}
	
	
	private void iteratedLocalSearch(double [] initState){
		double [] currentState = new double [initState.length];
		System.arraycopy(initState, 0, currentState, 0, currentState.length);
		visit(currentState);
		for(int i=0; i<this.paraR; i++){
			double [] tempState = this.randomState();
			visit(tempState);
			if(this.isBetter(tempState, currentState)){
				currentState = tempState;
			}
		}
		
		double [] ilsState = this.iterativeFirstImprovement(currentState);
		
		while(!this.have2Stop()){
			currentState = ilsState;
			currentState = this.perturbation(currentState);
			this.visit(currentState);
			currentState = this.iterativeFirstImprovement(currentState);
			//Restart
			if(Math.abs(this.restartProb) > 1e-6 && this.randGenerate.nextInt((int)(1.0/this.restartProb))==0){
				ilsState = this.randomState();
			}
		}
	}
	
	
	private double [] iterativeFirstImprovement(double [] startState){
		double [] currentState = new double [startState.length];
		System.arraycopy(startState, 0, currentState, 0, currentState.length);
		this.visit(currentState);
		
		boolean changed = true;
		while(changed && !this.have2Stop()){
			changed = false;
			ArrayList<LinkedList<Double>> neighbours = this.getNeighbours(currentState);
			while(neighbours.size() > 0 && !this.have2Stop()){
				int randomIndex = this.randGenerate.nextInt(neighbours.size());
				LinkedList<Double> neighbourList = neighbours.get(randomIndex);
				neighbours.remove(randomIndex);
				double[] neighbourState = new double [startState.length];
				for(int i = 0; i < neighbourState.length; i++){
					neighbourState[i] = neighbourList.get(i);
				}
				this.visit(neighbourState);
				if(this.isBetter(neighbourState, currentState)){
					currentState = neighbourState;
					changed = true;
					break;
				}
			}
		}
		return currentState;
	}

	
	@Override
	public void training() throws IOException {
		double [] initState = this.randomState();
		this.iteratedLocalSearch(initState);
		this.trainlogger.info("Lowest loss function value:" + this.getLowestObjectiveFunctionValue());
		String LowestParaLog = "";
		LowestParaLog += LearningHelper.typeWeightMap2String(this.typeMap, this.parameters);
		LowestParaLog += LearningHelper.parentTypeWeightMap2String(this.parentTypeMap, this.parameters);
		LowestParaLog += "text classifier weight:"+this.parameters[this.typeMap.size()+this.parentTypeMap.size()];
		this.trainlogger.info("Lowest loss parameters:\n" + LowestParaLog);	
		this.outputTrainingCost2JsonFile();
	}
	
	
	@Override
	public void initTraining(String labelPath) throws Exception{
		//Create record for training cost;
		this.trainingCostRecordIterations = new LinkedList<Double>();
		this.trainingCostRecordMin = new LinkedList<Double>();
		//Set up logger:
		FileHandler handler = new FileHandler("log/ParamILS_TrainLog"+System.currentTimeMillis()+".log", false);
		this.trainlogger.addHandler(handler);
		
		this.trainlogger.info("Initialize training");
		super.initTraining(labelPath);
		this.trainlogger.info("Loading data set and parsing data set are done");
		//Training naive Bayes text classifier
		this.trainlogger.info("Train naive Bayes text classifier");
		this.textClassifier = new NaiveBayesTextClassifierV3(this.trainingSet,this.trainlogger);
		this.textClassifier.LearnNaiveBayesText();
		this.textClassifier.predictNaiveBayesText();
		this.trainlogger.info("Train naive Bayes text classifier done.");
	}
	
	
	@Override
	public void outputTrainingResult() throws IOException{
		JSONArray result = new JSONArray();
		for(LearningBinaryIPSolverV3 solver:this.trainingSet.keySet()){
			JSONObject current = new JSONObject();
			current.put("Origin", solver.originalProgram2String());
			current.put("manual", solver.outputLabeledResult(this.trainingSet.get(solver).getBooleanLabels()));
			current.put("Automatic", solver.outputSolveResult());
			result.put(current);
		}
		JSONObject obj = new JSONObject();
		FileWriter file = new FileWriter("src/learning/labeling/result/result"+System.currentTimeMillis()+".json");
		obj.put("Iterations", this.maxIterations);
		obj.put("ObjectiveFunctionValue", this.getLowestObjectiveFunctionValue());
		obj.put("result", result);
		obj.write(file);
		file.close();
	}
	
	
	public void outputTrainingCost2JsonFile() throws IOException{
		FileWriter file = new FileWriter("log/ParaILSTrainingCurve.json");
		JSONObject obj = new JSONObject();
		obj.put("iterations", LearningHelper.outputTrainingCost2JSONArray(this.trainingCostRecordIterations));
		obj.put("min", LearningHelper.outputTrainingCost2JSONArray(this.trainingCostRecordMin));
		obj.write(file);
		file.close();
	}
	
	public void validateModel(String labelPath) throws Exception{
		double trainCost = objectiveFunction(this.parameters);
		
		String  labelString = ASTParserUtils.readFileToString(labelPath);
		JSONObject obj = new JSONObject(labelString);
		JSONArray dataArray = obj.getJSONArray("data");
		
		List<Integer> shuffleArray = new ArrayList<Integer>();
		for(int i=0; i< dataArray.length(); i++){
			shuffleArray.add(i);
		}
		
		for(int i = 0; i < dataArray.length(); i++) {
			int index = shuffleArray.get(i);
			String filePath = dataArray.getJSONObject(index).getString("file_path");
			String fileName = dataArray.getJSONObject(index).getString("file_name");
			String methodName = dataArray.getJSONObject(index).getString("method");
			int pos = dataArray.getJSONObject(index).getInt("pos");
			JSONArray labelJsonarray = dataArray.getJSONObject(index).getJSONArray("label");
			boolean [] label = new boolean[labelJsonarray.length()];
			for(int j =0; j< label.length; j++){
				label[j] = labelJsonarray.getBoolean(j);
			}
			
			ConstraintAndFeatureEncoderV3 encoder = ASTParserUtils.parseMethodV3(false,filePath, fileName,methodName,pos,label);
			LearningBinaryIPSolverV3 solver = new LearningBinaryIPSolverV3(encoder);
			solver.setDependenceConstraints(encoder.getASTConstraints(), encoder.getCFGConstraints(), encoder.getDDGConstraints());
			solver.setLineCostConstraints(encoder.getLineCounts());
			solver.setTypeMap(this.typeMap);
			solver.setParentTypeMap(this.parentTypeMap);
			solver.setStatementType(encoder.getStatementType());
			solver.setParentStatementType(encoder.getParentStatementType());
			//solver.setNestedLevels(encoder.getNestedLevel());
			//solver.setReferencedVariableCounts(encoder.getReferencedVariableCounts());
			int lineCount = solver.programLineCount(solver.outputLabeledResult(label));
			solver.setTargetLineCount(lineCount);
			ManualLabel mlabel = new ManualLabel(lineCount,label);
			this.validateSolverArray.add(solver);
			this.validateSet.put(solver,mlabel);
		}
		
		this.textClassifier.predictNaiveBayesTextOnTestSet(this.validateSet);
		
		double valCost = 0;
		double valN = (double)this.validateSet.keySet().size();
		for(LearningBinaryIPSolverV3 solver: this.validateSet.keySet()){
			solver.setParameters(this.parameters);
			valCost += this.computeDistance.distanceBetweenSets(solver.solve(),this.validateSet.get(solver).getBooleanLabels());
		}
		valCost = valCost/valN;
		
		System.out.println("Train model: distance: "+ trainCost);
		System.out.println("Validate model: distance: "+ valCost);
		System.out.println(this.bestStateHash);
		System.out.println(LearningHelper.hashKeyDoubleArray2String(this.parameters));
	}
	
	
	public static void main(String[] args) throws Exception {
		ParamILSV3 model = new ParamILSV3();
		model.initTraining("src/learning/labeling/labels.json");
		model.training();
		System.out.println("Lowest loss function value:"+model.getLowestObjectiveFunctionValue());
		System.out.println(model.getBestStateHash());
		model.outputTrainingResult();
		model.validateModel("src/learning/labeling/labels2.json");
	}
}
