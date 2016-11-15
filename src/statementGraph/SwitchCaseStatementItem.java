package statementGraph;

import org.eclipse.jdt.core.dom.SwitchCase;


public class SwitchCaseStatementItem extends ElementItem{

	private SwitchCase astNode; 
	
	public SwitchCaseStatementItem(SwitchCase astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public SwitchCase getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = code.split(System.getProperty("line.separator")).length; 
	}

	@Override
	protected void printName() {
		System.out.print("Switch Case Statement: "+astNode.toString());
	}
	
	@Override
	protected void printDebug() {
		System.out.print("Switch Case Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
	}
}