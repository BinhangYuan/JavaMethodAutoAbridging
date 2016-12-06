package statementGraph.graphNode;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IfStatement;

public class IfStatementWrapper extends StatementWrapper{

	private IfStatement astNode; 
	
	private StatementWrapper thenEntry = null;
	private StatementWrapper elseEntry = null;
	
	private boolean thenIsBlock;
	private boolean elseIsBlock;
	
	public void setThenEntry(StatementWrapper item){
		this.thenEntry = item;
	}
	
	public StatementWrapper getThenEntry(){
		return this.thenEntry;
	}
	
	public void setElseEntry(StatementWrapper item){
		this.elseEntry = item;
	}
	
	public StatementWrapper getElseEntry(){
		return this.elseEntry;
	}
	
	private List<StatementWrapper> thenBodyWrappers = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getThenBodyWrappers(){
		return this.thenBodyWrappers;
	}
	
	public void addThenBodyWrapper(StatementWrapper item){
		this.thenBodyWrappers.add(item);
	}
	
	private List<StatementWrapper> elseBodyWrappers = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getElseBodyWrappers(){
		return this.elseBodyWrappers;
	}
	
	public void addElseBodyWrapper(StatementWrapper item){
		this.elseBodyWrappers.add(item);
	}
	
	public IfStatementWrapper(IfStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		if(this.astNode.getElseStatement()==null){
			this.elseIsBlock = false;
		}
		else{
			this.elseIsBlock = this.astNode.getElseStatement().getNodeType() == ASTNode.BLOCK;
		}
		this.thenIsBlock = this.astNode.getThenStatement().getNodeType() == ASTNode.BLOCK;
	}
	
	public IfStatement getASTNode(){
		return this.astNode;
	}
	
	
	@Override
	public void printName() {
		System.out.print("If Statement: "+astNode.toString());
	}

	@Override
	public void printDebug() {
		System.out.print("If Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}else{
			super.getCFGSeqSuccessor().printName();
		}
		System.out.println("Then entry: -->");
		if(thenEntry == null){
			System.out.println("null");
		}else{
			this.thenEntry.printName();
		}
		System.out.println("Else entry: -->");
		if(elseEntry == null){
			System.out.println("null");
		}else{
			this.elseEntry.printName();
		}
		super.printDDGPredecessor();
	}

	@Override
	public int getLineCount() {
		int count = this.toString().split(System.getProperty("line.separator")).length;
		if(this.thenIsBlock){
			count += 1;
		}
		if(this.astNode.getElseStatement()!=null){
			count += (this.elseIsBlock?2:1);
		}
		return count;
	}

	@Override
	public String toString() {
		//This may be updated later;
		String result = new String();
		result = "if("+ this.astNode.getExpression().toString() +")";
		if(this.thenIsBlock){
			result += "{";
		}
		return result;
	}

	@Override
	public String computeOutput(int level) {
		String result = new String();
		result = super.computeIndent(level)+"if("+ this.astNode.getExpression().toString() +")";
		if(this.thenIsBlock){
			result += "{";
		}
		result += '\n';
		for(StatementWrapper statementWrapper: this.thenBodyWrappers){
			if(statementWrapper.isDisplay()){
				result += statementWrapper.computeOutput(level+1);
			}
		}
		if(this.thenIsBlock){
			result += (super.computeIndent(level)+"}");
		}
		result += '\n';
		if(this.astNode.getElseStatement()!=null){
			result += (super.computeIndent(level)+"else ");
			if(this.elseIsBlock){
				result += "{\n";
				for(StatementWrapper statementWrapper: this.elseBodyWrappers){
					if(statementWrapper.isDisplay()){
						result += statementWrapper.computeOutput(level+1);
					}
				}
				result += (super.computeIndent(level)+'}');
			}
			else{
				if(this.astNode.getElseStatement().getFlags()!=ASTNode.IF_STATEMENT){
					result += '\n';
				}
				if(this.elseBodyWrappers.get(0).isDisplay()){
					result += this.elseBodyWrappers.get(0).computeOutput(level+1);
				}
			}
			result += '\n';
		}
		return result;
	}
}

