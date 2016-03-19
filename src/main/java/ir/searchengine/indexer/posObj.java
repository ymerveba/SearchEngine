package ir.searchengine.indexer;

import java.util.ArrayList;
import java.util.List;

public class posObj{
		 private int docID ;
		 private String title;
		 private int freq =0; // frequency of the word per document
		 private List<Integer> position = new ArrayList<Integer>();
		 private double tf;
		 
		 public double getTf() {
			return tf;
		}

		public void setTf(double tf) {
			this.tf = tf;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setDocID(int docID){
			 this.docID = docID;
		 }
		 
		 public int getDocID(){
			 return this.docID;
		 }

		 public void incFreq(){
			 this.freq++;
		 }
		 
		 public void add(int num){
			 this.position.add(num);
		 }
		 
		 public List<Integer> getPos(){
			 return this.position;
		 }
		 
		 public int getFreq(){
			 return this.freq;
		 }

		@Override
		public String toString() {
			return "posObj [docID=" + docID + ", title=" + title + ", freq=" + freq + ", position=" + position + ", tf="
					+ tf + "]";
		}
	}