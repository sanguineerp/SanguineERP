package com.sanguine.webbanquets.controller;

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
import com.sanguine.webbanquets.bean.clsCostCenterMasterBean;
import com.sanguine.webbanquets.bean.clsMenuHeadMasterBean;
import com.sanguine.webbanquets.model.clsCostCenterMasterModel;
import com.sanguine.webbanquets.model.clsMenuHeadMasterModel;
import com.sanguine.webbanquets.service.clsMenuHeadMasterService;

@Controller
public class clsMenuHeadMasterController{

	@Autowired
	private clsMenuHeadMasterService objfrmMenuHeadMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;

	@RequestMapping(value = "/frmMenuHeadMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmMenuHeadMaster_1", "command", new clsMenuHeadMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmMenuHeadMaster", "command", new clsMenuHeadMasterBean());
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/loadMenuHeadCode", method = RequestMethod.GET)
	public @ResponseBody clsMenuHeadMasterModel funLoadMasterData(@RequestParam("docCode") String docCode,HttpServletRequest request){
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		clsMenuHeadMasterModel objfrmMenuHeadMaster = objfrmMenuHeadMasterService.funGetfrmMenuHeadMaster(docCode, clientCode);
		
		if (null == objfrmMenuHeadMaster) {
			objfrmMenuHeadMaster = new clsMenuHeadMasterModel(); 
			objfrmMenuHeadMaster.setStrMenuHeadCode("Invalid Code");
		}
		return objfrmMenuHeadMaster;
	}

	@RequestMapping(value = "/saveMenuHeadMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsMenuHeadMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsMenuHeadMasterModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objfrmMenuHeadMasterService.funAddUpdatefrmMenuHeadMaster(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Menu Head Code : ".concat(objModel.getStrMenuHeadCode()));
			return new ModelAndView("redirect:/frmMenuHeadMaster.html");
		}
		else
		{
			return new ModelAndView("frmMenuHeadMaster");
		}
	}

	private clsMenuHeadMasterModel funPrepareModel(clsMenuHeadMasterBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsMenuHeadMasterModel objModel=new clsMenuHeadMasterModel();
		if (objBean.getStrMenuHeadCode().trim().length() == 0){
			lastNo = objGlobalFunctionsService.funGetPMSMasterLastNo("tblbqmenuhead", "Menu Head Master","strMenuHeadCode",clientCode);
			String strMenuHeadCode = "BM" + String.format("%06d", lastNo);
			objModel.setStrMenuHeadCode(strMenuHeadCode);
			objModel.setStrMenuHeadName(objBean.getStrMenuHeadName());
			objModel.setStrOperational(objGlobal.funIfNull(objBean.getStrOperational(), "No", "Yes"));
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrClientCode(clientCode);
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
		}
		else
		{
			objModel.setStrMenuHeadCode(objBean.getStrMenuHeadCode());
			objModel.setStrMenuHeadName(objBean.getStrMenuHeadName());
			objModel.setStrOperational(objGlobal.funIfNull(objBean.getStrOperational(), "No", "Yes"));
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
			objModel.setStrClientCode(clientCode);
		}
		return objModel;
	}

}
