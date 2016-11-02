package statementGraph;

import org.eclipse.jdt.core.dom.DoStatement;

public class DoStatementItem extends ElementItem{
	
	private DoStatement astNode; 
	
	private ElementItem bodyEntry = null;
	
	public void setBodyEntry(ElementItem item){
		this.bodyEntry = item;
	}
	
	public ElementItem getBodyEntry(){
		return this.bodyEntry;
	}
	
	public DoStatementItem(DoStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
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

	@Override
	protected void print() {
		System.out.print("Do Statement: "+astNode.toString());
	}
}

