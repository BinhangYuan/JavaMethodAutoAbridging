package statementGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import statementGraph.graphNode.ElementItem;
import statementGraph.graphNode.ExpressionStatementItem;
import statementGraph.graphNode.VariableDeclarationStatementItem;


public class DDG {
	private CFG cfg;
	private Set<String> methodParameters =  new HashSet<String>();
	private Map<String,List<ElementItem>> allVariables = new HashMap<String,List<ElementItem>>();
	
	public DDG(CFG cfg){
		this.cfg = cfg;
		this.updateMethodParameters();
		this.updateAllVariables();
		
	}
	
	private void updateMethodParameters(){
		MethodDeclaration methodAst = this.cfg.getMethodDeclaration();
		for(Object s: methodAst.parameters()){
			if(s instanceof SingleVariableDeclaration){
				SingleVariableDeclaration svdec = (SingleVariableDeclaration)s;
				this.methodParameters.add(svdec.getName().getIdentifier());
				this.allVariables.put(svdec.getName().getIdentifier(), null);
			}
		}
	}
	
	private void updateAllVariables(){
		for(ElementItem item:this.cfg.getNodes()){
			if(item.getType()==ElementItem.VARIABLE_DECLARATION_STATEMENT){
				List<VariableDeclarationFragment> frags = ((VariableDeclarationStatementItem)item).getASTNode().fragments();
				for(VariableDeclarationFragment vfrag:frags){
					String vName = vfrag.getName().getIdentifier();
					assert(!this.allVariables.containsKey(vName));
					this.allVariables.put(vName, new LinkedList<ElementItem>());
					this.allVariables.get(vName).add(item);
				}
			}
			else if(item.getType()==ElementItem.EXPRESSION_STATEMENT){
				Expression exp = ((ExpressionStatementItem)item).getASTNode().getExpression();
				//if(exp instanceof )
			}
		}
	}
	
	public void printDDG(){
		for(String key: this.allVariables.keySet()){
			if(this.allVariables.get(key)!=null){
				System.out.println(key+": <"+this.allVariables.get(key).size()+">");
				this.allVariables.get(key).forEach((item)->{item.printName();});
			}
		}	
	}
}
