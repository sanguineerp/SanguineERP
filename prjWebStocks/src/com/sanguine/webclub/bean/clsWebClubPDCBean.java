package com.sanguine.webclub.bean;

import java.util.List;

public class clsWebClubPDCBean{
//Variable Declaration
	
	private String strMemCodeRecieved;
	
	private String strMemCodeIssued;
	
	private String strMemCode;

	private String strChequeNo;

	private String strDrawnOn;

	private String strType;

	private double dblChequeAmt;

	private String dteChequeDate;

	private String dteDateCreated;

	private String dteDateEdited;

	private String strUserCreated;

	private String strUserEdited;

	private String strClientCode;
		
	//private List<clsWebClubPDCBean> listPDCDtl;
	
	/*public List<clsWebClubPDCBean> getListPDCDtl() {
		return listPDCDtl;
	}
	public void setListPDCDtl(clsWebClubPDCBean objModel) {
		this.listPDCDtl = (List<clsWebClubPDCBean>) objModel;
	}*/
	
	
	private List<clsWebClubPDCBean> listPDCDtlRecieved;
	// Setter-Getter Methods

	public List<clsWebClubPDCBean> getListPDCDtlRecieved() {
		return listPDCDtlRecieved;
	}

	public void setListPDCDtlRecieved(List<clsWebClubPDCBean> listPDCDtlRecieved) {
		this.listPDCDtlRecieved = listPDCDtlRecieved;
	}
	
	private List<clsWebClubPDCBean> listPDCDtlIssued;
	// Setter-Getter Methods

	public List<clsWebClubPDCBean> getListPDCDtlIssued() {
		return listPDCDtlIssued;
	}

	public void setListPDCDtlIssued(List<clsWebClubPDCBean> listPDCDtlIssued) {
		this.listPDCDtlIssued = listPDCDtlIssued;
	}
	
	
	
	// Setter-Getter Methods
	public String getStrMemCode(){
		return strMemCode;
	}
	public void setStrMemCode(String strMemCode){
		this.strMemCode=strMemCode;
	}

	public String getStrChequeNo(){
		return strChequeNo;
	}
	public void setStrChequeNo(String strChequeNo){
		this.strChequeNo=strChequeNo;
	}

	public String getStrDrawnOn(){
		return strDrawnOn;
	}
	public void setStrDrawnOn(String strDrawnOn){
		this.strDrawnOn=strDrawnOn;
	}

	public String getStrType(){
		return strType;
	}
	public void setStrType(String strType){
		this.strType=strType;
	}

	public double getDblChequeAmt(){
		return dblChequeAmt;
	}
	public void setDblChequeAmt(double dblChequeAmt){
		this.dblChequeAmt=dblChequeAmt;
	}

	public String getDteChequeDate(){
		return dteChequeDate;
	}
	public void setDteChequeDate(String dteChequeDate){
		this.dteChequeDate=dteChequeDate;
	}

	public String getDteDateCreated(){
		return dteDateCreated;
	}
	public void setDteDateCreated(String dteDateCreated){
		this.dteDateCreated=dteDateCreated;
	}

	public String getDteDateEdited(){
		return dteDateEdited;
	}
	public void setDteDateEdited(String dteDateEdited){
		this.dteDateEdited=dteDateEdited;
	}

	public String getStrUserCreated(){
		return strUserCreated;
	}
	public void setStrUserCreated(String strUserCreated){
		this.strUserCreated=strUserCreated;
	}

	public String getStrUserEdited(){
		return strUserEdited;
	}
	public void setStrUserEdited(String strUserEdited){
		this.strUserEdited=strUserEdited;
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this.strClientCode=strClientCode;
	}

	public String getStrMemCodeRecieved() {
		return strMemCodeRecieved;
	}

	public void setStrMemCodeRecieved(String strMemCodeRecieved) {
		this.strMemCodeRecieved = strMemCodeRecieved;
	}

	public String getStrMemCodeIssued() {
		return strMemCodeIssued;
	}

	public void setStrMemCodeIssued(String strMemCodeIssued) {
		this.strMemCodeIssued = strMemCodeIssued;
	}
	
	
	



}
