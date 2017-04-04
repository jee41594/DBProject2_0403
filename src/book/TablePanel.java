/*
 JTable이 얹혀질 패널
 */

package book;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class TablePanel extends JPanel{
	
	private Connection con;
	JTable table;
	JScrollPane scroll;
	TableModel model;

	Vector list = new Vector();
	Vector<String> columnName = new Vector<String>();
	/*
	 내가 등록한 제품 테이블(book)이 와야된다.
	 ArrayList<Book> list = new ArrayList<Book>();
	 쓰지말고!Vector 썼다.
	 
	 Vector는 list와 둘다 같다 하지만 차이점은? -> 동기화 지원 여부
	 동기화를 지원! 특정쓰레드가 사용중인 경우 동시 진해중인 쓰레드가
	 접근을 못함 - > Vector는 동기화시켜놓아서 그런거임
	 동기화가 엉키는게 중요하지 않고 속도향상이 좋으면 ArrayList쓰고
	 동기화 중요시 속도 x -> Vector쓴다.
	 */
	
	int cols;
	
	public TablePanel() {

		table = new JTable();
		scroll = new JScrollPane(table);
		setLayout(new BorderLayout());
		
		add(scroll);
		setPreferredSize(new Dimension(650, 550));
		this.setBackground(Color.gray);
	}
	

	//이렇게 하면 Timming 문제 해결! 원할때 넘겨주면 된다.
	//생성자는 : 생성할떄 호출
	//일반메서드는 : 원할떄 호출
	public void setConnection(Connection con) {
		this.con=con;
		//connection을 넘겨 받았을 때만 select문을 날린다는 의미로 여기서 호출
		init();
		
		//테이블 모델을 JTable에 적용
		model = new AbstractTableModel() {
			
			public int getRowCount() {
				return list.size();
			}
			
			/*
			public String getColumnName(int index) {
			
				return list.;
			}
			 */
			
			public int getColumnCount() {
				//지금으로선 dto에 들어있기 대문에 columnCount를 프로그래밍적으로 받아오는건 어렵다.
				return cols;
			}
			public Object getValueAt(int row, int col) {
				//JTable이 호출하는 것! ArrayList에 들어있음
				//row = ArrayList의 index와 매칭! dto에 들어잇는거 빼야한다.

				Vector vec = (Vector)list.get(row);
				return vec.elementAt(col);
			}
		};
		
		//완성되었으니까 이제 마지막으로 테이블에 모델을 설정ㅎ재ㅜㄴ다.
		table.setModel(model);
	}
	

	
	//데이터베이스 가져오기 -> book테이블의 레코드 가져오기
	public void init() {
		String sql ="select * from book order by book_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(sql);
			rs =pstmt.executeQuery();
			cols = rs.getMetaData().getColumnCount();
			
			list.removeAll(list);

			while(rs.next()) {
				
				Vector<String> data = new Vector<String>();
				
				data.add(Integer.toString(rs.getInt("book_id")));
				data.add(rs.getString("book_name"));
				data.add(Integer.toString(rs.getInt("price")));
				data.add(rs.getString("img"));
				data.add(Integer.toString(rs.getInt("subcategory_id")));
				
				//기존 백터에 백터추가하는 것이다. Vectors of Vector
				list.add(data);
				
				/*
				rs의 정보를 컬렉션의 DTO로 옮겨담자!! 지금 rs는 book을 담고있다.
				dto는 역할을말하고 실제class는 book이다 setter계열로!
				
				Book dto = new Book();
				dto.setBook_id(rs.getInt("book_id"));
				dto.setBook_name(rs.getString("book_name"));
				dto.setImg(rs.getString("img"));
				dto.setPrice(rs.getInt("price"));
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				ist.add(dto);
				
				여기서는 dto로 불가능 */
			}
			

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if(rs!=null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
			
			if(pstmt!=null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}	
		}
	}
	
	public void update() {
		StringBuffer sb = new StringBuffer();
		sb.append("insert into book(");
		sb.append(" book_id,book_name");

	}
	
	
}
