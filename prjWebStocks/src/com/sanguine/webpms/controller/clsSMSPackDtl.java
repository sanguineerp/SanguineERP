package com.sanguine.webpms.controller;

public class clsSMSPackDtl {

	
	private String strUserId;
    private String strPassword;
    private String strSenderId;
    private String strSMSPack;
    
    
    
	public clsSMSPackDtl() {
		
	}



	public clsSMSPackDtl(String strUserId, String strPassword,
			String strSenderId, String strSMSPack) {
		
		this.strUserId = strUserId;
		this.strPassword = strPassword;
		this.strSenderId = strSenderId;
		this.strSMSPack = strSMSPack;
	}



	public String getStrUserId() {
		return strUserId;
	}



	public void setStrUserId(String strUserId) {
		this.strUserId = strUserId;
	}



	public String getStrPassword() {
		return strPassword;
	}



	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}



	public String getStrSenderId() {
		return strSenderId;
	}



	public void setStrSenderId(String strSenderId) {
		this.strSenderId = strSenderId;
	}



	public String getStrSMSPack() {
		return strSMSPack;
	}



	public void setStrSMSPack(String strSMSPack) {
		this.strSMSPack = strSMSPack;
	}
    
	
	
    
}
