package statementGraph;

import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.core.dom.ASTNode;


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
	
	private int itemType;
	protected int lineCount;
	
	private ElementItem seqSuccessor = null;
	
	private List<ElementItem> successors = new LinkedList<ElementItem>();
	private List<ElementItem> predecessor = new LinkedList<ElementItem>();
	
	
	protected void setType(int type){
		this.itemType = type;
	}
	
	public int getType(){
		return this.itemType;
	}
	
	public void setSeqSuccessor(ElementItem item){
		this.seqSuccessor = item;
	}
	
	public ElementItem getSeqSuccessor(){
		return this.seqSuccessor;
	}
	
	public List<ElementItem> getPredecessor(){
		return this.predecessor;
	}
	
	protected void addPredecessor(ElementItem item){
		this.predecessor.add(item);
	}
	
	public List<ElementItem> getSuccessors(){
		return this.successors;
	}
	
	protected void addSuccessors(ElementItem item){
		this.successors.add(item);
	}
	
	protected abstract void setLineCount(String code);
	
	public int getLineCount(){
		return this.lineCount;
	}
	
	protected abstract void print();
}
