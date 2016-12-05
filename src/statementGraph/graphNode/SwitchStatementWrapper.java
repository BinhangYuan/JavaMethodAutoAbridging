package statementGraph.graphNode;


import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.SwitchStatement;

public class SwitchStatementWrapper extends StatementWrapper{

	private SwitchStatement astNode; 
	private List<StatementWrapper> branchEntries = new LinkedList<StatementWrapper>();
	
	public void addBranchEntries(StatementWrapper item){
		this.branchEntries.add(item);
	}
	
	public List<StatementWrapper> getBranchEntries(){
		return this.branchEntries;
	}
	
	public SwitchStatementWrapper(SwitchStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
	}
	
	public SwitchStatement getASTNode(){
		return this.astNode;
	}
	
	private List<StatementWrapper> statementsWrappers = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getStatementsWrappers(){
		return this.statementsWrappers;
	}
	
	public void addStatementsWrapper(StatementWrapper item){
		this.statementsWrappers.add(item);
	}
	
	@Override
	public void printName() {
		System.out.print("Switch Statement: "+astNode.toString());
	}

	@Override
	public void printDebug() {
		System.out.print("Switch Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
		for(StatementWrapper e:this.branchEntries){
			System.out.println("Body entry: ");
			e.printName();
		}
		super.printDDGPredecessor();
	}

	@Override
	public int getLineCount() {
		return 2;
	}

	@Override
	public String toString() {
		return "switch (" + this.astNode.getExpression() + "){\n";
	}
}