package com.sanguine.webpms.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class clsFolioTaxDtl implements Serializable {
	private static final long serialVersionUID = 1L;

	public clsFolioTaxDtl() {
	}

	private String strTaxCode;

	private String strTaxDesc;

	private double dblTaxableAmt;

	private double dblTaxAmt;

	private String strDocNo;

	public String getStrTaxCode() {
		return strTaxCode;
	}

	public void setStrTaxCode(String strTaxCode) {
		this.strTaxCode = strTaxCode;
	}

	public String getStrTaxDesc() {
		return strTaxDesc;
	}

	public void setStrTaxDesc(String strTaxDesc) {
		this.strTaxDesc = strTaxDesc;
	}

	public double getDblTaxableAmt() {
		return dblTaxableAmt;
	}

	public void setDblTaxableAmt(double dblTaxableAmt) {
		this.dblTaxableAmt = dblTaxableAmt;
	}

	public double getDblTaxAmt() {
		return dblTaxAmt;
	}

	public void setDblTaxAmt(double dblTaxAmt) {
		this.dblTaxAmt = dblTaxAmt;
	}

	public String getStrDocNo() {
		return strDocNo;
	}

	public void setStrDocNo(String strDocNo) {
		this.strDocNo = strDocNo;
	}
}
