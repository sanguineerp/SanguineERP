package com.sanguine.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tblprodsuppmaster")
@IdClass(clsProdSuppMasterModel_ID.class)
@org.hibernate.annotations.Entity(dynamicInsert = true, dynamicUpdate = true)
public class clsProdSuppMasterModel {
	private String strSuppCode, strProdCode, strSuppUOM, dtLastDate, strLeadTime, strDefault, strSuppPartNo, strSuppPartDesc, strClientCode;
	private double dblLastCost, dblMaxQty, dblMargin, dblStandingOrder;
	@Transient
	private String strSuppName;

	private String strProdName;

	private double dblAMCAmt;
	
	private String dteInstallation;
	
	private int intWarrantyDays;
	
	public clsProdSuppMasterModel() {
	}

	public clsProdSuppMasterModel(clsProdSuppMasterModel_ID clsProdSuppMasterModel_ID) {
		this.strSuppCode = clsProdSuppMasterModel_ID.getStrSuppCode();
		this.strProdCode = clsProdSuppMasterModel_ID.getStrProdCode();
		this.strClientCode = clsProdSuppMasterModel_ID.getStrClientCode();
	}

	@Id
	@AttributeOverrides({ @AttributeOverride(name = "strSuppCode", column = @Column(name = "strSuppCode")), @AttributeOverride(name = "strProdCode", column = @Column(name = "strProdCode")), @AttributeOverride(name = "strClientCode", column = @Column(name = "strClientCode")) })
	/*
	 * @Column(name = "intId") private long intId;
	 */
	@Column(name = "strSuppCode")
	public String getStrSuppCode() {
		return strSuppCode;
	}

	public void setStrSuppCode(String strSuppCode) {
		this.strSuppCode = strSuppCode;
	}

	@Column(name = "strProdCode", columnDefinition = "VARCHAR(255) NOT NULL dafault ''")
	public String getStrProdCode() {
		return strProdCode;
	}

	public void setStrProdCode(String strProdCode) {
		this.strProdCode = strProdCode;
	}

	@Column(name = "strSuppUOM", columnDefinition = "VARCHAR(255) NOT NULL dafault ''")
	public String getStrSuppUOM() {
		return strSuppUOM;
	}

	public void setStrSuppUOM(String strSuppUOM) {
		this.strSuppUOM = strSuppUOM;
	}

	@Column(name = "dtLastDate", columnDefinition = "VARCHAR(255) NOT NULL dafault ''")
	public String getDtLastDate() {
		return dtLastDate;
	}

	public void setDtLastDate(String dtLastDate) {
		this.dtLastDate = dtLastDate;
	}

	@Column(name = "strLeadTime", columnDefinition = "VARCHAR(50) default ''")
	public String getStrLeadTime() {
		return strLeadTime;
	}

	public void setStrLeadTime(String strLeadTime) {
		this.strLeadTime =this.strDefault =(String) setDefaultValue(strLeadTime, "");  
	}

	@Column(name = "strDefault", columnDefinition = "VARCHAR(255) NOT NULL dafault ''")
	public String getStrDefault() {
		return strDefault;
	}

	public void setStrDefault(String strDefault) {
		this.strDefault =(String) setDefaultValue(strDefault, ""); 
	}

	@Column(name = "strSuppPartNo", columnDefinition = "VARCHAR(255) NOT NULL dafault ''")
	public String getStrSuppPartNo() {
		return strSuppPartNo;
	}

	public void setStrSuppPartNo(String strSuppPartNo) {
		this.strSuppPartNo = strSuppPartNo;
	}

	@Column(name = "strSuppPartDesc", columnDefinition = "VARCHAR(255) NOT NULL dafault ''")
	public String getStrSuppPartDesc() {
		return strSuppPartDesc;
	}

	public void setStrSuppPartDesc(String strSuppPartDesc) {
		this.strSuppPartDesc = strSuppPartDesc;
	}

	@Column(name = "strClientCode", columnDefinition = "VARCHAR(255) NOT NULL dafault ''")
	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}

	@Column(name = "dblLastCost", columnDefinition = "DECIMAL(18,4) NOT NULL dafault '0.0000'")
	public double getDblLastCost() {
		return dblLastCost;
	}

	public void setDblLastCost(double dblLastCost) {
		this.dblLastCost = dblLastCost;
	}

	@Column(name = "dblMaxQty", columnDefinition = "DECIMAL(18,4) NOT NULL dafault '0.0000'")
	public double getDblMaxQty() {
		return dblMaxQty;
	}

	public void setDblMaxQty(double dblMaxQty) {
		this.dblMaxQty =(Double) setDefaultValue(dblMaxQty, "0"); 
	}

	public String getStrSuppName() {
		return strSuppName;
	}

	public void setStrSuppName(String strSuppName) {
		this.strSuppName = strSuppName;
	}

	@Column(name = "dblMargin", columnDefinition = "DECIMAL(18,2) NOT NULL dafault '0.0000'")
	public double getDblMargin() {
		return dblMargin;
	}

	public void setDblMargin(double dblMargin) {
		this.dblMargin = (Double)setDefaultValue(dblMargin, "0"); 
	}

	@Transient
	public String getStrProdName() {
		return strProdName;
	}

	public void setStrProdName(String strProdName) {
		this.strProdName = strProdName;
	}

	public double getDblStandingOrder() {
		return dblStandingOrder;
	}

	public void setDblStandingOrder(double dblStandingOrder) {
		this.dblStandingOrder = (Double)setDefaultValue(dblStandingOrder, "0");  ;
	}

	public double getDblAMCAmt() {
		return dblAMCAmt;
	}

	public void setDblAMCAmt(double dblAMCAmt) {
		this.dblAMCAmt =(Double)setDefaultValue(dblAMCAmt, "0");
	}

	public String getDteInstallation() {
		return dteInstallation;
	}

	public void setDteInstallation(String dteInstallation) {
		this.dteInstallation = dteInstallation;
	}

	public int getIntWarrantyDays() {
		return intWarrantyDays;
	}

	public void setIntWarrantyDays(int intWarrantyDays) {
		this.intWarrantyDays = intWarrantyDays;
	}
	
	private Object setDefaultValue(Object value, Object defaultValue) {
		if (value != null && (value instanceof String && value.toString().length() > 0)) {
			return value;
		} else if (value != null && (value instanceof Double && value.toString().length() > 0)) {
			return value;
		} else if (value != null && (value instanceof Integer && value.toString().length() > 0)) {
			return value;
		} else if (value != null && (value instanceof Long && value.toString().length() > 0)) {
			return value;
		} else {
			return defaultValue;
		}
	}

}
