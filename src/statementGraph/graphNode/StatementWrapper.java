package statementGraph.graphNode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;


public abstract class StatementWrapper {
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

	
	public static Set<Integer> statementsLabelSet;
	public static Map<Integer,Integer> typeMap;
	
	
	//Encode parent node information, defined by myself;
	public static final int PARENT_ILLEGAL = -1000;
	public static final int PARENT_METHODDECLARATION = 1000;
	public static final int PARENT_DOSTATEMENT = 1010;
	public static final int PARENT_ENHANCEDFORSTATEMENT = 1020;
	public static final int PARENT_FORSTATEMENT = 1030;
	public static final int PARENT_IFSTATEMENT_THEN = 1041;
	public static final int PARENT_IFSTATEMENT_ELSE = 1042;
	public static final int PARENT_LABELEDSTATEMENT = 1050;
	public static final int PARENT_SWITCHSTATEMENT_FIRSTCASE = 1061;
	public static final int PARENT_SWITCHSTATEMENT_OTHERCASE = 1062;
	public static final int PARENT_SWITCHSTATEMENT_DEFAULTCASE = 1063;
	public static final int PARENT_SYNCHRONIZEDSTATEMENT = 1070;
	public static final int PARENT_TRYSTATEMENT_BODY = 1081;
	public static final int PARENT_TRYSTATEMENT_CATCH = 1082;
	public static final int PARENT_TRYSTATEMENT_FINAL = 1083;
	public static final int PARENT_WHILESTATEMENT = 1090;
	
	
	public static Set<Integer> parentStatementsLabelSet;
	public static Map<Integer,Integer> parentTypeMap;
	
	static{
		statementsLabelSet = new HashSet<Integer>(Arrays.asList(new Integer[]{
			ASSERT_STATEMENT,BREAK_STATEMENT,CONSTRUCTOR_INVOCATION,CONTINUE_STATEMENT,DO_STATEMENT,EMPTY_STATEMENT,
			ENHANCED_FOR_STATEMENT,EXPRESSION_STATEMENT,FOR_STATEMENT,IF_STATEMENT,LABELED_STATEMENT,RETURN_STATEMENT,
			SUPER_CONSTRUCTOR_INVOCATION,SWITCH_CASE,SWITCH_STATEMENT,SYNCHRONIZED_STATEMENT,THROW_STATEMENT,TRY_STATEMENT,
			TYPE_DECLARATION_STATEMENT,VARIABLE_DECLARATION_STATEMENT,WHILE_STATEMENT	
		}));
		typeMap = new HashMap<Integer,Integer>();
		int i = 0;
		for(Integer label: StatementWrapper.statementsLabelSet){
			typeMap.put(label, i++);
		}
		
		parentStatementsLabelSet = new HashSet<Integer>(Arrays.asList(new Integer[]{
				PARENT_METHODDECLARATION, PARENT_DOSTATEMENT, PARENT_ENHANCEDFORSTATEMENT,PARENT_FORSTATEMENT, PARENT_IFSTATEMENT_THEN,
				PARENT_IFSTATEMENT_ELSE, PARENT_LABELEDSTATEMENT,PARENT_SWITCHSTATEMENT_FIRSTCASE,PARENT_SWITCHSTATEMENT_OTHERCASE,
				PARENT_SWITCHSTATEMENT_DEFAULTCASE,PARENT_SYNCHRONIZEDSTATEMENT, PARENT_TRYSTATEMENT_BODY, PARENT_TRYSTATEMENT_CATCH,
				PARENT_TRYSTATEMENT_FINAL,PARENT_WHILESTATEMENT
		}));
		parentTypeMap = new HashMap<Integer,Integer>();
		i = 0;
		for(Integer label: StatementWrapper.parentStatementsLabelSet){
			parentTypeMap.put(label, i++);
		}
	}
	
	public static boolean isAstStatement(ASTNode node){
		int nodeType = node.getNodeType();
		return nodeType == ASSERT_STATEMENT||
			   nodeType == BLOCK||
			   nodeType == BREAK_STATEMENT||
			   nodeType == CONSTRUCTOR_INVOCATION||
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
	
	
	public static String statementTypeInt2String(int nodeType){
		switch (nodeType){
			case ASSERT_STATEMENT: return "Assert Statement";
			case BLOCK: return "Block";
			case BREAK_STATEMENT: return "Break Statement";
			case CONSTRUCTOR_INVOCATION: return "Constructor Invocation Statement";
			case CONTINUE_STATEMENT: return "Continue Statement";
			case DO_STATEMENT: return "Do Statement";
			case EMPTY_STATEMENT: return "Empty Statement";
			case ENHANCED_FOR_STATEMENT: return "Enhanced For Statement";
			case EXPRESSION_STATEMENT: return "Expression Statement";
			case FOR_STATEMENT: return "For Statement";
			case IF_STATEMENT: return "IF Statement";
			case LABELED_STATEMENT: return "Label Statement";
			case RETURN_STATEMENT: return "Return Statement";
			case SUPER_CONSTRUCTOR_INVOCATION: return "Super Constructor Invocation Statement";
			case SWITCH_CASE: return "Switch Case";
			case SWITCH_STATEMENT: return "Switch Statement";
			case SYNCHRONIZED_STATEMENT: return "Synchronized Statement";
			case THROW_STATEMENT: return "Throw Statement";
			case TRY_STATEMENT: return "Try Statement";
			case TYPE_DECLARATION_STATEMENT: return "Type Declaration Statement";
			case VARIABLE_DECLARATION_STATEMENT: return "Variable Declaration Statement";
			case WHILE_STATEMENT: return "While Statement";
			default: return "Unexpected Statement Type by Code "+nodeType;
		}
	}
	
	
	public static String parentStatementTypeInt2String(int parentNodeType){
		switch (parentNodeType){
			case PARENT_ILLEGAL: return "Illegal parenet type";
			case PARENT_METHODDECLARATION: return "Parent MethodDeclaration";
			case PARENT_DOSTATEMENT: return "Parent DoStatement";
			case PARENT_ENHANCEDFORSTATEMENT: return "Parent EnhancedForStatement";
			case PARENT_FORSTATEMENT: return "Parent ForStatement";
			case PARENT_IFSTATEMENT_THEN: return "Parent IfStatement Then";
			case PARENT_IFSTATEMENT_ELSE: return "Parent IfStatement Else";
			case PARENT_LABELEDSTATEMENT: return "Parent LabeledStatement";
			case PARENT_SWITCHSTATEMENT_FIRSTCASE: return "Parent SwitchStatement First";
			case PARENT_SWITCHSTATEMENT_OTHERCASE: return "Parent SwitchStatement Other";
			case PARENT_SWITCHSTATEMENT_DEFAULTCASE:return "Parent SwitchStatement Default";
			case PARENT_SYNCHRONIZEDSTATEMENT: return "Parent SynchronizedStatement";
			case PARENT_TRYSTATEMENT_BODY: return "Parent TryStatement Body";
			case PARENT_TRYSTATEMENT_CATCH: return "Parent TryStatement Catch";
			case PARENT_TRYSTATEMENT_FINAL: return "Parent TryStatement Final";
			case PARENT_WHILESTATEMENT: return "Parent WhileStatement";
			default: return "Unexpected Parent Statement Type by Code "+parentNodeType;
		
		}
	}
	
	
	public static boolean isSimpleStatement(ASTNode node){
		int nodeType = node.getNodeType();
		return nodeType == StatementWrapper.ASSERT_STATEMENT ||
			   nodeType == StatementWrapper.BREAK_STATEMENT ||
			   nodeType == StatementWrapper.CONSTRUCTOR_INVOCATION ||
			   nodeType == StatementWrapper.EMPTY_STATEMENT ||
			   nodeType == StatementWrapper.EXPRESSION_STATEMENT ||
			   nodeType == StatementWrapper.RETURN_STATEMENT || 
			   nodeType == StatementWrapper.SUPER_CONSTRUCTOR_INVOCATION ||
			   nodeType == StatementWrapper.SWITCH_CASE ||
			   nodeType == StatementWrapper.THROW_STATEMENT ||
			   nodeType == StatementWrapper.TYPE_DECLARATION_STATEMENT ||
			   nodeType == StatementWrapper.VARIABLE_DECLARATION_STATEMENT;
	}
	
	
	public static boolean isLoopStatement(ASTNode node){
		int nodeType = node.getNodeType();
		return nodeType == StatementWrapper.DO_STATEMENT ||
			   nodeType == StatementWrapper.WHILE_STATEMENT ||
			   nodeType == StatementWrapper.FOR_STATEMENT ||
			   nodeType == StatementWrapper.ENHANCED_FOR_STATEMENT;
	}
	
	public static boolean containsVariableDeclarations(ASTNode node){
		int nodeType = node.getNodeType();
		return nodeType == StatementWrapper.VARIABLE_DECLARATION_STATEMENT ||
			   nodeType == StatementWrapper.FOR_STATEMENT ||
			   nodeType == StatementWrapper.ENHANCED_FOR_STATEMENT ||
			   nodeType == StatementWrapper.TRY_STATEMENT;
	}
	
	
	public static Statement getASTNodeStatement(StatementWrapper item) throws Exception{
		int nodeType = item.getType();
		Statement statement = null;
		if(nodeType == StatementWrapper.ASSERT_STATEMENT){
			statement = ((AssertStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.BREAK_STATEMENT){
			statement = ((BreakStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.CONSTRUCTOR_INVOCATION){
			statement = ((ConstructorInvocationStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.CONTINUE_STATEMENT){
			statement = ((ContinueStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.DO_STATEMENT){
			statement = ((DoStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.EMPTY_STATEMENT){
			statement = ((EmptyStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.ENHANCED_FOR_STATEMENT){
			statement = ((EnhancedForStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.EXPRESSION_STATEMENT){
			statement = ((ExpressionStatementWrapper)item).getASTNode();	
		}
		else if(nodeType == StatementWrapper.FOR_STATEMENT){
			statement = ((ForStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.IF_STATEMENT){
			statement = ((IfStatementWrapper)item).getASTNode();	
		}
		else if(nodeType == StatementWrapper.LABELED_STATEMENT){
			statement = ((LabeledStatementWrapper)item).getASTNode();		
		}
		else if(nodeType == StatementWrapper.RETURN_STATEMENT){
			 statement = ((ReturnStatementWrapper)item).getASTNode();	
		}
		else if(nodeType == StatementWrapper.SUPER_CONSTRUCTOR_INVOCATION){
			statement = ((SuperConstructorInvocationStatementWrapper)item).getASTNode();		
		}
		else if(nodeType == StatementWrapper.SWITCH_CASE){
			statement = ((SwitchCaseStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.SWITCH_STATEMENT){
			statement = ((SwitchStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.SYNCHRONIZED_STATEMENT){
			statement = ((SynchronizedStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.THROW_STATEMENT){
			statement = ((ThrowStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.TRY_STATEMENT){
			statement = ((TryStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.TYPE_DECLARATION_STATEMENT){
			statement = ((TypeDeclarationStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.VARIABLE_DECLARATION_STATEMENT){
			statement = ((VariableDeclarationStatementWrapper)item).getASTNode();
		}
		else if(nodeType == StatementWrapper.WHILE_STATEMENT){
			statement = ((WhileStatementWrapper)item).getASTNode();
		}
		else{
			//System.out.println("Unexpected Type in CFG!");
			System.out.println(item.toString());
			throw new Exception("Unexpected Type:"+nodeType);
		}
		return statement;
	}
	
	private int itemType;
	
	protected void setType(int type){
		this.itemType = type;
	}
	
	public int getType(){
		return this.itemType;
	}
	
	private int parentType = PARENT_ILLEGAL;
	
	public void setParentType(int type){
		this.parentType = type;
	}
	
	public int getParentType(){
		return this.parentType;
	}
	
	
	private boolean isDisplay = false;
	
	public void setIsDisplay(boolean flag){
		this.isDisplay = flag;
	}
	
	public boolean isDisplay(){
		return this.isDisplay;
	}
	
	
	private StatementWrapper parent = null;
	
	public StatementWrapper getParent(){
		return this.parent;
	}
	
	public void setParent(StatementWrapper parent){
		this.parent = parent;
	}
	
	
	private StatementWrapper cfgSeqSuccessor = null;
	
	public void setCFGSeqSuccessor(StatementWrapper item){
		this.cfgSeqSuccessor = item;
	}
	
	public StatementWrapper getCFGSeqSuccessor(){
		return this.cfgSeqSuccessor;
	}
	
	
	//For the current implementation, this list should include only one element.
	private List<StatementWrapper> ddgDefinedPredecessor = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getDDGDefinedPredecessor(){
		return this.ddgDefinedPredecessor;
	}
	
	public void addDDGDefinedPredecessor(StatementWrapper item){
		this.ddgDefinedPredecessor.add(item);
	}
	
	
	private List<StatementWrapper> ddgUsageSuccessor = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getDDGUsageSuccessor(){
		return this.ddgUsageSuccessor;
	}
	
	public void addDDGUsageSuccessor(StatementWrapper item){
		this.ddgUsageSuccessor.add(item);
	}
	
	
	private List<SimpleName> usageVariables = new LinkedList<SimpleName>();
	private Set<String> usageVariableSet = new HashSet<String>();
	
	public void addUsageVariables(SimpleName identifier){
		this.usageVariables.add(identifier);
		this.usageVariableSet.add(identifier.getIdentifier());
	}
	
	public void addUsageVariables(List<SimpleName> identifiers){
		this.usageVariables.addAll(identifiers);
		for(SimpleName simple:identifiers){
			this.usageVariableSet.add(simple.getIdentifier());
		}
	}
	
	public List<SimpleName> getUsageVariables(){
		return this.usageVariables;
	}
	
	public Set<String> getUsageVariableSet(){
		return this.usageVariableSet;
	}
	
	
	//For the current implementation, this list should include only one element.
	private List<SimpleName> definedVariables = new LinkedList<SimpleName>();
	private Set<String> definedVariableSet = new HashSet<String>();
	
	public void addDefinedVariables(SimpleName identifier){
		this.definedVariables.add(identifier);
		this.definedVariableSet.add(identifier.getIdentifier());
	}
	
	public void addDefinedVariables(List<SimpleName> identifiers){
		this.definedVariables.addAll(identifiers);
		for(SimpleName simple:identifiers){
			this.definedVariableSet.add(simple.getIdentifier());
		}
	}
	
	public List<SimpleName> getDefinedVariables(){
		return this.definedVariables;
	}
	
	public Set<String> getDefinedVariableSet(){
		return this.definedVariableSet;
	}
	
	private int nestedLevel = -1;
	
	public int getNestedLevel(){
		Assert.isTrue(this.nestedLevel!=-1);
		return this.nestedLevel;
	}
	
	public void setNestedLevel(int level){
		this.nestedLevel = level;
	}
	
	private int variableCount = 0;
	
	public void addVariableCount(int count){
		this.variableCount += count;
	}
	
	public int getReferencedVariables(){
		return this.variableCount;
	}
	
	public abstract int getLineCount();
	
	public abstract void printDebug();
	
	public abstract void printName();
	
	public abstract String toString();
	
	public abstract String computeOutput(int level);
	
	protected String computeIndent(int level){
		char[] chars = new char[level];
		Arrays.fill(chars, '\t');
		return new String(chars);
	}
	
	
	protected void printDDGPredecessor(){
		System.out.println("DDG Predecessors: -->");
		if(this.getDDGDefinedPredecessor().isEmpty()){
			System.out.println("Empty");
		}
		else{
			for(StatementWrapper item: this.getDDGDefinedPredecessor()){
				if(item!=null){
					item.printName();
				}
				else{
					System.out.println("In Method Declaration");
				}
			}
		}
	}
}
