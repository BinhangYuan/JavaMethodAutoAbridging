package statementGraph;

public class DependencePair{
	public int sourceIndex;
	public int destIndex;
	
	public DependencePair(int source, int dest){
		this.sourceIndex = source;
		this.destIndex = dest;
	}
	
}