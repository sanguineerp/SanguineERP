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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsUOMService;
import com.sanguine.webbanquets.bean.clsItemMasterBean;
import com.sanguine.webbanquets.bean.clsMenuHeadMasterBean;
import com.sanguine.webbanquets.model.clsItemMasterModel;
import com.sanguine.webbanquets.model.clsMenuHeadMasterModel;
import com.sanguine.webbanquets.service.clsItemMasterService;

@Controller
public class clsItemMasterController{

	@Autowired
	private clsItemMasterService objItemMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;
	@Autowired
	private clsUOMService objclsUOMService;

	@RequestMapping(value = "/frmItemMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request){
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		List<String> uomList = new ArrayList<String>();
		uomList = objclsUOMService.funGetUOMList(clientCode);
		model.put("uomList", uomList);
		
		objGlobal = new clsGlobalFunctions();
		List<String> listTaxIndicator = new ArrayList<>();
		listTaxIndicator.add(" ");
		String[] alphabetSet = objGlobal.funGetAlphabetSet();
		for (int i = 0; i < alphabetSet.length; i++) {
			listTaxIndicator.add(alphabetSet[i]);
		}
		model.put("taxIndicatorList", listTaxIndicator);
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmItemMaster_1", "command", new clsItemMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmItemMaster", "command", new clsItemMasterBean());
		} else {
			return null;
		}
	}
	
	@RequestMapping(value = "/loadItemCode", method = RequestMethod.GET)
	public @ResponseBody clsItemMasterModel funLoadMasterData(@RequestParam("docCode") String docCode,HttpServletRequest request){
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		clsItemMasterModel objItemMaster = objItemMasterService.funGetItemMaster(docCode, clientCode);
		if (null == objItemMaster) {
			objItemMaster = new clsItemMasterModel(); 
			objItemMaster.setStrItemCode("Invalid Code");
		}
		return objItemMaster;
	}

	@RequestMapping(value = "/saveItemMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsItemMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsItemMasterModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objItemMasterService.funAddUpdateItemMaster(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Item Code : ".concat(objModel.getStrItemCode()));
			return new ModelAndView("redirect:/frmItemMaster.html");
		}
		else
		{
			return new ModelAndView("frmItemMaster");
		}
	}

	private clsItemMasterModel funPrepareModel(clsItemMasterBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsItemMasterModel objModel=new clsItemMasterModel();
		if (objBean.getStrItemCode().trim().length() == 0){
			lastNo = objGlobalFunctionsService.funGetPMSMasterLastNo("tblbqitemmaster", "Item Master","strItemCode",clientCode);
			String strItemCode = "BI" + String.format("%06d", lastNo);
			objModel.setStrItemCode(strItemCode);
			objModel.setStrItemName(objBean.getStrItemName());
			objModel.setStrOperational(objGlobal.funIfNull(objBean.getStrOperational(), "No", "Yes"));
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrClientCode(clientCode);
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
			objModel.setStrMenuHeadCode(objBean.getStrMenuHeadCode());
			objModel.setStrSubGroupCode(objBean.getStrSubGroupCode());
			objModel.setStrDepartmentCode(objBean.getStrDepartmentCode());
			objModel.setStrUnit(objBean.getStrUnit());
			objModel.setDblAmount(objBean.getDblAmount());
			objModel.setDblPercent(objBean.getDblPercent());
			objModel.setStrTaxIndicator(objGlobal.funIfNull(objBean.getStrTaxIndicator(), "", objBean.getStrTaxIndicator()));
		}
		else
		{
			objModel.setStrItemCode(objBean.getStrItemCode());
			objModel.setStrItemName(objBean.getStrItemName());
			objModel.setStrOperational(objGlobal.funIfNull(objBean.getStrOperational(), "No", "Yes"));
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrClientCode(clientCode);
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
			objModel.setStrMenuHeadCode(objBean.getStrMenuHeadCode());
			objModel.setStrSubGroupCode(objBean.getStrSubGroupCode());
			objModel.setStrDepartmentCode(objBean.getStrDepartmentCode());
			objModel.setStrUnit(objBean.getStrUnit());
			objModel.setDblAmount(objBean.getDblAmount());
			objModel.setDblPercent(objBean.getDblPercent());
			objModel.setStrTaxIndicator(objGlobal.funIfNull(objBean.getStrTaxIndicator(), "", objBean.getStrTaxIndicator()));
		}
		return objModel;
	}

}
