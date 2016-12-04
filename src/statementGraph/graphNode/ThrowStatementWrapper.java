package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.ThrowStatement;

public class ThrowStatementWrapper extends StatementWrapper{

	private ThrowStatement astNode; 
	
	public ThrowStatementWrapper(ThrowStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
	}
	
	public ThrowStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	public void printName() {
		System.out.print("Throw Statement: "+astNode.toString());
	}

	@Override
	public void printDebug() {
		System.out.print("Throw Statement: "+astNode.toString());
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
		return astNode.toString().split(System.getProperty("line.separator")).length;
	}

	@Override
	public String toString() {
		return astNode.toString();
	}

}
