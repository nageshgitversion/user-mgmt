package com.morningbasket.user_management_service.service;

import java.util.List;

import com.morningbasket.user_management_service.dto.ActivateAccount;
import com.morningbasket.user_management_service.dto.Login;
import com.morningbasket.user_management_service.dto.User;

public interface UserService {
	
	
	public boolean saveUser(User user) throws Throwable;
	
	public List<User> getAllUsers();
	
	public User findUserByID(Integer userId);
	
	public boolean deleteUserById(Integer userId);
	
	public boolean changeAccountStatus(Integer userId,String accStatus);
	
	public boolean activeAccount(ActivateAccount activate);
	
	public String loginAccount(Login login);
	
	public String forgotPwd(String emailId) throws Throwable;
	
	

	

}
