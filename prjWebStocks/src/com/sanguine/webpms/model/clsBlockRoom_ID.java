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
		super();
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
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((strClientCode == null) ? 0 : strClientCode.hashCode());
		result = prime * result
				+ ((strRoomCode == null) ? 0 : strRoomCode.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		clsBlockRoom_ID other = (clsBlockRoom_ID) obj;
		if (strClientCode == null) {
			if (other.strClientCode != null)
				return false;
		} else if (!strClientCode.equals(other.strClientCode))
			return false;
		if (strRoomCode == null) {
			if (other.strRoomCode != null)
				return false;
		} else if (!strRoomCode.equals(other.strRoomCode))
			return false;
		return true;
	}

	
}
