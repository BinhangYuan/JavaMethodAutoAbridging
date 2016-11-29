package statementGraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
			Statement statement = ElementItem.getASTNodeStatement(item);
			item.addDefinedVariables(ExpressionExtractor.getVariableSimpleNames(statement, true));
			item.addUsageVariables(ExpressionExtractor.getVariableSimpleNames(statement, true));
		}
	}
	
	public void printDDG(){
		
	}
}
