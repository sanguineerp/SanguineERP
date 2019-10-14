package com.sanguine.webbanquets.bean;

public class clsBanquetMasterBean{
//Variable Declaration
	private String strBanquetCode;

	private String strBanquetName;

	private long intId;

	private double dblRate;

	private String strTaxIndicator;

	private String strOperational;
	
	private String strUserCreated;
	
	private String strUserEdited;
	private String dteDateCreated;
	private String dteDateEdited;

	private String strClientCode;
	
	private String strBanquetTypeCode;

//Setter-Getter Methods
	public String getStrBanquetCode(){
		return strBanquetCode;
	}
	public void setStrBanquetCode(String strBanquetCode){
		this.strBanquetCode=strBanquetCode;
	}

	public String getStrBanquetName(){
		return strBanquetName;
	}
	public void setStrBanquetName(String strBanquetName){
		this.strBanquetName=strBanquetName;
	}

	public long getIntId(){
		return intId;
	}
	public void setIntId(long intId){
		this.intId=intId;
	}

	public double getDblRate(){
		return dblRate;
	}
	public void setDblRate(double dblRate){
		this.dblRate=dblRate;
	}

	public String getStrTaxIndicator(){
		return strTaxIndicator;
	}
	public void setStrTaxIndicator(String strTaxIndicator){
		this.strTaxIndicator=strTaxIndicator;
	}

	public String getStrOperational(){
		return strOperational;
	}
	public void setStrOperational(String strOperational){
		this.strOperational=strOperational;
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this.strClientCode=strClientCode;
	}
	public String getStrUserCreated() {
		return strUserCreated;
	}
	public void setStrUserCreated(String strUserCreated) {
		this.strUserCreated = strUserCreated;
	}
	public String getStrUserEdited() {
		return strUserEdited;
	}
	public void setStrUserEdited(String strUserEdited) {
		this.strUserEdited = strUserEdited;
	}
	public String getDteDateCreated() {
		return dteDateCreated;
	}
	public void setDteDateCreated(String dteDateCreated) {
		this.dteDateCreated = dteDateCreated;
	}
	public String getDteDateEdited() {
		return dteDateEdited;
	}
	public void setDteDateEdited(String dteDateEdited) {
		this.dteDateEdited = dteDateEdited;
	}
	public String getStrBanquetTypeCode() {
		return strBanquetTypeCode;
	}
	public void setStrBanquetTypeCode(String strBanquetTypeCode) {
		this.strBanquetTypeCode = strBanquetTypeCode;
	}



}
