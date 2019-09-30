package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;



public class clsSampleModel_ID  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column(name="intId")
	private String intId;
	public clsSampleModel_ID()
	{
		
	}

	public clsSampleModel_ID(String intId){
		this.intId=intId;
	}
	public String getIntId() {
		return intId;
	}

	public void setIntId(String intId) {
		this.intId = intId;
	}
	

	@Override
	public boolean equals(Object obj) {
		clsSampleModel_ID cp = (clsSampleModel_ID) obj;
		if (this.intId.equals(cp.intId)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.intId.hashCode();
	}

}
