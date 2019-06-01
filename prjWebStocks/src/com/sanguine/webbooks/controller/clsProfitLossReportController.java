package com.sanguine.webbooks.controller;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.Connection;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webbooks.bean.clsProfitLossReportBean;

@Controller
public class clsProfitLossReportController {

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	private clsGlobalFunctions objGlobal = null;

	// Open AccountHolderMaster
	@RequestMapping(value = "/frmProfitLossWebBook", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		if (urlHits.equalsIgnoreCase("1")) {
			return new ModelAndView("frmProfitLossWebBook", "command", new clsProfitLossReportBean());
		} else {
			return new ModelAndView("frmProfitLossWebBook_1", "command", new clsProfitLossReportBean());
		}
	}

	@RequestMapping(value = "/rptProfitLossReport", method = RequestMethod.GET)
	private void funReport(@ModelAttribute("command") clsProfitLossReportBean objBean, HttpServletResponse resp, HttpServletRequest req) {
		objGlobal = new clsGlobalFunctions();
		Connection con = objGlobal.funGetConnection(req);
		String strCurr = req.getSession().getAttribute("currValue").toString();
		double currValue = Double.parseDouble(strCurr);
		try {

			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String companyName = req.getSession().getAttribute("companyName").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
			if (objSetup == null) {
				objSetup = new clsPropertySetupModel();
			}
			
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
			String reportName = servletContext.getRealPath("/WEB-INF/reports/webbooks/rptProfitLossReport.jrxml");
			String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
			List<clsProfitLossReportBean> dataListPaymnet = new ArrayList<clsProfitLossReportBean>();
			List<clsProfitLossReportBean> dataListRecipt = new ArrayList<clsProfitLossReportBean>();
			List<clsProfitLossReportBean> dataListExtraExpense = new ArrayList<clsProfitLossReportBean>();
			// String
			// sqlDtl="select a.strVouchNo as strVouchNo,b.dblAmt/"+currValue+",a.strNarration from tblpaymenthd a ,tblpaymentdebtordtl b where a.strVouchNo=b.strVouchNo "
			// +" and a.dteVouchDate between '"+dteFromDate+"' and '"+dteToDate+"' and a.strPropertyCode='"+propertyCode+"' and a.strClientCode='"+clientCode+"'  ";
			// List listPayment =
			// objGlobalFunctionsService.funGetDataList(sqlDtl,"sql");
			// double totalPayment=0.0;
			// double totalRecipt=0.0;
			// if(listPayment.size()>0)
			// {
			// for(int i=0;i<listPayment.size() ;i++)
			// {
			// Object [] obj=(Object[]) listPayment.get(i);
			// clsProfitLossReportBean objProfitLoss=new
			// clsProfitLossReportBean();
			// objProfitLoss.setStrVouchNo(obj[0].toString());
			// objProfitLoss.setDblAmt(Double.parseDouble(obj[1].toString()));
			// objProfitLoss.setStrNarration(obj[2].toString());
			// totalPayment=totalPayment+Double.parseDouble(obj[1].toString());
			// dataListPaymnet.add(objProfitLoss);
			// }
			// }
			//
			// String
			// sqlRecipt="select a.strVouchNo as strVouchNo ,b.dblAmt/"+currValue+" as dblAmt ,a.strNarration   from tblreceipthd a,tblreceiptdebtordtl b  "
			// +" where a.strVouchNo=b.strVouchNo and a.dteVouchDate between '"+dteFromDate+"' and '"+dteToDate+"' and a.strPropertyCode='"+propertyCode+"' and a.strClientCode='"+clientCode+"' ";
			//
			// List listRecipt =
			// objGlobalFunctionsService.funGetDataList(sqlRecipt,"sql");
			// if(listRecipt.size()>0)
			// {
			// for(int i=0;i<listRecipt.size() ;i++)
			// {
			// Object [] obj=(Object[]) listRecipt.get(i);
			// clsProfitLossReportBean objProfitLoss=new
			// clsProfitLossReportBean();
			// objProfitLoss.setStrVouchNo(obj[0].toString());
			// objProfitLoss.setDblAmt(Double.parseDouble(obj[1].toString()));
			// objProfitLoss.setStrNarration(obj[2].toString());
			// totalRecipt=totalRecipt+Double.parseDouble(obj[1].toString());
			// dataListRecipt.add(objProfitLoss);
			// }
			// }
			// double totalExpenses=0.0;
			//
			//
			//
			//
			//
			// String
			// sqlExtraExpense="select a.strAccountName,ifnull((b.dblAmt-c.dblAmt)/"+currValue+",0) as dblAmt from tblacgroupmaster  d, tblacmaster a "
			// +" left outer join tblreceiptdebtordtl b on a.strAccountCode=b.strAccountCode  and b.dteBillDate  between '"+dteFromDate+"' and '"+dteToDate+"' "
			// +" left outer join tblpaymentdebtordtl c on a.strAccountCode=c.strAccountCode   and b.dteBillDate  between '"+dteFromDate+"' and '"+dteToDate+"' "
			// +" where a.strType='GL Code' and a.strCreditor ='No' and a.strDebtor='No' and a.strClientCode='"+clientCode+"' and a.strPropertyCode='"+propertyCode+"' and a.strGroupCode=d.strGroupCode and d.strCategory='EXPENSE' ";
			//
			// List listExpenses =
			// objGlobalFunctionsService.funGetDataList(sqlExtraExpense,"sql");
			// if(listExpenses.size()>0)
			// {
			// for(int i=0;i<listExpenses.size() ;i++)
			// {
			// Object [] obj=(Object[]) listExpenses.get(i);
			// if(!(Double.parseDouble(obj[1].toString())==0))
			// {
			// clsProfitLossReportBean objProfitLoss=new
			// clsProfitLossReportBean();
			// objProfitLoss.setStrVouchNo(obj[0].toString());
			// objProfitLoss.setDblAmt(Double.parseDouble(obj[1].toString()));
			// totalExpenses=totalExpenses+Double.parseDouble(obj[1].toString());
			// dataListExtraExpense.add(objProfitLoss);}
			// }
			// }

			double conversionRate = 1;
//			String webStockDB = req.getSession().getAttribute("WebStockDB").toString();
//			StringBuilder sbSql = new StringBuilder();
//			sbSql.append("select dblConvToBaseCurr from " + webStockDB + ".tblcurrencymaster where strCurrencyCode='" + currencyCode + "' and strClientCode='" + clientCode + "' ");
//			try
//			{
//				List list = objBaseService.funGetList(sbSql, "sql");
//				conversionRate = Double.parseDouble(list.get(0).toString());
//			}
			
			
			String prevProdCode="";
			int cnt=-1;
			
			List<clsProfitLossReportBean> listProduct=new ArrayList<clsProfitLossReportBean>(); 
			String sqlIncome = "SELECT b.strProdCode,c.strProdName,c.dblCostRM,b.dblQty, b.dblUnitPrice, b.dblQty*b.dblUnitPrice,right(b.strProdCode,7) "
							 + " FROM tblinvoicehd a,tblinvoicedtl b,tblproductmaster c "
							 + " WHERE DATE(a.dteInvDate) BETWEEN '" + dteFromDate + "' and '" + dteToDate + "' AND a.strInvCode=b.strInvCode "
							 + " AND b.strProdCode=c.strProdCode AND a.strClientCode='"+clientCode+"' "
							 + " order by b.strProdCode";

			List listIncome = objGlobalFunctionsService.funGetList(sqlIncome, "sql");
			
			
			for(int i=0;i<listIncome.size();i++)
			{
				Object[] objArr = (Object[]) listIncome.get(i);
				String prodCode = objArr[0].toString();
				double qty = Double.parseDouble(objArr[3].toString());
				double purchaseAmt = (Double.parseDouble(objArr[2].toString())*qty)/conversionRate;
				double saleAmt = (Double.parseDouble(objArr[5].toString()))/conversionRate;
				clsProfitLossReportBean objProfitLoss = new clsProfitLossReportBean();			


				if(prevProdCode.equals(objArr[0].toString()) )
				{
					objProfitLoss=listProduct.get(cnt);
				
					objProfitLoss.setDblPurAmt(objProfitLoss.getDblPurAmt()+purchaseAmt);
					objProfitLoss.setDblSaleAmt(objProfitLoss.getDblSaleAmt()+saleAmt);
					System.out.println(prodCode+"      "+qty+"      "+purchaseAmt+"      "+saleAmt);
				}
				else
				{
					
					objProfitLoss.setDblPurAmt(purchaseAmt);
					objProfitLoss.setDblSaleAmt(saleAmt);
					listProduct.add(objProfitLoss);
					cnt++;
				}
				prevProdCode=prodCode;
			}
			
			Map<String,clsProfitLossReportBean> hmSalesIncStmt = new HashMap<String,clsProfitLossReportBean>();
			funCalculateIncomeStmt("Expense", "tbljvhd", "tbljvdtl", dteFromDate, dteToDate, clientCode, hmSalesIncStmt,propertyCode);
			
		// Sale Receipt
			funCalculateIncomeStmt("Expense", "tblreceipthd", "tblreceiptdtl", dteFromDate, dteToDate, clientCode, hmSalesIncStmt,propertyCode);
					
		// Sale Payment
			funCalculateIncomeStmt("Expense", "tblpaymenthd", "tblpaymentdtl", dteFromDate, dteToDate, clientCode, hmSalesIncStmt,propertyCode);	
			BigDecimal totalExpense=new BigDecimal(0);
			for(Map.Entry<String, clsProfitLossReportBean> entry:hmSalesIncStmt.entrySet())
			{
				
				//listOfIncomeStatement.add(entry.getValue());
				clsProfitLossReportBean objProfitLoss = new clsProfitLossReportBean();
				objProfitLoss.setStrAccountName(entry.getValue().getStrAccountName());
				objProfitLoss.setDblAmt(entry.getValue().getDblAmt());
				totalExpense=totalExpense.add(entry.getValue().getDblAmt());
			
				dataListExtraExpense.add(objProfitLoss);
				
			}
/*			String sqlExtraExpense = "select a.strAccountName,ifnull((b.dblAmt-c.dblAmt)/17.0,0) as dblAmt ,a.strAccountCode ,d.strGroupName,d.strCategory "
									+ " from tblacgroupmaster  d  ,tblacmaster a  " + " left outer join tblreceiptdebtordtl b on a.strAccountCode=b.strAccountCode " 
									+ " and b.dteBillDate  between '" + dteFromDate + "' and '" + dteToDate + "'   "
									+ " left outer join tblpaymentdebtordtl c on a.strAccountCode=c.strAccountCode   "
									+ " and c.dteBillDate  between '" + dteFromDate + "' and '" + dteToDate + "'   " + " where a.strType='GL Code' and a.strCreditor ='No' and a.strDebtor='No' and a.strGroupCode=d.strGroupCode " 
									+ " and d.strCategory like'%EXPENSE' " + " and a.strClientCode='" + clientCode + "' and a.strPropertyCode='"
									+ propertyCode + "' order by b.dteBillDate ;";

			List listExpenses = objGlobalFunctionsService.funGetDataList(sqlExtraExpense, "sql");
			if (listExpenses.size() > 0) {
				for (int i = 0; i < listExpenses.size(); i++) {
					Object[] obj = (Object[]) listExpenses.get(i);
					if (!(Double.parseDouble(obj[1].toString()) == 0)) {
						clsProfitLossReportBean objProfitLoss = new clsProfitLossReportBean();
						objProfitLoss.setStrVouchNo(obj[0].toString());
						objProfitLoss.setDblAmt(Double.parseDouble(obj[1].toString()));
						totalExpense = totalExpense + Double.parseDouble(obj[1].toString());
						dataListExtraExpense.add(objProfitLoss);
					}
				}
			}*/
			BigDecimal purAmt=new BigDecimal(0);
			BigDecimal dblRevenue = new BigDecimal(0);
			for(clsProfitLossReportBean obj:listProduct)
			{
				 purAmt=purAmt.add(BigDecimal.valueOf(obj.getDblPurAmt()));
				 dblRevenue=dblRevenue.add(BigDecimal.valueOf(obj.getDblSaleAmt()));
			}
			BigDecimal grossProfit=dblRevenue.subtract(purAmt);			
			
			///Percentage Calculation
			
			BigDecimal perCostOfGoodSold=purAmt.divide(dblRevenue, 2, RoundingMode.HALF_UP);
			perCostOfGoodSold=perCostOfGoodSold.multiply(new BigDecimal(100));
			
			BigDecimal perTotalExp=totalExpense.divide(dblRevenue, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
			BigDecimal perGrossProfit=grossProfit.divide(dblRevenue, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
			BigDecimal netProfit=grossProfit.subtract(totalExpense);
			BigDecimal perNetProfit=netProfit.divide(dblRevenue, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
			if (perNetProfit.compareTo(BigDecimal.ZERO) < 0)
			{
				perNetProfit=perNetProfit.multiply(new BigDecimal(-1));
			}

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
			hm.put("fromDate", fromDate);
			hm.put("toDate", toDate);
			hm.put("dataListExtraExpense", dataListExtraExpense);
			hm.put("dataListRecipt", dataListRecipt);
			hm.put("totalExpense", totalExpense);
			
			hm.put("dblGrossProfit", grossProfit);
			hm.put("dblRevenue", dblRevenue);
			hm.put("dblCostofGood", purAmt);
			
			hm.put("perCostOfGoodSold", perCostOfGoodSold);
			hm.put("perTotalExp", perTotalExp);
			hm.put("netProfit", netProfit);
			hm.put("perNetProfit", perNetProfit);
			hm.put("perGrossProfit", perGrossProfit);
			
			
			
			JasperDesign jd = JRXmlLoader.load(reportName);
			JasperReport jr = JasperCompileManager.compileReport(jd);

			JasperPrint jp = JasperFillManager.fillReport(jr, hm, new JREmptyDataSource());
			List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
			ServletOutputStream servletOutputStream = resp.getOutputStream();
			jprintlist.add(jp);

			if (objBean.getStrDocType().trim().equalsIgnoreCase("pdf")) {
				
				byte[] bytes = null;
				bytes = JasperRunManager.runReportToPdf(jr, hm, con);
				resp.setContentType("application/pdf");
				resp.setContentLength(bytes.length);
				servletOutputStream.write(bytes, 0, bytes.length);
				servletOutputStream.flush();
				servletOutputStream.close();
			} else if (objBean.getStrDocType().trim().equalsIgnoreCase("xls")) {
				JRXlsExporter exporter = new JRXlsExporter();
				resp.setContentType("application/xlsx");
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jprintlist);
				exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
				exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
				resp.setHeader("Content-Disposition","inline;filename=rptProfitLossReport_"+dteFromDate+"_To_"+dteToDate+"_"+userCode+".xls");
				exporter.exportReport();
				servletOutputStream.write(byteArrayOutputStream.toByteArray()); 
				servletOutputStream.flush();
				servletOutputStream.close();

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
	
	private void funCalculateIncomeStmt(String catType, String hdTableName, String dtlTableName, String fromDate, String toDate
			,  String clientCode,  Map<String,clsProfitLossReportBean> hmIncomeStatement,String propCode)
	{
		StringBuilder sbSql=new StringBuilder();
		sbSql.setLength(0);
		StringBuilder sbOp=new StringBuilder(); 		
		
		
//		sbSql.append("select a.strType,b.strCategory,b.strGroupName,d.strCrDr,sum(d."+crdrColumn+"),a.strAccountName,a.strAccountCode "
//				+ "from tblacmaster a,tblacgroupmaster b,"+hdTableName+" c,"+dtlTableName+" d "
//				+ "where a.strGroupCode=b.strGroupCode "
//				+ "and a.strAccountCode=d.strAccountCode "
//				+ "and c.strVouchNo=d.strVouchNo "
//				+ "and b.strCategory='"+catType+"' "
//				+ "and a.strType='GL Code' "
//				+ "and date(c.dteVouchDate) between '" + fromDate + "' and '" + toDate + "' and c.strClientCode='"+clientCode+"' "
//				+ "group by a.strAccountCode order by b.strCategory; ");
		
		
		
		
		sbSql.append("select a.strType,b.strGroupCode,b.strGroupName,ifnull(d.strCrDr,''),if((c.strVouchNo is null),0,ifnull(sum(d.dblDrAmt),0)),a.strAccountName,a.strAccountCode,if((c.strVouchNo is null),0,ifnull(sum(d.dblCrAmt),0)) "
				+ "from  tblacgroupmaster b,tblsubgroupmaster s,tblacmaster a "
				+" left outer join "+dtlTableName+" d on  a.strAccountCode=d.strAccountCode " 
				+" left outer join "+hdTableName+"  c on c.strVouchNo=d.strVouchNo   and date(c.dteVouchDate) between '" + fromDate + "' and '" + toDate + "' and c.strClientCode='"+clientCode+"' and a.strPropertyCode='"+propCode+"'  "
				+ "where a.strSubGroupCode=s.strSubGroupCode "
				+ " and b.strGroupCode=s.strGroupCode "
				+ "and b.strCategory like'%EXPENSES' "
				+ "and a.strType='GL Code' "
				+ "group by a.strAccountCode  ");
		
		
		List listJV = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
		if (listJV != null && listJV.size() > 0)
		{
			for(int cn=0;cn<listJV.size();cn++)
			{
				Object[] objArr = (Object[]) listJV.get(cn);

				BigDecimal totalAmount = BigDecimal.valueOf(Double.parseDouble(objArr[4].toString())).subtract(BigDecimal.valueOf(Double.parseDouble(objArr[7].toString())));
				
				String accountCode=objArr[6].toString();
				clsProfitLossReportBean objBean = new clsProfitLossReportBean();
				
				if(hmIncomeStatement.containsKey(accountCode))
				{
					objBean=hmIncomeStatement.get(accountCode);
					objBean.setDblAmt(objBean.getDblAmt().add(totalAmount));
				}
				else
				{
				
					objBean.setStrAccountName(objArr[5].toString());
				    objBean.setDblAmt(totalAmount);
				}
			
				hmIncomeStatement.put(accountCode, objBean);
			}
		}
	}
	
	
	@RequestMapping(value={"/rptProfitLossReport1"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
	  private void funPOSProfitLossReport(@ModelAttribute("command") clsProfitLossReportBean objBean, HttpServletResponse resp, HttpServletRequest req)
	  {
	    objGlobal = new clsGlobalFunctions();
	    Connection con = objGlobal.funGetConnection(req);
	    String strCurr = req.getSession().getAttribute("currValue").toString();
	    double currValue = Double.parseDouble(strCurr);
	    try
	    {
	      String clientCode = req.getSession().getAttribute("clientCode").toString();
	      String companyName = req.getSession().getAttribute("companyName").toString();
	      String userCode = req.getSession().getAttribute("usercode").toString();
	      String propertyCode = req.getSession().getAttribute("propertyCode").toString();
	      String financialYear= req.getSession().getAttribute("financialYear").toString();
	      clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
	      if (objSetup == null) {
	        objSetup = new clsPropertySetupModel();
	      }
	      
	      String fromDate = objBean.getDteFromDate();
	      String toDate = objBean.getDteToDate();
	      
	      SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
	      Date dtFrm=sdf.parse(fromDate);
	      Date dtTo=sdf.parse(toDate);
	      int diffInDays = (int) ((dtFrm.getTime() - dtTo.getTime()) / (1000 * 60 * 60 * 24));
	      
	      diffInDays=diffInDays==0?1:diffInDays;
	      diffInDays=diffInDays<0?diffInDays*-1:diffInDays;
	      
	      String fd = fromDate.split("-")[0];
	      String fm = fromDate.split("-")[1];
	      String fy = fromDate.split("-")[2];
	      
	      String td = toDate.split("-")[0];
	      String tm = toDate.split("-")[1];
	      String ty = toDate.split("-")[2];
	      
	      String dteFromDate = fy + "-" + fm + "-" + fd;
	      String dteToDate = ty + "-" + tm + "-" + td;
	      String reportName = servletContext.getRealPath("/WEB-INF/reports/webbooks/rptProfitLossReport.jrxml");
	      String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
	      List<clsProfitLossReportBean> dataListPaymnet = new ArrayList();
	      List<clsProfitLossReportBean> dataListRecipt = new ArrayList();
	      List<clsProfitLossReportBean> dataListExtraExpense = new ArrayList();
	      
	      double conversionRate = 1.0D;
	      

	      String prevProdCode = "";
	      int cnt = -1;
	      
	      List<clsProfitLossReportBean> listProduct = new ArrayList();
	      
	      JSONObject jObjJVData = new JSONObject();
	      jObjJVData.put("dteFrom", dteFromDate);
	      jObjJVData.put("dteTo", dteToDate);
	      jObjJVData.put("strClientCode", clientCode);
	      
	      JSONObject jObj = funPOSTMethodUrlJosnObjectData("http://localhost:8080/prjSanguineWebService/WebBooksIntegration/funGetPOSData", jObjJVData);
	      double dblTotalSale=0,dblTotalPurchase=0;
	      if(jObj!=null){
	    	  dblTotalSale = Double.valueOf(Double.parseDouble(jObj.get("TotalSale").toString()));
		      dblTotalPurchase = Double.valueOf(Double.parseDouble(jObj.get("TotalPurchase").toString()));
		        
	      }
	      clsProfitLossReportBean objPL = new clsProfitLossReportBean();
	      
	      objPL.setDblPurAmt(dblTotalPurchase);
	      objPL.setDblSaleAmt(dblTotalSale);
	      listProduct.add(objPL);
	      
	      Map<String, clsProfitLossReportBean> hmSalesIncStmt = new HashMap();
	      funCalculateIncomeStmt("Expense", "tbljvhd", "tbljvdtl", dteFromDate, dteToDate, clientCode, hmSalesIncStmt, propertyCode);
	      

	      funCalculateIncomeStmt("Expense", "tblreceipthd", "tblreceiptdtl", dteFromDate, dteToDate, clientCode, hmSalesIncStmt, propertyCode);
	      

	      funCalculateIncomeStmt("Expense", "tblpaymenthd", "tblpaymentdtl", dteFromDate, dteToDate, clientCode, hmSalesIncStmt, propertyCode);
	      BigDecimal totalExpense = new BigDecimal(0);
	      for (Map.Entry<String, clsProfitLossReportBean> entry : hmSalesIncStmt.entrySet())
	      {


	        clsProfitLossReportBean objProfitLoss = new clsProfitLossReportBean();
	        objProfitLoss.setStrAccountCode(entry.getKey());
	        objProfitLoss.setStrAccountName(((clsProfitLossReportBean)entry.getValue()).getStrAccountName());
	        objProfitLoss.setDblAmt(((clsProfitLossReportBean)entry.getValue()).getDblAmt());
	        totalExpense = totalExpense.add(((clsProfitLossReportBean)entry.getValue()).getDblAmt());
	        
	    	dataListExtraExpense.add(objProfitLoss);	
	    	
	        if(objProfitLoss.getDblAmt().compareTo(new BigDecimal(0))==0){
	        	BigDecimal BudgetAmt=new BigDecimal(funGetAccountBalanceFromBudget(objProfitLoss.getStrAccountCode(),dteFromDate, dteToDate, clientCode,financialYear,diffInDays));
	        	objProfitLoss.setDblAmt(BudgetAmt);
	        	if(BudgetAmt.compareTo(new BigDecimal(0))!=0){
	        		objProfitLoss.setStrAccountName(objProfitLoss.getStrAccountName()+"       (B)");	
	        	}
	        	
	        }
	        
	      }
	      

	      BigDecimal purAmt = new BigDecimal(0);
	      BigDecimal dblRevenue = new BigDecimal(0);
	      for (clsProfitLossReportBean obj : listProduct)
	      {
	        purAmt = purAmt.add(BigDecimal.valueOf(obj.getDblPurAmt()));
	        dblRevenue = dblRevenue.add(BigDecimal.valueOf(obj.getDblSaleAmt()));
	      }
	      BigDecimal grossProfit = dblRevenue.subtract(purAmt);
	      


	      BigDecimal perCostOfGoodSold = purAmt.divide(dblRevenue, 2, RoundingMode.HALF_UP);
	      perCostOfGoodSold = perCostOfGoodSold.multiply(new BigDecimal(100));
	      
	      BigDecimal perTotalExp = totalExpense.divide(dblRevenue, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
	      BigDecimal perGrossProfit = grossProfit.divide(dblRevenue, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
	      BigDecimal netProfit = grossProfit.subtract(totalExpense);
	      BigDecimal perNetProfit = netProfit.divide(dblRevenue, 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
	      if (perNetProfit.compareTo(BigDecimal.ZERO) < 0)
	      {
	        perNetProfit = perNetProfit.multiply(new BigDecimal(-1));
	      }
	      
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
	      hm.put("fromDate", fromDate);
	      hm.put("toDate", toDate);
	      hm.put("dataListExtraExpense", dataListExtraExpense);
	      hm.put("dataListRecipt", dataListRecipt);
	      hm.put("totalExpense", totalExpense);
	      
	      hm.put("dblGrossProfit", grossProfit);
	      hm.put("dblRevenue", dblRevenue);
	      hm.put("dblCostofGood", purAmt);
	      
	      hm.put("perCostOfGoodSold", perCostOfGoodSold);
	      hm.put("perTotalExp", perTotalExp);
	      hm.put("netProfit", netProfit);
	      hm.put("perNetProfit", perNetProfit);
	      hm.put("perGrossProfit", perGrossProfit);
	      
	      String strPL = "NET PROFIT";
	      if (netProfit.compareTo(BigDecimal.ZERO) < 0)
	      {
	        strPL = "LOSS";
	      }
	      hm.put("strPL", strPL);
	      List list = new ArrayList();
	      list.add(hmSalesIncStmt);
	      
	      JasperDesign jd = JRXmlLoader.load(reportName);
	      JasperReport jr = JasperCompileManager.compileReport(jd);
	      JRBeanCollectionDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(list);
	      JasperPrint jp = JasperFillManager.fillReport(jr, hm, beanCollectionDataSource);
	      List<JasperPrint> jprintlist = new ArrayList();
	      ServletOutputStream servletOutputStream = resp.getOutputStream();
	      jprintlist.add(jp);
	      
	      if (objBean.getStrDocType().trim().equalsIgnoreCase("pdf"))
	      {
	        byte[] bytes = null;
	        bytes = JasperRunManager.runReportToPdf(jr, hm, con);
	        resp.setContentType("application/pdf");
	        resp.setContentLength(bytes.length);
	        servletOutputStream.write(bytes, 0, bytes.length);
	        servletOutputStream.flush();
	        servletOutputStream.close();
	      } else if (objBean.getStrDocType().trim().equalsIgnoreCase("xls")) {
	        JRXlsExporter exporter = new JRXlsExporter();
	        resp.setContentType("application/xlsx");
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jprintlist);
	        exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
	        exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
	        resp.setHeader("Content-Disposition", "inline;filename=rptProfitLossReport_" + dteFromDate + "_To_" + dteToDate + "_" + userCode + ".xls");
	        exporter.exportReport();
	        servletOutputStream.write(byteArrayOutputStream.toByteArray());
	        servletOutputStream.flush();
	        servletOutputStream.close();
	      }
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      try
	      {
	        con.close();
	      }
	      catch (SQLException e1) {
	        e1.printStackTrace();
	      }
	    }
	    finally
	    {
	      try
	      {
	        con.close();
	      }
	      catch (SQLException e) {
	        e.printStackTrace();
	      }
	    }
	  }

	private double funGetAccountBalanceFromBudget(String strAccountCode,String dteFromDate,String  dteToDate,String clientCode,String year, int diffInDays){
		double budgetAmt=0;
		try{
			
			String sql=" select ifnull(a.dblBudgetAmt,1)*"+diffInDays+"/365 from tblbudget a where a.strAccCode='"+strAccountCode+"' "
					+ " and a.strClientCode='"+clientCode+"' and a.strYear='"+year+"'  ";

			List listBudget = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			if (listBudget != null && listBudget.size() > 0)
			{
				budgetAmt=Double.parseDouble(listBudget.get(0).toString());
			}
			
		}catch(Exception w){
			w.printStackTrace();
		}
		return budgetAmt;
	}
	
	
	 public JSONObject funPOSTMethodUrlJosnObjectData(String strUrl, JSONObject objRows) {
		    JSONObject josnObjRet = new JSONObject();
		    try
		    {
		      URL url = new URL(strUrl);
		      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		      conn.setDoOutput(true);
		      conn.setRequestMethod("POST");
		      conn.setRequestProperty("Content-Type", "application/json");
		      OutputStream os = conn.getOutputStream();
		      os.write(objRows.toString().getBytes());
		      os.flush();
		      


		      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		      String output = "";String op = "";
		      
		      while ((output = br.readLine()) != null) {
		        op = op + output;
		      }
		      System.out.println("Result= " + op);
		      conn.disconnect();
		      
		      JSONParser parser = new JSONParser();
		      Object obj = parser.parse(op);
		      josnObjRet = (JSONObject)obj;
		    }
		    catch (Exception e)
		    {
		      e.printStackTrace();
		    } finally {
		      return josnObjRet;
		    }
		  }
		

	
}
