package com.sanguine.webpms.bean;

public class clsTaxProductDtl {

	private String strTaxProdCode;

	private String strTaxProdName;

	private double dblTaxProdAmt;

	private String strDeptCode;
	
	private double dblDiscountOnTariff;
	
	private double dblTotalExtraBedAmt;
	
	
	public String getStrTaxProdCode() {
		return strTaxProdCode;
	}

	public void setStrTaxProdCode(String strTaxProdCode) {
		this.strTaxProdCode = strTaxProdCode;
	}

	public String getStrTaxProdName() {
		return strTaxProdName;
	}

	public void setStrTaxProdName(String strTaxProdName) {
		this.strTaxProdName = strTaxProdName;
	}

	public double getDblTaxProdAmt() {
		return dblTaxProdAmt;
	}

	public void setDblTaxProdAmt(double dblTaxProdAmt) {
		this.dblTaxProdAmt = dblTaxProdAmt;
	}

	public String getStrDeptCode() {
		return strDeptCode;
	}

	public void setStrDeptCode(String strDeptCode) {
		this.strDeptCode = strDeptCode;
	}

	public double getDblDiscountOnTariff() {
		return dblDiscountOnTariff;
	}

	public void setDblDiscountOnTariff(double dblDiscountOnTariff) {
		this.dblDiscountOnTariff = dblDiscountOnTariff;
	}



	public double getDblTotalExtraBedAmt() {
		return dblTotalExtraBedAmt;
	}

	public void setDblTotalExtraBedAmt(double dblTotalExtraBedAmt) {
		this.dblTotalExtraBedAmt = dblTotalExtraBedAmt;
	}

}
