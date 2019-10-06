package Test.Test;



	import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;

	//https://docs.komoran.kr/firststep/tutorial.html 사이트 참고!!!!
	//shineware의 KOMORAN이용!!!


	public class ReadOut  {

	   
	   public static void main(String[] args) throws Exception {
	     
	      Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
	      File file= new  File("C:/Users/user/Documents/카카오톡 받은 파일/dummy_news"); //파일 경로 저장
	      
	      //파일인지 확인
	      FileFilter fileFilter =new FileFilter() {
	         public boolean accept(File file) {
	            return file.isFile();
	         }
	      };
	      //파일목록을 저장
	      File[] filelist=file.listFiles();

	     try {
	     BufferedWriter pw=new  BufferedWriter(new FileWriter("C:/Users/user/Documents/카카오톡 받은 파일/dummy_news/결과.txt",false));
	     KomoranResult analyzeResultList=null;
	     List<String> NounsList = null;
	     String reprint=null;
	     String line="";
	     for(File re:filelist) {
	        //파일 읽기
	    	 
	        Scanner scan=new Scanner(re);
	       
	       
	        
	        while(scan.hasNextLine()) {
	          line += scan.nextLine();   
	          analyzeResultList=komoran.analyze(line);
	          NounsList=analyzeResultList.getNouns();
	          reprint=String.join("\t", NounsList);
	         
	        }
	        pw.write(reprint+"\r\n");
	        line="";
	      
	     }
	    
	       
	      pw.close();
	         
	         }catch(IOException e) {
	            e.printStackTrace();
	         }
	             
	            
	            
	         }
	        
//	         /*
//	          * for (Token token : tokenList) {
//	          * 
//	          * System.out.format("(%2d, %2d) %s/%s\n", token.getBeginIndex(),
//	          * token.getEndIndex(), token.getMorph(), token.getPos()); }
//	          */

	     }
	      
	      
	    
	       

