package statementGraph;

import org.eclipse.jdt.core.dom.ExpressionStatement;

public class ExpressionStatementItem extends ElementItem{

	private ExpressionStatement astNode; 
	
	
	public ExpressionStatementItem(ExpressionStatement astNode){
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
		this.astNode = astNode;
	}
	
	public ExpressionStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		super.lineCount = code.split(System.getProperty("line.separator")).length;
	}
}

