package com.sanguine.webbanquets.controller;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.Connection;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsBillPassingService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsGroupMasterService;
import com.sanguine.service.clsLocationMasterService;
import com.sanguine.service.clsRecipeMasterService;
import com.sanguine.service.clsRequisitionService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.service.clsStkAdjustmentService;
import com.sanguine.service.clsSubGroupMasterService;
import com.sanguine.service.clsSupplierMasterService;
import com.sanguine.util.clsReportBean;
import com.sanguine.webbanquets.bean.clsAdvanceStatusReportBean;

@Controller
public class clsAdvanceStatusReportController {

	@Autowired
	private clsGlobalFunctionsService objGlobalService;
	@Autowired
	private clsSetupMasterService objSetupMasterService;
	@Autowired
	private ServletContext servletContext;
	private clsGlobalFunctions objGlobal = null;
	@Autowired
	private clsSupplierMasterService objSupplierMasterService;
	@Autowired
	private clsLocationMasterService objLocationMasterService;
	@Autowired
	private clsSubGroupMasterService objSubGroupMasterService;
	@Autowired
	clsBillPassingService objBillPassingService;
	@Autowired
	private clsGlobalFunctions objGlobalFunctions;
	@Autowired
	private clsGroupMasterService objGrpMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	@Autowired
	private clsLocationMasterService objLocService;
	@Autowired
	private clsRequisitionService objReqService;
	@Autowired
	private clsStkAdjustmentService objStkAdjService;
	@Autowired
	private clsRecipeMasterService objRecipeMasterService;

	@RequestMapping(value = "/frmAdvanceStatusReport", method = RequestMethod.GET)
	public ModelAndView funOpenAdvanceStatus(Map<String, Object> model, HttpServletRequest request) {
		request.getSession().setAttribute("formName", "frmAdvanceStatusReport");

		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmAdvanceStatusReport_1", "command", new clsReportBean());
		} else {
			return new ModelAndView("frmAdvanceStatusReport", "command", new clsReportBean());
		}

	}
	
	@RequestMapping(value = "/rptAdvanceStatusReport", method = RequestMethod.POST)
	private void funAdvanceStatus(@ModelAttribute("command") clsReportBean objBean, HttpServletResponse resp, HttpServletRequest req) {
		funCallAdvanceStatusReport(objBean, resp, req);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void funCallAdvanceStatusReport(clsReportBean objBean, HttpServletResponse resp, HttpServletRequest req) {
		objGlobal = new clsGlobalFunctions();
		Connection con = objGlobal.funGetConnection(req);
		try {	
			
			String type = objBean.getStrDocType();
			String fromDate = objBean.getDtFromDate();
			String toDate = objBean.getDtToDate();
			String fromTempDate = objGlobal.funGetDate("yyyy-MM-dd", fromDate);
			String toTempDate = objGlobal.funGetDate("yyyy-MM-dd", toDate);
			String strLocCodes = "";
			String strSuppCodes = "";
			ArrayList datalist = new ArrayList();
			HashMap hm = new HashMap();
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String companyName = req.getSession().getAttribute("companyName").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
					
			
			clsPropertySetupModel objSetup;
			objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);

			if (objSetup == null) {
				objSetup = new clsPropertySetupModel();
			}
			
			String reportName = servletContext.getRealPath("/WEB-INF/reports/webbanquet/rptAdvanceStatusReport.jrxml");
			String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
			String sqlPayment ="SELECT a.strReceiptNo,a.strReservationNo, "
					+ "IFNULL(b.strCustomerCode,''),IFNULL(c.strPname,''),e.strSettlementDesc,IFNULL(b.dteBookingDate,''),a.dblReceiptAmt "				
					+ "FROM tblreceipthd a "
					+ "LEFT OUTER JOIN tblbqbookinghd b ON a.strReservationNo=b.strBookingNo "
					+ "LEFT OUTER JOIN "+webStockDB+".tblpartymaster c ON c.strPCode=b.strCustomerCode "
					+ "LEFT OUTER JOIN tblreceiptdtl d ON d.strReceiptNo=a.strReceiptNo "
					+ "LEFT OUTER JOIN tblsettlementmaster e ON e.strSettlementCode=d.strSettlementCode "
					+ "WHERE a.strAgainst='Banquet' ORDER BY e.strSettlementDesc ";
			List listOfPayment = objGlobalFunctionsService.funGetDataList(sqlPayment, "sql");
			
		
			clsAdvanceStatusReportBean objAdvanceStatusReportBean ;
			
			for (int i = 0; i < listOfPayment.size(); i++) {
				Object PaymentData[] = (Object[]) listOfPayment.get(i);
				objAdvanceStatusReportBean = new clsAdvanceStatusReportBean();
				objAdvanceStatusReportBean.setStrReceiptNo(PaymentData[0].toString());
				objAdvanceStatusReportBean.setStrReservationNo(PaymentData[1].toString());
				objAdvanceStatusReportBean.setStrCustomerCode(PaymentData[2].toString());
				objAdvanceStatusReportBean.setStrPname(PaymentData[3].toString());
				objAdvanceStatusReportBean.setStrSettlementDesc(PaymentData[4].toString());
				objAdvanceStatusReportBean.setDteBookingDate(objGlobal.funGetDate("dd-MM-yyyy", PaymentData[5].toString()+" "));
				objAdvanceStatusReportBean.setDblReceiptAmt(Double.parseDouble(PaymentData[6].toString()));
				datalist.add(objAdvanceStatusReportBean);
			}
		
			JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(datalist);
			JasperDesign jd = JRXmlLoader.load(reportName);
			JasperReport jr = JasperCompileManager.compileReport(jd);			
			hm.put("strCompanyName", companyName);
			hm.put("strUserCode", userCode);
			hm.put("strImagePath", imagePath);
			hm.put("strAddr1", objSetup.getStrAdd1());
			hm.put("strAddr2", objSetup.getStrAdd2());
			hm.put("strCity", objSetup.getStrCity());
			hm.put("strState", objSetup.getStrState());
			hm.put("strCountry", objSetup.getStrCountry());
			hm.put("strPin", objSetup.getStrPin());
			hm.put("fromDate", fromDate);
			hm.put("toDate", toDate);
			hm.put("LocName", "");
			JasperPrint jp = JasperFillManager.fillReport(jr, hm, beanCollectionDataSource);
			List jprintlist = new ArrayList<JasperPrint>();
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
