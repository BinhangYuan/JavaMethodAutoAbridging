package statementGraph;


import org.eclipse.jdt.core.dom.TypeDeclarationStatement;

public class TypeDeclarationStatementItem extends ElementItem{

	private TypeDeclarationStatement astNode; 
	
	public TypeDeclarationStatementItem(TypeDeclarationStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public TypeDeclarationStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = code.split(System.getProperty("line.separator")).length;
	}

	@Override
	protected void printName() {
		System.out.print("Type Declaration Statement: "+astNode.toString());
	}
	
	@Override
	protected void printDebug() {
		System.out.print("Type Declaration Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getSeqSuccessor().printName();
		}
	}

}