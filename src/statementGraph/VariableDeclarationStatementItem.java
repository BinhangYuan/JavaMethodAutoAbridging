package statementGraph;


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
	protected void print() {
		System.out.print("Variable Declaration Statement: "+astNode.toString());
	}
}