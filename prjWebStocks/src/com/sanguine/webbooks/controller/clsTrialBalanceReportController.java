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

import com.mysql.jdbc.Connection;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.controller.clsStockFlashController;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.model.clsStockFlashModel;
import com.sanguine.service.clsCurrencyMasterService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webbooks.bean.clsCreditorOutStandingReportBean;
import com.sanguine.webbooks.model.clsParameterSetupModel;
import com.sanguine.webbooks.service.clsParameterSetupService;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

@Controller
public class clsTrialBalanceReportController {

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
	
	@Autowired 
	private clsStockFlashController objStkcontroller;
	
	@Autowired
	private clsParameterSetupService objParameterSetupService;
	// Open Buget Form
	@RequestMapping(value = "/frmTrialBalanceReport", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		Map<String, String> hmCurrency = objCurrencyMasterService.funCurrencyListToDisplay(clientCode);
		if (hmCurrency.isEmpty()) {
			hmCurrency.put("", "");
		}
		model.put("currencyList", hmCurrency);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmTrialBalanceReport_1", "command", new clsCreditorOutStandingReportBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmTrialBalanceReport", "command", new clsCreditorOutStandingReportBean());
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/rptTrialBalanceReport", method = RequestMethod.GET)
	private void funReport1(@ModelAttribute("command") clsCreditorOutStandingReportBean objBean, HttpServletResponse resp, HttpServletRequest req) {

		Connection con = objGlobal.funGetConnection(req);
		try {

			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String companyName = req.getSession().getAttribute("companyName").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			String currencyCode=objBean.getStrCurrency();
			String showData=objBean.getStrDocType();
			String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
			StringBuilder sbSql = new StringBuilder();
			sbSql.append("select dblConvToBaseCurr from "+webStockDB+".tblcurrencymaster where strCurrencyCode='"+currencyCode+"' and strClientCode='"+clientCode+"' ");
			try
			{
				List list = objBaseService.funGetList(sbSql,"sql");
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			
			double currValue = Double.parseDouble(req.getSession().getAttribute("currValue").toString());

			clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
			if (objSetup == null) {
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
			String reportName ="";
			
			//Code for Stock in hand start
			double dblStockInHand=0.0;
			String reportType="Total";
			String locationCode = req.getSession().getAttribute("locationCode").toString();
			String param1=reportType+","+locationCode+","+propertyCode+",No,ALL,Both,ALL,No,''";
			String paramForStkLedger=locationCode+","+propertyCode;		
			String manufCode="";
			String prodType="ALL";
			
			clsParameterSetupModel objModel = objParameterSetupService.funGetParameterSetup(clientCode, propertyCode);
			if(null==objModel)
			{
				objModel=new clsParameterSetupModel();
				objModel.setStrStockInHandAccCode("");
				objModel.setStrStockInHandAccName("");
				objModel.setStrClosingCode("");
				objModel.setStrClosingName("");
			}
//			var searchUrl=getContextPath()+"/frmStockFlashTotalReport.html?param1="+param1+"&fDate="+fromDate+"&tDate="+toDate+"&prodType="+prodType+"&ManufCode="+manufCode;
			List list=objStkcontroller.funShowStockTotalFlash( param1,fromDate,toDate, prodType, manufCode,  req, resp);
			if(list.size()>0)
			{
				List listStk=(List) list.get(0);
				clsStockFlashModel objStkModel=(clsStockFlashModel) listStk.get(17);
				dblStockInHand=Double.parseDouble(objStkModel.getDblValue().toString());
			
			}//stock in hand end
			
			if(objBean.getStrDocType().equals("All"))
			{
				 reportName = servletContext.getRealPath("/WEB-INF/reports/webbooks/rptTrialBalanceReportHavingAllCreditDebit.jrxml");
			}else{
				reportName = servletContext.getRealPath("/WEB-INF/reports/webbooks/rptTrialBalanceDebitCreditReport.jrxml");
			}String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");

			List<clsCreditorOutStandingReportBean> fieldList = new ArrayList<clsCreditorOutStandingReportBean>();
			String startDate = req.getSession().getAttribute("startDate").toString();
			String[] sp = startDate.split(" ");
			String[] spDate = sp[0].split("/");
			startDate = spDate[2] + "-" + spDate[1] + "-" + spDate[0];
			
			String tempFromDate = dteFromDate.split("-")[2] + "-" + dteFromDate.split("-")[1] + "-" + dteFromDate.split("-")[0];
			SimpleDateFormat obj = new SimpleDateFormat("dd-MM-yyyy");
			Date dt1 = obj.parse(tempFromDate);
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(dt1);
			cal.add(Calendar.DATE, -1);
			String newToDate = (cal.getTime().getYear() + 1900) + "-" + (cal.getTime().getMonth() + 1) + "-" + (cal.getTime().getDate());
			
			String showColumn="";
			
			sbSql.setLength(0);
			sbSql.append(" select b.strGroupCode,ifNull(b.strGroupName,''),a.strAccountCode,a.strAccountName,b.strDefaultType "
				+ " ,IF(a.strCrDr='Dr',a.intOpeningBal,0) dblDebitAmt, IF(a.strCrDr='Cr',a.intOpeningBal,0) dblCreditAmt "
				+ " ,(IF(a.strCrDr='Dr',a.intOpeningBal,0) - IF(a.strCrDr='Cr',a.intOpeningBal,0)) dblBalanceAmt,strCategory " 
				+ " from tblacmaster a,tblacgroupmaster b,tblsubgroupmaster s " 
				+ " where s.strGroupCode=b.strGroupCode and s.strSubGroupCode=a.strSubGroupCode and  a.strClientCode='" + clientCode + "' and a.strPropertyCode='" + propertyCode + "' "
				+ " ORDER by a.strGroupCode ");
			Map<String,clsCreditorOutStandingReportBean> hmTrailBalance=new HashMap<String,clsCreditorOutStandingReportBean>();
			List listAccountDetails = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
			
			for (int i = 0; i < listAccountDetails.size(); i++) {
				Object[] objArr = (Object[]) listAccountDetails.get(i);
			
				if(objArr[2].toString().contains("direct") || objArr[2].toString().equals("1005-005-07"))
				{
					System.out.println(objArr[2].toString()+"    "+objArr[7].toString()+"    "+objArr[3].toString());
				}
				
				clsCreditorOutStandingReportBean objHmBean =new clsCreditorOutStandingReportBean();
				objHmBean.setStrGroupCode(objArr[0].toString());
				objHmBean.setStrGroupName(objArr[1].toString());
				objHmBean.setStrAccountCode(objArr[2].toString());
				objHmBean.setStrAccountName(objArr[3].toString());
				objHmBean.setTypeDebitCredit(objArr[4].toString());
				double openingAmt=0;
				if(objArr[8].toString().equals("DIRECT INCOME") || objArr[8].toString().equals("INDIRECT EXPENSES") 
						|| objArr[8].toString().equals("INDIRECT INCOME") || objArr[8].toString().equals("DIRECT EXPENSES")){
					objHmBean.setDblOpDrAmt(0);
					objHmBean.setDblOpCrAmt(0);
				}else{
					objHmBean.setDblOpDrAmt(Double.parseDouble(objArr[5].toString()));
					objHmBean.setDblOpCrAmt(Double.parseDouble(objArr[6].toString()));
					openingAmt=Double.parseDouble(objArr[5].toString())-Double.parseDouble(objArr[6].toString());
				}
				
				objHmBean.setDblOpnAmt(openingAmt);
				objHmBean.setDblBalAmt(openingAmt);
				objHmBean.setDblDrAmt(0.00);
				objHmBean.setDblCrAmt(0.00);
				
				hmTrailBalance.put(objArr[2].toString(),objHmBean);
			}
	
			BigDecimal bDTotalDebit=new BigDecimal(0);
			BigDecimal bDTotalCredit=new BigDecimal(0);
			if (!startDate.equals(dteFromDate)) {
								
				funCalAccountTransactionBal(userCode, propertyCode, clientCode, startDate, newToDate, hmTrailBalance,true);
			}
			funCalAccountTransactionBal(userCode, propertyCode, clientCode, dteFromDate, dteToDate, hmTrailBalance,false);
			
			for(Map.Entry<String, clsCreditorOutStandingReportBean> entry:hmTrailBalance.entrySet())
			{
				if(null!=entry.getValue().getStrGroupName())
				{
					
					clsCreditorOutStandingReportBean objHmBean =entry.getValue();
					objHmBean.setStrGroupCode(entry.getValue().getStrGroupCode());
					objHmBean.setStrGroupName(entry.getValue().getStrGroupName());
					objHmBean.setDblOpnAmt(entry.getValue().getDblOpnAmt());
					objHmBean.setTypeDebitCredit(entry.getValue().getTypeDebitCredit());
					//Credit amount
					if(entry.getValue().getDblCrAmt()<0)
					{
						objHmBean.setDblCrAmt(entry.getValue().getDblCrAmt()*(-1));
					}
					// Opening Credit amount
					if(entry.getValue().getDblOpCrAmt()<0)
					{
						objHmBean.setDblOpCrAmt(entry.getValue().getDblOpCrAmt()*(-1));
					}
					
					
					//For Two Column Report
					if(entry.getValue().getDblBalAmt()>0)
					{
						bDTotalDebit=bDTotalDebit.add(BigDecimal.valueOf(entry.getValue().getDblBalAmt()));
					}	
					if(entry.getValue().getDblBalAmt()<0)
					{
						bDTotalCredit=bDTotalCredit.add(BigDecimal.valueOf(entry.getValue().getDblBalAmt()));
					}
							  
					fieldList.add(objHmBean);
					
					/*if(objHmBean.getDblCrAmt() == 0 
							&& objBean.getDblDrAmt() == 0 
							&& objBean.getDblOpCrAmt() == 0 
							&& objBean.getDblOpDrAmt() == 0)
					{
						
					}
					else
					{	
						fieldList.add(objHmBean);
					}*/
//					System.out.println(objHmBean.getStrAccountCode()+","+objHmBean.getStrAccountName()+","+objHmBean.getDblDrAmt()+","+ objHmBean.getDblCrAmt() );
				}
			}
			
			clsCreditorOutStandingReportBean objHmBean =new clsCreditorOutStandingReportBean();
			objHmBean.setStrGroupCode("1001");
			objHmBean.setStrGroupName("CURRENT ASSETS");
			objHmBean.setStrAccountCode(objModel.getStrStockInHandAccCode());
			objHmBean.setStrAccountName(objModel.getStrStockInHandAccName());
			objHmBean.setTypeDebitCredit("Debit");
			objHmBean.setDblOpDrAmt(0.0);
			objHmBean.setDblOpCrAmt(0.0);
			double openingAmt=0.0;
			objHmBean.setDblOpnAmt(openingAmt);
			objHmBean.setDblBalAmt(openingAmt);
			objHmBean.setDblDrAmt(dblStockInHand);
			objHmBean.setDblCrAmt(0.00);
			/*if(objHmBean.getDblCrAmt() == 0 
					&& objBean.getDblDrAmt() == 0 
					&& objBean.getDblOpCrAmt() == 0 
					&& objBean.getDblOpDrAmt() == 0)
			{
				
			}
			else
			{
				fieldList.add(objHmBean);
			}*/
			fieldList.add(objHmBean);
			 objHmBean =new clsCreditorOutStandingReportBean();
			objHmBean.setStrGroupCode("1002");
			objHmBean.setStrGroupName("SALES");
			objHmBean.setStrAccountCode(objModel.getStrClosingCode());
			objHmBean.setStrAccountName(objModel.getStrClosingName());
			objHmBean.setTypeDebitCredit("Credit");
			objHmBean.setDblOpDrAmt(0.0);
			objHmBean.setDblOpCrAmt(0.0);
			 openingAmt=0.0;
			objHmBean.setDblOpnAmt(openingAmt);
			objHmBean.setDblBalAmt(openingAmt);
			objHmBean.setDblDrAmt(0.00);
			objHmBean.setDblCrAmt(dblStockInHand);
			/*if(objHmBean.getDblCrAmt() == 0 
					&& objBean.getDblDrAmt() == 0 
					&& objBean.getDblOpCrAmt() == 0 
					&& objBean.getDblOpDrAmt() == 0)
			{
				
			}
			else
				{
					fieldList.add(objHmBean);
				}*/
			
			fieldList.add(objHmBean);
			
			Collections.sort(fieldList, new clsTrialBalBeanComparator());
			
			bDTotalDebit.add(BigDecimal.valueOf(dblStockInHand));
			bDTotalCredit.add(BigDecimal.valueOf(dblStockInHand));
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
			hm.put("showColumn", showColumn);
			hm.put("bDTotalDebit", bDTotalDebit);
			hm.put("bDTotalCredit", bDTotalCredit);

		
			List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();

			JasperDesign jd = JRXmlLoader.load(reportName);
			JasperReport jr = JasperCompileManager.compileReport(jd);
			JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(fieldList);
			JasperPrint print = JasperFillManager.fillReport(jr, hm, beanCollectionDataSource);
			
			jprintlist.add(print);
			ServletOutputStream servletOutputStream = resp.getOutputStream();
			if (jprintlist.size() > 0) {
			if(objBean.getTypeDebitCredit().equals("PDF"))
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
			else {
					JRXlsExporter exporter = new JRXlsExporter();
					resp.setContentType("application/xlsx");
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jprintlist);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
					exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					resp.setHeader("Content-Disposition","inline;filename=rptTrailBalReport_"+dteFromDate+"_To_"+dteToDate+"_"+userCode+".xls");
					exporter.exportReport();
					servletOutputStream.write(byteArrayOutputStream.toByteArray()); 
					servletOutputStream.flush();
					servletOutputStream.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}

	}

	
	
	private int funCalAccountTransactionBal(String userCode, String propertyCode, String clientCode, String fromDate, String toDate, Map<String, clsCreditorOutStandingReportBean> hmTrialBal, boolean flgCalOpBal)throws Exception
	{
		double jvDrAmt=0,jvCrAmt=0;
		double payDrAmt=0,payCrAmt=0;
		double recDrAmt=0,recCrAmt=0;
		
		StringBuilder sbSql = new StringBuilder();
		sbSql.append(" SELECT b.strAccountCode,b.strAccountName,sum(b.dblDrAmt),sum(b.dblCrAmt),sum(b.dblDrAmt - b.dblCrAmt) "
			+ " FROM tbljvhd a, tbljvdtl b "
			+ " WHERE a.strVouchNo=b.strVouchNo AND DATE(a.dteVouchDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' AND a.strPropertyCode=b.strPropertyCode "
			+ " AND a.strPropertyCode='" + propertyCode + "' AND a.strClientCode='" + clientCode + "' group by b.strAccountCode ") ;
		List listJvAmt = objBaseService.funGetListModuleWise(sbSql, "sql", "WebBooks");
		
		for (int j = 0; j < listJvAmt .size(); j++) {
			clsCreditorOutStandingReportBean objOutStBean=new clsCreditorOutStandingReportBean();
			Object[] arrObj = (Object[]) listJvAmt .get(j);

			if(flgCalOpBal)
			{
				if(hmTrialBal.containsKey(arrObj[0].toString()))
				{
				    objOutStBean=hmTrialBal.get(arrObj[0].toString());
					objOutStBean.setDblOpDrAmt(objOutStBean.getDblOpDrAmt()+(Double.parseDouble(arrObj[2].toString())));
					objOutStBean.setDblOpCrAmt(objOutStBean.getDblOpCrAmt()+(Double.parseDouble(arrObj[2].toString())));
				}else{
					objOutStBean.setStrAccountCode(arrObj[0].toString());
					objOutStBean.setStrAccountName(arrObj[1].toString());
					objOutStBean.setDblOpDrAmt(Double.parseDouble(arrObj[2].toString()));
					objOutStBean.setDblOpCrAmt(Double.parseDouble(arrObj[3].toString()));
				}
				double openingAmt=objOutStBean.getDblOpDrAmt()-objOutStBean.getDblOpCrAmt();
				objOutStBean.setDblOpnAmt(openingAmt);
			}
			else
			{
				if(hmTrialBal.containsKey(arrObj[0].toString()))
				{
				    objOutStBean=hmTrialBal.get(arrObj[0].toString());
					objOutStBean.setDblDrAmt(objOutStBean.getDblDrAmt()+(Double.parseDouble(arrObj[2].toString())));
					objOutStBean.setDblCrAmt(objOutStBean.getDblCrAmt()+(Double.parseDouble(arrObj[3].toString())));
				}else{
					objOutStBean.setStrAccountCode(arrObj[0].toString());
					objOutStBean.setStrAccountName(arrObj[1].toString());
					objOutStBean.setDblDrAmt(Double.parseDouble(arrObj[2].toString()));
					objOutStBean.setDblCrAmt(Double.parseDouble(arrObj[3].toString()));
				}
				jvDrAmt+=Double.parseDouble(arrObj[2].toString());
				jvCrAmt+=Double.parseDouble(arrObj[3].toString());
			}
			
			hmTrialBal.put(arrObj[0].toString(), objOutStBean);
			System.out.println(objOutStBean.getStrAccountCode()+","+objOutStBean.getStrAccountName()+","+objOutStBean.getDblDrAmt()+","+ objOutStBean.getDblCrAmt()+","+objOutStBean.getDblOpnAmt());
		}
		
		
		sbSql.setLength(0);
		sbSql.append(" SELECT b.strAccountCode,b.strAccountName,sum(b.dblDrAmt),sum(b.dblCrAmt),sum(b.dblDrAmt - b.dblCrAmt) "
				+ " FROM tblpaymenthd a, tblpaymentdtl b "
				+ " WHERE a.strVouchNo=b.strVouchNo AND DATE(a.dteVouchDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' AND a.strPropertyCode=b.strPropertyCode "
				+ " AND a.strPropertyCode='" + propertyCode + "' AND a.strClientCode='" + clientCode + "' group by b.strAccountCode ") ;
		List listPayment = objBaseService.funGetListModuleWise(sbSql, "sql", "WebBooks");
			
		for (int j = 0; j < listPayment.size(); j++) {
			clsCreditorOutStandingReportBean objOutStBean=new clsCreditorOutStandingReportBean();
			Object[] arrObj = (Object[]) listPayment.get(j);
			
			if(flgCalOpBal)
			{
				if(hmTrialBal.containsKey(arrObj[0].toString()))
				{
				    objOutStBean=hmTrialBal.get(arrObj[0].toString());
					objOutStBean.setDblOpDrAmt(objOutStBean.getDblOpDrAmt()+(Double.parseDouble(arrObj[2].toString())));
					objOutStBean.setDblOpCrAmt(objOutStBean.getDblOpCrAmt()+(Double.parseDouble(arrObj[2].toString())));
				}else{
					objOutStBean.setStrAccountCode(arrObj[0].toString());
					objOutStBean.setStrAccountName(arrObj[1].toString());
					objOutStBean.setDblOpDrAmt(Double.parseDouble(arrObj[2].toString()));
					objOutStBean.setDblOpCrAmt(Double.parseDouble(arrObj[3].toString()));
				}
				double openingAmt=objOutStBean.getDblOpDrAmt()-objOutStBean.getDblOpCrAmt();
				objOutStBean.setDblOpnAmt(openingAmt);
			}
			else
			{
				if(hmTrialBal.containsKey(arrObj[0].toString()))
				{
				    objOutStBean=hmTrialBal.get(arrObj[0].toString());
					objOutStBean.setDblDrAmt(objOutStBean.getDblDrAmt()+(Double.parseDouble(arrObj[2].toString())));
					objOutStBean.setDblCrAmt(objOutStBean.getDblCrAmt()+(Double.parseDouble(arrObj[3].toString())));
				}else{
					objOutStBean.setStrAccountCode(arrObj[0].toString());
					objOutStBean.setStrAccountName(arrObj[1].toString());
					objOutStBean.setDblDrAmt(Double.parseDouble(arrObj[2].toString()));
					objOutStBean.setDblCrAmt(Double.parseDouble(arrObj[3].toString()));
				}
				payDrAmt+=Double.parseDouble(arrObj[2].toString());
				payCrAmt+=Double.parseDouble(arrObj[3].toString());
			}
				
			hmTrialBal.put(arrObj[0].toString(), objOutStBean);
		}
		
		
		sbSql.setLength(0);
		sbSql.append(" SELECT b.strAccountCode,b.strAccountName,sum(b.dblDrAmt),sum(b.dblCrAmt),sum(b.dblDrAmt - b.dblCrAmt) "
				+ " FROM tblreceipthd a, tblreceiptdtl b "
				+ " WHERE a.strVouchNo=b.strVouchNo AND DATE(a.dteVouchDate) BETWEEN '" + fromDate + "' AND '" + toDate + "' AND a.strPropertyCode=b.strPropertyCode "
				+ " AND a.strPropertyCode='" + propertyCode + "' AND a.strClientCode='" + clientCode + "' group by b.strAccountCode ") ;
		List listReceipt = objBaseService.funGetListModuleWise(sbSql, "sql", "WebBooks");
			
		for (int j = 0; j < listReceipt.size(); j++) {
			clsCreditorOutStandingReportBean objOutStBean=new clsCreditorOutStandingReportBean();
			Object[] arrObj = (Object[]) listReceipt.get(j);
			
			if(flgCalOpBal)
			{
				if(hmTrialBal.containsKey(arrObj[0].toString()))
				{
				    objOutStBean=hmTrialBal.get(arrObj[0].toString());
					objOutStBean.setDblOpDrAmt(objOutStBean.getDblOpDrAmt()+(Double.parseDouble(arrObj[2].toString())));
					objOutStBean.setDblOpCrAmt(objOutStBean.getDblOpCrAmt()+(Double.parseDouble(arrObj[2].toString())));
				}else{
					objOutStBean.setStrAccountCode(arrObj[0].toString());
					objOutStBean.setStrAccountName(arrObj[1].toString());
					objOutStBean.setDblOpDrAmt(Double.parseDouble(arrObj[2].toString()));
					objOutStBean.setDblOpCrAmt(Double.parseDouble(arrObj[3].toString()));
				}
				double openingAmt=objOutStBean.getDblOpDrAmt()-objOutStBean.getDblOpCrAmt();
				objOutStBean.setDblOpnAmt(openingAmt);
			}
			else
			{
				if(hmTrialBal.containsKey(arrObj[0].toString()))
				{
				    objOutStBean=hmTrialBal.get(arrObj[0].toString());
					objOutStBean.setDblDrAmt(objOutStBean.getDblDrAmt()+(Double.parseDouble(arrObj[2].toString())));
					objOutStBean.setDblCrAmt(objOutStBean.getDblCrAmt()+(Double.parseDouble(arrObj[3].toString())));
				}else{
					objOutStBean.setStrAccountCode(arrObj[0].toString());
					objOutStBean.setStrAccountName(arrObj[1].toString());
					objOutStBean.setDblDrAmt(Double.parseDouble(arrObj[2].toString()));
					objOutStBean.setDblCrAmt(Double.parseDouble(arrObj[3].toString()));
				}
				recDrAmt+=Double.parseDouble(arrObj[2].toString());
				recCrAmt+=Double.parseDouble(arrObj[3].toString());
			}
				
			hmTrialBal.put(arrObj[0].toString(), objOutStBean);
		}
				
		/*System.out.println("JV Dr = "+new BigDecimal(jvDrAmt)+"\tJv Cr = "+new BigDecimal(jvCrAmt));
		System.out.println("Pay Dr = "+new BigDecimal(payDrAmt)+"\tPay Cr = "+new BigDecimal(payCrAmt));
		System.out.println("Rec Dr = "+new BigDecimal(recDrAmt)+"\tRec Cr = "+new BigDecimal(recCrAmt));*/
		
		return 1;
	}
}

class clsTrialBalBeanComparator implements Comparator<clsCreditorOutStandingReportBean>
{

	@Override
	public int compare(clsCreditorOutStandingReportBean arg0, clsCreditorOutStandingReportBean arg1) {
		// TODO Auto-generated method stub
		return arg0.getStrGroupName().compareTo(arg1.getStrGroupName());
	}
	
}
