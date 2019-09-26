package com.sanguine.webpms.controller;

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
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webpms.bean.clsChangeRoomReportBean;
import com.sanguine.webpms.bean.clsFolioPrintingBean;
import com.sanguine.webpms.bean.clsReceiptReportBean;
@Controller
public class clsReceiptReportController {


	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	@RequestMapping(value = "/frmReceiptReport", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmReceiptReport_1", "command", new clsReceiptReportBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmReceiptReport", "command", new clsReceiptReportBean());
		} else {
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/rptReceipt", method = RequestMethod.GET)
	public void funGenerateCheckInListReport(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate, HttpServletRequest req, HttpServletResponse resp) {
		try {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			String companyName = req.getSession().getAttribute("companyName").toString();
			String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
			String fDate=fromDate.split("-")[2]+"-"+fromDate.split("-")[1]+"-"+fromDate.split("-")[0];
			String tDate=toDate.split("-")[2]+"-"+toDate.split("-")[1]+"-"+toDate.split("-")[0];
			clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
			if (objSetup == null) {
				objSetup = new clsPropertySetupModel();
			}
			
			String reportName = servletContext.getRealPath("/WEB-INF/reports/webpms/rptReceiptReport.jrxml");
			String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
			
			List<clsFolioPrintingBean> dataList = new ArrayList<clsFolioPrintingBean>();
			@SuppressWarnings("rawtypes")
			String propNameSql = "select a.strPropertyName from "+webStockDB+".tblpropertymaster a " + " where a.strPropertyCode='" + propertyCode + "' and a.strClientCode='" + clientCode + "' ";
			List listPropName = objGlobalFunctionsService.funGetDataList(propNameSql, "sql");
			String propName = "";
			if (listPropName.size() > 0) {
				propName = listPropName.get(0).toString();
			}

			HashMap reportParams = new HashMap();

			reportParams.put("pCompanyName", companyName);
			reportParams.put("pAddress1", objSetup.getStrAdd1() + "," + objSetup.getStrAdd2() + "," + objSetup.getStrCity());
			reportParams.put("pAddress2", objSetup.getStrState() + "," + objSetup.getStrCountry() + "," + objSetup.getStrPin());
			reportParams.put("pContactDetails", "");
			reportParams.put("strImagePath", imagePath);
			reportParams.put("strUserCode", userCode);
			reportParams.put("pFromDate", fDate);
			reportParams.put("pTtoDate", tDate);
			reportParams.put("propName", propName);
			
			/*String sqlSettlement = "select a.strSettlementDesc from tblsettlementmaster a ";
			List listOfChangeRoom = objGlobalFunctionsService.funGetDataList(sqlSettlement, "sql");
			ArrayList fieldList = new ArrayList();
			String settlementAgainst = "";
			for (int i = 0; i < listOfChangeRoom.size(); i++) {
				
				clsReceiptReportBean objBean = new clsReceiptReportBean();
				objBean.setStrSettlement(listOfChangeRoom.get(i).toString());*/
				clsReceiptReportBean objBean = null;
				ArrayList fieldList = new ArrayList();
				String sqlSettlementAmt = "SELECT c.strSettlementDesc,b.strSettlementCode,a.strAgainst, SUM(b.dblSettlementAmt) "
						+ "FROM tblreceipthd a, tblreceiptdtl b,tblsettlementmaster c WHERE a.strReceiptNo=b.strReceiptNo AND "
						+ "b.strSettlementCode=c.strSettlementCode AND DATE(a.dteReceiptDate) BETWEEN '"+fromDate+"' AND '"+toDate+"' "
						+ "AND a.strClientCode = '"+clientCode+"'  AND B.strClientCode = '"+clientCode+"' AND C.strClientCode = '"+clientCode+"' GROUP BY a.strAgainst,b.strSettlementCode ";
				List listSettlement = objGlobalFunctionsService.funGetDataList(sqlSettlementAmt, "sql");
				String settlementType = "";
				double settlementAmt = 0.0;
				for(int index=0;index<listSettlement.size();index++)
				{
					Object[] obj = (Object[]) listSettlement.get(index);
					settlementType=obj[0].toString();
					if(settlementType.equalsIgnoreCase("Cash"))
					{	objBean = new clsReceiptReportBean();
						objBean.setStrSettlementType(settlementType);
						objBean.setStrSettlementAgainst(obj[2].toString());
						if(objBean.getStrSettlementAgainst().equalsIgnoreCase("Bill"))
						{
						objBean.setDblSettlementAmtBill(Double.parseDouble(obj[3].toString()));
						}
						else if(objBean.getStrSettlementAgainst().equalsIgnoreCase("Folio-No"))
						{
						objBean.setDblSettlementAmtFolio(Double.parseDouble(obj[3].toString()));
						}
						else 
						{
						objBean.setDblSettlementAmtReservation(Double.parseDouble(obj[3].toString()));
						}
						fieldList.add(objBean);
					}
					else if(settlementType.equalsIgnoreCase("Credit"))
					{
						objBean = new clsReceiptReportBean();
						objBean.setStrSettlementType(settlementType);
						objBean.setStrSettlementAgainst(obj[2].toString());
						if(objBean.getStrSettlementAgainst().equalsIgnoreCase("Bill"))
						{
						objBean.setDblSettlementAmtBill(Double.parseDouble(obj[3].toString()));
						}
						else if(objBean.getStrSettlementAgainst().equalsIgnoreCase("Folio-No"))
						{
						objBean.setDblSettlementAmtFolio(Double.parseDouble(obj[3].toString()));
						}
						else 
						{
						objBean.setDblSettlementAmtReservation(Double.parseDouble(obj[3].toString()));
						}
						fieldList.add(objBean);
					}
					
					else
					{
						objBean = new clsReceiptReportBean();
						objBean.setStrSettlementType(settlementType);
						objBean.setStrSettlementAgainst(obj[2].toString());
						if(objBean.getStrSettlementAgainst().equalsIgnoreCase("Bill"))
						{
						objBean.setDblSettlementAmtBill(Double.parseDouble(obj[3].toString()));
						}
						else if(objBean.getStrSettlementAgainst().equalsIgnoreCase("Folio-No"))
						{
						objBean.setDblSettlementAmtFolio(Double.parseDouble(obj[3].toString()));
						}
						else 
						{
						objBean.setDblSettlementAmtReservation(Double.parseDouble(obj[3].toString()));
						}
						fieldList.add(objBean);
					}
				}
			
			
			
			JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(fieldList);
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
				resp.setHeader("Content-Disposition", "inline;filename=ChangeRoomReport.pdf");
				exporter.exportReport();
				servletOutputStream.flush();
				servletOutputStream.close();
			}
			
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
