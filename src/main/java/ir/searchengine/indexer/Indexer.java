package ir.searchengine.indexer;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Indexer {
	public static List<String> EnglishStopWords=new ArrayList<String>();
	public static void main(String[] args) {
		try {
			String[] englishStopWords = { "a", "about", "above", "after", "again", "against", "all", "am", "an", "and",
					"any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between",
					"both", "but", "by", "can't", "cannot", "could", "could", "couldn't", "did", "didn't", "do", "does",
					"doesn't", "doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't",
					"has", "hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's",
					"hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if",
					"in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't",
					"mymyself", "no", "", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our",
					"ours", "	ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's",
					"should", "shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them",
					"themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've",
					"this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd",
					"we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's",
					"which", "while	", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you",
					"you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves" };
                EnglishStopWords = Arrays.asList(englishStopWords);
			    IndexingHelper.MongoDB();
				Map<String, TreeMap<Integer, posObj>> wordMap = IndexingHelper.createDocList();
				//System.out.println(wordMap.get("computer"));
				Map<String, Float> invertedIndexMap = IndexingHelper.createInvertedIndexMAP(wordMap);
				wordMap = IndexingHelper.generateTFIDF(wordMap,invertedIndexMap);
			    System.out.println("Total Documents: "+IndexingHelper.total_documents);
			   /* for(TreeMap<Integer, posObj> str:wordMap.values())
			    	for (posObj  p:str.values())
			    System.out.println("Total Words: "+p.getTf());
			*/
			  IndexingHelper.insertToMongo(wordMap,invertedIndexMap);
			
			
			
			
			
			
			
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
