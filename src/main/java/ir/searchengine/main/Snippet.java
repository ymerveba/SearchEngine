
/*
Anup Rokkam Pratap: 80529276
Yuvaraj Merve Basavaraj: 91605284
*/

package ir.searchengine.main;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class Snippet {

	
	public static ArrayList<Integer> printMatches(String text, String word) {
		
		
		ArrayList<Integer> index = new ArrayList<Integer>();
		try{
	    Pattern pattern = Pattern.compile(word);
	    Matcher matcher = pattern.matcher(text);
	    matcher.find();
        index.add(matcher.start());
        index.add(matcher.end());
        
		}catch(Exception e){
			try{
			String [] new_regex = word.split(" ");
			Pattern pattern = Pattern.compile(new_regex[1]);
		    Matcher matcher = pattern.matcher(text);
		    matcher.find();
	        index.add(matcher.start());
	        index.add(matcher.end());
			}catch(Exception i){
				
			}
			
			return index;
		}
	 
		
		return index;
	}
	
	
	private static String gen_Snippet(ArrayList<Integer> index, String text){
		int length = text.length();
		if(length == 0 || index.size() == 0){
			return "No text to display preview";
		}
		
		int start = index.get(0);
		int end = index.get(1);
		int new_start = start - 100;
		int new_end = end + 100;
		
		String snippet = new String();
		
		//return appropriate msg
		
		
		if((new_start)<0){
			new_start = 0;
		}
		if(new_end>length){
			new_end = length;
		}
		
		snippet = text.substring(new_start, new_end);
		
		//System.out.println(snippet+ "...");
		return snippet.substring(snippet.indexOf(' ')+1);
		
		
	}
	
	public static String generate_snippet(String word,String text){
		
		ArrayList<Integer> index = printMatches(text.toLowerCase(), word.toLowerCase());
		return gen_Snippet(index, text);
	}
		
}
