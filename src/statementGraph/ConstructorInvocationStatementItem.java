package statementGraph;

import org.eclipse.jdt.core.dom.ConstructorInvocation;

//this ( [ Expression { , Expression } ] ) ;
public class ConstructorInvocationStatementItem extends ElementItem{

	private ConstructorInvocation astNode; 
	
	
	public ConstructorInvocationStatementItem(ConstructorInvocation astNode){
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
		this.astNode = astNode;
	}
	
	public ConstructorInvocation getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		super.lineCount = code.split(System.getProperty("line.separator")).length;	
	}
}

