package com.sanguine.webpms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsPMSSalesFlashBean;

@Controller
public class clsPMSSalesFlashController {
	@Autowired
	private clsGlobalFunctionsService objGlobalService;

	private HashMap<String, clsPMSSalesFlashBean> mapIncomeHeads;

	@Autowired
	private clsGlobalFunctions objGlobal;

	@RequestMapping(value = "/frmPMSSalesFlash", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model,
			HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPMSSalesFlash_1", "command",
					new clsPMSSalesFlashBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPMSSalesFlash", "command",
					new clsPMSSalesFlashBean());
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/loadSettlementWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funLoadSettlementWiseDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listofSettlementDtl = new ArrayList<clsPMSSalesFlashBean>();

		String sql = "select c.strSettlementDesc,sum(b.dblSettlementAmt) "
				+ " from tblreceipthd a ,tblreceiptdtl b ,tblsettlementmaster c"
				+ " where a.strReceiptNo=b.strReceiptNo"
				+ " and date(a.dteReceiptDate)  between '"
				+ fromDte
				+ "' and '"
				+ toDte
				+ "' "
				+ " and b.strSettlementCode=c.strSettlementCode"
				+ " and a.strClientCode=b.strClientCode and b.strClientCode=c.strClientCode "
				+ " group by b.strSettlementCode;";

		List listSettlementDtl = objGlobalService.funGetListModuleWise(sql,"sql");
		if (!listSettlementDtl.isEmpty()) {
			for (int i = 0; i < listSettlementDtl.size(); i++) {
				Object[] arr2 = (Object[]) listSettlementDtl.get(i);
				clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
				objBean.setStrSettlementDesc(arr2[0].toString());
				objBean.setDblSettlementAmt(arr2[1].toString());
				listofSettlementDtl.add(objBean);
			}
		}
		return listofSettlementDtl;
	}

	@RequestMapping(value = "/loadRevenueHeadWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funLoadRevenueHeadWiseDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listofRevenueDtl = new ArrayList<clsPMSSalesFlashBean>();
		HashMap<String, Double> hmRevenueType = new HashMap<String, Double>();
		String sql = "select * from "
				+ " (SELECT b.strRevenueType as strRevenueType , SUM(b.dblDebitAmt) as Amount "
				+ " FROM tblbillhd a, tblbilldtl b WHERE a.strBillNo=b.strBillNo AND DATE(a.dteBillDate) BETWEEN '"
				+ fromDte
				+ "' AND '"
				+ toDte
				+ "' GROUP BY b.strRevenueType) c"
				+ " union"
				+ " select * from"
				+ " (SELECT b.strRevenueType as strRevenueType, SUM(b.dblDebitAmt) as Amount "
				+ " FROM tblfoliohd a,tblfoliodtl b"
				+ " WHERE a.strFolioNo=b.strFolioNo AND DATE(b.dteDocDate) BETWEEN '"
				+ fromDte + "' AND '" + toDte + "' "
				+ " GROUP BY b.strRevenueType ) d ";

		List listRevenueDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listRevenueDtl.isEmpty()) {
			for (int i = 0; i < listRevenueDtl.size(); i++) {
				Object[] arr2 = (Object[]) listRevenueDtl.get(i);
				if (hmRevenueType.containsKey(arr2[0].toString())) {
					double dblAmount = hmRevenueType.get(arr2[0].toString())
							+ Double.parseDouble(arr2[1].toString());
					hmRevenueType.put(arr2[0].toString(), dblAmount);

				} else {
					hmRevenueType.put(arr2[0].toString(),
							Double.parseDouble(arr2[1].toString()));
				}
			}
		}
		for (HashMap.Entry<String, Double> hmRevenue : hmRevenueType.entrySet()) {
			clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
			objBean.setStrRevenueType(hmRevenue.getKey());
			objBean.setDblAmount(hmRevenue.getValue());
			listofRevenueDtl.add(objBean);
		}

		return listofRevenueDtl;
	}

	@RequestMapping(value = "/loadTaxWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funTaxWiseDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listofTaxDtl = new ArrayList<clsPMSSalesFlashBean>();
		String sql = " SELECT  IFNULL(c.strTaxDesc,''), IFNULL(SUM(c.dblTaxableAmt),0), IFNULL(SUM(c.dblTaxAmt),0) "
				+ " FROM tblbillhd a "
				+ " LEFT OUTER "
				+ " JOIN tblbilldtl b ON a.strBillNo=b.strBillNo "
				+ " LEFT OUTER "
				+ " JOIN tblbilltaxdtl c ON b.strDocNo=c.strDocNo AND b.strBillNo=c.strBillNo "
				+ " WHERE DATE(a.dteBillDate) BETWEEN '"
				+ fromDte
				+ "' AND '"
				+ toDte
				+ "' AND c.strTaxDesc!='' "
				+ " GROUP BY c.strTaxDesc; ";

		List listTaxDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listTaxDtl.isEmpty()) {
			for (int i = 0; i < listTaxDtl.size(); i++) {
				Object[] arr2 = (Object[]) listTaxDtl.get(i);
				clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
				objBean.setStrTaxDesc(arr2[0].toString());
				objBean.setDblTaxableAmt(arr2[1].toString());
				objBean.setDblTaxAmt(arr2[2].toString());
				listofTaxDtl.add(objBean);
			}
		}

		return listofTaxDtl;
	}

	@RequestMapping(value = "/loadExpectedArrWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funExpectedArrWiseDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listofExpectedArrDtl = new ArrayList<clsPMSSalesFlashBean>();
		String sql = "SELECT a.strReservationNo,  DATE_FORMAT(a.dteDateCreated,'%d-%m-%Y'),CONCAT(e.strFirstName,' ',e.strMiddleName,' ',e.strLastName),   IFNULL(d.dblReceiptAmt,0), DATE_FORMAT(a.dteArrivalDate,'%d-%m-%Y'), DATE_FORMAT(a.dteDepartureDate,'%d-%m-%Y') "
				+ " FROM tblreservationhd a "
				+ " LEFT OUTER JOIN tblreservationdtl b ON a.strReservationNo=b.strReservationNo "
				+ " LEFT OUTER JOIN tblbookingtype c ON a.strBookingTypeCode=c.strBookingTypeCode "
				+ " LEFT OUTER JOIN tblreceipthd d ON a.strReservationNo=d.strRegistrationNo "
				+ " LEFT OUTER JOIN tblguestmaster e ON e.strGuestCode=b.strGuestCode "
				+ " WHERE DATE(a.dteArrivalDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode=b.strClientCode "
				+ " AND a.strReservationNo NOT IN (SELECT strReservationNo FROM tblcheckinhd) ;";

		List listArrivalDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listArrivalDtl.isEmpty()) {
			for (int i = 0; i < listArrivalDtl.size(); i++) {
				Object[] arr2 = (Object[]) listArrivalDtl.get(i);
				clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
				objBean.setStrReservationNo(arr2[0].toString());
				objBean.setDteReservationDate(arr2[1].toString());
				objBean.setStrGuestName(arr2[2].toString());
				objBean.setDblReceiptAmt(arr2[3].toString());
				objBean.setDteArrivalDate(arr2[4].toString());
				objBean.setDteDepartureDate(arr2[5].toString());
				listofExpectedArrDtl.add(objBean);

			}
		}

		return listofExpectedArrDtl;
	}

	
	@RequestMapping(value = "/loadExpectedDeptWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funExpectedDeptWiseDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listofExpectedDeptDtl = new ArrayList<clsPMSSalesFlashBean>();
		String sql="SELECT a.strCheckInNo,a.strType, DATE(a.dteDepartureDate),c.strRoomDesc,c.strRoomTypeDesc, CONCAT(d.strFirstName,' ',d.strMiddleName,' ',d.strLastName) "
                  +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d,tblbillhd e "
                  +" WHERE a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode "
                  +" AND b.strGuestCode=d.strGuestCode "
                  +" AND DATE(a.dteCheckInDate) BETWEEN '"+fromDte+"' AND '"+toDte+"';";
		
		List listExpectedDeptDtl=objGlobalService.funGetListModuleWise(sql,"sql");
		if(!listExpectedDeptDtl.isEmpty())
		{
			for(int i=0;i<listExpectedDeptDtl.size();i++)
			{
				Object[] arr2=(Object[]) listExpectedDeptDtl.get(i);
				clsPMSSalesFlashBean objBean=new clsPMSSalesFlashBean();
				objBean.setStrCheckInNo(arr2[0].toString());
				objBean.setStrBookingType(arr2[1].toString());
				objBean.setDteDepartureDate(arr2[2].toString());
				objBean.setStrRoomDesc(arr2[3].toString());
				objBean.setStrRoomType(arr2[4].toString());
				objBean.setStrGuestName(arr2[5].toString());
				listofExpectedDeptDtl.add(objBean);
				
			}
		}
		
		return listofExpectedDeptDtl;
		
		
	
	}

	@RequestMapping(value = "/loadCheckInDtl", method = RequestMethod.GET)
	public @ResponseBody List funCheckInDtl(HttpServletRequest request) 
	{
		    String fromDate = request.getParameter("frmDte").toString();
			String[] arr = fromDate.split("-");
			String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
			String toDate = request.getParameter("toDte").toString();
			String[] arr1 = toDate.split("-");
			String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

			List<clsPMSSalesFlashBean> listofCheckInDtl = new ArrayList<clsPMSSalesFlashBean>();
			String sql="SELECT a.strCheckInNo,a.strType, DATE(a.dteArrivalDate),c.strRoomDesc,c.strRoomTypeDesc,CONCAT(d.strFirstName,' ',d.strMiddleName,' ',d.strLastName), "
                      +" a.tmeArrivalTime"
					  +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d,tblbillhd e "
                      +" WHERE DATE(a.dteCheckInDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode AND b.strGuestCode=d.strGuestCode "
                      +" AND a.strCheckInNo=e.strCheckInNo ;";
			List listCheckInDtl = objGlobalService.funGetListModuleWise(sql, "sql");
			if (!listCheckInDtl.isEmpty()) {
				for (int i = 0; i < listCheckInDtl.size(); i++) {
					Object[] arr2 = (Object[]) listCheckInDtl.get(i);
					clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
					objBean.setStrCheckInNo(arr2[0].toString());
					objBean.setStrGuestName(arr2[5].toString());
					objBean.setDteCheckInDate(arr2[2].toString());
					objBean.setStrRoomDesc(arr2[3].toString());
					objBean.setStrRoomType(arr2[4].toString());
					objBean.setStrBookingType(arr2[1].toString());
					objBean.setStrArrivalTime(arr2[6].toString());
					listofCheckInDtl.add(objBean);

				}
			}
            return listofCheckInDtl;
	}

	@RequestMapping(value = "/loadCheckOutDtl", method = RequestMethod.GET)
	public @ResponseBody List funCheckOutDtl(HttpServletRequest request) 
	{
		    String fromDate = request.getParameter("frmDte").toString();
			String[] arr = fromDate.split("-");
			String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
			String toDate = request.getParameter("toDte").toString();
			String[] arr1 = toDate.split("-");
			String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

			List<clsPMSSalesFlashBean> listofCheckOutDtl = new ArrayList<clsPMSSalesFlashBean>();
			String sql="SELECT a.strCheckInNo,a.strType , DATE(a.dteDepartureDate),c.strRoomDesc,c.strRoomTypeDesc,CONCAT(d.strFirstName,' ',d.strMiddleName,' ',d.strLastName),"
					  +" e.dblGrandTotal"
                      +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d,tblbillhd e "
                      +" WHERE a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode AND b.strGuestCode=d.strGuestCode "
                      +" AND a.strCheckInNo=e.strCheckInNo AND DATE(a.dteCheckInDate) BETWEEN '"+fromDte+"' AND '"+toDte+"';";
			List listCheckOutDtl = objGlobalService.funGetListModuleWise(sql, "sql");
			if (!listCheckOutDtl.isEmpty()) {
				for (int i = 0; i < listCheckOutDtl.size(); i++) {
				    Object[] arr2=(Object[]) listCheckOutDtl.get(i);
					clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
					objBean.setStrCheckInNo(arr2[0].toString());
					objBean.setStrBookingType(arr2[1].toString());
					objBean.setDteDepartureDate(arr2[2].toString());
					objBean.setStrRoomDesc(arr2[3].toString());
					objBean.setStrRoomType(arr2[4].toString());
					objBean.setStrGuestName(arr2[5].toString());
					objBean.setDblGrandTotal(arr2[6].toString());
					listofCheckOutDtl.add(objBean);

				}
			}
			return listofCheckOutDtl;
	
	}
	
	
	
	@RequestMapping(value = "/loadCancelationWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funCancelationDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listofCancelationDtl = new ArrayList<clsPMSSalesFlashBean>();
		String sql = "SELECT a.strReservationNo, CONCAT(c.strFirstName,' ',c.strMiddleName,' ',c.strLastName) AS strGuestName, e.strBookingTypeDesc,h.strRoomTypeDesc,DATE_FORMAT(b.dteReservationDate,'%d-%m-%Y') AS dteReservationDate,DATE_FORMAT(a.dteCancelDate,'%d-%m-%Y') AS dteCancelDate,f.strRoomDesc, g.strReasonDesc, a.strRemarks "
				+ " FROM tblroomcancelation a,tblreservationhd b,tblguestmaster c,tblreservationdtl d,tblbookingtype e,tblroom f, tblreasonmaster g,tblroomtypemaster h "
				+ " WHERE DATE(a.dteCancelDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strReservationNo=b.strReservationNo AND b.strCancelReservation='Y' AND b.strReservationNo=d.strReservationNo "
				+ " AND d.strGuestCode=c.strGuestCode AND b.strBookingTypeCode = e.strBookingTypeCode AND d.strRoomType=f.strRoomTypeCode "
				+ " AND a.strReasonCode=g.strReasonCode AND a.strClientCode=b.strClientCode AND h.strRoomTypeCode=d.strRoomType "
				+ " GROUP BY b.strReservationNo,d.strGuestCode ;";
		List listCancelationDtl = objGlobalService.funGetListModuleWise(sql,"sql");

		if (!listCancelationDtl.isEmpty()) {
			for (int i = 0; i < listCancelationDtl.size(); i++) {
				Object[] arr2 = (Object[]) listCancelationDtl.get(i);
				clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
				objBean.setStrReservationNo(arr2[0].toString());
				objBean.setStrGuestName(arr2[1].toString());
				objBean.setStrBookingType(arr2[2].toString());
				objBean.setStrRoomType(arr2[3].toString());
				objBean.setDteReservationDate(arr2[4].toString());
				objBean.setDteCancelDate(arr2[5].toString());
				objBean.setStrRoomDesc(arr2[6].toString());
				objBean.setStrReasonDesc(arr2[7].toString());
				objBean.setStrRemark(arr2[8].toString());
				listofCancelationDtl.add(objBean);
			}
		}

		return listofCancelationDtl;

	}

	@RequestMapping(value = "/loadNoShowDtl", method = RequestMethod.GET)
	public @ResponseBody List funNoShowDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listofNoShowDtl = new ArrayList<clsPMSSalesFlashBean>();
		String sql = "SELECT CONCAT(c.strFirstName,' ',c.strMiddleName,' ',c.strLastName),a.strReservationNo,a.strNoRoomsBooked, IFNULL(b.dblReceiptAmt,0) "
				+ " from tblreservationhd a left outer join tblreceipthd b "
				+ " on a.strReservationNo=b.strReservationNo,tblguestmaster c,tblreservationdtl d "
				+ " where  a.strReservationNo=d.strReservationNo and d.strGuestCode=c.strGuestCode "
				+ " and date(a.dteArrivalDate) between '"
				+ fromDte
				+ "' and '"
				+ toDte
				+ "' and "
				+ " date(a.dteDepartureDate) between '"
				+ fromDte
				+ "' and '"
				+ toDte
				+ "' "
				+ " and  a.strReservationNo Not IN(select strReservationNo from tblcheckinhd )";
		List listNoShowDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listNoShowDtl.isEmpty()) {
			for (int i = 0; i < listNoShowDtl.size(); i++) {
				Object[] arr2 = (Object[]) listNoShowDtl.get(i);
				clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
				objBean.setStrGuestName(arr2[0].toString());
				objBean.setStrReservationNo(arr2[1].toString());
				objBean.setStrNoOfRooms(arr2[2].toString());
				objBean.setDblReceiptAmt(arr2[3].toString());
				listofNoShowDtl.add(objBean);
			}
		}
		return listofNoShowDtl;
	}

	@RequestMapping(value = "/loadVoidBillDtl", method = RequestMethod.GET)
	public @ResponseBody List funVoidBillDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listofVoidBillDtl = new ArrayList<clsPMSSalesFlashBean>();
		String sql = "SELECT a.strBillNo, DATE_FORMAT(a.dteBillDate,'%d-%m-%Y'),CONCAT(e.strGuestPrefix,\" \",e.strFirstName,\" \",e.strLastName) AS gName,d.strRoomDesc,b.strPerticulars, "
				+ " SUM(b.dblDebitAmt), a.strReasonName,a.strRemark,a.strVoidType, a.strUserCreated "
				+ " FROM tblvoidbillhd a inner join tblvoidbilldtl b on a.strBillNo=b.strBillNo "
				+ " left outer join tblcheckindtl c on a.strCheckInNo=c.strCheckInNo "
				+ " left outer join tblroom d on a.strRoomNo=d.strRoomCode "
				+ " left outer join tblguestmaster e on c.strGuestCode=e.strGuestCode "
				+ " where c.strPayee='Y' AND a.strVoidType='fullVoid' or a.strVoidType='itemVoid' "
				+ " AND DATE(a.dteBillDate) BETWEEN '"
				+ fromDte
				+ "' AND '"
				+ toDte
				+ "' "
				+ " GROUP BY a.strBillNo,b.strPerticulars "
				+ " ORDER BY a.dteBillDate,a.strBillNo;";
		
		List listVoidBill = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listVoidBill.isEmpty()) {
			for (int i = 0; i < listVoidBill.size(); i++) {
				Object[] arr2 = (Object[]) listVoidBill.get(i);
				clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
				objBean.setStrBillNo(arr2[0].toString());
				objBean.setDteBillDate(arr2[1].toString());
				objBean.setStrGuestName(arr2[2].toString());
				objBean.setStrRoomDesc(arr2[3].toString());
				objBean.setStrPerticular(arr2[4].toString());
				objBean.setDblVoidDebitAmt(arr2[5].toString());
				objBean.setStrReasonDesc(arr2[6].toString());
				objBean.setStrRemark(arr2[7].toString());
				objBean.setStrVoidType(arr2[8].toString());
				objBean.setStrVoidUser(arr2[9].toString());
				listofVoidBillDtl.add(objBean);
			}
		}

		return listofVoidBillDtl;
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportSettlementWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportSettlementWisePMSSalesFlash(HttpServletRequest request)
	{    
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		String sql = "select c.strSettlementDesc,sum(b.dblSettlementAmt) "
				+ " from tblreceipthd a ,tblreceiptdtl b ,tblsettlementmaster c"
				+ " where a.strReceiptNo=b.strReceiptNo"
				+ " and date(a.dteReceiptDate)  between '"
				+ fromDte
				+ "' and '"
				+ toDte
				+ "' "
				+ " and b.strSettlementCode=c.strSettlementCode"
				+ " and a.strClientCode=b.strClientCode and b.strClientCode=c.strClientCode "
				+ " group by b.strSettlementCode;";

		List listSettlementDtl = objGlobalService.funGetListModuleWise(sql,"sql");
		if (!listSettlementDtl.isEmpty()) {
			for (int i = 0; i < listSettlementDtl.size(); i++) {
				Object[] arr2 = (Object[]) listSettlementDtl.get(i);
				List DataList = new ArrayList<>();
				DataList.add(arr2[0].toString());
				DataList.add(arr2[1].toString());
				detailList.add(DataList);
			}
		}
		headerList.add("Settlement Type");
		headerList.add("Settlement Amount");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		retList.add("SettlementWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
    }
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportRevenueHeadWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportRevenueHeadWisePMSSalesFlash(HttpServletRequest request)
	{    
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		HashMap<String, Double> hmRevenueType = new HashMap<String, Double>();
		String sql="select * from "
				+ " (SELECT b.strRevenueType as strRevenueType , SUM(b.dblDebitAmt) as Amount "
				+ " FROM tblbillhd a, tblbilldtl b WHERE a.strBillNo=b.strBillNo AND DATE(a.dteBillDate) BETWEEN '"
				+ fromDte
				+ "' AND '"
				+ toDte
				+ "' GROUP BY b.strRevenueType) c"
				+ " union"
				+ " select * from"
				+ " (SELECT b.strRevenueType as strRevenueType, SUM(b.dblDebitAmt) as Amount "
				+ " FROM tblfoliohd a,tblfoliodtl b"
				+ " WHERE a.strFolioNo=b.strFolioNo AND DATE(b.dteDocDate) BETWEEN '"
				+ fromDte + "' AND '" + toDte + "' "
				+ " GROUP BY b.strRevenueType ) d ";
		List listRevenueDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listRevenueDtl.isEmpty()) {
			for (int i = 0; i < listRevenueDtl.size(); i++) {
				Object[] arr2 = (Object[]) listRevenueDtl.get(i);
				if (hmRevenueType.containsKey(arr2[0].toString())) {
					double dblAmount = hmRevenueType.get(arr2[0].toString())
							+ Double.parseDouble(arr2[1].toString());
					hmRevenueType.put(arr2[0].toString(), dblAmount);

				} else {
					hmRevenueType.put(arr2[0].toString(),
							Double.parseDouble(arr2[1].toString()));
				}
			}
		}
		for (HashMap.Entry<String, Double> hmRevenue : hmRevenueType.entrySet()) {
			List DataList = new ArrayList<>();
			DataList.add(hmRevenue.getKey());
			DataList.add(hmRevenue.getValue());
			detailList.add(DataList);
		}
		
		headerList.add("Revenue Type");
		headerList.add("Amount");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		retList.add("RevenueHeadWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);

	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportTaxWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportTaxWisePMSSalesFlash(HttpServletRequest request)
	{    
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		String sql=" SELECT  IFNULL(c.strTaxDesc,''), IFNULL(SUM(c.dblTaxableAmt),0), IFNULL(SUM(c.dblTaxAmt),0) "
				+ " FROM tblbillhd a "
				+ " LEFT OUTER "
				+ " JOIN tblbilldtl b ON a.strBillNo=b.strBillNo "
				+ " LEFT OUTER "
				+ " JOIN tblbilltaxdtl c ON b.strDocNo=c.strDocNo AND b.strBillNo=c.strBillNo "
				+ " WHERE DATE(a.dteBillDate) BETWEEN '"
				+ fromDte
				+ "' AND '"
				+ toDte
				+ "' AND c.strTaxDesc!='' "
				+ " GROUP BY c.strTaxDesc;";
		List listTaxDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listTaxDtl.isEmpty()) {
			for (int i = 0; i < listTaxDtl.size(); i++) {
				Object[] arr2 = (Object[]) listTaxDtl.get(i);
				List DataList = new ArrayList<>();
				DataList.add(arr2[0].toString());
				DataList.add(arr2[1].toString());
				DataList.add(arr2[2].toString());
				detailList.add(DataList);
			}
		}

		headerList.add("Tax Description");
		headerList.add("Taxable Amount");
		headerList.add("Tax Amount");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		retList.add("TaxWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportExpectedArrWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportExpectedArrWisePMSSalesFlash(HttpServletRequest request)
	{    
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		String sql="SELECT a.strReservationNo,  DATE_FORMAT(a.dteDateCreated,'%d-%m-%Y'),CONCAT(e.strFirstName,' ',e.strMiddleName,' ',e.strLastName),   IFNULL(d.dblReceiptAmt,0), DATE_FORMAT(a.dteArrivalDate,'%d-%m-%Y'), DATE_FORMAT(a.dteDepartureDate,'%d-%m-%Y') "
				+ " FROM tblreservationhd a "
				+ " LEFT OUTER JOIN tblreservationdtl b ON a.strReservationNo=b.strReservationNo "
				+ " LEFT OUTER JOIN tblbookingtype c ON a.strBookingTypeCode=c.strBookingTypeCode "
				+ " LEFT OUTER JOIN tblreceipthd d ON a.strReservationNo=d.strRegistrationNo "
				+ " LEFT OUTER JOIN tblguestmaster e ON e.strGuestCode=b.strGuestCode "
				+ " WHERE DATE(a.dteArrivalDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode=b.strClientCode "
				+ " AND a.strReservationNo NOT IN (SELECT strReservationNo FROM tblcheckinhd) ;";
		List listArrivalDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listArrivalDtl.isEmpty()) {
			for (int i = 0; i < listArrivalDtl.size(); i++) {
				Object[] arr2 = (Object[]) listArrivalDtl.get(i);
				List DataList = new ArrayList<>();
			    DataList.add(arr2[0].toString());
			    DataList.add(arr2[1].toString());
			    DataList.add(arr2[2].toString());
			    DataList.add(arr2[3].toString());
			    DataList.add(arr2[4].toString());
			    DataList.add(arr2[5].toString());
				detailList.add(DataList);

			}
		}
		headerList.add("Reservation No");
		headerList.add("Reservation Date");
		headerList.add("Guest Name");
		headerList.add("Receipt Amount");
		headerList.add("Arrival Date");
		headerList.add("Departure Date");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		retList.add("ExpectedArrWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportExpectedDeptWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportExpectedDeptWisePMSSalesFlash(HttpServletRequest request)
	{    
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		String sql="SELECT a.strCheckInNo,a.strType, DATE(a.dteDepartureDate),c.strRoomDesc,c.strRoomTypeDesc, CONCAT(d.strFirstName,' ',d.strMiddleName,' ',d.strLastName) "
                  +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d,tblbillhd e "
                  +" WHERE a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode "
                  +" AND b.strGuestCode=d.strGuestCode "
                  +" AND DATE(a.dteCheckInDate) BETWEEN '"+fromDte+"' AND '"+toDte+"';";
		List listExpectedDeptDtl=objGlobalService.funGetListModuleWise(sql,"sql");
		if(!listExpectedDeptDtl.isEmpty())
		{
			for(int i=0;i<listExpectedDeptDtl.size();i++)
			{
				Object[] arr2=(Object[]) listExpectedDeptDtl.get(i);
				List DataList = new ArrayList<>();
				DataList.add(arr2[0].toString());
				DataList.add(arr2[1].toString());
				DataList.add(arr2[2].toString());
				DataList.add(arr2[3].toString());
				DataList.add(arr2[4].toString());
				DataList.add(arr2[5].toString());
				detailList.add( DataList);
				
			}
		}
		headerList.add("CheckIn No");
		headerList.add("Booking Type");
		headerList.add("Departure Date");
		headerList.add("Room Description");
		headerList.add("Room Type");
		headerList.add("Guest Name");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		retList.add("ExpectedDeptWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportCheckInWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportCheckInWisePMSSalesFlash(HttpServletRequest request)
	{    
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		String sql="SELECT a.strCheckInNo,a.strType, DATE(a.dteArrivalDate),c.strRoomDesc,c.strRoomTypeDesc,CONCAT(d.strFirstName,' ',d.strMiddleName,' ',d.strLastName), "
                 +" a.tmeArrivalTime"
				 +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d,tblbillhd e "
                 +" WHERE DATE(a.dteCheckInDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode AND b.strGuestCode=d.strGuestCode "
                 +" AND a.strCheckInNo=e.strCheckInNo ;";
		List listCheckInDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listCheckInDtl.isEmpty()) {
			for (int i = 0; i < listCheckInDtl.size(); i++) {
				Object[] arr2 = (Object[]) listCheckInDtl.get(i);
				List DataList = new ArrayList<>();
				DataList.add(arr2[0].toString());
				DataList.add(arr2[5].toString());
				DataList.add(arr2[2].toString());
				DataList.add(arr2[3].toString());
				DataList.add(arr2[4].toString());
				DataList.add(arr2[1].toString());
				DataList.add(arr2[6].toString());
				detailList.add(DataList);

			}
		}
		headerList.add("CheckIn No");
		headerList.add("Guest Name");
		headerList.add("CheckIn Date");
		headerList.add("Room Description");
		headerList.add("Room Type");
		headerList.add("Booking Type");
		headerList.add("Arrival Time");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		retList.add("CheckInWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportCheckOutWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportCheckOutWisePMSSalesFlash(HttpServletRequest request)
	{    
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		String sql="SELECT a.strCheckInNo,a.strType , DATE(a.dteDepartureDate),c.strRoomDesc,c.strRoomTypeDesc,CONCAT(d.strFirstName,' ',d.strMiddleName,' ',d.strLastName),"
				  +" e.dblGrandTotal"
                +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d,tblbillhd e "
                +" WHERE a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode AND b.strGuestCode=d.strGuestCode "
                +" AND a.strCheckInNo=e.strCheckInNo AND DATE(a.dteCheckInDate) BETWEEN '"+fromDte+"' AND '"+toDte+"';";
		List listCheckOutDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listCheckOutDtl.isEmpty()) {
			for (int i = 0; i < listCheckOutDtl.size(); i++) {
			    Object[] arr2=(Object[]) listCheckOutDtl.get(i);
			    List DataList = new ArrayList<>();
			    DataList.add(arr2[0].toString());
			    DataList.add(arr2[1].toString());
			    DataList.add(arr2[2].toString());
			    DataList.add(arr2[3].toString());
			    DataList.add(arr2[4].toString());
			    DataList.add(arr2[5].toString());
			    DataList.add(arr2[6].toString());
				detailList.add(DataList);

			}
		}
		headerList.add("CheckIn No");
		headerList.add("Booking Type");
		headerList.add("Departure Date");
		headerList.add("Room Description");
		headerList.add("Room Type");
		headerList.add("Guest Name");
		headerList.add("Grand Total");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		retList.add("CheckOutWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportCancelationWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportCancelationWisePMSSalesFlash(HttpServletRequest request)
	{    
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		String sql = "SELECT a.strReservationNo, CONCAT(c.strFirstName,' ',c.strMiddleName,' ',c.strLastName) AS strGuestName, e.strBookingTypeDesc,h.strRoomTypeDesc,DATE_FORMAT(b.dteReservationDate,'%d-%m-%Y') AS dteReservationDate,DATE_FORMAT(a.dteCancelDate,'%d-%m-%Y') AS dteCancelDate,f.strRoomDesc, g.strReasonDesc, a.strRemarks "
				+ " FROM tblroomcancelation a,tblreservationhd b,tblguestmaster c,tblreservationdtl d,tblbookingtype e,tblroom f, tblreasonmaster g,tblroomtypemaster h "
				+ " WHERE DATE(a.dteCancelDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strReservationNo=b.strReservationNo AND b.strCancelReservation='Y' AND b.strReservationNo=d.strReservationNo "
				+ " AND d.strGuestCode=c.strGuestCode AND b.strBookingTypeCode = e.strBookingTypeCode AND d.strRoomType=f.strRoomTypeCode "
				+ " AND a.strReasonCode=g.strReasonCode AND a.strClientCode=b.strClientCode AND h.strRoomTypeCode=d.strRoomType "
				+ " GROUP BY b.strReservationNo,d.strGuestCode ;";
		List listCancelationDtl = objGlobalService.funGetListModuleWise(sql,"sql");

		if (!listCancelationDtl.isEmpty()) {
			for (int i = 0; i < listCancelationDtl.size(); i++) {
				Object[] arr2 = (Object[]) listCancelationDtl.get(i);
				List DataList = new ArrayList<>();
				DataList.add(arr2[0].toString());
				DataList.add(arr2[1].toString());
				DataList.add(arr2[2].toString());
				DataList.add(arr2[3].toString());
				DataList.add(arr2[4].toString());
				DataList.add(arr2[5].toString());
				DataList.add(arr2[6].toString());
				DataList.add(arr2[7].toString());
				DataList.add(arr2[8].toString());
				detailList.add(DataList);
			}
		}
	
		headerList.add("Reservation No");
		headerList.add("Guest Name");
		headerList.add("Booking type");
		headerList.add("Room Type");
		headerList.add("Cancel Date");
		headerList.add("Room Description");
		headerList.add("Reason");
		headerList.add("Remark");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		retList.add("CancelationWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportNoShowWiseWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funNoShowWisePMSSalesFlash(HttpServletRequest request)
	{    
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		String sql = "SELECT CONCAT(c.strFirstName,' ',c.strMiddleName,' ',c.strLastName),a.strReservationNo,a.strNoRoomsBooked, IFNULL(b.dblReceiptAmt,0) "
				+ " from tblreservationhd a left outer join tblreceipthd b "
				+ " on a.strReservationNo=b.strReservationNo,tblguestmaster c,tblreservationdtl d "
				+ " where  a.strReservationNo=d.strReservationNo and d.strGuestCode=c.strGuestCode "
				+ " and date(a.dteArrivalDate) between '"
				+ fromDte
				+ "' and '"
				+ toDte
				+ "' and "
				+ " date(a.dteDepartureDate) between '"
				+ fromDte
				+ "' and '"
				+ toDte
				+ "' "
				+ " and  a.strReservationNo Not IN(select strReservationNo from tblcheckinhd )";
		List listNoShowDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listNoShowDtl.isEmpty()) {
			for (int i = 0; i < listNoShowDtl.size(); i++) {
				Object[] arr2 = (Object[]) listNoShowDtl.get(i);
				List DataList = new ArrayList<>();
				DataList.add(arr2[0].toString());
				DataList.add(arr2[1].toString());
				DataList.add(arr2[2].toString());
				DataList.add(arr2[3].toString());
				detailList.add(DataList);
			}
		}

		headerList.add("Guest Name");
		headerList.add("Reservation No");
		headerList.add("No of Rooms");
		headerList.add("Payment");
		
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		retList.add("NoShowWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportVoidBillWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funVoidBillWisePMSSalesFlash(HttpServletRequest request)
	{    
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		String sql = "SELECT a.strBillNo, DATE_FORMAT(a.dteBillDate,'%d-%m-%Y'),CONCAT(e.strGuestPrefix,\" \",e.strFirstName,\" \",e.strLastName) AS gName,d.strRoomDesc,b.strPerticulars, "
				+ " SUM(b.dblDebitAmt), a.strReasonName,a.strRemark,a.strVoidType, a.strUserCreated "
				+ " FROM tblvoidbillhd a inner join tblvoidbilldtl b on a.strBillNo=b.strBillNo "
				+ " left outer join tblcheckindtl c on a.strCheckInNo=c.strCheckInNo "
				+ " left outer join tblroom d on a.strRoomNo=d.strRoomCode "
				+ " left outer join tblguestmaster e on c.strGuestCode=e.strGuestCode "
				+ " where c.strPayee='Y' AND a.strVoidType='fullVoid' or a.strVoidType='itemVoid' "
				+ " AND DATE(a.dteBillDate) BETWEEN '"
				+ fromDte
				+ "' AND '"
				+ toDte
				+ "' "
				+ " GROUP BY a.strBillNo,b.strPerticulars "
				+ " ORDER BY a.dteBillDate,a.strBillNo;";
		
		List listVoidBill = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listVoidBill.isEmpty()) {
			for (int i = 0; i < listVoidBill.size(); i++) {
				Object[] arr2 = (Object[]) listVoidBill.get(i);
				List DataList = new ArrayList<>();
				DataList.add(arr2[0].toString());
				DataList.add(arr2[1].toString());
				DataList.add(arr2[2].toString());
				DataList.add(arr2[3].toString());
				DataList.add(arr2[4].toString());
				DataList.add(arr2[5].toString());
				DataList.add(arr2[6].toString());
				DataList.add(arr2[7].toString());
				DataList.add(arr2[8].toString());
				DataList.add(arr2[9].toString());
				detailList.add(DataList);
			}
		}
		headerList.add("Bill No");
		headerList.add("Bill Date");
		headerList.add("Guest Name");
		headerList.add("Room Description");
		headerList.add("Particular");
		headerList.add("Amount");
		headerList.add("Reason");
		headerList.add("Remark");
		headerList.add("Void Type");
		headerList.add("Void User");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		retList.add("VoidBillWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
}
