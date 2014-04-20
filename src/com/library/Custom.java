package com.library;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "customBean")
@SessionScoped
public class Custom {

	private String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private boolean showPassword = false;

	public void setShowPassword(boolean showPassword) {
		this.showPassword = showPassword;
	}

	public boolean isShowPassword() {
		return showPassword;
	}
}
