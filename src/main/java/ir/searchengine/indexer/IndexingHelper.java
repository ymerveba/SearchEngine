package ir.searchengine.indexer;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import ir.searchengine.dao.Utilities;

public class IndexingHelper {
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
	
	static float total_documents = 0;
	  public static TreeMap<String, TreeMap<Integer, posObj>> createDocList(){
		  BasicDBObject basicDBObject = new BasicDBObject();
			DBCursor dbcursor = dbCollection.find(basicDBObject);
			
			ArrayList<String> tokens;
			TreeMap<String,TreeMap<Integer, posObj>> indexmap = new TreeMap<String, TreeMap<Integer,posObj>>();
            while (dbcursor.hasNext()){
            
				BasicDBObject obj = (BasicDBObject) dbcursor.next();
				String title=obj.getString("TITLE :");
				int doc_id=Integer.parseInt(obj.getString("DOC ID:"));
				tokens = Utilities.tokenizeFile(obj.getString("TEXT :")+title);
				int docLength = tokens.size();
				int position = 0;
				for(String s : tokens){
					position++;
					if(s.length()>1&&!s.equals(" ")){
					if(!indexmap.containsKey(s)){
						indexmap.put(s, new TreeMap<Integer, posObj>());
						posObj d= new posObj();
						d.incFreq();
						d.add(position);
						d.setTitle(title);
						d.setDocID(doc_id);
						Double term_freq= (double)d.getFreq()/docLength;
						d.setTf((term_freq));
						//d.setTf(1+Math.log10(term_freq));
						
						indexmap.get(s).put(doc_id,d);
						d=null;
					}else{
						if(!indexmap.get(s).containsKey(doc_id)){
							posObj d= new posObj();
							d.incFreq();
							d.add(position);
							d.setTitle(title);
							d.setDocID(doc_id);
							Double term_freq= (double)d.getFreq()/docLength;
							d.setTf((term_freq));
							//d.setTf(1+Math.log10(term_freq));
							indexmap.get(s).put(doc_id,d);
							d=null;
						}else{
							posObj d= indexmap.get(s).get(doc_id);
							d.incFreq();
							d.add(position);
							d.setTitle(title);
							d.setDocID(doc_id);
							Double term_freq= (double)d.getFreq()/docLength;
							d.setTf((term_freq));
							//d.setTf(1+Math.log10(term_freq));
							
							indexmap.get(s).put(doc_id,d);
							d=null;
						}
					}
				} 
					}
				
				total_documents++;//System.out.println('s'+indexmap.get("computer").get(doc_id).getTf());
				obj=null;
            	
            	tokens.clear();
            	obj=null;
            }
            return indexmap;
	}	
         	  
	  public static Map<String, Float> createInvertedIndexMAP(Map<String, TreeMap<Integer, posObj>> wordMap) {
		  TreeMap<String, Float> idfScoreMap= new TreeMap<String,Float>();
			//idf calculation
		  float docs_contain_term,log_idf;
		for(String s : wordMap.keySet()){
			     docs_contain_term = wordMap.get(s).size();
				 log_idf = (float) Math.log10((float)total_documents/docs_contain_term);
				
                idfScoreMap.put(s, log_idf);
               // if(s.equals("computer")) System.out.println(log_idf+""+total_documents+""+wordMap.get(s).size());
		
		}
			return idfScoreMap;
		}
		

			public static void insertToMongo(Map<String, TreeMap<Integer, posObj>> wordMap,
					Map<String, Float> invertedIndexMap) {
				BasicDBObject indexObj;
					for (String str : wordMap.keySet()) {
					
					
					 indexObj = new BasicDBObject();
					indexObj.put("Token:", str);
					List<BasicDBObject> docList= new ArrayList<BasicDBObject>();
					for(int doc_id:wordMap.get(str).keySet()){
						BasicDBObject documentDetail = new BasicDBObject();
						posObj doc = wordMap.get(str).get(doc_id);
						documentDetail.put("DocID:", doc_id);
						documentDetail.put("Title:", doc.getTitle());
						documentDetail.put("Position:", doc.getPos());
						documentDetail.put("TF-IDF:", doc.getTf());
						doc=null;
					    docList.add(documentDetail);
					    documentDetail=null;
					}
					indexObj.put("DOC:", docList);
					docList=null;
					docMap.insert(indexObj);
					indexObj=null;
					
				}
					BasicDBObject dbobj;
				for (String str : invertedIndexMap.keySet()) {
					
					
					 dbobj = new BasicDBObject();
					dbobj.put("Token:", str);
					dbobj.put("IDF:", invertedIndexMap.get(str));
					InvertedIndex.insert(dbobj);
					dbobj=null;
					
					
				}
				

				
				
			}

			public static Map<String, TreeMap<Integer, posObj>> generateTFIDF(
					Map<String, TreeMap<Integer, posObj>> wordMap, Map<String, Float> invertedIndexMap) {
			
			for(String s : wordMap.keySet()){
				
				for(int i : wordMap.get(s).keySet()){
					double log_idf = invertedIndexMap.get(s);
					posObj d = wordMap.get(s).get(i);
					double tf = d.getTf();
					d.setTf(log_idf*tf);
					wordMap.get(s).put(i,d);
					d=null;
					
				}
			}
			return wordMap;
			}


			

}
