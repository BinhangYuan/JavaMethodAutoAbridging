package statementGraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import statementGraph.graphNode.EdgeItem;
import statementGraph.graphNode.StatementWrapper;



public class ConstraintEncoder {
	private CFG cfg;
	private DDG ddg;
	
	private List<StatementWrapper> statementItems;
	private Map<StatementWrapper,Integer> index = new HashMap<StatementWrapper,Integer>();
	private List<Integer> lineCountConstraints = new LinkedList<Integer>();
	
	private List<EdgeItem> ddgConstraints = new LinkedList<EdgeItem>();
	private List<DependencePair> ddgConstraintsSerializer = new LinkedList<DependencePair>();
	
	private List<EdgeItem> astConstraints = new LinkedList<EdgeItem>();
	private List<DependencePair> astConstraintsSerializer = new LinkedList<DependencePair>();
	
	private List<EdgeItem> cfgConstraints = new LinkedList<EdgeItem>();
	private List<DependencePair> cfgConstraintsSerializer = new LinkedList<DependencePair>();
	
	
	public ConstraintEncoder(CFG cfg, DDG ddg){
		this.cfg = cfg;
		this.ddg = ddg;
		this.statementItems = cfg.getNodes();
		
		for(int i=0; i<this.statementItems.size();i++){
			this.index.put(this.statementItems.get(i), i);
			this.lineCountConstraints.add(this.statementItems.get(i).getLineCount());
		}
		this.encodeDDG();
		this.encodeAST();
		this.encodeCFG();
	}
	
	public void encodeDDG(){
		for(StatementWrapper source: this.statementItems){
			for(StatementWrapper dest: source.getDDGUsageSuccessor()){
				this.ddgConstraints.add(new EdgeItem(source,dest,EdgeItem.DDGPrority));
			}
		}
		//Stub, not encoding
		for(EdgeItem e: this.ddgConstraints){
			this.ddgConstraintsSerializer.add(new DependencePair(this.index.get(e.start), this.index.get(e.end)));
		}
	}
	
	
	public void encodeCFG(){
		
	}
	
	public void encodeAST(){
		for(StatementWrapper dest: this.statementItems){
			StatementWrapper parent = ddg.astSkel.getParent(dest);
			if(parent!=null){
				this.astConstraints.add(new EdgeItem(parent,dest,EdgeItem.ASTPrority));
			}
		}
		//Stub, not encoding
		for(EdgeItem e: this.astConstraints){
			this.astConstraintsSerializer.add(new DependencePair(this.index.get(e.start), this.index.get(e.end)));
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
	
	public List<DependencePair> getDDGConstraints(){
		return this.ddgConstraintsSerializer;
	}
	
	public List<DependencePair> getASTConstraints(){
		return this.astConstraintsSerializer;
	}
	
	public List<DependencePair> getCFGConstraints(){
		return this.cfgConstraintsSerializer;
	}
	
	public List<Integer> getLineCounts(){
		return this.lineCountConstraints;
	}
}
