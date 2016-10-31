package statementGraph;


import org.eclipse.jdt.core.dom.ForStatement;


public class ForStatementItem extends ElementItem{

	private ForStatement astNode; 
	
	public ForStatementItem(ForStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public ForStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		int total = code.split(System.getProperty("line.separator")).length;
		int body = astNode.getBody().toString().split(System.getProperty("line.separator")).length;
		super.lineCount = total - body; //Maybe problematic, check again! 
	}

	@Override
	protected void print() {
		System.out.print("For Statement: "+astNode.toString());
	}
}

