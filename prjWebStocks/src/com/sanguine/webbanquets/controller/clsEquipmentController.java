package com.sanguine.webbanquets.controller;

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
import com.sanguine.webbanquets.bean.clsEquipmentBean;
import com.sanguine.webbanquets.bean.clsSampleBean;
import com.sanguine.webbanquets.model.clsEquipmentModel;
import com.sanguine.webbanquets.service.clsEquipmentService;
import com.sanguine.webbanquets.service.clsSampleService;
import com.sanguine.webclub.model.clsWebClubMemberProfileModel;
import com.sanguine.webclub.model.clsWebClubRegionMasterModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class clsEquipmentController{

	@Autowired
	private clsEquipmentService objEquipmentService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;


	
	@Autowired
	clsSampleService objSampleService;
	
	@RequestMapping(value = "/frmEquipment", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {

		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		
		//objSampleService.funSaveData("WebBAnquet");
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmEquipment_1", "command", new clsEquipmentBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmEquipment", "command", new clsEquipmentBean());
		} else {
			return null;
		}
	}

	//Load data
	
	@RequestMapping(value = "/loadEquipmentName", method = RequestMethod.GET)
	public @ResponseBody clsEquipmentModel funAssignFields(@RequestParam("docCode") String docCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		clsEquipmentModel objModel = objEquipmentService.funGetEquipment(docCode, clientCode);

		if (null == objModel) {
			objModel = new clsEquipmentModel();
			objModel.setStrEquipmentCode("Invalid Code");
		}
		return objModel;
	}

	
	
//Save or Update Equipment
	@RequestMapping(value = "/saveEquipment", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsEquipmentBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsEquipmentModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objEquipmentService.funAddUpdateEquipment(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Equipment Saved Successfully");
			
			return new ModelAndView("redirect:/frmEquipment.html");
		}
		else{
			return new ModelAndView("frmEquipment");
		}
	}

//Convert bean to model function
	private clsEquipmentModel funPrepareModel(clsEquipmentBean objBean,String userCode,String clientCode){

		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsEquipmentModel objModel = new clsEquipmentModel();;
		if (objBean.getStrEquipmentCode().trim().length() == 0){
		/*lastNo = objGlobalFunctionsService.funGetLastNo("tblequipment", "Equipment Master", "intId", clientCode);*/
		lastNo = objGlobalFunctionsService.funGetLastNoModuleWise("tblequipment", "Equipment Master","intGId",clientCode,"1-WebStocks");
		String strEquipmentCode = "EC" + String.format("%06d", lastNo);
		objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrClientCode(clientCode);
		objModel.setStrEquipmentCode(objBean.getStrEquipmentCode());
		objModel.setStrEquipmentName(objBean.getStrEquipmentName());
		objModel.setStrUserCreated(userCode);
		objModel.setStrUserEdited(userCode);
		objModel.setIntId(lastNo);
		objModel.setStrEquipmentCode(strEquipmentCode);
		objModel.setStrOperational(objBean.getStrOperational());
		}
		
		else
		{
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrClientCode(clientCode);
			objModel.setStrEquipmentCode(objBean.getStrEquipmentCode());
			objModel.setStrEquipmentName(objBean.getStrEquipmentName());
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
			objModel.setStrOperational(objBean.getStrOperational());
			
		}
		return objModel;

	}

}
