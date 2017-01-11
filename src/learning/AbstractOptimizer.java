package learning;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.json.JSONArray;
import org.json.JSONObject;

import ilpSolver.LearningBinaryIPSolverV0;
import statementGraph.ASTParserUtils;
import statementGraph.ConstraintAndFeatureEncoder;
import statementGraph.graphNode.StatementWrapper;

public abstract class AbstractOptimizer {
	protected double [] parameters;
	protected Map<Integer,Integer> typeMap;
	protected JaccardDistance computeDistance = new JaccardDistance();
	protected Map<LearningBinaryIPSolverV0,ManualLabel> trainingSet = new HashMap<LearningBinaryIPSolverV0,ManualLabel>();

	abstract protected double objectiveFunction(double [] paras);
	
	
	public void setParameters(double[] para){
		Assert.isTrue(para.length==this.parameters.length);
		this.parameters = para;
	}
	
	
	public double[] getParameters(){
		return this.parameters;
	}
	
	
	protected AbstractOptimizer(){
		this.typeMap = new HashMap<Integer,Integer>();
		int i = 0;
		for(Integer label: StatementWrapper.statementsLabelSet){
			typeMap.put(label, i++);
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
		Collections.shuffle(shuffleArray);
		
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
			
			LearningBinaryIPSolverV0 solver = new LearningBinaryIPSolverV0(encoder);
			solver.setDependenceConstraints(encoder.getASTConstraints(), encoder.getCFGConstraints(), encoder.getDDGConstraints());
			solver.setLineCostConstraints(encoder.getLineCounts());
			solver.setTypeMap(this.typeMap);
			solver.setStatementType(encoder.getStatementType());
			
			solver.setTargetLineCount(lineCount);
			ManualLabel mlabel = new ManualLabel(lineCount,label);
			this.trainingSet.put(solver,mlabel);
		}
	}
	
	
	abstract public void training() throws IOException;
	
	abstract public void outputTrainingResult() throws IOException;
}
