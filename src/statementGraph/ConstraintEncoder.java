package statementGraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import statementGraph.graphNode.EdgeItem;
import statementGraph.graphNode.ElementItem;

public class ConstraintEncoder {
	private CFG cfg;
	private DDG ddg;
	
	private List<ElementItem> statementItems;
	private Map<ElementItem,Integer> index = new HashMap<ElementItem,Integer>();
	
	private List<EdgeItem> ddgConstraints = new LinkedList<EdgeItem>();
	private List<EdgeItem> astConstraints = new LinkedList<EdgeItem>();
	private List<EdgeItem> cfgConstraints = new LinkedList<EdgeItem>();
	
	
	public ConstraintEncoder(CFG cfg, DDG ddg){
		this.cfg = cfg;
		this.ddg = ddg;
		this.statementItems = cfg.getNodes();
		
		for(int i=0; i<this.statementItems.size();i++){
			index.put(this.statementItems.get(i), i);
		}
		
		this.encodeDDG();
		this.encodeAST();
		this.encodeCFG();
	}
	
	public void encodeDDG(){
		for(ElementItem source: this.statementItems){
			for(ElementItem dest: source.getDDGUsageSuccessor()){
				this.ddgConstraints.add(new EdgeItem(source,dest,EdgeItem.DDGPrority));
			}
		}
	}
	
	
	public void encodeCFG(){
		
	}
	
	public void encodeAST(){
		for(ElementItem dest: this.statementItems){
			ElementItem parent = ddg.astSkel.getParent(dest);
			if(parent!=null){
				this.astConstraints.add(new EdgeItem(parent,dest,EdgeItem.ASTPrority));
			}
		}
	}
	
	public void printConstraints(){
		for(int i=0; i<this.statementItems.size(); i++){
			System.out.println("Node <"+i+">");
			System.out.println("<|" + this.statementItems.get(i).toString() +"|>");
			System.out.println("Line count: "+ this.statementItems.get(i).getLineCount());
			
		}
		System.out.println("DDG constraints:");
		for(EdgeItem edge:this.ddgConstraints){
			System.out.println(this.index.get(edge.start)+"====>"+this.index.get(edge.end));
		}
		System.out.println("CFG constraints:");
		for(EdgeItem edge:this.cfgConstraints){
			System.out.println(this.index.get(edge.start)+"====>"+this.index.get(edge.end));
		}
		System.out.println("AST constraints:");
		for(EdgeItem edge:this.astConstraints){
			System.out.println(this.index.get(edge.start)+"====>"+this.index.get(edge.end));
		}
	}
}
