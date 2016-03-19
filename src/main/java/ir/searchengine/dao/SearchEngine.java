package ir.searchengine.dao;

import java.util.List;

import ir.searchengine.main.Result;

public class SearchEngine {

	public static void main(String[] args)  {
		
		//SearchEngineHelper.MongoDB();
		try{
			List<Result> SearchResults = SearchEngineHelper.fnSearch("donald bren school of information and computer science".toLowerCase());
		if(SearchResults.isEmpty()) System.out.println("Word Not found");
		
		else
		for(Result s : SearchResults){
			System.out.println(s.getName());
		}}catch(Exception e){
			System.out.println("Something went wront"+e);
		}
		

	}

}
