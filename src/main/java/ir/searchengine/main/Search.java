package ir.searchengine.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import ir.searchengine.QP.Query;
import ir.searchengine.QP.QueryProcessor;
import ir.searchengine.QP.Utilities;

public class Search {
	
	//ArrayList<Score> final_Score = new ArrayList<Score>();
	
	@SuppressWarnings("unused")
	private static ArrayList<String> tokenizeQuery(String query){
		ArrayList<String> query_tokens = Utilities.tokenizeQuery(query);
		return query_tokens;
	}
	
	/*
	method to reterive the DOCID AND TF-IDF FOR A QUERY TOKEN	
	Takes in only a token as i/p parameter
	*/
private static TreeMap<Integer, Double> RankedObject= new TreeMap<Integer, Double>();
	private static TreeMap<Integer, DocumentDetails> retrive_posting(String query_token) throws IOException, ClassNotFoundException{
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB("db01");
		DBCollection dbCollection = db.getCollection("docMap");
		//DBObject object = dbCollection.findOne();
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("Token:",query_token);
		DBCursor dbcursor = dbCollection.find(basicDBObject);
	    //byte[] javaObjectByte = (byte[]) object.get("posting");
		@SuppressWarnings("unchecked")
		List<BasicDBObject> posting= (List<BasicDBObject>) dbcursor.next().get("DOC:");
		 TreeMap<Integer, DocumentDetails> tempDOCMAP= new TreeMap<Integer, DocumentDetails>();
		 TreeMap<Integer, DocumentDetails> realDOCMAP= new TreeMap<Integer, DocumentDetails>();
		 TreeMap<String, Integer> title_ID= new TreeMap<String, Integer>();
		 for(BasicDBObject ob:posting){
			 DocumentDetails d= new DocumentDetails();
        	 	int doc_id=(Integer)ob.get("DocID:");
				String title = (String)ob.get("Title:");
				
				@SuppressWarnings("unchecked")
				List<Integer> position = (List<Integer>)ob.get("Position:");
				double tf_idf= (Double)ob.get("TF-IDF:");
				d.setDoc_id(doc_id);
				d.setTF_IDFScore(tf_idf);
                d.setPosition(position);
				d.setTitle(title);
				if(null!=title&&!title.isEmpty()&&title.contains(search.toString())){
					d.setWordPresentInTitle(true);RankedObject.put(doc_id,2.0);}
				else 
					if(null!=title&&title.isEmpty()&&title.contains(query_token)) {
						double cosine=1.0;
						if(RankedObject.containsKey(doc_id))
						 cosine = RankedObject.get(doc_id)* 1.5/search.length;
						RankedObject.put(doc_id,cosine);
						}
					
				tempDOCMAP.put(doc_id, d);
				if(null!=title&&!title.isEmpty()){
				title_ID.put(title, doc_id);
				}
        	 }
	   for(int docID: title_ID.values()){
		   realDOCMAP.put(docID,tempDOCMAP.get(docID));
	   }
	   
	   
	    mongoClient.close();
	    return realDOCMAP;
	}
	
	
	private static ArrayList<Doc_Score> score_calc(ArrayList<Query> query) throws ClassNotFoundException, IOException{
		TreeMap<Integer,TreeMap<String, DocumentDetails>> details = new TreeMap<Integer, TreeMap<String,DocumentDetails>>();
		TreeMap<String,TreeMap<Integer, DocumentDetails>> queryWithDocument = new TreeMap<String, TreeMap<Integer,DocumentDetails>>();
		for(Query q: query){
			String token = q.getQuery_token();
			TreeMap<Integer, DocumentDetails> token_posting = retrive_posting(token); //for every query token reterive its posting list
			queryWithDocument.put(token, token_posting);
			for(int i : token_posting.keySet()){
				if(!details.containsKey(i)){
					details.put(i, new TreeMap<String, DocumentDetails>());
				}
					details.get(i).put(token,token_posting.get(i));
				
			}
		}
	
		ArrayList<Doc_Score> doc_scores =  Cosine_Similarity(details, query,queryWithDocument);
		Collections.sort(doc_scores);
		//return the top5 results
		ArrayList<Doc_Score> top_five = new ArrayList<Doc_Score>();
		int size = doc_scores.size();
		int limit = 0;
		if(size<5){
			limit = size;
		}else{
			limit = 5;
		}
		for(int i = 0; i<limit ; i++){
			top_five.add(doc_scores.get(i));
			System.out.println(doc_scores.get(i).getCosine_score());
		}
		return top_five;
	
	}
	
	private static ArrayList<Doc_Score> Cosine_Similarity(TreeMap<Integer,TreeMap<String, DocumentDetails>> details, ArrayList<Query>query,TreeMap<String,TreeMap<Integer, DocumentDetails>> queryWithDOc){
		ArrayList<Doc_Score> doc_scores = new ArrayList<Doc_Score>();
		double query_magnitude = 0;
		//calculate magnitude for query
		for(Query qu : query){
			query_magnitude += Math.pow(qu.getTf_idf(),2);
		}
		query_magnitude = Math.sqrt(query_magnitude);
		
		for(Integer i : details.keySet()){
			double doc_score_numerator= 0;
			double doc_magnitude = 0;
			
			for(Query q : query){
				if(details.get(i).containsKey(q.getQuery_token())){
					
					doc_score_numerator += details.get(i).get(q.getQuery_token()).getTF_IDFScore()* (Double) q.getTf_idf();
					doc_magnitude += Math.pow(details.get(i).get(q.getQuery_token()).getTF_IDFScore(), 2); //calculate magnitude for doc
				}
			}
			double cosine_similarity = 0;
			doc_magnitude = Math.sqrt(doc_magnitude);
			if(query_magnitude != 0.0 || doc_magnitude != 0.0){
				cosine_similarity = doc_score_numerator/(query_magnitude*doc_magnitude);
			}
		//if(RankedObject.containsKey(i))
       // cosine_similarity = cosine_similarity+ RankedObject.get(i);
		Doc_Score d= new Doc_Score(i, cosine_similarity);
		
			doc_scores.add(d);
		}
		return doc_scores;
	}
	
	
	public static ArrayList<Result> SearchEngine(String query) throws ClassNotFoundException, IOException{
		//String query = "irvine 1information";
		search= query.split(" ");
		ArrayList<Query> query_tokens = new ArrayList<Query>();
		query_tokens = QueryProcessor.Qprocessor(query); //query tokens and the corresponding tf-idf value (no duplicates)
		ArrayList<Doc_Score> documents = score_calc(query_tokens);
		return retrive_URL(documents, query);
	}

	private static ArrayList<Result> retrive_URL(ArrayList<Doc_Score> documents, String query) {
		ArrayList<Result> urls = new ArrayList<Result>();
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB("db01");
		DBCollection dbCollection = db.getCollection("myIR_Collection7");
		for(Doc_Score d: documents){
		BasicDBObject basicDBObject = new BasicDBObject();
		basicDBObject.put("DOC ID:",d.getDoc_ID());
		DBCursor dbcursor = dbCollection.find(basicDBObject);
		BasicDBObject obj = (BasicDBObject) dbcursor.next();
		
		Result r =new Result();
		r.setName((String) obj.get("TITLE :"));
		String url = (String) obj.get("URL :");
		String text = (String) obj.get("TEXT :");
		System.out.println(r.getName());
		r.setUrl(url);
		r.setCosine(d.getCosine_score());
		r.setSnippet(Snippet.generate_snippet(query,text));
		urls.add(r);
		}
		
		mongoClient.close();
		return urls;
		
	}
	@SuppressWarnings("unused")
	private static String[] search;
	public static void main(String [] args) throws ClassNotFoundException, IOException{
		//my_query = "family";
		ArrayList<Result> result = SearchEngine("mondego");
		for(Result s : result){
			//System.out.println(s.getName());
			
			System.out.println(s.getName()+s.getCosine());
			//System.out.println(s.getSnippet() + "...");
			//System.out.println(" ==== ");
		}
	}
	
	
}
