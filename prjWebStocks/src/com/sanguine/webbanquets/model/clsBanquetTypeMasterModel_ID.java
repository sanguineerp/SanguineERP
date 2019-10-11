package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsBanquetTypeMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strBanquetTypeCode")
	private String strBanquetTypeCode;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsBanquetTypeMasterModel_ID(){}
	public clsBanquetTypeMasterModel_ID(String strBanquetTypeCode,String strClientCode){
		this.strBanquetTypeCode=strBanquetTypeCode;
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrBanquetTypeCode(){
		return strBanquetTypeCode;
	}
	public void setStrBanquetTypeCode(String strBanquetTypeCode){
		this. strBanquetTypeCode = strBanquetTypeCode;
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
		clsBanquetTypeMasterModel_ID objModelId = (clsBanquetTypeMasterModel_ID)obj;
		if(this.strBanquetTypeCode.equals(objModelId.getStrBanquetTypeCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strBanquetTypeCode.hashCode()+this.strClientCode.hashCode();
	}

}
