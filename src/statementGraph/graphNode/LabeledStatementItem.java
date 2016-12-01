package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.LabeledStatement;

public class LabeledStatementItem extends ElementItem{

	private LabeledStatement astNode; 
	
	public LabeledStatementItem(LabeledStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public LabeledStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = 1; //This may be problematic.
	}

	@Override
	public void printName() {
		System.out.print("Labeled Statement: "+astNode.toString());
	}
	
	@Override
	public void printDebug() {
		System.out.print("Labeled Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
		super.printDDGPredecessor();
	}
}

