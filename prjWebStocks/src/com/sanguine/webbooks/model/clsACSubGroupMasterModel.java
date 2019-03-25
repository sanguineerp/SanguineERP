package com.sanguine.webbooks.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="tblsubgroupmaster")
@IdClass(clsACSubGroupMasterModel_ID.class)

public class clsACSubGroupMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsACSubGroupMasterModel(){}

	public clsACSubGroupMasterModel(clsACSubGroupMasterModel_ID objModelID){
		strSubGroupCode = objModelID.getStrSubGroupCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strSubGroupCode",column=@Column(name="strSubGroupCode")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="intSGCode")
	private long intSGCode;

	@Column(name="strSubGroupCode")
	private String strSubGroupCode;

	@Column(name="strSubGroupName")
	private String strSubGroupName;

	@Column(name="strGroupCode")
	private String strGroupCode;

	@Column(name="strUnderSubGroup")
	private String strUnderSubGroup;

	@Column(name="strUserCreated")
	private String strUserCreated;

	@Column(name="strUserModified")
	private String strUserModified;

	@Column(name="dteCreatedDate")
	private String dteCreatedDate;

	@Column(name="dteLastModified")
	private String dteLastModified;

	@Column(name="strPropertyCode")
	private String strPropertyCode;

	@Column(name="strClientCode")
	private String strClientCode;

	@Transient
	private String strGroupName;
	
//Setter-Getter Methods
	public long getIntSGCode(){
		return intSGCode;
	}
	public void setIntSGCode(long intSGCode){
		this. intSGCode = (Long) setDefaultValue( intSGCode, "NA");
	}

	public String getStrSubGroupCode(){
		return strSubGroupCode;
	}
	public void setStrSubGroupCode(String strSubGroupCode){
		this. strSubGroupCode = (String) setDefaultValue( strSubGroupCode, "NA");
	}

	public String getStrSubGroupName(){
		return strSubGroupName;
	}
	public void setStrSubGroupName(String strSubGroupName){
		this. strSubGroupName = (String) setDefaultValue( strSubGroupName, "NA");
	}

	public String getStrGroupCode(){
		return strGroupCode;
	}
	public void setStrGroupCode(String strGroupCode){
		this. strGroupCode = (String) setDefaultValue( strGroupCode, "NA");
	}

	public String getStrUnderSubGroup(){
		return strUnderSubGroup;
	}
	public void setStrUnderSubGroup(String strUnderSubGroup){
		this. strUnderSubGroup = (String) setDefaultValue( strUnderSubGroup, "NA");
	}

	public String getStrUserCreated(){
		return strUserCreated;
	}
	public void setStrUserCreated(String strUserCreated){
		this. strUserCreated = (String) setDefaultValue( strUserCreated, "NA");
	}

	public String getStrUserModified(){
		return strUserModified;
	}
	public void setStrUserModified(String strUserModified){
		this. strUserModified = (String) setDefaultValue( strUserModified, "NA");
	}

	public String getDteCreatedDate(){
		return dteCreatedDate;
	}
	public void setDteCreatedDate(String dteCreatedDate){
		this.dteCreatedDate=dteCreatedDate;
	}

	public String getDteLastModified(){
		return dteLastModified;
	}
	public void setDteLastModified(String dteLastModified){
		this.dteLastModified=dteLastModified;
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

	public String getStrGroupName() {
		return strGroupName;
	}

	public void setStrGroupName(String strGroupName) {
		this.strGroupName = strGroupName;
	}

}
