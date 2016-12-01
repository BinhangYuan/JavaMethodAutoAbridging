package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.IfStatement;

public class IfStatementItem extends ElementItem{

	private IfStatement astNode; 
	
	private ElementItem thenEntry = null;
	private ElementItem elseEntry = null;
	
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
		this.setLineCount(astNode.toString());
	}
	
	public IfStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		int total = code.split(System.getProperty("line.separator")).length;
		int thenblock = (astNode.getThenStatement()==null ? 0 : astNode.getThenStatement().toString().split(System.getProperty("line.separator")).length);
		int elseblock = (astNode.getElseStatement()==null ? 0 : astNode.getElseStatement().toString().split(System.getProperty("line.separator")).length);
		super.lineCount = total - thenblock - elseblock; //Maybe problematic, check again! 
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
		}
		else{
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
}

