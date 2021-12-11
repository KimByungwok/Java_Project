package com.java.ex;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import java.awt.Font;

public class Member extends JFrame implements ActionListener, ListSelectionListener {
	JPanel leftPanel;
	// 왼쪽 패널
	JPanel scode, sname, sprice, sdivision, Btn, Panel;
	// 텍스트 필드 선언
	JTextField tf1, tf2, tf3;
	JComboBox<String> combo; // 상품 종류를 위한 콤보박스 선언
	// 버튼 4개 선언
	JButton b1, b2, b3, b4, b5; // 입력, 수정, 삭제, 검색 버튼
	// 테이블
	Vector<String> columnsNames;
	Vector<Vector<String>> rowData;
	JTable table;
	JScrollPane jsp;
	// 콤보박스에 들어갈 데이터 배열
	String[] data = { "S급", "A급", "B급", "C급" }; // 상품 등급
	// DB 연동을 위한 준비
	static String driver = "org.mariadb.jdbc.Driver";
	static String url = "jdbc:mariadb://Localhost:3306/test";
	static String uid = "root";
	static String pwd = "281471";
	static String query = "select * from quddnr";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public Member() {
		setLayout(new BorderLayout());
		// 닫기 버튼을 누르면 메모리에서 해제
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		columnsNames = new Vector<String>();
		rowData = new Vector<Vector<String>>();

		columnsNames.add("상품 코드"); // 머리글
		columnsNames.add("상품명");
		columnsNames.add("상품 가격");
		columnsNames.add("상품 등급");
		columnsNames.add("수정 시간");
		
		// 테이블 생성, 사이즈 , 위치 배치
		table = new JTable(rowData, columnsNames);
		jsp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(jsp, BorderLayout.NORTH);
		jsp.setPreferredSize(new Dimension(480, 320)); // 테이블 크기
		//셀 크기 조절
		table.getColumn("상품 코드").setPreferredWidth(10);
		table.getColumn("상품명").setPreferredWidth(20);
		table.getColumn("상품 가격").setPreferredWidth(20);
		table.getColumn("상품 등급").setPreferredWidth(20);
		table.getColumn("수정 시간").setPreferredWidth(150);
		//테이블 높이 수정, 글씨체 수정
		table.setRowHeight(25);
		Font font = new Font("굴림", Font.BOLD, 15);
		table.setFont(font);
		
		// 테이블 컬럼 누를 때 정렬
		table.setAutoCreateRowSorter(true);
		TableRowSorter tablesorter = new TableRowSorter(table.getModel());
		table.setRowSorter(tablesorter);
		
		// 테이블 중앙 정렬
		DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
		dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcm = table.getColumnModel();
		for (int i = 0; i < tcm.getColumnCount(); i++) {
			tcm.getColumn(i).setCellRenderer(dtcr);
		}

		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		// 모든 객체 생성
		leftPanel = new JPanel();

		JLabel scode = new JLabel(" 상품 코드 : "); // 상품코드를 위한 Panel
		JLabel sname = new JLabel(" 상품명 : "); // 상품명 위한 Panel
		JLabel sprice = new JLabel(" 상품 가격 : "); // 상품 가격을 위한 Panel
		JLabel sdivision = new JLabel(" 상품 등급 : "); // 상품 종류를 위한 Panel
		Font font1 = new Font("굴림", Font.BOLD, 20);
		Font font2 = new Font("굴림", Font.BOLD, 20);
		Font font3 = new Font("굴림", Font.BOLD, 20);
		Font font4 = new Font("굴림", Font.BOLD, 20);
		scode.setFont(font1);
		sname.setFont(font2);
		sprice.setFont(font3);
		sdivision.setFont(font4);

		tf1 = new JTextField(10); // 상품 코드를 위한 TextFeild
		tf2 = new JTextField(10); // 상품명을 위한 TextFeild
		tf3 = new JTextField(10); // 상품 가격을 위한 TextFeild
		combo = new JComboBox<String>(data); // 종류를 위한 ComboBox

		panel.add(scode);
		panel.add(tf1);
		panel.add(sname);
		panel.add(tf2);
		panel.add(sprice);
		panel.add(tf3);
		panel.add(sdivision);
		panel.add(combo);

		JPanel panel2 = new JPanel();
		add(panel2, BorderLayout.SOUTH);

		b1 = new JButton("입력"); // 버튼 선언
		b2 = new JButton("수정");
		b3 = new JButton("삭제");
		b3.setBackground(Color.RED);
		// b4 = new JButton("검색");
		b5 = new JButton("종료");

		b1.setFont(font1);
		b2.setFont(font2);
		b3.setFont(font3);
		b5.setFont(font4);

		panel2.add(b1);
		panel2.add(b2);
		panel2.add(b3);
		// panel2.add(b4);
		panel2.add(b5);

		scode = new JLabel();
		sname = new JLabel();
		sprice = new JLabel();
		sdivision = new JLabel();

		Btn = new JPanel();
		// 이벤트 부착
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		// b4.addActionListener(this);
		b5.addActionListener(this);

		setTitle("재고 관리 프로그램");
		// setLocationRelativeTo(null);
		setSize(1100, 450);
		setVisible(true);

		getcon(); // 데이터 베이스에 접근 가능하도록 커넥션을 설정
	}

	public static void main(String[] args) {
		new Member();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// String data = arg0.getSource(); // 버튼의 객체, 이름을 리턴
		String value = arg0.getActionCommand(); // 버튼의 라벨값을 리턴

		if (value.equals("입력"))
			this.insertMember();

		if (value.equals("수정"))
			this.updateMember();

		if (value.equals("삭제"))
			this.deleteMember();
		if (value.equals("종료"))
			this.closeMember();
	}

	@Override // 리스트의 이벤트를 위한 메소드
	public void valueChanged(ListSelectionEvent arg0) {
		// 클릭한 데이터를 가져옴
	}

	// 데이터 베이스에 접근 가능하도록 커넥션을 설정
	public void getcon() {
		try {
			// 어느 데이터베이스를 사용할 것인지를 설정
			Class.forName(driver); // 대소문자 구문
			// 실제 데이터 베이스에 접근하기 위한 소스를 작성 == 접속완료되면 커넥션을 리턴
			con = DriverManager.getConnection(url, uid, pwd);
			System.out.println("DB 접속 완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
		getAllMember(); // 출력 메소드

		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int selection = table.getSelectedRow();

				Vector<String> vc = rowData.get(selection);
				tf1.setText(vc.get(0));
			}

		});
	}

	public void closeMember() {
		setVisible(false);
		try {
			con.close();
			System.out.println("DB 연동 해제");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("연동 해제 실패");
		}

	}

	// 입력 버튼을 누르면 실행되는 메소드
	public void insertMember() {
		// 데이터를 모두 입력 받은 후에 데이터 베이스에 연결하여 데이터를 삽입
		// 리스트에다가 상품 정보를 하나의 스트링으로 만든후에 뿌려줌
		try {
			SimpleDateFormat formatedNow1 = new SimpleDateFormat("yy년 MM월dd일 HH시mm분ss초");
			Date time = new Date();
			String time2 = formatedNow1.format(time);

			// 모든 데이터를 가져오시오
			String sql = "insert into quddnr values(?, ?, ?, ?,?)";
			// 쿼리를 날리기위한 객체를 선언
			int resultin = JOptionPane.showConfirmDialog(null, "이대로 입력할까요?", "Confirm", JOptionPane.YES_NO_OPTION);
			if (resultin == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "입력 완료");
				time = new Date();
				time2 = formatedNow1.format(time);
				System.out.println(time2);
				pstmt = con.prepareStatement(sql);
				// ?에 데이터를 입력
				pstmt.setString(1, tf1.getText()); // 상품 코드
				pstmt.setString(2, tf2.getText()); // 상품명
				pstmt.setString(3, tf3.getText()); // 상품 가격
				pstmt.setString(4, String.valueOf(combo.getSelectedItem())); // 상품 종류
				pstmt.setString(5, time2);

				pstmt.executeUpdate();

				Vector<String> v = new Vector<String>();
				v.add(tf1.getText());
				v.add(tf2.getText());
				v.add(tf3.getText());
				v.add(String.valueOf(combo.getSelectedItem()));
				v.add(time2);
				rowData.add(v);

			} else {
				JOptionPane.showMessageDialog(null, "입력 취소");
			}
			tf1.setText("");
			tf2.setText("");
			tf3.setText("");

			table.updateUI();
			// 다 쓰고 나면 자원을 닫아라
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// getAllMember();
	}

	// 출력 실행되는 메소드
	public void getAllMember() {
		try {
			// 쿼리 준비
			String sql = "select * from quddnr";
			pstmt = con.prepareStatement(sql);
			// 결과를 리턴 받기에 resultset 객체를 선언해서 받음
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Vector<String> v = new Vector<String>();
				v.add(rs.getString(1));
				v.add(rs.getString(2));
				v.add(rs.getString(3));
				v.add(rs.getString(4));
				v.add(rs.getString(5));

				rowData.add(v);

				tf1.setText("");
				tf2.setText("");
				tf3.setText("");
			}

			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		table.updateUI();
	}

	// 백터에 있는 데이터를 리스트에 부착

	// 수정 버튼을 누르면 실행되는 메소드
	// 코드 를 중심으로 상품명과 상품 금액 상품 종류 수정
	// insertMember()에서 처럼 update 구문에 물음표(?)를 이용해서 구현 가능
	public void updateMember() {
		try {

			SimpleDateFormat formatedNow1 = new SimpleDateFormat("yyyy년 MM월dd일 HH시mm분ss초");
			Date time = new Date();
			String time2 = formatedNow1.format(time);

			String sql = "update quddnr set name = ?,price = ?, division = ? ,formatedNow =? where code = ? ";
			// 쿼리를 날리기위한 객체를 선언

			int resultup = JOptionPane.showConfirmDialog(null, "진짜 바꿔?", "Confirm", JOptionPane.YES_NO_OPTION);
			if (resultup == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "바꿈ㅇㅇ");
				time = new Date();
				time2 = formatedNow1.format(time);

				System.out.println(time2);
				pstmt = con.prepareStatement(sql);

				pstmt.setString(1, tf2.getText()); // 상품명
				pstmt.setString(2, tf3.getText()); // 상품가격
				pstmt.setString(3, String.valueOf(combo.getSelectedItem())); // 상품 종류
				pstmt.setString(4, time2); // 입력 시간
				pstmt.setString(5, tf1.getText()); // 상품 코드

				pstmt.executeUpdate();

				int selection = table.getSelectedRow();
				Vector<String> v = new Vector<String>();
				v.add(tf1.getText());
				v.add(tf2.getText());
				v.add(tf3.getText());
				v.add(String.valueOf(combo.getSelectedItem()));
				v.add(time2);

				rowData.setElementAt(v, selection);
				table.updateUI();

			} else {
				JOptionPane.showMessageDialog(null, "안 바꿈");
			}
			// 쿼리실행 = 데이터를 돌려 받을때

			tf1.setText("");
			tf2.setText("");
			tf3.setText("");

			// 다 쓰면 자원을 닫아라
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("[에러]updatelunchmanage() 메소드의 SQL 오류 = " + e.getMessage());
		}
		// getAllMember(); // 출력 메소드
	}

	// 삭제 버튼을 누르면 실행되는 메소드
	// 코드를 중심으로 그 코드에 대한 모든 내용을 삭제
	// insertMember()에서 처럼 delete 구문에 물음표(?)를 이용해서 구현 가능
	public void deleteMember() {
		try {
			String sql = "delete from quddnr where code=" + "'" + tf1.getText() + "'";
			// 쿼리를 날리기위한 객체를 선언
			pstmt = con.prepareStatement(sql);
			int resultdel = JOptionPane.showConfirmDialog(null, "진짜 지워?", "Confirm", JOptionPane.YES_NO_OPTION);
			if (resultdel == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "ㅇㅋ지움");
				pstmt.executeUpdate();// 쿼리실행 = 데이터를 돌려 받을때
				int selection = table.getSelectedRow();
				rowData.remove(selection);
				table.updateUI();

			} else {
				JOptionPane.showMessageDialog(null, "취소합니다");
			}

			tf1.setText("");
			tf2.setText("");
			tf3.setText("");

			// 다 쓰면 자원을 닫아라
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// getAllMember(); // 출력 메소드
	}
}


