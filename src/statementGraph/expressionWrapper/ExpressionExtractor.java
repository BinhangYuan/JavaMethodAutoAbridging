package statementGraph.expressionWrapper;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;


public class ExpressionExtractor {
	//This is copied from jextract
	public static List<Expression> getExpressions(Statement statement, ExpressionInstanceChecker instanceChecker) {
		List<Expression> expressionList = new ArrayList<Expression>();
		if(statement instanceof Block) {
			Block block = (Block)statement;
			@SuppressWarnings("unchecked")
			List<Statement> blockStatements = block.statements();
			for(Statement blockStatement : blockStatements)
				expressionList.addAll(getExpressions(blockStatement,instanceChecker));
		}
		else if(statement instanceof IfStatement) {
			IfStatement ifStatement = (IfStatement)statement;
			Expression expression = ifStatement.getExpression();
			expressionList.addAll(getExpressions(expression,instanceChecker));
			expressionList.addAll(getExpressions(ifStatement.getThenStatement(),instanceChecker));
			if(ifStatement.getElseStatement() != null) {
				expressionList.addAll(getExpressions(ifStatement.getElseStatement(),instanceChecker));
			}
		}
		else if(statement instanceof ForStatement) {
			ForStatement forStatement = (ForStatement)statement;
			@SuppressWarnings("unchecked")
			List<Expression> initializers = forStatement.initializers();
			for(Expression initializer : initializers)
				expressionList.addAll(getExpressions(initializer,instanceChecker));
			Expression expression = forStatement.getExpression();
			if(expression != null)
				expressionList.addAll(getExpressions(expression,instanceChecker));
			@SuppressWarnings("unchecked")
			List<Expression> updaters = forStatement.updaters();
			for(Expression updater : updaters)
				expressionList.addAll(getExpressions(updater,instanceChecker));
			expressionList.addAll(getExpressions(forStatement.getBody(),instanceChecker));
		}
		else if(statement instanceof EnhancedForStatement) {
			EnhancedForStatement enhancedForStatement = (EnhancedForStatement)statement;
			Expression expression = enhancedForStatement.getExpression();
			SingleVariableDeclaration variableDeclaration = enhancedForStatement.getParameter();
			expressionList.addAll(getExpressions(variableDeclaration.getName(),instanceChecker));
			if(variableDeclaration.getInitializer() != null)
				expressionList.addAll(getExpressions(variableDeclaration.getInitializer(),instanceChecker));
			expressionList.addAll(getExpressions(expression,instanceChecker));
			expressionList.addAll(getExpressions(enhancedForStatement.getBody(),instanceChecker));
		}
		else if(statement instanceof WhileStatement) {
			WhileStatement whileStatement = (WhileStatement)statement;
			Expression expression = whileStatement.getExpression();
			expressionList.addAll(getExpressions(expression,instanceChecker));
			expressionList.addAll(getExpressions(whileStatement.getBody(),instanceChecker));
		}
		else if(statement instanceof DoStatement) {
			DoStatement doStatement = (DoStatement)statement;
			Expression expression = doStatement.getExpression();
			expressionList.addAll(getExpressions(expression,instanceChecker));
			expressionList.addAll(getExpressions(doStatement.getBody(),instanceChecker));
		}
		else if(statement instanceof ExpressionStatement) {
			ExpressionStatement expressionStatement = (ExpressionStatement)statement;
			Expression expression = expressionStatement.getExpression();
			expressionList.addAll(getExpressions(expression,instanceChecker));
		}
		else if(statement instanceof SwitchStatement) {
			SwitchStatement switchStatement = (SwitchStatement)statement;
			Expression expression = switchStatement.getExpression();
			expressionList.addAll(getExpressions(expression,instanceChecker));
			@SuppressWarnings("unchecked")
			List<Statement> switchStatements = switchStatement.statements();
			for(Statement switchStatement2 : switchStatements)
				expressionList.addAll(getExpressions(switchStatement2,instanceChecker));
		}
		else if(statement instanceof SwitchCase) {
			SwitchCase switchCase = (SwitchCase)statement;
			Expression expression = switchCase.getExpression();
			if(expression != null)
				expressionList.addAll(getExpressions(expression,instanceChecker));
		}
		else if(statement instanceof AssertStatement) {
			AssertStatement assertStatement = (AssertStatement)statement;
			Expression expression = assertStatement.getExpression();
			expressionList.addAll(getExpressions(expression,instanceChecker));
			Expression message = assertStatement.getMessage();
			if(message != null)
				expressionList.addAll(getExpressions(message,instanceChecker));
		}
		else if(statement instanceof LabeledStatement) {
			LabeledStatement labeledStatement = (LabeledStatement)statement;
			if(labeledStatement.getLabel() != null)
				expressionList.addAll(getExpressions(labeledStatement.getLabel(),instanceChecker));
			expressionList.addAll(getExpressions(labeledStatement.getBody(),instanceChecker));
		}
		else if(statement instanceof ReturnStatement) {
			ReturnStatement returnStatement = (ReturnStatement)statement;
			Expression expression = returnStatement.getExpression();
			expressionList.addAll(getExpressions(expression,instanceChecker));	
		}
		else if(statement instanceof SynchronizedStatement) {
			SynchronizedStatement synchronizedStatement = (SynchronizedStatement)statement;
			Expression expression = synchronizedStatement.getExpression();
			expressionList.addAll(getExpressions(expression,instanceChecker));
			expressionList.addAll(getExpressions(synchronizedStatement.getBody(),instanceChecker));
		}
		else if(statement instanceof ThrowStatement) {
			ThrowStatement throwStatement = (ThrowStatement)statement;
			Expression expression = throwStatement.getExpression();
			expressionList.addAll(getExpressions(expression,instanceChecker));
		}
		else if(statement instanceof TryStatement) {
			TryStatement tryStatement = (TryStatement)statement;
			
			//List<VariableDeclarationExpression> resources = tryStatement.resources();
			//for(VariableDeclarationExpression expression : resources) {
			//	expressionList.addAll(getExpressions(expression,instanceChecker));
			//}
			
			expressionList.addAll(getExpressions(tryStatement.getBody(),instanceChecker));
			@SuppressWarnings("unchecked")
			List<CatchClause> catchClauses = tryStatement.catchClauses();
			for(CatchClause catchClause : catchClauses) {
				SingleVariableDeclaration variableDeclaration = catchClause.getException();
				expressionList.addAll(getExpressions(variableDeclaration.getName(),instanceChecker));
				if(variableDeclaration.getInitializer() != null)
					expressionList.addAll(getExpressions(variableDeclaration.getInitializer(),instanceChecker));
				expressionList.addAll(getExpressions(catchClause.getBody(),instanceChecker));
			}
			Block finallyBlock = tryStatement.getFinally();
			if(finallyBlock != null)
				expressionList.addAll(getExpressions(finallyBlock,instanceChecker));
		}
		else if(statement instanceof VariableDeclarationStatement) {
			VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement)statement;
			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment> fragments = variableDeclarationStatement.fragments();
			for(VariableDeclarationFragment fragment : fragments) {
				expressionList.addAll(getExpressions(fragment.getName(),instanceChecker));
				expressionList.addAll(getExpressions(fragment.getInitializer(),instanceChecker));
			}
		}
		else if(statement instanceof ConstructorInvocation) {
			ConstructorInvocation constructorInvocation = (ConstructorInvocation)statement;
			@SuppressWarnings("unchecked")
			List<Expression> arguments = constructorInvocation.arguments();
			for(Expression argument : arguments){
				expressionList.addAll(getExpressions(argument,instanceChecker));
			}
		}
		else if(statement instanceof SuperConstructorInvocation) {
			SuperConstructorInvocation superConstructorInvocation = (SuperConstructorInvocation)statement;
			if(superConstructorInvocation.getExpression() != null){
				expressionList.addAll(getExpressions(superConstructorInvocation.getExpression(),instanceChecker));
			}
			@SuppressWarnings("unchecked")
			List<Expression> arguments = superConstructorInvocation.arguments();
			for(Expression argument : arguments){
				expressionList.addAll(getExpressions(argument,instanceChecker));
			}
		}
		else if(statement instanceof BreakStatement) {
			BreakStatement breakStatement = (BreakStatement)statement;
			if(breakStatement.getLabel() != null){
				expressionList.addAll(getExpressions(breakStatement.getLabel(),instanceChecker));
			}
		}
		else if(statement instanceof ContinueStatement) {
			ContinueStatement continueStatement = (ContinueStatement)statement;
			if(continueStatement.getLabel() != null){
				expressionList.addAll(getExpressions(continueStatement.getLabel(),instanceChecker));
			}
		}
		return expressionList;
	}
	
	//This is copied from jextract
	public static List<Expression> getExpressions(Expression expression, ExpressionInstanceChecker instanceChecker) {
		List<Expression> expressionList = new ArrayList<Expression>();
		if(expression instanceof MethodInvocation) {
			MethodInvocation methodInvocation = (MethodInvocation)expression;
			if(methodInvocation.getExpression() != null){
				expressionList.addAll(getExpressions(methodInvocation.getExpression(),instanceChecker));
			}
			@SuppressWarnings("unchecked")
			List<Expression> arguments = methodInvocation.arguments();
			for(Expression argument : arguments){
				expressionList.addAll(getExpressions(argument,instanceChecker));
			}
			if(instanceChecker.instanceOf(methodInvocation)){
				expressionList.add(methodInvocation);
			}
		}
		else if(expression instanceof Assignment) {
			Assignment assignment = (Assignment)expression;
			expressionList.addAll(getExpressions(assignment.getLeftHandSide(),instanceChecker));
			expressionList.addAll(getExpressions(assignment.getRightHandSide(),instanceChecker));
			if(instanceChecker.instanceOf(assignment)){
				expressionList.add(assignment);
			}
		}
		else if(expression instanceof CastExpression) {
			CastExpression castExpression = (CastExpression)expression;
			expressionList.addAll(getExpressions(castExpression.getExpression(),instanceChecker));
			if(instanceChecker.instanceOf(castExpression)){
				expressionList.add(castExpression);
			}
		}
		else if(expression instanceof ClassInstanceCreation) {
			ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation)expression;
			if(classInstanceCreation.getExpression() != null){
				expressionList.addAll(getExpressions(classInstanceCreation.getExpression(),instanceChecker));
			}
			@SuppressWarnings("unchecked")
			List<Expression> arguments = classInstanceCreation.arguments();
			for(Expression argument : arguments){
				expressionList.addAll(getExpressions(argument,instanceChecker));
			}
			if(instanceChecker.instanceOf(classInstanceCreation)){
				expressionList.add(classInstanceCreation);
			}
			AnonymousClassDeclaration anonymousClassDeclaration = classInstanceCreation.getAnonymousClassDeclaration();
			if(anonymousClassDeclaration != null) {
				expressionList.addAll(getExpressions(anonymousClassDeclaration,instanceChecker));
			}
		}
		else if(expression instanceof ConditionalExpression) {
			ConditionalExpression conditionalExpression = (ConditionalExpression)expression;
			expressionList.addAll(getExpressions(conditionalExpression.getExpression(),instanceChecker));
			expressionList.addAll(getExpressions(conditionalExpression.getThenExpression(),instanceChecker));
			expressionList.addAll(getExpressions(conditionalExpression.getElseExpression(),instanceChecker));
			if(instanceChecker.instanceOf(conditionalExpression)){
				expressionList.add(conditionalExpression);
			}
		}
		else if(expression instanceof FieldAccess) {
			FieldAccess fieldAccess = (FieldAccess)expression;
			expressionList.addAll(getExpressions(fieldAccess.getExpression(),instanceChecker));
			expressionList.addAll(getExpressions(fieldAccess.getName(),instanceChecker));
			if(instanceChecker.instanceOf(fieldAccess)){
				expressionList.add(fieldAccess);
			}
		}
		else if(expression instanceof InfixExpression) {
			InfixExpression infixExpression = (InfixExpression)expression;
			expressionList.addAll(getExpressions(infixExpression.getLeftOperand(),instanceChecker));
			expressionList.addAll(getExpressions(infixExpression.getRightOperand(),instanceChecker));
			@SuppressWarnings("unchecked")
			List<Expression> extendedOperands = infixExpression.extendedOperands();
			for(Expression operand : extendedOperands){
				expressionList.addAll(getExpressions(operand,instanceChecker));
			}
			if(instanceChecker.instanceOf(infixExpression)){
				expressionList.add(infixExpression);
			}
		}
		else if(expression instanceof InstanceofExpression) {
			InstanceofExpression instanceofExpression = (InstanceofExpression)expression;
			expressionList.addAll(getExpressions(instanceofExpression.getLeftOperand(),instanceChecker));
			if(instanceChecker.instanceOf(instanceofExpression)){
				expressionList.add(instanceofExpression);
			}
		}
		else if(expression instanceof ParenthesizedExpression) {
			ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression)expression;
			expressionList.addAll(getExpressions(parenthesizedExpression.getExpression(),instanceChecker));
			if(instanceChecker.instanceOf(parenthesizedExpression)){
				expressionList.add(parenthesizedExpression);
			}
		}
		else if(expression instanceof PostfixExpression) {
			PostfixExpression postfixExpression = (PostfixExpression)expression;
			expressionList.addAll(getExpressions(postfixExpression.getOperand(),instanceChecker));
			if(instanceChecker.instanceOf(postfixExpression)){
				expressionList.add(postfixExpression);
			}
		}
		else if(expression instanceof PrefixExpression) {
			PrefixExpression prefixExpression = (PrefixExpression)expression;
			expressionList.addAll(getExpressions(prefixExpression.getOperand(),instanceChecker));
			if(instanceChecker.instanceOf(prefixExpression)){
				expressionList.add(prefixExpression);
			}
		}
		else if(expression instanceof SuperMethodInvocation) {
			SuperMethodInvocation superMethodInvocation = (SuperMethodInvocation)expression;
			@SuppressWarnings("unchecked")
			List<Expression> arguments = superMethodInvocation.arguments();
			for(Expression argument : arguments){
				expressionList.addAll(getExpressions(argument,instanceChecker));
			}
			if(instanceChecker.instanceOf(superMethodInvocation)){
				expressionList.add(superMethodInvocation);
			}
		}
		else if(expression instanceof VariableDeclarationExpression) {
			VariableDeclarationExpression variableDeclarationExpression = (VariableDeclarationExpression)expression;
			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment> fragments = variableDeclarationExpression.fragments();
			for(VariableDeclarationFragment fragment : fragments) {
				Expression nameExpression = fragment.getName();
				expressionList.addAll(getExpressions(nameExpression,instanceChecker));
				Expression initializerExpression = fragment.getInitializer();
				expressionList.addAll(getExpressions(initializerExpression,instanceChecker));
			}
			if(instanceChecker.instanceOf(variableDeclarationExpression)){
				expressionList.add(variableDeclarationExpression);
			}
		}
		else if(expression instanceof ArrayAccess) {
			ArrayAccess arrayAccess = (ArrayAccess)expression;
			expressionList.addAll(getExpressions(arrayAccess.getArray(),instanceChecker));
			expressionList.addAll(getExpressions(arrayAccess.getIndex(),instanceChecker));
			if(instanceChecker.instanceOf(arrayAccess)){
				expressionList.add(arrayAccess);
			}
		}
		else if(expression instanceof ArrayCreation) {
			ArrayCreation arrayCreation = (ArrayCreation)expression;
			@SuppressWarnings("unchecked")
			List<Expression> dimensions = arrayCreation.dimensions();
			for(Expression dimension : dimensions){
				expressionList.addAll(getExpressions(dimension,instanceChecker));
			}
			expressionList.addAll(getExpressions(arrayCreation.getInitializer(),instanceChecker));
			if(instanceChecker.instanceOf(arrayCreation)){
				expressionList.add(arrayCreation);
			}
		}
		else if(expression instanceof ArrayInitializer) {
			ArrayInitializer arrayInitializer = (ArrayInitializer)expression;
			@SuppressWarnings("unchecked")
			List<Expression> expressions = arrayInitializer.expressions();
			for(Expression arrayInitializerExpression : expressions){
				expressionList.addAll(getExpressions(arrayInitializerExpression,instanceChecker));
			}
			if(instanceChecker.instanceOf(arrayInitializer)){
				expressionList.add(arrayInitializer);
			}
		}
		else if(expression instanceof SimpleName) {
			SimpleName simpleName = (SimpleName)expression;
			if(instanceChecker.instanceOf(simpleName)){
				expressionList.add(simpleName);
			}
		}
		else if(expression instanceof QualifiedName) {
			QualifiedName qualifiedName = (QualifiedName)expression;
			expressionList.addAll(getExpressions(qualifiedName.getQualifier(),instanceChecker));
			expressionList.addAll(getExpressions(qualifiedName.getName(),instanceChecker));
			if(instanceChecker.instanceOf(qualifiedName)){
				expressionList.add(qualifiedName);
			}
		}
		else if(expression instanceof SuperFieldAccess) {
			SuperFieldAccess superFieldAccess = (SuperFieldAccess)expression;
			expressionList.addAll(getExpressions(superFieldAccess.getName(),instanceChecker));
			if(instanceChecker.instanceOf(superFieldAccess)){
				expressionList.add(superFieldAccess);
			}
		}
		else if(expression instanceof ThisExpression) {
			ThisExpression thisExpression = (ThisExpression)expression;
			if(thisExpression.getQualifier() != null){
				expressionList.addAll(getExpressions(thisExpression.getQualifier(),instanceChecker));
			}
			if(instanceChecker.instanceOf(thisExpression)){
				expressionList.add(thisExpression);
			}
		}
		else if(expression instanceof TypeLiteral) {
			TypeLiteral typeLiteral = (TypeLiteral)expression;
			if(instanceChecker.instanceOf(typeLiteral)){
				expressionList.add(typeLiteral);
			}
		}
		else if(expression instanceof StringLiteral) {
			StringLiteral stringLiteral = (StringLiteral)expression;
			if(instanceChecker.instanceOf(stringLiteral)){
				expressionList.add(stringLiteral);
			}
		}
		else if(expression instanceof NullLiteral) {
			NullLiteral nullLiteral = (NullLiteral)expression;
			if(instanceChecker.instanceOf(nullLiteral)){
				expressionList.add(nullLiteral);
			}
		}
		else if(expression instanceof NumberLiteral) {
			NumberLiteral numberLiteral = (NumberLiteral)expression;
			if(instanceChecker.instanceOf(numberLiteral)){
				expressionList.add(numberLiteral);
			}
		}
		else if(expression instanceof BooleanLiteral) {
			BooleanLiteral booleanLiteral = (BooleanLiteral)expression;
			if(instanceChecker.instanceOf(booleanLiteral)){
				expressionList.add(booleanLiteral);
			}
		}
		else if(expression instanceof CharacterLiteral) {
			CharacterLiteral characterLiteral = (CharacterLiteral)expression;
			if(instanceChecker.instanceOf(characterLiteral)){
				expressionList.add(characterLiteral);
			}
		}
		return expressionList;
	}
	
	//This is copied from jextract
	public static List<Expression> getExpressions(AnonymousClassDeclaration anonymousClassDeclaration, ExpressionInstanceChecker instanceChecker) {
		List<Expression> expressionList = new ArrayList<Expression>();
		@SuppressWarnings("unchecked")
		List<BodyDeclaration> bodyDeclarations = anonymousClassDeclaration.bodyDeclarations();
		for(BodyDeclaration bodyDeclaration : bodyDeclarations) {
			if(bodyDeclaration instanceof MethodDeclaration) {
				MethodDeclaration methodDeclaration = (MethodDeclaration)bodyDeclaration;
				Block body = methodDeclaration.getBody();
				if(body != null) {
					@SuppressWarnings("unchecked")
					List<Statement> statements = body.statements();
					for(Statement statement : statements) {
						expressionList.addAll(getExpressions(statement,instanceChecker));
					}
				}
			}
		}
		return expressionList;
	}
		
	//This is copied from jextract
	public static SimpleName getRightMostSimpleName(Expression expression) {
		SimpleName simpleName = null;
		if(expression instanceof SimpleName) {
			simpleName = (SimpleName)expression;
		}
		else if(expression instanceof QualifiedName) {
			QualifiedName leftHandSideQualifiedName = (QualifiedName)expression;
			simpleName = leftHandSideQualifiedName.getName();
		}
		else if(expression instanceof FieldAccess) {
			FieldAccess leftHandSideFieldAccess = (FieldAccess)expression;
			simpleName = leftHandSideFieldAccess.getName();
		}
		else if(expression instanceof ArrayAccess) {
			ArrayAccess leftHandSideArrayAccess = (ArrayAccess)expression;
			Expression array = leftHandSideArrayAccess.getArray();
			if(array instanceof SimpleName) {
				simpleName = (SimpleName)array;
			}
			else if(array instanceof QualifiedName) {
				QualifiedName arrayQualifiedName = (QualifiedName)array;
				simpleName = arrayQualifiedName.getName();
			}
			else if(array instanceof FieldAccess) {
				FieldAccess arrayFieldAccess = (FieldAccess)array;
				simpleName = arrayFieldAccess.getName();
			}
		}
		return simpleName;
	}

	//Modified by myself
	public static List<SimpleName> getVariableSimpleNames(Expression expression, boolean isDeclaration) {
		List<SimpleName> variableList = new ArrayList<SimpleName>();
		if(expression instanceof MethodInvocation) {
			MethodInvocation methodInvocation = (MethodInvocation)expression;
			if(methodInvocation.getExpression() != null){
				variableList.addAll(getVariableSimpleNames(methodInvocation.getExpression(),isDeclaration));
			}
			@SuppressWarnings("unchecked")
			List<Expression> arguments = methodInvocation.arguments();
			for(Expression argument : arguments){
				variableList.addAll(getVariableSimpleNames(argument,isDeclaration));
			}
		}
		else if(expression instanceof Assignment) {
			Assignment assignment = (Assignment)expression;
			variableList.addAll(getVariableSimpleNames(assignment.getLeftHandSide(),isDeclaration));
			variableList.addAll(getVariableSimpleNames(assignment.getRightHandSide(),isDeclaration));
		}
		else if(expression instanceof CastExpression) {
			CastExpression castExpression = (CastExpression)expression;
			variableList.addAll(getVariableSimpleNames(castExpression.getExpression(),isDeclaration));
		}
		else if(expression instanceof ClassInstanceCreation) {
			ClassInstanceCreation classInstanceCreation = (ClassInstanceCreation)expression;
			if(classInstanceCreation.getExpression() != null){
				variableList.addAll(getVariableSimpleNames(classInstanceCreation.getExpression(),isDeclaration));
			}
			@SuppressWarnings("unchecked")
			List<Expression> arguments = classInstanceCreation.arguments();
			for(Expression argument : arguments){
				variableList.addAll(getVariableSimpleNames(argument,isDeclaration));
			}
			/*
			//We ignore this for now!
			AnonymousClassDeclaration anonymousClassDeclaration = classInstanceCreation.getAnonymousClassDeclaration();
			if(anonymousClassDeclaration != null) {
				variableList.addAll(getVariableSimpleNames(anonymousClassDeclaration,isDeclaration));
			}
			*/
		}
		else if(expression instanceof ConditionalExpression) {
			ConditionalExpression conditionalExpression = (ConditionalExpression)expression;
			variableList.addAll(getVariableSimpleNames(conditionalExpression.getExpression(),isDeclaration));
			variableList.addAll(getVariableSimpleNames(conditionalExpression.getThenExpression(),isDeclaration));
			variableList.addAll(getVariableSimpleNames(conditionalExpression.getElseExpression(),isDeclaration));
		}
		else if(expression instanceof FieldAccess) {
			FieldAccess fieldAccess = (FieldAccess)expression;
			variableList.addAll(getVariableSimpleNames(fieldAccess.getExpression(),isDeclaration));
			variableList.addAll(getVariableSimpleNames(fieldAccess.getName(),isDeclaration));
		}
		else if(expression instanceof InfixExpression) {
			InfixExpression infixExpression = (InfixExpression)expression;
			variableList.addAll(getVariableSimpleNames(infixExpression.getLeftOperand(),isDeclaration));
			variableList.addAll(getVariableSimpleNames(infixExpression.getRightOperand(),isDeclaration));
			@SuppressWarnings("unchecked")
			List<Expression> extendedOperands = infixExpression.extendedOperands();
			for(Expression operand : extendedOperands){
				variableList.addAll(getVariableSimpleNames(operand,isDeclaration));
			}
		}
		else if(expression instanceof InstanceofExpression) {
			InstanceofExpression instanceofExpression = (InstanceofExpression)expression;
			variableList.addAll(getVariableSimpleNames(instanceofExpression.getLeftOperand(),isDeclaration));
		}
		else if(expression instanceof ParenthesizedExpression) {
			ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression)expression;
			variableList.addAll(getVariableSimpleNames(parenthesizedExpression.getExpression(),isDeclaration));
		}
		else if(expression instanceof PostfixExpression) {
			PostfixExpression postfixExpression = (PostfixExpression)expression;
			variableList.addAll(getVariableSimpleNames(postfixExpression.getOperand(),isDeclaration));
		}
		else if(expression instanceof PrefixExpression) {
			PrefixExpression prefixExpression = (PrefixExpression)expression;
			variableList.addAll(getVariableSimpleNames(prefixExpression.getOperand(),isDeclaration));
		}
		else if(expression instanceof SuperMethodInvocation) {
			SuperMethodInvocation superMethodInvocation = (SuperMethodInvocation)expression;
			@SuppressWarnings("unchecked")
			List<Expression> arguments = superMethodInvocation.arguments();
			for(Expression argument : arguments){
				variableList.addAll(getVariableSimpleNames(argument,isDeclaration));
			}
		}
		else if(expression instanceof VariableDeclarationExpression) {
			VariableDeclarationExpression variableDeclarationExpression = (VariableDeclarationExpression)expression;
			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment> fragments = variableDeclarationExpression.fragments();
			for(VariableDeclarationFragment fragment : fragments) {
				Expression nameExpression = fragment.getName();
				variableList.addAll(getVariableSimpleNames(nameExpression,isDeclaration));
				Expression initializerExpression = fragment.getInitializer();
				variableList.addAll(getVariableSimpleNames(initializerExpression,isDeclaration));
			}
		}
		else if(expression instanceof ArrayAccess) {
			ArrayAccess arrayAccess = (ArrayAccess)expression;
			variableList.addAll(getVariableSimpleNames(arrayAccess.getArray(),isDeclaration));
			variableList.addAll(getVariableSimpleNames(arrayAccess.getIndex(),isDeclaration));
		}
		else if(expression instanceof ArrayCreation) {
			ArrayCreation arrayCreation = (ArrayCreation)expression;
			@SuppressWarnings("unchecked")
			List<Expression> dimensions = arrayCreation.dimensions();
			for(Expression dimension : dimensions){
				variableList.addAll(getVariableSimpleNames(dimension,isDeclaration));
			}
			variableList.addAll(getVariableSimpleNames(arrayCreation.getInitializer(),isDeclaration));
		}
		else if(expression instanceof ArrayInitializer) {
			ArrayInitializer arrayInitializer = (ArrayInitializer)expression;
			@SuppressWarnings("unchecked")
			List<Expression> expressions = arrayInitializer.expressions();
			for(Expression arrayInitializerExpression : expressions){
				variableList.addAll(getVariableSimpleNames(arrayInitializerExpression,isDeclaration));
			}
		}
		else if(expression instanceof QualifiedName) {
			QualifiedName qualifiedName = (QualifiedName)expression;
			variableList.addAll(getVariableSimpleNames(qualifiedName.getQualifier(),isDeclaration));
			variableList.addAll(getVariableSimpleNames(qualifiedName.getName(),isDeclaration));
		}
		else if(expression instanceof SuperFieldAccess) {
			SuperFieldAccess superFieldAccess = (SuperFieldAccess)expression;
			variableList.addAll(getVariableSimpleNames(superFieldAccess.getName(),isDeclaration));
		}
		else if(expression instanceof ThisExpression) {
			ThisExpression thisExpression = (ThisExpression)expression;
			if(thisExpression.getQualifier() != null){
				variableList.addAll(getVariableSimpleNames(thisExpression.getQualifier(),isDeclaration));
			}
		}
		else if(expression instanceof SimpleName) {
			SimpleName simpleName = (SimpleName)expression;
			if(simpleName.isDeclaration()==isDeclaration){
				variableList.add(simpleName);
			}
		}
		return variableList;
	}

	//Modified by myself
	public static List<SimpleName> getVariableSimpleNames(Statement statement, boolean isDeclaration) throws Exception {
		List<SimpleName> variableList = new ArrayList<SimpleName>();
		
		if(statement instanceof AssertStatement) {
			AssertStatement assertStatement = (AssertStatement)statement;
			Expression expression = assertStatement.getExpression();
			variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
			Expression message = assertStatement.getMessage();
			if(message != null){
				variableList.addAll(getVariableSimpleNames(message,isDeclaration));
			}
		}
		else if(statement instanceof BreakStatement) {
			BreakStatement breakStatement = (BreakStatement)statement;
			if(breakStatement.getLabel() != null){
				variableList.addAll(getVariableSimpleNames(breakStatement.getLabel(),isDeclaration));
			}
		}
		else if(statement instanceof ConstructorInvocation) {
			ConstructorInvocation constructorInvocation = (ConstructorInvocation)statement;
			@SuppressWarnings("unchecked")
			List<Expression> arguments = constructorInvocation.arguments();
			for(Expression argument : arguments){
				variableList.addAll(getVariableSimpleNames(argument,isDeclaration));
			}
		}
		else if(statement instanceof ContinueStatement) {
			ContinueStatement continueStatement = (ContinueStatement)statement;
			if(continueStatement.getLabel() != null){
				variableList.addAll(getVariableSimpleNames(continueStatement.getLabel(),isDeclaration));
			}
		}
		else if(statement instanceof DoStatement) {
			DoStatement doStatement = (DoStatement)statement;
			Expression expression = doStatement.getExpression();
			variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
			//In my implementation, body will be taken care of in other place.
		}
		else if(statement instanceof EmptyStatement) {
			//Nothing to add.
		}
		else if(statement instanceof EnhancedForStatement) {
			EnhancedForStatement enhancedForStatement = (EnhancedForStatement)statement;
			Expression expression = enhancedForStatement.getExpression();
			SingleVariableDeclaration variableDeclaration = enhancedForStatement.getParameter();
			variableList.addAll(getVariableSimpleNames(variableDeclaration.getName(),isDeclaration));
			if(variableDeclaration.getInitializer() != null){
				variableList.addAll(getVariableSimpleNames(variableDeclaration.getInitializer(),isDeclaration));
			}
			variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
			//In my implementation, body will be taken care of in other place.
		}
		else if(statement instanceof ExpressionStatement) {
			ExpressionStatement expressionStatement = (ExpressionStatement)statement;
			Expression expression = expressionStatement.getExpression();
			variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
		}
		else if(statement instanceof ForStatement) {
			ForStatement forStatement = (ForStatement)statement;
			@SuppressWarnings("unchecked")
			List<Expression> initializers = forStatement.initializers();
			for(Expression initializer : initializers){
				variableList.addAll(getVariableSimpleNames(initializer,isDeclaration));
			}
			Expression expression = forStatement.getExpression();
			if(expression != null){
				variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
			}
			@SuppressWarnings("unchecked")
			List<Expression> updaters = forStatement.updaters();
			for(Expression updater : updaters){
				variableList.addAll(getVariableSimpleNames(updater,isDeclaration));
			}
			//In my implementation, body will be taken care of in other place.
		}
		else if(statement instanceof IfStatement) {
			IfStatement ifStatement = (IfStatement)statement;
			Expression expression = ifStatement.getExpression();
			variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
			//In my implementation, body will be taken care of in other place.
		}
		else if(statement instanceof LabeledStatement) {
			LabeledStatement labeledStatement = (LabeledStatement)statement;
			if(labeledStatement.getLabel() != null){
				variableList.addAll(getVariableSimpleNames(labeledStatement.getLabel(),isDeclaration));
			}
			//In my implementation, body will be taken care of in other place.
		}
		else if(statement instanceof ReturnStatement) {
			ReturnStatement returnStatement = (ReturnStatement)statement;
			Expression expression = returnStatement.getExpression();
			variableList.addAll(getVariableSimpleNames(expression,isDeclaration));	
		}
		else if(statement instanceof SuperConstructorInvocation) {
			SuperConstructorInvocation superConstructorInvocation = (SuperConstructorInvocation)statement;
			if(superConstructorInvocation.getExpression() != null){
				variableList.addAll(getVariableSimpleNames(superConstructorInvocation.getExpression(),isDeclaration));
			}
			@SuppressWarnings("unchecked")
			List<Expression> arguments = superConstructorInvocation.arguments();
			for(Expression argument : arguments){
				variableList.addAll(getVariableSimpleNames(argument,isDeclaration));
			}
		}
		else if(statement instanceof SwitchCase) {
			SwitchCase switchCase = (SwitchCase)statement;
			Expression expression = switchCase.getExpression();
			if(expression != null){
				variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
			}
		}
		else if(statement instanceof SwitchStatement) {
			SwitchStatement switchStatement = (SwitchStatement)statement;
			Expression expression = switchStatement.getExpression();
			variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
			//In my implementation, body will be taken care of in other place.
		}
		else if(statement instanceof SynchronizedStatement) {
			SynchronizedStatement synchronizedStatement = (SynchronizedStatement)statement;
			Expression expression = synchronizedStatement.getExpression();
			variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
			//In my implementation, body will be taken care of in other place.
		}
		else if(statement instanceof ThrowStatement) {
			ThrowStatement throwStatement = (ThrowStatement)statement;
			Expression expression = throwStatement.getExpression();
			variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
		}
		else if(statement instanceof TryStatement) {
			TryStatement tryStatement = (TryStatement)statement;
			@SuppressWarnings("unchecked")
			List<VariableDeclarationExpression> resources = tryStatement.resources();
			for(VariableDeclarationExpression expression : resources) {
				variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
			}
			//In my implementation, body will be taken care of in other place.
			@SuppressWarnings("unchecked")
			List<CatchClause> catchClauses = tryStatement.catchClauses();
			for(CatchClause catchClause : catchClauses) {
				SingleVariableDeclaration variableDeclaration = catchClause.getException();
				variableList.addAll(getVariableSimpleNames(variableDeclaration.getName(),isDeclaration));
				if(variableDeclaration.getInitializer() != null){
					variableList.addAll(getVariableSimpleNames(variableDeclaration.getInitializer(),isDeclaration));
				}
				//In my implementation, body will be taken care of in other place.
			}
			//In my implementation, body will be taken care of in other place.
		}
		else if(statement instanceof TypeDeclarationStatement){
			//Not handled yet.
		}
		else if(statement instanceof VariableDeclarationStatement) {
			VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement)statement;
			@SuppressWarnings("unchecked")
			List<VariableDeclarationFragment> fragments = variableDeclarationStatement.fragments();
			for(VariableDeclarationFragment fragment : fragments) {
				variableList.addAll(getVariableSimpleNames(fragment.getName(),isDeclaration));
				variableList.addAll(getVariableSimpleNames(fragment.getInitializer(),isDeclaration));
			}
		}
		else if(statement instanceof WhileStatement) {
			WhileStatement whileStatement = (WhileStatement)statement;
			Expression expression = whileStatement.getExpression();
			variableList.addAll(getVariableSimpleNames(expression,isDeclaration));
			//In my implementation, body will be taken care of in other place.
		}
		else{
			throw new Exception("Unexpected statement type in getVariableSimpleNames()");
		}
		return variableList;
	}
}
