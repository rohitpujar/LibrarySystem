package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "fines")
@SessionScoped
public class BookFines {

	int cardNo;
	int fineAmount;
	int loan_id;
	boolean paid;

	public int getLoan_id() {
		return loan_id;
	}

	public void setLoan_id(int loan_id) {
		this.loan_id = loan_id;
	}

	public int getCardNo() {
		return cardNo;
	}

	public void setCardNo(int cardNo) {
		this.cardNo = cardNo;
	}

	public int getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(int fineAmount) {
		this.fineAmount = fineAmount;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(boolean paid) {
		this.paid = paid;
	}

	public void getFines() {

	}

	public void insertIntoFinesFromBookLoans() {

		Connection con = getConnection();
		String insertQuery = "INSERT into book_fines(loan_id) SELECT bl.loan_id FROM book_loans as bl where bl.loan_id not in  (select f.loan_id from book_fines as f);";
		try {
			System.out.println("&**** TRIGGERED INSERT *******");
			PreparedStatement insertStmt = con.prepareStatement(insertQuery);
			int res = insertStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void calculateFines() {
		insertIntoFinesFromBookLoans();
		Connection con = getConnection();
		String tuplesWithPaid1 = "select loan_id,paid from book_fines where paid=1";
		PreparedStatement stmtPaid1;
		try {
			stmtPaid1 = con.prepareStatement(tuplesWithPaid1);

			ResultSet res1 = stmtPaid1.executeQuery();

			while (res1.next()) {
				int loanID = Integer.parseInt(res1.getString("loan_id"));
				String updateFor1 = "update book_fines set fine_amt = (select DATEDIFF((select date_in from book_loans where loan_id ="
						+ loanID
						+ "), (select  date_out from book_loans where loan_id = "
						+ loanID
						+ ")) * 0.25) where loan_id = "
						+ loanID
						+ ";}";
			
				PreparedStatement stmtupdate1 = con
						.prepareStatement(updateFor1);
				stmtupdate1.executeUpdate();
			}
			System.out.println("Update for 1 done");

			String tuplesWithPaid0 = "select loan_id,paid from book_fines where paid=0";
			PreparedStatement stmtPaid0 = con.prepareStatement(tuplesWithPaid0);
			ResultSet res2 = stmtPaid0.executeQuery();

			
			while (res2.next()) {

				int loanID = Integer.parseInt(res2.getString("loan_id"));

				String fineQuery = "update book_fines set fine_amt = (select DATEDIFF((select date_in from book_loans where loan_id = "
						+ loanID
						+ "),(select date_out from book_loans where loan_id = "
						+ loanID + ")) * 0.25)where loan_id = " + loanID + ";";
				PreparedStatement prep = con.prepareStatement(fineQuery);
				prep.executeUpdate();

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private Connection getConnection() {

		Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			con = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/library", "root", "vanashri");

			return con;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;

	}

}

class FineData {

}
