package statementGraph;

import org.eclipse.jdt.core.dom.ReturnStatement;


public class ReturnStatementItem extends ElementItem{

	private ReturnStatement astNode; 
	
	
	public ReturnStatementItem(ReturnStatement astNode){
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
		this.astNode = astNode;
	}
	
	public ReturnStatement getASTNode(){
		return this.astNode;
	}

	@Override
	protected void setLineCount(String code) {
		super.lineCount = code.split(System.getProperty("line.separator")).length;
	}
}

