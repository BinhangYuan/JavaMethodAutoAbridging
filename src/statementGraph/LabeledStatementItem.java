package statementGraph;

import org.eclipse.jdt.core.dom.LabeledStatement;

public class LabeledStatementItem extends ElementItem{

	private LabeledStatement astNode; 
	
	public LabeledStatementItem(LabeledStatement astNode){
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
		this.astNode = astNode;
	}
	
	public LabeledStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = 1; //This may be problematic.
	}
}

