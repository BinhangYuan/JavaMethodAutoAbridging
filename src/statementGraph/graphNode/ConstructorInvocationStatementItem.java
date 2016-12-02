package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.ConstructorInvocation;

//this ( [ Expression { , Expression } ] ) ;
public class ConstructorInvocationStatementItem extends ElementItem{

	private ConstructorInvocation astNode; 
	
	public ConstructorInvocationStatementItem(ConstructorInvocation astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
	}
	
	public ConstructorInvocation getASTNode(){
		return this.astNode;
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
		super.printDDGPredecessor();
	}
	
	@Override
	public String toString() {
		return astNode.toString();
	}

	@Override
	public int getLineCount() {
		return astNode.toString().split(System.getProperty("line.separator")).length;
	}
}

