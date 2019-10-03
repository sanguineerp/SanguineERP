package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsBanquetStaffCategeoryMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strStaffCategeoryCode")
	private String strStaffCategeoryCode;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsBanquetStaffCategeoryMasterModel_ID(){}
	public clsBanquetStaffCategeoryMasterModel_ID(String strStaffCategeoryCode,String strClientCode){
		this.strStaffCategeoryCode=strStaffCategeoryCode;
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrStaffCategeoryCode(){
		return strStaffCategeoryCode;
	}
	public void setStrStaffCategeoryCode(String strStaffCategeoryCode){
		this. strStaffCategeoryCode = strStaffCategeoryCode;
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
		clsBanquetStaffCategeoryMasterModel_ID objModelId = (clsBanquetStaffCategeoryMasterModel_ID)obj;
		if(this.strStaffCategeoryCode.equals(objModelId.getStrStaffCategeoryCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strStaffCategeoryCode.hashCode()+this.strClientCode.hashCode();
	}

}
