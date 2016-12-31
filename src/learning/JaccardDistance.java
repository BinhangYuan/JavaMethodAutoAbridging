package learning;

import org.eclipse.core.runtime.Assert;

public class JaccardDistance implements Distance{

	@Override
	public double distanceBetweenSets(boolean[] a, boolean[] b) {
		Assert.isTrue(a.length==b.length);
		int inter = 0;
		int union = 0;
		for(int i = 0; i < a.length; i++){
			if(a[i] && b[i]){
				inter ++;
				union ++;
			}
			else if( a[i] || b[i]){
				union ++;
			}
		}
		if(union == 0){
			return 0;
		}
		else{
			double dist = (double)(union - inter)/(double)(union);
			Assert.isTrue(0 <= dist && dist <=1);
			return dist;
		}
	}
}
