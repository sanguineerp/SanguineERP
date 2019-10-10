package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

@Embeddable
public class clsBanquetBookingModelDtl  implements Serializable {
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
		
		@Column(name="strBookingDate")
		private String strBookingDate;
		
		@Column(name="dblDocDiscAmt")
		private double dblDocDiscAmt;
		
		@Column(name="dblDocTaxAmt")
		private double dblDocTaxAmt;

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

		public String getStrBookingDate() {
			return strBookingDate;
		}

		public void setStrBookingDate(String strBookingDate) {
			this.strBookingDate = strBookingDate;
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

}
