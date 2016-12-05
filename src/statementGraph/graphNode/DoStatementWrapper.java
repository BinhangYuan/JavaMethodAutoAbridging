package statementGraph.graphNode;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.DoStatement;

public class DoStatementWrapper extends StatementWrapper{
	
	private DoStatement astNode;
	
	private boolean bodyIsBlock;
	
	private StatementWrapper bodyEntry = null;
	
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
	
	public void addBodyWrapper(StatementWrapper item){
		this.bodyWrappers.add(item);
	}
	
	public DoStatementWrapper(DoStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.bodyIsBlock = this.astNode.getBody().getNodeType() == ASTNode.BLOCK;
	}
	
	public DoStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	public void printName() {
		System.out.print("Do Statement: "+astNode.toString());
	}

	@Override
	public void printDebug() {
		System.out.print("Do Statement: "+astNode.toString());
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
		"do{\n}while("+this.astNode.getExpression().toString()+")":
		"do\nwhile("+this.astNode.getExpression().toString()+")";
	}

	@Override
	public int getLineCount() {
		return this.toString().split(System.getProperty("line.separator")).length;
	}

	@Override
	public String computeOutput() {
		String result = new String();
		result = "do";
		if(this.bodyIsBlock){
			result +='{';
		}
		result+='\n';
		for(StatementWrapper statementWrapper: this.bodyWrappers){
			if(statementWrapper.isDisplay()){
				result += statementWrapper.computeOutput();
			}
		}
		if(this.bodyIsBlock){
			result +='}';
		}
		result += "while("+this.astNode.getExpression().toString()+")";
		result+='\n';
		return result;
	}
}

