package com.sanguine.webbanquets.bean;

public class clsEquipmentBean{
//Variable Declaration
	private String strEquipmentCode;

	private String strEquipmentName;

	private String dteDateCreated;

	private String dteDateEdited;

	private String strUserCreated;

	private String strUserEdited;

	private String strClientCode;
	
	private String strOperational;

//Setter-Getter Methods
	public String getStrEquipmentCode(){
		return strEquipmentCode;
	}
	public void setStrEquipmentCode(String strEquipmentCode){
		this.strEquipmentCode=strEquipmentCode;
	}

	public String getStrEquipmentName(){
		return strEquipmentName;
	}
	public void setStrEquipmentName(String strEquipmentName){
		this.strEquipmentName=strEquipmentName;
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
	public String getStrOperational() {
		return strOperational;
	}
	public void setStrOperational(String strOperational) {
		this.strOperational = strOperational;
	}



}
