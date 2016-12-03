package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.SynchronizedStatement;

//synchronized ( Expression ) Block
public class SynchronizedStatementItem extends ElementItem{

	private SynchronizedStatement astNode; 
	
	public SynchronizedStatementItem(SynchronizedStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
	}
	
	public SynchronizedStatement getASTNode(){
		return this.astNode;
	}

	@Override
	public void printName() {
		System.out.print("Synchronized Statement: "+astNode.toString());
	}
	
	@Override
	public void printDebug() {
		System.out.print("Synchronized Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
		super.printDDGPredecessor();
	}

	@Override
	public int getLineCount() {
		return 2;
	}

	@Override
	public String toString() {
		return "synchronized (" + this.astNode.getExpression().toString() + "){\n";
	}
}
