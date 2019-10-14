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
@Table(name="tblbanquetmaster")
@IdClass(clsBanquetMasterModel_ID.class)

public class clsBanquetMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsBanquetMasterModel(){}

	public clsBanquetMasterModel(clsBanquetMasterModel_ID objModelID){
		strBanquetCode = objModelID.getStrBanquetCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strBanquetCode",column=@Column(name="strBanquetCode")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strBanquetCode",columnDefinition="VARCHAR(20) NOT NULL")
	private String strBanquetCode;

	@Column(name="strBanquetName",columnDefinition="VARCHAR(30) NOT NULL")
	private String strBanquetName;

	@Column(name="intId",columnDefinition="BIGINT(20) NOT NULL")
	private long intId;

	
	@Column(name="strOperational",columnDefinition="VARCHAR(5) NOT NULL")
	private String strOperational;
	
	@Column(name="strUserCreated",columnDefinition="VARCHAR(20) NOT NULL")
	private String strUserCreated;
	
	@Column(name="strUserEdited",columnDefinition="VARCHAR(20) NOT NULL")
	private String strUserEdited;
	
	@Column(name="dteDateCreated",columnDefinition="DATETIME NOT NULL")
	private String dteDateCreated;
	
	@Column(name="dteDateEdited",columnDefinition="DATETIME NOT NULL")
	private String dteDateEdited;

	@Column(name="strClientCode",columnDefinition="VARCHAR(10) NOT NULL")
	private String strClientCode;
	
	@Column(name="strBanquetTypeCode",columnDefinition="VARCHAR(20) NOT NULL")
	private String strBanquetTypeCode;
	
	

//Setter-Getter Methods
	public String getStrBanquetCode(){
		return strBanquetCode;
	}
	public void setStrBanquetCode(String strBanquetCode){
		this. strBanquetCode = (String) setDefaultValue( strBanquetCode, "NA");
	}

	public String getStrBanquetName(){
		return strBanquetName;
	}
	public void setStrBanquetName(String strBanquetName){
		this. strBanquetName = (String) setDefaultValue( strBanquetName, "NA");
	}

	public long getIntId(){
		return intId;
	}
	public void setIntId(long intId){
		this. intId = (Long) setDefaultValue( intId, "NA");
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

	public String getStrUserCreated() {
		return strUserCreated;
	}

	public void setStrUserCreated(String strUserCreated) {
		this.strUserCreated = strUserCreated;
	}

	public String getStrUserEdited() {
		return strUserEdited;
	}

	public void setStrUserEdited(String strUserEdited) {
		this.strUserEdited = strUserEdited;
	}

	public String getDteDateCreated() {
		return dteDateCreated;
	}

	public void setDteDateCreated(String dteDateCreated) {
		this.dteDateCreated = dteDateCreated;
	}

	public String getDteDateEdited() {
		return dteDateEdited;
	}

	public void setDteDateEdited(String dteDateEdited) {
		this.dteDateEdited = dteDateEdited;
	}

	public String getStrBanquetTypeCode() {
		return strBanquetTypeCode;
	}

	public void setStrBanquetTypeCode(String strBanquetTypeCode) {
		this.strBanquetTypeCode = strBanquetTypeCode;
	}

}
