package com.sanguine.webpms.bean;

import java.util.List;

public class clsCheckOutListReportBean {

	private String dteFromDate;

	private String dteToDate;

	private String strReservationNo;
	
	private String strCheckInNo;

	private String strBookingTypeDesc;

	private String dteDateCreated;

	private String strCorporateDesc;

	private String agentDescription;

	private String strBookerName;

	private String dteCancelDate;

	private String businessSrc;

	private String strBillingInstDesc;

	private String strFirstName;
	
	private String strMiddleName;
	
	private String strLastName;

	private String dteDepartureDate;
	
	private String strType;
	
	private String strRoomTypeCode;
	
	private String strRoomTypeDesc;
	
	private String strAddress;
	
	private double dblReceiptAmt;
	
	private String strArrivalFrom;
	

	// private String guestFirstName;
	//
	// private String strMiddleName;
	//
	// private String strLastName;
	//
	// private String strRoomTypeDesc;
	//
	// private String strAddress;
	//
	// private String strArrivalFrom;
	//
	// private String strProceedingTo;

	private double dblCreditAmt;

	private List<clsCheckOutListReportDtlBean> listCheckOutDtl;

	public String getDteFromDate() {
		return dteFromDate;
	}

	public void setDteFromDate(String dteFromDate) {
		this.dteFromDate = dteFromDate;
	}

	public String getDteToDate() {
		return dteToDate;
	}

	public void setDteToDate(String dteToDate) {
		this.dteToDate = dteToDate;
	}

	public String getStrReservationNo() {
		return strReservationNo;
	}

	public void setStrReservationNo(String strReservationNo) {
		this.strReservationNo = strReservationNo;
	}

	public String getStrBookingTypeDesc() {
		return strBookingTypeDesc;
	}

	public void setStrBookingTypeDesc(String strBookingTypeDesc) {
		this.strBookingTypeDesc = strBookingTypeDesc;
	}

	public String getDteDateCreated() {
		return dteDateCreated;
	}

	public void setDteDateCreated(String dteDateCreated) {
		this.dteDateCreated = dteDateCreated;
	}

	public String getStrCorporateDesc() {
		return strCorporateDesc;
	}

	public void setStrCorporateDesc(String strCorporateDesc) {
		this.strCorporateDesc = strCorporateDesc;
	}

	public String getAgentDescription() {
		return agentDescription;
	}

	public void setAgentDescription(String agentDescription) {
		this.agentDescription = agentDescription;
	}

	public String getStrBookerName() {
		return strBookerName;
	}

	public void setStrBookerName(String strBookerName) {
		this.strBookerName = strBookerName;
	}

	public String getDteCancelDate() {
		return dteCancelDate;
	}

	public void setDteCancelDate(String dteCancelDate) {
		this.dteCancelDate = dteCancelDate;
	}

	public String getBusinessSrc() {
		return businessSrc;
	}

	public void setBusinessSrc(String businessSrc) {
		this.businessSrc = businessSrc;
	}

	public String getStrBillingInstDesc() {
		return strBillingInstDesc;
	}

	public void setStrBillingInstDesc(String strBillingInstDesc) {
		this.strBillingInstDesc = strBillingInstDesc;
	}

	public String getStrFirstName() {
		return strFirstName;
	}

	public void setStrFirstName(String strFirstName) {
		this.strFirstName = strFirstName;
	}

	
	// public String getGuestFirstName() {
	// return guestFirstName;
	// }
	//
	// public void setGuestFirstName(String guestFirstName) {
	// this.guestFirstName = guestFirstName;
	// }
	//
	// public String getStrMiddleName() {
	// return strMiddleName;
	// }
	//
	// public void setStrMiddleName(String strMiddleName) {
	// this.strMiddleName = strMiddleName;
	// }
	//
	// public String getStrLastName() {
	// return strLastName;
	// }
	//
	// public void setStrLastName(String strLastName) {
	// this.strLastName = strLastName;
	// }
	//
	// public String getStrRoomTypeDesc() {
	// return strRoomTypeDesc;
	// }
	//
	// public void setStrRoomTypeDesc(String strRoomTypeDesc) {
	// this.strRoomTypeDesc = strRoomTypeDesc;
	// }
	//
	// public String getStrAddress() {
	// return strAddress;
	// }
	//
	// public void setStrAddress(String strAddress) {
	// this.strAddress = strAddress;
	// }
	//
	// public String getStrArrivalFrom() {
	// return strArrivalFrom;
	// }
	//
	// public void setStrArrivalFrom(String strArrivalFrom) {
	// this.strArrivalFrom = strArrivalFrom;
	// }
	//
	// public String getStrProceedingTo() {
	// return strProceedingTo;
	// }
	//
	// public void setStrProceedingTo(String strProceedingTo) {
	// this.strProceedingTo = strProceedingTo;
	// }

	public double getDblCreditAmt() {
		return dblCreditAmt;
	}

	public void setDblCreditAmt(double dblCreditAmt) {
		this.dblCreditAmt = dblCreditAmt;
	}

	public List<clsCheckOutListReportDtlBean> getListCheckOutDtl() {
		return listCheckOutDtl;
	}

	public void setListCheckOutDtl(List<clsCheckOutListReportDtlBean> listCheckOutDtl) {
		this.listCheckOutDtl = listCheckOutDtl;
	}

	public String getStrMiddleName() {
		return strMiddleName;
	}

	public void setStrMiddleName(String strMiddleName) {
		this.strMiddleName = strMiddleName;
	}

	public String getStrLastName() {
		return strLastName;
	}

	public void setStrLastName(String strLastName) {
		this.strLastName = strLastName;
	}

	public String getDteDepartureDate() {
		return dteDepartureDate;
	}

	public void setDteDepartureDate(String dteDepartureDate) {
		this.dteDepartureDate = dteDepartureDate;
	}

	public String getStrCheckInNo() {
		return strCheckInNo;
	}

	public void setStrCheckInNo(String strCheckInNo) {
		this.strCheckInNo = strCheckInNo;
	}

	public String getStrType() {
		return strType;
	}

	public void setStrType(String strType) {
		this.strType = strType;
	}

	public String getStrRoomTypeCode() {
		return strRoomTypeCode;
	}

	public void setStrRoomTypeCode(String strRoomTypeCode) {
		this.strRoomTypeCode = strRoomTypeCode;
	}

	public String getStrRoomTypeDesc() {
		return strRoomTypeDesc;
	}

	public void setStrRoomTypeDesc(String strRoomTypeDesc) {
		this.strRoomTypeDesc = strRoomTypeDesc;
	}

	public String getStrAddress() {
		return strAddress;
	}

	public void setStrAddress(String strAddress) {
		this.strAddress = strAddress;
	}

	public double getDblReceiptAmt() {
		return dblReceiptAmt;
	}

	public void setDblReceiptAmt(double dblReceiptAmt) {
		this.dblReceiptAmt = dblReceiptAmt;
	}

	public String getStrArrivalFrom() {
		return strArrivalFrom;
	}

	public void setStrArrivalFrom(String strArrivalFrom) {
		this.strArrivalFrom = strArrivalFrom;
	}

}
