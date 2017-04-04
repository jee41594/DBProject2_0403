/*
ㅇㅣ 클래스는 로직을 작성하는 것이 아니라
데이터 베이스의 레코드 1건을 담기위한 객체이다.
이런 목적으로 설계된 클래스를 가리켜
설계분야에서는 DTO(데이터 전달객체), VO라 한다.
 */

package book;

public class Book {
	private int book_id;
	private int subcategory_id;
	private String book_name;
	private int price;
	private String img;
	//getter setter호출 단축키 alt+shift+s+r -> select all	
	
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
		this.book_id = book_id;
	}
	public int getSubcategory_id() {
		return subcategory_id;
	}
	public void setSubcategory_id(int subcategory_id) {
		this.subcategory_id = subcategory_id;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}

}
