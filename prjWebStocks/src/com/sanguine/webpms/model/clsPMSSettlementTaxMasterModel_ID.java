package com.sanguine.webpms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")
public class clsPMSSettlementTaxMasterModel_ID implements Serializable {

	// Variable Declaration
	@Column(name = "strTaxCode")
	private String strTaxCode;

	@Column(name = "strClientCode")
	private String strClientCode;
	

	@Column(name = "strSettlementCode")
	private String strSettlementCode;

	public clsPMSSettlementTaxMasterModel_ID() {
	}

	public String getStrTaxCode() {
		return strTaxCode;
	}

	public void setStrTaxCode(String strTaxCode) {
		this.strTaxCode = strTaxCode;
	}

	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}

	public String getStrSettlementCode() {
		return strSettlementCode;
	}

	public void setStrSettlementCode(String strSettlementCode) {
		this.strSettlementCode = strSettlementCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((strClientCode == null) ? 0 : strClientCode.hashCode());
		result = prime
				* result
				+ ((strSettlementCode == null) ? 0 : strSettlementCode
						.hashCode());
		result = prime * result
				+ ((strTaxCode == null) ? 0 : strTaxCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		clsPMSSettlementTaxMasterModel_ID other = (clsPMSSettlementTaxMasterModel_ID) obj;
		if (strClientCode == null) {
			if (other.strClientCode != null)
				return false;
		} else if (!strClientCode.equals(other.strClientCode))
			return false;
		if (strSettlementCode == null) {
			if (other.strSettlementCode != null)
				return false;
		} else if (!strSettlementCode.equals(other.strSettlementCode))
			return false;
		if (strTaxCode == null) {
			if (other.strTaxCode != null)
				return false;
		} else if (!strTaxCode.equals(other.strTaxCode))
			return false;
		return true;
	}

	public clsPMSSettlementTaxMasterModel_ID(String strTaxCode,
			String strClientCode, String strSettlementCode) {
		super();
		this.strTaxCode = strTaxCode;
		this.strClientCode = strClientCode;
		this.strSettlementCode = strSettlementCode;
	}

	
	// HashCode and Equals Funtions
	

}
