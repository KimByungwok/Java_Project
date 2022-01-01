package com.java.ex;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.*;

public class Login extends JFrame{
	private JPanel loginPanel = new JPanel(new GridLayout(3,2));
	private JLabel idLabel = new JLabel("아이디 : ");
	private JLabel pwLabel = new JLabel("비밀번호 : ");
	private JTextField idText = new JTextField();
	private JPasswordField pwText = new JPasswordField();
	private JButton loginbtn = new JButton("로그인");
	private JButton idpwSearchbtn = new JButton("관리자 가입");
	
	static String driver = "org.mariadb.jdbc.Driver";
	static String url = "jdbc:mysql://Localhost:3306/test";
	static String uid = "root";
	static String pwd = "281471";
	static String query = "select * from :login";
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	
	
	public Login() {		
		super("로그인 창");
		
		this.setContentPane(loginPanel);
		loginPanel.add(idLabel);
		loginPanel.add(pwLabel);
		loginPanel.add(idText);
		loginPanel.add(pwText);
		loginPanel.add(idpwSearchbtn);
		loginPanel.add(loginbtn);
		
		idLabel.setHorizontalAlignment(NORMAL);
		pwLabel.setHorizontalAlignment(NORMAL);
	
		setSize(350,150);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getcon();
	
		loginbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				// 아이디 비번 확인 테스트 코드
				String id = idText.getText().trim();
				String pw = pwText.getText().trim();
				
				if (id.length()==0 || pw.length()==0) {
					JOptionPane.showMessageDialog(null, "아이디나 비밀번호를 입력하세요","로그인 오류",
							JOptionPane.DEFAULT_OPTION);
					return;
				}
				if (id.equals("kj2302") && pw.equals("aa281471")) {
					JOptionPane.showMessageDialog(null, "로그인 성공!","성공~",
							JOptionPane.DEFAULT_OPTION);
					Member mb = new Member();
					mb.pack();
					mb.setVisible(true);
					setVisible(false);
				}
			
				else {
				JOptionPane.showMessageDialog(null, "로그인 실패!","실패",
						JOptionPane.DEFAULT_OPTION);
				idText.setText("");
				pwText.setText("");
				}
			}
		});
		
		idpwSearchbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e1) {
				
			}
		});
	}
	public static void main(String[] args) {
		new Login();
	}
	public void getcon() {
		try {
			// 어느 데이터베이스를 사용할 것인지를 설정
			Class.forName(driver); // 대소문자 구문
			// 실제 데이터 베이스에 접근하기 위한 소스를 작성 == 접속완료되면 커넥션을 리턴
			con = DriverManager.getConnection(url, uid, pwd);
			System.out.println("DB 접속 완료");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("연동 실패");
		}
	}
}
