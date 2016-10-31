package statementGraph;

import org.eclipse.jdt.core.dom.DoStatement;

public class DoStatementItem extends ElementItem{
	
	private DoStatement astNode; 
	
	public DoStatementItem(DoStatement astNode){
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
		this.astNode = astNode;
	}
	
	public DoStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		int total = code.split(System.getProperty("line.separator")).length;
		int body = astNode.getBody().toString().split(System.getProperty("line.separator")).length;
		super.lineCount = total - body; //Maybe problematic, check again! 
	}
}

