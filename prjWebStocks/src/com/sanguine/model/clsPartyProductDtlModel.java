package com.sanguine.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")

public class clsPartyProductDtlModel implements Serializable {
	
	//@Column(name = "strTaxCode", columnDefinition = "VARCHAR(20) NOT NULL default ''")
	private String strProdCode;
	private String strProdName;
	private String strProdAmount;
	public String getStrProdCode() {
		return strProdCode;
	}
	public void setStrProdCode(String strProdCode) {
		this.strProdCode = strProdCode;
	}
	public String getStrProdName() {
		return strProdName;
	}
	public void setStrProdName(String strProdName) {
		this.strProdName = strProdName;
	}
	public String getStrProdAmount() {
		return strProdAmount;
	}
	public void setStrProdAmount(String strProdAmount) {
		this.strProdAmount = strProdAmount;
	}

	
}
