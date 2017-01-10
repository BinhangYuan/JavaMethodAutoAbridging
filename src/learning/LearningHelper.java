package learning;

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
}
