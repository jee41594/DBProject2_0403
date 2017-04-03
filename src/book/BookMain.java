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
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BookMain extends JFrame implements ItemListener, ActionListener{
	
	DBManager manager=DBManager.getInstance();
	Connection con;
	
	JPanel p_west; //좌측 등록폼
	JPanel p_content; //우측 영역 전체
	JPanel p_north; //우측 선택 모드 영역
	JPanel p_table; //JTable이 붙여질 패널
	JPanel p_grid; //그리드 방식으로 보여질 패널
	Choice ch_top;
	Choice ch_sub;
	JTextField t_name;
	JTextField t_price;
	Canvas can;
	JButton bt_regist;
	CheckboxGroup group;
	Checkbox ch_table, ch_grid;
	//static으로 스스로 올리는 것!
	Toolkit kit = Toolkit.getDefaultToolkit();
	Image img;
	JFileChooser chooser;

	public BookMain() {
		
		p_west = new JPanel();
		p_content = new JPanel();
		p_north = new JPanel();
		p_table = new JPanel();
		p_grid = new JPanel();
		
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
		
		bt_regist = new JButton("등록");
		
		group = new CheckboxGroup();
		ch_table = new Checkbox("Border", true, group);
		ch_grid = new Checkbox("Grid", false, group);
		
		//파일 추저 올리기
		chooser = new JFileChooser("C:/html_workspace/images");
		
		//초이스 컴포넌트의 크기 폭 조절
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
		p_north.setPreferredSize(new Dimension(650, 50));
		p_north.add(ch_table);
		p_north.add(ch_grid);
		
		p_content.setBackground(Color.cyan);
		p_content.setPreferredSize(new Dimension(650, 550));
		
		add(p_west, BorderLayout.WEST);
		add(p_north, BorderLayout.NORTH);
		
		p_table.add(p_north);
		p_table.add(p_content);
		
		add(p_table);

		init();
		
		ch_top.addItemListener(this);
		can.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent e) {
				openFile();
			}	
		});

		setVisible(true);
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void init(){
		//초이스 컴포넌트에 최상위 목록 보이기
		con = manager.getConnection();
		String sql = "select*from topcategory";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			//이 안에 있으면 못닫으므로 위로 뺐다
			pstmt = con.prepareStatement(sql);
			//쿼리문 수행
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
	}
	
	//하위 카테고리 가져오기
	public void getSub(String v) {
		//기존에 이미 채워진 아이템이 있다면, 먼저 싹 지운다.
			ch_sub.removeAll();

		//쿼리문이 정해져있으면 안된다.
		StringBuffer sb = new StringBuffer();
		sb.append("select*from subcategory");
		sb.append(" where topcategory_id =(");
		sb.append(" select topcategory_id from");
		sb.append(" topcategory where category_name ='"+v+"')");
		
		System.out.println(sb.toString()); //쿼리문이 잘 되고있나 확인해보자
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		
		try {
			pstmt = con.prepareStatement(sb.toString());
			rs= pstmt.executeQuery();
			while(rs.next()) {
				ch_sub.add(rs.getString("category_name"));
			}
			
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
	
	public void itemStateChanged(ItemEvent e) {
		Choice ch = (Choice)e.getSource();
		getSub(ch.getSelectedItem()); //한글이므로 item!
		
	}
	
	//그림 파일 불러오기
	public void openFile() {
		int result = chooser.showOpenDialog(this);
		if(result== JFileChooser.APPROVE_OPTION) {
			//선택한 이미지를 캔버스에 그릴 것이다.
			File file = chooser.getSelectedFile();
			img = kit.getImage(file.getAbsolutePath());	
			can.repaint();
		}
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("나 눌렀어?");		
	}

	public static void main(String[] args) {
		new BookMain();
	}



}
