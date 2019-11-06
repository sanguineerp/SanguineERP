package com.sanguine.webclub.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsWebClubPDCModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strClientCode")
	private String strClientCode;

	public clsWebClubPDCModel_ID(){}
	public clsWebClubPDCModel_ID(String strClientCode){
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this. strClientCode = strClientCode;
	}


//HashCode and Equals Funtions
	@Override
	public boolean equals(Object obj) {
		clsWebClubPDCModel_ID objModelId = (clsWebClubPDCModel_ID)obj;
		if(this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strClientCode.hashCode();
	}

}
