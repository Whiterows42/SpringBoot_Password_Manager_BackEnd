package com.nt.cntrl;

import org.springframework.web.bind.annotation.RestController;

import com.nt.entity.PasswordManager;
import com.nt.service.PasswordMangerService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;


@CrossOrigin("*")
@RestController
@RequestMapping("/api/pass")
public class PasswordController {

	@Autowired
	private PasswordMangerService passService;
	
	@PostMapping("/store_pass_req")
	public ResponseEntity<PasswordManager> getMethodName(@RequestBody PasswordManager pass) {
		try {
			boolean isStore = passService.storeNewPassword(pass);

			if (isStore) {

				return ResponseEntity.ok(pass);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pass);
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pass);
	}

	@GetMapping("/all_recored/{page_index}/{email}")
	public ResponseEntity<Page<PasswordManager>> getAllPasswordsEntity(@PathVariable int page_index  , @PathVariable String email) {
		Page<PasswordManager> list = null;
		try {
			list = passService.allPassManagers(email , page_index);
			if (list.isEmpty()) {

				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(list);
			}

		return	ResponseEntity.ok().body(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(list);
		}
//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(list);

	}

	@GetMapping("/getrecored/{id}")
	public ResponseEntity<PasswordManager> getMethodName(@PathVariable int id) {

		try {
			PasswordManager p = passService.findrecoredByid(id);
			if (p != null) {
				return ResponseEntity.ok().body(p);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteRecoredbyId(@PathVariable int id) {

		try {
			
			 passService.isDeletebyId(id);
			
			return ResponseEntity.ok("Deleted Successfully");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

//		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	@PutMapping("update_recored/{id}")
	public ResponseEntity<PasswordManager> updateRecored(@RequestBody PasswordManager p, @PathVariable int id) {
		//TODO: process PUT request
		PasswordManager pass = null;
		try {
			
		pass =	passService.update(p, id);
			if ( pass != null) {
				return ResponseEntity.ok(pass);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pass);
			
		} catch (Exception e) {
			// TODO: handle exception
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pass);
		}
	}
	

}
