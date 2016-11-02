package statementGraph;


import org.eclipse.jdt.core.dom.EnhancedForStatement;

public class EnhancedForStatementItem extends ElementItem{

	private EnhancedForStatement astNode; 
	
	private ElementItem bodyEntry;
	
	public void setBodyEntry(ElementItem item){
		this.bodyEntry = item;
	}
	
	public ElementItem getBodyEntry(){
		return this.bodyEntry;
	}
	
	public EnhancedForStatementItem(EnhancedForStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public EnhancedForStatement getASTNode(){
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
		System.out.print("Enhanced For Statement: "+astNode.toString());
	}
}

