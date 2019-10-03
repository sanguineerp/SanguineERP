package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsBanquetStaffMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strStaffCode")
	private String strStaffCode;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsBanquetStaffMasterModel_ID(){}
	public clsBanquetStaffMasterModel_ID(String strStaffCode,String strClientCode){
		this.strStaffCode=strStaffCode;
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrStaffCode(){
		return strStaffCode;
	}
	public void setStrStaffCode(String strStaffCode){
		this. strStaffCode = strStaffCode;
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
		clsBanquetStaffMasterModel_ID objModelId = (clsBanquetStaffMasterModel_ID)obj;
		if(this.strStaffCode.equals(objModelId.getStrStaffCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strStaffCode.hashCode()+this.strClientCode.hashCode();
	}

}
