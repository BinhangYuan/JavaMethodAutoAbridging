package statementGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class SimpleNameMaps {
	private Map<SimpleName, ASTNode> definition = new HashMap<SimpleName, ASTNode>();
	private Map<SimpleName, List<ASTNode>> utils = new HashMap<SimpleName, List<ASTNode>>();
	
	public SimpleNameMaps(MethodDeclaration methodASTNode){
		
		methodASTNode.accept( new ASTVisitor(){
			
			public boolean visit(VariableDeclarationFragment node){
				SimpleName name = node.getName();
				System.out.println("Declaration of '"+name + "' at pos " + node.getStartPosition());
				ASTNode parent = node.getParent();
				assert(parent.getNodeType() == ElementItem.VARIABLE_DECLARATION_STATEMENT);
				definition.put(name,parent);
				return false;
			}
			
			public boolean visit(SimpleName node){
				if(definition.containsKey(node)){
					System.out.println("Usage of '" + node + "' at pos " + node.getStartPosition());
				}
				else{
					System.out.println("This is not supposed to happen!");
				}
				return true;
			}
		
		});
		
	}

}
