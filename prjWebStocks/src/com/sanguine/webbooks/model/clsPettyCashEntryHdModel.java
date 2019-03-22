package com.sanguine.webbooks.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionOfElements;

@Entity
@Table(name = "tblpettycashhd")
@IdClass(clsPettyCashEntryHdModel_ID.class)
public class clsPettyCashEntryHdModel implements Serializable {
	private static final long serialVersionUID = 1L;

	public clsPettyCashEntryHdModel() {
	}

	public clsPettyCashEntryHdModel(clsPettyCashEntryHdModel_ID objModelID) {
		strVouchNo = objModelID.getStrVouchNo();
		strClientCode = objModelID.getStrClientCode();
	}
	
	@CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(name = "tblpettycashdtl", joinColumns = { @JoinColumn(name = "strClientCode"), @JoinColumn(name = "strVouchNo") })
	@Id
	@AttributeOverrides({ @AttributeOverride(name = "strVouchNo", column = @Column(name = "strVouchNo")), @AttributeOverride(name = "strClientCode", column = @Column(name = "strClientCode")) })
	private List<clsPettyCashEntryDtlModel> listDtlModel = new ArrayList<clsPettyCashEntryDtlModel>();
	
	
	// Variable Declaration
	@Column(name = "strVouchNo")
	private String strVouchNo;

	@Column(name = "intId")
	private long intId;

	@Column(name = "strNarration")
	private String strNarration;

	@Column(name = "dteVouchDate")
	private String dteVouchDate;
	
	@Column(name = "strClientCode")
	private String strClientCode;
	
	@Column(name = "strUserCreated", updatable = false)
	private String strUserCreated;

	@Column(name = "strUserEdited")
	private String strUserEdited;

	@Column(name = "dteDateCreated", updatable = false)
	private String dteDateCreated;

	@Column(name = "dteDateEdited")
	private String dteDateEdited;
	
	@Column(name = "dblGrandTotal")
	private double dblGrandTotal;

	public String getStrVouchNo() {
		return strVouchNo;
	}

	public void setStrVouchNo(String strVouchNo) {
		this.strVouchNo = strVouchNo;
	}

	public long getIntId() {
		return intId;
	}

	public void setIntId(long intId) {
		this.intId = intId;
	}

	public String getStrNarration() {
		return strNarration;
	}

	public void setStrNarration(String strNarration) {
		this.strNarration = strNarration;
	}

	public String getDteVouchDate() {
		return dteVouchDate;
	}

	public void setDteVouchDate(String dteVouchDate) {
		this.dteVouchDate = dteVouchDate;
	}

	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
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

	public List<clsPettyCashEntryDtlModel> getListDtlModel() {
		return listDtlModel;
	}

	public void setListDtlModel(List<clsPettyCashEntryDtlModel> listDtlModel) {
		this.listDtlModel = listDtlModel;
	}

	public double getDblGrandTotal() {
		return dblGrandTotal;
	}

	public void setDblGrandTotal(double dblGrandTotal) {
		this.dblGrandTotal = dblGrandTotal;
	}



}
