package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsBanquetWeekendMasterModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strDayNo")
	private String strDayNo;

	@Column(name="strClientCode")
	private String strClientCode;

	public clsBanquetWeekendMasterModel_ID(){}
	public clsBanquetWeekendMasterModel_ID(String strClientCode){		
		this.strClientCode=strClientCode;
	}

//Setter-Getter Methods
	public String getStrDayNo(){
		return strDayNo;
	}
	public void setStrDayNo(String strDayNo){
		this. strDayNo = strDayNo;
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
		clsBanquetWeekendMasterModel_ID objModelId = (clsBanquetWeekendMasterModel_ID)obj;
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
