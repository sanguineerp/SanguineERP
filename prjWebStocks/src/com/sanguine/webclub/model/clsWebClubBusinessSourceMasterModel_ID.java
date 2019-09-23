package com.sanguine.webclub.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsWebClubBusinessSourceMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strBusinessSrcCode")
	private String strBusinessSrcCode;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsWebClubBusinessSourceMasterModel_ID(){}
	public clsWebClubBusinessSourceMasterModel_ID(String strBusinessSrcCode,String strClientCode){
		this.strBusinessSrcCode=strBusinessSrcCode;
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrBusinessSrcCode(){
		return strBusinessSrcCode;
	}
	public void setStrBusinessSrcCode(String strBusinessSrcCode){
		this. strBusinessSrcCode = strBusinessSrcCode;
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
		clsWebClubBusinessSourceMasterModel_ID objModelId = (clsWebClubBusinessSourceMasterModel_ID)obj;
		if(this.strBusinessSrcCode.equals(objModelId.getStrBusinessSrcCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strBusinessSrcCode.hashCode()+this.strClientCode.hashCode();
	}

}
