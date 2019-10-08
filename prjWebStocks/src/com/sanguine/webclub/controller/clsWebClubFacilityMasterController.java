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
import com.sanguine.webclub.bean.clsWebClubFacilityMasterBean;
import com.sanguine.webclub.model.clsWebClubCompanyMasterModel;
import com.sanguine.webclub.model.clsWebClubCompanyMasterModel_ID;
import com.sanguine.webclub.model.clsWebClubFacilityMasterModel;
import com.sanguine.webclub.model.clsWebClubFacilityMasterModel_ID;
import com.sanguine.webclub.service.clsWebClubFacilityMasterService;

@Controller
public class clsWebClubFacilityMasterController {

	@Autowired
	private clsWebClubFacilityMasterService objWebClubFacilityMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal = null;

	// Open WebClubFacilityMaster
	@RequestMapping(value = "/frmWebClubFacilityMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {

		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmWebClubFacilityMaster_1", "command", new clsWebClubFacilityMasterModel());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmWebClubFacilityMaster", "command", new clsWebClubFacilityMasterModel());
		} else {
			return null;
		}

	}

	// Load Master Data On Form
	@RequestMapping(value = "/frmWebClubFacilityMaster1", method = RequestMethod.POST)
	public @ResponseBody clsWebClubFacilityMasterModel funLoadMasterData(HttpServletRequest request) {
		objGlobal = new clsGlobalFunctions();
		String sql = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("userCode").toString();
		clsWebClubFacilityMasterBean objBean = new clsWebClubFacilityMasterBean();
		String docCode = request.getParameter("docCode").toString();
		List listModel = objGlobalFunctionsService.funGetList(sql);
		clsWebClubFacilityMasterModel objWebClubFacilityMaster = new clsWebClubFacilityMasterModel();
		return objWebClubFacilityMaster;
	}

	// Save or Update WebClubFacilityMaster
	@RequestMapping(value = "/saveWebClubFacilityMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsWebClubFacilityMasterBean objBean, BindingResult result, HttpServletRequest req) {

		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}

		if (!result.hasErrors()) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propCode = req.getSession().getAttribute("propertyCode").toString();
			clsWebClubFacilityMasterModel objModel = funPrepareModel(objBean, userCode, clientCode);
			objWebClubFacilityMasterService.funAddUpdateWebClubFacilityMaster(objModel);

			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Facility Code : ".concat(objModel.getStrFacilityCode()));
			return new ModelAndView("redirect:/frmWebClubFacilityMaster.html?saddr=" + urlHits);
		} else {
			return new ModelAndView("redirect:/frmWebClubFacilityMaster.html?saddr=" + urlHits);
		}

	}

	// Convert bean to model function
	private clsWebClubFacilityMasterModel funPrepareModel(clsWebClubFacilityMasterBean objBean, String userCode, String clientCode) {
		objGlobal = new clsGlobalFunctions();
		long lastNo = 0;
		String strOperationalNY="N"; 
		if(objBean.getStrOperationalNY()!=null&& objBean.getStrOperationalNY().equalsIgnoreCase("Y"))
		{
			strOperationalNY=objBean.getStrOperationalNY();
		}
		clsWebClubFacilityMasterModel objModel;
		if (objBean.getStrFacilityCode().trim().length() == 0) {
			lastNo = objGlobalFunctionsService.funGetLastNo("tblFacilitymaster", "FacilityMaster", "intGId", clientCode);
			String facilityCode = "FC" + String.format("%06d", lastNo);
			objModel = new clsWebClubFacilityMasterModel(new clsWebClubFacilityMasterModel_ID(facilityCode, clientCode));
			objModel.setIntGId(lastNo);
			//objModel.setStrPropertyCode(propCode);
			objModel.setStrFacilityCode(facilityCode);
			objModel.setStrFacilityName(objBean.getStrFacilityName());			
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));			
			objModel.setStrUserCreated(userCode);			
		} else {
			
		    clsWebClubFacilityMasterModel objFacilityModel = objWebClubFacilityMasterService.funGetWebClubFacilityMaster(objBean.getStrFacilityCode(), clientCode);
			objModel = new clsWebClubFacilityMasterModel(new clsWebClubFacilityMasterModel_ID(objBean.getStrFacilityCode(), clientCode));
			objModel.setStrFacilityCode(objFacilityModel.getStrFacilityCode());
			objModel.setStrFacilityName(objBean.getStrFacilityName());			
			objModel.setDteDateCreated(objFacilityModel.getDteDateCreated());	
			objModel.setStrUserCreated(objFacilityModel.getStrUserCreated());						
			
		}
		objModel.setStrUserEdited(userCode);
		objModel.setStrOperationalNY(objBean.getStrOperationalNY());
		objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));		
		objModel.setStrOperationalNY(strOperationalNY);
		return objModel;
	}

	// Assign filed function to set data onto form for edit transaction.
	@RequestMapping(value = "/loadWebClubFacilityMasterData", method = RequestMethod.GET)
	public @ResponseBody clsWebClubFacilityMasterModel funAssignFields(@RequestParam("docCode") String facilityCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		clsWebClubFacilityMasterModel objFacilityModel = objWebClubFacilityMasterService.funGetWebClubFacilityMaster(facilityCode, clientCode);
		if (null == objFacilityModel) {
			objFacilityModel = new clsWebClubFacilityMasterModel();
			objFacilityModel.setStrFacilityCode("Invalid Code");
		}

		return objFacilityModel;
	}

}
