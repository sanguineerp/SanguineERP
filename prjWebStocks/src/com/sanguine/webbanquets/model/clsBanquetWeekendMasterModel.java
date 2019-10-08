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
@Table(name="tblweekendmaster")
@IdClass(clsBanquetWeekendMasterModel_ID.class)

public class clsBanquetWeekendMasterModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsBanquetWeekendMasterModel(){}

	public clsBanquetWeekendMasterModel(clsBanquetWeekendMasterModel_ID objModelID){		
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})

//Variable Declaration
	@Column(name="strDayNo")
	private String strDayNo;

	@Column(name="strDay")
	private String strDay;

	@Column(name="dtDteCreated")
	private String dtDteCreated;
	
	@Column(name="dtDteEdited")
	private String dtDteEdited;
	
	@Column(name="strUserCreated")
	private String strUserCreated;

	@Column(name="strUserEdited")
	private String strUserEdited;

	@Column(name="strClientCode")
	private String strClientCode;

	public String getDtDteCreated() {
		return dtDteCreated;
	}

	public void setDtDteCreated(String dtDteCreated) {
		this.dtDteCreated = dtDteCreated;
	}

	public String getDtDteEdited() {
		return dtDteEdited;
	}

	public void setDtDteEdited(String dtDteEdited) {
		this.dtDteEdited = dtDteEdited;
	}

	

//Setter-Getter Methods
	public String getStrDayNo(){
		return strDayNo;
	}
	public void setStrDayNo(String strDayNo){
		this. strDayNo = (String) setDefaultValue( strDayNo, "");
	}

	public String getStrDay(){
		return strDay;
	}
	public void setStrDay(String strDay){
		this. strDay = (String) setDefaultValue( strDay, "");
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

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this. strClientCode = (String) setDefaultValue( strClientCode, "");
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
