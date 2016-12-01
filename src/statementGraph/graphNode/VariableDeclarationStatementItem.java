package statementGraph.graphNode;


import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class VariableDeclarationStatementItem extends ElementItem{

	private VariableDeclarationStatement astNode; 
	
	public VariableDeclarationStatementItem(VariableDeclarationStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public VariableDeclarationStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = code.split(System.getProperty("line.separator")).length;
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
}