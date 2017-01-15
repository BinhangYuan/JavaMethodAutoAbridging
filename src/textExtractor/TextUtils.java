package textExtractor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import statementGraph.graphNode.AssertStatementWrapper;
import statementGraph.graphNode.BreakStatementWrapper;
import statementGraph.graphNode.ConstructorInvocationStatementWrapper;
import statementGraph.graphNode.ContinueStatementWrapper;
import statementGraph.graphNode.DoStatementWrapper;
import statementGraph.graphNode.EnhancedForStatementWrapper;
import statementGraph.graphNode.ExpressionStatementWrapper;
import statementGraph.graphNode.ForStatementWrapper;
import statementGraph.graphNode.IfStatementWrapper;
import statementGraph.graphNode.LabeledStatementWrapper;
import statementGraph.graphNode.ReturnStatementWrapper;
import statementGraph.graphNode.StatementWrapper;
import statementGraph.graphNode.SuperConstructorInvocationStatementWrapper;
import statementGraph.graphNode.SwitchCaseStatementWrapper;
import statementGraph.graphNode.SwitchStatementWrapper;
import statementGraph.graphNode.SynchronizedStatementWrapper;
import statementGraph.graphNode.ThrowStatementWrapper;
import statementGraph.graphNode.TryStatementWrapper;
import statementGraph.graphNode.TypeDeclarationStatementWrapper;
import statementGraph.graphNode.VariableDeclarationStatementWrapper;
import statementGraph.graphNode.WhileStatementWrapper;

public class TextUtils {
	
	static private boolean removeKeyword = true;
	
	static private Set<String> javaKeyword = new HashSet<String>(Arrays.asList(new String[]{"abstract","continue","for","new","switch","assert",
			"default","goto","package","synchronized","boolean","do","if","private","this","break","double","implements","protected","throw",
			"byte","else","import","public","throws","case","enum","instanceof","return","transient","catch","extends","int","short","try","char",
			"final","interface","static","void","class","finally","long","strictfp","volatile","const","float","native","super","while"
		})); 
	
	static private boolean isAllUppercase(String str){
		for(int i=0; i<str.length(); i++){
			if(str.charAt(i)<='Z' && str.charAt(i)>='A'){
				continue;
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	
	static private boolean isAllLowercase(String str){
		for(int i=0; i<str.length(); i++){
			if(str.charAt(i)<='z' && str.charAt(i)>='a'){
				continue;
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	
	static private LinkedList<String> splitCamelNaming(String str){
		LinkedList<String> result = new LinkedList<String>();
		int start = 0;
		for(int i=1;i<str.length();i++){
			if(str.charAt(i)<='Z' && str.charAt(i)>='A'){
				result.add(str.substring(start,i).toLowerCase());
				start = i;
			}
		}
		result.add(str.substring(start,str.length()).toLowerCase());
		return result;
	}
	
	
	/*
	 * Parse code fragment into word list by java naming conventions
	 */
	static public LinkedList<String> parseCodeFragment(String codeFragment){
		LinkedList<String> result = new LinkedList<String>();
		//Remove operators;
		codeFragment = codeFragment.replace('+', ' ');
		codeFragment = codeFragment.replace('-', ' ');
		codeFragment = codeFragment.replace('*', ' ');
		codeFragment = codeFragment.replace('/', ' ');
		codeFragment = codeFragment.replace('%', ' ');
		codeFragment = codeFragment.replace('=', ' ');
		codeFragment = codeFragment.replace('!', ' ');
		codeFragment = codeFragment.replace('&', ' ');
		codeFragment = codeFragment.replace('|', ' ');
		codeFragment = codeFragment.replace('^', ' ');
		codeFragment = codeFragment.replace('~', ' ');
		codeFragment = codeFragment.replace('<', ' ');
		codeFragment = codeFragment.replace('>', ' ');
		codeFragment = codeFragment.replace('(', ' ');
		codeFragment = codeFragment.replace(')', ' ');
		codeFragment = codeFragment.replace('[', ' ');
		codeFragment = codeFragment.replace(']', ' ');
		codeFragment = codeFragment.replace('{', ' ');
		codeFragment = codeFragment.replace('}', ' ');
		codeFragment = codeFragment.replace('.', ' ');
		codeFragment = codeFragment.replace('?', ' ');
		codeFragment = codeFragment.replace(':', ' ');
		codeFragment = codeFragment.replace('_', ' ');
		codeFragment = codeFragment.replace(';', ' ');
		codeFragment = codeFragment.replace(',', ' ');
		//Remove number
		codeFragment = codeFragment.replace('0', ' ');
		codeFragment = codeFragment.replace('1', ' ');
		codeFragment = codeFragment.replace('2', ' ');
		codeFragment = codeFragment.replace('3', ' ');
		codeFragment = codeFragment.replace('4', ' ');
		codeFragment = codeFragment.replace('5', ' ');
		codeFragment = codeFragment.replace('6', ' ');
		codeFragment = codeFragment.replace('7', ' ');
		codeFragment = codeFragment.replace('8', ' ');
		codeFragment = codeFragment.replace('9', ' ');
		
		for(String word : codeFragment.split(" +")){
			if(isAllLowercase(word)){
				result.add(word);
			}
			else if(isAllUppercase(word)){
				result.add(word.toLowerCase());
			}
			else{
				result.addAll(splitCamelNaming(word));
			}
		}		
		return result;
	}
	
	
	/*
	 * Parse statement into word list by java naming conventions
	 */
	public static LinkedList<String> parseStatement(StatementWrapper item) throws Exception{
		LinkedList<String> result = new LinkedList<String>();
		int nodeType = item.getType();
		if(nodeType == StatementWrapper.ASSERT_STATEMENT){
			AssertStatement statement = ((AssertStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.toString()));
		}
		else if(nodeType == StatementWrapper.BREAK_STATEMENT){
			BreakStatement statement = ((BreakStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.toString()));
		}
		else if(nodeType == StatementWrapper.CONSTRUCTOR_INVOCATION){
			ConstructorInvocation statement = ((ConstructorInvocationStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.toString()));
		}
		else if(nodeType == StatementWrapper.CONTINUE_STATEMENT){
			ContinueStatement statement = ((ContinueStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.toString()));
		}
		else if(nodeType == StatementWrapper.DO_STATEMENT){
			DoStatement statement = ((DoStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.getExpression().toString()));
		}
		else if(nodeType == StatementWrapper.EMPTY_STATEMENT){
			;
		}
		else if(nodeType == StatementWrapper.ENHANCED_FOR_STATEMENT){
			EnhancedForStatement statement = ((EnhancedForStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.getParameter().toString()));
			result.addAll(parseCodeFragment(statement.getExpression().toString()));
		}
		else if(nodeType == StatementWrapper.EXPRESSION_STATEMENT){
			ExpressionStatement statement = ((ExpressionStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.toString()));
		}
		else if(nodeType == StatementWrapper.FOR_STATEMENT){
			ForStatement statement = ((ForStatementWrapper)item).getASTNode();
			for(int i = 0; i< statement.initializers().size(); i++){
				Expression exp = (Expression)statement.initializers().get(i);
				result.addAll(parseCodeFragment(exp.toString()));
			}
			if(statement.getExpression()!=null){
				result.addAll(parseCodeFragment(statement.getExpression().toString()));
			}
			for(int i = 0; i< statement.updaters().size(); i++){
				Expression exp = (Expression)statement.updaters().get(i);
				result.addAll(parseCodeFragment(exp.toString()));
			}
		}
		else if(nodeType == StatementWrapper.IF_STATEMENT){
			IfStatement statement = ((IfStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.getExpression().toString()));
		}
		else if(nodeType == StatementWrapper.LABELED_STATEMENT){
			LabeledStatement statement = ((LabeledStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.getLabel().toString()));
		}
		else if(nodeType == StatementWrapper.RETURN_STATEMENT){
			 ReturnStatement statement = ((ReturnStatementWrapper)item).getASTNode();
			 result.addAll(parseCodeFragment(statement.getExpression().toString()));
		}
		else if(nodeType == StatementWrapper.SUPER_CONSTRUCTOR_INVOCATION){
			SuperConstructorInvocation statement = ((SuperConstructorInvocationStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.toString()));
		}
		else if(nodeType == StatementWrapper.SWITCH_CASE){
			SwitchCase statement = ((SwitchCaseStatementWrapper)item).getASTNode();
			if(statement.getExpression()!=null){
				result.addAll(parseCodeFragment(statement.getExpression().toString()));
			}
		}
		else if(nodeType == StatementWrapper.SWITCH_STATEMENT){
			SwitchStatement statement = ((SwitchStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.getExpression().toString()));	
		}
		else if(nodeType == StatementWrapper.SYNCHRONIZED_STATEMENT){
			SynchronizedStatement statement = ((SynchronizedStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.getExpression().toString()));	
		}
		else if(nodeType == StatementWrapper.THROW_STATEMENT){
			ThrowStatement statement = ((ThrowStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.getExpression().toString()));
		}
		else if(nodeType == StatementWrapper.TRY_STATEMENT){
			TryStatement statement = ((TryStatementWrapper)item).getASTNode();
			for(Object o: statement.resources()){
				ASTNode node = (ASTNode) o;
				result.addAll(parseCodeFragment(node.toString())); 
			}
			for(Object o: statement.catchClauses()){
				CatchClause catchItem = (CatchClause)o;
				result.addAll(parseCodeFragment(catchItem.getException().toString()));	
			}
		}
		else if(nodeType == StatementWrapper.TYPE_DECLARATION_STATEMENT){
			TypeDeclarationStatement statement = ((TypeDeclarationStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.toString()));
		}
		else if(nodeType == StatementWrapper.VARIABLE_DECLARATION_STATEMENT){
			VariableDeclarationStatement statement = ((VariableDeclarationStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.toString()));
		}
		else if(nodeType == StatementWrapper.WHILE_STATEMENT){
			WhileStatement statement = ((WhileStatementWrapper)item).getASTNode();
			result.addAll(parseCodeFragment(statement.getExpression().toString()));
		}
		else{
			System.out.println("Unexpected Type in parseStatement(TextUtils): "+item.toString());
			throw new Exception("Unexpected Type:"+nodeType);
		}
		if(removeKeyword){
			int i = 0;
			while(i<result.size()){
				if(javaKeyword.contains(result.get(i))){
					result.remove(i);
				}
				else{
					i++;
				}
			}
		}
		return result;
	}
	
	
	public static void main(String[] args){
		String codeFragment = "private Map<StatementWrapper,Integer> index = new HashMap<StatementWrapper,Integer>();";
		for(String temp:parseCodeFragment(codeFragment)){
			System.out.println(temp);
		}
	}
}
