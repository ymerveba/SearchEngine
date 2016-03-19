package ir.searchengine.main;

import java.util.ArrayList;
import java.util.List;

public class DocumentDetails  {
 double TF_IDFScore;
 List<Integer> position= new ArrayList<Integer>();
 String title;
 int doc_id;

public int getDoc_id() {
	return doc_id;
}
public void setDoc_id(int doc_id) {
	this.doc_id = doc_id;
}
Boolean WordPresentInTitle =false;
public String getTitle() {
	return title;
}
public Boolean getWordPresentInTitle() {
	return WordPresentInTitle;
}
public void setWordPresentInTitle(Boolean wordPresentInTitle) {
	WordPresentInTitle = wordPresentInTitle;
}
public void setTitle(String title) {
	this.title = title;
}
public double getTF_IDFScore() {
	return TF_IDFScore;
}
public void setTF_IDFScore(double iDFScore) {
	TF_IDFScore = iDFScore;
}
public List<Integer> getPosition() {
	return position;
}
public void setPosition(int position) {
	this.position.add(position);
}
public void setPosition(List<Integer> position) {
	this.position=(position);
}
 
}
