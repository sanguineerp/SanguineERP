package com.sanguine.crm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tblsalesordertaxdtl")
public class clsSalesOrderTaxDtlModel {

	
	
		@Column(name = "strSOCode", columnDefinition = "VARCHAR(20) NOT NULL default ''")
		private String strSOCode;

		@Column(name = "strTaxCode", columnDefinition = "VARCHAR(20) NOT NULL default ''")
		private String strTaxCode;

		@Column(name = "strTaxDesc", columnDefinition = "VARCHAR(100) NOT NULL default ''")
		private String strTaxDesc;

		@Column(name = "strTaxableAmt", columnDefinition = "Decimal(18,4) NOT NULL default '0.00'")
		private double strTaxableAmt;

		@Column(name = "strTaxAmt", columnDefinition = "Decimal(18,4) NOT NULL default '0.00'")
		private double strTaxAmt;

		@Column(name = "strClientCode")
		private String strClientCode;

		@Id
		@GeneratedValue
		@Column(name = "intId")
		private long intId;


		public String getStrTaxCode() {
			return strTaxCode;
		}

		public void setStrTaxCode(String strTaxCode) {
			this.strTaxCode = strTaxCode;
		}

		public String getStrTaxDesc() {
			return strTaxDesc;
		}

		public void setStrTaxDesc(String strTaxDesc) {
			this.strTaxDesc = strTaxDesc;
		}

		public double getStrTaxableAmt() {
			return strTaxableAmt;
		}

		public void setStrTaxableAmt(double strTaxableAmt) {
			this.strTaxableAmt = strTaxableAmt;
		}

		public double getStrTaxAmt() {
			return strTaxAmt;
		}

		public void setStrTaxAmt(double strTaxAmt) {
			this.strTaxAmt = strTaxAmt;
		}

		public String getStrClientCode() {
			return strClientCode;
		}

		public void setStrClientCode(String strClientCode) {
			this.strClientCode = strClientCode;
		}

		public long getIntId() {
			return intId;
		}

		public void setIntId(long intId) {
			this.intId = intId;
		}

		public String getStrSOCode() {
			return strSOCode;
		}

		public void setStrSOCode(String strSOCode) {
			this.strSOCode = strSOCode;
		}
	
}
