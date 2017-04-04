/*
 JTable�� ������ �г�
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
	 ���� ����� ��ǰ ���̺�(book)�� �;ߵȴ�.
	 ArrayList<Book> list = new ArrayList<Book>();
	 ��������!Vector ���.
	 
	 Vector�� list�� �Ѵ� ���� ������ ��������? -> ����ȭ ���� ����
	 ����ȭ�� ����! Ư�������尡 ������� ��� ���� �������� �����尡
	 ������ ���� - > Vector�� ����ȭ���ѳ��Ƽ� �׷�����
	 ����ȭ�� ��Ű�°� �߿����� �ʰ� �ӵ������ ������ ArrayList����
	 ����ȭ �߿�� �ӵ� x -> Vector����.
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
	

	//�̷��� �ϸ� Timming ���� �ذ�! ���Ҷ� �Ѱ��ָ� �ȴ�.
	//�����ڴ� : �����ҋ� ȣ��
	//�Ϲݸ޼���� : ���ҋ� ȣ��
	public void setConnection(Connection con) {
		this.con=con;
		//connection�� �Ѱ� �޾��� ���� select���� �����ٴ� �ǹ̷� ���⼭ ȣ��
		init();
		
		//���̺� ���� JTable�� ����
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
				//�������μ� dto�� ����ֱ� �빮�� columnCount�� ���α׷��������� �޾ƿ��°� ��ƴ�.
				return cols;
			}
			public Object getValueAt(int row, int col) {
				//JTable�� ȣ���ϴ� ��! ArrayList�� �������
				//row = ArrayList�� index�� ��Ī! dto�� ����մ°� �����Ѵ�.

				Vector vec = (Vector)list.get(row);
				return vec.elementAt(col);
			}
		};
		
		//�ϼ��Ǿ����ϱ� ���� ���������� ���̺� ���� ��������̤���.
		table.setModel(model);
	}
	

	
	//�����ͺ��̽� �������� -> book���̺��� ���ڵ� ��������
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
				
				//���� ���Ϳ� �����߰��ϴ� ���̴�. Vectors of Vector
				list.add(data);
				
				/*
				rs�� ������ �÷����� DTO�� �Űܴ���!! ���� rs�� book�� ����ִ�.
				dto�� ���������ϰ� ����class�� book�̴� setter�迭��!
				
				Book dto = new Book();
				dto.setBook_id(rs.getInt("book_id"));
				dto.setBook_name(rs.getString("book_name"));
				dto.setImg(rs.getString("img"));
				dto.setPrice(rs.getInt("price"));
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				ist.add(dto);
				
				���⼭�� dto�� �Ұ��� */
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
