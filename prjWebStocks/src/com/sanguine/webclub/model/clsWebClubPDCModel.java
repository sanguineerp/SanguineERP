package com.sanguine.webclub.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.sanguine.webbooks.model.clsWebBooksAuditDebtorDtlModel;

@Entity
@Table(name="tblpdcdtl")
@IdClass(clsWebClubPDCModel_ID.class)

public class clsWebClubPDCModel implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsWebClubPDCModel(){}

	public clsWebClubPDCModel(clsWebClubPDCModel_ID objModelID){
		strClientCode = objModelID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})
	
//Variable Declaration
	@Column(name="strMemCode")
	private String strMemCode;

	@Column(name="strChequeNo")
	private String strChequeNo;

	@Column(name="strDrawnOn")
	private String strDrawnOn;

	@Column(name="strType")
	private String strType;

	@Column(name="dblChequeAmt")
	private double dblChequeAmt;

	@Column(name="dteChequeDate")
	private String dteChequeDate;

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

//Setter-Getter Methods
	public String getStrMemCode(){
		return strMemCode;
	}
	public void setStrMemCode(String strMemCode){
		this. strMemCode = (String) setDefaultValue( strMemCode, "");
	}

	public String getStrChequeNo(){
		return strChequeNo;
	}
	public void setStrChequeNo(String strChequeNo){
		this. strChequeNo = (String) setDefaultValue( strChequeNo, "");
	}

	public String getStrDrawnOn(){
		return strDrawnOn;
	}
	public void setStrDrawnOn(String strDrawnOn){
		this. strDrawnOn = (String) setDefaultValue( strDrawnOn, "");
	}

	public String getStrType(){
		return strType;
	}
	public void setStrType(String strType){
		this. strType = (String) setDefaultValue( strType, "");
	}

	public double getDblChequeAmt(){
		return dblChequeAmt;
	}
	public void setDblChequeAmt(double dblChequeAmt){
		this. dblChequeAmt = (Double) setDefaultValue( dblChequeAmt, "NA");
	}

	public String getDteChequeDate(){
		return dteChequeDate;
	}
	public void setDteChequeDate(String dteChequeDate){
		this.dteChequeDate=dteChequeDate;
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
		this.strUserCreated=strUserCreated;
	}

	public String getStrUserEdited(){
		return strUserEdited;
	}
	public void setStrUserEdited(String strUserEdited){
		this.strUserEdited=strUserEdited;
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
