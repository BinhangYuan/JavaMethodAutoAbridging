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
	protected void print() {
		System.out.print("Assert Statement: "+astNode.toString());
	}
}
