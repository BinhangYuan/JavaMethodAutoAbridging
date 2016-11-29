package statementGraph.graphNode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;

import statementGraph.expressionWrapper.ExpressionExtractor;


public abstract class ElementItem {
	//Statement:
	public static final int ASSERT_STATEMENT = ASTNode.ASSERT_STATEMENT;
	public static final int BLOCK = ASTNode.BLOCK;
	public static final int BREAK_STATEMENT = ASTNode.BREAK_STATEMENT;
	public static final int CONSTRUCTOR_INVOCATION = ASTNode.CONSTRUCTOR_INVOCATION;
	public static final int CONTINUE_STATEMENT = ASTNode.CONTINUE_STATEMENT;
	public static final int DO_STATEMENT = ASTNode.DO_STATEMENT;
	public static final int EMPTY_STATEMENT = ASTNode.EMPTY_STATEMENT;
	public static final int ENHANCED_FOR_STATEMENT = ASTNode.ENHANCED_FOR_STATEMENT;
	public static final int EXPRESSION_STATEMENT = ASTNode.EXPRESSION_STATEMENT;
	public static final int FOR_STATEMENT = ASTNode.FOR_STATEMENT;
	public static final int IF_STATEMENT = ASTNode.IF_STATEMENT;
	public static final int LABELED_STATEMENT = ASTNode.LABELED_STATEMENT;
	public static final int RETURN_STATEMENT = ASTNode.RETURN_STATEMENT;
	public static final int SUPER_CONSTRUCTOR_INVOCATION = ASTNode.SUPER_CONSTRUCTOR_INVOCATION;
	public static final int SWITCH_CASE = ASTNode.SWITCH_CASE;
	public static final int SWITCH_STATEMENT = ASTNode.SWITCH_STATEMENT; 
	public static final int SYNCHRONIZED_STATEMENT = ASTNode.SYNCHRONIZED_STATEMENT;
	public static final int THROW_STATEMENT = ASTNode.THROW_STATEMENT;
	public static final int TRY_STATEMENT = ASTNode.TRY_STATEMENT;
	public static final int TYPE_DECLARATION_STATEMENT = ASTNode.TYPE_DECLARATION_STATEMENT;
	public static final int VARIABLE_DECLARATION_STATEMENT = ASTNode.VARIABLE_DECLARATION_STATEMENT;
	public static final int WHILE_STATEMENT = ASTNode.WHILE_STATEMENT;
	//Expression:
	public static final int MARKER_ANNOTATION = ASTNode.MARKER_ANNOTATION;
	public static final int NORMAL_ANNOTATION = ASTNode.NORMAL_ANNOTATION;
	public static final int SINGLE_MEMBER_ANNOTATION = ASTNode.SINGLE_MEMBER_ANNOTATION;
	public static final int ARRAY_ACCESS = ASTNode.ARRAY_ACCESS;
	public static final int ARRAY_CREATION = ASTNode.ARRAY_CREATION;
	public static final int ARRAY_INITIALIZER = ASTNode.ARRAY_INITIALIZER;
	public static final int ASSIGNMENT = ASTNode.ASSIGNMENT;
	public static final int BOOLEAN_LITERAL = ASTNode.BOOLEAN_LITERAL;
	public static final int CAST_EXPRESSION = ASTNode.CAST_EXPRESSION;
	public static final int CHARACTER_LITERAL = ASTNode.CHARACTER_LITERAL;
	public static final int CLASS_INSTANCE_CREATION = ASTNode.CLASS_INSTANCE_CREATION;
	public static final int CONDITIONAL_EXPRESSION = ASTNode.CONDITIONAL_EXPRESSION;
	public static final int FIELD_ACCESS = ASTNode.FIELD_ACCESS;
	public static final int INFIX_EXPRESSION = ASTNode.INFIX_EXPRESSION;
	public static final int INSTANCEOF_EXPRESSION = ASTNode.INSTANCEOF_EXPRESSION;
	public static final int METHOD_INVOCATION = ASTNode.METHOD_INVOCATION;
	public static final int QUALIFIED_NAME = ASTNode.QUALIFIED_NAME;
	public static final int SIMPLE_NAME = ASTNode.SIMPLE_NAME;
	public static final int NULL_LITERAL = ASTNode.NULL_LITERAL;
	public static final int NUMBER_LITERAL = ASTNode.NUMBER_LITERAL;
	public static final int PARENTHESIZED_EXPRESSION = ASTNode.PARENTHESIZED_EXPRESSION;
	public static final int POSTFIX_EXPRESSION = ASTNode.POSTFIX_EXPRESSION;
	public static final int PREFIX_EXPRESSION = ASTNode.PREFIX_EXPRESSION;
	public static final int STRING_LITERAL = ASTNode.STRING_LITERAL;
	public static final int SUPER_FIELD_ACCESS = ASTNode.SUPER_FIELD_ACCESS;
	public static final int SUPER_METHOD_INVOCATION = ASTNode.SUPER_METHOD_INVOCATION;
	public static final int THIS_EXPRESSION = ASTNode.THIS_EXPRESSION;
	public static final int TYPE_LITERAL = ASTNode.TYPE_LITERAL;
	public static final int VARIABLE_DECLARATION_EXPRESSION = ASTNode.VARIABLE_DECLARATION_EXPRESSION;
	
	//Declaration:
	public static final int CATCH_CLAUSE = ASTNode.CATCH_CLAUSE;
	public static final int COMPILATION_UNIT = ASTNode.COMPILATION_UNIT; //Not inside a method.
	public static final int FIELD_DECLARATION = ASTNode.FIELD_DECLARATION; //Not inside a method.
	public static final int IMPORT_DECLARATION = ASTNode.IMPORT_DECLARATION; //Not inside a method.
	public static final int INITIALIZER = ASTNode.INITIALIZER; //Not clear yet.
	public static final int JAVADOC = ASTNode.JAVADOC;
	public static final int METHOD_DECLARATION = ASTNode.METHOD_DECLARATION;
	public static final int PACKAGE_DECLARATION = ASTNode.PACKAGE_DECLARATION;
	//Undecided
	public static final int LINE_COMMENT = ASTNode.LINE_COMMENT;
	public static final int BLOCK_COMMENT = ASTNode.BLOCK_COMMENT;
	public static final int TAG_ELEMENT = ASTNode.TAG_ELEMENT;
	public static final int TEXT_ELEMENT = ASTNode.TEXT_ELEMENT;
	public static final int ENUM_DECLARATION = ASTNode.ENUM_DECLARATION;
	public static final int ENUM_CONSTANT_DECLARATION = ASTNode.ENUM_CONSTANT_DECLARATION;
	public static final int TYPE_PARAMETER = ASTNode.TYPE_PARAMETER; //Not inside a method.
	public static final int MEMBER_VALUE_PAIR = ASTNode.MEMBER_VALUE_PAIR;
	public static final int ANNOTATION_TYPE_DECLARATION = ASTNode.ANNOTATION_TYPE_DECLARATION;
	public static final int ANNOTATION_TYPE_MEMBER_DECLARATION = ASTNode.ANNOTATION_TYPE_DECLARATION;

	
	public static boolean isAstStatement(ASTNode node){
		int nodeType = node.getNodeType();
		return nodeType == ASSERT_STATEMENT||
			   nodeType == BLOCK||
			   nodeType == BREAK_STATEMENT||
			   nodeType ==CONSTRUCTOR_INVOCATION||
			   nodeType == CONTINUE_STATEMENT||
			   nodeType == DO_STATEMENT||
			   nodeType == EMPTY_STATEMENT||
			   nodeType == ENHANCED_FOR_STATEMENT||
			   nodeType == EXPRESSION_STATEMENT||
			   nodeType == FOR_STATEMENT||
			   nodeType == IF_STATEMENT||
			   nodeType == LABELED_STATEMENT||
			   nodeType == RETURN_STATEMENT||
			   nodeType == SUPER_CONSTRUCTOR_INVOCATION||
			   nodeType == SWITCH_CASE||
			   nodeType == SWITCH_STATEMENT|| 
			   nodeType == SYNCHRONIZED_STATEMENT||
			   nodeType == THROW_STATEMENT||
			   nodeType == TRY_STATEMENT||
			   nodeType == TYPE_DECLARATION_STATEMENT||
			   nodeType == VARIABLE_DECLARATION_STATEMENT||
			   nodeType == WHILE_STATEMENT;
	}
	
	public static boolean isSimpleStatement(ASTNode node){
		int nodeType = node.getNodeType();
		return nodeType == ElementItem.ASSERT_STATEMENT ||
			   nodeType == ElementItem.BREAK_STATEMENT ||
			   nodeType == ElementItem.CONSTRUCTOR_INVOCATION ||
			   nodeType == ElementItem.EMPTY_STATEMENT ||
			   nodeType == ElementItem.EXPRESSION_STATEMENT ||
			   nodeType == ElementItem.RETURN_STATEMENT || 
			   nodeType == ElementItem.SUPER_CONSTRUCTOR_INVOCATION ||
			   nodeType == ElementItem.SWITCH_CASE ||
			   nodeType == ElementItem.THROW_STATEMENT ||
			   nodeType == ElementItem.TYPE_DECLARATION_STATEMENT ||
			   nodeType == ElementItem.VARIABLE_DECLARATION_STATEMENT;
	}
	
	public static boolean isLoopStatement(ASTNode node){
		int nodeType = node.getNodeType();
		return nodeType == ElementItem.DO_STATEMENT ||
			   nodeType == ElementItem.WHILE_STATEMENT ||
			   nodeType == ElementItem.FOR_STATEMENT ||
			   nodeType == ElementItem.ENHANCED_FOR_STATEMENT;
	}
	
	
	public static Statement getASTNodeStatement(ElementItem item){
		int nodeType = item.getType();
		Statement statement = null;
		if(nodeType == ElementItem.ASSERT_STATEMENT){
			statement = ((AssertStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.BREAK_STATEMENT){
			statement = ((BreakStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.CONSTRUCTOR_INVOCATION){
			statement = ((ConstructorInvocationStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.DO_STATEMENT){
			statement = ((DoStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.EMPTY_STATEMENT){
			statement = ((EmptyStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.ENHANCED_FOR_STATEMENT){
			statement = ((EnhancedForStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.EXPRESSION_STATEMENT){
			statement = ((ExpressionStatementItem)item).getASTNode();	
		}
		else if(nodeType == ElementItem.FOR_STATEMENT){
			statement = ((ForStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.IF_STATEMENT){
			statement = ((IfStatementItem)item).getASTNode();	
		}
		else if(nodeType == ElementItem.LABELED_STATEMENT){
			statement = ((LabeledStatementItem)item).getASTNode();		
		}
		else if(nodeType == ElementItem.RETURN_STATEMENT){
			 statement = ((ReturnStatementItem)item).getASTNode();	
		}
		else if(nodeType == ElementItem.SUPER_CONSTRUCTOR_INVOCATION){
			statement = ((SuperConstructorInvocationStatementItem)item).getASTNode();		
		}
		else if(nodeType == ElementItem.SWITCH_CASE){
			statement = ((SwitchCaseStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.SWITCH_STATEMENT){
			statement = ((SwitchStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.SYNCHRONIZED_STATEMENT){
			statement = ((SynchronizedStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.THROW_STATEMENT){
			statement = ((ThrowStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.TRY_STATEMENT){
			statement = ((TryStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.TYPE_DECLARATION_STATEMENT){
			statement = ((TypeDeclarationStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.VARIABLE_DECLARATION_STATEMENT){
			statement = ((VariableDeclarationStatementItem)item).getASTNode();
		}
		else if(nodeType == ElementItem.WHILE_STATEMENT){
			statement = ((WhileStatementItem)item).getASTNode();
		}
		else{
			System.out.println("Unexpected Type in CFG!");
		}
		return statement;
	}
	
	private int itemType;
	protected int lineCount;
	
	private ElementItem cfgSeqSuccessor = null;
	
	private List<ElementItem> cfgSuccessors = new LinkedList<ElementItem>();
	private List<ElementItem> cfgPredecessor = new LinkedList<ElementItem>();
	
	private List<ElementItem> ddgDefinedPredecessor = new LinkedList<ElementItem>();
	private List<ElementItem> ddgUsageSuccessor = new LinkedList<ElementItem>();
	
	private List<SimpleName> usageVariables = new LinkedList<SimpleName>();
	private List<SimpleName> definedVariables = new LinkedList<SimpleName>();
	
	protected void setType(int type){
		this.itemType = type;
	}
	
	public int getType(){
		return this.itemType;
	}
	
	public void setCFGSeqSuccessor(ElementItem item){
		this.cfgSeqSuccessor = item;
	}
	
	public ElementItem getCFGSeqSuccessor(){
		return this.cfgSeqSuccessor;
	}
	
	public List<ElementItem> getCFGPredecessor(){
		return this.cfgPredecessor;
	}
	
	protected void addCFGPredecessor(ElementItem item){
		this.cfgPredecessor.add(item);
	}
	
	public List<ElementItem> getCFGSuccessors(){
		return this.cfgSuccessors;
	}
	
	protected void addCFGSuccessors(ElementItem item){
		this.cfgSuccessors.add(item);
	}
	
	public List<ElementItem> getDDGDefinedPredecessor(){
		return this.ddgDefinedPredecessor;
	}
	
	public List<ElementItem> getDDGUsageSuccessor(){
		return this.ddgUsageSuccessor;
	}
	
	protected void addDDGDefinedPredecessor(ElementItem item){
		this.ddgDefinedPredecessor.add(item);
	}
	
	protected void addDDGUsageSuccessor(ElementItem item){
		this.ddgUsageSuccessor.add(item);
	}
	
	public void addUsageVariables(SimpleName identifier){
		this.usageVariables.add(identifier);
	}
	
	public void addUsageVariables(List<SimpleName> identifiers){
		this.usageVariables.addAll(identifiers);
	}
	
	
	public List<SimpleName> getUsageVariables(){
		return this.usageVariables;
	}
	
	public void addDefinedVariables(SimpleName identifier){
		this.definedVariables.add(identifier);
	}
	
	public void addDefinedVariables(List<SimpleName> identifiers){
		this.definedVariables.addAll(identifiers);
	}
	
	public List<SimpleName> getDefinedVariables(){
		return this.definedVariables;
	}
	
	protected abstract void setLineCount(String code);
	
	public int getLineCount(){
		return this.lineCount;
	}
	
	public abstract void printDebug();
	
	public abstract void printName();
}
