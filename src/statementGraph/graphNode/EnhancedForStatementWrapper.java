package statementGraph.graphNode;


import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.EnhancedForStatement;

public class EnhancedForStatementWrapper extends StatementWrapper{

	private EnhancedForStatement astNode; 
	
	private StatementWrapper bodyEntry;
	
	private boolean bodyIsBlock;
	
	private List<StatementWrapper> bodyWrappers = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getBodyWrappers(){
		return this.bodyWrappers;
	}
	
	public void addBodyWrapper(StatementWrapper item){
		this.bodyWrappers.add(item);
	}
	
	public void setBodyEntry(StatementWrapper item){
		this.bodyEntry = item;
	}
	
	public StatementWrapper getBodyEntry(){
		return this.bodyEntry;
	}
	
	public EnhancedForStatementWrapper(EnhancedForStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.bodyIsBlock = this.astNode.getBody().getNodeType() == ASTNode.BLOCK;
	}
	
	public EnhancedForStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	public void printName() {
		System.out.print("Enhanced For Statement: "+astNode.toString());
	}

	@Override
	public void printDebug() {
		System.out.print("Enhanced For Statement: "+astNode.toString());
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
	public int getLineCount() {
		return this.toString().split(System.getProperty("line.separator")).length + (this.bodyIsBlock?1:0);
	}

	@Override
	public String toString() {
		 return this.bodyIsBlock?
		 "for ("+this.astNode.getParameter().toString()+" : "+this.astNode.getExpression().toString()+"){":
		 "for ("+this.astNode.getParameter().toString()+" : "+this.astNode.getExpression().toString()+")";
	}

	@Override
	public String computeOutput(int level) {
		String result = new String();
		result = super.computeIndent(level)+"for ("+this.astNode.getParameter().toString()+" : "+this.astNode.getExpression().toString()+")";
		if(this.bodyIsBlock){
			result += '{';
		}
		result += '\n';
		for(StatementWrapper statementWrapper: this.bodyWrappers){
			if(statementWrapper.isDisplay()){
				result += statementWrapper.computeOutput(level+1);
			}
		}
		if(this.bodyIsBlock){
			result +=(super.computeIndent(level)+'}');
		}
		result+='\n';
		return result;
	}
}

