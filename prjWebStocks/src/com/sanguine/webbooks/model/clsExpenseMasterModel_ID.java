package com.sanguine.webbooks.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;


@Embeddable
@SuppressWarnings("serial")

public class clsExpenseMasterModel_ID implements Serializable {

	@Column(name = "strExpCode")
	private String strExpCode;

	@Column(name = "strClientCode")
	private String strClientCode;

	public clsExpenseMasterModel_ID() {
	}

	public clsExpenseMasterModel_ID(String strExpCode, String strClientCode) {
		this.strExpCode = strExpCode;
		this.strClientCode = strClientCode;
	}

	public String getStrExpCode() {
		return strExpCode;
	}

	public void setStrExpCode(String strExpCode) {
		this.strExpCode = strExpCode;
	}

	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}

	@Override
	public boolean equals(Object obj) {
		clsExpenseMasterModel_ID objModelId = (clsExpenseMasterModel_ID) obj;
		if (this.strExpCode.equals(objModelId.getStrExpCode()) && this.strClientCode.equals(objModelId.getStrClientCode())) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public int hashCode() {
		return this.strExpCode.hashCode() + this.strClientCode.hashCode();
	}

}
