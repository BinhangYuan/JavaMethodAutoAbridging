package statementGraph;

import org.eclipse.jdt.core.dom.EmptyStatement;

//;
public class EmptyStatementItem extends ElementItem{

	private EmptyStatement astNode; 
	
	
	public EmptyStatementItem(EmptyStatement astNode){
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
		this.astNode = astNode;
	}
	
	public EmptyStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//This may be problematic
		super.lineCount = 0;
	}


}

