package statementGraph;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;

import statementGraph.graphNode.ElementItem;

public class SimplifiedAST {
	private CFG cfg;
	
	public SimplifiedAST(CFG graph){
		this.cfg = graph;
	}
	
	public List<ElementItem> getSiblings(ElementItem item){
		Statement state = ElementItem.getASTNodeStatement(item);
		//System.out.println("GetSiblings: <state>"+state.toString());
		ASTNode parent = state.getParent();
		//System.out.println("GetSiblings: <parent>"+parent.toString());
		
		if(parent instanceof Block){
			LinkedList<ElementItem> siblings = new LinkedList<ElementItem>();
			Block block = (Block) parent;
			for(Object o: block.statements()){
				Statement siblingsState = (Statement)o;
				if(siblingsState != ElementItem.getASTNodeStatement(item)){
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
	
	public ElementItem getParent(ElementItem item){
		Statement state = ElementItem.getASTNodeStatement(item);
		ASTNode parent = state.getParent();
		if(parent instanceof Block){
			parent = parent.getParent(); 
		}
		Assert.isTrue(ElementItem.isAstStatement(parent) || parent.getNodeType() == ASTNode.METHOD_DECLARATION);
		ElementItem parentItem = cfg.getItem(parent);
		return parentItem;
	}
}
