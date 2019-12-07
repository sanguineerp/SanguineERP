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
import com.sanguine.webbanquets.bean.clsFunctionMasterBean;
import com.sanguine.webbanquets.bean.clsServiceMasterBean;
import com.sanguine.webbanquets.model.clsFunctionMasterModel;
import com.sanguine.webbanquets.model.clsFunctionMasterModel_ID;
import com.sanguine.webbanquets.model.clsServiceMasterModel;
import com.sanguine.webbanquets.model.clsServiceMasterModel_ID;
import com.sanguine.webbanquets.service.clsServiceMasterService;

@Controller
public class clsServiceMasterController{

	@Autowired
	private clsServiceMasterService objServiceMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal;

//Open ServiceMaster
	@RequestMapping(value = "/frmServiceMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request){
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		objGlobal = new clsGlobalFunctions();
		List<String> listTaxIndicator = new ArrayList<>();
		listTaxIndicator.add(" ");
		String[] alphabetSet = objGlobal.funGetAlphabetSet();
		for (int i = 0; i < alphabetSet.length; i++) {
			listTaxIndicator.add(alphabetSet[i]);
		}
		model.put("taxIndicatorList", listTaxIndicator);
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmServiceMaster_1", "command", new clsServiceMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmServiceMaster", "command", new clsServiceMasterBean());
		} else {
			return null;
		}
		
		
	}
//Load Master Data On Form
	@RequestMapping(value = "/loadServiceMasterData", method = RequestMethod.GET)
	public @ResponseBody clsServiceMasterModel funLoadMasterData(@RequestParam("serviceCode") String serviceCode,HttpServletRequest request){
		
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		
	    clsServiceMasterModel objServiceMaster = objServiceMasterService.funGetServiceMaster(serviceCode, clientCode);
	    if (null == objServiceMaster) {
	    	objServiceMaster = new clsServiceMasterModel();
	    	objServiceMaster.setStrServiceCode("Invalid Code");
		}
		
		return objServiceMaster;
	}

//Save or Update ServiceMaster
	@RequestMapping(value = "/saveServiceMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsServiceMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			String propertyCode=req.getSession().getAttribute("propertyCode").toString();
			clsServiceMasterModel objModel = funPrepareModel(objBean,userCode,clientCode,propertyCode);
			objServiceMasterService.funAddUpdateServiceMaster(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Function Code : ".concat(objModel.getStrServiceCode()));
			return new ModelAndView("redirect:/frmServiceMaster.html");
		}
		else{
			return new ModelAndView("frmServiceMaster");
		}
	}

//Convert bean to model function
	private clsServiceMasterModel funPrepareModel(clsServiceMasterBean objBean,String userCode,String clientCode,String propertyCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		
	    clsServiceMasterModel objModel;
		   
	    if(objBean.getStrServiceCode().trim().length() == 0)
		{
	    	lastNo=objGlobalFunctionsService.funGetLastNo("tblservicemaster", "ServiceMaster", "intSId", clientCode);
	    	String serviceCode = "S" + String.format("%06d", lastNo);
	    	objModel=new clsServiceMasterModel(new clsServiceMasterModel_ID(serviceCode, clientCode));
	    	objModel.setIntSId(lastNo);
	    	objModel.setStrUserCreated(userCode);
	    	objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			
		}
		else
		{
			clsServiceMasterModel objModel1=objServiceMasterService.funGetServiceMaster(objBean.getStrServiceCode(), clientCode);
			if (null == objModel1) {
				lastNo=objGlobalFunctionsService.funGetLastNo("tblservicemaster", "ServiceMaster", "intSId", clientCode);
		    	String serviceCode = "S" + String.format("%06d", lastNo);
		    	objModel=new clsServiceMasterModel(new clsServiceMasterModel_ID(serviceCode, clientCode));
		    	objModel.setIntSId(lastNo);
		    	objModel.setStrUserCreated(userCode);
		    	objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			} else {
				objModel =new clsServiceMasterModel(new clsServiceMasterModel_ID(objBean.getStrServiceCode(), clientCode));
			}
			
	     }
	    
	    objModel.setStrClientCode(clientCode);
	    objModel.setStrDeptCode(objBean.getStrDeptCode());
	    objModel.setStrServiceName(objBean.getStrServiceName());
	    objModel.setStrOperationalYN(objGlobal.funIfNull(objBean.getStrOperationalYN(),"N","Y"));
	    objModel.setStrUserEdited(userCode);
	    objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
	    objModel.setStrPropertyCode(propertyCode);
	    objModel.setDblRate(objBean.getDblRate());
	    objModel.setStrTaxIndicator(objGlobal.funIfNull(objBean.getStrTaxIndicator(), "", objBean.getStrTaxIndicator()));
	    objModel.setStrServiceType(objBean.getStrServiceType());  
		return objModel;

	}

}
