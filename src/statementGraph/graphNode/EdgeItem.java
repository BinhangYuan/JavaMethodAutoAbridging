package statementGraph.graphNode;

public class EdgeItem {
	public static int DDGPrority = 1;
	public static int ASTPrority = 1;
	public static int CFGPrority = 1;
	
	public StatementWrapper start;
	public StatementWrapper end;
	/*
	 *	Priority = 1 means sequential order;
	 *	Priority = 2 means the last statement in a block linked back to the start point;
	 *	Priority = 3 means the break jump with a labeled statement.   
	 */
	int priority;
	
	public EdgeItem(StatementWrapper s, StatementWrapper e, int p){
		this.start = s;
		this.end = e;
		this.priority = p;
	}
}