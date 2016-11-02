package statementGraph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

public class ASTParserUtils {
	
	//use ASTParse to parse string
	public static void parse(String str) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
		
		cu.accept(new ASTVisitor() {
			
			public boolean visit(MethodDeclaration node){
				SimpleName name = node.getName();
				System.out.println("Vistor:: Method Declaration of: '"+name+ "' at line" +cu.getLineNumber(node.getStartPosition()));
				methods.add(node);
				return true;
			}
		});
		
		System.out.println("All Methods");
		for(MethodDeclaration node: methods){
			System.out.println("Method Declaration of: '"+node.getName()+ "' at line" +cu.getLineNumber(node.getStartPosition()));
			System.out.println(node.toString());
			System.out.println("Generate CFG:");
			SimpleNameMaps maps = new SimpleNameMaps(node);
			//CFG cfg = new CFG(node);
			//cfg.printCFG();
		}
	}
	 
	//read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
	 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			//System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
	 
		reader.close();
	 
		return  fileData.toString();	
	}
	 
	//loop directory to get file list (Not used yet!)
	public static void ParseFilesInDir() throws IOException{
		File dirs = new File(".");
		String dirPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator;
	 
		File root = new File(dirPath);
		//System.out.println(rootDir.listFiles());
		File[] files = root.listFiles ( );
		String filePath = null;
	 
		for (File f : files ) {
			filePath = f.getAbsolutePath();
			if(f.isFile()){
				parse(readFileToString(filePath));
			}
		}
	}
	 
	
	public static void main(String[] args) throws IOException {
		//ParseFilesInDir();
		String filePath = "/home/yuan/Desktop/PL research/eclipseWorkSpace/javaSrcCompress/src/testCodes/Solution350.java";
		parse(readFileToString(filePath));
	}	
}
