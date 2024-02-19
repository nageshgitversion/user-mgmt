package com.morningbasket.user_management_service.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.morningbasket.user_management_service.dto.ActivateAccount;
import com.morningbasket.user_management_service.dto.Login;
import com.morningbasket.user_management_service.dto.User;
import com.morningbasket.user_management_service.entity.UserMaster;
import com.morningbasket.user_management_service.repository.UserRepository;
import com.morningbasket.user_management_service.utility.MailCommunication;

@Service
public class UserServiceImpl implements UserService{
	
	
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private MailCommunication mail;

	@Override
	public boolean saveUser(User user) throws Throwable {
		
		Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
		
		logger.info("User data entry started");
		UserMaster userMaster = new UserMaster();
		
		BeanUtils.copyProperties(user, userMaster);
		
		
		
		userMaster.setAccStatus("In-Active");
		
		logger.info("Default account status fixed to In-Active");
		userMaster.setPassword(generateRandomPwd());
		
		logger.info("Password Generated Successfully");
		userMaster.setCreatedBy("admin");
	    userMaster.setUpdatedBy("admin");
		
		UserMaster save = repo.save(userMaster);
		
		String subject="Your Registration has been Successful";
		String fileName = "recover-pwd.txt";
		String body = readEmailBody(save.getFullName(),save.getPassword(),fileName);
		
		mail.sendMail(user.getEmailId(), subject, body);
		
		logger.info("Mail sent to user with details");
		return save.getUserId()!=null;
	}

	@Override
	public List<User> getAllUsers() {
		
		List<User> user = new ArrayList<>();
		List<UserMaster> entity = repo.findAll();
		BeanUtils.copyProperties(entity, user);
		
		return user;
	}

	@Override
	public User findUserByID(Integer userId) {
		Optional<UserMaster> findById = repo.findById(userId);
		
		
		if(findById.isPresent()) {
			User user = new User();
			UserMaster userMaster2 = findById.get();
			BeanUtils.copyProperties(userMaster2, user);
			return user;
		}
		return null;
	}

	@Override
	public boolean deleteUserById(Integer userId) {
		
		try {
			repo.deleteById(userId);
			return true;
		}catch(Exception e) {
			
			e.printStackTrace();	
		}	
		
		return false;
	}

	@Override
	public boolean changeAccountStatus(Integer userId, String accStatus) {
		Optional<UserMaster> findById = repo.findById(userId);
		
		
		if(findById.isPresent()) {
			UserMaster userMaster = findById.get();
			userMaster.setAccStatus(accStatus);
			repo.save(userMaster);
			return true;
		}
		return false;
	}
	
	
	

	@Override
	public boolean activeAccount(ActivateAccount activate) {
		UserMaster userMaster = new UserMaster();
		userMaster.setEmailId(activate.getEmailId());
		userMaster.setPassword(activate.getTemPwd());
		
		Example<UserMaster> of = Example.of(userMaster);
		
		List<UserMaster> findAll = repo.findAll(of);
		
		
		if(findAll.isEmpty()) {
			return false;
		}else {
			UserMaster userMaster2 = findAll.get(0);
			userMaster2.setAccStatus("Active");
			userMaster2.setPassword(activate.getNewPwd());
			repo.save(userMaster2);
			return true;
		}
	}

	@Override
	public String loginAccount(Login login) {
		UserMaster userMaster = new UserMaster();
		userMaster.setEmailId(login.getEmailId());
		userMaster.setPassword(login.getPassword());
		
		Example<UserMaster> of = Example.of(userMaster);
		List<UserMaster> findAll = repo.findAll(of);
		
		if(findAll.isEmpty()) {
			return "Invalid Credentials";
		}else {
			UserMaster userMaster2 = findAll.get(0);
			if(userMaster2.getAccStatus().equals("Active")) {
				return "Logged in Successfully";
			}else {
				return "Account not Activated";
			}
			
		}
		
	}

	@Override
	public String forgotPwd(String emailId) throws Throwable {
		UserMaster entity = repo.findByEmailId(emailId);
		
		if(entity == null) {
			return "No Data found";
		}
			String fileName = "registration.txt";
			String subject = "recover passowrd";
			String body = readEmailBody(entity.getFullName(),entity.getPassword(),fileName);
			boolean sendMail = mail.sendMail(emailId, subject, body);
		if(sendMail) {
			return "password sent to your mail succeessfully";
		}
		return null;
	}
	
	
	
	private String generateRandomPwd() {
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String alphan = "abcdefghijklmnopqrstuvwxyz";
		String numbers = "0123456789";
		
		String alphaNumeric = alpha+alphan+numbers;
		StringBuilder sb = new StringBuilder();
		
		Random random = new Random();
		Integer length =6;
		
		for(int i=0;i<length;i++) {
			int index = random.nextInt(alphaNumeric.length());
			char charAt = alphaNumeric.charAt(index);
			sb.append(charAt);
		}
		return sb.toString();
	}
	
	public String readEmailBody(String fullName,String pwd,String fileName) throws Throwable {
		
		String url = "";
		
		String mailBody = null;
		
		FileReader file = new FileReader(fileName);
		BufferedReader br = new BufferedReader(file);
		
		StringBuffer bf = new StringBuffer();
		
		String readLine = br.readLine();
		while(readLine!=null) {
			bf.append(readLine);
			
		 readLine = br.readLine();
		}
		br.close();
		
		mailBody = bf.toString();
		
		mailBody =mailBody.replace("{FULLNAME}", fullName);
		mailBody=mailBody.replace("{TEMP-PWD}", pwd);
		mailBody=mailBody.replace("{URL}", url);
		mailBody=mailBody.replace("{PWD}", pwd);
		
		return mailBody;
	}

	

}
