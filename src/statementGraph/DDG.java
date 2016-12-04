package statementGraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import statementGraph.expressionWrapper.ExpressionExtractor;
import statementGraph.graphNode.StatementWrapper;


public class DDG {
	private CFG cfg;
	public SimplifiedAST astSkel;
	private List<SimpleName> methodParameters =  new LinkedList<SimpleName>();
	private Map<String,List<StatementWrapper>> variablesDecl = new HashMap<String,List<StatementWrapper>>();
	
	public DDG(CFG cfg){
		this.cfg = cfg;
		this.astSkel = new SimplifiedAST(this.cfg);
		this.updateMethodParameters();
		this.updateAllVariables();
		this.buildEdges();
	}
	
	private void updateMethodParameters(){
		MethodDeclaration methodAst = this.cfg.getMethodDeclaration();
		for(Object s: methodAst.parameters()){
			if(s instanceof SingleVariableDeclaration){
				SingleVariableDeclaration svdec = (SingleVariableDeclaration)s;
				this.methodParameters.add(svdec.getName());
				this.variablesDecl.put(svdec.getName().getIdentifier(), new LinkedList<StatementWrapper>());
				this.variablesDecl.get(svdec.getName().getIdentifier()).add(null);
			}
		}
	}
	
	private void updateAllVariables(){
		for(StatementWrapper item:this.cfg.getNodes()){
			Statement statement = StatementWrapper.getASTNodeStatement(item);
			item.addDefinedVariables(ExpressionExtractor.getVariableSimpleNames(statement, true));
			if(!item.getDefinedVariables().isEmpty()){
				for(SimpleName var: item.getDefinedVariables()){
					if(!this.variablesDecl.containsKey(var.getIdentifier())){
						this.variablesDecl.put(var.getIdentifier(), new LinkedList<StatementWrapper>());
					}
					this.variablesDecl.get(var.getIdentifier()).add(item);
				}
			}
			item.addUsageVariables(ExpressionExtractor.getVariableSimpleNames(statement, false));
		}
	}
	
	private void buildEdges(){
		for(StatementWrapper item: this.cfg.getNodes()){
			if(!item.getUsageVariables().isEmpty()){
				for(String var: item.getUsageVariableSet()){
					if(this.variablesDecl.containsKey(var)){//If the variable is declared out of the scope of the function, we ignore it for now.
						if(this.variablesDecl.get(var).size()==1){//This is simple, only one declaration in the function scope
							StatementWrapper preItem = this.variablesDecl.get(var).get(0);
							if(preItem!=item){
								item.addDDGDefinedPredecessor(preItem);
								if(preItem!=null){
									preItem.addDDGUsageSuccessor(item);
								}
							}
						}
						else{//Little tricky here, we should consider the same name under different scope.
							if(!item.getDefinedVariableSet().contains(var)){
								StatementWrapper current = item;
								StatementWrapper parent = null;
								List<StatementWrapper> siblings = null;
								boolean done = false;
								while(!done){
									siblings = this.astSkel.getSiblings(current);
									for(StatementWrapper sibling: siblings){
										if(sibling.getDefinedVariableSet().contains(var)){
											StatementWrapper preItem = sibling;
											item.addDDGDefinedPredecessor(preItem);
											if(preItem!=null){
												preItem.addDDGUsageSuccessor(item);
											}
											done = true;
											break;
										}
									}
									if(!done){
										parent = this.astSkel.getParent(current);
										//System.out.println(parent.toString());
										if(parent.getDefinedVariableSet().contains(var)){
											StatementWrapper preItem = parent;
											item.addDDGDefinedPredecessor(preItem);
											if(preItem!=null){
												preItem.addDDGUsageSuccessor(item);
											}
											done = true;
											break;
										}
										else{
											current = parent;
										}
									}
								}
							}
						}
					}
				}
			}
		}		
	}
	
	
	public void printDDG(){
		System.out.println("Print the elements declaration and usage");
		for(StatementWrapper item:this.cfg.getNodes()){
			item.printName();
			System.out.println("Declares: "+item.getDefinedVariables());
			System.out.println("Uses: "+item.getUsageVariables());
		}
		System.out.println("==============================================");
		System.out.println("Variable declares: ");
		for(String name: this.variablesDecl.keySet()){
			System.out.println("Variable: <"+name+">:");
			for(StatementWrapper item: this.variablesDecl.get(name)){
				if(item!=null){
					item.printName();
				}
				else{
					System.out.println("Passed as parameter");
				}
			}
			System.out.println("-------------------------------------------");
		}
	}
}
