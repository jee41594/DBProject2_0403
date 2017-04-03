//emp ���̺��� �����͸� ó���ϴ� ��Ʈ�ѷ�!!

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
	
	String[] column; //column�� ���� �迭!
	String[][] data; //���ڵ带 ���� �迭!
	
	public DeptModel(Connection con) {
		this.con= con;
		
		//manager =  ConnectionManager.getInstance();
		/*
		 1. ����̹� �ε�
		 2. ����
		 3. ����
		 4. �ڿ��� �ݱ�
		 */
		
		try {			
			if(con!=null){
				String sql = "select*from dept";
				
				//�Ʒ��� pstmt�� ���� �����Ǵ� rs�� Ŀ���� �����ο� �� �ִ�.
				pstmt = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				
				//��� ���� ��ȯ
				rs = pstmt.executeQuery();
				
				//���� �÷��� ���غ��� MetaData�� �� ��ü ������ ���� ������ ������ ���̴�.
				ResultSetMetaData meta = rs.getMetaData(); 
				int count = meta.getColumnCount(); //�÷��� ����
				column = new String[count]; //count��ŭ �迭 ����
				
				//�÷����� ä����
				for(int i =0; i<column.length; i++) {
					//���⼭ Į������ 1���� �����Ѵ�. (0�� �ƴϰ�)
					column[i]=meta.getColumnName(i+1);
				}
				 
				rs.last(); //���ϸ��������� ����
				int total = rs.getRow(); //���ڵ� ��ȣ
				
				//�� ���ڵ� ���� �˾����� 2�����迭 ��������
				data = new String[total][column.length];
				
				rs.beforeFirst();
				//���ڵ带 �������迭�� data�� ä���ֱ�
				for(int a=0; a<data.length; a++) {
					rs.next();
					for(int i=0; i<data[a].length; i++) {
						data[a][i] = rs.getString(column[i]); //�߸𸣸� String���� �Ѵ�.
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
			//con�� ���⼭ ������ �ȵ��� ���ø����̼��� ������ �������� �ݾƾ� �Ѵ�.
			//������ â �� ��			
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
