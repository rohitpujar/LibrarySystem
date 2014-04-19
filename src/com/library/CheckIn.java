package com.library;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "checkIn")
@SessionScoped
public class CheckIn {

	ArrayList<BookLoanResults> data;
	int bookId;
	int cardNo;
	String firstName;
	String lastName;

	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public int getCardNo() {
		return cardNo;
	}

	public void setCardNo(int cardNo) {
		this.cardNo = cardNo;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void checkIn() {
		Connection con = getConnection();
	

		String checkQuery = "select Loan_id,Book_id,bl.Card_no,Date_out,fname,lname "
				+ "from book_loans as bl,borrower as b where b.fname like '%"+getFirstName()+"%' and b.lname like '%"+getLastName()+"%' and bl.Book_id like '%"+getBookId()+"%' "
				+ "and bl.Card_no like '%"+getCardNo()+"%' and b.Card_no=bl.Card_no;";
		System.out.println("Book loan locate query : "+checkQuery);
		try {
			PreparedStatement ps = con.prepareStatement(checkQuery);
			ResultSet res = ps.executeQuery();
			data = new ArrayList<CheckIn.BookLoanResults>();
			while(res.next()){
				BookLoanResults loans = new BookLoanResults();
				
				loans.bookId = res.getString("book_id");
				loans.cardNo = res.getString("card_no");
				loans.fname = res.getString("fname");
				loans.lname = res.getString("lname");
				loans.loanId = res.getString("loan_id");
				loans.dateOut = res.getDate("date_out");
				
				data.add(loans);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(" Result size is : "+data.size());
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

	public static class BookLoanResults{
		
		String loanId;
		String bookId;
		String cardNo;
		Date dateOut;
		String fname;
		String lname;
		
		public String getLoanId() {
			return loanId;
		}
		public void setLoanId(String loanId) {
			this.loanId = loanId;
		}
		public String getBookId() {
			return bookId;
		}
		public void setBookId(String bookId) {
			this.bookId = bookId;
		}
		public String getCardNo() {
			return cardNo;
		}
		public void setCardNo(String cardNo) {
			this.cardNo = cardNo;
		}
		
		
		public Date getDateOut() {
			return dateOut;
		}
		public void setDateOut(Date dateOut) {
			this.dateOut = dateOut;
		}
		public String getFname() {
			return fname;
		}
		public void setFname(String fname) {
			this.fname = fname;
		}
		public String getLname() {
			return lname;
		}
		public void setLname(String lname) {
			this.lname = lname;
		}
		
		
	}
}

