package com.nt.cntrl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nt.dto.OtpServiceDto;
import com.nt.entity.OtpEntity;
import com.nt.entity.User;
import com.nt.models.JwtRequest;
import com.nt.models.JwtResponse;
import com.nt.repo.OtpRepo;
import com.nt.security.JwtHelper;
import com.nt.service.OtpService;
import com.nt.service.UserService;

import jakarta.mail.MessagingException;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@CrossOrigin("*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private UserService userService;
    
    @Autowired
	private OtpRepo otpRepo;

    
    @Autowired
	private OtpService otpService;
    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) throws MessagingException {

        logger.info("Attempting to authenticate user: {}", request.getEmail());

        
        this.doAuthenticate(request.getEmail(), request.getPassword());
        
//       boolean isauthenticated = userService.verifyUserEntity(request.getEmail(), request.getPassword());
//       
//       if (!isauthenticated) {
//		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//	}
       
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.helper.generateToken(userDetails);
        this.otpService.sendOtpEmail(request.getEmail());
        JwtResponse response = new JwtResponse.Builder()
                .setJwtToken(token)
                .setUsername(userDetails.getUsername())
                .build();

        logger.info("User authenticated successfully: {}", userDetails.getUsername());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed for user: {}", email);
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>("Credentials Invalid!!", HttpStatus.UNAUTHORIZED);
    }
    
    @PostMapping("/create-user")
    public ResponseEntity<String> postMethodName(@RequestBody User entity) {
  
    	 logger.info("User creation request received");
    	String user = this.userService.createUser(entity);
        
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/forgotten-password")	
    public ResponseEntity<String> putMethodName(@RequestParam String email) {
    	
    	try {
			
    		this.otpService.sendOtpEmail(email);
    		return ResponseEntity.ok("Otp Send");
		} catch (Exception e) {
		e.printStackTrace();
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
    }

	@PostMapping("/verify-otp")
	public ResponseEntity<String> verifyOtp(@RequestBody OtpServiceDto otpServiceDto) {
		
		
	    try {
	        List<OtpEntity> otpEntities = otpRepo.findByEmail(otpServiceDto.getEmail());

	        if (otpEntities.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid OTP or OTP expired!");
	        }

	        for (OtpEntity otpEntity : otpEntities) {
	            if (otpEntity.getOtp().equals(otpServiceDto.getOtp())) {
	                if (otpEntity.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(2))) {
	                	this.otpService.removeExpiredOtps();
	                    return ResponseEntity.status(HttpStatus.GONE).body("OTP expired!");
	                }
	                
	                return ResponseEntity.ok().body("OTP verified successfully!");
	            }
	        }

	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid OTP or OTP expired!");

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
	    }
	}

    
}
