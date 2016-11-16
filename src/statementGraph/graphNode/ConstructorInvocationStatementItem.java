package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.ConstructorInvocation;

//this ( [ Expression { , Expression } ] ) ;
public class ConstructorInvocationStatementItem extends ElementItem{

	private ConstructorInvocation astNode; 
	
	
	public ConstructorInvocationStatementItem(ConstructorInvocation astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public ConstructorInvocation getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		super.lineCount = code.split(System.getProperty("line.separator")).length;	
	}

	@Override
	public void printName() {
		System.out.print("Constructor Invocation Statement: "+astNode.toString());
	}
	
	@Override
	public void printDebug() {
		System.out.print("Constructor Invocation Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
	}
}

