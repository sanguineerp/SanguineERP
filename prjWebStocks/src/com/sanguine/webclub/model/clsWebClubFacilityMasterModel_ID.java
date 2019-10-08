package com.sanguine.webclub.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsWebClubFacilityMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strFacilityCode")
	private String strFacilityCode;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsWebClubFacilityMasterModel_ID(){}
	public clsWebClubFacilityMasterModel_ID(String strFacilityCode,String strClientCode){
		this.strFacilityCode=strFacilityCode;
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrFacilityCode(){
		return strFacilityCode;
	}
	public void setStrFacilityCode(String strFacilityCode){
		this. strFacilityCode = strFacilityCode;
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
		clsWebClubFacilityMasterModel_ID objModelId = (clsWebClubFacilityMasterModel_ID)obj;
		if(this.strFacilityCode.equals(objModelId.getStrFacilityCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strFacilityCode.hashCode()+this.strClientCode.hashCode();
	}

}
