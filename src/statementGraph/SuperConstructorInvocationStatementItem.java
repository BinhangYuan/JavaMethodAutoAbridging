package statementGraph;

import org.eclipse.jdt.core.dom.SuperConstructorInvocation;

//[ Expression . ] super( [ Expression { , Expression } ] ) ;
public class SuperConstructorInvocationStatementItem extends ElementItem{

	private SuperConstructorInvocation astNode; 
	
	
	public SuperConstructorInvocationStatementItem(SuperConstructorInvocation astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public SuperConstructorInvocation getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		super.lineCount = code.split(System.getProperty("line.separator")).length;	
	}

	@Override
	protected void printName() {
		System.out.print("Super Constructor Invocation Statement: "+astNode.toString());
	}
	
	@Override
	protected void printDebug() {
		System.out.print("Super Constructor Invocation Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
	}
}
