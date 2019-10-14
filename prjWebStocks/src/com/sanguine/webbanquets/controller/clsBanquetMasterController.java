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
import com.sanguine.webbanquets.bean.clsBanquetMasterBean;
import com.sanguine.webbanquets.bean.clsEquipmentBean;
import com.sanguine.webbanquets.model.clsBanquetMasterModel;
import com.sanguine.webbanquets.model.clsEquipmentModel;
import com.sanguine.webbanquets.service.clsBanquetMasterService;

import java.util.List;
import java.util.Map;

@Controller
public class clsBanquetMasterController{

	@Autowired
	private clsBanquetMasterService objBanquetMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;


//Load Master Data On Form
	@RequestMapping(value = "/frmBanquetMaster", method = RequestMethod.GET)
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
			return new ModelAndView("frmBanquetMaster_1", "command", new clsBanquetMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBanquetMaster", "command", new clsBanquetMasterBean());
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/loadBanquetName", method = RequestMethod.GET)
	public @ResponseBody clsBanquetMasterModel funAssignFields(@RequestParam("docCode") String docCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		clsBanquetMasterModel objModel = objBanquetMasterService.funGetBanquetMaster(docCode, clientCode);

		if (null == objModel) {
			objModel = new clsBanquetMasterModel();
			objModel.setStrBanquetCode("Invalid Code");
		}
		return objModel;
	}
	
//Save or Update BanquetMaster
	@RequestMapping(value = "/saveBanquetMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsBanquetMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsBanquetMasterModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objBanquetMasterService.funAddUpdateBanquetMaster(objModel);
			
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Banquet Code : ".concat(objModel.getStrBanquetCode()));
			
			return new ModelAndView("redirect:/frmBanquetMaster.html");
		}
		else{
			return new ModelAndView("frmBanquetMaster");
		}
	}

//Convert bean to model function
	private clsBanquetMasterModel funPrepareModel(clsBanquetMasterBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsBanquetMasterModel objModel = new clsBanquetMasterModel();
		
		if (objBean.getStrBanquetCode().trim().length() == 0){
			/*lastNo = objGlobalFunctionsService.funGetLastNo("tblequipment", "Equipment Master", "intId", clientCode);*/
			lastNo = objGlobalFunctionsService.funGetLastNoModuleWise("tblbanquetmaster", "Banquet Master","intId",clientCode,"3-WebPMS");
			String strBanquetCode = "BC" + String.format("%06d", lastNo);

			objModel.setStrBanquetCode(strBanquetCode);
			
			objModel.setIntId(lastNo);
			objModel.setStrBanquetName(objBean.getStrBanquetName());
			objModel.setStrClientCode(clientCode);
			objModel.setStrOperational(objGlobal.funIfNull(objBean.getStrOperational(), "N", "Y"));
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrBanquetTypeCode(objBean.getStrBanquetTypeCode());
			
			
		}
		
		else
		{
			objModel.setStrBanquetCode(objBean.getStrBanquetCode());
		
			objModel.setIntId(objBean.getIntId());
			objModel.setStrBanquetName(objBean.getStrBanquetName());
			objModel.setStrClientCode(clientCode);
			objModel.setStrOperational(objGlobal.funIfNull(objBean.getStrOperational(), "N", "Y"));
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrBanquetTypeCode(objBean.getStrBanquetTypeCode());
			
		}
		return objModel;

	}

}
