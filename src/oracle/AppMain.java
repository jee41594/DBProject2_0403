/*
 public JTable(int numRows, int numColumns)
 */
package oracle;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class AppMain extends JFrame implements ItemListener{
	
	ConnectionManager manager;
	//모든 객체간 공유하기 위해 con 필요, con 전에 manager부터 필요!
	Connection con;
	
	JTable table;
	JScrollPane scroll;
	JPanel p_west, p_center;
	Choice choice;
	String[][] item = {
			{"테이블 선택 ▼",""},
			{"사원테이블","emp"},
			{"부서테이블","dept"}
	};
	
	//집합형태니까 배열로 만들 수 있다.
	TableModel[] model = new TableModel[item.length]; 
	
	public AppMain(){	

		/*---------------------------------------------------------------
		 디자인과 로직을 분리시키기 위해 중간자(Controller)의 존재가 필요!
		 JTable에서는 이 Controller의 역할을 TableModel이 해준다.
		 TableModel을 사용하면 JTable은 자신이 보여줘야 할 데이터를
		 TableModel로부터 정보를 얻어와 출력한다.
		 getColumnCount() 
		 getRowCount()
		 getValueAt()
		 
		 모델 : 현실의 db를 그대로 반영한 것을 모델이라 한다.
		 JTable은 모델한테 항상 물어본다. 몇행 몇열,,
		 getColumnCount, getRowCount 등을 호출하는 주체 = JTable
		-----------------------------------------------------------*/
		manager = ConnectionManager.getInstance();
		con = manager.getConnection();
		
		table = new JTable();
		scroll = new JScrollPane(table);
		p_west = new JPanel();
		p_center = new JPanel();
		choice = new Choice();
		

		//2차원 배열에 넣어서 초이스 구성
		for(int i =0; i<item.length; i++) {
			choice.add(item[i][0]);		
		}
		
		/* 이렇게하면 비효율적이므로!
		choice.add("선택 ▼");
		choice.add("emp");
		choice.add("dept");
		*/
		
		//테이블 모델들을 올려놓자
		model[0] = new DefaultTableModel();
		model[1] = new EmpModel(con);
		model[2] = new DeptModel(con);

		p_west.add(choice); //west영역에 붙이기
		p_center.add(scroll);
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(scroll);
		
		/* 테이블에 데이터 넣기
		table.setValueAt("사과", 0, 0);
		table.setValueAt("배", 0, 1);
		table.setValueAt("장미", 1, 0);
		table.setValueAt("튤립", 1, 1);
		table.setValueAt("잉어", 2, 0);
		table.setValueAt("붕어", 2, 1);
		*/		

		//ItemChoice와 ItemListener와 연결
		choice.addItemListener(this);
		
		//윈도우 창 닫을 때 connection 오라클접속도 끊기
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//커넥션 닫기
				manager.disConnect(con);
				//프로그램 종료
				System.exit(0);
				//setDefault사라지고 우리가 손으로 만든 것이다.
			}
		});

		pack();
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE); 이제 더이상 필요x
	}
	
	//해당되는 테이블 보여주기
	public void showData(int index) {
		System.out.println("당신이 보게 될 테이블은?" + item[index][1]);	
		
		table.setModel(model[index]);
		//해당되는 테이블 모델을 사용하면 된다!!
		//emp --> EmpModel
		//dept --> DeptModel
		//아무것도 아니면 --> DefaultTableModel
	}

	public void itemStateChanged(ItemEvent e) {
		Choice ch = (Choice)e.getSource();
		int index = ch.getSelectedIndex();
		showData(index);
	}
	
	public static void main(String[] args) {
		new AppMain();
	}
}
