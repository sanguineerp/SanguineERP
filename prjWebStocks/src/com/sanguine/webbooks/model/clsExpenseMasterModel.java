package com.sanguine.webbooks.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;


@Entity
@Table(name = "tblexpensemaster")
@IdClass(clsExpenseMasterModel_ID.class)

public class clsExpenseMasterModel  implements Serializable{
	
	

	private static final long serialVersionUID = 1L;


	public clsExpenseMasterModel() {
		
	}
	
	public clsExpenseMasterModel(clsExpenseMasterModel_ID objExpMaster) {
		this.strExpCode = objExpMaster.getStrExpCode();
		this.strClientCode = objExpMaster.getStrClientCode();
	}
	
	

	@Id
	@AttributeOverrides({ @AttributeOverride(name = "strExpCode", column = @Column(name = "strExpCode")), @AttributeOverride(name = "strClientCode", column = @Column(name = "strClientCode")) })

	
	@Column(name = "intEId", nullable = false, updatable = false)
	private long intEId;


	
	@Column(name = "strExpCode")
		private String strExpCode;

		@Column(name = "strClientCode")
		private String strClientCode;


		@Column(name = "stnExpName")
		private String stnExpName;
		
		
		@Column(name = "strExpShortName")
		private String strExpShortName;
		
		@Column(name = "strGLCode")
		private String strGLCode;
		
		@Column(name = "strUserCreated")
		private String strUserCreated;
		
		@Column(name = "dtCreatedDate")
		private String dtCreatedDate;
		
		@Column(name = "strUserModified")
		private String strUserModified;
		
		@Column(name = "dtLastModified")
		private String dtLastModified;
		
		
		
	
		public String getStrExpCode() {
			return strExpCode;
		}
		public void setStrExpCode(String strExpCode) {
			this.strExpCode = strExpCode;
		}
		public String getStrClientCode() {
			return strClientCode;
		}
		public void setStrClientCode(String strClientCode) {
			this.strClientCode = strClientCode;
		}
		public String getStnExpName() {
			return stnExpName;
		}
		public void setStnExpName(String stnExpName) {
			this.stnExpName = stnExpName;
		}
		public String getStrExpShortName() {
			return strExpShortName;
		}
		public void setStrExpShortName(String strExpShortName) {
			this.strExpShortName = strExpShortName;
		}
		public String getStrGLCode() {
			return strGLCode;
		}
		public void setStrGLCode(String strGLCode) {
			this.strGLCode = strGLCode;
		}
		public String getStrUserCreated() {
			return strUserCreated;
		}
		public void setStrUserCreated(String strUserCreated) {
			this.strUserCreated = strUserCreated;
		}
		public String getDtCreatedDate() {
			return dtCreatedDate;
		}
		public void setDtCreatedDate(String dtCreatedDate) {
			this.dtCreatedDate = dtCreatedDate;
		}
		public String getStrUserModified() {
			return strUserModified;
		}
		public void setStrUserModified(String strUserModified) {
			this.strUserModified = strUserModified;
		}
		public String getDtLastModified() {
			return dtLastModified;
		}
		public void setDtLastModified(String dtLastModified) {
			this.dtLastModified = dtLastModified;
		}

		public long getIntEId() {
			return intEId;
		}

		public void setIntEId(long intEId) {
			this.intEId = intEId;
		}
		
		
		



	
	
	

}
