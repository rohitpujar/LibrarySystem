package com.library;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@ManagedBean(name = "newUser")
@SessionScoped
public class NewUser {

	String fname;
	String lname;
	String address;
	String city;
	String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	String phone;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String newUserRegistration() {
		FacesContext context = FacesContext.getCurrentInstance();
		System.out.println("Reached the start of this method...");
		Connection con = getConnection();
		try {
			if(getFname().isEmpty() | getLname().isEmpty() | getAddress().isEmpty()){
				context.addMessage(null, new FacesMessage("Values cannot be null, please enter valid data"));
				return null;
			}
			String newUser = "INSERT INTO borrower VALUES(null,'" + getFname() + "','" + getLname() + "','" + getAddress() + "','" + getCity() + "','"
					+ getState() + "','" + getPhone() + "')";
			System.out.println("Create new User Query : " + newUser);
			PreparedStatement ps = con.prepareStatement(newUser);
			ps.executeUpdate();
			return "Welcome";
		}catch(MySQLIntegrityConstraintViolationException e){
			context.addMessage(null, new FacesMessage("Borrower already exists"));
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

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
