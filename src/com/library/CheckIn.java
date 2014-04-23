package com.library;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "checkIn")
@SessionScoped
public class CheckIn {

	ArrayList<BookLoanResults> data;
	String bookId;
	String loanId;
	String cardNo;
	String firstName;
	String lastName;
	String branchId;

	public String getLoanId() {
		return loanId;
	}

	public void setLoanId(String loanId) {
		this.loanId = loanId;
	}

	public ArrayList<BookLoanResults> getData() {
		return data;
	}

	public void setData(ArrayList<BookLoanResults> data) {
		this.data = data;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
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

	public String locateBooks() {
		Connection con = getConnection();

		String checkQuery = "select Loan_id,Book_id,Branch_id,bl.Card_no,Date_out,fname,lname "
				+ "from book_loans as bl,borrower as b where b.fname like '%" + getFirstName() + "%' and b.lname like '%" + getLastName()
				+ "%' and bl.Book_id like '%" + getBookId() + "%' " + "and bl.Card_no like '%" + getCardNo()
				+ "%' and b.Card_no=bl.Card_no and Date_in IS null;";
		System.out.println("Book loan locate query : " + checkQuery);
		try {
			PreparedStatement ps = con.prepareStatement(checkQuery);
			ResultSet res = ps.executeQuery();
			data = new ArrayList<CheckIn.BookLoanResults>();
			while (res.next()) {
				BookLoanResults loans = new BookLoanResults();

				loans.bookId = res.getString("book_id");
				loans.cardNo = res.getString("card_no");
				loans.branchId = res.getString("branch_id");
				loans.fname = res.getString("fname");
				loans.lname = res.getString("lname");
				loans.loanId = res.getString("loan_id");
				loans.dateOut = res.getDate("date_out");

				data.add(loans);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(" Result size is : " + data.size());
		return "CheckIn";
	}

	public String checkIn() {
		int validate = validate();
		if(validate == 1){
		FacesContext context = FacesContext.getCurrentInstance();
		Connection con = getConnection();
		String checkInQuery = "update book_loans set Date_in=now() where Book_id='" + getBookId() + "' and Branch_id=" + getBranchId()
				+ " and loan_id=" + getLoanId() + ";";
		String updateAvailability = "update book_copies set No_of_available_copies=No_of_available_copies+1 where Book_id='" + getBookId()
				+ "' and Branch_id=" + getBranchId();
		System.out.println("Check In query : "+checkInQuery);
		System.out.println("Update availability query : "+updateAvailability);
		try {
			PreparedStatement checkInStmt = con.prepareStatement(checkInQuery);
			PreparedStatement updateAvailabilityStmt = con.prepareStatement(updateAvailability);
			int res = checkInStmt.executeUpdate();
			System.out.println("Check In result : " + res);
			if (res == 1) {
				int availabileUpdateResult = updateAvailabilityStmt.executeUpdate();
				if (availabileUpdateResult == 1) {
					context.addMessage(null, new FacesMessage("Book checked in successfully!"));
				} else {
					System.out.println("%%%%%%% Problem only in availability updation ......");
				}
			} else {
				context.addMessage(null, new FacesMessage("Could not check in, please check input!"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "CheckIn";
		}else{
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage("Please enter Book ID, Card no, Branch ID, Loan ID"));
			return null;
		}
	}

	private int validate(){
		FacesContext context = FacesContext.getCurrentInstance();
		if(getBookId().isEmpty() || getCardNo().isEmpty() || getBranchId().isEmpty() || getLoanId().isEmpty()){
//			context.addMessage(null, new FacesMessage("Please enter Book ID, Card no, Branch ID, Loan ID"));
			return 0;
		} else{
			return 1;
		}
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

	public static class BookLoanResults {

		String loanId;
		String bookId;
		String cardNo;
		Date dateOut;
		String fname;
		String lname;
		String branchId;

		public String getBranchId() {
			return branchId;
		}

		public void setBranchId(String branchId) {
			this.branchId = branchId;
		}

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
