package statementGraph;

import org.eclipse.jdt.core.dom.WhileStatement;

public class WhileStatementItem extends ElementItem{

	private WhileStatement astNode; 
	
	private ElementItem bodyEntry;
	
	public void setBodyEntry(ElementItem item){
		this.bodyEntry = item;
	}
	
	public ElementItem getBodyEntry(){
		return this.bodyEntry;
	}
	
	public WhileStatementItem(WhileStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public WhileStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = code.split(System.getProperty("line.separator")).length;
	}

	@Override
	protected void print() {
		System.out.print("While Statement: "+astNode.toString());
	}
}
