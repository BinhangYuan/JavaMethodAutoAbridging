package statementGraph.constraintAndFeatureEncoder;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jdt.core.dom.SimpleName;

public class DependencePair{
	
	public static int TYPE_AST = 1;
	public static int TYPE_CFG = 2;
	public static int TYPE_DDG = 3;
	
	public static int SETFLAG_TRUE = 1;
	public static int SETFLAG_FALSE = 0;
	public static int SETFLAG_UNSET = -1;
	
	public int dependenceType;
	
	public int sourceIndex;
	
	public int destIndex;
	
	private int setByManualLabel = SETFLAG_UNSET;
	
	public boolean getManualLabelFlag() throws Exception{
		if(this.setByManualLabel == SETFLAG_UNSET){
			throw new Exception("Try to acess unset manual label flag");
		}else{
			return this.setByManualLabel == SETFLAG_TRUE;
		}
	}
	
	public void setManuaLabelFlag(boolean flag){
		this.setByManualLabel = (flag?SETFLAG_TRUE:SETFLAG_FALSE);
	}
	
	
	private int setByLearning = SETFLAG_UNSET;
	
	public boolean getLearningFlag() throws Exception{
		if(this.setByLearning == SETFLAG_UNSET){
			throw new Exception("Try to acess unset learned flag");
		}else{
			return this.setByLearning == SETFLAG_TRUE;
		}
	}
	
	public void setLearningFlag(boolean flag){
		this.setByLearning = (flag?SETFLAG_TRUE:SETFLAG_FALSE);
	}
	
	
	private double compressionRate = 1.0;
	
	public void setCompressionRate(double r){
		this.compressionRate = r;
	}
	
	public double getCompressionRate(){
		return this.compressionRate;
	}
	
	private List<SimpleName> sharedVariable = new LinkedList<SimpleName>();
	
	public void addSharedVariable(SimpleName var){
		Assert.isTrue(var.isDeclaration());
		this.sharedVariable.add(var);
	}
	
	public DependencePair(int source, int dest){
		this.sourceIndex = source;
		this.destIndex = dest;
	}
	
	public DependencePair(int source, int dest, int type){
		this.sourceIndex = source;
		this.destIndex = dest;
		this.dependenceType = type;
	}
}


