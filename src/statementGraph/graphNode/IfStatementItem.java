package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IfStatement;

public class IfStatementItem extends ElementItem{

	private IfStatement astNode; 
	
	private ElementItem thenEntry = null;
	private ElementItem elseEntry = null;
	
	private boolean thenIsBlock;
	private boolean elseIsBlock;
	
	public void setThenEntry(ElementItem item){
		this.thenEntry = item;
	}
	
	public ElementItem getThenEntry(){
		return this.thenEntry;
	}
	
	public void setElseEntry(ElementItem item){
		this.elseEntry = item;
	}
	
	public ElementItem getElseEntry(){
		return this.elseEntry;
	}
	
	public IfStatementItem(IfStatement astNode){
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
		if(this.thenIsBlock && this.astNode.getElseStatement()==null){
			count += 1;
		}
		else if(!this.thenIsBlock && this.astNode.getElseStatement()==null){
			//Nothing to be done;
		}
		else if(this.astNode.getElseStatement()!=null){
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
}

