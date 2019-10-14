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
@Table(name="tblequipment")
@IdClass(clsEquipmentModel_ID.class)

public class clsEquipmentModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsEquipmentModel(){}

	public clsEquipmentModel(clsEquipmentModel_ID objModelID){
		strEquipmentCode = objModelID.getStrEquipmentCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
	@AttributeOverride(name="strEquipmentCode",column=@Column(name="strEquipmentCode")),
	@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strEquipmentCode",columnDefinition = "VARCHAR(255) NOT NULL DEFAULT ''")
	private String strEquipmentCode;

	@Column(name="strEquipmentName",columnDefinition ="VARCHAR(100) NOT NULL DEFAULT ''")
	private String strEquipmentName;

	@Column(name="dteDateCreated",columnDefinition ="VARCHAR(20) NOT NULL")
	private String dteDateCreated;

	@Column(name="dteDateEdited",columnDefinition ="VARCHAR(20) NOT NULL")
	private String dteDateEdited;

	@Column(name="strUserCreated",columnDefinition ="NOT NULL DEFAULT ''")
	private String strUserCreated;

	@Column(name="strUserEdited",columnDefinition ="NOT NULL DEFAULT ''")
	private String strUserEdited;

	@Column(name="strClientCode",columnDefinition ="VARCHAR(10) NOT NULL")
	private String strClientCode;
	
	@Column(name="intId")
	private long intId;
	
	@Column(name="strOperational",columnDefinition ="VARCHAR(2) NOT NULL")
	private String strOperational;

	@Column(name="strDeptCode",columnDefinition ="VARCHAR(10) NOT NULL")
	private String strDeptCode;
	
	@Column(name="dblEquipmentRate",columnDefinition ="DECIMAL(18,4) NOT NULL DEFAULT '0.0000'")
	private double dblEquipmentRate;
	
	@Column(name = "strTaxIndicator", nullable = false)
	private String strTaxIndicator;

	
	//Setter-Getter Methods
	public String getStrEquipmentCode(){
		return strEquipmentCode;
	}
	public void setStrEquipmentCode(String strEquipmentCode){
		this. strEquipmentCode = (String) setDefaultValue( strEquipmentCode, "NA");
	}

	public String getStrEquipmentName(){
		return strEquipmentName;
	}
	public void setStrEquipmentName(String strEquipmentName){
		this. strEquipmentName = (String) setDefaultValue( strEquipmentName, "NA");
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

	public String getStrOperational() {
		return strOperational;
	}

	public void setStrOperational(String strOperational) {
		this.strOperational = strOperational;
	}

	public String getStrDeptCode() {
		return strDeptCode;
	}

	public void setStrDeptCode(String strDeptCode) {
		this.strDeptCode = strDeptCode;
	}

	public double getDblEquipmentRate() {
		return dblEquipmentRate;
	}

	public void setDblEquipmentRate(double dblEquipmentRate) {
		this.dblEquipmentRate = dblEquipmentRate;
	}

	public String getStrTaxIndicator() {
		return strTaxIndicator;
	}

	public void setStrTaxIndicator(String strTaxIndicator) {
		this.strTaxIndicator = strTaxIndicator;
	}

	
}
