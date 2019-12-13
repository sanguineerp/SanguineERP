package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class clsBanquetQuotationModelDtl  implements Serializable {
	private static final long serialVersionUID = 1L;

	//Variable Declaration
		

		@Column(name="strType")
		private String strType;

		@Column(name="strDocNo")
		private String strDocNo;

		@Column(name="strDocName")
		private String strDocName;

		@Column(name="dblDocQty")
		private double dblDocQty;
		

		@Column(name="dblDocRate")
		private double dblDocRate;

		@Column(name="dblDocTotalAmt")
		private double dblDocTotalAmt;
		
		@Column(name="strQuotationDate")
		private String strQuotationDate;
		
		@Column(name="dblDocDiscAmt")
		private double dblDocDiscAmt;
		
		@Column(name="dblDocTaxAmt")
		private double dblDocTaxAmt;

		@Column(name="strVendorCode")
		private String strVendorCode;
		
		public String getStrType() {
			return strType;
		}

		public void setStrType(String strType) {
			this.strType = strType;
		}

		public String getStrDocNo() {
			return strDocNo;
		}

		public void setStrDocNo(String strDocNo) {
			this.strDocNo = strDocNo;
		}

		public String getStrDocName() {
			return strDocName;
		}

		public void setStrDocName(String strDocName) {
			this.strDocName = strDocName;
		}

		public double getDblDocQty() {
			return dblDocQty;
		}

		public void setDblDocQty(double dblDocQty) {
			this.dblDocQty = dblDocQty;
		}

		public double getDblDocRate() {
			return dblDocRate;
		}

		public void setDblDocRate(double dblDocRate) {
			this.dblDocRate = dblDocRate;
		}

		public double getDblDocTotalAmt() {
			return dblDocTotalAmt;
		}

		public void setDblDocTotalAmt(double dblDocTotalAmt) {
			this.dblDocTotalAmt = dblDocTotalAmt;
		}

		public String getStrQuotationDate() {
			return strQuotationDate;
		}

		public void setStrQuotationDate(String strQuotationDate) {
			this.strQuotationDate = strQuotationDate;
		}

		public double getDblDocDiscAmt() {
			return dblDocDiscAmt;
		}

		public void setDblDocDiscAmt(double dblDocDiscAmt) {
			this.dblDocDiscAmt = dblDocDiscAmt;
		}

		public double getDblDocTaxAmt() {
			return dblDocTaxAmt;
		}

		public void setDblDocTaxAmt(double dblDocTaxAmt) {
			this.dblDocTaxAmt = dblDocTaxAmt;
		}

		public String getStrVendorCode() {
			return strVendorCode;
		}

		public void setStrVendorCode(String strVendorCode) {
			this.strVendorCode = strVendorCode;
		}

}
