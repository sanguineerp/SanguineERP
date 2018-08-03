package com.sanguine.crm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.crm.bean.clsInvoiceBean;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsCurrencyMasterService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@Controller
public class clsSalesAndSalesReturnSummaryReportController {

	
	@Autowired
	private ServletContext servletContext;

	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsCurrencyMasterService objCurrencyMasterService;

	@Autowired
	intfBaseService objBaseService;

	
	
		@RequestMapping(value = "/frmSalesAndSalesReturnSummaryReport", method = RequestMethod.GET)
		public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
		{
			String urlHits = "1";
			try
			{
				urlHits = request.getParameter("saddr").toString();
			}
			catch (NullPointerException e)
			{
				urlHits = "1";
			}
			model.put("urlHits", urlHits);
			String clientCode = request.getSession().getAttribute("clientCode").toString();
			Map<String, String> hmCurrency = objCurrencyMasterService.funCurrencyListToDisplay(clientCode);
			if (hmCurrency.isEmpty())
			{
				hmCurrency.put("", "");
			}
			model.put("currencyList", hmCurrency);
			
			

			if ("2".equalsIgnoreCase(urlHits))
			{
				return new ModelAndView("frmSalesAndSalesReturnSummaryReport_1", "command", new clsInvoiceBean());
			}
			else if ("1".equalsIgnoreCase(urlHits))
			{
				return new ModelAndView("frmSalesAndSalesReturnSummaryReport", "command", new clsInvoiceBean());
			}
			else
			{
				return null;
			}

		}
		
	
		@RequestMapping(value = "/rpSalesAndSalesReturnSummaryReport", method = RequestMethod.GET)
		private void funReportCall(@ModelAttribute("command") clsInvoiceBean objBean, HttpServletResponse resp, HttpServletRequest req)
		{
			
			if(objBean.getStrType().equalsIgnoreCase("Summary"))
			{
				funReport(objBean,req,resp,"rptSalesAndSalesReturnSummaryReport");
			}
			else if(objBean.getStrType().equalsIgnoreCase("Customer And SubGroup Wise Breakup"))
			{
				funReport(objBean,req,resp,"rptCustomerAndSubGruopWiseSalesReport");
			}
			else if(objBean.getStrType().equalsIgnoreCase("Sub GroupWise Tax Breakup"))
			{
				funReport(objBean,req,resp,"rptSubGruopWiseSalesBreakupReport");
			}

		}
	
		
		private void funReport(clsInvoiceBean objBean, HttpServletRequest req, HttpServletResponse resp,String reportName)
		{
			
			try
			{

				String clientCode = req.getSession().getAttribute("clientCode").toString();
				String companyName = req.getSession().getAttribute("companyName").toString();
				String userCode = req.getSession().getAttribute("usercode").toString();
				String propertyCode = req.getSession().getAttribute("propertyCode").toString();
				String locCode =  req.getSession().getAttribute("locationCode").toString();
				

				clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
				if (objSetup == null)
				{
					objSetup = new clsPropertySetupModel();
				}
				
				String fromDate = objBean.getDteFromDate();
				String toDate = objBean.getDteToDate();

				String dteFromDate = fromDate.split("-")[2] + "-" + fromDate.split("-")[1] + "-" + fromDate.split("-")[0];
				String dteToDate = toDate.split("-")[2] + "-" + toDate.split("-")[1] + "-" + toDate.split("-")[0];
				reportName = servletContext.getRealPath("/WEB-INF/reports/webcrm/"+reportName+".jrxml");
				String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");

				
				HashMap hm = new HashMap();
				hm.put("strCompanyName", companyName);
				hm.put("strUserCode", userCode);
				hm.put("strImagePath", imagePath);
				hm.put("strAddr1", objSetup.getStrAdd1());
				hm.put("strAddr2", objSetup.getStrAdd2());
				hm.put("strCity", objSetup.getStrCity());
				hm.put("strState", objSetup.getStrState());
				hm.put("strCountry", objSetup.getStrCountry());
				hm.put("strPin", objSetup.getStrPin());
				hm.put("dteFromDate", fromDate);
				hm.put("dteToDate", toDate);

				hm=funGetData(hm,locCode,dteFromDate,dteToDate,clientCode,objBean.getStrType());
				
				List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
				List fieldList = new ArrayList();
				fieldList.add(1);
				JasperDesign jd = JRXmlLoader.load(reportName);
				JasperReport jr = JasperCompileManager.compileReport(jd);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(fieldList);
				JasperPrint print = JasperFillManager.fillReport(jr, hm, beanCollectionDataSource);
				jprintlist.add(print);
				ServletOutputStream servletOutputStream = resp.getOutputStream();
				if (jprintlist.size() > 0)
				{
					JRExporter exporter = new JRPdfExporter();
					resp.setContentType("application/pdf");
					exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
					exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
					exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					exporter.exportReport();
					servletOutputStream.flush();
					servletOutputStream.close();

				}
				else
				{
					JRExporter exporter = new JRXlsExporter();
					resp.setContentType("application/xlsx");
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jprintlist);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletOutputStream);
					exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					exporter.exportReport();
					servletOutputStream.flush();
					servletOutputStream.close();
				}
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		
		
		
		
		private HashMap funGetData(HashMap hm,String locCode,String dteFromDate,String dteToDate,String clientCode,String reportType )
		{
			List<clsInvoiceBean> listOfSalesData = new ArrayList<clsInvoiceBean>();
			List<clsInvoiceBean> listOfSalesReturnData = new ArrayList<clsInvoiceBean>();
			StringBuilder sql = new StringBuilder();
			
			if(reportType.equals("Summary"))
			{
	            sql.append("SELECT count(a.strInvCode) as totalInvoice, ifnull(SUM(a.dblGrandTotal),0) as totalInvoiceAmt,saleRetutn.totalSR,saleRetutn.totalSRAmt "
	            		+ " FROM tblinvoicehd a,"
	            		+ " (SELECT count(a.strSRCode) as totalSR, ifnull(SUM(a.dblTotalAmt),0) as totalSRAmt"
	            		+ " FROM tblsalesreturnhd a"
	            		+ " WHERE Date(a.dteSRDate) between '"+dteFromDate+"' and '"+dteToDate+"' "
	            		+ " ORDER BY a.strSRCode)saleRetutn"
	            		+ " WHERE Date(a.dteInvDate) between '"+dteFromDate+"' and '"+dteToDate+"'  "
	            		+ " ORDER BY a.strInvCode ;");
				
				List listOfHeaderData = objGlobalFunctionsService.funGetList(sql.toString(), "sql");
				if (!listOfHeaderData.isEmpty()) {
					for (int i = 0; i < listOfHeaderData.size(); i++) 
					{   
						Object[] obj = (Object[]) listOfHeaderData.get(i);
						hm.put("Total Invoice Count", obj[0].toString());
						hm.put("Total Invoice Amt", Double.parseDouble(obj[1].toString()));
						hm.put("Total Return Count", obj[2].toString());
						hm.put("Total Return Amt", Double.parseDouble(obj[3].toString()));
				    }	
				}
				
				sql.setLength(0);
				sql.append("select b.strTaxDesc,c.dblPercent,ifnull(sum(b.dblTaxableAmt),0),ifnull(sum(b.dblTaxAmt),0)"
						+ " from tblinvoicehd a,tblinvtaxdtl b,tbltaxhd c"
						+ " where a.strInvCode=b.strInvCode and b.strTaxCode=c.strTaxCode and a.strClientCode='"+clientCode+"'"
						+ " group by b.strTaxCode") ;
				List listOfSaleData = objGlobalFunctionsService.funGetList(sql.toString(), "sql");
				if (!listOfSaleData.isEmpty()) 
				{
					for (int i = 0; i < listOfSaleData.size(); i++) {
						Object[] obj = (Object[]) listOfSaleData.get(i);
						clsInvoiceBean objInvBean = new clsInvoiceBean();
						objInvBean.setStrProdName(obj[0].toString());//tax Name
						objInvBean.setDblQty(Double.parseDouble(obj[1].toString()));// tax percent
						objInvBean.setDblTaxAmt(Double.parseDouble(obj[3].toString()));// total tax amt
						objInvBean.setDblTotalAmt(Double.parseDouble(obj[2].toString())); // total taxable amt
						listOfSalesData.add(objInvBean);
					}
				}
				
				sql.setLength(0);
				sql.append("select b.strTaxDesc,c.dblPercent, ifnull(SUM(b.strTaxableAmt),0), ifnull(SUM(b.strTaxAmt),0) "
						+ " from tblsalesreturnhd a,tblsalesreturntaxdtl b,tbltaxhd c"
						+ " where a.strSRCode=b.strSRCode AND b.strTaxCode=c.strTaxCode  and a.strClientCode='"+clientCode+"' "
						+ " and date(a.dteSRDate) between '"+dteFromDate+"' and '"+dteToDate+"'"
						+ " group BY b.strTaxCode ") ;
				List listOfSalesRtnData = objGlobalFunctionsService.funGetList(sql.toString(), "sql");
				if (!listOfSalesRtnData.isEmpty()) 
				{
					for (int i = 0; i < listOfSalesRtnData.size(); i++) {
						Object[] obj = (Object[]) listOfSalesRtnData.get(i);
						clsInvoiceBean objInvBean = new clsInvoiceBean();
						objInvBean.setStrProdName(obj[0].toString());//tax Name
						objInvBean.setDblQty(Double.parseDouble(obj[1].toString()));// tax percent
						objInvBean.setDblTaxAmt(Double.parseDouble(obj[3].toString()));// total tax amt
						objInvBean.setDblTotalAmt(Double.parseDouble(obj[2].toString())); // total taxable amt
						listOfSalesReturnData.add(objInvBean);
					}
				}		
					
			}
			else if(reportType.equalsIgnoreCase("Customer And SubGroup Wise Breakup"))
			{
		       sql.append("SELECT g.strSGCode,g.strSGName,ifnull(SUM(b.dblGrandTotal),0.0),ifnull(SUM(d.dblDiscAmt),0.0),ifnull(sum(c.dblQty),0.0) \n" 
				+ " FROM tblpartymaster a,tblinvoicedtl c,tblinvoicehd b left outer join tblsalesreturnhd d on b.strCustCode=d.strCustCode and b.strLocCode=d.strLocCode,"
				+ " tblproductmaster f,tblsubgroupmaster g\n"  
				+ " WHERE b.strCustCode=a.strPCode AND b.strLocCode='"+locCode+"' \n"  
				+ " AND DATE(b.dteInvDate) BETWEEN '"+dteFromDate+"' AND '"+dteToDate+"' AND b.strClientCode='"+clientCode+"' \n" 
				+ " and b.strInvCode=c.strInvCode\n"  
				+ " and c.strProdCode=f.strProdCode and g.strSGCode=f.strSGCode\n" 
				+ " GROUP BY g.strSGCode");
				
				List listOfSubGroupWiseData = objGlobalFunctionsService.funGetList(sql.toString(), "sql");
				if (!listOfSubGroupWiseData.isEmpty()) {
					for (int i = 0; i < listOfSubGroupWiseData.size(); i++) {
						Object[] obj = (Object[]) listOfSubGroupWiseData.get(i);
						clsInvoiceBean objInvBean = new clsInvoiceBean();
						objInvBean.setStrProdCode(obj[0].toString());//subGroup Code
						objInvBean.setStrProdName(obj[1].toString());//SubGroup Name
						objInvBean.setDblQty(Double.parseDouble(obj[2].toString()));//sale amount
						objInvBean.setDblTotalAmt(Double.parseDouble(obj[3].toString()));//return amount
						listOfSalesData.add(objInvBean);
					}	
				}	
				sql.setLength(0);
				sql.append("SELECT b.strCustCode,a.strPName, ifnull(SUM(b.dblGrandTotal),0.0),ifnull(SUM(d.dblDiscAmt),0.0),ifnull(sum(c.dblQty),0.0) \n"  
				+ " FROM tblpartymaster a,tblinvoicedtl c,tblinvoicehd b left outer join tblsalesreturnhd d on b.strCustCode=d.strCustCode and b.strLocCode=d.strLocCode"
				+ " WHERE b.strCustCode=a.strPCode AND b.strLocCode='"+locCode+"' \n"  
				+ " AND DATE(b.dteInvDate) BETWEEN '"+dteFromDate+"' AND '"+dteToDate+"' AND b.strClientCode='"+clientCode+"' \n"  
				+ " and b.strInvCode=c.strInvCode\r\n" 
				+ " GROUP BY b.strCustCode" );
				List listOfCustomerData = objGlobalFunctionsService.funGetList(sql.toString(), "sql");
				if (!listOfCustomerData.isEmpty()) 
				{
					for (int i = 0; i < listOfCustomerData.size(); i++) {
						Object[] obj = (Object[]) listOfCustomerData.get(i);
						clsInvoiceBean objInvBean = new clsInvoiceBean();
						objInvBean.setStrProdCode(obj[0].toString());//customer Code
						objInvBean.setStrProdName(obj[1].toString());//customer Name
						objInvBean.setDblQty(Double.parseDouble(obj[2].toString()));// sale amount
						objInvBean.setDblTotalAmt(Double.parseDouble(obj[3].toString()));//return amount
						listOfSalesReturnData.add(objInvBean);
					}
				}	
			}
			else
			{
				
				sql.append("SELECT b.strTaxDesc,c.dblPercent, ifnull(SUM(b.dblTaxableAmt),0.0), ifnull(SUM(b.dblTaxAmt),0.0),f.strSGName,f.strSGCode\n" 
						+ " FROM tblinvoicehd a,tblinvtaxdtl b,tbltaxhd c,tblinvoicedtl d,tblproductmaster e,tblsubgroupmaster f\n"  
						+ " WHERE a.strInvCode=b.strInvCode AND b.strTaxCode=c.strTaxCode AND a.strInvCode=d.strInvCode \n" 
						+ " AND d.strProdCode=e.strProdCode AND e.strSGCode=f.strSGCode and a.strClientCode='"+clientCode+"' \n" 
						+ " and date(a.dteInvDate) between '"+dteFromDate+"' and '"+dteToDate+"'\n" 
						+ " GROUP BY f.strSGCode,b.strTaxCode\n");
				List listOfSubGroupWiseData = objGlobalFunctionsService.funGetList(sql.toString(), "sql");
				if (!listOfSubGroupWiseData.isEmpty()) {
					for (int i = 0; i < listOfSubGroupWiseData.size(); i++) {
						Object[] obj = (Object[]) listOfSubGroupWiseData.get(i);
						clsInvoiceBean objInvBean = new clsInvoiceBean();
						objInvBean.setStrProdCode(obj[0].toString());//Tax Desc
						objInvBean.setDblQty(Double.parseDouble(obj[1].toString()));//Tax %
						objInvBean.setDblTotalAmt(Double.parseDouble(obj[2].toString()));//Sale Amount
						objInvBean.setDblTaxAmt(Double.parseDouble(obj[3].toString()));//Tax Amount
						objInvBean.setStrProdName(obj[4].toString()); //subGroup Name
						objInvBean.setStrCustCode(obj[5].toString()); //subGroup code
						listOfSalesData.add(objInvBean);
					}
					
				}
				
				sql.setLength(0);
				sql.append("SELECT b.strTaxDesc,c.dblPercent, ifnull(SUM(b.strTaxableAmt),0.0), ifnull(SUM(b.strTaxAmt),0.0),f.strSGName,f.strSGCode\n"  
						+ " FROM tblsalesreturnhd a,tblsalesreturntaxdtl b,tbltaxhd c,tblsalesreturndtl d,tblproductmaster e,tblsubgroupmaster f\n" 
						+ " WHERE a.strSRCode=b.strSRCode AND b.strTaxCode=c.strTaxCode AND a.strSRCode=d.strSRCode \n"  
						+ " AND d.strProdCode=e.strProdCode AND e.strSGCode=f.strSGCode and a.strClientCode='"+clientCode+"' \n" 
						+ " and date(a.dteSRDate) between '"+dteFromDate+"' and '"+dteToDate+"'\n"  
						+ " GROUP BY f.strSGCode,b.strTaxCode\n");
				List listOfCustomerData = objGlobalFunctionsService.funGetList(sql.toString(), "sql");
				if (!listOfCustomerData.isEmpty()) {
					for (int i = 0; i < listOfCustomerData.size(); i++) {
						Object[] obj = (Object[]) listOfCustomerData.get(i);
						clsInvoiceBean objInvBean = new clsInvoiceBean();
						objInvBean.setStrProdCode(obj[0].toString());//Tax Desc
						objInvBean.setDblQty(Double.parseDouble(obj[1].toString()));//Tax %
						objInvBean.setDblTotalAmt(Double.parseDouble(obj[2].toString()));//Sale Amount
						objInvBean.setDblTaxAmt(Double.parseDouble(obj[3].toString()));//Tax Amount
						objInvBean.setStrProdName(obj[4].toString()); //subGroup Name
						objInvBean.setStrCustCode(obj[5].toString()); //subGroup code
						listOfSalesReturnData.add(objInvBean);
					}
					
				}
			}
			hm.put("listOfcustomerData", listOfSalesReturnData);
			hm.put("listOfData", listOfSalesData);
			return hm;
		}
		
}
