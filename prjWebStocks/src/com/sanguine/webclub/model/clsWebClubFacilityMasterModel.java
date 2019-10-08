package com.sanguine.webclub.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="tblfacilitymaster")
@IdClass(clsWebClubFacilityMasterModel_ID.class)

public class clsWebClubFacilityMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsWebClubFacilityMasterModel(){}

	public clsWebClubFacilityMasterModel(clsWebClubFacilityMasterModel_ID objModelID){
		strFacilityCode = objModelID.getStrFacilityCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strFacilityCode",column=@Column(name="strFacilityCode")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strFacilityCode")
	private String strFacilityCode;

	@Column(name="strFacilityName")
	private String strFacilityName;

	@Column(name="dteDateCreated")
	private String dteDateCreated;

	@Column(name="dteDateEdited")
	private String dteDateEdited;

	@Column(name="strUserCreated")
	private String strUserCreated;

	@Column(name="strUserEdited")
	private String strUserEdited;

	@Column(name="strClientCode")
	private String strClientCode;

	@Column(name="strOperationalNY")
	private String strOperationalNY;

	@Column(name = "intGId", updatable = false)
	private long intGId;
	
//Setter-Getter Methods
	public String getStrFacilityCode(){
		return strFacilityCode;
	}
	public void setStrFacilityCode(String strFacilityCode){
		this. strFacilityCode = (String) setDefaultValue( strFacilityCode, "NA");
	}

	public String getStrFacilityName(){
		return strFacilityName;
	}
	public void setStrFacilityName(String strFacilityName){
		this. strFacilityName = (String) setDefaultValue( strFacilityName, "NA");
	}

	public String getDteDateCreated(){
		return dteDateCreated;
	}
	public void setDteDateCreated(String dteDateCreated){
		this.dteDateCreated=dteDateCreated;
	}

	public String getDteDateEdited(){
		return dteDateEdited;
	}
	public void setDteDateEdited(String dteDateEdited){
		this.dteDateEdited=dteDateEdited;
	}

	public String getStrUserCreated(){
		return strUserCreated;
	}
	public void setStrUserCreated(String strUserCreated){
		this. strUserCreated = (String) setDefaultValue( strUserCreated, "NA");
	}

	public String getStrUserEdited(){
		return strUserEdited;
	}
	public void setStrUserEdited(String strUserEdited){
		this. strUserEdited = (String) setDefaultValue( strUserEdited, "NA");
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this. strClientCode = (String) setDefaultValue( strClientCode, "NA");
	}

	public String getStrOperationalNY(){
		return strOperationalNY;
	}
	public void setStrOperationalNY(String strOperationalNY){
		this. strOperationalNY = (String) setDefaultValue( strOperationalNY, "NA");
	}
	public long getIntGId() {
		return intGId;
	}

	public void setIntGId(long intGId) {
		this.intGId = (Long) setDefaultValue(intGId, "NA");
	}


//Function to Set Default Values
	private Object setDefaultValue(Object value, Object defaultValue){
		if(value !=null && (value instanceof String && value.toString().length()>0)){
			return value;
		}
		else if(value !=null && (value instanceof Double && value.toString().length()>0)){
			return value;
		}
		else if(value !=null && (value instanceof Integer && value.toString().length()>0)){
			return value;
		}
		else if(value !=null && (value instanceof Long && value.toString().length()>0)){
			return value;
		}
		else{
			return defaultValue;
		}
	}

}
