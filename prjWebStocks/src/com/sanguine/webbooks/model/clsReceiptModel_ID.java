package com.sanguine.webbooks.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.Column;

@Embeddable
@SuppressWarnings("serial")
public class clsReceiptModel_ID implements Serializable {

	// Variable Declaration
	@Column(name = "strVouchNo")
	private String strVouchNo;

	@Column(name = "strClientCode")
	private String strClientCode;

	public clsReceiptModel_ID() {
	}

	public clsReceiptModel_ID(String strVouchNo, String strClientCode) {
		this.strVouchNo = strVouchNo;
		this.strClientCode = strClientCode;
	}

	// Setter-Getter Methods
	public String getStrVouchNo() {
		return strVouchNo;
	}

	public void setStrVouchNo(String strVouchNo) {
		this.strVouchNo = strVouchNo;
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
		clsReceiptModel_ID objModelId = (clsReceiptModel_ID) obj;
		if (this.strVouchNo.equals(objModelId.getStrVouchNo()) && this.strClientCode.equals(objModelId.getStrClientCode())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strVouchNo.hashCode() + this.strClientCode.hashCode();
	}

}
