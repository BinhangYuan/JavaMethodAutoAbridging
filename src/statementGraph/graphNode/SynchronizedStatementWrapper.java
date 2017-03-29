package statementGraph.graphNode;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.SynchronizedStatement;

//synchronized ( Expression ) Block
public class SynchronizedStatementWrapper extends StatementWrapper{

	private SynchronizedStatement astNode; 
	
	private List<StatementWrapper> bodyWrappers = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getBodyWrappers(){
		return this.bodyWrappers;
	}
	
	public void addBodyWrapper(StatementWrapper item){
		this.bodyWrappers.add(item);
	}
	
	public SynchronizedStatementWrapper(SynchronizedStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
	}
	
	public SynchronizedStatement getASTNode(){
		return this.astNode;
	}

	@Override
	public void printName() {
		System.out.print("Synchronized Statement: "+astNode.toString());
	}
	
	@Override
	public void printDebug() {
		System.out.print("Synchronized Statement: "+astNode.toString());
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
		return 2;
	}

	@Override
	public String toString() {
		return "synchronized (" + this.astNode.getExpression().toString() + "){";
	}

	@Override
	public String computeOutput(int level) {
		String result = new String();
		result = super.computeIndent(level)+this.toString();
		if(!this.bodyWrappers.isEmpty() && !this.bodyWrappers.get(0).isDisplay()){
			result += "...";
		}
		result += '\n';
		for(int i=0; i<this.bodyWrappers.size();i++){
			StatementWrapper statementWrapper = this.bodyWrappers.get(i);
			if(statementWrapper.isDisplay()){
				result += statementWrapper.computeOutput(level+1);
				if(i<this.bodyWrappers.size()-1 && !this.bodyWrappers.get(i+1).isDisplay()){
					result += "...";
				}
				result += "\n";
			}
		}
		result +=(super.computeIndent(level)+"}");
		return result;
	}
}
