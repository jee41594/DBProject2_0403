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
	//��� ��ü�� �����ϱ� ���� con �ʿ�, con ���� manager���� �ʿ�!
	Connection con;
	
	JTable table;
	JScrollPane scroll;
	JPanel p_west, p_center;
	Choice choice;
	String[][] item = {
			{"���̺� ���� ��",""},
			{"������̺�","emp"},
			{"�μ����̺�","dept"}
	};
	
	//�������´ϱ� �迭�� ���� �� �ִ�.
	TableModel[] model = new TableModel[item.length]; 
	
	public AppMain(){	

		/*---------------------------------------------------------------
		 �����ΰ� ������ �и���Ű�� ���� �߰���(Controller)�� ���簡 �ʿ�!
		 JTable������ �� Controller�� ������ TableModel�� ���ش�.
		 TableModel�� ����ϸ� JTable�� �ڽ��� ������� �� �����͸�
		 TableModel�κ��� ������ ���� ����Ѵ�.
		 getColumnCount() 
		 getRowCount()
		 getValueAt()
		 
		 �� : ������ db�� �״�� �ݿ��� ���� ���̶� �Ѵ�.
		 JTable�� ������ �׻� �����. ���� �,,
		 getColumnCount, getRowCount ���� ȣ���ϴ� ��ü = JTable
		-----------------------------------------------------------*/
		manager = ConnectionManager.getInstance();
		con = manager.getConnection();
		
		table = new JTable();
		scroll = new JScrollPane(table);
		p_west = new JPanel();
		p_center = new JPanel();
		choice = new Choice();
		

		//2���� �迭�� �־ ���̽� ����
		for(int i =0; i<item.length; i++) {
			choice.add(item[i][0]);		
		}
		
		/* �̷����ϸ� ��ȿ�����̹Ƿ�!
		choice.add("���� ��");
		choice.add("emp");
		choice.add("dept");
		*/
		
		//���̺� �𵨵��� �÷�����
		model[0] = new DefaultTableModel();
		model[1] = new EmpModel(con);
		model[2] = new DeptModel(con);

		p_west.add(choice); //west������ ���̱�
		p_center.add(scroll);
		add(p_west, BorderLayout.WEST);
		add(p_center);
		add(scroll);
		
		/* ���̺� ������ �ֱ�
		table.setValueAt("���", 0, 0);
		table.setValueAt("��", 0, 1);
		table.setValueAt("���", 1, 0);
		table.setValueAt("ƫ��", 1, 1);
		table.setValueAt("�׾�", 2, 0);
		table.setValueAt("�ؾ�", 2, 1);
		*/		

		//ItemChoice�� ItemListener�� ����
		choice.addItemListener(this);
		
		//������ â ���� �� connection ����Ŭ���ӵ� ����
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//Ŀ�ؼ� �ݱ�
				manager.disConnect(con);
				//���α׷� ����
				System.exit(0);
				//setDefault������� �츮�� ������ ���� ���̴�.
			}
		});

		pack();
		setVisible(true);
		//setDefaultCloseOperation(EXIT_ON_CLOSE); ���� ���̻� �ʿ�x
	}
	
	//�ش�Ǵ� ���̺� �����ֱ�
	public void showData(int index) {
		System.out.println("����� ���� �� ���̺���?" + item[index][1]);	
		
		table.setModel(model[index]);
		//�ش�Ǵ� ���̺� ���� ����ϸ� �ȴ�!!
		//emp --> EmpModel
		//dept --> DeptModel
		//�ƹ��͵� �ƴϸ� --> DefaultTableModel
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
