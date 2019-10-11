#-*- coding:utf-8 -*-


import os
import requests
from bs4 import BeautifulSoup
import pandas as pd
import urllib
from datetime import datetime
import lxml
import json
import pathlib
from datetime import datetime
import numpy as np
import config
import re


###########################################다음 키워드 뽑아오기
def getDaumKeywords():
    dhtmllist = []
    html=requests.get("https://www.daum.net").text
    soup=BeautifulSoup(html,'html.parser')

    title_list=soup.select(".list_mini .rank_cont .link_issue")
    ranking=soup.select(".list_mini .rank_cont .ir_wa")

    for top in title_list:
        dhtmllist.append(top.text)
    return dhtmllist

#############################################네이버 키워드 뽑아오기
def getNaverKeywords():
    nhtmllist = []
    url_naver = "http://www.naver.com"

    html = requests.get(url_naver).text
    soup = BeautifulSoup(html, 'html.parser')

    listHtml = soup.select('.ah_roll_area .ah_k')

    #keyword 추출
    naver_keywords = []
    for keyword in listHtml:
        nhtmllist.append(keyword.get_text())

    #cnt 개의 결과만을 반환
    return nhtmllist

########################################### 다음+  네이버 키워드 뽑아오

def getTotalKeywords():
    htmllist = []
    htmllist=getDaumKeywords()+getNaverKeywords()
    return htmllist

###########################################키워드로 다음 뉴스 링크와 타이틀만 저장 



def DaumSearch(search_word,cnt):
    furl="https://search.daum.net/search?w=news&sort=recency&q="
    surl="&cluster=n&DA=STC&s=NS&a=STCF&dc=STC&pg=1&r=1&p="
    lurl="&rc=1&at=more&sd=&ed=&period="

    #데이터 초기화를 위해 for문 안에 daumitem배열 선언을 한다.
    daumlist=[]
    #한페이지당 10개의 뉴스가 있으므로 30개를 가지기 위해서 3까지
    for i in range(cnt):
        #검색할 주소 를 다 더해준다.
        try:
            url = requests.get(furl + search_word + surl + str(i) + lurl)
        except requests.exceptions.Timeout as errt:
            print ("Timeout Error:",errt) 
        finally:
            #검색
            soup = BeautifulSoup(url.content,'html.parser')
            #뉴스의 url주소를 가져온다.
            urllink = soup.select("a[class*=f_link_b]")
            #뉴스의 제목을 가져온다.
            urlname = soup.select(".f_link_b")
            #각각을 딕셔너리에 추가한다.
            for list1,list2 in zip(urllink,urlname):
                daumlist.append({"site":"Daum","keyword":search_word,"title":list2.text,"link":list1.get('href')})

    return daumlist
#########################################키워드로 네이버 뉴스 링크와 타이틀만 저장 
def NaverSearch(search_words, cnt):
    enc_text = urllib.parse.quote(search_words)
    url = "https://openapi.naver.com/v1/search/news.json?query={0}&display={1}&sort={2}".format(
        enc_text, cnt, "date"
    ) 

    #naver API를 이용하여 검색
    request = urllib.request.Request(url)
    request.add_header("X-Naver-Client-Id", config.clientID)
    request.add_header("X-Naver-Client-Secret", config.clientSecret)
    try:
        response = urllib.request.urlopen(request)
    except Exception as e:
        print(e)
    else:
        rescode = response.getcode()
        if(rescode == 200):
            response_body = response.read()
            news_list = json.loads(response_body.decode('utf-8'))['items']

            #title과 link만 추출하여 담기
            result_list = []
            for news in news_list:
                result_list.append({
                    "site" : "Naver",
                    "keyword" : search_words ,
                    'title' : re.sub("<[^>]*>", '', news['title']) ,
                    'link' : news['originallink']})
            
        else:
            print("Error Code:" + rescode)
   
    #결과 반환 (없으면 없는대로)
    return result_list


#########################################네이버와 다음에서 키워드 검색 후 html content 뽑아오는 함수 

def SearchSave(keyword,site):
    now=datetime.now()
    nowTime =  now.strftime("%y%m%d")
    #nowTimeTime = str((int(now.strftime("%H")) / 10 * 10) + int((now.strftime("%M")) / 10 * 10))
    nowTimeTime =  "%02d%02d" % (now.hour ,now.minute // 10 * 10)
    #2칸을 채우는데 빈곳은 0으로 채운다. 
    num = 1
    #키워드 네이버+다음을 한다.
    for i in keyword:
        #
        result1 = NaverSearch(i , 30) # 네이버에서 링크 30개.......?
        result2 = DaumSearch(i , 3) # 다음에서  페이지 3페이지 총 30개 !
        df = pd.DataFrame( result1 + result2 )
        #경로 하나 만들고
        n = "%02d" % (num)
        filepath = ("C:/바탕화면/" + nowTime + "/" + nowTimeTime + "/" + site + "_K_"+ n)
        if not os.path.isdir(filepath):
            os.makedirs(filepath)
            os.chdir(filepath)
        else:
            os.chdir(filepath)
        num2=1
        #html 저장하는 것 하나 만들기
        for link,title in zip(df['link'] , df['title']):
            try:
                url = requests.get(link , timeout = 10)
                #soup = BeautifulSoup(url , 'html.parser' , from_encoding = 'utf-8')
                soup = BeautifulSoup(url.content,'html.parser')
                savefilelist = soup.html.body
                savefiletitle=title
                f = open(str(num2) +".html" , mode = "w" , encoding = 'utf-8')
                f.write(savefiletitle)
                f.write(savefilelist.text)
            except requests.exceptions.Timeout as errt:
                print ("Timeout Error:",errt) 
            except requests.exceptions.TooManyRedirects as rerr:
                print (rerr)
            except requests.exceptions.RequestException as e:
                print (e)
            except requests.exceptions.HTTPError as err:
                print (err)
            except requests.exceptions.ContentDecodingError as derr:
                print (derr)
            except AttributeError as ea:
                print (ea)
            finally:
               
                f.close()
                num2+=1
        num+=1

#getDaumKeywords() 다음 키워드 뽑기
#getNaverKeywords() 네이버 키워드 뽑기
        
#다음+네이버 키워드 뽑아오는 함수 getTotalKeywords()
#다음 링크 주소 저장 함수 DaumSearch(search_word , cnt) cnt는 페이지 수 이다.한페이지당 10개
#네이버 링크 주소 저장 함수 NaverSearch(search_words , cnt)
#제대로 검색해보자 SearchSave(keyword , searchfunction , site)


#DaumSearchSave(getTotalKeywords())

# Daum D Naver N의 키워드를 검색하는 것!
#SearchSave(getNaverKeywords() ,  "N") 으로 함수 적용 
SearchSave(getDaumKeywords() , "D")


