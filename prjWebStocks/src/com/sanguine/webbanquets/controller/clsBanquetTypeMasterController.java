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
import com.sanguine.webbanquets.bean.clsBanquetTypeMasterBean;
import com.sanguine.webbanquets.bean.clsEquipmentBean;
import com.sanguine.webbanquets.model.clsBanquetTypeMasterModel;
import com.sanguine.webbanquets.model.clsCostCenterMasterModel;
import com.sanguine.webbanquets.service.clsBanquetTypeMasterService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class clsBanquetTypeMasterController{

	@Autowired
	private clsBanquetTypeMasterService objBanquetTypeMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	@Autowired
	private clsGlobalFunctions objGlobal;

//Open BanquetTypeMaster
	@RequestMapping(value = "/frmBanquetTypeMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {

		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		List<String> listTaxIndicator = new ArrayList<>();
		listTaxIndicator.add(" ");
		String[] alphabetSet = objGlobal.funGetAlphabetSet();
		for (int i = 0; i < alphabetSet.length; i++) {
			listTaxIndicator.add(alphabetSet[i]);
		}
		model.put("taxIndicatorList", listTaxIndicator);
		//objSampleService.funSaveData("WebBAnquet");
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBanquetTypeMaster_1", "command", new clsBanquetTypeMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBanquetTypeMaster", "command", new clsBanquetTypeMasterBean());
		} else {
			return null;
		}
	}

	@RequestMapping(value = "/loadBanquetType", method = RequestMethod.GET)
	public @ResponseBody clsBanquetTypeMasterModel funAssignFields(@RequestParam("docCode") String docCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
/*		clsEquipmentModel objModel = objEquipmentService.funGetEquipment(docCode, clientCode);
*/		clsBanquetTypeMasterModel objModel = objBanquetTypeMasterService.funGetBanquetTypeMaster(docCode, clientCode);

		if (null == objModel) {
			objModel = new clsBanquetTypeMasterModel(); 
			objModel.setStrBanquetTypeCode("Invalid Code");
		}
		return objModel;
	}
	
//Save or Update BanquetTypeMaster
	@RequestMapping(value = "/saveBanquetTypeMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsBanquetTypeMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsBanquetTypeMasterModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objBanquetTypeMasterService.funAddUpdateBanquetTypeMaster(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Banquet Type Code : ".concat(objModel.getStrBanquetTypeCode()));

			
			return new ModelAndView("redirect:/frmBanquetTypeMaster.html");
		}
		else{
			return new ModelAndView("frmBanquetTypeMaster");
		}
	}

//Convert bean to model function
	private clsBanquetTypeMasterModel funPrepareModel(clsBanquetTypeMasterBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsBanquetTypeMasterModel objModel = new clsBanquetTypeMasterModel();
		
		if (objBean.getStrBanquetTypeCode().trim().length() == 0){
			/*lastNo = objGlobalFunctionsService.funGetLastNo("tblequipment", "Equipment Master", "intId", clientCode);*/
			lastNo = objGlobalFunctionsService.funGetLastNoModuleWise("tblbanquettypemaster", "Banquet Type Master","intId",clientCode,"3-WebPMS");
			String strEquipmentCode = "BT" + String.format("%06d", lastNo);

			objModel.setDblRate(objBean.getDblRate());
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setIntId(lastNo);
			objModel.setStrBanquetTypeCode(strEquipmentCode);
			objModel.setStrBanquetTypeName(objBean.getStrBanquetTypeName());
			objModel.setStrClientCode(clientCode);
			objModel.setStrTaxIndicator(objBean.getStrTaxIndicator());
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
			
		}
		else
		{
			objModel.setDblRate(objBean.getDblRate());
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setIntId(objBean.getIntId());
			objModel.setStrBanquetTypeCode(objBean.getStrBanquetTypeCode());
			objModel.setStrBanquetTypeName(objBean.getStrBanquetTypeName());
			objModel.setStrClientCode(clientCode);
			objModel.setStrTaxIndicator(objBean.getStrTaxIndicator());
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
		}
		return objModel;

	}

}
