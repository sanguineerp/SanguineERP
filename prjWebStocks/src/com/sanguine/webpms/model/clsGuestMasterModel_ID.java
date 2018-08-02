package com.sanguine.webpms.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Column;

@Embeddable
@SuppressWarnings("serial")
public class clsGuestMasterModel_ID implements Serializable {

	// Variable Declaration
	@Column(name = "strGuestCode")
	private String strGuestCode;

	@Column(name = "strClientCode")
	private String strClientCode;

	public clsGuestMasterModel_ID() {
	}

	public clsGuestMasterModel_ID(String strGuestCode, String strClientCode) {
		this.strGuestCode = strGuestCode;
		this.strClientCode = strClientCode;
	}

	// Setter-Getter Methods
	public String getStrGuestCode() {
		return strGuestCode;
	}

	public void setStrGuestCode(String strGuestCode) {
		this.strGuestCode = strGuestCode;
	}

	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}

	// HashCode and Equals Funtions
	@Override
	public boolean equals(Object obj) {
		clsGuestMasterModel_ID objModelId = (clsGuestMasterModel_ID) obj;
		if (this.strGuestCode.equals(objModelId.getStrGuestCode()) && this.strClientCode.equals(objModelId.getStrClientCode())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strGuestCode.hashCode() + this.strClientCode.hashCode();
	}

}
