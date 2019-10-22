package com.sanguine.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.sanguine.bean.clsProductMasterBean;
import com.sanguine.crm.service.clsPartyMasterService;
import com.sanguine.model.clsGroupMasterModel;
import com.sanguine.model.clsGroupMasterModel_ID;
import com.sanguine.model.clsLocationMasterModel;
import com.sanguine.model.clsLocationMasterModel_ID;
import com.sanguine.model.clsMISDtlModel;
import com.sanguine.model.clsOpeningStkDtl;
import com.sanguine.model.clsPOSSalesDtlModel;
import com.sanguine.model.clsPartyTaxIndicatorDtlModel;
import com.sanguine.model.clsProdSuppMasterModel;
import com.sanguine.model.clsProductMasterModel;
import com.sanguine.model.clsProductMasterModel_ID;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.model.clsPurchaseIndentDtlModel;
import com.sanguine.model.clsPurchaseOrderDtlModel;
import com.sanguine.model.clsRequisitionDtlModel;
import com.sanguine.model.clsStkPostingDtlModel;
import com.sanguine.model.clsSubGroupMasterModel;
import com.sanguine.model.clsSubGroupMasterModel_ID;
import com.sanguine.model.clsSupplierMasterModel;
import com.sanguine.model.clsSupplierMasterModel_ID;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsGroupMasterService;
import com.sanguine.service.clsLocationMasterService;
import com.sanguine.service.clsPOSLinkUpService;
import com.sanguine.service.clsProductMasterService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.service.clsSubGroupMasterService;
import com.sanguine.service.clsSupplierMasterService;
import com.sanguine.webpms.bean.clsGuestMasterBean;
import com.sanguine.webpms.dao.clsGuestMasterDao;
import com.sanguine.webpms.dao.clsRoomTypeMasterDao;
import com.sanguine.webpms.model.clsGuestMasterHdModel;
import com.sanguine.webpms.model.clsRoomMasterModel;
import com.sanguine.webpms.model.clsRoomMasterModel_ID;
import com.sanguine.webpms.model.clsRoomTypeMasterModel;
import com.sanguine.webpms.service.clsGuestMasterService;
import com.sanguine.webpms.service.clsRoomMasterService;

@Controller
public class clsExcelExportImportController {

	@Autowired
	private clsSupplierMasterService objSupplierMasterService;
	
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
	
	@Autowired
	private clsRoomMasterService objRoomMasterService;
	
	@Autowired
	private clsProductMasterController objProductMaster ;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalService;
	
	@Autowired
	private clsLocationMasterController objLocMaster ;
	@Autowired
	private clsLocationMasterService objLocationMasterService;
	
	@Autowired
	private clsSubGroupMasterService objSubGrpMasterService;
	
	@Autowired
	private clsGroupMasterService objGrpMasterService;


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
				
			case "frmSupplieMaster":
				list = funSupplierList(worksheet, request);
				break;
				
			case "frmProductMaster":
			    list = funProductList(worksheet,request,excelfile);
			    break;
			}
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return list;
	}

	private List funSupplierList(HSSFSheet worksheet, HttpServletRequest request) {
	

		List listGuestlist = new ArrayList<>();
		int RowCount = 0;
		//String prodCode = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		
		//String prodStock=request.getParameter("prodStock");
		try {
			int i = 1;
			
			String strSupplierCode = "";
			
			HashMap<String,String> hm = new HashMap<String,String>();
			HashMap<String, String> hmRoom = new HashMap<String, String>();
			List list = new ArrayList<>();
			
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object representing a single row in excel
				
				
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				
				
				//String strSuppCode = row.getCell(0).toString();
							
				hm.put(row.getCell(1).toString(),row.getCell(2).toString());
				//hmRoom.put(row.getCell(0).toString(), row.getCell(1).toString());
				
				
				
				//list.add(strRoomName);
			}
			
			funCheckSupplier(hm,clientCode,userCode);
			
			

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

	private void funCheckSupplier(HashMap<String, String> hm,
			String clientCode, String userCode) {
		
		 for (Entry<String, String> entry : hm.entrySet())  {
			 
		 
		
		String sqlCheck = "SELECT * FROM tblpartymaster a WHERE a.strClientCode='"+clientCode+"' AND a.strPName='"+entry.getKey()+"'";
		
		List list=objGlobalFunctionsService.funGetListModuleWise(sqlCheck, "sql");
		
		if(list!=null && list.size()>0)
		{
			
			
		}
		else
		{
				clsSupplierMasterModel objModel = new clsSupplierMasterModel();					
				long lastNo = 0;

				lastNo = objGlobalFunctionsService.funGetLastNo("tblpartymaster", "SupplierMaster", "intPid", clientCode);
				String PCode = "S" + String.format("%06d", lastNo);
				objModel = new clsSupplierMasterModel(new clsSupplierMasterModel_ID(PCode, clientCode));
				objModel.setIntPId(lastNo);
				objModel.setStrUserCreated(userCode);
				objModel.setDtCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				
				objModel.setStrPName(entry.getKey());
				objModel.setStrPhone("");
				long mobileNo =new Double(Double.parseDouble(entry.getValue())).longValue(); 
						
				objModel.setStrMobile(Long.toString(mobileNo));
				objModel.setStrFax("");
				objModel.setStrContact("");
				objModel.setStrEmail("");

				objModel.setStrBankName("");
				objModel.setStrBankAdd1("");
				objModel.setStrBankAdd2("");
				objModel.setStrTaxNo1("");
				objModel.setStrTaxNo2("");
				objModel.setStrPmtTerms("");
				objModel.setStrAcCrCode("");
				objModel.setStrMAdd1("");
				objModel.setStrMAdd2("");
				objModel.setStrMCity("");
				objModel.setStrMPin("");
				objModel.setStrMState("");
				objModel.setStrMCountry("");
				objModel.setStrBAdd1("");
				objModel.setStrBAdd2("");
				objModel.setStrBCity("");
				objModel.setStrBPin("");
				objModel.setStrBState("");
				objModel.setStrBCountry("");
				objModel.setStrSAdd1("");
				objModel.setStrSAdd2("");
				objModel.setStrSCity("");
				objModel.setStrSPin("");
				objModel.setStrSState("");
				objModel.setStrSCountry("");
				objModel.setStrCST("");
				objModel.setStrVAT("");
				objModel.setStrExcise("");
				objModel.setStrServiceTax("");
				objModel.setStrSubType("");
				objModel.setDtExpiryDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				objModel.setStrManualCode("");
				objModel.setStrRegistration("");
				objModel.setStrRange("");
				objModel.setStrDivision("");
				objModel.setStrCommissionerate("");
				objModel.setStrBankAccountNo("");
				objModel.setStrBankABANo("");
				objModel.setIntCreditDays(0);
				objModel.setDblCreditLimit(0);
				objModel.setDblLatePercentage(0.0);
				objModel.setDblRejectionPercentage(0.0);
				objModel.setStrIBANNo("");
				objModel.setStrSwiftCode("");
				objModel.setStrCategory("");
				objModel.setStrExcisable("");
				objModel.setStrCategory("");
				objModel.setStrUserModified(userCode);
				objModel.setDtLastModified(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				objModel.setDtCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				objModel.setStrUserCreated(userCode);
				objModel.setStrPartyType("");
				objModel.setStrPType("Supp");
				objModel.setStrPartyIndi("");
				objModel.setStrOperational("");
				objModel.setStrAccManager("");
				objModel.setStrECCNo("");
				objModel.setDtInstallions(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				objModel.setStrGSTNo("");
				objModel.setStrLocCode("");
				objModel.setStrPropCode("");
				objModel.setStrExternalCode("");
				objModel.setStrCurrency("");
				objModel.setStrComesaRegion("");
				objModel.setStrDebtorCode("");
				
				List<clsPartyTaxIndicatorDtlModel> listPartyTaxDtl = new ArrayList<clsPartyTaxIndicatorDtlModel>();
				
				objModel.setArrListPartyTaxDtlModel(listPartyTaxDtl);
				
				objSupplierMasterService.funAddUpdate(objModel);
				
		}
	}
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
			HashMap<String, String> hmRoom = new HashMap<String, String>();
			List list = new ArrayList<>();
			
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object representing a single row in excel
				
				
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				
				strRoomName = row.getCell(0).toString();
				objModel = new clsRoomMasterModel();
							
				hm.put(row.getCell(1).toString(),Double.parseDouble(row.getCell(2).toString()));
				hmRoom.put(row.getCell(0).toString(), row.getCell(1).toString());
				
				
				
				//list.add(strRoomName);
			}
			
			funCheckRoomType(hm,clientCode,userCode);
			
			funCheckRoom(hmRoom,clientCode,userCode);

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

	

	private void funCheckRoom(HashMap<String, String> hm, String clientCode,
			String userCode) {
		
		 for (Map.Entry<String,String> entry : hm.entrySet())  {
		 
			 String sqlData = "select * from tblroom a where a.strRoomDesc='"+entry.getKey()+"' and a.strClientCode='"+clientCode+"'";
			 
			 List list=objGlobalFunctionsService.funGetListModuleWise(sqlData, "sql");
				
				if(list!=null && list.size()>0)
				{
					for (int cnt = 0; cnt < list.size(); cnt++) {
						Object[] arrObj = (Object[]) list.get(cnt);
						String strRoomCode = arrObj[0].toString();
						
						long lastNo = 0;
						clsRoomMasterModel objModel;
						if (strRoomCode.trim().length() == 0) {
							lastNo = objGlobalFunctionsService.funGetPMSMasterLastNo("tblroom", "RoomMaster", "strRoomCode", clientCode);
							String roomCode = "RC" + String.format("%06d", lastNo);
							objModel = new clsRoomMasterModel(new clsRoomMasterModel_ID(roomCode, clientCode));
						} else {
							objModel = new clsRoomMasterModel(new clsRoomMasterModel_ID(strRoomCode, clientCode));
						}
						objModel.setStrRoomDesc(arrObj[1].toString());
						objModel.setStrRoomTypeCode(arrObj[2].toString());
						objModel.setStrFloorCode(arrObj[3].toString());
						objModel.setStrBedType(arrObj[4].toString());
						objModel.setStrFurniture(arrObj[5].toString());
						objModel.setStrExtraBedCode(arrObj[6].toString());
						objModel.setStrUpholstery(arrObj[7].toString());
						objModel.setStrLocation(arrObj[8].toString());
						objModel.setStrBathTypeCode(arrObj[9].toString());
						objModel.setStrColorScheme(arrObj[10].toString());
						objModel.setStrPolishType(arrObj[11].toString());
						objModel.setStrGuestAmenities(arrObj[12].toString());
						objModel.setStrInterConnectRooms(arrObj[13].toString());
						objModel.setStrProvisionForSmokingYN(arrObj[14].toString());
						objModel.setStrDeactiveYN(arrObj[15].toString());
						objModel.setStrUserCreated(arrObj[16].toString());
						objModel.setDteDateCreated(arrObj[18].toString());
						objModel.setStrUserEdited(userCode);
						objModel.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
						objModel.setStrStatus("Free");
						objModel.setStrAccountCode(arrObj[22].toString());
						objModel.setStrRoomTypeDesc(arrObj[23].toString());
						
						objRoomMasterService.funAddUpdateRoomMaster(objModel);
						
					}
						
					}
				
				else
				{
					 String sqlNewData = "select * from tblroomtypemaster a where a.strRoomTypeDesc='"+entry.getValue()+"' and a.strClientCode='"+clientCode+"'";
					 
					 List listNew=objGlobalFunctionsService.funGetListModuleWise(sqlNewData, "sql");
					
					 if(listNew!=null && listNew.size()>0)
					 {
						 for (int cnt = 0; cnt < listNew.size(); cnt++) {
								Object[] arrObj = (Object[]) listNew.get(cnt);
								
								long lastNo = 0;
								clsRoomMasterModel objModel;
								
									lastNo = objGlobalFunctionsService.funGetPMSMasterLastNo("tblroom", "RoomMaster", "strRoomCode", clientCode);
									String roomCode = "RC" + String.format("%06d", lastNo);
									objModel = new clsRoomMasterModel(new clsRoomMasterModel_ID(roomCode, clientCode));
								
									
								
								objModel.setStrRoomDesc(entry.getKey().toString());
								objModel.setStrRoomTypeCode(arrObj[0].toString());
								objModel.setStrFloorCode("");
								objModel.setStrBedType("");
								objModel.setStrFurniture("");
								objModel.setStrExtraBedCode("");
								objModel.setStrUpholstery("");
								objModel.setStrLocation("");
								objModel.setStrBathTypeCode("");
								objModel.setStrColorScheme("");
								objModel.setStrPolishType("");
								objModel.setStrGuestAmenities("");
								objModel.setStrInterConnectRooms("");
								objModel.setStrProvisionForSmokingYN("");
								objModel.setStrDeactiveYN("");
								objModel.setStrUserCreated(arrObj[3].toString());
								objModel.setDteDateCreated(arrObj[5].toString());
								objModel.setStrUserEdited(userCode);
								objModel.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
								objModel.setStrStatus("Free");
								objModel.setStrAccountCode("");
								objModel.setStrRoomTypeDesc(arrObj[1].toString());
								
								objRoomMasterService.funAddUpdateRoomMaster(objModel);
						 }
					 }
				}
			}
	
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
		String header = "Guest Code,First Name,Middle Name,Last Name,Gender,MobileNo";
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		try{
		String sql="SELECT a.strGuestCode,a.strFirstName,a.strMiddleName,a.strLastName,a.strGender,a.lngMobileNo "
				+ "FROM tblguestmaster a "
				+ "WHERE a.strClientCode='"+clientCode+"';";
	            
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
	             long mobNo = new Double(Double.parseDouble(obj[5].toString())).longValue(); 
	            		 
	             DataGuestList.add(mobNo);	           
	            

	             
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
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/SupplierMasterImport", method = RequestMethod.GET)
	public ModelAndView funSupplierMasterExport(HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		
		//String LocCode = "";
		List list = new ArrayList<>();
		List AllGuestlist= new ArrayList();
		List DataGuestList=null;
		clsGuestMasterBean objBean=null;
		String header = "Code,Supplier Name,Mobile Number";
		List ExportList = new ArrayList();
		String[] ExcelHeader = header.split(",");
		ExportList.add(ExcelHeader);
		try{
		String sql="select a.strPCode, a.strPName ,a.strMobile from tblpartymaster a where a.strClientCode='"+clientCode+"'";
	            
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
			List<clsGuestMasterBean> listData = new ArrayList<clsGuestMasterBean>();
			while (i <= worksheet.getLastRowNum()) {
				// Creates an object representing a single row in excel
				
				
				HSSFRow row = worksheet.getRow(i++);
				// Sets the Read data to the model class
				RowCount = row.getRowNum();
				// Creates an object for the Candidate Model
				clsGuestMasterBean objBean = new clsGuestMasterBean();
				if(row.getCell(0)==null)
				{
					objBean.setStrGuestCode("");
				}
				else
				{
				objBean.setStrGuestCode(row.getCell(0).toString());
				}
				if(row.getCell(1)==null)
				{
					objBean.setStrFirstName("");
				}
				else
				{
				objBean.setStrFirstName(row.getCell(1).toString());
				}
				if(row.getCell(2)==null)
				{
					objBean.setStrMiddleName("");
				}
				else
				{
				objBean.setStrMiddleName(row.getCell(2).toString());
				}
				if(row.getCell(3)==null)
				{
					objBean.setStrLastName("");
				}
				else
				{
				objBean.setStrLastName(row.getCell(3).toString());
				}
				if(row.getCell(4)==null)
				{
					objBean.setStrGender("");
				}
				else
				{
				objBean.setStrGender(row.getCell(4).toString());
				}
				if(row.getCell(5)==null)
				{
					objBean.setIntMobileNo(0);
				}
				else
				{
				long mobNo = new Double(Double.parseDouble(row.getCell(5).toString())).longValue(); 
				objBean.setIntMobileNo(mobNo);
				}
				listData.add(objBean);
			/*	Guest Code,First Name,Middle Name,Last Name,Gender,MobileNo*/

				
				
			}
			funAddGuestData(listData,clientCode,userCode);

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

private void funAddGuestData(List listData, String clientCode, String userCode) {
	
	
	for (int i = 0; i < listData.size(); i++) {
		clsGuestMasterBean objGuestMasterBean = (clsGuestMasterBean) listData.get(i);
		
		String sql = "select * from tblguestmaster a where a.strGuestCode='"+objGuestMasterBean.getStrGuestCode()+"' and a.strClientCode='"+clientCode+"'";
		List list = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		if(list!=null && list.size()>0)
		{
			
		}
		else
		{			
			clsGuestMasterHdModel objGuestMasterModel = new clsGuestMasterHdModel();
			
			long lastNo = 0;
			lastNo = objGlobalFunctionsService.funGetPMSMasterLastNo("tblguestmaster", "GuestMaster", "strGuestCode", clientCode);
			String guestCode = "GT" + String.format("%06d", lastNo);
			objGuestMasterModel.setStrGuestCode(guestCode);
			objGuestMasterModel.setStrUserCreated(userCode);
			objGuestMasterModel.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			
			
			objGuestMasterModel.setStrGuestPrefix("");
			objGuestMasterModel.setStrFirstName(objGuestMasterBean.getStrFirstName());
			objGuestMasterModel.setStrMiddleName(objGuestMasterBean.getStrMiddleName());
			objGuestMasterModel.setStrLastName(objGuestMasterBean.getStrLastName());
			objGuestMasterModel.setStrGender(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrGender(), "M", objGuestMasterBean.getStrGender()));
			objGuestMasterModel.setStrDesignation(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrDesignation(), "NA", objGuestMasterBean.getStrDesignation()));
			if (null == objGuestMasterBean.getDteDOB()) {
				objGuestMasterModel.setDteDOB("1900-01-01");
			} else {
				
				objGuestMasterModel.setDteDOB(objGlobalFunctions.funGetDate("yyyy-MM-dd", objGuestMasterBean.getDteDOB()));
				
			}

			// objGuestMasterModel.setDteDOB(objGlobal.funIfNull(objGuestMasterBean.getDteDOB(),"1900-01-01",objGuestMasterBean.getDteDOB()));
			objGuestMasterModel.setStrAddress(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrAddress(), "NA", objGuestMasterBean.getStrAddress()));
			objGuestMasterModel.setStrCity(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrCity(), "NA", objGuestMasterBean.getStrCity()));
			objGuestMasterModel.setStrState(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrState(), "NA", objGuestMasterBean.getStrState()));
			objGuestMasterModel.setStrCountry(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrCountry(), "NA", objGuestMasterBean.getStrCountry()));
			objGuestMasterModel.setStrNationality(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrNationality(), "NA", objGuestMasterBean.getStrNationality()));
			objGuestMasterModel.setIntPinCode(objGuestMasterBean.getIntPinCode());
			objGuestMasterModel.setLngMobileNo(objGuestMasterBean.getIntMobileNo());
			objGuestMasterModel.setLngFaxNo(objGuestMasterBean.getIntFaxNo());
			objGuestMasterModel.setStrEmailId(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrEmailId(), "NA", objGuestMasterBean.getStrEmailId()));
			objGuestMasterModel.setStrPANNo(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrPANNo(), "NA", objGuestMasterBean.getStrPANNo()));
			objGuestMasterModel.setStrArrivalFrom(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrArrivalFrom(), "NA", objGuestMasterBean.getStrArrivalFrom()));
			objGuestMasterModel.setStrProceedingTo(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrProceedingTo(), "NA", objGuestMasterBean.getStrProceedingTo()));
			objGuestMasterModel.setStrStatus(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrStatus(), "NA", objGuestMasterBean.getStrStatus()));
			objGuestMasterModel.setStrVisitingType(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrVisitingType(), "NA", objGuestMasterBean.getStrVisitingType()));
			objGuestMasterModel.setStrCorporate(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrCorporate(), "N", objGuestMasterBean.getStrCorporate()));
			objGuestMasterModel.setStrPassportNo(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrPassportNo(), "NA", objGuestMasterBean.getStrPassportNo()));
			objGuestMasterModel.setStrUserEdited(userCode);
			objGuestMasterModel.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objGuestMasterModel.setStrProceedingTo(objGlobalFunctions.funIfNull(objGuestMasterBean.getStrProceedingTo(), "NA", objGuestMasterBean.getStrProceedingTo()));

			if (null == objGuestMasterBean.getDtePassportExpiryDate()) {
				objGuestMasterModel.setDtePassportExpiryDate("1900-01-01");
			} else {
				objGuestMasterModel.setDtePassportExpiryDate(objGlobalFunctions.funGetDate("yyyy-MM-dd", objGuestMasterBean.getDtePassportExpiryDate()));
			}

			if (null == objGuestMasterBean.getDtePassportIssueDate()) {
				objGuestMasterModel.setDtePassportIssueDate("1900-01-01");
			} else {
				objGuestMasterModel.setDtePassportIssueDate(objGlobalFunctions.funGetDate("yyyy-MM-dd", objGuestMasterBean.getDtePassportIssueDate()));
			}

			objGuestMasterModel.setStrGSTNo(objGuestMasterBean.getStrGSTNo());
			objGuestMasterModel.setStrUIDNo(objGuestMasterBean.getStrUIDNo());
			
			if (null == objGuestMasterBean.getDteAnniversaryDate()) {
				objGuestMasterModel.setDteAnniversaryDate("1900-01-01");
			} else {
				objGuestMasterModel.setDteAnniversaryDate(objGlobalFunctions.funGetDate("yyyy-MM-dd", objGuestMasterBean.getDteAnniversaryDate()));
			}
			
			objGuestMasterModel.setStrDefaultAddr(objGuestMasterBean.getStrDefaultAddr());
			
			objGuestMasterModel.setStrAddressLocal(objGuestMasterBean.getStrAddressLocal());
			objGuestMasterModel.setStrCityLocal(objGuestMasterBean.getStrCityLocal());
			objGuestMasterModel.setStrStateLocal(objGuestMasterBean.getStrStateLocal());
			objGuestMasterModel.setStrCountryLocal(objGuestMasterBean.getStrCountryLocal());
			objGuestMasterModel.setIntPinCodeLocal(objGuestMasterBean.getIntPinCodeLocal());
			
			objGuestMasterModel.setStrAddrPermanent(objGuestMasterBean.getStrAddrPermanent());
			objGuestMasterModel.setStrCityPermanent(objGuestMasterBean.getStrCityPermanent());
			objGuestMasterModel.setStrStatePermanent(objGuestMasterBean.getStrStatePermanent());
			objGuestMasterModel.setStrCountryPermanent(objGuestMasterBean.getStrCountryPermanent());
			objGuestMasterModel.setIntPinCodePermanent(objGuestMasterBean.getIntPinCodePermanent());
			
			objGuestMasterModel.setStrAddressOfc(objGuestMasterBean.getStrAddressOfc());
			objGuestMasterModel.setStrCityOfc(objGuestMasterBean.getStrCityOfc());
			objGuestMasterModel.setStrStateOfc(objGuestMasterBean.getStrStateOfc());
			objGuestMasterModel.setStrCountryOfc(objGuestMasterBean.getStrCountryOfc());
			objGuestMasterModel.setIntPinCodeOfc(objGuestMasterBean.getIntPinCodeOfc());

			// objGuestMasterModel.setDtePassportExpiryDate(objGlobal.funIfNull(objGuestMasterBean.getDtePassportExpiryDate(),"1900-01-01",objGuestMasterBean.getDtePassportExpiryDate()));
			// objGuestMasterModel.setDtePassportIssueDate(objGlobal.funIfNull(objGuestMasterBean.getDtePassportIssueDate(),"1900-01-01",objGuestMasterBean.getDtePassportIssueDate()));
			objGuestMasterModel.setStrClientCode(clientCode);
			
			objGuestMasterDao.funAddUpdateGuestMaster(objGuestMasterModel);
			
		
		}
	}
	
	
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

@SuppressWarnings({ "unchecked", "rawtypes" })
@RequestMapping(value = "/ProductMasterExport", method = RequestMethod.GET)
public ModelAndView funProductMasterExcelExport(HttpServletRequest request) {
	String clientCode = request.getSession().getAttribute("clientCode").toString();

	List list = new ArrayList<>();
	List ProductMasterlist = new ArrayList();
	
	String header = "Product Name,Subgroup,Group,Location,Part No,RecivedUOM,Prod Type,Cost RM,Cost Manu,OrduptoLvl,ReorderLvl,NotInUse,ExpDate,LotNo,RevLevel,SlNo,ForSale,SaleNo,Desc,UnitPrice,Tax Indicator,ExceedPO,StagDel,DelPeriod,Type,Specification,Weight,BomCal,WtUOM,CalAmtOn,Class,BatchQty,MaxLvl,BinNo,TariffNo,ListPrice,Remark,ReceivedUOM,IssueUOM,RecipeUOM,ReceiveConversion,IssueConversion,RecipeConversion,BarCode,Manufacture";
		

	List ExportList = new ArrayList();
	String[] ExcelHeader = header.split(",");
	ExportList.add(ExcelHeader);

	String sql = "SELECT a.strProdName,b.strSGName,d.strGName, IFNULL(c.strLocName,'LocCode'),if(a.strPartNo='','partNo',a.strPartNo), "
				+" if(a.strUOM='','UOM',a.strUOM),a.strProdType,a.dblCostRM,  "
				+" a.dblCostManu, a.dblOrduptoLvl,a.dblReorderLvl,if(a.strNotInUse='','NotInUse',a.strNotInUse), "
				+" if(a.strExpDate='','ExpDte',a.strExpDate),if(a.strLotNo='','LotNo',a.strLotNo),if(a.strRevLevel='','RevLevel',a.strRevLevel),if(a.strSlNo='','SINo',a.strSlNo),if(a.strForSale='','Forsale',a.strForSale),if(a.strSaleNo='','SaleNo',a.strSaleNo), "
				+" if(a.strDesc='','Desc',a.strDesc),a.dblUnitPrice,if(a.strTaxIndicator='','Tax',a.strTaxIndicator),if(a.strExceedPO='','PO',a.strExceedPO),if(a.strStagDel='','StagDel',a.strStagDel),if(a.intDelPeriod='','delPeriod',a.intDelPeriod), "
				+" if(a.strType='','Type',a.strType),if(a.strSpecification='','Speci',a.strSpecification),a.dblWeight,if( a.strBomCal='','Cal',a.strBomCal),if(a.strWtUOM='','WTUOM',a.strWtUOM),if(a.strCalAmtOn='','CalAmt',a.strCalAmtOn),if(a.strClass='','Class',a.strClass),a.dblBatchQty,a.dblMaxLvl, "
				+" if(a.strBinNo='','BinNo',a.strBinNo),if(a.strTariffNo='','Tariff',a.strTariffNo),a.dblListPrice,if(a.strRemark='','Remark',a.strRemark), a.strReceivedUOM,a.strIssueUOM,a.strRecipeUOM,a.dblReceiveConversion,a.dblIssueConversion,a.dblRecipeConversion,if(a.strBarCode='','BarCode',a.strBarCode), "
				+" if(a.strManufacturerCode='','MCode',a.strManufacturerCode)  "
				+" FROM tblproductmaster a  "
				+" LEFT OUTER JOIN tblsubgroupmaster b ON a.strSGCode=b.strSGCode  "
				+" LEFT OUTER JOIN tblgroupmaster d ON b.strGCode=d.strGCode  "
				+" LEFT OUTER JOIN tbllocationmaster c ON a.strLocCode=c.strLocCode  "
				+" WHERE a.strClientCode='"+clientCode+"' ";
	list = objGlobalFunctionsService.funGetList(sql, "sql");
	
	for (int i = 0; i < list.size(); i++) {
		Object[] ob = (Object[]) list.get(i);
		List DataList = new ArrayList<>();
		DataList.add(ob[0]);
		DataList.add(ob[1]);
		DataList.add(ob[2]);
		if(ob[3].toString().equalsIgnoreCase("LocCode"))
		{
		DataList.add(" ");
		}
		else{
			DataList.add(ob[3]);
			}
	    if(ob[4].toString().equalsIgnoreCase("partNo"))
		{
		DataList.add(" ");	
		}else{
		DataList.add(ob[4]);
		}
	    if(ob[5].toString().equalsIgnoreCase("UOM"))
	    {
	    	DataList.add(" ");		
	    }else{
	    	DataList.add(ob[5]);	
	    }
		
		DataList.add(ob[6]);
		DataList.add(ob[7]);
		DataList.add(ob[8]);
		DataList.add(ob[9]);
		DataList.add(ob[10]);
		if(ob[11].toString().equalsIgnoreCase("NotInUse"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[11]);	
		}
		if(ob[12].toString().equalsIgnoreCase("ExpDte"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[12]);
		}
		if(ob[13].toString().equalsIgnoreCase("LotNo"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[13]);

		}
		if(ob[14].toString().equalsIgnoreCase("RevLevel"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[14]);
		}
		if(ob[15].toString().equalsIgnoreCase("SINo"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[15]);
		}
		if(ob[16].toString().equalsIgnoreCase("Forsale"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[16]);
		}
		if(ob[17].toString().equalsIgnoreCase("SaleNo"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[17]);
		}
		if(ob[18].toString().equalsIgnoreCase("Desc"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[18]);
		}
		DataList.add(ob[19]);
		if(ob[20].toString().equalsIgnoreCase("Tax"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[20]);
		}
		if(ob[21].toString().equalsIgnoreCase("PO"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[21]);
		}
		if(ob[22].toString().equalsIgnoreCase("StagDel"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[22]);
		}
		if(ob[23].toString().equalsIgnoreCase("delPeriod"))
		{
			DataList.add(0);	
		}else{
			DataList.add(ob[23]);
		}
		if(ob[24].toString().equalsIgnoreCase("Type"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[24]);
		}
		if(ob[25].toString().equalsIgnoreCase("Speci"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[25]);
		}
		DataList.add(ob[26]);
		if(ob[27].toString().equalsIgnoreCase("Cal"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[27]);
		}
		if(ob[28].toString().equalsIgnoreCase("WTUOM"))
		{
			DataList.add(" ");
		}else{
			DataList.add(ob[28]);
		}
		if(ob[29].toString().equalsIgnoreCase("CalAmt"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[29]);
		}
		if(ob[30].toString().equalsIgnoreCase("Class"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[30]);
		}
		DataList.add(ob[31]);
		DataList.add(ob[32]);
		if(ob[33].toString().equalsIgnoreCase("BinNo"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[33]);
		}
		if(ob[34].toString().equalsIgnoreCase("Tariff"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[34]);
		}
		DataList.add(ob[35]);
		if(ob[36].toString().equalsIgnoreCase("Remark"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[36]);
		}
		DataList.add(ob[37]);
		DataList.add(ob[38]);
		DataList.add(ob[39]);	
		DataList.add(ob[40]);
		DataList.add(ob[41]);
		DataList.add(ob[42]);
		if(ob[43].toString().equalsIgnoreCase("BarCode"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[43]);
		}
		if(ob[44].toString().equalsIgnoreCase("MCode"))
		{
			DataList.add(" ");	
		}else{
			DataList.add(ob[44]);
		}
		
		
		
		ProductMasterlist.add(DataList);
	}
	ExportList.add(ProductMasterlist);
	return new ModelAndView("excelView", "stocklist", ExportList);
}

@SuppressWarnings({ "rawtypes", "unchecked" })
public List funProductList(HSSFSheet worksheet, HttpServletRequest request, MultipartFile excelfile) {
	List productMasterlist = new ArrayList<>();
    int RowCount = 0;
	HashMap<String,String> location = new HashMap<String,String>();
	HashMap<String,String> group = new HashMap<String,String>();
	HashMap<String,String> subgroup = new HashMap<String,String>();
	HashMap<String,clsProductMasterBean> product = new HashMap<String,clsProductMasterBean>();
	
	//String prodCode = "";
	String clientCode = request.getSession().getAttribute("clientCode").toString();
	String userCode = request.getSession().getAttribute("usercode").toString();
	String propertyCode = request.getSession().getAttribute("propertyCode").toString();
	
	//String prodStock=request.getParameter("prodStock");
	try {
		int i = 1;
		
		while (i <= worksheet.getLastRowNum()) {
			// Creates an object representing a single row in excel
			
			
			HSSFRow row = worksheet.getRow(i++);
			// Sets the Read data to the model class
			RowCount = row.getRowNum();
			// Creates an object for the Candidate Model
			clsProductMasterBean objProd=new clsProductMasterBean();
			location.put(String.valueOf(row.getCell(3)), String.valueOf(row.getCell(3)));
			group.put(String.valueOf(row.getCell(2)), String.valueOf(row.getCell(2)));
			subgroup.put(String.valueOf(row.getCell(1)), String.valueOf(row.getCell(1)));
			
		    String prodName=String.valueOf(row.getCell(0));
			objProd.setStrProdName(funCheckIfNullExcelData(row.getCell(0), "", row.getCell(0)));
			objProd.setStrSGCode(String.valueOf(row.getCell(1)));
			objProd.setStrLocCode(String.valueOf(row.getCell(3)));
			objProd.setStrPartNo(funCheckIfNullExcelData(row.getCell(4),"", row.getCell(4)));
			objProd.setStrReceivedUOM(funCheckIfNullExcelData(row.getCell(5),"", row.getCell(5)));
			objProd.setStrProdType(funCheckIfNullExcelData(row.getCell(6),"", row.getCell(6)));
			objProd.setDblCostRM(Double.parseDouble(funCheckIfNullExcelData(row.getCell(7), "0.00", row.getCell(7))));
			objProd.setDblCostManu(Double.parseDouble(funCheckIfNullExcelData(row.getCell(8), "0.00", row.getCell(8))));
		    objProd.setDblOrduptoLvl(Double.parseDouble(funCheckIfNullExcelData(row.getCell(9), "0.00", row.getCell(9))));
			
			objProd.setDblReorderLvl(Double.parseDouble(funCheckIfNullExcelData(row.getCell(10),"0.00", row.getCell(10))));
			objProd.setStrNotInUse(funCheckIfNullExcelData(row.getCell(11),"", row.getCell(11)));
			objProd.setStrExpDate(funCheckIfNullExcelData(row.getCell(12),"", row.getCell(12)));
			objProd.setStrLotNo(funCheckIfNullExcelData(row.getCell(13),"", row.getCell(13)));
			objProd.setStrRevLevel(funCheckIfNullExcelData(row.getCell(14),"", row.getCell(14)));
			objProd.setStrSlNo(funCheckIfNullExcelData(row.getCell(15),"", row.getCell(15)));
			objProd.setStrForSale(funCheckIfNullExcelData(row.getCell(16),"", row.getCell(16)));
			objProd.setStrSaleNo(funCheckIfNullExcelData(row.getCell(17),"", row.getCell(17)));
			objProd.setStrDesc(funCheckIfNullExcelData(row.getCell(18),"", row.getCell(18)));
			objProd.setDblUnitPrice(Double.parseDouble(funCheckIfNullExcelData(row.getCell(19),"0.00", row.getCell(19))));
			objProd.setStrTaxIndicator(funCheckIfNullExcelData(row.getCell(20),"", row.getCell(20)));
			objProd.setStrExceedPO(funCheckIfNullExcelData(row.getCell(21),"", row.getCell(21)));
			objProd.setStrStagDel(funCheckIfNullExcelData(row.getCell(22),"", row.getCell(22)));
			int del=new Double(Double.parseDouble(funCheckIfNullExcelData(row.getCell(23),"0", row.getCell(23)))).intValue();
			objProd.setIntDelPeriod(del);
			//objProd.setIntDelPeriod(Integer.parseInt(funCheckIfNullExcelData(row.getCell(23), "0", row.getCell(23))));
			objProd.setStrType(funCheckIfNullExcelData(row.getCell(24),"", row.getCell(24)));
			objProd.setStrSpecification(funCheckIfNullExcelData(row.getCell(25),"", row.getCell(25)));
			objProd.setDblWeight(Double.parseDouble(funCheckIfNullExcelData(row.getCell(26), "0.00", row.getCell(26))));
			objProd.setStrBomCal(funCheckIfNullExcelData(row.getCell(27),"", row.getCell(27)));
			objProd.setStrWtUOM(funCheckIfNullExcelData(row.getCell(28),"", row.getCell(28)));
			objProd.setStrCalAmtOn(funCheckIfNullExcelData(row.getCell(29),"", row.getCell(29)));
			objProd.setStrClass(funCheckIfNullExcelData(row.getCell(30),"", row.getCell(30)));
			objProd.setDblBatchQty(Double.parseDouble(funCheckIfNullExcelData(row.getCell(31), "0.00", row.getCell(31))));
			objProd.setDblMaxLvl(Double.parseDouble(funCheckIfNullExcelData(row.getCell(32),"0.00", row.getCell(32))));
			objProd.setStrBinNo(funCheckIfNullExcelData(row.getCell(33),"", row.getCell(33)));
			objProd.setStrTariffNo(funCheckIfNullExcelData(row.getCell(34),"", row.getCell(34)));
			objProd.setDblListPrice(Double.parseDouble(funCheckIfNullExcelData(row.getCell(35), "0.00", row.getCell(35))));
			objProd.setStrRemark(funCheckIfNullExcelData(row.getCell(36),"", row.getCell(36)));
			objProd.setStrReceivedUOM(funCheckIfNullExcelData(row.getCell(37),"", row.getCell(37)));
			objProd.setStrIssueUOM(funCheckIfNullExcelData(row.getCell(38),"", row.getCell(38)));
			objProd.setStrRecipeUOM(funCheckIfNullExcelData(row.getCell(39),"", row.getCell(39)));
			objProd.setDblReceiveConversion(Double.parseDouble(funCheckIfNullExcelData(row.getCell(40), "0.00", row.getCell(40))));
			objProd.setDblIssueConversion(Double.parseDouble(funCheckIfNullExcelData(row.getCell(41), "0.00", row.getCell(41))));
			objProd.setDblRecipeConversion(Double.parseDouble(funCheckIfNullExcelData(row.getCell(42), "0.00", row.getCell(42))));
			objProd.setStrBarCode(funCheckIfNullExcelData(row.getCell(43),"", row.getCell(43)));
			objProd.setStrManufacturerCode(funCheckIfNullExcelData(row.getCell(44),"", row.getCell(44)));
			
			product.put(prodName, objProd);
			
			
			}
		
		for(HashMap.Entry<String,String> locData : location.entrySet() )
		{
			
			long lastNo = 0;
			String locName=locData.getKey();
			String locCode=locData.getValue();
			String sql="select a.strLocCode from tbllocationmaster a where a.strLocName='"+locName+"'  ";
			List locationlist= objGlobalService.funGetList(sql);
			if(locationlist!=null && locationlist.size()>0)
			{
				locCode=(String) locationlist.get(0);
				
			}
			else
			{
				lastNo = objGlobalFunctionsService.funGetLastNo("tbllocationmaster", "LocationeMaster", "intid", clientCode);
			    locCode = "L" + String.format("%06d", lastNo);
			    clsLocationMasterModel locModel= new clsLocationMasterModel(new clsLocationMasterModel_ID(locCode, clientCode));
			    locModel.setIntid(lastNo);
			    locModel.setStrLocCode(locCode);
			    locModel.setStrMonthEnd(" ");
			    locModel.setStrLocName(locName);
			    locModel.setStrLocDesc(" ");
			    locModel.setStrAvlForSale("N");
			    locModel.setStrActive("Y");
				locModel.setStrPickable("N");
				locModel.setStrReceivable("N");
				locModel.setStrExciseNo(" ");
				locModel.setStrType("");
				locModel.setStrClientCode(clientCode);
				locModel.setStrPropertyCode(propertyCode);
				locModel.setStrExternalCode("");
				locModel.setStrUserCreated(userCode);
				locModel.setStrUserModified(userCode);
				locModel.setDtCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				locModel.setDtLastModified(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				locModel.setStrUnderLocCode(" ");
				objLocationMasterService.funAddUpdate(locModel);
			}
			location.put(locName, locCode);
		 }
		
		
		
		for(HashMap.Entry<String,String> groupData :group.entrySet())
		{
			
			long lastNo = 0;
			String groupName=groupData.getKey();
			String groupCode=groupData.getValue();
			String sql="select a.strGCode from tblgroupmaster a where a.strGName='"+groupName+"' ";
			List groupList= objGlobalService.funGetList(sql);
			if(groupList!=null && groupList.size()>0)
			{
				groupCode=(String) groupList.get(0);
				
			}
			else
			{
				lastNo = objGlobalFunctionsService.funGetLastNoModuleWise("tblgroupmaster", "GroupMaster","intGId",clientCode,"1-WebStocks");
				groupCode = "G" + String.format("%06d", lastNo);
				clsGroupMasterModel grpModel = new clsGroupMasterModel(new clsGroupMasterModel_ID(groupCode, clientCode));
				grpModel.setStrGCode(groupCode);
				grpModel.setIntGId(lastNo);
				grpModel.setStrGName(groupName);
				grpModel.setStrGDesc(" ");
				grpModel.setStrUserCreated(userCode);
				grpModel.setStrUserModified(userCode);
				grpModel.setStrClientCode(clientCode);
				grpModel.setDtCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				grpModel.setDtLastModified(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				objGrpMasterService.funAddGroup(grpModel);
			}
			group.put(groupName, groupCode);
		}
		
		
		for(HashMap.Entry<String,String> subgroupData :subgroup.entrySet() )
		{
			long lastNo = 0;
			String subgroupName=subgroupData.getKey();
			String subgroupCode=subgroupData.getValue();
			String sql="select a.strSGCode from tblsubgroupmaster a where a.strSGName='"+subgroupName+"' ";
			List subgroupList= objGlobalService.funGetList(sql);
			if(subgroupList!=null && subgroupList.size()>0)
			{
				subgroupCode=(String) subgroupList.get(0);
				
			}
			else
			{
				lastNo = objGlobalFunctionsService.funGetLastNoModuleWise("tblsubgroupmaster", "SubGroupMaster", "intSGId", clientCode,"1-WebStocks");
				String subGroupCode = "SG" + String.format("%06d", lastNo);
				clsSubGroupMasterModel subgroupModel = new clsSubGroupMasterModel(new clsSubGroupMasterModel_ID(subGroupCode, clientCode));
				subgroupModel.setIntSGId(lastNo);
				subgroupModel.setStrSGCode(subGroupCode);
				subgroupModel.setStrSGName(subgroupName);
				subgroupModel.setStrClientCode(clientCode);
				subgroupModel.setStrUserCreated(userCode);
				subgroupModel.setStrUserModified(userCode);
				subgroupModel.setStrExciseChapter(" ");
				subgroupModel.setStrSGDesc(" ");
				subgroupModel.setStrExciseable("N");
				subgroupModel.setStrGCode(" ");
				subgroupModel.setDtCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				subgroupModel.setDtLastModified(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
				subgroupModel.setIntSortingNo(0);
				subgroupModel.setStrSGDescHeader(" ");
				objSubGrpMasterService.funAddUpdate(subgroupModel);
			}
			subgroup.put(subgroupName, subgroupCode);
		}
		
		
		for(Entry<String, clsProductMasterBean> productData :product.entrySet() )
		{
		long lastNo = 0;
		String productCode="";
		String productName=productData.getKey();
		clsProductMasterBean objBean=productData.getValue();
		productName=productName.replaceAll("'", "\\\\'");
		String sql="select a.strProdCode from tblproductmaster a where a.strProdName='"+productName+"'";
		List productList= objGlobalService.funGetList(sql);
		if(productList!=null && productList.size()>0)
		{
			productCode=(String) productList.get(0);
			clsProductMasterModel	objModel = new clsProductMasterModel(new clsProductMasterModel_ID(productCode, clientCode));
			objModel.setStrProdCode(productCode);
			objModel.setStrUserCreated(userCode);
			objModel.setDtCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrProductImage(objProductMaster.funBlankBlob());
			objModel.setStrProdName(objBean.getStrProdName().toUpperCase());
			objModel.setStrPartNo(objBean.getStrPartNo());
			objModel.setStrUOM(objGlobalFunctions.funIfNull(objBean.getStrUOM(), "", objBean.getStrUOM()));
            String subgroup1=objBean.getStrSGCode();
			
			objModel.setStrSGCode(objGlobalFunctions.funIfNull(subgroup.get(subgroup1), "", subgroup.get(subgroup1)));
			objModel.setStrProdType(objGlobalFunctions.funIfNull(objBean.getStrProdType(), "", objBean.getStrProdType()));
			objModel.setDblCostRM(objBean.getDblCostRM());
			objModel.setDblCostManu(objBean.getDblCostManu());
			String locCode1=objBean.getStrLocCode();
			objModel.setStrLocCode(location.get(locCode1));
			objModel.setDblOrduptoLvl(objBean.getDblOrduptoLvl());
			objModel.setDblReorderLvl(objBean.getDblReorderLvl());
			objModel.setStrNotInUse(objGlobalFunctions.funIfNull(objBean.getStrNotInUse(), "N", "Y"));
			objModel.setStrExpDate(objGlobalFunctions.funIfNull(objBean.getStrExpDate(), "N", "Y"));
			objModel.setStrLotNo(objGlobalFunctions.funIfNull(objBean.getStrRevLevel(), "N", "Y"));
			objModel.setStrRevLevel(objGlobalFunctions.funIfNull(objBean.getStrRevLevel(), "N", "Y"));
			objModel.setStrSlNo(objGlobalFunctions.funIfNull(objBean.getStrSlNo(), "N", "Y"));
			objModel.setStrForSale(objGlobalFunctions.funIfNull(objBean.getStrForSale(), "N", "Y"));
			objModel.setStrSaleNo(objGlobalFunctions.funIfNull(objBean.getStrSaleNo(), "", objBean.getStrSaleNo()));
			objModel.setStrDesc(objGlobalFunctions.funIfNull(objBean.getStrDesc(), "", objBean.getStrDesc()));
			objModel.setDblUnitPrice(objBean.getDblUnitPrice());
			objModel.setStrTaxIndicator(objGlobalFunctions.funIfNull(objBean.getStrTaxIndicator(), "", objBean.getStrTaxIndicator()));
			objModel.setStrExceedPO(objGlobalFunctions.funIfNull(objBean.getStrExceedPO(), "N", "Y"));
			objModel.setStrStagDel(objGlobalFunctions.funIfNull(objBean.getStrStagDel(), "N", "Y"));
			objModel.setIntDelPeriod(objBean.getIntDelPeriod());
			objModel.setStrType(objGlobalFunctions.funIfNull(objBean.getStrType(), "", objBean.getStrType()));
			objModel.setStrSpecification(objGlobalFunctions.funIfNull(objBean.getStrSpecification(), "", objBean.getStrSpecification()));
			objModel.setDblWeight(objBean.getDblWeight());
			objModel.setStrBomCal(objGlobalFunctions.funIfNull(objBean.getStrBomCal(), "", objBean.getStrBomCal()));
			objModel.setStrWtUOM(objGlobalFunctions.funIfNull(objBean.getStrWtUOM(), "", objBean.getStrWtUOM()));
			objModel.setStrCalAmtOn(objGlobalFunctions.funIfNull(objBean.getStrCalAmtOn(), "", objBean.getStrCalAmtOn()));
			objModel.setStrClass(objGlobalFunctions.funIfNull(objBean.getStrClass(), "", objBean.getStrClass()));
			objModel.setStrUserModified(userCode);
			objModel.setDtLastModified(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDblBatchQty(objBean.getDblBatchQty());
			objModel.setDblMaxLvl(objBean.getDblMaxLvl());
			objModel.setStrBinNo(objGlobalFunctions.funIfNull(objBean.getStrBinNo(), "", objBean.getStrBinNo()));
			objModel.setStrTariffNo(objGlobalFunctions.funIfNull(objBean.getStrTariffNo(), "", objBean.getStrTariffNo()));
			objModel.setDblListPrice(objBean.getDblListPrice());
			objModel.setStrRemark(objGlobalFunctions.funIfNull(objBean.getStrRemark(), "", objBean.getStrRemark()));
			objModel.setStrIssueUOM(objGlobalFunctions.funIfNull(objBean.getStrIssueUOM(), "", objBean.getStrIssueUOM()));
			objModel.setStrReceivedUOM(objGlobalFunctions.funIfNull(objBean.getStrReceivedUOM(), "", objBean.getStrReceivedUOM()));
			objModel.setStrRecipeUOM(objGlobalFunctions.funIfNull(objBean.getStrRecipeUOM(), "", objBean.getStrRecipeUOM()));
			objModel.setDblIssueConversion(objBean.getDblIssueConversion());
			objModel.setDblReceiveConversion(objBean.getDblReceiveConversion());
			objModel.setDblRecipeConversion(objBean.getDblRecipeConversion());
			objModel.setStrSpecification(objBean.getStrSpecification());
			objModel.setStrNonStockableItem(objGlobalFunctions.funIfNull(objBean.getStrNonStockableItem(), "N", "Y"));
			objModel.setStrPickMRPForTaxCal(objGlobalFunctions.funIfNull(objBean.getStrPickMRPForTaxCal(), "N", "Y"));
			objModel.setStrManufacturerCode(objBean.getStrManufacturerCode());
			objModel.setStrHSNCode(objGlobalFunctions.funIfNull(objBean.getStrHSNCode(), "", objBean.getStrHSNCode()));
			
			if (objBean.getDblYieldPer() == 0.0) {
				double yieldper = 100.00;
				objBean.setDblYieldPer(yieldper);

			}
			objModel.setDblYieldPer(objBean.getDblYieldPer());
			objModel.setStrBarCode(objBean.getStrBarCode());
			objModel.setDblMRP((objBean.getDblMRP()));
			objModel.setStrExciseable(objBean.getStrExciseable());
			objModel.setStrComesaItem(objBean.getStrComesaItem());

			if (objBean.getStrProdNameMarathi() == null) {
				objModel.setStrProdNameMarathi("");
			} else {
				objModel.setStrProdNameMarathi(objBean.getStrProdNameMarathi());
			}
			objProductMasterService.funAddUpdateGeneral(objModel);
		}
		else
		{
			lastNo = objGlobalFunctionsService.funGetLastNo("tblproductmaster", "ProductMaster", "intId", clientCode);
			productCode = "P" + String.format("%07d", lastNo);
			clsProductMasterModel objModel = new clsProductMasterModel(new clsProductMasterModel_ID(productCode, clientCode));
			objModel.setIntId(lastNo);
			objModel.setStrProdCode(productCode);
			objModel.setStrUserCreated(userCode);
			objModel.setDtCreatedDate(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrProductImage(objProductMaster.funBlankBlob());
			objModel.setStrProdName(objBean.getStrProdName().toUpperCase());
			objModel.setStrPartNo(objBean.getStrPartNo());
			objModel.setStrUOM(objGlobalFunctions.funIfNull(objBean.getStrUOM(), "", objBean.getStrUOM()));
			String subgroup1=objBean.getStrSGCode();
			
			objModel.setStrSGCode(objGlobalFunctions.funIfNull(subgroup.get(subgroup1), "", subgroup.get(subgroup1)));
			objModel.setStrProdType(objGlobalFunctions.funIfNull(objBean.getStrProdType(), "", objBean.getStrProdType()));
			objModel.setDblCostRM(objBean.getDblCostRM());
			objModel.setDblCostManu(objBean.getDblCostManu());
			String locCode1=objBean.getStrLocCode();
			objModel.setStrLocCode(location.get(locCode1));
			objModel.setDblOrduptoLvl(objBean.getDblOrduptoLvl());
			objModel.setDblReorderLvl(objBean.getDblReorderLvl());
			objModel.setStrNotInUse(objGlobalFunctions.funIfNull(objBean.getStrNotInUse(), "N", "Y"));
			objModel.setStrExpDate(objGlobalFunctions.funIfNull(objBean.getStrExpDate(), "N", "Y"));
			objModel.setStrLotNo(objGlobalFunctions.funIfNull(objBean.getStrRevLevel(), "N", "Y"));
			objModel.setStrRevLevel(objGlobalFunctions.funIfNull(objBean.getStrRevLevel(), "N", "Y"));
			objModel.setStrSlNo(objGlobalFunctions.funIfNull(objBean.getStrSlNo(), "N", "Y"));
			objModel.setStrForSale(objGlobalFunctions.funIfNull(objBean.getStrForSale(), "N", "Y"));
			objModel.setStrSaleNo(objGlobalFunctions.funIfNull(objBean.getStrSaleNo(), "", objBean.getStrSaleNo()));
			objModel.setStrDesc(objGlobalFunctions.funIfNull(objBean.getStrDesc(), "", objBean.getStrDesc()));
			objModel.setDblUnitPrice(objBean.getDblUnitPrice());
			objModel.setStrTaxIndicator(objGlobalFunctions.funIfNull(objBean.getStrTaxIndicator(), "", objBean.getStrTaxIndicator()));
			objModel.setStrExceedPO(objGlobalFunctions.funIfNull(objBean.getStrExceedPO(), "N", "Y"));
			objModel.setStrStagDel(objGlobalFunctions.funIfNull(objBean.getStrStagDel(), "N", "Y"));
			objModel.setIntDelPeriod(objBean.getIntDelPeriod());
			objModel.setStrType(objGlobalFunctions.funIfNull(objBean.getStrType(), "", objBean.getStrType()));
			objModel.setStrSpecification(objGlobalFunctions.funIfNull(objBean.getStrSpecification(), "", objBean.getStrSpecification()));
			objModel.setDblWeight(objBean.getDblWeight());
			objModel.setStrBomCal(objGlobalFunctions.funIfNull(objBean.getStrBomCal(), "", objBean.getStrBomCal()));
			objModel.setStrWtUOM(objGlobalFunctions.funIfNull(objBean.getStrWtUOM(), "", objBean.getStrWtUOM()));
			objModel.setStrCalAmtOn(objGlobalFunctions.funIfNull(objBean.getStrCalAmtOn(), "", objBean.getStrCalAmtOn()));
			objModel.setStrClass(objGlobalFunctions.funIfNull(objBean.getStrClass(), "", objBean.getStrClass()));
			objModel.setStrUserModified(userCode);
			objModel.setDtLastModified(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDblBatchQty(objBean.getDblBatchQty());
			objModel.setDblMaxLvl(objBean.getDblMaxLvl());
			objModel.setStrBinNo(objGlobalFunctions.funIfNull(objBean.getStrBinNo(), "", objBean.getStrBinNo()));
			objModel.setStrTariffNo(objGlobalFunctions.funIfNull(objBean.getStrTariffNo(), "", objBean.getStrTariffNo()));
			objModel.setDblListPrice(objBean.getDblListPrice());
			objModel.setStrRemark(objGlobalFunctions.funIfNull(objBean.getStrRemark(), "", objBean.getStrRemark()));
			objModel.setStrIssueUOM(objGlobalFunctions.funIfNull(objBean.getStrIssueUOM(), "", objBean.getStrIssueUOM()));
			objModel.setStrReceivedUOM(objGlobalFunctions.funIfNull(objBean.getStrReceivedUOM(), "", objBean.getStrReceivedUOM()));
			objModel.setStrRecipeUOM(objGlobalFunctions.funIfNull(objBean.getStrRecipeUOM(), "", objBean.getStrRecipeUOM()));
			objModel.setDblIssueConversion(objBean.getDblIssueConversion());
			objModel.setDblReceiveConversion(objBean.getDblReceiveConversion());
			objModel.setDblRecipeConversion(objBean.getDblRecipeConversion());
			objModel.setStrSpecification(objBean.getStrSpecification());
			objModel.setStrNonStockableItem(objGlobalFunctions.funIfNull(objBean.getStrNonStockableItem(), "N", "Y"));
			objModel.setStrPickMRPForTaxCal(objGlobalFunctions.funIfNull(objBean.getStrPickMRPForTaxCal(), "N", "Y"));
			objModel.setStrManufacturerCode(objBean.getStrManufacturerCode());
			objModel.setStrHSNCode(objGlobalFunctions.funIfNull(objBean.getStrHSNCode(), "", objBean.getStrHSNCode()));
			
			if (objBean.getDblYieldPer() == 0.0) {
				double yieldper = 100.00;
				objBean.setDblYieldPer(yieldper);

			}
			objModel.setDblYieldPer(objBean.getDblYieldPer());
			objModel.setStrBarCode(objBean.getStrBarCode());
			objModel.setDblMRP((objBean.getDblMRP()));
			objModel.setStrExciseable(objBean.getStrExciseable());
			objModel.setStrComesaItem(objBean.getStrComesaItem());

			if (objBean.getStrProdNameMarathi() == null) {
				objModel.setStrProdNameMarathi("");
			} else {
				objModel.setStrProdNameMarathi(objBean.getStrProdNameMarathi());
			}
			objProductMasterService.funAddUpdateGeneral(objModel);
		}
	    
		
	}
		
	
		
		
	} catch (Exception e) {
		logger.error(e);
		e.printStackTrace();
		List list = new ArrayList<>();
		list.add("Invalid Excel File");
		
		return list;
	}
	return productMasterlist;
}


}
