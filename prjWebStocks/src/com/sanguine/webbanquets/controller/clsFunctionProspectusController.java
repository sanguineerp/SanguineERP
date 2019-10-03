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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webbanquets.bean.clsFunctionProspectusBean;
import com.sanguine.webbanquets.model.clsMenuHeadMasterModel;
import com.sanguine.webpms.bean.clsFolioPrintingBean;

@Controller
public class clsFunctionProspectusController
{
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	@Autowired
	private clsSetupMasterService objSetupMasterService;
	@Autowired
	private ServletContext servletContext;
	
	@RequestMapping(value = "/frmFunctionProspectus", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmFunctionProspectus_1", "command", new clsFunctionProspectusBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmFunctionProspectus", "command", new clsFunctionProspectusBean());
		} else {
			return null;
		}
	}
	
	@RequestMapping(value = "/loadBookingCode", method = RequestMethod.GET)
	public @ResponseBody String funLoadMasterData(@RequestParam("docCode") String docCode,HttpServletRequest request){
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String sql="";
		String returnString="";
		try
		{
			sql="SELECT a.strBookingNo FROM tblbqbookinghd a WHERE a.strBookingNo='"+docCode+"' "
					+ "AND a.strClientCode='"+clientCode+"' ";
			List listData=objGlobalFunctionsService.funGetList(sql, "sql");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return returnString;
	}
	
	@RequestMapping(value = "/rptFunctionProspectus", method = RequestMethod.GET)
	public void funGenerateFolioPrintingReport(clsFunctionProspectusBean objFunctionProspectusBean, HttpServletRequest req, HttpServletResponse resp) 
	{
		try 
		{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			String companyName = req.getSession().getAttribute("companyName").toString();
			String sql="";
			String fromDate="",toDate="";
			HashMap reportParams = new HashMap();
			List dataList = new ArrayList();
			List listData=objGlobalFunctionsService.funGetList(sql, "sql");
			clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
			if (objSetup == null) {
				objSetup = new clsPropertySetupModel();
			}
			String reportName = servletContext.getRealPath("/WEB-INF/reports/webBanquets/rptFunctionProspectus.jrxml");
			
			reportParams.put("pCompanyName", companyName);
			reportParams.put("pAddress1", objSetup.getStrAdd1() + "," + objSetup.getStrAdd2() + "," + objSetup.getStrCity());
			reportParams.put("pAddress2", objSetup.getStrState() + "," + objSetup.getStrCountry() + "," + objSetup.getStrPin());
			reportParams.put("pContactDetails", "9182966345");
			reportParams.put("pBookingNo", "101");
			reportParams.put("pDate", "");
			reportParams.put("pTime", "");
			reportParams.put("pArea", "");
			reportParams.put("pFunction", "Company Function");
			reportParams.put("pPAX", "100-150");
			reportParams.put("pDuration", "12:00 - 5:00");
			reportParams.put("pEventCoordinator", "Sanguine");
			reportParams.put("strUserCode", userCode);
			JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataList);
			JasperDesign jd = JRXmlLoader.load(reportName);
			JasperReport jr = JasperCompileManager.compileReport(jd);
			JasperPrint jp = JasperFillManager.fillReport(jr, reportParams, beanCollectionDataSource);
			List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
			if (jp != null) 
			{
				jprintlist.add(jp);
				ServletOutputStream servletOutputStream = resp.getOutputStream();
				JRExporter exporter = new JRPdfExporter();
				resp.setContentType("application/pdf");
				exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
				exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
				exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
				resp.setHeader("Content-Disposition", "inline;filename=FunctionProspectus_" + fromDate + "_To_" + toDate + "_" + userCode + ".pdf");
				exporter.exportReport();
				servletOutputStream.flush();
				servletOutputStream.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
		
	

}
