package statementGraph.graphNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;


//This may not be used in the future, keep it here for now.
public class SimpleNameMaps {
	private Map<String, ASTNode> definition = new HashMap<String, ASTNode>();
	private Map<String, List<ASTNode>> utils = new HashMap<String, List<ASTNode>>();
	
	public SimpleNameMaps(MethodDeclaration methodASTNode){
		
		methodASTNode.accept( new ASTVisitor(){
			
			public boolean visit(VariableDeclarationFragment node){
				SimpleName name = node.getName();
				System.out.println("Declaration of '"+name + "' at pos " + node.getStartPosition());
				ASTNode parent = node.getParent();
				assert(parent.getNodeType() == ElementItem.VARIABLE_DECLARATION_STATEMENT);
				return false;
			}
			
			public boolean visit(SimpleName node){
				if(definition.containsKey(node.getIdentifier())){
					System.out.println("Usage of '" + node.getIdentifier() + "' at pos " + node.getStartPosition());
				}
				else{
					System.out.println("Declaration of '"+node.getIdentifier() + "' at pos " + node.getStartPosition());
					definition.put(node.getIdentifier(), null);
					if(node.getParent().getNodeType() == ASTNode.METHOD_DECLARATION){
						System.out.println("In Method Declaration");
					}
					else if(node.getParent().getNodeType() == ASTNode.SINGLE_VARIABLE_DECLARATION){
						System.out.println("In Single Variable Declaration");
					}
					System.out.println(node.getParent());
				}
				return true;
			}
		});
		
	}

}
