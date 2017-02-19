package stubQuery;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.json.JSONArray;
import org.json.JSONObject;

import statementGraph.ASTParserUtils;

/*
 * Fake a query interface rather than link to PDB
 */

public class StubQuery {
	private static int filterVersion = 1;
	
	private ArrayList<File> targetDirectories;
	
	public void setTargetDirectories(ArrayList<File> dirs){
		this.targetDirectories = dirs;
	}
	
	private int lineLimits = 60;
	
	public void setLineLimits(int lines){
		this.lineLimits = lines;
	}
	
	private HashSet<String> keywords = new HashSet<String>();
	
	public void setKeywords(HashSet<String> keywords){
		this.keywords = keywords;
	}
	
	private HashMap<MethodDeclaration,JSONObject> tempBuffer = new HashMap<MethodDeclaration,JSONObject>();
	
	public void visit() throws Exception{
		for(final File dir: this.targetDirectories){
			this.visit(dir);
		}
	}
	
	private void visit(final File target) throws Exception{
		if(target.isDirectory()){
			for(final File entry: target.listFiles()){
				visit(entry);
			}
		}
		else{
			this.checkOneFile(target);
		}
	}
	
	private boolean filter0(MethodDeclaration method){
		if(method.getBody()==null){
			return false;
		}
		if(method.getBody().toString().split(System.getProperty("line.separator")).length < this.lineLimits){
			return false;
		}
		return true;
	}
	
	private boolean filter1(MethodDeclaration method){
		if(method.getBody()==null){
			return false;
		}
		if(method.getBody().toString().split(System.getProperty("line.separator")).length < this.lineLimits){
			return false;
		}
		if(method.getJavadoc()==null){//Does not have a description
			return false;
		}
		String doc = method.getJavadoc().toString();
		for(String keyword:this.keywords){
			if(!doc.toLowerCase().contains(keyword.toLowerCase())){
				return false;
			}
		}
		return true;
	}
	
	private HashMap<MethodDeclaration,JSONObject> parseStubQuery(String filePath) throws Exception {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		String str = ASTParserUtils.readFileToString(filePath);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setEnvironment(new String[]{filePath}, null, null, true);
		parser.setUnitName("Anything");
		parser.setResolveBindings(false);
							
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
							
		cu.accept(new ASTVisitor() {	
			public boolean visit(MethodDeclaration node){
				//SimpleName name = node.getName();
				//System.out.println("Vistor:: Method Declaration of: '"+name+ "' at line" +cu.getLineNumber(node.getStartPosition()));
				methods.add(node);
				return true;
			}
		});
		
		HashMap<MethodDeclaration,JSONObject> results = new HashMap<MethodDeclaration,JSONObject>();
		for(MethodDeclaration method: methods){
			if(filterVersion==0){
				if(this.filter0(method)){
					System.out.println(filePath+": "+method.getName().toString());
					System.out.println(method.toString());
					JSONObject info= new JSONObject();
					info.put("file_path", filePath.substring(filePath.indexOf("dataset")));
					info.put("pos", cu.getLineNumber(method.getStartPosition()));
					info.put("method", method.getName().toString());
					results.put(method,info);
				}
			}
			else if(filterVersion==1){
				if(this.filter1(method)){
					System.out.println(filePath+": "+method.getName().toString());
					System.out.println(method.toString());
					JSONObject info= new JSONObject();
					info.put("file_path", filePath.substring(filePath.indexOf("dataset")));
					info.put("pos", cu.getLineNumber(method.getStartPosition()));
					info.put("method", method.getName().toString());
					//info.put("doc", method.getJavadoc().toString());
					results.put(method,info);
				}
			}
		}
		return results;
	}
	
	
	private void checkOneFile(final File target) throws Exception{
		//System.out.println(target.getAbsolutePath()+":"+target.getName());
		this.tempBuffer.putAll(parseStubQuery(target.getAbsolutePath()));
	}
	
	public void outputResult() throws IOException{
		JSONObject obj = new JSONObject();
		JSONArray results = new JSONArray();
		for(MethodDeclaration method:this.tempBuffer.keySet()){
			results.put(this.tempBuffer.get(method));
		}
		obj.put("results", results);
		obj.put("filter", filterVersion);
		FileWriter resultFile = new FileWriter("src/stubQuery/result/result"+System.currentTimeMillis()+".json");
		obj.write(resultFile);
		resultFile.close();
	}
	
	public static void main(String[] args) throws Exception{
		String  labelString = ASTParserUtils.readFileToString("src/stubQuery/category.json");
		JSONObject obj = new JSONObject(labelString);
		JSONArray jarray = obj.getJSONArray("query");
		JSONObject query = jarray.getJSONObject(jarray.length()-1);
		JSONArray jdirs = query.getJSONArray("directories");
		ArrayList<File> dirs = new ArrayList<File>();
		for(int i = 0; i < jdirs.length(); i ++){
			dirs.add(new File(jdirs.getString(i)));
		}
		StubQuery stub = new StubQuery();
		HashSet<String> keywords = new HashSet<String>();
		//keywords.add("image");
		//keywords.add("tag");
		//keywords.add("cursor");
		//keywords.add("start");
		//keywords.add("url");
		//keywords.add("connection");
		//keywords.add("style");
		//keywords.add("process");
		//keywords.add("dir");
		//keywords.add("file");
		//keywords.add("throw");
		//keywords.add("subsequence");
		keywords.add("middle");
		stub.setKeywords(keywords);
		stub.setTargetDirectories(dirs);
		stub.setLineLimits(20);
		stub.visit();
		stub.outputResult();
	}
}
