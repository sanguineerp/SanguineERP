package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Table(name="tbltest")
@Entity
@IdClass(clsSampleModel_ID.class)
public class clsSampleModel implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public  clsSampleModel(){
		
	}
	public clsSampleModel(clsSampleModel_ID objSampleModel_ID) {
		intId=objSampleModel_ID.getIntId();
	}
	
	
	@Id
	@AttributeOverrides({ @AttributeOverride(name = "intId", column = @Column(name = "intId")) })
	
	
	
	@Column(name="test")
	private String test;

	@Column(name="intId")
	private String intId;
	
	
	
	public String getIntId() {
		return intId;
	}

	public void setIntId(String intId) {
		this.intId = intId;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}
	
	
	
}
