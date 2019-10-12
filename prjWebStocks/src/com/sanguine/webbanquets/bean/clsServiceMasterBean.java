package com.sanguine.webbanquets.bean;

public class clsServiceMasterBean{
//Variable Declaration
	private String strServiceCode;

	private String strServiceName;
	
	private String strPropertyCode;

	private String strClientCode;

	private String strUserCreated;

	private String strUserEdited;

	private String dteDateCreated;

	private String dteDateEdited;

	private String strOperationalYN;
	
	private String strDeptCode;
	
	private double dblRate;
	
	private String strTaxIndicator;
	
	

public double getDblRate() {
		return dblRate;
	}
	public void setDblRate(double dblRate) {
		this.dblRate = dblRate;
	}
	//Setter-Getter Methods
	public String getStrServiceCode(){
		return strServiceCode;
	}
	public void setStrServiceCode(String strServiceCode){
		this.strServiceCode=strServiceCode;
	}

	public String getStrServiceName() {
		return strServiceName;
	}
	public void setStrServiceName(String strServiceName) {
		this.strServiceName = strServiceName;
	}
	public String getStrPropertyCode(){
		return strPropertyCode;
	}
	public void setStrPropertyCode(String strPropertyCode){
		this.strPropertyCode=strPropertyCode;
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this.strClientCode=strClientCode;
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

	public String getStrOperationalYN(){
		return strOperationalYN;
	}
	public void setStrOperationalYN(String strOperationalYN){
		this.strOperationalYN=strOperationalYN;
	}
	public String getStrDeptCode() {
		return strDeptCode;
	}
	public void setStrDeptCode(String strDeptCode) {
		this.strDeptCode = strDeptCode;
	}
	public String getStrTaxIndicator() {
		return strTaxIndicator;
	}
	public void setStrTaxIndicator(String strTaxIndicator) {
		this.strTaxIndicator = strTaxIndicator;
	}



}
