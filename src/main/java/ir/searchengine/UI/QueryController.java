

package ir.searchengine.UI;

import java.io.IOException;
import java.util.List;

import ir.searchengine.dao.SearchEngineHelper;
import ir.searchengine.main.Result;
import ir.searchengine.main.Search;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class QueryController {

	@RequestMapping(value="/search")
	private String displaySearch(HttpServletRequest request, final Model model) throws ClassNotFoundException, IOException{
		//Search.SearchEngine(request.getParameter("query"));
		try{
			List<Result> SearchResults = SearchEngineHelper.fnSearch((String)request.getParameter("query"));
			model.addAttribute("searchresult",SearchResults);
			if(SearchResults.isEmpty()) System.out.println("Word Not found");
			
		else 
		for(Result s : SearchResults){
			System.out.println(s.getName());
		}}catch(Exception e){
			System.out.println("Something went wront"+e);
		}
		
		//System.out.println("asdsd");
		model.addAttribute("query", request.getParameter("query"));
		return("search");
	}
}
