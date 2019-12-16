package com.sanguine.webpms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsHouseKeepingMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strHouseKeepCode")
	private String strHouseKeepCode;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsHouseKeepingMasterModel_ID(){}
	public clsHouseKeepingMasterModel_ID(String strHouseKeepCode,String strClientCode){
		this.strHouseKeepCode=strHouseKeepCode;
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrHouseKeepCode(){
		return strHouseKeepCode;
	}
	public void setStrHouseKeepCode(String strHouseKeepCode){
		this. strHouseKeepCode = strHouseKeepCode;
	}

	

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this. strClientCode = strClientCode;
	}




}
