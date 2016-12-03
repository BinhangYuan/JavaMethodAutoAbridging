package statementGraph.graphNode;


import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnhancedForStatement;

public class EnhancedForStatementItem extends ElementItem{

	private EnhancedForStatement astNode; 
	
	private ElementItem bodyEntry;
	
	private boolean bodyIsBlock;
	
	public void setBodyEntry(ElementItem item){
		this.bodyEntry = item;
	}
	
	public ElementItem getBodyEntry(){
		return this.bodyEntry;
	}
	
	public EnhancedForStatementItem(EnhancedForStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.bodyIsBlock = this.astNode.getBody().getNodeType() == ASTNode.BLOCK;
	}
	
	public EnhancedForStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	public void printName() {
		System.out.print("Enhanced For Statement: "+astNode.toString());
	}

	@Override
	public void printDebug() {
		System.out.print("Enhanced For Statement: "+astNode.toString());
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

	@Override
	public int getLineCount() {
		return this.toString().split(System.getProperty("line.separator")).length + (this.bodyIsBlock?1:0);
	}

	@Override
	public String toString() {
		 return this.bodyIsBlock?
		 "for ("+this.astNode.getParameter().toString()+" : "+this.astNode.getExpression().toString()+"){":
		 "for ("+this.astNode.getParameter().toString()+" : "+this.astNode.getExpression().toString()+")";
	}
}
