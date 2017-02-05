package statementGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import statementGraph.graphNode.AssertStatementWrapper;
import statementGraph.graphNode.BreakStatementWrapper;
import statementGraph.graphNode.ConstructorInvocationStatementWrapper;
import statementGraph.graphNode.ContinueStatementWrapper;
import statementGraph.graphNode.DoStatementWrapper;
import statementGraph.graphNode.EmptyStatementWrapper;
import statementGraph.graphNode.EnhancedForStatementWrapper;
import statementGraph.graphNode.ExpressionStatementWrapper;
import statementGraph.graphNode.ForStatementWrapper;
import statementGraph.graphNode.IfStatementWrapper;
import statementGraph.graphNode.LabeledStatementWrapper;
import statementGraph.graphNode.ReturnStatementWrapper;
import statementGraph.graphNode.StatementWrapper;
import statementGraph.graphNode.StatementWrapperFactory;
import statementGraph.graphNode.SuperConstructorInvocationStatementWrapper;
import statementGraph.graphNode.SwitchCaseStatementWrapper;
import statementGraph.graphNode.SwitchStatementWrapper;
import statementGraph.graphNode.SynchronizedStatementWrapper;
import statementGraph.graphNode.ThrowStatementWrapper;
import statementGraph.graphNode.TryStatementWrapper;
import statementGraph.graphNode.TypeDeclarationStatementWrapper;
import statementGraph.graphNode.VariableDeclarationStatementWrapper;
import statementGraph.graphNode.WhileStatementWrapper;

public class SimplifiedAST {
	private StatementWrapperFactory factory = new StatementWrapperFactory();
	
	private MethodDeclaration methodASTNode = null;
	
	public MethodDeclaration getASTNode(){
		return this.methodASTNode;
	}
	
	private HashMap<ASTNode,Integer> astMap = new HashMap<ASTNode,Integer>();
	
	public HashMap<ASTNode,Integer> getAstMap(){
		return this.astMap;
	}
	
	private List<StatementWrapper> nodes = new ArrayList<StatementWrapper>();
	
	public List<StatementWrapper> getAllWrapperList(){
		return this.nodes;
	}
	
	private List<StatementWrapper> methodBlock = new LinkedList<StatementWrapper>();
	
	private int uncompressedLineCount;
	
	public int getUncompressedLineCount(){
		return uncompressedLineCount;
	}
	
	public SimplifiedAST(MethodDeclaration methodASTNode) throws Exception{
		this.methodASTNode = methodASTNode;
		this.buildTreeNodes(this.methodASTNode,0);
		this.buildHierachy(this.methodASTNode);
		boolean[] allDisplay = new boolean[this.nodes.size()];
		Arrays.fill(allDisplay, true);
		this.uncompressedLineCount = this.computeBodyLines(allDisplay);
	}
	
	public StatementWrapper getItem(ASTNode node){
		if(this.astMap.containsKey(node)){
			return this.nodes.get(this.astMap.get(node));
		}
		else{
			return null;
		}
	}
	
	private void buildTreeNodes(ASTNode node,int level) throws Exception{
		int nodeType = node.getNodeType();
		if(nodeType == StatementWrapper.METHOD_DECLARATION){
			if(((MethodDeclaration)node).getBody()==null){
				return;
			}
			else{
				for(Object statement:((MethodDeclaration)node).getBody().statements()){
					buildTreeNodes((ASTNode)statement,level+1);
				}
			}
		}
		else if(nodeType == StatementWrapper.BLOCK){
			for(Object statement:((Block)node).statements()){
				buildTreeNodes((ASTNode)statement,level);
			}
		}
		else if(nodeType == StatementWrapper.ASSERT_STATEMENT){
			AssertStatementWrapper item = (AssertStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.BREAK_STATEMENT){
			BreakStatementWrapper item = (BreakStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.CONSTRUCTOR_INVOCATION){
			ConstructorInvocationStatementWrapper item = (ConstructorInvocationStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.CONTINUE_STATEMENT){
			ContinueStatementWrapper item = (ContinueStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.DO_STATEMENT){
			DoStatementWrapper item = (DoStatementWrapper) this.factory.createWrapper(node);;
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
			if(((DoStatement)node).getBody().getNodeType() != StatementWrapper.BLOCK){
				buildTreeNodes((ASTNode)(((DoStatement)node).getBody()),level+1);
			}
			else{
				Block body = (Block)((DoStatement)node).getBody();
				for(Object statement: body.statements()){
					buildTreeNodes((ASTNode)statement,level+1);
				}
			}
		}
		else if(nodeType == StatementWrapper.EMPTY_STATEMENT){
			EmptyStatementWrapper item = (EmptyStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.ENHANCED_FOR_STATEMENT){
			EnhancedForStatementWrapper item = (EnhancedForStatementWrapper) this.factory.createWrapper(node);;
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
			if(((EnhancedForStatement)node).getBody().getNodeType() != StatementWrapper.BLOCK){
				buildTreeNodes((ASTNode)(((EnhancedForStatement)node).getBody()),level+1);
			}
			else{
				Block body = (Block)((EnhancedForStatement)node).getBody();
				for(Object statement: body.statements()){
					buildTreeNodes((ASTNode)statement,level+1);
				}
			}
		}
		else if(nodeType == StatementWrapper.EXPRESSION_STATEMENT){
			ExpressionStatementWrapper item = (ExpressionStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.FOR_STATEMENT){
			ForStatementWrapper item = (ForStatementWrapper) this.factory.createWrapper(node);;
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
			if(((ForStatement)node).getBody().getNodeType() != StatementWrapper.BLOCK){
				buildTreeNodes((ASTNode)(((ForStatement)node).getBody()),level+1);
			}
			else{
				Block body = (Block)((ForStatement)node).getBody();
				for(Object statement: body.statements()){
					buildTreeNodes((ASTNode)statement,level+1);
				}
			}
		}
		else if(nodeType == StatementWrapper.IF_STATEMENT){
			IfStatementWrapper item = (IfStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
			buildTreeNodes((ASTNode)(((IfStatement)(node)).getThenStatement()),level+1);
			if(((IfStatement)(node)).getElseStatement() != null){
				buildTreeNodes((ASTNode)(((IfStatement)(node)).getElseStatement()),level+1);
			}
		}
		else if(nodeType == StatementWrapper.LABELED_STATEMENT){
			LabeledStatementWrapper item = (LabeledStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
			buildTreeNodes((ASTNode)(((LabeledStatement)(node)).getBody()),level+1);
		}
		else if(nodeType == StatementWrapper.RETURN_STATEMENT){
			ReturnStatementWrapper item = (ReturnStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node,nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.SUPER_CONSTRUCTOR_INVOCATION){
			SuperConstructorInvocationStatementWrapper item = (SuperConstructorInvocationStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.SWITCH_CASE){
			SwitchCaseStatementWrapper item = (SwitchCaseStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node,nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.SWITCH_STATEMENT){
			SwitchStatementWrapper item = (SwitchStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node,nodes.size());
			nodes.add(item);
			for(Object statement:((SwitchStatement)node).statements()){
				buildTreeNodes((ASTNode)statement,level+1);
			}
		}
		else if(nodeType == StatementWrapper.SYNCHRONIZED_STATEMENT){
			SynchronizedStatementWrapper item = (SynchronizedStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
			Block body = (Block)((SynchronizedStatement)node).getBody();
			for(Object statement: body.statements()){
				buildTreeNodes((ASTNode)statement,level+1);
			}
		}
		else if(nodeType == StatementWrapper.THROW_STATEMENT){
			ThrowStatementWrapper item = (ThrowStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.TRY_STATEMENT){
			TryStatementWrapper item = (TryStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
			Block body = (Block)((TryStatement)node).getBody();
			for(Object statement: body.statements()){
				buildTreeNodes((ASTNode)statement,level+1);
			}
			@SuppressWarnings("unchecked")
			List<CatchClause> catchClauses = ((TryStatement)node).catchClauses();
			for(CatchClause catchItem: catchClauses){
				Block catchbody = catchItem.getBody();
				for(Object statement: catchbody.statements()){
					buildTreeNodes((ASTNode)statement,level+1);
				}
			}
			Block finalbody = (Block)((TryStatement)node).getFinally();
			if(finalbody!=null){
				for(Object statement: finalbody.statements()){
					buildTreeNodes((ASTNode)statement,level+1);
				}
			}
		}
		else if(nodeType == StatementWrapper.TYPE_DECLARATION_STATEMENT){
			TypeDeclarationStatementWrapper item = (TypeDeclarationStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.VARIABLE_DECLARATION_STATEMENT){
			VariableDeclarationStatementWrapper item = (VariableDeclarationStatementWrapper) this.factory.createWrapper(node);
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
		}
		else if(nodeType == StatementWrapper.WHILE_STATEMENT){
			WhileStatementWrapper item = (WhileStatementWrapper) this.factory.createWrapper(node);;
			item.setNestedLevel(level);
			astMap.put(node, nodes.size());
			nodes.add(item);
			if(((WhileStatement)node).getBody().getNodeType() != StatementWrapper.BLOCK){
				buildTreeNodes((ASTNode)(((WhileStatement)node).getBody()),level+1);
			}
			else{
				Block body = (Block)((WhileStatement)node).getBody();
				for(Object statement: body.statements()){
					buildTreeNodes((ASTNode)statement,level+1);
				}
			}
		}
		else{
			System.out.println("Unexpected Type in Simplified AST!");
		}
	}
	
	
	private void buildHierachy(ASTNode node){
		int nodeType = node.getNodeType();
		if(nodeType == StatementWrapper.METHOD_DECLARATION){
			if(((MethodDeclaration)node).getBody()==null){
				return;
			}
			else{
				for(Object statement:((MethodDeclaration)node).getBody().statements()){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
					currentWrapper.setParentType(StatementWrapper.PARENT_METHODDECLARATION);
					this.methodBlock.add(currentWrapper);
					buildHierachy((ASTNode)statement);
				}
			}
		}
		else if(nodeType == StatementWrapper.ASSERT_STATEMENT){
			return;
		}
		else if(nodeType == StatementWrapper.BREAK_STATEMENT){
			return;
		}
		else if(nodeType == StatementWrapper.CONSTRUCTOR_INVOCATION){
			return;
		}
		else if(nodeType == StatementWrapper.CONTINUE_STATEMENT){
			return;
		}
		else if(nodeType == StatementWrapper.DO_STATEMENT){
			DoStatementWrapper item = (DoStatementWrapper) nodes.get(astMap.get(node));
			ASTNode body = ((DoStatement)node).getBody();
			if(body.getNodeType() != StatementWrapper.BLOCK){
				StatementWrapper currentWrapper = nodes.get(astMap.get(body));
				currentWrapper.setParentType(StatementWrapper.PARENT_DOSTATEMENT);
				item.addBodyWrapper(currentWrapper);
				buildHierachy(body);
			}
			else{
				Block bodyBlock = (Block) body;
				for(Object statement: bodyBlock.statements()){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
					currentWrapper.setParentType(StatementWrapper.PARENT_DOSTATEMENT);
					item.addBodyWrapper(currentWrapper);
					buildHierachy((ASTNode)statement);
				}
			}
		}
		else if(nodeType == StatementWrapper.EMPTY_STATEMENT){
			return;
		}
		else if(nodeType == StatementWrapper.ENHANCED_FOR_STATEMENT){
			EnhancedForStatementWrapper item = (EnhancedForStatementWrapper) nodes.get(astMap.get(node));
			ASTNode body = ((EnhancedForStatement)node).getBody();
			if(body.getNodeType() != StatementWrapper.BLOCK){
				StatementWrapper currentWrapper = nodes.get(astMap.get(body));
				currentWrapper.setParentType(StatementWrapper.PARENT_ENHANCEDFORSTATEMENT);
				item.addBodyWrapper(currentWrapper);
				buildHierachy(body);
			}
			else{
				Block bodyBlock = (Block) body;
				for(Object statement: bodyBlock.statements()){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
					currentWrapper.setParentType(StatementWrapper.PARENT_ENHANCEDFORSTATEMENT);
					item.addBodyWrapper(currentWrapper);
					buildHierachy((ASTNode)statement);
				}
			}
		}
		else if(nodeType == StatementWrapper.EXPRESSION_STATEMENT){
			return;
		}
		else if(nodeType == StatementWrapper.FOR_STATEMENT){
			ForStatementWrapper item = (ForStatementWrapper) nodes.get(astMap.get(node));
			ASTNode body = ((ForStatement)node).getBody();
			if(body.getNodeType() != StatementWrapper.BLOCK){
				StatementWrapper currentWrapper = nodes.get(astMap.get(body));
				currentWrapper.setParentType(StatementWrapper.PARENT_FORSTATEMENT);
				item.addBodyWrapper(currentWrapper);
				buildHierachy(body);
			}
			else{
				Block bodyBlock = (Block) body;
				for(Object statement: bodyBlock.statements()){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
					currentWrapper.setParentType(StatementWrapper.PARENT_FORSTATEMENT);
					item.addBodyWrapper(currentWrapper);
					buildHierachy((ASTNode)statement);
				}
			}
		}
		else if(nodeType == StatementWrapper.IF_STATEMENT){
			IfStatementWrapper item = (IfStatementWrapper) nodes.get(astMap.get(node));
			ASTNode thenBody = ((IfStatement)node).getThenStatement();
			if(thenBody.getNodeType() != StatementWrapper.BLOCK){
				StatementWrapper currentWrapper = nodes.get(astMap.get(thenBody));
				currentWrapper.setParentType(StatementWrapper.PARENT_IFSTATEMENT_THEN);
				item.addThenBodyWrapper(currentWrapper);
				buildHierachy(thenBody);
			}
			else{
				Block thenBodyBlock = (Block) thenBody;
				for(Object statement: thenBodyBlock.statements()){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
					currentWrapper.setParentType(StatementWrapper.PARENT_IFSTATEMENT_THEN);
					item.addThenBodyWrapper(currentWrapper);
					buildHierachy((ASTNode)statement);
				}
			}
			ASTNode elseBody = ((IfStatement)node).getElseStatement();
			if(elseBody != null){
				if(elseBody.getNodeType() != StatementWrapper.BLOCK){
					StatementWrapper currentWrapper = nodes.get(astMap.get(elseBody));
					currentWrapper.setParentType(StatementWrapper.PARENT_IFSTATEMENT_ELSE);
					item.addElseBodyWrapper(currentWrapper);
					buildHierachy(elseBody);
				}
				else{
					Block elseBodyBlock = (Block) elseBody;
					for(Object statement: elseBodyBlock.statements()){
						StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
						currentWrapper.setParentType(StatementWrapper.PARENT_IFSTATEMENT_ELSE);
						item.addElseBodyWrapper(currentWrapper);
						buildHierachy((ASTNode)statement);
					}
				}
			}
		}
		else if(nodeType == StatementWrapper.LABELED_STATEMENT){
			LabeledStatementWrapper item = (LabeledStatementWrapper) nodes.get(astMap.get(node));
			ASTNode body = ((LabeledStatement)node).getBody();
			if(body.getNodeType() != StatementWrapper.BLOCK){
				StatementWrapper currentWrapper = nodes.get(astMap.get(body));
				currentWrapper.setParentType(StatementWrapper.PARENT_LABELEDSTATEMENT);
				item.addBodyWrapper(currentWrapper);
				buildHierachy(body);
			}
			else{
				Block bodyBlock = (Block) body;
				for(Object statement: bodyBlock.statements()){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
					currentWrapper.setParentType(StatementWrapper.PARENT_LABELEDSTATEMENT);
					item.addBodyWrapper(currentWrapper);
					buildHierachy((ASTNode)statement);
				}
			}
		}
		else if(nodeType == StatementWrapper.RETURN_STATEMENT){
			return;
		}
		else if(nodeType == StatementWrapper.SUPER_CONSTRUCTOR_INVOCATION){
			return;
		}
		else if(nodeType == StatementWrapper.SWITCH_CASE){
			return;
		}
		else if(nodeType == StatementWrapper.SWITCH_STATEMENT){
			SwitchStatementWrapper item = (SwitchStatementWrapper) nodes.get(astMap.get(node));
			@SuppressWarnings("rawtypes")
			List statements = ((SwitchStatement)node).statements();
			List<StatementWrapper> tempList = new LinkedList<StatementWrapper>();
			boolean firstBranch = true;
			boolean isDefault = false;
			boolean lastIsBlock = false;
			for(int i=0; i < statements.size();i++){
				Object statement = statements.get(i);
				if(i==0){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
					Assert.isTrue(currentWrapper.getType()==StatementWrapper.SWITCH_CASE);
					if(((SwitchCaseStatementWrapper)currentWrapper).getASTNode().isDefault()){
						currentWrapper.setParentType(StatementWrapper.PARENT_SWITCHSTATEMENT_DEFAULTCASE);
						isDefault = true;
					}
					else{
						currentWrapper.setParentType(StatementWrapper.PARENT_SWITCHSTATEMENT_FIRSTCASE);
					}
					item.addBranchEntries(currentWrapper);
				}
				else if(((ASTNode)statement).getNodeType()==StatementWrapper.SWITCH_CASE){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
					if(((SwitchCaseStatementWrapper)currentWrapper).getASTNode().isDefault()){
						currentWrapper.setParentType(StatementWrapper.PARENT_SWITCHSTATEMENT_DEFAULTCASE);
						isDefault = true; 
					}
					else{
						currentWrapper.setParentType(StatementWrapper.PARENT_SWITCHSTATEMENT_FIRSTCASE);
					}
					item.addBranchStatementsWrapper(tempList);
					item.addisBlockFlag(lastIsBlock);
					((SwitchCaseStatementWrapper)(item.getBranchEntries().get(item.getBranchEntries().size()-1))).setBranchIsBlock(lastIsBlock);
					item.addBranchEntries(currentWrapper);
					tempList = new LinkedList<StatementWrapper>();
					firstBranch = false;
				}
				else{
					int parentType = isDefault?StatementWrapper.PARENT_SWITCHSTATEMENT_DEFAULTCASE:
									(firstBranch?StatementWrapper.PARENT_SWITCHSTATEMENT_FIRSTCASE:
												StatementWrapper.PARENT_SWITCHSTATEMENT_OTHERCASE);
					if(((ASTNode)statement).getNodeType()==StatementWrapper.BLOCK){
						@SuppressWarnings("rawtypes")
						List blockStatements = ((Block)statement).statements();
						for(Object nestedStatement: blockStatements){
							StatementWrapper currentWrapper = nodes.get(astMap.get(nestedStatement));
							currentWrapper.setParentType(parentType);
							tempList.add(currentWrapper);
							buildHierachy((ASTNode)nestedStatement);
						}
						lastIsBlock = true;
						//This assertion just checks some situation that this version cannot handle yet.
						if(i<statements.size()-1){
							Assert.isTrue(((ASTNode)statements.get(i+1)).getNodeType()==StatementWrapper.SWITCH_CASE);
						}
					}
					else{
						lastIsBlock = false;
						StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
						currentWrapper.setParentType(parentType);
						tempList.add(currentWrapper);
						buildHierachy((ASTNode)statement);
					}
				}
			}
			item.addBranchStatementsWrapper(tempList);
			item.addisBlockFlag(lastIsBlock);
		}
		else if(nodeType == StatementWrapper.SYNCHRONIZED_STATEMENT){
			SynchronizedStatementWrapper item = (SynchronizedStatementWrapper) nodes.get(astMap.get(node));
			Block body = (Block)((SynchronizedStatement)node).getBody();
			for(Object statement: body.statements()){
				StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
				currentWrapper.setParentType(StatementWrapper.PARENT_SYNCHRONIZEDSTATEMENT);
				item.addBodyWrapper(currentWrapper);
				buildHierachy((ASTNode)statement);
			}
		}
		else if(nodeType == StatementWrapper.THROW_STATEMENT){
			return;
		}
		else if(nodeType == StatementWrapper.TRY_STATEMENT){
			TryStatementWrapper item = (TryStatementWrapper) nodes.get(astMap.get(node));
			Block body = (Block)((TryStatement)node).getBody();
			for(Object statement: body.statements()){
				StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
				currentWrapper.setParentType(StatementWrapper.PARENT_TRYSTATEMENT_BODY);
				item.addBodyWrapper(currentWrapper);
				buildHierachy((ASTNode)statement);
			}
			@SuppressWarnings("unchecked")
			List<CatchClause> catchClauses = ((TryStatement)(node)).catchClauses();
			for(CatchClause catchItem: catchClauses){
				Block catchbody = catchItem.getBody();
				for(Object statement: catchbody.statements()){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
					currentWrapper.setParentType(StatementWrapper.PARENT_TRYSTATEMENT_CATCH);
					item.addCatchClauseWrapper(catchItem, currentWrapper);
					buildHierachy((ASTNode)statement);
				}
			}
			Block finalbody = (Block)((TryStatement)node).getFinally();
			if(finalbody!=null){
				for(Object statement: finalbody.statements()){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement));
					currentWrapper.setParentType(StatementWrapper.PARENT_TRYSTATEMENT_FINAL);
					item.addFinalBodyWrapper(currentWrapper);
					buildHierachy((ASTNode)statement);
				}
			}
		}
		else if(nodeType == StatementWrapper.TYPE_DECLARATION_STATEMENT){
			return;
		}
		else if(nodeType == StatementWrapper.VARIABLE_DECLARATION_STATEMENT){
			return;
		}
		else if(nodeType == StatementWrapper.WHILE_STATEMENT){
			WhileStatementWrapper item = (WhileStatementWrapper) nodes.get(astMap.get(node));
			ASTNode body = ((WhileStatement)node).getBody();
			if(body.getNodeType() != StatementWrapper.BLOCK){
				StatementWrapper currentWrapper = nodes.get(astMap.get(body));
				currentWrapper.setParentType(Statement.WHILE_STATEMENT);
				item.addBodyWrapper(currentWrapper);
				buildHierachy(body);
			}
			else{
				Block bodyBlock = (Block) body;
				for(Object statement: bodyBlock.statements()){
					StatementWrapper currentWrapper = nodes.get(astMap.get(statement)); 
					currentWrapper.setParentType(StatementWrapper.PARENT_WHILESTATEMENT);
					item.addBodyWrapper(currentWrapper);
					buildHierachy((ASTNode)statement);
				}
			}
		}
		else{
			System.out.println("Unexpected Type in AST!("+nodeType+")");
		}
	}
	
	
	public List<StatementWrapper> getSiblings(StatementWrapper item) throws Exception{
		Statement state = StatementWrapper.getASTNodeStatement(item);
		//System.out.println("GetSiblings: <state>"+state.toString());
		ASTNode parent = state.getParent();
		//System.out.println("GetSiblings: <parent>"+parent.toString());
		
		if(parent instanceof Block){
			LinkedList<StatementWrapper> siblings = new LinkedList<StatementWrapper>();
			Block block = (Block) parent;
			for(Object o: block.statements()){
				Statement siblingsState = (Statement)o;
				if(siblingsState != StatementWrapper.getASTNodeStatement(item)){
					siblings.add(this.getItem(siblingsState));
				}
				else{
					break;
				}
			}
			return siblings;
		}
		else{
			return null;
		}
	}
	
	
	public StatementWrapper getParent(StatementWrapper item) throws Exception{
		Statement state = StatementWrapper.getASTNodeStatement(item);
		ASTNode parent = state.getParent();
		if(parent instanceof Block){
			parent = parent.getParent(); 
		}
		if(parent instanceof CatchClause){
			parent = parent.getParent();
		}
		Assert.isTrue(StatementWrapper.isAstStatement(parent) || parent.getNodeType() == ASTNode.METHOD_DECLARATION);
		StatementWrapper parentItem = this.getItem(parent);
		return parentItem;
	}

	public String computeOutput(boolean [] solution){
		Assert.isTrue(solution.length == this.nodes.size());
		String result = new String();
		Javadoc methodDoc = this.methodASTNode.getJavadoc();
		this.methodASTNode.setJavadoc(null);
		result = this.methodASTNode.toString().substring(0, this.methodASTNode.toString().indexOf('{')+1);
		this.methodASTNode.setJavadoc(methodDoc);
		result += '\n';
		result += this.computeBody(solution);
		result += '}';
		return result;
	}
	
	private String computeBody(boolean [] solution){
		Assert.isTrue(solution.length == this.nodes.size());
		//Set up result;
		for(int i=0; i<solution.length; i++){
			this.nodes.get(i).setIsDisplay(solution[i]);
		}
		String result = new String();
		//Recursive handle each statement:
		for(StatementWrapper statementWrapper: this.methodBlock){
			if(statementWrapper.isDisplay()){
				result += statementWrapper.computeOutput(1);
			}
		}
		return result;
	}
	
	public int computeBodyLines(boolean[] solution){
		return this.computeBody(solution).split(System.getProperty("line.separator")).length;
	}
}
