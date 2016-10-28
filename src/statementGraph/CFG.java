package statementGraph;

import java.util.HashMap;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class CFG {
	
	private MethodDeclaration methodASTNode;
	
	private HashMap<ElementItem, ASTNode> astMap = new HashMap<ElementItem, ASTNode>();
	
	
	
	public CFG(MethodDeclaration astNode){
		this.astMap.clear();
		this.methodASTNode = astNode;
		buildGraphNodes();
	}
	
	private void buildGraphNodes(){
		
	}
	
	
}
