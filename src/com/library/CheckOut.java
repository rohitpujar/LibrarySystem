package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "checkOut")
@SessionScoped
public class CheckOut {

	String bookId;
	int branchId;
	int cardNo;

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public int getBranchId() {
		return branchId;
	}

	public void setBranchId(int branchId) {
		this.branchId = branchId;
	}

	public int getCardNo() {
		return cardNo;
	}

	public void setCardNo(int cardNo) {
		this.cardNo = cardNo;
	}

	public String checkOut() {
		System.out.println("Executing method.........");
		FacesContext context = FacesContext.getCurrentInstance();
		Connection con = getConnection();
		String checkOutQuery = "insert into Book_loans (Book_id,branch_id,card_no,date_out,due_date,Date_in) "
				+ "select '"+getBookId()+"',"+getBranchId()+","+getCardNo()+",now(),ADDDATE(now(),INTERVAL 14 DAY),null "
				+ "from Dual where (select count(*) as c from book_loans where card_no="+getCardNo()+")<3;";
		
		System.out.println("CheckOut query : "+checkOutQuery);
		try {
//			PreparedStatement ps = con.prepareStatement(checkOutQuery);
			
			java.sql.Statement stmt = con.createStatement();
//			int result = ps.executeUpdate();
			int result = stmt.executeUpdate(checkOutQuery);
			System.out.println("Result : "+result);
			if(result == 1){
				context.addMessage(null, new FacesMessage("Book Successfully checked out!!"));
				return "CheckOut";
			}
		} catch (SQLException e) {
			context.addMessage(null, new FacesMessage("The user has already taken three books, no more check outs permitted!"));
			return null;
		}
		return "CheckOut";
	}

	private Connection getConnection() {
		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "vanashri");

			return con;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

}
