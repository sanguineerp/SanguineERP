package com.sanguine.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.util.clsReportBean;
import com.sanguine.webbanquets.bean.clsBookingFlashBean;

@Controller
public class clsSupplierTaxWiseGRNReportController
{
	
	@Autowired
	private clsGlobalFunctions objGlobalFunctions;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	
	@RequestMapping(value = "/frmSupplierTaxWiseGRNReport", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model,HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmSupplierTaxWiseGRNReport_1", "command",new clsReportBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmSupplierTaxWiseGRNReport", "command",new clsReportBean());
		} else {
			return null;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/rptfrmSupplierTaxWiseGRNReport", method = RequestMethod.POST)
	private ModelAndView funSupplierTaxGRNReport(@ModelAttribute("command") clsReportBean objBean,HttpServletResponse resp, HttpServletRequest req) 
	{
		String fromDate = objGlobalFunctions.funGetDate("yyyy-MM-dd",objBean.getDtFromDate());
		String toDate = objGlobalFunctions.funGetDate("yyyy-MM-dd",objBean.getDtToDate());
		String propertyCode = req.getSession().getAttribute("propertyCode").toString();
        String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		HashMap<String,Integer> HmTaxNameTaxIndex = new HashMap<String,Integer>();
		
		String dateTime[] = objGlobalFunctions.funGetCurrentDateTime("dd-MM-yyyy").split(" ");
		List footer = new ArrayList<>();
		
		String suppCode=objBean.getStrSuppCode();
		String suppName=" ";
		double subTotal=0.0;
		String taxDesc=" ";
		//double taxAmt=0.0;
		double grossTotal=0.0;
		
	
		
		List exportList = new ArrayList();
		exportList.add("rptSupplierTaxWiseReport_" + fromDate + "to" + toDate + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("Supplier Tax Wise GRN Report");
		exportList.add(titleData);
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
		filterData.add(toDate);
        exportList.add(filterData);
        
        String sqltaxheader="SELECT DISTINCT(d.strTaxDesc) FROM tblgrnhd a,tblgrntaxdtl c,tbltaxhd d "
                           +" WHERE a.strGRNCode=c.strGRNCode AND c.strTaxCode=d.strTaxCode and date(a.dtGRNDate) between '"+fromDate+"' and '"+toDate+"' "
                           +" GROUP BY c.strTaxCode,a.strGRNCode ORDER BY a.strGRNCode; ";
        List listheader = objGlobalFunctionsService.funGetList(sqltaxheader.toString(), "sql");
		for (int k = 0; k < listheader.size(); k++)
		{
	        String taxName= listheader.get(k).toString();
	        HmTaxNameTaxIndex.put(taxName, k);
		}
		List headerList = new ArrayList();
		headerList.add("Supplier Code");
		headerList.add("Supplier Name");
		headerList.add("Net Total");
		for(HashMap.Entry<String,Integer> taxheader :HmTaxNameTaxIndex.entrySet())
		{
			String taxName=taxheader.getKey();
			headerList.add(taxName);
		}
		headerList.add("Gross Amount");
		Object[] objHeader = (Object[]) headerList.toArray();
		String[] ExcelHeader = new String[objHeader.length];
		
		for (int k = 0; k < objHeader.length; k++) {
		
			ExcelHeader[k] = objHeader[k].toString();
			
		}
		exportList.add(ExcelHeader);
	
		
		
		List SuppTaxList = new ArrayList();
		DecimalFormat df = objGlobalFunctions.funGetDecimatFormat(req); //new DecimalFormat("#0.00");
		String sql=" ";
		if(objBean.getStrSuppCode()!="")
		{
			sql="select a.strSuppCode, b.strPName,sum(a.dblSubTotal) "
	                   +" from tblgrnhd a,tblpartymaster b "
	                   +" where a.strSuppCode=b.strPCode and date(a.dtGRNDate) between '"+fromDate+"' and '"+toDate+"' and  a.strSuppCode='"+suppCode+"' group by b.strPCode ";
		}
		else{
	     sql="select a.strSuppCode, b.strPName,sum(a.dblSubTotal) "
                   +" from tblgrnhd a,tblpartymaster b "
                   +" where a.strSuppCode=b.strPCode and date(a.dtGRNDate) between '"+fromDate+"' and '"+toDate+"'  group by b.strPCode ";
		}
		List list = objGlobalFunctionsService.funGetList(sql.toString(), "sql");
		if(null!=list && list.size()>0)
		{
			
		  for (int i = 0; i < list.size(); i++)
		   {
			    double taxAmt=0.0;
				Object[] ob = (Object[]) list.get(i);
				suppCode=ob[0].toString();
				suppName=ob[1].toString();
				subTotal=Double.parseDouble(ob[2].toString());
			    
				List dataList = new ArrayList<>();
				dataList.add(suppCode); // SuppCode
				dataList.add(suppName); // SuppName
				dataList.add(subTotal); // SubTotal
				
				for(int m=0;m<HmTaxNameTaxIndex.size();m++) 
				{
					dataList.add(m+3,0);
				}
				
			    
		String sql1="select b.strPName,d.strTaxDesc,c.strTaxCode ,sum(c.strTaxAmt) "
                   +" from tblgrnhd a,tblpartymaster b,tblgrntaxdtl c,tbltaxhd d "
                   +" where a.strSuppCode='"+suppCode+"' and a.strSuppCode=b.strPCode  and a.strGRNCode=c.strGRNCode and c.strTaxCode=d.strTaxCode "
                   +" group by c.strTaxCode"
                   +" order by a.strGRNCode ";
		
		List listTax = objGlobalFunctionsService.funGetList(sql1.toString(), "sql");
		
				for (int j = 0; j < listTax.size(); j++)
					{
					    
						Object[] obj = (Object[]) listTax.get(j);
						taxDesc = obj[1].toString();
						taxAmt +=Double.parseDouble(obj[3].toString());
						int index=headerList.indexOf(taxDesc);
						dataList.remove(index);
					    dataList.add(index, Double.parseDouble(obj[3].toString()));
						
					 }
	        
				grossTotal=subTotal+taxAmt;
				dataList.add(grossTotal); // GrossTotal
				SuppTaxList.add(dataList);

             }
		}
		List blank = new ArrayList<>();
		blank.add("");
		SuppTaxList.add(blank);
		footer.add("Created on :" +dateTime[0]);
		footer.add("AT :" +dateTime[1]);
		footer.add("By :" +userCode);
		SuppTaxList.add(footer);
		
		
		exportList.add(SuppTaxList);

		return new ModelAndView("excelViewFromDateTodateWithReportName","listFromDateTodateWithReportName", exportList);
		
	
		
	}
}
