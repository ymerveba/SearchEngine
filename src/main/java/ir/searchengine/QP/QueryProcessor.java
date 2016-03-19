package ir.searchengine.QP;



import java.util.ArrayList;
import java.util.TreeMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class QueryProcessor {
	
	String Query;
	static ArrayList<String> querytokens_list = new ArrayList<String>();
	

	public String getQuery() {
		return Query;
	}
	
	public static ArrayList<String> returnquery(){
		
		return querytokens_list;
	}


	private QueryProcessor(String Query) {
		this.Query = Query;
	}
	
	//returns query tokens and the corresponding tf-idf value
	public static ArrayList<Query> Qprocessor(String Query){
		MongoClient	mongoClient = null;
		QueryProcessor myquery = new QueryProcessor(Query);
		TreeMap<String, Integer> querymap = new TreeMap<String, Integer>();
		querytokens_list = Utilities.tokenizeQuery(myquery.getQuery()); //tokenize query
		double query_size = querytokens_list.size(); 
		for( int i=0; i<query_size; i++){
			if(querymap.containsKey(querytokens_list.get(i))){
				querymap.put(querytokens_list.get(i), querymap.get(querytokens_list.get(i))+1);
			}else{
					querymap.put(querytokens_list.get(i),1);
			}
		}
		ArrayList<Query> query = new ArrayList<Query>();
		
		try{
			mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB("db01");
			DBCollection dbCollection = db.getCollection("InvIndex");
		for(String s : querymap.keySet()){
			BasicDBObject basicDBObject = new BasicDBObject();
			basicDBObject.put("Token:",s);
			DBCursor dbcursor = dbCollection.find(basicDBObject);
		
			double idf;
			try{
				idf = Double.parseDouble(dbcursor.next().get("IDF:").toString())*querymap.get(s);
			}catch(Exception e){
				continue;
			}
			 double query_Norm_idf = idf/query_size; //normalized query tf_idf
			
			 
			 query.add(new Query(s, query_Norm_idf));	
		}
	
		return query;
		}
		finally{
			mongoClient.close();
		}
	}
		
}
