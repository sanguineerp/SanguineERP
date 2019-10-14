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
	@Column(name="strStaffCode",columnDefinition = "VARCHAR(10) NOT NULL")
	private String strStaffCode;

	@Column(name="strStaffName",columnDefinition = "VARCHAR(50) NOT NULL")
	private String strStaffName;

	@Column(name="strStaffCatCode",columnDefinition = "VARCHAR(10) NOT NULL")
	private String strStaffCatCode;

	@Column(name="strOperationalYN",columnDefinition = "VARCHAR(10) NOT NULL")
	private String strOperationalYN;

	@Column(name="strClientCode",columnDefinition = "VARCHAR(20) NOT NULL DEFAULT ''")
	private String strClientCode;

	@Column(name="strUserCreated",columnDefinition = "VARCHAR(20) NOT NULL DEFAULT ''")
	private String strUserCreated;

	@Column(name="strUserEdited",columnDefinition = "VARCHAR(20) NOT NULL DEFAULT ''")
	private String strUserEdited;
	
	@Column(name="dtCreated",columnDefinition = "DATETIME NOT NULL")
	private String dtCreated;
	
	@Column(name="dtEdited",columnDefinition = "DATETIME NOT NULL")
	private String dtEdited;
	
	@Column(name="IntSTId",columnDefinition = "BIGINT(20) NOT NULL")
	private long intSTId;
	
	@Column(name="strMobile",columnDefinition = "VARCHAR(50) NOT NULL DEFAULT '' ")
	private String strMobile;
	
	@Column(name="strEmail",columnDefinition = "VARCHAR(50) NOT NULL DEFAULT '' ")
	private String strEmail;

	

        public long getIntSTId() {
		return intSTId;
	}

	public void setIntSTId(long intSTId) {
		this.intSTId = intSTId;
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

	public String getStrStaffCatCode(){
		return strStaffCatCode;
	}
	public void setStrStaffCatCode(String strStaffCatCode){
		this. strStaffCatCode = (String) setDefaultValue( strStaffCatCode, "");
	}

	public String getStrOperationalYN(){
		return strOperationalYN;
	}
	public void setStrOperationalYN(String strOperationalYN){
		this. strOperationalYN = (String) setDefaultValue( strOperationalYN, "N");
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


        public String getStrMobile() {
		return strMobile;
	}

	public void setStrMobile(String strMobile) {
		this.strMobile = (String)setDefaultValue(strMobile, "");
	}

	public String getStrEmail() {
		return strEmail;
	}

	public void setStrEmail(String strEmail) {
		this.strEmail = (String)setDefaultValue(strEmail, "");
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
