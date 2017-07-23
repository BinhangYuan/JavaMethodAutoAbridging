package statementGraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import statementGraph.expressionWrapper.ExpressionExtractor;
import statementGraph.expressionWrapper.ExpressionInstanceChecker;
import statementGraph.graphNode.StatementWrapper;


public class DDG {
	public SimplifiedAST sAST;
	private List<SimpleName> methodParameters =  new LinkedList<SimpleName>();
	private Map<String,List<StatementWrapper>> variablesDecl = new HashMap<String,List<StatementWrapper>>();
	private Map<SimpleName,Integer> referencedCount = new HashMap<SimpleName,Integer>();
	private Map<SimpleName,Type> variablesType = new HashMap<SimpleName,Type>();
	
	private boolean debug = false;
	
	public int getVariableAccessCount(SimpleName var){
		Assert.isTrue(var.isDeclaration());
		return this.referencedCount.get(var);
	}
	
	public DDG(SimplifiedAST sAST) throws Exception{
		this.sAST = sAST;
		this.updateMethodParameters();
		this.updateAllVariables();
		this.buildEdges();
	}
	
	private void updateMethodParameters(){
		MethodDeclaration methodAst = this.sAST.getASTNode();
		for(Object s: methodAst.parameters()){
			if(s instanceof SingleVariableDeclaration){
				SingleVariableDeclaration svdec = (SingleVariableDeclaration)s;
				this.methodParameters.add(svdec.getName());
				this.variablesDecl.put(svdec.getName().getIdentifier(), new LinkedList<StatementWrapper>());
				this.variablesDecl.get(svdec.getName().getIdentifier()).add(null);
			}
		}
	}
	
	private void updateAllVariables() throws Exception{
		for(StatementWrapper item:this.sAST.getAllWrapperList()){
			Statement statement = StatementWrapper.getASTNodeStatement(item);
			if(StatementWrapper.containsVariableDeclarations(statement)){
				item.addDefinedVariables(ExpressionExtractor.getVariableSimpleNames(statement, true));
			}
			if(!item.getDefinedVariables().isEmpty()){
				for(SimpleName var: item.getDefinedVariables()){
					if(!this.variablesDecl.containsKey(var.getIdentifier())){
						this.variablesDecl.put(var.getIdentifier(), new LinkedList<StatementWrapper>());
					}
					this.variablesDecl.get(var.getIdentifier()).add(item);
					if(var.isDeclaration()){
						//Handle reference counts;
						Assert.isTrue(!this.referencedCount.containsKey(var));
						this.referencedCount.put(var, 0);
						//Get the type;
						Assert.isTrue(!this.variablesType.containsKey(var));
						if(var.getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_FRAGMENT){
							if(var.getParent().getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT){
								VariableDeclarationStatement varStatement = (VariableDeclarationStatement) var.getParent().getParent();
								Assert.isTrue(statement.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT);
								Assert.isTrue(varStatement==statement);
								this.variablesType.put(var, varStatement.getType());
							}
							else if(var.getParent().getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_EXPRESSION){
								VariableDeclarationExpression varExpression = (VariableDeclarationExpression) var.getParent().getParent();
								this.variablesType.put(var, varExpression.getType());
							}
							else{
								throw new Exception("Unexpected parent!");
							}
						}
						else if(var.getParent().getNodeType() == ASTNode.SINGLE_VARIABLE_DECLARATION){
							SingleVariableDeclaration varDecl = (SingleVariableDeclaration) var.getParent();
							this.variablesType.put(var, varDecl.getType());
						}
						else{
							throw new Exception("Unexpected parent!");
						}
					}
				}
			}
			item.addUsageVariables(ExpressionExtractor.getVariableSimpleNames(statement, false));
		}
		//Update reference varible count;
		for(StatementWrapper item:this.sAST.getAllWrapperList()){
			Statement statement = StatementWrapper.getASTNodeStatement(item);
			int count = ExpressionExtractor.getExpressions(statement, new ExpressionInstanceChecker(ASTNode.SIMPLE_NAME)).size();
					//+ExpressionExtractor.getExpressions(statement, new ExpressionInstanceChecker(ASTNode.QUALIFIED_NAME)).size();
			if(debug){
				System.out.println("<<<<<<DDG simpleName count>>>>>>");
				System.out.println(statement.toString());
				for(Object o:ExpressionExtractor.getExpressions(statement, new ExpressionInstanceChecker(ASTNode.SIMPLE_NAME))){
					System.out.print(((SimpleName)(o)).getIdentifier()+" ");
				}
				System.out.println();
			}
			item.addVariableCount(count);
		}
	}
	
	private void buildEdges() throws Exception{
		for(StatementWrapper item: this.sAST.getAllWrapperList()){
			if(!item.getUsageVariables().isEmpty()){
				for(String varName: item.getUsageVariableSet()){
					if(this.variablesDecl.containsKey(varName)){//If the variable is declared out of the scope of the function, we ignore it for now.
						if(this.variablesDecl.get(varName).size()==1){//This is simple, only one declaration in the function scope
							StatementWrapper preItem = this.variablesDecl.get(varName).get(0);
							if(preItem!=item){
								item.addDDGDefinedPredecessor(preItem);
								if(preItem!=null){
									preItem.addDDGUsageSuccessor(item);
									boolean updatedReferenceCount = false;
									for(SimpleName var: preItem.getDefinedVariables()){
										Assert.isTrue(this.referencedCount.containsKey(var));
										if(var.getIdentifier().equals(varName)){
											this.referencedCount.put(var, this.referencedCount.get(var)+1);
											updatedReferenceCount = true;
											break;
										}
									}
									Assert.isTrue(updatedReferenceCount);
								}
							}
						}
						else{//Little tricky here, we should consider the same name under different scope.
							if(debug){
								System.out.println(item.toString());
							}
							if(!item.getDefinedVariableSet().contains(varName)){
								StatementWrapper current = item;
								StatementWrapper parent = null;
								List<StatementWrapper> siblings = null;
								boolean done = false;
								while(!done){
									siblings = this.sAST.getSiblings(current);
									if(siblings!=null){
										for(StatementWrapper sibling: siblings){
											if(sibling.getDefinedVariableSet().contains(varName)){
												StatementWrapper preItem = sibling;
												item.addDDGDefinedPredecessor(preItem);
												if(preItem!=null){
													preItem.addDDGUsageSuccessor(item);
													boolean updatedReferenceCount = false;
													for(SimpleName var: preItem.getDefinedVariables()){
														Assert.isTrue(this.referencedCount.containsKey(var));
														if(var.getIdentifier().equals(varName)){
															this.referencedCount.put(var, this.referencedCount.get(var)+1);
															updatedReferenceCount = true;
															break;
														}
													}
													Assert.isTrue(updatedReferenceCount);
												}
												done = true;
												break;
											}
										}
									}
									if(!done){
										if(debug){
											System.out.println(varName);
											System.out.println(current.toString());
										}
										parent = this.sAST.getParent(current);
										if(debug){
											System.out.println(parent.toString());
										}
										if(parent.getDefinedVariableSet().contains(varName)){
											StatementWrapper preItem = parent;
											item.addDDGDefinedPredecessor(preItem);
											if(preItem!=null){
												preItem.addDDGUsageSuccessor(item);
												boolean updatedReferenceCount = false;
												for(SimpleName var: preItem.getDefinedVariables()){
													Assert.isTrue(this.referencedCount.containsKey(var));
													if(var.getIdentifier().equals(varName)){
														this.referencedCount.put(var, this.referencedCount.get(var)+1);
														updatedReferenceCount = true;
														break;
													}
												}
												Assert.isTrue(updatedReferenceCount);
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
		for(StatementWrapper item:this.sAST.getAllWrapperList()){
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
		System.out.println("==============================================");
		System.out.println("Variable reference count: ");
		for(SimpleName var: this.referencedCount.keySet()){
			System.out.println(var.getIdentifier()+":"+this.referencedCount.get(var));
		}
		System.out.println("==============================================");
		System.out.println("Variable type: ");
		for(SimpleName var: this.variablesType.keySet()){
			System.out.println(var.getIdentifier()+":"+this.variablesType.get(var).toString());
		}
		System.out.println("==============================================");
	}
}
