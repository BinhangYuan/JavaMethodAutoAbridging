package statementGraph.graphNode;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.TryStatement;


public class TryStatementItem extends ElementItem{

	private TryStatement astNode; 
	
	public TryStatementItem(TryStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
	}
	
	public TryStatement getASTNode(){
		return this.astNode;
	}

	@Override
	public void printName() {
		System.out.print("Try Statement: "+astNode.toString());
	}
	
	@Override
	public void printDebug() {
		System.out.print("Try Statement: "+astNode.toString());
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
		//stub for now, may not work;
		int count = this.toString().split(System.getProperty("line.separator")).length;
		count += this.astNode.catchClauses().size();
		if(this.astNode.getFinally()!=null){
			count += 1;
		}
		return count;
	}

	@Override
	public String toString() {
		//This may be problematic! stub here for now.
		String result = new String();
		if(this.astNode.resources().isEmpty()){
			result = "try {\n";
		}
		else{
			result = "try (";
			for(Object o: this.astNode.resources()){
				ASTNode node = (ASTNode) o;
				result += node.toString(); 
			}
			result += "{\n";
		}
		return result;
	}
}
