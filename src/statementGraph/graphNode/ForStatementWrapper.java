package statementGraph.graphNode;


import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ForStatement;

import org.eclipse.jdt.core.dom.Expression;


public class ForStatementWrapper extends StatementWrapper{

	private ForStatement astNode; 
	
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
	
	public ForStatementWrapper(ForStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.bodyIsBlock = this.astNode.getBody().getNodeType() == ASTNode.BLOCK;
	}
	
	public ForStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	public void printName() {
		System.out.print("For Statement: "+astNode.toString());
	}

	@Override
	public void printDebug() {
		System.out.print("For Statement: "+astNode.toString());
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
		String forInit = new String();
		for(int i = 0; i< this.astNode.initializers().size(); i++){
			Expression exp = (Expression)this.astNode.initializers().get(i);
			if(i==0){
				forInit = exp.toString();
			}
			else{
				forInit += (", "+exp.toString());
			}
		}
		String forUpdate = new String();
		for(int i = 0; i< this.astNode.updaters().size(); i++){
			Expression exp = (Expression)this.astNode.updaters().get(i);
			if(i==0){
				forUpdate = exp.toString();
			}
			else{
				forUpdate += (", "+exp.toString());
			}
		}
		return this.bodyIsBlock?
		"for ("+forInit+"; "+ this.astNode.getExpression()==null?"":this.astNode.getExpression().toString()+"; "+ forUpdate +"){":
		"for ("+forInit+"; "+ this.astNode.getExpression()==null?"":this.astNode.getExpression().toString()+"; "+ forUpdate +")";
	}
	
	@Override
	public String computeOutput(int level) {
		String result = super.computeIndent(level)+this.toString();
		result += '\n';
		for(StatementWrapper statementWrapper: this.bodyWrappers){
			if(statementWrapper.isDisplay()){
				result += statementWrapper.computeOutput(level+1);
			}
		}
		if(this.bodyIsBlock){
			result +=(super.computeIndent(level)+"}\n");
		}
		return result;
	}
}

