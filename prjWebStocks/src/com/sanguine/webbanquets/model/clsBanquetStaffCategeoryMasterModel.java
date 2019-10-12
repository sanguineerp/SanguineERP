package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="tblstaffcategeorymaster")
@IdClass(clsBanquetStaffCategeoryMasterModel_ID.class)

public class clsBanquetStaffCategeoryMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsBanquetStaffCategeoryMasterModel(){}

	public clsBanquetStaffCategeoryMasterModel(clsBanquetStaffCategeoryMasterModel_ID objModelID){
		strStaffCategeoryCode = objModelID.getStrStaffCategeoryCode();
		strDeptCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strStaffCategeoryCode",column=@Column(name="strStaffCategeoryCode",columnDefinition = "VARCHAR(10) NOT NULL")),
@AttributeOverride(name="strDeptCode",column=@Column(name="strDeptCode",columnDefinition = "VARCHAR(20) NOT NULL DEFAULT ''"))
	})

//Variable Declaration
	@Column(name="strStaffCategeoryCode",columnDefinition = "VARCHAR(10) NOT NULL")
	private String strStaffCategeoryCode;

	@Column(name="strStaffCategeoryName",columnDefinition = "VARCHAR(50) NOT NULL")
	private String strStaffCategeoryName;

	@Column(name="strStaffCount",columnDefinition = "VARCHAR(20) NOT NULL")
	private String strStaffCount;

	@Column(name="strOperationalYN",columnDefinition = "VARCHAR(10) NOT NULL")
	private String strOperationalYN;

	@Column(name="dteDateCreated",columnDefinition = "DATETIME NOT NULL")
	private String dteDateCreated;

	@Column(name="dteDateEdited",columnDefinition = "DATETIME NOT NULL")
	private String dteDateEdited;

	@Column(name="strClientCode",columnDefinition = "VARCHAR(20) NOT NULL DEFAULT ''")
	private String strClientCode;

	@Column(name="strDeptCode",columnDefinition = "VARCHAR(20) NOT NULL DEFAULT ''")
	private String strDeptCode;

	@Column(name="strUserCreated",columnDefinition = "VARCHAR(20) NOT NULL DEFAULT ''")
	private String strUserCreated;

	@Column(name="strUserEdited",columnDefinition = "VARCHAR(20) NOT NULL DEFAULT ''")
	private String strUserEdited;

	@Column(name="intSCId",columnDefinition = "BIGINT(20) NOT NULL DEFAULT '0'")
	private long intSCId;

//Setter-Getter Methods
	public String getStrStaffCategeoryCode(){
		return strStaffCategeoryCode;
	}
	public void setStrStaffCategeoryCode(String strStaffCategeoryCode){
		this. strStaffCategeoryCode = (String) setDefaultValue( strStaffCategeoryCode, "");
	}

	public String getStrStaffCategeoryName(){
		return strStaffCategeoryName;
	}
	public void setStrStaffCategeoryName(String strStaffCategeoryName){
		this. strStaffCategeoryName = (String) setDefaultValue( strStaffCategeoryName, "");
	}

	public String getStrStaffCount(){
		return strStaffCount;
	}
	public void setStrStaffCount(String strStaffCount){
		this. strStaffCount = (String) setDefaultValue( strStaffCount, "");
	}

	public String getStrOperationalYN(){
		return strOperationalYN;
	}
	public void setStrOperationalYN(String strOperationalYN){
		this. strOperationalYN = (String) setDefaultValue( strOperationalYN, "N");
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
		this. strClientCode = (String) setDefaultValue( strClientCode, "");
	}

	public String getStrDeptCode(){
		return strDeptCode;
	}
	public void setStrDeptCode(String strDeptCode){
		this. strDeptCode = (String) setDefaultValue( strDeptCode, "");
	}

	public String getStrUserCreated(){
		return strUserCreated;
	}
	public void setStrUserCreated(String strUserCreated){
		this. strUserCreated = (String) setDefaultValue( strUserCreated, "");
	}

	public String getStrUserEdited(){
		return strUserEdited;
	}
	public void setStrUserEdited(String strUserEdited){
		this. strUserEdited = (String) setDefaultValue( strUserEdited, "");
	}

	public long getIntSCId(){
		return intSCId;
	}
	public void setIntSCId(long intSCId){
		this. intSCId = (Long) setDefaultValue( intSCId, "");
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
