
/*
Anup Rokkam Pratap: 80529276
Yuvaraj Merve Basavaraj: 91605284
*/

package ir.searchengine.main;

public class Result {
	
	String url;
	String name;
	String snippet;
	Boolean TitlehasWord =false;
	Double cosine_similarity;
	Boolean presentInConsecutive = false;
	public Double getCosine() {
		return cosine_similarity;
	}
	public void setCosine(Double double1) {
		cosine_similarity = double1;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	
	@Override
	public String toString() {
		return "Result [url=" + url + ", name=" + name + ", snippet=" + snippet
				+ "]";
	}
	
	
}
