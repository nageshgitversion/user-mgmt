package com.morningbasket.user_management_service.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.morningbasket.user_management_service.dto.ActivateAccount;
import com.morningbasket.user_management_service.dto.Login;
import com.morningbasket.user_management_service.dto.User;
import com.morningbasket.user_management_service.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService service;

	
	
	@PostMapping("/user")
	public ResponseEntity<String> saveUser(@RequestBody User user) throws Throwable{
		
		boolean save = service.saveUser(user);
		
		if(save) {
			return new ResponseEntity<>("Data Saved Successfully",HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>("Data Not Saved",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/activateuser")
	public ResponseEntity<String> activate(@RequestBody ActivateAccount account){
		
		boolean active = service.activeAccount(account);
		
		if(active) {
			return new ResponseEntity<>("Account activated Successfully",HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Invalid Credentials",HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers(){
		
		List<User> allUsers = service.getAllUsers();
		
		return new ResponseEntity<>(allUsers,HttpStatus.OK);
		
	}
	
	@GetMapping("/getbyid{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Integer userId){
		
		User user = service.findUserByID(userId);
		
		return new ResponseEntity<>(user,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/delete/{userId}")
	public ResponseEntity<String> deleteById(@PathVariable Integer userId){
		
		boolean isdeleted = service.deleteUserById(userId);
		
		if(isdeleted) {
			return new ResponseEntity<>("Record deleted successfully",HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Record not deleted",HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@GetMapping("/statuschange/{userId}/{accStatus}")
	public ResponseEntity<String> changeStatus(@PathVariable Integer userId,@PathVariable String accStatus){
		
		boolean ischanged = service.changeAccountStatus(userId, accStatus);
		
		if(ischanged) {
			return new ResponseEntity<>("Status changed",HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Status not changed",HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	@PostMapping("/log")
	public ResponseEntity<String> login(@RequestBody Login login){
		String status = service.loginAccount(login);
		
		return new ResponseEntity<>(status,HttpStatus.OK);
		
	}
	
	@GetMapping("/forgot/{email}")
	public ResponseEntity<String> forgotPassword(@PathVariable String email) throws Throwable{
		
		String status = service.forgotPwd(email);
		
		return new ResponseEntity<>(status,HttpStatus.OK);
		
	}
	

}
