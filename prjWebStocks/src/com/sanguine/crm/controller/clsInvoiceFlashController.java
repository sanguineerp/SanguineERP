package com.sanguine.crm.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.math.BigDecimal;
import com.mysql.jdbc.Connection;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.crm.bean.clsInvoiceBean;
import com.sanguine.crm.service.clsCRMSettlementMasterService;
import com.sanguine.model.clsCompanyMasterModel;
import com.sanguine.model.clsCurrencyMasterModel;
import com.sanguine.model.clsLocationMasterModel;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsCurrencyMasterService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsLocationMasterService;
import com.sanguine.service.clsSetupMasterService;

@Controller
public class clsInvoiceFlashController {

	@Autowired
	private clsGlobalFunctionsService objGlobalService;
	@Autowired
	private clsLocationMasterService objLocationMasterService;

	@Autowired
	private clsCRMSettlementMasterService objSettlementService;
	
	@Autowired
	private clsCurrencyMasterService objCurrencyMasterService;

	@Autowired
	private clsSetupMasterService objSetupMasterService;
	
	@Autowired
	private clsGlobalFunctions objGlobalFunctions;
	
	@Autowired
	private clsGlobalFunctions objGlobalfunction;
	
	@Autowired
	private ServletContext servletContext;
	
	String baseCurrencyCode="";
	Map<String,String> currencyList=new TreeMap<String, String>();
	@RequestMapping(value = "/frmInvoiceFlash", method = RequestMethod.GET)
	public ModelAndView funInvoice(@ModelAttribute("command") clsInvoiceBean objBean, BindingResult result, HttpServletRequest req, Map<String, Object> model) {
		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		return funGetModelAndView(req);
	}

	@RequestMapping(value = "/loadInvoiceFlash", method = RequestMethod.GET)
	public @ResponseBody List funLoadInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String dbWebBook=request.getSession().getAttribute("WebBooksDB").toString();
		clsCompanyMasterModel objCompModel = objSetupMasterService.funGetObject(strClientCode);
		
		DecimalFormat df = objGlobalFunctions.funGetDecimatFormat(request);
		String currencyName="";
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblSubTotalValue = new BigDecimal(0);
		BigDecimal dblTaxTotalValue = new BigDecimal(0);
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, strClientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, strClientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		
		List<clsInvoiceBean> listofInvFlash = new ArrayList<clsInvoiceBean>();
		List<Object> listofInvoiveTotal = new ArrayList<>();
		StringBuilder  sqlInvoiceFlash = new StringBuilder(); 
		sqlInvoiceFlash.setLength(0);

	
		if (objCompModel.getStrWebBookModule().equals("Yes")) {
			sqlInvoiceFlash.append("select a.strInvCode ,DATE_FORMAT(a.dteInvDate,'%d-%m-%Y'),b.strPName,a.strAgainst,a.strVehNo,a.dblSubTotalAmt/" + currValue + ",a.dblTaxAmt/" + currValue + ""
					+ ",a.dblGrandTotal/" + currValue + ",a.strExciseable,c.strSettlementDesc,ifnull(d.strVouchNo,''),ifnull(a.strNarration,'') "
					+ " FROM tblpartymaster b,tblsettlementmaster c,tblinvoicehd a left outer join "+dbWebBook+".tbljvhd d on a.strInvCode=d.strSourceDocNo"
					+ " where   date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' " + " and a.strLocCode='" + locCode +"' "
					+ " and a.strCustCode=b.strPCode and  a.strClientCode='" + strClientCode + "'");
			if (!settlementcode.equals("All")) {
				sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
			}
			if (!custCode.equals("All")) {
				sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
			}
			if(!currencyCode.equalsIgnoreCase("All"))
			{
				sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
			}
			

			sqlInvoiceFlash.append("and a.strSettlementCode=c.strSettlementCode  and a.dblSubTotalAmt>0 "
				+ "  order by a.strInvCode ");
		}
		else
		{
			sqlInvoiceFlash.append("select a.strInvCode ,DATE_FORMAT(a.dteInvDate,'%d-%m-%Y'),b.strPName,a.strAgainst,a.strVehNo,a.dblSubTotalAmt/" + currValue + ",a.dblTaxAmt/" + currValue + ""
					+ ",(a.dblSubTotalAmt/" + currValue + "+ a.dblTaxAmt/" + currValue + "),a.strExciseable,c.strSettlementDesc,'',ifnull(a.strNarration,'') "
					+ " FROM tblpartymaster b,tblsettlementmaster c,tblinvoicehd a "
					+ " where   date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' " + " and a.strLocCode='" + locCode +"' "
					+ " and a.strCustCode=b.strPCode and  a.strClientCode='" + strClientCode + "'");
			if (!settlementcode.equals("All")) {
				sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
			}
			if (!custCode.equals("All")) {
				sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
			}
			if(!currencyCode.equalsIgnoreCase("All"))
			{
				sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
			}
			

			sqlInvoiceFlash.append("and a.strSettlementCode=c.strSettlementCode and a.strNarration not like '%Entry deleted%'"
				+ "  order by a.strInvCode ");
		}
		
		
		
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");

		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				clsInvoiceBean objBean = new clsInvoiceBean();
				objBean.setStrInvCode(objInvoice[0].toString());
				objBean.setDteInvDate(objInvoice[1].toString());
				objBean.setStrCustName(objInvoice[2].toString());
				objBean.setStrAgainst(objInvoice[3].toString());
				objBean.setStrVehNo(objInvoice[4].toString());
				objBean.setDblSubTotalAmt(Double.parseDouble(objInvoice[5].toString()));
				objBean.setDblTaxAmt(Double.parseDouble(objInvoice[6].toString()));
				objBean.setDblTotalAmt(Double.parseDouble(objInvoice[7].toString()));
				objBean.setStrExciseable(objInvoice[8].toString());
				objBean.setStrSerialNo(objInvoice[10].toString());
				if(objInvoice[10].toString().isEmpty())
				{
					if(!objInvoice[11].toString().isEmpty())
					{
						if(objInvoice[11].toString().contains("/"))
						{
							objBean.setStrSerialNo(objInvoice[11].toString().split("/")[1]);
						}
					}
				}
				objBean.setStrCurrency(currencyName);
				objBean.setStrNarration(objInvoice[11].toString());
				
				listofInvFlash.add(objBean);
				objBean.setStrSettleDesc(objInvoice[9].toString());
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[7].toString()));
				dblTotalValue = dblTotalValue.add(value);
				value = new BigDecimal(Double.parseDouble(objInvoice[5].toString()));
				dblSubTotalValue = dblSubTotalValue.add(value);

				value = new BigDecimal(Double.parseDouble(objInvoice[6].toString()));
				dblTaxTotalValue = dblTaxTotalValue.add(value);
			}
		}
		listofInvoiveTotal.add(listofInvFlash);
		listofInvoiveTotal.add(df.format(dblTotalValue));
		listofInvoiveTotal.add(df.format(dblSubTotalValue));
		listofInvoiveTotal.add(df.format(dblTaxTotalValue));
		//System.out.print(dblTaxTotalValue + "ttoalsubtotal" + dblTaxTotalValue);
		return listofInvoiveTotal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/exportInvoiceExcel", method = RequestMethod.GET)
	public ModelAndView funExportInvoiceFlash(@RequestParam("custcode") String strCustCode, HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List listInvoice = new ArrayList();

		clsLocationMasterModel objLocCode = objLocationMasterService.funGetObject(locCode, clientCode);

		String repeortfileName = "InvoiceFlashReport" + "_" + objLocCode.getStrLocName() + "_" + fromDate + "_To_" + toDate + "_" + userCode;
		repeortfileName = repeortfileName.replaceAll(" ", "");
		listInvoice.add(repeortfileName);

		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblSubTotalValue = new BigDecimal(0);
		BigDecimal dblTaxTotalValue = new BigDecimal(0);
		String[] ExcelHeader = { "Invoice Code", "Date", "Customer Name", "Against", "Vehicle No", "Excisable", "Sub Total", "Tax Amount", "Total Amount" };
		listInvoice.add(ExcelHeader);

		List listofInvFlash = new ArrayList();

		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		StringBuilder  sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("select a.strInvCode ,a.dteInvDate,b.strPName,a.strAgainst,a.strVehNo,a.dblSubTotalAmt,a.dblTaxAmt,a.dblGrandTotal,a.strExciseable from tblinvoicehd a ,tblpartymaster b  where   date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' " + " and a.strLocCode='" + locCode + "' and a.strCustCode=b.strPCode and  a.strClientCode='" + strClientCode + "'");
		if (!strCustCode.equals("All")) {
			sqlInvoiceFlash.append( " and a.strCustCode='" + strCustCode + "' ");
		}

		DecimalFormat df = new DecimalFormat("#.##");
		double floatingPoint = 0.0;
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(objInvoice[2].toString());
				dataList.add(objInvoice[3].toString());
				dataList.add(objInvoice[4].toString());
				dataList.add(objInvoice[8].toString());
				floatingPoint = Double.parseDouble(objInvoice[5].toString());
				floatingPoint = Double.parseDouble(df.format(floatingPoint).toString());
				dataList.add(floatingPoint);

				floatingPoint = Double.parseDouble(objInvoice[6].toString());
				floatingPoint = Double.parseDouble(df.format(floatingPoint).toString());
				dataList.add(floatingPoint);

				floatingPoint = Double.parseDouble(objInvoice[7].toString());
				floatingPoint = Double.parseDouble(df.format(floatingPoint).toString());
				dataList.add(floatingPoint);

				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[7].toString()));
				dblTotalValue = dblTotalValue.add(value);
				value = new BigDecimal(Double.parseDouble(objInvoice[5].toString()));
				dblSubTotalValue = dblSubTotalValue.add(value);

				value = new BigDecimal(Double.parseDouble(objInvoice[6].toString()));
				dblTaxTotalValue = dblTaxTotalValue.add(value);
				listofInvFlash.add(dataList);

				if (i == listOfInvoice.size() - 1) {
					dataList = new ArrayList<>();
					dataList.add("");
					dataList.add("");
					dataList.add("");
					dataList.add("");
					dataList.add("");
					dataList.add("Total");
					String a = df.format(dblSubTotalValue);
					dataList.add(df.format(dblSubTotalValue));
					a = df.format(dblTaxTotalValue);
					dataList.add(df.format(dblTaxTotalValue));
					dataList.add(df.format(dblTotalValue));
					listofInvFlash.add(dataList);
				}
			}
		}
		listInvoice.add(listofInvFlash);
		// return new ModelAndView("excelView", "stocklist", listInvoice);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", listInvoice);
	}

	private ModelAndView funGetModelAndView(HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();

		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		ModelAndView objModelView = null;
		if ("2".equalsIgnoreCase(urlHits)) {
			objModelView = new ModelAndView("frmInvoiceFlash_1");
		} else if ("1".equalsIgnoreCase(urlHits)) {
			objModelView = new ModelAndView("frmInvoiceFlash");
		}
		Map<String, String> mapProperty = objGlobalService.funGetPropertyList(clientCode);
		if (mapProperty.isEmpty()) {
			mapProperty.put("", "");
		}
		String propertyCode = req.getSession().getAttribute("propertyCode").toString();
		String locationCode = req.getSession().getAttribute("locationCode").toString();
		Map<String, String> settlementList = objSettlementService.funGetSettlementComboBox(clientCode);
		settlementList.put("All", "All");
		objModelView.addObject("settlementList", settlementList);
		
		currencyList.put("All", "All");
		List<clsCurrencyMasterModel> listCurrency = objCurrencyMasterService.funGetAllCurrencyDataModel(clientCode);
		if (listCurrency != null) 
		{
			for(int cnt=0;cnt<listCurrency.size();cnt++)
			{
				clsCurrencyMasterModel objModel=listCurrency.get(cnt);
				currencyList.put(objModel.getStrCurrencyCode(),objModel.getStrCurrencyName());
			}
		
		}
		objModelView.addObject("currencyList", currencyList);
		
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
		baseCurrencyCode= objSetup.getStrCurrencyCode();
		
		objModelView.addObject("listProperty", mapProperty);
		objModelView.addObject("LoggedInProp", propertyCode);
		objModelView.addObject("LoggedInLoc", locationCode);

		return objModelView;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadTenderWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funTenderWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String currencyName="";
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
	
		List listofInvoiveTotal = new ArrayList();
		

		BigDecimal dblTotalValue = new BigDecimal(0);

		List listofInvFlash = new ArrayList();

		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("select c.strSettlementCode,ifnull(c.strSettlementDesc,''),ifnull(c.strSettlementType,''),sum(a.dblGrandTotal) "
				+ " from tblinvoicehd a,tblsettlementmaster c,tblpartymaster d  "
				+ " where  a.strSettlementCode=c.strSettlementCode and  a.strCustCode=d.strPCode "
				+ "  and a.strLocCode='" + locCode + "' and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  a.strClientCode='" + strClientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
		}
		
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		

		sqlInvoiceFlash.append("group by a.strSettlementCode ");

		DecimalFormat df = new DecimalFormat("#.##");
		double floatingPoint = 0.0;
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(objInvoice[2].toString());

				floatingPoint = Double.parseDouble(objInvoice[3].toString());
				floatingPoint = Double.parseDouble(df.format(floatingPoint).toString());
				dataList.add(floatingPoint);
				dataList.add(currencyName);
				listofInvFlash.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);

			}
		}

		listofInvoiveTotal.add(listofInvFlash);
		listofInvoiveTotal.add(dblTotalValue);
		return listofInvoiveTotal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadOpertorWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funOperatorWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String currencyName="";
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		
		List listofInvoiveTotal = new ArrayList();
		BigDecimal dblTotalValue = new BigDecimal(0);
		List listofInvFlash = new ArrayList();

		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("SELECT b.strUserCode,b.strUserName,  sum(a.dblGrandTotal)/" + currValue + ",sum(a.dblDiscountAmt)/" + currValue + ",ifnull(c.strSettlementDesc,'')  from tblinvoicehd a,tbluserhd b,tblsettlementmaster c " + " WHERE a.strUserCreated=b.strUserCode and a.strSettlementCode=c.strSettlementCode " + " and a.strLocCode='" + locCode + "' and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate
				+ "' and  a.strClientCode='" + strClientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strSettlementCode='" + settlementcode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append(" and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append("group by a.strUserCreated,c.strSettlementCode order by a.strUserCreated ");

		DecimalFormat df = new DecimalFormat("#.##");
		double floatingPoint = 0.0;
		String strUserCode = "";
		double userSalesTtl = 0.0;
		Map<String, Double> hmUserSalesDtl = new HashMap<String, Double>();
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				floatingPoint = Double.parseDouble(objInvoice[2].toString());
				floatingPoint = Double.parseDouble(df.format(floatingPoint).toString());
				dataList.add(floatingPoint);
				dataList.add(objInvoice[3].toString());
				dataList.add(objInvoice[4].toString());

				if (!(objInvoice[0].toString().equals(strUserCode))) {
					// DataList.add("");
					if (i == 0) {
						hmUserSalesDtl.put(objInvoice[0].toString(), Double.parseDouble(objInvoice[2].toString()));
					} else {
						hmUserSalesDtl.put(strUserCode, userSalesTtl);
					}

					userSalesTtl = 0.0;
				} else {
					// DataList.add(userSalesTtl);
				}
				userSalesTtl += Double.parseDouble(objInvoice[2].toString());
				strUserCode = objInvoice[0].toString();
				dataList.add(currencyName);
				listofInvFlash.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalValue = dblTotalValue.add(value);

			}
			hmUserSalesDtl.put(strUserCode, userSalesTtl);
		}

		listofInvoiveTotal.add(listofInvFlash);
		listofInvoiveTotal.add(dblTotalValue);
		listofInvoiveTotal.add(hmUserSalesDtl);
		return listofInvoiveTotal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadCustomerWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funCustomerWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String currencyName="";
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		List listInvoice = new ArrayList();
		List listofInvoiveTotal = new ArrayList();
		BigDecimal dblTotalValue = new BigDecimal(0);
	

		List listofInvFlash = new ArrayList();

		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("select b.strCustCode,a.strPName,a.strPType,count(b.strInvCode),sum(b.dblGrandTotal)/" + currValue + " " + " from tblpartymaster a,tblinvoicehd b where b.strCustCode=a.strPCode  and b.strLocCode='" + locCode + "' " + " and date(b.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  b.strClientCode='" + strClientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  b.strSettlementCode='" + settlementcode + "' ");
		}
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  b.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append( " and  b.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append("group by b.strCustCode  order by sum(b.dblGrandTotal) desc ");

		DecimalFormat df = new DecimalFormat("#.##");

		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(objInvoice[2].toString());
				dataList.add(objInvoice[3].toString());
				dataList.add(objInvoice[4].toString());
				dataList.add(currencyName);

				listofInvFlash.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[4].toString()));
				dblTotalValue = dblTotalValue.add(value);

			}

		}

		listofInvoiveTotal.add(listofInvFlash);
		listofInvoiveTotal.add(dblTotalValue);

		return listofInvoiveTotal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadSKUWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funProductWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String currencyName="";
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		List listofInvoiveTotal = new ArrayList();
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTotalQty = new BigDecimal(0);
		List listofInvFlash = new ArrayList();

		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append( "SELECT  b.strProdCode,c.strProdName,sum(b.dblQty)/" + currValue + ", SUM(b.dblQty*b.dblPrice)/" + currValue + " FROM tblinvoicedtl b, tblproductmaster c,tblinvoicehd a,tblpartymaster d " + " WHERE a.strInvCode=b.strInvCode  and b.strProdCode=c.strProdCode and a.strCustCode=d.strPCode and a.strLocCode='" + locCode + "' " + " and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  a.strClientCode='" + strClientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strSettlementCode='" + settlementcode + "' ");
		}
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append("group by b.strProdCode ");

		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(objInvoice[2].toString());
				dataList.add(objInvoice[3].toString());
				dataList.add(currencyName);

				listofInvFlash.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);
				BigDecimal qty = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalQty = dblTotalQty.add(qty);
				
				
			}

		}

		listofInvoiveTotal.add(listofInvFlash);
		listofInvoiveTotal.add(dblTotalValue);
		listofInvoiveTotal.add(dblTotalQty);

		return listofInvoiveTotal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadCategoryWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funCategoryWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String withZeroAmt = request.getParameter("withZeroAmt").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String currencyName="";
		List listofInvoiveTotal = new ArrayList();
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTotalDis = new BigDecimal(0);
		BigDecimal dblTotalQty = new BigDecimal(0);
		List listofInvFlash = new ArrayList();
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}

		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("select b.strSGCode,c.strSGName,sum(d.dblQty)/" + currValue + ",sum(d.dblQty*d.dblUnitPrice)/" + currValue + ",e.strGName,sum(d.dblProdDiscAmount)/" + currValue + "  from tblinvoicehd a,tblproductmaster b,tblsubgroupmaster c,tblinvoicedtl d,tblgroupmaster e,tblpartymaster f " + " where a.strInvCode=d.strInvCode  and d.strProdCode=b.strProdCode and b.strSGCode=c.strSGCode and c.strGCode=e.strGCode and a.strCustCode=f.strPCode and a.strLocCode='" + locCode + "' " + " and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate
				+ "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strSettlementCode='" + settlementcode + "' ");
		}
		
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append( "  group by  c.strSGCode ");

		
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[4].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(objInvoice[2].toString());
				if(withZeroAmt.equals("Yes"))
				{
					double amt=Double.valueOf(objInvoice[3].toString())-Double.valueOf(objInvoice[5].toString());
					dataList.add(String.valueOf(amt));
				}
				else
				{
					dataList.add(objInvoice[3].toString());
				}
				
				dataList.add(objInvoice[5].toString());
				dataList.add(currencyName);
				listofInvFlash.add(dataList);
				BigDecimal disValue = new BigDecimal(Double.parseDouble(objInvoice[5].toString()));
				dblTotalDis= dblTotalDis.add(disValue);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);
				if(withZeroAmt.equals("Yes"))
				{
					dblTotalValue=dblTotalValue.subtract(dblTotalDis);
				}
				BigDecimal qtyTot = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalQty= dblTotalQty.add(qtyTot);

			}

		}

		listofInvoiveTotal.add(listofInvFlash);
		listofInvoiveTotal.add(dblTotalValue);
		listofInvoiveTotal.add(dblTotalDis);
		listofInvoiveTotal.add(dblTotalQty);
		return listofInvoiveTotal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadManufactureWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funManufactureWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String currencyName="";
		List listofInvoiveTotal = new ArrayList();
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTotalQty = new BigDecimal(0);

		List listofInvFlash = new ArrayList();
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}

		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("  select b.strManufacturerCode,c.strManufacturerName,sum(d.dblQty)/" + currValue + ",sum(d.dblQty*d.dblUnitPrice)/" + currValue + " from tblinvoicehd a,tblproductmaster b,tblmanufacturemaster c,tblinvoicedtl d,tblpartymaster e " + " where a.strInvCode=d.strInvCode  and d.strProdCode=b.strProdCode and b.strManufacturerCode=c.strManufacturerCode  and a.strCustCode=e.strPCode  and a.strLocCode='" + locCode + "' "
				+ " and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
		}
		
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append( " group by  c.strManufacturerCode ");

		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(objInvoice[2].toString());
				dataList.add(objInvoice[3].toString());
				dataList.add(currencyName);
				listofInvFlash.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);
				BigDecimal Qty = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalQty = dblTotalQty.add(Qty);
				

			}

		}

		listofInvoiveTotal.add(listofInvFlash);
		listofInvoiveTotal.add(dblTotalValue);
		listofInvoiveTotal.add(dblTotalQty);

		return listofInvoiveTotal;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadDepartmentWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funDepartmentWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String currencyName="";
		List listofInvoiveTotal = new ArrayList();
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTotalQty= new BigDecimal(0);

		List listofInvFlash = new ArrayList();
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}

		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append(" select b.strLocCode,b.strLocName ,sum(d.dblQty)/" + currValue + ",sum(d.dblQty*d.dblUnitPrice)/" + currValue + " from tblinvoicehd a,tbllocationmaster b,tblinvoicedtl d,tblpartymaster c " + "   where a.strInvCode=d.strInvCode  and a.strLocCode=b.strLocCode  and a.strCustCode=c.strPCode  " + " and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
		}
		if (!locCode.equals("All")) {
			sqlInvoiceFlash.append( "and a.strLocCode='" + locCode + "' ");
		}
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append(" and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append(" group by  a.strLocCode ");

		DecimalFormat df = new DecimalFormat("#.##");

		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(objInvoice[2].toString());
				dataList.add(objInvoice[3].toString());
				dataList.add(currencyName);
				listofInvFlash.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);
				BigDecimal totalQty = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalQty = dblTotalQty.add(totalQty);
				

			}

		}

		listofInvoiveTotal.add(listofInvFlash);
		listofInvoiveTotal.add(dblTotalQty);
		listofInvoiveTotal.add(dblTotalValue);

		return listofInvoiveTotal;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadMonthWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funMonthWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {
		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String currencyName="";
		List listofInvoiveTotal = new ArrayList();
		BigDecimal dblTotalValue = new BigDecimal(0);
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}

		List listofInvFlash = new ArrayList();

		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("select c.strSettlementCode,ifnull(c.strSettlementDesc,''),ifnull(c.strSettlementType,''),sum(a.dblGrandTotal)/" + currValue + ",MONTHNAME(DATE(a.dteInvDate)), YEAR(DATE(a.dteInvDate))"
				+ " from tblinvoicehd a,tblsettlementmaster c,tblpartymaster d " + " where  a.strSettlementCode=c.strSettlementCode and a.strCustCode=d.strPCode " + " and a.strLocCode='" + locCode + "' and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
		}
		
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append(" and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append("group by a.strSettlementCode,YEAR(DATE(a.dteInvDate)),MONTHNAME(DATE(a.dteInvDate))"
				        + "  order by YEAR(DATE(a.dteInvDate)) desc,MONTHNAME(DATE(a.dteInvDate)) desc");

		DecimalFormat df = new DecimalFormat("#.##");
		double floatingPoint = 0.0;
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(objInvoice[2].toString());

				floatingPoint = Double.parseDouble(objInvoice[3].toString());
				floatingPoint = Double.parseDouble(df.format(floatingPoint).toString());
				dataList.add(floatingPoint);
				dataList.add(objInvoice[4].toString());
				dataList.add(objInvoice[5].toString());
				dataList.add(currencyName);
				listofInvFlash.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);

			}
		}

		listofInvoiveTotal.add(listofInvFlash);
		listofInvoiveTotal.add(dblTotalValue);
		return listofInvoiveTotal;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/loadRegionWiseDtl", method = RequestMethod.GET)
	public @ResponseBody List funRegionWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String currencyName="";
		List listofInvoiveTotal = new ArrayList();
		BigDecimal dblTotalValue = new BigDecimal(0);
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		
		List listofInvFlash = new ArrayList();

		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("SELECT c.strRegionCode,c.strRegionDesc, SUM(b.dblGrandTotal)/" + currValue + ""
				+ " FROM tblpartymaster a,tblinvoicehd b,tblregionmaster c"
				+ " WHERE b.strCustCode=a.strPCode AND a.strRegion=c.strRegionCode  "
				+ " and b.strLocCode='" + locCode + "' " + " "
				+ " and date(b.dteInvDate) between '" + fromDate + "' and '" + toDate + "' "
				+ " and  b.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  b.strSettlementCode='" + settlementcode + "' ");
		}
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append(" and  b.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append(" and  b.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append("group by a.strRegion  order by sum(b.dblGrandTotal) desc ");
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(objInvoice[2].toString());
				dataList.add(currencyName);
				listofInvFlash.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalValue = dblTotalValue.add(value);

			}

		}

		listofInvoiveTotal.add(listofInvFlash);
		listofInvoiveTotal.add(dblTotalValue);

		return listofInvoiveTotal;
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportBillWiseInvoiceFlash", method = RequestMethod.GET)
	private ModelAndView funExportBillWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		// Totals at Bottom
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");	
		totalsList.add("");
		totalsList.add("");	
		totalsList.add("");	
		totalsList.add("");
		totalsList.add("");
		
		
		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String dbWebBook=request.getSession().getAttribute("WebBooksDB").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String currencyName="";
		clsCompanyMasterModel objCompModel = objSetupMasterService.funGetObject(clientCode);

		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{   
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblSubTotalValue = new BigDecimal(0);
		BigDecimal dblTaxTotalValue = new BigDecimal(0);
		
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		headerList.add("Invoice Code");
		headerList.add("Date");
		headerList.add("JV No");
		headerList.add("Customer Name");
		headerList.add("Settement");
		headerList.add("Against");
		headerList.add("Vehicle No");
		headerList.add("Currency");
		headerList.add("SubTotal");
		//headerList.add("Tax Amount");
		Map<String,String> mapAllTaxes=new LinkedHashMap<>();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("select a.strTaxCode,a.strTaxDesc from tbltaxhd a order by a.strTaxCode ");
		List listOfTax = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (listOfTax !=null && !listOfTax.isEmpty()) {
		 for (int i = 0; i < listOfTax.size(); i++) {
				Object[] objTax = (Object[]) listOfTax.get(i);
				headerList.add(objTax[1].toString());
				mapAllTaxes.put(objTax[0].toString(),objTax[1].toString());
			
				
				
		 }
		}
		headerList.add("Grand Total");
		headerList.add("Remark");
        
		Map<String,Double> mapTotalTaxAmt=new TreeMap<>();
		/*sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("select a.strInvCode ,DATE_FORMAT(a.dteInvDate,'%d-%m-%Y'),b.strPName,a.strAgainst,a.strVehNo,a.dblSubTotalAmt/" + currValue + ",a.dblTaxAmt/" + currValue + ""
				+ ",a.dblGrandTotal/" + currValue + ",a.strExciseable,c.strSettlementDesc,ifnull(d.strVouchNo,''),ifnull(a.strNarration,'')  "
				+ " FROM tblpartymaster b,tblsettlementmaster c,tblinvoicehd a left outer join "+dbWebBook+".tbljvhd d on a.strInvCode=d.strSourceDocNo"
				+ " where   date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' " + " and a.strLocCode='" + locCode +"' "
				+ " and a.strCustCode=b.strPCode and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
		}
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		
		sqlInvoiceFlash.append( "and a.strSettlementCode=c.strSettlementCode "
		 + " order by a.strInvCode");
		*/
		sqlInvoiceFlash.setLength(0);
		if (objCompModel.getStrWebBookModule().equals("Yes")) {
			sqlInvoiceFlash.append("select a.strInvCode ,DATE_FORMAT(a.dteInvDate,'%d-%m-%Y'),b.strPName,a.strAgainst,a.strVehNo,a.dblSubTotalAmt/" + currValue + ",a.dblTaxAmt/" + currValue + ""
					+ ",a.dblGrandTotal/" + currValue + ",a.strExciseable,c.strSettlementDesc,ifnull(d.strVouchNo,''),ifnull(a.strNarration,'') "
					+ " FROM tblpartymaster b,tblsettlementmaster c,tblinvoicehd a left outer join "+dbWebBook+".tbljvhd d on a.strInvCode=d.strSourceDocNo"
					+ " where   date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' " + " and a.strLocCode='" + locCode +"' "
					+ " and a.strCustCode=b.strPCode and  a.strClientCode='" + clientCode + "'");
			if (!settlementcode.equals("All")) {
				sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
			}
			if (!custCode.equals("All")) {
				sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
			}
			if(!currencyCode.equalsIgnoreCase("All"))
			{
				sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
			}
			

			sqlInvoiceFlash.append("and a.strSettlementCode=c.strSettlementCode "
				+ "  order by a.strInvCode ");
		}
		else
		{
			sqlInvoiceFlash.append("select a.strInvCode ,DATE_FORMAT(a.dteInvDate,'%d-%m-%Y'),b.strPName,a.strAgainst,a.strVehNo,a.dblSubTotalAmt/" + currValue + ",a.dblTaxAmt/" + currValue + ""
					+ ",(a.dblSubTotalAmt/" + currValue + "+ a.dblTaxAmt/" + currValue + "),a.strExciseable,c.strSettlementDesc,'',ifnull(a.strNarration,'') "
					+ " FROM tblpartymaster b,tblsettlementmaster c,tblinvoicehd a "
					+ " where   date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' " + " and a.strLocCode='" + locCode +"' "
					+ " and a.strCustCode=b.strPCode and  a.strClientCode='" + clientCode + "'");
			if (!settlementcode.equals("All")) {
				sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
			}
			if (!custCode.equals("All")) {
				sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
			}
			if(!currencyCode.equalsIgnoreCase("All"))
			{
				sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
			}
			

			sqlInvoiceFlash.append("and a.strSettlementCode=c.strSettlementCode  and a.dblSubTotalAmt>0 "
				+ "  order by a.strInvCode ");
		}
		
		DecimalFormat df = new DecimalFormat("#.##");
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (listOfInvoice !=null && !listOfInvoice.isEmpty()) {
		 for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				String jvNo="";
				jvNo=objInvoice[10].toString();
				if(objInvoice[10].toString().isEmpty())
				{
					if(!objInvoice[11].toString().isEmpty())
					{
						if(objInvoice[11].toString().contains("/"))
						{
							jvNo=objInvoice[11].toString().split("/")[1];
						}
					}
				}
				dataList.add(jvNo);
				dataList.add(objInvoice[2].toString());
				dataList.add(objInvoice[9].toString());
				dataList.add(objInvoice[3].toString());
				dataList.add(objInvoice[4].toString());
				dataList.add(currencyName);
				dataList.add(df.format(Double.parseDouble(objInvoice[5].toString())));
				
				sqlInvoiceFlash.setLength(0);
				sqlInvoiceFlash.append("select a.strTaxCode,a.strTaxDesc,a.dblTaxAmt from tblinvtaxdtl a where a.strInvCode='"+objInvoice[0].toString()+"' order by a.strTaxCode;");
				List listTax = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
				Map<String,String> mapTaxAmt=new LinkedHashMap<>();
				if (listTax !=null && !listTax.isEmpty()) {
				 for (int j = 0; j < listTax.size(); j++) {
						Object[] objTaxAmt = (Object[]) listTax.get(j);
						mapTaxAmt.put(objTaxAmt[0].toString(),objTaxAmt[2].toString());
						
				 }
				}
						
			     for(Map.Entry map:mapAllTaxes.entrySet())
				 {
			    	 
			    	 if(mapTaxAmt.containsKey(map.getKey()))
			    	 {
			    		 dataList.add(df.format(Double.parseDouble(mapTaxAmt.get(map.getKey()).toString())));
			    		 if(mapTotalTaxAmt.containsKey(map.getKey()))
			    		 {
			    			 double totalTaxAmt=mapTotalTaxAmt.get(map.getKey().toString());
			    			 mapTotalTaxAmt.put(map.getKey().toString(),(totalTaxAmt+Double.parseDouble(df.format((Double.parseDouble(mapTaxAmt.get(map.getKey().toString())))))));
			    			 
			    		 }
			    		 else
			    		 {
			    			 double dblTotalTaxAmt=Double.parseDouble(df.format((Double.parseDouble(mapTaxAmt.get(map.getKey().toString())))));
			    			 mapTotalTaxAmt.put(map.getKey().toString(), dblTotalTaxAmt);
			    		 }
			    		 
			    		 
			    	 }
			    	 else
			    	 {
			    		 dataList.add("0.00");
			    	 }
				 }
						
						
						
						
						
				
				//dataList.add(df.format(Double.parseDouble(objInvoice[6].toString())));
				dataList.add(df.format(Double.parseDouble(objInvoice[7].toString())));
				dataList.add(objInvoice[11].toString());
				detailList.add(dataList);
				
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[7].toString()));
				dblTotalValue = dblTotalValue.add(value);
				value = new BigDecimal(Double.parseDouble(objInvoice[5].toString()));
				dblSubTotalValue = dblSubTotalValue.add(value);
				value = new BigDecimal(Double.parseDouble(objInvoice[6].toString()));
				dblTaxTotalValue = dblTaxTotalValue.add(value);
			}
		}

		totalsList.add(df.format(dblSubTotalValue));
		//totalsList.add(df.format(dblTaxTotalValue));
		for(Map.Entry map:mapAllTaxes.entrySet())
		{
		    if(mapTotalTaxAmt.containsKey(map.getKey().toString()))
		    {
		    	double dblFinalTotalTAxAmt=mapTotalTaxAmt.get(map.getKey().toString());
		    	totalsList.add(df.format(dblFinalTotalTAxAmt));
		    }
		    else
		    {
		    	totalsList.add("0.00");
		    }
			
		}
		totalsList.add(df.format(dblTotalValue));
		
		
		
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] excelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			excelHeader[k] = objHeader[k].toString();
		}
		
		//detailList.add(listofInvFlash);
		List blankList = new ArrayList();
		detailList.add(blankList);// Blank Row at Bottom
		detailList.add(totalsList);
		
		retList.add("BillWiseInvoiceData_" + fromDate + "to" + toDate + "_" + userCode);
		retList.add(excelHeader);
		retList.add(detailList);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	
	
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportSettlementWiseInvoiceFlash", method = RequestMethod.GET)
	private ModelAndView funExportSettlementWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		// Totals at Bottom
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");	
		totalsList.add("");	
		
		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String currencyName="";
		BigDecimal dblTotalValue = new BigDecimal(0);
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}

		
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("select c.strSettlementCode,ifnull(c.strSettlementDesc,''),ifnull(c.strSettlementType,''),sum(a.dblGrandTotal)/" + currValue + " "
				+ " from tblinvoicehd a,tblsettlementmaster c,tblpartymaster d "
				+ " where  a.strSettlementCode=c.strSettlementCode and  a.strCustCode=d.strPCode "
				+ " and a.strLocCode='" + locCode + "' and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
		}
		
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append(" and  a.strCurrencyCode='" + currencyCode + "' ");
		}

		sqlInvoiceFlash.append( "group by a.strSettlementCode ");

		DecimalFormat df = new DecimalFormat("#.##");
		
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
		
		  for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List DataList = new ArrayList<>();
				DataList.add(objInvoice[0].toString());
				DataList.add(objInvoice[1].toString());
				DataList.add(objInvoice[2].toString());
				DataList.add(currencyName);
				DataList.add(df.format(new BigDecimal(Double.parseDouble(objInvoice[3].toString()))));
				
				detailList.add(DataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);

			}
		}

		totalsList.add(df.format(dblTotalValue));
		
		headerList.add("Settlement Code");
		headerList.add("Settlement Name");
		headerList.add("Settlement Type");
		headerList.add("Currency");
		headerList.add("Sales Amount");

		Object[] objHeader = (Object[]) headerList.toArray();

		String[] excelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			excelHeader[k] = objHeader[k].toString();
		}
		
		//detailList.add(listofInvFlash);
		List blankList = new ArrayList();
		detailList.add(blankList);// Blank Row at Bottom
		detailList.add(totalsList);
		
		retList.add("SettlementWiseInvoiceData_" + fromDate + "to" + toDate + "_" + userCode);
		retList.add(excelHeader);
		retList.add(detailList);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportOperatorWiseInvoiceFlash", method = RequestMethod.GET)
	private ModelAndView funExportOperatorWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		// Totals at Bottom
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");	
		totalsList.add("");
		totalsList.add("");	
		totalsList.add("");	

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String currencyName="";
		BigDecimal dblTotalValue = new BigDecimal(0);
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}

		
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("SELECT b.strUserCode,b.strUserName,  sum(a.dblGrandTotal)/" + currValue + ",sum(a.dblDiscountAmt)/" + currValue + ",ifnull(c.strSettlementDesc,'')  from tblinvoicehd a,tbluserhd b,tblsettlementmaster c " + " WHERE a.strUserCreated=b.strUserCode and a.strSettlementCode=c.strSettlementCode " + " and a.strLocCode='" + locCode + "' and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate
				+ "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append(" and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append("group by a.strUserCreated,c.strSettlementCode order by a.strUserCreated ");


		DecimalFormat df = new DecimalFormat("#.##");
		double floatingPoint = 0.0;
		String strUserCode = "";
		double userSalesTtl = 0.0;
		Map<String, Double> hmUserSalesDtl = new HashMap<String, Double>();
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
		
		  for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(currencyName);
				floatingPoint = Double.parseDouble(objInvoice[2].toString());
				floatingPoint = Double.parseDouble(df.format(floatingPoint).toString());
				dataList.add(df.format(floatingPoint));
				dataList.add(objInvoice[3].toString());
				dataList.add(objInvoice[4].toString());
				dataList.add(df.format(floatingPoint));

				if (!(objInvoice[0].toString().equals(strUserCode))) {
					// DataList.add("");
					if (i == 0) {
						hmUserSalesDtl.put(objInvoice[0].toString(), Double.parseDouble(objInvoice[2].toString()));
					} else {
						hmUserSalesDtl.put(strUserCode, userSalesTtl);
					}

					userSalesTtl = 0.0;
				} else {
					// DataList.add(userSalesTtl);
				}
				userSalesTtl += Double.parseDouble(objInvoice[2].toString());
				strUserCode = objInvoice[0].toString();
				detailList.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalValue = dblTotalValue.add(value);

			}
			hmUserSalesDtl.put(strUserCode, userSalesTtl);
		}

		totalsList.add(df.format(dblTotalValue));
		
		
		
		headerList.add("User Code");
		headerList.add("User Name");
		headerList.add("Currency");
		headerList.add("Sales Amount");
		headerList.add("Discount Amt");
		headerList.add("Settlement Mode");
		headerList.add("Operator total");

		Object[] objHeader = (Object[]) headerList.toArray();

		String[] excelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			excelHeader[k] = objHeader[k].toString();
		}
		
		//detailList.add(listofInvFlash);
		List blankList = new ArrayList();
		detailList.add(blankList);// Blank Row at Bottom
		detailList.add(totalsList);
		
		retList.add("OperatorWiseInvoiceData_" + fromDate + "to" + toDate + "_" + userCode);
		retList.add(excelHeader);
		retList.add(detailList);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportCustomerWiseInvoiceFlash", method = RequestMethod.GET)
	private ModelAndView funExportCustomerWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		// Totals at Bottom
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");
		totalsList.add("");
		totalsList.add("");

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String currencyName="";
		BigDecimal dblTotalValue = new BigDecimal(0);
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("select b.strCustCode,a.strPName,a.strPType,count(b.strInvCode),sum(b.dblGrandTotal)/" + currValue + " " + " from tblpartymaster a,tblinvoicehd b where b.strCustCode=a.strPCode  and b.strLocCode='" + locCode + "' " + " and date(b.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  b.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  b.strSettlementCode='" + settlementcode + "' ");
		}
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  b.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append(" and  b.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append( "group by b.strCustCode  order by sum(b.dblGrandTotal) desc ");

		DecimalFormat df = new DecimalFormat("#.##");
		
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
		  for (int i = 0; i < listOfInvoice.size(); i++) 
			{
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List DataList = new ArrayList<>();
				DataList.add(objInvoice[0].toString());
				DataList.add(objInvoice[1].toString());
				DataList.add(objInvoice[2].toString());
				DataList.add(objInvoice[3].toString());
				DataList.add(currencyName);
				DataList.add(df.format(Double.valueOf(objInvoice[4].toString())));
				
				detailList.add(DataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[4].toString()));
				dblTotalValue = dblTotalValue.add(value);

		   }
		}

		totalsList.add(df.format(dblTotalValue));
		
		headerList.add("Customer Code");
		headerList.add("Customer Name");
		headerList.add("Customer Type");
		headerList.add("No Of Bills");
		headerList.add("Currency");
		headerList.add("Sales Amount");

		Object[] objHeader = (Object[]) headerList.toArray();

		String[] excelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			excelHeader[k] = objHeader[k].toString();
		}
		
		//detailList.add(listofInvFlash);
		List blankList = new ArrayList();
		detailList.add(blankList);// Blank Row at Bottom
		detailList.add(totalsList);
		
		retList.add("CustomerWiseInvoiceData_" + fromDate + "to" + toDate + "_" + userCode);
		retList.add(excelHeader);
		retList.add(detailList);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportProductWiseInvoiceFlash", method = RequestMethod.GET)
	private ModelAndView funExportProductWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		// Totals at Bottom
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String currencyName="";
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTotalQty = new BigDecimal(0);
			
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("SELECT  b.strProdCode,c.strProdName,sum(b.dblQty)/" + currValue + ", SUM(b.dblQty*b.dblPrice)/" + currValue + " FROM tblinvoicedtl b, tblproductmaster c,tblinvoicehd a,tblpartymaster d " + " WHERE a.strInvCode=b.strInvCode  and b.strProdCode=c.strProdCode and a.strCustCode=d.strPCode and a.strLocCode='" + locCode + "' " + " and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
		}
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append(" and  a.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append(" and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append("group by b.strProdCode ");

		DecimalFormat df = new DecimalFormat("#.##");
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
		for (int i = 0; i < listOfInvoice.size(); i++) 
		   {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(currencyName);
				dataList.add(df.format(Double.valueOf(objInvoice[2].toString())));
				dataList.add(df.format(Double.valueOf(objInvoice[3].toString())));
				
				detailList.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);			
				BigDecimal valueQty = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalQty = dblTotalQty.add(valueQty);
			}
		}

		totalsList.add(df.format(dblTotalQty));
		totalsList.add(df.format(dblTotalValue));
	
		
		headerList.add("Product Code");
		headerList.add("Product Name");
		headerList.add("Currency");
		headerList.add("Quantity");
		headerList.add("Sales Amount");

		Object[] objHeader = (Object[]) headerList.toArray();

		String[] excelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			excelHeader[k] = objHeader[k].toString();
		}
		
		//detailList.add(listofInvFlash);
		List blankList = new ArrayList();
		detailList.add(blankList);// Blank Row at Bottom
		detailList.add(totalsList);
		
		retList.add("ProductWiseInvoiceData_" + fromDate + "to" + toDate + "_" + userCode);
		retList.add(excelHeader);
		retList.add(detailList);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportGroupSubGroupWiseInvoiceFlash", method = RequestMethod.GET)
	private ModelAndView funExportGroupSubGroupWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		// Totals at Bottom
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");
		
		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String withZeroAmt = request.getParameter("withZeroAmt").toString();
		String currencyName="";
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTotalQty = new BigDecimal(0);
		BigDecimal dblTotalDis = new BigDecimal(0);
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("select b.strSGCode,c.strSGName,sum(d.dblQty)/" + currValue + ",sum(d.dblQty*d.dblUnitPrice)/" + currValue + ",e.strGName,sum(d.dblProdDiscAmount)/" + currValue + "  from tblinvoicehd a,tblproductmaster b,tblsubgroupmaster c,tblinvoicedtl d,tblgroupmaster e,tblpartymaster f " + " where a.strInvCode=d.strInvCode  and d.strProdCode=b.strProdCode and b.strSGCode=c.strSGCode and c.strGCode=e.strGCode and a.strCustCode=f.strPCode and a.strLocCode='" + locCode + "' " + " and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate
				+ "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  a.strSettlementCode='" + settlementcode + "' ");
		}
		
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append(" and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append( "  group by  c.strSGCode ");

		DecimalFormat df = new DecimalFormat("#.##");
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) 
		{
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				BigDecimal disValue = new BigDecimal(Double.parseDouble(objInvoice[5].toString()));
				dblTotalDis= dblTotalDis.add(disValue);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);
				if(withZeroAmt.equals("Yes"))
				{
					dblTotalValue=dblTotalValue.subtract(dblTotalDis);
				}
				BigDecimal qtyTot = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalQty= dblTotalQty.add(qtyTot);
			}
		}
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[4].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(currencyName);
				dataList.add(df.format(Double.valueOf(objInvoice[2].toString())));
				dataList.add(df.format(Double.valueOf(objInvoice[5].toString())));
				if(withZeroAmt.equals("Yes"))
				{
					double amt=Double.valueOf(objInvoice[3].toString())-Double.valueOf(objInvoice[5].toString());
					dataList.add(df.format(amt));
					dataList.add(df.format(Math.round((amt/dblTotalValue.doubleValue())*100))+"%");
				}
				else
				{
					dataList.add(df.format(Double.valueOf(objInvoice[3].toString())));
					dataList.add(df.format(Math.round((Double.valueOf(objInvoice[3].toString())/dblTotalValue.doubleValue())*100))+"%");
				}
				
				detailList.add(dataList);
				
			}
		}
		totalsList.add(df.format(dblTotalQty));
		totalsList.add(df.format(dblTotalDis));
		totalsList.add(df.format(dblTotalValue));
		
		
		headerList.add("Group Name");
		headerList.add("SubGroup Name");
		headerList.add("Currency");
		headerList.add("Quantity");
		headerList.add("Dis Amount");
		headerList.add("Sales Amt");
		headerList.add("Sales Per");

		Object[] objHeader = (Object[]) headerList.toArray();

		String[] excelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			excelHeader[k] = objHeader[k].toString();
		}
		
		
		
		//detailList.add(listofInvFlash);
		List blankList = new ArrayList();
		detailList.add(blankList);// Blank Row at Bottom
		detailList.add(totalsList);
		
		retList.add("GroupSubGroupWiseInvoiceData_" + fromDate + "to" + toDate + "_" + userCode);
		retList.add(excelHeader);
		retList.add(detailList);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportManufactureWiseInvoiceFlash", method = RequestMethod.GET)
	private ModelAndView funExportManufactureWiseWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		// Totals at Bottom
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");
	
		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String currencyName="";
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTotalQty = new BigDecimal(0);
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append("  select b.strManufacturerCode,c.strManufacturerName,sum(d.dblQty)/" + currValue + ",sum(d.dblQty*d.dblUnitPrice)/" + currValue + " from tblinvoicehd a,tblproductmaster b,tblmanufacturemaster c,tblinvoicedtl d,tblpartymaster e " + " where a.strInvCode=d.strInvCode  and d.strProdCode=b.strProdCode and b.strManufacturerCode=c.strManufacturerCode  and a.strCustCode=e.strPCode  and a.strLocCode='" + locCode + "' "
				+ " and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strSettlementCode='" + settlementcode + "' ");
		}
		
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append(" and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append( " group by  c.strManufacturerCode ");

		DecimalFormat df = new DecimalFormat("#.##");
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(currencyName);
				dataList.add(df.format(Double.valueOf(objInvoice[2].toString())));
				dataList.add(df.format(Double.valueOf(objInvoice[3].toString())));
				
				detailList.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);
				BigDecimal valueQty = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalQty = dblTotalQty.add(valueQty);

			}
		}
		totalsList.add(df.format(dblTotalQty));
		totalsList.add(df.format(dblTotalValue));
		
		
		headerList.add("Manufacture Code");
		headerList.add("Manufacture Name");
		headerList.add("Currency");
		headerList.add("Qty");
		headerList.add("Sales Amt");

		Object[] objHeader = (Object[]) headerList.toArray();

		String[] excelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			excelHeader[k] = objHeader[k].toString();
		}
		
		//detailList.add(listofInvFlash);
		List blankList = new ArrayList();
		detailList.add(blankList);// Blank Row at Bottom
		detailList.add(totalsList);
		
		retList.add("ManufactureWiseInvoiceData_" + fromDate + "to" + toDate + "_" + userCode);
		retList.add(excelHeader);
		retList.add(detailList);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportDepartmentWiseInvoiceFlash", method = RequestMethod.GET)
	private ModelAndView funExportDepartmentWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest req) {

		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		// Totals at Bottom
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");
		
		String fromDate = req.getParameter("frmDte").toString();
		String toDate = req.getParameter("toDte").toString();
		String locCode = req.getParameter("locCode").toString();
		String custCode = req.getParameter("custCode").toString();
		String currencyCode=req.getParameter("currencyCode").toString();
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String currencyName="";
		BigDecimal dblTotalValue = new BigDecimal(0);
		BigDecimal dblTotalQty = new BigDecimal(0);
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}

		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append( " select b.strLocCode,b.strLocName ,sum(d.dblQty)/" + currValue + ",sum(d.dblQty*d.dblUnitPrice)/" + currValue + " from tblinvoicehd a,tbllocationmaster b,tblinvoicedtl d,tblpartymaster c " + "   where a.strInvCode=d.strInvCode  and a.strLocCode=b.strLocCode  and a.strCustCode=c.strPCode  " + " and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strSettlementCode='" + settlementcode + "' ");
		}
		if (!locCode.equals("All")) {
			sqlInvoiceFlash.append( "and a.strLocCode='" + locCode + "' ");
		}
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append(" group by  a.strLocCode ");

		DecimalFormat df = new DecimalFormat("#.##");

		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(currencyName);
				dataList.add(df.format(Double.valueOf(objInvoice[2].toString())));
				dataList.add(df.format(Double.valueOf(objInvoice[3].toString())));
				
				detailList.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);
				
				BigDecimal qty = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalQty = dblTotalQty.add(qty);

			}

		}

		totalsList.add(df.format(dblTotalQty));
		totalsList.add(df.format(dblTotalValue));
		
		headerList.add("Department Code");
		headerList.add("Department Name");
		headerList.add("Currency");
		headerList.add("Quantity");
		headerList.add("Sales Amt");

		Object[] objHeader = (Object[]) headerList.toArray();

		String[] excelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			excelHeader[k] = objHeader[k].toString();
		}
		
		//detailList.add(listofInvFlash);
		List blankList = new ArrayList();
		detailList.add(blankList);// Blank Row at Bottom
		detailList.add(totalsList);
		
		retList.add("DepartmentWiseInvoiceData_" + fromDate + "to" + toDate + "_" + userCode);
		retList.add(excelHeader);
		retList.add(detailList);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportMonthWiseInvoiceFlash", method = RequestMethod.GET)
	private ModelAndView funExportMonthWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		// Totals at Bottom
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");
		totalsList.add("");
		totalsList.add("");
		
		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String currencyName="";
		BigDecimal dblTotalValue = new BigDecimal(0);
				
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}


		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append( "select c.strSettlementCode,ifnull(c.strSettlementDesc,''),ifnull(c.strSettlementType,''),sum(a.dblGrandTotal)/" + currValue + ",MONTHNAME(DATE(a.dteInvDate)), YEAR(DATE(a.dteInvDate))"
				+ " from tblinvoicehd a,tblsettlementmaster c,tblpartymaster d " + " where  a.strSettlementCode=c.strSettlementCode and a.strCustCode=d.strPCode " + " and a.strLocCode='" + locCode + "' and date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' and  a.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strSettlementCode='" + settlementcode + "' ");
		}
		
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  a.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
		}
		
		sqlInvoiceFlash.append( "group by a.strSettlementCode,YEAR(DATE(a.dteInvDate)),MONTHNAME(DATE(a.dteInvDate))"
				        + "  order by YEAR(DATE(a.dteInvDate)) desc,MONTHNAME(DATE(a.dteInvDate)) desc");

		DecimalFormat df = new DecimalFormat("#.##");
		double floatingPoint = 0.0;
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
			for (int i = 0; i < listOfInvoice.size(); i++) {
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[1].toString());
				dataList.add(objInvoice[2].toString());
				dataList.add(objInvoice[4].toString());
				dataList.add(objInvoice[5].toString()+" ");
				dataList.add(currencyName);
				floatingPoint = Double.parseDouble(objInvoice[3].toString());
				dataList.add(df.format(floatingPoint));
				
				
				detailList.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[3].toString()));
				dblTotalValue = dblTotalValue.add(value);

			}
		}

		totalsList.add(df.format(dblTotalValue));
		
		headerList.add("Settlement Name");
		headerList.add("Settlement Type");
		headerList.add("Month Name");
		headerList.add("Year");
		headerList.add("Currency");
		headerList.add("Sales Amt");

		Object[] objHeader = (Object[]) headerList.toArray();

		String[] excelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			excelHeader[k] = objHeader[k].toString();
		}
		
		//detailList.add(listofInvFlash);
		List blankList = new ArrayList();
		detailList.add(blankList);// Blank Row at Bottom
		detailList.add(totalsList);
		
		retList.add("MonthWiseInvoiceData_" + fromDate + "to" + toDate + "_" + userCode);
		retList.add(excelHeader);
		retList.add(detailList);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportRegionWiseInvoiceFlash", method = RequestMethod.GET)
	private ModelAndView funExportRegionWiseInvoiceFlash(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request) {

		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		// Totals at Bottom
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");

		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String currencyName="";
		BigDecimal dblTotalValue = new BigDecimal(0);
		
		double currValue = 1.0;
		if(currencyCode.equalsIgnoreCase("All"))
		{
			currencyName=currencyList.get(baseCurrencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		else
		{
			currencyName=currencyList.get(currencyCode);
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
			if (objCurrModel != null) {
				currValue = objCurrModel.getDblConvToBaseCurr();
			}
		}
		
		StringBuilder sqlInvoiceFlash = new StringBuilder();
		sqlInvoiceFlash.setLength(0);
		sqlInvoiceFlash.append( "SELECT c.strRegionCode,c.strRegionDesc, SUM(b.dblGrandTotal)/" + currValue + ""
				+ " FROM tblpartymaster a,tblinvoicehd b,tblregionmaster c"
				+ " WHERE b.strCustCode=a.strPCode AND a.strRegion=c.strRegionCode  "
				+ " and b.strLocCode='" + locCode + "' " + " "
				+ " and date(b.dteInvDate) between '" + fromDate + "' and '" + toDate + "' "
				+ " and  b.strClientCode='" + clientCode + "'");
		if (!settlementcode.equals("All")) {
			sqlInvoiceFlash.append(" and  b.strSettlementCode='" + settlementcode + "' ");
		}
		if (!custCode.equals("All")) {
			sqlInvoiceFlash.append( " and  b.strCustCode='" + custCode + "' ");
		}
		if(!currencyCode.equalsIgnoreCase("All"))
		{
			sqlInvoiceFlash.append( " and  b.strCurrencyCode='" + currencyCode + "' ");
		}
		sqlInvoiceFlash.append( "group by a.strRegion  order by sum(b.dblGrandTotal) desc ");

		DecimalFormat df = new DecimalFormat("#.##");
		
		List listOfInvoice = objGlobalService.funGetList(sqlInvoiceFlash.toString(), "sql");
		if (!listOfInvoice.isEmpty()) {
		  for (int i = 0; i < listOfInvoice.size(); i++) 
			{
				Object[] objInvoice = (Object[]) listOfInvoice.get(i);
				List dataList = new ArrayList<>();
				dataList.add(objInvoice[0].toString());
				dataList.add(objInvoice[1].toString());
				dataList.add(currencyName);
				dataList.add(df.format(Double.valueOf(objInvoice[2].toString())));
				
				detailList.add(dataList);
				BigDecimal value = new BigDecimal(Double.parseDouble(objInvoice[2].toString()));
				dblTotalValue = dblTotalValue.add(value);

		   }
		  
		}

		totalsList.add(df.format(dblTotalValue));
		
		headerList.add("Region Code");
		headerList.add("Region Name");
		headerList.add("Currency");
		headerList.add("Sales Amount");

		Object[] objHeader = (Object[]) headerList.toArray();

		String[] excelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			excelHeader[k] = objHeader[k].toString();
		}
		
		List blankList = new ArrayList();
		detailList.add(blankList);// Blank Row at Bottom
		detailList.add(totalsList);
		
		retList.add("RegionWiseInvoiceData_" + fromDate + "to" + toDate + "_" + userCode);
		retList.add(excelHeader);
		retList.add(detailList);
		return new ModelAndView("excelViewWithReportName", "listWithReportName", retList);
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/rptBillWiseFlashReportPDF", method = RequestMethod.GET)
	private void funBillWiseFlashReportPDF(@RequestParam("settlementcode") String settlementcode, HttpServletRequest request,HttpServletResponse resp) {
		
			
		
		String fromDate = request.getParameter("frmDte").toString();
		String toDate = request.getParameter("toDte").toString();
		String locCode = request.getParameter("locCode").toString();
		String custCode = request.getParameter("custCode").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String dbWebBook=request.getSession().getAttribute("WebBooksDB").toString();
		String companyName = request.getSession().getAttribute("companyName").toString();
		String currencyCode=request.getParameter("currencyCode").toString();
		String propertyCode = request.getSession().getAttribute("propertyCode").toString();
		StringBuilder sqlInvoiceFlashPDF= new StringBuilder();
		try
		{	
			Connection con = objGlobalfunction.funGetConnection(request);
			String currencyName="";
			DecimalFormat df = new DecimalFormat("#.##");
			double currValue = 1.0;
			String type="pdf";
			
			
			clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
			
			
			if(currencyCode.equalsIgnoreCase("All"))
			{   
				currencyName=currencyList.get(baseCurrencyCode);
				clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(baseCurrencyCode, clientCode);
				if (objCurrModel != null) {
					currValue = objCurrModel.getDblConvToBaseCurr();
				}
			}
			else
			{
				currencyName=currencyList.get(currencyCode);
				clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currencyCode, clientCode);
				if (objCurrModel != null) {
					currValue = objCurrModel.getDblConvToBaseCurr();
				}
			}
			
			sqlInvoiceFlashPDF.append("select a.strInvCode ,DATE_FORMAT(a.dteInvDate,'%d-%m-%Y') as dteInvDate,b.strPName as strCustName,a.dblSubTotalAmt/" + currValue + " as subTotalAmt,a.dblTaxAmt/" + currValue + " as taxAmt"
					+ ",(a.dblSubTotalAmt/" + currValue + "+ a.dblTaxAmt/" + currValue + ") as totalAmt,ifnull(a.strNarration,'') as strRemark"
					+ " FROM tblpartymaster b,tblsettlementmaster c,tblinvoicehd a "
					+ " where   date(a.dteInvDate) between '" + fromDate + "' and '" + toDate + "' " + " and a.strLocCode='" + locCode +"' "
					+ " and a.strCustCode=b.strPCode and  a.strClientCode='" + clientCode + "'");
			if (!settlementcode.equals("All")) {
				sqlInvoiceFlashPDF.append(" and  a.strSettlementCode='" + settlementcode + "' ");
			}
			if (!custCode.equals("All")) {
				sqlInvoiceFlashPDF.append( " and  a.strCustCode='" + custCode + "' ");
			}
			if(!currencyCode.equalsIgnoreCase("All"))
			{
				sqlInvoiceFlashPDF.append( " and  a.strCurrencyCode='" + currencyCode + "' ");
			}
			
	
			sqlInvoiceFlashPDF.append("and a.strSettlementCode=c.strSettlementCode and a.dblSubTotalAmt>0 "
				+ "  order by a.strInvCode ");
			
		/*	List listOfInvoicePDF = objGlobalService.funGetList(sqlInvoiceFlashPDF.toString(), "sql");
			List dataList= new ArrayList<>();
				
			clsInvoiceBean objInvoiceFlashBean=new clsInvoiceBean();
			if (!listOfInvoicePDF.isEmpty() && listOfInvoicePDF!=null) {
				for (int i = 0; i < listOfInvoicePDF.size(); i++) {
					Object[] objInvoice = (Object[]) listOfInvoicePDF.get(i);
					objInvoiceFlashBean=new clsInvoiceBean();
					objInvoiceFlashBean.setStrInvCode(objInvoice[0].toString());
					objInvoiceFlashBean.setDteInvDate(objInvoice[1].toString());
					objInvoiceFlashBean.setStrCustName(objInvoice[2].toString());
					objInvoiceFlashBean.setDblSubTotalAmt(Double.parseDouble(df.format(Double.parseDouble(objInvoice[3].toString()))));
					objInvoiceFlashBean.setDblTaxAmt(Double.parseDouble(df.format(Double.parseDouble(objInvoice[4].toString()))));
					objInvoiceFlashBean.setDblTotalAmt(Double.parseDouble(df.format(Double.parseDouble(objInvoice[5].toString()))));
					dataList.add(objInvoiceFlashBean);
					
					
				}
			}*/
			String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
			String reportName = servletContext.getRealPath("/WEB-INF/reports/webcrm/rptCustomerWiseInvoicePDF.jrxml");
			JasperDesign jd = JRXmlLoader.load(reportName);
			
			
			JRDesignQuery billWiseInvoice = new JRDesignQuery();
			billWiseInvoice.setText(sqlInvoiceFlashPDF.toString());
			Map<String, JRDataset> datasetMap = jd.getDatasetMap();
			JRDesignDataset taxSumDataset = (JRDesignDataset) datasetMap.get("dsBillWiseInvoice");
			taxSumDataset.setQuery(billWiseInvoice);
			
			
			
			
		
			
			JasperReport jr = JasperCompileManager.compileReport(jd);
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
			/*hm.put("srCode", srCode);
			hm.put("locationCode", locationCode);
			hm.put("locationName", locationName);
			hm.put("againstName", againstName);
			hm.put("strInvoiceCode", dcCode);
			hm.put("custCode", custCode);
			hm.put("custName", custName);
			hm.put("currencyName", currencyName);
			hm.put("SRDate", salesReturnDate);
			hm.put("InvoiceDate", invoiceDate);
	*/
			JasperPrint p = JasperFillManager.fillReport(jr, hm, con);
			if (type.trim().equalsIgnoreCase("pdf")) {
				ServletOutputStream servletOutputStream = resp.getOutputStream();
				byte[] bytes = null;
				bytes = JasperRunManager.runReportToPdf(jr, hm, con);
				resp.setContentType("application/pdf");
				resp.setContentLength(bytes.length);
				servletOutputStream.write(bytes, 0, bytes.length);
				resp.setHeader("Content-Disposition", "attachment;filename=" + "rptBillWiseInvoice." + type.trim());
				servletOutputStream.flush();
				servletOutputStream.close();
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
}
