package com.sanguine.webbanquets.bean;

import java.util.ArrayList;
import java.util.List;

import com.sanguine.webbanquets.model.clsBanquetBookingModelDtl;

public class clsBanquetBookingBean{
//Variable Declaration
	private String strBookingNo;

	private String strPropertyCode;

	private String dteBookingDate;

	private String dteFromDate;
	
	private String tmeFromTime;
	
	private String tmeToTime;

	private String dteToDate;

	private String strUserCreated;

	private String strUserEdited;

	private String dteDateCreated;

	private String dteDateEdited;

	private String strCustomerCode;

	private String strEmailID;

	private long intMinPaxNo;

	private long intMaxPaxNo;

	private String strClientCode;

	private String strEventCoordinatorCode;

	private String strAreaCode;

	private String strFunctionCode;
	
	private String strBillingInstructionCode;
	
	private String  strBookingStatus;
	
	private double dblSubTotal;
	
	private double dblDiscAmt;
	
	private double dblTaxAmt;
	
	private double dblGrandTotal;
	
	private String  strBanquetCode;
	
	List<clsBanquetBookingModelDtl> listSeriveDtl=new ArrayList<clsBanquetBookingModelDtl>();
	List<clsBanquetBookingModelDtl> listEquipDtl=new ArrayList<clsBanquetBookingModelDtl>();
	List<clsBanquetBookingModelDtl> listStaffCatDtl=new ArrayList<clsBanquetBookingModelDtl>();
	List<clsBanquetBookingModelDtl> listMenuItemDtl=new ArrayList<clsBanquetBookingModelDtl>();
	

	//private List<clsclsBanquetBookingBeanDtlModel> listclsclsBanquetBookingBeanDtlModel
//Setter-Getter Methods
	public String getStrBookingNo(){
		return strBookingNo;
	}
	public void setStrBookingNo(String strBookingNo){
		this.strBookingNo=strBookingNo;
	}

	public String getStrPropertyCode(){
		return strPropertyCode;
	}
	public void setStrPropertyCode(String strPropertyCode){
		this.strPropertyCode=strPropertyCode;
	}

	public String getDteBookingDate(){
		return dteBookingDate;
	}
	public void setDteBookingDate(String dteBookingDate){
		this.dteBookingDate=dteBookingDate;
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

	public String getStrCustomerCode(){
		return strCustomerCode;
	}
	public void setStrCustomerCode(String strCustomerCode){
		this.strCustomerCode=strCustomerCode;
	}

	public String getStrEmailID(){
		return strEmailID;
	}
	public void setStrEmailID(String strEmailID){
		this.strEmailID=strEmailID;
	}

	public long getIntMinPaxNo(){
		return intMinPaxNo;
	}
	public void setIntMinPaxNo(long intMinPaxNo){
		this.intMinPaxNo=intMinPaxNo;
	}

	public long getIntMaxPaxNo(){
		return intMaxPaxNo;
	}
	public void setIntMaxPaxNo(long intMaxPaxNo){
		this.intMaxPaxNo=intMaxPaxNo;
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this.strClientCode=strClientCode;
	}

	public String getStrEventCoordinatorCode(){
		return strEventCoordinatorCode;
	}
	public void setStrEventCoordinatorCode(String strEventCoordinatorCode){
		this.strEventCoordinatorCode=strEventCoordinatorCode;
	}

	public String getStrAreaCode(){
		return strAreaCode;
	}
	public void setStrAreaCode(String strAreaCode){
		this.strAreaCode=strAreaCode;
	}

	public String getStrFunctionCode(){
		return strFunctionCode;
	}
	public void setStrFunctionCode(String strFunctionCode){
		this.strFunctionCode=strFunctionCode;
	}
	public String getDteFromDate() {
		return dteFromDate;
	}
	public void setDteFromDate(String dteFromDate) {
		this.dteFromDate = dteFromDate;
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
	public String getDteToDate() {
		return dteToDate;
	}
	public void setDteToDate(String dteToDate) {
		this.dteToDate = dteToDate;
	}
	public String getStrBillingInstructionCode() {
		return strBillingInstructionCode;
	}
	public void setStrBillingInstructionCode(String strBillingInstructionCode) {
		this.strBillingInstructionCode = strBillingInstructionCode;
	}
    
	public String getStrBookingStatus() {
		return strBookingStatus;
	}
	public void setStrBookingStatus(String strBookingStatus) {
		this.strBookingStatus = strBookingStatus;
	}
	public List<clsBanquetBookingModelDtl> getListSeriveDtl() {
		return listSeriveDtl;
	}
	public void setListSeriveDtl(List<clsBanquetBookingModelDtl> listSeriveDtl) {
		this.listSeriveDtl = listSeriveDtl;
	}
	public List<clsBanquetBookingModelDtl> getListEquipDtl() {
		return listEquipDtl;
	}
	public void setListEquipDtl(List<clsBanquetBookingModelDtl> listEquipDtl) {
		this.listEquipDtl = listEquipDtl;
	}
	public List<clsBanquetBookingModelDtl> getListStaffCatDtl() {
		return listStaffCatDtl;
	}
	public void setListStaffCatDtl(List<clsBanquetBookingModelDtl> listStaffCatDtl) {
		this.listStaffCatDtl = listStaffCatDtl;
	}
	public List<clsBanquetBookingModelDtl> getListMenuItemDtl() {
		return listMenuItemDtl;
	}
	public void setListMenuItemDtl(List<clsBanquetBookingModelDtl> listMenuItemDtl) {
		this.listMenuItemDtl = listMenuItemDtl;
	}
	public double getDblSubTotal() {
		return dblSubTotal;
	}
	public void setDblSubTotal(double dblSubTotal) {
		this.dblSubTotal = dblSubTotal;
	}
	public double getDblDiscAmt() {
		return dblDiscAmt;
	}
	public void setDblDiscAmt(double dblDiscAmt) {
		this.dblDiscAmt = dblDiscAmt;
	}
	public double getDblTaxAmt() {
		return dblTaxAmt;
	}
	public void setDblTaxAmt(double dblTaxAmt) {
		this.dblTaxAmt = dblTaxAmt;
	}
	public double getDblGrandTotal() {
		return dblGrandTotal;
	}
	public void setDblGrandTotal(double dblGrandTotal) {
		this.dblGrandTotal = dblGrandTotal;
	}
	public String getStrBanquetCode() {
		return strBanquetCode;
	}
	public void setStrBanquetCode(String strBanquetCode) {
		this.strBanquetCode = strBanquetCode;
	}

	/*public List<clsclsBanquetBookingBeanDtlModel> getclsclsBanquetBookingBeanDtlModel(){
		return listclsclsBanquetBookingBeanDtlModel;
	}
	public void setclsclsBanquetBookingBeanDtlModel(List<clsclsBanquetBookingBeanDtlModel listclsclsBanquetBookingBeanDtlModel>){
		return listclsclsBanquetBookingBeanDtlModel;
	}*/

}
