package learning.v4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.json.JSONArray;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV4;
import learning.JaccardDistance;
import learning.ManualLabel;
import statementGraph.ASTParserUtils;
import statementGraph.constraintAndFeatureEncoder.ConstraintAndFeatureEncoderV4;
import statementGraph.graphNode.StatementWrapper;

public abstract class AbstractOptimizerV4 {
	static private boolean shuffleFlag = false;
	protected double [] parameters;
	protected Map<Integer,Integer> typeMap;
	protected Map<Integer,Integer> parentTypeMap;
	protected JaccardDistance computeDistance = new JaccardDistance();
	protected Map<LearningBinaryIPSolverV4,ManualLabel> trainingSet = new HashMap<LearningBinaryIPSolverV4,ManualLabel>();
	protected LinkedList<LearningBinaryIPSolverV4> solverArray = new LinkedList<LearningBinaryIPSolverV4>();
	protected Map<LearningBinaryIPSolverV4,ManualLabel> validateSet = new HashMap<LearningBinaryIPSolverV4,ManualLabel>();
	protected LinkedList<LearningBinaryIPSolverV4> validateSolverArray = new LinkedList<LearningBinaryIPSolverV4>(); 
	
	abstract protected double objectiveFunction(double [] paras);
	
	
	public void setParameters(double[] para){
		Assert.isTrue(para.length==this.parameters.length);
		this.parameters = para;
	}
	
	
	public double[] getParameters(){
		return this.parameters;
	}
	
	
	protected AbstractOptimizerV4(){
		this.typeMap = new HashMap<Integer,Integer>();
		int i = 0;
		for(Integer label: StatementWrapper.statementsLabelSet){
			this.typeMap.put(label, i++);
		}
		this.parentTypeMap = new HashMap<Integer,Integer>();
		i = 0;
		for(Integer label: StatementWrapper.parentStatementsLabelSet){
			this.parentTypeMap.put(label, i++);
		}
	}
	
	
	public void initTraining(String labelPath) throws Exception{
		String  labelString = ASTParserUtils.readFileToString(labelPath);
		JSONObject obj = new JSONObject(labelString);
		
		JSONArray dataArray = obj.getJSONArray("data");
		//Split the data set into training set and test set. 
		List<Integer> shuffleArray = new ArrayList<Integer>();
		for(int i=0; i< dataArray.length(); i++){
			shuffleArray.add(i);
		}
		
		if(shuffleFlag){
			Collections.shuffle(shuffleArray);
			System.out.println("Shuffle data set:");
			for(Integer i: shuffleArray){
				System.out.print(i+" ");
			}
			System.out.println();
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
			
			ConstraintAndFeatureEncoderV4 encoder = ASTParserUtils.parseMethodV4(true,filePath, fileName,methodName,pos,label);
			LearningBinaryIPSolverV4 solver = new LearningBinaryIPSolverV4(encoder);
			solver.setDependenceConstraints(encoder.getASTConstraints(), encoder.getCFGConstraints(), encoder.getDDGConstraints());
			solver.setLineCostConstraints(encoder.getLineCounts());
			solver.setTypeMap(this.typeMap);
			solver.setParentTypeMap(this.parentTypeMap);
			solver.setStatementType(encoder.getStatementType());
			solver.setParentStatementType(encoder.getParentStatementType());
			int lineCount = solver.programLineCount(solver.outputLabeledResult(label));
			solver.setTargetLineCount(lineCount);
			ManualLabel mlabel = new ManualLabel(lineCount,label);
			this.solverArray.add(solver);
			this.trainingSet.put(solver,mlabel);
		}
	}
	
	
	abstract public void training() throws IOException;
	
	abstract public void outputTrainingResult() throws IOException;
}
