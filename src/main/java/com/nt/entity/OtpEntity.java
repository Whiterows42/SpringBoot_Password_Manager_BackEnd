package com.nt.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity
@Table(name = "otp_verification")
public class OtpEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	 private String email;
	    private String otp;
	    private LocalDateTime createdAt;
		public OtpEntity(long id, String email, String otp, LocalDateTime createdAt) {
			super();
			this.id = id;
			this.email = email;
			this.otp = otp;
			this.createdAt = createdAt;
		}
		
		
		
		
		public OtpEntity() {
			super();
			// TODO Auto-generated constructor stub
		}




		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
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
		public LocalDateTime getCreatedAt() {
			return createdAt;
		}
		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}
	    
	    
	    
	
	
}
