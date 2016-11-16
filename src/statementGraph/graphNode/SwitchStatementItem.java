package statementGraph.graphNode;


import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.SwitchStatement;

public class SwitchStatementItem extends ElementItem{

	private SwitchStatement astNode; 
	private List<ElementItem> branchEntries = new LinkedList<ElementItem>();
	
	public void addBranchEntries(ElementItem item){
		this.branchEntries.add(item);
	}
	
	public List<ElementItem> getBranchEntries(){
		return this.branchEntries;
	}
	
	public SwitchStatementItem(SwitchStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.setLineCount(astNode.toString());
	}
	
	public SwitchStatement getASTNode(){
		return this.astNode;
	}
	
	@Override
	protected void setLineCount(String code) {
		//It should be the length excluding the body.
		super.lineCount = code.split(System.getProperty("line.separator")).length; 
	}
	
	@Override
	public void printName() {
		System.out.print("Switch Statement: "+astNode.toString());
	}

	@Override
	public void printDebug() {
		System.out.print("Switch Statement: "+astNode.toString());
		System.out.println("Successor: -->");
		if(super.getCFGSeqSuccessor() == null){
			System.out.println("null");
		}
		else{
			super.getCFGSeqSuccessor().printName();
		}
		for(ElementItem e:this.branchEntries){
			System.out.println("Body entry: ");
			e.printName();
		}
	}
}