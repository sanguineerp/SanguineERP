package com.sanguine.webbanquets.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.sanguine.webbanquets.bean.clsBanquetBookingBean;
import com.sanguine.webbanquets.bean.clsFunctionProspectusBean;
import com.sanguine.webbanquets.model.clsBanquetBookingModelDtl;
import com.sanguine.webbanquets.model.clsBanquetBookingModelHd;
import com.sanguine.webbanquets.model.clsBanquetQuotationModelHd;
import com.sanguine.webbanquets.service.clsBanquetBookingService;
import com.sanguine.webbanquets.service.clsBanquetQuotationService;

@Controller
public class clsBanquetBookingController{

	@Autowired
	private clsBanquetBookingService objBanquetBookingService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsSetupMasterService objSetupMasterService;
	
	@Autowired
	private clsGlobalFunctions objGlobalFunctions;

	@Autowired
	private clsBanquetQuotationService objBanquetQuotationService;
//Open BanquetBooking
	
	DecimalFormat df= new DecimalFormat("#.##");
	@RequestMapping(value = "/frmBanquetBooking", method = RequestMethod.GET)
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
			return new ModelAndView("frmBanquetBooking_1", "command", new clsBanquetBookingBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBanquetBooking", "command", new clsBanquetBookingBean());
		} else {
			return null;
		
		}
		
	}
//Load Header Table Data On Form
	@RequestMapping(value = "/loadBanquetBookingHd", method = RequestMethod.GET)
	public @ResponseBody clsBanquetBookingModelHd funLoadHdData(@RequestParam("bookingCode") String bookingCode,HttpServletRequest request){
		
		String sql="";
			String clientCode=request.getSession().getAttribute("clientCode").toString();
			String userCode=request.getSession().getAttribute("usercode").toString();
			clsBanquetBookingModelHd objHDModel=objBanquetBookingService.funGetBookingData(bookingCode, clientCode);
			if (null == objHDModel) {
				objHDModel = new clsBanquetBookingModelHd();
				objHDModel.setStrBookingNo("Invalid Code");
			}
			return objHDModel;
	}

	@RequestMapping(value = "/loadBanquetQuotation", method = RequestMethod.GET)
	public @ResponseBody clsBanquetQuotationModelHd funBanquetQuotationHd(@RequestParam("quotationCode") String bookingCode,HttpServletRequest request){
		
		String sql="";
			String clientCode=request.getSession().getAttribute("clientCode").toString();
			String userCode=request.getSession().getAttribute("usercode").toString();
			clsBanquetQuotationModelHd objHDModel=objBanquetQuotationService.funGetQuotationData(bookingCode, clientCode);
			if (null == objHDModel) {
				objHDModel = new clsBanquetQuotationModelHd();
				objHDModel.setStrQuotationNo("Invalid Code");
			}
			return objHDModel;
	}


//Save or Update BanquetBooking
	/**
	 * @param objBean
	 * @param result
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/saveBanquetBooking", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(Map<String, Object> model,@ModelAttribute("command") @Valid clsBanquetBookingBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			String propCode=req.getSession().getAttribute("propertyCode").toString();
			String startDate=req.getSession().getAttribute("startDate").toString();
			boolean flag=true;			
			
				clsBanquetBookingModelHd objHdModel = funPrepareHdModel(objBean,userCode,clientCode,req,propCode);
				List<clsBanquetBookingModelDtl> listBookingDtl=new ArrayList<clsBanquetBookingModelDtl>();
				
				objBanquetBookingService.funAddUpdateBanquetBookingHd(objHdModel);
						
			if(objBean.getListSeriveDtl()!=null && !objBean.getListSeriveDtl().isEmpty())
			{
				List<clsBanquetBookingModelDtl>list=objBean.getListSeriveDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetBookingModelDtl objDtl=list.get(i);
					if(objDtl.getStrType()!=null)
					{
						objDtl.setStrType("In Service");
						objDtl.setDblDocQty(1);
						objDtl.setStrBookingDate(objHdModel.getDteBookingDate());
						objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
						objDtl.setStrVendorCode("");
						listBookingDtl.add(objDtl);	
					}				
				}				
			}
			if(objBean.getListEquipDtl()!=null && !objBean.getListEquipDtl().isEmpty())
			{
				List<clsBanquetBookingModelDtl>list=objBean.getListEquipDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetBookingModelDtl objDtl=list.get(i);
				
					if(objDtl.getStrDocName()!=null)
					{
					objDtl.setStrType("Equipment");
					objDtl.setStrBookingDate(objHdModel.getDteBookingDate());
					objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
					objDtl.setStrVendorCode("");
					listBookingDtl.add(objDtl);	
					}
				}	
			}
			if(objBean.getListStaffCatDtl()!=null && !objBean.getListStaffCatDtl().isEmpty())
			{
				List<clsBanquetBookingModelDtl>list=objBean.getListStaffCatDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetBookingModelDtl objDtl=list.get(i);
					if(objDtl.getStrDocName()!=null)
					{
					objDtl.setStrType("Staff");
					objDtl.setStrBookingDate(objHdModel.getDteBookingDate());
					objDtl.setStrVendorCode("");
					listBookingDtl.add(objDtl);					
					}
				}				
			}
			if(objBean.getListMenuItemDtl()!=null && !objBean.getListMenuItemDtl().isEmpty())
			{
				List<clsBanquetBookingModelDtl>list=objBean.getListMenuItemDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetBookingModelDtl objDtl=list.get(i);
					if(objDtl.getStrDocName()!=null)
					{
					objDtl.setStrType("Menu");
					objDtl.setStrBookingDate(objHdModel.getDteBookingDate());
					objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
					objDtl.setStrVendorCode("");
					listBookingDtl.add(objDtl);					
					}
				}				
			}
			if(objBean.getListExternalServices()!=null && !objBean.getListExternalServices().isEmpty())
			{
				List<clsBanquetBookingModelDtl>list=objBean.getListExternalServices();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetBookingModelDtl objDtl=list.get(i);
						objDtl.setStrType("Ex Service");
						objDtl.setDblDocQty(1);
						objDtl.setStrBookingDate(objHdModel.getDteBookingDate());
						objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
						listBookingDtl.add(objDtl);	
									
				}				
			}
			if(objBean.getStrBanquetCode()!=null)
			{
				String sql = "SELECT b.dblRate,a.strBanquetName FROM tblbanquetmaster a,tblbanquettypemaster b WHERE a.strBanquetCode='"+objBean.getStrBanquetCode()+"' and a.strBanquetTypeCode=b.strBanquetTypeCode and a.strClientCode='"+clientCode+"' ";
				List listBanquet = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");				
				if(!listBanquet.isEmpty())
				{			
					clsBanquetBookingModelDtl objDtl=new clsBanquetBookingModelDtl();
					Object[] obj = (Object[])listBanquet.get(0);					
					objDtl.setStrType("Banquet");
					objDtl.setStrDocNo(objBean.getStrBanquetCode());
					objDtl.setDblDocQty(1);
					objDtl.setDblDocRate(Double.parseDouble(obj[0].toString()));;
					objDtl.setStrDocName(obj[1].toString());
					objDtl.setStrBookingDate(objHdModel.getDteBookingDate());
					objDtl.setDblDocTotalAmt(Double.parseDouble(obj[0].toString()));
					objDtl.setStrVendorCode("");
					listBookingDtl.add(objDtl);	
					
				}
			}
			
			objHdModel.setListBanquetBookingDtlModels(listBookingDtl);
			objBanquetBookingService.funAddUpdateBanquetBookingHd(objHdModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Booking No : ".concat(objHdModel.getStrBookingNo()));
			return new ModelAndView("redirect:/frmBanquetBooking.html");
			}
		else{
			return new ModelAndView("frmBanquetBooking");
		}
	}

//Convert bean to model function
	private clsBanquetBookingModelHd funPrepareHdModel(clsBanquetBookingBean objBean,String userCode,String clientCode,HttpServletRequest req,String propCode){

		long lastNo=0;
		String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
		clsBanquetBookingModelHd objHDModel=new clsBanquetBookingModelHd();
		if (objBean.getStrBookingNo().isEmpty()) // New Entry
		{
			String[] bookDate = objBean.getDteBookingDate().split("-");
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
				String sql = "select ifnull(max(MID(a.strBookingNo,8,5)),'' )" + " from tblbqbookinghd a where MID(a.strBookingNo,5,1) = '" + transYear + "' " + " and MID(a.strBookingNo,6,1) = '" + transMonth + "' " + " and MID(a.strBookingNo,1,2) = '" + propCode + "' and strClientCode='" + clientCode + "' ";
				String sqlAudit = " select ifnull(max(MID(a.strTransCode,8,5)),'' ) " + " from "+webStockDB+".tblaudithd a where MID(a.strTransCode,5,1) = '" + transYear + "' " + " and MID(a.strTransCode,6,1) = '" + transMonth + "' " + " and MID(a.strTransCode,1,2) = '" + propCode + "' and strClientCode='" + clientCode + "' " + "and a.strTransType='Invoice' ;  ";
				
				
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
					bookCode = propCode + "BK" + transYear + transMonth  + String.format("%05d", lastnoLive + 1);
				} else {
					bookCode = propCode + "BK" + transYear + transMonth  +String.format("%05d", lastnoAudit + 1);

			   }
			objHDModel.setStrBookingNo(bookCode);
		
		}
		else // Update
		{
			/*objBanquetBookingService.funDeleteRecord("delete from clsBanquetBookingModelHd where strBookingNo='" + objBean.getStrBookingNo() + "'  and strClientCode='" + clientCode + "'","hql");
			objBanquetBookingService.funDeleteRecord("delete from tblbqbookingdtl  where strBookingNo='" + objBean.getStrBookingNo() + "'  and strClientCode='" + clientCode + "';","sql");
		*/
			objHDModel.setStrBookingNo(objBean.getStrBookingNo());
			
		}
		objHDModel.setStrUserCreated(userCode);
		objHDModel.setStrUserEdited(userCode);
		objHDModel.setStrPropertyCode(propCode);
		objHDModel.setDteDateCreated(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
		objHDModel.setDteDateEdited(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
		objHDModel.setStrClientCode(clientCode);
		objHDModel.setDteBookingDate(objGlobalFunctions.funGetDateAndTime("yyyy-MM-dd", objBean.getDteBookingDate()));
		objHDModel.setDteFromDate(objGlobalFunctions.funGetDateAndTime("yyyy-MM-dd", objBean.getDteFromDate()));
		objHDModel.setDteToDate(objGlobalFunctions.funGetDateAndTime("yyyy-MM-dd", objBean.getDteToDate()));
		objHDModel.setIntMaxPaxNo(objBean.getIntMaxPaxNo());
		objHDModel.setIntMinPaxNo(objBean.getIntMinPaxNo());
		objHDModel.setStrAreaCode(objBean.getStrAreaCode());
		objHDModel.setStrBillingInstructionCode(objBean.getStrBillingInstructionCode());
		objHDModel.setStrBookingStatus(objBean.getStrBookingStatus());
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
	
	@RequestMapping(value = "/loadBookingFunServiceData", method = RequestMethod.GET)
	public @ResponseBody List funLoadBookingFunServiceData(@RequestParam("functionCode") String funCode,@RequestParam("bookingCode") String  bookingCode, HttpServletRequest req)
	{
		List list =null;
		try{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String 	 sql="select  a.strServiceCode,a.strServiceName ,s.dblRate, if(ifnull(b.strDocNo,'')='','N','Y')"
					+ " from tblservicemaster s , tblfunctionservice a left outer join tblbqbookingdtl b on a.strServiceCode=b.strDocNo and b.strBookingNo='"+bookingCode+"'"
					+ "where  a.strFunctionCode='"+funCode+"' and s.strServiceCode=a.strServiceCode  and s.strClientCode='"+clientCode+"' and s.strServiceType='Internal' group by  a.strServiceCode;";
			list= objGlobalFunctionsService.funGetDataList(sql, "sql");		
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return list;
	}
	
	@RequestMapping(value = "/loadBookingExternalServiceData", method = RequestMethod.GET)
	public @ResponseBody List funLoadBookingExternalServiceData(@RequestParam("functionCode") String funCode,@RequestParam("bookingCode") String  bookingCode, HttpServletRequest req)
	{
		List list =null;
		try{
			String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String 	 sql="select a.strDocNo,a.strDocName,a.dblDocRate,a.strVendorCode, b.strPName "
					+ " from tblbqbookingdtl a ,`"+webStockDB+"`.tblpartymaster b "
					+ "where a.strVendorCode=b.strPCode and  a.strBookingNo='"+bookingCode+"' and a.strType='Ex Service';";
			
			list= objGlobalFunctionsService.funGetDataList(sql, "sql");		
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return list;
	}

	
	@RequestMapping(value = "/loadPropertyCode", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/loadBookingServiceData", method = RequestMethod.GET)
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
	
	@RequestMapping(value = "/loadBanquetRate", method = RequestMethod.GET)
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

	
	@RequestMapping(value = "/checkStaffCnt", method = RequestMethod.GET)
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
	
	
	
	
	@RequestMapping(value = "/checkBooking", method = RequestMethod.GET)
	public @ResponseBody boolean funCheckBooking(@RequestParam("fromTime") String fromTime,@RequestParam("fromDate") String fromDate,@RequestParam("locName") String locName,HttpServletRequest req)
	{
		String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
		List list =null;		
		boolean condition = false;		
		try{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String sqlCheck = "select a.strBookingNo,b.strLocName from tblbqbookinghd a ,"+webStockDB+".tbllocationmaster b "
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
	
	
	
}
