package statementGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import statementGraph.expressionWrapper.ExpressionExtractor;
import statementGraph.expressionWrapper.ExpressionInstanceChecker;
import statementGraph.graphNode.AssertStatementItem;
import statementGraph.graphNode.BreakStatementItem;
import statementGraph.graphNode.ConstructorInvocationStatementItem;
import statementGraph.graphNode.DoStatementItem;
import statementGraph.graphNode.ElementItem;
import statementGraph.graphNode.EmptyStatementItem;
import statementGraph.graphNode.EnhancedForStatementItem;
import statementGraph.graphNode.ExpressionStatementItem;
import statementGraph.graphNode.ForStatementItem;
import statementGraph.graphNode.IfStatementItem;
import statementGraph.graphNode.LabeledStatementItem;
import statementGraph.graphNode.ReturnStatementItem;
import statementGraph.graphNode.SuperConstructorInvocationStatementItem;
import statementGraph.graphNode.SwitchCaseStatementItem;
import statementGraph.graphNode.SwitchStatementItem;
import statementGraph.graphNode.SynchronizedStatementItem;
import statementGraph.graphNode.ThrowStatementItem;
import statementGraph.graphNode.TryStatementItem;
import statementGraph.graphNode.TypeDeclarationStatementItem;
import statementGraph.graphNode.VariableDeclarationStatementItem;
import statementGraph.graphNode.WhileStatementItem;


public class DDG {
	private CFG cfg;
	private SimplifiedAST astSkel;
	private List<SimpleName> methodParameters =  new LinkedList<SimpleName>();
	private Map<String,List<ElementItem>> variablesDecl = new HashMap<String,List<ElementItem>>();
	
	public DDG(CFG cfg){
		this.cfg = cfg;
		this.astSkel = new SimplifiedAST(this.cfg);
		this.updateMethodParameters();
		this.updateAllVariables();
		this.buildEdges();
	}
	
	private void updateMethodParameters(){
		MethodDeclaration methodAst = this.cfg.getMethodDeclaration();
		for(Object s: methodAst.parameters()){
			if(s instanceof SingleVariableDeclaration){
				SingleVariableDeclaration svdec = (SingleVariableDeclaration)s;
				this.methodParameters.add(svdec.getName());
				this.variablesDecl.put(svdec.getName().getIdentifier(), new LinkedList<ElementItem>());
				this.variablesDecl.get(svdec.getName().getIdentifier()).add(null);
			}
		}
	}
	
	private void updateAllVariables(){
		for(ElementItem item:this.cfg.getNodes()){
			Statement statement = ElementItem.getASTNodeStatement(item);
			item.addDefinedVariables(ExpressionExtractor.getVariableSimpleNames(statement, true));
			if(!item.getDefinedVariables().isEmpty()){
				for(SimpleName var: item.getDefinedVariables()){
					if(!this.variablesDecl.containsKey(var.getIdentifier())){
						this.variablesDecl.put(var.getIdentifier(), new LinkedList<ElementItem>());
					}
					this.variablesDecl.get(var.getIdentifier()).add(item);
				}
			}
			item.addUsageVariables(ExpressionExtractor.getVariableSimpleNames(statement, false));
		}
	}
	
	private void buildEdges(){
		for(ElementItem item: this.cfg.getNodes()){
			if(!item.getUsageVariables().isEmpty()){
				for(SimpleName var: item.getUsageVariables()){
					if(this.variablesDecl.containsKey(var.getIdentifier())){//If the variable is declared out of the scope of the function, we ignore it for now.
						if(this.variablesDecl.get(var.getIdentifier()).size()==1){//This is simple, only one declaration in the function scope
							ElementItem preItem = this.variablesDecl.get(var.getIdentifier()).get(0);
							item.addDDGDefinedPredecessor(preItem);
							if(preItem!=null){
								preItem.addDDGUsageSuccessor(item);
							}
						}
						else{//Little tricky here, we should consider the same name under different scope.
							ElementItem current = item;
							ElementItem parent = null;
							List<ElementItem> siblings = null;
							boolean done = false;
							while(done){
								siblings = this.astSkel.getSiblings(current);
								for(ElementItem sibling: siblings){
									if(sibling.getUsageVariables().contains(var.getIdentifier())){
										ElementItem preItem = sibling;
										item.addDDGDefinedPredecessor(preItem);
										if(preItem!=null){
											preItem.addDDGUsageSuccessor(item);
										}
										done = true;
										break;
									}
								}
								if(!done){
									parent = this.astSkel.getParent(current);
									if(parent.getUsageVariables().contains(var.getIdentifier())){
										ElementItem preItem = parent;
										item.addDDGDefinedPredecessor(preItem);
										if(preItem!=null){
											preItem.addDDGUsageSuccessor(item);
										}
										done = true;
										break;
									}
									else{
										current = parent;
									}
								}
							}
						}
					}
				}
			}
		}		
	}
	
	
	public void printDDG(){
		System.out.println("Print the elements declaration and usage");
		for(ElementItem item:this.cfg.getNodes()){
			item.printName();
			System.out.println("Declares: "+item.getDefinedVariables());
			System.out.println("Uses: "+item.getUsageVariables());
		}
		System.out.println("==============================================");
		System.out.println("Variable declares: ");
		for(String name: this.variablesDecl.keySet()){
			System.out.println("Variable: <"+name+">:");
			for(ElementItem item: this.variablesDecl.get(name)){
				if(item!=null){
					item.printName();
				}
				else{
					System.out.println("Passed as parameter");
				}
			}
			System.out.println("-------------------------------------------");
		}
	}
}
