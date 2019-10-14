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
import com.sanguine.webbanquets.bean.clsBanquetStaffMasterBean;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel;
import com.sanguine.webbanquets.model.clsBanquetStaffMasterModel_ID;
import com.sanguine.webbanquets.service.clsBanquetStaffMasterService;

@Controller
public class clsBanquetStaffMasterController{

	@Autowired
	private clsBanquetStaffMasterService objBanquetStaffMasterService;
			
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;

//Open BanquetStaffMaster
	@RequestMapping(value = "/frmBanquetStaffMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(){
		return new ModelAndView("frmBanquetStaffMaster","command", new clsBanquetStaffMasterBean());
	}
//Load Master Data On Form
	@RequestMapping(value = "/frmBanquetStaffMaster1", method = RequestMethod.POST)
	public @ResponseBody clsBanquetStaffMasterModel funLoadMasterData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql="";
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String userCode=request.getSession().getAttribute("userCode").toString();
		clsBanquetStaffMasterBean objBean=new clsBanquetStaffMasterBean();
		String docCode=request.getParameter("docCode").toString();
		List listModel=objGlobalFunctionsService.funGetList(sql);
		clsBanquetStaffMasterModel objBanquetStaffMaster = new clsBanquetStaffMasterModel();
		return objBanquetStaffMaster;
	}

//Save or Update BanquetStaffMaster
	@RequestMapping(value = "/saveBanquetStaffMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsBanquetStaffMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsBanquetStaffMasterModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objBanquetStaffMasterService.funAddUpdateBanquetStaffMaster(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", objModel.getStrStaffCode());
			return new ModelAndView("redirect:/frmBanquetStaffMaster.html");
		}
		else{
			return new ModelAndView("frmBanquetStaffMaster");
		}
	}
	
	@RequestMapping(value = "/loadStaffMasterData", method = RequestMethod.GET)
	public @ResponseBody clsBanquetStaffMasterModel funAssignFields(@RequestParam("staffCode") String staffCode, HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		clsBanquetStaffMasterModel objLocCode = objBanquetStaffMasterService.funGetObject(staffCode, clientCode);
		if (null == objLocCode) {
			objLocCode = new clsBanquetStaffMasterModel();
			//objLocCode.setStrLocCode("Invalid Code");
		}

		return objLocCode;
	}
	

//Convert bean to model function
	private clsBanquetStaffMasterModel funPrepareModel(clsBanquetStaffMasterBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsBanquetStaffMasterModel objModel = null;		
		clsBanquetStaffMasterModel mpModel;
			if (objBean.getStrStaffCode().trim().length() == 0) {
				lastNo = objGlobalFunctionsService.funGetLastNo("tblstaffmaster", "MemberProfile", "intSTId", clientCode);
				String customerCode = "ST" + String.format("%06d", lastNo);
				mpModel = new clsBanquetStaffMasterModel(new clsBanquetStaffMasterModel_ID(customerCode, clientCode));
				mpModel.setIntSTId(lastNo);				
				mpModel.setStrStaffCode(customerCode);
				mpModel.setStrStaffName(objBean.getStrStaffName());
				mpModel.setStrStaffCatCode(objBean.getStrStaffCatCode());
				mpModel.setStrOperationalYN(objBean.getStrOperationalYN());
				mpModel.setDtCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));		
				mpModel.setDtEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));	
				mpModel.setStrClientCode(clientCode);
				mpModel.setStrUserCreated(userCode);			
				mpModel.setStrUserEdited(userCode);	
				mpModel.setStrMobile(objBean.getStrMobile());
				mpModel.setStrEmail(objBean.getStrEmail());
			} else {
				
				clsBanquetStaffMasterModel objMemberProfile = objBanquetStaffMasterService.funGetBanquetStaffMaster(objBean.getStrStaffCode(), clientCode);
				if (null == objMemberProfile) {
					lastNo = objGlobalFunctionsService.funGetLastNo("tblstaffmaster", "MemberProfile", "intSTId", clientCode);
					String customerCode = "ST" + String.format("%06d", lastNo);
					mpModel = new clsBanquetStaffMasterModel(new clsBanquetStaffMasterModel_ID(customerCode, clientCode));
					mpModel.setIntSTId(lastNo);				
					mpModel.setStrStaffCode(customerCode);
					mpModel.setStrStaffName(objBean.getStrStaffName());
					mpModel.setStrStaffCatCode(objBean.getStrStaffCatCode());
					mpModel.setStrOperationalYN(objBean.getStrOperationalYN());
					mpModel.setDtCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));		
					mpModel.setDtEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));	
					mpModel.setStrClientCode(clientCode);
					mpModel.setStrUserCreated(userCode);			
					mpModel.setStrUserEdited(userCode);	
					mpModel.setStrMobile(objBean.getStrMobile());
					mpModel.setStrEmail(objBean.getStrEmail());
					
				} else {
					mpModel = new clsBanquetStaffMasterModel(new clsBanquetStaffMasterModel_ID(objBean.getStrStaffCode(), clientCode));	
					mpModel.setIntSTId(objMemberProfile.getIntSTId());				
					mpModel.setStrStaffCode(objBean.getStrStaffCode());
					mpModel.setStrStaffName(objBean.getStrStaffName());
					mpModel.setStrStaffCatCode(objBean.getStrStaffCatCode());
					mpModel.setStrOperationalYN(objBean.getStrOperationalYN());
					mpModel.setDtCreated(objMemberProfile.getDtCreated());						
					mpModel.setStrClientCode(objMemberProfile.getStrClientCode());
					mpModel.setStrUserCreated(objMemberProfile.getStrUserCreated());	
					mpModel.setStrMobile(objBean.getStrMobile());
					mpModel.setStrEmail(objBean.getStrEmail());
					}
			}			
			mpModel.setStrUserEdited(userCode);			
			mpModel.setDtEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		
			return mpModel;		

	}

}
