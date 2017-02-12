package learning.labeling;

import org.json.*;

import statementGraph.ASTParserUtils;

public class SourceCodeDataLoader {
	
	public static void displaySourceCode(String filePath, String fileName, String methodName, int pos, boolean [] manualLabel) throws Exception{
		System.out.println("Display source code data:");
		System.out.println(filePath+fileName+"-"+methodName);
		ASTParserUtils.parseMethodV5(true, filePath, fileName,methodName,pos,manualLabel);
	}
	
	
	public static void showDataSet() throws Exception{
		String  labelString = ASTParserUtils.readFileToString("src/learning/labeling/labels.json");
		JSONObject obj = new JSONObject(labelString);
		
		JSONArray dataArray = obj.getJSONArray("data");
		
		for(int i = 0; i < dataArray.length(); i++){
			String filePath = dataArray.getJSONObject(i).getString("file_path");
			String fileName = dataArray.getJSONObject(i).getString("file_name");
			String methodName = dataArray.getJSONObject(i).getString("method");
			int pos = dataArray.getJSONObject(i).getInt("pos");
			JSONArray labelJsonarray = dataArray.getJSONObject(i).getJSONArray("label");
			boolean [] labels = new boolean[labelJsonarray.length()];
			for(int j =0; j< labels.length; j++){
				labels[j] = labelJsonarray.getBoolean(j);
			}
			displaySourceCode(filePath,fileName,methodName,pos,labels);
		}
	}
	
	public static void showLastProgram() throws Exception{
		String  labelString = ASTParserUtils.readFileToString("src/learning/labeling/labels.json");
		JSONObject obj = new JSONObject(labelString);
		
		JSONArray dataArray = obj.getJSONArray("data");
		
		if(dataArray.length()>0){
			
			int i = dataArray.length()-1;
			System.out.println("No. "+i+ " program to label");
			String filePath = dataArray.getJSONObject(i).getString("file_path");
			String fileName = dataArray.getJSONObject(i).getString("file_name");
			String methodName = dataArray.getJSONObject(i).getString("method");
			int pos = dataArray.getJSONObject(i).getInt("pos");
			JSONArray labelJsonarray = dataArray.getJSONObject(i).getJSONArray("label");
			boolean [] labels = new boolean[labelJsonarray.length()];
			for(int j =0; j< labels.length; j++){
				labels[j] = labelJsonarray.getBoolean(j);
			}
			displaySourceCode(filePath,fileName,methodName,pos,labels);
		}
	}
	
	
	public static void showProgram(int index)throws Exception{
		String  labelString = ASTParserUtils.readFileToString("src/learning/labeling/labels.json");
		JSONObject obj = new JSONObject(labelString);
		
		JSONArray dataArray = obj.getJSONArray("data");
		
		if(dataArray.length()>index){
			System.out.println("No. "+index+ " program to label");
			String filePath = dataArray.getJSONObject(index).getString("file_path");
			String fileName = dataArray.getJSONObject(index).getString("file_name");
			String methodName = dataArray.getJSONObject(index).getString("method");
			int pos = dataArray.getJSONObject(index).getInt("pos");
			JSONArray labelJsonarray = dataArray.getJSONObject(index).getJSONArray("label");
			boolean [] labels = new boolean[labelJsonarray.length()];
			for(int j =0; j< labels.length; j++){
				labels[j] = labelJsonarray.getBoolean(j);
			}
			displaySourceCode(filePath,fileName,methodName,pos,labels);
		}
	}
	
	public static void main(String[] args) throws Exception{
		showProgram(40);
		//showLastProgram();
		//showDataSet();
	}
}
