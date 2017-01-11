package learning;

import java.text.DecimalFormat;
import java.util.Map;

import statementGraph.graphNode.StatementWrapper;

public class LearningHelper {
	static String outputDoubleArray2String(double[] array){
		String result = new String();
		for(int i=0; i<array.length;i++){
			result += array[i];
			if(i!=array.length-1){
				result += " "; 
			}
		}
		return result;
	}
	
	
	static String hashKeyDoubleArray2String(double[] array){
		String result = new String();
		for(int i=0; i<array.length;i++){
			result += (new DecimalFormat("#0.0").format(array[i]));
			if(i!=array.length-1){
				result += "_"; 
			}
		}
		return result;
	}
	
	
	static String typeWeightMap2String(Map<Integer,Integer> map, double [] para){
		String result = new String();
		for(Integer type : map.keySet()){
			result += (StatementWrapper.statementTypeInt2String(type)+": "+para[map.get(type)]+"\n");
		}
		return result;
	}	
}
