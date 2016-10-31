package statementGraph;


import org.eclipse.jdt.core.dom.SwitchStatement;

public class SwitchStatementItem extends ElementItem{

	private SwitchStatement astNode; 
	
	public SwitchStatementItem(SwitchStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public SwitchStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = code.split(System.getProperty("line.separator")).length; 
	}

	@Override
	protected void print() {
		System.out.print("Switch Statement: "+astNode.toString());
	}
}

