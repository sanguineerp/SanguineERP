package com.sanguine.webpms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class clsFolioDtlModel implements Serializable {
	private static final long serialVersionUID = 1L;

	public clsFolioDtlModel() {
	}

	// Variable Declaration
	@Column(name = "dteDocDate")
	private String dteDocDate;

	@Column(name = "strDocNo")
	private String strDocNo;

	@Column(name = "strPerticulars")
	private String strPerticulars;

	@Column(name = "dblDebitAmt")
	private double dblDebitAmt;

	@Column(name = "dblCreditAmt")
	private double dblCreditAmt;

	@Column(name = "dblBalanceAmt")
	private double dblBalanceAmt;

	@Column(name = "strRevenueType")
	private String strRevenueType;

	@Column(name = "strRevenueCode")
	private String strRevenueCode;
	
	@Column(name = "dblQuantity",columnDefinition = "DECIMAL(18,4) NOT NULL default '0.0000'")
	private double dblQuantity;

	
	@Column(name = "strUserEdited",columnDefinition = "VARCHAR(50) NOT NULL DEFAULT ''")
	private String strUserEdited;
	
	@Column(name = "dteDateEdited",columnDefinition = "DATETIME NOT NULL DEFAULT '1900-01-01 00:00:00'")
	private String dteDateEdited;
	
	@Column(name = "strTransactionType",columnDefinition = "VARCHAR(50) NOT NULL DEFAULT ''")
	private String strTransactionType;
	
	
	
	// Setter-Getter Methods
	public String getDteDocDate() {
		return dteDocDate;
	}

	public void setDteDocDate(String dteDocDate) {
		this.dteDocDate = dteDocDate;
	}

	public String getStrDocNo() {
		return strDocNo;
	}

	public void setStrDocNo(String strDocNo) {
		this.strDocNo = (String) setDefaultValue(strDocNo, "");
	}

	public String getStrPerticulars() {
		return strPerticulars;
	}

	public void setStrPerticulars(String strPerticulars) {
		this.strPerticulars = (String) setDefaultValue(strPerticulars, "");
	}

	public double getDblDebitAmt() {
		return dblDebitAmt;
	}

	public void setDblDebitAmt(double dblDebitAmt) {
		this.dblDebitAmt = (Double) setDefaultValue(dblDebitAmt, "0.0000");
	}

	public double getDblCreditAmt() {
		return dblCreditAmt;
	}

	public void setDblCreditAmt(double dblCreditAmt) {
		this.dblCreditAmt = (Double) setDefaultValue(dblCreditAmt, "0.0000");
	}

	public double getDblBalanceAmt() {
		return dblBalanceAmt;
	}

	public void setDblBalanceAmt(double dblBalanceAmt) {
		this.dblBalanceAmt = (Double) setDefaultValue(dblBalanceAmt, "0.0000");
	}

	public String getStrRevenueType() {
		return strRevenueType;
	}

	public void setStrRevenueType(String strRevenueType) {
		this.strRevenueType = strRevenueType;
	}

	public String getStrRevenueCode() {
		return strRevenueCode;
	}

	public void setStrRevenueCode(String strRevenueCode) {
		this.strRevenueCode = strRevenueCode;
	}
	
	public double getDblQuantity() {
		return dblQuantity;
	}

	public void setDblQuantity(double dblQuantity) {
		this.dblQuantity = dblQuantity;
	}

	// Function to Set Default Values
	private Object setDefaultValue(Object value, Object defaultValue) {
		if (value != null && (value instanceof String && value.toString().length() > 0)) {
			return value;
		} else if (value != null && (value instanceof Double && value.toString().length() > 0)) {
			return value;
		} else if (value != null && (value instanceof Integer && value.toString().length() > 0)) {
			return value;
		} else if (value != null && (value instanceof Long && value.toString().length() > 0)) {
			return value;
		} else {
			return defaultValue;
		}
	}

	public String getStrUserEdited() {
		return strUserEdited;
	}

	public void setStrUserEdited(String strUserEdited) {
		this.strUserEdited = strUserEdited;
	}

	public String getDteDateEdited() {
		return dteDateEdited;
	}

	public void setDteDateEdited(String dteDateEdited) {
		this.dteDateEdited = dteDateEdited;
	}

	public String getStrTransactionType() {
		return strTransactionType;
	}

	public void setStrTransactionType(String strTransactionType) {
		this.strTransactionType = strTransactionType;
	}

	

	

}
