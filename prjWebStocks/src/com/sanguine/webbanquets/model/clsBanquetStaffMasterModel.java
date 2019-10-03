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
@Table(name="tblstaffmaster")
@IdClass(clsBanquetStaffMasterModel_ID.class)

public class clsBanquetStaffMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsBanquetStaffMasterModel(){}

	public clsBanquetStaffMasterModel(clsBanquetStaffMasterModel_ID objModelID){
		strStaffCode = objModelID.getStrStaffCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strStaffCode",column=@Column(name="strStaffCode")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strStaffCode")
	private String strStaffCode;

	@Column(name="strStaffName")
	private String strStaffName;

	@Column(name="strDeptCode")
	private String strDeptCode;

	@Column(name="strOperationalYN")
	private String strOperationalYN;

	@Column(name="strClientCode")
	private String strClientCode;

	@Column(name="strUserCreated")
	private String strUserCreated;

	@Column(name="strUserEdited")
	private String strUserEdited;
	
	@Column(name="dtCreated")
	private String dtCreated;
	
	@Column(name="dtEdited")
	private String dtEdited;
	
	@Column(name="IntGId")
	private long intGId;
	

public long getIntGId() {
		return intGId;
	}

	public void setIntGId(long intGId) {
		this.intGId = intGId;
	}

public String getDtCreated() {
		return dtCreated;
	}

	public void setDtCreated(String dtCreated) {
		this.dtCreated = dtCreated;
	}

	public String getDtEdited() {
		return dtEdited;
	}

	public void setDtEdited(String dtEdited) {
		this.dtEdited = dtEdited;
	}

	//Setter-Getter Methods
	public String getStrStaffCode(){
		return strStaffCode;
	}
	public void setStrStaffCode(String strStaffCode){
		this. strStaffCode = (String) setDefaultValue( strStaffCode, "");
	}

	public String getStrStaffName(){
		return strStaffName;
	}
	public void setStrStaffName(String strStaffName){
		this. strStaffName = (String) setDefaultValue( strStaffName, "");
	}

	public String getStrDeptCode(){
		return strDeptCode;
	}
	public void setStrDeptCode(String strDeptCode){
		this. strDeptCode = (String) setDefaultValue( strDeptCode, "");
	}

	public String getStrOperationalYN(){
		return strOperationalYN;
	}
	public void setStrOperationalYN(String strOperationalYN){
		this. strOperationalYN = (String) setDefaultValue( strOperationalYN, "");
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this. strClientCode = (String) setDefaultValue( strClientCode, "");
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
