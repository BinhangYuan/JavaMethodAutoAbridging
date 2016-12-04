package statementGraph;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;

import statementGraph.graphNode.StatementWrapper;

public class SimplifiedAST {
	private CFG cfg;
	
	public SimplifiedAST(CFG graph){
		this.cfg = graph;
	}
	
	public List<StatementWrapper> getSiblings(StatementWrapper item){
		Statement state = StatementWrapper.getASTNodeStatement(item);
		//System.out.println("GetSiblings: <state>"+state.toString());
		ASTNode parent = state.getParent();
		//System.out.println("GetSiblings: <parent>"+parent.toString());
		
		if(parent instanceof Block){
			LinkedList<StatementWrapper> siblings = new LinkedList<StatementWrapper>();
			Block block = (Block) parent;
			for(Object o: block.statements()){
				Statement siblingsState = (Statement)o;
				if(siblingsState != StatementWrapper.getASTNodeStatement(item)){
					siblings.add(cfg.getItem(siblingsState));
				}
				else{
					break;
				}
			}
			return siblings;
		}
		else{
			return null;
		}
	}
	
	public StatementWrapper getParent(StatementWrapper item){
		Statement state = StatementWrapper.getASTNodeStatement(item);
		ASTNode parent = state.getParent();
		if(parent instanceof Block){
			parent = parent.getParent(); 
		}
		Assert.isTrue(StatementWrapper.isAstStatement(parent) || parent.getNodeType() == ASTNode.METHOD_DECLARATION);
		StatementWrapper parentItem = cfg.getItem(parent);
		return parentItem;
	}
}
