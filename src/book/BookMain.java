/*
����� ������ copy ����������

 */

package book;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class BookMain extends JFrame implements ItemListener, ActionListener{
	
	DBManager manager=DBManager.getInstance();
	Connection con;
	
	JPanel p_west; //���� �����
	JPanel p_content; //���� ���� ��ü
	JPanel p_north; //���� ���� ��� ����
	JPanel p_center; //Flow�� ����Ǿ� p_table�� p_grid�� ��� ������ѳ��� �����̳�
	JPanel p_table; //JTable�� �ٿ��� �г�
	JPanel p_grid; //�׸��� ������� ������ �г�

	Choice ch_top;
	Choice ch_sub;
	JTextField t_name;
	JTextField t_price;
	Canvas can;
	JButton bt_regist;
	CheckboxGroup group;
	Checkbox ch_table, ch_grid;
	//static���� ������ �ø��� ��!
	Toolkit kit = Toolkit.getDefaultToolkit();
	Image img;
	JFileChooser chooser;
	File file;

	//html option���� �ٸ��Ƿ�, Choice ������Ʈ�� ���� �̸� �޾Ƴ���!!
	//new�� ���� �� ũ��� db�� �������̱� ������!
	//String[][] subcategory; �迭������ ����x
	//�� �÷����� rs��ü�� ��ü�� ���̴�. �׷����ν� ��� ������?
	//���̻� rs.last, rs.getRow ���� ����� �ʿ䰡 ����.
	
	ArrayList<SubCategory> subcategory = new ArrayList<SubCategory>();

	
	public BookMain() {
		
		//���⼭ �ƹ��� con�Ѱܺ��� null�̹Ƿ� �ð��� ������ �ʿ��ϴ�. Timming����!�̷���
		//�����ڷ� �ϸ� �ȵȴ�.
		p_table = new TablePanel();
		p_grid = new GridPanel();
		
		p_west = new JPanel();
		p_content = new JPanel();
		p_north = new JPanel();
		p_center = new JPanel();
		
		//p_table = new JPanel();

		ch_top = new Choice();
		ch_sub= new Choice();
		t_name = new JTextField(13);
		t_price = new JTextField(13);
		
		URL url = this.getClass().getResource("/ryan2.jpg");
		
		try {
			img = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		can = new Canvas(){
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0, 140,140, this);
			}
		};
		
		can.setPreferredSize(new Dimension(140, 140));
		
		bt_regist = new JButton("���");
		
		group = new CheckboxGroup();
		ch_table = new Checkbox("Border", true, group);
		ch_grid = new Checkbox("Grid", false, group);
		
		//���� ���� �ø���
		chooser = new JFileChooser("C:/html_workspace/images");
		
		//���̽� ������Ʈ�� ũ�� �� ����
		ch_top.setPreferredSize(new Dimension(130, 45));
		ch_sub.setPreferredSize(new Dimension(130, 45));
		
		p_west.setPreferredSize(new Dimension(150, 600));
		p_west.setBackground(Color.pink);
		p_west.add(ch_top);
		p_west.add(ch_sub);
		p_west.add(t_name);
		p_west.add(t_price);
		p_west.add(can);
		p_west.add(bt_regist);
		
		p_north.setBackground(Color.YELLOW);
		//p_north.setPreferredSize(new Dimension(650, 50));
		
		p_north.add(ch_table);
		p_north.add(ch_grid);
		
		p_center.setBackground(Color.green);
		p_center.add(p_table);
		p_center.add(p_grid);

		p_content.setBackground(Color.cyan);
		p_content.setPreferredSize(new Dimension(650, 550));
		p_content.add(p_center);
		
		add(p_west, BorderLayout.WEST);
		add(p_north, BorderLayout.NORTH);
		
		//p_table.add(p_north);
		p_content.add(p_table);
		
		add(p_content);

		init();
		
		ch_top.addItemListener(this);		
		can.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				openFile();
			}	
		});
		
		bt_regist.addActionListener(this);
		
		//���̽� ������Ʈ�� ������ ����
		ch_table.addItemListener(this);
		ch_grid.addItemListener(this);

		setVisible(true);
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void init(){
		//���̽� ������Ʈ�� �ֻ��� ��� ���̱�
		con = manager.getConnection();
		String sql = "select*from topcategory order by topcategory_id asc";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			//�� �ȿ� ������ �������Ƿ� ���� ����
			pstmt = con.prepareStatement(sql);
			//������ ����
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				ch_top.add(rs.getString("CATEGORY_NAME"));
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
			
		}
		
		//���̺� �гΰ� �׸��� �гο��� Connection ����
		//but !!
		//p_table.set�̶���᤿�� �ȵȴ�. JPanel���� setConnection�� ����
		//�ڽ� �ڷ������� �������ش�. 
		((TablePanel)p_table).setConnection(con);
		((GridPanel)p_grid).setConnection(con);
		
	}
	
	//���� ī�װ� ��������
	public void getSub(String v) {
		//������ �̹� ä���� �������� �ִٸ�, ���� �� �����.
		ch_sub.removeAll();

		//�������� ������������ �ȵȴ�.
		StringBuffer sb = new StringBuffer();
		sb.append("select*from subcategory");
		sb.append(" where topcategory_id =(");
		sb.append(" select topcategory_id from");
		sb.append(" topcategory where category_name ='"+v+"') order by subcategory_id asc ");
		
		System.out.println(sb.toString()); //�������� �� �ǰ��ֳ� Ȯ���غ���
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		
		try {
			pstmt = con.prepareStatement(sb.toString());
			rs= pstmt.executeQuery();
			
			/* ���� ī�װ��� ������ 2���� �迭�� ��� + ���
			������Ű�� ���� �������� ������
			subcategory = new String[���ڵ� ��][�÷� ��];
			subcategory = new String[][];
			�̷��� ���ʿ� ���� �ؿ� collectionFramework��! */

			/*
			 rs�� ����� ���ڵ� 1���� SubCategory
			 Ŭ������ �ν��Ͻ� 1���� ����
			 */
			
			while(rs.next()){		
				SubCategory dto = new SubCategory(); //�ֺ�DTO ä����������!
				//int subcategory_id = rs.getInt("subcategory_id");
				//dto.setSubcategory_id(subcategory_id);
				//���ٷ� ǥ������
				
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				dto.setCategory_name(rs.getString("category_name"));
				dto.setTopcateogry_id(rs.getInt("topcategory_id"));
				
				//�÷��ǿ� ���
				subcategory.add(dto);
				//���� choice component�� ����������? category_name
				//rs. �ص� �ǰ� dto.�ص���
				ch_sub.add(dto.getCategory_name());
			}
			
			/*	���� �ʿ�x	
			while(rs.next()) {
				ch_sub.add(rs.getString("category_name"));
			}*/
			
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
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
	
	//��ǰ ��� �޼���
	public void regist() {
		/*
		 ���� ���� ������ subcategory choice�� index�� ���ؼ� �� index�� arraylist��
		 �����Ͽ� ��ü�� ��ȯ ������ ������ �����ϰ� �� �� �ִ�.
		 */
		int index = ch_sub.getSelectedIndex();
		SubCategory dto = subcategory.get(index); //dto �̾ȿ��� �̸�, id�� �� �� ����ִ�.

		String book_name = t_name.getText();//å�̸�
		int price = Integer.parseInt(t_price.getText()); //����
		//String���� �ص� �Ǵµ� �� int��? �ڷ����� ���� ��Ȯ�� ������ ����ϱ� ���ؼ�!
		//img ��� �ؿ� openFile���� fileŬ���� �� �� �ִ�. File file ��������
		String img = file.getName(); //���ϸ�
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into book(book_id, subcategory_id, book_name, price, img)");
		//����Ŭ�� �̹��� ������ byte������ �� �� �ִ�? �ִ�.
		//subcategory_id �� arraylist�� ����,, 
		sb.append("values(seq_book.nextval,"+dto.getSubcategory_id()+",'"+book_name+"',"+price+",'"+img+"')");
		//������ book_name�̶� ���� ����ڰ� �Է��ѰŴϱ�,, ��׵� ����ȭ
		System.out.println(sb.toString());
		
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			//SQL���� DML(insert, delete, update)
			
			int result = pstmt.executeUpdate();
			
			//���� �޼���� ���ڰ��� ��ȯ�ϸ�, 
			//�� ���ڰ��� �������� ���� ������ �޴� ���ڵ� ���� ��ȯ�Ѵ�.
			//insert�� ��� ������ 1 �� ��ȯ�ȴ�.
			if(result !=0) {
				//System.out.println(book_name + "��ϼ���");
				copy();
				
				((TablePanel)p_table).init();
				((TablePanel)p_table).table.updateUI();

	
			} else {
				System.out.println("����");				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if(pstmt!=null){
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		
		//�����ϴ� ������ choice�� �ƴϹǷ�! ���ǹ��� �ʿ��ϴ�
		if(obj==ch_top) {
			Choice ch = (Choice)e.getSource();
			getSub(ch.getSelectedItem()); //�ѱ��̹Ƿ� item!
		} else if(obj==ch_table) {
			//System.out.println("���̺���");
			p_table.setVisible(true);
			p_grid.setVisible(false);
		} else if(obj==ch_grid) {
			//System.out.println("�׸��庼��");
			p_table.setVisible(false);
			p_grid.setVisible(true);
		}
	}
	
	//�׸� ���� �ҷ�����
	public void openFile() {
		int result = chooser.showOpenDialog(this);
		if(result== JFileChooser.APPROVE_OPTION) {
			//������ �̹����� ĵ������ �׸� ���̴�.
			file = chooser.getSelectedFile();
			img = kit.getImage(file.getAbsolutePath());	
			can.repaint();
			System.out.println(file.getAbsolutePath());
		}
	}
	
	/* �̹��� �����ϱ�
	 ������ ������ �̹����� �����ڰ� ������ ��ġ�� ���縦 �س���!!
	 */
	public void copy() {
		
		FileInputStream fis=null;
		FileOutputStream fos=null;

		try {
			String dest = "C:/java_workspace2/DBProject2/data/";
			String filename =file.getName();
			
			fis = new FileInputStream(file);
			fos = new FileOutputStream(dest+filename);
			
			//���� �о���̴� �����Ͱ�����ִ°� �ƴϰ� ������ ����ֵ�.
			int data;
			byte[] b= new byte[1024]; //��������Ʈ�� ���⿡ ����!
			
			while(true) {
				//�ϳ��� �д°� �����ɸ��ϱ� byte�� �غ���!
				data = fis.read(b);		
				if(data ==-1)break;
				fos.write(b);
			}
			
			JOptionPane.showMessageDialog(this, "����Ϸ�");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}		
		}

	}
	
	

	
	public void actionPerformed(ActionEvent e) {
		regist();
	}
	
	public static void main(String[] args) {
		new BookMain();
	}
}
