package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import vo.Menber;


public class MenberDao {
	public Menber getmenber(String uid) throws ClassNotFoundException, SQLException
	{
		String sql = "SELECT * FROM menber WHERE \"UID\"=?";
		// 0. 드라이버 로드
		Class.forName("oracle.jdbc.driver.OracleDriver");
		// 1. 접속
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
				"wooyoung", "wooyoung");
		// 2. 실행
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, uid);
		// 3. 결과
		ResultSet rs = st.executeQuery();
		
		Menber menber = null;
		
		if(rs.next())
		{
			menber = new Menber();
			menber.setUid(rs.getString("uid"));
			menber.setPwd(rs.getString("pwd"));
			menber.setName(rs.getString("name"));
			menber.setGender(rs.getString("gender"));
			menber.setBirth(rs.getString("birth"));
			menber.setIsLunar(rs.getString("is_lunar"));
			menber.setCPhone(rs.getString("cphone"));
			menber.setEmail(rs.getString("email"));
			menber.setHabit(rs.getString("habit"));
			menber.setRegDate(rs.getDate("regdate"));
		}
		
		rs.close();
		st.close();
		con.close();
		
		return menber;
	}
	
	public int insert(Menber menber) throws ClassNotFoundException, SQLException
	{
		String sql = "INSERT INTO menber(\"UID\", \"PWD\", \"NAME\", GENDER, BIRTH, IS_LUNAR, CPHONE, EMAIL, HABIT, REGDATE) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
		// 0. 드라이버 로드
		Class.forName("oracle.jdbc.driver.OracleDriver");
		// 1. 접속
		Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl",
				"wooyoung", "wooyoung");
		// 2. 실행
		PreparedStatement st = con.prepareStatement(sql);
		st.setString(1, menber.getUid());
		st.setString(2, menber.getPwd());
		st.setString(3, menber.getName());
		st.setString(4, menber.getGender());
		st.setString(5, menber.getBirth());
		st.setString(6, menber.getIsLunar());
		st.setString(7, menber.getCPhone());
		st.setString(8, menber.getEmail());
		st.setString(9, menber.getHabit());
		
		int result = st.executeUpdate();
		
		st.close();
		con.close();
		
		return result;
	}
}
