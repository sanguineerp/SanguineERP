package com.sanguine.webpms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webpms.bean.clsBlockRoomReportBean;
import com.sanguine.webpms.bean.clsComplimentryReportBean;

@Controller
public class clsBlockRoomReportController {
	
	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	
	@RequestMapping(value = "/frmBlockRoomReport", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBlockRoomReport_1", "command", new clsBlockRoomReportBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBlockRoomReport", "command", new clsBlockRoomReportBean());
		} else {
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/rptBlockRoomReport", method = RequestMethod.GET)
	public void funGenerateCheckInListReport(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		
		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String propertyCode = req.getSession().getAttribute("propertyCode").toString();
		String companyName = req.getSession().getAttribute("companyName").toString();
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
		if (objSetup == null) {
			objSetup = new clsPropertySetupModel();
		}
		String reportName = servletContext.getRealPath("/WEB-INF/reports/webpms/rptBlockRoomReport.jrxml");
		String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
		List<clsBlockRoomReportBean> dataList = new ArrayList<clsBlockRoomReportBean>();

		HashMap reportParams = new HashMap();
		
		
		/*String sql = "SELECT d.strRoomDesc,Date(a.dteArrivalDate), DATE(a.dteDepartureDate),"
				+ "c.strFirstName,c.strMiddleName,c.strLastName,e.dblRoomTerrif ,a.strCheckInNo,f.strReasonDesc,a.strRemarks "
				+ "FROM tblcheckinhd a,tblcheckindtl b,tblguestmaster c,tblroom d,"
				+ "tblroomtypemaster e,tblreasonmaster f "
				+ "WHERE a.strCheckInNo=b.strCheckInNo and b.strGuestCode=c.strGuestCode "
				+ "and b.strRoomNo=d.strRoomCode and b.strRoomTYpe=e.strRoomTypeCode "
				+ "and a.strComplimentry='Y' and Date(a.dteCheckInDate) between '"+fromDate+"' and '"+toDate+"' "
				+ "and a.strClientCode='"+clientCode+"' AND a.strReasonCode=f.strReasonCode";*/
		
		String sql="SELECT a.strRoomCode,b.strRoomDesc,a.dteValidFrom,a.dteValidTo,c.strReasonDesc,a.strRemark,a.strClientCode "
				+ "FROM tblblockroom a,tblroom b,tblreasonmaster c  "
				+ "WHERE a.strRoomCode=b.strRoomCode AND a.strClientCode=b.strClientCode "
				+ "and a.strReason=c.strReasonCode and Date(a.dteValidFrom) between '"+fromDate+"' and '"+toDate+"' AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' AND c.strClientCode='"+clientCode+"'";
		
		
		List listBlockRoom = objGlobalFunctionsService.funGetDataList(sql, "sql");
		clsBlockRoomReportBean objBean;
		for(int index=0;index<listBlockRoom.size();index++)
		{
			objBean = new clsBlockRoomReportBean();
			Object[] obj = (Object[]) listBlockRoom.get(index);
			
			objBean.setStrRoomCode(obj[0].toString());
			objBean.setStrRoomNo(obj[1].toString());
			objBean.setDteValidFrom(objGlobal.funGetDate("dd-MM-yyyy", obj[2].toString()));
			objBean.setDteValidTo(objGlobal.funGetDate("dd-MM-yyyy", obj[3].toString()));
			objBean.setStrReason(obj[4].toString());
			objBean.setStrRemark(obj[5].toString());
			objBean.setStrClientCode(obj[6].toString());
			
			
			dataList.add(objBean);
		}
		

		String[] fdte = fromDate.split("-");
		fromDate = fdte[2] + "-" + fdte[1] + "-" + fdte[0];

		String[] todte = toDate.split("-");
		toDate = todte[2] + "-" + todte[1] + "-" + todte[0];
		
		HashMap hm = new HashMap();
		hm.put("pCompanyName", companyName);
		hm.put("strUserCode", userCode);
		hm.put("strImagePath", imagePath);
		hm.put("pAddress1", objSetup.getStrAdd1());
		hm.put("pAddress2", objSetup.getStrAdd2());
		hm.put("strCity", objSetup.getStrCity());
		hm.put("strState", objSetup.getStrState());
		hm.put("strCountry", objSetup.getStrCountry());
		hm.put("strPin", objSetup.getStrPin());
		hm.put("dataList", dataList);
		hm.put("fromDate", fromDate);
		hm.put("toDate", toDate);
		hm.put("pContactDetails", "");
		
		if (dataList != null && !dataList.isEmpty()){
		
		JasperDesign jd = JRXmlLoader.load(reportName);
		JasperReport jr = JasperCompileManager.compileReport(jd);

		JasperPrint jp = JasperFillManager.fillReport(jr, hm, new JREmptyDataSource());
		List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		jprintlist.add(jp);
		if (jprintlist.size() > 0) {
			ServletOutputStream servletOutputStream = resp.getOutputStream();
			String type = "pdf";
			if (type.trim().equalsIgnoreCase("pdf")) {
				JRExporter exporter = new JRPdfExporter();
				resp.setContentType("application/pdf");
				exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
				exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
				exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
				resp.setHeader("Content-Disposition", "inline;filename=" + "rptGuestListReport." + type.trim());
				exporter.exportReport();
				servletOutputStream.flush();
				servletOutputStream.close();
			} else {
				JRExporter exporter = new JRXlsExporter();
				resp.setContentType("application/xlsx");
				exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jprintlist);
				exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletOutputStream);
				exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
				resp.setHeader("Content-Disposition", "attachment;filename=" + "rptGuestListReport." + type.trim());
				exporter.exportReport();
				servletOutputStream.flush();
				servletOutputStream.close();
			}
		}
		} else {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			/*resp.getWriter().append("No Record Found");*/

			try {
				resp.getWriter().append("No Record Found");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		

	}

}
