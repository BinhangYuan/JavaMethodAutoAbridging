package statementGraph;

import org.eclipse.jdt.core.dom.SynchronizedStatement;

//synchronized ( Expression ) Block
public class SynchronizedStatementItem extends ElementItem{

	private SynchronizedStatement astNode; 
	
	public SynchronizedStatementItem(SynchronizedStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public SynchronizedStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = 1; //This may be problematic.
	}

	@Override
	protected void print() {
		System.out.print("Synchronized Statement: "+astNode.toString());
	}
}
