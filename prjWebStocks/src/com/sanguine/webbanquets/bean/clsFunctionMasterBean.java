package com.sanguine.webbanquets.bean;

public class clsFunctionMasterBean{
//Variable Declaration
	private String strFunctionCode;

	private String strFunctionName;

	private String strOperationalYN;

	private String strPropertyCode;

	private String strClientCode;

	private String strUserCreated;

	private String strUserEdited;

	private String strDateCreated;

	private String strDateEdited;
	
	private long intFId;

//Setter-Getter Methods
	public String getStrFunctionCode(){
		return strFunctionCode;
	}
	public void setStrFunctionCode(String strFunctionCode){
		this.strFunctionCode=strFunctionCode;
	}

	public String getStrFunctionName(){
		return strFunctionName;
	}
	public void setStrFunctionName(String strFunctionName){
		this.strFunctionName=strFunctionName;
	}

	public String getStrOperationalYN(){
		return strOperationalYN;
	}
	public void setStrOperationalYN(String strOperationalYN){
		this.strOperationalYN=strOperationalYN;
	}

	public String getStrPropertyCode(){
		return strPropertyCode;
	}
	public void setStrPropertyCode(String strPropertyCode){
		this.strPropertyCode=strPropertyCode;
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this.strClientCode=strClientCode;
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

	public String getStrDateCreated(){
		return strDateCreated;
	}
	public void setStrDateCreated(String strDateCreated){
		this.strDateCreated=strDateCreated;
	}

	public String getStrDateEdited(){
		return strDateEdited;
	}
	public void setStrDateEdited(String strDateEdited){
		this.strDateEdited=strDateEdited;
	}
	public long getIntFId() {
		return intFId;
	}
	public void setIntFId(long intFId) {
		this.intFId = intFId;
	}



}
