package com.sanguine.webbooks.controller;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.itextpdf.text.pdf.events.IndexEvents.Entry;
import com.mysql.jdbc.Connection;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsCurrencyMasterService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webbooks.bean.clsCreditorOutStandingReportBean;
import com.sanguine.webbooks.bean.clsIncomeStmtReportBean;

@Controller
public class clsBalanceSheetController {

	@Autowired
	private clsGlobalFunctions objGlobal;
	
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

	
		@RequestMapping(value = "/frmBalanceSheet", method = RequestMethod.GET)
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
				return new ModelAndView("frmBalanceSheet_1", "command", new clsCreditorOutStandingReportBean());
			}
			else if ("1".equalsIgnoreCase(urlHits))
			{
				return new ModelAndView("frmBalanceSheet", "command", new clsCreditorOutStandingReportBean());
			}
			else
			{
				return null;
			}

		}
		
		
		@RequestMapping(value = "/rptBalanceSheet1", method = RequestMethod.GET)
		private void funReport(@ModelAttribute("command") clsCreditorOutStandingReportBean objBean, HttpServletResponse resp, HttpServletRequest req)
		{
			objGlobal = new clsGlobalFunctions();
			Connection con = objGlobal.funGetConnection(req);
			try
			{

				String clientCode = req.getSession().getAttribute("clientCode").toString();
				String companyName = req.getSession().getAttribute("companyName").toString();
				String userCode = req.getSession().getAttribute("usercode").toString();
				String propertyCode = req.getSession().getAttribute("propertyCode").toString();
				String currencyCode = objBean.getStrCurrency();
//				double conversionRate = 1;
				String webStockDB = req.getSession().getAttribute("WebStockDB").toString();
				StringBuilder sbSql = new StringBuilder();
//				sbSql.append("select dblConvToBaseCurr from " + webStockDB + ".tblcurrencymaster where strCurrencyCode='" + currencyCode + "' and strClientCode='" + clientCode + "' ");
//				try
//				{
//					List list = objBaseService.funGetList(sbSql, "sql");
//					conversionRate = Double.parseDouble(list.get(0).toString());
//				}
//				catch (Exception e)
//				{
//					e.printStackTrace();
//				}

				double currValue = Double.parseDouble(req.getSession().getAttribute("currValue").toString());

				clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
				if (objSetup == null)
				{
					objSetup = new clsPropertySetupModel();
				}
				String type = "PDF";
				String fromDate = objBean.getDteFromDate();
				String toDate = objBean.getDteToDate();

				String fd = fromDate.split("-")[0];
				String fm = fromDate.split("-")[1];
				String fy = fromDate.split("-")[2];

				String td = toDate.split("-")[0];
				String tm = toDate.split("-")[1];
				String ty = toDate.split("-")[2];

				String dteFromDate = fy + "-" + fm + "-" + fd;
				String dteToDate = ty + "-" + tm + "-" + td;
				String reportName = servletContext.getRealPath("/WEB-INF/reports/webbooks/rptBalanceSheet.jrxml");
				String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");

				ArrayList dataList = new ArrayList();

			

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

				
				List<clsIncomeStmtReportBean> listOfBalancesheet = new ArrayList<clsIncomeStmtReportBean>();
				List<clsIncomeStmtReportBean> listOfDebtor = new ArrayList<clsIncomeStmtReportBean>();
				 /* Income statement
				 * 
				 * 
				 */

				List <String>listACC=new ArrayList<String>();
				
				

				
				BigDecimal totalSalesAmt=new BigDecimal(0);
				BigDecimal totalOtherExpenses=new BigDecimal(0);
				BigDecimal totalOtherIncome=new BigDecimal(0);
				BigDecimal totalExpenseAmt=new BigDecimal(0);
				
				Map<String,clsIncomeStmtReportBean> hmSalesIncStmt = new HashMap<String,clsIncomeStmtReportBean>();
				String startDate = req.getSession().getAttribute("startDate").toString();

				String[] sp = startDate.split(" ");
				String[] spDate = sp[0].split("/");
				startDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
				int cnt=1;
				/*if (!startDate.equals(dteFromDate)) {
					String tempFromDate = dteFromDate.split("-")[2] + "-" + dteFromDate.split("-")[1] + "-" + dteFromDate.split("-")[0];
					SimpleDateFormat obj = new SimpleDateFormat("dd-MM-yyyy");
					Date dt1;
					try {
						dt1 = obj.parse(tempFromDate);
						GregorianCalendar cal = new GregorianCalendar();
						cal.setTime(dt1);
						cal.add(Calendar.DATE, -1);
						String newToDate = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());
				
				// Sale JV
				funCalculateBalanceSheetListAccountCode("ASSETS", "tbljvhd", "tbljvdtl", startDate, newToDate,cnt , clientCode, sbSql, hmSalesIncStmt,listACC,propertyCode);
				cnt++;
				// Sale Receipt
				funCalculateBalanceSheetListAccountCode("ASSETS", "tblreceipthd", "tblreceiptdtl", startDate, newToDate,cnt , clientCode, sbSql, hmSalesIncStmt,listACC,propertyCode);
							
				// Sale Payment
				funCalculateBalanceSheetListAccountCode("ASSETS", "tblpaymenthd", "tblpaymentdtl", startDate, newToDate, cnt, clientCode, sbSql, hmSalesIncStmt,listACC,propertyCode);	
					
					}catch(Exception e)
					{
						
					}
				}
				*/

				// Sale JV
				funCalculateBalanceSheetListAccountCode("ASSETS", "tbljvhd", "tbljvdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmSalesIncStmt,listACC,propertyCode);
				cnt++;
				// Sale Receipt
				funCalculateBalanceSheetListAccountCode("ASSETS", "tblreceipthd", "tblreceiptdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmSalesIncStmt,listACC,propertyCode);
							
				// Sale Payment
				funCalculateBalanceSheetListAccountCode("ASSETS", "tblpaymenthd", "tblpaymentdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmSalesIncStmt,listACC,propertyCode);	
					
				
				
				
//				
//				// Sale JV
//					funCalculateBalanceSheet("ASSETS", "tbljvhd", "tbljvdtl", dteFromDate, dteToDate, "dblCrAmt", clientCode, sbSql, hmSalesIncStmt);
//				
//				// Sale Receipt
//					funCalculateBalanceSheet("ASSETS", "tblreceipthd", "tblreceiptdtl", dteFromDate, dteToDate, "dblCrAmt", clientCode, sbSql, hmSalesIncStmt);
//							
//				// Sale Payment
//					funCalculateBalanceSheet("ASSETS", "tblpaymenthd", "tblpaymentdtl", dteFromDate, dteToDate, "dblCrAmt", clientCode, sbSql, hmSalesIncStmt);	
//					
				listACC=new ArrayList<String>();
				
				Map<String,clsIncomeStmtReportBean> hmOtherExpensesIncStmt = new HashMap<String,clsIncomeStmtReportBean>();
				
				 cnt=1;
					/*if (!startDate.equals(dteFromDate)) {
						String tempFromDate = dteFromDate.split("-")[2] + "-" + dteFromDate.split("-")[1] + "-" + dteFromDate.split("-")[0];
						SimpleDateFormat obj = new SimpleDateFormat("dd-MM-yyyy");
						Date dt1;
						try {
							dt1 = obj.parse(tempFromDate);
							GregorianCalendar cal = new GregorianCalendar();
							cal.setTime(dt1);
							cal.add(Calendar.DATE, -1);
							String newToDate = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());

			 //  CAPITAL AND RESERVES Receipt 
			    funCalculateBalanceSheetListAccountCode("LIABILITY", "tbljvhd", "tbljvdtl", startDate, newToDate, cnt, clientCode, sbSql, hmOtherExpensesIncStmt,listACC,propertyCode);
			    cnt++;
			   //  CAPITAL AND RESERVES Receipt 
				funCalculateBalanceSheetListAccountCode("LIABILITY", "tblreceipthd", "tblreceiptdtl", startDate, newToDate, cnt, clientCode, sbSql, hmOtherExpensesIncStmt,listACC,propertyCode);
										
			 //  CAPITAL AND RESERVES Payment	
				funCalculateBalanceSheetListAccountCode("LIABILITY", "tblpaymenthd", "tblpaymentdtl", startDate, newToDate, cnt, clientCode, sbSql, hmOtherExpensesIncStmt,listACC,propertyCode);

				}catch(Exception e)
						{
							
						}
					}*/
					
//				// LIABILITY JV
					funCalculateBalanceSheetListAccountCode("LIABILITY", "tbljvhd", "tbljvdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmOtherExpensesIncStmt,listACC,propertyCode);
//						
//				// LIABILITY 
					funCalculateBalanceSheetListAccountCode("LIABILITY", "tblreceipthd", "tblreceiptdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmOtherExpensesIncStmt,listACC,propertyCode);
//							
//				//LIABILITY	
					funCalculateBalanceSheetListAccountCode("LIABILITY", "tblpaymenthd", "tblpaymentdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmOtherExpensesIncStmt,listACC,propertyCode);
							
				// Calculate Gross Profit = Total Income (Sales) - Total Other Expenses (COG)	
					
				for(Map.Entry<String, clsIncomeStmtReportBean> entry:hmSalesIncStmt.entrySet())
				{
					totalSalesAmt=totalSalesAmt.add(entry.getValue().getDblValue());
					if(entry.getValue().getDblValue().compareTo(new BigDecimal(0))!=0){
						listOfBalancesheet.add(entry.getValue());	
					}
					
				}
				
				for(int i=0;i<listOfBalancesheet.size();i++)
				{
				System.out.println(	listOfBalancesheet.get(i).getStrGroupName());
				System.out.println(	listOfBalancesheet.get(i).getStrAccountName());
				}
				for(Map.Entry<String, clsIncomeStmtReportBean> entry:hmOtherExpensesIncStmt.entrySet())
				{
					totalOtherExpenses=totalOtherExpenses.add(entry.getValue().getDblValue());
					if(entry.getValue().getDblValue().compareTo(new BigDecimal(0))!=0){
						listOfBalancesheet.add(entry.getValue());	
					}
					
				}
			   
				listACC=new ArrayList<String>();
				
				listACC.add("1007-001-00");
				listACC.add("1007-002-00");
				
				listACC.add("1008-001-00");
				listACC.add("1008-002-00");
				Map<String,clsIncomeStmtReportBean> hmShareCapital= new HashMap<String,clsIncomeStmtReportBean>();	
				
				 cnt=1;
				/*	if (!startDate.equals(dteFromDate)) {
						String tempFromDate = dteFromDate.split("-")[2] + "-" + dteFromDate.split("-")[1] + "-" + dteFromDate.split("-")[0];
						SimpleDateFormat obj = new SimpleDateFormat("dd-MM-yyyy");
						Date dt1;
						try {
							dt1 = obj.parse(tempFromDate);
							GregorianCalendar cal = new GregorianCalendar();
							cal.setTime(dt1);
							cal.add(Calendar.DATE, -1);
							String newToDate = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());

			 //  CAPITAL AND RESERVES Receipt 
			    funCalculateBalanceSheetListAccountCode("EQUITY  CAPITAL", "tbljvhd", "tbljvdtl", startDate, newToDate, cnt, clientCode, sbSql, hmShareCapital,listACC,propertyCode);
			    cnt++;
			   //  CAPITAL AND RESERVES Receipt 
				funCalculateBalanceSheetListAccountCode("EQUITY  CAPITAL", "tblreceipthd", "tblreceiptdtl", startDate, newToDate, cnt, clientCode, sbSql, hmShareCapital,listACC,propertyCode);
										
			 //  CAPITAL AND RESERVES Payment	
				funCalculateBalanceSheetListAccountCode("EQUITY  CAPITAL", "tblpaymenthd", "tblpaymentdtl", startDate, newToDate, cnt, clientCode, sbSql, hmShareCapital,listACC,propertyCode);

				}catch(Exception e)
						{
							
						}
					}
					*/
					
				funCalculateBalanceSheetListAccountCode("EQUITY  CAPITAL", "tbljvhd", "tbljvdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmShareCapital,listACC,propertyCode);
				 cnt++;	
			// Other SHARE CAPITAL Receipt 
				funCalculateBalanceSheetListAccountCode("EQUITY  CAPITAL", "tblreceipthd", "tblreceiptdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmShareCapital,listACC,propertyCode);
									
			// Other SHARE CAPITAL Payment	
				funCalculateBalanceSheetListAccountCode("EQUITY  CAPITAL", "tblpaymenthd", "tblpaymentdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmShareCapital,listACC,propertyCode);

							
				/*			
				Map<String,clsIncomeStmtReportBean> hmShareCapital= new HashMap<String,clsIncomeStmtReportBean>();	
					
				// Other SHARE CAPITAL JV
					funCalculateBalanceSheet("CAPITAL AND RESERVES", "tbljvhd", "tbljvdtl", dteFromDate, dteToDate, "dblCrAmt", clientCode, sbSql, hmShareCapital);
							
				// Other SHARE CAPITAL Receipt 
					funCalculateBalanceSheet("CAPITAL AND RESERVES", "tblreceipthd", "tblreceiptdtl", dteFromDate, dteToDate, "dblCrAmt", clientCode, sbSql, hmShareCapital);
										
				// Other SHARE CAPITAL Payment	
					funCalculateBalanceSheet("CAPITAL AND RESERVES", "tblpaymenthd", "tblpaymentdtl", dteFromDate, dteToDate, "dblCrAmt", clientCode, sbSql, hmShareCapital);
	*/
				for(Map.Entry<String, clsIncomeStmtReportBean> entry:hmShareCapital.entrySet())
				{
					totalOtherIncome=totalOtherIncome.add(entry.getValue().getDblValue());
					if(entry.getValue().getDblValue().compareTo(new BigDecimal(0))!=0){
						listOfBalancesheet.add(entry.getValue());	
					}
					
				}
				
				
		Comparator<clsIncomeStmtReportBean> objComparator = new Comparator<clsIncomeStmtReportBean>()
					{
						@Override
						public int compare(clsIncomeStmtReportBean o1, clsIncomeStmtReportBean o2)
						{
							return o1.getStrCategory().compareToIgnoreCase(o2.getStrCategory());
						}
					};

		 Collections.sort(listOfBalancesheet, objComparator);
		 
		Comparator<clsIncomeStmtReportBean> objgroup = new Comparator<clsIncomeStmtReportBean>()
					{
						@Override
						public int compare(clsIncomeStmtReportBean o1, clsIncomeStmtReportBean o2)
						{
							return o1.getStrGroupCategory().compareToIgnoreCase(o2.getStrGroupCategory());
						}
					};

		 Collections.sort(listOfBalancesheet, objgroup);
		 
			Comparator<clsIncomeStmtReportBean> objAcc = new Comparator<clsIncomeStmtReportBean>()
					{
						@Override
						public int compare(clsIncomeStmtReportBean o1, clsIncomeStmtReportBean o2)
						{
							return o1.getStrAccountCode().compareToIgnoreCase(o2.getStrAccountCode());
						}
					};

		 Collections.sort(listOfBalancesheet, objAcc);
			
		 Map<String,BigDecimal> hmCalNetAssets=new HashMap<String,BigDecimal>();
		 clsIncomeStmtReportBean objBeanDebtorDtl;
		if(listOfBalancesheet.size()>0)
		{
			
			int listcnt=0;
			for(clsIncomeStmtReportBean obj:listOfBalancesheet)
			{
				if(hmCalNetAssets.containsKey(obj.getStrGroupName()))
				{
					hmCalNetAssets.put(obj.getStrGroupName(),hmCalNetAssets.get(obj.getStrGroupName()).add(obj.getDblValue()));
				}else{
					hmCalNetAssets.put(obj.getStrGroupName(), obj.getDblValue());
					
				}
				
				//
				// fun get debtor detail
				objBeanDebtorDtl=new clsIncomeStmtReportBean();
				objBeanDebtorDtl.setStrAccountCode(obj.getStrAccountCode());
				objBeanDebtorDtl.setStrAccountName(obj.getStrAccountName());
				
				
				if(funGetAccWiseDebtorDetail(obj.getStrAccountCode() ,dteFromDate, dteToDate,clientCode,propertyCode,listOfDebtor))
				{
					listOfDebtor.add(listcnt,objBeanDebtorDtl);	
					
					listcnt=listOfDebtor.size();
				}
			}
		}
		
		BigDecimal netCurrenAsset=new BigDecimal(0); 
		BigDecimal totNetAsset=new BigDecimal(0); 
//		if(!hmCalNetAssets.isEmpty()){
//		netCurrenAsset=hmCalNetAssets.get("CURRENT ASSETS").subtract(hmCalNetAssets.get("CURRENT LIABILITIES"));
//		totNetAsset=hmCalNetAssets.get("NON- CURRENT ASSETS").add(netCurrenAsset);
//		}
		
		hm.put("listDebtorDtl",listOfDebtor );
		
		hm.put("netCurrenAsset",netCurrenAsset);
		hm.put("totNetAsset",totNetAsset);
		
		
		
		
		
			
		
		
				
				////////////////////////////////////////////////////////////////////////////////////////////
				

				/**
				 * Income statement
				 * 
				 * erased on 24-04-2019 
				 */



				List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();

				JasperDesign jd = JRXmlLoader.load(reportName);
				JasperReport jr = JasperCompileManager.compileReport(jd);
				JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(listOfBalancesheet);
				JasperPrint print = JasperFillManager.fillReport(jr, hm, beanCollectionDataSource);
				jprintlist.add(print);
				ServletOutputStream servletOutputStream = resp.getOutputStream();
				if (jprintlist.size() > 0)
				{
				  if(objBean.getStrDocType().equals("PDF"))
					{
					JRExporter exporter = new JRPdfExporter();
					resp.setContentType("application/pdf");
					exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
					exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
					exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					// resp.setHeader("Content-Disposition",
					// "inline;filename=rptProductWiseGRNReport_"+dteFromDate+"_To_"+dteToDate+"_"+userCode+".pdf");
					exporter.exportReport();
					servletOutputStream.flush();
					servletOutputStream.close();

				}
				else
				{
					
					JRXlsExporter exporter = new JRXlsExporter();
					resp.setContentType("application/xlsx");
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jprintlist);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
					exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					resp.setHeader("Content-Disposition","inline;filename=rptBalanceSheet_"+dteFromDate+"_To_"+dteToDate+"_"+userCode+".xls");
					exporter.exportReport();
					servletOutputStream.write(byteArrayOutputStream.toByteArray()); 
					servletOutputStream.flush();
					servletOutputStream.close();

				}
			}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					con.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}

		}
		private boolean  funGetAccWiseDebtorDetail(String accCode ,String dteFromDate,String  dteToDate,
				String clientCode,String propertyCode,List listOfDebtor){
			boolean isDebtor=false;
			//Map<String,clsIncomeStmtReportBean> hmDebtor=new HashMap<String, clsIncomeStmtReportBean>();
			String sql="select c.strDebtorCode,c.strDebtorName,c.strCrDr,sum(c.dblAmt) "
				+" from tbljvhd a,tbljvdtl b, tbljvdebtordtl c  "
				+" where a.strVouchNo=b.strVouchNo and a.strVouchNo=c.strVouchNo"
				+" and b.strAccountCode=c.strAccountCode"
				+" and b.strAccountCode='"+accCode+"'"
				+" AND DATE(a.dteVouchDate) BETWEEN '"+dteFromDate+"' AND '"+dteToDate+"' " 
				+" AND c.strClientCode='"+clientCode+"' AND a.strPropertyCode='"+propertyCode+"'"
				+" group BY c.strDebtorCode;"; 
			List listDebtor = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			if (listDebtor != null && listDebtor.size() > 0)
			{
				clsIncomeStmtReportBean obBean;
				for(int cn=0;cn<listDebtor.size();cn++)
				{
					isDebtor=true;
					obBean= new clsIncomeStmtReportBean();
					Object[] objArr = (Object[]) listDebtor.get(cn);
					/*if(hmDebtor.containsKey(objArr[0].toString())){
						obBean=hmDebtor.get(objArr[0].toString());
						obBean.setDblBalAmt(obBean.getDblBalAmt()+Double.parseDouble(objArr[3].toString()));
						
					}else{*/
						obBean.setStrAccountName(objArr[1].toString());
						//obBean.setStrAccountCode(objArr[0].toString());
						obBean.setDblBalAmt(Double.parseDouble(objArr[3].toString()));
						if(objArr[2].toString().equalsIgnoreCase("Cr")){
							obBean.setStrName("Credit");	
						}else{
							obBean.setStrName("Debit");
						}
						
					//}
					
					//hmDebtor.put(objArr[0].toString(),obBean);
						listOfDebtor.add(obBean);
				}
			}
			sql="select c.strDebtorCode,c.strDebtorName,c.strCrDr,sum(c.dblAmt) "
					+" from tblpaymenthd a,tblpaymentdtl b, tblpaymentdebtordtl c  "
					+" where a.strVouchNo=b.strVouchNo and a.strVouchNo=c.strVouchNo"
					+" and b.strAccountCode=c.strAccountCode"
					+" and b.strAccountCode='"+accCode+"'"
					+" AND DATE(a.dteVouchDate) BETWEEN '"+dteFromDate+"' AND '"+dteToDate+"' " 
					+" AND c.strClientCode='"+clientCode+"' AND a.strPropertyCode='"+propertyCode+"'"
					+" group BY c.strDebtorCode;"; 
				listDebtor = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
				if (listDebtor != null && listDebtor.size() > 0)
				{
					clsIncomeStmtReportBean obBean;
					for(int cn=0;cn<listDebtor.size();cn++)
					{
					isDebtor=true;
					obBean= new clsIncomeStmtReportBean();
					Object[] objArr = (Object[]) listDebtor.get(cn);
					/*if(hmDebtor.containsKey(objArr[0].toString())){
						
						obBean=hmDebtor.get(objArr[0].toString());
						obBean.setDblBalAmt(obBean.getDblBalAmt()-Double.parseDouble(objArr[3].toString()));
						
					}else{*/
						obBean.setStrAccountName(objArr[1].toString());
						//obBean.setStrAccountCode(objArr[0].toString());
						obBean.setDblBalAmt(Double.parseDouble(objArr[3].toString()));
						if(objArr[2].toString().equalsIgnoreCase("Cr")){
							obBean.setStrName("Credit");	
						}else{
							obBean.setStrName("Debit");
						}	
					//}
						listOfDebtor.add(obBean);
					}
					//hmDebtor.put(objArr[0].toString(),obBean);}
				}
			sql="select c.strDebtorCode,c.strDebtorName,c.strCrDr,sum(c.dblAmt) "
					+" from tblreceipthd a,tblreceiptdtl b, tblreceiptdebtordtl c  "
					+" where a.strVouchNo=b.strVouchNo and a.strVouchNo=c.strVouchNo"
					+" and b.strAccountCode=c.strAccountCode"
					+" and b.strAccountCode='"+accCode+"'"
					+" AND DATE(a.dteVouchDate) BETWEEN '"+dteFromDate+"' AND '"+dteToDate+"' " 
					+" AND c.strClientCode='"+clientCode+"' AND a.strPropertyCode='"+propertyCode+"'"
					+" group BY c.strDebtorCode;"; 
				listDebtor = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
				if (listDebtor != null && listDebtor.size() > 0)
				{
					clsIncomeStmtReportBean obBean;
					for(int cn=0;cn<listDebtor.size();cn++)
					{isDebtor=true;
					obBean= new clsIncomeStmtReportBean();
					Object[] objArr = (Object[]) listDebtor.get(cn);
					/*if(hmDebtor.containsKey(objArr[0].toString())){
						obBean=hmDebtor.get(objArr[0].toString());
						obBean.setDblBalAmt(obBean.getDblBalAmt()+Double.parseDouble(objArr[3].toString()));
						
					}else{*/
						obBean.setStrAccountName(objArr[1].toString());
						//obBean.setStrAccountCode(objArr[0].toString());
						obBean.setDblBalAmt(Double.parseDouble(objArr[3].toString()));
						if(objArr[2].toString().equalsIgnoreCase("Cr")){
							obBean.setStrName("Credit");	
						}else{
							obBean.setStrName("Debit");
						}
					//}
						listOfDebtor.add(obBean);
					//hmDebtor.put(objArr[0].toString(),obBean);}
					}
				}
				
				/*for(Map.Entry<String, clsIncomeStmtReportBean> entry:hmDebtor.entrySet())
					{
						listOfDebtor.add(entry.getValue());	
					}*/
			return isDebtor;
		}
		
		
		@RequestMapping(value = "/rptBalanceSheet", method = RequestMethod.GET)
		private ModelAndView funGenerateBalanceSheetInExcel(@ModelAttribute("command") clsCreditorOutStandingReportBean objBean, HttpServletResponse resp, HttpServletRequest req)
		{
			List listExcelData = new ArrayList();
			Connection con = objGlobal.funGetConnection(req);
			try
			{
				String clientCode = req.getSession().getAttribute("clientCode").toString();
				String companyName = req.getSession().getAttribute("companyName").toString();
				String userCode = req.getSession().getAttribute("usercode").toString();
				String propertyCode = req.getSession().getAttribute("propertyCode").toString();
				String currencyCode = objBean.getStrCurrency();
				double conversionRate = 1;
				String webStockDB = req.getSession().getAttribute("WebStockDB").toString();
				StringBuilder sbSql = new StringBuilder();
				sbSql.append("select dblConvToBaseCurr from " + webStockDB + ".tblcurrencymaster where strCurrencyCode='" + currencyCode + "' and strClientCode='" + clientCode + "' ");
				try
				{
					List list = objBaseService.funGetList(sbSql, "sql");
					conversionRate = Double.parseDouble(list.get(0).toString());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				double currValue = Double.parseDouble(req.getSession().getAttribute("currValue").toString());

				clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
				if (objSetup == null)
				{
					objSetup = new clsPropertySetupModel();
				}
				String type = "PDF";
				String fromDate = objBean.getDteFromDate();
				String toDate = objBean.getDteToDate();

				String fd = fromDate.split("-")[0];
				String fm = fromDate.split("-")[1];
				String fy = fromDate.split("-")[2];

				String td = toDate.split("-")[0];
				String tm = toDate.split("-")[1];
				String ty = toDate.split("-")[2];

				String dteFromDate = fy + "-" + fm + "-" + fd;
				String dteToDate = ty + "-" + tm + "-" + td;
								
				Map<String,Map<String,Double>> hmBalanceSheetCatWiseData=new HashMap<String,Map<String,Double>>();
								
				sbSql.setLength(0);
				sbSql.append("select a.strType,b.strCategory,b.strGroupName,d.strCrDr,sum(d.dblCrAmt) as Sale,sum(d.dblDrAmt) as Purchase,a.strAccountName "
					+ "from tblacmaster a,tblsubgroupmaster s,tblacgroupmaster b,tbljvhd c,tbljvdtl d "
					+ "where s.strGroupCode=b.strGroupCode and a.strSubGroupCode=s.strSubGroupCode and a.strAccountCode=d.strAccountCode "
					+ "and c.strVouchNo=d.strVouchNo and a.strType='GL Code' "
					+ "and date(c.dteVouchDate) between '" + dteFromDate + "' and '" + dteToDate + "' "
					+ "group by a.strAccountCode order by b.strCategory; ");
				List listJV = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
				if (listJV != null && listJV.size() > 0)
				{
					for(int cn=0;cn<listJV.size();cn++)
					{
						Object[] objArr = (Object[]) listJV.get(cn);
						String groupCategory = objArr[1].toString();
						String groupName = objArr[2].toString();
						double creditAmount = Double.parseDouble(objArr[4].toString());
						double debitAmount = Double.parseDouble(objArr[5].toString());
						double totalAmt=creditAmount+debitAmount;
						Map<String,Double> hmGroupWiseData=new HashMap<String,Double>();
						
						if(hmBalanceSheetCatWiseData.containsKey(groupCategory)) 
						{
							hmGroupWiseData=hmBalanceSheetCatWiseData.get(groupCategory);
							if(hmGroupWiseData.containsKey(groupName))
								totalAmt=hmGroupWiseData.get(groupName)+totalAmt;
						}
						hmGroupWiseData.put(groupName,totalAmt);
						hmBalanceSheetCatWiseData.put(groupCategory,hmGroupWiseData);
					}
				}
				
				sbSql.setLength(0);
				sbSql.append("select a.strType,b.strCategory,b.strGroupName,d.strCrDr,sum(d.dblCrAmt) as Sale,sum(d.dblDrAmt) as Purchase,a.strAccountName "
					+ "from tblacmaster a,tblacgroupmaster b,tblsubgroupmaster s,tblpaymenthd c,tblpaymentdtl d "
					+ "where b.strGroupCode=s.strGroupCode and s.strSubGroupCode=a.strSubGroupCode and a.strAccountCode=d.strAccountCode "
					+ "and c.strVouchNo=d.strVouchNo and a.strType='GL Code' "
					+ "and date(c.dteVouchDate) between '" + dteFromDate + "' and '" + dteToDate + "' "
					+ "group by a.strAccountCode order by b.strCategory; ");
				List listPayment = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
				if (listPayment != null && listPayment.size() > 0)
				{
					for(int cn=0;cn<listPayment.size();cn++)
					{
						Object[] objArr = (Object[]) listPayment.get(cn);
						String groupCategory = objArr[1].toString();
						String groupName = objArr[2].toString();
						double creditAmount = Double.parseDouble(objArr[4].toString());
						double debitAmount = Double.parseDouble(objArr[5].toString());
						double totalAmt=creditAmount+debitAmount;
						
						Map<String,Double> hmGroupWiseData=new HashMap<String,Double>();
						if(hmBalanceSheetCatWiseData.containsKey(groupCategory)) 
						{
							hmGroupWiseData=hmBalanceSheetCatWiseData.get(groupCategory);
							if(hmGroupWiseData.containsKey(groupName))
								totalAmt=hmGroupWiseData.get(groupName)+totalAmt;
						}
						hmGroupWiseData.put(groupName,totalAmt);
						hmBalanceSheetCatWiseData.put(groupCategory,hmGroupWiseData);
					}
				}
				
				sbSql.setLength(0);
				sbSql.append("select a.strType,b.strCategory,b.strGroupName,d.strCrDr,sum(d.dblCrAmt) as Sale,sum(d.dblDrAmt) as Purchase,a.strAccountName "
					+ "from tblacmaster a,tblacgroupmaster b,tblsubgroupmaster s,tblreceipthd c,tblreceiptdtl d "
					+ "where s.strGroupCode=b.strGroupCode and s.strSubGroupCode=a.strSubGroupCode and a.strAccountCode=d.strAccountCode "
					+ "and c.strVouchNo=d.strVouchNo and a.strType='GL Code' "
					+ "and date(c.dteVouchDate) between '" + dteFromDate + "' and '" + dteToDate + "' "
					+ "group by a.strAccountCode order by b.strCategory; ");
				List listReceipt = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
				if (listReceipt != null && listReceipt.size() > 0)
				{
					for(int cn=0;cn<listReceipt.size();cn++)
					{
						Object[] objArr = (Object[]) listReceipt.get(cn);
						String groupCategory = objArr[1].toString();
						String groupName = objArr[2].toString();
						double creditAmount = Double.parseDouble(objArr[4].toString());
						double debitAmount = Double.parseDouble(objArr[5].toString());
						double totalAmt=creditAmount+debitAmount;
						
						Map<String,Double> hmGroupWiseData=new HashMap<String,Double>();
						if(hmBalanceSheetCatWiseData.containsKey(groupCategory)) 
						{
							hmGroupWiseData=hmBalanceSheetCatWiseData.get(groupCategory);
							if(hmGroupWiseData.containsKey(groupName))
								totalAmt=hmGroupWiseData.get(groupName)+totalAmt;
						}
						hmGroupWiseData.put(groupName,totalAmt);
						hmBalanceSheetCatWiseData.put(groupCategory,hmGroupWiseData);
					}
				}
				
				List<List<String>> listAssets=new ArrayList<List<String>>();
				List<String> listCatName=new ArrayList<String>();
				listCatName.add("ASSETS");
				listAssets.add(listCatName);
				
				List<List<String>> listLiabilities=new ArrayList<List<String>>();
				listCatName=new ArrayList<String>();
				listCatName.add("LIABILITIES");
				listLiabilities.add(listCatName);
				
				List<List<String>> listCapitals=new ArrayList<List<String>>();
				listCatName=new ArrayList<String>();
				listCatName.add("CAPITALS");
				listCapitals.add(listCatName);
				
				double total=0,capitalTotal=0;
				for(Map.Entry<String, Map<String,Double>> entry:hmBalanceSheetCatWiseData.entrySet())
				{
					if(entry.getKey().equals("ASSET") || entry.getKey().equals("INVENTORY") || entry.getKey().equals("DIRECT INCOME") 
						|| entry.getKey().equals("SUNDRY DEBTOR") || entry.getKey().equals("BANK BALANCE") || entry.getKey().equals("CASH BALANCE"))
					{
						List<String> listAssetGroups=new ArrayList<String>();
						for(Map.Entry<String, Double> entryGroup:entry.getValue().entrySet())
						{
							listAssetGroups.add(entryGroup.getKey());
							listAssetGroups.add(entryGroup.getValue().toString());
							listAssets.add(listAssetGroups);
							total+=entryGroup.getValue();
						}
					}
					
					if(entry.getKey().equals("LIABILITY") || entry.getKey().equals("INDIRECT EXPENSE") || entry.getKey().equals("SUNDRY CREDITOR")|| entry.getKey().equals("EXPENSE") )
					{
						List<String> listLiabilityGroups=new ArrayList<String>();
						for(Map.Entry<String, Double> entryLiability:entry.getValue().entrySet())
						{
							listLiabilityGroups.add(entryLiability.getKey());
							listLiabilityGroups.add(entryLiability.getValue().toString());
							listLiabilities.add(listLiabilityGroups);
							total+=entryLiability.getValue();
						}
					}
					
					if(entry.getKey().equals("SHARE CAPITAL") || entry.getKey().equals("EQUITY  CAPITAL"))
					{
						List<String> listCapitalGroups=new ArrayList<String>();
						for(Map.Entry<String, Double> entryCapitals:entry.getValue().entrySet())
						{
							listCapitalGroups.add(entryCapitals.getKey());
							listCapitalGroups.add(entryCapitals.getValue().toString());
							listCapitals.add(listCapitalGroups);
							capitalTotal+=entryCapitals.getValue();
						}
					}
				}
				
				String[] arrStr={"",""};
				
				listExcelData.add("BALANCE SHEET");	//0
				List listData1=new ArrayList();
				listData1.add(companyName);				
				listData1.add("BALANCE SHEET");
				listExcelData.add(listData1);	//1
				
				List listDates=new ArrayList();
				listDates.add("From Date : "+fromDate);
				listDates.add("To Date : "+toDate);
				listExcelData.add(listDates);	//2
								
				List listData=new ArrayList();
								
				List<List> listTotals=new ArrayList<List>();
				List list=new ArrayList();
				list.add("TOTAL");
				list.add(String.valueOf(total));
				listTotals.add(list);
				
				List<List> listCapitalTotals=new ArrayList<List>();
				List list2=new ArrayList();
				list2.add("CAPITAL TOTAL");
				list2.add(String.valueOf(capitalTotal));
				listCapitalTotals.add(list2);
				
				listData.add(listAssets);
				listData.add(listLiabilities);
				listData.add(listTotals);
				listData.add(listCapitals);
				listData.add(listCapitalTotals);
								
				listExcelData.add(listData);	//3
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					con.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
			
			return new ModelAndView("excelViewForAccountReports", "excelDataList", listExcelData);

		}
		
		private void funCalculateBalanceSheet(String catType, String hdTableName, String dtlTableName, String fromDate, String toDate
				, String crdrColumn, String clientCode, StringBuilder sbSql, Map<String,clsIncomeStmtReportBean> hmIncomeStatement)
		{
			sbSql.setLength(0);
//			sbSql.append("select a.strType, a.strGroupCode,b.strGroupName,d.strCrDr,sum(d.dblCrAmt) as Sale,sum(d.dblDrAmt) as Purchase,a.strAccountName,a.strAccountCode "
//					+ "from tblacmaster a,tblacgroupmaster b,"+hdTableName+" c,"+dtlTableName+" d "
//					+ "where a.strGroupCode=b.strGroupCode "
//					+ "and a.strAccountCode=d.strAccountCode "
//					+ "and c.strVouchNo=d.strVouchNo "
//					+ "and b.strCategory='"+catType+"' "
//					+ "and a.strType='GL Code' "
//					+ "and date(c.dteVouchDate) between '" + fromDate + "' and '" + toDate + "' and c.strClientCode='"+clientCode+"' "
//					+ "group by a.strAccountCode order by b.strCategory,a.strGroupCode ; ");
			
			
			StringBuilder sbOp=new StringBuilder(); 			
			sbSql.append("select a.strType, sb.strGroupCode,b.strGroupName,ifnull(d.strCrDr,''),ifnull(sum(d.dblCrAmt),0) as Sale,ifnull(sum(d.dblDrAmt),0) as Purchase,a.strAccountName,a.strAccountCode "
					+"  from  tblsubgroupmaster sb,tblacgroupmaster b,tblacmaster a "
					+" left outer join tbljvdtl d on  a.strAccountCode=d.strAccountCode " 
					+" left outer join tbljvhd  c on c.strVouchNo=d.strVouchNo   and date(c.dteVouchDate) between '" + fromDate + "' and '" + toDate + "' and c.strClientCode='"+clientCode+"' "
					+ "where sb.strGroupCode=b.strGroupCode "
					+ " and a.strSubGroupCode =sb.strSubGroupCode"
					+ "and b.strCategory='"+catType+"' "
					+ "and a.strType='GL Code' "
					+ "group by a.strAccountCode  order by b.strGroupCode ,a.strAccountCode ; ");
			
			List listJV = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
			if (listJV != null && listJV.size() > 0)
			{
				for(int cn=0;cn<listJV.size();cn++)
				{
					Object[] objArr = (Object[]) listJV.get(cn);

					String groupCategory = objArr[1].toString();
					String groupName = objArr[2].toString();
					BigDecimal creditAmount = BigDecimal.valueOf(Double.parseDouble(objArr[4].toString()));
					BigDecimal debitAmount = BigDecimal.valueOf(Double.parseDouble(objArr[5].toString()));
					
					
					BigDecimal totalAmt=debitAmount.subtract(creditAmount);
					
					String accountCode=objArr[6].toString();
					clsIncomeStmtReportBean objBean = new clsIncomeStmtReportBean();
					
					if(hmIncomeStatement.containsKey(accountCode))
					{
						objBean=hmIncomeStatement.get(accountCode);
						objBean.setDblValue(objBean.getDblValue().add(totalAmt));
					}
					else
					{
						objBean.setStrGroupCategory(groupCategory);
						objBean.setStrGroupName(groupName);
						
						objBean.setStrAccountName(objArr[6].toString());
						objBean.setStrAccountCode(objArr[7].toString());
						
						sbOp.setLength(0);
						BigDecimal opAmt=new BigDecimal(0);
						sbOp.append(" select IF(a.strCrDr='Dr',a.intOpeningBal,0) dblDebitAmt, IF(a.strCrDr='Cr',a.intOpeningBal,0) dblCreditAmt, (IF(a.strCrDr='Dr',a.intOpeningBal,0) - IF(a.strCrDr='Cr',a.intOpeningBal,0)) dblBalanceAmt  from tblacmaster a where a.strAccountCode='"+objArr[7].toString()+"' "
					     +" and a.strClientCode='"+clientCode+"' ");
						List listOP = objGlobalFunctionsService.funGetListModuleWise(sbOp.toString(), "sql");
					    if(listOP.size()>0)
					    {
					    	Object obj[]=(Object[])listOP.get(0);
					    	opAmt=BigDecimal.valueOf( Double.parseDouble(obj[2].toString()));
					    }
					    objBean.setDblValue(opAmt.add(totalAmt));
					
					}
					objBean.setStrCategory(catType);
					hmIncomeStatement.put(accountCode, objBean);
				}
			}
			////////////////////////////////////////////////Payment
			
			
			
			sbSql.append("select a.strType, b.strGroupCode,b.strGroupName,ifnull(d.strCrDr,''),ifnull(sum(d.dblCrAmt),0) as Sale,ifnull(sum(d.dblDrAmt),0) as Purchase,a.strAccountName,a.strAccountCode "
					+"  from  tblacgroupmaster b,tblsubgroupmaster sb,tblacmaster a "
					+" left outer join tblreceiptdtl d on  a.strAccountCode=d.strAccountCode " 
					+" left outer join tblreceipthd  c on c.strVouchNo=d.strVouchNo   and date(c.dteVouchDate) between '" + fromDate + "' and '" + toDate + "' and c.strClientCode='"+clientCode+"' "
					+ " where sb.strGroupCode=b.strGroupCode "
					+ " and a.strSubGroupCode=sb.strSubGroupCode"
					+ "and b.strCategory='"+catType+"' "
					+ "and a.strType='GL Code' "
					+ "group by a.strAccountCode  order by b.strGroupCode ,a.strAccountCode ; ");
			
			List listRec = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
			if (listRec != null && listRec.size() > 0)
			{
				for(int cn=0;cn<listRec.size();cn++)
				{
					Object[] objArr = (Object[]) listRec.get(cn);

					String groupCategory = objArr[1].toString();
					String groupName = objArr[2].toString();
					BigDecimal creditAmount = BigDecimal.valueOf(Double.parseDouble(objArr[4].toString()));
					BigDecimal debitAmount = BigDecimal.valueOf(Double.parseDouble(objArr[5].toString()));
					
					
					BigDecimal totalAmt=debitAmount.subtract(creditAmount);
					
					String accountCode=objArr[6].toString();
					clsIncomeStmtReportBean objBean = new clsIncomeStmtReportBean();
					
					if(hmIncomeStatement.containsKey(accountCode))
					{
						objBean=hmIncomeStatement.get(accountCode);
						objBean.setDblValue(objBean.getDblValue().add(totalAmt));
					}
					else
					{
						objBean.setStrGroupCategory(groupCategory);
						objBean.setStrGroupName(groupName);
						objBean.setDblValue(totalAmt);
						objBean.setStrAccountName(objArr[6].toString());
						objBean.setStrAccountCode(objArr[7].toString());
					}
					objBean.setStrCategory(catType);
					hmIncomeStatement.put(accountCode, objBean);
				}
			}
			
			sbSql.append("select a.strType, b.strGroupCode,b.strGroupName,ifnull(d.strCrDr,''),ifnull(sum(d.dblCrAmt),0) as Sale,ifnull(sum(d.dblDrAmt),0) as Purchase,a.strAccountName,a.strAccountCode "
					+"  from  tblsubgroupmaster sb,tblacgroupmaster b,tblacmaster a "
					+" left outer join tblpaymentdtl d on  a.strAccountCode=d.strAccountCode " 
					+" left outer join tblpaymenthd  c on c.strVouchNo=d.strVouchNo   and date(c.dteVouchDate) between '" + fromDate + "' and '" + toDate + "' and c.strClientCode='"+clientCode+"' "
					+ "where a.strSubGroupCode=sb.strSubGroupCode and sb.strGroupCode=b.strGroupCode "
					+ "and b.strCategory='"+catType+"' "
					+ "and a.strType='GL Code' "
					+ "group by a.strAccountCode  order by a.strSubGroupCode,b.strGroupCode ,a.strAccountCode ; ");
			
			List listPay= objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
			if (listPay != null && listPay.size() > 0)
			{
				for(int cn=0;cn<listPay.size();cn++)
				{
					Object[] objArr = (Object[]) listPay.get(cn);

					String groupCategory = objArr[1].toString();
					String groupName = objArr[2].toString();
					BigDecimal creditAmount = BigDecimal.valueOf(Double.parseDouble(objArr[4].toString()));
					BigDecimal debitAmount = BigDecimal.valueOf(Double.parseDouble(objArr[5].toString()));
					
					
					BigDecimal totalAmt=debitAmount.subtract(creditAmount);
					
					String accountCode=objArr[6].toString();
					clsIncomeStmtReportBean objBean = new clsIncomeStmtReportBean();
					
					if(hmIncomeStatement.containsKey(accountCode))
					{
						objBean=hmIncomeStatement.get(accountCode);
						objBean.setDblValue(objBean.getDblValue().add(totalAmt));
					}
					else
					{
						objBean.setStrGroupCategory(groupCategory);
						objBean.setStrGroupName(groupName);
						objBean.setDblValue(totalAmt);
						objBean.setStrAccountName(objArr[6].toString());
						objBean.setStrAccountCode(objArr[7].toString());
					}
					objBean.setStrCategory(catType);
					hmIncomeStatement.put(accountCode, objBean);
				}
			}
			
		}
		
		private void funCalculateBalanceSheetListAccountCode(String catType, String hdTableName, String dtlTableName, String fromDate, String toDate
				, int cnt, String clientCode, StringBuilder sbSql, Map<String,clsIncomeStmtReportBean> hmIncomeStatement,List<String> listAccounts,String propCode)
		{
			StringBuilder sbOp=new StringBuilder(); 		

			if(listAccounts!=null && listAccounts.size()>0){
				for(String acc:listAccounts)
				{
					acc=acc.trim().split(" ")[0];
					sbSql.setLength(0);
					sbSql.append("select a.strType,b.strGroupCode,b.strGroupName,ifnull(d.strCrDr,''),IFNULL(SUM(d.dblCrAmt),0) AS Sale, IFNULL(SUM(d.dblDrAmt),0) AS Purchase,a.strAccountName,a.strAccountCode, "
							+" b.strCategory from tblsubgroupmaster sb, tblacgroupmaster b,tblacmaster a "
							+" left outer join "+dtlTableName+" d on  a.strAccountCode=d.strAccountCode " 
							+" left outer join "+hdTableName+"  c on c.strVouchNo=d.strVouchNo   and date(c.dteVouchDate) between '" + fromDate + "' and '" + toDate + "' and c.strClientCode='"+clientCode+"'  and a.strPropertyCode='"+propCode+"' "
							+" where a.strSubGroupCode=sb.strSubGroupCode"
							+ " and sb.strGroupCode=b.strGroupCode "
							+" and a.strAccountCode='"+acc+"' "
							+" group by a.strAccountCode  "
							+" ORDER BY b.strGroupName ");
					
					List listJV = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
					if (listJV != null && listJV.size() > 0)
					{
						for(int cn=0;cn<listJV.size();cn++)
						{
							Object[] objArr = (Object[]) listJV.get(cn);
							System.out.println(objArr[2].toString());
							String groupCategory = objArr[1].toString();
							String groupName = objArr[2].toString();
							BigDecimal creditAmount = BigDecimal.valueOf(Double.parseDouble(objArr[4].toString()));
							BigDecimal debitAmount = BigDecimal.valueOf(Double.parseDouble(objArr[5].toString()));
			
							
							
							BigDecimal totalAmt=debitAmount.subtract(creditAmount);
							
							String accountCode=objArr[7].toString();
							clsIncomeStmtReportBean objBean = new clsIncomeStmtReportBean();
							
							if(hmIncomeStatement.containsKey(accountCode))
							{
								objBean=hmIncomeStatement.get(accountCode);
								objBean.setDblValue(objBean.getDblValue().add(totalAmt));
							}
							else
							{
								objBean.setStrGroupCategory(groupCategory);
								objBean.setStrGroupName(groupName);
								
								objBean.setStrAccountName(objArr[6].toString());
								objBean.setStrAccountCode(objArr[7].toString());
								
								BigDecimal opAmt=new BigDecimal(0);
								if(cnt==1)
								{
								sbOp.setLength(0);
								
								sbOp.append(" select IF(a.strCrDr='Dr',a.intOpeningBal,0) dblDebitAmt, IF(a.strCrDr='Cr',a.intOpeningBal,0) dblCreditAmt, (IF(a.strCrDr='Dr',a.intOpeningBal,0) - IF(a.strCrDr='Cr',a.intOpeningBal,0)) dblBalanceAmt  from tblacmaster a where a.strAccountCode='"+objArr[7].toString()+"' "
							     +" and a.strClientCode='"+clientCode+"' ");
								List listOP = objGlobalFunctionsService.funGetListModuleWise(sbOp.toString(), "sql");
							    if(listOP.size()>0)
							    {
							    	Object obj[]=(Object[])listOP.get(0);
							    	opAmt=BigDecimal.valueOf( Double.parseDouble(obj[2].toString()));
							    }
								}
							    objBean.setDblValue(opAmt.add(totalAmt));
							}
							objBean.setStrCategory(catType);
							hmIncomeStatement.put(accountCode, objBean);
						}
					}

					
				}
			}else{
				sbSql.setLength(0);
				sbSql.append("select a.strType,b.strGroupCode,b.strGroupName,ifnull(d.strCrDr,''),IFNULL(SUM(d.dblCrAmt),0) AS Sale, IFNULL(SUM(d.dblDrAmt),0) AS Purchase,a.strAccountName,a.strAccountCode, "
						+" b.strCategory from tblsubgroupmaster sb, tblacgroupmaster b,tblacmaster a "
						+" left outer join "+dtlTableName+" d on  a.strAccountCode=d.strAccountCode " 
						+" left outer join "+hdTableName+"  c on c.strVouchNo=d.strVouchNo   and date(c.dteVouchDate) between '" + fromDate + "' and '" + toDate + "' and c.strClientCode='"+clientCode+"'  and a.strPropertyCode='"+propCode+"' "
						+" where a.strSubGroupCode=sb.strSubGroupCode"
						+ " and sb.strGroupCode=b.strGroupCode "
						+ "and b.strCategory='"+catType+"' "
						+" group by a.strAccountCode  "
						+" ORDER BY b.strGroupName ");
				
				List listJV = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
				if (listJV != null && listJV.size() > 0)
				{
					for(int cn=0;cn<listJV.size();cn++)
					{
						Object[] objArr = (Object[]) listJV.get(cn);
						System.out.println(objArr[2].toString());
						String groupCategory = objArr[1].toString();
						String groupName = objArr[2].toString();
						BigDecimal creditAmount = BigDecimal.valueOf(Double.parseDouble(objArr[4].toString()));
						BigDecimal debitAmount = BigDecimal.valueOf(Double.parseDouble(objArr[5].toString()));
		
						
						
						BigDecimal totalAmt=debitAmount.subtract(creditAmount);
						
						String accountCode=objArr[7].toString();
						clsIncomeStmtReportBean objBean = new clsIncomeStmtReportBean();
						
						if(hmIncomeStatement.containsKey(accountCode))
						{
							objBean=hmIncomeStatement.get(accountCode);
							objBean.setDblValue(objBean.getDblValue().add(totalAmt));
						}
						else
						{
							objBean.setStrGroupCategory(groupCategory);
							objBean.setStrGroupName(groupName);
							
							objBean.setStrAccountName(objArr[6].toString());
							objBean.setStrAccountCode(objArr[7].toString());
							
							BigDecimal opAmt=new BigDecimal(0);
							if(cnt==1)
							{
							sbOp.setLength(0);
							
							sbOp.append(" select IF(a.strCrDr='Dr',a.intOpeningBal,0) dblDebitAmt, IF(a.strCrDr='Cr',a.intOpeningBal,0) dblCreditAmt, (IF(a.strCrDr='Dr',a.intOpeningBal,0) - IF(a.strCrDr='Cr',a.intOpeningBal,0)) dblBalanceAmt  from tblacmaster a where a.strAccountCode='"+objArr[7].toString()+"' "
						     +" and a.strClientCode='"+clientCode+"' ");
							List listOP = objGlobalFunctionsService.funGetListModuleWise(sbOp.toString(), "sql");
						    if(listOP.size()>0)
						    {
						    	Object obj[]=(Object[])listOP.get(0);
						    	opAmt=BigDecimal.valueOf( Double.parseDouble(obj[2].toString()));
						    }
							}
						    objBean.setDblValue(opAmt.add(totalAmt));
						}
						objBean.setStrCategory(catType);
						hmIncomeStatement.put(accountCode, objBean);
					}
				}

			}
			
		}
		
		

		
		@RequestMapping(value = "/rptBalanceSheet2", method = RequestMethod.GET)
		private ModelAndView funReport1(@ModelAttribute("command") clsCreditorOutStandingReportBean objBean, HttpServletResponse resp, HttpServletRequest req)
		{
			List listExcelData = new ArrayList();
			objGlobal = new clsGlobalFunctions();
			Connection con = objGlobal.funGetConnection(req);
			try
			{
				String clientCode = req.getSession().getAttribute("clientCode").toString();
				String companyName = req.getSession().getAttribute("companyName").toString();
				String userCode = req.getSession().getAttribute("usercode").toString();
				String propertyCode = req.getSession().getAttribute("propertyCode").toString();
				String currencyCode = objBean.getStrCurrency();
				String webStockDB = req.getSession().getAttribute("WebStockDB").toString();
				StringBuilder sbSql = new StringBuilder();

				double currValue = Double.parseDouble(req.getSession().getAttribute("currValue").toString());

				clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
				if (objSetup == null)
				{
					objSetup = new clsPropertySetupModel();
				}
				String type = "PDF";
				String fromDate = objBean.getDteFromDate();
				String toDate = objBean.getDteToDate();

				String fd = fromDate.split("-")[0];
				String fm = fromDate.split("-")[1];
				String fy = fromDate.split("-")[2];

				String td = toDate.split("-")[0];
				String tm = toDate.split("-")[1];
				String ty = toDate.split("-")[2];

				String dteFromDate = fy + "-" + fm + "-" + fd;
				String dteToDate = ty + "-" + tm + "-" + td;
			
				ArrayList dataList = new ArrayList();
				List listSubGrp=null;
				sbSql.setLength(0); 
				sbSql.append("select b.strSubGroupCode,b.strSubGroupName,a.strGroupCode,a.strGroupName,a.strCategory from tblacgroupmaster a ,tblsubgroupmaster b"
						+ " where a.strGroupCode=b.strGroupCode "
						+ " and a.strClientCode=b.strClientCode and b.strUnderSubGroup ='' and a.strClientCode='"+clientCode+"'");
				
				Map<String,Object> hmGrpSubGrpAcc=new LinkedHashMap<String,Object>();
				Map<String,Object> hmSubGrpUnderSbGrpAcc=new LinkedHashMap();
				List listUnderSb = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
				if (listUnderSb != null && listUnderSb.size() > 0)
				{
					for(int i=0;i<listUnderSb.size();i++){
						Object obj[]=(Object[])listUnderSb.get(i);
						hmSubGrpUnderSbGrpAcc=new LinkedHashMap();
						String strGroupCode=obj[2].toString();
						String strGroupName=obj[3].toString();
						String strSubGroupCode=obj[0].toString();
						String strSubGroupName=obj[1].toString();
						String strGroupCat=obj[4].toString();
						if(strGroupCat.equalsIgnoreCase("ASSET") || strGroupCat.equalsIgnoreCase("LIABILITY") ){
								funGetUnderSubGroupList(strGroupCode,strSubGroupCode,strSubGroupName,hmSubGrpUnderSbGrpAcc );
								hmGrpSubGrpAcc.put(strGroupCat+"-"+strGroupCode+"_"+strGroupName+"!"+strSubGroupCode+"_"+strSubGroupName, hmSubGrpUnderSbGrpAcc);
						}
						/*strGroupCat="ASSET";
					}else if(strGroupCat.equalsIgnoreCase("EXPENSE")){
						strGroupCat="LIABILITY";*/
						
					}
				}
			
				//System.out.println(hmGrpSubGrpAcc);
				//List<List<String>> listAssets=new ArrayList<List<String>>();
				//List<List<String>> listLiabilities=new ArrayList<List<String>>();
				
				
				
				listExcelData.add("BALANCE SHEET");	//0
				List listData1=new ArrayList();
				listData1.add(companyName);				
				listData1.add("BALANCE SHEET");
				listExcelData.add(listData1);	//1
				
				List listDates=new ArrayList();
				listDates.add("From Date : "+fromDate +"	   To Date : "+toDate);
				listDates.add("");
				listExcelData.add(listDates);	//2
								
				
								
				
				List<List> listHeaders=new ArrayList<List>(); 
				List list=new ArrayList();
				list.add("ASSET");
				listHeaders.add(list);
				list=new ArrayList();
				list.add("LIABILITY");
				listHeaders.add(list);
				
				listExcelData.add(listHeaders); //3
				
				List<List> listTotals=new ArrayList<List>();
				 list=new ArrayList();
				list.add("TOTAL");
				list.add(String.valueOf(0));
				listTotals.add(list);
				
				
				
				
				
			//	listData.add(listLiabilities);
				
			
				
				
				//listData.add(listTotals);
			//	listData.add(listCapitals);
				//listData.add(listCapitalTotals);
								
				
				
/////////////////////////////////////////////////////////////////
				
			//	Map<String,Object> hmGrpSubGrpAcc=(Map) listData.get(0);
				//List<List<String>> list1=new ArrayList<>(); 
				List<String> listLiabilities=new ArrayList<>(); 
				List<String> listAssets=new ArrayList<>(); 
				int cnt=1;
				for(String groupAcc:hmGrpSubGrpAcc.keySet()){
					List accList=new ArrayList<>();
					if(hmGrpSubGrpAcc.get(groupAcc) instanceof Map){
						if(groupAcc.startsWith("LIABILITY")){//Checking category
							listLiabilities.add("LIABILITY");
							groupAcc=groupAcc.replace("LIABILITY-",""); //Removed category name
							String[] arrGrpSubGrp=groupAcc.split("!"); // [0] =Group NAme, [1]= Subgroup Name
							if(!listLiabilities.contains(arrGrpSubGrp[0])){
								listLiabilities.add(arrGrpSubGrp[0]); //Grp Added	
							}
							listLiabilities.add(arrGrpSubGrp[1]); //Sub Grp Added
							
							//Get Sub Grp map 
							Map<String,Object> mapSubGrpAcc=(Map) hmGrpSubGrpAcc.get("LIABILITY-"+groupAcc);
							//Check SubGroupAccs
							String strSubGrpMapKey[]=arrGrpSubGrp[1].toString().split("_");
							List listSubGrpAcc=new ArrayList();
							listSubGrpAcc=funCheckMapKey(mapSubGrpAcc,strSubGrpMapKey[0]+"!"+strSubGrpMapKey[1],listSubGrpAcc);// SubGrp code!name
							if(listSubGrpAcc!=null){
								listLiabilities.addAll(listSubGrpAcc); //All Accounts of Subgroup
							}
							if(listSubGrpAcc.size()==0 &&listSubGrpAcc!=null){
								//check Under SubGroup
								listSubGrpAcc=new ArrayList(); 
								listSubGrpAcc=funIterateMap(mapSubGrpAcc,listSubGrpAcc);
								listLiabilities.addAll(listSubGrpAcc);
							}
							
						}else{
							
							//Checking category
							listAssets.add("ASSET");
							groupAcc=groupAcc.replace("ASSET-",""); //Removed category name
							String[] arrGrpSubGrp=groupAcc.split("!"); // [0] =Group NAme, [1]= Subgroup Name
							if(!listAssets.contains(arrGrpSubGrp[0])){
								listAssets.add(arrGrpSubGrp[0]); //Grp Added	
							}
							listAssets.add(arrGrpSubGrp[1]); //Sub Grp Added
							
							//Get Sub Grp map 
							Map<String,Object> mapSubGrpAcc=(Map) hmGrpSubGrpAcc.get("ASSET-"+groupAcc);
							//Check SubGroupAccs
							String strSubGrpMapKey[]=arrGrpSubGrp[1].toString().split("_");
							List listSubGrpAcc=new ArrayList();
							listSubGrpAcc=funCheckMapKey(mapSubGrpAcc,strSubGrpMapKey[0]+"!"+strSubGrpMapKey[1],listSubGrpAcc);// SubGrp code!name
							if(listSubGrpAcc!=null){
								listAssets.addAll(listSubGrpAcc); //All Accounts of Subgroup
							}
							if(listSubGrpAcc.size()==0 &&listSubGrpAcc!=null){
								//check Under SubGroup
								listSubGrpAcc=new ArrayList(); 
								listSubGrpAcc=funIterateMap(mapSubGrpAcc,listSubGrpAcc);
								listAssets.addAll(listSubGrpAcc);
							}
						}
						
					}

				}
				Map<String,clsIncomeStmtReportBean> hmSalesIncStmt = new HashMap<String,clsIncomeStmtReportBean>();
	
				// Sale JV
				funCalculateBalanceSheetListAccountCode("ASSETS", "tbljvhd", "tbljvdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmSalesIncStmt,listAssets,propertyCode);
				cnt++;
				// Sale Receipt
				funCalculateBalanceSheetListAccountCode("ASSETS", "tblreceipthd", "tblreceiptdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmSalesIncStmt,listAssets,propertyCode);
							
				// Sale Payment
				funCalculateBalanceSheetListAccountCode("ASSETS", "tblpaymenthd", "tblpaymentdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmSalesIncStmt,listAssets,propertyCode);	
					
				
				Map<String,clsIncomeStmtReportBean> hmOtherExpensesIncStmt = new HashMap<String,clsIncomeStmtReportBean>();
				
				 cnt=1;
				 
					
//					// LIABILITY JV
						funCalculateBalanceSheetListAccountCode("LIABILITY", "tbljvhd", "tbljvdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmOtherExpensesIncStmt,listLiabilities,propertyCode);
//							
//					// LIABILITY 
						funCalculateBalanceSheetListAccountCode("LIABILITY", "tblreceipthd", "tblreceiptdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmOtherExpensesIncStmt,listLiabilities,propertyCode);
//								
//					//LIABILITY	
						funCalculateBalanceSheetListAccountCode("LIABILITY", "tblpaymenthd", "tblpaymentdtl", dteFromDate, dteToDate, cnt, clientCode, sbSql, hmOtherExpensesIncStmt,listLiabilities,propertyCode);
								
					// Calculate Gross Profit = Total Income (Sales) - Total Other Expenses (COG)	
						
					List<clsIncomeStmtReportBean> listOfBalancesheet = new ArrayList<clsIncomeStmtReportBean>();
					
					clsIncomeStmtReportBean obAssest;
					for(int k=0;k<listAssets.size();k++){
						String strAssets=listAssets.get(k);
						
						if(hmSalesIncStmt.containsKey(strAssets.trim().split(" ")[0])){
							obAssest=hmSalesIncStmt.get(strAssets.trim().split(" ")[0]);	
							strAssets=strAssets+"!"+obAssest.getDblValue();
							listAssets.set(k,strAssets);
						}
						
						
					}
					for(int k=0;k<listLiabilities.size();k++){
						String strLiability=listLiabilities.get(k);
						
						if(hmOtherExpensesIncStmt.containsKey(strLiability.trim().split(" ")[0])){
							obAssest=hmOtherExpensesIncStmt.get(strLiability.trim().split(" ")[0]);	
							strLiability=strLiability+"!"+obAssest.getDblValue();
							listLiabilities.set(k,strLiability);
						}
						
					}
					
					List listData=new ArrayList();
					listData.add(listLiabilities);
					listData.add(listAssets);
					listExcelData.add(listData);	//4
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					con.close();
				}
				catch (SQLException e)
				{
					e.printStackTrace();
				}
			}
	return new ModelAndView("excelViewForAccountReports", "excelDataList", listExcelData);
 }
		
	private List funIterateMap(Map<String,Object> map){
		List accList=null;
		for(String groupAcc:map.keySet()){
			if(map.get(groupAcc) instanceof Map){
				Map map1=(Map)map.get(groupAcc);
				accList=funIterateMap(map1);
			}else{
				accList=(List) map.get(groupAcc);
			}	
		}
		
		return accList;
	}
	
	private void funGetUnderSubGroupList(String strGroupCode,String strSubGroupCode,String strSubGroupName,Map hmSubGrpUnderSbGrpAcc )
	{
		
		String sql="select a.strGroupCode,a.strSubGroupCode,a.strSubGroupName,a.strUnderSubGroup"
				+ " from tblsubgroupmaster a,tblacgroupmaster b "
				+ " where a.strGroupCode=b.strGroupCode and a.strGroupCode='"+strGroupCode+"'"
				+ " and a.strUnderSubGroup='"+strSubGroupCode+"' ";
		
		List listSubGrp= objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		if (listSubGrp != null && listSubGrp.size() > 0)
		{
			List alSBGrpAccList1=new ArrayList<>();
			Map<String,Object> hmSubGrpUnderSbGrpAcc1=new LinkedHashMap<>();
			for(int j=0;j<listSubGrp.size();j++){
				hmSubGrpUnderSbGrpAcc1=new LinkedHashMap();
				Object obj1[]=(Object[])listSubGrp.get(j);
				 
				funGetUnderSubGroupList(obj1[0].toString(),obj1[1].toString(),obj1[2].toString(),hmSubGrpUnderSbGrpAcc1);
				hmSubGrpUnderSbGrpAcc.putAll(hmSubGrpUnderSbGrpAcc1);
			}
		}else{
			List alAcc=funGetSubGroupAcc(strSubGroupCode,hmSubGrpUnderSbGrpAcc );
			hmSubGrpUnderSbGrpAcc.put(strSubGroupCode+"!"+strSubGroupName, alAcc);
		}
	}
	
	private List funGetSubGroupAcc(String strSubGroupCode,Map hmSubGrpUnderSbGrpAcc ){
		
		String sql="select  a.strAccountCode,a.strAccountName from tblacmaster a ,tblsubgroupmaster b where a.strSubGroupCode=b.strSubGroupCode and a.strClientCode=b.strClientCode "
				+ " and b.strSubGroupCode='"+strSubGroupCode+"' ";

		List listAcc= objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		List alSBGrpAccList2=new ArrayList<>();
		if (listAcc != null && listAcc.size() > 0)
		{
			for(int j=0;j<listAcc.size();j++){
				Object ob[]=(Object[])listAcc.get(j);
				alSBGrpAccList2.add(ob[0].toString()+" "+ob[1].toString());
			}
			
		}
		return alSBGrpAccList2;
	}
	
		
	private List funIterateMap(Map<String,Object> map,List accList){
		for(String groupAcc:map.keySet()){
			if(map.get(groupAcc) instanceof Map){
				Map map1=(Map)map.get(groupAcc);
				accList.add(groupAcc);
				accList=funIterateMap(map1,accList);
			}else if(map.get(groupAcc) instanceof List){
				accList.add(groupAcc);
				accList=(List) map.get(groupAcc);
			}	
		}
		
		return accList;
	}
	
	private List funCheckMapKey(Map<String,Object> mapSubGrp,String key,List AccList){
		//List AccList=null;
		if(mapSubGrp.containsKey(key)){
			if(mapSubGrp.get(key) instanceof List){
				AccList=(List)mapSubGrp.get(key);
			}else if(mapSubGrp.get(key) instanceof Map){
				AccList.add(key);
				funCheckMapKey((Map<String,Object>)mapSubGrp.get(key),key,AccList);
			}
		}
		return AccList;
	}
	

}
