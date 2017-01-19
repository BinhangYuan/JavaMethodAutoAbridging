package statementGraph.graphNode;


import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
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
	
	private List<List<StatementWrapper>> branchStatementsWrappers = new LinkedList<List<StatementWrapper>>();
	
	public List<List<StatementWrapper>> getStatementsWrappers(){
		return this.branchStatementsWrappers;
	}
	
	public void addBranchStatementsWrapper(List<StatementWrapper> item){
		this.branchStatementsWrappers.add(item);
	}
	
	private List<Boolean> isBlockList = new LinkedList<Boolean>();
	
	public List<Boolean> getIsBlockList(){
		return this.isBlockList;
	}
	
	public void addisBlockFlag(Boolean flag){
		this.isBlockList.add(flag);
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

	@Override
	public String computeOutput(int level) {
		String result = new String();
		result += super.computeIndent(level)+this.toString();
		Assert.isTrue(this.branchEntries.size() == this.branchStatementsWrappers.size() && 
				this.branchEntries.size() == this.isBlockList.size());
		for(int i=0; i< this.branchEntries.size();i++){
			StatementWrapper statementWrapper = this.branchEntries.get(i);
			if(statementWrapper.isDisplay()){
				result += statementWrapper.computeOutput(level);
				result += this.isBlockList.get(i)?" {\n":"\n";
			
				for(StatementWrapper nestedStatementWrapper: this.branchStatementsWrappers.get(i)){
					if(nestedStatementWrapper.isDisplay()){
						result += nestedStatementWrapper.computeOutput(level+1);
					}
				}
				result += this.isBlockList.get(i)?(super.computeIndent(level)+"}\n"):"";
			}
		}
		result += (super.computeIndent(level)+"}\n");
		return result;
	}
}