package learning.v6;

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

import ilpSolver.LearningBinaryIPSolverV6;
import learning.JaccardDistance;
import learning.ManualLabel;
import statementGraph.ASTParserUtils;
import statementGraph.constraintAndFeatureEncoder.ConstraintAndFeatureEncoderV6;
import statementGraph.graphNode.StatementWrapper;

public abstract class AbstractOptimizerV6 {
	static private boolean shuffleFlag = false;
	protected double [] parameters;
	final protected Map<Integer,Integer> typeMap;
	final protected Map<Integer,Integer> parentTypeMap;
	protected JaccardDistance computeDistance = new JaccardDistance();
	protected Map<LearningBinaryIPSolverV6,ManualLabel> trainingSet = new HashMap<LearningBinaryIPSolverV6,ManualLabel>();
	protected LinkedList<LearningBinaryIPSolverV6> trainingSolverArray = new LinkedList<LearningBinaryIPSolverV6>();
	protected Map<LearningBinaryIPSolverV6,ManualLabel> validateSet = new HashMap<LearningBinaryIPSolverV6,ManualLabel>();
	protected LinkedList<LearningBinaryIPSolverV6> validateSolverArray = new LinkedList<LearningBinaryIPSolverV6>(); 
	
	
	public void setParameters(double[] para){
		Assert.isTrue(para.length==this.parameters.length);
		this.parameters = para;
	}
	
	
	public double[] getParameters(){
		return this.parameters;
	}
	
	
	protected AbstractOptimizerV6(){
		this.typeMap = StatementWrapper.typeMap;
		this.parentTypeMap = StatementWrapper.parentTypeMap;
	}
	
	
	
	
	
	public void initTraining(String labelPath) throws Exception{
		String  labelString = ASTParserUtils.readFileToString(labelPath);
		JSONObject obj = new JSONObject(labelString);
		
		JSONArray dataArray = obj.getJSONArray("data");
		
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
			
			ConstraintAndFeatureEncoderV6 encoder = ASTParserUtils.parseMethodV6(false,filePath, fileName,methodName,pos,label);
			LearningBinaryIPSolverV6 solver = new LearningBinaryIPSolverV6(encoder);
			solver.setDependenceConstraints(encoder.getASTConstraints(), encoder.getCFGConstraints(), encoder.getDDGConstraints());
			solver.setLineCostConstraints(encoder.getLineCounts());
			solver.setTypeMap(this.typeMap);
			solver.setParentTypeMap(this.parentTypeMap);
			solver.setStatementType(encoder.getStatementType());
			solver.setParentStatementType(encoder.getParentStatementType());
			solver.setNestedLevels(encoder.getNestedLevel());
			solver.setReferencedVariableCounts(encoder.getReferencedVariableCounts());
			int lineCount = solver.programLineCount(solver.outputLabeledResult(label));
			solver.setTargetLineCount(lineCount);
			ManualLabel mlabel = new ManualLabel(lineCount,label);
			this.trainingSolverArray.add(solver);
			this.trainingSet.put(solver,mlabel);
		}
	}
	
	
	abstract public void training() throws IOException;
	
	abstract public void outputTrainingResult() throws IOException;
}
