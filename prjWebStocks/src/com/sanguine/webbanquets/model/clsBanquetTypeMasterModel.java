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
@Table(name="tblbanquettypemaster")
@IdClass(clsBanquetTypeMasterModel_ID.class)

public class clsBanquetTypeMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsBanquetTypeMasterModel(){}

	public clsBanquetTypeMasterModel(clsBanquetTypeMasterModel_ID objModelID){
		strBanquetTypeCode = objModelID.getStrBanquetTypeCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strBanquetTypeCode",column=@Column(name="strBanquetTypeCode")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strBanquetTypeCode",columnDefinition = "VARCHAR(20) NOT NULL")
	private String strBanquetTypeCode;

	@Column(name="strBanquetTypeName",columnDefinition = "VARCHAR(50) NOT NULL")
	private String strBanquetTypeName;

	@Column(name="intId",columnDefinition = "BIGINT(20) NOT NULL")
	private long intId;

	@Column(name="dblRate",columnDefinition = "DECIMAL(18,4) NOT NULL")
	private double dblRate;

	@Column(name="strTaxIndicator",columnDefinition = "VARCHAR(5) NOT NULL")
	private String strTaxIndicator;

	@Column(name="strUserCreated",columnDefinition = "VARCHAR(20) NOT NULL")
	private String strUserCreated;

	@Column(name="strUserEdited",columnDefinition = "VARCHAR(20) NOT NULL")
	private String strUserEdited;

	@Column(name="dteDateCreated",columnDefinition = "DATETIME NOT NULL")
	private String dteDateCreated;

	@Column(name="dteDateEdited",columnDefinition = "DATETIME NOT NULL")
	private String dteDateEdited;

	@Column(name="strClientCode",columnDefinition = "VARCHAR(20) NOT NULL")
	private String strClientCode;

//Setter-Getter Methods
	public String getStrBanquetTypeCode(){
		return strBanquetTypeCode;
	}
	public void setStrBanquetTypeCode(String strBanquetTypeCode){
		this. strBanquetTypeCode = (String) setDefaultValue( strBanquetTypeCode, "NA");
	}

	public String getStrBanquetTypeName(){
		return strBanquetTypeName;
	}
	public void setStrBanquetTypeName(String strBanquetTypeName){
		this. strBanquetTypeName = (String) setDefaultValue( strBanquetTypeName, "NA");
	}

	public long getIntId(){
		return intId;
	}
	public void setIntId(long intId){
		this. intId = (Long) setDefaultValue( intId, "NA");
	}

	public double getDblRate(){
		return dblRate;
	}
	public void setDblRate(double dblRate){
		this. dblRate = (Double) setDefaultValue( dblRate, "NA");
	}

	public String getStrTaxIndicator(){
		return strTaxIndicator;
	}
	public void setStrTaxIndicator(String strTaxIndicator){
		this. strTaxIndicator = (String) setDefaultValue( strTaxIndicator, "NA");
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
