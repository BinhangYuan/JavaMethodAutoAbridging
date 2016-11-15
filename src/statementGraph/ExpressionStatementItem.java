package statementGraph;

import org.eclipse.jdt.core.dom.ExpressionStatement;

public class ExpressionStatementItem extends ElementItem{

	private ExpressionStatement astNode; 
	
	
	public ExpressionStatementItem(ExpressionStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public ExpressionStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		super.lineCount = code.split(System.getProperty("line.separator")).length;
	}
	
	@Override
	protected void printName() {
		System.out.print("Expression Statement: "+astNode.toString());
	}

	@Override
	protected void printDebug() {
		System.out.print("Expression Statement: "+astNode.toString());	
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
	}
}

