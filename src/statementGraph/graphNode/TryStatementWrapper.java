package statementGraph.graphNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.TryStatement;


public class TryStatementWrapper extends StatementWrapper{

	private TryStatement astNode; 
	
	public TryStatement getASTNode(){
		return this.astNode;
	}

	private List<StatementWrapper> bodyWrappers = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getBodyWrappers(){
		return this.bodyWrappers;
	}
	
	public void addBodyWrapper(StatementWrapper item){
		this.bodyWrappers.add(item);
	}
	
	private List<StatementWrapper> finalBodyWrappers = new LinkedList<StatementWrapper>();
	
	public List<StatementWrapper> getFinalBodyWrappers(){
		return this.finalBodyWrappers;
	}
	
	public void addFinalBodyWrapper(StatementWrapper item){
		this.finalBodyWrappers.add(item);
	}
	
	private List<CatchClause> catchList = new LinkedList<CatchClause>();
	
	public List<CatchClause> getCatchList(){
		return this.catchList;
	}
	
	public TryStatementWrapper(TryStatement astNode){
		this.astNode = astNode;
		super.setType(astNode.getNodeType());
		this.catchList.addAll(astNode.catchClauses());
		for(CatchClause catchClause: this.catchList){
			this.catchMap.put(catchClause, new LinkedList<StatementWrapper>());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addCatchListAll(List items){
		this.catchList.addAll(items);
	}
	
	private Map<CatchClause,LinkedList<StatementWrapper>> catchMap = new HashMap<CatchClause,LinkedList<StatementWrapper>>();
	
	public void addCatchClauseWrapper(CatchClause catchItem, StatementWrapper item){
		this.catchMap.get(catchItem).add(item);
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
			result += "){\n";
		}
		return result;
	}

	@Override
	public String computeOutput(int level) {
		String result = new String();
		result = super.computeIndent(level);
		if(this.astNode.resources().isEmpty()){
			result += "try {\n";
		}
		else{
			result += "try (";
			for(Object o: this.astNode.resources()){
				ASTNode node = (ASTNode) o;
				result += node.toString(); 
			}
			result += "){\n";
		}
		//Handle body:
		for(StatementWrapper statementWrapper: this.bodyWrappers){
			if(statementWrapper.isDisplay()){
				result += statementWrapper.computeOutput(level+1);
			}
		}
		result += (super.computeIndent(level)+"} ");
		//Handle catches:
		if(this.catchList.size()!=0){
			for(CatchClause catchItem: this.catchList){
				result += "catch (" + catchItem.getException().toString() + "){\n";
				for(StatementWrapper statementWrapper: this.catchMap.get(catchItem)){
					if(statementWrapper.isDisplay()){
						result += statementWrapper.computeOutput(level+1);
					}
				}
				result += (super.computeIndent(level)+"} ");
			}
			
		}
		//Handle final:		
		if(this.astNode.getFinally()!=null){
			result += "finally {\n";
			for(StatementWrapper statementWrapper: this.finalBodyWrappers){
				if(statementWrapper.isDisplay()){
					result += statementWrapper.computeOutput(level+1);
				}
			}
			result += (super.computeIndent(level)+"} ");
		}
		result += '\n';
		return result;
	}
}
