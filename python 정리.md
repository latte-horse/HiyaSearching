## python

### 파이썬 install 하기

1. 파이썬 설치 장소에서  Scripts 파일을 찾는다.
2. 주소창에 cmd 를 입력하면 해당 주소를 가진 상태로 cmd가 실행된다.

```cmd
D:\download\Scripts>pip install requests 
```

3. 설치 후 파이썬 shell를 실행해서 import 해보자.

## 파이썬으로 크롤링 하기

```python
import requests
from bs4 import BeautifulSoup

html=requests.get("https://www.daum.net").text
soup=BeautifulSoup(html,'html.parser')

title_list=soup.select(".list_mini .rank_cont .link_issue")
ranking=soup.select(".list_mini .rank_cont .ir_wa")

#잘 뽑아왔는지 확인!
for rank,title in zip(ranking,title_list):
	r=''.join(rank)
	t=''.join(title)
	print("{}:{}".format(r,t))

	
1위:한아름
2위:이다연
3위:검찰개혁 동요메들리
4위:하나금융그룹 챔피언십
5위:강시원
6위:장하나
7위:정다은
8위:ufc
9위:조상우
    
# 뽑아온 리스트를 리스트에 저장하기
list=[] 
# 빈 리스트 하나 선언
#만약 리스트속 내용을 지우고 싶으면  list=[:]

for top in title_list:
    list.append(top.text)

#리스트에 잘 저장되었나 확인
print(list)
['한아름', '이다연', '검찰개혁 동요메들리', '하나금융그룹 챔피언십', '강시원', '장하나', '정다은', 'ufc', '조상우', '퍼플백']


```

