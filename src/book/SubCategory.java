/*
객체지향 언어인 자바에서는 현실의 사물을 클래스로 정의하지만
Database에서는 현실의 사물을 Entity라는 객체 개념으로 표현한다.
결국 객체를 표현하는 방법만 다를 뿐 개념은 같다.
현실 반영이라는 개념은 같음!

객체지향 언어에서 클래스가 인스턴스를 생성해내는 거푸집이라면
데이터베이스 분야에서 테이블은 레코드를 저장할 수 있는 틀로 봐도 무방하다.
이때 하나의 레코드는 결국 하나의 객체로 봐야 한다.
결론) 테이블에 존재하는 상품 레코드 수가 총 5개 라면 개발자는 이 각각의 레코드를
5개의 인스턴스로 각각 받으면 된다.

아래의 클래스는 로직 작성용이 아니라 즉 한건의 레코드를 담기 위한 저장공간용도로만
사용할 클래스이다. 어플리케이션 설계뿐야에서는 이러한 목적의 클래스를 가리켜
VO, DTO라 한다.
VO = Value Object = 값만 담긴 객체
DTO = Data Transfer Object = 값을 전달하기 위한 객체

 */

package book;
//변수명은 테이블과 일치시켜야 한다.
public class SubCategory {
	/*
	왜 private? 로직이 아니라 데이터만을 닫는 용도이기 때문에 데이터는 보호되어야 한다.
	Dummy클래스
	오직 데이터 담는 그릇용!
	
	배열과의 차이점은?
	배열과 객체는 차원이 틀린 방식으로 개발되므로 
	객체로 처리하는 것이 훨씬 더 작업방식이나 개념 상 좋다!
	배열은 String이나 int로 정하기는 하지만 자료형들이 섞일 수 없고
	객체지향적이지 않다.
	*/
	
	private int subcategory_id;
	private int topcateogry_id;
	private String category_name;
	
	//이걸 누군가 가져갈 수 있도록 은닉화 시키자!!
	//alt+shift+s -> generate getter/setter

	public int getSubcategory_id() {
		return subcategory_id;
	}
	public void setSubcategory_id(int subcategory_id) {
		this.subcategory_id = subcategory_id;
	}
	public int getTopcateogry_id() {
		return topcateogry_id;
	}
	public void setTopcateogry_id(int topcateogry_id) {
		this.topcateogry_id = topcateogry_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
}
