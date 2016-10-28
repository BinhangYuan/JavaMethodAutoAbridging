package statementGraph;

import java.util.List;

public class BreakStatementItem extends ElementItem{

	private List<ElementItem> successors;
	
	@Override
	public List<ElementItem> getSuccessors() {
		// TODO Auto-generated method stub
		return successors;
	}

}

