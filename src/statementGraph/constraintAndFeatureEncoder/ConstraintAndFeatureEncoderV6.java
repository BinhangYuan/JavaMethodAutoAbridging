package statementGraph.constraintAndFeatureEncoder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.SimpleName;

import statementGraph.CFG;
import statementGraph.DDG;
import statementGraph.SimplifiedAST;
import statementGraph.graphNode.StatementWrapper;
import statementGraph.graphNode.SwitchStatementWrapper;



public class ConstraintAndFeatureEncoderV6 {
	private SimplifiedAST sAST;
	@SuppressWarnings("unused")
	private CFG cfg;
	@SuppressWarnings("unused")
	private DDG ddg;
	
	private List<StatementWrapper> statementItems;
	private Map<StatementWrapper,Integer> index = new HashMap<StatementWrapper,Integer>();
	private List<Integer> lineCountConstraints = new LinkedList<Integer>();
	private List<Integer> feature_statementTypes = new LinkedList<Integer>();
	private List<Integer> feature_parentStatementTypes = new LinkedList<Integer>();
	private List<Integer> feature_nestedLevel = new LinkedList<Integer>();
	private List<Integer> feature_referencedVariableCounts = new LinkedList<Integer>();
	
	private List<DependencePair> ddgConstraintsSerializer = new LinkedList<DependencePair>();
	
	private List<DependencePair> astConstraintsSerializer = new LinkedList<DependencePair>();
	
	private List<DependencePair> cfgConstraintsSerializer = new LinkedList<DependencePair>();
	
	
	public ConstraintAndFeatureEncoderV6(SimplifiedAST sAST, CFG cfg, DDG ddg) throws Exception{
		this.sAST = sAST;
		this.cfg = cfg;
		this.ddg = ddg;
		this.statementItems = cfg.getNodes();
		
		this.encodeLineConstraints();
		this.encodeDDG();
		this.encodeAST();
		this.encodeCFG();
		this.encodeFeatureStatementType();
		this.encodeFeatureParentStatementType();
		this.encodeFeatureNestedLevel();
		this.encodeFeatureReferenceVariable();
	}
	
	private void encodeDDG(){
		for(StatementWrapper source: this.statementItems){
			for(StatementWrapper dest: source.getDDGUsageSuccessor()){
				Assert.isNotNull(source);
				Assert.isNotNull(dest);
				DependencePair newEdge = new DependencePair(this.index.get(source), this.index.get(dest), DependencePair.TYPE_DDG);
				boolean done = false;
				for(SimpleName usedVar: dest.getUsageVariables()){
					if(source.getDefinedVariableSet().contains(usedVar.getIdentifier())){
						for(SimpleName definedVar: source.getDefinedVariables()){
							if(definedVar.getIdentifier().equals(usedVar.getIdentifier())){
								newEdge.addSharedVariable(definedVar);
								done = true;
								break;
							}
						}
					}
				}
				Assert.isTrue(done);
				this.ddgConstraintsSerializer.add(newEdge);
			}
		}
	}
	
	
	private void encodeCFG(){
		//In our implementation, we only consider the control flow constraints about switch statement.
		for(StatementWrapper item: this.statementItems){
			if(item.getType()==StatementWrapper.SWITCH_STATEMENT){
				SwitchStatementWrapper swithItem = (SwitchStatementWrapper) (item);
				Assert.isTrue(swithItem.getBranchEntries().size() == swithItem.getBranchStatementsWrappers().size());
				for(int i=0;i<swithItem.getBranchEntries().size();i++){
					StatementWrapper source = swithItem.getBranchEntries().get(i);
					for(int j=0; j<swithItem.getBranchStatementsWrappers().get(i).size();j++){
						StatementWrapper dest = swithItem.getBranchStatementsWrappers().get(i).get(j);
						Assert.isNotNull(source);
						Assert.isNotNull(dest);
						DependencePair newEdge = new DependencePair(this.index.get(source), this.index.get(dest), DependencePair.TYPE_CFG);
						this.cfgConstraintsSerializer.add(newEdge);
					}
				}
			}
		}
	}
	
	
	private void encodeAST() throws Exception{
		for(StatementWrapper dest: this.statementItems){
			StatementWrapper parent = sAST.getParent(dest);
			if(parent!=null){
				this.astConstraintsSerializer.add(new DependencePair(this.index.get(parent), this.index.get(dest), DependencePair.TYPE_AST));
			}
		}
	}
	
	
	private void encodeLineConstraints(){
		for(int i=0; i<this.statementItems.size();i++){
			this.index.put(this.statementItems.get(i), i);
			this.lineCountConstraints.add(this.statementItems.get(i).getLineCount());
		}
	}
	
	
	private void encodeFeatureStatementType(){
		for(StatementWrapper statementWrapper: this.statementItems){
			this.feature_statementTypes.add(statementWrapper.getType());
		}
	}
	
	
	private void encodeFeatureParentStatementType(){
		for(StatementWrapper statementWrapper: this.statementItems){
			Assert.isTrue(statementWrapper.getParentType()!=StatementWrapper.PARENT_ILLEGAL);
			this.feature_parentStatementTypes.add(statementWrapper.getParentType());
		}
	}
	
	
	private void encodeFeatureNestedLevel(){
		for(StatementWrapper statementWrapper: this.statementItems){
			this.feature_nestedLevel.add(statementWrapper.getNestedLevel());
		}
	}
	
	
	private void encodeFeatureReferenceVariable(){
		for(StatementWrapper statementWrapper: this.statementItems){
			this.feature_referencedVariableCounts.add(statementWrapper.getReferencedVariables());
		}
	}
	
	public void printConstraints(){
		for(int i=0; i<this.statementItems.size(); i++){
			System.out.println("Node <"+i+">");
			System.out.println("<|" + this.statementItems.get(i).toString() +"|>");
			System.out.println("Line count: "+ this.statementItems.get(i).getLineCount());
		}
		System.out.println("DDG constraints:");
		for(DependencePair edge:this.ddgConstraintsSerializer){
			System.out.println(edge.sourceIndex +"====>"+ edge.destIndex);
		}
		System.out.println("CFG constraints:");
		for(DependencePair edge:this.cfgConstraintsSerializer){
			System.out.println(edge.sourceIndex +"====>"+ edge.destIndex);
		}
		System.out.println("AST constraints:");
		for(DependencePair edge:this.astConstraintsSerializer){
			System.out.println(edge.sourceIndex +"====>"+ edge.destIndex);
		}
		System.out.println("Statement types:");
		for(Integer type: this.feature_statementTypes){
			System.out.print(type+" ");
		}
		System.out.println();
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
	
	public List<Integer> getStatementType(){
		return this.feature_statementTypes;
	}
	
	public List<Integer> getParentStatementType(){
		return this.feature_parentStatementTypes;
	}
	
	public List<Integer> getNestedLevel(){
		return this.feature_nestedLevel;
	}
	
	public List<Integer> getReferencedVariableCounts(){
		return this.feature_referencedVariableCounts;
	}
	
	public String compressedProgram2String(boolean [] flags){
		return this.sAST.computeOutput(flags);
	}
	
	public String originProgram2String(){
		boolean[] allTrue = new boolean[this.statementItems.size()];
		Arrays.fill(allTrue, true);
		return this.sAST.computeOutput(allTrue);
	}
	
	public List<StatementWrapper> getStatementWrapperList(){
		return this.sAST.getAllWrapperList();
	}
	
	public SimplifiedAST getSimplifiedAST(){
		return this.sAST;
	}
}