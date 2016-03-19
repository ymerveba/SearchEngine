package ir.searchengine.dao;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import ir.searchengine.main.DocumentDetails;
import ir.searchengine.main.Result;
import ir.searchengine.main.Snippet;


public class SearchEngineHelper {
	static MongoClient mongoClient;
	static DB db;
	static DBCollection InvertedIndex;
	static DBCollection docMap;
	static DBCollection dbCollection; 
	public static String delimiters = "[^a-zA-Z0-9]";
	
	public static void MongoDB() throws UnknownHostException {

		mongoClient = new MongoClient( "localhost" , 27017 );
		db = mongoClient.getDB("db01");
		InvertedIndex = db.getCollection("InvIndex");
		docMap = db.getCollection("docMap");
	   dbCollection = db.getCollection("myIR_Collection7");
	}
	private static int titleHasWord(String title,String word){
		
		int i = 0;
		Pattern p = Pattern.compile(word);
		Matcher m = p.matcher( title );
		while (m.find()) {
		    i++;
		}
		return i;
	}
	 public static List<Result> fnSearch(String search) throws UnknownHostException{
	   	   
	      
		 SearchEngineHelper.MongoDB();
		   String[] searchArr = search.split(" ");
		   int querySize = searchArr.length;
		   double mag_query = 0.0;
		   
		   Map<String, Double> queryMap = new HashMap<String, Double>();
		   for(String word: searchArr){
			   
			   double queryWC = 0.0;
			   if(queryMap.containsKey(word)){
				   
				   queryWC = queryMap.get(word);
			   }
			   queryWC += 1.0/querySize;
			   queryMap.put(word, queryWC);
			   
		   }
		   

		   int queryWordPros = 0;
		   Map<String,RankObject> AndOrTempMap = new HashMap<String, RankObject>();
		    for(String word: searchArr){
			   
			   queryWordPros++;
			   BasicDBObject query = new BasicDBObject();
		       query.put("Token:", word);
		       DBCursor cursorWORDMAP = docMap.find(query);
		       DBCursor cursorIDF = InvertedIndex.find(query);
		       
		       if (cursorWORDMAP.hasNext()&&cursorIDF.hasNext()) {   
		           BasicDBObject obj = (BasicDBObject) cursorWORDMAP.next();	
		           BasicDBObject idfOBJ = (BasicDBObject) cursorIDF.next();
		           List<BasicDBObject> temp = (List<BasicDBObject>) obj.get("DOC:");
		         
		           double queryTF = queryMap.get(word);
		           queryTF = (queryTF)*(Double)idfOBJ.get("IDF:");
		           //System.out.println(queryTF);
		           
		           queryMap.put(word, queryTF);
		           
		           mag_query = Math.sqrt((Math.pow(mag_query, 2)+Math.pow(queryTF, 2)));
		         
		    
		           for(BasicDBObject tempObj: temp){
		        	   int countrobj = 1;
		        	   
		        	   String docid = tempObj.get("DocID:").toString();
		        	   double tfidfrobj = Double.parseDouble(String.valueOf(tempObj.get("TF-IDF:")));
		        	   	     // System.out.println(tfidfrobj);  	   
		        	   RankObject robj;
		        	   if(AndOrTempMap.containsKey(docid)){
		        		   robj = AndOrTempMap.get(docid);
		        		   countrobj = robj.qrWC;
		        		   robj.qrWC = countrobj+1;
		        		   
		        		   robj.tfidf_score += tfidfrobj;
		        		   
		        	   }
		        	   else{
		        		   robj = new RankObject();
		        		   robj.qrWC = countrobj;
		        		   robj.tfidf_score = tfidfrobj;
		        		   
		        		   
		        	   }
		        	   
		        	   robj.cos_score += (tfidfrobj*queryTF);
		        	   robj.mag_doc = Math.sqrt((Math.pow(robj.mag_doc, 2)+Math.pow(tfidfrobj, 2)));
		        	   
	        		   robj.posSet.addAll((List<Integer>)tempObj.get("Position:"));
	        		   robj.setTitle((String)tempObj.get("Title:"));
	        		   if(robj.getTitle()==null)continue;
	        		   //System.out.println(robj.getTitle());
	        		   String[] title = robj.getTitle().toLowerCase().split(" ");
	        		   List<String> titleList =Arrays.asList(title);
	        		   if(titleList.contains(word.toLowerCase())){
	        			   //System.out.println(robj.getTitle());
	        			  
	        			   robj.setTitle_score((double)titleHasWord(robj.getTitle().toLowerCase(), word.toLowerCase())/querySize);
	        			   if(robj.getTitle().equals("Donald Bren School of Information and Computer Sciences @ University of California, Irvine"))
	        				   robj.setPos_score(100);
	        		   }
	        		   if(queryWordPros == querySize&&querySize>1){
	        			   
	        			   if(robj.qrWC == querySize){
	        				   
	        				   //Check
	        					Iterator treIterator = robj.posSet.iterator();
	        					int prev = 0;
	        					boolean flag = false;
	        					while(treIterator.hasNext()){
	        						int curr = Integer.parseInt(String.valueOf(treIterator.next()));
	        						if(prev>0){
	        							if(prev == curr-1){
	        								robj.setPos_score(1);
	        								flag = true;
	        								break;
	        							}
	        						}
	    							prev = curr;
	        					}
	        					if(!flag)
	             				   robj.posSet.clear();
	        			   }
	        			   else
	        				   robj.posSet.clear();
	        			   
	        		   }else if (querySize==1) robj.setPos_score(1);
	        		   
	        		   AndOrTempMap.put(docid, robj);
		        	   	        	   
		           }
		       } 
		   }

	       Iterator<String> itrMap = AndOrTempMap.keySet().iterator();
	       while(itrMap.hasNext()){
	    	   
	    	   String keyTemp = (String)itrMap.next();//System.out.println(keyTemp);
	    	   RankObject robjTemp = AndOrTempMap.get(keyTemp);
	    	  
	    	   double cos = robjTemp.cos_score/(robjTemp.mag_doc*mag_query);
	    	   
	    	   if(AndOrTempMap.containsKey(keyTemp)){
	    		   if(cos>=0.5){
	    		  cos = cos + (0.5)*robjTemp.getPos_score()+robjTemp.getTitle_score();
	    		  if(querySize>1){
	    			  cos+= (robjTemp.qrWC==querySize)?0.5:0;
	    		  }
	    	   }
	    		   
	    	   }
	    	   robjTemp.page_rank = cos;
	    	   AndOrTempMap.put(keyTemp, robjTemp);
	    
	       }

		   if(AndOrTempMap.size()==0){
			   
			   System.out.println("Not found\n");
			   return new ArrayList<Result>();
			   
		   }
		
		   ArrayList<Result> urls = new ArrayList<Result>();	   	
		   	if(AndOrTempMap.size()>0){

		   	List<Entry<String, RankObject>> AndOrList = new ArrayList<Entry<String, RankObject>>(AndOrTempMap.entrySet());
		 // System.out.println(AndOrTempMap.get(277).page_rank);
		   	AndOrList = fnSort(AndOrTempMap);
		//  for(int i=0;i<AndOrList.size();i++)
		//System.out.println(AndOrList.get(i).getValue().getTitle());
			int size = AndOrList.size();
			if(size>100)size=100;
						
	//System.out.println(size);
					
			for(int i=0;i<size;i++){
				Map.Entry<String, RankObject> mapEntry = AndOrList.get(i);
//				System.out.println(mapEntry.getValue());
				 BasicDBObject queryUrl = new BasicDBObject();
				
				 queryUrl.put("DOC ID:",Integer.valueOf(mapEntry.getKey()));
				 DBCursor cursorUrl = dbCollection.find(queryUrl);
	           
				 if (cursorUrl.hasNext()) { 
					 
					DBObject objUrl = (DBObject) cursorUrl.next();
	                      Result r= new Result();
					
					r.setName((String) objUrl.get("TITLE :"));
					String url = (String) objUrl.get("URL :");
					String text = (String) objUrl.get("TEXT :");
					r.setCosine((Double)mapEntry.getValue().getPage_rank());
					r.setUrl(url);
					r.setSnippet(Snippet.generate_snippet(search,text));
					//System.out.println(r.getName());
					if(removeDuplicates(urls, r))
					urls.add(r);
		           		
				 }
	          		
	           }
			}
			
			
			return  urls.subList(0, 5);
			
		       
	  }
	  public static boolean removeDuplicates(ArrayList<Result> urls,Result Object){
		  List<Result> listCustomer = new ArrayList<Result>();    
		  for (Result customer: urls)
		  {
			  if(!Object.getName().isEmpty()){
				  if(!Object.getName().equals(customer.getName())){
					  continue;
				  } else return false; 
				  }
				  else return false;
		    
		   }
		  return true;
	  }
	public static List<Entry<String, RankObject>> fnSort(Map<String,RankObject> mapToSort){
		   
			List<Entry<String, RankObject>> tokenPairList = new ArrayList<Entry<String, RankObject>>(mapToSort.entrySet());
			Collections.sort( tokenPairList, new Comparator<Map.Entry<String, RankObject>>()
			{
				public int compare( Map.Entry<String, RankObject> mapEntry1, Map.Entry<String, RankObject> mapEntry2 )
				{
				    Double value2 = Double.valueOf(mapEntry2.getValue().page_rank);    
				    Double value1 = Double.valueOf(mapEntry1.getValue().page_rank);
				    
					return (value2).compareTo( value1 );
				}
			
			} );

			return tokenPairList;
		   
	   }
	
}
