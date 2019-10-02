package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsMenuHeadMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strMenuHeadCode")
	private String strMenuHeadCode;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsMenuHeadMasterModel_ID(){}
	public clsMenuHeadMasterModel_ID(String strMenuHeadCode,String strClientCode){
		this.strMenuHeadCode=strMenuHeadCode;
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrMenuHeadCode(){
		return strMenuHeadCode;
	}
	public void setStrMenuHeadCode(String strMenuHeadCode){
		this. strMenuHeadCode = strMenuHeadCode;
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
		clsMenuHeadMasterModel_ID objModelId = (clsMenuHeadMasterModel_ID)obj;
		if(this.strMenuHeadCode.equals(objModelId.getStrMenuHeadCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strMenuHeadCode.hashCode()+this.strClientCode.hashCode();
	}

}
