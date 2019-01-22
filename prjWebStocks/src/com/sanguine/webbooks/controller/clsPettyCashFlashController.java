package com.sanguine.webbooks.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.hql.ast.SqlASTFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsCurrencyMasterModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webbooks.bean.clsCreditorOutStandingReportBean;
import com.sanguine.webbooks.bean.clsExpenseMasterBean;



@Controller
public class clsPettyCashFlashController {
	

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;


	
	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@Autowired
	clsGlobalFunctionsService objGlobalService;
	@RequestMapping(value = "/frmPettyCashFlash", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPettyCashFlash_1", "command", new clsExpenseMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPettyCashFlash", "command", new clsExpenseMasterBean());
		} else {
			return null;
		}
				
	} 
	
	@RequestMapping(value = "/rptPettyCashFlashDetail", method = RequestMethod.GET)
	private  @ResponseBody List funPettyCashDetailReport(@RequestParam("fromDat") String fromDate ,@RequestParam("toDat") String toDate, HttpServletResponse resp, HttpServletRequest req){
		
		StringBuilder sbSql = new StringBuilder();
		
		
		sbSql.append(" SELECT a.strVouchNo AS Voucer_No,c.strExpCode AS EX_NO,c.stnExpName as Expense_Name ,DATE_FORMAT(a.dteVouchDate,'%d-%m-%Y') AS Voucher_Date ,a.strUserCreated as User_Name,IFNULL(c.strGLCode,'') AS Acount_Code,c.stnExpName AS Name,b.strNarration AS Narration,DATE_FORMAT(a.dteDateCreated,'%d-%m-%Y') as Created_Date,b.dblAmount AS Amount"
				+" FROM tblpettycashhd a, tblpettycashdtl b, tblexpensemaster c,tblacmaster d"
				+" WHERE a.strVouchNo=b.strVouchNo AND b.strExpCode=c.strExpCode AND c.strGLCode = d.strAccountCode AND a.dteVouchDate BETWEEN '"+fromDate +"' AND '"+toDate+ "';");

		List listDtl = objGlobalService.funGetListModuleWise(sbSql.toString(), "sql");
		
		return listDtl;
		
		
	}


	@RequestMapping(value = "/rptPettyCashFlashSummary", method = RequestMethod.GET)
	private  @ResponseBody List funPettyCashSummaryReport(@RequestParam("fromDat") String fromDate ,@RequestParam("toDat") String toDate, HttpServletResponse resp, HttpServletRequest req){
		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String companyName = req.getSession().getAttribute("companyName").toString();
		
		StringBuilder sbSql = new StringBuilder();

		sbSql.append("SELECT a.strVouchNo AS Voucer_NO,DATE_FORMAT(b.dteVouchDate,'%d-%m-%Y') AS DATE,DATE_FORMAT(b.dteDateCreated,'%d-%m-%Y') as Created_Date,b.strUserCreated as User_Name,SUM(a.dblAmount) AS Amount "
				+" FROM tblpettycashdtl a,tblpettycashhd b, tblexpensemaster c "
				+"WHERE a.strVouchNo=b.strVouchNo AND a.strExpCode=c.strExpCode AND b.dteVouchDate AND a.strExpCode=c.strExpCode BETWEEN '"+fromDate +"' AND '"+toDate+ "' group by b.strVouchNo;");
		List listDtl = objGlobalService.funGetListModuleWise(sbSql.toString(), "sql");
		
		return listDtl;
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/frmExportPettyCashSummary", method = RequestMethod.GET)
	private ModelAndView funExportPettyCashSummaryData(String param1, @RequestParam(value = "fDate") String fDate, @RequestParam(value = "tDate") String tDate,HttpServletRequest req, HttpServletResponse res){
		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
 		DecimalFormat df =objGlobal.funGetDecimatFormat(req);
		List listCashSummary = new ArrayList();	
 						
						
					


		listCashSummary.add("Ledger_" + fDate + "to" + tDate + "_" + userCode);
		String[] ExcelHeader = { "Voucher Number", "Voucher Date", "Created Date", "User Name", "Amount" };
		listCashSummary.add(ExcelHeader);

		String sql = (" SELECT a.strVouchNo AS Voucer_NO,DATE_FORMAT(b.dteVouchDate,'%d-%m-%Y') AS DATE,DATE_FORMAT(b.dteDateCreated,'%d-%m-%Y') as Created_Date,b.strUserCreated as User_Name,SUM(a.dblAmount) AS Amount "
				+" FROM tblpettycashdtl a,tblpettycashhd b, tblexpensemaster c "
				+"WHERE a.strVouchNo=b.strVouchNo AND a.strExpCode=c.strExpCode AND b.dteVouchDate AND a.strExpCode=c.strExpCode BETWEEN '"+fDate +"' AND '"+tDate+ "' group by b.strVouchNo;");

	
		
		List listBillLedger = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		List objList = new ArrayList();
		double totalAmount = 0;
		for (int i = 0; i < listBillLedger.size(); i++) {
			Object[] obj = (Object[]) listBillLedger.get(i);
			List listemp = new ArrayList();
			listemp.add(obj[0]);
			listemp.add(obj[1]);
			listemp.add(obj[2]);
			listemp.add(obj[3]);
			listemp.add(obj[4]);

			totalAmount = totalAmount + Double.parseDouble(obj[4].toString());
			
			objList.add(listemp);
			
		}
		
		List listemp = new ArrayList();
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("Total");
		listemp.add(totalAmount);
		objList.add(listemp);
		
		listCashSummary.add(objList);
		
		return new ModelAndView("excelViewWithReportName", "listWithReportName", listCashSummary);
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/frmExportPettyCashDetails", method = RequestMethod.GET)
	private ModelAndView funExportPettyCashDetailsgData(String param1, @RequestParam(value = "fDate") String fDate, @RequestParam(value = "tDate") String tDate,HttpServletRequest req, HttpServletResponse res){

		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
 		DecimalFormat df =objGlobal.funGetDecimatFormat(req);
		List listCashDetail = new ArrayList();	
 						
						

								
		listCashDetail.add("Ledger_" + fDate + "to" + tDate + "_" + userCode);
		String[] ExcelHeader = { "Voucher Number", "Expense Number"," Expense Name ", "Voucher Date"," 	User Name " ,"Account Code"," Account Name " ,"Transaction Narration"," Created Date " ,"Amount" };
		listCashDetail.add(ExcelHeader);

		String sql = (" SELECT a.strVouchNo AS Voucer_No,c.strExpCode AS EX_NO,c.stnExpName as Expense_Name ,DATE_FORMAT(a.dteVouchDate,'%d-%m-%Y') AS Voucher_Date ,a.strUserCreated as User_Name,IFNULL(c.strGLCode,'') AS Acount_Code,c.stnExpName AS Name,b.strNarration AS Narration,DATE_FORMAT(a.dteDateCreated,'%d-%m-%Y') as Created_Date,b.dblAmount AS Amount"
				+" FROM tblpettycashhd a, tblpettycashdtl b, tblexpensemaster c,tblacmaster d"
				+" WHERE a.strVouchNo=b.strVouchNo AND b.strExpCode=c.strExpCode AND c.strGLCode = d.strAccountCode AND a.dteVouchDate BETWEEN '"+fDate +"' AND '"+tDate+ "';");

	
		double totalAmount = 0;

		List listBillLedger = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		List objList = new ArrayList();
		for (int i = 0; i < listBillLedger.size(); i++) {
			Object[] obj = (Object[]) listBillLedger.get(i);
			List listemp = new ArrayList();
			listemp.add(obj[0]);
			listemp.add(obj[1]);
			listemp.add(obj[2]);
			listemp.add(obj[3]);
			listemp.add(obj[4]);
			listemp.add(obj[5]);
			listemp.add(obj[6]);
			listemp.add(obj[7]);
			listemp.add(obj[8]);
			listemp.add(obj[9]);
			totalAmount = totalAmount + Double.parseDouble(obj[9].toString());

			objList.add(listemp);
			
		}
		
		List listemp = new ArrayList();
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("Total");
		listemp.add(totalAmount);
		objList.add(listemp);
		
		listCashDetail.add(objList);
	
		return new ModelAndView("excelViewWithReportName", "listWithReportName", listCashDetail);
		
		
	}

}
