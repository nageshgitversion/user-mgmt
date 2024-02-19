package com.morningbasket.user_management_service.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.morningbasket.user_management_service.entity.UserMaster;

public interface UserRepository extends JpaRepository<UserMaster, Serializable>{
	
	
	public UserMaster findByEmailId(String email);

}
