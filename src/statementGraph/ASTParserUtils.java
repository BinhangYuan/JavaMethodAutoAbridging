package statementGraph;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

import ilpSolver.NaiveBinaryIPSolver;

public class ASTParserUtils {
	
	//use ASTParse to parse string
	public static void parse(String filePath, String fileName) throws IOException {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		//parser.setS
		String str = readFileToString(filePath+fileName);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setEnvironment(new String[]{filePath}, null, null, true);
		parser.setUnitName("Anything");
		parser.setResolveBindings(true);
		
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
			CFG cfg = new CFG(node);
			//cfg.printCFG();
			System.out.println("Generate DDG:");
			DDG ddg = new DDG(cfg);
			//ddg.printDDG();
			//cfg.printCFG();
			ConstraintEncoder encoder = new ConstraintEncoder(cfg,ddg);
			encoder.printConstraints();
			NaiveBinaryIPSolver solver = new NaiveBinaryIPSolver();
			solver.setDependenceConstraints(encoder.getASTConstraints(), encoder.getCFGConstraints(), encoder.getDDGConstraints());
			solver.setLineCostConstraints(encoder.getLineCounts());
			solver.setTargetLineCount(15);
			solver.solve();
		}
	}
	
	
	//This is used for a simple verification 
	public static void parseVaraibleName(String filePath, String fileName) throws IOException {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		//parser.setS
		String str = readFileToString(filePath+fileName);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setEnvironment(new String[]{filePath}, null, null, true);
		parser.setUnitName("Anything");
		parser.setResolveBindings(true);
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		ArrayList<SimpleName> varaibles = new ArrayList<SimpleName>();
			
		cu.accept(new ASTVisitor() {
				
			public boolean visit(SimpleName node){
				varaibles.add(node);
				return true;
			}
		});
			
		System.out.println("All varabile declaration:");
		for(SimpleName node: varaibles){
			
			if(node.resolveBinding().getKind()==IBinding.VARIABLE && node.isDeclaration()){
				System.out.println("SimpleName: " + node.getIdentifier());
				System.out.println("is variable and defined at line" +cu.getLineNumber(node.getStartPosition()));
			}
			else if(node.resolveBinding().getKind()==IBinding.VARIABLE && !node.isDeclaration()){
				System.out.println("SimpleName: " + node.getIdentifier());
				System.out.println("is variable and used at line" +cu.getLineNumber(node.getStartPosition()));
			}
			else{
				System.out.println("SimpleName: " + node.getIdentifier());
				System.out.println("is not variable!");
			}
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
				//parse(readFileToString(filePath));
			}
		}
	}
	 
	
	public static void main(String[] args) throws IOException {
		//ParseFilesInDir();
		String filePath = "/home/yuan/Desktop/PL research/eclipseWorkSpace/javaSrcCompress/src/testCodes/";
		String fileName = "Solution350.java";
		parse(filePath,fileName);
		//parseVaraibleName(filePath,fileName);
	}	
}
