package learning;

public class ManualLabel {
	private int lineConstraint;
	
	private boolean [] booleanLabels;
	
	public ManualLabel(int lines, boolean [] labels){
		this.lineConstraint = lines;
		this.booleanLabels = labels;
	}
	
	public int getLineConstraintt(){
		return this.lineConstraint;
	}
	
	public boolean [] getBooleanLabels(){
		return this.booleanLabels;
	}
}
