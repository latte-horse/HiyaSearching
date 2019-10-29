package lab.latte.issue.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import lab.latte.issue.model.EmployeeVO;
import lab.latte.issue.service.IHrService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Resource(name = "hrService")
	private IHrService hrService;
	
	@Resource(name="envProperties")
	private Properties env;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {

		return "home";
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(Locale locale, Model model) {
		
		List<EmployeeVO> listEmployee = hrService.getEmployeesAll();
		model.addAttribute("employees", listEmployee);
		
		return "test";
	}
	
	@RequestMapping(value = "/3d-test", method = RequestMethod.GET)
	public String test3d(Locale locale, Model model) {
		
		return "3d-test";
	}
	
	@RequestMapping(value = "/main-test", method = RequestMethod.GET)
	public String mainTest(Locale locale, Model model) {
		
		return "main-test";
	}

	
	
	@Resource(name = "restTemplate")
	protected RestTemplate restTemplate;
	
	@RequestMapping(value = "api/searching" , method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> searchNaver (String[] main , String keyword , String nowTime  ,
			Model model ) throws  IOException {//traindition = ture  諛곗뿴 諛쏄린
		String clientId = "lXA5GRw7Os5t_Hs1sF28";//�븷�뵆由ъ��씠�뀡 �겢�씪�씠�뼵�듃 �븘�씠�뵒媛�";
        String clientSecret = "8DC2rlIJdi";//�븷�뵆由ъ��씠�뀡 �겢�씪�씠�뼵�듃 �떆�겕由욧컪";
        
        Map<String, Object> resultMap = new HashMap<String, Object>();
//		List<NaverApiVO> list = null;
//		String text = URLEncoder.encode(main[0] , "UTF-8");
//
//    	try {
//    	String apiURL = "https://openapi.naver.com/v1/search/news.json?query="
//    			+ main[0] + "+"
//    			+ main[1] + "+"
//    			+ main[2]
//    			+ "&display=10&start=1&sort=sim";
//    	System.out.print(main[0] + main[1] + main[2]);
//    	URL url = new URL(apiURL);
//    	HttpURLConnection con = (HttpURLConnection)url.openConnection(); 
//    	
//    	con.setRequestMethod("GET");
//    	con.setRequestProperty("X-Naver-Client-Id", clientId);
//    	con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
//    	
//    	BufferedReader br;
//    	br = new BufferedReader(new InputStreamReader(con.getInputStream() , "UTF-8"));
//    	
//    	String inputLine;
//    	StringBuffer responsere = new StringBuffer();
//    	while((inputLine = br.readLine()) != null) {
//    		responsere.append(inputLine);
//    	}
//    	
//    	br.close();
//    	
//    	System.out.println(responsere.toString());
//    	
//    	
//    	
//    	}catch(Exception e) {
//    		e.printStackTrace();
//    	}
		try {
		String apiURL = "https://openapi.naver.com/v1/search/news.json?query="
    			+ main[0] + "+"
    			+ main[1] + "+"
    			+ main[2]
    			+ "&display=10&start=1&sort=sim";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String,String> keyvalue = new HashMap<String, String>();
		keyvalue.put("X-Naver-Client-Id" , clientId);
		keyvalue.put( "X-Naver-Client-Secret", clientSecret);
		
		headers.setAll(keyvalue);
		
		HttpEntity entity = new HttpEntity("parameters" , headers);
		ResponseEntity response = restTemplate.exchange(apiURL,  HttpMethod.GET , entity , String.class);
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(response.getBody().toString());
		JSONArray docuArray = (JSONArray)jsonObject.get("items");
		List originallink = new ArrayList();
		List description = new ArrayList();
		List title = new ArrayList();
		
		for(int i = 0 ; i <docuArray.size() ; i++) {
			JSONObject tmp = (JSONObject)docuArray.get(i);
			
			title.add((String)tmp.get("title"));
			
			
			originallink.add((String)tmp.get("originallink"));
			
			description.add((String)tmp.get("description"));
			
		}
		
		
		
		
		
//		JSONObject docuObject = (JSONObject)docuArray.get(0);
//		model.addAttribute("originallink" , originallink);
//		model.addAttribute("description" , description);
//		model.addAttribute("title" , title);
//		
		
		resultMap.put("description", description);
		resultMap.put("title", title);
		resultMap.put("originallink", originallink);
		
		
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
		
    	return resultMap;
       
//		URL url = new URL("https://search.daum.net/search?DA=STC&cluster=y&ed="
//    			+ nowTime + "235959"
//				+ "&https_on=on&nil_suggest=btn&period=u&q="
//				//+ node['word'] + "+" + num1node
//				+ keyword + "+" + main[0] + "+" + main[1] + "+" + main[2] + "+" + main[3]
//		    	+ "&sd="
//		    	+ nowTime + "000000"
//		    	+ "&w=news");
//  	 	
//        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
//        String line;
//        int checkline = 0;
//        while((line = reader.readLine()) != null) {
//        	if(line.contains(s))
//        }
        
        
	}
	
	
	@RequestMapping(value = "/j-Test" , method = RequestMethod.GET)
	public String jtest(String modelid) {
		
		return "j-Test";
	}
}
