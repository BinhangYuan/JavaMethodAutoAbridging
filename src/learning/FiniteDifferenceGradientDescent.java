package learning;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;

import ilpSolver.LearningBinaryIPSolverV0;
import statementGraph.ASTParserUtils;
import statementGraph.CFG;
import statementGraph.ConstraintAndFeatureEncoder;
import statementGraph.DDG;
import statementGraph.SimplifiedAST;
import statementGraph.graphNode.StatementWrapper;

public class FiniteDifferenceGradientDescent {
	private double [] parameters;
	private Map<Integer,Integer> typeMap;
	private double stepLength = 0.1;
	private double epsilon = 0.01;
	private JaccardDistance computeDistance = new JaccardDistance();
	
	private Map<LearningBinaryIPSolverV0,ManualLabel> trainingSet = new HashMap<LearningBinaryIPSolverV0,ManualLabel>();
	private int maxIterations = 100;
	private double threshold = 0.00001;
	
	void setStepLength(double step){
		this.stepLength = step;
	}
	
	private double computeCost(double [] paras){
		double cost = 0;
		double n = (double)this.trainingSet.keySet().size();
		for(LearningBinaryIPSolverV0 solver: this.trainingSet.keySet()){
			solver.setParameters(paras);
			cost += this.computeDistance.distanceBetweenSets(solver.solve(),this.trainingSet.get(solver).getBooleanLabels());
		}
		return cost/n;
	}	
	
	public void initTraining(String name) throws IOException{
		File dirs = new File(name);
		String dirPath = dirs.getCanonicalPath() + File.separator+"src"+File.separator;
		File root = new File(dirPath);
		//System.out.println(rootDir.listFiles());
		File[] files = root.listFiles ( );
	 
		for (File f : files ) {
			String filePath = f.getAbsolutePath();
			if(f.isFile()){
				//parse(readFileToString(filePath));
				ASTParser parser = ASTParser.newParser(AST.JLS8);
				String str = ASTParserUtils.readFileToString(filePath);
				parser.setSource(str.toCharArray());
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setEnvironment(new String[]{filePath}, null, null, true);
				parser.setUnitName("Anything");
				parser.setResolveBindings(true);
				
				final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
				ArrayList<MethodDeclaration> methods = new ArrayList<MethodDeclaration>();
				
				cu.accept(new ASTVisitor() {
					public boolean visit(MethodDeclaration node){
						SimpleName name = node.getName();
						System.out.println("Vistor:: Method Declaration of: '"+name+ "' at line" +cu.getLineNumber(node.getStartPosition()));
						methods.add(node);
						return true;
					}
				});
				
				for(MethodDeclaration node: methods){
					SimplifiedAST sAST = new SimplifiedAST(node);
					CFG cfg = new CFG(sAST);
					DDG ddg = new DDG(sAST);
					ConstraintAndFeatureEncoder encoder = new ConstraintAndFeatureEncoder(sAST,cfg,ddg);
					LearningBinaryIPSolverV0 solver = new LearningBinaryIPSolverV0();
					solver.setDependenceConstraints(encoder.getASTConstraints(), encoder.getCFGConstraints(), encoder.getDDGConstraints());
					solver.setLineCostConstraints(encoder.getLineCounts());
					solver.setTypeMap(this.typeMap);
					solver.setStatementType(encoder.getStatementType());
					ManualLabel label = new ManualLabel(f.getName()+"_"+node.getName().getIdentifier()+".");
					solver.setTargetLineCount(label.getLineConstraintt());
					this.trainingSet.put(solver,label);
				}
			}
		}
	}
	
	public void training(){
		for(int i = 0; i < this.maxIterations; i++){
			double precost = this.computeCost(this.parameters);
			System.out.println("Training process: Iteration "+i +": "+ precost);
			double [] derivative = new double[this.parameters.length];
			
			for(int j = 0; j < this.parameters.length; j++ ){
				double [] tempPara = new double[this.parameters.length];
				System.arraycopy(this.parameters, 0, tempPara, 0, this.parameters.length);
				tempPara[j] += this.epsilon; 
				double updateCost = this.computeCost(tempPara);
				double partialDerivative_j = (updateCost-precost)/this.epsilon;
				derivative[j] = partialDerivative_j;
			}
			
			//update parameters
			for(int j = 0; j < this.parameters.length; j++){
				this.parameters[j] += (this.stepLength*derivative[j]);
			}
			
			double postcost = this.computeCost(this.parameters);
			
			if(Math.abs(precost-postcost) < this.threshold){
				break;
			}
		}
	}
	
	public FiniteDifferenceGradientDescent(){
		this.parameters = new double[StatementWrapper.statementsLabelSet.size()];
		Arrays.fill(this.parameters,1.0);
		this.typeMap = new HashMap<Integer,Integer>();
		int i = 0;
		for(Integer label: StatementWrapper.statementsLabelSet){
			typeMap.put(label, i++);
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		FiniteDifferenceGradientDescent model = new FiniteDifferenceGradientDescent();
		model.initTraining(".");
		model.training();
	}
}
