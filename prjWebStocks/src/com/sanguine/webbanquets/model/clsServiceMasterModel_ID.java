package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsServiceMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strServiceCode")
	private String strServiceCode;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsServiceMasterModel_ID(){}
	public clsServiceMasterModel_ID(String strServiceCode,String strClientCode){
		this.strServiceCode=strServiceCode;
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrServiceCode(){
		return strServiceCode;
	}
	public void setStrServiceCode(String strServiceCode){
		this. strServiceCode = strServiceCode;
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this. strClientCode = strClientCode;
	}


//HashCode and Equals Funtions
	@Override
	public boolean equals(Object obj) {
		clsServiceMasterModel_ID objModelId = (clsServiceMasterModel_ID)obj;
		if(this.strServiceCode.equals(objModelId.getStrServiceCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strServiceCode.hashCode()+this.strClientCode.hashCode();
	}

}
