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
@Table(name="tblbusinesssource")
@IdClass(clsWebClubBusinessSourceMasterModel_ID.class)

public class clsWebClubBusinessSourceMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsWebClubBusinessSourceMasterModel(){}

	public clsWebClubBusinessSourceMasterModel(clsWebClubBusinessSourceMasterModel_ID objModelID){
		strBusinessSrcCode = objModelID.getStrBusinessSrcCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strBusinessSrcCode",column=@Column(name="strBusinessSrcCode")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strBusinessSrcCode")
	private String strBusinessSrcCode;

	@Column(name="strBusinessSrcName")
	private String strBusinessSrcName;

	@Column(name="dblPercent")
	private double dblPercent;

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
	
	@Column(name = "intId", nullable = false, updatable = false)
	private long intId;

//Setter-Getter Methods
	public String getStrBusinessSrcCode(){
		return strBusinessSrcCode;
	}
	public void setStrBusinessSrcCode(String strBusinessSrcCode){
		this. strBusinessSrcCode = (String) setDefaultValue( strBusinessSrcCode, "NA");
	}

	public String getStrBusinessSrcName(){
		return strBusinessSrcName;
	}
	public void setStrBusinessSrcName(String strBusinessSrcName){
		this. strBusinessSrcName = (String) setDefaultValue( strBusinessSrcName, "NA");
	}

	public double getDblPercent(){
		return dblPercent;
	}
	public void setDblPercent(double dblPercent){
		this. dblPercent = (Double) setDefaultValue( dblPercent, "NA");
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

	public long getIntId() {
		return intId;
	}

	public void setIntId(long intId) {
		this.intId = intId;
	}

}
