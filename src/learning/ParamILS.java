/*
 * This file refers to the method introduced in http://www.cs.ubc.ca/labs/beta/Projects/ParamILS/algorithms.html
 * Some implementation refers to their ruby source code.
 */
package learning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Assert;

import ilpSolver.LearningBinaryIPSolverV0;
import statementGraph.graphNode.StatementWrapper;

public class ParamILS extends AbstractOptimizer{
	static double[] candidate = {1.0,2.0,3.0,4.0,5.0,6.0};
	private Random randGenerate = new Random();
	
	private HashMap<String,Double> visitedCandidates = new HashMap<String,Double>();
	private int iterations = 0;
	private int maxIterations = 1000;
	private int paraLength;
	private int paraR = 10;
	private int paraS = 3;
	private double restartProb = 0.01;
	private String bestStateHash = null;
	
	private Logger trainlogger = Logger.getLogger("learning");
	private LinkedList<Double> trainingCostRecordIterations;
	private LinkedList<Double> trainingCostRecordMin;
	
	
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
		for(LearningBinaryIPSolverV0 solver: this.trainingSet.keySet()){
			Assert.isNotNull(paras);
			solver.setParameters(paras);
			cost += this.computeDistance.distanceBetweenSets(solver.solve(),this.trainingSet.get(solver).getBooleanLabels());
		}
		return cost/n;
	}
	
	
	public ParamILS(){
		super();
		this.paraLength = StatementWrapper.statementsLabelSet.size();
	}
	
	
	private boolean visit(double[] state){
		String stateHash = LearningHelper.HashKeyDoubleArray2String(state);
		if(this.visitedCandidates.containsKey(stateHash)){
			return false;
		}
		else{
			double cost = this.objectiveFunction(state);
			this.visitedCandidates.put(stateHash, cost);
			
			this.trainingCostRecordIterations.add(cost);
			this.trainlogger.info("Training process: Iteration "+this.iterations +" with cost value: "+ cost);
			this.trainlogger.info("Para:"+LearningHelper.outputDoubleArray2String(state));
			
			this.iterations++;
			if(this.bestStateHash==null || cost < this.visitedCandidates.get(this.bestStateHash)){
				this.bestStateHash = stateHash;
			}
			this.trainingCostRecordMin.add(this.visitedCandidates.get(this.bestStateHash));
			return true;
		}
	}
	
	
	private boolean isBetter(double[] state1, double[] state2){
		Assert.isTrue(this.visitedCandidates.containsKey(LearningHelper.HashKeyDoubleArray2String(state1)));
		double cost1 = this.visitedCandidates.get(LearningHelper.HashKeyDoubleArray2String(state1));
		Assert.isTrue(this.visitedCandidates.containsKey(LearningHelper.HashKeyDoubleArray2String(state2)));
		double cost2 = this.visitedCandidates.get(LearningHelper.HashKeyDoubleArray2String(state2));
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
					if(!this.visitedCandidates.containsKey(LearningHelper.HashKeyDoubleArray2String(tempNeighbour))){
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
	public void training() {
		double [] initState = this.randomState();
		this.iteratedLocalSearch(initState);
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
	}
	
	
	
	public static void main(String[] args) throws Exception {
		ParamILS model = new ParamILS();
		model.initTraining("src/learning/labeling/labels.json");
		model.training();
		System.out.println("Lowest loss function value:"+model.getLowestObjectiveFunctionValue());
		System.out.println(model.getBestStateHash());
	}
}
