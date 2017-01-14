package textExtractor;

import java.util.LinkedList;

public class TextUtils {
	
	static private boolean isAllUppercase(String str){
		for(int i=0; i<str.length(); i++){
			if(str.charAt(i)<='Z' && str.charAt(i)>='A'){
				continue;
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	
	static private boolean isAllLowercase(String str){
		for(int i=0; i<str.length(); i++){
			if(str.charAt(i)<='z' && str.charAt(i)>='a'){
				continue;
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	
	static private LinkedList<String> splitCamelNaming(String str){
		LinkedList<String> result = new LinkedList<String>();
		int start = 0;
		for(int i=1;i<str.length();i++){
			if(str.charAt(i)<='Z' && str.charAt(i)>='A'){
				result.add(str.substring(start,i).toLowerCase());
				start = i;
			}
		}
		result.add(str.substring(start,str.length()).toLowerCase());
		return result;
	}
	
	
	/*
	 * Parse statement into word list by java naming conventions
	 */
	static public LinkedList<String> parseCodeFragment(String codeFragment){
		LinkedList<String> result = new LinkedList<String>();
		//Remove operators;
		codeFragment = codeFragment.replace('+', ' ');
		codeFragment = codeFragment.replace('-', ' ');
		codeFragment = codeFragment.replace('*', ' ');
		codeFragment = codeFragment.replace('/', ' ');
		codeFragment = codeFragment.replace('%', ' ');
		codeFragment = codeFragment.replace('=', ' ');
		codeFragment = codeFragment.replace('!', ' ');
		codeFragment = codeFragment.replace('&', ' ');
		codeFragment = codeFragment.replace('|', ' ');
		codeFragment = codeFragment.replace('^', ' ');
		codeFragment = codeFragment.replace('~', ' ');
		codeFragment = codeFragment.replace('<', ' ');
		codeFragment = codeFragment.replace('>', ' ');
		codeFragment = codeFragment.replace('(', ' ');
		codeFragment = codeFragment.replace(')', ' ');
		codeFragment = codeFragment.replace('[', ' ');
		codeFragment = codeFragment.replace(']', ' ');
		codeFragment = codeFragment.replace('{', ' ');
		codeFragment = codeFragment.replace('}', ' ');
		codeFragment = codeFragment.replace('.', ' ');
		codeFragment = codeFragment.replace('?', ' ');
		codeFragment = codeFragment.replace(':', ' ');
		codeFragment = codeFragment.replace('_', ' ');
		codeFragment = codeFragment.replace(';', ' ');
		codeFragment = codeFragment.replace(',', ' ');
		//Remove number
		codeFragment = codeFragment.replace('0', ' ');
		codeFragment = codeFragment.replace('1', ' ');
		codeFragment = codeFragment.replace('2', ' ');
		codeFragment = codeFragment.replace('3', ' ');
		codeFragment = codeFragment.replace('4', ' ');
		codeFragment = codeFragment.replace('5', ' ');
		codeFragment = codeFragment.replace('6', ' ');
		codeFragment = codeFragment.replace('7', ' ');
		codeFragment = codeFragment.replace('8', ' ');
		codeFragment = codeFragment.replace('9', ' ');
		
		for(String word : codeFragment.split(" +")){
			if(isAllLowercase(word)){
				result.add(word);
			}
			else if(isAllUppercase(word)){
				result.add(word.toLowerCase());
			}
			else{
				result.addAll(splitCamelNaming(word));
			}
		}		
		return result;
	}
	
	public static void main(String[] args){
		String codeFragment = "private Map<StatementWrapper,Integer> index = new HashMap<StatementWrapper,Integer>();";
		for(String temp:parseCodeFragment(codeFragment)){
			System.out.println(temp);
		}
	}
}
