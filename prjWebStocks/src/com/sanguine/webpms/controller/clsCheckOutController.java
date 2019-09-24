package com.sanguine.webpms.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
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
import com.sanguine.model.clsCompanyMasterModel;
import com.sanguine.model.clsPropertyMaster;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsPropertyMasterService;
import com.sanguine.webpms.bean.clsCheckInBean;
import com.sanguine.webpms.bean.clsCheckOutBean;
import com.sanguine.webpms.bean.clsCheckOutRoomDtlBean;
import com.sanguine.webpms.dao.clsWebPMSDBUtilityDao;
import com.sanguine.webpms.model.clsBillDtlModel;
import com.sanguine.webpms.model.clsBillHdModel;
import com.sanguine.webpms.model.clsBillTaxDtlModel;
import com.sanguine.webpms.model.clsCheckInDtl;
import com.sanguine.webpms.model.clsCheckInHdModel;
import com.sanguine.webpms.model.clsFolioDtlModel;
import com.sanguine.webpms.model.clsFolioHdModel;
import com.sanguine.webpms.model.clsFolioTaxDtl;
import com.sanguine.webpms.model.clsGuestMasterHdModel;
import com.sanguine.webpms.model.clsPropertySetupHdModel;
import com.sanguine.webpms.model.clsRoomMasterModel;
import com.sanguine.webpms.service.clsCheckInService;
import com.sanguine.webpms.service.clsCheckOutService;
import com.sanguine.webpms.service.clsGuestMasterService;
import com.sanguine.webpms.service.clsPropertySetupService;
import com.sanguine.webpms.service.clsRoomMasterService;

@Controller
public class clsCheckOutController {
	@Autowired
	private clsCheckOutService objCheckOutService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;

	@Autowired
	private clsPropertySetupService objPropertySetupService;

	@Autowired
	private clsGuestMasterService objGuestService;

	@Autowired
	private clsPropertyMasterService objPropertyMasterService;

	@Autowired
	private clsCheckOutService objcheckOutService;

	@Autowired
	private clsCheckInService objCheckInService;

	@Autowired
	clsRoomMasterService objRoomMaster;
	

	@Autowired
	private clsWebPMSDBUtilityDao objWebPMSUtility;
	
	@Autowired
	private clsBillPrintingController objBillPrintingController;

	// Open CheckIn
	@RequestMapping(value = "/frmCheckOut", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		clsCheckOutBean objBean = new clsCheckOutBean();
		String PMSDate = objGlobal.funGetDate("yyyy-MM-dd", request.getSession().getAttribute("PMSDate").toString());
		
		// String
		// PMSStartDay=request.getSession().getAttribute("PMSStartDay").toString();
		
		model.put("PMSDate", PMSDate);
		request.getSession().setAttribute("checkOutNo", "");
		return new ModelAndView("frmCheckOut", "command", objBean);
	}

	// get room detail for checkout
	@RequestMapping(value = "/getRoomDtlList", method = RequestMethod.GET)
	public @ResponseBody List funLoadMasterData(@RequestParam("roomCode") String roomCode, HttpServletRequest request) {
		/*
		 * String sql=
		 * "select a.strCheckInNo,a.strRegistrationNo,ifnull(a.strReservationNo,'NA'),ifnull(a.strWalkinNo,'NA') "
		 * +
		 * " ,c.strCorporateCode,d.strRoomNo,d.strFolioNo,concat(f.strFirstName,' ',f.strMiddleName,' ',f.strLastName) as GuestName "
		 * +
		 * " ,date(a.dteArrivalDate),date(a.dteDepartureDate),ifnull(g.strCorporateCode,'NA'),ifnull(sum(e.dblDebitAmt),0)+ifnull(sum(h.dblTaxAmt),0) "
		 * +
		 * " from tblcheckinhd a left outer join tblcheckindtl b on a.strCheckInNo=b.strCheckInNo "
		 * +
		 * " left outer join tblreservationhd c on a.strReservationNo=c.strReservationNo "
		 * + " left outer join tblfoliohd d on a.strCheckInNo=d.strCheckInNo " +
		 * " left outer join tblfoliodtl e on d.strFolioNo=e.strFolioNo " +
		 * " left outer join tblfoliotaxdtl h on e.strFolioNo=h.strFolioNo and e.strDocNo=h.strDocNo "
		 * +
		 * " left outer join tblguestmaster f on b.strGuestCode=f.strGuestCode "
		 * + " left outer join tblwalkinhd g on a.strWalkInNo=g.strWalkinNo " +
		 * " where d.strRoomNo='"+roomCode+"' " + " group by e.strFolioNo ";
		 * List list = objGlobalFunctionsService.funGetListModuleWise(sql,
		 * "sql");
		 */

		String clientCode = request.getSession().getAttribute("clientCode").toString();

		String sql = "select a.strCheckInNo,a.strRegistrationNo,ifnull(a.strReservationNo,'NA'),ifnull(a.strWalkinNo,'NA') " + " ,c.strCorporateCode,d.strRoomNo,d.strFolioNo,concat(IFNULL(f.strFirstName,''),' ',IFNULL(f.strMiddleName,''),' ',IFNULL(f.strLastName,'')) as GuestName " + " ,date(a.dteArrivalDate),date(a.dteDepartureDate),ifnull(g.strCorporateCode,'NA') "
				+ " from tblcheckinhd a left outer join tblcheckindtl b on a.strCheckInNo=b.strCheckInNo AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"'"
				+ " left outer join tblreservationhd c on a.strReservationNo=c.strReservationNo AND c.strClientCode='"+clientCode+"'"
				+ " left outer join tblfoliohd d on a.strCheckInNo=d.strCheckInNo AND d.strClientCode='"+clientCode+"'" 
				+ " left outer join tblguestmaster f on b.strGuestCode=f.strGuestCode AND f.strClientCode='"+clientCode+"'" 
				+ " left outer join tblwalkinhd g on a.strWalkInNo=g.strWalkinNo AND g.strClientCode='"+clientCode+"'"
				+ " where d.strRoomNo='" + roomCode + "' " + " group by d.strFolioNo ";
		List list = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");

		List<clsCheckOutRoomDtlBean> listCheckOutRoomDtl = new ArrayList<clsCheckOutRoomDtlBean>();
		for (int i = 0; i < list.size(); i++) {
			Object obj[] = (Object[]) list.get(i);
			clsCheckOutRoomDtlBean objCheckOutRoomDtlBean = new clsCheckOutRoomDtlBean();
			objCheckOutRoomDtlBean.setStrRoomNo(obj[5].toString());
			objCheckOutRoomDtlBean.setStrRegistrationNo(obj[1].toString());
			objCheckOutRoomDtlBean.setStrFolioNo(obj[6].toString());
			objCheckOutRoomDtlBean.setStrReservationNo(obj[2].toString());
			objCheckOutRoomDtlBean.setStrGuestName(obj[7].toString());
			objCheckOutRoomDtlBean.setDteCheckInDate(obj[8].toString());
			objCheckOutRoomDtlBean.setDteCheckOutDate(obj[9].toString());
			objCheckOutRoomDtlBean.setStrCorporate(obj[10].toString());
			objCheckOutRoomDtlBean.setDblAmount(0);

			sql = "select a.strFolioNo,sum(b.dblDebitAmt) " + " from tblfoliohd a,tblfoliodtl b " + " where a.strFolioNo=b.strFolioNo and a.strRoomNo='" + roomCode + "' " + " AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' group by a.strFolioNo";
			List listFolioAmt = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			for (int cnt = 0; cnt < listFolioAmt.size(); cnt++) {
				Object[] arrObjFolio = (Object[]) listFolioAmt.get(cnt);
				objCheckOutRoomDtlBean.setDblAmount(objCheckOutRoomDtlBean.getDblAmount() + Double.parseDouble(arrObjFolio[1].toString()));
			}

			sql = "select sum(b.dblDebitAmt),sum(c.dblTaxAmt) " + " from tblfoliohd a,tblfoliodtl b,tblfoliotaxdtl c " + " where a.strFolioNo=b.strFolioNo and b.strFolioNo=c.strFolioNo and b.strDocNo=c.strDocNo " + " and a.strRoomNo='" + roomCode + "' " + " AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' group by b.strFolioNo";
			List listFolioTaxAmt = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			for (int cnt = 0; cnt < listFolioTaxAmt.size(); cnt++) {
				Object[] arrObjFolio = (Object[]) listFolioTaxAmt.get(cnt);
				objCheckOutRoomDtlBean.setDblAmount(objCheckOutRoomDtlBean.getDblAmount() + Double.parseDouble(arrObjFolio[1].toString()));
			}

			sql = "select a.strReceiptNo,sum(b.dblSettlementAmt) " + " from tblreceipthd a,tblreceiptdtl b " + " where a.strReceiptNo=b.strReceiptNo and a.strRegistrationNo='" + objCheckOutRoomDtlBean.getStrRegistrationNo() + "' and a.strAgainst='Check-In' " + " AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' group by a.strReceiptNo";
			List listReceiptAmtAtCheckIN = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			for (int cnt = 0; cnt < listReceiptAmtAtCheckIN.size(); cnt++) {
				Object[] arrObjFolio = (Object[]) listReceiptAmtAtCheckIN.get(cnt);
				objCheckOutRoomDtlBean.setDblAmount(objCheckOutRoomDtlBean.getDblAmount() - Double.parseDouble(arrObjFolio[1].toString()));
			}
			
			sql = "SELECT Sum(a.dblCreditAmt) from tblfoliodtl a where a.strFolioNo='"+obj[6].toString()+"' AND a.strClientCode='"+clientCode+"'";
			List listFolioDisc = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			for (int cnt = 0; cnt < listFolioDisc.size(); cnt++) {
				objCheckOutRoomDtlBean.setDblAmount(objCheckOutRoomDtlBean.getDblAmount() - Double.parseDouble(listFolioDisc.get(cnt).toString()));
			}
            
			boolean flgIsFullPayment=false;
			if(!objCheckOutRoomDtlBean.getStrReservationNo().toString().isEmpty())
			{
				sql="SELECT ifnull(SUM(c.dblSettlementAmt),0),ifnull(sum(b.dblGrandTotal),0)"
						+ " FROM tblreceipthd a left outer join tblbillhd b on a.strBillNo=b.strBillNo and a.strReservationNo and b.strReservationNo,tblreceiptdtl c"
						+ " WHERE a.strReceiptNo=c.strReceiptNo AND a.strReservationNo='"+objCheckOutRoomDtlBean.getStrReservationNo()+"'"; 
					List listCheckForFullPayment= objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
					for (int cnt = 0; cnt < listCheckForFullPayment.size(); cnt++) 
					{
						Object arrObjFolio[] = (Object[]) listCheckForFullPayment.get(cnt);
						if(Double.parseDouble(arrObjFolio[0].toString())>0)
						{
							if(Double.parseDouble(arrObjFolio[0].toString())==Double.parseDouble(arrObjFolio[1].toString()))
							{
								flgIsFullPayment=true;
								break;
							}
						}
					}
					if(!flgIsFullPayment)
					{
						sql = "select sum(b.dblSettlementAmt) " + " from tblreceipthd a,tblreceiptdtl b " + " where a.strReceiptNo=b.strReceiptNo and a.strReservationNo='" + objCheckOutRoomDtlBean.getStrReservationNo() + "' " + " and a.strAgainst='Reservation' " + " group by a.strReceiptNo";
						List listReceiptAmtAtReservation = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
						for (int cnt = 0; cnt < listReceiptAmtAtReservation.size(); cnt++) {
							Object arrObjFolio = (Object) listReceiptAmtAtReservation.get(cnt);
							objCheckOutRoomDtlBean.setDblAmount(objCheckOutRoomDtlBean.getDblAmount() - Double.parseDouble(arrObjFolio.toString()));
						}	
					}
			}
			
			listCheckOutRoomDtl.add(objCheckOutRoomDtlBean);
		}

		return listCheckOutRoomDtl;
	}

	// Save or Update CheckIn
	@RequestMapping(value = "/saveCheckOut", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsCheckOutBean objBean, BindingResult result, HttpServletRequest req,HttpServletResponse resp) {
		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		String strFolioNo="";
		boolean checkFolioStatus=false;
		if (!result.hasErrors()) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			List<clsCheckOutRoomDtlBean> listCheckOutRoomDtlBeans = objBean.getListCheckOutRoomDtlBeans();
			String PMSDate = objGlobal.funGetDate("yyyy-MM-dd", req.getSession().getAttribute("PMSDate").toString());
			String billNo = "";
			String RommNo = "";
			String GSTNo="";
			String companyName = req.getSession().getAttribute("companyName").toString();
			String propCode = req.getSession().getAttribute("propertyCode").toString();
			clsPropertySetupHdModel objModel = objPropertySetupService.funGetPropertySetup(propCode, clientCode);  //pms property setup
			if (objModel != null) {
				GSTNo=objModel.getStrGSTNo();
			} 
			for (int i = 0; i < listCheckOutRoomDtlBeans.size(); i++) {
				clsCheckOutRoomDtlBean objCheckOutRoomDtlBean = listCheckOutRoomDtlBeans.get(i);
				clsFolioHdModel objFolioHdModel = objCheckOutService.funGetFolioHdModel(objCheckOutRoomDtlBean.getStrRoomNo(), objCheckOutRoomDtlBean.getStrRegistrationNo(), "", clientCode);
				if (objFolioHdModel != null) {
					// generate billNo.
					String transaDate = objGlobal.funGetCurrentDateTime("dd-MM-yyyy").split(" ")[0];
					billNo = objGlobal.funGeneratePMSDocumentCode("frmBillHd", transaDate, req);
					RommNo = objFolioHdModel.getStrRoomNo();
					clsBillHdModel objBillHdModel = new clsBillHdModel();
					objBillHdModel.setStrBillNo(billNo);
					objBillHdModel.setDteBillDate(PMSDate);
					objBillHdModel.setStrClientCode(clientCode);
					objBillHdModel.setStrCheckInNo(objFolioHdModel.getStrCheckInNo());
					objBillHdModel.setStrFolioNo(objFolioHdModel.getStrFolioNo());
					strFolioNo=objFolioHdModel.getStrFolioNo();
					objBillHdModel.setStrRoomNo(objFolioHdModel.getStrRoomNo());
					objBillHdModel.setStrExtraBedCode(objFolioHdModel.getStrExtraBedCode());
					objBillHdModel.setStrRegistrationNo(objFolioHdModel.getStrRegistrationNo());
					objBillHdModel.setStrReservationNo(objFolioHdModel.getStrReservationNo());
					objBillHdModel.setDblGrandTotal(0.00);
					objBillHdModel.setStrUserCreated(userCode);
					objBillHdModel.setStrUserEdited(userCode);
					objBillHdModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
					objBillHdModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
					objBillHdModel.setStrGSTNo(GSTNo);
					objBillHdModel.setStrCompanyName(companyName);

					double grandTotal = 0;

					List<clsBillDtlModel> listBillDtlModel = new ArrayList<clsBillDtlModel>();
					// Set<clsBillDtlModel> setBillDtlModel=new
					// TreeSet<clsBillDtlModel>();
					List<clsFolioDtlModel> listFolioDtlModel = objFolioHdModel.getListFolioDtlModel();
					for (clsFolioDtlModel objFolioDtlModel : listFolioDtlModel) {
						clsBillDtlModel objBillDtlModel = new clsBillDtlModel();
						objBillDtlModel.setStrFolioNo(objFolioHdModel.getStrFolioNo());
						objBillDtlModel.setStrDocNo(objFolioDtlModel.getStrDocNo());
						objBillDtlModel.setStrPerticulars(objFolioDtlModel.getStrPerticulars());
						objBillDtlModel.setDteDocDate(objFolioDtlModel.getDteDocDate());
						objBillDtlModel.setDblBalanceAmt(objFolioDtlModel.getDblBalanceAmt());
						objBillDtlModel.setDblCreditAmt(objFolioDtlModel.getDblCreditAmt());
						objBillDtlModel.setDblDebitAmt(objFolioDtlModel.getDblDebitAmt());
						objBillDtlModel.setStrRevenueType(objFolioDtlModel.getStrRevenueType());
						objBillDtlModel.setStrRevenueCode(objFolioDtlModel.getStrRevenueCode());
						objBillDtlModel.setStrUserEdited(objFolioDtlModel.getStrUserEdited());
						objBillDtlModel.setDteDateEdited(objFolioDtlModel.getDteDateEdited());
						objBillDtlModel.setStrTransactionType(objFolioDtlModel.getStrTransactionType());
						
						
						
						grandTotal += objFolioDtlModel.getDblDebitAmt();
						listBillDtlModel.add(objBillDtlModel);
						// setBillDtlModel.add(objBillDtlModel);
					}

					List<clsBillTaxDtlModel> listBillTaxDtlModel = new ArrayList<clsBillTaxDtlModel>();
					// Set<clsBillTaxDtlModel> setBillTaxDtlModel=new
					// TreeSet<clsBillTaxDtlModel>();
					List<clsFolioTaxDtl> listFolioTaxDtlModel = objFolioHdModel.getListFolioTaxDtlModel();
					for (clsFolioTaxDtl objFolioTaxDtlModel : listFolioTaxDtlModel) {
						clsBillTaxDtlModel objBillTaxDtlModel = new clsBillTaxDtlModel();
						objBillTaxDtlModel.setStrDocNo(objFolioTaxDtlModel.getStrDocNo());
						objBillTaxDtlModel.setStrTaxCode(objFolioTaxDtlModel.getStrTaxCode());
						objBillTaxDtlModel.setStrTaxDesc(objFolioTaxDtlModel.getStrTaxDesc());
						objBillTaxDtlModel.setDblTaxableAmt(objFolioTaxDtlModel.getDblTaxableAmt());
						objBillTaxDtlModel.setDblTaxAmt(objFolioTaxDtlModel.getDblTaxAmt());
						grandTotal += objBillTaxDtlModel.getDblTaxAmt();

						listBillTaxDtlModel.add(objBillTaxDtlModel);
						// setBillTaxDtlModel.add(objBillTaxDtlModel);
					}
					objBillHdModel.setStrBillSettled("N");
					objBillHdModel.setDblGrandTotal(grandTotal);
					objBillHdModel.setListBillDtlModels(listBillDtlModel);
					objBillHdModel.setListBillTaxDtlModels(listBillTaxDtlModel);
					// objBillHdModel.setSetBillDtlModels(setBillDtlModel);
					// objBillHdModel.setSetBillTaxDtlModels(setBillTaxDtlModel);
					
					LocalTime localTime = LocalTime.now();
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
					
					String strCurrentTime = localTime.format(dateTimeFormatter);
					String sqlUpdateCheckOutTime = "update tblcheckinhd a set a.tmeDepartureTime='"+strCurrentTime+"' where a.strCheckInNo='"+objBillHdModel.getStrCheckInNo()+"'  AND a.strClientCode='"+clientCode+"'";
					objWebPMSUtility.funExecuteUpdate(sqlUpdateCheckOutTime, "sql");
					
					String sqlUpdateCheckOutDate ="UPDATE tblcheckinhd a SET a.dteDepartureDate= CONCAT('"+PMSDate+"',' ','00:00:00') "
					+ "WHERE a.strCheckInNo='"+objBillHdModel.getStrCheckInNo()+"' AND a.strClientCode='"+clientCode+"'";
					
					
					objWebPMSUtility.funExecuteUpdate(sqlUpdateCheckOutDate, "sql");
					
					String sql = "insert into tblfoliobckp (select * from tblfoliodtl where strFolioNo='"+objFolioHdModel.getStrFolioNo()+"' AND strclientCode='"+clientCode+"')";
					objWebPMSUtility.funExecuteUpdate(sql, "sql");
					
					
					objCheckOutService.funSaveCheckOut(objFolioHdModel, objBillHdModel);
					
					String sqlBillPerticulars = "select a.strPerticulars from tblbilldtl a where a.strBillNo='"+billNo+"' and a.strClientCode='"+clientCode+"'";
					List listCheckForFullPayment= objGlobalFunctionsService.funGetListModuleWise(sqlBillPerticulars, "sql");
					String strParticulars = "";
					for(int j=0;j<listCheckForFullPayment.size();j++)
					{
						strParticulars = strParticulars+listCheckForFullPayment.get(j).toString()+",";
					}
					strParticulars=strParticulars.substring(0,strParticulars.length()-1);
					
					
					objBillPrintingController.funGenerateBillPrintingReport(PMSDate, PMSDate, billNo, strParticulars, req, resp); 
					
				}
				funSendSMSPayment(billNo, clientCode, RommNo, propCode);
				req.getSession().setAttribute("success", true);
				req.getSession().setAttribute("successMessage", "Room No. : ".concat(objBean.getStrSearchTextField()));
			}
			return new ModelAndView("frmCheckOut.html?saddr=" + urlHits);
		} else {
			return new ModelAndView("frmCheckOut?saddr=" + urlHits);
		}
	}
	
	@RequestMapping(value = "/isCheckFolioStatus", method = RequestMethod.GET)
	public @ResponseBody boolean funIsCheckFolioStatus(@RequestParam("folioNo") String strFolioNo,@RequestParam("checkOutDate") String checkOutDate, HttpServletRequest req)
	{
		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String propertyCode = req.getSession().getAttribute("propertyCode").toString();
		boolean checkFolioStatus=true;
		String sql = " select TIME_FORMAT(SYSDATE(),'%H:%i%p')from DUAL";
		List listCheckFolio = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		String tmeCheckoutTime = "";
		String strTimeDiff =  "";
		if(listCheckFolio.size()>0)
		{
			tmeCheckoutTime = listCheckFolio.get(0).toString();
		}
		clsPropertySetupHdModel objModel = objPropertySetupService.funGetPropertySetup(propertyCode, clientCode);
		if(tmeCheckoutTime.contains("PM"))
		{
			checkFolioStatus=false;
		}
				
		
		return checkFolioStatus;
	}
	

	@RequestMapping(value = "/isPendingRoomTerrif", method = RequestMethod.POST)
	public @ResponseBody boolean funIsPendingRoomTerrif(@RequestParam("roomCode") String roomCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		boolean res = false;
		String PMSCheckOutTime = req.getSession().getAttribute("PMSCheckOutTime").toString();
		String[] time = PMSCheckOutTime.split(":");
		int hr = Integer.parseInt(time[0]);
		int min = Integer.parseInt(time[1]);
		int sec = Integer.parseInt(time[2]);
		Date dtCurrentDate = new Date();
		Date dtCheckOutDate = new Date(dtCurrentDate.getYear(), dtCurrentDate.getMonth(), dtCurrentDate.getDate(), hr, min, sec);
		String PMSDate = objGlobal.funGetDate("yyyy-MM-dd", req.getSession().getAttribute("PMSDate").toString());

		long diff = objGlobal.funCompareTime(dtCheckOutDate, dtCurrentDate);
		System.out.println(diff);
		if (diff < 0) {
			String sql = "select b.strDocNo " + " from tblfoliohd a,tblfoliodtl b " + " where a.strFolioNo=b.strFolioNo and a.strRoomNo='" + roomCode + "' and date(dteDocDate)='" + PMSDate + "' " + " and b.strRevenueType='Room' and a.strClientCode='" + clientCode + "' ";
			List listRoomTerrif = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			if (listRoomTerrif.size() == 0) {
				res = true;
			}
		}
		return res;
	}

	private void funSendSMSPayment(String billNo, String clientCode, String roomNo, String propCode) {

		String strMobileNo = "";
		clsPropertySetupHdModel objSetup = objPropertySetupService.funGetPropertySetup(propCode, clientCode);

		List listcheckOut = objCheckOutService.funGetCheckOut(roomNo, billNo, clientCode);

		clsBillHdModel objBillHdModel = (clsBillHdModel) listcheckOut.get(0);
		List listDtl = objBillHdModel.getListBillDtlModels();
		// clsBillDtlModel dtlModel=(clsBillDtlModel) listDtl.get(0);
		clsCheckInHdModel objHdModel = objCheckInService.funGetCheckInData(objBillHdModel.getStrCheckInNo(), clientCode);

		List listcheckDtl = objHdModel.getListCheckInDtl();

		for (int i = 0; i < listcheckDtl.size(); i++) {
			clsCheckInDtl obkCheckInDtl = (clsCheckInDtl) listcheckDtl.get(i);

			if (obkCheckInDtl.getStrPayee().equalsIgnoreCase("Y")) {
				List list1 = objGuestService.funGetGuestMaster(obkCheckInDtl.getStrGuestCode(), clientCode);
				clsGuestMasterHdModel objGuestModel = null;

				if (list1.size() > 0) {
					objGuestModel = (clsGuestMasterHdModel) list1.get(0);

				}

				String smsAPIUrl = objSetup.getStrSMSAPI();

				String smsContent = objSetup.getStrCheckOutSMSContent();

				if (!smsAPIUrl.equals("")) {
					if (smsContent.contains("%%CompanyName")) {
						List<clsCompanyMasterModel> listCompanyModel = objPropertySetupService.funGetListCompanyMasterModel(clientCode);
						smsContent = smsContent.replace("%%CompanyName", listCompanyModel.get(0).getStrCompanyName());
					}
					if (smsContent.contains("%%PropertyName")) {
						clsPropertyMaster objProperty = objPropertyMasterService.funGetProperty(propCode, clientCode);
						smsContent = smsContent.replace("%%PropertyName", objProperty.getPropertyName());
					}

					if (smsContent.contains("%%billNo")) {
						smsContent = smsContent.replace("%%billNo", billNo);
					}

					if (smsContent.contains("%%Checkoutdate")) {
						smsContent = smsContent.replace("%%Checkoutdate", objBillHdModel.getDteBillDate());
					}
					if (smsContent.contains("%%GuestName")) {
						smsContent = smsContent.replace("%%GuestName", objGuestModel.getStrFirstName() + " " + objGuestModel.getStrMiddleName() + " " + objGuestModel.getStrLastName());
					}

					if (smsContent.contains("%%RoomNo")) {
						clsRoomMasterModel roomNo1 = objRoomMaster.funGetRoomMaster(obkCheckInDtl.getStrRoomNo(), clientCode);
						smsContent = smsContent.replace("%%RoomNo", roomNo1.getStrRoomDesc());
					}

					if (smsAPIUrl.contains("ReceiverNo")) {

						smsAPIUrl = smsAPIUrl.replace("ReceiverNo", String.valueOf(objGuestModel.getLngMobileNo()));
						strMobileNo = String.valueOf(objGuestModel.getLngMobileNo());
					}
					if (smsAPIUrl.contains("MsgContent")) {
						smsAPIUrl = smsAPIUrl.replace("MsgContent", smsContent);
						smsAPIUrl = smsAPIUrl.replace(" ", "%20");
					}

					URL url;
					HttpURLConnection uc = null;
					StringBuilder output = new StringBuilder();

					try {
						url = new URL(smsAPIUrl);
						uc = (HttpURLConnection) url.openConnection();
						if (String.valueOf(objGuestModel.getLngMobileNo()).length() >= 10) {
							BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream(), Charset.forName("UTF-8")));
							String inputLine;
							while ((inputLine = in.readLine()) != null) {
								output.append(inputLine);
							}
							in.close();
						}

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						uc.disconnect();
					}
				}
			}
			
		}
	}
	
	@RequestMapping(value = "/frmCheckOut1", method = RequestMethod.GET)
	public ModelAndView funOpenForm1(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		String strFolioNo = request.getParameter("docCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String strRoomNo="";
		String sqlRoomNo = "select a.strRoomNo from tblfoliohd a where a.strFolioNo='"+strFolioNo+"' and a.strClientCode='"+clientCode+"'";
		List listRoomNo = objGlobalFunctionsService.funGetListModuleWise(sqlRoomNo, "sql");
		if(listRoomNo!=null && listRoomNo.size()>0)
		{
			strRoomNo = listRoomNo.get(0).toString();
		}
		try {
			urlHits = request.getParameter("saddr").toString();

		} catch (NullPointerException e) {
			urlHits = "1";
		}

		model.put("urlHits", urlHits);

		request.getSession().setAttribute("checkOutNo", strRoomNo);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmCheckOut_1", "command", new clsCheckOutBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmCheckOut", "command", new clsCheckOutBean());
		} else {
			return null;
		}
	}

}
