package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.SwitchCase;


public class SwitchCaseStatementWrapper extends StatementWrapper{

	private SwitchCase astNode; 
	
	public SwitchCaseStatementWrapper(SwitchCase astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
	}
	
	public SwitchCase getASTNode(){
		return this.astNode;
	}

	@Override
	public void printName() {
		System.out.print("Switch Case Statement: "+astNode.toString());
	}
	
	@Override
	public void printDebug() {
		System.out.print("Switch Case Statement: "+astNode.toString());
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
	public int getLineCount() {
		return 1;
	}

	@Override
	public String toString() {
		return this.astNode.getExpression()==null?
		"default:":
		"case " + this.astNode.getExpression() + ": "; 
	}

	@Override
	public String computeOutput(int level) {
		return super.computeIndent(level)+this.toString();
	}	
}