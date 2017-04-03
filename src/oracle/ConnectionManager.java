/*
 각 TableModel마다 접속 정보와 접속 객체를 두게 되면
 접속 정보가 바뀔 때 모든 클래스의 코드도 수정해야 하는 유지보수상의 문제 뿐만 아니라
 각 TableModel 마다 Connection을 생성하기 때문에 접속이 여러개 발생한다.
 하나의 어플리케이션이 오라클과 맺는 접속은 1개만으로도 충분하다.
 그리고 접속이 여러개이면 하나의 세션에서 발생시키는 각종 DML 작업이
 통일되지 못하게 된다.. 즉 다른 사람으로 인식됨
 xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
 --> Connection 여러개여도 상관없다! finally에서 닫으므로,,
 
 객체 인스턴스를 메모리 heap에 한개만 만드는 방법!
 
 <순서>
 현재
 ConnectionManager 는 heap올라가고
 static에 선언되어있는 instance가 얘를 가리키고 있다.
 
 누군가 getInstance메서드 호출하면?
 new는 안되고 기존것만 가져가므로 heap에는 1개만 올라간다.
 
 --------------------------------------------
 connection을 하나만 공유하는 방법은?
 Connection con; 선언하고 어디서 올리지? - 생성자호출하는 곳에서!
 
 */

package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	static private ConnectionManager instance;
	String driver="oracle.jdbc.driver.OracleDriver";
	String url="jdbc:oracle:thin:@localhost:1521:XE";
	String user="batman";
	String password="1234";
	
	Connection con;
	
	//개발자가 제공하는 방법 의외에 접근은 아예 차단하자! = 사용자에 의한 임의생성, 즉 new 막자
	private ConnectionManager() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	//인스턴스의 생성없이도 외부에서 메서드를 호출하여
	//이 객체의 인스턴스를 가져갈 수 있도록
	//getter를 제공해주자.
	static public ConnectionManager getInstance() {
		//instance가 null이면 내가 뉴해주겠어
		if(instance==null) {
			instance = new ConnectionManager();
		}
		return instance;
		//여기에만 static하면 말그대로 instance이므로 위에있는 instance가 안보인다.
		//위에도 static해줘야한다.
	}
	
	//이 메서드를 호출하는 호출자는 Connection 객체를
	//반환받게 된다.
	public Connection getConnection() {
		return con;
	}
	
	//커넥션을 다 사용 후 닫기
	public void disConnect(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
