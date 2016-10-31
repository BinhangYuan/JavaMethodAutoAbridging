package statementGraph;


import org.eclipse.jdt.core.dom.TypeDeclarationStatement;

public class TypeDeclarationStatementItem extends ElementItem{

	private TypeDeclarationStatement astNode; 
	
	public TypeDeclarationStatementItem(TypeDeclarationStatement astNode){
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
		this.astNode = astNode;
	}
	
	public TypeDeclarationStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = code.split(System.getProperty("line.separator")).length;
	}

}