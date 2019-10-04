package com.sanguine.webbanquets.bean;

import java.io.Serializable;

public class clsBookingFlashBean implements Serializable
{
	private String dteFromDate;
	private String dteToDate;
	private String strBookingNo;
	private String strBookingStatus;
	private String dteBookingDate;
	private String tmeFromTime;
	private String tmeToTime;
	private String strAreaCode;
	private String strFunctionCode;
	private double dblAmt;
	
	
	
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
	public String getStrBookingNo() {
		return strBookingNo;
	}
	public void setStrBookingNo(String strBookingNo) {
		this.strBookingNo = strBookingNo;
	}
	public String getStrBookingStatus() {
		return strBookingStatus;
	}
	public void setStrBookingStatus(String strBookingStatus) {
		this.strBookingStatus = strBookingStatus;
	}
	public String getDteBookingDate() {
		return dteBookingDate;
	}
	public void setDteBookingDate(String dteBookingDate) {
		this.dteBookingDate = dteBookingDate;
	}
	public String getTmeFromTime() {
		return tmeFromTime;
	}
	public void setTmeFromTime(String tmeFromTime) {
		this.tmeFromTime = tmeFromTime;
	}
	public String getTmeToTime() {
		return tmeToTime;
	}
	public void setTmeToTime(String tmeToTime) {
		this.tmeToTime = tmeToTime;
	}
	public String getStrAreaCode() {
		return strAreaCode;
	}
	public void setStrAreaCode(String strAreaCode) {
		this.strAreaCode = strAreaCode;
	}
	public String getStrFunctionCode() {
		return strFunctionCode;
	}
	public void setStrFunctionCode(String strFunctionCode) {
		this.strFunctionCode = strFunctionCode;
	}
	public double getDblAmt() {
		return dblAmt;
	}
	public void setDblAmt(double dblAmt) {
		this.dblAmt = dblAmt;
	}
}
