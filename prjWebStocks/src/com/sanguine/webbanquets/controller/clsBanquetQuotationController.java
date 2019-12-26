package com.sanguine.webbanquets.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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

import com.ibm.icu.text.DecimalFormat;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsCompanyMasterModel;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webbanquets.bean.clsBanquetQuotationBean;
import com.sanguine.webbanquets.bean.clsFunctionProspectusBean;
import com.sanguine.webbanquets.model.clsBanquetQuotationModelDtl;
import com.sanguine.webbanquets.model.clsBanquetQuotationModelHd;
import com.sanguine.webbanquets.service.clsBanquetQuotationService;


@Controller
public class clsBanquetQuotationController{

	@Autowired
	private clsBanquetQuotationService objBanquetQuotationService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsSetupMasterService objSetupMasterService;
	
	@Autowired
	private clsGlobalFunctions objGlobalFunctions;
	@Autowired
	private ServletContext servletContext;


//Open BanquetQuotation
	
	DecimalFormat df= new DecimalFormat("#.##");
	@RequestMapping(value = "/frmBanquetQuotation", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request){

		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		String webStockDB=request.getSession().getAttribute("WebStockDB").toString();
		List listOfProperty = objGlobalFunctionsService.funGetList("select strPropertyName from "+webStockDB+".tblpropertymaster where strPropertyCode='" + propCode + "' ");
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		model.put("listOfProperty", listOfProperty);
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBanquetQuotation_1", "command", new clsBanquetQuotationBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBanquetQuotation", "command", new clsBanquetQuotationBean());
		} else {
			return null;
		
		}
		
	}
//Load Header Table Data On Form
	@RequestMapping(value = "/loadBanquetQuotationHd", method = RequestMethod.GET)
	public @ResponseBody clsBanquetQuotationModelHd funLoadHdData(@RequestParam("QuotationCode") String QuotationCode,HttpServletRequest request){
		
		String sql="";
			String clientCode=request.getSession().getAttribute("clientCode").toString();
			String userCode=request.getSession().getAttribute("usercode").toString();
			clsBanquetQuotationModelHd objHDModel=objBanquetQuotationService.funGetQuotationData(QuotationCode, clientCode);
			if (null == objHDModel) {
				objHDModel = new clsBanquetQuotationModelHd();
				objHDModel.setStrQuotationNo("Invalid Code");
			}
			return objHDModel;
	}



//Save or Update BanquetQuotation
	/**
	 * @param objBean
	 * @param result
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/saveBanquetQuotation", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(Map<String, Object> model,@ModelAttribute("command") @Valid clsBanquetQuotationBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			String propCode=req.getSession().getAttribute("propertyCode").toString();
			String startDate=req.getSession().getAttribute("startDate").toString();
			boolean flag=true;			
			
				clsBanquetQuotationModelHd objHdModel = funPrepareHdModel(objBean,userCode,clientCode,req,propCode);
				List<clsBanquetQuotationModelDtl> listQuotationDtl=new ArrayList<clsBanquetQuotationModelDtl>();
				
				objBanquetQuotationService.funAddUpdateBanquetQuotationHd(objHdModel);
						
			if(objBean.getListSeriveDtl()!=null && !objBean.getListSeriveDtl().isEmpty())
			{
				List<clsBanquetQuotationModelDtl>list=objBean.getListSeriveDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetQuotationModelDtl objDtl=list.get(i);
					if(objDtl.getStrType()!=null)
					{
						objDtl.setStrType("In Service");
						objDtl.setDblDocQty(1);
						objDtl.setStrQuotationDate(objHdModel.getDteQuotationDate());
						objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
						objDtl.setStrVendorCode("");
						listQuotationDtl.add(objDtl);	
					}				
				}				
			}
			if(objBean.getListEquipDtl()!=null && !objBean.getListEquipDtl().isEmpty())
			{
				List<clsBanquetQuotationModelDtl>list=objBean.getListEquipDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetQuotationModelDtl objDtl=list.get(i);
				
					if(objDtl.getStrDocName()!=null)
					{
					objDtl.setStrType("Equipment");
					objDtl.setStrQuotationDate(objHdModel.getDteQuotationDate());
					objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
					objDtl.setStrVendorCode("");
					listQuotationDtl.add(objDtl);	
					}
				}	
			}
			if(objBean.getListStaffCatDtl()!=null && !objBean.getListStaffCatDtl().isEmpty())
			{
				List<clsBanquetQuotationModelDtl>list=objBean.getListStaffCatDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetQuotationModelDtl objDtl=list.get(i);
					if(objDtl.getStrDocName()!=null)
					{
					objDtl.setStrType("Staff");
					objDtl.setStrQuotationDate(objHdModel.getDteQuotationDate());
					objDtl.setStrVendorCode("");
					listQuotationDtl.add(objDtl);					
					}
				}				
			}
			if(objBean.getListMenuItemDtl()!=null && !objBean.getListMenuItemDtl().isEmpty())
			{
				List<clsBanquetQuotationModelDtl>list=objBean.getListMenuItemDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetQuotationModelDtl objDtl=list.get(i);
					if(objDtl.getStrDocName()!=null)
					{
					objDtl.setStrType("Menu");
					objDtl.setStrQuotationDate(objHdModel.getDteQuotationDate());
					objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
					objDtl.setStrVendorCode("");
					listQuotationDtl.add(objDtl);					
					}
				}				
			}
			if(objBean.getListExternalServices()!=null && !objBean.getListExternalServices().isEmpty())
			{
				List<clsBanquetQuotationModelDtl>list=objBean.getListExternalServices();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetQuotationModelDtl objDtl=list.get(i);
						objDtl.setStrType("Ex Service");
						objDtl.setDblDocQty(1);
						objDtl.setStrQuotationDate(objHdModel.getDteQuotationDate());
						objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
						listQuotationDtl.add(objDtl);	
									
				}				
			}
			if(objBean.getStrBanquetCode()!=null)
			{
				String sql = "SELECT b.dblRate,a.strBanquetName FROM tblbanquetmaster a,tblbanquettypemaster b WHERE a.strBanquetCode='"+objBean.getStrBanquetCode()+"' and a.strBanquetTypeCode=b.strBanquetTypeCode and a.strClientCode='"+clientCode+"' ";
				List listBanquet = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");				
				if(!listBanquet.isEmpty())
				{			
					clsBanquetQuotationModelDtl objDtl=new clsBanquetQuotationModelDtl();
					Object[] obj = (Object[])listBanquet.get(0);					
					objDtl.setStrType("Banquet");
					objDtl.setStrDocNo(objBean.getStrBanquetCode());
					objDtl.setDblDocQty(1);
					objDtl.setDblDocRate(Double.parseDouble(obj[0].toString()));;
					objDtl.setStrDocName(obj[1].toString());
					objDtl.setStrQuotationDate(objHdModel.getDteQuotationDate());
					objDtl.setDblDocTotalAmt(Double.parseDouble(obj[0].toString()));
					objDtl.setStrVendorCode("");
					listQuotationDtl.add(objDtl);	
					
				}
			}
			
			objHdModel.setListBanquetQuotationDtlModels(listQuotationDtl);
			objBanquetQuotationService.funAddUpdateBanquetQuotationHd(objHdModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Quotation No : ".concat(objHdModel.getStrQuotationNo()));
			return new ModelAndView("redirect:/frmBanquetQuotation.html");
			}
		else{
			return new ModelAndView("frmBanquetQuotation");
		}
	}

//Convert bean to model function
	private clsBanquetQuotationModelHd funPrepareHdModel(clsBanquetQuotationBean objBean,String userCode,String clientCode,HttpServletRequest req,String propCode){

		long lastNo=0;
		String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
		clsBanquetQuotationModelHd objHDModel=new clsBanquetQuotationModelHd();
		if (objBean.getStrQuotationNo().isEmpty()) // New Entry
		{
			String[] bookDate = objBean.getDteQuotationDate().split("-");
			String dateBook = bookDate[2] + "-" + bookDate[1] + "-" + bookDate[0];
			String bookCode ="";
		
				String transYear="A";
				List<clsCompanyMasterModel> listClsCompanyMasterModel = objSetupMasterService.funGetListCompanyMasterModel();
				if (listClsCompanyMasterModel.size() > 0) {
					clsCompanyMasterModel objCompanyMasterModel = listClsCompanyMasterModel.get(listClsCompanyMasterModel.size() - 1);
					transYear=objCompanyMasterModel.getStrYear();
				}
				
				String[] spDate = dateBook.split("-");
				String transMonth = objGlobalFunctions.funGetAlphabet(Integer.parseInt(spDate[1])-1);
				String sql = "select ifnull(max(MID(a.strQuotationNo,8,5)),'' )" + " from tblbqquotationhd a where MID(a.strQuotationNo,5,1) = '" + transYear + "' " + " and MID(a.strQuotationNo,6,1) = '" + transMonth + "' " + " and MID(a.strQuotationNo,1,2) = '" + propCode + "' and strClientCode='" + clientCode + "' ";
				/*String sqlAudit = " select ifnull(max(MID(a.strTransCode,8,5)),'' ) " + " from "+webStockDB+".tblaudithd a where MID(a.strTransCode,5,1) = '" + transYear + "' " + " and MID(a.strTransCode,6,1) = '" + transMonth + "' " + " and MID(a.strTransCode,1,2) = '" + propCode + "' and strClientCode='" + clientCode + "' " + "and a.strTransType='Invoice' ;  ";*/
				
				String sqlAudit = "SELECT IFNULL(MAX(MID(a.strQuotationNo,8,5)),'') "
						+ "FROM tblbqquotationhd a "
						+ "WHERE MID(a.strQuotationNo,5,1) = '"+transYear+"' AND MID(a.strQuotationNo,6,1) = '"+transMonth+"' AND MID(a.strQuotationNo,1,2) = '"+propCode+"' AND strClientCode='"+clientCode+"'";

				
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
				if (lastnoLive > lastnoAudit) {
					bookCode = propCode + "BQ" + transYear + transMonth  + String.format("%05d", lastnoLive + 1);
				} else {
					bookCode = propCode + "BQ" + transYear + transMonth  +String.format("%05d", lastnoAudit + 1);

			   }
			objHDModel.setStrQuotationNo(bookCode);
		
		}
		else // Update
		{
			/*objBanquetQuotationService.funDeleteRecord("delete from clsBanquetQuotationModelHd where strQuotationNo='" + objBean.getStrQuotationNo() + "'  and strClientCode='" + clientCode + "'","hql");
			objBanquetQuotationService.funDeleteRecord("delete from tblbqquotationdtl  where strQuotationNo='" + objBean.getStrQuotationNo() + "'  and strClientCode='" + clientCode + "';","sql");
		*/
			objHDModel.setStrQuotationNo(objBean.getStrQuotationNo());
			
		}
		objHDModel.setStrUserCreated(userCode);
		objHDModel.setStrUserEdited(userCode);
		objHDModel.setStrPropertyCode(propCode);
		objHDModel.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
		objHDModel.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
		objHDModel.setStrClientCode(clientCode);
		objHDModel.setDteQuotationDate(objGlobalFunctions.funGetDateAndTime("yyyy-MM-dd", objBean.getDteQuotationDate()));
		objHDModel.setDteFromDate(objGlobalFunctions.funGetDateAndTime("yyyy-MM-dd", objBean.getDteFromDate()));
		objHDModel.setDteToDate(objGlobalFunctions.funGetDateAndTime("yyyy-MM-dd", objBean.getDteToDate()));
		objHDModel.setIntMaxPaxNo(objBean.getIntMaxPaxNo());
		objHDModel.setIntMinPaxNo(objBean.getIntMinPaxNo());
		objHDModel.setStrAreaCode(objBean.getStrAreaCode());
		objHDModel.setStrBillingInstructionCode(objBean.getStrBillingInstructionCode());
		objHDModel.setStrQuotationStatus(objBean.getStrQuotationStatus());
		objHDModel.setStrCustomerCode(objBean.getStrCustomerCode());
		objHDModel.setStrEmailID(objBean.getStrEmailID());
		objHDModel.setStrEventCoordinatorCode(objBean.getStrEventCoordinatorCode());
		objHDModel.setStrFunctionCode(objBean.getStrFunctionCode());
		objHDModel.setTmeFromTime(objBean.getTmeFromTime());
		objHDModel.setTmeToTime(objBean.getTmeToTime());
		objHDModel.setDblSubTotal(Double.parseDouble(df.format(objBean.getDblSubTotal())));
		objHDModel.setStrBanquetCode(objBean.getStrBanquetCode());
		
		return objHDModel;

	}
	
	@RequestMapping(value = "/loadQuotationFunServiceData", method = RequestMethod.GET)
	public @ResponseBody List funLoadQuotationFunServiceData(@RequestParam("functionCode") String funCode,@RequestParam("QuotationCode") String  QuotationCode, HttpServletRequest req)
	{
		List list =null;
		try{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String 	 sql="select  a.strServiceCode,a.strServiceName ,s.dblRate, if(ifnull(b.strDocNo,'')='','N','Y')"
					+ " from tblservicemaster s , tblfunctionservice a left outer join tblbqquotationdtl b on a.strServiceCode=b.strDocNo and b.strQuotationNo='"+QuotationCode+"'"
					+ "where  a.strFunctionCode='"+funCode+"' and s.strServiceCode=a.strServiceCode  and s.strClientCode='"+clientCode+"' and s.strServiceType='Internal' group by  a.strServiceCode;";
			list= objGlobalFunctionsService.funGetDataList(sql, "sql");		
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return list;
	}
	
	@RequestMapping(value = "/loadQuotationExternalServiceData", method = RequestMethod.GET)
	public @ResponseBody List funLoadQuotationExternalServiceData(@RequestParam("functionCode") String funCode,@RequestParam("QuotationCode") String  QuotationCode, HttpServletRequest req)
	{
		List list =null;
		try{
			String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String 	 sql="select a.strDocNo,a.strDocName,a.dblDocRate,a.strVendorCode, b.strPName "
					+ " from tblbqquotationdtl a ,`"+webStockDB+"`.tblpartymaster b "
					+ "where a.strVendorCode=b.strPCode and  a.strClientCode='"+QuotationCode+"' and a.strType='Ex Service';";
			
			list= objGlobalFunctionsService.funGetDataList(sql, "sql");		
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return list;
	}

	
	@RequestMapping(value = "/loadPropertyForQuotationCode", method = RequestMethod.GET)
	public @ResponseBody List funLoadPropertyCode(@RequestParam("docCode") String docCode,HttpServletRequest req)
	{
		List list =null;
		try{
			String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String 	 sql="select a.strPropertyName from "+webStockDB+".tblpropertymaster a where a.strPropertyCode='"+docCode+"' and a.strClientCode='"+clientCode+"'";
			list= objGlobalFunctionsService.funGetDataList(sql, "sql");		
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return list;
	}
	
	@RequestMapping(value = "/loadQuotationServiceData", method = RequestMethod.GET)
	public @ResponseBody List funLoadFunctionServiceMasterData(@RequestParam("functionCode") String funCode,HttpServletRequest req)
	{
		List list =null;
		try{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String 	 sql="select a.strServiceCode,a.strServiceName,b.dblRate from tblfunctionservice a,tblservicemaster b where "
					+ " a.strServiceCode=b.strServiceCode and   a.strFunctionCode='"+funCode+"' and a.strClientCode='"+clientCode+"' and a.strApplicable='Y' and b.strServiceType='Internal' ";
			list= objGlobalFunctionsService.funGetDataList(sql, "sql");		
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return list;
	}
	
	@RequestMapping(value = "/loadBanquetRateForQuotation", method = RequestMethod.GET)
	public @ResponseBody List funLoadBanquetRate(@RequestParam("BanquetCode") String banquetCode,HttpServletRequest req)
	{
		List list =null;
		try{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String 	 sql="	select a.strBanquetName,b.strBanquetTypeName,b.dblRate from tblbanquetmaster a,tblbanquettypemaster b"
					+ " where a.strBanquetTypeCode=b.strBanquetTypeCode and a.strOperational='Y'  and a.strClientCode='"+clientCode+"' and a.strBanquetCode='"+banquetCode+"'";
			list= objGlobalFunctionsService.funGetDataList(sql, "sql");		
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return list;
	}

	
	@RequestMapping(value = "/checkQuotationStaffCnt", method = RequestMethod.GET)
	public @ResponseBody boolean funCheckStaffCnt(@RequestParam("staffCode") String staffCode,@RequestParam("staffCnt") String staffCnt,HttpServletRequest req)
	{
		List list =null;
		
		boolean condition = false;
		double dblCnt = Double.parseDouble(staffCnt);
		try{
			String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String 	 sql="select a.strStaffCount from tblstaffcategeorymaster a where a.strStaffCategeoryCode='"+staffCode+"' and a.strClientCode='"+clientCode+"'";
			list= objGlobalFunctionsService.funGetDataList(sql, "sql");
			if(list!=null && list.size()>0)
			{
				Double strCnt = Double.parseDouble(list.get(0).toString());
				if(dblCnt > strCnt)
				{
					condition = false;
				}
				else
				{
					condition = true;
				}
			}
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return condition;
	}
	
	
	
	
	@RequestMapping(value = "/checkQuotation", method = RequestMethod.GET)
	public @ResponseBody boolean funCheckQuotation(@RequestParam("fromTime") String fromTime,@RequestParam("fromDate") String fromDate,@RequestParam("locName") String locName,HttpServletRequest req)
	{
		String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
		List list =null;		
		boolean condition = false;		
		try{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String sqlCheck = "select a.strQuotationNo,b.strLocName from tblbqquotationhd a ,"+webStockDB+".tbllocationmaster b "
					+ "where '"+objGlobalFunctions.funGetDate("yyyy-MM-dd", fromDate)+"' between Date(a.dteFromDate) and Date(a.dteToDate) "
					+ "AND '"+fromTime+"' between a.tmeFromTime and a.tmeToTime and a.strClientCode='"+clientCode+"'  AND a.strAreaCode=b.strLocCode";
			
			List listAudit = objGlobalFunctionsService.funGetListModuleWise(sqlCheck, "sql");
			if(!listAudit.isEmpty())
			{
				Object[] obj = (Object[])listAudit.get(0);	
				if(locName.equalsIgnoreCase(obj[1].toString()))
				{
					condition=true;
				}
			}
		}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return condition;
	}
	
	@RequestMapping(value = "/rptBanquetQuotation", method = RequestMethod.GET)
	public void funGenerateFunctionProspectus(@RequestParam("code") String code, HttpServletRequest req, HttpServletResponse resp) 
	{
		try 
		{
			funGenarateFunctionProspectus(code,req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
		 
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public void funGenarateFunctionProspectus(String strBookingNo,HttpServletRequest req,HttpServletResponse resp) throws Exception 
	{
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String propertyCode = req.getSession().getAttribute("propertyCode").toString();
		String companyName = req.getSession().getAttribute("companyName").toString();
		String webStockDB = req.getSession().getAttribute("WebStockDB").toString();
		String sql="";
		String fromDate="",toDate="";
		String strServiceType="";
		HashMap reportParams = new HashMap();
		clsFunctionProspectusBean objBean=null;
		List listStaff = new ArrayList<>();
		List listEquipment = new ArrayList<>();
		List listService = new ArrayList<>();
		List listMenu = new ArrayList<>();
		List listExtService = new ArrayList<>();
		List listIntService = new ArrayList<>();
		double dblTotalAmt = 0.0;
		
		String reportName = servletContext.getRealPath("/WEB-INF/reports/webbanquet/rptBanquetQuotation.jrxml");
		String imagePath = servletContext.getRealPath("/resources/images/Sanguine_Logo_Icon.png");
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
		if (objSetup == null) {
			objSetup = new clsPropertySetupModel();
		}		
		
		sql="SELECT a.strQuotationNo,b.strType,a.strCustomerCode,a.dteQuotationDate,c.strLocName,a.strFunctionCode,a.intMinPaxNo,a.intMaxPaxNo,a.tmeFromTime,a.tmeToTime, IFNULL(d.strStaffName,''), IFNULL(d.strMobile,''), IFNULL(d.strEmail,''), IFNULL(g.strBanquetName,''), DATE(a.dteToDate)- DATE(a.dteFromDate) "
				+ "FROM tblbqquotationhd a "
				+ "LEFT OUTER "
				+ "JOIN tblbqquotationdtl b ON b.strQuotationNo=a.strQuotationNo "
				+ "LEFT OUTER "
				+ "JOIN "+webStockDB+".tbllocationmaster c ON c.strLocCode=a.strAreaCode "
				+ "LEFT OUTER "
				+ "JOIN tblstaffmaster d ON d.strStaffCode=a.strEventCoordinatorCode "
				+ "LEFT OUTER "
				+ "JOIN bankmms.tblpartymaster e ON e.strPCode=a.strCustomerCode "
				+ "LEFT OUTER "
				+ "JOIN tblfunctionmaster f ON f.strFunctionCode=a.strFunctionCode "
				+ "LEFT OUTER "
				+ "JOIN tblbanquetmaster g ON a.strBanquetCode=g.strBanquetCode "
				+ "WHERE a.strQuotationNo=b.strQuotationNo AND a.strQuotationNo='"+strBookingNo+"' AND a.strClientCode='"+clientCode+"' "
				+ "GROUP BY b.strType";
		
		List listData = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		
		for(int i=0;i<listData.size();i++)
		{
			Object[] obj = (Object[])listData.get(i);
			strServiceType=obj[1].toString();
			reportParams.put("pArea", obj[4].toString());
			reportParams.put("pEventCoordinator", obj[10].toString());
			reportParams.put("pMobile", obj[11].toString());
			reportParams.put("pEmail", obj[12].toString());
			reportParams.put("pBanquetName", obj[13].toString());
			if(obj[2].toString().startsWith("C"))
			{
				sql="SELECT a.strPCode,a.strPName,a.strOperational FROM tblpartymaster a WHERE a.strPCode='"+obj[2].toString()+"' "
					+ "AND a.strPropCode='"+propertyCode+"' AND a.strOperational='Y' AND a.strClientCode='"+clientCode+"'";
				List listCustDetails = objGlobalFunctionsService.funGetList(sql, "sql");
				for(int m=0;m<listCustDetails.size();m++)
				{
					Object[] objCust = (Object[])listCustDetails.get(m);
					reportParams.put("pCustomerName", objCust[1].toString());
				}
			}
			if(obj[5].toString().startsWith("FM"))
			{
				sql="SELECT b.strType,b.strDocName,c.strFunctionName,b.dblDocQty,b.dblDocRate "
						+ "FROM tblbqquotationhd a "
						+ "LEFT OUTER "
						+ "JOIN tblbqquotationdtl b ON b.strQuotationNo=a.strQuotationNo "
						+ "LEFT OUTER "
						+ "JOIN tblfunctionmaster c ON c.strFunctionCode=a.strFunctionCode "
						+ "WHERE a.strQuotationNo='"+strBookingNo+"' AND c.strFunctionCode='"+obj[5].toString()+"' AND c.strOperationalYN='Y' AND b.strType='"+strServiceType+"' AND a.strPropertyCode='"+propertyCode+"' AND a.strClientCode='"+clientCode+"';";
					List listFunction = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
					for(int k=0;k<listFunction.size();k++)
					{
						Object[] objFunction = (Object[])listFunction.get(k);
						reportParams.put("pFunction", objFunction[2].toString());
						if(strServiceType.equals("Staff"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
					        objBean.setStrQty(objFunction[3].toString());
					        objBean.setDblTotal(Double.parseDouble(objFunction[4].toString()));
					        dblTotalAmt+=Double.parseDouble(objFunction[4].toString());
							listStaff.add(objBean);
						}
						else if(strServiceType.equals("Service"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							listService.add(objBean);
						}
						else if(strServiceType.equals("Equipment"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							 objBean.setStrQty(objFunction[3].toString());
							 objBean.setDblTotal(Double.parseDouble(objFunction[4].toString()));
							 dblTotalAmt+=Double.parseDouble(objFunction[4].toString());
							listEquipment.add(objBean);
						}
						else if(strServiceType.equals("Menu"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							objBean.setStrQty(objFunction[3].toString());
							objBean.setDblTotal(Double.parseDouble(objFunction[4].toString()));
							dblTotalAmt+=Double.parseDouble(objFunction[4].toString());
						    listMenu.add(objBean);
						}
						else if(strServiceType.equals("Ex Service"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							objBean.setStrQty(objFunction[3].toString());
							objBean.setDblTotal(Double.parseDouble(objFunction[4].toString()));
							dblTotalAmt+=Double.parseDouble(objFunction[4].toString());
						    listExtService.add(objBean);
						}
						else if(strServiceType.equals("In Service"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							objBean.setStrQty(objFunction[3].toString());
							objBean.setDblTotal(Double.parseDouble(objFunction[4].toString()));
							dblTotalAmt+=Double.parseDouble(objFunction[4].toString());
						    listIntService.add(objBean);
						}
						/*else if(strServiceType.equals("Banquet"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							objBean.setStrQty(objFunction[3].toString());
						    listMenu.add(objBean);
						}*/
					}
			}
			
			reportParams.put("pBookingNo", obj[0].toString());
			reportParams.put("pDate", obj[3].toString().substring(0,obj[3].toString().indexOf(" ")).split("-")[2]+"-"+obj[3].toString().substring(0,obj[3].toString().indexOf(" ")).split("-")[1]+"-"+obj[3].toString().substring(0,obj[3].toString().indexOf(" ")).split("-")[0]);
			reportParams.put("pTime", obj[3].toString().substring(obj[3].toString().indexOf(" "),obj[3].toString().length()-3));
			reportParams.put("pPAX", obj[6].toString()+" - "+obj[7].toString());
			int k=(int) Math.round(Double.parseDouble(obj[14].toString()));			
			String frTime[]=obj[8].toString().split(":");
			String toTime[]=obj[9].toString().split(":");
			int hrsCnt=Integer.parseInt(frTime[0])-Integer.parseInt(toTime[0]);
			int minCnt=Integer.parseInt(frTime[1])-Integer.parseInt(toTime[1]);
			
			String hrsCount=null;
			if(String.valueOf(hrsCnt).startsWith("-"))
			{
				String hrsValue[]=String.valueOf(hrsCnt).split("-");
				hrsCount=hrsValue[1];
			}
			else
			{
				hrsCount=String.valueOf(hrsCnt);
			}
			String minCount=null;
			if(String.valueOf(minCnt).startsWith("-"))
			{
				String hrsValue[]=String.valueOf(minCnt).split("-");
				minCount=hrsValue[1];
			}
			else
			{
				minCount=String.valueOf(minCnt);
			}
			reportParams.put("pDuration",k+" Day"+ " "+hrsCount+" Hours "+minCount+" Minutes");
			
			
			//reportParams.put("pDuration",obj[8].toString()+" - "+obj[9].toString());
			
		}
		reportParams.put("pCompanyName", companyName);
		reportParams.put("pAddress1", objSetup.getStrAdd1() + "," + objSetup.getStrAdd2() + "," + objSetup.getStrCity());
		reportParams.put("pAddress2", objSetup.getStrState() + "," + objSetup.getStrCountry() + "," + objSetup.getStrPin());
		reportParams.put("strUserCode", userCode);
		reportParams.put("strImagePath", imagePath);
		reportParams.put("listStaff", listStaff);
		reportParams.put("listEquipment", listEquipment);
		reportParams.put("listService", listService);
		reportParams.put("listMenu", listMenu);
		reportParams.put("listExtService", listExtService);
		reportParams.put("listIntService", listIntService);
		reportParams.put("pdblTotalAmt", dblTotalAmt);
		
		
		JasperDesign jd = JRXmlLoader.load(reportName);
		JasperReport jr = JasperCompileManager.compileReport(jd);
		JasperPrint jp = JasperFillManager.fillReport(jr, reportParams,new JREmptyDataSource());
		List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		if (jp != null) 
		{
			jprintlist.add(jp);
			ServletOutputStream servletOutputStream = resp.getOutputStream();
			JRExporter exporter = new JRPdfExporter();
			resp.setContentType("application/pdf");
			exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
			exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
			exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
			resp.setHeader("Content-Disposition", "inline;filename=FunctionProspectus_" + fromDate + "_To_" + toDate + "_" + userCode + ".pdf");
			exporter.exportReport();
			servletOutputStream.flush();
			servletOutputStream.close();
		}
	
		
	}
	  
	  
	
}
