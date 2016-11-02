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
	protected void printName() {
		System.out.print("Do Statement: "+astNode.toString());
	}

	@Override
	protected void printDebug() {
		System.out.print("Do Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getSeqSuccessor().printName();
		}
		System.out.println("Body entry: -->");
		if(bodyEntry == null){
			System.out.println("null");
		}else{
			this.bodyEntry.printName();
		}
	}
}

