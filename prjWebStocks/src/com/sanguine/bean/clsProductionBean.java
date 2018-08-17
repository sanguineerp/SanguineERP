package com.sanguine.bean;

import java.util.List;

import com.sanguine.model.clsProductionDtlModel;

public class clsProductionBean {
	private String strPDCode, dtPDDate, strLocCode, strNarration;
	private String strWOCode, strUserCreated, dtCreatedDate, strUserModified, dtLastModified, strAuthorise, strClientCode;
	private long Intid;

	private String strPOSItemCode,strProdCode,strProdName,strUOM,strProdType;
	private double dblQty;
	
	public long getIntid() {
		return Intid;
	}

	public void setIntid(long intid) {
		Intid = intid;
	}

	private List<clsProductionDtlModel> listProductionDtl;

	public List<clsProductionDtlModel> getListProductionDtl() {
		return listProductionDtl;
	}

	public void setListProductionDtl(List<clsProductionDtlModel> listProductionDtl) {
		this.listProductionDtl = listProductionDtl;
	}

	public String getStrPDCode() {
		return strPDCode;
	}

	public void setStrPDCode(String strPDCode) {
		this.strPDCode = strPDCode;
	}

	public String getDtPDDate() {
		return dtPDDate;
	}

	public void setDtPDDate(String dtPDDate) {
		this.dtPDDate = dtPDDate;
	}

	public String getStrLocCode() {
		return strLocCode;
	}

	public void setStrLocCode(String strLocCode) {
		this.strLocCode = strLocCode;
	}

	public String getStrNarration() {
		return strNarration;
	}

	public void setStrNarration(String strNarration) {
		this.strNarration = strNarration;
	}

	public String getStrWOCode() {
		return strWOCode;
	}

	public void setStrWOCode(String strWOCode) {
		this.strWOCode = strWOCode;
	}

	public String getStrUserCreated() {
		return strUserCreated;
	}

	public void setStrUserCreated(String strUserCreated) {
		this.strUserCreated = strUserCreated;
	}

	public String getDtCreatedDate() {
		return dtCreatedDate;
	}

	public void setDtCreatedDate(String dtCreatedDate) {
		this.dtCreatedDate = dtCreatedDate;
	}

	public String getStrUserModified() {
		return strUserModified;
	}

	public void setStrUserModified(String strUserModified) {
		this.strUserModified = strUserModified;
	}

	public String getDtLastModified() {
		return dtLastModified;
	}

	public void setDtLastModified(String dtLastModified) {
		this.dtLastModified = dtLastModified;
	}

	public String getStrAuthorise() {
		return strAuthorise;
	}

	public void setStrAuthorise(String strAuthorise) {
		this.strAuthorise = strAuthorise;
	}

	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}

	public String getStrPOSItemCode() {
		return strPOSItemCode;
	}

	public void setStrPOSItemCode(String strPOSItemCode) {
		this.strPOSItemCode = strPOSItemCode;
	}

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

	

	public String getStrUOM() {
		return strUOM;
	}

	public void setStrUOM(String strUOM) {
		this.strUOM = strUOM;
	}

	public String getStrProdType() {
		return strProdType;
	}

	public void setStrProdType(String strProdType) {
		this.strProdType = strProdType;
	}

	public double getDblQty() {
		return dblQty;
	}

	public void setDblQty(double dblQty) {
		this.dblQty = dblQty;
	}

	
}
