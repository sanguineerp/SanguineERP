package com.sanguine.webclub.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@SuppressWarnings("serial")
public class clsWebClubCategeoryWiseFacilityModel_ID implements Serializable {
	
	public clsWebClubCategeoryWiseFacilityModel_ID() {
	}
	public clsWebClubCategeoryWiseFacilityModel_ID(String strCatCode,String strClientCode) {
		this.strCatCode = strCatCode;
		this.strClientCode = strClientCode;
		}
	

	// Variable Declaration
	@Column(name = "strCatCode")
	private String strCatCode;

	
	@Column(name = "strClientCode")
	private String strClientCode;	
	


	// Setter-Getter Methods
	public String getStrCatCode() {
		return strCatCode;
	}

	public void setStrCatCode(String strCatCode) {
		this.strCatCode = strCatCode;
	}

	public String getStrClientCode() {
		return strClientCode;
	}

	public void setStrClientCode(String strClientCode) {
		this.strClientCode = strClientCode;
	}
	
	

	// HashCode and Equals Funtions
	@Override
	public boolean equals(Object obj) {
		clsWebClubCategeoryWiseFacilityModel_ID objModelId = (clsWebClubCategeoryWiseFacilityModel_ID) obj;
		if (this.strCatCode.equals(objModelId.getStrCatCode()) && this.strClientCode.equals(objModelId.getStrClientCode())) 
		{
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.strCatCode.hashCode() +  this.strClientCode.hashCode();
	}

}
