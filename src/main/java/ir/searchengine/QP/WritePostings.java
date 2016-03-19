package ir.searchengine.QP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.TreeMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class WritePostings {

	public static void read() throws IOException, ClassNotFoundException{
		File file = new File("C:\\Personal Projects\\Proj2-2016-02-08\\ir_index.txt");
	    FileInputStream f = new FileInputStream(file);
	    ObjectInputStream s = new ObjectInputStream(f);
	    TreeMap<String, TreeMap<Integer,Float>> indexMap = (TreeMap<String, TreeMap<Integer,Float>>) s.readObject();
	    s.close();
		MongoClient mongoClient = null;

		try {
			mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB("db01");
			DBCollection dbCollection = db.getCollection("myIR_INDEX");

			for (String str : indexMap.keySet()) {
				
				
				BasicDBObject dbobj = new BasicDBObject();
				dbobj.put("Token", str);
				
				TreeMap<String,Float> a= new TreeMap<String,Float>();
				for(int doc_id:indexMap.get(str).keySet()){
				a.put(String.valueOf(doc_id),indexMap.get(str).get(doc_id));
				}
				dbobj.put("POSTING", new BasicDBObject(a));
				dbCollection.insert(dbobj);
				
			}

			

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mongoClient != null) {
				mongoClient.close();
			}
		}
	}
	
	public static void main(String[]args){
		try {
			read();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
