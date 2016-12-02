package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;


public class ElementItemFactory {
	
	public ElementItem createElementItem(ASTNode node){
		if(node.getNodeType() == ElementItem.ASSERT_STATEMENT){
			return new AssertStatementItem((AssertStatement)node);
		}
		else if(node.getNodeType() == ElementItem.BREAK_STATEMENT){
			return new BreakStatementItem((BreakStatement)node);
		}
		else if(node.getNodeType() == ElementItem.CONSTRUCTOR_INVOCATION){
			return new ConstructorInvocationStatementItem((ConstructorInvocation)(node));
		}
		else if(node.getNodeType() == ElementItem.CONTINUE_STATEMENT){
			return new ContinueStatementItem((ContinueStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.DO_STATEMENT){
			return new DoStatementItem((DoStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.EMPTY_STATEMENT){
			return new EmptyStatementItem((EmptyStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.ENHANCED_FOR_STATEMENT){
			return new EnhancedForStatementItem((EnhancedForStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.EXPRESSION_STATEMENT){
			return new ExpressionStatementItem((ExpressionStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.FOR_STATEMENT){
			return new ForStatementItem((ForStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.IF_STATEMENT){
			return new IfStatementItem((IfStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.LABELED_STATEMENT){
			return new LabeledStatementItem((LabeledStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.RETURN_STATEMENT){
			return new ReturnStatementItem((ReturnStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.SWITCH_CASE){
			return new SwitchCaseStatementItem((SwitchCase)(node));
		}
		else if(node.getNodeType() == ElementItem.SWITCH_STATEMENT){
			return new SwitchStatementItem((SwitchStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.SYNCHRONIZED_STATEMENT){
			return new SynchronizedStatementItem((SynchronizedStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.THROW_STATEMENT){
			return new ThrowStatementItem((ThrowStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.TRY_STATEMENT){
			return new TryStatementItem((TryStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.TYPE_DECLARATION_STATEMENT){
			return new TypeDeclarationStatementItem((TypeDeclarationStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.VARIABLE_DECLARATION_STATEMENT){
			return new VariableDeclarationStatementItem((VariableDeclarationStatement)(node));
		}
		else if(node.getNodeType() == ElementItem.WHILE_STATEMENT){
			return new WhileStatementItem((WhileStatement)(node));
		}
		else{
			System.out.println("Unexpected type");
			return null;
		}
		
	}

}
