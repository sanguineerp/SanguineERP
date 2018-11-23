package com.sanguine.crm.bean;

import java.util.List;

public class clsAMCFlashBean {

	
	private String dteFromDate;
	
	private String dteToDate;
	
	private String strCustomerName;
	
	private String dteInstallation;
	
	private String dteExpiry;
	
	private double dblAMCAmt;
	
	private double dblLicenceAmt;
	
	private String strCustCode;
	
	private String strCustomerCode;
	
	private String strInvCode;
	
	private String dteInvDate;
	
	private String strSettlementCode;
	
	private List <clsAMCFlashDtlBean>listAMCDtl;
	

	public String getDteFromDate() {
		return dteFromDate;
	}

	public void setDteFromDate(String dteFromDate) {
		this.dteFromDate = dteFromDate;
	}

	public String getDteToDate() {
		return dteToDate;
	}

	public void setDteToDate(String dteToDate) {
		this.dteToDate = dteToDate;
	}

	public String getStrCustomerName() {
		return strCustomerName;
	}

	public void setStrCustomerName(String strCustomerName) {
		this.strCustomerName = strCustomerName;
	}

	public String getDteInstallation() {
		return dteInstallation;
	}

	public void setDteInstallation(String dteInstallation) {
		this.dteInstallation = dteInstallation;
	}

	public String getDteExpiry() {
		return dteExpiry;
	}

	public void setDteExpiry(String dteExpiry) {
		this.dteExpiry = dteExpiry;
	}

	public double getDblAMCAmt() {
		return dblAMCAmt;
	}

	public void setDblAMCAmt(double dblAMCAmt) {
		this.dblAMCAmt = dblAMCAmt;
	}

	public double getDblLicenceAmt() {
		return dblLicenceAmt;
	}

	public void setDblLicenceAmt(double dblLicenceAmt) {
		this.dblLicenceAmt = dblLicenceAmt;
	}

	public String getStrCustCode() {
		return strCustCode;
	}

	public void setStrCustCode(String strCustCode) {
		this.strCustCode = strCustCode;
	}

	public List<clsAMCFlashDtlBean> getListAMCDtl() {
		return listAMCDtl;
	}

	public void setListAMCDtl(List<clsAMCFlashDtlBean> listAMCDtl) {
		this.listAMCDtl = listAMCDtl;
	}

	public String getStrCustomerCode() {
		return strCustomerCode;
	}

	public void setStrCustomerCode(String strCustomerCode) {
		this.strCustomerCode = strCustomerCode;
	}

	public String getStrInvCode() {
		return strInvCode;
	}

	public void setStrInvCode(String strInvCode) {
		this.strInvCode = strInvCode;
	}

	public String getDteInvDate() {
		return dteInvDate;
	}

	public void setDteInvDate(String dteInvDate) {
		this.dteInvDate = dteInvDate;
	}

	public String getStrSettlementCode() {
		return strSettlementCode;
	}

	public void setStrSettlementCode(String strSettlementCode) {
		this.strSettlementCode = strSettlementCode;
	}

	

}
