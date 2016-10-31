package statementGraph;

import org.eclipse.jdt.core.dom.SwitchCase;


public class SwitchCaseStatementItem extends ElementItem{

	private SwitchCase astNode; 
	
	public SwitchCaseStatementItem(SwitchCase astNode){
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
		this.astNode = astNode;
	}
	
	public SwitchCase getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = code.split(System.getProperty("line.separator")).length; 
	}
}