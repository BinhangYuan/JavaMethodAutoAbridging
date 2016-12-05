package statementGraph.graphNode;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.LabeledStatement;

public class LabeledStatementWrapper extends StatementWrapper{

	private LabeledStatement astNode; 
	
	public LabeledStatementWrapper(LabeledStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
	}
	
	public LabeledStatement getASTNode(){
		return this.astNode;
	}
	
	private List<StatementWrapper> bodyWrappers = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getBodyWrappers(){
		return this.bodyWrappers;
	}
	
	public void addBodyWrapper(StatementWrapper item){
		this.bodyWrappers.add(item);
	}

	@Override
	public void printName() {
		System.out.print("Labeled Statement: "+astNode.toString());
	}
	
	@Override
	public void printDebug() {
		System.out.print("Labeled Statement: "+astNode.toString());
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
		return this.astNode.getLabel().toString()+":\n";
	}
}

