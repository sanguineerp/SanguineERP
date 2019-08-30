package com.sanguine.webpms.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsLinkUpHdModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsPropertySetupBean;
import com.sanguine.webpms.model.clsLinkupModel;
import com.sanguine.webpms.model.clsPropertySetupHdModel;
import com.sanguine.webpms.service.clsPropertySetupService;

@Controller
public class clsPropertySetupController {
	@Autowired
	private clsPropertySetupService objPropertySetupService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;
	String webStockDB="";

	// Open PropertySetup
	@RequestMapping(value = "/frmPropertySetup", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String propertyCode = request.getSession().getAttribute("propertyCode").toString();
		webStockDB=request.getSession().getAttribute("WebStockDB").toString();
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		List listOfProperty = objGlobalFunctionsService.funGetList("select strPropertyName from "+webStockDB+".tblpropertymaster");
		model.put("listOfProperty", listOfProperty);

		clsPropertySetupHdModel objModel = objPropertySetupService.funGetPropertySetup(propertyCode, clientCode);
		// objGlobalFunctionsService.funGetList("select tmeCheckInTime,tmeCheckOutTime from tblpropertysetup   "
		// +
		// " where strPropertyCode='"+propertyCode+"' and strClientCode='"+clientCode+"' ");
		if (objModel == null) {
			model.put("checkInTime", "00:01:00");
			model.put("checkOutTime", "23:59:00");
			model.put("GSTNo", "");
			model.put("BankACNumber", "");
			model.put("BankIFSC", "");
			model.put("HSCCode", "");
			model.put("panNo", "");
			model.put("BranchName", "");
			model.put("bankAcName", "");
			model.put("", "");
			model.put("SmsApi", "");
			model.put("ReservationEmail", "");
			model.put("smsContentForReservatiojn", "");
			model.put("emailContentForCheckIn", "");
			model.put("emailContentForReservation", "");
		} else {
			model.put("checkInTime", objModel.getTmeCheckInTime());
			model.put("checkOutTime", objModel.getTmeCheckOutTime());
			model.put("GSTNo",objModel.getStrGSTNo());
			model.put("BankACNumber", objModel.getStrBankAcNumber());
			model.put("BankIFSC", objModel.getStrBankIFSC());
			model.put("HSCCode", objModel.getStrHscCode());
			model.put("panNo", objModel.getStrPanNo());
			model.put("BranchName", objModel.getStBranchName());
			model.put("bankAcName", objModel.getStrBankAcName());
			model.put("RoomLimit", objModel.getStrRoomLimit());
			model.put("SmsApi", objModel.getStrSMSAPI());
			model.put("ReservationEmail", objModel.getStrReservationEmailContent());
			model.put("smsContentForReservatiojn", objModel.getStrReservationSMSContent());
			model.put("emailContentForCheckIn", objModel.getStrCheckInEmailContent());
			model.put("emailContentForReservation", objModel.getStrReservationEmailContent());
		}
		
		String sql = "select count(1) from tblroom a where a.strClientCode='" + clientCode + "' ";
		
		List listOfRoom = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		if(listOfRoom.size()>0){
		
			BigInteger cnt = (BigInteger) listOfRoom.get(0);
		
			model.put("listOfRoom", cnt);
		}
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPropertySetup_1", "command", new clsPropertySetupBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPropertySetup", "command", new clsPropertySetupBean());
		} else {
			return null;
		}
	}

	// Load Master Data On Form
	@RequestMapping(value = "/frmPropertySetup1", method = RequestMethod.POST)
	public @ResponseBody clsPropertySetupHdModel funLoadMasterData(HttpServletRequest request) {
		return null;
	}

	// Save or Update PropertySetup
	@RequestMapping(value = "/savePropertySetup", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsPropertySetupBean objBean, BindingResult result, HttpServletRequest req) {
		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		if (!result.hasErrors()) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			objPropertySetupService.funAddUpdatePropertySetup(funPrepareModel(objBean, userCode, clientCode));
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Property Code. : ".concat(objBean.getStrPropertyCode()));
			return new ModelAndView("redirect:/frmPropertySetup.html?saddr=" + urlHits);
		} else {
			return new ModelAndView("frmPropertySetup?saddr=" + urlHits);
		}
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadPMSLinkUpData", method = RequestMethod.POST)
	public @ResponseBody List funLoadLinkupData(@RequestParam("strDoc") String strDoc, HttpServletRequest request, HttpServletResponse response) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();

		String urlHits = "1";
		String propertyCode = request.getSession().getAttribute("propertyCode").toString();
		// urlHits=request.getParameter("saddr").toString();
		String sql = "";
		if (strDoc.equals("Tax")) 
		{
			sql = "SELECT a.strTaxCode,a.strTaxDesc,ifnull(b.strAccountCode,''),ifnull(b.strMasterDesc,'') FROM tbltaxmaster a,tbllinkup b where a.strTaxCode=b.strMasterCode and  a.strClientCode = '"+clientCode+"'";
			
		} 
		else if (strDoc.equals("Settlement")) 
		{
			sql = " select a.strSettlementCode,a.strSettlementDesc from tblsettlementmaster a where a.strClientCode = '"+clientCode+"' ";
			
			
		} 
		else if (strDoc.equals("Department")) 
		{
			sql = " select a.strDeptCode,a.strDeptDesc from tbldepartmentmaster a where a.strClientCode = '"+clientCode+"' ";
			
		} 
		else if (strDoc.equals("Room Type")) 
		{
			sql = " select a.strRoomCode,a.strRoomDesc from tblroom a where a.strClientCode = '"+clientCode+"' ";
			
		} 
		else if (strDoc.equals("Package")) 
		{
			sql = " SELECT a.strPackageCode,a.strPackageName,b.strMasterCode,b.strMasterDesc FROM tblpackagemasterhd a,tbllinkup b WHERE a.strPackageCode=b.strMasterCode AND a.strClientCode = '"+clientCode+"' ";
			
		} 
		

		ArrayList list = (ArrayList) objGlobalFunctionsService.funGetDataList(sql, "sql");
		List listARLinkUp = new ArrayList<clsLinkUpHdModel>();

		if (strDoc.equals("Department")) {
			for (int cnt = 0; cnt < list.size(); cnt++) {
				clsLinkupModel objModel = new clsLinkupModel();
				Object[] arrObj = (Object[]) list.get(cnt);
				objModel.setStrMasterCode(arrObj[0].toString());
				objModel.setStrMasterName(arrObj[1].toString());
				/*objModel.setStrAccountCode(arrObj[2].toString());
				objModel.setStrMasterDesc(arrObj[3].toString());
				*/listARLinkUp.add(objModel);
			}
		} else if (strDoc.equals("Settlement")) {
			for (int cnt = 0; cnt < list.size(); cnt++) {
				clsLinkupModel objModel = new clsLinkupModel();
				Object[] arrObj = (Object[]) list.get(cnt);
				objModel.setStrMasterCode(arrObj[0].toString());
				objModel.setStrMasterName(arrObj[1].toString());
				/*objModel.setStrAccountCode(arrObj[2].toString());
				objModel.setStrMasterDesc(arrObj[3].toString());
			*/	listARLinkUp.add(objModel);
			}
		} else if (strDoc.equals("Tax")) {
			for (int cnt = 0; cnt < list.size(); cnt++) {
				clsLinkupModel objModel = new clsLinkupModel();
				Object[] arrObj = (Object[]) list.get(cnt);
				objModel.setStrMasterCode(arrObj[0].toString());
				objModel.setStrMasterName(arrObj[1].toString());
				objModel.setStrAccountCode(arrObj[2].toString());
				objModel.setStrMasterDesc(arrObj[3].toString());
				listARLinkUp.add(objModel);
			}
		} else if (strDoc.equals("Room Type")) {
			for (int cnt = 0; cnt < list.size(); cnt++) {
				clsLinkupModel objModel = new clsLinkupModel();
				Object[] arrObj = (Object[]) list.get(cnt);
				objModel.setStrMasterCode(arrObj[0].toString());
				objModel.setStrMasterName(arrObj[1].toString());
				listARLinkUp.add(objModel);
			}
		} else if (strDoc.equals("Package")) {
			for (int cnt = 0; cnt < list.size(); cnt++) {
				clsLinkupModel objModel = new clsLinkupModel();
				Object[] arrObj = (Object[]) list.get(cnt);
				objModel.setStrMasterCode(arrObj[0].toString());
				objModel.setStrMasterName(arrObj[1].toString());
				objModel.setStrMasterDesc(arrObj[2].toString());
				objModel.setStrAccountCode(arrObj[3].toString());
				listARLinkUp.add(objModel);
			}
		} else if (strDoc.equals("Discount")) {
			if (list.size() > 0) {
				for (int cnt = 0; cnt < list.size(); cnt++) {
					clsLinkUpHdModel objModel = new clsLinkUpHdModel();
					Object[] arrObj = (Object[]) list.get(cnt);
					objModel.setStrMasterCode("PURDISC");
					objModel.setStrMasterName("Purchase Discount");
					objModel.setStrMasterDesc(arrObj[2].toString());
					objModel.setStrAccountCode(arrObj[3].toString());
					objModel.setStrExSuppCode(arrObj[4].toString());
					objModel.setStrExSuppName(arrObj[5].toString());
					listARLinkUp.add(objModel);
				}
			} else {
				clsLinkUpHdModel objModel = new clsLinkUpHdModel();
				objModel.setStrMasterCode("PURDISC");
				objModel.setStrMasterName("Purchase Discount");
				objModel.setStrMasterDesc("");
				objModel.setStrAccountCode("");
				objModel.setStrExSuppCode("");
				objModel.setStrExSuppName("");
				listARLinkUp.add(objModel);
			}
		} else if (strDoc.equals("RoundOff")) {
			if (list.size() > 0) {
				for (int cnt = 0; cnt < list.size(); cnt++) {
					clsLinkUpHdModel objModel = new clsLinkUpHdModel();
					Object[] arrObj = (Object[]) list.get(cnt);
					objModel.setStrMasterCode("PURROUNDOFF");
					objModel.setStrMasterName("Purchase RoundOff");
					objModel.setStrMasterDesc(arrObj[2].toString());
					objModel.setStrAccountCode(arrObj[3].toString());
					objModel.setStrExSuppCode(arrObj[4].toString());
					objModel.setStrExSuppName(arrObj[5].toString());
					listARLinkUp.add(objModel);
				}
			} else {
				clsLinkUpHdModel objModel = new clsLinkUpHdModel();
				objModel.setStrMasterCode("PURROUNDOFF");
				objModel.setStrMasterName("Purchase RoundOff");
				objModel.setStrMasterDesc("");
				objModel.setStrAccountCode("");
				objModel.setStrExSuppCode("");
				objModel.setStrExSuppName("");
				listARLinkUp.add(objModel);
			}
		} else if (strDoc.equals("ExtraCharge")) {
			if (list.size() > 0) {
				for (int cnt = 0; cnt < list.size(); cnt++) {
					clsLinkUpHdModel objModel = new clsLinkUpHdModel();
					Object[] arrObj = (Object[]) list.get(cnt);
					objModel.setStrMasterCode("PUREXTARACHARGES");
					objModel.setStrMasterName("Purchase Extra Charges");
					objModel.setStrMasterDesc(arrObj[2].toString());
					objModel.setStrAccountCode(arrObj[3].toString());
					objModel.setStrExSuppCode(arrObj[4].toString());
					objModel.setStrExSuppName(arrObj[5].toString());
					listARLinkUp.add(objModel);
				}
			} else {
				clsLinkUpHdModel objModel = new clsLinkUpHdModel();
				objModel.setStrMasterCode("PUREXTARACHARGES");
				objModel.setStrMasterName("Purchase Extra Charges");
				objModel.setStrMasterDesc("");
				objModel.setStrAccountCode("");
				objModel.setStrExSuppCode("");
				objModel.setStrExSuppName("");
				listARLinkUp.add(objModel);
			}
		}
		else if (strDoc.equals("OtherCharge")) {
			if(list.size()>0){
			String charge="FreightInsuranceOther ChargeFOBTAXPAYABLE";
			for (int cnt = 0; cnt < list.size(); cnt++) {
				clsLinkUpHdModel objModel = new clsLinkUpHdModel();
				Object[] arrObj = (Object[]) list.get(cnt);
				if(arrObj[0].toString().equals("Freight")){
					objModel.setStrMasterCode("Freight");
					objModel.setStrMasterName("Freight");
					charge=charge.replaceAll("Freight","");
				}else if(arrObj[0].toString().equals("Insurance")){
					objModel.setStrMasterCode("Insurance");
					objModel.setStrMasterName("Insurance");
					charge=charge.replaceAll("Insurance","");
				}else if(arrObj[0].toString().equals("Other Charge")){
					objModel.setStrMasterCode("Other Charge");
					objModel.setStrMasterName("Other Charge");
					charge=charge.replaceAll("Other Charge","");
				}else if(arrObj[0].toString().equals("FOB")){
					objModel.setStrMasterCode("FOB");
					objModel.setStrMasterName("FOB");
					charge=charge.replaceAll("FOB","");
				}else if(arrObj[0].toString().equals("TAXPAYABLE")){
					objModel.setStrMasterCode("TAXPAYABLE");
					objModel.setStrMasterName("TAXPAYABLE");
					charge=charge.replaceAll("TAXPAYABLE","");
				}
				
				objModel.setStrAccountCode(arrObj[3].toString());
				objModel.setStrMasterDesc(arrObj[2].toString());
				
				listARLinkUp.add(objModel);
			}
		 	clsLinkUpHdModel objModel = null;
			 
			for(int cnt =list.size();cnt<5;cnt++){
				//Object[] arrObj = (Object[]) list.get(cnt);
				objModel = new clsLinkUpHdModel();
				if(charge.contains("Freight")){
					objModel.setStrMasterCode("Freight");
					objModel.setStrMasterName("Freight");
					objModel.setStrAccountCode("");
					objModel.setStrMasterDesc("");
					charge=charge.replaceAll("Freight","");
			
				}else if(charge.contains("Insurance")){
					objModel.setStrMasterCode("Insurance");
					objModel.setStrMasterName("Insurance");
					objModel.setStrAccountCode("");
					objModel.setStrMasterDesc("");
					charge=charge.replaceAll("Insurance","");
				}else if(charge.contains("Other Charge")){
					objModel.setStrMasterCode("Other Charge");
					objModel.setStrMasterName("Other Charge");
					objModel.setStrAccountCode("");
					objModel.setStrMasterDesc("");
					charge=charge.replaceAll("Other Charge","");
				}else if(charge.contains("FOB")){
					objModel.setStrMasterCode("FOB");
					objModel.setStrMasterName("FOB");
					objModel.setStrAccountCode("");
					objModel.setStrMasterDesc("");
					charge=charge.replaceAll("FOB","");
				}else if(charge.equals("TAXPAYABLE")){
					objModel.setStrMasterCode("TAXPAYABLE");
					objModel.setStrMasterName("TAXPAYABLE");
					objModel.setStrAccountCode("");
					objModel.setStrMasterDesc("");
					charge=charge.replaceAll("TAXPAYABLE","");
				}
				listARLinkUp.add(objModel);
			}
		}
		else{
			clsLinkUpHdModel objModel = null;
			for(int cnt =0;cnt<5;cnt++){
				objModel = new clsLinkUpHdModel();
				if(cnt==0){
					objModel.setStrMasterCode("Freight");
					objModel.setStrMasterName("Freight");
					objModel.setStrAccountCode("");
					objModel.setStrMasterDesc("");
			
				}else if(cnt==1){
					objModel.setStrMasterCode("Insurance");
					objModel.setStrMasterName("Insurance");
					objModel.setStrAccountCode("");
					objModel.setStrMasterDesc("");
				}else if(cnt==2){
					objModel.setStrMasterCode("Other Charge");
					objModel.setStrMasterName("Other Charge");
					objModel.setStrAccountCode("");
					objModel.setStrMasterDesc("");
				}else if(cnt==3){
					objModel.setStrMasterCode("FOB");
					objModel.setStrMasterName("FOB");
					objModel.setStrAccountCode("");
					objModel.setStrMasterDesc("");
				}else if(cnt==4){
					objModel.setStrMasterCode("TAXPAYABLE");
					objModel.setStrMasterName("TAXPAYABLE");
					objModel.setStrAccountCode("");
					objModel.setStrMasterDesc("");
				}
					listARLinkUp.add(objModel);
				}
			}
		}
		else if (strDoc.equals("Location")) 
		{
			for (int cnt = 0; cnt < list.size(); cnt++) {
				clsLinkUpHdModel objModel = new clsLinkUpHdModel();
				Object[] arrObj = (Object[]) list.get(cnt);
				objModel.setStrMasterCode(arrObj[0].toString());
				objModel.setStrMasterName(arrObj[1].toString());
				objModel.setStrMasterDesc(arrObj[2].toString());
				objModel.setStrAccountCode(arrObj[3].toString());
				objModel.setStrWebBookAccCode(arrObj[4].toString());
				objModel.setStrWebBookAccName(arrObj[5].toString());
				listARLinkUp.add(objModel);
			}
		}
		return listARLinkUp;
	}
	
	
	
	
	// Convert bean to model function
	private clsPropertySetupHdModel funPrepareModel(clsPropertySetupBean objBean, String userCode, String clientCode) {
		clsPropertySetupHdModel objPropertySetupModel = new clsPropertySetupHdModel();
		List listProperties = objGlobalFunctionsService.funGetList("select strPropertyCode from "+webStockDB+".tblpropertymaster " + " where strPropertyName='" + objBean.getStrPropertyCode() + "'");
		String propertyCode = "";
		if (listProperties.size() > 0) {
			propertyCode = listProperties.get(0).toString();
		}
		objPropertySetupModel.setStrPropertyCode(propertyCode);
		objPropertySetupModel.setStrClientCode(clientCode);
		objPropertySetupModel.setTmeCheckInTime(objBean.getTmeCheckInTime());
		objPropertySetupModel.setTmeCheckOutTime(objBean.getTmeCheckOutTime());

		objPropertySetupModel.setStrSMSProvider(objBean.getStrSMSProvider());
		objPropertySetupModel.setStrReservationSMSContent(objBean.getStrReservationSMSContent());
		objPropertySetupModel.setStrCheckInSMSContent(objBean.getStrCheckInSMSContent());
		objPropertySetupModel.setStrAdvAmtSMSContent(objBean.getStrAdvAmtSMSContent());
		objPropertySetupModel.setStrCheckOutSMSContent(objBean.getStrCheckOutSMSContent());
		objPropertySetupModel.setStrSMSAPI(objBean.getStrSMSAPI());
		objPropertySetupModel.setStrRoomLimit(objBean.getStrRoomLimit());
		String GSTNo="";
		if(!objBean.getStrGSTNo().toString().equals(null))
		{
			GSTNo=objBean.getStrGSTNo().toString();
		}
		objPropertySetupModel.setStrGSTNo(objBean.getStrGSTNo().toString());
		objPropertySetupModel.setStrBankAcName(objBean.getStrBankAcName().toString());
		objPropertySetupModel.setStrBankAcNumber(objBean.getStrBankAcNumber().toString());
		objPropertySetupModel.setStrBankIFSC(objBean.getStrBankIFSC().toString());
		objPropertySetupModel.setStBranchName(objBean.getStBranchName().toString());
		objPropertySetupModel.setStrPanNo(objBean.getStrPanNo().toString());
		objPropertySetupModel.setStrHscCode(objBean.getStrHscCode().toString());
		objPropertySetupModel.setStrReservationEmailContent(objBean.getStrReservationEmailContent());
		objPropertySetupModel.setStrCheckInEmailContent(objBean.getStrCheckInEmailContent());
		
		
		return objPropertySetupModel;
	}
}
