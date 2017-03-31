package statementGraph.graphNode;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.WhileStatement;

public class WhileStatementWrapper extends StatementWrapper{

	private WhileStatement astNode; 
	
	private StatementWrapper bodyEntry;
	
	private boolean bodyIsBlock;
	
	public void setBodyEntry(StatementWrapper item){
		this.bodyEntry = item;
	}
	
	public StatementWrapper getBodyEntry(){
		return this.bodyEntry;
	}
	
	private List<StatementWrapper> bodyWrappers = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getBodyWrappers(){
		return this.bodyWrappers;
	}
	
	public int getBodyLength(){
		return this.bodyWrappers.size();
	}
	
	public void addBodyWrapper(StatementWrapper item){
		this.bodyWrappers.add(item);
	}
	
	public WhileStatementWrapper(WhileStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.bodyIsBlock = this.astNode.getBody().getNodeType() == ASTNode.BLOCK;
	}
	
	public WhileStatement getASTNode(){
		return this.astNode;
	}

	@Override
	public void printName() {
		System.out.print("While Statement: "+astNode.toString());
	}	
		
	@Override
	public void printDebug() {
		System.out.print("While Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
		System.out.println("Body entry: -->");
		if(bodyEntry == null){
			System.out.println("null");
		}else{
			this.bodyEntry.printName();
		}
		super.printDDGPredecessor();
	}
	
	@Override
	public String toString() {
		return this.bodyIsBlock?
		"while("+this.astNode.getExpression().toString()+"){":
		"while("+this.astNode.getExpression().toString()+")";
	}

	@Override
	public int getLineCount() {
		return this.toString().split(System.getProperty("line.separator")).length + (this.bodyIsBlock?1:0);
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
				if(this.bodyIsBlock){
					result += "\n";
				}
			}
		}
		if(this.bodyIsBlock){
			result +=(super.computeIndent(level)+"}");
		}
		return result;
	}
}
