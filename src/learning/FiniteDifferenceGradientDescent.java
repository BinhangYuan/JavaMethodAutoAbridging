package learning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV0;
import statementGraph.ASTParserUtils;
import statementGraph.ConstraintAndFeatureEncoder;
import statementGraph.graphNode.StatementWrapper;

public class FiniteDifferenceGradientDescent {
	private double [] parameters;
	private Map<Integer,Integer> typeMap;
	private double stepLength = 1;
	private double epsilon = 0.1;
	private JaccardDistance computeDistance = new JaccardDistance();
	private Logger trainlogger = Logger.getLogger("learning");
	private LinkedList<Double> trainingCostRecord;
	
	private Map<LearningBinaryIPSolverV0,ManualLabel> trainingSet = new HashMap<LearningBinaryIPSolverV0,ManualLabel>();
	private int maxIterations = 100;
	private double threshold = 0.0000001;
	
	void setStepLength(double step){
		this.stepLength = step;
	}
	
	private double computeCost(double [] paras){
		double cost = 0;
		double n = (double)this.trainingSet.keySet().size();
		for(LearningBinaryIPSolverV0 solver: this.trainingSet.keySet()){
			solver.setParameters(paras);
			cost += this.computeDistance.distanceBetweenSets(solver.solve(),this.trainingSet.get(solver).getBooleanLabels());
		}
		return cost/n;
	}	
	
	public void initTraining(String labelPath) throws Exception{
		//Create record for training cost;
		this.trainingCostRecord = new LinkedList<Double>();
		
		//Set up logger:
		FileHandler handler = new FileHandler("log/trainLog"+System.currentTimeMillis()+".log", false);
		this.trainlogger.addHandler(handler);
		
		this.trainlogger.info("Initialize training");
		String  labelString = ASTParserUtils.readFileToString(labelPath);
		JSONObject obj = new JSONObject(labelString);
		
		JSONArray dataArray = obj.getJSONArray("data");
		//Split the data set into training set and test set. 
		List<Integer> shuffleArray = new ArrayList<Integer>();
		for(int i=0; i< dataArray.length(); i++){
			shuffleArray.add(i);
		}
		Collections.shuffle(shuffleArray);
		
		this.trainlogger.info("Shuffling data set is done");
		System.out.println("Shuffle data set:");
		for(Integer i: shuffleArray){
			System.out.print(i+" ");
		}
		System.out.println();
	 
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
			int lineCount = dataArray.getJSONObject(index).getInt("lineCount");
			
			ConstraintAndFeatureEncoder encoder = ASTParserUtils.parseMethod(true,filePath, fileName,methodName,pos,label);
			
			LearningBinaryIPSolverV0 solver = new LearningBinaryIPSolverV0();
			solver.setDependenceConstraints(encoder.getASTConstraints(), encoder.getCFGConstraints(), encoder.getDDGConstraints());
			solver.setLineCostConstraints(encoder.getLineCounts());
			solver.setTypeMap(this.typeMap);
			solver.setStatementType(encoder.getStatementType());
			
			solver.setTargetLineCount(lineCount);
			ManualLabel mlabel = new ManualLabel(lineCount,label);
			this.trainingSet.put(solver,mlabel);
		}
		this.trainlogger.info("Loading data set and parsing data set are done");
	}
	
	
	public void training(){
		this.trainlogger.info("Training start");
		int stopLength = 3;
		boolean[] stops = new boolean[stopLength]; 
		for(int i = 0; i < this.maxIterations; i++){
			double precost = this.computeCost(this.parameters);
			this.trainingCostRecord.add(precost);
			System.out.println("Training process: Iteration "+i +" with cost value: "+ precost);
			double [] derivative = new double[this.parameters.length];
			
			for(int j = 0; j < this.parameters.length; j++ ){
				double [] tempPara = new double[this.parameters.length];
				System.arraycopy(this.parameters, 0, tempPara, 0, this.parameters.length);
				tempPara[j] += this.epsilon; 
				double updateCost = this.computeCost(tempPara);
				double partialDerivative_j = (updateCost-precost)/this.epsilon;
				derivative[j] = partialDerivative_j;
			}
			
			//update parameters
			for(int j = 0; j < this.parameters.length; j++){
				this.parameters[j] -= (this.stepLength*derivative[j]);
			}
			
			double postcost = this.computeCost(this.parameters);
			
			this.trainlogger.info("Training process: Iteration "+i +" with pre cost value: "+ precost+ " post cost value: "+postcost);
			this.trainlogger.info("Para:"+LearningHelper.outputDoubleArray2String(this.parameters));
			/*
			if(Math.abs(precost-postcost) < this.threshold){
				break;
			}*/
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
	}
	
	public FiniteDifferenceGradientDescent(){
		this.parameters = new double[StatementWrapper.statementsLabelSet.size()];
		Arrays.fill(this.parameters,1.0);
		this.parameters[1] = 2;
		this.typeMap = new HashMap<Integer,Integer>();
		int i = 0;
		for(Integer label: StatementWrapper.statementsLabelSet){
			typeMap.put(label, i++);
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		FiniteDifferenceGradientDescent model = new FiniteDifferenceGradientDescent();
		model.initTraining("src/learning/labeling/labels.json");
		model.training();
	}
}
