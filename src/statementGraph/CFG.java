package statementGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

public class CFG {
	private ElementItemFactory factory = new ElementItemFactory();
	
	private MethodDeclaration methodASTNode;
	
	private HashMap<ASTNode,ElementItem> astMap = new HashMap<ASTNode,ElementItem>();
	
	private List<ElementItem> nodes = new ArrayList<ElementItem>();
	
	
	public CFG(MethodDeclaration astNode){
		this.astMap.clear();
		this.methodASTNode = astNode;
		buildGraphNodes(astNode);
	}
	
	
	private void buildGraphNodes(ASTNode node){
		int nodeType = node.getNodeType();
		if(nodeType == ElementItem.METHOD_DECLARATION){
			if(((MethodDeclaration)node).getBody()==null){
				return;
			}
			else{
				for(Object statement:((MethodDeclaration)node).getBody().statements()){
					buildGraphNodes((ASTNode)statement);
				}
			}
		}
		else if(nodeType == ElementItem.BLOCK){
			for(Object statement:((Block)node).statements()){
				buildGraphNodes((ASTNode)statement);
			}
		}
		else if(nodeType == ElementItem.ASSERT_STATEMENT){
			AssertStatementItem item = (AssertStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.BREAK_STATEMENT){
			BreakStatementItem item = (BreakStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.CONSTRUCTOR_INVOCATION){
			ConstructorInvocationStatementItem item = (ConstructorInvocationStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.DO_STATEMENT){
			DoStatementItem item = (DoStatementItem) this.factory.createElementItem(node);;
			astMap.put(node, item);
			nodes.add(item);
			if(((DoStatement)node).getBody().getNodeType() != ElementItem.BLOCK){
				buildGraphNodes((ASTNode)(((DoStatement)node).getBody()));
			}
			else{
				Block body = (Block)((DoStatement)node).getBody();
				for(Object statement: body.statements()){
					buildGraphNodes((ASTNode)statement);
				}
			}
		}
		else if(nodeType == ElementItem.EMPTY_STATEMENT){
			EmptyStatementItem item = (EmptyStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.ENHANCED_FOR_STATEMENT){
			EnhancedForStatementItem item = (EnhancedForStatementItem) this.factory.createElementItem(node);;
			astMap.put(node, item);
			nodes.add(item);
			if(((EnhancedForStatement)node).getBody().getNodeType() != ElementItem.BLOCK){
				buildGraphNodes((ASTNode)(((DoStatement)node).getBody()));
			}
			else{
				Block body = (Block)((EnhancedForStatement)node).getBody();
				for(Object statement: body.statements()){
					buildGraphNodes((ASTNode)statement);
				}
			}
		}
		else if(nodeType == ElementItem.EXPRESSION_STATEMENT){
			ExpressionStatementItem item = (ExpressionStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.FOR_STATEMENT){
			ForStatementItem item = (ForStatementItem) this.factory.createElementItem(node);;
			astMap.put(node, item);
			nodes.add(item);
			if(((ForStatement)node).getBody().getNodeType() != ElementItem.BLOCK){
				buildGraphNodes((ASTNode)(((ForStatement)node).getBody()));
			}
			else{
				Block body = (Block)((ForStatement)node).getBody();
				for(Object statement: body.statements()){
					buildGraphNodes((ASTNode)statement);
				}
			}
		}
		else if(nodeType == ElementItem.IF_STATEMENT){
			IfStatementItem item = (IfStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
			buildGraphNodes((ASTNode)(((IfStatement)(node)).getThenStatement()));
			if(((IfStatement)(node)).getElseStatement() != null){
				buildGraphNodes((ASTNode)(((IfStatement)(node)).getElseStatement()));
			}
		}
		else if(nodeType == ElementItem.LABELED_STATEMENT){
			LabeledStatementItem item = (LabeledStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
			buildGraphNodes((ASTNode)(((LabeledStatement)(node)).getBody()));
		}
		else if(nodeType == ElementItem.RETURN_STATEMENT){
			ReturnStatementItem item = (ReturnStatementItem) this.factory.createElementItem(node);
			astMap.put(node,item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.SUPER_CONSTRUCTOR_INVOCATION){
			SuperConstructorInvocationStatementItem item = (SuperConstructorInvocationStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.SWITCH_CASE){
			SwitchCaseStatementItem item = (SwitchCaseStatementItem) this.factory.createElementItem(node);
			astMap.put(node,item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.SWITCH_STATEMENT){
			SwitchStatementItem item = (SwitchStatementItem) this.factory.createElementItem(node);
			astMap.put(node,item);
			nodes.add(item);
			for(Object statement:((SwitchStatement)node).statements()){
				buildGraphNodes((ASTNode)statement);
			}
		}
		else if(nodeType == ElementItem.SYNCHRONIZED_STATEMENT){
			SynchronizedStatementItem item = (SynchronizedStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
			Block body = (Block)((SynchronizedStatement)node).getBody();
			for(Object statement: body.statements()){
				buildGraphNodes((ASTNode)statement);
			}
		}
		else if(nodeType == ElementItem.THROW_STATEMENT){
			ThrowStatementItem item = (ThrowStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.TRY_STATEMENT){
			TryStatementItem item = (TryStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
			Block body = (Block)((TryStatement)node).getBody();
			for(Object statement: body.statements()){
				buildGraphNodes((ASTNode)statement);
			}
			Block finalbody = (Block)((TryStatement)node).getFinally();
			for(Object statement: finalbody.statements()){
				buildGraphNodes((ASTNode)statement);
			}
		}
		else if(nodeType == ElementItem.TYPE_DECLARATION_STATEMENT){
			TypeDeclarationStatementItem item = (TypeDeclarationStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.VARIABLE_DECLARATION_STATEMENT){
			VariableDeclarationStatementItem item = (VariableDeclarationStatementItem) this.factory.createElementItem(node);
			astMap.put(node, item);
			nodes.add(item);
		}
		else if(nodeType == ElementItem.WHILE_STATEMENT){
			WhileStatementItem item = (WhileStatementItem) this.factory.createElementItem(node);;
			astMap.put(node, item);
			nodes.add(item);
			if(((WhileStatement)node).getBody().getNodeType() != ElementItem.BLOCK){
				buildGraphNodes((ASTNode)(((WhileStatement)node).getBody()));
			}
			else{
				Block body = (Block)((WhileStatement)node).getBody();
				for(Object statement: body.statements()){
					buildGraphNodes((ASTNode)statement);
				}
			}
		}
		else{
			System.out.println("Unexpected Type in CFG!");
		}
	}
	
	
	public void printCFG(){
		System.out.println("Nodes:");
		for(int i=0 ; i < nodes.size(); i++){
			ElementItem item = nodes.get(i);
			System.out.println("Node "+i+": <========");
			item.print();
			System.out.println("========>");
		}
	}
}
