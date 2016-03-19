
package ir.searchengine.main;

public class Doc_Score implements Comparable<Doc_Score>{
	int doc_ID;
	double cosine_score;
	String title;
	Boolean presentInTitle;
	String DistanceBetweenPosition;
	public int getDoc_ID() {
		return doc_ID;
	}
	public Boolean getPresentInTitle() {
		return presentInTitle;
	}
	public void setPresentInTitle(Boolean presentInTitle) {
		this.presentInTitle = presentInTitle;
	}
	public void setDoc_ID(String doc_ID) {
		this.doc_ID = Integer.parseInt(doc_ID);
	}
	public double getCosine_score() {
		return cosine_score;
	}
	public void setCosine_score(double cosine_score) {
		this.cosine_score = cosine_score;
	}
	public Doc_Score(int doc_ID, double cosine_score) {
		super();
		this.doc_ID =  doc_ID;
		this.cosine_score = cosine_score;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPosition() {
		return DistanceBetweenPosition;
	}
	public void setPosition(String position) {
		this.DistanceBetweenPosition = position;
	}
	public int compareTo(Doc_Score o) {
		//return Double.compare(o.getCosine_score(), this.cosine_score);
		return Double.compare( this.cosine_score,o.getCosine_score());
		/*if(this.cosine_score<o.getCosine_score())
	          return 1;
	    else if(o.getCosine_score()<this.cosine_score)
	          return -1;
	    return 0;*/
	}
	
	
	
	
}
