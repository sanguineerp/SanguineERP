package com.sanguine.webbooks.model;

import javax.persistence.Embeddable;

@Embeddable
public class clsPettyCashEntryDtlModel {

	
	private String strExpCode;

	private String strNarration;

	private double dblAmount;

	public String getStrExpCode() {
		return strExpCode;
	}

	public void setStrExpCode(String strExpCode) {
		this.strExpCode = strExpCode;
	}

	public String getStrNarration() {
		return strNarration;
	}

	public void setStrNarration(String strNarration) {
		this.strNarration = strNarration;
	}

	public double getDblAmount() {
		return dblAmount;
	}

	public void setDblAmount(double dblAmount) {
		this.dblAmount = dblAmount;
	}
	
	
}
