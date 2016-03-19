package ir.searchengine.main;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GoogleSearch {
	public static void main(String[] args) throws Exception {
		String google = "http://www.google.com/search?q=";
		String search = "mondego site:ics.uci.edu";
		String charset = "UTF-8";
		String userAgent = "yuvaraj";

		Elements links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select(".g>.r>a");
int count=0;
		for (Element link : links) {
			if(count>4)break;
		    String title = link.text();
		    String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
		    url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

		    if (!url.startsWith("http")) {
		        continue; // Ads/news/etc.
		    }

		    System.out.println(++count+")"+"Title: " + title);
		    System.out.println("URL: " + url);
		}
	    }
	
}
