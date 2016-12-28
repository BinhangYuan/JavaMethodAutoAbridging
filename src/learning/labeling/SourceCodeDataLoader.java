package learning.labeling;

import java.io.IOException;

import org.json.*;

import statementGraph.ASTParserUtils;

public class SourceCodeDataLoader {
	
	public static void displaySourceCode(String filePath, String fileName, String methodName, int pos, boolean [] manualLabel) throws IOException{
		System.out.println("Display source code data:");
		System.out.println(filePath+"-"+methodName);
		ASTParserUtils.parseMethod(filePath, fileName,methodName,pos,manualLabel);
	}
	
	
	public static void main(String[] args) throws IOException{
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
}
