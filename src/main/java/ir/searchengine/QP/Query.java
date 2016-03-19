
/*
Anup Rokkam Pratap: 80529276
Yuvaraj Merve Basavaraj: 91605284
*/


package ir.searchengine.QP;


public class Query {

	String query_token;
	Double tf_idf;
	
	public Query(String query_token, Double tf_idf){
		this.query_token = query_token;
		this.tf_idf = tf_idf;
	}
	
	public String getQuery_token() {
		return query_token;
	}
	public void setQuery_token(String query_token) {
		this.query_token = query_token;
	}
	public Double getTf_idf() {
		return tf_idf;
	}
	public void setTf_idf(Double tf_idf) {
		this.tf_idf = tf_idf;
	}
	
}
