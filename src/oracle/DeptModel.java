//emp 테이블의 데이터를 처리하는 컨트롤러!!

package oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.AbstractTableModel;

public class DeptModel extends AbstractTableModel{
	Connection con;
	PreparedStatement pstmt;
	ResultSet rs;
	
	String[] column; //column을 넣을 배열!
	String[][] data; //레코드를 넣을 배열!
	
	public DeptModel(Connection con) {
		this.con= con;
		
		//manager =  ConnectionManager.getInstance();
		/*
		 1. 드라이버 로드
		 2. 접속
		 3. 쿼리
		 4. 자원들 닫기
		 */
		
		try {			
			if(con!=null){
				String sql = "select*from dept";
				
				//아래의 pstmt에 의해 생성되는 rs는 커서가 자유로울 수 있다.
				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
				//결과 집합 반환
				rs = pstmt.executeQuery();
				
				//먼저 컬럼을 구해보자 MetaData란 그 자체 데이터 설정 정보를 얻어오는 것이다.
				ResultSetMetaData meta = rs.getMetaData(); 
				int count = meta.getColumnCount(); //컬럼의 갯수
				column = new String[count]; //count만큼 배열 생성
				
				//컬럼명을 채우자
				for(int i =0; i<column.length; i++) {
					//여기서 칼럼들은 1부터 시작한다. (0이 아니고)
					column[i]=meta.getColumnName(i+1);
				}
				 
				rs.last(); //제일마지막으로 보냄
				int total = rs.getRow(); //레코드 번호
				
				//총 레코드 수를 알았으니 2차원배열 생성하자
				data = new String[total][column.length];
				
				rs.beforeFirst();
				//레코드를 이차원배열인 data에 채워넣기
				for(int a=0; a<data.length; a++) {
					rs.next();
					for(int i=0; i<data[a].length; i++) {
						data[a][i] = rs.getString(column[i]); //잘모르면 String으로 한다.
					}
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
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
			//con은 여기서 닫으면 안도고 어플리케이션이 끝나는 시점에서 닫아야 한다.
			//윈도우 창 끌 때			
			/*
			if(con!=null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			} */
		}
	}
	
	@Override
	public int getColumnCount() {
		return column.length;
	}
	
	@Override
	public String getColumnName(int index) {
		return column[index];
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public Object getValueAt(int row, int col) {
		return data[row][col];
	}

}
