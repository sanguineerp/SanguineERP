package com.sanguine.webpms.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;

import org.hibernate.annotations.CollectionOfElements;

import com.sanguine.webpms.bean.clsPMSPaymentReceiptDtlBean;

@Entity
@Table(name = "tblreceipthd")
@IdClass(clsPMSPaymentModel_ID.class)
public class clsPMSPaymentHdModel implements Serializable {
	private static final long serialVersionUID = 1L;

	public clsPMSPaymentHdModel() {
	}

	public clsPMSPaymentHdModel(clsPMSPaymentModel_ID objModelID) {
		strReceiptNo = objModelID.getStrReceiptNo();
		strClientCode = objModelID.getStrClientCode();
	}

	@CollectionOfElements(fetch = FetchType.LAZY)
	@JoinTable(name = "tblreceiptdtl", joinColumns = { @JoinColumn(name = "strClientCode"), @JoinColumn(name = "strReceiptNo") })
	@Id
	@AttributeOverrides({ @AttributeOverride(name = "strClientCode", column = @Column(name = "strClientCode")), @AttributeOverride(name = "strReceiptNo", column = @Column(name = "strReceiptNo")) })
	private List<clsPMSPaymentReceiptDtl> listPaymentRecieptDtlModel;

	// Variable Declaration
	@Column(name = "strReceiptNo")
	private String strReceiptNo;

	@Column(name = "strRegistrationNo")
	private String strRegistrationNo;

	@Column(name = "strReservationNo")
	private String strReservationNo;

	@Column(name = "strFolioNo")
	private String strFolioNo;

	@Column(name = "dblReceiptAmt")
	private double dblReceiptAmt;

	@Column(name = "dblPaidAmt")
	private double dblPaidAmt;

	@Column(name = "strAgainst")
	private String strAgainst;

	@Column(name = "strClientCode")
	private String strClientCode;

	@Column(name = "dteDateCreated", updatable = false)
	private String dteDateCreated;

	@Column(name = "dteDateEdited")
	private String dteDateEdited;

	@Column(name = "strUserCreated", updatable = false)
	private String strUserCreated;

	@Column(name = "strUserEdited")
	private String strUserEdited;

	@Column(name = "dteReceiptDate")
	private String dteReceiptDate;

	@Column(name = "strCheckInNo")
	private String strCheckInNo;

	@Column(name = "strBillNo")
	private String strBillNo;

	@Column(name = "strFlagOfAdvAmt")
	private String strFlagOfAdvAmt;

	// Setter-Getter Methods
	public String getStrReceiptNo() {
		return strReceiptNo;
	}

	public void setStrReceiptNo(String strReceiptNo) {
		this.strReceiptNo = (String) setDefaultValue(strReceiptNo, "");
	}

	public String getStrRegistrationNo() {
		return strRegistrationNo;
	}

	public void setStrRegistrationNo(String strRegistrationNo) {
		this.strRegistrationNo = (String) setDefaultValue(strRegistrationNo, "");
	}

	public String getStrReservationNo() {
		return strReservationNo;
	}

	public void setStrReservationNo(String strReservationNo) {
		this.strReservationNo = (String) setDefaultValue(strReservationNo, "");
	}

	public String getStrFolioNo() {
		return strFolioNo;
	}

	public void setStrFolioNo(String strFolioNo) {
		this.strFolioNo = (String) setDefaultValue(strFolioNo, "");
	}

	public double getDblReceiptAmt() {
		return dblReceiptAmt;
	}

	public void setDblReceiptAmt(double dblReceiptAmt) {
		this.dblReceiptAmt = (Double) setDefaultValue(dblReceiptAmt, "0.0000");
	}

	public double getDblPaidAmt() {
		return dblPaidAmt;
	}

	public void setDblPaidAmt(double dblPaidAmt) {
		this.dblPaidAmt = (Double) setDefaultValue(dblPaidAmt, "0.0000");
	}

	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = (String) setDefaultValue(strClientCode, "");
	}

	public String getStrCheckInNo() {
		return strCheckInNo;
	}

	public void setStrCheckInNo(String strCheckInNo) {
		this.strCheckInNo = strCheckInNo;
	}

	// Function to Set Default Values
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

	public String getStrAgainst() {
		return strAgainst;
	}

	public void setStrAgainst(String strAgainst) {
		this.strAgainst = strAgainst;
	}

	public List<clsPMSPaymentReceiptDtl> getListPaymentRecieptDtlModel() {
		return listPaymentRecieptDtlModel;
	}

	public void setListPaymentRecieptDtlModel(List<clsPMSPaymentReceiptDtl> listPaymentRecieptDtlModel) {
		this.listPaymentRecieptDtlModel = listPaymentRecieptDtlModel;
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

	public String getDteReceiptDate() {
		return dteReceiptDate;
	}

	public void setDteReceiptDate(String dteReceiptDate) {
		this.dteReceiptDate = dteReceiptDate;
	}

	public String getStrBillNo() {
		return strBillNo;
	}

	public void setStrBillNo(String strBillNo) {
		this.strBillNo = strBillNo;
	}

	public String getStrFlagOfAdvAmt() {
		return strFlagOfAdvAmt;
	}

	public void setStrFlagOfAdvAmt(String strFlagOfAdvAmt) {
		this.strFlagOfAdvAmt = strFlagOfAdvAmt;
	}

}
