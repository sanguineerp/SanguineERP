package com.sanguine.webpms.controller;

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

import java.util.List;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsHouseKeepingMasterBean;
import com.sanguine.webpms.dao.clsHouseKeepingMasterDao;
import com.sanguine.webpms.model.clsHouseKeepingMasterModel;
import com.sanguine.webpms.model.clsPMSReasonMasterModel;
import com.sanguine.webpms.service.clsHouseKeepingMasterService;

@Controller
public class clsHouseKeepingMasterController{

	@Autowired
	private clsHouseKeepingMasterService objHouseKeepingMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	@Autowired
	private clsHouseKeepingMasterDao objHouseKeepDao;
	
	private clsGlobalFunctions objGlobal=null;

//Open HouseKeepingMaster
	@RequestMapping(value = "/frmHouseKeepingMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(){
		return new ModelAndView("frmHouseKeepingMaster","command", new clsHouseKeepingMasterModel());
	}
//Load Master Data On Form
	@RequestMapping(value = "/frmHouseKeepingMaster1", method = RequestMethod.POST)
	public @ResponseBody clsHouseKeepingMasterModel funLoadMasterData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql="";
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String userCode=request.getSession().getAttribute("userCode").toString();
		clsHouseKeepingMasterBean objBean=new clsHouseKeepingMasterBean();
		String docCode=request.getParameter("docCode").toString();
		List listModel=objGlobalFunctionsService.funGetList(sql);
		clsHouseKeepingMasterModel objHouseKeepingMaster = new clsHouseKeepingMasterModel();
		return objHouseKeepingMaster;
	}

//Save or Update HouseKeepingMaster
	@RequestMapping(value = "/saveHouseKeepingMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsHouseKeepingMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsHouseKeepingMasterModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objHouseKeepingMasterService.funAddUpdateHouseKeepingMaster(objModel);
			
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Business Code : ".concat(objModel.getStrHouseKeepCode()));

			return new ModelAndView("redirect:/frmHouseKeepingMaster.html");
		}
		else{
			return new ModelAndView("frmHouseKeepingMaster");
		}
	}

//Convert bean to model function
	private clsHouseKeepingMasterModel funPrepareModel(clsHouseKeepingMasterBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsHouseKeepingMasterModel objModel = new clsHouseKeepingMasterModel();
		
		if (objBean.getStrHouseKeepCode().trim().length() == 0) {
			lastNo = objGlobalFunctionsService.funGetPMSMasterLastNo("tblhousekeepmaster", "HouseKeepingMaster", "strHouseKeepCode", clientCode);
			String houseKeepCode = "HK" + String.format("%06d", lastNo);
			objModel.setStrHouseKeepCode(houseKeepCode);
			objModel.setStrUserCreated(userCode);
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		} else {
			objModel.setStrHouseKeepCode(objBean.getStrHouseKeepCode());
			objModel.setStrUserCreated(userCode);
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));

		}
		
		objModel.setStrClientCode(clientCode);
		objModel.setStrHouseKeepName(objBean.getStrHouseKeepName());
		objModel.setStrRemarks(objBean.getStrRemarks());
		objModel.setStrUserEdited(userCode);
		objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		
		
		
		return objModel;

	}
	
	@RequestMapping(value = "/loadHouseKeepData", method = RequestMethod.GET)
	public @ResponseBody clsHouseKeepingMasterModel funFetchReasonMasterData(@RequestParam("houseKeepCode") String strHouseKKeepCode, HttpServletRequest req) {

		String clientCode = req.getSession().getAttribute("clientCode").toString();
		clsHouseKeepingMasterModel objHouseKepModel = objHouseKeepDao.funGetHouseKeepingMaster(strHouseKKeepCode, clientCode);
		return objHouseKepModel;
	}


}
