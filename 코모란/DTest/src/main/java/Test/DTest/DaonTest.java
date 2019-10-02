package Test.DTest;

import java.util.List;


import kr.bydelta.koala.daon.Tagger;
import kr.bydelta.koala.data.Sentence;

// 또는 eunjeon 대신 다른 분석기 가능: arirang, daon, etri, eunjeon, hnn, kkma, kmr, okt, rhino 

public class DaonTest {

	public static void main(String[] args) {
		

		Tagger tagger = new Tagger();
		// 코모란 분석기는 경량 분석기를 사용하는 옵션이 있습니다. 예: new Tagger(true)
		// ETRI 분석기의 경우 API 키를 필수적으로 전달해야 합니다. 예: new Tagger(API_KEY)

		// 문단을 분석해서 문장들로 얻기 (각 API가 문단 분리를 지원하지 않아도, KoalaNLP가 자동으로 구분합니다)
		List<Sentence> taggedParagraph = tagger.tag("티백 하나서 미세 플라스틱 조각 116억개 나와\r\n" + 
				"인체상 위험 아직 확인 안 돼..추가 연구 필요\r\n" + 
				"\r\n" + 
				"(서울=뉴스1) 김서연 기자 = 티백으로 우려낸 차(茶) 한잔에서 수십억 개의 미세 플라스틱이 나온다는 연구 결과가 발표됐다.\r\n" + 
				"\r\n" + 
				"30일(현지시간) CNN에 따르면 캐나다 맥길대 연구진은 지난 25일 발표한 논문에서 4개의 티백 제품을 끓는 물에 넣어 분석한 결과, 티백 하나에서 116억개에 달하는 미세 플라스틱과 31억개 나노 플라스틱 조각이 방출됐다고 밝혔다.\r\n" + 
				"\r\n" + 
				"이는 다른 음식이나 음료 등에서 발견된 검출 양보다 수천 배 더 많은 것이다. 많은 티백 업체들은 제품을 만들 때 폴리프로필렌을 사용하고 있다.\r\n" + 
				"\r\n" + 
				"연구진은 티백에서 찻잎을 제거한 뒤 티백으로 차를 끓이는 과정을 따라했다. CNN은 과학자들이 여태까지 다양한 음식에서 미세 플라스틱 조각을 발견했지만, 차를 끓이거나 다른 뜨거운 음료를 만드는 동안에도 미세 플라스틱이 배출될 수 있는지에 대한 연구는 거의 이뤄지지 않았다고 설명했다.\r\n" + 
				"\r\n" + 
				"아직 이 미세 플라스틱 조각을 마신 것이 건강에 끼치는 영향은 알려지지 않았으며, 맥길대 연구진은 이 분야에 대한 더 많은 연구가 필요하다고 말했다.\r\n" + 
				"\r\n" + 
				"올해 초 발표된 또 다른 연구에 따르면 사람들은 매주 신용카드 무게에 달하는 평균 5g의 플라스틱을 섭취하는 것으로 나타났다.\r\n" + 
				"\r\n" + 
				"세계보건기구(WHO)는 지난달 공개한 수돗물 및 용기에 든 생수(bottled water)에 포함된 플라스틱으로 인한 건강상 위험 검토보고서에서 \"현재 수준에서는 미세 플라스틱 조각이 건강상 위험을 초래하진 않는 것으로 보인다\"고 밝혔다.\r\n" + 
				"\r\n" + 
				"그러나 동시에 \"정보가 제한돼 있으며, 미세 플라스틱 조각과 이것이 인체에 미치는 영향에 대해서는 더 많은 연구가 필요하다\"고 경고했다. WHO 연구원은 \"미세 플라스틱은 식수를 포함해 모든 곳에 있기 때문에 건강에 끼치는 영향에 대해 더 많이 알아야 한다\"고 말했다.\r\n" + 
				"\r\n" + 
				"sy@news1.kr\r\n" + 
				"\r\n" + 
				"Copyright ⓒ 뉴스1코리아 www.news1.kr 무단복제 및 전재 – 재배포금지");
		// 또는 tagger.invoke(...)

		// 분석 결과는 Sentence 클래스의 List입니다.
		System.out.println(taggedParagraph.get(0).singleLineString()); // "문단을 분석합니다."의 품사분석 결과 출력

	}

}
