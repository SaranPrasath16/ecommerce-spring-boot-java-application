package com.ecommerce.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ecommerce.util.IdGenerator;

@Document(collection="user")
@TypeAlias("User")
public class User {
	@Id
	private String userId;
	private String userName;
	@Indexed(unique = true)
	private String email;
	private String password;
	private long mobile;
	private String address;
	private boolean isMainAdmin;
	private boolean isProductAdmin;
	private boolean isOrdersAdmin;

	public boolean isOrdersAdmin() {
		return isOrdersAdmin;
	}
	public void setOrdersAdmin(boolean isOrdersAdmin) {
		this.isOrdersAdmin = isOrdersAdmin;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getMobile() {
		return mobile;
	}
	public void setMobile(long mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isMainAdmin() {
		return isMainAdmin;
	}
	public void setMainAdmin(boolean isMainAdmin) {
		this.isMainAdmin = isMainAdmin;
	}
	public boolean isProductAdmin() {
		return isProductAdmin;
	}
	public void setProductAdmin(boolean isProductAdmin) {
		this.isProductAdmin = isProductAdmin;
	}
	public User() {
		super();
        if (this.userId == null || this.userId.isEmpty()) {
            this.userId = "USER_" + IdGenerator.generateRandomNumber();
        }		
	}
	public User(String userName, String email, String password, long mobile, String address, boolean isMainAdmin,
			boolean isProductAdmin, boolean isOrdersAdmin) {
		this();
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.mobile = mobile;
		this.address = address;
		this.isMainAdmin = isMainAdmin;
		this.isProductAdmin = isProductAdmin;
		this.isOrdersAdmin = isOrdersAdmin;
	}



	

}
