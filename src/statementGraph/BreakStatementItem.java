package statementGraph;

import org.eclipse.jdt.core.dom.BreakStatement;


public class BreakStatementItem extends ElementItem{

	private BreakStatement astNode; 
	
	
	public BreakStatementItem(BreakStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public BreakStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		super.lineCount = code.split(System.getProperty("line.separator")).length;
	}
	
	@Override
	protected void printName() {
		System.out.print("Break Statement: "+astNode.toString());
	}

	@Override
	protected void printDebug() {
		System.out.print("Break Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
	}
}

