package statementGraph.graphNode;


import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class VariableDeclarationStatementWrapper extends StatementWrapper{

	private VariableDeclarationStatement astNode; 
	
	public VariableDeclarationStatementWrapper(VariableDeclarationStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
	}
	
	public VariableDeclarationStatement getASTNode(){
		return this.astNode;
	}

	@Override
	public void printName() {
		System.out.print("Variable Declaration Statement: "+astNode.toString());
	}
	
	@Override
	public void printDebug() {
		System.out.print("Variable Declaration Statement: "+astNode.toString());
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
	public String toString() {
		return astNode.toString();
	}

	@Override
	public int getLineCount() {
		return astNode.toString().split(System.getProperty("line.separator")).length;
	}
}