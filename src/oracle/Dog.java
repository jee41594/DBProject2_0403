/*
 강아지 클래스의 인스턴스를 오직 1개만 만들기
 -> 싱글톤(SingleTon pattern) 패턴 = 개발 패턴 중 하나임!
 
 javaSE ->
 javaEE 고급기술(javaSE를 포함하여 기업용 어플리케이션 제작에 사용됨) ->
 ex) 멍멍이집 - javaSE 그냥망치로 만든다
 ex) 롯데타워 - javaEE 개발 계획, 개발 이론등이 필요하다.
 
 
 90년대 초, 개발자 4명 (Gang of four)= 개발서적 출간 
"Design Pattern"  --> 객체의 인스턴스를 오직 1개만 만드는 패턴! = SingleTon이라 칭하기 시작
	(설계 	습관)
	
개발을 하다보면 어느 사람이든 인간의 로직의 보편성이 존재한다. 같은 고민을 하게 된다.
= 전세계적으로 다 비슷하다.

장점 : 국적, 사는 곳이 달라도 개발자들끼리 SingleTon이라하면 다 알아듣는다.
그래서 전산에서 가장 중요한 역할을 하는 책이 되었다.
 
 */

package oracle;

public class Dog {
	//누군가가 Dog형 변수를 접근할 수 있도로 기회를 주자.
	//Dog형에 들어있는 instance는 null이고 null이 아니게 만들자
	//얘마저도 static으로 하면 밑에 instance가 얘를 볼 수 있따.
	static private Dog instatnce;
	
	//강아지가 여러개 올라갈 수 없도록 우리가 방어해보자!
	//new에 의한 생성을 막자!!
	private Dog() {
		//이렇게하면 클래스 내에서의 호출만가느하므로
		//UseDog에서 new Dog(); 불가능!
		//그러나 방어는햇지만 1마리도 만들 수 없어 우리는 1마리만 만들고싶다..
	}
	
	//new 할필요없이 Dog.instance하면된다.
	//그러나 static에서 non-static이 보이지 않는다.
	static public Dog getIntatnce() {
		if(instatnce==null) {
			instatnce = new Dog();
			
		}
		return instatnce;
	}
}
