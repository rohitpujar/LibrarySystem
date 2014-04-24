package com.library;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "searchBook")
@SessionScoped
public class SearchBooks {

	ArrayList<SearchResults> data;

	public ArrayList<SearchResults> getData() {
		return data;
	}

	public void setData(ArrayList<SearchResults> data) {
		this.data = data;
	}

	String bookId;
	String title;
	String authorName;
	// adding additional columns required to display the query results
	String branchId;
	String noOfCopies;
	String availableCopies;

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getNoOfCopies() {
		return noOfCopies;
	}

	public void setNoOfCopies(String noOfCopies) {
		this.noOfCopies = noOfCopies;
	}

	public String getAvailableCopies() {
		return availableCopies;
	}

	public void setAvailableCopies(String availableCopies) {
		this.availableCopies = availableCopies;
	}

	public ArrayList<SearchResults> searchBooks() {
		data = new ArrayList<SearchResults>();
		FacesContext context = FacesContext.getCurrentInstance();
		Connection con = getConnection();
		String searchQuery = "select  out1.book_id,   out1.title,   out1.author_name,   bc.branch_id,   bc.no_of_copies,bc.No_of_available_copies from    (select    b.book_id, b.title, ba.author_name   from   book as b, book_authors as ba   where       ba.book_id = b.book_Id   and b.book_Id like '%"
				+ getBookId()
				+ "%'    and b.title like '%"
				+ getTitle()
				+ "%'     and ba.author_name like '%"
				+ getAuthorName()
				+ "%' group by b.book_id) as out1,   book_copies as bc where   out1.book_id = bc.book_id;";
		try {
			System.out.println("Executing search Query : " + searchQuery);
			PreparedStatement ps = con.prepareStatement(searchQuery);

			ResultSet res = ps.executeQuery();
			while (res.next()) {
				SearchResults sr = new SearchResults();
				// System.out.println(res.getString("title"));
				// System.out.println(res.getString("author_name"));

				sr.bookId = res.getString("book_id");
				sr.title = res.getString("title");
				sr.authorName = res.getString("author_name");
				sr.branchId = res.getString("branch_id");
				sr.noOfCopies = res.getString("no_of_copies");
				sr.availableCopies = res.getString("no_of_available_copies");
				data.add(sr);
			}
			System.out.println("ArrayList Data size is : " + data.size());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
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

	public static class SearchResults {
		// this class is to store the resultset

		String bookId;
		String title;
		String authorName;
		String branchId;
		String noOfCopies;
		String availableCopies;

		public String getBookId() {
			return bookId;
		}

		public void setBookId(String bookId) {
			this.bookId = bookId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAuthorName() {
			return authorName;
		}

		public void setAuthorName(String authorName) {
			this.authorName = authorName;
		}

		public String getBranchId() {
			return branchId;
		}

		public void setBranchId(String branchId) {
			this.branchId = branchId;
		}

		public String getNoOfCopies() {
			return noOfCopies;
		}

		public void setNoOfCopies(String noOfCopies) {
			this.noOfCopies = noOfCopies;
		}

		public String getAvailableCopies() {
			return availableCopies;
		}

		public void setAvailableCopies(String availableCopies) {
			this.availableCopies = availableCopies;
		}

	}

}
