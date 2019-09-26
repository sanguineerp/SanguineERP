package com.sanguine.webpms.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.Connection;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webpms.bean.clsFolioPrintingBean;
import com.sanguine.webpms.dao.clsWebPMSDBUtilityDao;
import com.sanguine.webpms.service.clsFolioService;

@Controller
public class clsFolioPrintingController {
	@Autowired
	private clsFolioService objFolioService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@Autowired
	private clsWebPMSDBUtilityDao objWebPMSUtility;

	// Open Folio Printing
	@RequestMapping(value = "/frmFolioPrinting", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmFolioPrinting_1", "command", new clsFolioPrintingBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmFolioPrinting", "command", new clsFolioPrintingBean());
		} else {
			return null;
		}
	}

	// Save folio posting
	@RequestMapping(value = "/rptFolioPrinting", method = RequestMethod.GET)
	public void funGenerateFolioPrintingReport(@RequestParam("folioNo") String folioNo, HttpServletRequest req, HttpServletResponse resp) {
		try {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			String companyName = req.getSession().getAttribute("companyName").toString();
			clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
			if (objSetup == null) {
				objSetup = new clsPropertySetupModel();
			}
			String reportName = servletContext.getRealPath("/WEB-INF/reports/webpms/rptFolioPrinting.jrxml");
			String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");

			List<clsFolioPrintingBean> dataList = new ArrayList<clsFolioPrintingBean>();
			@SuppressWarnings("rawtypes")
			HashMap reportParams = new HashMap();

			String sqlParametersFromFolio = "SELECT a.strFolioNo,e.strRoomDesc,a.strRegistrationNo,a.strReservationNo " + " ,date(b.dteArrivalDate),b.tmeArrivalTime ,ifnull(date(b.dteDepartureDate),'NA'),ifnull(b.tmeDepartureTime,'NA')" + " ,d.strGuestPrefix,d.strFirstName,d.strMiddleName,d.strLastName ,b.intNoOfAdults,b.intNoOfChild,'NA',d.strGuestCode "
					+ " FROM tblfoliohd a LEFT OUTER JOIN tblreservationhd b ON a.strReservationNo=b.strReservationNo AND b.strClientCode='"+clientCode+"'" + " LEFT OUTER JOIN tblguestmaster d ON a.strGuestCode=d.strGuestCode AND d.strClientCode='"+clientCode+"'" + " LEFT OUTER JOIN tblroom e ON a.strRoomNo=e.strRoomCode AND e.strClientCode='"+clientCode+"'" + " where a.strFolioNo='" + folioNo + "' and a.strClientCode='" + clientCode + "'";

			String sqlFolio = "select strReservationNo,strWalkInNo from tblfoliohd where strFolioNo='" + folioNo + "' and strClientCode='" + clientCode + "' ";
			List folioDtl = objFolioService.funGetParametersList(sqlFolio);
			if (folioDtl.size() > 0) {
				Object[] arrFolioDtl = (Object[]) folioDtl.get(0);
				if (!arrFolioDtl[1].toString().isEmpty()) {
					sqlParametersFromFolio = "SELECT a.strFolioNo,e.strRoomDesc,a.strRegistrationNo,a.strReservationNo " + " ,date(b.dteWalkinDate),b.tmeWalkinTime ,ifnull(date(b.dteCheckOutDate),'NA'),ifnull(b.tmeCheckOutTime,'NA')" + " ,d.strGuestPrefix,d.strFirstName,d.strMiddleName,d.strLastName ,b.intNoOfAdults,b.intNoOfChild,'NA',d.strGuestCode "
							+ " FROM tblfoliohd a LEFT OUTER JOIN tblwalkinhd b ON a.strWalkinNo=b.strWalkinNo  AND b.strClientCode='"+clientCode+"'" + " LEFT OUTER JOIN tblguestmaster d ON a.strGuestCode=d.strGuestCode AND d.strClientCode='"+clientCode+"'" + " LEFT OUTER JOIN tblroom e ON a.strRoomNo=e.strRoomCode  AND e.strClientCode='"+clientCode+"'" + " where a.strFolioNo='" + folioNo + "' and a.strClientCode='" + clientCode + "'";
				}
			}

			// get all parameters

			List listOfParametersFromFolio = objFolioService.funGetParametersList(sqlParametersFromFolio);
			if (listOfParametersFromFolio.size() > 0) {
				Object[] arr = (Object[]) listOfParametersFromFolio.get(0);

				String folio = arr[0].toString();
				String roomNo = arr[1].toString();
				String registrationNo = arr[2].toString();
				String reservationNo = arr[3].toString();
				String arrivalDate = arr[4].toString();
				String arrivalTime = arr[5].toString();
				String departureDate = arr[6].toString();
				String departureTime = arr[7].toString();
				String gPrefix = arr[8].toString();
				String gFirstName = arr[9].toString();
				String gMiddleName = arr[10].toString();
				String gLastName = arr[11].toString();
				String adults = arr[12].toString();
				String childs = arr[13].toString();
				String billNo = arr[14].toString();
				String guestCode = arr[15].toString();
				String guestAddr = "";
				String sqlAddr	="SELECT IFNULL(d.strDefaultAddr,''), IFNULL(d.strAddressLocal,''), "
						+ "IFNULL(d.strCityLocal,''), IFNULL(d.strStateLocal,''), "
						+ "IFNULL(d.strCountryLocal,''), IFNULL(d.intPinCodeLocal,''), "
						+ "IFNULL(d.strAddrPermanent,''), IFNULL(d.strCityPermanent,''), "
						+ "IFNULL(d.strStatePermanent,''), IFNULL(d.strCountryPermanent,''), "
						+ "IFNULL(d.intPinCodePermanent,''), IFNULL(d.strAddressOfc,''), "
						+ "IFNULL(d.strCityOfc,''), IFNULL(d.strStateOfc,''), "
						+ "IFNULL(d.strCountryOfc,''), IFNULL(d.intPinCodeOfc,''), "
						+ "IFNULL(d.strGSTNo,'') FROM tblguestmaster d "
						+ "WHERE d.strGuestCode= '"+guestCode+"' AND d.strClientCode='"+clientCode+"'";
				
				List listguest = objFolioService.funGetParametersList(sqlAddr);
				
				if (listguest.size() > 0) {
					Object[] arrGuest = (Object[]) listguest.get(0);
					if (arrGuest[0].toString().equalsIgnoreCase("Permanent")) { // check
																				// default
																				// addr
						guestAddr = arrGuest[6].toString() + ","
								+ arrGuest[7].toString() + ","
								+ arrGuest[8].toString() + ","
								+ arrGuest[9].toString() + ","
								+ arrGuest[10].toString();
					} else if (arrGuest[0].toString()
							.equalsIgnoreCase("Office")) {
						guestAddr = arrGuest[11].toString() + ","
								+ arrGuest[12].toString() + ","
								+ arrGuest[13].toString() + ","
								+ arrGuest[14].toString() + ","
								+ arrGuest[15].toString();
					} else { // Local
						guestAddr = arrGuest[1].toString() + ","
								+ arrGuest[2].toString() + ","
								+ arrGuest[3].toString() + ","
								+ arrGuest[4].toString() + ","
								+ arrGuest[5].toString();
					}
				}

				reportParams.put("pCompanyName", companyName);
				reportParams.put("pAddress1", objSetup.getStrAdd1() + "," + objSetup.getStrAdd2() + "," + objSetup.getStrCity());
				reportParams.put("pAddress2", objSetup.getStrState() + "," + objSetup.getStrCountry() + "," + objSetup.getStrPin());
				reportParams.put("pContactDetails", "");
				reportParams.put("strImagePath", imagePath);
				reportParams.put("pGuestName", gPrefix + " " + gFirstName + " " + gMiddleName + " " + gLastName);
				reportParams.put("pFolioNo", folio);
				reportParams.put("pRoomNo", roomNo);
				reportParams.put("pRegistrationNo", registrationNo);
				reportParams.put("pReservationNo", reservationNo);
				reportParams.put("pArrivalDate", objGlobal.funGetDate("dd-MM-yyyy", arrivalDate));
				reportParams.put("pArrivalTime", arrivalTime);
				reportParams.put("pDepartureDate", objGlobal.funGetDate("dd-MM-yyyy", departureDate));
				reportParams.put("pDepartureTime", departureTime);
				reportParams.put("pAdult", adults);
				reportParams.put("pChild", childs);
				reportParams.put("pGuestAddress", guestAddr);
				reportParams.put("pRemarks", "");
				reportParams.put("strUserCode", userCode);
				reportParams.put("pBillNo", billNo);

				// get folio details
				String sqlFolioDtl = "SELECT DATE_FORMAT(b.dteDocDate,'%d-%m-%Y'),b.strDocNo,IFNULL(SUBSTRING_INDEX(SUBSTRING_INDEX(b.strPerticulars,'(', -1),')',1),''),b.dblQuantity,b.dblDebitAmt,b.dblCreditAmt,b.dblBalanceAmt ,b.strPerticulars" + " FROM tblfoliohd a LEFT OUTER JOIN tblfoliodtl b ON a.strFolioNo=b.strFolioNo AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"'" + " WHERE a.strFolioNo='" + folioNo + "' and b.strRevenueType!='Discount'"
									+ " order by b.dteDocDate ASC";
				List folioDtlList = objFolioService.funGetParametersList(sqlFolioDtl);
				for (int i = 0; i < folioDtlList.size(); i++) {
					Object[] folioArr = (Object[]) folioDtlList.get(i);
					String docDate = folioArr[0].toString();
					if (folioArr[1] == null) {
						continue;
					} else {
						clsFolioPrintingBean folioPrintingBean = new clsFolioPrintingBean();
						String docNo = folioArr[1].toString();
						String particulars = folioArr[2].toString();
						double debitAmount = Double.parseDouble(folioArr[4].toString());
						double creditAmount = Double.parseDouble(folioArr[5].toString());
						double balance = debitAmount - creditAmount;
						String strCompletePertName = folioArr[7].toString();

						folioPrintingBean.setDteDocDate(docDate);
						folioPrintingBean.setStrDocNo(docNo);
						folioPrintingBean.setStrPerticulars(particulars);
						folioPrintingBean.setDblDebitAmt(debitAmount);
						folioPrintingBean.setDblCreditAmt(creditAmount);
						folioPrintingBean.setDblBalanceAmt(balance);
						folioPrintingBean.setDblQuantity(Double.parseDouble(folioArr[3].toString()));
						dataList.add(folioPrintingBean);
						

						if(!strCompletePertName.contains("POS"))
						{
						sqlFolioDtl = "SELECT DATE_FORMAT(date(a.dteDocDate),'%d-%m-%Y'),a.strDocNo,b.strTaxDesc,b.dblTaxAmt,0,0 " + " FROM tblfoliodtl a,tblfoliotaxdtl b where a.strDocNo=b.strDocNo " + " and  a.strFolioNo='" + folioNo + "' and a.strDocNo='" + docNo + "' AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"'";
						List listFolioTaxDtl = objWebPMSUtility.funExecuteQuery(sqlFolioDtl, "sql");
						for (int cnt = 0; cnt < listFolioTaxDtl.size(); cnt++) {
							Object[] arrObjFolioTaxDtl = (Object[]) listFolioTaxDtl.get(cnt);

							folioPrintingBean = new clsFolioPrintingBean();
							folioPrintingBean.setDteDocDate(arrObjFolioTaxDtl[0].toString());
							folioPrintingBean.setStrDocNo(arrObjFolioTaxDtl[1].toString());
							folioPrintingBean.setStrPerticulars(arrObjFolioTaxDtl[2].toString());
							folioPrintingBean.setDblDebitAmt(Double.parseDouble(arrObjFolioTaxDtl[3].toString()));
							folioPrintingBean.setDblCreditAmt(Double.parseDouble(arrObjFolioTaxDtl[4].toString()));
							folioPrintingBean.setDblBalanceAmt(Double.parseDouble(arrObjFolioTaxDtl[3].toString()) - Double.parseDouble(arrObjFolioTaxDtl[4].toString()));
							folioPrintingBean.setTax(true);
							dataList.add(folioPrintingBean);
						}
						}
					}
				}
				
				sqlFolioDtl = "SELECT DATE_FORMAT(b.dteDocDate,'%d-%m-%Y'),b.strDocNo,b.strPerticulars,b.dblDebitAmt,b.dblCreditAmt,b.dblBalanceAmt,b.strRevenueType" 
						+ " FROM tblfoliohd a LEFT OUTER JOIN tblfoliodtl b ON a.strFolioNo=b.strFolioNo AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"'" 
						+ " WHERE  a.strFolioNo='" + folioNo + "' and b.strRevenueType='Discount'";
				folioDtlList = objFolioService.funGetParametersList(sqlFolioDtl);
				if(folioDtlList.size()>0)
				{
				for (int j = 0; j < folioDtlList.size(); j++) {
					clsFolioPrintingBean folioPrintingBean = new clsFolioPrintingBean();
					Object[] obj = (Object[])folioDtlList.get(0);
					BigDecimal bgDebit = (BigDecimal)obj[3];
					BigDecimal bgCredit = (BigDecimal)obj[4];
					folioPrintingBean.setDblCreditAmt(bgDebit.doubleValue());
					double balance = bgDebit.doubleValue() - bgCredit.doubleValue();

					folioPrintingBean.setDteDocDate(obj[0].toString());
					folioPrintingBean.setStrDocNo(obj[1].toString());
					folioPrintingBean.setStrPerticulars("Discount");
					folioPrintingBean.setDblDebitAmt(0);
					folioPrintingBean.setDblCreditAmt(bgCredit.doubleValue());
					folioPrintingBean.setDblBalanceAmt(balance);

					dataList.add(folioPrintingBean);
				}
				}
				
				// get payment details
				/*String sqlPaymentDtl = "Select IFNULL(DATE(b.dteDocDate),''),ifnull(c.strReceiptNo,''),ifnull(e.strSettlementDesc,''),'0.00' AS debitAmt,ifnull(d.dblSettlementAmt,0.0) AS creditAmt,'0.00' AS balance" + " FROM tblfoliohd a LEFT OUTER JOIN tblfoliodtl b ON a.strFolioNo=b.strFolioNo " + " left outer join tblreceipthd c on a.strFolioNo=c.strFolioNo and a.strReservationNo=c.strReservationNo "
						+ " left outer join tblreceiptdtl d on c.strReceiptNo=d.strReceiptNo " + " left outer join tblsettlementmaster e on d.strSettlementCode=e.strSettlementCode " + " WHERE  a.strFolioNo='" + folioNo + "' " + " group by a.strFolioNo ";*/
				
				String sqlPaymentDtl = "SELECT date(b.dteDocDate),c.strReceiptNo,e.strSettlementDesc,'0.00' as debitAmt,d.dblSettlementAmt as creditAmt" + " ,'0.00' as balance " + " FROM tblfoliohd a LEFT OUTER JOIN tblfoliodtl b ON a.strFolioNo=b.strFolioNo AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"'" + " left outer join tblreceipthd c on a.strFolioNo=c.strFolioNo and a.strReservationNo=c.strReservationNo AND c.strClientCode='"+clientCode+"'"
						+ " left outer join tblreceiptdtl d on c.strReceiptNo=d.strReceiptNo AND d.strClientCode='"+clientCode+"'" + " left outer join tblsettlementmaster e on d.strSettlementCode=e.strSettlementCode AND e.strClientCode='"+clientCode+"'" + " WHERE a.strFolioNo='" + folioNo + "' " + " group by a.strFolioNo ";
				
				List paymentDtlList = objFolioService.funGetParametersList(sqlPaymentDtl);
				if(paymentDtlList!=null && paymentDtlList.size()>1){
					
					for (int i = 0; i < paymentDtlList.size(); i++) {
						Object[] paymentArr = (Object[]) paymentDtlList.get(i);

						String docDate = paymentArr[0].toString();
						if (paymentArr[1] == null) {
							continue;
						} else {
							clsFolioPrintingBean folioPrintingBean = new clsFolioPrintingBean();

							String docNo = paymentArr[1].toString();
							String particulars = paymentArr[2].toString();
							double debitAmount = Double.parseDouble(paymentArr[3].toString());
							double creditAmount = Double.parseDouble(paymentArr[4].toString());
							double balance = debitAmount - creditAmount;

							folioPrintingBean.setDteDocDate(objGlobal.funGetDate("dd-mm-yyyy", docDate));
							folioPrintingBean.setStrDocNo(docNo);
							folioPrintingBean.setStrPerticulars(particulars);
							folioPrintingBean.setDblDebitAmt(debitAmount);
							folioPrintingBean.setDblCreditAmt(creditAmount);
							folioPrintingBean.setDblBalanceAmt(balance);

							dataList.add(folioPrintingBean);
						}
					}

				}
				}
			List<clsFolioPrintingBean> listTax=new ArrayList<>();
			for(clsFolioPrintingBean folioPrintingBean :dataList){
				if(folioPrintingBean.isTax()){
					listTax.add(folioPrintingBean);
					
				}
				
			}
			
			JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataList);
			JasperDesign jd = JRXmlLoader.load(reportName);
			JasperReport jr = JasperCompileManager.compileReport(jd);
			JasperPrint jp = JasperFillManager.fillReport(jr, reportParams, beanCollectionDataSource);
			List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
			if (jp != null) {
				jprintlist.add(jp);
				ServletOutputStream servletOutputStream = resp.getOutputStream();
				JRExporter exporter = new JRPdfExporter();
				resp.setContentType("application/pdf");
				exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
				exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
				exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
				resp.setHeader("Content-Disposition", "inline;filename=Folio.pdf");
				exporter.exportReport();
				servletOutputStream.flush();
				servletOutputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
