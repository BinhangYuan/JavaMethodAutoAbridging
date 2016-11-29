package testCodes;



public class Solution404 {
	private class TreeNode {
	     int val;
	     TreeNode left;
	     TreeNode right;
	     @SuppressWarnings("unused")
	     TreeNode(int x) { 
	    	 this.val = x; 
	     }
	}
	
	public int sumOfLeftLeaves(TreeNode root) {
        if(root==null){
            return 0;
        }
        int sum = leftLeaveValue(root), foo=0;
        TreeNode current = root.left;
        while(current!=null){
            sum += sumOfLeftLeaves(current.right);
            current = current.left;
        }
        if(root.right!=null){
            sum += sumOfLeftLeaves(root.right);
        }
        return sum;
    }
    
    public int leftLeaveValue(TreeNode root){
        if(root==null || root.left==null){
            return 0;
        }
        TreeNode current = root;
        while(current.left!=null){
            current = current.left;
        }
        return current.right==null?current.val:0;
    }
}