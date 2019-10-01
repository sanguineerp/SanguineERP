package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@SuppressWarnings("serial")

public class clsEquipmentModel_ID implements Serializable{

//Variable Declaration
	@Column(name="strEquipmentCode")
	private String strEquipmentCode;

	@Column(name="strClientCode")
	private String strClientCode;
	

	public clsEquipmentModel_ID(){}
	public clsEquipmentModel_ID(String strEquipmentCode,String strClientCode){
		this.strEquipmentCode=strEquipmentCode;
		this.strClientCode=strClientCode;
		
	}

//Setter-Getter Methods
	public String getStrEquipmentCode(){
		return strEquipmentCode;
	}
	public void setStrEquipmentCode(String strEquipmentCode){
		this. strEquipmentCode = strEquipmentCode;
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
		clsEquipmentModel_ID objModelId = (clsEquipmentModel_ID)obj;
		if(this.strEquipmentCode.equals(objModelId.getStrEquipmentCode())&& this.strClientCode.equals(objModelId.getStrClientCode())){
			return true;
		}
		else{
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strEquipmentCode.hashCode()+this.strClientCode.hashCode();
	}
	

}
