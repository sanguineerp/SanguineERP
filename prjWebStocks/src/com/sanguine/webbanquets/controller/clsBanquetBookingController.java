package com.sanguine.webbanquets.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsCompanyMasterModel;
import com.sanguine.model.clsSettlementMasterModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webbanquets.bean.clsBanquetBookingBean;
import com.sanguine.webbanquets.model.clsBanquetBookingModelDtl;
import com.sanguine.webbanquets.model.clsBanquetBookingModelHd;
import com.sanguine.webbanquets.service.clsBanquetBookingService;
import com.sanguine.webpms.bean.clsReservationBean;
import com.sanguine.webpms.model.clsPropertySetupHdModel;

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


//Open BanquetBooking
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
	

//		clsPropertySetupHdModel objPropertySetupModel = objPropertySetupService.funGetPropertySetup(propCode, clientCode);
//		String noOfRoom = objPropertySetupModel.getStrRoomLimit();
//		
//		model.put("noOfRoom", noOfRoom);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBanquetBooking_1", "command", new clsBanquetBookingBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBanquetBooking", "command", new clsBanquetBookingBean());
		} else {
			return null;
		
		}
		
	}
//Load Header Table Data On Form
	@RequestMapping(value = "/loadBanquetBookingHd", method = RequestMethod.POST)
	public @ResponseBody clsBanquetBookingModelHd funLoadHdData(HttpServletRequest request){
		
		String sql="";
			String clientCode=request.getSession().getAttribute("clientCode").toString();
			String userCode=request.getSession().getAttribute("userCode").toString();
			clsBanquetBookingBean objBean=new clsBanquetBookingBean();
			String docCode=request.getParameter("docCode").toString();
			List listHdModel=objGlobalFunctionsService.funGetList(sql);
			clsBanquetBookingModelHd objBanquetBooking = new clsBanquetBookingModelHd();
			return objBanquetBooking;
	}

//Load Dtl Table Data On Form
	/*@RequestMapping(value = "/loadBanquetBookingDtl", method = RequestMethod.POST)
	public @ResponseBody clsBanquetBookingDtlModelHd funLoadDtlData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql=""
			String clientCode=request.getSession().getAttribute("clientCode").toString();
			String userCode=request.getSession().getAttribute("userCode").toString();
		clsBanquetBookingBean objBean=new clsBanquetBookingBean();
		String docCode=req.getParameter("docCode").toString();
		List listDtlModel=objGlobalFunctionsService.funGetList(sql);
	clsBanquetBookingDtlModel objBanquetBookingDtl = new clsBanquetBookingDtlModel();
	return objBanquetBookingDtl;
	}*/

//Save or Update BanquetBooking
	/**
	 * @param objBean
	 * @param result
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/saveBanquetBooking", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsBanquetBookingBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			String propCode=req.getSession().getAttribute("propertyCode").toString();
			String startDate=req.getSession().getAttribute("startDate").toString();
			clsBanquetBookingModelHd objHdModel = funPrepareHdModel(objBean,userCode,clientCode,req,propCode);
			List<clsBanquetBookingModelDtl> listBookingDtl=new ArrayList<clsBanquetBookingModelDtl>();
			
			objBanquetBookingService.funAddUpdateBanquetBookingHd(objHdModel);
			
			if(objBean.getListSeriveDtl()!=null && !objBean.getListSeriveDtl().isEmpty())
			{
				List<clsBanquetBookingModelDtl>list=objBean.getListSeriveDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetBookingModelDtl objDtl=list.get(i);
					objDtl.setStrType("Service");
					objDtl.setDblDocQty(1);
					objDtl.setStrBookingDate(objHdModel.getDteBookingDate());
					objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
					listBookingDtl.add(objDtl);
					
				}
			
				
				
			}
			if(objBean.getListEquipDtl()!=null && !objBean.getListEquipDtl().isEmpty())
			{
				List<clsBanquetBookingModelDtl>list=objBean.getListEquipDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetBookingModelDtl objDtl=list.get(i);
				
					objDtl.setStrType("Equipment");
					objDtl.setStrBookingDate(objHdModel.getDteBookingDate());
					objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
					listBookingDtl.add(objDtl);
					
				}
				
			}
			if(objBean.getListStaffCatDtl()!=null && !objBean.getListStaffCatDtl().isEmpty())
			{
				List<clsBanquetBookingModelDtl>list=objBean.getListStaffCatDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetBookingModelDtl objDtl=list.get(i);
					
					objDtl.setStrType("Staff");
					objDtl.setStrBookingDate(objHdModel.getDteBookingDate());
				//	objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
					listBookingDtl.add(objDtl);
					
				}
				
			}
			if(objBean.getListMenuItemDtl()!=null && !objBean.getListMenuItemDtl().isEmpty())
			{
				List<clsBanquetBookingModelDtl>list=objBean.getListMenuItemDtl();
				for(int i=0;i<list.size();i++)
				{
					clsBanquetBookingModelDtl objDtl=list.get(i);
					
					objDtl.setStrType("Menu");
					objDtl.setStrBookingDate(objHdModel.getDteBookingDate());
					objDtl.setDblDocTotalAmt(objDtl.getDblDocQty() * objDtl.getDblDocRate());
					listBookingDtl.add(objDtl);
					
				}
				
			}
			objHdModel.setListBanquetBookingDtlModels(listBookingDtl);
			objBanquetBookingService.funAddUpdateBanquetBookingHd(objHdModel);
			
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
				/*String sql = "select ifnull(max(MID(a.strInvCode,8,5)),'' ) " + " from tblinvoicehd a where MID(a.strInvCode,5,1) = '" + transYear + "' " + " and MID(a.strInvCode,1,2) = '" + propCode + "' and strClientCode='" + clientCode + "' ";	//and MID(a.strInvCode,6,1) = '" + transMonth + "' " + " 
				String sqlAudit = " select ifnull(max(MID(a.strTransCode,8,5)),'' ) " + " from tblaudithd a where MID(a.strTransCode,5,1) = '" + transYear + "' and MID(a.strTransCode,1,2) = '" + propCode + "' and strClientCode='" + clientCode + "' " + "and a.strTransType='Invoice' ;  ";  		//" + " and MID(a.strTransCode,6,1) = '" + transMonth + "' " + "
				*/
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

				
				/*clsSettlementMasterModel objModel = objSttlementMasterService.funGetObject(objBean.getStrSettlementCode(), clientCode);*/
			
				
				if (lastnoLive > lastnoAudit) {
					bookCode = propCode + "BK" + transYear + transMonth  + String.format("%05d", lastnoLive + 1);
				} else {
					bookCode = propCode + "BK" + transYear + transMonth  +String.format("%05d", lastnoAudit + 1);

			   }
			objHDModel.setStrBookingNo(bookCode);
		
		}
		else // Update
		{
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
		return objHDModel;

	}

}
