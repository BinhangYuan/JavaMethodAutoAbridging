package statementGraph;

import org.eclipse.jdt.core.dom.AssertStatement;


public class AssertStatementItem extends ElementItem{
	
	private AssertStatement astNode; 
	
	public AssertStatementItem(AssertStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public AssertStatement getASTNode(){
		return this.astNode;
	}

	@Override
	protected void setLineCount(String code) {
		super.lineCount = code.split(System.getProperty("line.separator")).length;
	}
	
	@Override
	protected void printName() {
		System.out.print("Assert Statement: "+astNode.toString());
	}

	@Override
	protected void printDebug() {
		System.out.print("Assert Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
	}
}
