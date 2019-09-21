package com.sanguine.controller;

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
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsPropertyMasterService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webpms.bean.clsPMSPaymentBean;
import com.sanguine.webpms.bean.clsPaymentReciptBean;
import com.sanguine.webpms.dao.clsPMSPaymentDao;
import com.sanguine.webpms.dao.clsPMSSettlementMasterDao;
import com.sanguine.webpms.model.clsPMSPaymentHdModel;
import com.sanguine.webpms.model.clsPMSPaymentReceiptDtl;
import com.sanguine.webpms.service.clsCheckInService;
import com.sanguine.webpms.service.clsFolioService;
import com.sanguine.webpms.service.clsGuestMasterService;
import com.sanguine.webpms.service.clsPMSPaymentService;
import com.sanguine.webpms.service.clsPropertySetupService;
import com.sanguine.webpms.service.clsReservationService;
import com.sanguine.webpms.service.clsRoomMasterService;

@Controller
public class clsPMSReprintController {
	
	
	@Autowired
	private clsPMSPaymentService objPaymentService;

	@Autowired
	private clsPMSPaymentDao objPaymentDao;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@Autowired
	private clsPropertySetupService objPropertySetupService;

	@Autowired
	private clsGuestMasterService objGuestService;

	@Autowired
	private clsPropertyMasterService objPropertyMasterService;

	@Autowired
	clsPMSPaymentDao objDao;

	@Autowired
	private clsReservationService objReservationService;
	@Autowired
	private clsFolioService objFolioService;

	@Autowired
	clsCheckInService objCheckInService;

	@Autowired
	clsPMSSettlementMasterDao objtPMSSettlement;

	@Autowired
	clsRoomMasterService objRoomMaster;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunService;
	
	
	@RequestMapping(value = "/frmPMSReprintReceipt", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";

		
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		if (urlHits.equalsIgnoreCase("1")) {

			return new ModelAndView("frmPMSReprintReceipt", "command", new clsPMSPaymentBean());
		} else {
			return new ModelAndView("frmPMSReprintReceipt_1", "command", new clsPMSPaymentBean());
		}
	}
	
	@RequestMapping(value = "/loadPMSReceiptData", method = RequestMethod.GET)
	public @ResponseBody clsPMSPaymentBean funLoadReceiptData(@RequestParam("receiptNo") String receiptNo, HttpServletRequest request) {

		String clientCode = request.getSession().getAttribute("clientCode").toString();
		clsPMSPaymentBean objPaymentRecBean = new clsPMSPaymentBean();
		clsPMSPaymentHdModel objPaymentModel = objPaymentDao.funGetPaymentModel(receiptNo, clientCode);

		objPaymentRecBean.setStrReceiptNo(objPaymentModel.getStrReceiptNo());
		objPaymentRecBean.setStrAgainst(objPaymentModel.getStrAgainst());

		if (objPaymentModel.getStrAgainst().equals("Reservation")) {
			objPaymentRecBean.setStrDocNo(objPaymentModel.getStrReservationNo());
		} else if (objPaymentModel.getStrAgainst().equals("Folio-No")) {
			objPaymentRecBean.setStrDocNo(objPaymentModel.getStrFolioNo());

		} else if (objPaymentModel.getStrAgainst().equals("Check-In")) {
			objPaymentRecBean.setStrDocNo(objPaymentModel.getStrCheckInNo());
		} else {
			objPaymentRecBean.setStrDocNo(objPaymentModel.getStrBillNo());
		}
		objPaymentRecBean.setStrFolioNo(objPaymentModel.getStrFolioNo());
		objPaymentRecBean.setStrRegistrationNo(objPaymentModel.getStrRegistrationNo());
		objPaymentRecBean.setStrFlagOfAdvAmt(objPaymentModel.getStrFlagOfAdvAmt());
		clsPMSPaymentReceiptDtl objPaymentDtlModel = objPaymentModel.getListPaymentRecieptDtlModel().get(0);
		objPaymentRecBean.setStrRemarks(objPaymentDtlModel.getStrRemarks());
		objPaymentRecBean.setStrCardNo(objPaymentDtlModel.getStrCardNo());
		objPaymentRecBean.setStrSettlementCode(objPaymentDtlModel.getStrSettlementCode());
		objPaymentRecBean.setDblPaidAmt(objPaymentModel.getDblPaidAmt());
		objPaymentRecBean.setDblReceiptAmt(objPaymentModel.getDblReceiptAmt());
		objPaymentRecBean.setDblSettlementAmt(objPaymentDtlModel.getDblSettlementAmt());
		objPaymentRecBean.setDteExpiryDate(objGlobal.funGetDate("yyyy/MM/dd", objPaymentDtlModel.getDteExpiryDate()));

		return objPaymentRecBean;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/rptReprintReservationPaymentRecipt", method = RequestMethod.GET)
	public void funGeneratePaymentRecipt(@RequestParam("reciptNo") String reciptNo, @RequestParam("checkAgainst") String checkAgainst, HttpServletRequest req, HttpServletResponse resp) {
		try {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			String companyName = req.getSession().getAttribute("companyName").toString();
			String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
			clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
			if (objSetup == null) {
				objSetup = new clsPropertySetupModel();
			}

			String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
			String userName = "";
			String sqlUserName = "select strUserName from "+webStockDB+".tbluserhd where strUserCode='" + userCode + "' ";

			List listOfUser = objGlobalFunctionsService.funGetDataList(sqlUserName, "sql");
			if (listOfUser.size() > 0) {
				// Object[] userData = (Object[]) listOfUser.get(0);
				userName = listOfUser.get(0).toString();
			}

			HashMap reportParams = new HashMap();
			reportParams.put("pCompanyName", companyName);
			reportParams.put("pAddress1", objSetup.getStrAdd1() + "," + objSetup.getStrAdd2() + "," + objSetup.getStrCity());
			reportParams.put("pAddress2", objSetup.getStrState() + "," + objSetup.getStrCountry() + "," + objSetup.getStrPin());
			reportParams.put("pContactDetails", "");
			reportParams.put("strImagePath", imagePath);
			reportParams.put("userName", userName);
			ArrayList datalist = new ArrayList();
			String reportName = "";
			if (checkAgainst.equalsIgnoreCase("Reservation")) {
				reportName = servletContext.getRealPath("/WEB-INF/reports/webpms/rptReservationPaymentRecipt.jrxml");
				/*String sqlPayment = "select a.strReceiptNo,ifnull(d.intNoOfAdults,''),ifnull(d.intNoOfChild,'')  ,ifnull(a.strReservationNo,'')" + ",ifnull(c.strRoomType,''),DATE_FORMAT(d.dteArrivalDate,'%d-%m-%Y'),DATE_FORMAT(d.dteDepartureDate,'%d-%m-%Y'),f.strFirstName" + ",f.strMiddleName,f.strLastName,ifnull(e.strSettlementDesc,''),a.dblPaidAmt,b.strRemarks"
						+ ",DATE_FORMAT(a.dteReceiptDate,'%d-%m-%Y') " + "from tblreceipthd a left outer join  tblreceiptdtl b on a.strReceiptNo=b.strReceiptNo " + "left outer join  tblreservationdtl c on a.strReservationNo=c.strReservationNo " + "left outer join  tblreservationhd d on a.strReservationNo=d.strReservationNo "
						+ "left outer join  tblsettlementmaster e on b.strSettlementCode=e.strSettlementCode " + "left outer join  tblguestmaster f    on c.strGuestCode=f.strGuestCode " + "where a.strReceiptNo='" + reciptNo + "' and a.strClientCode='" + clientCode + "'  ";
				*/
				String sqlPayment = "select a.strReceiptNo,ifnull(d.intNoOfAdults,''),ifnull(d.intNoOfChild,'')  ,ifnull(a.strReservationNo,'')" 
						+ ",ifnull(c.strRoomType,''),DATE_FORMAT(d.dteArrivalDate,'%d-%m-%Y'),DATE_FORMAT(d.dteDepartureDate,'%d-%m-%Y'),f.strFirstName" 
						+ ",f.strMiddleName,f.strLastName,ifnull(e.strSettlementDesc,''),a.dblPaidAmt,b.strRemarks"
						+ ",DATE_FORMAT(a.dteReceiptDate,'%d-%m-%Y'),g.strRoomTypeDesc " 
						+ "from tblreceipthd a left outer join  tblreceiptdtl b on a.strReceiptNo=b.strReceiptNo " 
						+ "left outer join  tblreservationdtl c on a.strReservationNo=c.strReservationNo "
						+ "left outer join  tblreservationhd d on a.strReservationNo=d.strReservationNo "
						+ "left outer join  tblsettlementmaster e on b.strSettlementCode=e.strSettlementCode " 
						+ "left outer join  tblguestmaster f    on c.strGuestCode=f.strGuestCode,tblroomtypemaster g " 
						+ "where c.strRoomType=g.strRoomTypeCode "
						+ "and a.strReceiptNo='" + reciptNo + "' and a.strClientCode='" + clientCode + "'  ";
				List listOfPayment = objGlobalFunctionsService.funGetDataList(sqlPayment, "sql");

				for (int i = 0; i < listOfPayment.size(); i++) {
					Object PaymentData[] = (Object[]) listOfPayment.get(i);

					String strReceiptNo = PaymentData[0].toString();
					String intNoOfAdults = PaymentData[1].toString();
					String intNoOfChild = PaymentData[2].toString();
					String strReservationNo = PaymentData[3].toString();
					String strRoomType = PaymentData[14].toString();
					String dteArrivalDate = PaymentData[5].toString();
					String dteDepartureDate = PaymentData[6].toString();
					String strFirstName = PaymentData[7].toString();
					String strMiddleName = PaymentData[8].toString();
					String strLastName = PaymentData[9].toString();
					String strSettlementDesc = PaymentData[10].toString();
					String dblPaidAmt = PaymentData[11].toString();
					String strRemarks = PaymentData
							[12].toString();
					String dteReciptDate = PaymentData[13].toString();
					String dteModifiedDate = PaymentData[13].toString();

					clsPaymentReciptBean objPaymentReciptBean = new clsPaymentReciptBean();
					objPaymentReciptBean.setStrReceiptNo(strReceiptNo);
					objPaymentReciptBean.setIntNoOfAdults(intNoOfAdults);
					objPaymentReciptBean.setIntNoOfChild(intNoOfChild);
					objPaymentReciptBean.setStrReservationNo(strReservationNo);
					objPaymentReciptBean.setStrRoomType(strRoomType);
					objPaymentReciptBean.setDteArrivalDate(dteArrivalDate);
					objPaymentReciptBean.setDteDepartureDate(dteDepartureDate);
					objPaymentReciptBean.setStrFirstName(strFirstName+" ");
					objPaymentReciptBean.setStrMiddleName(strMiddleName+" ");
					objPaymentReciptBean.setStrLastName(strLastName);
					objPaymentReciptBean.setStrSettlementDesc(strSettlementDesc);
					objPaymentReciptBean.setDblPaidAmt(dblPaidAmt);
					objPaymentReciptBean.setStrRemarks(strRemarks);
					objPaymentReciptBean.setDteReciptDate(dteReciptDate);
					objPaymentReciptBean.setDteModifiedDate(dteModifiedDate);
					datalist.add(objPaymentReciptBean);
				}
			} else if (checkAgainst.equalsIgnoreCase("Bill")) {
				reportName = servletContext.getRealPath("/WEB-INF/reports/webpms/rptBillNoPaymentRecipt.jrxml");
				/*
				 * String sqlPayment=
				 * "select a.strReceiptNo,ifnull(e.intNoOfAdults,'NA'),ifnull(e.intNoOfChild,'NA'),a.strReservationNo ,c.strBillNo,ifnull(e.strRoomType,'NA') "
				 * +
				 * ",DATE_FORMAT(g.dteArrivalDate,'%d-%m-%Y'),DATE_FORMAT(g.dteDepartureDate,'%d-%m-%Y'),ifnull(f.strFirstName,'NA'),ifnull(f.strMiddleName,'NA')"
				 * +
				 * ",ifnull(f.strLastName,'NA'),d.strSettlementDesc,a.dblPaidAmt,b.strRemarks,DATE_FORMAT(a.dteReceiptDate,'%d-%m-%Y') "
				 * +
				 * "from tblreceipthd a left outer join  tblreceiptdtl b on a.strReceiptNo=b.strReceiptNo "
				 * +
				 * "left outer join tblbillhd c on a.strRegistrationNo=c.strRegistrationNo  "
				 * +
				 * "left outer join  tblsettlementmaster d on b.strSettlementCode=d.strSettlementCode "
				 * +
				 * "left outer join tblreservationdtl e on a.strReservationNo=e.strReservationNo "
				 * +
				 * "left outer join tblreservationhd g on a.strReservationNo=g.strReservationNo "
				 * +
				 * "left outer join tblguestmaster f on e.strGuestCode=f.strGuestCode "
				 * +"where a.strReceiptNo='"+reciptNo+"' and a.strClientCode='"+
				 * clientCode+"'  ";
				 */

				String sqlPayment = " select a.strReceiptNo ,ifnull(c.intNoOfAdults,''),ifnull(c.intNoOfChild,''),a.strReservationNo,e.strBillNo, " 
						+ " ifnull(j.strRoomTypeDesc,'') ,DATE_FORMAT(c.dteArrivalDate,'%d-%m-%Y'), DATE_FORMAT(c.dteDepartureDate,'%d-%m-%Y'), " 
						+ " ifnull(h.strFirstName,''), ifnull(h.strMiddleName,''),ifnull(h.strLastName,''), "
						+ " f.strSettlementDesc,a.dblPaidAmt,b.strRemarks, DATE_FORMAT(a.dteReceiptDate,'%d-%m-%Y') " 
						+ " from tblreceipthd a ,tblreceiptdtl b,tblcheckinhd c ,tblcheckindtl d ,tblbillhd e , " + " tblsettlementmaster f ,tblguestmaster h, tblroom i,tblroomtypemaster j " 
						+ " where a.strReceiptNo=b.strReceiptNo and a.strCheckInNo =c.strCheckInNo  "
						+ " and c.strCheckInNo=d.strCheckInNo   and a.strReservationNo =c.strReservationNo " 
						+ " and a.strCheckInNo = e.strCheckInNo    " 
						+ " and a.strReservationNo =e.strReservationNo " 
						+ " and d.strCheckInNo = e.strCheckInNo    " 
						+ " and c.strCheckInNo = e.strCheckInNo  and d.strRoomNo = e.strRoomNo " 
						+ "  and d.strRoomNo = i.strRoomCode and i.strRoomTypeCode=j.strRoomTypeCode "
						+ " and b.strSettlementCode=f.strSettlementCode and d.strGuestCode=h.strGuestCode " 
						+ " and a.strReceiptNo='" + reciptNo + "' and a.strClientCode='" + clientCode + "' "
								+ " group by a.strReceiptNo ";

				List listOfPayment = objGlobalFunctionsService.funGetDataList(sqlPayment, "sql");

				for (int i = 0; i < listOfPayment.size(); i++) {
					Object PaymentData[] = (Object[]) listOfPayment.get(i);

					String strReceiptNo = PaymentData[0].toString();
					String intNoOfAdults = PaymentData[1].toString();
					String intNoOfChild = PaymentData[2].toString();
					String strReservationNo = PaymentData[3].toString();
					String strBillNo = PaymentData[4].toString();
					String strRoomType = PaymentData[5].toString();
					String dteArrivalDate = PaymentData[6].toString();
					String dteDepartureDate = PaymentData[7].toString();
					String strFirstName = PaymentData[8].toString();
					String strMiddleName = PaymentData[9].toString();
					String strLastName = PaymentData[10].toString();
					String strSettlementDesc = PaymentData[11].toString();
					String dblPaidAmt = PaymentData[12].toString();
					String strRemarks = PaymentData[13].toString();
					String dteReciptDate = PaymentData[14].toString();
					String dteModifiedDate = PaymentData[14].toString();

					clsPaymentReciptBean objPaymentReciptBean = new clsPaymentReciptBean();
					objPaymentReciptBean.setStrReceiptNo(strReceiptNo);
					objPaymentReciptBean.setIntNoOfAdults(intNoOfAdults);
					objPaymentReciptBean.setIntNoOfChild(intNoOfChild);
					objPaymentReciptBean.setStrReservationNo(strReservationNo);
					objPaymentReciptBean.setStrRoomType(strRoomType);
					objPaymentReciptBean.setDteArrivalDate(dteArrivalDate);
					objPaymentReciptBean.setDteDepartureDate(dteDepartureDate);
					objPaymentReciptBean.setStrFirstName(strFirstName+" ");
					objPaymentReciptBean.setStrMiddleName(strMiddleName+" ");
					objPaymentReciptBean.setStrLastName(strLastName);
					objPaymentReciptBean.setStrSettlementDesc(strSettlementDesc);
					objPaymentReciptBean.setDblPaidAmt(dblPaidAmt);
					objPaymentReciptBean.setStrRemarks(strRemarks);
					objPaymentReciptBean.setDteReciptDate(dteReciptDate);
					objPaymentReciptBean.setDteModifiedDate(dteModifiedDate);
					objPaymentReciptBean.setStrBillNo(strBillNo);
					datalist.add(objPaymentReciptBean);
				}
			} else {
				reportName = servletContext.getRealPath("/WEB-INF/reports/webpms/rptCheckInPaymentRecipt.jrxml");

				String sqlPayment = "SELECT a.strReceiptNo, IFNULL(c.intNoOfAdults,''), "
						+ "IFNULL(c.intNoOfChild,''), a.strReservationNo,c.strCheckInNo, "
						+ "IFNULL(g.strRoomTypeDesc,''),  DATE_FORMAT(c.dteArrivalDate,'%d-%m-%Y'), "
						+ "DATE_FORMAT(c.dteDepartureDate,'%d-%m-%Y'),   IFNULL(f.strFirstName,''), "
						+ "IFNULL(f.strMiddleName,''), IFNULL(f.strLastName,''),   "
						+ "IFNULL(d.strSettlementDesc,''),a.dblPaidAmt, b.strRemarks, "
						+ "DATE_FORMAT(a.dteReceiptDate,'%d-%m-%Y'),h.strRoomDesc "
						+ "FROM tblreceipthd a "
						+ "LEFT OUTER JOIN tblreceiptdtl b ON a.strReceiptNo=b.strReceiptNo "
						+ "LEFT OUTER JOIN tblcheckinhd c ON a.strRegistrationNo=c.strRegistrationNo "
						+ "LEFT OUTER JOIN tblcheckindtl e ON a.strCheckInNo=e.strCheckInNo "
						+ "LEFT OUTER JOIN tblroomtypemaster g ON g.strRoomTypeCode=e.strRoomType "
						+ "LEFT OUTER JOIN tblroom h ON e.strRoomNo=h.strRoomCode "
						+ "LEFT OUTER JOIN tblguestmaster f ON e.strGuestCode=f.strGuestCode "
						+ "LEFT OUTER JOIN tblsettlementmaster d ON b.strSettlementCode=d.strSettlementCode "
						+ "WHERE a.strReceiptNo='"+reciptNo+"' AND a.strClientCode='"+clientCode+"'";
				List listOfPayment = objGlobalFunctionsService.funGetDataList(sqlPayment, "sql");

				for (int i = 0; i < listOfPayment.size(); i++) {
					Object PaymentData[] = (Object[]) listOfPayment.get(i);
					
					String strCGS;
					String strSGST;
					String strReceiptNo = PaymentData[0].toString();
					String intNoOfAdults = PaymentData[1].toString();
					String intNoOfChild = PaymentData[2].toString();
					String strReservationNo = PaymentData[3].toString();
					String strCheckInNo = PaymentData[4].toString();
					String strRoomType = PaymentData[5].toString();
					String dteArrivalDate = PaymentData[6].toString();
					String dteDepartureDate = PaymentData[7].toString();
					String strFirstName = PaymentData[8].toString();
					String strMiddleName = PaymentData[9].toString();
					String strLastName = PaymentData[10].toString();
					String strSettlementDesc = PaymentData[11].toString();
					String dblPaidAmt = PaymentData[12].toString();
					String strRemarks = PaymentData[13].toString();
					String dteReciptDate = PaymentData[14].toString();
					String dteModifiedDate = PaymentData[14].toString();
					String strRoomDesc = PaymentData[15].toString();
					
					reportParams.put("pstrRoomDesc", strRoomDesc);
					
					clsPaymentReciptBean objPaymentReciptBean = new clsPaymentReciptBean();
					objPaymentReciptBean.setStrReceiptNo(strReceiptNo);
					objPaymentReciptBean.setIntNoOfAdults(intNoOfAdults);
					objPaymentReciptBean.setIntNoOfChild(intNoOfChild);
					objPaymentReciptBean.setStrReservationNo(strReservationNo);
					objPaymentReciptBean.setStrRoomType(strRoomType);
					objPaymentReciptBean.setDteArrivalDate(dteArrivalDate);
					objPaymentReciptBean.setDteDepartureDate(dteDepartureDate);
					objPaymentReciptBean.setStrFirstName(strFirstName+" ");
					objPaymentReciptBean.setStrMiddleName(strMiddleName+" ");
					objPaymentReciptBean.setStrLastName(strLastName);
					objPaymentReciptBean.setStrSettlementDesc(strSettlementDesc);
					objPaymentReciptBean.setDblPaidAmt(dblPaidAmt);
					objPaymentReciptBean.setStrRemarks(strRemarks);
					objPaymentReciptBean.setDteReciptDate(dteReciptDate);
					objPaymentReciptBean.setDteModifiedDate(dteModifiedDate);
					objPaymentReciptBean.setStrCheckInNo(strCheckInNo);
					datalist.add(objPaymentReciptBean);
				}
			}

			JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(datalist);
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
				resp.setHeader("Content-Disposition", "inline;filename=PaymentRecipt.pdf");
				exporter.exportReport();
				servletOutputStream.flush();
				servletOutputStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	
}
