package testCodes;

import java.util.HashMap;
import java.util.LinkedList;

public class Solution350 {
    public int[] intersect(int[] nums1, int[] nums2) {
        HashMap<Integer,Integer> nums1Count = new HashMap<Integer,Integer>();
        for(int i = 0; i < nums1.length; i++){
            if(nums1Count.containsKey(nums1[i])){
                nums1Count.put(nums1[i],nums1Count.get(nums1[i])+1);
            }
            else{
                nums1Count.put(nums1[i],1);
            }
        }
        LinkedList<Integer> resultTemp = new LinkedList<Integer>();
        for(int i = 0; i < nums2.length; i++){
            if(nums1Count.containsKey(nums2[i])){
                resultTemp.add(nums2[i]);
                int countValue = nums1Count.get(nums2[i]);
                countValue--;
                if(countValue == 0){
                    nums1Count.remove(nums2[i]);
                }
                else{
                    nums1Count.put(nums2[i],countValue);
                }
            }
        }
        int [] result = new int[resultTemp.size()];
        for(int i = 0; i < resultTemp.size(); i++){
            result[i] = resultTemp.get(i);
        }
        return result;
    }
}