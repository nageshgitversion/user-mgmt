package com.morningbasket.user_management_service.dto;

public class ActivateAccount {
	
	private String emailId;
	private String temPwd;
	private String newPwd;
	private String confirmPwd;
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getTemPwd() {
		return temPwd;
	}
	public void setTemPwd(String temPwd) {
		this.temPwd = temPwd;
	}
	public String getNewPwd() {
		return newPwd;
	}
	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}
	public String getConfirmPwd() {
		return confirmPwd;
	}
	public void setConfirmPwd(String confirmPwd) {
		this.confirmPwd = confirmPwd;
	}

}
