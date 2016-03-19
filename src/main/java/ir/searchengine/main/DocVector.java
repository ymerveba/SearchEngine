/*
Anup Rokkam Pratap: 80529276
Yuvaraj Merve Basavaraj: 91605284
*/

package ir.searchengine.main;

import java.util.ArrayList;
import java.util.TreeMap;

public class DocVector {
	
	String query_token = new String();
	Float tf_idf ;
	public String getQuery_token() {
		return query_token;
	}
	public void setQuery_token(String query_token) {
		this.query_token = query_token;
	}
	public Float getTf_idf() {
		return tf_idf;
	}
	public void setTf_idf(Float tf_idf) {
		this.tf_idf = tf_idf;
	}
	
	public DocVector(String query_token, Float tf_idf) {
		this.query_token = query_token;
		this.tf_idf = tf_idf;
	}
	
	
	
}
