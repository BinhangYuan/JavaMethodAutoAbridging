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


public class StatementWrapperFactory {
	
	public StatementWrapper createWrapper(ASTNode node){
		if(node.getNodeType() == StatementWrapper.ASSERT_STATEMENT){
			return new AssertStatementWrapper((AssertStatement)node);
		}
		else if(node.getNodeType() == StatementWrapper.BREAK_STATEMENT){
			return new BreakStatementWrapper((BreakStatement)node);
		}
		else if(node.getNodeType() == StatementWrapper.CONSTRUCTOR_INVOCATION){
			return new ConstructorInvocationStatementWrapper((ConstructorInvocation)(node));
		}
		else if(node.getNodeType() == StatementWrapper.CONTINUE_STATEMENT){
			return new ContinueStatementWrapper((ContinueStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.DO_STATEMENT){
			return new DoStatementWrapper((DoStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.EMPTY_STATEMENT){
			return new EmptyStatementWrapper((EmptyStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.ENHANCED_FOR_STATEMENT){
			return new EnhancedForStatementWrapper((EnhancedForStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.EXPRESSION_STATEMENT){
			return new ExpressionStatementWrapper((ExpressionStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.FOR_STATEMENT){
			return new ForStatementWrapper((ForStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.IF_STATEMENT){
			return new IfStatementWrapper((IfStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.LABELED_STATEMENT){
			return new LabeledStatementWrapper((LabeledStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.RETURN_STATEMENT){
			return new ReturnStatementWrapper((ReturnStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.SWITCH_CASE){
			return new SwitchCaseStatementWrapper((SwitchCase)(node));
		}
		else if(node.getNodeType() == StatementWrapper.SWITCH_STATEMENT){
			return new SwitchStatementWrapper((SwitchStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.SYNCHRONIZED_STATEMENT){
			return new SynchronizedStatementWrapper((SynchronizedStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.THROW_STATEMENT){
			return new ThrowStatementWrapper((ThrowStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.TRY_STATEMENT){
			return new TryStatementWrapper((TryStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.TYPE_DECLARATION_STATEMENT){
			return new TypeDeclarationStatementWrapper((TypeDeclarationStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.VARIABLE_DECLARATION_STATEMENT){
			return new VariableDeclarationStatementWrapper((VariableDeclarationStatement)(node));
		}
		else if(node.getNodeType() == StatementWrapper.WHILE_STATEMENT){
			return new WhileStatementWrapper((WhileStatement)(node));
		}
		else{
			System.out.println("Unexpected type");
			return null;
		}
		
	}

}
