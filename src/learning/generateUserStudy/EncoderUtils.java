package learning.generateUserStudy;

import java.util.Random;

import ilpSolver.LearningBinaryIPSolverV5;
import ilpSolver.LearningBinaryIPSolverV6;
import ilpSolver.NaiveBinaryIPSolver;
import statementGraph.ASTParserUtils;
import statementGraph.constraintAndFeatureEncoder.ConstraintAndFeatureEncoderV5;
import statementGraph.constraintAndFeatureEncoder.ConstraintAndFeatureEncoderV6;
import statementGraph.graphNode.StatementWrapper;

public class EncoderUtils {
	
	public static int ORIGINAL = 0;
	public static int NAIVEMETHOD = 1;
	public static int MYMETHOD = 2;
	public static Random randGenerate = new Random();
	
	public static LearningBinaryIPSolverV5 encodeSolverV5(String filePath, String fileName,String methodName,int pos) throws Exception{
		ConstraintAndFeatureEncoderV5 encoder = ASTParserUtils.parseMethodV5(true,filePath, fileName,methodName,pos,null);
		LearningBinaryIPSolverV5 solver = new LearningBinaryIPSolverV5(encoder);
		solver.setDependenceConstraints(encoder.getASTConstraints(), encoder.getCFGConstraints(), encoder.getDDGConstraints());
		solver.setLineCostConstraints(encoder.getLineCounts());
		solver.setTypeMap(StatementWrapper.typeMap);
		solver.setParentTypeMap(StatementWrapper.parentTypeMap);
		solver.setStatementType(encoder.getStatementType());
		solver.setParentStatementType(encoder.getParentStatementType());
		solver.setNestedLevels(encoder.getNestedLevel());
		solver.setReferencedVariableCounts(encoder.getReferencedVariableCounts());
		return solver;
	}
	
	public static LearningBinaryIPSolverV6 encodeSolverV6(String filePath, String fileName,String methodName,int pos) throws Exception{
		ConstraintAndFeatureEncoderV6 encoder = ASTParserUtils.parseMethodV6(true,filePath, fileName,methodName,pos,null);
		LearningBinaryIPSolverV6 solver = new LearningBinaryIPSolverV6(encoder);
		solver.setDependenceConstraints(encoder.getASTConstraints(), encoder.getCFGConstraints(), encoder.getDDGConstraints());
		solver.setLineCostConstraints(encoder.getLineCounts());
		solver.setTypeMap(StatementWrapper.typeMap);
		solver.setParentTypeMap(StatementWrapper.parentTypeMap);
		solver.setStatementType(encoder.getStatementType());
		solver.setParentStatementType(encoder.getParentStatementType());
		solver.setNestedLevels(encoder.getNestedLevel());
		solver.setReferencedVariableCounts(encoder.getReferencedVariableCounts());
		return solver;
	}
	
	
	public static NaiveBinaryIPSolver encodeNaiveSolver(String filePath, String fileName,String methodName,int pos) throws Exception{
		ConstraintAndFeatureEncoderV5 encoder = ASTParserUtils.parseMethodV5(true,filePath, fileName,methodName,pos,null);
		NaiveBinaryIPSolver solver = new NaiveBinaryIPSolver(encoder);
		solver.setDependenceConstraints(encoder.getASTConstraints(), encoder.getCFGConstraints(), encoder.getDDGConstraints());
		solver.setLineCostConstraints(encoder.getLineCounts());
		return solver;
	}
}
