package ir.searchengine.QP;


import java.util.ArrayList;
import java.util.Scanner;

	public class Utilities {
		public static String delimiters = "[^a-zA-Z0-9]";
		public static ArrayList<String> tokenizeQuery(String s) {
			// TODO Write body!
			ArrayList<String> tokens = new ArrayList<String>();
			Scanner file_scanner = null;
			try{
			file_scanner = new Scanner(s);
			
			while(file_scanner.hasNext()){
				String i = file_scanner.next().toLowerCase().replaceAll("[^a-zA-Z0-9]","");
					tokens.add(i);
			}
			} 
			finally{
				file_scanner.close();
			}
			//System.out.println(tokens.toString());
			return tokens;
				
		}
	
}
