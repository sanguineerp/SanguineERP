package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name="tblbqmenuhead")
@IdClass(clsMenuHeadMasterModel_ID.class)

public class clsMenuHeadMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsMenuHeadMasterModel(){}

	public clsMenuHeadMasterModel(clsMenuHeadMasterModel_ID objModelID){
		strMenuHeadCode = objModelID.getStrMenuHeadCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strMenuHeadCode",column=@Column(name="strMenuHeadCode")),
		@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strMenuHeadCode")
	private String strMenuHeadCode;

	@Column(name="strMenuHeadName")
	private String strMenuHeadName;

	@Column(name="dteDateCreated")
	private String dteDateCreated;

	@Column(name="dteDateEdited")
	private String dteDateEdited;

	@Column(name="strUserCreated")
	private String strUserCreated;

	@Column(name="strUserEdited")
	private String strUserEdited;

	@Column(name="strOperational")
	private String strOperational;

	@Column(name="strClientCode")
	private String strClientCode;

//Setter-Getter Methods
	public String getStrMenuHeadCode(){
		return strMenuHeadCode;
	}
	public void setStrMenuHeadCode(String strMenuHeadCode){
		this. strMenuHeadCode = (String) setDefaultValue( strMenuHeadCode, "NA");
	}

	public String getStrMenuHeadName(){
		return strMenuHeadName;
	}
	public void setStrMenuHeadName(String strMenuHeadName){
		this. strMenuHeadName = (String) setDefaultValue( strMenuHeadName, "NA");
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

	public String getStrOperational(){
		return strOperational;
	}
	public void setStrOperational(String strOperational){
		this. strOperational = (String) setDefaultValue( strOperational, "NA");
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
