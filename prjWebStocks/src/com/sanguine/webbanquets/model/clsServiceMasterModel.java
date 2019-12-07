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
@Table(name="tblservicemaster")
@IdClass(clsServiceMasterModel_ID.class)

public class clsServiceMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsServiceMasterModel(){}

	public clsServiceMasterModel(clsServiceMasterModel_ID objModelID){
		strServiceCode = objModelID.getStrServiceCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strServiceCode",column=@Column(name="strServiceCode")),
        @AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strServiceCode")
	private String strServiceCode;
	
	@Column(name="strServiceName", columnDefinition = "VARCHAR(255) NOT NULL default ''")
	private String strServiceName;
	

	@Column(name="strPropertyCode")
	private String strPropertyCode;

	@Column(name="strClientCode")
	private String strClientCode;

	@Column(name="strUserCreated", nullable = false, updatable = false, columnDefinition = "VARCHAR(255) NOT NULL default ''")
	private String strUserCreated;

	@Column(name="strUserEdited", columnDefinition = "VARCHAR(255) NOT NULL default ''")
	private String strUserEdited;

	@Column(name="dteDateCreated", nullable = false, updatable = false, columnDefinition = "VARCHAR(255) NOT NULL default ''")
	private String dteDateCreated;

	@Column(name="dteDateEdited", columnDefinition = "VARCHAR(255) NOT NULL default ''")
	private String dteDateEdited;

	@Column(name="strOperationalYN")
	private String strOperationalYN;
	
	@Column(name = "intSId", nullable = false)
	private long intSId;
    
	@Column(name="strDeptCode")
	private String strDeptCode;
	
	@Column(name="dblRate")
	private double dblRate;
	
	@Column(name = "strTaxIndicator", nullable = false)
	private String strTaxIndicator;
	
	@Column(name="strServiceType")
	private String strServiceType;
	

//Setter-Getter Methods
	public String getStrServiceCode(){
		return strServiceCode;
	}
	public void setStrServiceCode(String strServiceCode){
		this. strServiceCode = (String) setDefaultValue( strServiceCode, "NA");
	}

	public String getStrServiceName() {
		return strServiceName;
	}

	public void setStrServiceName(String strServiceName) {
		this.strServiceName = strServiceName;
	}

	public String getStrPropertyCode(){
		return strPropertyCode;
	}
	public void setStrPropertyCode(String strPropertyCode){
		this. strPropertyCode = (String) setDefaultValue( strPropertyCode, "NA");
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this. strClientCode = (String) setDefaultValue( strClientCode, "NA");
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

	public String getStrOperationalYN(){
		return strOperationalYN;
	}
	public void setStrOperationalYN(String strOperationalYN){
		this. strOperationalYN = (String) setDefaultValue( strOperationalYN, "NA");
	}


public long getIntSId() {
		return intSId;
	}

	public void setIntSId(long intSId) {
		this.intSId = intSId;
	}

	public String getStrDeptCode() {
		return strDeptCode;
	}

	public void setStrDeptCode(String strDeptCode) {
		this.strDeptCode = strDeptCode;
	}

	public double getDblRate() {
		return dblRate;
	}

	public void setDblRate(double dblRate) {
		this.dblRate = dblRate;
	}

	public String getStrTaxIndicator() {
		return strTaxIndicator;
	}

	public void setStrTaxIndicator(String strTaxIndicator) {
		this.strTaxIndicator = strTaxIndicator;
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

	public String getStrServiceType() {
		return strServiceType;
	}

	public void setStrServiceType(String strServiceType) {
		this.strServiceType = strServiceType;
	}

}
