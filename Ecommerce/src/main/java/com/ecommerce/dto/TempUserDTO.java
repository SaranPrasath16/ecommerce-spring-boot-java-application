package com.ecommerce.dto;

import java.time.LocalDateTime;

public class TempUserDTO {
    private String email;
    private String encodedPassword;
    private String userName;
    private long mobile;
    private String address;
    private LocalDateTime otpGeneratedTime;
    
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEncodedPassword() {
		return encodedPassword;
	}
	public void setEncodedPassword(String encodedPassword) {
		this.encodedPassword = encodedPassword;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public LocalDateTime getOtpGeneratedTime() {
		return otpGeneratedTime;
	}
	public void setOtpGeneratedTime(LocalDateTime otpGeneratedTime) {
		this.otpGeneratedTime = otpGeneratedTime;
	}

	public TempUserDTO(String email, String encodedPassword, String userName, long mobile, String address,
			LocalDateTime otpGeneratedTime) {
		super();
		this.email = email;
		this.encodedPassword = encodedPassword;
		this.userName = userName;
		this.mobile = mobile;
		this.address = address;
		this.otpGeneratedTime = otpGeneratedTime;
	}
	public TempUserDTO() {
		super();
	}

}
