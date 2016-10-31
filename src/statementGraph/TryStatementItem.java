package statementGraph;

import org.eclipse.jdt.core.dom.TryStatement;


public class TryStatementItem extends ElementItem{

	private TryStatement astNode; 
	
	public TryStatementItem(TryStatement astNode){
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
		this.astNode = astNode;
	}
	
	public TryStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = code.split(System.getProperty("line.separator")).length;
	}

}
