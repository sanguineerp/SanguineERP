package com.sanguine.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.tomcat.util.http.fileupload.FileUploadBase.IOFileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.text.SimpleDateFormat;
import com.sanguine.crm.model.clsPartyMasterModel;
import com.sanguine.crm.service.clsPartyMasterService;
import com.sanguine.model.clsGroupMasterModel;
import com.sanguine.model.clsMISDtlModel;
import com.sanguine.model.clsOpeningStkDtl;
import com.sanguine.model.clsPOSSalesDtlModel;
import com.sanguine.model.clsProdSuppMasterModel;
import com.sanguine.model.clsProductMasterModel;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.model.clsPurchaseIndentDtlModel;
import com.sanguine.model.clsPurchaseOrderDtlModel;
import com.sanguine.model.clsRequisitionDtlModel;
import com.sanguine.model.clsStkPostingDtlModel;
import com.sanguine.model.clsSubGroupMasterModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsGroupMasterService;
import com.sanguine.service.clsPOSLinkUpService;
import com.sanguine.service.clsProductMasterService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.service.clsSubGroupMasterService;
import com.sanguine.webpms.bean.clsGuestMasterBean;
import com.sanguine.webpms.bean.clsRoomMasterBean;
import com.sanguine.webpms.dao.clsGuestMasterDao;
import com.sanguine.webpms.dao.clsRoomTypeMasterDao;
import com.sanguine.webpms.model.clsGuestMasterHdModel;
import com.sanguine.webpms.model.clsRoomMasterModel;
import com.sanguine.webpms.model.clsRoomTypeMasterModel;
import com.sanguine.webpms.service.clsGuestMasterService;

@Controller
public class clsExcelExportImportController {

	@Autowired
	private clsProductMasterService objProductMasterService;
	
	@Autowired
	private clsPartyMasterService objPartyMaster;
	
	@Autowired
	private clsSubGroupMasterService objSubGroupMasterService;

	@Autowired
	private clsGroupMasterService objGroupMasterService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsPOSLinkUpService objPOSLinkUpService;

	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@Autowired
	private clsGRNController objGRNController;
	
	@Autowired
	private clsGlobalFunctions objGlobalFunctions;
	
	@Autowired
	private clsGuestMasterDao objGuestMasterDao;
	
	@Autowired
	private clsGuestMasterService  objGuestMasterService;
	
	@Autowired
	private clsRoomTypeMasterDao objRoomTypeMasterDao;

	final static Logger logger = Logger.getLogger(clsExcelExportImportController.class);

	/**
	 * Open The Excel Export Import From
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/frmExcelExportImport", method = RequestMethod.GET)
	public ModelAndView funOpenForm(HttpServletRequest request) {
		String exportUOM = request.getParameter("exportUOM");
		request.getSession().removeAttribute("exportUOM");
		request.getSession().setAttribute("exportUOM", exportUOM);
		return new ModelAndView("frmExcelExportImport");
	}

	/**
	 * Opening Stock Export Data
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/frmOpeningStkExcelExport", method = RequestMethod.GET)
	public ModelAndView funstkOpeningExcelExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String locCode = request.getParameter("strLocCode");
		String header = "Group Name, SubGroupName,ProductCode,ProductName,Qty,UOM,Cost Per Unit,Revision Level,Lot No";
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propCode, clientCode);
		String hql = "";
		if (objSetup.getStrShowAllProdToAllLoc() == null || objSetup.getStrShowAllProdToAllLoc() == "N") {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProductReOrderLevelModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strLocationCode='" + locCode + "' ";
		} else {
			hql = "from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c " + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' and c.strClientCode='" + clientCode + "'";
		}

		List list = objGlobalFunctionsService.funGetList(hql, "hql");
		String expUOM = request.getSession().getAttribute("exportUOM").toString();
		request.getSession().removeAttribute("exportUOM");
		List OpeningStklist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object[] ob = (Object[]) list.get(i);
			clsProductMasterModel prodModel = (clsProductMasterModel) ob[0];
			clsSubGroupMasterModel subGroupModel = (clsSubGroupMasterModel) ob[1];
			clsGroupMasterModel groupModel = (clsGroupMasterModel) ob[2];

			List DataList = new ArrayList<>();
			DataList.add(groupModel.getStrGName());
			DataList.add(subGroupModel.getStrSGName());
			DataList.add(prodModel.getStrProdCode());
			DataList.add(prodModel.getStrProdName());
			DataList.add("");
			if (expUOM.equals("RecUOM")) {
				DataList.add(prodModel.getStrReceivedUOM());
			}
			if (expUOM.equals("IssueUOM")) {
				DataList.add(prodModel.getStrIssueUOM());
			}
			if (expUOM.equals("RecipeUOM")) {
				DataList.add(prodModel.getStrRecipeUOM());
			}

			DataList.add(prodModel.getDblCostRM());
			DataList.add("0.00");
			DataList.add("0.00");
			OpeningStklist.add(DataList);
		}
		ExportList.add(OpeningStklist);

		return new ModelAndView("excelView", "stocklist", ExportList);
	}

	/**
	 * Exporting Physical Stock Posting Data
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/PhyStkPstExcelExport", method = RequestMethod.GET)
	public ModelAndView funPhyStkPstExcelExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		// String
		// locCode=request.getSession().getAttribute("locationCode").toString();
		
		String locCode = request.getParameter("locCode");
		String sgCode = request.getParameter("sgCode");
		String gCode = request.getParameter("gCode");
		String prodWiseStock = request.getParameter("prodWiseStock");
		
		String header ="";
		if(prodWiseStock.equals("Yes"))
		{
			header="Group Name, SubGroupName,ProductCode,ProductName,Stock,Qty,UOM";
		}else{
			header="Group Name, SubGroupName,ProductCode,ProductName,Qty,UOM";
		}
				
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propCode, clientCode);
		String hql = "";
		if (objSetup.getStrShowAllProdToAllLoc() == null || objSetup.getStrShowAllProdToAllLoc() == "N") {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProductReOrderLevelModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strLocationCode='" + locCode + "' ";
		} else {
			if (!locCode.equals("") || !locCode.isEmpty()) {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c " + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode  " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  ";
			if (!sgCode.equals("") || !sgCode.isEmpty()) 
			{
				hql += "and a.strSGCode='"+sgCode+"'";
			}
			
			if (!gCode.equals("") || !gCode.isEmpty()) 
			{
				hql += "and b.strGCode='"+gCode+"'";
			}
			}else{
				hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProductReOrderLevelModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strLocationCode='" + locCode + "' ";	
				if (!sgCode.equals("") || !sgCode.isEmpty()) 
				{
					hql += "and a.strSGCode='"+sgCode+"'";
				}
				
				if (!gCode.equals("") || !gCode.isEmpty()) 
				{
					hql += "and b.strGCode='"+gCode+"'";
				}
			}
		
		}
		

		List list = objGlobalFunctionsService.funGetList(hql, "hql");
		List PhyStkPstlist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {

			Object[] ob = (Object[]) list.get(i);
			clsProductMasterModel prodModel = (clsProductMasterModel) ob[0];
			clsSubGroupMasterModel subGroupModel = (clsSubGroupMasterModel) ob[1];
			clsGroupMasterModel groupModel = (clsGroupMasterModel) ob[2];
			List DataList = new ArrayList<>();
			
			if(prodWiseStock.equals("Yes")){
			double stock=objGlobalFunctions.funGetStockForProductRecUOM(prodModel.getStrProdCode(),request);
			if(stock>0)
			{
			DataList.add(groupModel.getStrGName());
			DataList.add(subGroupModel.getStrSGName());
			DataList.add(prodModel.getStrProdCode());
			DataList.add(prodModel.getStrProdName());
			
			DataList.add(stock);
			
			DataList.add("");
			DataList.add(prodModel.getStrUOM());
			PhyStkPstlist.add(DataList);
			}
			}else{
				DataList.add(groupModel.getStrGName());
				DataList.add(subGroupModel.getStrSGName());
				DataList.add(prodModel.getStrProdCode());
				DataList.add(prodModel.getStrProdName());
				DataList.add("");
				DataList.add(prodModel.getStrUOM());
				PhyStkPstlist.add(DataList);
			}
		}
		ExportList.add(PhyStkPstlist);

		return new ModelAndView("excelView", "stocklist", ExportList);
	}

	/**
	 * Location Master Reorder Level Exporting
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/LocationMasterReorderLevelExcelExport", method = RequestMethod.GET)
	public ModelAndView funLocMastReOrderLevelExcelExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String LocCode = "";
		List list = new ArrayList<>();
		List LocMastReOrderLvllist = new ArrayList();
		if (request.getParameter("locCode") != null) {
			LocCode = request.getParameter("locCode").toString();
		}
		String header = "Location Code,Location Name,GroupName, SubGroupName,ProductCode,ProductName,Non Stockable,ReOrderLevel,ReOrderQty";
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);

		String sql = "select ifnull(e.strLocCode,'') as strLocCode,ifnull(e.strLocName,'') as strLocName,ifnull(c.strGName,'') as strGName,ifnull(b.strSGName,'') as strSGName," + " a.strProdCode,a.strProdName,a.strNonStockableItem,ifnull(d.dblReOrderLevel,0.00) as dblReOrderLevel,ifnull(d.dblReOrderQty,0.00) as dblReOrderQty "
				+ " from tblproductmaster a left outer join tblsubgroupmaster b on a.strSGCode=b.strSGCode and b.strClientCode='" + clientCode + "' " + " left outer join tblgroupmaster c on c.strGCode=b.strGCode and c.strClientCode='" + clientCode + "' " + " left outer join tblreorderlevel d on d.strProdCode=a.strProdCode and d.strLocationCode='" + LocCode + "'  and d.strClientCode='"
				+ clientCode + "'" + " left outer join tbllocationmaster e on d.strLocationCode=e.strLocCode and e.strLocCode='" + LocCode + "'  and  e.strClientCode='" + clientCode + "'" + " where a.strClientCode='" + clientCode + "' ";
		list = objGlobalFunctionsService.funGetList(sql, "sql");
		for (int i = 0; i < list.size(); i++) {
			Object[] ob = (Object[]) list.get(i);
			List DataList = new ArrayList<>();
			DataList.add(ob[0]);
			DataList.add(ob[1]);
			DataList.add(ob[2]);
			DataList.add(ob[3]);
			DataList.add(ob[4]);
			DataList.add(ob[5]);
			DataList.add(ob[6]);
			DataList.add(ob[7]);
			DataList.add(ob[8]);
			LocMastReOrderLvllist.add(DataList);
		}
		ExportList.add(LocMastReOrderLvllist);
		return new ModelAndView("excelView", "stocklist", ExportList);
	}

	/**
	 * for POS Sales Excel Export for third party POS Sales
	 * 
	 * @param request
	 * @return
	 */

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/POSSalesExcelExport", method = RequestMethod.GET)
	public ModelAndView funPOSSalesExcelExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String LocCode = "";
		List list = new ArrayList<>();
		List LocMastReOrderLvllist = new ArrayList();
		if (request.getParameter("locCode") != null) {
			LocCode = request.getParameter("locCode").toString();
		}
		String header = "POS Code,POS Item Code,POS Item Name,Qty,Rate,BillDate(dd/mm/yyyy)";
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);

		ExportList.add(LocMastReOrderLvllist);
		return new ModelAndView("excelView", "stocklist", ExportList);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/MaterialReqExport", method = RequestMethod.GET)
	public ModelAndView funMaterialReqExcelExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		// String
		// locCode=request.getSession().getAttribute("locationCode").toString();
		
		String locCode = request.getParameter("locCode");
		String sgCode = request.getParameter("sgCode");
		String gCode = request.getParameter("gCode");
		String prodWiseStock = request.getParameter("prodWiseStock");
		
		String header ="";
		if(prodWiseStock.equals("Yes"))
		{
			header="Group Name, SubGroupName,ProductCode,ProductName,Stock,Qty,UOM";
		}else{
			header="Group Name, SubGroupName,ProductCode,ProductName,Qty,UOM";
		}
				
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propCode, clientCode);
		String hql = "";
		if (objSetup.getStrShowAllProdToAllLoc() == null || objSetup.getStrShowAllProdToAllLoc() == "N") {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProductReOrderLevelModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strLocationCode='" + locCode + "' ";
		} else {
			if (!locCode.equals("") || !locCode.isEmpty()) {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c " + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode  " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  ";
			if (!sgCode.equals("") || !sgCode.isEmpty()) 
			{
				hql += "and a.strSGCode='"+sgCode+"'";
			}
			
			if (!gCode.equals("") || !gCode.isEmpty()) 
			{
				hql += "and b.strGCode='"+gCode+"'";
			}
			}else{
				hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProductReOrderLevelModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strLocationCode='" + locCode + "' ";	
				if (!sgCode.equals("") || !sgCode.isEmpty()) 
				{
					hql += "and a.strSGCode='"+sgCode+"'";
				}
				
				if (!gCode.equals("") || !gCode.isEmpty()) 
				{
					hql += "and b.strGCode='"+gCode+"'";
				}
			}
		
		}
		

		List list = objGlobalFunctionsService.funGetList(hql, "hql");
		List MaterialReq = new ArrayList();
		for (int i = 0; i < list.size(); i++) {

			Object[] ob = (Object[]) list.get(i);
			clsProductMasterModel prodModel = (clsProductMasterModel) ob[0];
			clsSubGroupMasterModel subGroupModel = (clsSubGroupMasterModel) ob[1];
			clsGroupMasterModel groupModel = (clsGroupMasterModel) ob[2];
			List DataList = new ArrayList<>();
			
			if(prodWiseStock.equals("Yes")){
			double stock=objGlobalFunctions.funGetStockForProductRecUOM(prodModel.getStrProdCode(),request);
			if(stock>0)
			{
			DataList.add(groupModel.getStrGName());
			DataList.add(subGroupModel.getStrSGName());
			DataList.add(prodModel.getStrProdCode());
			DataList.add(prodModel.getStrProdName());
			
			DataList.add(stock);
			
			DataList.add("");
			DataList.add(prodModel.getStrUOM());
			MaterialReq.add(DataList);
			}
			}else{
				DataList.add(groupModel.getStrGName());
				DataList.add(subGroupModel.getStrSGName());
				DataList.add(prodModel.getStrProdCode());
				DataList.add(prodModel.getStrProdName());
				DataList.add("");
				DataList.add(prodModel.getStrUOM());
				MaterialReq.add(DataList);
			}
		}
		ExportList.add(MaterialReq);

		return new ModelAndView("excelView", "stocklist", ExportList);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/PurchaseIndentExport", method = RequestMethod.GET)
	public ModelAndView funPurchaseIndentExcelExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		// String
		// locCode=request.getSession().getAttribute("locationCode").toString();
		
		String locCode = request.getParameter("locCode");
		String sgCode = request.getParameter("sgCode");
		String gCode = request.getParameter("gCode");
		String prodWiseStock = request.getParameter("prodWiseStock");
		
		String header ="";
		if(prodWiseStock.equals("Yes"))
		{
			header="Group Name, SubGroupName,ProductCode,ProductName,Stock,Qty,UOM";
		}else{
			header="Group Name, SubGroupName,ProductCode,ProductName,Qty,UOM";
		}
				
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propCode, clientCode);
		String hql = "";
		if (objSetup.getStrShowAllProdToAllLoc() == null || objSetup.getStrShowAllProdToAllLoc() == "N") {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProductReOrderLevelModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strLocationCode='" + locCode + "' ";
		} else {
			if (!locCode.equals("") || !locCode.isEmpty()) {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c " + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode  " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  ";
			if (!sgCode.equals("") || !sgCode.isEmpty()) 
			{
				hql += "and a.strSGCode='"+sgCode+"'";
			}
			
			if (!gCode.equals("") || !gCode.isEmpty()) 
			{
				hql += "and b.strGCode='"+gCode+"'";
			}
			}else{
				hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProductReOrderLevelModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strLocationCode='" + locCode + "' ";	
				if (!sgCode.equals("") || !sgCode.isEmpty()) 
				{
					hql += "and a.strSGCode='"+sgCode+"'";
				}
				
				if (!gCode.equals("") || !gCode.isEmpty()) 
				{
					hql += "and b.strGCode='"+gCode+"'";
				}
			}
		
		}
		

		List list = objGlobalFunctionsService.funGetList(hql, "hql");
		List PhyStkPstlist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {

			Object[] ob = (Object[]) list.get(i);
			clsProductMasterModel prodModel = (clsProductMasterModel) ob[0];
			clsSubGroupMasterModel subGroupModel = (clsSubGroupMasterModel) ob[1];
			clsGroupMasterModel groupModel = (clsGroupMasterModel) ob[2];
			List DataList = new ArrayList<>();
			
			if(prodWiseStock.equals("Yes")){
			double stock=objGlobalFunctions.funGetStockForProductRecUOM(prodModel.getStrProdCode(),request);
			if(stock>0)
			{
			DataList.add(groupModel.getStrGName());
			DataList.add(subGroupModel.getStrSGName());
			DataList.add(prodModel.getStrProdCode());
			DataList.add(prodModel.getStrProdName());
			
			DataList.add(stock);
			
			DataList.add("");
			DataList.add(prodModel.getStrUOM());
			PhyStkPstlist.add(DataList);
			}
			}else{
				DataList.add(groupModel.getStrGName());
				DataList.add(subGroupModel.getStrSGName());
				DataList.add(prodModel.getStrProdCode());
				DataList.add(prodModel.getStrProdName());
				DataList.add("");
				DataList.add(prodModel.getStrUOM());
				PhyStkPstlist.add(DataList);
			}
		}
		ExportList.add(PhyStkPstlist);

		return new ModelAndView("excelView", "stocklist", ExportList);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/MISExport", method = RequestMethod.GET)
	public ModelAndView funMISExcelExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		// String
		// locCode=request.getSession().getAttribute("locationCode").toString();
		
		String locCode = request.getParameter("locCode");
		String sgCode = request.getParameter("sgCode");
		String gCode = request.getParameter("gCode");
		String prodWiseStock = request.getParameter("prodWiseStock");
		
		String header ="";
		if(prodWiseStock.equals("Yes"))
		{
			header="Group Name, SubGroupName,ProductCode,ProductName,Stock,Qty,UOM";
		}else{
			header="Group Name, SubGroupName,ProductCode,ProductName,Qty,UOM";
		}
				
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propCode, clientCode);
		String hql = "";
		if (objSetup.getStrShowAllProdToAllLoc() == null || objSetup.getStrShowAllProdToAllLoc() == "N") {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProductReOrderLevelModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strLocationCode='" + locCode + "' ";
		} else {
			if (!locCode.equals("") || !locCode.isEmpty()) {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c " + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode  " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  ";
			if (!sgCode.equals("") || !sgCode.isEmpty()) 
			{
				hql += "and a.strSGCode='"+sgCode+"'";
			}
			
			if (!gCode.equals("") || !gCode.isEmpty()) 
			{
				hql += "and b.strGCode='"+gCode+"'";
			}
			}else{
				hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProductReOrderLevelModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strLocationCode='" + locCode + "' ";	
				if (!sgCode.equals("") || !sgCode.isEmpty()) 
				{
					hql += "and a.strSGCode='"+sgCode+"'";
				}
				
				if (!gCode.equals("") || !gCode.isEmpty()) 
				{
					hql += "and b.strGCode='"+gCode+"'";
				}
			}
		
		}
		

		List list = objGlobalFunctionsService.funGetList(hql, "hql");
		List MISStk = new ArrayList();
		for (int i = 0; i < list.size(); i++) {

			Object[] ob = (Object[]) list.get(i);
			clsProductMasterModel prodModel = (clsProductMasterModel) ob[0];
			clsSubGroupMasterModel subGroupModel = (clsSubGroupMasterModel) ob[1];
			clsGroupMasterModel groupModel = (clsGroupMasterModel) ob[2];
			List DataList = new ArrayList<>();
			
			if(prodWiseStock.equals("Yes")){
			double stock=objGlobalFunctions.funGetStockForProductRecUOM(prodModel.getStrProdCode(),request);
			if(stock>0)
			{
			DataList.add(groupModel.getStrGName());
			DataList.add(subGroupModel.getStrSGName());
			DataList.add(prodModel.getStrProdCode());
			DataList.add(prodModel.getStrProdName());
			
			DataList.add(stock);
			
			DataList.add("");
			DataList.add(prodModel.getStrUOM());
			MISStk.add(DataList);
			}
			}else{
				DataList.add(groupModel.getStrGName());
				DataList.add(subGroupModel.getStrSGName());
				DataList.add(prodModel.getStrProdCode());
				DataList.add(prodModel.getStrProdName());
				DataList.add("");
				DataList.add(prodModel.getStrUOM());
				MISStk.add(DataList);
			}
		}
		ExportList.add(MISStk);

		return new ModelAndView("excelView", "stocklist", ExportList);
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/PurchaseOrderExport", method = RequestMethod.GET)
	public ModelAndView funPurchaseOrderExcelExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		// String
		// locCode=request.getSession().getAttribute("locationCode").toString();
		
		String suppCode = request.getParameter("suppCode");
		String sgCode = request.getParameter("sgCode");
		String gCode = request.getParameter("gCode");
		String prodWiseStock = request.getParameter("prodWiseStock");
		
		String header ="";
		if(prodWiseStock.equals("Yes"))
		{
			header="Group Name, SubGroupName,ProductCode,ProductName,Stock,Qty,UOM";
		}else{
			header="Group Name, SubGroupName,ProductCode,ProductName,Qty,UOM";
		}
				
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propCode, clientCode);
		String hql = "";
		if (objSetup.getStrShowAllProdToAllLoc() == null || objSetup.getStrShowAllProdToAllLoc() == "N") {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProdSuppMasterModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strSuppCode='" + suppCode + "' ";
		} else {
			if (!suppCode.equals("") || !suppCode.isEmpty()) {
			hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c " + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode  " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  ";
			if (!sgCode.equals("") || !sgCode.isEmpty()) 
			{
				hql += "and a.strSGCode='"+sgCode+"'";
			}
			
			if (!gCode.equals("") || !gCode.isEmpty()) 
			{
				hql += "and b.strGCode='"+gCode+"'";
			}
			}else{
				hql = " from clsProductMasterModel a, clsSubGroupMasterModel b,clsGroupMasterModel c ,clsProdSuppMasterModel d" + " where a.strSGCode=b.strSGCode  and b.strGCode=c.strGCode and a.strProdCode=d.strProdCode " + " and a.strClientCode='" + clientCode + "' and b.strClientCode='" + clientCode + "' " + "and c.strClientCode='" + clientCode + "'  and d.strSuppCode='" + suppCode + "' ";	
				if (!sgCode.equals("") || !sgCode.isEmpty()) 
				{
					hql += "and a.strSGCode='"+sgCode+"'";
				}
				
				if (!gCode.equals("") || !gCode.isEmpty()) 
				{
					hql += "and b.strGCode='"+gCode+"'";
				}
			}
		
		}
		

		List list = objGlobalFunctionsService.funGetList(hql, "hql");
		List PhyStkPstlist = new ArrayList();
		for (int i = 0; i < list.size(); i++) {

			Object[] ob = (Object[]) list.get(i);
			clsProductMasterModel prodModel = (clsProductMasterModel) ob[0];
			clsSubGroupMasterModel subGroupModel = (clsSubGroupMasterModel) ob[1];
			clsGroupMasterModel groupModel = (clsGroupMasterModel) ob[2];
			List DataList = new ArrayList<>();
			
			if(prodWiseStock.equals("Yes")){
			double stock=objGlobalFunctions.funGetStockForProductRecUOM(prodModel.getStrProdCode(),request);
			if(stock>0)
			{
			DataList.add(groupModel.getStrGName());
			DataList.add(subGroupModel.getStrSGName());
			DataList.add(prodModel.getStrProdCode());
			DataList.add(prodModel.getStrProdName());
			
			DataList.add(stock);
			
			DataList.add("");
			DataList.add(prodModel.getStrUOM());
			PhyStkPstlist.add(DataList);
			}
			}else{
				DataList.add(groupModel.getStrGName());
				DataList.add(subGroupModel.getStrSGName());
				DataList.add(prodModel.getStrProdCode());
				DataList.add(prodModel.getStrProdName());
				DataList.add("");
				DataList.add(prodModel.getStrUOM());
				PhyStkPstlist.add(DataList);
			}
		}
		ExportList.add(PhyStkPstlist);

		return new ModelAndView("excelView", "stocklist", ExportList);
	}
	
	
	/**
	 * Only Import
	 * 
	 * @param excelfile
	 * @param request
	 * @param res
	 * @return
	 * @throws IOFileUploadException
	 */

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/ExcelExportImport", method = RequestMethod.POST)
	public @ResponseBody List funUploadExcel(@RequestParam("file") MultipartFile excelfile, HttpServletRequest request, HttpServletResponse res) throws IOFileUploadException {
		List list = new ArrayList<>();
		String formname = request.getParameter("formname").toString();
		try {

			// Creates a workbook object from the uploaded excelfile
			HSSFWorkbook workbook = new HSSFWorkbook(excelfile.getInputStream());
			// Creates a worksheet object representing the first sheet
			HSSFSheet worksheet = workbook.getSheetAt(0);
			// Reads the data in excel file until last row is encountered
			switch (formname) {
			case "frmOpeningStock":
				list = funOpeningStocks(worksheet, request);
				break;

			case "frmPhysicalStkPosting":
				list = funPhyStkPsting(worksheet, request);
				break;
			case "frmLocationMaster":
				list = funLocMastReOrderLvl(worksheet, request);
				break;

			case "frmPOSSalesSheet":
				list = funLoadPOSSalesData(worksheet, request);
				break;

			case "frmSalesOrder":
				list = funLoadPOSSalesData(worksheet, request);
				break;
				
			case "frmMaterialReq":
				list = funMaterialReq(worksheet, request);
				break;
				
			case "frmPurchaseIndent":
				list = funPI(worksheet, request);
				break;
				
			case "frmPurchaseOrder":
				list = funPO(worksheet, request);
				break;
				
			case "frmMIS":
				list = funMIS(worksheet, request);
				break;

			case "frmGuestMaster":
				list = funGuestList(worksheet, request);
				break;
				
			case "frmRoomMaster":
				list = funRoomList(worksheet, request);
				break;
				
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return list;
	}

	private List funRoomList(HSSFSheet worksheet, HttpServletRequest request) {
	

		List listGuestlist = new ArrayList<>();
		int RowCount = 0;
		//String prodCode = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		
		//String prodStock=request.getParameter("prodStock");
		try {
			int i = 1;
			
			String strRoomName = "";
			clsRoomMasterModel objModel = new clsRoomMasterModel();
			HashMap<String,Double> hm = new HashMap<String,Double>();
			List list = new ArrayList<>();
			
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object representing a single row in excel
				
				
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				
				strRoomName = row.getCell(0).toString();
				objModel = new clsRoomMasterModel();
							
				hm.put(row.getCell(1).toString(),Double.parseDouble(row.getCell(2).toString()));
				
				
				
				list.add(strRoomName);
			}
			
			funCheckRoomType(hm,clientCode,userCode);
			
			funCheckRoom(hm,clientCode,userCode);

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			List list = new ArrayList<>();
			list.add("Invalid Excel File");
			//list.add("Invalid Entry In Row No." + RowCount + " and Product Code " + prodCode + " ");
			return list;
		}
		return listGuestlist;
	
	}

	

	private void funCheckRoom(HashMap<String, Double> hm, String clientCode,
			String userCode) {
		
		
		
	}

	private void funCheckRoomType(HashMap<String, Double> hm, String clientCode, String userCode) {
		
		 for (Map.Entry<String,Double> entry : hm.entrySet())  {
			 
		 
		
		String sqlCheck = "select * from tblroomtypemaster a where a.strRoomTypeDesc='"+entry.getKey()+"' and a.strClientCode='"+clientCode+"'";
		
		List list=objGlobalFunctionsService.funGetListModuleWise(sqlCheck, "sql");
		
		if(list!=null && list.size()>0)
		{
			
			clsRoomTypeMasterModel objRoomTypeMasterModel =  new clsRoomTypeMasterModel();
			
			for (int cnt = 0; cnt < list.size(); cnt++) {
				Object[] arrObj = (Object[]) list.get(cnt);
			
			objRoomTypeMasterModel.setStrRoomTypeCode(arrObj[0].toString());
			objRoomTypeMasterModel.setStrRoomTypeDesc(entry.getKey());
			objRoomTypeMasterModel.setDblRoomTerrif(entry.getValue());
			objRoomTypeMasterModel.setStrUserCreated(arrObj[3].toString());
			objRoomTypeMasterModel.setStrUserEdited(arrObj[4].toString());
			objRoomTypeMasterModel.setDteDateCreated(arrObj[5].toString());
			objRoomTypeMasterModel.setDteDateEdited(arrObj[6].toString());
			objRoomTypeMasterModel.setStrClientCode(clientCode);
			
			}
			objRoomTypeMasterDao.funAddUpdateRoomMaster(objRoomTypeMasterModel);
		}
		else
		{
			clsRoomTypeMasterModel objRoomTypeMasterModel =  new clsRoomTypeMasterModel();
					
					long lastNo = 0;

					lastNo = objGlobalFunctionsService.funGetPMSMasterLastNo("tblroomtypemaster", "RoomTypeMaster", "strRoomTypeCode", clientCode);
					String roomTypeCode = "RT" + String.format("%06d", lastNo);
					// String deptCode="D0000001";
					
					
					
					
					objRoomTypeMasterModel.setStrRoomTypeCode(roomTypeCode);
					objRoomTypeMasterModel.setStrRoomTypeDesc(entry.getKey());
					objRoomTypeMasterModel.setDblRoomTerrif(entry.getValue());
					objRoomTypeMasterModel.setStrUserCreated(userCode);
					objRoomTypeMasterModel.setStrUserEdited(userCode);
					objRoomTypeMasterModel.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
					objRoomTypeMasterModel.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
					objRoomTypeMasterModel.setStrClientCode(clientCode);
					
					objRoomTypeMasterDao.funAddUpdateRoomMaster(objRoomTypeMasterModel);
				
		}
	}
	}

	/*
	 * Start WebStock Import
	 */
	/**
	 * Opening Stock Import function
	 * 
	 * @param worksheet
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List funOpeningStocks(HSSFSheet worksheet, HttpServletRequest request) {
		String expUOM = request.getSession().getAttribute("exportUOM").toString();

		List listOpeningStklist = new ArrayList<>();
		int RowCount = 0;
		String prodCode = "";
		try {

			int i = 1;
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object for the Candidate Model
				clsOpeningStkDtl OpeningStkDtl = new clsOpeningStkDtl();
				// Creates an object representing a single row in excel
				HSSFRow row = worksheet.getRow(i++);

				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				prodCode = row.getCell(2).getStringCellValue();
				Cell c = row.getCell(4);
				if (c != null && c.getCellType() != 1) {
					OpeningStkDtl.setStrProdCode(row.getCell(2).getStringCellValue());
					String prodName = "";
					if (row.getCell(3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						prodName = String.valueOf(row.getCell(3).getNumericCellValue());
					} else {
						prodName = row.getCell(3).getRichStringCellValue().toString();
					}
					OpeningStkDtl.setStrProdName(prodName);
					OpeningStkDtl.setDblQty(row.getCell(4).getNumericCellValue());

					String uom = funGetProductUOM(row.getCell(2).getStringCellValue(), expUOM, request);
					OpeningStkDtl.setStrUOM(uom); // thiS is for Independent of
													// excel sheet uom

					// OpeningStkDtl.setStrUOM(row.getCell(5).getStringCellValue());
					// thiS is for according to EXcel uom
					OpeningStkDtl.setDblCostPUnit(row.getCell(6).getNumericCellValue());
					OpeningStkDtl.setDblRevLvl(row.getCell(7).getNumericCellValue());
					OpeningStkDtl.setStrLotNo(String.valueOf(row.getCell(8).getNumericCellValue()));
					// Sends the model object to service layer for validation,
					// data processing and then to persist
					listOpeningStklist.add(OpeningStkDtl);
				}

			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			List list = new ArrayList<>();
			list.add("Invalid Excel File");
			list.add("Invalid Entry In Row No." + RowCount + " and Product Code " + prodCode + " ");
			return list;
		}
		return listOpeningStklist;
	}

	/**
	 * \ Physical Stock Posting Import function
	 * 
	 * @param worksheet
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List funPhyStkPsting(HSSFSheet worksheet, HttpServletRequest request) {
		List listPhyStklist = new ArrayList<>();
		int RowCount = 0;
		String prodCode = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		
		String prodStock=request.getParameter("prodStock");
		try {
			int i = 1;
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object for the Candidate Model
				clsStkPostingDtlModel PhyStkDtl = new clsStkPostingDtlModel();
				// Creates an object representing a single row in excel
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				prodCode = row.getCell(2).getStringCellValue();
				Cell c = row.getCell(4);
				if (c != null && c.getCellType() != 1) {
					if (row.getCell(4).getNumericCellValue() >= 0) {
						PhyStkDtl.setStrProdCode(row.getCell(2).getStringCellValue());
						String prodName = "";
						if (row.getCell(3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							prodName = String.valueOf(row.getCell(3).getNumericCellValue());
						} else {
							prodName = row.getCell(3).getRichStringCellValue().toString();
						}

						PhyStkDtl.setStrProdName(prodName);
						if(prodStock.equals("Yes"))
						{
							PhyStkDtl.setDblPStock(row.getCell(5).getNumericCellValue());
						}else{
							PhyStkDtl.setDblPStock(row.getCell(4).getNumericCellValue());
						}
						clsProductMasterModel Prodmodel = objProductMasterService.funGetObject(prodCode, clientCode);
						PhyStkDtl.setDblPrice(Prodmodel.getDblCostRM());
						PhyStkDtl.setDblWeight(Prodmodel.getDblWeight());

						List ProdList = objGRNController.funLatestGRNProductRate(prodCode, request);

						if (!ProdList.isEmpty()) {
							PhyStkDtl.setDblActualRate(Double.parseDouble((ProdList.get(1)).toString()));

						} else {
							PhyStkDtl.setDblActualRate(Prodmodel.getDblCostRM());
						}

						// PhyStkDtl.setDblCStock(objGlobalFunctions.funGetCurrentStockForProduct(prodCode,
						// objMISHd.getStrLocFrom(), clientCode,
						// userCode,startDate,objGlobalFunctions.funGetCurrentDate("yyyy-MM-dd")));
						// Sends the model object to service layer for
						// validation,
						// data processing and then to persist
						listPhyStklist.add(PhyStkDtl);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			List list = new ArrayList<>();
			list.add("Invalid Excel File");
			list.add("Invalid Entry In Row No." + RowCount + " and Product Code " + prodCode + " ");
			return list;
		}
		return listPhyStklist;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List funPI(HSSFSheet worksheet, HttpServletRequest request) {
		List listPhyStklist = new ArrayList<>();
		int RowCount = 0;
		String prodCode = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		
		//String prodStock=request.getParameter("prodStock");
		try {
			int i = 1;
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object for the Candidate Model
				clsPurchaseIndentDtlModel PhyStkDtl = new clsPurchaseIndentDtlModel();
				// Creates an object representing a single row in excel
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				prodCode = row.getCell(2).getStringCellValue();
				Cell c = row.getCell(4);
				if (c != null && c.getCellType() != 1) {
					if (row.getCell(4).getNumericCellValue() >= 0) {
						PhyStkDtl.setStrProdCode(row.getCell(2).getStringCellValue());
						String prodName = "";
						if (row.getCell(3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							prodName = String.valueOf(row.getCell(3).getNumericCellValue());
						} else {
							prodName = row.getCell(3).getRichStringCellValue().toString();
						}

						PhyStkDtl.setStrProdName(prodName);
						
							PhyStkDtl.setStrDocType(row.getCell(5).toString());
						
							PhyStkDtl.setDblQty(row.getCell(4).getNumericCellValue());
						
						clsProductMasterModel Prodmodel = objProductMasterService.funGetObject(prodCode, clientCode);
						PhyStkDtl.setDblAmount(Prodmodel.getDblCostRM());
						
						//PhyStkDtl.setdb(Prodmodel.getDblWeight());

						List ProdList = objGRNController.funLatestGRNProductRate(prodCode, request);

						

						// PhyStkDtl.setDblCStock(objGlobalFunctions.funGetCurrentStockForProduct(prodCode,
						// objMISHd.getStrLocFrom(), clientCode,
						// userCode,startDate,objGlobalFunctions.funGetCurrentDate("yyyy-MM-dd")));
						// Sends the model object to service layer for
						// validation,
						// data processing and then to persist
						listPhyStklist.add(PhyStkDtl);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			List list = new ArrayList<>();
			list.add("Invalid Excel File");
			list.add("Invalid Entry In Row No." + RowCount + " and Product Code " + prodCode + " ");
			return list;
		}
		return listPhyStklist;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List funMIS(HSSFSheet worksheet, HttpServletRequest request) {
		List listPhyStklist = new ArrayList<>();
		int RowCount = 0;
		String prodCode = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		
		//String prodStock=request.getParameter("prodStock");
		try {
			int i = 1;
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object for the Candidate Model
				clsMISDtlModel misDtl = new clsMISDtlModel();
				// Creates an object representing a single row in excel
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				prodCode = row.getCell(2).getStringCellValue();
				Cell c = row.getCell(4);
				if (c != null && c.getCellType() != 1) {
					if (row.getCell(4).getNumericCellValue() >= 0) {
						misDtl.setStrProdCode(row.getCell(2).getStringCellValue());
						String prodName = "";
						if (row.getCell(3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							prodName = String.valueOf(row.getCell(3).getNumericCellValue());
						} else {
							prodName = row.getCell(3).getRichStringCellValue().toString();
						}

						misDtl.setStrProdName(prodName);
						
						misDtl.setStrUOM(row.getCell(5).toString());
						
						misDtl.setDblQty(row.getCell(4).getNumericCellValue());
						
						clsProductMasterModel Prodmodel = objProductMasterService.funGetObject(prodCode, clientCode);
						misDtl.setDblUnitPrice(Prodmodel.getDblCostRM());
						
						misDtl.setDblStock(Prodmodel.getDblStock());
						misDtl.setStrRemarks(Prodmodel.getStrRemark());
						List ProdList = objGRNController.funLatestGRNProductRate(prodCode, request);

						

						// PhyStkDtl.setDblCStock(objGlobalFunctions.funGetCurrentStockForProduct(prodCode,
						// objMISHd.getStrLocFrom(), clientCode,
						// userCode,startDate,objGlobalFunctions.funGetCurrentDate("yyyy-MM-dd")));
						// Sends the model object to service layer for
						// validation,
						// data processing and then to persist
						listPhyStklist.add(misDtl);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			List list = new ArrayList<>();
			list.add("Invalid Excel File");
			list.add("Invalid Entry In Row No." + RowCount + " and Product Code " + prodCode + " ");
			return list;
		}
		return listPhyStklist;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List funPO(HSSFSheet worksheet, HttpServletRequest request) {
		List listPhyStklist = new ArrayList<>();
		int RowCount = 0;
		String prodCode = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		
		//String prodStock=request.getParameter("prodStock");
		try {
			int i = 1;
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object for the Candidate Model
				clsPurchaseOrderDtlModel objPODtlModel = new clsPurchaseOrderDtlModel();
				// Creates an object representing a single row in excel
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				prodCode = row.getCell(2).getStringCellValue();
				Cell c = row.getCell(4);
				if (c != null && c.getCellType() != 1) {
					if (row.getCell(4).getNumericCellValue() >= 0) {
						objPODtlModel.setStrProdCode(row.getCell(2).getStringCellValue());
						String prodName = "";
						if (row.getCell(3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							prodName = String.valueOf(row.getCell(3).getNumericCellValue());
						} else {
							prodName = row.getCell(3).getRichStringCellValue().toString();
						}

						objPODtlModel.setStrProdName(prodName);
						
						//objPODtlModel.setstr(row.getCell(5).toString());
						
						objPODtlModel.setDblOrdQty(row.getCell(4).getNumericCellValue());
						
						clsProductMasterModel Prodmodel = objProductMasterService.funGetObject(prodCode, clientCode);
						clsProdSuppMasterModel Prodmodel1 = objProductMasterService.funGetProdSupp(prodCode, clientCode);
						
						
						objPODtlModel.setDblAmount(Prodmodel.getDblCostRM());
						objPODtlModel.setStrUOM(Prodmodel.getStrUOM());
						//objPODtlModel.setStrSuppCode(Prodmodel.getstrsu);
						objPODtlModel.setDblWeight(Prodmodel.getDblWeight());
						//objPODtlModel.setDblPrice(Prodmodel.getDblUnitPrice());
						objPODtlModel.setStrSuppCode(Prodmodel1.getStrSuppCode());
						objPODtlModel.setStrSuppName(Prodmodel1.getStrSuppName());
						
						objPODtlModel.setStrRemarks(Prodmodel.getStrRemark());
						objPODtlModel.setDblWeight(Prodmodel.getDblWeight());
						
						
						List ProdList = objGRNController.funLatestGRNProductRate(prodCode, request);

						

						// PhyStkDtl.setDblCStock(objGlobalFunctions.funGetCurrentStockForProduct(prodCode,
						// objMISHd.getStrLocFrom(), clientCode,
						// userCode,startDate,objGlobalFunctions.funGetCurrentDate("yyyy-MM-dd")));
						// Sends the model object to service layer for
						// validation,
						// data processing and then to persist
						listPhyStklist.add(objPODtlModel);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			List list = new ArrayList<>();
			list.add("Invalid Excel File");
			list.add("Invalid Entry In Row No." + RowCount + " and Product Code " + prodCode + " ");
			return list;
		}
		return listPhyStklist;
	}

	
	/**
	 * Location Master Reorder Level Import
	 * 
	 * @param worksheet
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List funLocMastReOrderLvl(HSSFSheet worksheet, HttpServletRequest request) {
		List listReoderLvllist = new ArrayList<>();
		int RowCount = 0;
		String prodCode = "";
		try {
			int i = 1;
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object for the Candidate Model

				List ReorderLvlList = new ArrayList<>();
				// Creates an object representing a single row in excel
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				String ReOrderlvl = String.valueOf(row.getCell(7).getNumericCellValue());
				String ReOrderQty = String.valueOf(row.getCell(8).getNumericCellValue());
				if (!ReOrderlvl.equals("") || !ReOrderQty.equals("")) {
					RowCount = row.getRowNum();
					ReorderLvlList.add(row.getCell(4).getStringCellValue());
					prodCode = row.getCell(4).getStringCellValue();
					String prodName = "";
					if (row.getCell(5).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
						prodName = String.valueOf(row.getCell(5).getNumericCellValue());
					} else {
						prodName = row.getCell(5).getRichStringCellValue().toString();
					}
					ReorderLvlList.add(prodName);
					ReorderLvlList.add(row.getCell(7).getNumericCellValue());
					ReorderLvlList.add(row.getCell(8).getNumericCellValue());
					// Sends the model object to service layer for validation,
					// data processing and then to persist
					listReoderLvllist.add(ReorderLvlList);
				}
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			List list = new ArrayList<>();
			list.add("Invalid Excel File");
			list.add("Invalid Entry In Row No." + RowCount + " and Product Code " + prodCode + " ");
			return list;
		}
		return listReoderLvllist;
	}

	private String funGetProductUOM(String prodCode, String UOMType, HttpServletRequest request) {
		String UOM = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String hql = "from clsProductMasterModel a  where a.strProdCode='" + prodCode + "'  and a.strClientCode='" + clientCode + "' ";

		List list = objGlobalFunctionsService.funGetList(hql, "hql");
		clsProductMasterModel prodModel = (clsProductMasterModel) list.get(0);
		if (UOMType.equals("RecUOM")) {
			UOM = prodModel.getStrReceivedUOM();
		}
		if (UOMType.equals("IssueUOM")) {
			UOM = prodModel.getStrIssueUOM();
		}
		if (UOMType.equals("RecipeUOM")) {
			UOM = prodModel.getStrRecipeUOM();
		}
		return UOM;
	}

	private List funLoadPOSSalesData(HSSFSheet worksheet, HttpServletRequest request) {
		List listPOSSalelist = new ArrayList<>();
		List<clsPOSSalesDtlModel> listPOSSalesDtl = new ArrayList<clsPOSSalesDtlModel>();
		int RowCount = 0;
		String prodCode = "";
		String sql = " insert into tblpossalesdtl (strClientCode,strPOSItemCode,dblQuantity,dblRate,dteBillDate,strPOSCode,strPOSItemName,strSACode,strWSItemCode)  ";
		String sqlValues = " values ";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sf2 = new SimpleDateFormat("dd-MM-yyyy");
		Date fromDate = null;
		Date toDate = null;
		// new File("C:\\Directory1").mkdir();
		Date dte = null;
		try {
			int i = 1;
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object for the Candidate Model

				List POSDataList = new ArrayList<>();
				// Creates an object representing a single row in excel
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				clsPOSSalesDtlModel objSalesDtl = new clsPOSSalesDtlModel();
				String posCode = (row.getCell(0) != null) ? row.getCell(0).getStringCellValue() : "";
				String itemCode = (row.getCell(1) != null) ? row.getCell(1).getStringCellValue() : "";
				String itemName = (row.getCell(2) != null) ? row.getCell(2).getStringCellValue() : "";
				String qty = (row.getCell(3) != null) ? String.valueOf(row.getCell(3).getNumericCellValue()) : "";
				String rate = (row.getCell(4) != null) ? String.valueOf(row.getCell(4).getNumericCellValue()) : "";
				String billDate = (row.getCell(5) != null) ? row.getCell(5).toString() : "";
				if (!posCode.equals("") && !itemName.equals("") && !qty.equals("") && !rate.equals("") && !billDate.equals("")) {
					dte = new Date(billDate);

					if (i == 2) {
						fromDate = dte;
						toDate = dte;
					}
					if (dte.compareTo(fromDate) < 0) {
						fromDate = dte;
					}
					if (dte.compareTo(toDate) > 0) {
						toDate = dte;
					}

					billDate = sf.format(dte);
					// System.out.println(sf.parse(billDate));
					if (!posCode.equals("") && !itemName.equals("") && !qty.equals("") && !rate.equals("")) {
						objSalesDtl.setStrPOSCode(posCode);
						objSalesDtl.setStrPOSItemCode(itemCode);
						objSalesDtl.setStrPOSItemName(itemName);
						objSalesDtl.setDblQuantity(Double.parseDouble(qty));
						objSalesDtl.setDblRate(Double.parseDouble(rate));
						objSalesDtl.setDteBillDate(billDate);
						listPOSSalesDtl.add(objSalesDtl);
						sqlValues += " ('" + clientCode + "','" + itemCode + "','" + qty + "','" + rate + "','" + billDate + "','" + posCode + "','" + itemName + "','',''),";
					}

				}
			}

			if (listPOSSalesDtl.size() > 0) {
				sqlValues = sqlValues.substring(0, sqlValues.length() - 1);
				objPOSLinkUpService.funExecute(sql + sqlValues);
				listPOSSalelist.add(listPOSSalesDtl);
				listPOSSalelist.add(sf2.format(fromDate));
				listPOSSalelist.add(sf2.format(toDate));
				System.out.println("fromDate==" + sf2.format(fromDate) + "---------" + "ToDate==" + sf2.format(toDate));
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			List list = new ArrayList<>();
			list.add("Invalid Excel File");
			list.add("Invalid Entry In Row No." + RowCount + " and Product Code " + prodCode + " ");
			return list;
		}
		return listPOSSalelist;
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List funMaterialReq(HSSFSheet worksheet, HttpServletRequest request) {
		List listMaterial = new ArrayList<>();
		int RowCount = 0;
		String prodCode = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		
		//String prodStock=request.getParameter("prodStock");
		try {
			int i = 1;
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object for the Candidate Model
				clsRequisitionDtlModel PhyStkDtl = new clsRequisitionDtlModel();
				// Creates an object representing a single row in excel
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				prodCode = row.getCell(2).getStringCellValue();
				Cell c = row.getCell(4);
				if (c != null && c.getCellType() != 1) {
					if (row.getCell(4).getNumericCellValue() >= 0) {
						PhyStkDtl.setStrProdCode(row.getCell(2).getStringCellValue());
						String prodName = "";
						if (row.getCell(3).getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
							prodName = String.valueOf(row.getCell(3).getNumericCellValue());
						} else {
							prodName = row.getCell(3).getRichStringCellValue().toString();
						}

						PhyStkDtl.setStrProdName(prodName);
						
							PhyStkDtl.setStrUOM(row.getCell(5).toString());
						
							PhyStkDtl.setDblQty(row.getCell(4).getNumericCellValue());
						
						clsProductMasterModel Prodmodel = objProductMasterService.funGetObject(prodCode, clientCode);
						PhyStkDtl.setDblUnitPrice(Prodmodel.getDblCostRM());
						PhyStkDtl.setStrUOM(Prodmodel.getStrUOM());
						
						//PhyStkDtl.setdb(Prodmodel.getDblWeight());

						List ProdList = objGRNController.funLatestGRNProductRate(prodCode, request);

						

						// PhyStkDtl.setDblCStock(objGlobalFunctions.funGetCurrentStockForProduct(prodCode,
						// objMISHd.getStrLocFrom(), clientCode,
						// userCode,startDate,objGlobalFunctions.funGetCurrentDate("yyyy-MM-dd")));
						// Sends the model object to service layer for
						// validation,
						// data processing and then to persist
						listMaterial.add(PhyStkDtl);
					}
				}
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			List list = new ArrayList<>();
			list.add("Invalid Excel File");
			list.add("Invalid Entry In Row No." + RowCount + " and Product Code " + prodCode + " ");
			return list;
		}
		return listMaterial;
	}

	
	/*
	 * End WebStock Import
	 */
	
	/*PMS Import*/
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/GuestMasterExport", method = RequestMethod.GET)
	public ModelAndView funPMSGuestExcelExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		
		//String LocCode = "";
		List list = new ArrayList<>();
		List AllGuestlist= new ArrayList();
		List DataGuestList=null;
		clsGuestMasterBean objBean=null;
		String header = "Guest Code,GuestPrefix,First Name,Middle Name,Last Name,Gender,DOB,Designation,Address,City,State,Country,Nationality,PinCode,MobileNo,FaxNo,EmailId,PANNo,ArrivalFrom,ProceedingTo,Status,VisitingType,PassportNo,PassportIssueDate,PassportExpiryDate,Corporate,UserCreated,UserEdited,DateCreated,DateEdited,ClientCode,GSTNo,UIDNo,AnniversaryDate,DefaultAddr,AddressLocal,CityLocal,StateLocal,CountryLocal,PinCodeLocal,AddrPermanent,CityPermanent,StatePermanent,CountryPermanent,PinCodePermanent,AddressOfc,CityOfc,StateOfc,CountryOfc,PinCodeOfc";
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		try{
		String sql="select * "
				+ " from tblguestmaster a where a.strClientCode='"+clientCode+"';";
	            
		list=objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		if(!list.isEmpty())
	   {
			for (int i = 0; i < list.size(); i++)
			{
	             Object[] obj = (Object[]) list.get(i);
	             DataGuestList=new ArrayList<>();
	             DataGuestList.add(obj[0].toString());
	             DataGuestList.add(obj[1].toString());
	             DataGuestList.add(obj[2].toString());
	             DataGuestList.add(obj[3].toString());
	             DataGuestList.add(obj[4].toString());
	             DataGuestList.add(obj[5].toString());
	             DataGuestList.add(objGlobalFunctions.funGetDate("dd-MM-yyyy",obj[6].toString()));
	             DataGuestList.add(obj[7].toString());
	             DataGuestList.add(obj[8].toString());
	             DataGuestList.add(obj[9].toString());
	             DataGuestList.add(obj[10].toString());
	             DataGuestList.add(obj[11].toString());
	             DataGuestList.add(obj[12].toString());
	             DataGuestList.add(obj[13].toString());
	             
	             DataGuestList.add(obj[14].toString());
	             DataGuestList.add(obj[15].toString());
	             DataGuestList.add(obj[16].toString());
	             DataGuestList.add(obj[17].toString());
	             DataGuestList.add(obj[18].toString());
	             DataGuestList.add(obj[19].toString());
	             DataGuestList.add(obj[20].toString());
	             DataGuestList.add(obj[21].toString());
	             DataGuestList.add(obj[22].toString());
	             DataGuestList.add(objGlobalFunctions.funGetDate("dd-MM-yyyy",obj[23].toString()));
	             DataGuestList.add(objGlobalFunctions.funGetDate("dd-MM-yyyy",obj[24].toString()));
	             DataGuestList.add(obj[25].toString());
	             DataGuestList.add(obj[26].toString());
	             DataGuestList.add(obj[27].toString());
	             DataGuestList.add(objGlobalFunctions.funGetDate("dd-MM-yyyy",obj[28].toString()));
	             DataGuestList.add(objGlobalFunctions.funGetDate("dd-MM-yyyy",obj[29].toString()));
	             DataGuestList.add(obj[30].toString());
	             DataGuestList.add(obj[31].toString());
	             DataGuestList.add(obj[32].toString());
	             DataGuestList.add(objGlobalFunctions.funGetDate("dd-MM-yyyy",obj[33].toString()));
	             DataGuestList.add(obj[34].toString());
	             DataGuestList.add(obj[35].toString());
	             DataGuestList.add(obj[36].toString());
	             DataGuestList.add(obj[37].toString());
	             DataGuestList.add(obj[38].toString());
	             DataGuestList.add(obj[39].toString());
	             DataGuestList.add(obj[40].toString());
	             DataGuestList.add(obj[41].toString());
	             DataGuestList.add(obj[42].toString());
	             DataGuestList.add(obj[43].toString());
	             DataGuestList.add(obj[44].toString());
	             DataGuestList.add(obj[45].toString());
	             DataGuestList.add(obj[46].toString());
	             DataGuestList.add(obj[47].toString());
	             DataGuestList.add(obj[48].toString());
	             DataGuestList.add(obj[49].toString());

	             
	             AllGuestlist.add(DataGuestList);
			}
		}
		//
		
		}
		catch(Exception ex){
			ex.printStackTrace();
			}
		ExportList.add(AllGuestlist);
		
		return new ModelAndView("excelView", "stocklist", ExportList);
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/RoomMasterImport", method = RequestMethod.GET)
	public ModelAndView funPMSRoomExcelExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		
		//String LocCode = "";
		List list = new ArrayList<>();
		List AllGuestlist= new ArrayList();
		List DataGuestList=null;
		clsGuestMasterBean objBean=null;
		String header = "Room Number,Room Type Desc,Rate";
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		try{
		String sql="select a.strRoomDesc,a.strRoomTypeDesc,b.dblRoomTerrif from tblroom a,tblroomtypemaster b  where a.strRoomTypeCode=b.strRoomTypeCode and a.strClientCode='"+clientCode+"'";
	            
		list=objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		if(!list.isEmpty())
	   {
			for (int i = 0; i < list.size(); i++)
			{
	             Object[] obj = (Object[]) list.get(i);
	             DataGuestList=new ArrayList<>();
	             DataGuestList.add(obj[0].toString());
	             DataGuestList.add(obj[1].toString());
	             DataGuestList.add(Double.parseDouble(obj[2].toString()));
	             
	             
	            

	             
	             AllGuestlist.add(DataGuestList);
			}
		}
		//
		
		}
		catch(Exception ex){
			ex.printStackTrace();
			}
		ExportList.add(AllGuestlist);
		
		return new ModelAndView("excelView", "stocklist", ExportList);
	}
	
	
@SuppressWarnings({ "rawtypes", "unchecked" })
	public List funGuestList(HSSFSheet worksheet, HttpServletRequest request) {
		List listGuestlist = new ArrayList<>();
		int RowCount = 0;
		//String prodCode = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		
		//String prodStock=request.getParameter("prodStock");
		try {
			int i = 1;
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object representing a single row in excel
				
				
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				// Creates an object for the Candidate Model
				clsGuestMasterBean objGuest= new clsGuestMasterBean();
				objGuest.setStrGuestCode(funCheckIfNullExcelData(row.getCell(0),"",row.getCell(0)));
				objGuest.setStrGuestPrefix(funCheckIfNullExcelData(row.getCell(1),"",row.getCell(1)));
				objGuest.setStrFirstName(funCheckIfNullExcelData(row.getCell(2),"",row.getCell(2)));
				objGuest.setStrMiddleName(funCheckIfNullExcelData(row.getCell(3),"",row.getCell(3)));
				objGuest.setStrLastName(funCheckIfNullExcelData(row.getCell(4),"",row.getCell(4)));
				objGuest.setStrGender(funCheckIfNullExcelData(row.getCell(5),"",row.getCell(5)));
				objGuest.setDteDOB(funCheckIfNullExcelData(row.getCell(6),"",row.getCell(6)));
				objGuest.setStrDesignation(funCheckIfNullExcelData(row.getCell(7),"",row.getCell(7)));
				objGuest.setStrAddress(funCheckIfNullExcelData(row.getCell(8),"",row.getCell(8)));
				objGuest.setStrCity(funCheckIfNullExcelData(row.getCell(9),"",row.getCell(9)));
				objGuest.setStrState(funCheckIfNullExcelData(row.getCell(10),"",row.getCell(10)));
				objGuest.setStrCountry(funCheckIfNullExcelData(row.getCell(11),"",row.getCell(11)));
				objGuest.setStrNationality(funCheckIfNullExcelData(row.getCell(12),"",row.getCell(12)));
				long pinCode =new Double(Double.parseDouble(funCheckIfNullExcelData(row.getCell(13),"",row.getCell(13)))).longValue();
				objGuest.setIntPinCode(pinCode);
				long MobileNo =new Double(Double.parseDouble(funCheckIfNullExcelData(row.getCell(14),"",row.getCell(14)))).longValue();
				objGuest.setIntMobileNo(MobileNo);
				long FaxNo =new Double(Double.parseDouble(funCheckIfNullExcelData(row.getCell(15),"",row.getCell(15)))).longValue();
				objGuest.setIntFaxNo(FaxNo);
				objGuest.setStrEmailId(funCheckIfNullExcelData(row.getCell(16),"",row.getCell(16)));
				objGuest.setStrPANNo(funCheckIfNullExcelData(row.getCell(17),"",row.getCell(17)));
				objGuest.setStrArrivalFrom(funCheckIfNullExcelData(row.getCell(18),"",row.getCell(18)));
				objGuest.setStrProceedingTo(funCheckIfNullExcelData(row.getCell(19),"",row.getCell(19)));
				objGuest.setStrStatus(funCheckIfNullExcelData(row.getCell(20),"",row.getCell(20)));
				objGuest.setStrVisitingType(funCheckIfNullExcelData(row.getCell(21),"",row.getCell(21)));
				objGuest.setStrPassportNo(funCheckIfNullExcelData(row.getCell(22),"",row.getCell(22)));
				objGuest.setDtePassportIssueDate(funCheckIfNullExcelData(row.getCell(23),"",row.getCell(23)));
				objGuest.setDtePassportExpiryDate(funCheckIfNullExcelData(row.getCell(24),"",row.getCell(24)));
				objGuest.setStrCorporate(funCheckIfNullExcelData(row.getCell(25),"",row.getCell(25)));
				objGuest.setStrUserCreated(funCheckIfNullExcelData(row.getCell(26),"",row.getCell(26)));
				objGuest.setStrUserEdited(funCheckIfNullExcelData(row.getCell(27),"",row.getCell(27)));
				objGuest.setDteDateCreated(funCheckIfNullExcelData(row.getCell(28),"",row.getCell(28)));
				objGuest.setDteDateEdited(funCheckIfNullExcelData(row.getCell(29),"",row.getCell(29)));
				objGuest.setStrClientCode(funCheckIfNullExcelData(row.getCell(30),"",row.getCell(30)));
				objGuest.setStrGSTNo(funCheckIfNullExcelData(row.getCell(31),"",row.getCell(31)));
				objGuest.setStrUIDNo(funCheckIfNullExcelData(row.getCell(32),"",row.getCell(32)));
				objGuest.setDteAnniversaryDate(funCheckIfNullExcelData(row.getCell(33),"",row.getCell(33)));
				objGuest.setStrDefaultAddr(funCheckIfNullExcelData(row.getCell(34),"",row.getCell(34)));
				objGuest.setStrAddressLocal(funCheckIfNullExcelData(row.getCell(35),"",row.getCell(35)));
				objGuest.setStrCityLocal(funCheckIfNullExcelData(row.getCell(36),"",row.getCell(36)));
				objGuest.setStrStateLocal(funCheckIfNullExcelData(row.getCell(37),"",row.getCell(37)));
				objGuest.setStrCountryLocal(funCheckIfNullExcelData(row.getCell(38),"",row.getCell(38)));
				int PinCodeLocal =new Double(Double.parseDouble(funCheckIfNullExcelData(row.getCell(39),"",row.getCell(39)))).intValue();
				objGuest.setIntPinCodeLocal(PinCodeLocal);
				objGuest.setStrAddrPermanent(funCheckIfNullExcelData(row.getCell(40),"",row.getCell(40)));
				objGuest.setStrCityPermanent(funCheckIfNullExcelData(row.getCell(41),"",row.getCell(41)));
				objGuest.setStrStatePermanent(funCheckIfNullExcelData(row.getCell(42),"",row.getCell(42)));
				objGuest.setStrCountryPermanent(funCheckIfNullExcelData(row.getCell(43),"",row.getCell(43)));
				int PinCodePermanent =new Double(Double.parseDouble(funCheckIfNullExcelData(row.getCell(44),"",row.getCell(44)))).intValue();
				objGuest.setIntPinCodePermanent(PinCodePermanent);
				objGuest.setStrAddressOfc(funCheckIfNullExcelData(row.getCell(45),"",row.getCell(45)));
				objGuest.setStrCityOfc(funCheckIfNullExcelData(row.getCell(46),"",row.getCell(46)));
				objGuest.setStrStateOfc(funCheckIfNullExcelData(row.getCell(47),"",row.getCell(47)));
				objGuest.setStrCountryOfc(funCheckIfNullExcelData(row.getCell(48),"",row.getCell(48)));
				int PinCodeOfc =new Double(Double.parseDouble(funCheckIfNullExcelData(row.getCell(49),"",row.getCell(49)))).intValue();
				objGuest.setIntPinCodeOfc(PinCodeOfc);
				

					
				
				
				listGuestlist.add(objGuest);
				clsGuestMasterHdModel objGuestMasterModel = objGuestMasterService.funPrepareGuestModel(objGuest,clientCode,userCode);
				
				
				objGuestMasterDao.funAddUpdateGuestMaster(objGuestMasterModel);

				
				
			}

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			List list = new ArrayList<>();
			list.add("Invalid Excel File");
			//list.add("Invalid Entry In Row No." + RowCount + " and Product Code " + prodCode + " ");
			return list;
		}
		return listGuestlist;
	}

private String funCheckIfNullExcelData(Cell input,String defaultValue,Cell assignedValue )
{
	String op = "notnull";
	if (null == input) {
		op = defaultValue;
	} else {
		op = input.toString();
	}
	return op;
}

}
