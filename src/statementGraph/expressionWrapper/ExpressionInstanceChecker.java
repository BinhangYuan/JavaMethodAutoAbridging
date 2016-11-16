package statementGraph.expressionWrapper;

import org.eclipse.jdt.core.dom.Expression;

public class ExpressionInstanceChecker{
	private int type;
	
	public ExpressionInstanceChecker(int type){
		this.type = type;
	}
	
	public boolean instanceOf(Expression e){
		return e.getNodeType()==this.type;
	}
}
