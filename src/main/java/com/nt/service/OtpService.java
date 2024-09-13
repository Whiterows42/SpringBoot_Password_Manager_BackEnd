package com.nt.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.time.LocalDateTime;

import com.nt.entity.OtpEntity;
import com.nt.repo.OtpRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class OtpService {

	@Autowired
	private OtpRepo otpRepo;
	
	@Autowired
    private JavaMailSender mailSender;
	
	 public String generateOtp() {
	        return String.format("%06d", new Random().nextInt(999999));
	    }
	 
	 public void sendOtpEmail(String toEmail) throws MessagingException {
		    String otp = generateOtp();
		    
		    // Create a MimeMessage
		    MimeMessage message = mailSender.createMimeMessage();
		    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		    helper.setTo(toEmail);
		    helper.setSubject("Your OTP Code");

		    // Construct your HTML email body
		    String htmlMsg = "<!DOCTYPE html>"
		            + "<html>"
		            + "<body style='font-family: Arial, sans-serif; color: #333;'>"
		            + "<div style='max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd;'>"
		            + "<h2 style='text-align: center; color: #4CAF50;'>Your OTP Code</h2>"
		            + "<p>Hello,</p>"
		            + "<p>Your OTP code is: <strong style='font-size: 18px; color: #ff0000;'>" + otp + "</strong></p>"
		            + "<p>Please use this code within the next 5 minutes to complete your request.</p>"
		            + "<hr>"
		            + "<p style='text-align: center;'>Thank you for using our service!</p>"
		            + "</div>"
		            + "</body>"
		            + "</html>";

		    // Set the content of the email
		    helper.setText(htmlMsg, true); // `true` means we're sending HTML

		    // Send the email
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
