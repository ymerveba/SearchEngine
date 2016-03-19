package ir.searchengine.dao;

import java.util.Set;
import java.util.TreeSet;

public class RankObject{
	
	int qrWC;
	double tfidf_score;
	Set<Integer> posSet;
	double cos_score;
	double mag_doc;
	
	double page_rank;
	private double pos_score;
	private double title_score;
	private String title;
	public double getPos_score() {
		return pos_score;
	}

	public void setPos_score(double pos_score) {
		this.pos_score = pos_score;
	}

	public double getTitle_score() {
		return title_score;
	}

	public void setTitle_score(double title_score) {
		this.title_score = title_score;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	RankObject(){
		this.qrWC = 0;
		this.tfidf_score = 0.0;
		this.posSet = new TreeSet<Integer>();
		this.cos_score = 0.0;
		this.mag_doc = 0.0;
		this.pos_score = 0.0;
		this.title_score = 0.0;
		this.page_rank = 0.0;
	}

	public int getQrWC() {
		return qrWC;
	}

	public void setQrWC(int qrWC) {
		this.qrWC = qrWC;
	}

	public double getTfidf_score() {
		return tfidf_score;
	}

	public void setTfidf_score(double tfidf_score) {
		this.tfidf_score = tfidf_score;
	}

	public Set<Integer> getPosSet() {
		return posSet;
	}

	public void setPosSet(Set<Integer> posSet) {
		this.posSet = posSet;
	}

	public double getCos_score() {
		return cos_score;
	}

	public void setCos_score(double cos_score) {
		this.cos_score = cos_score;
	}

	public double getMag_doc() {
		return mag_doc;
	}

	public void setMag_doc(double mag_doc) {
		this.mag_doc = mag_doc;
	}

	public double getPage_rank() {
		return page_rank;
	}

	public void setPage_rank(double page_rank) {
		this.page_rank = page_rank;
	}
	
}