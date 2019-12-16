package com.sanguine.webpms.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.math.BigDecimal;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.crm.bean.clsSalesReturnBean;
import com.sanguine.model.clsCompanyMasterModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsPMSSalesFlashBean;
import com.sanguine.webpms.bean.clsRevenueHeadReportBean;

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
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		BigDecimal dblTotalValue = new BigDecimal(0);
	
		
		List<clsPMSSalesFlashBean> listofSettlementDtl = new ArrayList<clsPMSSalesFlashBean>();
		List listofSettlementTotal = new ArrayList<>();

		String sql = "select c.strSettlementDesc,sum(b.dblSettlementAmt) "
				+ " from tblreceipthd a ,tblreceiptdtl b ,tblsettlementmaster c"
				+ " where a.strReceiptNo=b.strReceiptNo"
				+ " and date(a.dteReceiptDate)  between '"
				+ fromDte
				+ "' and '"
				+ toDte
				+ "' "
				+ " and b.strSettlementCode=c.strSettlementCode"
				+ " and a.strClientCode=b.strClientCode and b.strClientCode='"+strClientCode+"' AND c.strClientCode='"+strClientCode+"'"
				+ " group by b.strSettlementCode;";

		List listSettlementDtl = objGlobalService.funGetListModuleWise(sql,"sql");
		if (!listSettlementDtl.isEmpty()) {
			for (int i = 0; i < listSettlementDtl.size(); i++) {
				Object[] arr2 = (Object[]) listSettlementDtl.get(i);
				clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
				objBean.setStrSettlementDesc(arr2[0].toString());
				objBean.setDblSettlementAmt(arr2[1].toString());
				listofSettlementDtl.add(objBean);
				dblTotalValue = new BigDecimal(Double.parseDouble(arr2[1].toString())).add(dblTotalValue);
			}
		}
		listofSettlementTotal.add(listofSettlementDtl);
		listofSettlementTotal.add(dblTotalValue);
		return listofSettlementTotal;
	}

	@RequestMapping(value = "/loadRevenueHeadWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funLoadRevenueHeadWiseDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTaxTotalValue = new BigDecimal(0);

		List<clsPMSSalesFlashBean> listofRevenueDtl = new ArrayList<clsPMSSalesFlashBean>();
		List listofRevenueHeadTotal = new ArrayList<>();
		HashMap<String,clsPMSSalesFlashBean> hmRevenueType = new HashMap<String,clsPMSSalesFlashBean >();
		
		
		String sql=" select * from  "
                  +" (select a.strRevenueType AS strRevenueType,sum(a.Amount),sum(b.TAXAMT) from (SELECT a.strBillNo,b.strDocNo ,b.strRevenueType AS strRevenueType, sum(b.dblDebitAmt) AS Amount "
                  +" FROM tblbillhd a, tblbilldtl b "
                  +" WHERE a.strBillNo=b.strBillNo  AND DATE(a.dteBillDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
                  +" GROUP BY a.strBillNo ,b.strDocNo) a, "
                  +" (select a.strBillNo,b.strDocNo,sum(b.dblTaxAmt) AS TAXAMT  "
                  +" from tblbillhd a , tblbilltaxdtl b "
                  +" WHERE a.strBillNo=b.strBillNo  AND DATE(a.dteBillDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
                  +" GROUP BY a.strBillNo,b.strDocNo) b "
                  +" where a.strBillNo=b.strBillNo AND a.strDocNo=b.strDocNo group by  a.strRevenueType)  c "
                  +" UNION select * from  "
                  +" (select a.strRevenueType AS strRevenueType,sum(a.Amount),sum(b.TAXAMT) from (SELECT a.strFolioNo,b.strDocNo,b.strRevenueType AS strRevenueType, SUM(b.dblDebitAmt) AS Amount "
                  +" FROM tblfoliohd a,tblfoliodtl b "
                  +" WHERE a.strFolioNo=b.strFolioNo     AND DATE(b.dteDocDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
                  +" GROUP BY b.strRevenueType)  a , "
                  +" (select a.strFolioNo,b.strDocNo,sum(b.dblTaxAmt) AS TAXAMT from tblfoliodtl a ,tblfoliotaxdtl b "
                  +" where a.strFolioNo=b.strFolioNo and DATE(a.dteDocDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
                  +" GROUP BY a.strFolioNo,b.strDocNo) b "
                  +" where a.strFolioNo=b.strFolioNo and a.strDocNo=b.strDocNo group by a.strRevenueType )  d ; ";
		
		List listRevenueDtl=objGlobalService.funGetListModuleWise(sql, "sql");
	
		if(!listRevenueDtl.isEmpty())
		{
			for(int i=0;i< listRevenueDtl.size();i++)
			{
				Object[] arr2=(Object[]) listRevenueDtl.get(i);
				clsPMSSalesFlashBean objBean=new clsPMSSalesFlashBean();
				
				if(hmRevenueType.containsKey(arr2[0].toString()))
				{
					objBean=hmRevenueType.get(arr2[0].toString());
			        double newAmount=objBean.getDblAmount() + Double.parseDouble(arr2[1].toString());
			        objBean.setDblAmount(newAmount);
			        double newTaxAmt=objBean.getDblTaxAmount() + Double.parseDouble(arr2[2].toString());
			        objBean.setDblTaxAmount(newTaxAmt);
			        hmRevenueType.put(arr2[0].toString(), objBean);
				}
				else
				{
					objBean.setDblAmount(Double.parseDouble(arr2[1].toString()));
					objBean.setDblTaxAmount(Double.parseDouble(arr2[2].toString()));
					hmRevenueType.put(arr2[0].toString(),objBean);
				}
				dblTotalValue = new BigDecimal(Double.parseDouble(arr2[1].toString())).add(dblTotalValue);
				
				dblTaxTotalValue =  new BigDecimal(Double.parseDouble(arr2[2].toString())).add(dblTaxTotalValue);
			}
		}
		
		for(HashMap.Entry<String,clsPMSSalesFlashBean> hmRevenue : hmRevenueType.entrySet() )
		{
			clsPMSSalesFlashBean objBean=new clsPMSSalesFlashBean();
			objBean.setStrRevenueType(hmRevenue.getKey());
		    clsPMSSalesFlashBean obj=hmRevenue.getValue();
		    double amount=obj.getDblAmount();
		    objBean.setDblAmount(amount);
		    double taxAmount=obj.getDblTaxAmount();
		    objBean.setDblTaxAmount(taxAmount);
		    listofRevenueDtl.add(objBean);
		    
			
		}
		listofRevenueHeadTotal.add(listofRevenueDtl);
		listofRevenueHeadTotal.add(dblTotalValue);
		listofRevenueHeadTotal.add(dblTaxTotalValue);
		return listofRevenueHeadTotal;
	}

	@RequestMapping(value = "/loadTaxWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funTaxWiseDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTaxTotalValue = new BigDecimal(0);

		List<clsPMSSalesFlashBean> listofTaxDtl = new ArrayList<clsPMSSalesFlashBean>();
		List listofTaxTotal = new ArrayList<>();
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
				+ "' AND c.strTaxDesc!='' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' AND c.strClientCode='"+strClientCode+"' "
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
                dblTotalValue = new BigDecimal(Double.parseDouble(arr2[1].toString())).add(dblTotalValue);
				dblTaxTotalValue =  new BigDecimal(arr2[2].toString()).add(dblTaxTotalValue);
			}
		}
		listofTaxTotal.add(listofTaxDtl);
		listofTaxTotal.add(dblTotalValue);
		listofTaxTotal.add(dblTaxTotalValue);

		return listofTaxTotal;
	}

	@RequestMapping(value = "/loadExpectedArrWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funExpectedArrWiseDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		BigDecimal dblTotalValue = new BigDecimal(0);
		

		List<clsPMSSalesFlashBean> listofExpectedArrDtl = new ArrayList<clsPMSSalesFlashBean>();
		List listofExpectedArrTotal = new ArrayList<>();
		String sql = "SELECT a.strReservationNo,  DATE_FORMAT(a.dteDateCreated,'%d-%m-%Y'),CONCAT(e.strFirstName,' ',e.strMiddleName,' ',e.strLastName),    DATE_FORMAT(a.dteDepartureDate,'%d-%m-%Y'), DATE_FORMAT(a.dteArrivalDate,'%d-%m-%Y'), IFNULL(d.dblReceiptAmt,0) "
				+ " FROM tblreservationhd a "
				+ " LEFT OUTER JOIN tblreservationdtl b ON a.strReservationNo=b.strReservationNo "
				+ " LEFT OUTER JOIN tblbookingtype c ON a.strBookingTypeCode=c.strBookingTypeCode "
				+ " LEFT OUTER JOIN tblreceipthd d ON a.strReservationNo=d.strRegistrationNo "
				+ " LEFT OUTER JOIN tblguestmaster e ON e.strGuestCode=b.strGuestCode "
				+ " WHERE DATE(a.dteArrivalDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode=b.strClientCode "
				+ "AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' AND c.strClientCode='"+strClientCode+"' AND d.strClientCode='"+strClientCode+"' "
				+ "AND e.strClientCode='"+strClientCode+"' AND a.strReservationNo NOT IN (SELECT strReservationNo FROM tblcheckinhd) ;";

		List listArrivalDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listArrivalDtl.isEmpty()) {
			for (int i = 0; i < listArrivalDtl.size(); i++) {
				Object[] arr2 = (Object[]) listArrivalDtl.get(i);
				clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
				objBean.setStrReservationNo(arr2[0].toString());
				objBean.setDteReservationDate(arr2[1].toString());
				objBean.setStrGuestName(arr2[2].toString());
				objBean.setDteDepartureDate(arr2[3].toString());
				objBean.setDteArrivalDate(arr2[4].toString());
				objBean.setDblReceiptAmt(arr2[5].toString());
				listofExpectedArrDtl.add(objBean);
				dblTotalValue = new BigDecimal(Double.parseDouble(arr2[5].toString())).add(dblTotalValue);

			}
		}
		listofExpectedArrTotal.add(listofExpectedArrDtl);
		listofExpectedArrTotal.add(dblTotalValue);
		return listofExpectedArrTotal;
	}

	@RequestMapping(value = "/loadExpectedDeptWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funExpectedDeptWiseDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listofExpectedDeptDtl = new ArrayList<clsPMSSalesFlashBean>();
		String sql="SELECT a.strCheckInNo,a.strType, DATE(a.dteDepartureDate),c.strRoomDesc,c.strRoomTypeDesc,"
				  +" CONCAT(d.strFirstName,' ',d.strMiddleName,'',d.strLastName) "
                  +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d "
                  +" WHERE a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode AND b.strGuestCode=d.strGuestCode "
                  + "AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' AND c.strClientCode='"+strClientCode+"' "
                  + "AND d.strClientCode='"+strClientCode+"'"
                  +" AND DATE(a.dteDepartureDate) BETWEEN '"+fromDte+"' AND '"+toDte+"';";
		
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
			String strClientCode = request.getSession().getAttribute("clientCode").toString();
		    String fromDate = request.getParameter("frmDte").toString();
			String[] arr = fromDate.split("-");
			String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
			String toDate = request.getParameter("toDte").toString();
			String[] arr1 = toDate.split("-");
			String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

			List<clsPMSSalesFlashBean> listofCheckInDtl = new ArrayList<clsPMSSalesFlashBean>();
			String sql="SELECT a.strCheckInNo,a.strType, DATE(a.dteArrivalDate),c.strRoomDesc,c.strRoomTypeDesc, "
                      +" CONCAT(d.strFirstName,'', d.strMiddleName,'',d.strLastName), a.tmeArrivalTime "
                      +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d "
                      +" WHERE a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode "
                      +" AND b.strGuestCode=d.strGuestCode "
                      + "AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' AND c.strClientCode='"+strClientCode+"' "
                      + "AND d.strClientCode='"+strClientCode+"'"
                      + "AND DATE(a.dteCheckInDate) BETWEEN '"+fromDte+"' AND '"+toDte+"';";
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
			String strClientCode = request.getSession().getAttribute("clientCode").toString();
		    String fromDate = request.getParameter("frmDte").toString();
			String[] arr = fromDate.split("-");
			String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
			String toDate = request.getParameter("toDte").toString();
			String[] arr1 = toDate.split("-");
			String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
			BigDecimal dblTotalValue = new BigDecimal(0);
			
			List<clsPMSSalesFlashBean> listofCheckOutDtl = new ArrayList<clsPMSSalesFlashBean>();
			List listofCheckOutTotal = new ArrayList<>();

			String sql="SELECT a.strCheckInNo,a.strType, DATE(a.dteDepartureDate),c.strRoomDesc,c.strRoomTypeDesc, "
                      +" CONCAT(d.strFirstName,'', d.strMiddleName,'',d.strLastName), e.dblGrandTotal "
                      +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d,tblbillhd e "
                      +" WHERE a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode AND b.strGuestCode=d.strGuestCode " 
                      +" AND  a.strCheckInNo=e.strCheckInNo AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' "
                      + "AND c.strClientCode='"+strClientCode+"' AND d.strClientCode='"+strClientCode+"' AND e.strClientCode='"+strClientCode+"' "
                      + "AND DATE(a.dteDepartureDate) BETWEEN '"+fromDte+"' AND '"+toDte+"';";
			List listCheckOutDtl = objGlobalService.funGetListModuleWise(sql, "sql");
			if (!listCheckOutDtl.isEmpty()) {
				for (int i = 0; i < listCheckOutDtl.size(); i++) {
				    Object[] arr2=(Object[]) listCheckOutDtl.get(i);
					clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
					objBean.setStrBillNo(arr2[0].toString());
					objBean.setStrBookingType(arr2[1].toString());
					objBean.setDteDepartureDate(arr2[2].toString());
					objBean.setStrRoomDesc(arr2[3].toString());
					objBean.setStrRoomType(arr2[4].toString());
					objBean.setStrGuestName(arr2[5].toString());
					objBean.setDblGrandTotal(arr2[6].toString());
					listofCheckOutDtl.add(objBean);
					dblTotalValue = new BigDecimal(Double.parseDouble(arr2[6].toString())).add(dblTotalValue);
				}
			}
			 listofCheckOutTotal.add(listofCheckOutDtl); 
			 listofCheckOutTotal.add(dblTotalValue); 
			return listofCheckOutTotal;
	
	}
	
	@RequestMapping(value = "/loadCancelationWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funCancelationDtl(HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String ClientCode = request.getSession().getAttribute("clientCode").toString();
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
				+ "AND a.strClientCode='"+ClientCode+"' AND b.strClientCode='"+ClientCode+"' AND c.strClientCode='"+ClientCode+"' "
				+ "AND d.strClientCode='"+ClientCode+"' AND e.strClientCode='"+ClientCode+"' AND f.strClientCode='"+ClientCode+"' "
				+ "AND g.strClientCode='"+ClientCode+"' AND h.strClientCode='"+ClientCode+"' "
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
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
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
				+ " AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' "
				+ "AND c.strClientCode='"+strClientCode+"' AND d.strClientCode='"+strClientCode+"'"
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
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listofVoidBillDtl = new ArrayList<clsPMSSalesFlashBean>();
		String sql = "SELECT a.strBillNo, DATE_FORMAT(a.dteBillDate,'%d-%m-%Y'),CONCAT(e.strGuestPrefix,\" \",e.strFirstName,\" \",e.strLastName) AS gName,d.strRoomDesc,b.strPerticulars, "
				+ " SUM(b.dblDebitAmt), a.strReasonName,a.strRemark,a.strVoidType, a.strUserCreated "
				+ " FROM tblvoidbillhd a inner join tblvoidbilldtl b on a.strBillNo=b.strBillNo AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
				+ " left outer join tblcheckindtl c on a.strCheckInNo=c.strCheckInNo AND c.strClientCode='"+strClientCode+"'"
				+ " left outer join tblroom d on a.strRoomNo=d.strRoomCode AND d.strClientCode='"+strClientCode+"'"
				+ " left outer join tblguestmaster e on c.strGuestCode=e.strGuestCode  AND e.strClientCode='"+strClientCode+"'"
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
	
	@RequestMapping(value = "/loadPaymentForSalesFlash", method = RequestMethod.GET)
	public @ResponseBody List funPaymentDtl(HttpServletRequest request) {
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listPayment = new ArrayList<clsPMSSalesFlashBean>();
		String sql = "SELECT a.strReceiptNo, DATE(a.dteReceiptDate),"
				+ "Concat(d.strFirstName,' ',d.strMiddleName,' ',d.strLastName),"
				+ "a.strAgainst,e.strSettlementDesc,a.dblReceiptAmt "
				+ "FROM tblreceipthd a,tblreceiptdtl b,tblcheckindtl c,tblguestmaster d,tblsettlementmaster e "
				+ "WHERE a.strReceiptNo=b.strReceiptNo and a.strCheckInNo=c.strCheckInNo "
				+ "and c.strGuestCode=d.strGuestCode and b.strSettlementCode=e.strSettlementCode "
				+ "AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' "
				+ "AND c.strClientCode='"+strClientCode+"' AND d.strClientCode='"+strClientCode+"' "
				+ "AND e.strClientCode='"+strClientCode+"' "
				+ "AND DATE(a.dteReceiptDate) BETWEEN '"+fromDte+"' "
				+ "AND '"+toDte+"';";
		
		List listVoidBill = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listVoidBill.isEmpty()) {
			for (int i = 0; i < listVoidBill.size(); i++) {
				Object[] arr2 = (Object[]) listVoidBill.get(i);
				clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
				
				objBean.setStrReceiptNo(arr2[0].toString());
				objBean.setDteReceiptDate(objGlobal.funGetDate("dd-MM-yyyy",arr2[1].toString()));
				objBean.setStrGuestName(arr2[2].toString());
				objBean.setStrType(arr2[3].toString());
				objBean.setStrSettlement(arr2[4].toString());
				objBean.setDblAmount(Double.parseDouble(arr2[5].toString()));
				
				
				listPayment.add(objBean);
			}
		}

		return listPayment;
	}
	
	@RequestMapping(value = "/loadBillPrintingForSalesFlash", method = RequestMethod.GET)
	public @ResponseBody List funBillPrintingDtl(HttpServletRequest request) {
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];

		List<clsPMSSalesFlashBean> listPayment = new ArrayList<clsPMSSalesFlashBean>();
		String sql = "select a.strBillNo,Date(a.dteBillDate),c.strRoomDesc,"
				+ "Concat(e.strFirstName,' ',e.strMiddleName,' ',e.strLastName),a.dblGrandTotal,g.dblDiscount,f.strCheckInNo "
				+ "from tblbillhd a ,tblbilldtl b,tblroom c,tblcheckindtl d,tblguestmaster e,tblcheckinhd f,tblwalkinroomratedtl g "
				+ "where a.strBillNo=b.strBillNo "
				+ "and a.strRoomNo=c.strRoomCode and a.strCheckInNo=d.strCheckInNo and d.strGuestCode=e.strGuestCode "
				+ "and d.strCheckInNo=f.strCheckInNo "
				+ "and f.strWalkInNo=g.strWalkinNo "
				+ "and Date(a.dteBillDate) between '"+fromDte+"' and '"+toDte+"' and a.strClientCode='"+strClientCode+"' group by a.strBillNo";
		
		List listVoidBill = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listVoidBill.isEmpty()) {
			for (int i = 0; i < listVoidBill.size(); i++) {
				Object[] arr2 = (Object[]) listVoidBill.get(i);
				clsPMSSalesFlashBean objBean = new clsPMSSalesFlashBean();
				String strPerticular = "";
				
				objBean.setStrBillNo(arr2[0].toString());
				objBean.setDteBillDate(objGlobal.funGetDate("dd-MM-yyyy",arr2[1].toString()));
				objBean.setStrRoomDesc(arr2[2].toString());
				objBean.setStrGuestName(arr2[3].toString());
				objBean.setDblGrndTotal(Double.parseDouble(arr2[4].toString()));
				//objBean.setDblDiscount(Double.parseDouble(arr2[5].toString()));
				objBean.setStrCheckInNo(arr2[6].toString());
				
				String sqlDisc = "select sum(a.dblDebitAmt) from tblbilldtl a where a.strBillNo='"+arr2[0].toString()+"' and a.strPerticulars='Room Tariff' and a.strClientCode='"+strClientCode+"'";
				List listDisc = objGlobalService.funGetListModuleWise(sqlDisc, "sql");
				if(listDisc!=null && listDisc.size()>0)
				{
					double dblDiscAmt = Double.parseDouble(listDisc.get(0).toString());
					dblDiscAmt = (dblDiscAmt*Double.parseDouble(arr2[5].toString())/100);
					objBean.setDblDiscount(dblDiscAmt);
				}
				
				String sqlTaxAmt = "select sum(a.dblTaxAmt) from tblbilltaxdtl a where a.strBillNo='"+arr2[0].toString()+"' and a.strTaxCode like 'TC%' and a.strClientCode='"+strClientCode+"'";
				List listTaxAmt = objGlobalService.funGetListModuleWise(sqlTaxAmt, "sql");
				if(listTaxAmt!=null && listTaxAmt.size()>0)
				{
					objBean.setDblTaxAmount(Double.parseDouble(listTaxAmt.get(0).toString()));
				}

				String sqlAdvanceAmt = "select a.dblReceiptAmt from tblreceipthd a where a.strCheckInNo='"+arr2[6].toString()+"' "
						+ "and a.strAgainst='Check-In' and a.strClientCode='"+strClientCode+"';";
				List listAdvAmt = objGlobalService.funGetListModuleWise(sqlAdvanceAmt, "sql");
				if(listAdvAmt!=null && listAdvAmt.size()>0)
				{
					objBean.setDblAdvanceAmount(Double.parseDouble(listAdvAmt.get(0).toString()));
				}
				else
				{
					objBean.setDblAdvanceAmount(0);
				}
				
				String sqlPerticulars = "select a.strPerticulars from tblbilldtl a where a.strBillNo='"+objBean.getStrBillNo()+"' and a.strClientCode='"+strClientCode+"'";
				List listPerticulars = objGlobalService.funGetListModuleWise(sqlPerticulars, "sql");
				if(listPerticulars!=null && listPerticulars.size()>0)
				{
					for(int p=0;p<listPerticulars.size();p++)
					{
						strPerticular = strPerticular+listPerticulars.get(p).toString()+",";
					}
					objBean.setStrPerticular(strPerticular);
				}
				listPayment.add(objBean);
			}
		}

		return listPayment;
	}
	
	
	@RequestMapping(value = "/loadHousekeepingSummary", method = RequestMethod.GET)
	public @ResponseBody Map funloadHousekeepingSummary(HttpServletRequest request) throws ParseException {
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		String PMSDate=request.getSession().getAttribute("PMSDate").toString();
		List listRoomCLeanCheck =  new ArrayList();
		Map<String,List> mapHousekeepingSummary = new HashMap<String, List>();
		DateFormat formatter ; 
		List<Date> dates = new ArrayList<Date>();
		clsPMSSalesFlashBean objBean = null;
		TreeSet listDatesHeader = new TreeSet();
		List listRoomWise = new ArrayList();
		//Taking all rooms from tblroom
		
		String sqlRoom = "select a.strRoomCode,a.strRoomDesc from tblroom a where a.strClientCode='"+strClientCode+"'"; 

		List listMain = new ArrayList();
		List listRoomNo= objGlobalService.funGetListModuleWise(sqlRoom, "sql");
		for(int r = 0;r<listRoomNo.size();r++)
		{
			listRoomWise = new ArrayList();
			Object[] arr2   = (Object[]) listRoomNo.get(r);
			listRoomWise.add(arr2[1]);
		int count=0;
		formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date  startDate = (Date)formatter.parse(fromDate); 
		Date  endDate = (Date)formatter.parse(toDate);
		long interval = 24*1000 * 60 * 60; // 1 hour in millis
		long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
		long curTime = startDate.getTime();
		
		while (curTime <= endTime) {
			objBean = new clsPMSSalesFlashBean();
			Date tempDate = new Date(curTime);
		    
			SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");  
		    String strDate= formatter2.format(tempDate);  
		    objBean.setDteDatesForHousekeeping(strDate);
		    curTime += interval;
		    
		    listDatesHeader.add(strDate);
		    
		    String sqlRoomCLeanCheck = "SELECT  IF(SPACE(b.strHouseKeepCode)=0,'Completed','Pending') "
		    		+ "FROM tblroomhousekeepdtl b where  DATE(b.dteDate)='"+objGlobal.funGetDate("yyyy-MM-dd",strDate)+"' and b.strRoomCode='"+arr2[0]+"' "
		    		+ "group by b.strRoomCode"; 
		    
		    listRoomCLeanCheck = objGlobalService.funGetListModuleWise(sqlRoomCLeanCheck, "sql");
		  
		    if(listRoomCLeanCheck!=null && listRoomCLeanCheck.size()>0)
    		{
		    	listRoomWise.add(listRoomCLeanCheck.get(0).toString());
    		}
		    else
		    {
		    	listRoomWise.add("Pending");
		    }
		  count++;
		   
		 }
		//listMain.add(listDatesHeader);
		listMain.add(listRoomWise);
	}
		List listDates = new ArrayList<>();
		
		Iterator<Integer> iterator = listDatesHeader.iterator(); 
		while (iterator.hasNext()) 
            {
				listDates.add(iterator.next());
            }
		mapHousekeepingSummary.put("data", listMain); 
		mapHousekeepingSummary.put("date", listDates); 
		
		
		
				
		return mapHousekeepingSummary;
	}
	
	private String funGetDayOfWeek(int day) {
		String dayOfWeek = "Sun";

		switch (day) {
		case 0:
			dayOfWeek = "Sun";
			break;

		case 1:
			dayOfWeek = "Sat";
			break;

		case 2:
			dayOfWeek = "Fir";
			break;

		case 3:
			dayOfWeek = "Thu";
			break;

		case 4:
			dayOfWeek = "Wed";
			break;

		case 5:
			dayOfWeek = "Tue";
			break;

		case 6:
			dayOfWeek = "Mon";
			break;
		}

		return dayOfWeek;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportSettlementWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportSettlementWisePMSSalesFlash(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		List totalsList = new ArrayList();
		totalsList.add("Total");
		
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		BigDecimal dblTotalValue = new BigDecimal(0);
		DecimalFormat df = new DecimalFormat("#.##");
		

		
		String sql = "select c.strSettlementDesc,sum(b.dblSettlementAmt) "
				+ " from tblreceipthd a ,tblreceiptdtl b ,tblsettlementmaster c"
				+ " where a.strReceiptNo=b.strReceiptNo"
				+ " and date(a.dteReceiptDate)  between '"
				+ fromDte
				+ "' and '"
				+ toDte
				+ "' "
				+ " and b.strSettlementCode=c.strSettlementCode"
				+ " and a.strClientCode=b.strClientCode and b.strClientCode=c.strClientCode AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' AND c.strClientCode='"+strClientCode+"'"
				+ " group by b.strSettlementCode;";

		List listSettlementDtl = objGlobalService.funGetListModuleWise(sql,"sql");
		if (!listSettlementDtl.isEmpty()) {
			for (int i = 0; i < listSettlementDtl.size(); i++) {
				Object[] arr2 = (Object[]) listSettlementDtl.get(i);
				List DataList = new ArrayList<>();
				DataList.add(arr2[0].toString());
				DataList.add(arr2[1].toString());
				detailList.add(DataList);
				dblTotalValue = new BigDecimal(df.format(Double.parseDouble(arr2[1].toString()))).add(dblTotalValue);
				
			}
		}
	
		totalsList.add(dblTotalValue);
		
		retList.add("SettlementWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("Settlement Wise Report");
		retList.add(titleData);
		
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
		filterData.add(toDate);
        retList.add(filterData); 
		
		headerList.add("Settlement Type");
		headerList.add("Settlement Amount");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		
		
		List blankList = new ArrayList();
	    detailList.add(blankList);// Blank Row at Bottom
	    detailList.add(totalsList);
			
        retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportRevenueHeadWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportRevenueHeadWisePMSSalesFlash(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		List totalsList = new ArrayList();
		totalsList.add("Total");
		
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTaxTotalValue = new BigDecimal(0);
		DecimalFormat df = new DecimalFormat("#.##");

		List<clsPMSSalesFlashBean> listofRevenueDtl = new ArrayList<clsPMSSalesFlashBean>();
		
		HashMap<String,clsPMSSalesFlashBean> hmRevenueType = new HashMap<String,clsPMSSalesFlashBean >();
		
		
		String sql=" select * from  "
                  +" (select a.strRevenueType AS strRevenueType,sum(a.Amount),sum(b.TAXAMT) from (SELECT a.strBillNo,b.strDocNo ,b.strRevenueType AS strRevenueType, sum(b.dblDebitAmt) AS Amount "
                  +" FROM tblbillhd a, tblbilldtl b "
                  +" WHERE a.strBillNo=b.strBillNo  AND DATE(a.dteBillDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
                  +" GROUP BY a.strBillNo ,b.strDocNo) a, "
                  +" (select a.strBillNo,b.strDocNo,sum(b.dblTaxAmt) AS TAXAMT  "
                  +" from tblbillhd a , tblbilltaxdtl b "
                  +" WHERE a.strBillNo=b.strBillNo  AND DATE(a.dteBillDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
                  +" GROUP BY a.strBillNo,b.strDocNo) b "
                  +" where a.strBillNo=b.strBillNo AND a.strDocNo=b.strDocNo group by  a.strRevenueType)  c "
                  +" UNION select * from  "
                  +" (select a.strRevenueType AS strRevenueType,sum(a.Amount),sum(b.TAXAMT) from (SELECT a.strFolioNo,b.strDocNo,b.strRevenueType AS strRevenueType, SUM(b.dblDebitAmt) AS Amount "
                  +" FROM tblfoliohd a,tblfoliodtl b "
                  +" WHERE a.strFolioNo=b.strFolioNo     AND DATE(b.dteDocDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
                  +" GROUP BY b.strRevenueType)  a , "
                  +" (select a.strFolioNo,b.strDocNo,sum(b.dblTaxAmt) AS TAXAMT from tblfoliodtl a ,tblfoliotaxdtl b "
                  +" where a.strFolioNo=b.strFolioNo and DATE(a.dteDocDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
                  +" GROUP BY a.strFolioNo,b.strDocNo) b "
                  +" where a.strFolioNo=b.strFolioNo and a.strDocNo=b.strDocNo group by a.strRevenueType )  d ; ";
		
		List listRevenueDtl=objGlobalService.funGetListModuleWise(sql, "sql");
	
		if(!listRevenueDtl.isEmpty())
		{
			for(int i=0;i< listRevenueDtl.size();i++)
			{
				Object[] arr2=(Object[]) listRevenueDtl.get(i);
				clsPMSSalesFlashBean objBean=new clsPMSSalesFlashBean();
				
				if(hmRevenueType.containsKey(arr2[0].toString()))
				{
					objBean=hmRevenueType.get(arr2[0].toString());
			        double newAmount=objBean.getDblAmount() + Double.parseDouble(arr2[1].toString());
			        objBean.setDblAmount(newAmount);
			        double newTaxAmt=objBean.getDblTaxAmount() + Double.parseDouble(arr2[2].toString());
			        objBean.setDblTaxAmount(newTaxAmt);
			        hmRevenueType.put(arr2[0].toString(), objBean);
				}
				else
				{
					objBean.setDblAmount(Double.parseDouble(arr2[1].toString()));
					objBean.setDblTaxAmount(Double.parseDouble(arr2[2].toString()));
					hmRevenueType.put(arr2[0].toString(),objBean);
				}
				dblTotalValue = new BigDecimal(df.format(Double.parseDouble(arr2[1].toString()))).add(dblTotalValue);
				
				dblTaxTotalValue =  new BigDecimal(df.format(Double.parseDouble(arr2[2].toString()))).add(dblTaxTotalValue);
			
			}
		}
		
		for(HashMap.Entry<String,clsPMSSalesFlashBean> hmRevenue : hmRevenueType.entrySet() )
		{
			
		   List DataList = new ArrayList<>();
		   DataList.add(hmRevenue.getKey());
		   clsPMSSalesFlashBean obj=hmRevenue.getValue();
		   double amount=obj.getDblAmount();
		   DataList.add(amount);
		  
		   double taxAmount=obj.getDblTaxAmount();
		   DataList.add(df.format(taxAmount));
		  
		   detailList.add(DataList);
		    
			
		}
		
		totalsList.add(dblTotalValue);
		totalsList.add(dblTaxTotalValue);
		retList.add("RevenueHeadWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("Revenue Head Wise Report");
		retList.add(titleData);
		
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
		filterData.add(toDate);
		retList.add(filterData); 

		headerList.add("Revenue Type");
		headerList.add("Amount");
		headerList.add("Tax Amount");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		
		List blankList = new ArrayList();
	    detailList.add(blankList);// Blank Row at Bottom
	    detailList.add(totalsList);
	    
		
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);

	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportTaxWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportTaxWisePMSSalesFlash(HttpServletRequest request)
	{   
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		List totalsList = new ArrayList();
		totalsList.add("Total");
		
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTaxTotalValue = new BigDecimal(0);
		DecimalFormat df = new DecimalFormat("#.##");
		String sql=" SELECT  IFNULL(c.strTaxDesc,''), IFNULL(SUM(c.dblTaxableAmt),0), IFNULL(SUM(c.dblTaxAmt),0) "
				+ " FROM tblbillhd a "
				+ " LEFT OUTER "
				+ " JOIN tblbilldtl b ON a.strBillNo=b.strBillNo  AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
				+ " LEFT OUTER "
				+ " JOIN tblbilltaxdtl c ON b.strDocNo=c.strDocNo AND b.strBillNo=c.strBillNo  AND c.strClientCode='"+strClientCode+"'"
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
				dblTotalValue = new BigDecimal(df.format(Double.parseDouble(arr2[1].toString()))).add(dblTotalValue);
				dblTaxTotalValue =  new BigDecimal(df.format(Double.parseDouble(arr2[2].toString()))).add(dblTaxTotalValue);
			}
		}
        totalsList.add(dblTotalValue);
        totalsList.add(dblTaxTotalValue);
        
        retList.add("TaxWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
        List titleData = new ArrayList<>();
		titleData.add("Tax Wise Report");
		retList.add(titleData);
		
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
		filterData.add(toDate);
		retList.add(filterData); 
		
		headerList.add("Tax Description");
		headerList.add("Taxable Amount");
		headerList.add("Tax Amount");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		
		List blankList = new ArrayList();
	    detailList.add(blankList);// Blank Row at Bottom
	    detailList.add(totalsList);
	    
		
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportExpectedArrWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportExpectedArrWisePMSSalesFlash(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");	
		
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		BigDecimal dblTotalValue = new BigDecimal(0);
		DecimalFormat df = new DecimalFormat("#.##");
		String sql="SELECT a.strReservationNo,  DATE_FORMAT(a.dteDateCreated,'%d-%m-%Y'),CONCAT(e.strFirstName,' ',e.strMiddleName,' ',e.strLastName),   IFNULL(d.dblReceiptAmt,0), DATE_FORMAT(a.dteArrivalDate,'%d-%m-%Y'), DATE_FORMAT(a.dteDepartureDate,'%d-%m-%Y') "
				+ " FROM tblreservationhd a "
				+ " LEFT OUTER JOIN tblreservationdtl b ON a.strReservationNo=b.strReservationNo AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
				+ " LEFT OUTER JOIN tblbookingtype c ON a.strBookingTypeCode=c.strBookingTypeCode AND c.strClientCode='"+strClientCode+"'"
				+ " LEFT OUTER JOIN tblreceipthd d ON a.strReservationNo=d.strRegistrationNo AND d.strClientCode='"+strClientCode+"'"
				+ " LEFT OUTER JOIN tblguestmaster e ON e.strGuestCode=b.strGuestCode AND e.strClientCode='"+strClientCode+"'"
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
			    dblTotalValue = new BigDecimal(df.format(Double.parseDouble(arr2[3].toString()))).add(dblTotalValue);
				detailList.add(DataList);

			}
		}
		totalsList.add(dblTotalValue);
		retList.add("ExpectedArrWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("Expected Arrival Report");
		retList.add(titleData);
			
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
	    filterData.add(toDate);
	    retList.add(filterData);  
		
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
		List blankList = new ArrayList();
	    detailList.add(blankList);// Blank Row at Bottom
	    detailList.add(totalsList);
	    
		
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportExpectedDeptWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportExpectedDeptWisePMSSalesFlash(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
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
		
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		String sql="SELECT a.strCheckInNo,a.strType, DATE(a.dteDepartureDate),c.strRoomDesc,c.strRoomTypeDesc,"
				  +" CONCAT(d.strFirstName,' ',d.strMiddleName,'',d.strLastName) "
                  +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d "
                  +" WHERE a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode AND b.strGuestCode=d.strGuestCode "
                  +" AND DATE(a.dteDepartureDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' AND c.strClientCode='"+strClientCode+"' AND d.strClientCode='"+strClientCode+"';";
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
		retList.add("ExpectedDeptWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("Expected Departure Report");
		retList.add(titleData);
			
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
	    filterData.add(toDate);
	    retList.add(filterData);  
		
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
		
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportCheckInWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportCheckInWisePMSSalesFlash(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
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
		
		DecimalFormat df = new DecimalFormat("#.##");
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
		retList.add("CheckInWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("CheckIn Report");
		retList.add(titleData);
			
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
	    filterData.add(toDate);
	    retList.add(filterData);  
	    
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
		
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportCheckOutWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportCheckOutWisePMSSalesFlash(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");	
		totalsList.add("");
		totalsList.add("");	
		totalsList.add("");	
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		BigDecimal dblTotalValue = new BigDecimal(0);
		DecimalFormat df = new DecimalFormat("#.##");
		String sql="SELECT e.strBillNo,a.strType , DATE(a.dteDepartureDate),c.strRoomDesc,c.strRoomTypeDesc,CONCAT(d.strFirstName,' ',d.strMiddleName,' ',d.strLastName),"
				  +" e.dblGrandTotal"
                +" FROM tblcheckinhd a,tblcheckindtl b,tblroom c,tblguestmaster d,tblbillhd e "
                +" WHERE a.strCheckInNo=b.strCheckInNo AND b.strRoomNo=c.strRoomCode AND b.strGuestCode=d.strGuestCode "
                +" AND a.strCheckInNo=e.strCheckInNo AND DATE(a.dteCheckInDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' AND c.strClientCode='"+strClientCode+"' AND d.strClientCode='"+strClientCode+"' AND e.strClientCode='"+strClientCode+"';";
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
			
			    dblTotalValue = new BigDecimal(df.format(Double.parseDouble(arr2[6].toString()))).add(dblTotalValue);
				detailList.add(DataList);
				

			}
		}
		totalsList.add(dblTotalValue);
		df.format(dblTotalValue);
		retList.add("CheckOutWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("CheckOut Report");
		retList.add(titleData);
			
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
	    filterData.add(toDate);
	    retList.add(filterData);  
	    
		headerList.add("Bill No");
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
		List blankList = new ArrayList();
	    detailList.add(blankList);// Blank Row at Bottom
	    detailList.add(totalsList);
		
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	}
 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportCancelationWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funExportCancelationWisePMSSalesFlash(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
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
		
		DecimalFormat df = new DecimalFormat("#.##");
		String sql = "SELECT a.strReservationNo, CONCAT(c.strFirstName,' ',c.strMiddleName,' ',c.strLastName) AS strGuestName, e.strBookingTypeDesc,h.strRoomTypeDesc,DATE_FORMAT(b.dteReservationDate,'%d-%m-%Y') AS dteReservationDate,DATE_FORMAT(a.dteCancelDate,'%d-%m-%Y') AS dteCancelDate,f.strRoomDesc, g.strReasonDesc, a.strRemarks "
				+ " FROM tblroomcancelation a,tblreservationhd b,tblguestmaster c,tblreservationdtl d,tblbookingtype e,tblroom f, tblreasonmaster g,tblroomtypemaster h "
				+ " WHERE DATE(a.dteCancelDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' AND a.strReservationNo=b.strReservationNo AND b.strCancelReservation='Y' AND b.strReservationNo=d.strReservationNo "
				+ " AND d.strGuestCode=c.strGuestCode AND b.strBookingTypeCode = e.strBookingTypeCode AND d.strRoomType=f.strRoomTypeCode "
				+ " AND a.strReasonCode=g.strReasonCode AND a.strClientCode=b.strClientCode AND h.strRoomTypeCode=d.strRoomType AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' AND c.strClientCode='"+strClientCode+"' AND d.strClientCode='"+strClientCode+"' AND e.strClientCode='"+strClientCode+"' AND f.strClientCode='"+strClientCode+"' AND g.strClientCode='"+strClientCode+"' AND h.strClientCode='"+strClientCode+"'"
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
		retList.add("CancelationWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);	
		List titleData = new ArrayList<>();
		titleData.add("Cancelation Report");
		retList.add(titleData);
		
		
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
	    filterData.add(toDate);
	    retList.add(filterData); 
	    
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
		
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportNoShowWiseWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funNoShowWisePMSSalesFlash(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");	
		
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
	
		BigDecimal dblTotalValue = new BigDecimal(0);
		DecimalFormat df = new DecimalFormat("#.##");
		String sql = "SELECT CONCAT(c.strFirstName,' ',c.strMiddleName,' ',c.strLastName),a.strReservationNo,a.strNoRoomsBooked, IFNULL(b.dblReceiptAmt,0) "
				+ " from tblreservationhd a left outer join tblreceipthd b "
				+ " on a.strReservationNo=b.strReservationNo AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"',tblguestmaster c,tblreservationdtl d "
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
				+ " and  a.strReservationNo Not IN(select strReservationNo from tblcheckinhd  where strClientCode='"+strClientCode+"')";
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
		
		totalsList.add(dblTotalValue);
		retList.add("NoShowWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("No Show Report");
		retList.add(titleData);
		
		
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
	    filterData.add(toDate);
	    retList.add(filterData); 
	    
		headerList.add("Guest Name");
		headerList.add("Reservation No");
		headerList.add("No of Rooms");
		headerList.add("Payment");
		
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		List blankList = new ArrayList();
	    detailList.add(blankList);// Blank Row at Bottom
	    detailList.add(totalsList);
		
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportPMSPaymentFlash", method = RequestMethod.GET)
	private ModelAndView funPMSPaymentFlash(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		List totalsList = new ArrayList();
		totalsList.add("Total");
		BigDecimal dblTotalValue = new BigDecimal(0);
		DecimalFormat df = new DecimalFormat("#.##");
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		String sql = "SELECT a.strReceiptNo, DATE(a.dteReceiptDate),"
				+ "Concat(d.strFirstName,' ',d.strMiddleName,' ',d.strLastName),"
				+ "a.strAgainst,e.strSettlementDesc,a.dblReceiptAmt "
				+ "FROM tblreceipthd a,tblreceiptdtl b,tblcheckindtl c,tblguestmaster d,tblsettlementmaster e "
				+ "WHERE a.strReceiptNo=b.strReceiptNo and a.strCheckInNo=c.strCheckInNo "
				+ "and c.strGuestCode=d.strGuestCode and b.strSettlementCode=e.strSettlementCode AND DATE(a.dteReceiptDate) BETWEEN '"+fromDte+"' "
				+ "AND '"+toDte+"';";
		
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
				dblTotalValue = new BigDecimal(df.format(Double.parseDouble(arr2[5].toString()))).add(dblTotalValue);
				detailList.add(DataList);
			}
		}
		totalsList.add("");
		totalsList.add("");
		totalsList.add("");
		totalsList.add("");
		totalsList.add(dblTotalValue);
		retList.add("PMSPaymentSlip_" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("PMS Payment");
		retList.add(titleData);
		
		
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
	    filterData.add(toDate);
	    retList.add(filterData);
	    
		headerList.add("Receipt No");
		headerList.add("Receipt Date");
		headerList.add("Guest Name");
		headerList.add("Against");
		headerList.add("Settlement Type");
		headerList.add("Amount");
		
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		
		retList.add(ExcelHeader);
		detailList.add(totalsList);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportPMSBillPrinting", method = RequestMethod.GET)
	private ModelAndView funPMSBillPrinting(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		List totalsList = new ArrayList();
		totalsList.add("Total");
		BigDecimal dblTotalBillValue = new BigDecimal(0);
		BigDecimal dblTotalDiscoun = new BigDecimal(0);
		BigDecimal dblTotalTaxAmt = new BigDecimal(0);
		BigDecimal dblTotalAdvanceAmt = new BigDecimal(0);
		DecimalFormat df = new DecimalFormat("0.00");
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		String sql = "select a.strBillNo,Date(a.dteBillDate),c.strRoomDesc,"
				+ "Concat(e.strFirstName,' ',e.strMiddleName,' ',e.strLastName),a.dblGrandTotal,g.dblDiscount,f.strCheckInNo "
				+ "from tblbillhd a ,tblbilldtl b,tblroom c,tblcheckindtl d,tblguestmaster e,tblcheckinhd f,tblwalkinroomratedtl g "
				+ "where a.strBillNo=b.strBillNo "
				+ "and a.strRoomNo=c.strRoomCode and a.strCheckInNo=d.strCheckInNo and d.strGuestCode=e.strGuestCode "
				+ "and d.strCheckInNo=f.strCheckInNo "
				+ "and f.strWalkInNo=g.strWalkinNo "
				+ "and Date(a.dteBillDate) between '"+fromDte+"' and '"+toDte+"' and a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"' AND c.strClientCode='"+strClientCode+"' AND d.strClientCode='"+strClientCode+"' AND e.strClientCode='"+strClientCode+"' AND f.strClientCode='"+strClientCode+"' AND g.strClientCode='"+strClientCode+"' group by a.strBillNo";
		
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
				dblTotalBillValue = new BigDecimal(df.format(Double.parseDouble(arr2[4].toString()))).add(dblTotalBillValue);
				String sqlDisc = "select sum(a.dblDebitAmt) from tblbilldtl a where a.strBillNo='"+arr2[0].toString()+"' and a.strPerticulars='Room Tariff' and a.strClientCode='"+strClientCode+"'";
				List listDisc = objGlobalService.funGetListModuleWise(sqlDisc, "sql");
				if(listDisc!=null && listDisc.size()>0)
				{
					double dblDiscAmt = Double.parseDouble(listDisc.get(0).toString());
					dblDiscAmt = dblDiscAmt -(dblDiscAmt*Double.parseDouble(arr2[5].toString())/100);
					DataList.add(dblDiscAmt);
					dblTotalDiscoun = new BigDecimal(df.format(Double.parseDouble(listDisc.get(0).toString()))).add(dblTotalDiscoun);
				}
				
				String sqlTaxAmt = "select sum(a.dblTaxAmt) from tblbilltaxdtl a where a.strBillNo='"+arr2[0].toString()+"' and a.strTaxCode like 'TC%' and a.strClientCode='"+strClientCode+"'";
				List listTaxAmt = objGlobalService.funGetListModuleWise(sqlTaxAmt, "sql");
				if(listTaxAmt!=null && listTaxAmt.size()>0)
				{
					DataList.add(Double.parseDouble(listTaxAmt.get(0).toString()));
					dblTotalTaxAmt = new BigDecimal(df.format(Double.parseDouble(listTaxAmt.get(0).toString()))).add(dblTotalTaxAmt);
				}

				String sqlAdvanceAmt = "select a.dblReceiptAmt from tblreceipthd a where a.strCheckInNo='"+arr2[6].toString()+"' "
						+ "and a.strAgainst='Check-In' and a.strClientCode='"+strClientCode+"';";
				List listAdvAmt = objGlobalService.funGetListModuleWise(sqlAdvanceAmt, "sql");
				if(listAdvAmt!=null && listAdvAmt.size()>0)
				{
					dblTotalAdvanceAmt = new BigDecimal(df.format(Double.parseDouble(listAdvAmt.get(0).toString()))).add(dblTotalAdvanceAmt);
					DataList.add(Double.parseDouble(listAdvAmt.get(0).toString()));
				}
				else
				{
					DataList.add(0);
				}
				
				
				
				
				
				detailList.add(DataList);
			}
		}

		totalsList.add("");
		totalsList.add("");
		totalsList.add("");
		
		totalsList.add(dblTotalBillValue);
		totalsList.add(dblTotalDiscoun);
		totalsList.add(dblTotalTaxAmt);
		totalsList.add(dblTotalAdvanceAmt);
		retList.add("PMSBillPrinting" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("PMS Bill Printing");
		retList.add(titleData);
		
		
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
	    filterData.add(toDate);
	    retList.add(filterData);
	    
		headerList.add("Bill No");
		headerList.add("Bill Date");
		headerList.add("Room No");
		headerList.add("Guest Name");
		headerList.add("Bill Amount");
		headerList.add("Discount Amount");
		headerList.add("Tax Amount");
		headerList.add("Advance Amount");
		
		
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		
		retList.add(ExcelHeader);
		detailList.add(totalsList);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportVoidBillWisePMSSalesFlash", method = RequestMethod.GET)
	private ModelAndView funVoidBillWisePMSSalesFlash(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		
		
		DecimalFormat df = new DecimalFormat("#.##");
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		String sql = "SELECT a.strBillNo, DATE_FORMAT(a.dteBillDate,'%d-%m-%Y'),CONCAT(e.strGuestPrefix,\" \",e.strFirstName,\" \",e.strLastName) AS gName,d.strRoomDesc,b.strPerticulars, "
				+ " SUM(b.dblDebitAmt), a.strReasonName,a.strRemark,a.strVoidType, a.strUserCreated "
				+ " FROM tblvoidbillhd a inner join tblvoidbilldtl b on a.strBillNo=b.strBillNo AND a.strClientCode='"+strClientCode+"' AND b.strClientCode='"+strClientCode+"'"
				+ " left outer join tblcheckindtl c on a.strCheckInNo=c.strCheckInNo AND c.strClientCode='"+strClientCode+"'"
				+ " left outer join tblroom d on a.strRoomNo=d.strRoomCode AND d.strClientCode='"+strClientCode+"'"
				+ " left outer join tblguestmaster e on c.strGuestCode=e.strGuestCode  AND e.strClientCode='"+strClientCode+"'"
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
		retList.add("VoidBillWisePMSSalesFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("Void Bill Report");
		retList.add(titleData);
		
		
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
	    filterData.add(toDate);
	    retList.add(filterData);
	    
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
		
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	}
	@RequestMapping(value = "/loadPerticulars", method = RequestMethod.GET)
	public ModelAndView funGetBillPerticulars(Map<String, Object> model,@RequestParam("billNo")String strBillNo ,HttpServletRequest request) {
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String urlHits = "1";
		String strBillPerticular = "";
		String sqlPerticular="select a.strPerticulars from tblbilldtl a where a.strBillNo='"+strBillNo+"' and a.strClientCode='"+strClientCode+"'";
		
		List listRecord = objGlobalService.funGetListModuleWise(sqlPerticular, "sql");
	
		
		ArrayList<String> perticularList = new ArrayList<>(); 
		model.put("perticular", listRecord);
	
		return new ModelAndView("frmPMSSalesFlash");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportPMSHousekeepingSummary", method = RequestMethod.GET)
	private ModelAndView funPMSHousekeepingSummary(HttpServletRequest request) throws ParseException
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		TreeSet listDatesHeader = new TreeSet();
		List listRoomCLeanCheck =  new ArrayList();
		List<String> listRoomWise = new ArrayList<String>();
		DateFormat formatter ; 
	
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		BigDecimal dblTotalValue = new BigDecimal(0);
		DecimalFormat df = new DecimalFormat("#.##");
		
		
	
		
		retList.add("Hosekeeping Service" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("Hosekeeping Service");
		retList.add(titleData);
		
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
		filterData.add(toDate);
        retList.add(filterData); 
        
        
        String sqlRoom = "select a.strRoomCode,a.strRoomDesc from tblroom a where a.strClientCode='"+strClientCode+"'"; 

		List<List> listMain = new ArrayList<List>();
		List listRoomNo= objGlobalService.funGetListModuleWise(sqlRoom, "sql");
		for(int r = 0;r<listRoomNo.size();r++)
		{
			listRoomWise = new ArrayList();
			Object[] arr2   = (Object[]) listRoomNo.get(r);
			listRoomWise.add((String) arr2[1]);
		int count=0;
		formatter = new SimpleDateFormat("dd-MM-yyyy");
		Date  startDate = (Date)formatter.parse(fromDate); 
		Date  endDate = (Date)formatter.parse(toDate);
		long interval = 24*1000 * 60 * 60; // 1 hour in millis
		long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
		long curTime = startDate.getTime();
		
		while (curTime <= endTime) {
			Date tempDate = new Date(curTime);
		    
			SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");  
		    String strDate= formatter2.format(tempDate);  
		    curTime += interval;
		    
		    listDatesHeader.add(strDate);
		    
		    String sqlRoomCLeanCheck = "SELECT  IF(SPACE(b.strHouseKeepCode)=0,'Completed','Pending') "
		    		+ "FROM tblroomhousekeepdtl b where  DATE(b.dteDate)='"+objGlobal.funGetDate("yyyy-MM-dd",strDate)+"' and b.strRoomCode='"+arr2[0]+"' "
		    		+ "group by b.strRoomCode"; 
		    
		    listRoomCLeanCheck = objGlobalService.funGetListModuleWise(sqlRoomCLeanCheck, "sql");
		  
		    if(listRoomCLeanCheck!=null && listRoomCLeanCheck.size()>0)
    		{
		    	listRoomWise.add(listRoomCLeanCheck.get(0).toString());
    		}
		    else
		    {
		    	listRoomWise.add("Pending");
		    }
		  count++;
		   
		 }
		listMain.add(listRoomWise);
		}
        
      
        		

			
			if (!listRoomWise.isEmpty()) {
			for (int i = 0; i < listMain.size(); i++) {
			//String   arr3 = listRoomWise.get(i).toString();
			List DataList = new ArrayList<>();
			List listTemp = listMain.get(i);
			for(int j=0;j<listTemp.size();j++)
			{
			
			DataList.add(listTemp.get(j));
			//DataList.add("SUMEET");
			
			
			

			}
			detailList.add(DataList);
			
			}
			}
		
		Object[] objHeader = (Object[]) listDatesHeader.toArray();

		String[] ExcelHeader = new String[objHeader.length+1];
		ExcelHeader[0] = "Room No";
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k+1] = objHeader[k].toString();
		}
		
		
		List blankList = new ArrayList();
	    detailList.add(blankList);// Blank Row at Bottom
	    
			
        retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
    }
	
}

