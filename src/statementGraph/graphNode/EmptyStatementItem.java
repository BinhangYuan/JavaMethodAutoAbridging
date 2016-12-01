package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.EmptyStatement;

//;
public class EmptyStatementItem extends ElementItem{

	private EmptyStatement astNode; 
	
	
	public EmptyStatementItem(EmptyStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public EmptyStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//This may be problematic
		super.lineCount = 0;
	}

	@Override
	public void printName() {
		System.out.print("Empty Statement: "+astNode.toString());
	}
	
	@Override
	public void printDebug() {
		System.out.print("Empty Statement: "+astNode.toString());
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

