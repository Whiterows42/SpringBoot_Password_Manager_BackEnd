package com.nt.dto;

public class OtpServiceDto {

	private String email;

	private String otp;

	public OtpServiceDto(String email, String otp) {
		super();
		this.email = email;
		this.otp = otp;
	}

	public OtpServiceDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

}
