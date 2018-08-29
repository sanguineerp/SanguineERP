package com.sanguine.webbooks.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class clsSundryDebtorMasterItemDetialModel implements Serializable {

	private static final long serialVersionUID = 1L;
	public clsSundryDebtorMasterItemDetialModel(){
		
	}
	
	private String strProductCode;

	private String strProductName;

	private double dblAMCAmt;

	private double dblLicenceAmt;
	
	private String strAMCType;
	
	private String dteInstallation;
	
	private int intWarrantyDays;
	
	public String getStrProductCode() {
		return strProductCode;
	}

	public void setStrProductCode(String strProductCode) {
		this.strProductCode = strProductCode;
	}

	public String getStrProductName() {
		return strProductName;
	}

	public void setStrProductName(String strProductName) {
		this.strProductName = strProductName;
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

	public String getStrAMCType() {
		return strAMCType;
	}

	public void setStrAMCType(String strAMCType) {
		this.strAMCType = strAMCType;
	}

	public String getDteInstallation() {
		return dteInstallation;
	}

	public void setDteInstallation(String dteInstallation) {
		this.dteInstallation = dteInstallation;
	}

	public int getIntWarrantyDays() {
		return intWarrantyDays;
	}

	public void setIntWarrantyDays(int intWarrantyDays) {
		this.intWarrantyDays = intWarrantyDays;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	// Function to Set Default Values
		private Object setDefaultValue(Object value, Object defaultValue)
		{
			if (value != null && (value instanceof String && value.toString().length() > 0))
			{
				return value;
			}
			else if (value != null && (value instanceof Double && value.toString().length() > 0))
			{
				return value;
			}
			else if (value != null && (value instanceof Integer && value.toString().length() > 0))
			{
				return value;
			}
			else if (value != null && (value instanceof Long && value.toString().length() > 0))
			{
				return value;
			}
			else
			{
				return defaultValue;
			}
		}

}
