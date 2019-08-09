package com.sanguine.webpms.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
/*
@Entity
@Table(name = "tblsettlementtax")
@IdClass(clsPMSSettlementTaxMasterModel_ID.class)*/
import javax.persistence.Transient;

@Embeddable
public class clsPMSSettlementTaxMasterModel implements Serializable {
	private static final long serialVersionUID = 1L;

	public clsPMSSettlementTaxMasterModel() {
	}

	/*public clsPMSSettlementTaxMasterModel(clsPMSSettlementTaxMasterModel_ID objModelID) {
		strTaxCode = objModelID.getStrTaxCode();
		strClientCode = objModelID.getStrClientCode();
		strSettlementCode = objModelID.getStrSettlementCode();
	}*/

	/*@Id
	@AttributeOverrides({ @AttributeOverride(name = "strTaxCode", column = @Column(name = "strTaxCode")), @AttributeOverride(name = "strSettlementCode", column = @Column(name = "strSettlementCode")), @AttributeOverride(name = "strClientCode", column = @Column(name = "strClientCode")) })
	// Variable Declaration
	@Column(name = "strTaxCode")
*///	private String strTaxCode;

//	@Column(name = "strSettlementCode")
	private String strSettlementCode;

	//@Column(name = "strSettlementName")
	private String strSettlementName;

	//@Column(name = "strApplicable")
	private String strApplicable;

	//@Column(name = "dteFrom")
	private String dteFrom;

	//@Column(name = "dteTo")
	private String dteTo;

	//@Column(name = "strUserCreated")
	private String strUserCreated;

	//@Column(name = "strUserEdited")
	private String strUserEdited;

	//@Column(name = "dteDateCreated")
	private String dteDateCreated;

	//@Column(name = "dteDateEdited")
	private String dteDateEdited;

	//@Column(name = "strClientCode")
	//private String strClientCode;

	
	/*public String getStrTaxCode() {
		return strTaxCode;
	}

	public void setStrTaxCode(String strTaxCode) {
		this.strTaxCode = strTaxCode;
	}*/

	public String getStrSettlementCode() {
		return strSettlementCode;
	}

	public void setStrSettlementCode(String strSettlementCode) {
		this.strSettlementCode = strSettlementCode;
	}

	public String getStrSettlementName() {
		return strSettlementName;
	}

	public void setStrSettlementName(String strSettlementName) {
		this.strSettlementName = strSettlementName;
	}

	public String getStrApplicable() {
		return strApplicable;
	}

	public void setStrApplicable(String strApplicable) {
		this.strApplicable = strApplicable;
	}

	public String getDteFrom() {
		return dteFrom;
	}

	public void setDteFrom(String dteFrom) {
		this.dteFrom = dteFrom;
	}

	public String getDteTo() {
		return dteTo;
	}

	public void setDteTo(String dteTo) {
		this.dteTo = dteTo;
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
/*
	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}
*/

	public String getStrUserCreated() {
		return strUserCreated;
	}

	public void setStrUserCreated(String strUserCreated) {
		this.strUserCreated = strUserCreated;
	}

	
	

	// Setter-Getter Methods

	

}
