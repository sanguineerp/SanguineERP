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
@Table(name="tblbqitemmaster")
@IdClass(clsItemMasterModel_ID.class)

public class clsItemMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsItemMasterModel(){}

	public clsItemMasterModel(clsItemMasterModel_ID objModelID){
		strItemCode = objModelID.getStrItemCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strItemCode",column=@Column(name="strItemCode")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strItemCode")
	private String strItemCode;

	@Column(name="strItemName")
	private String strItemName;

	@Column(name="strMenuHeadCode")
	private String strMenuHeadCode;

	@Column(name="strSubGroupCode")
	private String strSubGroupCode;

	@Column(name="strDepartmentCode")
	private String strDepartmentCode;

	@Column(name="strUnit")
	private String strUnit;

	@Column(name="dblAmount")
	private double dblAmount;

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

	@Column(name="strOperational")
	private String strOperational;
	
	@Column(name = "strTaxIndicator", nullable = false)
	private String strTaxIndicator;


//Setter-Getter Methods
	public String getStrItemCode(){
		return strItemCode;
	}
	public void setStrItemCode(String strItemCode){
		this. strItemCode = (String) setDefaultValue( strItemCode, "NA");
	}

	public String getStrItemName(){
		return strItemName;
	}
	public void setStrItemName(String strItemName){
		this. strItemName = (String) setDefaultValue( strItemName, "NA");
	}

	public String getStrMenuHeadCode(){
		return strMenuHeadCode;
	}
	public void setStrMenuHeadCode(String strMenuHeadCode){
		this. strMenuHeadCode = (String) setDefaultValue( strMenuHeadCode, "NA");
	}

	public String getStrSubGroupCode(){
		return strSubGroupCode;
	}
	public void setStrSubGroupCode(String strSubGroupCode){
		this. strSubGroupCode = (String) setDefaultValue( strSubGroupCode, "NA");
	}

	public String getStrDepartmentCode(){
		return strDepartmentCode;
	}
	public void setStrDepartmentCode(String strDepartmentCode){
		this. strDepartmentCode = (String) setDefaultValue( strDepartmentCode, "NA");
	}

	public String getStrUnit(){
		return strUnit;
	}
	public void setStrUnit(String strUnit){
		this. strUnit = (String) setDefaultValue( strUnit, "NA");
	}

	public double getDblAmount(){
		return dblAmount;
	}
	public void setDblAmount(double dblAmount){
		this. dblAmount = (Double) setDefaultValue( dblAmount, "NA");
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

	public String getStrOperational(){
		return strOperational;
	}
	public void setStrOperational(String strOperational){
		this. strOperational = (String) setDefaultValue( strOperational, "NA");
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

}
