
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

public class IDF_gen {
	public static void idf_calc() throws IOException, ClassNotFoundException {
		File file = new File("C:\\Personal Projects\\Proj2-2016-02-08\\ir_idf.txt");
		
		FileInputStream f = new FileInputStream(file);
		ObjectInputStream s = new ObjectInputStream(f);
		TreeMap<String, Float> idfmap = (TreeMap<String, Float>)s.readObject();
		s.close();
		MongoClient mongoClient = null;

		try {
			mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB("db01");
			DBCollection dbCollection = db.getCollection("myIR_IDF");

			for (String str : idfmap.keySet()) {
				
				
				BasicDBObject dbobj = new BasicDBObject();
				dbobj.put("Token", str);
				dbobj.put("IDF", idfmap.get(str));
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

	public static void main(String[] args) {
		try {
			idf_calc();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}