package com.sanguine.webpms.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")
public class clsBlockRoom_ID implements Serializable{
	
	
	
	@Column(name = "strRoomCode")
	private String strRoomCode;

	@Column(name = "strClientCode")
	private String strClientCode;
	
	
	public clsBlockRoom_ID() {
	}


	public clsBlockRoom_ID(String strRoomCode, String strClientCode) {
		this.strRoomCode = strRoomCode;
		this.strClientCode = strClientCode;
	}


	public String getStrRoomCode() {
		return strRoomCode;
	}


	public void setStrRoomCode(String strRoomCode) {
		this.strRoomCode = strRoomCode;
	}


	public String getStrClientCode() {
		return strClientCode;
	}


	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}


	@Override
	public int hashCode() {
		return this.strRoomCode.hashCode() + this.strClientCode.hashCode();
	}


	@Override
	public boolean equals(Object obj) {
		clsBlockRoom_ID objModelId = (clsBlockRoom_ID) obj;
		if (this.strRoomCode.equals(objModelId.getStrRoomCode()) && this.strClientCode.equals(objModelId.getStrClientCode())) {
			return true;
		} else {
			return false;
		}
	}

	
}
