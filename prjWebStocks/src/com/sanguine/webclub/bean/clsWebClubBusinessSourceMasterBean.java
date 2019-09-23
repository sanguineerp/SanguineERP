package com.sanguine.webclub.bean;

public class clsWebClubBusinessSourceMasterBean{
//Variable Declaration
	private String strBusinessSrcCode;

	private String strBusinessSrcName;

	private double dblPercent;

	private String dteDateCreated;

	private String dteDateEdited;

	private String strUserCreated;

	private String strUserEdited;

	private String strClientCode;

//Setter-Getter Methods
	public String getStrBusinessSrcCode(){
		return strBusinessSrcCode;
	}
	public void setStrBusinessSrcCode(String strBusinessSrcCode){
		this.strBusinessSrcCode=strBusinessSrcCode;
	}

	public String getStrBusinessSrcName(){
		return strBusinessSrcName;
	}
	public void setStrBusinessSrcName(String strBusinessSrcName){
		this.strBusinessSrcName=strBusinessSrcName;
	}

	public double getDblPercent(){
		return dblPercent;
	}
	public void setDblPercent(double dblPercent){
		this.dblPercent=dblPercent;
	}

	public String getDteDateCreated(){
		return dteDateCreated;
	}
	public void setDteDateCreated(String dteDateCreated){
		this.dteDateCreated=dteDateCreated;
	}

	public String getDteDateEdited(){
		return dteDateEdited;
	}
	public void setDteDateEdited(String dteDateEdited){
		this.dteDateEdited=dteDateEdited;
	}

	public String getStrUserCreated(){
		return strUserCreated;
	}
	public void setStrUserCreated(String strUserCreated){
		this.strUserCreated=strUserCreated;
	}

	public String getStrUserEdited(){
		return strUserEdited;
	}
	public void setStrUserEdited(String strUserEdited){
		this.strUserEdited=strUserEdited;
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this.strClientCode=strClientCode;
	}



}
