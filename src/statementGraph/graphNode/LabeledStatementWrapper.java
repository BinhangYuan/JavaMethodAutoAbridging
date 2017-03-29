package statementGraph.graphNode;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.LabeledStatement;

public class LabeledStatementWrapper extends StatementWrapper{

	private LabeledStatement astNode; 
	
	private boolean bodyIsBlock;
	
	public LabeledStatementWrapper(LabeledStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.bodyIsBlock = this.astNode.getBody().getNodeType() == ASTNode.BLOCK;
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
		return this.astNode.getLabel().toString().split(System.getProperty("line.separator")).length + (this.bodyIsBlock?1:0);
	}

	@Override
	public String toString() {
		return this.astNode.getLabel().toString()+":\n";
	}

	@Override
	public String computeOutput(int level) {
		String result = super.computeIndent(level)+this.astNode.getLabel().toString()+" :";
		if(this.bodyIsBlock){
			result += '{';
		}
		if(!this.bodyWrappers.get(0).isDisplay()){
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
		if(this.bodyIsBlock){
			result += "}";
		}
		return result;
	}
}