package lab.latte.issue.service;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.stereotype.Service;

import lab.latte.issue.model.NaverApiVO;

@Service
public interface NaverApiService {

 
    public List<NaverApiVO> searchNaver(String[] keyword);
  /*  	List<NaverApiVO> list = null;
    	URL url; 
    	url = new URL( "https://openapi.naver.com/v1/search/news.xml?query="
    			+ URLEncoder.encode(keyword[0] , "UTR-8") + "+"
    			+ URLEncoder.encode(keyword[1] , "UTR-8") + "+"
    			+ URLEncoder.encode(keyword[2] , "UTR-8")     			
    			+ "&display=10&start=1&sort=sim");
    	URLConnection urlCon = url.openConnection();
    	urlCon.setRequestProperty("X-Naver-Client-Id", clientId);
    	urlCon.setRequestProperty("X-Naver-Client-Secret", clientSecret);
    	
    	
    	return list;
    }*/

}