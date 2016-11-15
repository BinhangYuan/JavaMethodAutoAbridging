package testCodes;

public class Solution453 {
	
	public int minMoves(int[] nums) {
		int sum = 0;
		if(nums.length==0){
			return sum;
	    }
		int min = nums[0];
	    for(int i=1; i<nums.length; i++){
	    	if(nums[i]>=min){
	    		sum += (nums[i]-min);
	        }
	        else{
	        	sum += (min-nums[i])*i;
	            min = nums[i];
	        }
	    }
	    return sum;
	}
}
