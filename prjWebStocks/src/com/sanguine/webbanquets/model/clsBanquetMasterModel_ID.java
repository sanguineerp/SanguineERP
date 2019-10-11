package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsBanquetMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strBanquetCode")
	private String strBanquetCode;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsBanquetMasterModel_ID(){}
	public clsBanquetMasterModel_ID(String strBanquetCode,String strClientCode){
		this.strBanquetCode=strBanquetCode;
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrBanquetCode(){
		return strBanquetCode;
	}
	public void setStrBanquetCode(String strBanquetCode){
		this. strBanquetCode = strBanquetCode;
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
		clsBanquetMasterModel_ID objModelId = (clsBanquetMasterModel_ID)obj;
		if(this.strBanquetCode.equals(objModelId.getStrBanquetCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strBanquetCode.hashCode()+this.strClientCode.hashCode();
	}

}
