package com.sanguine.webclub.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webclub.bean.clsWebClubBusinessSourceMasterBean;
import com.sanguine.webclub.model.clsWebClubBusinessSourceMasterModel;
import com.sanguine.webclub.service.clsWebClubBusinessSourceMasterService;

@Controller
public class clsWebClubBusinessSourceMasterController{

	@Autowired
	private clsWebClubBusinessSourceMasterService objWebClubBusinessSourceMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;

//Open WebClubBusinessSourceMaster
	@RequestMapping(value = "/frmWebClubBusinessSourceMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {

		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmWebClubBusinessSourceMaster_1", "command", new clsWebClubBusinessSourceMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmWebClubBusinessSourceMaster", "command", new clsWebClubBusinessSourceMasterBean());
		} else {
			return null;
		}

	}

//Load Master Data On Form
	@RequestMapping(value = "/loadWebClubBusinessSourceData", method = RequestMethod.GET)
	public @ResponseBody List funFetchWalkinInformation(@RequestParam("docCode") String businessSourceCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();

		String sql = "select a.strBusinessSrcCode,a.strBusinessSrcName,a.dblPercent from tblbusinesssource a where a.strBusinessSrcCode='"+businessSourceCode+"' and a.strClientCode='"+clientCode+"'";

		List listWalkinData = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");

		return listWalkinData;
	}

//Save or Update WebClubBusinessSourceMaster
	@RequestMapping(value = "/saveWebClubBusinessSourceMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsWebClubBusinessSourceMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsWebClubBusinessSourceMasterModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objWebClubBusinessSourceMasterService.funAddUpdateWebClubBusinessSourceMaster(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Business Source Code : ".concat(objBean.getStrBusinessSrcCode()));
			return new ModelAndView("redirect:/frmWebClubBusinessSourceMaster.html");
		}
		else{
			return new ModelAndView("frmWebClubBusinessSourceMaster");
		}
	}

//Convert bean to model function
	private clsWebClubBusinessSourceMasterModel funPrepareModel(clsWebClubBusinessSourceMasterBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsWebClubBusinessSourceMasterModel objModel = new clsWebClubBusinessSourceMasterModel();
		objGlobal = new clsGlobalFunctions();
		

		if (objBean.getStrBusinessSrcCode().trim().length() == 0){
			lastNo = objGlobalFunctionsService.funGetLastNo("tblbusinesssource", "BusinessSource Master", "intId", clientCode);
			// lastNo=1;
			String businessSourceCode = "BS" + String.format("%06d", lastNo);
			objModel.setStrBusinessSrcCode(businessSourceCode);
			objModel.setStrBusinessSrcName(objBean.getStrBusinessSrcName());
			objModel.setDblPercent(objBean.getDblPercent());
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrClientCode(clientCode);
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
			objModel.setIntId(lastNo);
		}
		else
		{
		objModel.setStrBusinessSrcName(objBean.getStrBusinessSrcName());
		objModel.setStrBusinessSrcCode(objBean.getStrBusinessSrcCode());
		objModel.setDblPercent(objBean.getDblPercent());
		objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrClientCode(clientCode);
		objModel.setStrUserCreated(userCode);
		objModel.setStrUserEdited(userCode);
		
		}
		return objModel;

	}

}
