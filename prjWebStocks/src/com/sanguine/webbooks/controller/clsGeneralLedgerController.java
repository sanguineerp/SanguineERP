package com.sanguine.webbooks.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsCurrencyMasterService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webbooks.bean.clsDebtorLedgerBean;

@Controller
public class clsGeneralLedgerController {

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;	

	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@Autowired
	clsCurrencyMasterService objCurrencyMasterService;
	
	@Autowired
	intfBaseService objBaseService;

	@RequestMapping(value = "/frmGeneralLedger", method = RequestMethod.GET)
	public ModelAndView funOpenForm(@ModelAttribute("command") clsDebtorLedgerBean objBean, BindingResult result, Map<String, Object> model, HttpServletRequest req) {
		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		Map<String, String> hmCurrency = objCurrencyMasterService.funCurrencyListToDisplay(clientCode);
		if (hmCurrency.isEmpty()) {
			hmCurrency.put("", "");
		}
		model.put("currencyList", hmCurrency);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmGeneralLedger_1", "command", new clsDebtorLedgerBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmGeneralLedger", "command", new clsDebtorLedgerBean());
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/getGeneralLedger", method = RequestMethod.GET)
	public @ResponseBody List funLoadGeneralLedger(@RequestParam(value = "param1") String param1, @RequestParam(value = "fDate") String fDate, @RequestParam(value = "tDate") String tDate, @RequestParam(value = "currency") String currency, HttpServletRequest req, HttpServletResponse resp) {
		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String propertyCode = req.getSession().getAttribute("propertyCode").toString();
		String startDate = req.getSession().getAttribute("startDate").toString();

		String userCode = req.getSession().getAttribute("usercode").toString();
		String[] spParam1 = param1.split(",");
		String glCode = spParam1[0];

		String fromDate = objGlobal.funGetDate("yyyy-MM-dd", fDate);
		String toDate = objGlobal.funGetDate("yyyy-MM-dd", tDate);

		startDate = startDate.split("/")[2] + "-" + startDate.split("/")[1] + "-" + startDate.split("/")[0];
		double conversionRate=1;
		
		String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("select dblConvToBaseCurr from "+webStockDB+".tblcurrencymaster where strCurrencyCode='"+currency+"' and strClientCode='"+clientCode+"' ");
		try
		{
			List list = objBaseService.funGetList(sbSql,"sql");
			conversionRate=Double.parseDouble(list.get(0).toString());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		objGlobal.funInvokeWebBookGeneralLedger(glCode, startDate, propertyCode, fromDate, toDate, clientCode,conversionRate, userCode, req, resp, "Creditor");
		String hql = "  from clsLedgerSummaryModel a where a.strUserCode='" + userCode + "' and a.strPropertyCode='" + propertyCode + "' and a.strClientCode='" + clientCode + "' order by a.dteVochDate, a.strTransTypeForOrderBy  ";
		List listBillLedger = objGlobalFunctionsService.funGetListModuleWise(hql, "hql");

		return listBillLedger;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/frmExportGeneralLedger", method = RequestMethod.GET)
	private ModelAndView funExportLedger(@RequestParam(value = "param1") String param1, @RequestParam(value = "fDate") String fDate
		, @RequestParam(value = "tDate") String tDate,@RequestParam(value = "currency") String currency,@RequestParam(value="strShowNarration") boolean isShowNarration, HttpServletRequest req, HttpServletResponse resp) {

		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		

		String propertyCode = req.getSession().getAttribute("propertyCode").toString();


		String fromDate = objGlobal.funGetDate("yyyy-MM-dd", fDate);
		String toDate = objGlobal.funGetDate("yyyy-MM-dd", tDate);
		List listLedger = new ArrayList();
		listLedger.add("GeneralLedger_" + fromDate + "to" + toDate + "_" + userCode);
		String[] ExcelHeader = { "Transaction Date", "Transaction Type", "Ref No", "Chq/BillNo", "Bill Date", "Dr", "Cr", "Balance" };
		if(isShowNarration){
			 ExcelHeader = new String[]{ "Transaction Date", "Transaction Type", "Ref No","Narration", "Chq/BillNo", "Bill Date", "Dr", "Cr", "Balance" };		
		}
		
		listLedger.add(ExcelHeader);

		double conversionRate=1;
		String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
		StringBuilder sbSql = new StringBuilder();
		sbSql.append("select dblConvToBaseCurr from "+webStockDB+".tblcurrencymaster where strCurrencyCode='"+currency+"' and strClientCode='"+clientCode+"' ");
		try
		{
			List list = objBaseService.funGetList(sbSql,"sql");
			conversionRate=Double.parseDouble(list.get(0).toString());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		sbSql.setLength(0);
		sbSql.append(" SELECT DATE_FORMAT(DATE(dteVochDate),'%d-%m-%Y'),strTransType,strVoucherNo,strChequeBillNo,DATE_FORMAT(DATE(dteBillDate),'%d-%m-%Y'),dblDebitAmt,dblCreditAmt,dblBalanceAmt,ifnull(strNarration,'') " 
			+ " from tblledgersummary where strUserCode='" + userCode + "' and strPropertyCode='" + propertyCode + "' AND strClientCode='" + clientCode + "'  " + " order by dteVochDate, strTransTypeForOrderBy ");

		
		double debit = 0.0;
		double credit = 0.0;
		
		List listBillLedger = objGlobalFunctionsService.funGetListModuleWise(sbSql.toString(), "sql");
		List objList = new ArrayList();
		double bal = 0.00;
		String totBalAmt="";
		
		DecimalFormat df = objGlobal.funGetDecimatFormat(req);
		String twoDecimalVal="";
		double debitAmt=0.00,creditAmt=0.00;
		for (int i = 0; i < listBillLedger.size(); i++) {
			Object[] obj = (Object[]) listBillLedger.get(i);
			bal = bal + ((Double.parseDouble(obj[5].toString()) - Double.parseDouble(obj[6].toString())) / conversionRate);
			debit=debit+(Double.parseDouble(obj[5].toString())/conversionRate);
			credit=credit+(Double.parseDouble(obj[6].toString())/conversionRate);
			
			List listemp = new ArrayList();
			listemp.add(obj[0]);
			listemp.add(obj[1]);
			listemp.add(obj[2]);
			if(isShowNarration){
				listemp.add(obj[8]);
			}
			listemp.add(obj[3]);
			listemp.add(obj[4].toString());
			
			debitAmt = Double.parseDouble(obj[5].toString())/conversionRate;
			
			if(Double.parseDouble(obj[5].toString())< 0){
				twoDecimalVal="";
				twoDecimalVal =df.format((debitAmt)*-1); 
				listemp.add("("+twoDecimalVal+")");
			}else{
				listemp.add(df.format(debitAmt));
			}
			
			creditAmt = (Double.parseDouble(obj[6].toString())/conversionRate);
			if(Double.parseDouble(obj[6].toString())< 0){
				twoDecimalVal="";
				twoDecimalVal =df.format(creditAmt*-1); 
				listemp.add("("+twoDecimalVal+")");
			}else{
				listemp.add(df.format(creditAmt));
			}
			
			if(bal < 0){
				twoDecimalVal = df.format(bal*-1);
				listemp.add("("+twoDecimalVal+")");
				totBalAmt = "("+twoDecimalVal+")";
				
			}else{
				listemp.add(df.format(bal));
				totBalAmt = df.format(bal);
			}
			
			objList.add(listemp);
		}
		
	// Summary Detail
		
		List listemp = new ArrayList();
		
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("Total");
		listemp.add(df.format(debit));
		listemp.add(df.format(credit));
		listemp.add(totBalAmt);
		objList.add(listemp);
		
		listemp = new ArrayList();
		
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		objList.add(listemp);
		
		listemp = new ArrayList();
		
		listemp.add("Transaction Type");
		listemp.add("Amount");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		objList.add(listemp);
		
        listemp = new ArrayList();
		
		listemp.add("Total Debit");
		twoDecimalVal="";
		if(debit < 0){
			twoDecimalVal = df.format(debit*-1);
			listemp.add("("+twoDecimalVal+")");
			
		}else{
			listemp.add(df.format(debit));
		}
		
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		objList.add(listemp);
		
				
		listemp = new ArrayList();
		
		listemp.add("Total Credit");
		twoDecimalVal="";
		if(credit < 0){
			twoDecimalVal = df.format(credit*-1);
			listemp.add("("+twoDecimalVal+")");
			
		}else{
			listemp.add(df.format(credit));
		}
		
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		objList.add(listemp);
		
			
		listemp = new ArrayList();
		
		listemp.add("Closing Balance");
		twoDecimalVal="";
		if((debit-credit) < 0){
			twoDecimalVal = df.format((debit-credit)*-1);
			listemp.add("("+ twoDecimalVal +")");
			
		}else{
			listemp.add(df.format(debit-credit));
		}
		
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		listemp.add("");
		objList.add(listemp);
		
		listLedger.add(objList);

		return new ModelAndView("excelViewWithReportName", "listWithReportName", listLedger);
	}
}
