package learning;

public class JaccardDistance implements Distance{

	@Override
	public double distanceBetweenSets(boolean[] a, boolean[] b) {
		assert a.length==b.length;
		int inter = 0;
		int union = 0;
		for(int i = 0; i < a.length; i++){
			if(a[i] && b[i]){
				inter ++;
			}
			else if( a[i] || b[i]){
				union ++;
			}
		}
		if( union == 0){
			return 0;
		}
		else{
			return (double)(union - inter)/(double)(union);
		}
	}
}
