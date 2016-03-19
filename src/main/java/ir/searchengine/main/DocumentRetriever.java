/*
Anup Rokkam Pratap: 80529276
Yuvaraj Merve Basavaraj: 91605284
*/
package ir.searchengine.main;

import ir.searchengine.QP.QueryProcessor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

public class DocumentRetriever {

	static ArrayList<String> query = new ArrayList<String>();
	
	public static void getQuery(){
		query = QueryProcessor.returnquery();
	}
	
	//Takes in only a token as i/p parameter
	public static TreeMap<Integer, Float> retriveDocId_tfidf(String query_token) throws IOException, ClassNotFoundException{
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		DB db = mongoClient.getDB("myIR_D10");
		DBCollection dbCollection = db.getCollection("myIR_index");
		//DBObject object = dbCollection.findOne();
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("Token",query_token);
		DBCursor dbcursor = dbCollection.find(basicDBObject);
	    //byte[] javaObjectByte = (byte[]) object.get("posting");
		byte[] javaObjectByte = (byte[]) dbcursor.next().get("posting");
	    InputStream inputStream = new ByteArrayInputStream(javaObjectByte);
	    ObjectInputStream in = new ObjectInputStream(inputStream);
	    TreeMap<Integer, Float> query_posting = (TreeMap<Integer, Float>)in.readObject();
	    in.close();
	    inputStream.close();
	    mongoClient.close();
	    return query_posting;
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException{
		String token ="irvine";
		System.out.println(retriveDocId_tfidf(token));
		
	}
	
	
	
	
}
