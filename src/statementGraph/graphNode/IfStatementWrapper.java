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
	
	public int getThenBodyLength(){
		return this.thenBodyWrappers.size();
	}
	
	public void addThenBodyWrapper(StatementWrapper item){
		this.thenBodyWrappers.add(item);
	}
	
	private List<StatementWrapper> elseBodyWrappers = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getElseBodyWrappers(){
		return this.elseBodyWrappers;
	}
	
	public int getElseBodyLength(){
		return this.elseBodyWrappers.size();
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
		int count = this.astNode.getParent().getNodeType() == StatementWrapper.IF_STATEMENT?
				0:this.toString().split(System.getProperty("line.separator")).length;
		if(this.thenIsBlock){
			count += 1;
		}
		if(this.astNode.getElseStatement()!=null){
			count += ((this.thenIsBlock?0:1)+(this.elseIsBlock?1:0));
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
		return this.computeIfOutput(level, false);
	}
	
	private String computeIfOutput(int level, boolean nestedElseFlag){
		String result = new String();
		result = (nestedElseFlag?"":super.computeIndent(level))+"if("+ this.astNode.getExpression().toString() +")";
		if(this.thenIsBlock){
			result += "{";
		}
		if(!this.thenBodyWrappers.isEmpty() && !this.thenBodyWrappers.get(0).isDisplay()){
			result += "...";
		}
		result += '\n';
		for(int i=0; i<this.thenBodyWrappers.size();i++){
			StatementWrapper statementWrapper= this.thenBodyWrappers.get(i);
			if(statementWrapper.isDisplay()){
				result += statementWrapper.computeOutput(level+1);
				if(i<this.thenBodyWrappers.size()-1 && !this.thenBodyWrappers.get(i+1).isDisplay()){
					result += "...";
				}
				result += "\n";
			}
		}
		if(this.thenIsBlock){
			result += (this.astNode.getElseStatement()==null?(super.computeIndent(level)+"}"):(super.computeIndent(level)+"} "));
		}
		if(this.astNode.getElseStatement()!=null){
			result += (this.thenIsBlock?"else ":(super.computeIndent(level)+"else "));
			if(this.elseIsBlock){
				result += "{";
				if(!this.elseBodyWrappers.isEmpty() && !this.elseBodyWrappers.get(0).isDisplay()){
					result += "...";
				}
				result += '\n';
				for(int i=0; i<this.elseBodyWrappers.size();i++){
					StatementWrapper statementWrapper= this.elseBodyWrappers.get(i);
					if(statementWrapper.isDisplay()){
						result += statementWrapper.computeOutput(level+1);
						if(i<this.elseBodyWrappers.size()-1 && !this.elseBodyWrappers.get(i+1).isDisplay()){
							result += "...";
						}
						result += "\n";
					}
				}
				result += (super.computeIndent(level)+"}");
			}
			else{
				if(this.astNode.getElseStatement().getNodeType()==ASTNode.IF_STATEMENT){
					IfStatementWrapper nestedElse = (IfStatementWrapper) this.elseBodyWrappers.get(0);
					if(nestedElse.isDisplay()){
						result += nestedElse.computeIfOutput(level, true);
					}
					else{
						result += "...\n";
					}
				}
				else{
					
					if(this.elseBodyWrappers.get(0).isDisplay()){
						result += '\n';
						result += this.elseBodyWrappers.get(0).computeOutput(level+1);
					}
					else{
						result += "...";
					}
				}
			}
		}
		return result;
	}
}

