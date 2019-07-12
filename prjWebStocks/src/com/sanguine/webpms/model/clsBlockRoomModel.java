package com.sanguine.webpms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	
	@Column(name = "strRoomCode")
	private String strRoomCode;
	
	@Column(name = "strRoomType")
	private String strRoomType;
	
	@Column(name = "strDocNo")
	private String strClientCode;
	
	@Column(name = "strRemark")
	private String strRemarks;
	
	@Column(name = "strReason")
	private String strReason;
	
	@Column(name = "dteValidFrom")
	private String dteValidFrom;
	
	@Column(name = "dteValidTo")
	private String dteValidTo;
	
	

}
