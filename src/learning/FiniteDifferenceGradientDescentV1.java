package learning;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV1;
import statementGraph.graphNode.StatementWrapper;

public class FiniteDifferenceGradientDescentV1 extends AbstractOptimizer{
	private double stepLength = 0.1;
	private double epsilon = 0.01;
	private int maxIterations = 100;
	private double threshold = 0.0000001;
	
	private Logger trainlogger = Logger.getLogger("learning.FiniteDifferenceGradientDescent");
	private LinkedList<Double> trainingCostRecord;
	
	
	public FiniteDifferenceGradientDescentV1(){
		super();
		this.parameters = new double[StatementWrapper.statementsLabelSet.size()];
		Arrays.fill(this.parameters,1.0);
	}
	
	
	@Override
	protected double objectiveFunction(double [] paras){
		double cost = 0;
		double n = (double)this.trainingSet.keySet().size();
		for(LearningBinaryIPSolverV1 solver: this.trainingSet.keySet()){
			solver.setParameters(paras);
			cost += this.computeDistance.distanceBetweenSets(solver.solve(),this.trainingSet.get(solver).getBooleanLabels());
		}
		return cost/n;
	}	
	
	
	@Override
	public void initTraining(String labelPath) throws Exception{
		//Create record for training cost;
		this.trainingCostRecord = new LinkedList<Double>();
		//Set up logger:
		FileHandler handler = new FileHandler("log/FDGD_TrainLog"+System.currentTimeMillis()+".log", false);
		this.trainlogger.addHandler(handler);
		
		this.trainlogger.info("Initialize training");
		super.initTraining(labelPath);
		this.trainlogger.info("Loading data set and parsing data set are done");
	}
	
	
	@Override
	public void training(){
		this.trainlogger.info("Training start");
		int stopLength = 3;
		boolean[] stops = new boolean[stopLength]; 
		for(int i = 0; i < this.maxIterations; i++){
			double precost = this.objectiveFunction(this.parameters);
			this.trainingCostRecord.add(precost);
			System.out.println("Training process: Iteration "+i +" with cost value: "+ precost);
			double [] derivative = new double[this.parameters.length];
			
			for(int j = 0; j < this.parameters.length; j++ ){
				double [] tempPara = new double[this.parameters.length];
				System.arraycopy(this.parameters, 0, tempPara, 0, this.parameters.length);
				tempPara[j] += this.epsilon; 
				double updateCost = this.objectiveFunction(tempPara);
				double partialDerivative_j = (updateCost-precost)/this.epsilon;
				derivative[j] = partialDerivative_j;
			}
			
			//update parameters
			for(int j = 0; j < this.parameters.length; j++){
				this.parameters[j] -= (this.stepLength*derivative[j]);
			}
			
			double postcost = this.objectiveFunction(this.parameters);
			
			this.trainlogger.info("Training process: Iteration "+i +" with pre cost value: "+ precost+ " post cost value: "+postcost);
			this.trainlogger.info("Para:"+LearningHelper.outputDoubleArray2String(this.parameters));
			//Stop if 3 continuous iterations do not change;
			if(i<3){
				stops[i] = (precost - postcost < this.threshold);
			}
			else{
				for(int j=0; j<stopLength-1; j++){
					stops[j] = stops[j+1];
				}
				stops[stopLength-1] =  (precost - postcost < this.threshold); 
			}
			boolean stopHere = true;
			for(int j=0; j<stopLength; j++){
				stopHere = stopHere && stops[j];
				if(!stopHere){
					break;
				}
			}
			if(stopHere){
				break;
			}
		}
		this.trainlogger.info("Lowest loss parameters:\n" + LearningHelper.typeWeightMap2String(this.typeMap, this.parameters));
	}
	
	
	@Override
	public void outputTrainingResult() throws IOException{
		JSONArray result = new JSONArray();
		for(LearningBinaryIPSolverV1 solver:this.trainingSet.keySet()){
			JSONObject current = new JSONObject();
			current.put("Origin", solver.originalProgram2String());
			current.put("manual", solver.outputLabeledResult(this.trainingSet.get(solver).getBooleanLabels()));
			current.put("Automatic", solver.outputSolveResult());
			result.put(current);
		}
		JSONObject obj = new JSONObject();
		FileWriter file = new FileWriter("src/learning/labeling/result"+System.currentTimeMillis()+".json");
		obj.put("result", result);
		obj.write(file);
		file.close();
	}
	
	
	
	
	public static void main(String[] args) throws Exception {
		FiniteDifferenceGradientDescentV1 model = new FiniteDifferenceGradientDescentV1();
		model.initTraining("src/learning/labeling/labels.json");
		model.training();
	}
}
