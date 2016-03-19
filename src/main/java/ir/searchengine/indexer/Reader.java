package ir.searchengine.indexer;

import java.net.UnknownHostException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class Reader {
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
	public static void main(String[] args) throws UnknownHostException {
		MongoDB();
	
		 BasicDBObject query = new BasicDBObject();
	      
	       DBCursor cursorWORDMAP = docMap.find(query);
	       
	
		 while (cursorWORDMAP.hasNext()) { 
			 BasicDBObject obj = (BasicDBObject) cursorWORDMAP.next();	
	
		List<BasicDBObject> temp = (List<BasicDBObject>) obj.get("DOC:");
		 for(BasicDBObject ob:temp){
        	 
        	 int doc_id=(Integer)ob.get("DocID:");
				ob.get("Title:");
				ob.get("Position:");
				ob.get("TF-IDF:");
       if((Double)ob.get("TF-IDF:")>0)
        	System.out.println(ob.get("TF-IDF:"));
         }

	}

	}}
