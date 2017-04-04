/*
등록할 때마다 copy 만하지말고

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
	
	JPanel p_west; //좌측 등록폼
	JPanel p_content; //우측 영역 전체
	JPanel p_north; //우측 선택 모드 영역
	JPanel p_center; //Flow가 적용되어 p_table과 p_grid를 모두 존재시켜놓은 컨테이너
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
	File file;

	//html option과는 다르므로, Choice 컴포넌트의 값을 미리 받아놓자!!
	//new를 못함 이 크기는 db에 의존적이기 때문에!
	//String[][] subcategory; 배열따위는 이제x
	//이 컬렉션은 rs객체를 대체할 것이다. 그럼으로써 얻는 장점은?
	//더이상 rs.last, rs.getRow 등의 고생할 필요가 없다.
	
	ArrayList<SubCategory> subcategory = new ArrayList<SubCategory>();

	
	public BookMain() {
		
		//여기서 아무리 con넘겨봐도 null이므로 시간차 생성이 필요하다. Timming문제!이럴땐
		//생성자로 하면 안된다.
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
		
		//초이스 컴포넌트와 리스터 연결
		ch_table.addItemListener(this);
		ch_grid.addItemListener(this);

		setVisible(true);
		setSize(800,600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void init(){
		//초이스 컴포넌트에 최상위 목록 보이기
		con = manager.getConnection();
		String sql = "select*from topcategory order by topcategory_id asc";
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
		
		//테이블 패널과 그리드 패널에게 Connection 전달
		//but !!
		//p_table.set이라고햐ㅏ면 안된다. JPanel에는 setConnection이 없다
		//자식 자료형으로 내려가준다. 
		((TablePanel)p_table).setConnection(con);
		((GridPanel)p_grid).setConnection(con);
		
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
		sb.append(" topcategory where category_name ='"+v+"') order by subcategory_id asc ");
		
		System.out.println(sb.toString()); //쿼리문이 잘 되고있나 확인해보자
		PreparedStatement pstmt=null;
		ResultSet rs= null;
		
		try {
			pstmt = con.prepareStatement(sb.toString());
			rs= pstmt.executeQuery();
			
			/* 서브 카테고리의 정보를 2차원 배열에 담기 + 출력
			고정시키지 말고 동적으로 만들자
			subcategory = new String[레코드 수][컬럼 수];
			subcategory = new String[][];
			이렇게 할필요 없고 밑에 collectionFramework로! */

			/*
			 rs에 담겨진 레코드 1개는 SubCategory
			 클래스의 인스턴스 1개로 받자
			 */
			
			while(rs.next()){		
				SubCategory dto = new SubCategory(); //텅빈DTO 채워나가야함!
				//int subcategory_id = rs.getInt("subcategory_id");
				//dto.setSubcategory_id(subcategory_id);
				//한줄로 표현가능
				
				dto.setSubcategory_id(rs.getInt("subcategory_id"));
				dto.setCategory_name(rs.getString("category_name"));
				dto.setTopcateogry_id(rs.getInt("topcategory_id"));
				
				//컬렉션에 담기
				subcategory.add(dto);
				//뭐가 choice component에 보여져야함? category_name
				//rs. 해도 되고 dto.해도됨
				ch_sub.add(dto.getCategory_name());
			}
			
			/*	이제 필요x	
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
	
	//상품 등록 메서드
	public void regist() {
		/*
		 내가 지금 선택한 subcategory choice의 index를 구해서 그 index로 arraylist를
		 접근하여 객체를 반환 받으면 정보를 유용하게 쓸 수 있다.
		 */
		int index = ch_sub.getSelectedIndex();
		SubCategory dto = subcategory.get(index); //dto 이안에는 이름, id값 등 다 들어있다.

		String book_name = t_name.getText();//책이름
		int price = Integer.parseInt(t_price.getText()); //가격
		//String으로 해도 되는데 왜 int로? 자료형에 대한 정확한 종류를 명시하기 위해서!
		//img 경로 밑에 openFile에서 file클래스 쓸 수 있다. File file 변수빼자
		String img = file.getName(); //파일명
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into book(book_id, subcategory_id, book_name, price, img)");
		//오라클에 이미지 정보가 byte정보로 들어갈 수 있다? 있다.
		//subcategory_id 는 arraylist로 받음,, 
		sb.append("values(seq_book.nextval,"+dto.getSubcategory_id()+",'"+book_name+"',"+price+",'"+img+"')");
		//나머지 book_name이랑 등은 사용자가 입력한거니까,, 얘네들 변수화
		System.out.println(sb.toString());
		
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement(sb.toString());
			//SQL문이 DML(insert, delete, update)
			
			int result = pstmt.executeUpdate();
			
			//위의 메서드는 숫자값을 반환하며, 
			//이 숫자값은 이쿼리에 의해 영향을 받는 레코드 수를 반환한다.
			//insert의 경우 언제나 1 이 반환된다.
			if(result !=0) {
				//System.out.println(book_name + "등록성공");
				copy();
				
				((TablePanel)p_table).init();
				((TablePanel)p_table).table.updateUI();

	
			} else {
				System.out.println("실패");				
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
		
		//위험하다 언제나 choice가 아니므로! 조건문이 필요하다
		if(obj==ch_top) {
			Choice ch = (Choice)e.getSource();
			getSub(ch.getSelectedItem()); //한글이므로 item!
		} else if(obj==ch_table) {
			//System.out.println("테이블볼래");
			p_table.setVisible(true);
			p_grid.setVisible(false);
		} else if(obj==ch_grid) {
			//System.out.println("그리드볼래");
			p_table.setVisible(false);
			p_grid.setVisible(true);
		}
	}
	
	//그림 파일 불러오기
	public void openFile() {
		int result = chooser.showOpenDialog(this);
		if(result== JFileChooser.APPROVE_OPTION) {
			//선택한 이미지를 캔버스에 그릴 것이다.
			file = chooser.getSelectedFile();
			img = kit.getImage(file.getAbsolutePath());	
			can.repaint();
			System.out.println(file.getAbsolutePath());
		}
	}
	
	/* 이미지 복사하기
	 유저가 선택한 이미지를 개발자가 지정한 위치로 복사를 해놓자!!
	 */
	public void copy() {
		
		FileInputStream fis=null;
		FileOutputStream fos=null;

		try {
			String dest = "C:/java_workspace2/DBProject2/data/";
			String filename =file.getName();
			
			fis = new FileInputStream(file);
			fos = new FileOutputStream(dest+filename);
			
			//현재 읽어들이는 데이터가들어있는게 아니게 개수만 들어있따.
			int data;
			byte[] b= new byte[1024]; //실제데이트는 여기에 있음!
			
			while(true) {
				//하나씩 읽는거 오래걸리니까 byte로 해보자!
				data = fis.read(b);		
				if(data ==-1)break;
				fos.write(b);
			}
			
			JOptionPane.showMessageDialog(this, "복사완료");
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
