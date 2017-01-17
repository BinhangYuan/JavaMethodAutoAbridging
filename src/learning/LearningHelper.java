package learning;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Map;
import org.json.JSONArray;

import statementGraph.graphNode.StatementWrapper;

public class LearningHelper {
	public static String outputDoubleArray2String(double[] array){
		String result = new String();
		for(int i=0; i<array.length;i++){
			result += array[i];
			if(i!=array.length-1){
				result += " "; 
			}
		}
		return result;
	}
	
	
	public static String hashKeyDoubleArray2String(double[] array){
		String result = new String();
		for(int i=0; i<array.length;i++){
			result += (new DecimalFormat("#0.0").format(array[i]));
			if(i!=array.length-1){
				result += "_"; 
			}
		}
		return result;
	}
	
	
	public static String typeWeightMap2String(Map<Integer,Integer> map, double [] para){
		String result = new String();
		for(Integer type : map.keySet()){
			result += (StatementWrapper.statementTypeInt2String(type)+": "+para[map.get(type)]+"\n");
		}
		return result;
	}
	
	
	public static String parentTypeWeightMap2String(Map<Integer,Integer> map, double [] para){
		String result = new String();
		for(Integer parentNodeType : map.keySet()){
			result += (StatementWrapper.parentStatementTypeInt2String(parentNodeType)+": "+para[StatementWrapper.statementsLabelSet.size()+map.get(parentNodeType)]+"\n");
		}
		return result;
	}
	
	
	public static JSONArray outputTrainingCost2JSONArray(LinkedList<Double> costs){
		JSONArray line = new JSONArray();
		for(Double value:costs){
			line.put(value);
		}
		return line;
	}
}
