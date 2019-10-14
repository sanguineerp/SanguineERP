package com.sanguine.webbanquets.controller;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
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

import com.ibm.icu.math.BigDecimal;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webbanquets.bean.clsBookingFlashBean;
import com.sanguine.webbanquets.bean.clsFunctionProspectusBean;
import com.sanguine.webpms.bean.clsPMSSalesFlashBean;

@Controller
public class clsBookingFlashController 
{
	@Autowired
	private clsGlobalFunctionsService objGlobalService;
	@Autowired
	private clsSetupMasterService objSetupMasterService;
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private clsFunctionProspectusController objFunctionProspectusController;
	
	@RequestMapping(value = "/frmBookingFlash", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model,HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBookingFlash_1", "command",new clsBookingFlashBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBookingFlash", "command",new clsBookingFlashBean());
		} else {
			return null;
		}
	}
	
	
	@RequestMapping(value = "/loadBookingDetail", method = RequestMethod.GET)
	public @ResponseBody List funBookingDtl(HttpServletRequest request) {
		
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
	    String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		String strType=request.getParameter("reportType").toString();
		if(strType.equalsIgnoreCase("Confirm Booking"))
		{
			strType="Confirm";
		}
		else if(strType.equalsIgnoreCase("Waiting List"))
		{
			strType="Waiting";
		}
		else if(strType.equalsIgnoreCase("Provisional List"))
		{
			strType="Provisional";
		}
		else if(strType.equalsIgnoreCase("Cancel List"))
		{
			strType="Cancel";
		}
		
		BigDecimal dblTotalValue = new BigDecimal(0);
		
		List<clsBookingFlashBean> listofBookingDtl = new ArrayList<clsBookingFlashBean>();
		List listofBookingTotal = new ArrayList<>();
        String sql="select a.strBookingNo,a.strBookingStatus,DATE_FORMAT( DATE(a.dteBookingDate),'%d-%m-%Y') as dte,a.tmeFromTime,a.tmeToTime,a.strAreaCode, "
                  +" a.strFunctionCode,a.dblGrandTotal "
                  +" from tblbqbookinghd  a "
                  +" where  DATE(a.dteBookingDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' and a.strClientCode='"+strClientCode+"'  and a.strBookingStatus='"+strType+"';"; 
        
        List listBookingDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listBookingDtl.isEmpty()) {
			for (int i = 0; i < listBookingDtl.size(); i++) {
			    Object[] arr2=(Object[]) listBookingDtl.get(i);
			    clsBookingFlashBean objBean = new clsBookingFlashBean();
				
			    objBean.setStrBookingNo(arr2[0].toString());
			    objBean.setStrBookingStatus(arr2[1].toString());
			    objBean.setDteBookingDate(arr2[2].toString());
			    objBean.setDteBookingDate(arr2[2].toString());
			    objBean.setTmeFromTime(arr2[3].toString());
			    objBean.setTmeToTime(arr2[4].toString());
			    objBean.setStrAreaCode(arr2[5].toString());
			    objBean.setStrFunctionCode(arr2[6].toString());
			    objBean.setDblAmt(Double.parseDouble(arr2[7].toString()));
			    listofBookingDtl.add(objBean);
				dblTotalValue = new BigDecimal(Double.parseDouble(arr2[7].toString())).add(dblTotalValue);
			}
		}
		listofBookingTotal.add(listofBookingDtl);
		listofBookingTotal.add(dblTotalValue);
        
        
		return listofBookingTotal;
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/exportBookingFlashDtl", method = RequestMethod.GET)
	private ModelAndView funExportBookingFlashDtl(HttpServletRequest request)
	{    
		String strClientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List retList = new ArrayList();
		List detailList = new ArrayList();
		List headerList = new ArrayList();
		List totalsList = new ArrayList();
		totalsList.add("Total");
		totalsList.add("");
		totalsList.add("");	
		totalsList.add("");
		totalsList.add("");	
		totalsList.add("");	
		totalsList.add("");
		String fromDate = request.getParameter("frmDte").toString();
		String[] arr = fromDate.split("-");
		String fromDte = arr[2] + "-" + arr[1] + "-" + arr[0];
		
		String toDate = request.getParameter("toDte").toString();
		String[] arr1 = toDate.split("-");
		String toDte = arr1[2] + "-" + arr1[1] + "-" + arr1[0];
		
		String strType=request.getParameter("reportType").toString();
		if(strType.equalsIgnoreCase("Confirm Booking"))
		{
			strType="Confirm";
		}
		else if(strType.equalsIgnoreCase("Waiting List"))
		{
			strType="Waiting";
		}
		else if(strType.equalsIgnoreCase("Provisional List"))
		{
			strType="Provisional";
		}
		else if(strType.equalsIgnoreCase("Cancel List"))
		{
			strType="Cancel";
		}
		
		
		BigDecimal dblTotalValue = new BigDecimal(0);
		DecimalFormat df = new DecimalFormat("#.##");
		String sql="select a.strBookingNo,a.strBookingStatus,DATE_FORMAT( DATE(a.dteBookingDate),'%d-%m-%Y') as dte,a.tmeFromTime,a.tmeToTime,a.strAreaCode, "
                +" a.strFunctionCode,a.dblGrandTotal "
                +" from tblbqbookinghd  a "
                +" where  DATE(a.dteBookingDate) BETWEEN '"+fromDte+"' AND '"+toDte+"' and a.strClientCode='"+strClientCode+"'  and a.strBookingStatus='"+strType+"';"; 
		List listCheckOutDtl = objGlobalService.funGetListModuleWise(sql, "sql");
		if (!listCheckOutDtl.isEmpty()) {
			for (int i = 0; i < listCheckOutDtl.size(); i++) {
			    Object[] arr2=(Object[]) listCheckOutDtl.get(i);
			    List DataList = new ArrayList<>();
			    DataList.add(arr2[0].toString());
			    DataList.add(arr2[1].toString());
			    DataList.add(arr2[2].toString());
			    DataList.add(arr2[3].toString());
			    DataList.add(arr2[4].toString());
			    DataList.add(arr2[5].toString());
			    DataList.add(arr2[6].toString());
			    DataList.add(arr2[7].toString());
			
			    dblTotalValue = new BigDecimal(df.format(Double.parseDouble(arr2[7].toString()))).add(dblTotalValue);
				detailList.add(DataList);
				

			}
		}
		totalsList.add(dblTotalValue);
		df.format(dblTotalValue);
		retList.add("BookingFlashData_" + fromDte + "to" + toDte + "_" + userCode);
		List titleData = new ArrayList<>();
		titleData.add("Confirm Booking Report");
		retList.add(titleData);
			
		List filterData = new ArrayList<>();
		filterData.add("From Date");
		filterData.add(fromDate);
		filterData.add("To Date");
	    filterData.add(toDate);
	    retList.add(filterData);  
	    
		headerList.add("Booking No");
		headerList.add("Booking Status");
		headerList.add("Booking Date");
		headerList.add("From Time");
		headerList.add("To Time");
		headerList.add("Area Code");
		headerList.add("Function Code");
		headerList.add("Amount");
		Object[] objHeader = (Object[]) headerList.toArray();

		String[] ExcelHeader = new String[objHeader.length];
		for (int k = 0; k < objHeader.length; k++) {
			ExcelHeader[k] = objHeader[k].toString();
		}
		List blankList = new ArrayList();
	    detailList.add(blankList);// Blank Row at Bottom
	    detailList.add(totalsList);
		
		retList.add(ExcelHeader);
		retList.add(detailList);
		
		return new ModelAndView("excelViewFromToDteReportName", "listFromToDateReportName", retList);
	}
	
	
	@RequestMapping(value = "/rptOpenFunctionProspectus", method = RequestMethod.GET)
	public void funGenerateFunctionProspectus(@RequestParam("bookingNo")String strBookingNo, HttpServletRequest req, HttpServletResponse resp) 
	{
		try 
		{
			objFunctionProspectusController.funGenarateFunctionProspectus(strBookingNo, req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
