package com.sanguine.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
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
import com.sanguine.bean.clsParentDataForBOM;
import com.sanguine.bean.clsRecipeMasterBean;
import com.sanguine.model.clsBomDtlModel;
import com.sanguine.model.clsBomHdModel;
import com.sanguine.model.clsProductMasterModel;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsRecipeMasterService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.util.clsReportBean;

@Controller
public class clsRecipeMasterController {
	@Autowired
	private clsRecipeMasterService objRecipeMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	@Autowired
	private clsSetupMasterService objSetupMasterService;
	@Autowired
	private ServletContext servletContext;
	private clsGlobalFunctions objGlobal = null;
	
	@Autowired 
	private  clsWhatIfAnalysisController objWhatIfAnalysisController;
	
	@Autowired 
	private clsReportsController objReportsController;
	
	@Autowired
	private clsGlobalFunctions objGlobalFun;
	
//	double dblAmt=0.0;
//	double dblRate=0.0;
//	double bomrate =0.0;
//	double bomAmt=0.0;
	@RequestMapping(value = "/frmBOMMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		List<String> listProcess = new ArrayList<>();
		listProcess.add("Select");
		listProcess.add("Production");
		model.put("processList", listProcess);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBOMMaster_1", "command", new clsRecipeMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBOMMaster", "command", new clsRecipeMasterBean());
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/frmBOMMaster1", method = RequestMethod.POST)
	public ModelAndView funOpenFormWithBomCode(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		clsRecipeMasterBean bean = new clsRecipeMasterBean();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();

		String bomCode = request.getParameter("BOMCode").toString();
		clsBomHdModel objBomHd = objRecipeMasterService.funGetObject(bomCode, clientCode);
		bean = funPrepareBean(objBomHd, clientCode);
		List listBomDtl = objRecipeMasterService.funGetDtlList(bomCode, clientCode);
		List<clsBomDtlModel> listBOMDtlTemp = new ArrayList<clsBomDtlModel>();

		for (int i = 0; i < listBomDtl.size(); i++) {
			Object[] ob = (Object[]) listBomDtl.get(i);
			clsBomDtlModel bomDtl = (clsBomDtlModel) ob[0];
			clsProductMasterModel prodMaster = (clsProductMasterModel) ob[1];
			bomDtl.setStrProdName(prodMaster.getStrProdName());
			listBOMDtlTemp.add(bomDtl);
		}

		List<String> listProcess = new ArrayList<>();
		listProcess.add(bean.getStrProcessName());
		model.put("processList", listProcess);
		bean.setListBomDtlModel(listBOMDtlTemp);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBOMMaster_1", "command", bean);
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBOMMaster", "command", bean);
		} else {
			return new ModelAndView("frmBOMMaster", "command", bean);
		}

	}

	@RequestMapping(value = "/loadBOMMaster", method = RequestMethod.POST)
	public @ResponseBody clsRecipeMasterBean funLoadFormWithBomCode(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		clsRecipeMasterBean bean = new clsRecipeMasterBean();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();

		String bomCode = request.getParameter("BOMCode").toString();
		clsBomHdModel objBomHd = objRecipeMasterService.funGetObject(bomCode, clientCode);
		if (objBomHd != null) {
			bean = funPrepareBean(objBomHd, clientCode);
			List listBomDtl = objRecipeMasterService.funGetDtlList(bomCode, clientCode);
			List<clsBomDtlModel> listBOMDtlTemp = new ArrayList<clsBomDtlModel>();

			for (int i = 0; i < listBomDtl.size(); i++) {
				Object[] ob = (Object[]) listBomDtl.get(i);
				clsBomDtlModel bomDtl = (clsBomDtlModel) ob[0];
				clsProductMasterModel prodMaster = (clsProductMasterModel) ob[1];
				bomDtl.setStrProdName(prodMaster.getStrProdName());
				listBOMDtlTemp.add(bomDtl);
			}

			bean.setListBomDtlModel(listBOMDtlTemp);
		} else {
			bean.setStrBOMCode("Invalid Code");
		}

		return bean;

	}

	@RequestMapping(value = "/saveRecipeMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsRecipeMasterBean objBean, BindingResult result, HttpServletRequest req) {
		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		clsBomHdModel objHdModel = null;
		objGlobal = new clsGlobalFunctions();
		if (!result.hasErrors()) {
			List<clsBomDtlModel> listBomDtl = objBean.getListBomDtlModel();
			boolean flagDtlDataInserted = false;
			if (null != listBomDtl && listBomDtl.size() > 0) {
				objHdModel = funPrepareModel(objBean, userCode, clientCode);
				objRecipeMasterService.funAddUpdate(objHdModel);
				String bomCode = objHdModel.getStrBOMCode();
				objRecipeMasterService.funDeleteDtl(bomCode, clientCode);
				for (clsBomDtlModel ob : listBomDtl) {
					if (null != ob.getStrChildCode()) {
						ob.setStrBOMCode(bomCode);
						ob.setStrClientCode(clientCode);
						objRecipeMasterService.funAddUpdateDtl(ob);
					}
				}
				flagDtlDataInserted = true;
			}
			if (flagDtlDataInserted == true) {
				req.getSession().setAttribute("success", true);
				req.getSession().setAttribute("successMessage", "Recipe Code : ".concat(objHdModel.getStrBOMCode()));
			}
			return new ModelAndView("redirect:/frmBOMMaster.html?saddr=" + urlHits);
		} else {
			return new ModelAndView("redirect:/frmBOMMaster.html?saddr=" + urlHits);
		}
	}

	// AssignField function to set data onto form for edit transaction.
	@RequestMapping(value = "/loadProductData", method = RequestMethod.GET)
	public @ResponseBody clsParentDataForBOM funAssignFields(@RequestParam("prodCode") String code, HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		return funGetParentDataForBOM(code, clientCode, "");
	}

	@RequestMapping(value = "/loadRecipeMaster", method = RequestMethod.GET)
	public @ResponseBody ModelAndView funAssignFields1(@RequestParam("BOMCode") String code, Map<String, Object> model, HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		List<clsBomDtlModel> listBomDtl = objRecipeMasterService.funGetDtlList(code, clientCode);
		clsBomHdModel objBomHd = objRecipeMasterService.funGetObject(code, clientCode);
		clsRecipeMasterBean objBean = funPrepareBean(objBomHd, clientCode);
		objBean.setListBomDtlModel(listBomDtl);
		List<String> listProcess = new ArrayList<>();
		listProcess.add(objBean.getStrProcessName());
		model.put("processList", listProcess);
		return new ModelAndView("frmBOMMaster", "command", new clsRecipeMasterBean());
	}

	// Returns a single master record by passing code as primary key. Also
	// generates next Code if transaction is for Save Master
	private clsBomHdModel funPrepareModel(clsRecipeMasterBean objBean, String userCode, String clientCode) {
		long lastNo = 0;
		clsBomHdModel objHdModel = new clsBomHdModel();
		if (objBean.getStrBOMCode().length() == 0) {
			lastNo = objGlobalFunctionsService.funGetLastNo("tblbommasterhd", "BOMMaster", "intId", clientCode);
			String code = "B" + String.format("%07d", lastNo);
			objHdModel.setStrBOMCode(code);
			objHdModel.setIntId(lastNo);
			objHdModel.setStrUserCreated(userCode);
			objHdModel.setDtCreatedDate(objGlobal.funGetCurrentDate("yyyy-MM-dd"));
			objHdModel.setStrClientCode(clientCode);
		} else {
			objHdModel.setStrBOMCode(objBean.getStrBOMCode());
		}
		objHdModel.setStrParentCode(objBean.getStrParentCode());
		objHdModel.setStrProcessCode(objBean.getStrProcessCode());
		objHdModel.setDtValidFrom(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDtValidFrom()));
		objHdModel.setDtValidTo(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDtValidTo()));
		objHdModel.setStrUserModified(userCode);
		objHdModel.setDtLastModified(objGlobal.funGetCurrentDate("yyyy-MM-dd"));
		objHdModel.setStrUOM(objBean.getStrUOM());
		objHdModel.setDblQty(objBean.getDblQty());
		objHdModel.setStrMethod(objBean.getStrMethod());
		objHdModel.setStrBOMType("R");
		return objHdModel;
	}

	private clsRecipeMasterBean funPrepareBean(clsBomHdModel objModel, String clientCode) {
		objGlobal = new clsGlobalFunctions();
		clsParentDataForBOM objParentData = funGetParentDataForBOM(objModel.getStrParentCode(), clientCode, objModel.getStrBOMCode());
		clsRecipeMasterBean objBean = new clsRecipeMasterBean();
		objBean.setStrBOMCode(objModel.getStrBOMCode());
		objBean.setStrParentCode(objModel.getStrParentCode());
		objBean.setStrProcessCode(objModel.getStrProcessCode());
		objBean.setDtValidFrom(objGlobal.funGetDate("yyyy/MM/dd", objModel.getDtValidFrom()));
		objBean.setDtValidTo(objGlobal.funGetDate("yyyy/MM/dd", objModel.getDtValidTo()));
		objBean.setStrProcessName(objParentData.getStrProcessName());
		objBean.setStrParentName(objParentData.getStrParentName());
		objBean.setStrPOSItemCode(objParentData.getStrPartNo());
		objBean.setStrSGCode(objParentData.getStrSGCode());
		objBean.setStrSGName(objParentData.getStrSGName());
		objBean.setStrType(objParentData.getStrProdType());
		objBean.setStrUOM(objParentData.getStrUOM());
		objBean.setDblQty(objModel.getDblQty());
		return objBean;
	}

	public clsParentDataForBOM funGetParentDataForBOM(String parentProdCode, String clientCode, String bomCode) {
		clsParentDataForBOM objParentProduct = new clsParentDataForBOM();

		//String sqlCheckBom = " select a.strBOMCode from tblbommasterhd a " + " where a.strParentCode='" + parentProdCode + "' and a.strClientCode='" + clientCode + "' ";
		String sqlCheckBom = "  select a.strBOMCode,b.strProdName from tblbommasterhd a,tblproductmaster b where a.strParentCode='"+parentProdCode+"' and a.strParentCode=b.strProdCode and a.strClientCode='"+clientCode+"'  ";
		List listCheckBom = objRecipeMasterService.funGetProductList(sqlCheckBom);
		if (bomCode.equals("")) {
			if (listCheckBom.size() > 0) {
				Object[] obj = (Object[]) listCheckBom.get(0);
				objParentProduct.setStrBOMCode(obj[0].toString());
				objParentProduct.setStrParentName(obj[1].toString());
				
			} else {
				String sql = "select a.strProdCode,a.strProdName,ifnull(a.strPartNo,'') as strPartNo,ifnull(a.strProdType,'') as strProdType, " + "ifnull(b.strSGCode,'') as strSGCode, ifnull(b.strSGName,'') as strSGName, " + "ifnull(c.strProcessCode,'') as strProcessCode, ifnull(d.strProcessName,'') as strProcessName, " + "ifnull(a.strUOM,'') as strUOM " + "from tblproductmaster a "
						+ "left outer join tblsubgroupmaster b  on  a.strSGCode=b.strSGCode and b.strClientCode='" + clientCode + "' " + "left outer join tblprodprocess c on a.strProdCode=c.strProdCode and c.strClientCode='" + clientCode + "' " + "left outer join tblprocessmaster d on c.strProcessCode=d.strProcessCode and d.strClientCode='" + clientCode + "' " + "where a.strProdCode='"
						+ parentProdCode + "' and a.strClientCode='" + clientCode + "' ";
				@SuppressWarnings("rawtypes")
				List listProduct = objRecipeMasterService.funGetProductList(sql);

				if (listProduct.size() == 0) {
					objParentProduct.setStrParentCode("Invalid Product Code");
				} else {
					Object[] ob = (Object[]) listProduct.get(0);
					objParentProduct.setStrParentCode(ob[0].toString());
					objParentProduct.setStrParentName(ob[1].toString());
					objParentProduct.setStrPartNo(ob[2].toString());
					objParentProduct.setStrProdType(ob[3].toString());
					objParentProduct.setStrSGCode(ob[4].toString());
					objParentProduct.setStrSGName(ob[5].toString());
					objParentProduct.setStrProcessCode(ob[6].toString());
					objParentProduct.setStrProcessName(ob[7].toString());
					objParentProduct.setStrUOM(ob[8].toString());
					objParentProduct.setStrBOMCode("");
				}
			}

		} else {
			String sql = "select a.strProdCode,a.strProdName,ifnull(a.strPartNo,'') as strPartNo,ifnull(a.strProdType,'') as strProdType, " + "ifnull(b.strSGCode,'') as strSGCode, ifnull(b.strSGName,'') as strSGName, " + "ifnull(c.strProcessCode,'') as strProcessCode, ifnull(d.strProcessName,'') as strProcessName, " + "ifnull(a.strUOM,'') as strUOM " + "from tblproductmaster a "
					+ "left outer join tblsubgroupmaster b  on  a.strSGCode=b.strSGCode and b.strClientCode='" + clientCode + "' " + "left outer join tblprodprocess c on a.strProdCode=c.strProdCode and c.strClientCode='" + clientCode + "' " + "left outer join tblprocessmaster d on c.strProcessCode=d.strProcessCode and d.strClientCode='" + clientCode + "' " + "where a.strProdCode='"
					+ parentProdCode + "' and a.strClientCode='" + clientCode + "' ";
			@SuppressWarnings("rawtypes")
			List listProduct = objRecipeMasterService.funGetProductList(sql);

			if (listProduct.size() == 0) {
				objParentProduct.setStrParentCode("Invalid Product Code");
			} else {
				Object[] ob = (Object[]) listProduct.get(0);
				objParentProduct.setStrParentCode(ob[0].toString());
				objParentProduct.setStrParentName(ob[1].toString());
				objParentProduct.setStrPartNo(ob[2].toString());
				objParentProduct.setStrProdType(ob[3].toString());
				objParentProduct.setStrSGCode(ob[4].toString());
				objParentProduct.setStrSGName(ob[5].toString());
				objParentProduct.setStrProcessCode(ob[6].toString());
				objParentProduct.setStrProcessName(ob[7].toString());
				objParentProduct.setStrUOM(ob[8].toString());
				objParentProduct.setStrBOMCode("");
			}
		}

		return objParentProduct;
	}

	/**
	 * Report Code
	 * 
	 * @return
	 * @throws JRException
	 */
	// Jai chandra 05-01-2015

	@RequestMapping(value = "/frmRecipesList", method = RequestMethod.GET)
	public ModelAndView funOpenProductionOrderSlipForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmRecipesList_1", "command", new clsReportBean());
		} else {
			return new ModelAndView("frmRecipesList", "command", new clsReportBean());
		}

	}

	@RequestMapping(value = "/rptRecipesList", method = RequestMethod.GET)
	private void funReport(@ModelAttribute("command") clsReportBean objBean, HttpServletResponse resp, HttpServletRequest req) {
		String ProdCode = objBean.getStrDocCode();
		String type = objBean.getStrDocType();
		String rateFrom =objBean.getStrShowBOM();
		funCallReport(ProdCode, type,rateFrom, resp, req,objBean);
	}

	@RequestMapping(value = "/invokeRecipesList", method = RequestMethod.GET)
	private void funCallReportOnClick(@RequestParam(value = "docCode") String docCode, HttpServletResponse resp, HttpServletRequest req) {
		String type = "pdf";
//		funCallReport(docCode, type, resp, req);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void funCallReport(String ProdCode, String type,String rateFrom , HttpServletResponse resp, HttpServletRequest req,clsReportBean obReportjBean) {
		try {
			objGlobal = new clsGlobalFunctions();
			Connection con = objGlobal.funGetConnection(req);
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String companyName = req.getSession().getAttribute("companyName").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			
			clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
        
			String reportName = servletContext.getRealPath("/WEB-INF/reports/rptRecipesList.jrxml");
			String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
			
			String recipeListPrice=objSetup.getStrRecipeListPrice();
			String strReceipePriceClmn="ifnull(cp.dblCostRM/cp.dblRecipeConversion,0)";
			if(recipeListPrice.equalsIgnoreCase("Received"))
			{
				strReceipePriceClmn="ifnull(cp.dblCostRM/cp.dblReceiveConversion,0)";
			}
			
			String sqlDtlQuery = "SELECT   h.strBOMCode as strBOMCode,h.strParentCode as strParentCode, " 
					+ "h.strprocesscode as strprocesscode,h.dblQty as ParentDdlQty,h.strUOM as ParentstrUOM, " 
					+ "p.strProdName as ParentProdName,ifnull(lp.strlocname,'') as parentLocation, d.strChildCode, "
					+ "ifnull(cp.strProdName,'')  as childProductName,ifnull(cp.strRecipeUOM,'') as childUOM, d.dblQty,"+strReceipePriceClmn+" as  price, "
					+ "IFNULL(pr.strprocessname,'') as strprocessname,ifnull(cl.strlocname,'') as childLocation  ,"
					+ "date(h.dtCreatedDate) as dtCreatedDate,date(h.dtValidFrom) as dtValidFrom," 
					+ "date(h.dtValidTo) as dtValidTo, h.strUserCreated as strUserCreated ,ifnull((cp.dblCostRM /cp.dblRecipeConversion)*d.dblQty,0) as value,d.dblQty"
					+ ",ifnull((d.dblQty/cp.dblRecipeConversion),0) AS InitialWt, ifnull(cp.strProdType,'') "
					+ "from tblbommasterhd  h inner join tblbommasterdtl AS d ON h.strBOMCode = d.strBOMCode and d.strClientCode='" + clientCode + "' " 
					+ "left outer join tblproductmaster   p ON h.strParentCode = p.strProdCode and p.strClientCode='" + clientCode + "' " + "left outer join tblproductmaster AS cp ON d.strChildCode = cp.strProdCode and cp.strClientCode='" + clientCode + "' "
					+ "left outer join tbllocationmaster  lp ON lp.strLocCode = p.strLocCode and lp.strClientCode='" + clientCode + "' " + "left outer join tbllocationmaster AS cl ON cl.strLocCode = cp.strLocCode and cl.strClientCode='" + clientCode + "' "
					+ "left outer join tblprocessmaster pr on h.strprocesscode=pr.strprocesscode and pr.strClientCode='" + clientCode + "' "
					+ "where  h.strClientCode='" + clientCode + "'";
			if (!ProdCode.equals("")) {
				sqlDtlQuery += " and h.strParentCode='" + ProdCode + "'  ";
			}
			
			String SGCode = "";
			String strSGCodes[] = obReportjBean.getStrSGCode().split(",");
			for (int i = 0; i < strSGCodes.length; i++) {
				if (SGCode.length() > 0) {
					SGCode = SGCode + " or p.strSGCode='" + strSGCodes[i] + "' ";
				} else {
					SGCode = "p.strSGCode='" + strSGCodes[i] + "' ";
				}

			}

			sqlDtlQuery = sqlDtlQuery + " and " + "(" + SGCode + ")";
			
			List<clsRecipeMasterBean> listDtlBean=new ArrayList<clsRecipeMasterBean>(); 
			List listChildRate = objGlobalFunctionsService.funGetList(sqlDtlQuery, "sql");
			String strParentCode="",finalwt="";
			if(listChildRate.size()>0)
			{
				for(int i=0;i<listChildRate.size();i++)
				{
					
					Object[] obj=(Object[])listChildRate.get(i);
					double bomrate =Double.parseDouble(obj[11].toString());
					double bomAmt=Double.parseDouble(obj[18].toString());
					if(rateFrom.equals("Last Purchase Rate")){
						List listRate =funGetBOMLastPurchaseRate(obj[7].toString(), clientCode);
						if(listRate.size()>0){
							bomrate=Double.parseDouble(listRate.get(0).toString());
							bomAmt=bomrate*Double.parseDouble(obj[19].toString());
					    }
					}
					clsRecipeMasterBean objBean=new clsRecipeMasterBean();
					objBean.setStrParentCode(obj[1].toString());
					objBean.setStrParentName(obj[5].toString());
					objBean.setStrChildCode(obj[7].toString());
					objBean.setStrChildName(obj[8].toString());
					objBean.setDblQty(Double.parseDouble(obj[10].toString()));
					objBean.setStrUOM(obj[9].toString());
					objBean.setStrLocation(obj[13].toString());
					
					if(obj[21].toString().equalsIgnoreCase("Semi Finished") || obj[21].toString().equalsIgnoreCase("Produced")){
						
						strParentCode=obj[7].toString();
						finalwt = obj[20].toString();
						double dblRecipeCost=objGlobalFun.funGetChildProduct(obj[21].toString(),clientCode,obj[0].toString(),strParentCode,finalwt,0);
						bomAmt=dblRecipeCost;
					}
					
					/*funGetBOMNodes(obj[7].toString(), 0,Double.parseDouble(obj[10].toString()), listChildNodes11);
					if(listChildNodes11.size()>0)
					{
						bomrate=0.0;
						bomAmt=0.0;
						for(String prodCode11:listChildNodes11)
						{
							
	//						String temp11 = (String) listChildNodes11.get(cnt);
							String prodCode1 = prodCode11.split(",")[0];
							double reqdQty11 = Double.parseDouble(prodCode11.split(",")[1]);
	//						bomrate=funGetBOMRate(prodCode1, clientCode);	
						
							
	//						bomAmt+=(amt*reqdQty11);
							if(rateFrom.equals("Last Purchase Rate")){
								List listBomRate=funGetBOMLastPurchaseRate(prodCode1, clientCode);
								if(listBomRate.size()>0){
									bomrate+=Double.parseDouble(listBomRate.get(0).toString());
									bomAmt=bomAmt+(Double.parseDouble(listBomRate.get(1).toString())*reqdQty11);	
								}
							}else{
								List listBom=funGetBOMRate(prodCode1, clientCode);	
								double rate=Double.parseDouble(listBom.get(0).toString());
								double amt=Double.parseDouble(listBom.get(1).toString());
								bomrate+=amt;
								bomAmt=bomAmt+(rate*reqdQty11);
							}
							
						}
					}*/
					
					
					objBean.setDblAmount(bomAmt);
					objBean.setDblPrice(bomrate);
					objBean.setDtValidFrom(obj[15].toString());
					objBean.setDtValidTo(obj[16].toString());
					listDtlBean.add(objBean);
					
				}
			}
			
		
			
			
			
			
			String sqlHDQuery = "";
//			JasperDesign jd = JRXmlLoader.load(reportName);
//			/*
//			 * JRDesignQuery newQuery= new JRDesignQuery();
//			 * newQuery.setText(sqlDtlQuery); jd.setQuery(newQuery);
//			 */
//
//			JRDesignQuery subQuery = new JRDesignQuery();

//			subQuery.setText(sqlDtlQuery);
//			Map<String, JRDataset> datasetMap = jd.getDatasetMap();
//			JRDesignDataset subDataset = (JRDesignDataset) datasetMap.get("dsRecipesDtl");
//			subDataset.setQuery(subQuery);
//			JasperReport jr = JasperCompileManager.compileReport(jd);

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
			hm.put("listDtlBean", listDtlBean);
			JasperDesign jd = JRXmlLoader.load(reportName);
			JasperReport jr = JasperCompileManager.compileReport(jd);

			JasperPrint jp = JasperFillManager.fillReport(jr, hm, new JREmptyDataSource());
			List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
			jprintlist.add(jp);

			if (jprintlist.size() > 0) {
				ServletOutputStream servletOutputStream = resp.getOutputStream();
				if (type.trim().equalsIgnoreCase("pdf")) {
					JRExporter exporter = new JRPdfExporter();
					resp.setContentType("application/pdf");
					exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
					exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
					exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					resp.setHeader("Content-Disposition", "inline;filename=" + "rptRecipeList." + type.trim());
					exporter.exportReport();
					servletOutputStream.flush();
					servletOutputStream.close();
				} else {
					JRExporter exporter = new JRXlsExporter();
					resp.setContentType("application/xlsx");
					exporter.setParameter(JRXlsExporterParameter.JASPER_PRINT_LIST, jprintlist);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, servletOutputStream);
					exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
					resp.setHeader("Content-Disposition", "attachment;filename=" + "rptRecipeList." + type.trim());
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

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List funGetBOMRate(String childCode, String clientCode) {
		List bomDtl =new ArrayList();
//		double bomDtl=0.0;
		try {
			String sql = "select a.dblCostRM ,a.dblCostRM/a.dblRecipeConversion from tblproductmaster a where a.strProdCode='" + childCode + "' and a.strClientCode='" + clientCode + "' ";
			List listChildRate = objGlobalFunctionsService.funGetList(sql, "sql");
			if (listChildRate.size() > 0) {
				for(int i=0;i<listChildRate.size();i++)
				{ 
					Object obj[]=(Object[])listChildRate.get(i);
					
					bomDtl.add(Double.parseDouble( obj[0].toString()));
					bomDtl.add(Double.parseDouble( obj[1].toString()));
//					bomDtl= Double.parseDouble(listChildRate.get(0).toString());
					
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bomDtl;
	}
	
	public int funGetBOMNodes(String parentProdCode, double bomQty, double qty, List<String> listChildNodes) {
		
		String sql = "select b.strChildCode from  tblbommasterhd a,tblbommasterdtl b " 
			+ "where a.strBOMCode=b.strBOMCode and a.strParentCode='" + parentProdCode + "' ";
		List listTemp = objGlobalFunctionsService.funGetList(sql, "sql");
		if (listTemp.size() > 0) {
			for (int cnt = 0; cnt < listTemp.size(); cnt++) {
				String childNode = (String) listTemp.get(cnt);
				bomQty = funGetBOMQty(childNode, parentProdCode);
				funGetBOMNodes(childNode, bomQty, qty * bomQty, listChildNodes);
				listChildNodes.add(childNode + "," + bomQty);
			}
		} else {
			
		}
		return 1;
	}
	
	public double funGetBOMQty(String childCode, String parentCode) {
		double bomQty = 0;
		try {
			String sql = "select ifnull(left(((c.dblReceiveConversion/c.dblIssueConversion)/c.dblRecipeConversion),6) * b.dblQty,0) as BOMQty " + "from tblbommasterhd a,tblbommasterdtl b,tblproductmaster c ,tblproductmaster d " + "where a.strBOMCode=b.strBOMCode and a.strParentCode=d.strProdCode and a.strParentCode='" + parentCode + "' and b.strChildCode=c.strProdCode " + "and b.strChildCode='"+ childCode + "'";
			List listChildQty = objGlobalFunctionsService.funGetList(sql, "sql");
			if (listChildQty.size() > 0) {
				bomQty = (Double) listChildQty.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bomQty;
	}


	
	public List funGetBOMLastPurchaseRate(String childCode, String clientCode) {
		List bomDtl =new ArrayList();
		try {
			String sql = "select  b.dblUnitPrice/c.dblRecipeConversion , b.dblUnitPrice from tblgrnhd a, tblgrndtl b,tblproductmaster c where a.strGRNCode=b.strGRNCode and a.strClientCode='"+clientCode+"' and b.strProdCode='"+childCode+"'  and b.strProdCode=c.strProdCode order by a.dtBillDate desc limit 1 ;";
			List listChildRate = objGlobalFunctionsService.funGetList(sql, "sql");
			if (listChildRate.size() > 0) {
				for(int i=0;i<listChildRate.size();i++)
				{ 
					Object obj[]=(Object[])listChildRate.get(i);
					
					bomDtl.add(Double.parseDouble( obj[0].toString()));
					bomDtl.add(Double.parseDouble( obj[1].toString()));
//					bomDtl= Double.parseDouble(listChildRate.get(0).toString());
					
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return bomDtl;
	}


	// ///////////////////////////Yield Concept//////////////////////////////

	//
	// @RequestMapping(value = "/frmYieldMaster", method = RequestMethod.GET)
	// public ModelAndView funOpenFormYield(Map<String,Object> model,
	// HttpServletRequest request)
	// {
	// String urlHits="1";
	// try{
	// urlHits=request.getParameter("saddr").toString();
	// }catch(NullPointerException e){
	// urlHits="1";
	// }
	// model.put("urlHits",urlHits);
	// List<String> listProcess = new ArrayList<>();
	// listProcess.add("Select");
	// listProcess.add("Production");
	// model.put("processList", listProcess);
	// if("2".equalsIgnoreCase(urlHits)){
	// return new ModelAndView("frmYieldMaster_1","command", new
	// clsRecipeMasterBean());
	// }else if("1".equalsIgnoreCase(urlHits)){
	// return new ModelAndView("frmYieldMaster","command", new
	// clsRecipeMasterBean());
	// }else {
	// return null;
	// }
	//
	// }
	//
	//
	//
	// @RequestMapping(value = "/frmYieldMaster1", method = RequestMethod.POST)
	// public ModelAndView funOpenFormWithYieldCode(Map<String,Object> model,
	// HttpServletRequest request)
	// {
	// String urlHits="1";
	// try{
	// urlHits=request.getParameter("saddr").toString();
	// }catch(NullPointerException e){
	// urlHits="1";
	// }
	// model.put("urlHits",urlHits);
	// clsRecipeMasterBean bean=new clsRecipeMasterBean();
	// String
	// clientCode=request.getSession().getAttribute("clientCode").toString();
	// String userCode=request.getSession().getAttribute("usercode").toString();
	//
	// String bomCode=request.getParameter("BOMCode").toString();
	// clsBomHdModel
	// objBomHd=objRecipeMasterService.funGetObject(bomCode,clientCode);
	// bean = funPrepareBean(objBomHd,clientCode);
	// List listBomDtl=objRecipeMasterService.funGetDtlList(bomCode,clientCode);
	// List<clsBomDtlModel> listBOMDtlTemp = new ArrayList<clsBomDtlModel>();
	//
	// for(int i=0;i<listBomDtl.size();i++)
	// {
	// Object[] ob = (Object[])listBomDtl.get(i);
	// clsBomDtlModel bomDtl=(clsBomDtlModel)ob[0];
	// clsProductMasterModel prodMaster=(clsProductMasterModel)ob[1];
	// bomDtl.setStrProdName(prodMaster.getStrProdName());
	// listBOMDtlTemp.add(bomDtl);
	// }
	//
	// List<String> listProcess = new ArrayList<>();
	// listProcess.add(bean.getStrProcessName());
	// model.put("processList", listProcess);
	// bean.setListBomDtlModel(listBOMDtlTemp);
	//
	//
	// if("2".equalsIgnoreCase(urlHits)){
	// return new ModelAndView("frmYieldMaster_1","command",bean);
	// }else if("1".equalsIgnoreCase(urlHits)){
	// return new ModelAndView("frmYieldMaster","command",bean);
	// }else {
	// return new ModelAndView("frmYieldMaster","command",bean);
	// }
	//
	// }
	//
	//
	// @RequestMapping(value = "/saveYieldMaster", method = RequestMethod.POST)
	// public ModelAndView funSaveUpdate(@ModelAttribute("command") @Valid
	// clsRecipeMasterBean objBean,BindingResult result,HttpServletRequest req)
	// {
	// String urlHits="1";
	// try{
	// urlHits=req.getParameter("saddr").toString();
	// }catch(NullPointerException e){
	// urlHits="1";
	// }
	// String clientCode=req.getSession().getAttribute("clientCode").toString();
	// String userCode=req.getSession().getAttribute("usercode").toString();
	// clsBomHdModel objHdModel=null;
	// objGlobal=new clsGlobalFunctions();
	// if(!result.hasErrors())
	// {
	// List<clsBomDtlModel> listBomDtl = objBean.getListBomDtlModel();
	// boolean flagDtlDataInserted = false;
	// if(null != listBomDtl && listBomDtl.size() > 0)
	// {
	// objHdModel=funPrepareYieldModel(objBean,userCode,clientCode);
	// objRecipeMasterService.funAddUpdate(objHdModel);
	// String bomCode=objHdModel.getStrBOMCode();
	// objRecipeMasterService.funDeleteDtl(bomCode,clientCode);
	// for (clsBomDtlModel ob : listBomDtl)
	// {
	// if(null!=ob.getStrChildCode())
	// {
	// ob.setStrBOMCode(bomCode);
	// ob.setStrClientCode(clientCode);
	// objRecipeMasterService.funAddUpdateDtl(ob);
	// }
	// }
	// flagDtlDataInserted=true;
	// }
	// if(flagDtlDataInserted==true)
	// {
	// req.getSession().setAttribute("success", true);
	// req.getSession().setAttribute("successMessage","Recipe Code : ".concat(objHdModel.getStrBOMCode()));
	// }
	// return new ModelAndView("redirect:/frmBOMMaster.html?saddr="+urlHits);
	// }
	// else
	// {
	// return new ModelAndView("frmBOMMaster?saddr="+urlHits);
	// }
	// }
	//
	//
	// private clsBomHdModel funPrepareYieldModel(clsRecipeMasterBean
	// objBean,String userCode,String clientCode)
	// {
	// long lastNo=0;
	// clsBomHdModel objHdModel = new clsBomHdModel();
	// if(objBean.getStrBOMCode().length()==0)
	// {
	// lastNo=objGlobalFunctionsService.funGetLastNo("tblbommasterhd","BOMMaster","intId",
	// clientCode);
	// String code = "B" + String.format("%07d", lastNo);
	// objHdModel.setStrBOMCode(code);
	// objHdModel.setIntId(lastNo);
	// objHdModel.setStrUserCreated(userCode);
	// objHdModel.setDtCreatedDate(objGlobal.funGetCurrentDate("yyyy-MM-dd"));
	// objHdModel.setStrClientCode(clientCode);
	// }
	// else
	// {
	// objHdModel.setStrBOMCode(objBean.getStrBOMCode());
	// }
	// objHdModel.setStrParentCode(objBean.getStrParentCode());
	// objHdModel.setStrProcessCode(objBean.getStrProcessCode());
	// objHdModel.setDtValidFrom(objGlobal.funGetDate("yyyy-MM-dd",objBean.getDtValidFrom()));
	// objHdModel.setDtValidTo(objGlobal.funGetDate("yyyy-MM-dd",objBean.getDtValidTo()));
	// objHdModel.setStrUserModified(userCode);
	// objHdModel.setDtLastModified(objGlobal.funGetCurrentDate("yyyy-MM-dd"));
	// objHdModel.setStrUOM(objBean.getStrUOM());
	// objHdModel.setDblQty(objBean.getDblQty());
	// objHdModel.setStrMethod(objBean.getStrMethod());
	// objHdModel.setStrBOMType("Y");
	// return objHdModel;
	// }
	//
	//
	//
	//

}
