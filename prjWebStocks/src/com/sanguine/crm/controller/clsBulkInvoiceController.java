package com.sanguine.crm.controller;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.Connection;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.controller.clsJVGeneratorController;
import com.sanguine.crm.bean.clsInvoiceBean;
import com.sanguine.crm.bean.clsInvoiceDtlBean;
import com.sanguine.crm.bean.clsSalesOrderBean;
import com.sanguine.crm.model.clsDeliveryChallanHdModel;
import com.sanguine.crm.model.clsDeliveryChallanHdModel_ID;
import com.sanguine.crm.model.clsDeliveryChallanModelDtl;
import com.sanguine.crm.model.clsInvSalesOrderDtl;
import com.sanguine.crm.model.clsInvoiceHdModel;
import com.sanguine.crm.model.clsInvoiceModelDtl;
import com.sanguine.crm.model.clsInvoiceProdTaxDtl;
import com.sanguine.crm.model.clsInvoiceTaxDtlModel;
import com.sanguine.crm.model.clsSalesOrderDtl;
import com.sanguine.crm.service.clsCRMSettlementMasterService;
import com.sanguine.crm.service.clsDeliveryChallanHdService;
import com.sanguine.crm.service.clsInvoiceHdService;
import com.sanguine.crm.service.clsPartyMasterService;
import com.sanguine.crm.service.clsSalesOrderService;
import com.sanguine.model.clsCompanyMasterModel;
import com.sanguine.model.clsCurrencyMasterModel;
import com.sanguine.model.clsProductMasterModel;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.model.clsSettlementMasterModel;
import com.sanguine.service.clsCurrencyMasterService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsProductMasterService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.util.clsNumberToWords;



@Controller
public class clsBulkInvoiceController {
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	
	@Autowired
	private clsInvoiceHdService objInvoiceHdService;

	@Autowired
	private clsDeliveryChallanHdService objDeliveryChallanHdService;

	@Autowired
	private clsGlobalFunctions objGlobalFunctions;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@Autowired
	private clsProductMasterService objProductMasterService;
   
	@Autowired
	private clsCRMSettlementMasterService objSttlementMasterService;
	
	@Autowired
	private clsPartyMasterService objPartyMasterService;
	
	@Autowired
	clsJVGeneratorController objJVGen;
	

	@Autowired
	private clsCRMSettlementMasterService objSettlementService;
    
	@Autowired
	private clsCurrencyMasterService objCurrencyMasterService;

	@Autowired
	private intfBaseService objBaseService;
	

	@Autowired
	private clsSalesOrderService objSalesOrderService;
	
	@RequestMapping(value = "/frmBulkInvoice", method = RequestMethod.GET)
	public ModelAndView funOpenSOVarienceListForm(Map<String, Object> model, HttpServletRequest request) {
		
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		request.getSession().setAttribute("formName", "frmBulkInvoice");
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		List<String> strAgainst = new ArrayList<>();
		strAgainst.add("Direct");
//		strAgainst.add("Delivery Challan");
		strAgainst.add("Sales Order");
		model.put("againstList", strAgainst);

		Map<String, String> settlementList = objSettlementService.funGetSettlementComboBox(clientCode);
		model.put("settlementList", settlementList);

		Map<String, String> hmCurrency = objCurrencyMasterService.funCurrencyListToDisplay(clientCode);
		if (hmCurrency.isEmpty()) {
			hmCurrency.put("", "");
		}
		model.put("currencyList", hmCurrency);
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBulkInvoice_1", "command", new clsInvoiceBean());
		} else {
			return new ModelAndView("frmBulkInvoice", "command",  new clsInvoiceBean());
		}

	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/loadAllCustomerSO", method = RequestMethod.GET)
	public @ResponseBody List funAssignFields(@RequestParam("CustCode") String strCustCode,@RequestParam("frmDate") String dteFrmDate,@RequestParam("toDate") String dteToDate,HttpServletRequest req)
	{
		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String StrSuppCode[]=strCustCode.split(",");
		List listSo=new ArrayList();
		String fd = dteFrmDate.split("-")[0];
		String fm = dteFrmDate.split("-")[1];
		String fy = dteFrmDate.split("-")[2];

		String td = dteToDate.split("-")[0];
		String tm = dteToDate.split("-")[1];
		String ty = dteToDate.split("-")[2];

		String dteFrmDateSql = fy + "-" + fm + "-" + fd;
		String dteToDateSql = ty + "-" + tm + "-" + td;
		
		clsSalesOrderBean objSoBean=null;
		
		for(int i=0;i<StrSuppCode.length;i++)
		{
			String strSupplierCode=StrSuppCode[i];
			String sqlSO=" SELECT a.strSOCode,a.dteSODate,a.strCustCode,a.dblSubTotal,c.strPName,a.strStatus "
				+" FROM tblsalesorderhd a,tbllocationmaster b,tblpartymaster c " 
				+" WHERE a.strLocCode=b.strLocCode AND a.strCustCode=c.strPCode "
				+"  AND c.strPType='cust' AND a.strSOCode NOT IN( "
				+" SELECT strSOCode FROM tblinvsalesorderdtl)  "
				+" AND a.strClientCode='"+clientCode+"' AND a.strCustCode='"+strSupplierCode+"' " 
				+" AND a.strLocCode='L000001' AND date(a.dteFulmtDate) BETWEEN '"+dteFrmDateSql+"' AND '"+dteToDateSql+"'";
				
			List listSO = objGlobalFunctionsService.funGetList(sqlSO,"sql");
			if (!listSO.isEmpty())
			{
				
				for (int j = 0; j < listSO.size(); j++)
				{
				    objSoBean=new clsSalesOrderBean();
					Object[] objSo = (Object[]) listSO.get(j);
					objSoBean.setStrSOCode(objSo[0].toString());
					objSoBean.setDteSODate(funDateConversion(objSo[1].toString()));
					objSoBean.setStrCustCode(objSo[2].toString());
					objSoBean.setDblSubTotal(Double.parseDouble(objSo[3].toString()));
					objSoBean.setStrCustName(objSo[4].toString());
					listSo.add(objSoBean);
				}			
			}

		
	}
	return listSo;
}
	
	
	@SuppressWarnings({ "unused", "rawtypes" })
	@RequestMapping(value = "/saveBulkInvoice", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsInvoiceBean objBean, BindingResult result, HttpServletRequest request)
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
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		String startDate = request.getSession().getAttribute("startDate").toString();
		String strLocCode= request.getSession().getAttribute("locationCode").toString();
		double dblCurrencyConv = 1.0;
		Date today = Calendar.getInstance().getTime();
		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		String reportDate = df.format(today);
		
		
		String strInvoiceDate=  objGlobalFunctions.funGetDateAndTime("yyyy-MM-dd", objBean.getDteInvDate());   
		
		objBean.setStrInvCode("");
		
		
		List listSO = new ArrayList();
        Map<String, List> mapSoList=new LinkedHashMap<String, List>();
		List<clsInvoiceBean> listInvoiceBean =objBean.getListMultipleSOCodes();
		if (!result.hasErrors())
		{
		if(listInvoiceBean.size()>0)
		{
			
			for(clsInvoiceBean objbean:listInvoiceBean)
			{
				if(objbean.getSOCodethemes()!=null)
				{
					String strSOCode=objbean.getStrSOCode();
					String strCustName=objbean.getStrCustName();
					String strCustCode=objbean.getStrCustCode();
					if(mapSoList.containsKey(strCustCode))
					{
						List listSoCodes=mapSoList.get(strCustCode);
						listSoCodes.add(strSOCode);
						mapSoList.put(strCustCode, listSoCodes);
				    }
					else
					{
						listSO = new ArrayList();
						listSO.add(strSOCode);
						mapSoList.put(strCustCode, listSO);
					}	
				}
			}
		}
		//String[] SOCode = objBean.getStrSOCode().split(",");
		List<clsInvoiceDtlBean> listInvoiceDtlBean=new ArrayList<clsInvoiceDtlBean>();
		clsInvoiceDtlBean objInvDtl=null;
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propCode, clientCode);
		clsCompanyMasterModel objCompModel = objSetupMasterService.funGetObject(clientCode);
		clsInvoiceHdModel objHDModel =null;
		StringBuilder sqlQuery = new StringBuilder();
		DecimalFormat decFormat = objGlobalFunctions.funGetDecimatFormat(request);

		List<clsInvoiceModelDtl> listInvDtlModel = null;

		Map<String, List<clsInvoiceModelDtl>> hmInvCustDtl = new HashMap<String, List<clsInvoiceModelDtl>>();
		Map<String, Map<String, clsInvoiceTaxDtlModel>> hmInvCustTaxDtl = new HashMap<String, Map<String, clsInvoiceTaxDtlModel>>();
		Map<String, List<clsInvoiceProdTaxDtl>> hmInvProdTaxDtl = new HashMap<String, List<clsInvoiceProdTaxDtl>>();
		
		
		StringBuilder arrInvCode = new StringBuilder();
		StringBuilder arrDcCode = new StringBuilder();
		StringBuilder sbSql = new StringBuilder();
		//Customer wise SO Map
		clsInvoiceModelDtl objInvDtlModel =null;
		
		
	    for(Map.Entry<String, List> entrySOList : mapSoList.entrySet())
	    {
			hmInvCustDtl = new HashMap<String, List<clsInvoiceModelDtl>>();
			hmInvCustTaxDtl = new HashMap<String, Map<String, clsInvoiceTaxDtlModel>>();
			hmInvProdTaxDtl = new HashMap<String, List<clsInvoiceProdTaxDtl>>();
		
			double dblSubTotalAmt=0;
			Map mapSubTotal=new HashMap<>();
			
	    	List listSOCust=entrySOList.getValue();
	    	String custCode =entrySOList.getKey();
	    	String strSOCodes="";
	    	if(listSOCust.size()>0){
	    		for(Object objSo:listSOCust){
	    			if(strSOCodes.isEmpty()){
	    				strSOCodes=objSo.toString();
	    			}else{
	    				strSOCodes=strSOCodes+","+objSo.toString();
	    			}
	    		}
	    	}
	    	List objSales = objSalesOrderService.funGetMultipleSODetailsForInvoice(listSOCust,custCode, clientCode);
	    	if (null != objSales)
			{
	    		for (int i = 0; i < objSales.size(); i++)
			   {
	    			objInvDtl=new clsInvoiceDtlBean();
					Object[] obj = (Object[]) objSales.get(i);
					clsSalesOrderDtl saleDtl = new clsSalesOrderDtl();
					objInvDtl.setStrProdCode(obj[0].toString());
					objInvDtl.setStrProdName(obj[1].toString());
					objInvDtl.setStrProdType(obj[2].toString());
	
				//	clsProductMasterModel objProdMaster = objProductMasterService.funGetObject(obj[0].toString(), clientCode);
					objInvDtl.setDblQty(Double.parseDouble(obj[3].toString()));
					objInvDtl.setDblUnitPrice(Double.parseDouble(obj[4].toString()));
					// saleDtl.setDblUnitPrice(objProdMaster.getDblMRP());
					objInvDtl.setDblWeight(Double.parseDouble(obj[5].toString()));
					objInvDtl.setStrClientCode(clientCode);
					objInvDtl.setStrCustCode(obj[6].toString());
					objInvDtl.setStrSOCode(obj[8].toString());				
					double discPer=Double.parseDouble(obj[9].toString());								
					objInvDtl.setDblDisAmt((discPer/100)*(saleDtl.getDblAcceptQty()*saleDtl.getDblUnitPrice()));
					objInvDtl.setIntindex(i);

					
					
					if (!(objInvDtl.getDblQty() == 0))
					{
						//List listDtlBean = objGlobalFunctionsService.funGetList("select strExciseable,strPickMRPForTaxCal from tblproductmaster " + " where strProdCode='" + objInvDtl.getStrProdCode() + "' ", "sql");
						clsProductMasterModel objProdTempUom = objProductMasterService.funGetObject(objInvDtl.getStrProdCode(), clientCode);
					//	Object[] arrProdDtl = (Object[]) listDtlBean.get(0);
						String excisable = objProdTempUom.getStrExciseable();// arrProdDtl[0].toString();
						String pickMRP = objProdTempUom.getStrPickMRPForTaxCal();//
						String key = objInvDtl.getStrCustCode() + "!" + excisable;

						if (hmInvCustDtl.containsKey(key))
						{
							listInvDtlModel = hmInvCustDtl.get(key);
						}
						else
						{
							listInvDtlModel = new ArrayList<clsInvoiceModelDtl>();
						}

						objInvDtlModel = new clsInvoiceModelDtl();

						objInvDtlModel.setStrProdCode(objInvDtl.getStrProdCode());
						objInvDtlModel.setDblPrice(objInvDtl.getDblUnitPrice() * dblCurrencyConv);
						objInvDtlModel.setDblQty(objInvDtl.getDblQty());
						objInvDtlModel.setDblWeight(objInvDtl.getDblWeight());
						objInvDtlModel.setStrProdType(objInvDtl.getStrProdType());
						objInvDtlModel.setStrPktNo(objInvDtl.getStrPktNo());
						objInvDtlModel.setStrRemarks(objInvDtl.getStrRemarks());
						objInvDtlModel.setIntindex(objInvDtl.getIntindex());
						objInvDtlModel.setStrInvoiceable(objInvDtl.getStrInvoiceable());
						objInvDtlModel.setStrSerialNo(objInvDtl.getStrSerialNo());
						objInvDtlModel.setDblUnitPrice(Double.parseDouble(decFormat.format(objInvDtl.getDblUnitPrice() * dblCurrencyConv)));
						objInvDtlModel.setDblAssValue(Double.parseDouble(decFormat.format(objInvDtl.getDblAssValue() * dblCurrencyConv)));
						objInvDtlModel.setDblBillRate(Double.parseDouble(decFormat.format(objInvDtl.getDblBillRate() * dblCurrencyConv)));
						objInvDtlModel.setStrSOCode(objInvDtl.getStrSOCode());
						objInvDtlModel.setStrCustCode(objInvDtl.getStrCustCode());
						objInvDtlModel.setStrUOM(objProdTempUom.getStrReceivedUOM());
						objInvDtlModel.setDblUOMConversion(objProdTempUom.getDblReceiveConversion());
						objInvDtlModel.setDblProdDiscAmount(objInvDtl.getDblDisAmt());
						List<clsInvoiceProdTaxDtl> listInvProdTaxDtl = null;
						if (hmInvProdTaxDtl.containsKey(key))
						{
							listInvProdTaxDtl = hmInvProdTaxDtl.get(key);
						}
						else
						{
							listInvProdTaxDtl = new ArrayList<clsInvoiceProdTaxDtl>();
						}

						double prodMRP = objInvDtl.getDblUnitPrice() * dblCurrencyConv;
						if (objInvDtl.getDblWeight() > 0)
						{
							prodMRP = prodMRP * objInvDtl.getDblWeight();
						}
						double marginePer = 0;
						double marginAmt = 0;
						double billRate = prodMRP;

						sqlQuery.setLength(0);
						sqlQuery.append("select a.dblMargin,a.strProdCode from tblprodsuppmaster a " + " where a.strSuppCode='" + objInvDtl.getStrCustCode() + "' and a.strProdCode='" + objInvDtl.getStrProdCode() + "' " + " and a.strClientCode='" + clientCode + "' ");
						List listProdMargin = objGlobalFunctionsService.funGetList(sqlQuery.toString(), "sql");
						if (listProdMargin.size() > 0)
						{
							Object[] arrObjProdMargin = (Object[]) listProdMargin.get(0);
							marginePer = Double.parseDouble(arrObjProdMargin[0].toString());
							marginAmt = prodMRP * (marginePer / 100);
							billRate = prodMRP - marginAmt;
						}
						clsInvoiceProdTaxDtl objInvProdTaxDtl = new clsInvoiceProdTaxDtl();
						objInvProdTaxDtl.setStrProdCode(objInvDtl.getStrProdCode());
						objInvProdTaxDtl.setStrCustCode(objInvDtl.getStrCustCode());
						objInvProdTaxDtl.setStrDocNo("Margin");
						objInvProdTaxDtl.setDblValue(Double.parseDouble(decFormat.format(marginAmt)));
						objInvProdTaxDtl.setDblTaxableAmt(0);
						objInvProdTaxDtl.setDblWeight(objInvDtl.getDblWeight());
						listInvProdTaxDtl.add(objInvProdTaxDtl);

						sqlQuery.setLength(0);
						sqlQuery.append("select a.dblDiscount from tblpartymaster a " + " where a.strPCode='" + objInvDtl.getStrCustCode() + "' and a.strPType='Cust' ");
						List listproddiscount = objGlobalFunctionsService.funGetList(sqlQuery.toString(), "sql");
						double discAmt=0;
					    if(listproddiscount != null && listproddiscount.size() != 0)
						{
					    	Object objproddiscount = (Object) listproddiscount.get(0);
							double discPer1 = Double.parseDouble(objproddiscount.toString());
						    discAmt = billRate * (discPer1 / 100) * dblCurrencyConv;
							
						}
						
						billRate = billRate - discAmt;
						System.out.println(billRate);
						objInvDtlModel.setDblBillRate(Double.parseDouble(decFormat.format(billRate)));

						objInvProdTaxDtl = new clsInvoiceProdTaxDtl();
						objInvProdTaxDtl.setStrProdCode(objInvDtl.getStrProdCode());
						objInvProdTaxDtl.setStrCustCode(objInvDtl.getStrCustCode());
						objInvProdTaxDtl.setStrDocNo("Disc");
						objInvProdTaxDtl.setDblValue(Double.parseDouble(decFormat.format(discAmt)));
						objInvProdTaxDtl.setDblTaxableAmt(0);
						objInvProdTaxDtl.setDblWeight(objInvDtl.getDblWeight());
						listInvProdTaxDtl.add(objInvProdTaxDtl);

						double prodRateForTaxCal = objInvDtl.getDblUnitPrice() * dblCurrencyConv;
						/*if (objInvDtl.getDblWeight() > 0)
						{
							prodRateForTaxCal = objInvDtl.getDblUnitPrice() * objInvDtl.getDblWeight() * dblCurrencyConv;
						}*/
						String prodTaxDtl = objInvDtl.getStrProdCode() + "," + prodRateForTaxCal + "," + objInvDtl.getStrCustCode() + "," + objInvDtl.getDblQty() + ",0,"+objInvDtl.getDblWeight();
						Map<String, String> hmProdTaxDtl = objGlobalFunctions.funCalculateTax(prodTaxDtl, "Sales", strInvoiceDate, "0",objBean.getStrSettlementCode(), request);
						System.out.println("Map Size= " + hmProdTaxDtl.size());

						Map<String, clsInvoiceTaxDtlModel> hmInvTaxDtl = new HashMap<String, clsInvoiceTaxDtlModel>();
						if (hmInvCustTaxDtl.containsKey(key))
						{
							hmInvTaxDtl = hmInvCustTaxDtl.get(key);
						}
						else
						{
							hmInvTaxDtl.clear();
						}
						clsInvoiceTaxDtlModel objInvTaxModel = null;
						for (Map.Entry<String, String> mapEntrySet : hmProdTaxDtl.entrySet())
						{
							// 137.2#T0000011#Vat 12.5#NA#12.5#15.244444444444444
							// taxable amt,Tax code,tax desc,tax type,tax per

							String taxDtl = mapEntrySet.getValue();
							String taxCode = mapEntrySet.getKey();
							double taxableAmt = Double.parseDouble(decFormat.format(Double.parseDouble(taxDtl.split("#")[0])));
							double taxAmt = Double.parseDouble(taxDtl.split("#")[5]);
							String shortName = taxDtl.split("#")[6];

							double taxAmtForSingleQty = taxAmt / objInvDtl.getDblQty();
							
							taxAmtForSingleQty = Double.parseDouble(decFormat.format(taxAmtForSingleQty));
							
							taxAmt = taxAmtForSingleQty * objInvDtl.getDblQty();

							// For Check it is Correct Or not
							// double
							// taxAmt=Math.round(Double.parseDouble(taxDtl.split("#")[5]));

							if (hmInvTaxDtl.containsKey(mapEntrySet.getKey()))
							{
								objInvTaxModel = hmInvTaxDtl.get(mapEntrySet.getKey());
								objInvTaxModel.setDblTaxableAmt(objInvTaxModel.getDblTaxableAmt() + taxableAmt);
								objInvTaxModel.setDblTaxAmt(objInvTaxModel.getDblTaxAmt() + taxAmt);
							}
							else
							{
								objInvTaxModel = new clsInvoiceTaxDtlModel();
								objInvTaxModel.setStrTaxCode(taxDtl.split("#")[1]);
								objInvTaxModel.setDblTaxAmt(taxAmt);
								objInvTaxModel.setDblTaxableAmt(taxableAmt);
								objInvTaxModel.setStrTaxDesc(taxDtl.split("#")[2]);
							}

							if (null != objInvTaxModel)
							{
								hmInvTaxDtl.put(taxCode, objInvTaxModel);
							}

							objInvProdTaxDtl = new clsInvoiceProdTaxDtl();
							objInvProdTaxDtl.setStrProdCode(objInvDtl.getStrProdCode());
							objInvProdTaxDtl.setStrCustCode(objInvDtl.getStrCustCode());
							objInvProdTaxDtl.setStrDocNo(taxDtl.split("#")[1]);
							objInvProdTaxDtl.setDblValue(Double.parseDouble(decFormat.format(taxAmt)));
							objInvProdTaxDtl.setDblTaxableAmt(Double.parseDouble(decFormat.format(taxableAmt)));
							objInvProdTaxDtl.setDblWeight(objInvDtl.getDblWeight());
							listInvProdTaxDtl.add(objInvProdTaxDtl);
						
							if(!mapSubTotal.containsKey(objInvProdTaxDtl.getStrProdCode()+""+objInvDtl.getDblWeight()+""+objInvDtl.getDblQty()))
							{
								dblSubTotalAmt=dblSubTotalAmt+objInvProdTaxDtl.getDblTaxableAmt();
							}
							
							mapSubTotal.put(objInvProdTaxDtl.getStrProdCode()+""+objInvDtl.getDblWeight()+""+objInvDtl.getDblQty(), dblSubTotalAmt);
						
						}
            
						hmInvCustTaxDtl.put(key, hmInvTaxDtl);
						hmInvProdTaxDtl.put(key, listInvProdTaxDtl);
						
                        System.out.println("***********"+dblSubTotalAmt);
                        
						boolean flgProdFound = false;
						double taxtotal = 0;
					
						for (Map.Entry<String, List<clsInvoiceProdTaxDtl>> entryTaxTemp : hmInvProdTaxDtl.entrySet())
						{
							if (!flgProdFound)
							{
								List<clsInvoiceProdTaxDtl> listProdTaxDtl = entryTaxTemp.getValue();
								for (clsInvoiceProdTaxDtl objProdTaxDtl : listInvProdTaxDtl)
								{
									if (objProdTaxDtl.getStrProdCode().equals(objInvDtlModel.getStrProdCode()))
									{
										if (!objProdTaxDtl.getStrDocNo().equals("Margin"))
										{
											if (!objProdTaxDtl.getStrDocNo().equals("Disc"))
											{
												taxtotal += objProdTaxDtl.getDblValue();
												flgProdFound = true;
											}
										}
									}
								}
							}
						}

						double totalMarginAmt = marginAmt * objInvDtlModel.getDblQty();
						double totalDiscAmt = discAmt * objInvDtlModel.getDblQty();
						double assesableRateForSingleQty = (prodMRP) - (totalMarginAmt + totalDiscAmt + taxtotal);
						double assesableRate = (prodMRP * objInvDtlModel.getDblQty()) - (totalMarginAmt + totalDiscAmt + taxtotal);
						if (assesableRate < 0)
						{
							assesableRate = 0;
						}

						double assableUnitRate = (assesableRate / objInvDtlModel.getDblQty());
						assableUnitRate = Double.parseDouble(decFormat.format(assableUnitRate));

						objInvDtlModel.setDblAssValue(assableUnitRate * objInvDtlModel.getDblQty());
						// objInvDtlModel.setDblAssValue(assesableRate);
						listInvDtlModel.add(objInvDtlModel);
						// hmInvCustDtl.put(objInvDtl.getStrCustCode(),listInvDtlModel);
						hmInvCustDtl.put(key, listInvDtlModel);
					//	System.out.println(hmInvTaxDtl);
					}
				
			  }
		}	
	    	List listcust = objGlobalFunctionsService.funGetList("select a.strBAdd1,a.strBAdd2,a.strSCity,a.strSState,a.strSCountry,a.strSPin,a.strMobile from tblpartymaster a where a.strPCode='"+custCode+"'");
	    	if(listcust.size()>0)
	    	{
	    		Object[] arrCustDtl = (Object[]) listcust.get(0);
	    		objBean.setStrSAdd1(arrCustDtl[0].toString());
	    		objBean.setStrSAdd2(arrCustDtl[1].toString());
	    		objBean.setStrSCity(arrCustDtl[2].toString());
	    		objBean.setStrSState(arrCustDtl[3].toString());
	    		objBean.setStrSCountry(arrCustDtl[4].toString());
	    		objBean.setStrSPin(arrCustDtl[5].toString());
	    		objBean.setStrMobileNoForSettlement(arrCustDtl[6].toString());
	    		
	    		
	    	}
	    	
	    	
			objHDModel = new clsInvoiceHdModel();
			objHDModel.setStrUserModified(userCode);
			objHDModel.setDteLastModified(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objHDModel.setDteInvDate(strInvoiceDate);
			objHDModel.setStrAgainst("Sales Order");
			objHDModel.setStrAuthorise(objGlobalFunctions.funCheckFormAuthorization("Invoice", request));
			objHDModel.setStrInvNo("");
			objHDModel.setStrDktNo(objGlobalFunctions.funIfNull(objBean.getStrDktNo()," ",objBean.getStrDktNo()));
			objHDModel.setStrLocCode(objGlobalFunctions.funIfNull(objBean.getStrLocCode()," ",objBean.getStrLocCode()));
			objHDModel.setStrMInBy(objGlobalFunctions.funIfNull(objBean.getStrMInBy()," ",objBean.getStrMInBy()));
			objHDModel.setStrNarration(objGlobalFunctions.funIfNull(objBean.getStrNarration()," ",objBean.getStrNarration()));
			objHDModel.setStrPackNo(objBean.getStrPackNo());
			objHDModel.setStrPONo(objBean.getStrPONo());
			objHDModel.setStrReaCode(objBean.getStrReaCode());
			objHDModel.setStrSAdd1(objBean.getStrSAdd1());
			objHDModel.setStrSAdd2(objBean.getStrSAdd2());
			objHDModel.setStrSCity(objBean.getStrSCity());
			objHDModel.setStrSCtry(objBean.getStrSCtry());
			objHDModel.setStrSerialNo(objBean.getStrSerialNo());
			objHDModel.setStrSPin(objBean.getStrSPin());
			objHDModel.setStrSState(objBean.getStrSState());
			objHDModel.setStrTimeInOut(objBean.getStrTimeInOut());
			objHDModel.setStrVehNo(objBean.getStrVehNo());
			objHDModel.setStrWarraValidity(objBean.getStrWarraValidity());
			objHDModel.setStrWarrPeriod(objBean.getStrWarrPeriod());
			objHDModel.setDblSubTotalAmt(0.0);
			objHDModel.setStrSOCode(strSOCodes);
			objHDModel.setStrSettlementCode(objBean.getStrSettlementCode());
			objHDModel.setStrUserCreated(userCode);
			objHDModel.setDteCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objHDModel.setStrClientCode(clientCode);

			objHDModel.setStrMobileNo(objBean.getStrMobileNoForSettlement());
			objHDModel.setDblTotalAmt(0.0);
			objHDModel.setDblTaxAmt(0.0);

			objHDModel.setStrCurrencyCode(objBean.getStrCurrencyCode());
			
			dblCurrencyConv = objBean.getDblCurrencyConv();
			if(dblCurrencyConv ==0){
				dblCurrencyConv=1.0;
			}
			objHDModel.setDblCurrencyConv(dblCurrencyConv);
			objHDModel.setStrDeliveryNote(" ");
			objHDModel.setStrSupplierRef(" ");
			objHDModel.setStrOtherRef(" ");
			objHDModel.setStrBuyersOrderNo(" ");//objBean.getStrBuyersOrderNo()
			objHDModel.setDteBuyerOrderNoDated(strInvoiceDate);//objBean.getDteBuyerOrderNoDated()
			objHDModel.setStrDispatchDocNo(" ");//objBean.getStrDispatchDocNo()
			//Below field need to discuss
			objHDModel.setDteDispatchDocNoDated(strInvoiceDate);//objBean.getDteDispatchDocNoDated()
			objHDModel.setStrDispatchThrough(" ");//objBean.getStrDispatchThrough()
			objHDModel.setStrDestination(" ");//objBean.getStrDestination()
			objHDModel.setDblExtraCharges(0);//objBean.getDblExtraCharges()
			objHDModel.setStrLocCode(strLocCode);
			
			// /********Save Data forDetail in SO***********////

			
			for (Map.Entry<String, List<clsInvoiceModelDtl>> entrySetInvoiceModelDtl : hmInvCustDtl.entrySet())
			{
				double qty = 0.0;
				double weight = 0.0;
				List<clsInvoiceModelDtl> listInvoiceDtlModel = hmInvCustDtl.get(entrySetInvoiceModelDtl.getKey());

				if (objBean.getStrInvCode().isEmpty()) // New Entry
				{
					String[] invDate = objHDModel.getDteInvDate().split(" ");
					String[] invDate1=invDate[0].split("-");
					String dateInvoice = invDate1[2] + "-" + invDate1[1] + "-" + invDate1[0];
					String invCode ="";
				
						
						
						String transYear="A";
						List<clsCompanyMasterModel> listClsCompanyMasterModel = objSetupMasterService.funGetListCompanyMasterModel();
						if (listClsCompanyMasterModel.size() > 0) {
							clsCompanyMasterModel objCompanyMasterModel = listClsCompanyMasterModel.get(listClsCompanyMasterModel.size() - 1);
							transYear=objCompanyMasterModel.getStrYear();
						}
						
						String[] spDate = dateInvoice.split("-");
						String transMonth = objGlobalFunctions.funGetAlphabet(Integer.parseInt(spDate[1])-1);
						/*String sql = "select ifnull(max(MID(a.strInvCode,8,5)),'' ) " + " from tblinvoicehd a where MID(a.strInvCode,5,1) = '" + transYear + "'  and MID(a.strInvCode,1,2) = '" + propCode + "' and strClientCode='" + clientCode + "' "; //" + " and MID(a.strInvCode,6,1) = '" + transMonth + "' " + "
						String sqlAudit = " select ifnull(max(MID(a.strTransCode,8,5)),'' ) " + " from tblaudithd a where MID(a.strTransCode,5,1) = '" + transYear + "'  and MID(a.strTransCode,1,2) = '" + propCode + "' and strClientCode='" + clientCode + "' " + "and a.strTransType='Invoice' ;  "; //" + " and MID(a.strTransCode,6,1) = '" + transMonth + "' " + "
						*/
						String sql = "select ifnull(max(MID(a.strInvCode,8,5)),'' ) " + " from tblinvoicehd a where MID(a.strInvCode,5,1) = '" + transYear + "' " + " and MID(a.strInvCode,6,1) = '" + transMonth + "' " + " and MID(a.strInvCode,1,2) = '" + propCode + "' and strClientCode='" + clientCode + "' ";
						String sqlAudit = " select ifnull(max(MID(a.strTransCode,8,5)),'' ) " + " from tblaudithd a where MID(a.strTransCode,5,1) = '" + transYear + "' " + " and MID(a.strTransCode,6,1) = '" + transMonth + "' " + " and MID(a.strTransCode,1,2) = '" + propCode + "' and strClientCode='" + clientCode + "' " + "and a.strTransType='Invoice' ;  ";
						
						
						List listAudit = objGlobalFunctionsService.funGetListModuleWise(sqlAudit, "sql");
						long lastnoAudit;
						if (listAudit != null && !listAudit.isEmpty() && !listAudit.contains("")) {
							lastnoAudit = Integer.parseInt(listAudit.get(0).toString());

						} else {
							lastnoAudit = 0;
						}
						List list = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
						long lastnoLive;
						if (list != null && !list.isEmpty() && !list.contains("")) {
							lastnoLive = Integer.parseInt(list.get(0).toString());

						} else {
							lastnoLive = 0;
						}

						
						clsSettlementMasterModel objModel = objSttlementMasterService.funGetObject(objBean.getStrSettlementCode(), clientCode);
						if(objSetup.getStrSettlementWiseInvSer().equals("Yes"))
						{
							
						if (lastnoLive > lastnoAudit) {
							invCode = propCode + "IV" + transYear + transMonth + objModel.getStrInvSeriesChar() + String.format("%05d", lastnoLive + 1);
						} else {
							invCode = propCode + "IV" + transYear + transMonth + objModel.getStrInvSeriesChar() +String.format("%05d", lastnoAudit + 1);
						}
						
						
					}
					else{
						
						if (lastnoLive > lastnoAudit) {
							invCode = propCode + "IV" + transYear + transMonth  + String.format("%05d", lastnoLive + 1);
						} else {
							invCode = propCode + "IV" + transYear + transMonth  +String.format("%05d", lastnoAudit + 1);
						}
					}
//					else{
//					invCode = objGlobalFunctions.funGenerateDocumentCode("frmInvoice", dateInvoice, req);
//					
//					}
					objHDModel.setStrInvCode(invCode);
					objHDModel.setStrUserCreated(userCode);
					objHDModel.setDteCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
					objHDModel.setStrDulpicateFlag("N");
				}
				else // Update
				{
					objHDModel.setStrUserCreated(userCode);
					objHDModel.setDteCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
					objHDModel.setStrInvCode(objBean.getStrInvCode());
					objHDModel.setStrDulpicateFlag("Y");
				}
				String customerCode = entrySetInvoiceModelDtl.getKey().substring(0, entrySetInvoiceModelDtl.getKey().length() - 2);
				String exciseable = entrySetInvoiceModelDtl.getKey().substring(entrySetInvoiceModelDtl.getKey().length() - 1, entrySetInvoiceModelDtl.getKey().length());

				objHDModel.setStrExciseable(exciseable);
				objHDModel.setStrCustCode(customerCode);
				objHDModel.setListInvDtlModel(listInvoiceDtlModel);

				double subTotal = 0, taxAmt = 0, totalAmt = 0, totalExcisableAmt = 0;
				for (clsInvoiceModelDtl objInvItemDtl : listInvoiceDtlModel)
				{
					List list = objGlobalFunctionsService.funGetList("select strExciseable,strPickMRPForTaxCal,dblMRP from tblproductmaster " + " where strProdCode='" + objInvItemDtl.getStrProdCode() + "' ", "sql");
					// String excisable=list.get(0).toString();
					Object[] arrProdDtl = (Object[]) list.get(0);
					String excisable = arrProdDtl[0].toString();
					String pickMRP = arrProdDtl[1].toString();
					double dblMrp = Double.parseDouble(arrProdDtl[2].toString()) * dblCurrencyConv;
					String key = customerCode + "!" + excisable;
					if (pickMRP.equals("Y"))
					{
						List<clsInvoiceProdTaxDtl> listInvProdTaxDtl = hmInvProdTaxDtl.get(key);
						
						/*for (clsInvoiceProdTaxDtl objInvTaxModel : listInvProdTaxDtl)
						{
 							if (objInvItemDtl.getStrProdCode().equals(objInvTaxModel.getStrProdCode()))
							{
								if (objInvTaxModel.getStrCustCode().equals(custCode) && objInvTaxModel.getStrProdCode().equals(objInvItemDtl.getStrProdCode()) && !((objInvTaxModel.getStrDocNo().equals("Disc")) || (objInvTaxModel.getStrDocNo().equals("Margin"))))
								{
									String taxCode = objInvTaxModel.getStrDocNo();
									List listAbtment = objGlobalFunctionsService.funGetList("select a.dblAbatement,strExcisable " + " from tbltaxhd a where a.strTaxCode='" + taxCode + "' and a.strClientCode='" + clientCode + "' ", "sql");
									Object[] arrObjExc = (Object[]) listAbtment.get(0);
									double dblAbtmt = Double.parseDouble(arrObjExc[0].toString());
									String excisableTax = arrObjExc[1].toString();

									if (dblAbtmt > 0)
									{
										//totalAmt += (objInvItemDtl.getDblQty() * dblMrp) * dblAbtmt / 100;
										//totalAmt += objInvItemDtl.getDblAssValue();
										if (excisableTax.equalsIgnoreCase("Y") && excisable.equalsIgnoreCase("Y"))
										{
											totalExcisableAmt += objInvItemDtl.getDblAssValue();
										}
									}
									else
									{
										//totalAmt += objInvItemDtl.getDblAssValue();
										if (excisableTax.equalsIgnoreCase("Y") && excisable.equalsIgnoreCase("Y"))
										{
											totalExcisableAmt += objInvItemDtl.getDblAssValue();
										}
									}
								}
							}
						}*/
						
						totalAmt += objInvItemDtl.getDblAssValue();
					}
					else
					{
						// No condition Checked
						if (objInvItemDtl.getDblAssValue()==(objInvItemDtl.getDblUnitPrice() * objInvItemDtl.getDblQty()))
						{
							totalAmt += objInvItemDtl.getDblUnitPrice() * objInvItemDtl.getDblQty();
						}
						else
						{
							totalAmt += objInvItemDtl.getDblAssValue();
						}

						List<clsInvoiceProdTaxDtl> listInvProdTaxDtl = hmInvProdTaxDtl.get(key);
						for (clsInvoiceProdTaxDtl objInvTaxModel : listInvProdTaxDtl)
						{
							if (objInvItemDtl.getStrProdCode().equals(objInvTaxModel.getStrProdCode()))
							{
								if (objInvTaxModel.getStrCustCode().equals(custCode) && objInvTaxModel.getStrProdCode().equals(objInvItemDtl.getStrProdCode()) && !((objInvTaxModel.getStrDocNo().equals("Disc")) || (objInvTaxModel.getStrDocNo().equals("Margin"))))
								{
									String taxCode = objInvTaxModel.getStrDocNo();
									List listExcTax = objGlobalFunctionsService.funGetList("select a.strExcisable from tbltaxhd a where a.strTaxCode='" + taxCode + "' and a.strClientCode='" + clientCode + "' ", "sql");

									if (listExcTax.size() > 0)
									{
										String excisableTax = listExcTax.get(0).toString();
										if (excisableTax.equalsIgnoreCase("Y") && excisable.equalsIgnoreCase("Y"))
										{
											totalExcisableAmt += objInvItemDtl.getDblAssValue();
										}
									}
								}
							}
						}
					}
				}

				double excisableTaxAmt = 0;
				List<clsInvoiceTaxDtlModel> listInvoiceTaxDtl = new ArrayList<clsInvoiceTaxDtlModel>();
				Map<String, clsInvoiceTaxDtlModel> hmInvTaxDtlTemp = hmInvCustTaxDtl.get(entrySetInvoiceModelDtl.getKey());
				for (Map.Entry<String, clsInvoiceTaxDtlModel> entryTaxDtl : hmInvTaxDtlTemp.entrySet())
				{
					listInvoiceTaxDtl.add(entryTaxDtl.getValue());
					taxAmt += entryTaxDtl.getValue().getDblTaxAmt();

					String sqlTaxDtl = "select strExcisable from tbltaxhd " + " where strTaxCode='" + entryTaxDtl.getValue().getStrTaxCode() + "' ";
					List list = objGlobalFunctionsService.funGetList(sqlTaxDtl, "sql");
					if (list.size() > 0)
					{
						for (int cntExTax = 0; cntExTax < list.size(); cntExTax++)
						{
							String excisable = list.get(cntExTax).toString();
							if (excisable.equalsIgnoreCase("Y"))
							{
								excisableTaxAmt += entryTaxDtl.getValue().getDblTaxAmt();
							}
						}
					}
				}

				double grandTotal = totalAmt + taxAmt+objHDModel.getDblExtraCharges();
				subTotal = totalAmt + excisableTaxAmt;

				if (exciseable.equalsIgnoreCase("Y"))
				{
					subTotal = totalExcisableAmt + excisableTaxAmt;
					grandTotal = totalExcisableAmt + taxAmt;
					objHDModel.setDblTotalAmt(totalExcisableAmt);
				}
				else
				{
					objHDModel.setDblTotalAmt(Double.parseDouble(decFormat.format(totalAmt)));
				}
				objHDModel.setDblSubTotalAmt(Double.parseDouble(decFormat.format(dblSubTotalAmt)));
				objHDModel.setDblTaxAmt(Double.parseDouble(decFormat.format(taxAmt)));
				objHDModel.setDblGrandTotal(Double.parseDouble(decFormat.format(grandTotal)));

				List<clsInvSalesOrderDtl> listInvSODtl = new ArrayList<clsInvSalesOrderDtl>();
				String[] arrSOCodes = objHDModel.getStrSOCode().split(",");
				clsInvSalesOrderDtl objInvSODtl =null;
				for (int cn = 0; cn < arrSOCodes.length; cn++)
				{
					objInvSODtl = new clsInvSalesOrderDtl();
					objInvSODtl.setStrSOCode(arrSOCodes[cn]);
					objInvSODtl.setDteInvDate(objHDModel.getDteInvDate());
					listInvSODtl.add(objInvSODtl);
				}
				objHDModel.setListInvSalesOrderModel(listInvSODtl);
				objHDModel.setListInvTaxDtlModel(listInvoiceTaxDtl);
				objHDModel.setListInvProdTaxDtlModel(hmInvProdTaxDtl.get(entrySetInvoiceModelDtl.getKey()));
				//objHDModel.setDblDiscountAmt(objBean.getDblDiscountAmt() * dblCurrencyConv);
				objHDModel.setDblDiscountAmt(0);//For Bakery instead of discount Client is using the maragin so default disc amt zero
				objHDModel.setDblDiscount(0);
				double totalAmount = objHDModel.getDblTotalAmt() - objHDModel.getDblDiscountAmt();
				objHDModel.setDblTotalAmt(totalAmount);
				//objHDModel.setDblGrandTotal(objHDModel.getDblGrandTotal() - objHDModel.getDblDiscountAmt());
				objHDModel.setStrCloseIV("N");
				objHDModel.setDblExtraCharges(objBean.getDblExtraCharges());
				objHDModel.setStrJVNo("");
				
				//For Keeping audit of voided/deleted items....	
				//objHDModel=funSaveVoidedProductList(objHDModel,objBean,clientCode);

				objInvoiceHdService.funAddUpdateInvoiceHd(objHDModel);
				String dcCode = "";
				if (objSetup.getStrEffectOfInvoice().equals("DC"))
				{
					//dcCode = funDataSetInDeliveryChallan(objHDModel, request);
				}
				arrInvCode.append(objHDModel.getStrInvCode() + ",");
				arrDcCode.append(dcCode + ",");
			}

			if (objCompModel.getStrWebBookModule().equals("Yes"))
			{

				objHDModel.setDteInvDate(objHDModel.getDteInvDate().split(" ")[0]);
				boolean authorisationFlag = false;
				if (null != request.getSession().getAttribute("hmAuthorization"))
				{
					HashMap<String, Boolean> hmAuthorization = (HashMap) request.getSession().getAttribute("hmAuthorization");
					if (hmAuthorization.containsKey("Invoice"))
					{
						authorisationFlag = hmAuthorization.get("Invoice");
					}
				}

				if(!authorisationFlag)
				{
					String retuenVal=objJVGen.funGenrateJVforInvoice(objHDModel.getStrInvCode(), clientCode, userCode, propCode, request);
					String JVGenMessage="";
					String[] arrVal=retuenVal.split("!");
					
					boolean flgJVPosting=true;
					if(arrVal[0].equals("ERROR"))
					{
						JVGenMessage=arrVal[1];
						flgJVPosting=false;
					}
					else
					{
						objHDModel.setStrDktNo(arrVal[0]);
						objHDModel.setStrJVNo(arrVal[0]);
						

						objInvoiceHdService.funAddUpdateInvoiceHd(objHDModel);
					}
					
					request.getSession().setAttribute("JVGen", flgJVPosting);
					request.getSession().setAttribute("JVGenMessage", JVGenMessage);
					
				}

				request.getSession().setAttribute("success", true);
				request.getSession().setAttribute("successMessage", "Invoice Code : ".concat(arrInvCode.toString()));
				request.getSession().setAttribute("rptInvCode", arrInvCode);
				request.getSession().setAttribute("rptInvDate", objHDModel.getDteInvDate());
				request.getSession().setAttribute("rptDcCode", arrDcCode);

				return new ModelAndView("redirect:/frmBulkInvoice.html?saddr=" + urlHits);
			}
			else
			{
				request.getSession().setAttribute("success", true);
				request.getSession().setAttribute("successMessage", "Invoice Code : ".concat(arrInvCode.toString()));
				request.getSession().setAttribute("rptInvCode", arrInvCode);
				request.getSession().setAttribute("rptInvDate", objHDModel.getDteInvDate());
				request.getSession().setAttribute("rptDcCode", arrDcCode);
			}
		

	    }
		return new ModelAndView("redirect:/frmBulkInvoice.html?saddr=" + urlHits);

	}

	else
	{
		return new ModelAndView("frmBulkInvoice", "command", new clsInvoiceBean());
	}
		
}	

	@RequestMapping(value = "/rptBulkInvoice", method = RequestMethod.GET)
	private void funShopOrderList(@RequestParam("rptInvCode") String InvCode,@RequestParam("rptInvDate") String InvDAte, HttpServletResponse resp, HttpServletRequest req) {
		
		// there two Frmate aviable for shop order list 1st without table wise
		// and 2nd table wise which contain two table

		// frmCallShopOrderListWithoutTablewise(objBean, resp, req);
		funCallBulkInvoicePrinting(InvCode, resp, req);

	}

	
	
	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private void funCallBulkInvoicePrinting(String InvCode, HttpServletResponse resp, HttpServletRequest req) {

		try {

			Connection con = objGlobalFunctions.funGetConnection(req);
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			String strDocType="PDF";
			
			String[] arrInvCode = InvCode.split(",");
			req.getSession().removeAttribute("rptInvCode");
			
			
			List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
			
				for (int i = 0; i < arrInvCode.length; i++) {
					String invCode = arrInvCode[i].toString();
					JasperPrint jp= funCallReportInvoiceFormat8Report(invCode, resp, req);
					jprintlist.add(jp);
				}
			
				ServletOutputStream servletOutputStream = resp.getOutputStream();
				JRExporter exporter = new JRPdfExporter();
				resp.setContentType("application/pdf");
				exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
				exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
				exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
				resp.setHeader("Content-Disposition", "inline;filename=rptTaxInvoiceRetail" + userCode + ".pdf");
				exporter.exportReport();
				servletOutputStream.flush();
				servletOutputStream.close();

			/*if (jprintlist.size() > 0) {
				ServletOutputStream servletOutputStream = resp.getOutputStream();
				if (strDocType.equals("PDF")) {
					JRExporter exporter = new JRPdfExporter();
					resp.setContentType("application/pdf");
					exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
					exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
					exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					resp.setHeader("Content-Disposition", "inline;filename=BulkInvoive_" + userCode + ".pdf");
					servletOutputStream.flush();
					servletOutputStream.close();
				} else {
					JRExporter exporter = new JRXlsExporter();
					resp.setContentType("application/xlsx");
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jprintlist);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletOutputStream);
					exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					resp.setHeader("Content-Disposition", "inline;filename=BulkInvoive_" + userCode + ".xls");
					exporter.exportReport();
					servletOutputStream.flush();
					servletOutputStream.close();
				}
			} else {
				resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
				try {
					resp.getWriter().append("No Record Found");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
*/
		} catch (Exception e) {
			e.printStackTrace();

		}

	}


	public JasperPrint funCallReportInvoiceFormat8Report(String InvCode,HttpServletResponse resp, HttpServletRequest req)
	{

		// String InvCode=req.getParameter("rptInvCode").toString();

		req.getSession().removeAttribute("rptInvCode");
		//type = "pdf";
		String[] arrInvCode = InvCode.split(",");
		req.getSession().removeAttribute("rptInvCode");

		InvCode = arrInvCode[0].toString();
		Connection con = objGlobalFunctions.funGetConnection(req);
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String companyName = req.getSession().getAttribute("companyName").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String propertyCode = req.getSession().getAttribute("propertyCode").toString();
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
		if (objSetup == null)
		{
			objSetup = new clsPropertySetupModel();
		}
		String suppName = "";

		String reportName = servletContext.getRealPath("/WEB-INF/reports/webcrm/rptInvoiceFormat8GSTWithSubgroup.jrxml");
		String imagePath = servletContext.getRealPath("/resources/images/company_Logo.jpg");

		String challanDate = "";
		String PONO = "";
		String InvDate = "";
		String CustName = "";
		String dcCode = "";
		List listGSTSummary=new ArrayList();

	/*	String sqlProdDtl = " select  c.strProdName,c.strProdNameMarathi,b.dblQty,"
				// + "c.dblCostRM,"
				+ " IFNULL(b.dblPrice,0.00),c.dblMRP, IFNULL(b.dblBillRate,0.00)"
				+ " AS dblPrice, a.dteInvDate, " + " IFNULL(d.strPName,''),ifnull(e.strDCCode,''),"
				+ " ifnull(e.dteDCDate,''),ifnull(e.strPONo,''), " + " b.strProdCode,c.strHSNCode,b.dblProdDiscAmount as discAmt,IFNULL(d.strBAdd1,''),"
				+ " IFNULL(d.strBAdd2,''), " + " IFNULL(d.strBState,''),IFNULL(d.strBPin,'') ,IFNULL(d.strSAdd1,''),IFNULL(d.strSAdd2,''), " + " IFNULL(d.strSState,''),IFNULL(d.strSPin,'') "
				+ ", ifNull(strCST,''),b.dblProdDiscAmount,if(b.dblWeight=0,1,b.dblWeight),f.strSGName  "//26
				+ " from tblinvoicehd a left outer join tblinvoicedtl b on a.strInvCode=b.strInvCode   " + " left outer join tblproductmaster c  "
				+ " on b.strProdCode=c.strProdCode left outer join tblpartymaster d on a.strCustCode=d.strPCode " + " left outer join tbldeliverychallanhd e on a.strSOCode=e.strDCCode " + ""
				+ " left outer join tblsubgroupmaster f on f.strSGCode=c.strSGCode " + ""
				+ " where a.strInvCode='" + InvCode + "' and a.strClientCode='" + clientCode + "' ";
	*/
		String sqlProdDtl = " select  c.strProdName,c.strProdNameMarathi,b.dblQty,"
				// + "c.dblCostRM,"
				+ " IFNULL(b.dblPrice,0.00),c.dblMRP, IFNULL(b.dblBillRate,0.00)"
				+ " AS dblPrice, a.dteInvDate, " + " IFNULL(d.strPName,''),ifnull(e.strDCCode,''),"
				+ " ifnull(e.dteDCDate,''),ifnull(e.strPONo,''), " + " b.strProdCode,c.strHSNCode,g.dblValue as discAmt,IFNULL(d.strBAdd1,''),"
				+ " IFNULL(d.strBAdd2,''), " + " IFNULL(d.strBState,''),IFNULL(d.strBPin,'') ,IFNULL(d.strSAdd1,''),IFNULL(d.strSAdd2,''), " + " IFNULL(d.strSState,''),IFNULL(d.strSPin,'') "
				+ ",IFNULL(d.strGSTNo,''),b.dblProdDiscAmount,b.dblWeight,f.strSGName,IFNULL(d.strEmail,''), IFNULL(d.strMobile,''),f.intSortingNo  "//29
				+ " from tblinvoicehd a left outer join tblinvoicedtl b on a.strInvCode=b.strInvCode   " + " left outer join tblproductmaster c  "
				+ " on b.strProdCode=c.strProdCode left outer join tblpartymaster d on a.strCustCode=d.strPCode " + " left outer join tbldeliverychallanhd e on a.strSOCode=e.strDCCode " + ""
				+ " left outer join tblsubgroupmaster f on f.strSGCode=c.strSGCode " + ""
				+ " left outer join tblinvprodtaxdtl  g on a.strInvCode=g.strInvCode and a.strCustCode=g.strCustCode  and b.dblWeight=g.dblWeight  " + " and b.strProdCode=g.strProdCode and g.strDocNo='Disc'   " + ""
				+ " where a.strInvCode='" + InvCode + "' and a.strClientCode='" + clientCode + "'"
				+ " ORDER BY f.intSortingNo,f.strSGName,c.strProdName; ";
		String bAddress = "";
		String bState = "";
		String bPin = "";
		String sAddress = "";
		String sState = "";
		String sPin = "";
		String custGSTNo = "";
		String custEmailID="",custMobileNo="";
		double totalInvoiceValue = 0.0;
		List listProdDtl = objGlobalFunctionsService.funGetDataList(sqlProdDtl, "sql");
		List<clsInvoiceDtlBean> dataList = new ArrayList<clsInvoiceDtlBean>();
		Map<Double, Double> hmCGSTCalculateTax = new HashMap<Double, Double>();
		Map<Double, Double> hmSGSTCalculateTax = new HashMap<Double, Double>();
		  Map<String,clsInvoiceDtlBean> mapGSTSummary=new HashMap<>();
		if (listProdDtl.size() > 0)
		{
			for (int i = 0; i < listProdDtl.size(); i++)
			{
				Object[] obj = (Object[]) listProdDtl.get(i);
				clsInvoiceDtlBean objDtlBean = new clsInvoiceDtlBean();
				objDtlBean.setStrProdName(obj[0].toString());
				
				objDtlBean.setStrProdNamemarthi(obj[1].toString());
				objDtlBean.setDblQty(Double.parseDouble(obj[2].toString()));
				objDtlBean.setDblCostRM(Double.parseDouble(obj[3].toString()));
				if(Double.parseDouble(obj[24].toString())>0){
					objDtlBean.setDblCostRM(Double.parseDouble(obj[3].toString())*Double.parseDouble(obj[24].toString()));
				}
				
				objDtlBean.setDblMRP(Double.parseDouble(obj[4].toString()));
				objDtlBean.setDblPrice(Double.parseDouble(obj[5].toString()));
				InvDate = objGlobalFunctions.funGetDate("dd-MM-yyyy", obj[6].toString());
				CustName = obj[7].toString();
				challanDate = obj[9].toString();
				PONO = obj[10].toString();
				dcCode = obj[8].toString();
				objDtlBean.setStrHSN(obj[12].toString());
				objDtlBean.setStrProdCode(obj[11].toString());
			
				objDtlBean.setStrSubGroupName(obj[25].toString());
				// objDtlBean.setDblDisAmt(Double.parseDouble(obj[13].toString())*Double.parseDouble(obj[2].toString()));
				bAddress = obj[14].toString() + " " + obj[15].toString();
				bState = obj[16].toString();
				bPin = obj[17].toString();
				objDtlBean.setDblDisAmt(Double.parseDouble(obj[13].toString()));
				sAddress = obj[18].toString() + " " + obj[19].toString();
				sState = obj[20].toString();
				sPin = obj[21].toString();
				custGSTNo = obj[22].toString();
				custEmailID=obj[26].toString();
				custMobileNo=obj[27].toString();
				objDtlBean.setDblWeight(Double.parseDouble(obj[24].toString()));
				double qty=Double.parseDouble(obj[2].toString());
				double rate=Double.parseDouble(obj[5].toString());
				double subTotal=qty*rate;
				double discAmt=Double.parseDouble(obj[23].toString());								
				double netTotal=subTotal-discAmt;
				totalInvoiceValue = totalInvoiceValue + netTotal;

				String sqlQuery = " select b.strTaxCode,b.dblPercent,a.dblValue,b.strShortName,a.dblTaxableAmt " + " "
						+ " from tblinvprodtaxdtl a,tbltaxhd b " + ""
						+ " where a.strDocNo=b.strTaxCode and a.strInvCode='" + InvCode + "' " + " and  a.strProdCode='" + obj[11].toString() + "' and a.strClientCode='" + clientCode + "'  and a.dblWeight='"+obj[24].toString()+"' ";

				List listProdGST = objGlobalFunctionsService.funGetDataList(sqlQuery, "sql");

				if (listProdGST.size() > 0)
				{
					for (int j = 0; j < listProdGST.size(); j++)
					{
						double cGStAmt = 0.0;
						double sGStAmt = 0.0;
						Object[] objGST = (Object[]) listProdGST.get(j);
						if (objGST[3].toString().equalsIgnoreCase("CGST"))
						{
							objDtlBean.setDblCGSTPer(Double.parseDouble(objGST[1].toString()));
							objDtlBean.setDblCGSTAmt(Double.parseDouble(objGST[2].toString()));
							objDtlBean.setDblTaxableAmt(Double.parseDouble(objGST[4].toString()));
							
						//	totalInvoiceValue = totalInvoiceValue + Double.parseDouble(objGST[2].toString());
						}
						else if (objGST[3].toString().equalsIgnoreCase("SGST"))
						{
							objDtlBean.setDblSGSTPer(Double.parseDouble(objGST[1].toString()));
							objDtlBean.setDblSGSTAmt(Double.parseDouble(objGST[2].toString()));
							objDtlBean.setDblTaxableAmt(Double.parseDouble(objGST[4].toString()));
						//	totalInvoiceValue = totalInvoiceValue + Double.parseDouble(objGST[2].toString());
						}
						else
						{
							objDtlBean.setDblNonGSTTaxPer(Double.parseDouble(objGST[1].toString()));
							objDtlBean.setDblNonGSTTaxAmt(Double.parseDouble(objGST[2].toString()));
							objDtlBean.setDblTaxableAmt(Double.parseDouble(objGST[4].toString()));
						//	totalInvoiceValue = totalInvoiceValue + Double.parseDouble(objGST[2].toString());
						}
					}
				}
				DecimalFormat decFormat = new DecimalFormat("#.##");
				objDtlBean.setDblTotalAmt(Double.parseDouble(decFormat.format(objDtlBean.getDblTaxableAmt()+objDtlBean.getDblSGSTAmt()+objDtlBean.getDblCGSTAmt())));
				dataList.add(objDtlBean);

			}
			
			String sqlGSTSummary="select a.strDocNo,sum(a.dblTaxableAmt),sum(a.dblValue),b.strHSNCode,c.strTaxDesc,c.dblPercent,c.dblAbatement,c.strShortName"
					+ " from tblinvprodtaxdtl a,tblproductmaster b ,tbltaxhd c "
					+ " where a.strProdCode=b.strProdCode  and a.strDocNo=c.strTaxCode and a.strInvCode='" + InvCode + "' "
					+ " group by b.strHSNCode,a.strDocNo";
		    List listProdGST = objGlobalFunctionsService.funGetDataList(sqlGSTSummary, "sql");
	      
			if (listProdGST.size() > 0)
			{
				clsInvoiceDtlBean objDtlBean = new clsInvoiceDtlBean();
				for (int j = 0; j < listProdGST.size(); j++)
				{
					Object[] objGST = (Object[]) listProdGST.get(j);
					
					if(mapGSTSummary.containsKey(objGST[3].toString()+""+objGST[1].toString())){
						objDtlBean =mapGSTSummary.get(objGST[3].toString()+""+objGST[1].toString());
						objDtlBean.setDblGSTAmt(objDtlBean.getDblGSTAmt()+Double.parseDouble(objGST[2].toString()));
						if (objGST[7].toString().equalsIgnoreCase("SGST"))
						 {
							 objDtlBean.setDblSGSTPer(Double.parseDouble(objGST[5].toString()));
							 objDtlBean.setDblSGSTAmt(Double.parseDouble(objGST[2].toString()));	
						 }
						if (objGST[7].toString().equalsIgnoreCase("CGST"))
						 {
							 objDtlBean.setDblCGSTPer(Double.parseDouble(objGST[5].toString()));
							 objDtlBean.setDblCGSTAmt(Double.parseDouble(objGST[2].toString()));
						 }
						
						 
					}else{
						 objDtlBean = new clsInvoiceDtlBean();
						 objDtlBean.setStrHSN(objGST[3].toString());
						 objDtlBean.setDblTaxableAmt(Double.parseDouble(objGST[1].toString()));
						 objDtlBean.setDblGSTPer(Double.parseDouble(objGST[6].toString()));
						 objDtlBean.setDblGSTAmt(Double.parseDouble(objGST[2].toString()));
						 if (objGST[7].toString().equalsIgnoreCase("CGST"))
						 {
							 objDtlBean.setDblCGSTPer(Double.parseDouble(objGST[5].toString()));
							 objDtlBean.setDblCGSTAmt(Double.parseDouble(objGST[2].toString()));
							 	 
						 }
						 if (objGST[7].toString().equalsIgnoreCase("SGST"))
						 {
							 objDtlBean.setDblSGSTPer(Double.parseDouble(objGST[5].toString()));
							 objDtlBean.setDblSGSTAmt(Double.parseDouble(objGST[2].toString()));	
						 }
						 
						// objDtlBean.setDblTotalAmt(Double.parseDouble(objGST[1].toString()));
						 
						 mapGSTSummary.put(objGST[3].toString()+""+objGST[1].toString(), objDtlBean);
					}
					 
					 	 
				}
			}
			
		}
		listGSTSummary.clear();
		for(Map.Entry<String,clsInvoiceDtlBean> entry : mapGSTSummary.entrySet()){
			clsInvoiceDtlBean objDtlBean =entry.getValue();
			objDtlBean.setDblTotalAmt(objDtlBean.getDblTaxableAmt()+objDtlBean.getDblGSTAmt());
			listGSTSummary.add(entry.getValue());
			
		}
		JasperPrint jp = null;
		try
		{
			
			String shortName = " Paisa";
			String currCode = req.getSession().getAttribute("currencyCode").toString();
			clsCurrencyMasterModel objCurrModel = objCurrencyMasterService.funGetCurrencyMaster(currCode, clientCode);
			if (objCurrModel != null)
			{
				shortName = objCurrModel.getStrShortName();
			}

			clsNumberToWords obj1 = new clsNumberToWords();
			//DecimalFormat decFormat = new DecimalFormat("#");
			String totalInvoiceValueInWords = obj1.getNumberInWorld(totalInvoiceValue, shortName);

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
			hm.put("InvCode", InvCode);
			hm.put("InvDate", InvDate);
			hm.put("challanDate", challanDate);
			hm.put("PONO", PONO);
			hm.put("CustName", CustName);
			hm.put("PODate", challanDate);
			hm.put("dcCode", dcCode);
			hm.put("dataList", dataList);
			hm.put("bAddress", bAddress);
			hm.put("bState", bState);
			hm.put("bPin", bPin);
			hm.put("sAddress", sAddress);
			hm.put("sState", sState);
			hm.put("sPin", sPin);
			hm.put("totalInvoiceValueInWords", totalInvoiceValueInWords);
			hm.put("totalInvoiceValue", totalInvoiceValue);
			hm.put("strGSTNo.", objSetup.getStrCST());
			hm.put("custGSTNo", custGSTNo);
			hm.put("custMobileNo", custMobileNo);
			hm.put("custEmailID", custEmailID);
			hm.put("listGSTSummary", listGSTSummary);

			// ////////////

			JasperDesign jd = JRXmlLoader.load(reportName);
			JasperReport jr = JasperCompileManager.compileReport(jd);
			JRDataSource beanCollectionDataSource = new JRBeanCollectionDataSource(dataList);
			jp = JasperFillManager.fillReport(jr, hm, beanCollectionDataSource);
			
	

			//ServletOutputStream servletOutputStream = resp.getOutputStream();
		//	List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
			//jprintlist.add(jp);


				/*JRExporter exporter = new JRPdfExporter();
				resp.setContentType("application/pdf");
				exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
				exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
				exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
				resp.setHeader("Content-Disposition", "inline;filename=rptTaxInvoiceRetail" + userCode + ".pdf");
				exporter.exportReport();
				servletOutputStream.flush();
				servletOutputStream.close();*/

		
			// ///////////////

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}finally{
			return jp;
		}
	

	}
	
	
	
	public String funDateConversion(String dteDate)
	{
		String fd = dteDate.split("-")[2];
		String fm = dteDate.split("-")[1];
		String fy = dteDate.split("-")[0];
		String dteddmmyy = fd + "-" + fm + "-" + fy;
		return dteddmmyy;
	
		
	}

	private String funDataSetInDeliveryChallan(clsInvoiceHdModel objInvHDModel, HttpServletRequest req)
	{

		boolean flg = true;
		clsDeliveryChallanHdModel objDcHdModel = new clsDeliveryChallanHdModel();
		long lastNo = 0;
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String propCode = req.getSession().getAttribute("propertyCode").toString();
		String startDate = req.getSession().getAttribute("startDate").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String sqlFetchDc = "select strDCCode from tbldeliverychallanhd where strSoCode='" + objInvHDModel.getStrInvCode() + "' and strClientCode='" + objInvHDModel.getStrClientCode() + "' ";
		List listFetchDc = objGlobalFunctionsService.funGetList(sqlFetchDc, "sql");

		if (!listFetchDc.isEmpty())
		{
			Object objDc = listFetchDc.get(0);

			objDcHdModel = new clsDeliveryChallanHdModel(new clsDeliveryChallanHdModel_ID(objDc.toString(), clientCode));
		}
		else
		{

			lastNo = objGlobalFunctionsService.funGetLastNo("tbldeliverychallanhd", "DCCode", "intId", clientCode);
			String year = objGlobalFunctions.funGetSplitedDate(startDate)[2];
			String cd = objGlobalFunctions.funGetTransactionCode("DC", propCode, year);
			String strDCCode = cd + String.format("%06d", lastNo);
			objDcHdModel.setStrDCCode(strDCCode);
			objDcHdModel.setStrUserCreated(userCode);
			objDcHdModel.setDteCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objDcHdModel.setIntid(lastNo);
		}
		objDcHdModel.setStrSOCode(objInvHDModel.getStrInvCode());
		objDcHdModel.setDteDCDate(objInvHDModel.getDteInvDate());
		objDcHdModel.setStrAgainst(objInvHDModel.getStrAgainst());
		objDcHdModel.setStrPONo(objInvHDModel.getStrPONo());
		objDcHdModel.setStrNarration(objInvHDModel.getStrNarration());
		objDcHdModel.setStrPackNo(objInvHDModel.getStrPackNo());
		objDcHdModel.setStrLocCode(objInvHDModel.getStrLocCode());
		objDcHdModel.setStrVehNo(objInvHDModel.getStrVehNo());
		objDcHdModel.setStrMInBy(objInvHDModel.getStrMInBy());
		objDcHdModel.setStrTimeInOut(objInvHDModel.getStrTimeInOut());
		objDcHdModel.setStrUserModified(objInvHDModel.getStrUserModified());
		objDcHdModel.setDteLastModified(objInvHDModel.getDteLastModified());
		objDcHdModel.setStrAuthorise(objGlobalFunctions.funCheckFormAuthorization("Invoice", req));
		objDcHdModel.setStrDktNo(objInvHDModel.getStrDktNo());
		objDcHdModel.setStrSAdd1(objInvHDModel.getStrSAdd1());
		objDcHdModel.setStrSAdd2(objInvHDModel.getStrSAdd2());
		objDcHdModel.setStrSCity(objInvHDModel.getStrSCity());
		objDcHdModel.setStrSCtry(objInvHDModel.getStrSCtry());
		objDcHdModel.setStrSerialNo(objInvHDModel.getStrSerialNo());
		objDcHdModel.setStrSPin(objInvHDModel.getStrSPin());
		objDcHdModel.setStrSState(objInvHDModel.getStrSState());
		objDcHdModel.setStrReaCode(objInvHDModel.getStrReaCode());
		objDcHdModel.setStrWarraValidity(objInvHDModel.getStrWarraValidity());
		objDcHdModel.setStrWarrPeriod(objInvHDModel.getStrWarrPeriod());
		objDcHdModel.setStrClientCode(objInvHDModel.getStrClientCode());
		objDcHdModel.setStrCustCode(objInvHDModel.getStrCustCode());
		objDcHdModel.setStrSettlementCode(objInvHDModel.getStrSettlementCode());
		objDcHdModel.setStrCloseDC("N");
		objDcHdModel.setStrDCNo("");
		objDeliveryChallanHdService.funAddUpdateDeliveryChallanHd(objDcHdModel);
		clsInvoiceModelDtl objInvDtlModel = null;
		List<clsInvoiceModelDtl> listInvDtl = objInvHDModel.getListInvDtlModel();
		if (!listInvDtl.isEmpty())
			objDeliveryChallanHdService.funDeleteDtl(objDcHdModel.getStrDCCode(), clientCode);
		{
			for (int i = 0; i < listInvDtl.size(); i++)
			{
				objInvDtlModel = listInvDtl.get(i);
				clsDeliveryChallanModelDtl objDcDtlModel = new clsDeliveryChallanModelDtl();
				objDcDtlModel.setStrDCCode(objDcHdModel.getStrDCCode());
				objDcDtlModel.setStrProdCode(objInvDtlModel.getStrProdCode());
				objDcDtlModel.setDblQty(objInvDtlModel.getDblQty());
				objDcDtlModel.setDblPrice(objInvDtlModel.getDblUnitPrice());
				objDcDtlModel.setDblWeight(objInvDtlModel.getDblWeight());
				objDcDtlModel.setStrProdType(objInvDtlModel.getStrProdType());
				objDcDtlModel.setStrPktNo(objInvDtlModel.getStrPktNo());
				objDcDtlModel.setStrRemarks(objInvDtlModel.getStrRemarks());
				objDcDtlModel.setStrInvoiceable(objInvDtlModel.getStrInvoiceable());
				objDcDtlModel.setStrSerialNo(objInvDtlModel.getStrInvoiceable());
				objDcDtlModel.setIntindex(objInvDtlModel.getIntindex());
				objDcDtlModel.setStrClientCode(objInvHDModel.getStrClientCode());

				objDeliveryChallanHdService.funAddUpdateDeliveryChallanDtl(objDcDtlModel);
			}
		}

		String sqlUpdateDC = "update tblinvsalesorderdtl set strDCCode='" + objDcHdModel.getStrDCCode() + "' where strInvCode='" + objInvHDModel.getStrInvCode() + "' ";
		objGlobalFunctionsService.funUpdateAllModule(sqlUpdateDC, "sql");
		return objDcHdModel.getStrDCCode();
	}

}
