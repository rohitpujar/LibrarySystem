package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

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

		String checkForAvailabilityQuery = "select No_of_copies,No_of_available_copies from book_copies where Book_id='" + getBookId()
				+ "' and Branch_id=" + getBranchId() + ";";
		String checkOutQuery = "insert into book_loans (Book_id,branch_id,card_no,date_out,due_date,Date_in) " + "select '" + getBookId() + "',"
				+ getBranchId() + "," + getCardNo() + ",now(),ADDDATE(now(),INTERVAL 14 DAY),null "
				+ "from Dual where (select count(*) as c from book_loans where card_no=" + getCardNo() + ")<3;";

		String updateAvailableCopiesQuery = "update book_copies set No_of_available_copies=No_of_available_copies-1 where Book_id='" + getBookId()
				+ "' and Branch_id=" + getBranchId() + ";";
		System.out.println("CheckOut query : " + checkOutQuery);
		try {
			// PreparedStatement ps = con.prepareStatement(checkOutQuery);
			java.sql.PreparedStatement availability = con.prepareStatement(checkForAvailabilityQuery);
			java.sql.Statement stmt = con.createStatement();
			java.sql.Statement updateAvailableCopies = con.createStatement();

			int availableCopies=0;
			int noOfCopies=0;
			// int result = ps.executeUpdate();
			System.out.println("Check if available query : "+checkForAvailabilityQuery);
			ResultSet availabilityResult = availability.executeQuery();
			while(availabilityResult.next()){
			availableCopies = availabilityResult.getInt("No_of_available_copies");
			noOfCopies = availabilityResult.getInt("No_of_copies");
			System.out.println("Available copies at Branch " + getBranchId() + " is : " + availableCopies);
			}
			if (availableCopies > 0 && noOfCopies > 0) {
				System.out.println(" ********* SO FAR SO GOOD ************");
				System.out.println("Update available : "+updateAvailableCopiesQuery);
				int resultForAvailableCopyUpdate = updateAvailableCopies.executeUpdate(updateAvailableCopiesQuery);
				int resultForCheckOut = stmt.executeUpdate(checkOutQuery);
				System.out.println("Result : " + resultForCheckOut);
				if (resultForCheckOut == 1) {
					context.addMessage(null, new FacesMessage("Book Successfully checked out!!"));
					return "CheckOut";
				} else{
					context.addMessage(null, new FacesMessage("User already has taken 3 books,no more permitted!!"));
					return "CheckOut";
				}
			} else {
				context.addMessage(null, new FacesMessage("This branch has no copies of the book left!!"));
			}
		} catch (MySQLIntegrityConstraintViolationException e) {
			context.addMessage(null, new FacesMessage("No such record exists"));
		} catch (SQLException e) {
			// context.addMessage(null, new
			// FacesMessage("The user has already taken three books, no more check outs permitted!"));
			e.printStackTrace();
			// return null;
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
