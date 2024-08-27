package com.nt.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.nt.entity.OtpEntity;
import com.nt.repo.OtpRepo;

@Service
public class OtpService {

	@Autowired
	private OtpRepo otpRepo;
	
	@Autowired
    private JavaMailSender mailSender;
	
	 public String generateOtp() {
	        return String.format("%06d", new Random().nextInt(999999));
	    }
	 
	 
	 public void sendOtpEmail(String toEmail) {
	        String otp = generateOtp();
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(toEmail);
	        message.setSubject("Your OTP Code");
	        message.setText("Your OTP code is: " + otp);
	        mailSender.send(message);

	        // Store OTP in the database with timestamp
	        OtpEntity otpEntity = new OtpEntity();
	        otpEntity.setEmail(toEmail);
	        otpEntity.setOtp(otp);
	        otpEntity.setCreatedAt(LocalDateTime.now());

	        otpRepo.save(otpEntity);
	    }
	 
	 // Scheduled job to delete OTPs older than 2 minutes
	    @Scheduled(fixedRate = 60000) // Every minute
	    public void removeExpiredOtps() {
	        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(2);
	        otpRepo.deleteByCreatedAtBefore(expiryTime);
	    }
	 
}
