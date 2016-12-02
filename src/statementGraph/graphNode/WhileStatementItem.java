package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.WhileStatement;

public class WhileStatementItem extends ElementItem{

	private WhileStatement astNode; 
	
	private ElementItem bodyEntry;
	
	private boolean bodyIsBlock;
	
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
	public void printName() {
		System.out.print("While Statement: "+astNode.toString());
	}	
		
	@Override
	public void printDebug() {
		System.out.print("While Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
		System.out.println("Body entry: -->");
		if(bodyEntry == null){
			System.out.println("null");
		}else{
			this.bodyEntry.printName();
		}
		super.printDDGPredecessor();
	}
}
