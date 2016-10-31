package statementGraph;

import org.eclipse.jdt.core.dom.IfStatement;

public class IfStatementItem extends ElementItem{

	private IfStatement astNode; 
	
	public IfStatementItem(IfStatement astNode){
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
		this.astNode = astNode;
	}
	
	public IfStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		int total = code.split(System.getProperty("line.separator")).length;
		int thenblock = astNode.getThenStatement()==null ? 0 : astNode.getThenStatement().toString().split(System.getProperty("line.separator")).length;
		int elseblock = astNode.getElseStatement()==null ? 0 : astNode.getElseStatement().toString().split(System.getProperty("line.separator")).length;
		super.lineCount = total - thenblock - elseblock; //Maybe problematic, check again! 
	}

}

