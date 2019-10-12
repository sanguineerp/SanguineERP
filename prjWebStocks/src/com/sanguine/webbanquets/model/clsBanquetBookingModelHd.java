package com.sanguine.webbanquets.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.persistence.Id;

import org.hibernate.annotations.CollectionOfElements;

import com.sanguine.webpms.model.clsBillDtlModel;

@Entity
@Table(name="tblbqbookinghd")
@IdClass(clsBanquetBookingModel_ID.class)

public class clsBanquetBookingModelHd implements Serializable{
	private static final long serialVersionUID = 1L;
	public clsBanquetBookingModelHd(){}

	public clsBanquetBookingModelHd(clsBanquetBookingModel_ID objModelID){
		strBookingNo = objModelID.getStrBookingNo();
		strClientCode = objModelID.getStrClientCode();
	}
	
	@CollectionOfElements(fetch = FetchType.EAGER)
	@JoinTable(name = "tblbqbookingdtl", joinColumns = { @JoinColumn(name = "strBookingNo"), @JoinColumn(name = "strClientCode") })
	@Id
	@AttributeOverrides({
		@AttributeOverride(name="strBookingNo",column=@Column(name="strBookingNo")),@AttributeOverride(name="strClientCode",column=@Column(name="strClientCode"))
	})
	private List<clsBanquetBookingModelDtl> listBanquetBookingDtlModels = new ArrayList<clsBanquetBookingModelDtl>();
	
	/*@Id
	@AttributeOverrides({ @AttributeOverride(name = "strBillNo", column = @Column(name = "strBillNo")), @AttributeOverride(name = "strClientCode", column = @Column(name = "strClientCode")) })
	//private List<clsBillDtlModel> listBillDtlModels = new ArrayList<clsBillDtlModel>();
*/
//Variable Declaration
	@Column(name="strBookingNo")
	private String strBookingNo;

	@Column(name="strPropertyCode")
	private String strPropertyCode;

	@Column(name="dteBookingDate")
	private String dteBookingDate;

	@Column(name="dteFromDate")
	private String dteFromDate;

	@Column(name="dteToDate")
	private String dteToDate;
	

	@Column(name="tmeFromTime")
	private String tmeFromTime;

	@Column(name="tmeToTime")
	private String tmeToTime;

	@Column(name="strUserCreated")
	private String strUserCreated;

	@Column(name="strUserEdited")
	private String strUserEdited;

	@Column(name="dteDateCreated")
	private String dteDateCreated;

	@Column(name="dteDateEdited")
	private String dteDateEdited;

	@Column(name="strCustomerCode")
	private String strCustomerCode;

	@Column(name="strEmailID")
	private String strEmailID;

	@Column(name="intMinPaxNo")
	private long intMinPaxNo;

	@Column(name="intMaxPaxNo")
	private long intMaxPaxNo;

	@Column(name="strClientCode")
	private String strClientCode;

	@Column(name="strEventCoordinatorCode")
	private String strEventCoordinatorCode;

	@Column(name="strAreaCode")
	private String strAreaCode;

	@Column(name="strFunctionCode")
	private String strFunctionCode;
	
	@Column(name="strBillingInstructionCode")
	private String strBillingInstructionCode;
	
	@Column(name="strBookingStatus")
	private String strBookingStatus;
	
	
	@Column(name="dblSubTotal")
	private double dblSubTotal;

	@Column(name="dblDiscAmt")
	private double dblDiscAmt;
	
	@Column(name="dblTaxAmt")
	private double dblTaxAmt;
	
	@Column(name="dblGrandTotal")
	private double dblGrandTotal;
	
	@Column(name="strBanquetCode")
	private String strBanquetCode;

	//Setter-Getter Methods
	public String getStrBookingNo(){
		return strBookingNo;
	}
	public void setStrBookingNo(String strBookingNo){
		this. strBookingNo = (String) setDefaultValue( strBookingNo, "NA");
	}

	public String getStrPropertyCode(){
		return strPropertyCode;
	}
	public void setStrPropertyCode(String strPropertyCode){
		this. strPropertyCode = (String) setDefaultValue( strPropertyCode, "NA");
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
		this. strUserCreated = (String) setDefaultValue( strUserCreated, "NA");
	}

	public String getStrUserEdited(){
		return strUserEdited;
	}
	public void setStrUserEdited(String strUserEdited){
		this. strUserEdited = (String) setDefaultValue( strUserEdited, "NA");
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
		this. strCustomerCode = (String) setDefaultValue( strCustomerCode, "NA");
	}

	public String getStrEmailID(){
		return strEmailID;
	}
	public void setStrEmailID(String strEmailID){
		this. strEmailID = (String) setDefaultValue( strEmailID, "NA");
	}

	public long getIntMinPaxNo(){
		return intMinPaxNo;
	}
	public void setIntMinPaxNo(long intMinPaxNo){
		this. intMinPaxNo = (Long) setDefaultValue( intMinPaxNo, "NA");
	}

	public long getIntMaxPaxNo(){
		return intMaxPaxNo;
	}
	public void setIntMaxPaxNo(long intMaxPaxNo){
		this. intMaxPaxNo = (Long) setDefaultValue( intMaxPaxNo, "NA");
	}

	public String getStrClientCode(){
		return strClientCode;
	}
	public void setStrClientCode(String strClientCode){
		this. strClientCode = (String) setDefaultValue( strClientCode, "NA");
	}

	public String getStrEventCoordinatorCode(){
		return strEventCoordinatorCode;
	}
	public void setStrEventCoordinatorCode(String strEventCoordinatorCode){
		this. strEventCoordinatorCode = (String) setDefaultValue( strEventCoordinatorCode, "NA");
	}

	public String getStrAreaCode(){
		return strAreaCode;
	}
	public void setStrAreaCode(String strAreaCode){
		this. strAreaCode = (String) setDefaultValue( strAreaCode, "NA");
	}

	public String getStrFunctionCode(){
		return strFunctionCode;
	}
	public void setStrFunctionCode(String strFunctionCode){
		this. strFunctionCode = (String) setDefaultValue( strFunctionCode, "NA");
	}


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

	public List<clsBanquetBookingModelDtl> getListBanquetBookingDtlModels() {
		return listBanquetBookingDtlModels;
	}

	public void setListBanquetBookingDtlModels(
			List<clsBanquetBookingModelDtl> listBanquetBookingDtlModels) {
		this.listBanquetBookingDtlModels = listBanquetBookingDtlModels;
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
	

	//Function to Set Default Values
	private Object setDefaultValue(Object value, Object defaultValue){
		if(value !=null && (value instanceof String && value.toString().length()>0)){
			return value;
		}
		else if(value !=null && (value instanceof Double && value.toString().length()>0)){
			return value;
		}
		else if(value !=null && (value instanceof Integer && value.toString().length()>0)){
			return value;
		}
		else if(value !=null && (value instanceof Long && value.toString().length()>0)){
			return value;
		}
		else{
			return defaultValue;
		}
	}

	public String getStrBanquetCode() {
		return strBanquetCode;
	}

	public void setStrBanquetCode(String strBanquetCode) {
		this.strBanquetCode = strBanquetCode;
	}

}
