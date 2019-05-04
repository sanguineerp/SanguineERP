package com.sanguine.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sanguine.base.service.intfBaseService;
import com.sanguine.crm.controller.clsSalesReturnController;
import com.sanguine.crm.model.clsInvoiceHdModel;
import com.sanguine.crm.model.clsInvoiceModelDtl;
import com.sanguine.crm.model.clsInvoiceTaxDtlModel;
import com.sanguine.crm.model.clsPartyMasterModel;
import com.sanguine.crm.model.clsSalesReturnDtlModel;
import com.sanguine.crm.model.clsSalesReturnHdModel;
import com.sanguine.crm.service.clsCRMSettlementMasterService;
import com.sanguine.crm.service.clsInvoiceHdService;
import com.sanguine.crm.service.clsSalesReturnService;
import com.sanguine.model.clsGRNDtlModel;
import com.sanguine.model.clsGRNHdModel;
import com.sanguine.model.clsGRNTaxDtlModel;
import com.sanguine.model.clsLinkUpHdModel;
import com.sanguine.model.clsProductMasterModel;
import com.sanguine.model.clsPurchaseReturnDtlModel;
import com.sanguine.model.clsPurchaseReturnHdModel;
import com.sanguine.model.clsPurchaseReturnTaxDtlModel;
import com.sanguine.model.clsSettlementMasterModel;
import com.sanguine.model.clsStkAdjustmentDtlModel;
import com.sanguine.model.clsStkAdjustmentHdModel;
import com.sanguine.model.clsTaxHdModel;
import com.sanguine.service.clsGRNService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsLinkUpService;
import com.sanguine.service.clsProductMasterService;
import com.sanguine.service.clsPurchaseReturnService;
import com.sanguine.service.clsStkAdjustmentService;
import com.sanguine.webbooks.bean.clsJVBean;
import com.sanguine.webbooks.bean.clsJVDetailsBean;
import com.sanguine.webbooks.bean.clsPaymentBean;
import com.sanguine.webbooks.bean.clsPaymentDetailsBean;
import com.sanguine.webbooks.controller.clsJVController;
import com.sanguine.webbooks.controller.clsPaymentController;
import com.sanguine.webbooks.model.clsJVDtlModel;
import com.sanguine.webbooks.model.clsJVHdModel;
import com.sanguine.webbooks.model.clsPaymentGRNDtlModel;
import com.sanguine.webbooks.model.clsPaymentHdModel;
import com.sanguine.webbooks.service.clsSundryCreditorMasterService;

@Controller
public class clsJVGeneratorController {

	@Autowired
	private clsLinkUpService objLinkupService;
	
	@Autowired
	private clsGlobalFunctions objGlobalFunctions;
	
	@Autowired
	private clsProductMasterService objProductMasterService;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	clsGRNService objGRNService;
	
	@Autowired
	clsInvoiceHdService objInvoiceService;
	
	@Autowired
	clsJVController objJVController;
	
	@Autowired
	intfBaseService objBaseService;
	
	@Autowired
	clsCRMSettlementMasterService objSttlementMasterService;
	
	@Autowired
	private clsPurchaseReturnService objPurchaseReturnService;
	
	@Autowired 
	private clsSalesReturnService objSalesReturnService;
	
	@Autowired
	clsSalesReturnController objSalesReturnController;
	
	@Autowired
	private clsSundryCreditorMasterService objSundryCreditorMasterService;
	
	@Autowired
	private clsPaymentController objPaymentController;
	
	@Autowired
	private clsStkAdjustmentService objStkAdjService;
	
	public String funGenrateJVforGRN(String GRNCode, String clientCode, String userCode, String propCode, HttpServletRequest req) {
				
		StringBuilder sbSql=new StringBuilder();
		clsJVBean objJVBean = new clsJVBean();
		String jvCode = "";
		List list=objGRNService.funGetObject(GRNCode, clientCode);
		boolean flgLinkup=true;
		StringBuilder sbLinkUpErrorMessage=new StringBuilder();
		sbLinkUpErrorMessage.append("ERROR!");
		List<clsJVDetailsBean> listJVDetailBean = new ArrayList<clsJVDetailsBean>();
		
		if(null!=list && list.size()>0)
		{
			Object[] arrObjHd=(Object[])list.get(0);
			clsGRNHdModel objModel=(clsGRNHdModel)arrObjHd[0];
			String date=objModel.getDtGRNDate().split(" ")[0];
			objModel.setDtGRNDate(date);
			
			String currentDateTime=objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd");
			String time=currentDateTime.split(" ")[1];
			String supplierType="";	
			
			sbSql.setLength(0);
			sbSql.append("select objPartyMasterModel from clsPartyMasterModel objPartyMasterModel "
				+ " where objPartyMasterModel.strPCode='"+objModel.getStrSuppCode()+"' and objPartyMasterModel.strClientCode='"+clientCode+"' "
				+ " and objPartyMasterModel.strPropCode='"+propCode+"' ");
			try
			{
				List listSuppDtl=objBaseService.funGetListModuleWise(sbSql, "hql", "WebStocks");
				
				if(listSuppDtl.size()>0)
				{
					clsPartyMasterModel objPartyMasterModel=(clsPartyMasterModel)listSuppDtl.get(0);
					supplierType=objPartyMasterModel.getStrPartyType();
				}
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			String custCode = objModel.getStrSuppCode();
			double debitAmt = objModel.getDblTotal();
			String strCurr = req.getSession().getAttribute("currValue").toString();
			double currValue = Double.parseDouble(strCurr);
			clsLinkUpHdModel objLinkCust = objLinkupService.funGetARLinkUp(custCode, clientCode, propCode, "Supplier", "Purchase");
			if (objLinkCust != null) {
				if (objModel.getStrRefNo().equals("")) {
					
					objJVBean.setStrVouchNo("");
					objJVBean.setDblAmt(debitAmt * currValue);
				} else {
					
					objJVBean.setStrVouchNo(objModel.getStrRefNo());
					objJVBean.setDblAmt(debitAmt);
				}
				
				objJVBean.setStrVouchNo("");
				sbSql.setLength(0);
				sbSql.append("select objJVModel from clsJVHdModel objJVModel where objJVModel.strSourceDocNo='"+objModel.getStrGRNCode()+"' and objJVModel.strClientCode='"+clientCode+"'");	
				try
				{
					List listJVDtl=objBaseService.funGetListModuleWise(sbSql, "hql", "WebBooks");
					if(listJVDtl.size()>0)
					{
						clsJVHdModel objJVHdModel=(clsJVHdModel)listJVDtl.get(0);
						objJVBean.setStrVouchNo(objJVHdModel.getStrVouchNo());
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
				objJVBean.setStrNarration("JV Generated by GRN:" + objModel.getStrGRNCode());
				objJVBean.setStrSancCode("");
				objJVBean.setStrType("");
				objJVBean.setDteVouchDate(objGlobalFunctions.funGetDate("dd-MM-yyyy", objModel.getDtGRNDate()) +" "+time);
				objJVBean.setIntVouchMonth(1);
				objJVBean.setDblAmt(debitAmt * currValue);
				objJVBean.setStrTransType("R");
				objJVBean.setStrTransMode("A");
				objJVBean.setStrModuleType("AP");
				objJVBean.setStrMasterPOS("WEBSTOCKS");
				objJVBean.setStrUserCreated(userCode);
				objJVBean.setStrUserEdited(userCode);
				objJVBean.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				objJVBean.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				objJVBean.setStrClientCode(clientCode);
				objJVBean.setStrPropertyCode(propCode);
				objJVBean.setStrSource("GRN");
				objJVBean.setStrSourceDocNo(objModel.getStrGRNCode());
				objJVBean.setStrCurrency(objModel.getStrCurrency());
				objJVBean.setDblConversion(objModel.getDblConversion());
			// jvhd entry end
	
				
			// jvdtl entry Start
				List listTempDtl = objGRNService.funGetDtlList(GRNCode, clientCode);
				
				for (int i = 0; i < listTempDtl.size(); i++) {
					Object[] ob = (Object[]) listTempDtl.get(i);
					clsGRNDtlModel objDtl = (clsGRNDtlModel) ob[0];
	
					clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					clsProductMasterModel objProdModle = objProductMasterService.funGetObject(objDtl.getStrProdCode(), clientCode);
					clsLinkUpHdModel objLinkSubGroup = objLinkupService.funGetARLinkUp(objProdModle.getStrSGCode(), clientCode, propCode, "SubGroup", "Purchase");
					if (objProdModle != null && objLinkSubGroup != null) {
						
						objJVDetailBean.setStrAccountCode(objLinkSubGroup.getStrAccountCode());
						objJVDetailBean.setStrDescription(objLinkSubGroup.getStrMasterDesc());
						objJVDetailBean.setStrDC("Dr");
						objJVDetailBean.setDblDebitAmt(objDtl.getDblTotalPrice());
						objJVDetailBean.setDblCreditAmt(0.00);
						objJVDetailBean.setStrNarration("Stock In Hand Amt");
						if(supplierType.equals("Foreign"))
							objJVDetailBean.setStrNarration("Stock In Hand Amt, inclusive of Freight, Insurance and Other Charges");
						
						objJVDetailBean.setStrOneLineAcc("R");
						
						listJVDetailBean.add(objJVDetailBean);
					}
					else
					{
						flgLinkup=false;
						sbLinkUpErrorMessage.append("Check SubGroup linkup"+objProdModle.getStrSGCode()+", ");
					}
				}
							
				String sql = "select a.strTaxCode,a.strTaxDesc,a.strTaxableAmt,a.strTaxAmt "
					+ " from clsGRNTaxDtlModel a, clsTaxHdModel b " 
					+ " where a.strTaxCode=b.strTaxCode and a.strGRNCode='" + GRNCode + "' and a.strClientCode='" + clientCode + "' "
				    + " and b.strChargesPayable='N' ";
				List listTaxDtl = objGlobalFunctionsService.funGetList(sql, "hql");
				List<clsGRNTaxDtlModel> listGRNTaxDtl = new ArrayList<clsGRNTaxDtlModel>();
				for (int cnt = 0; cnt < listTaxDtl.size(); cnt++) {
					clsGRNTaxDtlModel objTaxDtl = new clsGRNTaxDtlModel();
					Object[] arrObj = (Object[]) listTaxDtl.get(cnt);
					objTaxDtl.setStrTaxCode(arrObj[0].toString());
					objTaxDtl.setStrTaxDesc(arrObj[1].toString());
					objTaxDtl.setStrTaxableAmt(Double.parseDouble(arrObj[2].toString()));
					objTaxDtl.setStrTaxAmt(Double.parseDouble(arrObj[3].toString()));
					listGRNTaxDtl.add(objTaxDtl);
				}
				
				double totalTaxAmt=0;
				if (listGRNTaxDtl != null) {
					for (clsGRNTaxDtlModel objTaxDtl : listGRNTaxDtl) {
						clsLinkUpHdModel objLinkTax = objLinkupService.funGetARLinkUp(objTaxDtl.getStrTaxCode(), clientCode, propCode, "Tax", "Purchase");
						if (objLinkTax != null) {
					
							clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
							objJVDetailBean.setStrAccountCode(objLinkTax.getStrAccountCode());
							objJVDetailBean.setStrDescription(objLinkTax.getStrMasterDesc());
							objJVDetailBean.setStrDC("Dr");
							objJVDetailBean.setDblDebitAmt(objTaxDtl.getStrTaxAmt());
							objJVDetailBean.setDblCreditAmt(0.00);
							objJVDetailBean.setStrNarration("WS Tax Desc :" + objTaxDtl.getStrTaxDesc());
							objJVDetailBean.setStrOneLineAcc("R");
							
							listJVDetailBean.add(objJVDetailBean);
							totalTaxAmt+=objTaxDtl.getStrTaxAmt();
						}
						else
						{
							flgLinkup=false;
							sbLinkUpErrorMessage.append("Check Tax linkup"+objTaxDtl.getStrTaxCode()+", ");
						}
					}
				}
	
				if(supplierType.equals("Foreign"))
				{
					if(objModel.getDblFreight()>0)
					{
						clsLinkUpHdModel objLinkFreight = objLinkupService.funGetARLinkUp("Freight", clientCode, propCode, "OtherCharge", "Purchase");
						if (objLinkFreight != null) {
							clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
							objJVDetailBean.setStrAccountCode(objLinkFreight.getStrAccountCode());
							objJVDetailBean.setStrDescription(objLinkFreight.getStrMasterDesc());
							objJVDetailBean.setStrDC("Dr");
							objJVDetailBean.setDblDebitAmt(objModel.getDblFreight());
							objJVDetailBean.setDblCreditAmt(0.00);
							objJVDetailBean.setStrNarration("Freight Charges ");
							objJVDetailBean.setStrOneLineAcc("R");
							
							listJVDetailBean.add(objJVDetailBean);
						}
						else
						{
							flgLinkup=false;
							sbLinkUpErrorMessage.append("Check Freight linkup, ");
						}
					}
					if(objModel.getDblInsurance()>0)
					{
						clsLinkUpHdModel objLinkInsurance = objLinkupService.funGetARLinkUp("Insurance", clientCode, propCode, "OtherCharge", "Purchase");
						if (objLinkInsurance != null) {
							clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
							objJVDetailBean.setStrAccountCode(objLinkInsurance.getStrAccountCode());
							objJVDetailBean.setStrDescription(objLinkInsurance.getStrMasterDesc());
							objJVDetailBean.setStrDC("Dr");
							objJVDetailBean.setDblDebitAmt(objModel.getDblInsurance());
							objJVDetailBean.setDblCreditAmt(0.00);
							objJVDetailBean.setStrNarration("Insurance Charges ");
							objJVDetailBean.setStrOneLineAcc("R");
							
							listJVDetailBean.add(objJVDetailBean);
						}
						else
						{
							flgLinkup=false;
							sbLinkUpErrorMessage.append("Check Insurance linkup, ");
						}
					}
					if(objModel.getDblOtherCharges()>0)
					{
						clsLinkUpHdModel objLinkOtherCharges = objLinkupService.funGetARLinkUp("Other Charge", clientCode, propCode, "OtherCharge", "Purchase");
						if (objLinkOtherCharges != null) {
							clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
							objJVDetailBean.setStrAccountCode(objLinkOtherCharges.getStrAccountCode());
							objJVDetailBean.setStrDescription(objLinkOtherCharges.getStrMasterDesc());
							objJVDetailBean.setStrDC("Dr");
							objJVDetailBean.setDblDebitAmt(objModel.getDblOtherCharges());
							objJVDetailBean.setDblCreditAmt(0.00);
							objJVDetailBean.setStrNarration("Other Charges ");
							objJVDetailBean.setStrOneLineAcc("R");
							
							listJVDetailBean.add(objJVDetailBean);
						}
						else
						{
							flgLinkup=false;
							sbLinkUpErrorMessage.append("Check Other Charges linkup, ");
						}
					}
					
					double totalAgtClearingCharges=0, totalVATClaim=0;
					if(objModel.getDblClearingCharges()>0)
					{
						clsLinkUpHdModel objLinkClearingAgtCharges = objLinkupService.funGetARLinkUp("AgentClearingCharges", clientCode, propCode, "OtherCharge", "Purchase");
						if (objLinkClearingAgtCharges != null) {
							clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
							objJVDetailBean.setStrAccountCode(objLinkClearingAgtCharges.getStrAccountCode());
							objJVDetailBean.setStrDescription(objLinkClearingAgtCharges.getStrMasterDesc());
							objJVDetailBean.setStrDC("Dr");
							objJVDetailBean.setDblDebitAmt(objModel.getDblClearingCharges());
							objJVDetailBean.setDblCreditAmt(0.00);
							objJVDetailBean.setStrNarration("Clearing Agent Charges ");
							objJVDetailBean.setStrOneLineAcc("R");
							
							listJVDetailBean.add(objJVDetailBean);
							totalAgtClearingCharges+=objModel.getDblClearingCharges();
						}
						else
						{
							flgLinkup=false;
							sbLinkUpErrorMessage.append("Check Clearing Agent Charges linkup, ");
						}
					}
					if(objModel.getDblVATClaim()>0)
					{
						clsLinkUpHdModel objLinkVATClaim = objLinkupService.funGetARLinkUp("VATClaim", clientCode, propCode, "OtherCharge", "Purchase");
						if (objLinkVATClaim != null) {
							clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
							objJVDetailBean.setStrAccountCode(objLinkVATClaim.getStrAccountCode());
							objJVDetailBean.setStrDescription(objLinkVATClaim.getStrMasterDesc());
							objJVDetailBean.setStrDC("Dr");
							objJVDetailBean.setDblDebitAmt(objModel.getDblVATClaim());
							objJVDetailBean.setDblCreditAmt(0.00);
							objJVDetailBean.setStrNarration("VAT Claim ");
							objJVDetailBean.setStrOneLineAcc("R");
							
							listJVDetailBean.add(objJVDetailBean);
							totalVATClaim+=objModel.getDblVATClaim();
						}
						else
						{
							flgLinkup=false;
							sbLinkUpErrorMessage.append("Check VAT Claim linkup, ");
						}
					}
				
					
					sql = "select a.strTaxCode,a.strTaxDesc,a.strTaxableAmt,a.strTaxAmt "
							+ " from clsGRNTaxDtlModel a, clsTaxHdModel b " 
							+ " where a.strTaxCode=b.strTaxCode and a.strGRNCode='" + GRNCode + "' and a.strClientCode='" + clientCode + "' "
						    + " and b.strChargesPayable='Y' ";
					List listTaxDtlChPayables = objGlobalFunctionsService.funGetList(sql, "hql");
					List<clsGRNTaxDtlModel> listGRNTaxChPayableDtl = new ArrayList<clsGRNTaxDtlModel>();
					for (int cnt = 0; cnt < listTaxDtlChPayables.size(); cnt++) {
						clsGRNTaxDtlModel objTaxDtl = new clsGRNTaxDtlModel();
						Object[] arrObj = (Object[]) listTaxDtlChPayables.get(cnt);
						objTaxDtl.setStrTaxCode(arrObj[0].toString());
						objTaxDtl.setStrTaxDesc(arrObj[1].toString());
						objTaxDtl.setStrTaxableAmt(Double.parseDouble(arrObj[2].toString()));
						objTaxDtl.setStrTaxAmt(Double.parseDouble(arrObj[3].toString()));
						listGRNTaxChPayableDtl.add(objTaxDtl);
					}
					
					double totalVATClaimAndClearingCharges=totalVATClaim+totalAgtClearingCharges;
					if(totalVATClaimAndClearingCharges>0)
					{
						if (listGRNTaxChPayableDtl != null) {
							for (clsGRNTaxDtlModel objTaxDtl : listGRNTaxChPayableDtl) {
								clsLinkUpHdModel objLinkTax = objLinkupService.funGetARLinkUp(objTaxDtl.getStrTaxCode(), clientCode, propCode, "Tax", "Purchase");
								if (objLinkTax != null) {
							
									clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
									objJVDetailBean.setStrAccountCode(objLinkTax.getStrAccountCode());
									objJVDetailBean.setStrDescription(objLinkTax.getStrMasterDesc());
									objJVDetailBean.setStrDC("Cr");
									objJVDetailBean.setDblDebitAmt(0.00);
									objJVDetailBean.setDblCreditAmt(totalVATClaimAndClearingCharges);
									objJVDetailBean.setStrNarration("VAT Claim and Clearing Charges Payable");
									objJVDetailBean.setStrOneLineAcc("R");
									
									listJVDetailBean.add(objJVDetailBean);
									totalTaxAmt+=objTaxDtl.getStrTaxAmt();
								}
								else
								{
									flgLinkup=false;
									sbLinkUpErrorMessage.append("Check Tax linkup for "+objTaxDtl.getStrTaxCode()+", ");
								}
							}
						}
					}
					
					clsLinkUpHdModel objLinkTaxPayable = objLinkupService.funGetARLinkUp("TAXPAYABLE", clientCode, propCode, "OtherCharge", "Purchase");
					if (objLinkTaxPayable != null) {
				
						clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
						objJVDetailBean.setStrAccountCode(objLinkTaxPayable.getStrAccountCode());
						objJVDetailBean.setStrDescription(objLinkTaxPayable.getStrMasterDesc());
						objJVDetailBean.setStrDC("Cr");
						objJVDetailBean.setDblDebitAmt(0.00);
						objJVDetailBean.setDblCreditAmt(totalTaxAmt);
						objJVDetailBean.setStrNarration("Tax Payable for Foreign Imports");
						objJVDetailBean.setStrOneLineAcc("R");
						
						listJVDetailBean.add(objJVDetailBean);
					}
					else
					{
						flgLinkup=false;
						sbLinkUpErrorMessage.append("Check Tax Payable linkup, ");
					}
				}
				
				if (objModel.getDblDisAmt() * currValue !=  0) {
					clsLinkUpHdModel objLinkDisc = objLinkupService.funGetARLinkUp("PURDISC", clientCode, propCode, "Discount", "Purchase");
					if (objLinkDisc != null) {
							
						clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
						objJVDetailBean.setStrAccountCode(objLinkDisc.getStrAccountCode());
						objJVDetailBean.setStrDescription(objLinkDisc.getStrMasterDesc());
						objJVDetailBean.setStrDC("Cr");
						objJVDetailBean.setDblCreditAmt(objModel.getDblDisAmt());
						objJVDetailBean.setDblDebitAmt(0.00);
						objJVDetailBean.setStrNarration("WS Discount Desc :" + objModel.getDblDisAmt());
						objJVDetailBean.setStrOneLineAcc("R");
						
						listJVDetailBean.add(objJVDetailBean);
					}
					else
					{
						flgLinkup=false;
						sbLinkUpErrorMessage.append("Check Discount linkup, ");
					}
				}
	
				if (objModel.getDblExtra() !=  0) {
					clsLinkUpHdModel objLinkExtraCharge = objLinkupService.funGetARLinkUp("ExtraCharge", clientCode, propCode, "ExtraCharge", "Purchase");
					if (objLinkExtraCharge != null) {
												
						clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
						objJVDetailBean.setStrAccountCode(objLinkExtraCharge.getStrAccountCode());
						objJVDetailBean.setStrDescription(objLinkExtraCharge.getStrMasterDesc());
						objJVDetailBean.setStrDC("Dr");
						objJVDetailBean.setDblDebitAmt(objModel.getDblExtra());
						objJVDetailBean.setDblCreditAmt(0.00);
						objJVDetailBean.setStrNarration("WS Extra Charge Desc :" + objModel.getDblExtra());
						objJVDetailBean.setStrOneLineAcc("R");
						
						listJVDetailBean.add(objJVDetailBean);
					}
					else
					{
						flgLinkup=false;
						sbLinkUpErrorMessage.append("Check Extra Charge linkup, ");
					}
				}
	
				if(!clientCode.equals("261.001")){
					if (objModel.getDblRoundOff() != 0) {
						clsLinkUpHdModel objLinkRoundOff = objLinkupService.funGetARLinkUp("PURROUNDOFF", clientCode, propCode, "RoundOff", "Purchase");
						if (objLinkRoundOff != null) {
		
							clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
							objJVDetailBean.setStrAccountCode(objLinkRoundOff.getStrAccountCode());
							objJVDetailBean.setStrDescription(objLinkRoundOff.getStrMasterDesc());
							if (objModel.getDblRoundOff() > 0) {
								objJVDetailBean.setStrDC("Dr");
								objJVDetailBean.setDblDebitAmt(objModel.getDblRoundOff());
								objJVDetailBean.setDblCreditAmt(0.00);
							} else {
								objJVDetailBean.setStrDC("Cr");
								objJVDetailBean.setDblCreditAmt(objModel.getDblRoundOff()*-1);
								objJVDetailBean.setDblDebitAmt(0.00);
							}
							objJVDetailBean.setStrOneLineAcc("R");
							listJVDetailBean.add(objJVDetailBean);
						}
						else
						{
							flgLinkup=false;
							sbLinkUpErrorMessage.append("Check Round Off linkup, ");
						}
					}

				}
				
				String sqlGRNDtl = " select a.strGRNCode,a.dblTotal,b.strDebtorCode,b.strPName,date(a.dtGRNDate),a.strNarration ,date(a.dtDueDate),a.strBillNo,b.strPCode "
					+ " from tblgrnhd a,tblpartymaster b "
					+ " where a.strSuppCode =b.strPCode and a.strGRNCode='" + objModel.getStrGRNCode() + "' and a.strClientCode='" + objModel.getStrClientCode() + "' ";
				List listSupplier = objGlobalFunctionsService.funGetList(sqlGRNDtl, "sql");
				if (null != listSupplier) {
					for (int i = 0; i < listSupplier.size(); i++) {
						
						Object[] ob = (Object[]) listSupplier.get(i);
						
						String accountCode="",accountName="",debtorCode="",debtorName="";
						List listCustSuppAccDetails=objGlobalFunctions.funGetCustSupplierAccountDetails("Supplier",ob[8].toString() , clientCode, propCode);
						if(listCustSuppAccDetails.size()>0)
						{
							debtorCode=listCustSuppAccDetails.get(0).toString();
							debtorName=listCustSuppAccDetails.get(1).toString();
							accountCode=listCustSuppAccDetails.get(2).toString();
							accountName=listCustSuppAccDetails.get(3).toString();
						}
						else
						{
							flgLinkup=false;
							sbLinkUpErrorMessage.append("Check Supplier linkup "+ob[3].toString()+", ");
						}
					
						clsJVDetailsBean objJVDtlBean=new clsJVDetailsBean();
						objJVDtlBean.setStrAccountCode(accountCode);
						objJVDtlBean.setStrDescription(accountName);
						objJVDtlBean.setStrDC("Cr");
						objJVDtlBean.setDblCreditAmt(objModel.getDblTotal());
						if(supplierType.equals("Foreign"))
						{
							double FOBAmt=objModel.getDblSubTotal();
							double freightAmt=objModel.getDblFreight();
							double insuranceAmt=objModel.getDblInsurance();
							double otherCharges=objModel.getDblOtherCharges();
							double totalDebitAmt=FOBAmt;
							totalDebitAmt=FOBAmt+freightAmt+insuranceAmt+otherCharges+objModel.getDblRoundOff()+objModel.getDblExtra()-objModel.getDblDisAmt() ;
							objJVDtlBean.setDblCreditAmt(totalDebitAmt);
						}
						objJVDtlBean.setDblDebitAmt(0.00);
						objJVDtlBean.setStrNarration("GRN Supplier ");
						if(supplierType.equals("Foreign"))
							objJVDtlBean.setStrNarration("GRN Supplier inclusive of Freight, Insurance and Other Charges");
							
						objJVDtlBean.setStrOneLineAcc("R");
						listJVDetailBean.add(objJVDtlBean);
				// jvdtl entry end
						
						
				// jvDebtor detail entry start
						clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
						objJVDetailBean.setStrAccountCode(accountCode);
						objJVDetailBean.setStrDescription(accountName);
						objJVDetailBean.setStrDC("Cr");
						objJVDetailBean.setDblCreditAmt(objModel.getDblTotal());
						if(supplierType.equals("Foreign"))
						{
							double FOBAmt=objModel.getDblSubTotal();
							double freightAmt=objModel.getDblFreight();
							double insuranceAmt=objModel.getDblInsurance();
							double otherCharges=objModel.getDblOtherCharges();
							double totalDebitAmt=FOBAmt;
							totalDebitAmt=FOBAmt+freightAmt+insuranceAmt+otherCharges+objModel.getDblRoundOff()+objModel.getDblExtra()-objModel.getDblDisAmt() ;
							objJVDetailBean.setDblCreditAmt(totalDebitAmt);
						}
							
						objJVDetailBean.setDblDebitAmt(0.00);
						objJVDetailBean.setStrNarration("GRN Supplier " + objModel.getStrSuppCode());
						objJVDetailBean.setStrOneLineAcc("R");
						
						objJVDetailBean.setStrDebtorCode(debtorCode);
						objJVDetailBean.setStrDebtorName(debtorName);
						objJVDetailBean.setStrBillNo(ob[7].toString());
						objJVDetailBean.setStrInvoiceNo(ob[0].toString());
						objJVDetailBean.setStrGuest("");
						objJVDetailBean.setStrCreditNo("");
						objJVDetailBean.setDteBillDate(ob[4].toString());
						objJVDetailBean.setDteInvoiceDate(ob[4].toString());
						objJVDetailBean.setDteDueDate(ob[6].toString());
						objJVDetailBean.setStrRegistrationNo("");
						
						listJVDetailBean.add(objJVDetailBean);
					}
				}
			// jvDebtor detail entry end
				
			}
			else
			{
				flgLinkup=false;
				sbLinkUpErrorMessage.append("Supplier linkup is not done");
			}
			clsJVHdModel objJVHdModel =null;
			if(flgLinkup)
			{
				objJVBean.setListJVDtlBean(listJVDetailBean);
				objJVHdModel = objJVController.funGenerateJV(objJVBean, userCode, clientCode, "APGL", req,"GRN");
				jvCode=objJVHdModel.getStrVouchNo()+"! ";
			}
			else
			{
				jvCode=sbLinkUpErrorMessage.toString();
			}
			
			
			
			
			//Generate Direct Payment for Jv in Cash Settlement
			
			if(objModel.getStrPayMode().equalsIgnoreCase("cash")){  //hard code settlement = cash
				//funGeneratePaymentForGRNJV(objModel,objModel.getStrPayMode(), clientCode,  userCode,  propCode,req);
				
			}else if(objModel.getStrPayMode().startsWith("S00")){ 				//check cash settlement code

				String sqlCheckGrnSettlement="select a.strSettlementType from tblsettlementmaster a where a.strSettlementCode='"+objModel.getStrPayMode()+"' ";
				List listGrnSettlement = objGlobalFunctionsService.funGetList(sqlCheckGrnSettlement, "sql");
				if (null != listGrnSettlement) {
					String settlementType=(String) listGrnSettlement.get(0);
					if(settlementType.equalsIgnoreCase("cash")){
						clsPaymentHdModel objPaymentHdModel = funGeneratePaymentForGRNJV(objModel,objJVHdModel,objModel.getStrPayMode(), clientCode,  userCode,  propCode,req);
						
					}
					
				}
			}
			
			
		}
		return jvCode;
	}
	
	public String funGenrateJVforInvoice(String invoiceCode, String clientCode, String userCode, String propCode, HttpServletRequest req) {

		boolean flgLinkup=true;
		StringBuilder sbLinkUpErrorMessage=new StringBuilder();
		sbLinkUpErrorMessage.append("ERROR!");
		StringBuilder sbSql=new StringBuilder();
		clsJVBean objJVBean = new clsJVBean();
		clsInvoiceHdModel objModel=objInvoiceService.funGetInvoiceHd(invoiceCode, clientCode);
		String date=objModel.getDteInvDate().split(" ")[0];
		objModel.setDteInvDate(date);

		List<clsJVDetailsBean> listJVDetailBean = new ArrayList<clsJVDetailsBean>();
		clsInvoiceHdModel objInvHDModelList = objInvoiceService.funGetInvoiceDtl(invoiceCode, clientCode);
		List<clsInvoiceModelDtl> listDtlModel = objInvHDModelList.getListInvDtlModel();
		
		String jvCode = "",withHoldingApplicable="";
		String custCode = objModel.getStrCustCode();
		double debitAmt = objModel.getDblGrandTotal();
		String strCurr = req.getSession().getAttribute("currValue").toString();
		double currValue = Double.parseDouble(strCurr);
		double taxableAmt=0;
		double taxAmt=0;
		double roundOff=0;
		
		clsLinkUpHdModel objLinkCust = objLinkupService.funGetARLinkUp(custCode, clientCode, propCode, "Customer", "Sale");
		if (objLinkCust != null) {
			if (objModel.getStrDktNo().equals("")) {
				objJVBean.setStrVouchNo("");

			} else {
				objJVBean.setStrVouchNo(objModel.getStrDktNo());
			}
			
			String currentDateTime=objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd");
			String time=currentDateTime.split(" ")[1];
			
			objJVBean.setStrVouchNo("");
			sbSql.setLength(0);
			sbSql.append("select objJVModel from clsJVHdModel objJVModel where objJVModel.strSourceDocNo='"+objModel.getStrInvCode()+"' and objJVModel.strClientCode='"+clientCode+"'");	
			try
			{
				List listJVDtl=objBaseService.funGetListModuleWise(sbSql, "hql", "WebBooks");
				if(listJVDtl.size()>0)
				{
					clsJVHdModel objJVHdModel=(clsJVHdModel)listJVDtl.get(0);
					objJVBean.setStrVouchNo(objJVHdModel.getStrVouchNo());
				}
				System.out.println(listJVDtl);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			objJVBean.setStrNarration("JV Generated by Invoice:" + objModel.getStrInvCode());
			objJVBean.setStrSancCode("");
			objJVBean.setStrType("");
			objJVBean.setDteVouchDate(objGlobalFunctions.funGetDate("dd-MM-yyyy", objModel.getDteInvDate()) +" "+time);
			objJVBean.setIntVouchMonth(1);
			objJVBean.setDblAmt(objModel.getDblSubTotalAmt());
			objJVBean.setStrTransType("R");
			objJVBean.setStrTransMode("A");
			objJVBean.setStrModuleType("AP");
			objJVBean.setStrMasterPOS("WEBCRM");
			objJVBean.setStrUserCreated(userCode);
			objJVBean.setStrUserEdited(userCode);
			objJVBean.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objJVBean.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objJVBean.setStrClientCode(clientCode);
			objJVBean.setStrPropertyCode(propCode);
			objJVBean.setStrSource("Invoice");
			objJVBean.setStrSourceDocNo(objModel.getStrInvCode());
			objJVBean.setStrCurrency(objModel.getStrCurrencyCode());
			objJVBean.setDblConversion(objModel.getDblCurrencyConv());
		// jvhd entry end
			
			
		// jvdtl entry Start
			for (clsInvoiceModelDtl objDtl : listDtlModel) {

				clsProductMasterModel objProdModle = objProductMasterService.funGetObject(objDtl.getStrProdCode(), clientCode);
				clsLinkUpHdModel objLinkSubGroup = objLinkupService.funGetARLinkUp(objProdModle.getStrSGCode(), clientCode, propCode, "SubGroup", "Sale");
				if (objProdModle != null && objLinkSubGroup != null) {
					
					clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					
					objJVDetailBean.setStrAccountCode(objLinkSubGroup.getStrAccountCode());
					objJVDetailBean.setStrDescription(objLinkSubGroup.getStrMasterDesc());
					objJVDetailBean.setStrDC("Cr");
					objJVDetailBean.setDblDebitAmt(0.00);
					objJVDetailBean.setDblCreditAmt(objDtl.getDblQty() * objDtl.getDblUnitPrice());
					objJVDetailBean.setStrNarration("WS Product code :" + objDtl.getStrProdCode());
					objJVDetailBean.setStrOneLineAcc("R");
					
					listJVDetailBean.add(objJVDetailBean);
				}
				else
				{
					flgLinkup=false;
					sbLinkUpErrorMessage.append("Check SubGroup linkup, ");
				}
			}

			List<clsInvoiceTaxDtlModel> listTaxDtl = new ArrayList<clsInvoiceTaxDtlModel>();
			String sqlInv = "select strTaxCode,strTaxDesc,dblTaxableAmt,dblTaxAmt from tblinvtaxdtl " + "where strInvCode='" + invoiceCode + "' and strClientCode='" + clientCode + "'";
			List list = objGlobalFunctionsService.funGetList(sqlInv, "sql");
			
			for (int cnt = 0; cnt < list.size(); cnt++) {
				clsInvoiceTaxDtlModel objTaxDtl = new clsInvoiceTaxDtlModel();
				Object[] arrObj = (Object[]) list.get(cnt);
				objTaxDtl.setStrTaxCode(arrObj[0].toString());
				objTaxDtl.setStrTaxDesc(arrObj[1].toString());
				objTaxDtl.setDblTaxableAmt(Double.parseDouble(arrObj[2].toString()));
				objTaxDtl.setDblTaxAmt(Double.parseDouble(arrObj[3].toString()));
				taxAmt+=Double.parseDouble(arrObj[3].toString());
				listTaxDtl.add(objTaxDtl);
				if(Double.parseDouble(arrObj[3].toString())>0)
				{
				taxableAmt+=Double.parseDouble(arrObj[2].toString());
			    }
			}
			
			if (listTaxDtl != null) {
				for (clsInvoiceTaxDtlModel objTaxDtl : listTaxDtl) {
					clsLinkUpHdModel objLinkTax = objLinkupService.funGetARLinkUp(objTaxDtl.getStrTaxCode(), clientCode, propCode, "Tax", "Sale");
					if (objLinkTax != null) {
						
						clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
						
						objJVDetailBean.setStrAccountCode(objLinkTax.getStrAccountCode());
						objJVDetailBean.setStrDescription(objLinkTax.getStrMasterDesc());
						objJVDetailBean.setStrDC("Cr");
						objJVDetailBean.setDblDebitAmt(0.00);
						objJVDetailBean.setDblCreditAmt(objTaxDtl.getDblTaxAmt());
						objJVDetailBean.setStrNarration("WS Tax Desc :" + objTaxDtl.getStrTaxDesc());
						objJVDetailBean.setStrOneLineAcc("R");
						
						listJVDetailBean.add(objJVDetailBean);
					}
					else
					{
						flgLinkup=false;
						sbLinkUpErrorMessage.append("Check Tax linkup, ");
					}
				}
			}
			
			if (objModel.getDblDiscountAmt() / currValue != 0) {
				clsLinkUpHdModel objLinkDisc = objLinkupService.funGetARLinkUp("SALEDISC", clientCode, propCode, "Discount", "Sale");
				if (objLinkDisc != null) {
										
					clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					
					objJVDetailBean.setStrAccountCode(objLinkDisc.getStrAccountCode());
					objJVDetailBean.setStrDescription(objLinkDisc.getStrMasterDesc());
					objJVDetailBean.setStrDC("Dr");
					objJVDetailBean.setDblDebitAmt(objModel.getDblDiscountAmt());
					objJVDetailBean.setDblCreditAmt(0.00);
					objJVDetailBean.setStrNarration("WS Sale Discount Desc :" + objModel.getDblDiscountAmt());
					objJVDetailBean.setStrOneLineAcc("R");
					
					listJVDetailBean.add(objJVDetailBean);
				}
				else
				{
					flgLinkup=false;
					sbLinkUpErrorMessage.append("Check Discount linkup, ");
				}
			}
			
			if (objModel.getDblExtraCharges() > 0) {
				clsLinkUpHdModel objLinkDisc = objLinkupService.funGetARLinkUp("ExtraCharge", clientCode, propCode, "ExtraCharge", "Sale");
				if (objLinkDisc != null) {
										
					clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					
					objJVDetailBean.setStrAccountCode(objLinkDisc.getStrAccountCode());
					objJVDetailBean.setStrDescription(objLinkDisc.getStrMasterDesc());
					objJVDetailBean.setStrDC("Cr");
					objJVDetailBean.setDblCreditAmt(objModel.getDblExtraCharges());
					objJVDetailBean.setDblDebitAmt(0.00);
					objJVDetailBean.setStrNarration("WS Sale Extra Charges :" + objModel.getDblExtraCharges());
					objJVDetailBean.setStrOneLineAcc("R");
					
					listJVDetailBean.add(objJVDetailBean);
				}
				else
				{
					flgLinkup=false;
					sbLinkUpErrorMessage.append("Check Extra Charges linkup, ");
				}
			}
			
			roundOff=objModel.getDblGrandTotal()-(objModel.getDblSubTotalAmt()+ taxAmt+objModel.getDblExtraCharges())-objModel.getDblDiscountAmt();
			
			
			
			if (roundOff != 0) {
				clsLinkUpHdModel objLinkRoundOff = objLinkupService.funGetARLinkUp("RoundOff", clientCode, propCode, "RoundOff", "Sale");
				if (objLinkRoundOff != null) {

					clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					objJVDetailBean.setStrAccountCode(objLinkRoundOff.getStrAccountCode());
					objJVDetailBean.setStrDescription(objLinkRoundOff.getStrMasterDesc());
					if (roundOff > 0) {
						objJVDetailBean.setStrDC("Cr");
						objJVDetailBean.setDblDebitAmt(0.00);
						objJVDetailBean.setDblCreditAmt(roundOff);
					} else {
						objJVDetailBean.setStrDC("Dr");
						objJVDetailBean.setDblCreditAmt(0.00);
						objJVDetailBean.setDblDebitAmt(roundOff*-1);
					}
					objJVDetailBean.setStrOneLineAcc("R");
					listJVDetailBean.add(objJVDetailBean);
				}
				else
				{
					flgLinkup=false;
					sbLinkUpErrorMessage.append("Check Round Off linkup, ");
				}
			}

			clsLinkUpHdModel objLinkSettlements=null;
			clsSettlementMasterModel objSettlementMasterModel = objSttlementMasterService.funGetObject(objModel.getStrSettlementCode(), clientCode);
			if(objSettlementMasterModel.getStrSettlementType().equals("Cash") || objSettlementMasterModel.getStrSettlementType().equals("Online Payment"))
			{
				objLinkSettlements = objLinkupService.funGetARLinkUp(objModel.getStrSettlementCode(), clientCode, propCode, "Settlement", "Sale");
			}
			
			String sql = " select a.strInvCode,a.dblGrandTotal,b.strDebtorCode,b.strPName,date(a.dteInvDate),a.strNarration "
				+ ",date(a.dteInvDate),a.strInvCode,b.strApplForWT " 
				+ " from tblinvoicehd a,tblpartymaster b " 
				+ " where a.strCustCode =b.strPCode and a.strInvCode='" + objModel.getStrInvCode() + "' and a.strClientCode='" + objModel.getStrClientCode() + "'   ";
			List listCust = objGlobalFunctionsService.funGetList(sql, "sql");
			if (null != listCust) {
				for (int i = 0; i < listCust.size(); i++) {
					
					Object[] ob = (Object[]) listCust.get(i);
					
					String accountCode="",accountName="",debtorCode="",debtorName="";
					List listCustSuppAccDetails=objGlobalFunctions.funGetCustSupplierAccountDetails("Customer",objModel.getStrCustCode(), clientCode, propCode);
					if(listCustSuppAccDetails.size()>0)
					{
						debtorCode=listCustSuppAccDetails.get(0).toString();
						debtorName=listCustSuppAccDetails.get(1).toString();
						accountCode=listCustSuppAccDetails.get(2).toString();
						accountName=listCustSuppAccDetails.get(3).toString();
					}
					else
					{
						flgLinkup=false;
						sbLinkUpErrorMessage.append("Check Customer linkup, ");
					}
					
					if(objSettlementMasterModel.getStrSettlementType().equals("Cash") || objSettlementMasterModel.getStrSettlementType().equals("Online Payment"))
					{
						if(null!=objLinkSettlements)
						{
							accountCode=objLinkSettlements.getStrAccountCode();
							accountName=objLinkSettlements.getStrMasterDesc();
						}
					}
					
					clsJVDetailsBean objJVDtlBean=new clsJVDetailsBean();
					objJVDtlBean.setStrAccountCode(accountCode);
					objJVDtlBean.setStrDescription(accountName);
					objJVDtlBean.setStrDC("Dr");
					objJVDtlBean.setDblDebitAmt(debitAmt);
					objJVDtlBean.setDblCreditAmt(0.00);
					objJVDtlBean.setStrNarration("Invoice Customer ");
					objJVDtlBean.setStrOneLineAcc("R");
					listJVDetailBean.add(objJVDtlBean);
				// jvdtl entry end

				// jvDebtor detail entry start
					
					if(objSettlementMasterModel.getStrSettlementType().equals("Credit"))
					{	
						clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
						objJVDetailBean.setStrAccountCode(accountCode);
						objJVDetailBean.setStrDescription(accountName);
						objJVDetailBean.setStrDC("Dr");
						objJVDetailBean.setDblDebitAmt(debitAmt);
						objJVDetailBean.setDblCreditAmt(0.00);
						objJVDetailBean.setStrNarration("Invoice Customer ");
						objJVDetailBean.setStrOneLineAcc("R");
						objJVDetailBean.setStrDebtorCode(debtorCode);
						objJVDetailBean.setStrDebtorName(debtorName);
						objJVDetailBean.setStrBillNo(ob[7].toString());
						objJVDetailBean.setStrInvoiceNo(ob[0].toString());
						objJVDetailBean.setStrGuest("");
						objJVDetailBean.setStrCreditNo("");
						objJVDetailBean.setDteBillDate(ob[4].toString());
						objJVDetailBean.setDteInvoiceDate(ob[4].toString());
						objJVDetailBean.setDteDueDate(ob[6].toString());
						objJVDetailBean.setStrRegistrationNo("");
						
						listJVDetailBean.add(objJVDetailBean);
					}
					withHoldingApplicable=ob[8].toString();
				}
			}
		// jvDebtor detail entry end
			
		}
		else
		{
			flgLinkup=false;
			sbLinkUpErrorMessage.append("Check Customer linkup, ");
		}
		
		if(flgLinkup)
		{
			objJVBean.setListJVDtlBean(listJVDetailBean);
			clsJVHdModel objJVHdModel = objJVController.funGenerateJV(objJVBean, userCode, clientCode, "AR", req, "Invoice");
			jvCode=objJVHdModel.getStrVouchNo();
			
			if(withHoldingApplicable.equals("Y"))
				funGenerateReversalTaxJV(objModel.getStrInvCode(), objModel.getDteInvDate(), taxableAmt, objModel.getStrCustCode(), clientCode, userCode, propCode, "Invoice", req,objModel.getStrCurrencyCode(),objModel.getDblCurrencyConv());
		}
		else
		{
			jvCode=sbLinkUpErrorMessage.toString();
		}
		
		return jvCode;
	}
	
	private String funGenerateReversalTaxJV(String docCode, String docDate,double docTaxableAmt, String customerCode, String clientCode, String userCode, String propCode, String transType, HttpServletRequest req,String currency,double conversionRate)
	{
		StringBuilder sbSql=new StringBuilder();
		clsJVBean objJVBean = new clsJVBean();
		String jvCode = "";
		
		String strCurr = req.getSession().getAttribute("currValue").toString();
		double currValue = Double.parseDouble(strCurr);
		
		clsLinkUpHdModel objLinkCust = objLinkupService.funGetARLinkUp(customerCode, clientCode, propCode, "Customer", "Sale");
		if (objLinkCust != null) {
			
			String currentDateTime=objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd");
			String time=currentDateTime.split(" ")[1];
			String withHoldingTaxCode="",withHoldingTaxName="";
			double withHoldingTaxAmt=0;
			
			sbSql.setLength(0);
			sbSql.append("select objTaxModel from clsTaxHdModel objTaxModel where objTaxModel.strTaxReversal='Y' and objTaxModel.strClientCode='"+clientCode+"'");
			try
			{
				List listJVDtl=objBaseService.funGetListModuleWise(sbSql, "hql", "WebStocks");
				if(listJVDtl.size()>0)
				{
					clsTaxHdModel objTaxModel=(clsTaxHdModel)listJVDtl.get(0);
					withHoldingTaxCode=objTaxModel.getStrTaxCode();
					withHoldingTaxName=objTaxModel.getStrTaxDesc();
					withHoldingTaxAmt=docTaxableAmt*(objTaxModel.getDblPercent()/100);
				}
				System.out.println(listJVDtl);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			String sourceDocNo=docCode+withHoldingTaxCode;
			objJVBean.setStrVouchNo("");
			sbSql.setLength(0);
			sbSql.append("select objJVModel from clsJVHdModel objJVModel where objJVModel.strSourceDocNo='"+sourceDocNo+"' and objJVModel.strClientCode='"+clientCode+"'");	
			try
			{
				List listJVDtl=objBaseService.funGetListModuleWise(sbSql, "hql", "WebBooks");
				if(listJVDtl.size()>0)
				{
					clsJVHdModel objJVHdModel=(clsJVHdModel)listJVDtl.get(0);
					objJVBean.setStrVouchNo(objJVHdModel.getStrVouchNo());
				}
				System.out.println(listJVDtl);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			objJVBean.setStrNarration("JV Generated for withholding :" + docCode);
			objJVBean.setStrSancCode("");
			objJVBean.setStrType("");
			objJVBean.setDteVouchDate(objGlobalFunctions.funGetDate("dd-MM-yyyy", docDate) +" "+time);
			objJVBean.setIntVouchMonth(1);
			objJVBean.setDblAmt(withHoldingTaxAmt);
			objJVBean.setStrTransType("R");
			objJVBean.setStrTransMode("A");
			objJVBean.setStrModuleType("AP");
			objJVBean.setStrMasterPOS("WEBCRM");
			objJVBean.setStrUserCreated(userCode);
			objJVBean.setStrUserEdited(userCode);
			objJVBean.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objJVBean.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objJVBean.setStrClientCode(clientCode);
			objJVBean.setStrPropertyCode(propCode);
			objJVBean.setStrSource("Invoice");
			objJVBean.setStrSourceDocNo(sourceDocNo);
			objJVBean.setDblConversion(conversionRate);
			objJVBean.setStrCurrency(currency);
			// jvhd entry end

			
		// jvdtl entry Start
			List<clsJVDetailsBean> listJVDetailBean = new ArrayList<clsJVDetailsBean>();
			clsLinkUpHdModel objLinkTax = objLinkupService.funGetARLinkUp(withHoldingTaxCode, clientCode, propCode, "Tax", "Sale");
			if (objLinkTax != null) {
						
				clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
				objJVDetailBean.setStrAccountCode(objLinkTax.getStrAccountCode());
				objJVDetailBean.setStrDescription(objLinkTax.getStrMasterDesc());
				
				if(transType.equals("Invoice"))
				{
					objJVDetailBean.setStrDC("Dr");
					objJVDetailBean.setDblCreditAmt(0.00);
					objJVDetailBean.setDblDebitAmt(withHoldingTaxAmt);
				}
				else
				{
					objJVDetailBean.setStrDC("Cr");
					objJVDetailBean.setDblDebitAmt(0.00);
					objJVDetailBean.setDblCreditAmt(withHoldingTaxAmt);
				}
				objJVDetailBean.setStrNarration("WT Tax Desc :" + withHoldingTaxName);
				objJVDetailBean.setStrOneLineAcc("R");
				
				listJVDetailBean.add(objJVDetailBean);
			}
			
			/*String accCode="",accName="";
			List listDCAcc=objGlobalFunctions.funGetDebotCreditorAccountDetails("Debtor",objLinkCust.getStrAccountCode() , clientCode, propCode);
			if(listDCAcc.size()>0)
			{
				accCode=listDCAcc.get(0).toString();
				accName=listDCAcc.get(1).toString();
			}*/
		
			String sql = " select a.strInvCode,a.dblGrandTotal,b.strDebtorCode,b.strPName,date(a.dteInvDate),a.strNarration ,date(a.dteInvDate),a.strInvCode,b.strApplForWT " 
				+ " from tblinvoicehd a,tblpartymaster b " 
				+ " where a.strCustCode =b.strPCode and a.strInvCode='" + docCode + "' and a.strClientCode='" + clientCode + "'   ";
			
			if(transType.equals("SalesReturn"))
			{
				sql = " select a.strSRCode,a.dblTotalAmt,b.strDebtorCode,b.strPName,date(a.dteSRDate),'',date(a.dteSRDate),a.strSRCode,b.strApplForWT"
					+ " from tblsalesreturnhd a,tblpartymaster b " 
					+ " where a.strCustCode =b.strPCode and a.strSRCode='" + docCode + "' and a.strClientCode='" + clientCode + "'   ";
			} 
			
			List listCustDetail = objGlobalFunctionsService.funGetList(sql, "sql");
			if (null != listCustDetail) {
				for (int i = 0; i < listCustDetail.size(); i++) {
					
					Object[] ob = (Object[]) listCustDetail.get(i);
					
					String accountCode="",accountName="",debtorCode="",debtorName="";
					List listCustSuppAccDetails=objGlobalFunctions.funGetCustSupplierAccountDetails("Customer",customerCode, clientCode, propCode);
					if(listCustSuppAccDetails.size()>0)
					{
						debtorCode=listCustSuppAccDetails.get(0).toString();
						debtorName=listCustSuppAccDetails.get(1).toString();
						accountCode=listCustSuppAccDetails.get(2).toString();
						accountName=listCustSuppAccDetails.get(3).toString();
					}
					
					clsJVDetailsBean objJVDetailBeanCust=new clsJVDetailsBean();
					objJVDetailBeanCust.setStrAccountCode(accountCode);
					objJVDetailBeanCust.setStrDescription(accountName);
					
					if(transType.equals("Invoice"))
					{
						objJVDetailBeanCust.setStrDC("Cr");
						objJVDetailBeanCust.setDblCreditAmt(withHoldingTaxAmt);
						objJVDetailBeanCust.setDblDebitAmt(0.00);
					}
					else
					{
						objJVDetailBeanCust.setStrDC("Dr");
						objJVDetailBeanCust.setDblDebitAmt(withHoldingTaxAmt);
						objJVDetailBeanCust.setDblCreditAmt(0.00);
					}
					objJVDetailBeanCust.setStrNarration("Invoice Withholding Customer: " +debtorName);
					objJVDetailBeanCust.setStrOneLineAcc("R");
					listJVDetailBean.add(objJVDetailBeanCust);
				// jvdtl entry end

					
				// jvDebtor detail entry start
					clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					objJVDetailBean.setStrAccountCode(accountCode);
					objJVDetailBean.setStrDescription(accountName);
					
					if(transType.equals("Invoice"))
					{
						objJVDetailBean.setStrDC("Cr");
						objJVDetailBean.setDblCreditAmt(withHoldingTaxAmt);
						objJVDetailBean.setDblDebitAmt(0.00);
					}
					else
					{
						objJVDetailBean.setStrDC("Dr");
						objJVDetailBean.setDblDebitAmt(withHoldingTaxAmt);
						objJVDetailBean.setDblCreditAmt(0.00);
					}
					
					objJVDetailBean.setStrNarration("Withholding Customer ");
					objJVDetailBean.setStrOneLineAcc("R");
					objJVDetailBean.setStrDebtorCode(debtorCode);
					objJVDetailBean.setStrDebtorName(debtorName);
					objJVDetailBean.setStrBillNo(ob[7].toString());
					objJVDetailBean.setStrInvoiceNo(ob[0].toString());
					objJVDetailBean.setStrGuest("");
					objJVDetailBean.setStrCreditNo("");
					objJVDetailBean.setDteBillDate(ob[4].toString());
					objJVDetailBean.setDteInvoiceDate(ob[4].toString());
					objJVDetailBean.setDteDueDate(ob[6].toString());
					objJVDetailBean.setStrRegistrationNo("");
					
					listJVDetailBean.add(objJVDetailBean);
				}
			}
		// jvDebtor detail entry end

			objJVBean.setListJVDtlBean(listJVDetailBean);
			objJVController.funGenerateJV(objJVBean, userCode, clientCode, "AR", req, "Invoice");
		}
		
		return jvCode;	
	}

	public String funGenrateJVforPurchaseReturn(String PRCode,  String clientCode, String userCode, String propCode, HttpServletRequest req) {
		
		boolean flgLinkup=true;
		StringBuilder sbLinkUpErrorMessage=new StringBuilder();
		sbLinkUpErrorMessage.append("ERROR!");
		List<clsJVDetailsBean> listJVDetailBean = new ArrayList<clsJVDetailsBean>();
		StringBuilder sbSql=new StringBuilder();
		clsJVBean objJVBean = new clsJVBean();
		String jvCode = "", supplierType="";
		clsPurchaseReturnHdModel objModel=objPurchaseReturnService.funGetObject(PRCode, clientCode);
		String date=objModel.getDtPRDate().split(" ")[0];
		objModel.setDtPRDate(date);
			
		String currentDateTime=objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd");
		String time=currentDateTime.split(" ")[1];
			
		sbSql.setLength(0);
		sbSql.append("select objPartyMasterModel from clsPartyMasterModel objPartyMasterModel "
			+ " where objPartyMasterModel.strPCode='"+objModel.getStrSuppCode()+"' and objPartyMasterModel.strClientCode='"+clientCode+"' "
			+ " and objPartyMasterModel.strPropCode='"+propCode+"' ");
		try
		{
			List listSuppDtl=objBaseService.funGetListModuleWise(sbSql, "hql", "WebStocks");
			
			if(listSuppDtl.size()>0)
			{
				clsPartyMasterModel objPartyMasterModel=(clsPartyMasterModel)listSuppDtl.get(0);
				supplierType=objPartyMasterModel.getStrPartyType();
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		String custCode = objModel.getStrSuppCode();
		double debitAmt = objModel.getDblTotal();
		String strCurr = req.getSession().getAttribute("currValue").toString();
		double currValue = Double.parseDouble(strCurr);
		clsLinkUpHdModel objLinkCust = objLinkupService.funGetARLinkUp(custCode, clientCode, propCode, "Supplier", "Purchase");
		if (objLinkCust != null) {
			if (objModel.getStrPRNo().equals("")) {
				
				objJVBean.setStrVouchNo("");
				objJVBean.setDblAmt(debitAmt * currValue);
			} else {
				
				objJVBean.setStrVouchNo(objModel.getStrPRNo());
				objJVBean.setDblAmt(debitAmt);
			}
			objJVBean.setDblAmt(debitAmt * currValue);
			objJVBean.setStrVouchNo("");
			
			sbSql.setLength(0);
			sbSql.append("select objJVModel from clsJVHdModel objJVModel where objJVModel.strSourceDocNo='"+objModel.getStrPRCode()+"' and objJVModel.strClientCode='"+clientCode+"'");	
			try
			{
				List listJVDtl=objBaseService.funGetListModuleWise(sbSql, "hql", "WebBooks");
				if(listJVDtl.size()>0)
				{
					clsJVHdModel objJVHdModel=(clsJVHdModel)listJVDtl.get(0);
					objJVBean.setStrVouchNo(objJVHdModel.getStrVouchNo());
				}
				System.out.println(listJVDtl);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			objJVBean.setStrNarration("JV Genrated by Pur-Ret:" + objModel.getStrPRCode());
			objJVBean.setStrSancCode("");
			objJVBean.setStrType("");
			objJVBean.setDteVouchDate(objGlobalFunctions.funGetDate("dd-MM-yyyy", objModel.getDtPRDate()) +" "+time);
			objJVBean.setIntVouchMonth(1);
			objJVBean.setDblAmt(debitAmt * currValue);
			objJVBean.setStrTransType("R");
			objJVBean.setStrTransMode("A");
			objJVBean.setStrModuleType("AP");
			objJVBean.setStrMasterPOS("WEBSTOCKS");
			objJVBean.setStrUserCreated(userCode);
			objJVBean.setStrUserEdited(userCode);
			objJVBean.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objJVBean.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objJVBean.setStrClientCode(clientCode);
			objJVBean.setStrPropertyCode(propCode);
			objJVBean.setStrSource("PR");
			objJVBean.setStrSourceDocNo(objModel.getStrPRCode());
			objJVBean.setStrCurrency(objModel.getStrCurrency());
			objJVBean.setDblConversion(objModel.getDblConversion());
		// jvhd entry end
				
				
		// jvdtl entry Start
			List listTempDtl = objPurchaseReturnService.funGetDtlList(PRCode, clientCode);
			
			for (int i = 0; i < listTempDtl.size(); i++) {
				Object[] ob = (Object[]) listTempDtl.get(i);
				clsPurchaseReturnDtlModel objDtl = (clsPurchaseReturnDtlModel) ob[0];
		
				clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
				clsProductMasterModel objProdModle = objProductMasterService.funGetObject(objDtl.getStrProdCode(), clientCode);
				clsLinkUpHdModel objLinkSubGroup = objLinkupService.funGetARLinkUp(objProdModle.getStrSGCode(), clientCode, propCode, "SubGroup", "Purchase");
				if (objProdModle != null && objLinkSubGroup != null) {
						
					objJVDetailBean.setStrAccountCode(objLinkSubGroup.getStrAccountCode());
					objJVDetailBean.setStrDescription(objLinkSubGroup.getStrMasterDesc());
					objJVDetailBean.setStrDC("Cr");
					objJVDetailBean.setDblDebitAmt(0.00);
					objJVDetailBean.setDblCreditAmt(objDtl.getDblTotalPrice());
					objJVDetailBean.setStrNarration("WS Product code :" + objDtl.getStrProdCode());
					objJVDetailBean.setStrOneLineAcc("R");
							
					listJVDetailBean.add(objJVDetailBean);
				}
				else
				{
					flgLinkup=false;
					sbLinkUpErrorMessage.append("Check Supplier linkup, ");
				}
			}
			
		////Tax Entry
			
			double totalTaxAmt=0;
			String sql = "select strTaxCode,strTaxDesc,strTaxableAmt,strTaxAmt from clsPurchaseReturnTaxDtlModel " + "where strPRCode='" + PRCode + "' and strClientCode='" + clientCode + "'";
			List listTaxDtl = objGlobalFunctionsService.funGetList(sql, "hql");
			List<clsPurchaseReturnTaxDtlModel> listPRTaxDtl = new ArrayList<clsPurchaseReturnTaxDtlModel>();
			for (int cnt = 0; cnt < listTaxDtl.size(); cnt++) {
				clsPurchaseReturnTaxDtlModel objTaxDtl = new clsPurchaseReturnTaxDtlModel();
				Object[] arrObj = (Object[]) listTaxDtl.get(cnt);
				objTaxDtl.setStrTaxCode(arrObj[0].toString());
				objTaxDtl.setStrTaxDesc(arrObj[1].toString());
				objTaxDtl.setStrTaxableAmt(Double.parseDouble(arrObj[2].toString()));
				objTaxDtl.setStrTaxAmt(Double.parseDouble(arrObj[3].toString()));
				listPRTaxDtl.add(objTaxDtl);
			}
			
			if (listPRTaxDtl != null) {
				for (clsPurchaseReturnTaxDtlModel objTaxDtl : listPRTaxDtl) {
					clsLinkUpHdModel objLinkTax = objLinkupService.funGetARLinkUp(objTaxDtl.getStrTaxCode(), clientCode, propCode, "Tax", "Purchase");
					if (objLinkTax != null) {
				
						clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
						objJVDetailBean.setStrAccountCode(objLinkTax.getStrAccountCode());
						objJVDetailBean.setStrDescription(objLinkTax.getStrMasterDesc());
						objJVDetailBean.setStrDC("Cr");
						objJVDetailBean.setDblDebitAmt(0.0);
						objJVDetailBean.setDblCreditAmt(objTaxDtl.getStrTaxAmt());
						objJVDetailBean.setStrNarration("WS Tax Desc :" + objTaxDtl.getStrTaxDesc());
						objJVDetailBean.setStrOneLineAcc("R");
						
						listJVDetailBean.add(objJVDetailBean);
						totalTaxAmt+=objTaxDtl.getStrTaxAmt();
					}
					else
					{
						flgLinkup=false;
						sbLinkUpErrorMessage.append("Check Tax linkup, ");
					}
				}
			}
		
			
			if(supplierType.equals("Foreign"))
			{
				clsLinkUpHdModel objLinkTaxPayable = objLinkupService.funGetARLinkUp("TAXPAYABLE", clientCode, propCode, "OtherCharge", "Purchase");
				if (objLinkTaxPayable != null) {
			
					clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					objJVDetailBean.setStrAccountCode(objLinkTaxPayable.getStrAccountCode());
					objJVDetailBean.setStrDescription(objLinkTaxPayable.getStrMasterDesc());
					objJVDetailBean.setStrDC("Dr");
					objJVDetailBean.setDblDebitAmt(totalTaxAmt);
					objJVDetailBean.setDblCreditAmt(0.00);
					objJVDetailBean.setStrNarration("Tax Payable for Foreign Imports :" + objLinkTaxPayable.getStrMasterDesc());
					objJVDetailBean.setStrOneLineAcc("R");
					
					listJVDetailBean.add(objJVDetailBean);
				}
				else
				{
					flgLinkup=false;
					sbLinkUpErrorMessage.append("Check Tax Payable linkup, ");
				}
			}
			
						
			String sqlPRDtl = " select a.strPRCode,a.dblSubTotal,b.strDebtorCode,b.strPName,date(a.dtPRDate),a.strNarration "
				+ ",date(a.dtPRDate),'',b.strPCode " 
				+ " from tblpurchasereturnhd a,tblpartymaster b " 
				+ " where a.strSuppCode =b.strPCode and a.strPRCode='" + objModel.getStrPRCode() + "' and a.strClientCode='" + objModel.getStrClientCode() + "' ";
			List listSupplier = objGlobalFunctionsService.funGetList(sqlPRDtl, "sql");
			if (null != listSupplier) {
				for (int i = 0; i < listSupplier.size(); i++) {
						
					Object[] ob = (Object[]) listSupplier.get(i);
					String accountCode="",accountName="",debtorCode="",debtorName="";
					List listCustSuppAccDetails=objGlobalFunctions.funGetCustSupplierAccountDetails("Supplier",ob[8].toString(), clientCode, propCode);
					if(listCustSuppAccDetails.size()>0)
					{
						debtorCode=listCustSuppAccDetails.get(0).toString();
						debtorName=listCustSuppAccDetails.get(1).toString();
						accountCode=listCustSuppAccDetails.get(2).toString();
						accountName=listCustSuppAccDetails.get(3).toString();
					}
					else
					{
						flgLinkup=false;
						sbLinkUpErrorMessage.append("Check Supplier linkup, ");
					}
					
					clsJVDetailsBean objJVDtlBean=new clsJVDetailsBean();
					objJVDtlBean.setStrAccountCode(accountCode);
					objJVDtlBean.setStrDescription(accountName);
					objJVDtlBean.setStrDC("Dr");
					objJVDtlBean.setDblCreditAmt(0.00);
					objJVDtlBean.setDblDebitAmt(objModel.getDblTotal());
					
					if(supplierType.equals("Foreign"))
					{
						objJVDtlBean.setDblCreditAmt(objModel.getDblSubTotal());
					}
					
					objJVDtlBean.setStrNarration("Purchase Return " + ob[3].toString());
					objJVDtlBean.setStrOneLineAcc("R");
					listJVDetailBean.add(objJVDtlBean);
			// jvdtl entry end
			
					
			// jvDebtor detail entry start
					clsJVDetailsBean objJVDetailBeanForSupp=new clsJVDetailsBean();
					objJVDetailBeanForSupp.setStrDebtorCode(debtorCode);
					objJVDetailBeanForSupp.setStrDebtorName(debtorName);
					objJVDetailBeanForSupp.setStrDC("Dr");
					objJVDetailBeanForSupp.setDblCreditAmt(0.0);
					objJVDetailBeanForSupp.setDblDebitAmt(Double.parseDouble(ob[1].toString()));
					objJVDetailBeanForSupp.setStrBillNo(ob[7].toString());
					objJVDetailBeanForSupp.setStrInvoiceNo(ob[0].toString());
					objJVDetailBeanForSupp.setStrGuest("");
					objJVDetailBeanForSupp.setStrAccountCode(accountCode);
					objJVDetailBeanForSupp.setStrDescription(accountName);
					objJVDetailBeanForSupp.setStrCreditNo("");
					objJVDetailBeanForSupp.setDteBillDate(ob[4].toString());
					objJVDetailBeanForSupp.setDteInvoiceDate(ob[4].toString());
					objJVDetailBeanForSupp.setDteDueDate(ob[4].toString());
					objJVDetailBeanForSupp.setStrNarration(ob[5].toString());
					objJVDetailBeanForSupp.setStrOneLineAcc("R");
					objJVDetailBeanForSupp.setStrRegistrationNo("");
							
					listJVDetailBean.add(objJVDetailBeanForSupp);
				}
			}
		// jvDebtor detail entry end
		
		}
		else
		{
			flgLinkup=false;
			sbLinkUpErrorMessage.append("Check Supplier linkup, ");
		}
		
		if(flgLinkup)
		{
			objJVBean.setListJVDtlBean(listJVDetailBean);
			clsJVHdModel objJVHdModel = objJVController.funGenerateJV(objJVBean, userCode, clientCode, "APGL", req,"GRN");
			jvCode=objJVHdModel.getStrVouchNo();
		}
		else
		{
			jvCode=sbLinkUpErrorMessage.toString();
		}
		
		return jvCode;
	}
	
	public String funGenrateJVforSalesReturn(String SRCode, String clientCode, String userCode, String propCode, HttpServletRequest req) {

		List<clsJVDetailsBean> listJVDetailBean = new ArrayList<clsJVDetailsBean>();
		StringBuilder sbSql=new StringBuilder();
		clsJVBean objJVBean = new clsJVBean();
		clsSalesReturnHdModel objModel=objSalesReturnService.funGetSalesReturnHd(SRCode, clientCode);
		String date=objModel.getDteSRDate().split(" ")[0];
		objModel.setDteSRDate(date);
		
		boolean flgLinkup=true;
		StringBuilder sbLinkUpErrorMessage=new StringBuilder();
		sbLinkUpErrorMessage.append("ERROR!");
		String jvCode = "",withHoldingApplicable="";
		String custCode = objModel.getStrCustCode();
		double debitAmt = Double.parseDouble(objModel.getDblTotalAmt());
		String strCurr = req.getSession().getAttribute("currValue").toString();
		double currValue = Double.parseDouble(strCurr);
		double taxableAmt=0;
		String customerCode="";
		
		clsLinkUpHdModel objLinkCust = objLinkupService.funGetARLinkUp(custCode, clientCode, propCode, "Customer", "Sale");
		if (objLinkCust != null) {
			if (objModel.getStrSRCode().equals("")) {
				objJVBean.setStrVouchNo("");

			} else {
				objJVBean.setStrVouchNo(objModel.getStrSRCode());
			}
			
			String currentDateTime=objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd");
			String time=currentDateTime.split(" ")[1];
			
			objJVBean.setStrVouchNo("");
			sbSql.setLength(0);
			sbSql.append("select objJVModel from clsJVHdModel objJVModel where objJVModel.strSourceDocNo='"+objModel.getStrSRCode()+"' and objJVModel.strClientCode='"+clientCode+"'");	
			try
			{
				List listJVDtl=objBaseService.funGetListModuleWise(sbSql, "hql", "WebBooks");
				if(listJVDtl.size()>0)
				{
					clsJVHdModel objJVHdModel=(clsJVHdModel)listJVDtl.get(0);
					objJVBean.setStrVouchNo(objJVHdModel.getStrVouchNo());
				}
				System.out.println(listJVDtl);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			
			double dblSubtotal = debitAmt -( objModel.getDblDiscAmt()) + objModel.getDblTaxAmt();
			objJVBean.setStrNarration("JV Genrated by Sales-Ret:" + objModel.getStrSRCode());
			objJVBean.setStrSancCode("");
			objJVBean.setStrType("");
			objJVBean.setDteVouchDate(objGlobalFunctions.funGetDate("dd-MM-yyyy", objModel.getDteSRDate()) +" "+time);
			objJVBean.setIntVouchMonth(1);
			objJVBean.setDblAmt(dblSubtotal);
			objJVBean.setStrTransType("R");
			objJVBean.setStrTransMode("A");
			objJVBean.setStrModuleType("AP");
			objJVBean.setStrMasterPOS("WEBCRM");
			objJVBean.setStrUserCreated(userCode);
			objJVBean.setStrUserEdited(userCode);
			objJVBean.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objJVBean.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objJVBean.setStrClientCode(clientCode);
			objJVBean.setStrPropertyCode(propCode);
			objJVBean.setStrSource("Sales-Ret");
			objJVBean.setStrSourceDocNo(objModel.getStrSRCode());
			objJVBean.setStrCurrency(objModel.getStrCurrency());
			objJVBean.setDblConversion(objModel.getDblConversion());
		// jvhd entry end
		
			
		// jvdtl entry Start
			List<clsSalesReturnDtlModel> listSalesDtl = new ArrayList<clsSalesReturnDtlModel>();
			List<Object> listSRDtlModel = objSalesReturnController.funGetSalesReturnDtl(SRCode, clientCode);
			for (int i = 0; i < listSRDtlModel.size(); i++) 
			{
				Object[] obj = (Object[]) listSRDtlModel.get(i);
				
				clsProductMasterModel objProdModle = objProductMasterService.funGetObject(obj[1].toString(), clientCode);
				clsLinkUpHdModel objLinkSubGroup = objLinkupService.funGetARLinkUp(objProdModle.getStrSGCode(), clientCode, propCode, "SubGroup", "Sale");
				if (objProdModle != null && objLinkSubGroup != null) {
					
					clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					
					objJVDetailBean.setStrAccountCode(objLinkSubGroup.getStrAccountCode());
					objJVDetailBean.setStrDescription(objLinkSubGroup.getStrMasterDesc());
					objJVDetailBean.setStrDC("Dr");
					objJVDetailBean.setDblDebitAmt(Double.valueOf(obj[2].toString()) * Double.valueOf(obj[3].toString()));
					objJVDetailBean.setDblCreditAmt(0.00);
					objJVDetailBean.setStrNarration("WS Product code :" + obj[1].toString());
					objJVDetailBean.setStrOneLineAcc("R");
					
					listJVDetailBean.add(objJVDetailBean);
				}
				else
				{
					flgLinkup=false;
					sbLinkUpErrorMessage.append("Check SubGroup linkup, ");
				}
			}
			
			List<clsInvoiceTaxDtlModel> listTaxDtl = new ArrayList<clsInvoiceTaxDtlModel>();
			String sqlInv = "select strTaxCode,strTaxDesc,strTaxableAmt,strTaxAmt from tblsalesreturntaxdtl " + "where strSRCode='" + SRCode + "' and strClientCode='" + clientCode + "'";
			List list = objGlobalFunctionsService.funGetList(sqlInv, "sql");
			
			for (int cnt = 0; cnt < list.size(); cnt++) {
				clsInvoiceTaxDtlModel objTaxDtl = new clsInvoiceTaxDtlModel();
				Object[] arrObj = (Object[]) list.get(cnt);
				objTaxDtl.setStrTaxCode(arrObj[0].toString());
				objTaxDtl.setStrTaxDesc(arrObj[1].toString());
				objTaxDtl.setDblTaxableAmt(Double.parseDouble(arrObj[2].toString()));
				objTaxDtl.setDblTaxAmt(Double.parseDouble(arrObj[3].toString()));

				listTaxDtl.add(objTaxDtl);
				if(Double.parseDouble(arrObj[3].toString())>0)
				{
					taxableAmt+=Double.parseDouble(arrObj[2].toString());
				}
			}
			
			if (listTaxDtl != null) {
				for (clsInvoiceTaxDtlModel objTaxDtl : listTaxDtl) {
					clsLinkUpHdModel objLinkTax = objLinkupService.funGetARLinkUp(objTaxDtl.getStrTaxCode(), clientCode, propCode, "Tax", "Sale");
					if (objLinkTax != null) {
						
						clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
						
						objJVDetailBean.setStrAccountCode(objLinkTax.getStrAccountCode());
						objJVDetailBean.setStrDescription(objLinkTax.getStrMasterDesc());
						objJVDetailBean.setStrDC("Dr");
						objJVDetailBean.setDblDebitAmt(objTaxDtl.getDblTaxAmt());
						objJVDetailBean.setDblCreditAmt(0.00);
						objJVDetailBean.setStrNarration("WS Tax Desc :" + objTaxDtl.getStrTaxDesc());
						objJVDetailBean.setStrOneLineAcc("R");
						
						listJVDetailBean.add(objJVDetailBean);
					}
					else
					{
						flgLinkup=false;
						sbLinkUpErrorMessage.append("Check Tax linkup, ");
					}
				}
			}

			if (objModel.getDblDiscAmt()  / currValue != 0) {
				clsLinkUpHdModel objLinkDisc = objLinkupService.funGetARLinkUp("Discount", clientCode, propCode, "Discount", "Sale");
				if (objLinkDisc != null) {
										
					clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					
					objJVDetailBean.setStrAccountCode(objLinkDisc.getStrAccountCode());
					objJVDetailBean.setStrDescription(objLinkDisc.getStrMasterDesc());
					objJVDetailBean.setStrDC("Cr");
					objJVDetailBean.setDblDebitAmt(0.00);
					objJVDetailBean.setDblCreditAmt(objModel.getDblDiscAmt());
					objJVDetailBean.setStrNarration("WS Sale Discount Desc :" + objModel.getDblDiscAmt());
					objJVDetailBean.setStrOneLineAcc("R");
					
					listJVDetailBean.add(objJVDetailBean);
				}
				else
				{
					flgLinkup=false;
					sbLinkUpErrorMessage.append("Check Discount linkup, ");
				}
			}
		// jvdtl entry end

			
		// jvDebtor detail entry start
			
			String sqlSRDtl  = "  select a.strSRCode,a.dblTotalAmt,b.strDebtorCode,b.strPName,a.strCustCode,date(a.dteSRDate)," 
				+ " a.strDCCode,b.strApplForWT from tblsalesreturnhd a,tblpartymaster b "
				+ " where a.strSRCode='" + objModel.getStrSRCode() + "' " 
			    + " and a.strClientCode='" + objModel.getStrClientCode() + "' " 
				+ " and a.strCustCode='" + objModel.getStrCustCode() + "'  and a.strCustCode=b.strPCode ";
			
			List listSupplier = objGlobalFunctionsService. funGetList(sqlSRDtl, "sql");
			if (null != listSupplier) {
				for (int i = 0; i < listSupplier.size(); i++) {
					
					Object[] ob = (Object[]) listSupplier.get(i);
					customerCode=ob[4].toString();
					withHoldingApplicable=ob[7].toString();
										
					String accountCode="",accountName="",debtorCode="",debtorName="";
					List listCustSuppAccDetails=objGlobalFunctions.funGetCustSupplierAccountDetails("Customer",ob[4].toString() , clientCode, propCode);
					if(listCustSuppAccDetails.size()>0)
					{
						debtorCode=listCustSuppAccDetails.get(0).toString();
						debtorName=listCustSuppAccDetails.get(1).toString();
						accountCode=listCustSuppAccDetails.get(2).toString();
						accountName=listCustSuppAccDetails.get(3).toString();
					}
					else
					{
						flgLinkup=false;
						sbLinkUpErrorMessage.append("Check Customer linkup, ");
					}
					
					clsJVDetailsBean objJVDtlBean=new clsJVDetailsBean();
					objJVDtlBean.setStrAccountCode(accountCode);
					objJVDtlBean.setStrDescription(accountName);
					objJVDtlBean.setStrDC("Cr");
					objJVDtlBean.setDblDebitAmt(0.00);
					objJVDtlBean.setDblCreditAmt(debitAmt);
					objJVDtlBean.setStrNarration("Sales Return Customer ");
					objJVDtlBean.setStrOneLineAcc("R");
					listJVDetailBean.add(objJVDtlBean);
					
					
					clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					objJVDetailBean.setStrAccountCode(accountCode);
					objJVDetailBean.setStrDescription(accountName);
					objJVDetailBean.setStrDC("Cr");
					objJVDetailBean.setDblDebitAmt(0.00);
					objJVDetailBean.setDblCreditAmt(Double.parseDouble(objModel.getDblTotalAmt()));
					objJVDetailBean.setStrNarration("Sales Return Customer");
					objJVDetailBean.setStrOneLineAcc("R");
					
					objJVDetailBean.setStrDebtorCode(debtorCode);
					objJVDetailBean.setStrDebtorName(debtorName);
					objJVDetailBean.setDblDebitAmt(0.00);
					objJVDetailBean.setStrBillNo(ob[0].toString());
					objJVDetailBean.setStrInvoiceNo(ob[0].toString());
					objJVDetailBean.setStrGuest("");
					objJVDetailBean.setStrCreditNo("");
					objJVDetailBean.setDteBillDate(ob[5].toString());
					objJVDetailBean.setDteInvoiceDate(ob[5].toString());
					objJVDetailBean.setDteDueDate(ob[5].toString());
					objJVDetailBean.setStrRegistrationNo("");
					
					listJVDetailBean.add(objJVDetailBean);
				}
			}	
			
		// jvDebtor detail entry end
			
		}
		else
		{
			flgLinkup=false;
			sbLinkUpErrorMessage.append("Check Round Off linkup, ");
		}
		
		if(flgLinkup)
		{
			objJVBean.setListJVDtlBean(listJVDetailBean);
			clsJVHdModel objJVHDModel = objJVController.funGenerateJV(objJVBean, userCode, clientCode, "APGL", req,"Sales-Ret");
			jvCode=objJVHDModel.getStrVouchNo();
			if(withHoldingApplicable.equals("Y"))
				funGenerateReversalTaxJV(objModel.getStrSRCode(), objModel.getDteSRDate(), taxableAmt, customerCode, clientCode, userCode, propCode, "SalesReturn", req,objModel.getStrCurrency(),objModel.getDblConversion());
		}
		else
		{
			jvCode=sbLinkUpErrorMessage.toString();
		}
		
		return jvCode;
	}
	
	public clsPaymentHdModel funGeneratePaymentForGRNJV(clsGRNHdModel objModel,clsJVHdModel objJVHdModel,String settlementCode,String clientCode, String userCode, String propCode,HttpServletRequest req){
		StringBuilder sbSql=new StringBuilder();
		clsPaymentBean objPaymentBean = new clsPaymentBean();
		clsPaymentHdModel objPaymentHdModel = new clsPaymentHdModel();
		String paymentCode = "";
		try{
			double debitAmt = objModel.getDblTotal();
			String strCurr = req.getSession().getAttribute("currValue").toString();
			double currValue = Double.parseDouble(strCurr);
			String strCashSettleBankAcc="",strCashSettleBankAccName="",strCreditorCode="",strCreditorName="",strCreditorAccCode="",strCreditorAccName="";
			clsLinkUpHdModel objLinkCust = objLinkupService.funGetARLinkUp(settlementCode, clientCode, propCode, "Settlement", "Sale");
			if (objLinkCust != null) {
				strCashSettleBankAcc=objLinkCust.getStrAccountCode();
				strCashSettleBankAccName=objLinkCust.getStrMasterDesc();
				if (objModel.getStrJVNo().equals("")) {
					
					objPaymentBean.setStrVouchNo("");
					objPaymentBean.setDblAmt(debitAmt * currValue);
				} else {
					
					objPaymentBean.setStrVouchNo(objModel.getStrJVNo());
					objPaymentBean.setDblAmt(debitAmt);
				}
				
			objLinkCust = objLinkupService.funGetARLinkUp(objModel.getStrSuppCode(), clientCode, propCode, "Supplier", "Purchase");
				if (objLinkCust != null) {
					strCreditorCode=objLinkCust.getStrAccountCode();
					strCreditorName=objLinkCust.getStrMasterDesc();
					//strCreditorAccCode="";
					//strCreditorAccName="";
				}
			
				objPaymentBean.setStrVouchNo("");
			}
			
			sbSql.setLength(0);
			sbSql.append("select a.strVouchNo from tblpaymentgrndtl a where a.strGRNCode='"+objModel.getStrGRNCode()+"' and a.strClientCode='"+clientCode+"'");	
			try
			{
				String currentDateTime=objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd");
				String time=currentDateTime.split(" ")[1];
				
				List listJVDtl=objBaseService.funGetListModuleWise(sbSql, "sql", "WebBooks");
				if(listJVDtl !=null && listJVDtl.size()>0)
				{
					objPaymentBean.setStrVouchNo(listJVDtl.get(0).toString());
				}
				objPaymentBean.setStrBankCode(strCashSettleBankAcc);
				objPaymentBean.setStrNarration("Payment Generated by GRN:" + objModel.getStrGRNCode());
				objPaymentBean.setStrSancCode(strCreditorCode);
				objPaymentBean.setStrType("");
				objPaymentBean.setDteClearence(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				objPaymentBean.setDteChequeDate(objGlobalFunctions.funGetDate("dd-MM-yyyy", objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd")));
				objPaymentBean.setStrChequeNo("");
				objPaymentBean.setDteVouchDate(objGlobalFunctions.funGetDate("dd-MM-yyyy", objModel.getDtGRNDate()) +" "+time);
				objPaymentBean.setIntVouchMonth(1);
				objPaymentBean.setDblAmt(debitAmt * currValue);
				
				objPaymentBean.setStrTransMode("R");
				objPaymentBean.setStrModuleType("AR");
				objPaymentBean.setStrUserCreated(userCode);
				objPaymentBean.setStrUserEdited(userCode);
				objPaymentBean.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				objPaymentBean.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				objPaymentBean.setStrClientCode(clientCode);
				objPaymentBean.setStrPropertyCode(propCode);
				objPaymentBean.setStrCurrency(objModel.getStrCurrency());
				objPaymentBean.setDblConversion(objModel.getDblConversion());
				
				
			// Payment hd entry end
				
				//Payment debtor dtl
				List<clsPaymentDetailsBean> listPaymentDetailsBean = new ArrayList<clsPaymentDetailsBean>();
				if(null!=objJVHdModel.getListJVDtlModel()){
					
				for(clsJVDtlModel objJVDtlModel:objJVHdModel.getListJVDtlModel()){
					//clsJVDtlModel objJVDtlModel=objJVHdModel.getListJVDtlModel().get(0);
					//	objJVDtlModel.gets
					clsPaymentDetailsBean objPaymentBeanDetails =new clsPaymentDetailsBean();
					if(objJVDtlModel.getStrCrDr().equals("Dr")){
						
						objPaymentBeanDetails.setStrDebtorCode("") ;
						objPaymentBeanDetails.setStrDebtorName("");
						
						objPaymentBeanDetails.setStrAccountCode(strCashSettleBankAcc);
						objPaymentBeanDetails.setStrDescription(strCashSettleBankAccName);
						objPaymentBeanDetails.setStrDC("Cr");
						objPaymentBeanDetails.setDblDebitAmt(objJVDtlModel.getDblCrAmt());
						objPaymentBeanDetails.setDblCreditAmt(objJVDtlModel.getDblDrAmt());
					}else{
						
						objPaymentBeanDetails.setStrDebtorCode(strCreditorCode) ;
						objPaymentBeanDetails.setStrDebtorName(strCreditorName);
						
						objPaymentBeanDetails.setStrAccountCode(objJVDtlModel.getStrAccountCode());
						objPaymentBeanDetails.setStrDescription(objJVDtlModel.getStrAccountName());
						objPaymentBeanDetails.setStrDC("Dr");
						objPaymentBeanDetails.setDblDebitAmt(objJVDtlModel.getDblCrAmt());
						objPaymentBeanDetails.setDblCreditAmt(objJVDtlModel.getDblDrAmt());
						
					}
					
					listPaymentDetailsBean.add(objPaymentBeanDetails);
					
					
					}
				}
				
				
				objPaymentBean.setListPaymentDetailsBean(listPaymentDetailsBean);
				
				 
				
				
				
				
				
				
				
				
				
				List<clsPaymentGRNDtlModel> listPaymentGRNDtl = new ArrayList<clsPaymentGRNDtlModel>();
				clsPaymentGRNDtlModel objPaymentGRNDtlModel=new clsPaymentGRNDtlModel();
					objPaymentGRNDtlModel.setDblGRNAmt(objModel.getDblTotal() * currValue);
					objPaymentGRNDtlModel.setDblPayedAmt(objModel.getDblTotal() * currValue);
					objPaymentGRNDtlModel.setStrPropertyCode(propCode);
					objPaymentGRNDtlModel.setDteBillDate(objModel.getDtBillDate());
					objPaymentGRNDtlModel.setStrGRNCode(objModel.getStrGRNCode());
					objPaymentGRNDtlModel.setStrGRNBIllNo(objModel.getStrBillNo());
					objPaymentGRNDtlModel.setDteGRNDate(objModel.getDtGRNDate());
					objPaymentGRNDtlModel.setDteGRNDueDate(objModel.getDtDueDate());
					objPaymentGRNDtlModel.setStrSelected("Tick");
				listPaymentGRNDtl.add(objPaymentGRNDtlModel);
				objPaymentBean.setListPaymentGRNDtl(listPaymentGRNDtl);
				objPaymentBean.setStrCurrency(objModel.getStrCurrency());
					
					
					
				 objPaymentHdModel = objPaymentController.funGeneratePayment(objPaymentBean, userCode, clientCode,  req, propCode, currValue);
				
				
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return objPaymentHdModel;
	}

	public String funGenerateJVforSTKAdjustment(String stkAdjCode, String clientCode, String userCode, String propCode, HttpServletRequest req) {
				
		StringBuilder sbSql=new StringBuilder();
		clsJVBean objJVBean = new clsJVBean();
		String jvCode = "";
		List list=objStkAdjService.funGetObject(stkAdjCode, clientCode);
		boolean flgLinkup=true;
		StringBuilder sbLinkUpErrorMessage=new StringBuilder();
		sbLinkUpErrorMessage.append("ERROR!");
		
		//List<clsJVDetailsBean> listLossJVDetailBean = new ArrayList<clsJVDetailsBean>();
		
		if(null!=list && list.size()>0)
		{
			Object[] arrObjHd=(Object[])list.get(0);
			clsStkAdjustmentHdModel objModel=(clsStkAdjustmentHdModel)arrObjHd[0];
			String date=objModel.getDtSADate().split(" ")[0];
			objModel.setDtSADate(date);
			
			
			double debitAmt = objModel.getDblTotalAmt();
			String strCurr = req.getSession().getAttribute("currValue").toString();
			String currCode = req.getSession().getAttribute("currencyCode").toString();
			double currValue = Double.parseDouble(strCurr);
			String strExcessAccCode="",strExcessAccName="",strShortageAccCode="",strShortageAccName="";
			clsLinkUpHdModel objLinkCust = objLinkupService.funGetARLinkUp(objModel.getStrLocCode(), clientCode, propCode, "Location", "Purchase");
			if (objLinkCust != null) {
				
				 strShortageAccCode=objLinkCust.getStrAccountCode();
				 strShortageAccName=objLinkCust.getStrMasterDesc();
				 strExcessAccCode=objLinkCust.getStrWebBookAccCode();
				 strExcessAccName=objLinkCust.getStrWebBookAccName();
				
			}
			else
			{
				flgLinkup=false;
				sbLinkUpErrorMessage.append("Check Excess and Shortage Account linkup");
			}
			
			String currentDateTime=objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd");
			String time=currentDateTime.split(" ")[1];
			sbSql.setLength(0);
			sbSql.append("select a.strType from tblstockadjustmentdtl a where a.strSACode='"+objModel.getStrSACode()+"' group by a.strType;");
			List listSTKInOutDtl;
			try {
				listSTKInOutDtl = objBaseService.funGetListModuleWise(sbSql, "sql", "WebStocks");
				if(listSTKInOutDtl !=null && listSTKInOutDtl.size()>0)
				{
					for(int k=0;k<listSTKInOutDtl.size();k++){
						List<clsJVDetailsBean> listJVDetailBean = new ArrayList<clsJVDetailsBean>();
						double totalStkAdjustmentAmt=0;
						// For Profit and Loss JV 
						
						String jvNo="";
						sbSql.setLength(0);
						if(listSTKInOutDtl.get(k).toString().equalsIgnoreCase("IN")){
							sbSql.append("select a.strVouchNo from tbljvhd a ,tbljvdtl b where a.strVouchNo=b.strVouchNo and b.strNarration like '%Profit JV%' and a.strNarration like '%"+objModel.getStrSACode()+"%' group by a.strVouchNo ");
							List listSTKJVDtl = objBaseService.funGetListModuleWise(sbSql, "sql", "WebBooks");
							if(listSTKJVDtl !=null && listSTKJVDtl.size()>0)
							{
								jvNo=(String)listSTKJVDtl.get(0);
							}

						}else{
							sbSql.append("select a.strVouchNo from tbljvhd a ,tbljvdtl b where a.strVouchNo=b.strVouchNo and b.strNarration like '%Loss JV%' and a.strNarration like '%"+objModel.getStrSACode()+"%' group by a.strVouchNo ");
							List listSTKJVDtl = objBaseService.funGetListModuleWise(sbSql, "sql", "WebBooks");
							if(listSTKJVDtl !=null && listSTKJVDtl.size()>0)
							{
								jvNo=(String)listSTKJVDtl.get(0);
							}

						}
												
						objJVBean = new clsJVBean();
						objJVBean.setStrVouchNo(jvNo);
						
						objJVBean.setStrNarration("JV Generated by Stk Adsjustment:" + objModel.getStrSACode());
						objJVBean.setStrSancCode("");
						objJVBean.setStrType("");
						objJVBean.setDteVouchDate(objGlobalFunctions.funGetDate("dd-MM-yyyy", objModel.getDtSADate()) +" "+time);
						objJVBean.setIntVouchMonth(1);
						objJVBean.setDblAmt(debitAmt * currValue);
						objJVBean.setStrTransType("R");
						objJVBean.setStrTransMode("A");
						objJVBean.setStrModuleType("AP");
						objJVBean.setStrMasterPOS("WEBSTOCKS");
						objJVBean.setStrUserCreated(userCode);
						objJVBean.setStrUserEdited(userCode);
						objJVBean.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
						objJVBean.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
						objJVBean.setStrClientCode(clientCode);
						objJVBean.setStrPropertyCode(propCode);
						objJVBean.setStrSource("Stk Adjustment");
						objJVBean.setStrSourceDocNo(objModel.getStrSACode());
						objJVBean.setStrCurrency(currCode);
						objJVBean.setDblConversion(currValue);
					// jvhd entry end
			
						List listTempDtl = objStkAdjService.funGetDtlList(stkAdjCode, clientCode);
						clsJVDetailsBean objJVDetailBean=new clsJVDetailsBean();
					for (int i = 0; i < listTempDtl.size(); i++) {
						Object[] ob = (Object[]) listTempDtl.get(i);
						clsStkAdjustmentDtlModel objDtl = (clsStkAdjustmentDtlModel) ob[0];
		
						objJVDetailBean=new clsJVDetailsBean();
						clsProductMasterModel objProdModle = objProductMasterService.funGetObject(objDtl.getStrProdCode(), clientCode);
						clsLinkUpHdModel objLinkSubGroup = objLinkupService.funGetARLinkUp(objProdModle.getStrSGCode(), clientCode, propCode, "SubGroup", "Purchase");
						if (objProdModle != null && objLinkSubGroup != null) {
							if(objDtl.getStrType().equalsIgnoreCase(listSTKInOutDtl.get(k).toString())){ //Check In / Out
								objJVDetailBean.setStrAccountCode(objLinkSubGroup.getStrAccountCode());
								objJVDetailBean.setStrDescription(objLinkSubGroup.getStrMasterDesc());
								if(objDtl.getStrType().equalsIgnoreCase("IN")){
									objJVDetailBean.setStrDC("Dr");
									objJVDetailBean.setDblDebitAmt(objDtl.getDblPrice());
									objJVDetailBean.setDblCreditAmt(0.00);
									objJVDetailBean.setStrNarration("Profit JV");
									if(objDtl.getDblPrice()==0){
										totalStkAdjustmentAmt+=	objDtl.getDblQty()*	objDtl.getDblRate();
									}else{
										totalStkAdjustmentAmt+=	objDtl.getDblPrice();	
									}
									
								}else{
									objJVDetailBean.setStrDC("cr");
									objJVDetailBean.setDblDebitAmt(0.00);
									objJVDetailBean.setDblCreditAmt(objDtl.getDblPrice());
									objJVDetailBean.setStrNarration("Loss JV");
									if(objDtl.getDblPrice()==0){
										totalStkAdjustmentAmt+=	objDtl.getDblQty()*	objDtl.getDblRate();
									}else{
										totalStkAdjustmentAmt+=	objDtl.getDblPrice();	
									}
								}
								
								objJVDetailBean.setStrOneLineAcc("R");
								
								listJVDetailBean.add(objJVDetailBean);
							}
							
						}
								
						}
					
					clsJVDetailsBean objJVDtlBean=new clsJVDetailsBean();
					if(listSTKInOutDtl.get(k).toString().equalsIgnoreCase("IN")){
						objJVDtlBean.setStrAccountCode(strExcessAccCode);
						objJVDtlBean.setStrDescription(strExcessAccName);
						objJVDtlBean.setStrDC("Cr");
						objJVDtlBean.setDblCreditAmt(totalStkAdjustmentAmt);
						objJVDtlBean.setDblDebitAmt(0.00);
						objJVDtlBean.setStrNarration("STK Adjustment Profit");
							
					}else{
						objJVDtlBean.setStrAccountCode(strShortageAccCode);
						objJVDtlBean.setStrDescription(strShortageAccName);
						objJVDtlBean.setStrDC("Dr");
						objJVDtlBean.setDblCreditAmt(0.00);
						objJVDtlBean.setDblDebitAmt(totalStkAdjustmentAmt);
						objJVDtlBean.setStrNarration("STK Adjustment Loss");
							
						
					}
					objJVDtlBean.setStrOneLineAcc("R");
					listJVDetailBean.add(objJVDtlBean);
					
					
					clsJVHdModel objJVHdModel =null;
					if(flgLinkup)
					{
						objJVBean.setListJVDtlBean(listJVDetailBean);
						objJVHdModel = objJVController.funGenerateJV(objJVBean, userCode, clientCode, "APGL", req,"STKAdjuctment");
						jvCode=objJVHdModel.getStrVouchNo()+"! ";
						
						sbSql.setLength(0);
						sbSql.append("update tblstockadjustmentdtl  set strJVNo='"+objJVHdModel.getStrVouchNo()+"' where strSACode='"+stkAdjCode+"' and strType='"+listSTKInOutDtl.get(k).toString()+"' ");
						objBaseService.funExcecteUpdateModuleWise(sbSql, "sql", "WebStocks");
					}
					else
					{
						jvCode=sbLinkUpErrorMessage.toString();
					}
					
					}
						
					
				}
					
					
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		return jvCode;
	}
	
	
	
}
