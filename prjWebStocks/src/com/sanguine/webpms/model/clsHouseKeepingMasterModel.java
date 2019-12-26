package com.sanguine.webpms.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name="tblhousekeepmaster")
@IdClass(clsHouseKeepingMasterModel_ID.class)

public class clsHouseKeepingMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsHouseKeepingMasterModel(){}

	public clsHouseKeepingMasterModel(clsHouseKeepingMasterModel_ID objModelID){
		strHouseKeepCode = objModelID.getStrHouseKeepCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strHouseKeepCode",column=@Column(name="strHouseKeepCode")),
@AttributeOverride(name="intId",column=@Column(name="intId")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strHouseKeepCode")
	private String strHouseKeepCode;

	@Column(name="strHouseKeepName")
	private String strHouseKeepName;

	@Column(name="strRemarks")
	private String strRemarks;

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

//Setter-Getter Methods
	public String getStrHouseKeepCode(){
		return strHouseKeepCode;
	}
	public void setStrHouseKeepCode(String strHouseKeepCode){
		this. strHouseKeepCode = (String) setDefaultValue( strHouseKeepCode, "NA");
	}

	public String getStrHouseKeepName(){
		return strHouseKeepName;
	}
	public void setStrHouseKeepName(String strHouseKeepName){
		this. strHouseKeepName = (String) setDefaultValue( strHouseKeepName, "NA");
	}

	public String getStrRemarks(){
		return strRemarks;
	}
	public void setStrRemarks(String strRemarks){
		this. strRemarks = (String) setDefaultValue( strRemarks, "NA");
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
