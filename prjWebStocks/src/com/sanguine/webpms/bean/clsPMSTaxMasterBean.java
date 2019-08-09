package com.sanguine.webpms.bean;

import java.util.List;

import com.sanguine.model.clsProdSuppMasterModel;
import com.sanguine.webpms.model.clsPMSSettlementMasterHdModel;
import com.sanguine.webpms.model.clsPMSSettlementTaxMasterModel;

public class clsPMSTaxMasterBean {
	// Variable Declaration
	private long intGId;

	private String strTaxCode;

	private String strTaxDesc;

	private String strDeptCode;

	private String strIncomeHeadCode;

	private String strTaxType;

	private String strTaxOnType;

	private double dblTaxValue;

	private String strTaxOn;

	private String strDeplomat;

	private String strLocalOrForeigner;

	private String dteValidFrom;

	private String dteValidTo;

	private String strTaxOnTaxCode;

	private String strTaxOnTaxable;

	private String strTaxGroupCode;

	private String strUserCreated;

	private String strUserEdited;

	private String dteDateCreated;

	private String dteDateEdited;

	private String strClientCode;

	private String strAccountCode;
	
	private double dblFromRate;
	
	private double dblToRate;
	
	private String strSettlementCode;
	
	private String strSettlementDesc;
	
	private List<clsPMSSettlementTaxMasterModel> listSettlement;
	

	// Setter-Getter Methods
	public long getIntGId() {
		return intGId;
	}

	public void setIntGId(long intGId) {
		this.intGId = intGId;
	}

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

	public String getStrDeptCode() {
		return strDeptCode;
	}

	public void setStrDeptCode(String strDeptCode) {
		this.strDeptCode = strDeptCode;
	}

	public String getStrIncomeHeadCode() {
		return strIncomeHeadCode;
	}

	public void setStrIncomeHeadCode(String strIncomeHeadCode) {
		this.strIncomeHeadCode = strIncomeHeadCode;
	}

	public String getStrTaxType() {
		return strTaxType;
	}

	public void setStrTaxType(String strTaxType) {
		this.strTaxType = strTaxType;
	}

	public double getDblTaxValue() {
		return dblTaxValue;
	}

	public void setDblTaxValue(double dblTaxValue) {
		this.dblTaxValue = dblTaxValue;
	}

	public String getStrTaxOn() {
		return strTaxOn;
	}

	public void setStrTaxOn(String strTaxOn) {
		this.strTaxOn = strTaxOn;
	}

	public String getStrDeplomat() {
		return strDeplomat;
	}

	public void setStrDeplomat(String strDeplomat) {
		this.strDeplomat = strDeplomat;
	}

	public String getStrLocalOrForeigner() {
		return strLocalOrForeigner;
	}

	public void setStrLocalOrForeigner(String strLocalOrForeigner) {
		this.strLocalOrForeigner = strLocalOrForeigner;
	}

	public String getDteValidFrom() {
		return dteValidFrom;
	}

	public void setDteValidFrom(String dteValidFrom) {
		this.dteValidFrom = dteValidFrom;
	}

	public String getDteValidTo() {
		return dteValidTo;
	}

	public void setDteValidTo(String dteValidTo) {
		this.dteValidTo = dteValidTo;
	}

	public String getStrTaxOnTaxCode() {
		return strTaxOnTaxCode;
	}

	public void setStrTaxOnTaxCode(String strTaxOnTaxCode) {
		this.strTaxOnTaxCode = strTaxOnTaxCode;
	}

	public String getStrTaxOnTaxable() {
		return strTaxOnTaxable;
	}

	public void setStrTaxOnTaxable(String strTaxOnTaxable) {
		this.strTaxOnTaxable = strTaxOnTaxable;
	}

	public String getStrTaxGroupCode() {
		return strTaxGroupCode;
	}

	public void setStrTaxGroupCode(String strTaxGroupCode) {
		this.strTaxGroupCode = strTaxGroupCode;
	}

	public String getStrUserCreated() {
		return strUserCreated;
	}

	public void setStrUserCreated(String strUserCreated) {
		this.strUserCreated = strUserCreated;
	}

	public String getStrUserEdited() {
		return strUserEdited;
	}

	public void setStrUserEdited(String strUserEdited) {
		this.strUserEdited = strUserEdited;
	}

	public String getDteDateCreated() {
		return dteDateCreated;
	}

	public void setDteDateCreated(String dteDateCreated) {
		this.dteDateCreated = dteDateCreated;
	}

	public String getDteDateEdited() {
		return dteDateEdited;
	}

	public void setDteDateEdited(String dteDateEdited) {
		this.dteDateEdited = dteDateEdited;
	}

	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}

	public String getStrTaxOnType() {
		return strTaxOnType;
	}

	public void setStrTaxOnType(String strTaxOnType) {
		this.strTaxOnType = strTaxOnType;
	}

	public String getStrAccountCode() {
		return strAccountCode;
	}

	public void setStrAccountCode(String strAccountCode) {
		this.strAccountCode = strAccountCode;
	}

	public double getDblFromRate() {
		return dblFromRate;
	}

	public void setDblFromRate(double dblFromRate) {
		this.dblFromRate = dblFromRate;
	}

	public double getDblToRate() {
		return dblToRate;
	}

	public void setDblToRate(double dblToRate) {
		this.dblToRate = dblToRate;
	}

	public List<clsPMSSettlementTaxMasterModel> getListSettlement() {
		return listSettlement;
	}

	public void setListSettlement(List<clsPMSSettlementTaxMasterModel> listSettlement) {
		this.listSettlement = listSettlement;
	}

	public String getStrSettlementCode() {
		return strSettlementCode;
	}

	public void setStrSettlementCode(String strSettlementCode) {
		this.strSettlementCode = strSettlementCode;
	}

	public String getStrSettlementDesc() {
		return strSettlementDesc;
	}

	public void setStrSettlementDesc(String strSettlementDesc) {
		this.strSettlementDesc = strSettlementDesc;
	}

	

	
}
