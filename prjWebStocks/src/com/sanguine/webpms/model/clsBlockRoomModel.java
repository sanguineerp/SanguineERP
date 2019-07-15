package com.sanguine.webpms.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Entity
@Table(name = "tblblockroom")
@IdClass(clsBlockRoom_ID.class)
public class clsBlockRoomModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	public clsBlockRoomModel(clsBlockRoom_ID objModelID) {
		strRoomCode = objModelID.getStrRoomCode();
		strClientCode = objModelID.getStrClientCode();
	}
	
	public clsBlockRoomModel(String strRoomCode2, String clientCode) {
		// TODO Auto-generated constructor stub
	}

	
	@Id
	@AttributeOverrides({ @AttributeOverride(name = "strRoomCode", column = @Column(name = "strRoomCode")), @AttributeOverride(name = "strClientCode", column = @Column(name = "strClientCode")) })
	
	@Column(name = "strRoomCode")
	private String strRoomCode;
	
	@Column(name = "strRoomType")
	private String strRoomType;
	
	@Column(name = "strClientCode")
	private String strClientCode;
	
	@Column(name = "strRemark")
	private String strRemarks;
	
	@Column(name = "strReason")
	private String strReason;
	
	@Column(name = "dteValidFrom")
	private String dteValidFrom;
	
	@Column(name = "dteValidTo")
	private String dteValidTo;


	public String getStrRoomCode() {
		return strRoomCode;
	}

	public void setStrRoomCode(String strRoomCode) {
		this.strRoomCode = strRoomCode;
	}

	public String getStrRoomType() {
		return strRoomType;
	}

	public void setStrRoomType(String strRoomType) {
		this.strRoomType = strRoomType;
	}

	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}

	public String getStrRemarks() {
		return strRemarks;
	}

	public void setStrRemarks(String strRemarks) {
		this.strRemarks = strRemarks;
	}

	public String getStrReason() {
		return strReason;
	}

	public void setStrReason(String strReason) {
		this.strReason = strReason;
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

	
	
	
	
	

}
