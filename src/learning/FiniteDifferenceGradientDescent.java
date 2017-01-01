package learning;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.json.JSONArray;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV0;
import statementGraph.ASTParserUtils;
import statementGraph.CFG;
import statementGraph.ConstraintAndFeatureEncoder;
import statementGraph.DDG;
import statementGraph.SimplifiedAST;
import statementGraph.graphNode.StatementWrapper;

public class FiniteDifferenceGradientDescent {
	private double [] parameters;
	private Map<Integer,Integer> typeMap;
	private double stepLength = 0.1;
	private double epsilon = 0.1;
	private JaccardDistance computeDistance = new JaccardDistance();
	private Logger trainlogger = Logger.getLogger("learning");

	
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
		for(int i = 0; i < this.maxIterations; i++){
			double precost = this.computeCost(this.parameters);
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
				this.parameters[j] += (this.stepLength*derivative[j]);
			}
			
			double postcost = this.computeCost(this.parameters);
			
			this.trainlogger.info("Training process: Iteration "+i +" with pre cost value: "+ precost+ " post cost value: "+postcost);
			if(Math.abs(precost-postcost) < this.threshold){
				break;
			}
		}
	}
	
	public FiniteDifferenceGradientDescent(){
		this.parameters = new double[StatementWrapper.statementsLabelSet.size()];
		Arrays.fill(this.parameters,1.0);
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
