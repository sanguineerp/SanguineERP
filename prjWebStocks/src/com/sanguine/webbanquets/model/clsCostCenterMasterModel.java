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
@Table(name="tblcostcentermaster")
@IdClass(clsCostCenterMasterModel_ID.class)

public class clsCostCenterMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsCostCenterMasterModel(){}

	public clsCostCenterMasterModel(clsCostCenterMasterModel_ID objModelID){
		strCostCenterCode = objModelID.getStrCostCenterCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strCostCenterCode",column=@Column(name="strCostCenterCode")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strCostCenterCode",columnDefinition = "VARCHAR(10) NOT NULL")
	private String strCostCenterCode;

	@Column(name="strCostCenterName",columnDefinition = "VARCHAR(50) NOT NULL")
	private String strCostCenterName;

	@Column(name="intId",columnDefinition = "INT(5) NOT NULL")
	private long intId;

	@Column(name="strUserCreated",columnDefinition = "VARCHAR(10) NOT NULL")
	private String strUserCreated;

	@Column(name="strUserEdited",columnDefinition = "VARCHAR(10) NOT NULL")
	private String strUserEdited;

	@Column(name="dteDateCreated",columnDefinition = "DATETIME NOT NULL")
	private String dteDateCreated;

	@Column(name="dteDateEdited",columnDefinition = "DATETIME NOT NULL")
	private String dteDateEdited;

	@Column(name="strOperational",columnDefinition = "VARCHAR(5) NOT NULL")
	private String strOperational;

	@Column(name="strClientCode",columnDefinition = "VARCHAR(11) NOT NULL DEFAULT ''")
	private String strClientCode;

//Setter-Getter Methods
	public String getStrCostCenterCode(){
		return strCostCenterCode;
	}
	public void setStrCostCenterCode(String strCostCenterCode){
		this. strCostCenterCode = (String) setDefaultValue( strCostCenterCode, "NA");
	}

	public String getStrCostCenterName(){
		return strCostCenterName;
	}
	public void setStrCostCenterName(String strCostCenterName){
		this. strCostCenterName = (String) setDefaultValue( strCostCenterName, "NA");
	}

	public long getIntId(){
		return intId;
	}
	public void setIntId(long intId){
		this. intId = (Long) setDefaultValue( intId, "NA");
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
