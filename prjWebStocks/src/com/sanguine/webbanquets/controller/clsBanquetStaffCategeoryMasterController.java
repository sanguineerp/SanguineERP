package com.sanguine.webbanquets.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webbanquets.bean.clsBanquetStaffCategeoryMasterBean;
import com.sanguine.webbanquets.model.clsBanquetStaffCategeoryMasterModel;
import com.sanguine.webbanquets.model.clsBanquetStaffCategeoryMasterModel_ID;
import com.sanguine.webbanquets.service.clsBanquetStaffCategeoryMasterService;

@Controller
public class clsBanquetStaffCategeoryMasterController{

	@Autowired
	private clsBanquetStaffCategeoryMasterService objBanquetStaffCategeoryMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;

//Open BanquetStaffCategeoryMaster
	@RequestMapping(value = "/frmBanquetStaffCategeoryMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(){
		return new ModelAndView("frmBanquetStaffCategeoryMaster","command", new clsBanquetStaffCategeoryMasterModel());
	}
//Load Master Data On Form
	@RequestMapping(value = "/frmBanquetStaffCategeoryMaster1", method = RequestMethod.POST)
	public @ResponseBody clsBanquetStaffCategeoryMasterModel funLoadMasterData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql="";
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String userCode=request.getSession().getAttribute("usercode").toString();
		clsBanquetStaffCategeoryMasterBean objBean=new clsBanquetStaffCategeoryMasterBean();
		String docCode=request.getParameter("docCode").toString();
		List listModel=objGlobalFunctionsService.funGetList(sql);
		clsBanquetStaffCategeoryMasterModel objBanquetStaffCategeoryMaster = new clsBanquetStaffCategeoryMasterModel();
		return objBanquetStaffCategeoryMaster;
	}

//Save or Update BanquetStaffCategeoryMaster
	@RequestMapping(value = "/saveBanquetStaffCategeoryMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsBanquetStaffCategeoryMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsBanquetStaffCategeoryMasterModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objBanquetStaffCategeoryMasterService.funAddUpdateBanquetStaffCategeoryMaster(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", objModel.getStrStaffCategeoryCode());
			return new ModelAndView("redirect:/frmBanquetStaffCategeoryMaster.html");
		}
		else{
			return new ModelAndView("frmBanquetStaffCategeoryMaster");
		}
	}
	
	@RequestMapping(value = "/loadStaffCategeoryMasterData", method = RequestMethod.GET)
	public @ResponseBody clsBanquetStaffCategeoryMasterModel funAssignFields(@RequestParam("staffCatCode") String staffCatCode, HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode=request.getSession().getAttribute("usercode").toString();
		clsBanquetStaffCategeoryMasterModel objModel = objBanquetStaffCategeoryMasterService.funGetBanquetStaffCategeoryMaster(staffCatCode, clientCode);
		if (null == objModel) {
			objModel = new clsBanquetStaffCategeoryMasterModel();
			//objLocCode.setStrLocCode("Invalid Code");
		}

		return objModel;
	}
	
	
	
	private clsBanquetStaffCategeoryMasterModel funPrepareModel(clsBanquetStaffCategeoryMasterBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsBanquetStaffCategeoryMasterModel objModel = null;		
		clsBanquetStaffCategeoryMasterModel mpModel;
			if (objBean.getStrStaffCategeoryCode().trim().length() == 0) {
				lastNo = objGlobalFunctionsService.funGetLastNo("tblstaffcategeorymaster", "MemberProfile", "intSCId", clientCode);
				String customerCode = "SC" + String.format("%06d", lastNo);
				mpModel = new clsBanquetStaffCategeoryMasterModel(new clsBanquetStaffCategeoryMasterModel_ID(customerCode, clientCode));
				mpModel.setIntSCId(lastNo);				
				mpModel.setStrStaffCategeoryCode(customerCode);
				mpModel.setStrStaffCategeoryName(objBean.getStrStaffCategeoryName());
				mpModel.setStrStaffCount(objBean.getStrStaffCount());
				mpModel.setStrOperationalYN(objBean.getStrOperationalYN());
				mpModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));		
				mpModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));	
				mpModel.setStrClientCode(clientCode);
				mpModel.setStrDeptCode(objBean.getStrDeptCode());
				mpModel.setStrUserCreated(userCode);			
				mpModel.setStrUserEdited(userCode);								
			} else {				
				clsBanquetStaffCategeoryMasterModel objMemberProfile = objBanquetStaffCategeoryMasterService.funGetBanquetStaffCategeoryMaster(objBean.getStrStaffCategeoryCode(), clientCode);
				if (null == objMemberProfile) {
					lastNo = objGlobalFunctionsService.funGetLastNo("tblstaffcategeorymaster", "MemberProfile", "intSCId", clientCode);
					String customerCode = "SC" + String.format("%06d", lastNo);
					mpModel = new clsBanquetStaffCategeoryMasterModel(new clsBanquetStaffCategeoryMasterModel_ID(customerCode, clientCode));
					mpModel.setIntSCId(lastNo);				
					mpModel.setStrStaffCategeoryCode(customerCode);
					mpModel.setStrStaffCategeoryName(objBean.getStrStaffCategeoryName());
					mpModel.setStrStaffCount(objBean.getStrStaffCount());					
					mpModel.setStrOperationalYN(objBean.getStrOperationalYN());
					mpModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));		
					mpModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));	
					mpModel.setStrClientCode(clientCode);
					mpModel.setStrDeptCode(objBean.getStrDeptCode());
					mpModel.setStrUserCreated(userCode);			
					mpModel.setStrUserEdited(userCode);		
				} else {
					mpModel = new clsBanquetStaffCategeoryMasterModel(new clsBanquetStaffCategeoryMasterModel_ID(objBean.getStrStaffCategeoryCode(), clientCode));	
					mpModel.setIntSCId(objMemberProfile.getIntSCId());				
					mpModel.setStrStaffCategeoryCode(objBean.getStrStaffCategeoryCode());
					mpModel.setStrStaffCategeoryName(objBean.getStrStaffCategeoryName());
					mpModel.setStrStaffCount(objBean.getStrStaffCount());	
					mpModel.setStrOperationalYN(objBean.getStrOperationalYN());
					mpModel.setDteDateCreated(objMemberProfile.getDteDateCreated());	
					mpModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));					
					mpModel.setStrClientCode(objMemberProfile.getStrClientCode());
					mpModel.setStrDeptCode(objBean.getStrDeptCode());
					mpModel.setStrUserCreated(objMemberProfile.getStrUserCreated());	
					mpModel.setStrUserEdited(userCode);							
					}
			}			
			mpModel.setStrUserEdited(userCode);			
			mpModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		
			return mpModel;		

	}

}

	
	
	
	
	
	
	
	
	

