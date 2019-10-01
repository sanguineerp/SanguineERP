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
@Table(name="tblfunctionmaster")
@IdClass(clsFunctionMasterModel_ID.class)

public class clsFunctionMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsFunctionMasterModel(){}

	public clsFunctionMasterModel(clsFunctionMasterModel_ID objModelID){
		strFunctionCode = objModelID.getStrFunctionCode();
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strFunctionCode",column=@Column(name="strFunctionCode")),
@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strFunctionCode")
	private String strFunctionCode;

	@Column(name="strFunctionName")
	private String strFunctionName;

	@Column(name="strOperationalYN")
	private String strOperationalYN;

	@Column(name="strPropertyCode")
	private String strPropertyCode;

	@Column(name="strClientCode")
	private String strClientCode;

	@Column(name="strUserCreated")
	private String strUserCreated;

	@Column(name="strUserEdited")
	private String strUserEdited;

	@Column(name="strDateCreated")
	private String strDateCreated;

	@Column(name="strDateEdited")
	private String strDateEdited;
	
	@Column(name = "intFId", nullable = false, updatable = false)
	private long intFId;

//Setter-Getter Methods
	public String getStrFunctionCode(){
		return strFunctionCode;
	}
	public void setStrFunctionCode(String strFunctionCode){
		this. strFunctionCode = (String) setDefaultValue( strFunctionCode, "NA");
	}

	public String getStrFunctionName(){
		return strFunctionName;
	}
	public void setStrFunctionName(String strFunctionName){
		this. strFunctionName = (String) setDefaultValue( strFunctionName, "NA");
	}

	public String getStrOperationalYN(){
		return strOperationalYN;
	}
	public void setStrOperationalYN(String strOperationalYN){
		this. strOperationalYN = (String) setDefaultValue( strOperationalYN, "NA");
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

	public String getStrDateCreated(){
		return strDateCreated;
	}
	public void setStrDateCreated(String strDateCreated){
		this. strDateCreated = (String) setDefaultValue( strDateCreated, "NA");
	}

	public String getStrDateEdited(){
		return strDateEdited;
	}
	public void setStrDateEdited(String strDateEdited){
		this. strDateEdited = (String) setDefaultValue( strDateEdited, "NA");
	}


public long getIntFId() {
		return intFId;
	}

	public void setIntFId(long intFId) {
		this.intFId = intFId;
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
