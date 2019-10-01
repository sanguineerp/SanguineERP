package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsFunctionMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strFunctionCode")
	private String strFunctionCode;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsFunctionMasterModel_ID(){}
	public clsFunctionMasterModel_ID(String strFunctionCode,String strClientCode){
		this.strFunctionCode=strFunctionCode;
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrFunctionCode(){
		return strFunctionCode;
	}
	public void setStrFunctionCode(String strFunctionCode){
		this. strFunctionCode = strFunctionCode;
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
		clsFunctionMasterModel_ID objModelId = (clsFunctionMasterModel_ID)obj;
		if(this.strFunctionCode.equals(objModelId.getStrFunctionCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strFunctionCode.hashCode()+this.strClientCode.hashCode();
	}

}
