package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.WhileStatement;

public class WhileStatementWrapper extends StatementWrapper{

	private WhileStatement astNode; 
	
	private StatementWrapper bodyEntry;
	
	private boolean bodyIsBlock;
	
	public void setBodyEntry(StatementWrapper item){
		this.bodyEntry = item;
	}
	
	public StatementWrapper getBodyEntry(){
		return this.bodyEntry;
	}
	
	public WhileStatementWrapper(WhileStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
	}
	
	public WhileStatement getASTNode(){
		return this.astNode;
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
	
	@Override
	public String toString() {
		return this.bodyIsBlock?
		"while("+this.astNode.getExpression().toString()+"){":
		"while("+this.astNode.getExpression().toString()+")";
	}

	@Override
	public int getLineCount() {
		return this.toString().split(System.getProperty("line.separator")).length + (this.bodyIsBlock?1:0);
	}
}
