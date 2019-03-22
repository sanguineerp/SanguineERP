package com.sanguine.controller;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import com.sanguine.bean.clsMISBean;
import com.sanguine.bean.clsStockFlashBean;
import com.sanguine.model.clsCompanyMasterModel;
import com.sanguine.model.clsLocationMasterModel;
import com.sanguine.model.clsPropertyMaster;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsLocationMasterService;
import com.sanguine.service.clsPropertyMasterService;
import com.sanguine.service.clsSetupMasterService;

@Controller
public class clsMISMonthProductWiseFlash
{

	@Autowired
	private clsGlobalFunctionsService objGlobalService;

	@Autowired
	clsGlobalFunctions objGlobal;
	@Autowired
	private clsLocationMasterService objLocationMasterService;
	@Autowired
	private clsPropertyMasterService objPropertyMasterService;

	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@RequestMapping(value = "/frmMISMonthProdWiseStkFlash", method = RequestMethod.GET)
	public ModelAndView funOpenMISForm(@ModelAttribute("command") clsStockFlashBean objPropBean, BindingResult result, HttpServletRequest request)
	{
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		request.getSession().setAttribute("formName", "frmMIS");
		clsMISBean bean = new clsMISBean();
		String urlHits = "1";
		try
		{
			urlHits = request.getParameter("saddr").toString();
		}
		catch (NullPointerException e)
		{
			urlHits = "1";
		}
		ModelAndView objModelView = null;
		if ("2".equalsIgnoreCase(urlHits))
		{
			objModelView = new ModelAndView("frmMISMonthProdWiseStkFlash_1");
		}
		else if ("1".equalsIgnoreCase(urlHits))
		{
			objModelView = new ModelAndView("frmMISMonthProdWiseStkFlash");
		}
		Map<String, String> mapProperty = objGlobalService.funGetPropertyList(clientCode);
		if (mapProperty.isEmpty())
		{
			mapProperty.put("", "");
		}
		String propertyCode = request.getSession().getAttribute("propertyCode").toString();
		String locationCode = request.getSession().getAttribute("locationCode").toString();
		
		Map<String,String>mapReportType=new HashMap<>();
		mapReportType.put("Excel","Excel");
		mapReportType.put("Pdf","Pdf");

		objModelView.addObject("listProperty", mapProperty);
		objModelView.addObject("LoggedInProp", propertyCode);
		objModelView.addObject("LoggedInLoc", locationCode);
		objModelView.addObject("mapReportType", mapReportType);

		HashMap<String, String> mapLocation = (HashMap<String, String>) objGlobalService.funGetLocationList(propertyCode, clientCode);
		if (mapLocation.isEmpty())
		{
			mapLocation.put("", "");
		}
		mapLocation = clsGlobalFunctions.funSortByValues(mapLocation);
		objModelView.addObject("listLocation", mapLocation);

		return objModelView;

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/showMISFlash", method = RequestMethod.GET)
	private @ResponseBody List funShowMISFlash(@RequestParam(value = "fDate") String fDate, @RequestParam(value = "tDate") String tDate, @RequestParam(value = "propCode") String propCode, @RequestParam(value = "locCode") String locCode, HttpServletRequest req, HttpServletResponse resp)
	{

		List list = new ArrayList();
		List listReturn = new ArrayList();
		List finalList = new ArrayList();
		String fromDate = objGlobal.funGetDate("yyyy-MM-dd", fDate);
		String toDate = objGlobal.funGetDate("yyyy-MM-dd", tDate);
		Map<String, List> hmProd = new LinkedHashMap<String, List>();
		Map<String, List> hmSubGroup = new HashMap<String, List>();
		Map<String, List> hmHeader = new HashMap<String, List>();

		String prodCode = "";
		String subGrpName = "";
		List<String> listMonth = funGetMonth(fromDate, toDate);
		String sql = "select  c.strProdName,c.strUOM ,sum(b.dblQty),DATE_FORMAT(a.dtMISDate,'%M'),b.strProdCode,f.strGName,e.strSGName ,e.strSGCode,c.strProdCode,c.dblCostRM from tblmishd a " + " left outer join tbllocationmaster d on d.strLocCode=a.strLocFrom ,tblmisdtl b " + " left outer join tblproductmaster c on b.strProdCode=c.strProdCode  " + " left outer join tblsubgroupmaster e on c.strSGCode=e.strSGCode " + " left outer join tblgroupmaster f on e.strGCode=f.strGCode " + " where a.dtMISDate between '" + fromDate + "' and '" + toDate + "' and a.strMISCode=b.strMISCode and a.strLocFrom='" + locCode + "' " + " group by   Month(a.dtMISDate),f.strGCode, c.strProdCode  " + " order by e.strSGName, c.strProdName,Year(a.dtMISDate), Month(a.dtMISDate)   asc ";

		List listMISFlsh = objGlobalService.funGetList(sql);
		double totalQty = 0.0;
		double price = 0.0;
		if (null != listMISFlsh)
		{
			int j = 0;
			for (int i = 0; i < listMISFlsh.size(); i++)
			{

				Object[] obj = (Object[]) listMISFlsh.get(i);
				if (!hmProd.containsKey(obj[8].toString()))
				{
					if (!hmProd.isEmpty())
					{
						for (int cnt = j; cnt < listMonth.size(); cnt++)
						{
							list.add(0);
						}
					}
					List subGroupList = new ArrayList();
					subGroupList.add("");
					for (int cnt = 0; cnt < listMonth.size(); cnt++)
					{
						if (cnt == 0)
						{
							subGroupList.add("Category :" + obj[6].toString());
							subGroupList.add("");
						}
						else
						{
							subGroupList.add("");
						}
					}
					subGroupList.add("");
					subGroupList.add("");
					list.add(totalQty);
					list.add(price * totalQty);
					if (i != 0)
					{
						hmProd.put(prodCode, list);
					}

					// hmSubGroup.put(obj[6].toString(), hmProd);
					hmSubGroup.put(obj[6].toString(), subGroupList);
					list = new ArrayList();
					list.add(obj[6].toString());
					list.add(obj[0].toString());
					list.add(obj[1].toString());
					j = 1;
					totalQty = 0.0;

				}
				price = Double.parseDouble(obj[9].toString());
				if (listMonth.get(j).toString().equalsIgnoreCase(obj[3].toString()))
				{
					list.add(obj[2].toString());
					totalQty += Double.parseDouble(obj[2].toString());
				}
				else
				{
					list.add(0);
					i--;
				}
				j++;
				hmProd.put(obj[8].toString(), list);
				// hmSubGroup.put(obj[6].toString(), hmProd);
				prodCode = obj[8].toString();
				subGrpName = obj[6].toString();
			}

			if (!hmProd.isEmpty())
			{
				for (int cnt = j; cnt < listMonth.size(); cnt++)
				{

					list.add(0);
				}
			}
			list.add(totalQty);
			list.add(price * totalQty);
			hmProd.put(prodCode, list);
		}
		hmHeader.put("Header", listMonth);

		String subgroup = "";
		for (Map.Entry<String, List> entry : hmProd.entrySet())
		{
			List listData = entry.getValue();

			if (!listData.get(0).equals(subgroup))
			{
				List subgroupdata = hmSubGroup.get(listData.get(0));
				listReturn.add(subgroupdata);
			}
			subgroup = listData.get(0).toString();

			listReturn.add(entry.getValue());
		}
		finalList.add(listMonth);
		finalList.add(listReturn);
		return finalList;
	}

	private List<String> funGetMonth(String fromDate, String toDate)
	{
		List<String> list = new ArrayList<String>();
		String[] frmMonth = fromDate.split("-");
		String[] toMonth = toDate.split("-");
		int intfrmMonth = Integer.parseInt(frmMonth[1]) - 1;
		int inttoMonth = Integer.parseInt(toMonth[1]) - 1;
		int intfrmYear = Integer.parseInt(frmMonth[0]);
		int toYear = Integer.parseInt(toMonth[0]);
		String[] monthName = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
		list.add("Material");

		if (intfrmYear < toYear)
		{
			if (intfrmMonth > inttoMonth)
			{
				for (int i = intfrmMonth; i < monthName.length; i++)
				{
					String listMonth = monthName[i];
					list.add(listMonth);
				}
				for (int j = 0; j <= inttoMonth; j++)
				{
					String listMonth = monthName[j];
					list.add(listMonth);
				}

			}
			else
			{

				for (int i = intfrmMonth; i < inttoMonth; i++)
				{
					String listMonth = monthName[i];
					list.add(listMonth);
				}

			}
		}
		else
		{
			for (int j = intfrmMonth; j <= inttoMonth; j++)
			{
				String listMonth = monthName[j];
				list.add(listMonth);
			}

		}

		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/showMISFlashExport", method = RequestMethod.GET)
	private ModelAndView funShowMISFlashExport(@RequestParam(value = "fDate") String fDate, @RequestParam(value = "tDate") String tDate, @RequestParam(value = "propCode") String propCode, @RequestParam(value = "locCode") String locCode,@RequestParam(value = "exportType")String exportType, HttpServletRequest req, HttpServletResponse resp)
	{

		String fromDate = objGlobal.funGetDate("yyyy-MM-dd", fDate);
		String toDate = objGlobal.funGetDate("yyyy-MM-dd", tDate);
		String locNames = "";
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String[] monthName = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

		String[] frmMonth = fromDate.split("-");
		String[] toMonth = toDate.split("-");
		int intfrmMonth = Integer.parseInt(frmMonth[1]) - 1;
		int inttoMonth = Integer.parseInt(toMonth[1]) - 1;
		String fromDateToDisplay = frmMonth[2] + "-" + monthName[intfrmMonth] + "-" + frmMonth[0];
		String toDateToDisplay = toMonth[2] + "-" + monthName[inttoMonth] + "-" + toMonth[0];

		clsLocationMasterModel objModel = objLocationMasterService.funGetObject(locCode, clientCode);
		clsPropertyMaster objProperty = objPropertyMasterService.funGetProperty(propCode, req.getSession().getAttribute("clientCode").toString());
		locNames = objProperty.getPropertyName() + "," + objModel.getStrLocName();

		clsCompanyMasterModel objCompModel = objSetupMasterService.funGetObject(clientCode);
		String companyName = objCompModel.getStrCompanyName();

		List ExportList = new ArrayList();
		ExportList.add("rptMISFlash_" + fDate + "to" + tDate);

		List titleData = new ArrayList<>();
		titleData.add(companyName);
		ExportList.add(titleData);

		List titleData1 = new ArrayList<>();
		titleData1.add("Monthly Material Issue Main Store");
		ExportList.add(titleData1);

		List<String> titleData2 = new ArrayList<String>();

		titleData2.add("Reporting For:");
		titleData2.add(locNames);
		ExportList.add(titleData2);

		List filterData = new ArrayList<>();
		String periodFromDate = fromDateToDisplay + "  -  " + toDateToDisplay;

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String printedOnDate = dtf.format(now);

		filterData.add("Period From:");
		filterData.add(periodFromDate);
		filterData.add(" ");
		filterData.add("Printed On:");
		filterData.add(printedOnDate);
		Map<String, List> hmProd = new LinkedHashMap<String, List>();
		ExportList.add(filterData);
		List list = new ArrayList();
		List listReturn = new ArrayList();
		List finalList = new ArrayList();
		Map<String, List> hmSubGroup = new HashMap<String, List>();
		Map<String, List> hmHeader = new HashMap<String, List>();

		List<String> listMonth = funGetMonth(fromDate, toDate);
		String sql = "select  c.strProdName,c.strUOM ,sum(b.dblQty),DATE_FORMAT(a.dtMISDate,'%M'),b.strProdCode,f.strGName,e.strSGName ,e.strSGCode,c.strProdCode,c.dblCostRM from tblmishd a " + " left outer join tbllocationmaster d on d.strLocCode=a.strLocFrom ,tblmisdtl b " + " left outer join tblproductmaster c on b.strProdCode=c.strProdCode  " + " left outer join tblsubgroupmaster e on c.strSGCode=e.strSGCode " + " left outer join tblgroupmaster f on e.strGCode=f.strGCode " + " where a.dtMISDate between '" + fromDate + "' and '" + toDate + "' and a.strMISCode=b.strMISCode and a.strLocFrom='" + locCode + "' " + " group by   Month(a.dtMISDate),f.strGCode, c.strProdCode  " + " order by e.strSGName, c.strProdName,Year(a.dtMISDate), Month(a.dtMISDate)   asc ";

		List listMISFlsh = objGlobalService.funGetList(sql);
		double totalQty = 0.0;
		double price = 0.0;
		String prodCode = "";
		String subGrpName = "";
		DecimalFormat decimalFormater2Dec=new DecimalFormat("0.00");
		if (null != listMISFlsh)
		{
			int j = 0;
			for (int i = 0; i < listMISFlsh.size(); i++)
			{
				Object[] obj = (Object[]) listMISFlsh.get(i);
				if (!hmProd.containsKey(obj[8].toString()))
				{
					if (!hmProd.isEmpty())
					{
						for (int cnt = j; cnt < listMonth.size(); cnt++)
						{
							list.add(0);
						}
					}
					List subGroupList = new ArrayList();
					subGroupList.add("");
					for (int cnt = 0; cnt < listMonth.size(); cnt++)
					{
						if (cnt == 0)
						{
							subGroupList.add("Category :" + obj[6].toString());
							subGroupList.add("");
						}
						else
						{
							subGroupList.add("");
						}
					}
					subGroupList.add("");
					subGroupList.add("");
					list.add(decimalFormater2Dec.format(totalQty));
					if(clientCode.equals("240.001")){
						list.add(" ");	
					}else{
						list.add(decimalFormater2Dec.format(price * totalQty));
					}
					
					if (i != 0)
					{
						hmProd.put(prodCode, list);
					}

					// hmSubGroup.put(obj[6].toString(), hmProd);
					hmSubGroup.put(obj[6].toString(), subGroupList);
					list = new ArrayList();
					list.add(obj[6].toString());
					list.add(obj[0].toString());
					list.add(obj[1].toString());
					j = 1;
					totalQty = 0.0;

				}
				price = Double.parseDouble(obj[9].toString());
				if (listMonth.get(j).toString().equalsIgnoreCase(obj[3].toString()))
				{
					list.add(obj[2].toString());
					totalQty += Double.parseDouble(obj[2].toString());
				}
				else
				{
					list.add(0);
					i--;
				}
				j++;
				hmProd.put(obj[8].toString(), list);
				// hmSubGroup.put(obj[6].toString(), hmProd);
				prodCode = obj[8].toString();
				subGrpName = obj[6].toString();
			}

			if (!hmProd.isEmpty())
			{
				for (int cnt = j; cnt < listMonth.size(); cnt++)
				{

					list.add(0);
				}
			}
			list.add(decimalFormater2Dec.format(totalQty));
			if(clientCode.equals("240.001")){
				list.add(" ");	
			}else{
				list.add(decimalFormater2Dec.format(price * totalQty));	
			}
			
			hmProd.put(prodCode, list);
		}
		hmHeader.put("Header", listMonth);

		String subgroup = "";
		for (Map.Entry<String, List> entry : hmProd.entrySet())
		{
			List listData = entry.getValue();

			if (!listData.get(0).equals(subgroup))
			{
				List subgroupdata = hmSubGroup.get(listData.get(0));
				System.out.print(subgroupdata);
				subgroupdata.remove(0);
				listReturn.add(subgroupdata);
			}
			subgroup = listData.get(0).toString();
			listData.remove(0);
			listReturn.add(listData);
		}
		String header = "";
		// String[] ExcelHeader = new String[listMonth.size()]; ;
		for (int i = 0; i < listMonth.size(); i++)
		{
			if (i == 1)
			{
				header += "NOS" + ",";
			}
			header += listMonth.get(i).toString() + ",";

		}
		header += "Total Qty" + ",";
		if(clientCode.equals("240.001")){
			header += " ";
		}else{
			header += "Total Amount";
		}
		
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		ExportList.add(listReturn);

		if (exportType.equalsIgnoreCase("Excel"))
		{
			return new ModelAndView("excelViewFromDateTodateCompanyNameWithReportName", "listFromDateTodateWithReportName", ExportList);
		}
		else
		{			
			return new ModelAndView("pdfViewMISFlash", "PDFMISFlashData", ExportList);
		}
		
	}

}
