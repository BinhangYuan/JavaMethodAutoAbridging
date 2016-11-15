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
import org.eclipse.jdt.core.dom.Statement;
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
	private int entryIndex = -1;
	
	private ElementItemFactory factory = new ElementItemFactory();
	
	private MethodDeclaration methodASTNode;
	
	private HashMap<ASTNode,Integer> astMap = new HashMap<ASTNode,Integer>();
	
	private List<ElementItem> nodes = new ArrayList<ElementItem>();
	//private List<EdgeItem> edges = new ArrayList<EdgeItem>();
	
	
	public CFG(MethodDeclaration astNode){
		this.astMap.clear();
		this.methodASTNode = astNode;
		buildGraphNodes(astNode);
		buildGraphEdges(astNode);
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
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.BREAK_STATEMENT){
			BreakStatementItem item = (BreakStatementItem) this.factory.createElementItem(node);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.CONSTRUCTOR_INVOCATION){
			ConstructorInvocationStatementItem item = (ConstructorInvocationStatementItem) this.factory.createElementItem(node);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.DO_STATEMENT){
			DoStatementItem item = (DoStatementItem) this.factory.createElementItem(node);;
			astMap.put(node, nodes.size());
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
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.ENHANCED_FOR_STATEMENT){
			EnhancedForStatementItem item = (EnhancedForStatementItem) this.factory.createElementItem(node);;
			astMap.put(node, nodes.size());
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
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.FOR_STATEMENT){
			ForStatementItem item = (ForStatementItem) this.factory.createElementItem(node);;
			astMap.put(node, nodes.size());
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
			astMap.put(node, nodes.size());
			nodes.add(item);
			buildGraphNodes((ASTNode)(((IfStatement)(node)).getThenStatement()));
			if(((IfStatement)(node)).getElseStatement() != null){
				buildGraphNodes((ASTNode)(((IfStatement)(node)).getElseStatement()));
			}
		}
		else if(nodeType == ElementItem.LABELED_STATEMENT){
			LabeledStatementItem item = (LabeledStatementItem) this.factory.createElementItem(node);
			astMap.put(node, nodes.size());
			nodes.add(item);
			buildGraphNodes((ASTNode)(((LabeledStatement)(node)).getBody()));
		}
		else if(nodeType == ElementItem.RETURN_STATEMENT){
			ReturnStatementItem item = (ReturnStatementItem) this.factory.createElementItem(node);
			astMap.put(node,nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.SUPER_CONSTRUCTOR_INVOCATION){
			SuperConstructorInvocationStatementItem item = (SuperConstructorInvocationStatementItem) this.factory.createElementItem(node);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.SWITCH_CASE){
			SwitchCaseStatementItem item = (SwitchCaseStatementItem) this.factory.createElementItem(node);
			astMap.put(node,nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.SWITCH_STATEMENT){
			SwitchStatementItem item = (SwitchStatementItem) this.factory.createElementItem(node);
			astMap.put(node,nodes.size());
			nodes.add(item);
			for(Object statement:((SwitchStatement)node).statements()){
				buildGraphNodes((ASTNode)statement);
			}
		}
		else if(nodeType == ElementItem.SYNCHRONIZED_STATEMENT){
			SynchronizedStatementItem item = (SynchronizedStatementItem) this.factory.createElementItem(node);
			astMap.put(node, nodes.size());
			nodes.add(item);
			Block body = (Block)((SynchronizedStatement)node).getBody();
			for(Object statement: body.statements()){
				buildGraphNodes((ASTNode)statement);
			}
		}
		else if(nodeType == ElementItem.THROW_STATEMENT){
			ThrowStatementItem item = (ThrowStatementItem) this.factory.createElementItem(node);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.TRY_STATEMENT){
			TryStatementItem item = (TryStatementItem) this.factory.createElementItem(node);
			astMap.put(node, nodes.size());
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
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.VARIABLE_DECLARATION_STATEMENT){
			VariableDeclarationStatementItem item = (VariableDeclarationStatementItem) this.factory.createElementItem(node);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == ElementItem.WHILE_STATEMENT){
			WhileStatementItem item = (WhileStatementItem) this.factory.createElementItem(node);;
			astMap.put(node, nodes.size());
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
	
	
	private void buildGraphEdges(ASTNode node){
		int nodeType = node.getNodeType();
		if(nodeType == ElementItem.METHOD_DECLARATION){
			if(((MethodDeclaration)node).getBody()!=null){
				Block methodBody = ((MethodDeclaration)node).getBody();
				assert(astMap.get(methodBody.statements().get(0))==0);
				this.entryIndex = 0;
				buildGraphEdges((ASTNode)(methodBody));
			}
		}
		else if(nodeType == ElementItem.BLOCK){
			Block body = (Block)node;
			buildGraphEdges((ASTNode)body.statements().get(0));
			int i = 0;
			for(; i< body.statements().size()-1;i++){
				ElementItem start = nodes.get(astMap.get(body.statements().get(i)));
				ElementItem end = nodes.get(astMap.get(body.statements().get(i+1)));
				if(start.getType() != ElementItem.RETURN_STATEMENT){
					start.setCFGSeqSuccessor(end);
				}
				buildGraphEdges((ASTNode)body.statements().get(i));
			}
			buildGraphEdges((ASTNode)body.statements().get(i));
		}
		else if(nodeType == ElementItem.ASSERT_STATEMENT){
			return;
		}
		else if(nodeType == ElementItem.BREAK_STATEMENT){
			//This is a linear solution, should improve if necessary in the future;
			BreakStatement breakNode = (BreakStatement) node; 
			if(breakNode.getLabel()!=null){
				boolean flag = false;
				for(Object o : nodes){
					ElementItem statementItem = (ElementItem) o;
					if(statementItem.getType() == ElementItem.LABELED_STATEMENT){
						LabeledStatementItem labelItem = ((LabeledStatementItem)(statementItem));
						if(labelItem.getASTNode().getLabel().getIdentifier().equals(breakNode.getLabel().getIdentifier())){
							flag = true;
							ElementItem start = nodes.get(astMap.get(node));
							start.setCFGSeqSuccessor(labelItem);
							break;
						}
					}
				}
				if(!flag){
					System.out.println("This is an error, can not find the label!");
				}
			}
			// No label, link to the loop.
			else{
				//Find the most immediate loop;
				ASTNode ancestor = node.getParent();
				while(!ElementItem.isLoopStatement(ancestor) && ancestor.getNodeType()!=ElementItem.SWITCH_STATEMENT){
					ancestor = ancestor.getParent();
				}
				ElementItem start = nodes.get(astMap.get(node));
				ElementItem end = nodes.get(astMap.get(ancestor)).getCFGSeqSuccessor();
				start.setCFGSeqSuccessor(end);
			}
		}
		else if(nodeType == ElementItem.CONSTRUCTOR_INVOCATION){
			return;
		}
		else if(nodeType == ElementItem.DO_STATEMENT){
			//This may be problematic 
			DoStatement doNode = (DoStatement) node;
			if(doNode.getBody().getNodeType()==ElementItem.BLOCK){
				Block loopBlock = (Block)(doNode.getBody());
				if(loopBlock.statements().size()==0){
					return;
				}
				
				ASTNode firstStatement = (ASTNode) loopBlock.statements().get(0);
				DoStatementItem doItem = (DoStatementItem)nodes.get(astMap.get(node));
				ElementItem firstItem = nodes.get(astMap.get(firstStatement));
				doItem.setBodyEntry(firstItem);
				
				ASTNode lastStatement = (ASTNode) loopBlock.statements().get(loopBlock.statements().size()-1);
				ElementItem lastItem = nodes.get(astMap.get(lastStatement));
				lastItem.setCFGSeqSuccessor(doItem);
				//Recursive
				buildGraphEdges(loopBlock);
			}
			else{
				ASTNode bodyStatement = doNode.getBody();
				ElementItem bodyItem = nodes.get(astMap.get(bodyStatement));
				DoStatementItem doItem = (DoStatementItem)nodes.get(astMap.get(node));
				bodyItem.setCFGSeqSuccessor(doItem);
				doItem.setBodyEntry(bodyItem);
				buildGraphEdges(bodyStatement);
			}
		}
		else if(nodeType == ElementItem.EMPTY_STATEMENT){
			return;
		}
		else if(nodeType == ElementItem.ENHANCED_FOR_STATEMENT){
			//This may be problematic 
			EnhancedForStatement enforNode = (EnhancedForStatement) node;
			if(enforNode.getBody().getNodeType()==ElementItem.BLOCK){
				Block loopBlock = (Block)(enforNode.getBody());
				if(loopBlock.statements().size()==0){
					return;
				}
				
				ASTNode firstStatement = (ASTNode) loopBlock.statements().get(0);
				EnhancedForStatementItem enforItem = (EnhancedForStatementItem)nodes.get(astMap.get(node));
				ElementItem firstItem = nodes.get(astMap.get(firstStatement));
				enforItem.setBodyEntry(firstItem);
				
				ASTNode lastStatement = (ASTNode) loopBlock.statements().get(loopBlock.statements().size()-1);
				ElementItem lastItem = nodes.get(astMap.get(lastStatement));
				lastItem.setCFGSeqSuccessor(enforItem);
				//Recursive
				buildGraphEdges(loopBlock);
			}
			else{
				ASTNode bodyStatement = enforNode.getBody();
				ElementItem bodyItem = nodes.get(astMap.get(bodyStatement));
				EnhancedForStatementItem enforItem = (EnhancedForStatementItem)nodes.get(astMap.get(node));
				bodyItem.setCFGSeqSuccessor(enforItem);
				enforItem.setBodyEntry(bodyItem);
				buildGraphEdges(bodyStatement);
			}
		}
		else if(nodeType == ElementItem.EXPRESSION_STATEMENT){
			return;
		}
		else if(nodeType == ElementItem.FOR_STATEMENT){
			//This may be problematic 
			ForStatement forNode = (ForStatement) node;
			if(forNode.getBody().getNodeType()==ElementItem.BLOCK){
				Block loopBlock = (Block)(forNode.getBody());
				if(loopBlock.statements().size()==0){
					return;
				}
				
				ASTNode firstStatement = (ASTNode) loopBlock.statements().get(0);
				ForStatementItem forItem = (ForStatementItem)nodes.get(astMap.get(node));
				ElementItem firstItem = nodes.get(astMap.get(firstStatement));
				forItem.setBodyEntry(firstItem);
				
				ASTNode lastStatement = (ASTNode) loopBlock.statements().get(loopBlock.statements().size()-1);
				ElementItem lastItem = nodes.get(astMap.get(lastStatement));
				lastItem.setCFGSeqSuccessor(forItem);
				//Recursive
				buildGraphEdges(loopBlock);
			}
			else{
				ASTNode bodyStatement = forNode.getBody();
				ElementItem bodyItem = nodes.get(astMap.get(bodyStatement));
				ForStatementItem forItem = (ForStatementItem)nodes.get(astMap.get(node));
				bodyItem.setCFGSeqSuccessor(forItem);
				forItem.setBodyEntry(bodyItem);
				buildGraphEdges(bodyStatement);
			}
		}
		else if(nodeType == ElementItem.IF_STATEMENT){
			IfStatement ifnode = (IfStatement)node;
			IfStatementItem ifItem = (IfStatementItem)nodes.get(astMap.get(ifnode));
			ASTNode thenNode = ifnode.getThenStatement();
			ASTNode elseNode = ifnode.getElseStatement();
			if(thenNode.getNodeType() == ElementItem.BLOCK){
				Block thenBlock = (Block)thenNode;
				if(thenBlock.statements().size()==0){
					return;
				}
				ASTNode firstStatement = (ASTNode) thenBlock.statements().get(0);
				ASTNode lastStatement = (ASTNode) thenBlock.statements().get(thenBlock.statements().size()-1);
				ElementItem firstItem = nodes.get(astMap.get(firstStatement));
				ElementItem lastItem = nodes.get(astMap.get(lastStatement));
				lastItem.setCFGSeqSuccessor(ifItem.getCFGSeqSuccessor());
				ifItem.setThenEntry(firstItem);
				//Recursive
				buildGraphEdges(thenBlock);
			}
			else{
				ASTNode bodyStatement = (ASTNode)thenNode;
				ElementItem bodyItem = nodes.get(astMap.get(bodyStatement));
				bodyItem.setCFGSeqSuccessor(ifItem.getCFGSeqSuccessor());
				ifItem.setThenEntry(bodyItem);
				buildGraphEdges(bodyStatement);
			}
			if(elseNode != null){//Only cares then branch
				if(elseNode.getNodeType() == ElementItem.BLOCK){
					Block elseBlock = (Block)elseNode;
					if(elseBlock.statements().size()==0){
						return;
					}
					ASTNode firstStatement = (ASTNode) elseBlock.statements().get(0);
					ASTNode lastStatement = (ASTNode) elseBlock.statements().get(elseBlock.statements().size()-1);
					ElementItem firstItem = nodes.get(astMap.get(firstStatement));
					ElementItem lastItem = nodes.get(astMap.get(lastStatement));
					lastItem.setCFGSeqSuccessor(ifItem.getCFGSeqSuccessor());
					ifItem.setElseEntry(firstItem);
					//Recursive
					buildGraphEdges(elseBlock);
				}
				else{
					ASTNode bodyStatement = (ASTNode)elseNode;
					ElementItem bodyItem = nodes.get(astMap.get(bodyStatement));
					bodyItem.setCFGSeqSuccessor(ifItem.getCFGSeqSuccessor());
					ifItem.setElseEntry(bodyItem);
					buildGraphEdges(bodyStatement);
				}
			}
		}
		else if(nodeType == ElementItem.LABELED_STATEMENT){
			return;
		}
		else if(nodeType == ElementItem.RETURN_STATEMENT){
			ReturnStatementItem returnItem = (ReturnStatementItem) nodes.get(astMap.get(node));
			if(returnItem.getCFGSeqSuccessor()!=null){
				returnItem.setCFGSeqSuccessor(null);
			}
		}
		else if(nodeType == ElementItem.SUPER_CONSTRUCTOR_INVOCATION){
			return;
		}
		else if(nodeType == ElementItem.SWITCH_CASE){
			return;//This may be problematic;
		}
		else if(nodeType == ElementItem.SWITCH_STATEMENT){
			List<Statement> branchNodes = ((SwitchStatement)node).statements();
			SwitchStatementItem  switchItem =  (SwitchStatementItem) nodes.get(astMap.get(node));
			int i = 0;
			for(; i<branchNodes.size()-1 ;i++){
				Statement branchNode = branchNodes.get(i);
				ElementItem branchNodeItem = nodes.get(astMap.get(branchNode));
				if(branchNode.getNodeType()==ElementItem.SWITCH_CASE){
					SwitchCaseStatementItem caseItem = (SwitchCaseStatementItem) branchNodeItem;
					switchItem.addBranchEntries(caseItem);
				}
				ElementItem nextBranchNodeItem = nodes.get(astMap.get(branchNodes.get(i+1)));
				branchNodeItem.setCFGSeqSuccessor(nextBranchNodeItem);
				buildGraphEdges(branchNode);
			}
			Statement lastBranchNode = branchNodes.get(i);
			ElementItem lastBranchNodeItem = nodes.get(astMap.get(lastBranchNode));
			if(lastBranchNode.getNodeType()==ElementItem.SWITCH_CASE){
				SwitchCaseStatementItem caseItem = (SwitchCaseStatementItem) lastBranchNodeItem;
				switchItem.addBranchEntries(caseItem);
			}
			lastBranchNodeItem.setCFGSeqSuccessor(switchItem.getCFGSeqSuccessor());
			buildGraphEdges(lastBranchNode);
		}
		else if(nodeType == ElementItem.SYNCHRONIZED_STATEMENT){
			//This may not be handled correctly!
			Block syncBlock = ((SynchronizedStatement)(node)).getBody();
			if(syncBlock.statements().size()==0){
				return;
			}
			buildGraphEdges(syncBlock);
		}
		else if(nodeType == ElementItem.THROW_STATEMENT){
			return;
		}
		else if(nodeType == ElementItem.TRY_STATEMENT){
			Block tryBlock = ((TryStatement)(node)).getBody();
			if(tryBlock.statements().size()==0){
				return;
			}
			ASTNode firstStatement = (ASTNode) tryBlock.statements().get(0);
			ASTNode lastStatement = (ASTNode) tryBlock.statements().get(tryBlock.statements().size()-1);
			TryStatementItem tryItem = (TryStatementItem)nodes.get(astMap.get(node));
			ElementItem firstItem = nodes.get(astMap.get(firstStatement));
			ElementItem lastItem = nodes.get(astMap.get(lastStatement));
			lastItem.setCFGSeqSuccessor(tryItem.getCFGSeqSuccessor());
			tryItem.setCFGSeqSuccessor(firstItem);
			//Recursive
			buildGraphEdges(tryBlock);
		}
		else if(nodeType == ElementItem.TYPE_DECLARATION_STATEMENT){
			return;
		}
		else if(nodeType == ElementItem.VARIABLE_DECLARATION_STATEMENT){
			return;
		}
		else if(nodeType == ElementItem.WHILE_STATEMENT){
			//This may be problematic 
			WhileStatement whileNode = (WhileStatement) node;
			if(whileNode.getBody().getNodeType()==ElementItem.BLOCK){
				Block loopBlock = (Block)(whileNode.getBody());
				if(loopBlock.statements().size()==0){
					return;
				}
				
				ASTNode firstStatement = (ASTNode) loopBlock.statements().get(0);
				WhileStatementItem whileItem = (WhileStatementItem)nodes.get(astMap.get(node));
				ElementItem firstItem = nodes.get(astMap.get(firstStatement));
				whileItem.setBodyEntry(firstItem);
				
				ASTNode lastStatement = (ASTNode) loopBlock.statements().get(loopBlock.statements().size()-1);
				ElementItem lastItem = nodes.get(astMap.get(lastStatement));
				lastItem.setCFGSeqSuccessor(whileItem);
				//Recursive
				buildGraphEdges(loopBlock);
			}
			else{
				ASTNode bodyStatement = whileNode.getBody();
				ElementItem bodyItem = nodes.get(astMap.get(bodyStatement));
				WhileStatementItem whileItem = (WhileStatementItem)nodes.get(astMap.get(node));
				bodyItem.setCFGSeqSuccessor(whileItem);
				whileItem.setBodyEntry(bodyItem);
				buildGraphEdges(bodyStatement);
			}
		}
		else{
			System.out.println("Unexpected Type in CFG!");
		}
	}
	
	public MethodDeclaration getMethodDeclaration(){
		return this.methodASTNode;
	}
	
	public List<ElementItem> getNodes(){
		return this.nodes;
	}
	
	public void printCFG(){
		System.out.println("Nodes:");
		for(int i=0 ; i < nodes.size(); i++){
			ElementItem item = nodes.get(i);
			System.out.println("Node "+i+": <========");
			item.printDebug();
			System.out.println("========>");
		}
	}
}
