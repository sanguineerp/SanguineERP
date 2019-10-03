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
import com.sanguine.webbanquets.bean.clsCostCenterMasterBean;
import com.sanguine.webbanquets.bean.clsEquipmentBean;
import com.sanguine.webbanquets.model.clsCostCenterMasterModel;
import com.sanguine.webbanquets.model.clsEquipmentModel;
import com.sanguine.webbanquets.service.clsCostCenterMasterService;

import java.util.List;
import java.util.Map;

@Controller
public class clsCostCenterMasterController{

	@Autowired
	private clsCostCenterMasterService objCostCenterMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;

//Open CostCenterMaster
	/*@RequestMapping(value = "/frmCostCenterMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(){
		return new ModelAndView("frmCostCenterMaster","command", new clsCostCenterMasterModel());
	}
//Load Master Data On Form
	@RequestMapping(value = "/frmCostCenterMaster1", method = RequestMethod.POST)
	public @ResponseBody clsCostCenterMasterModel funLoadMasterData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql="";
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String userCode=request.getSession().getAttribute("userCode").toString();
		clsCostCenterMasterBean objBean=new clsCostCenterMasterBean();
		String docCode=request.getParameter("docCode").toString();
		List listModel=objGlobalFunctionsService.funGetList(sql);
		clsCostCenterMasterModel objCostCenterMaster = new clsCostCenterMasterModel();
		return objCostCenterMaster;
	}
*/
	
	@RequestMapping(value = "/frmCostCenterMaster", method = RequestMethod.GET)
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
			return new ModelAndView("frmCostCenterMaster_1", "command", new clsCostCenterMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmCostCenterMaster", "command", new clsCostCenterMasterBean());
		} else {
			return null;
		}
	}
	
	@RequestMapping(value = "/loadCostCenterCode", method = RequestMethod.GET)
	public @ResponseBody clsCostCenterMasterModel funAssignFields(@RequestParam("docCode") String docCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
/*		clsEquipmentModel objModel = objEquipmentService.funGetEquipment(docCode, clientCode);
*/		clsCostCenterMasterModel objModel = objCostCenterMasterService.funGetCostCenterMaster(docCode, clientCode);

		if (null == objModel) {
			objModel = new clsCostCenterMasterModel(); 
			objModel.setStrCostCenterCode("Invalid Code");
		}
		return objModel;
	}

	
	
//Save or Update CostCenterMaster
	@RequestMapping(value = "/saveCostCenterMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsCostCenterMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			clsCostCenterMasterModel objModel = funPrepareModel(objBean,userCode,clientCode);
			objCostCenterMasterService.funAddUpdateCostCenterMaster(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Cost Center Code : ".concat(objModel.getStrCostCenterCode()));
			return new ModelAndView("redirect:/frmCostCenterMaster.html");
		}
		else{
			return new ModelAndView("frmCostCenterMaster");
		}
	}

//Convert bean to model function
	private clsCostCenterMasterModel funPrepareModel(clsCostCenterMasterBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsCostCenterMasterModel objModel = new clsCostCenterMasterModel();
		
		if (objBean.getStrCostCenterCode().trim().length() == 0){
			/*lastNo = objGlobalFunctionsService.funGetLastNo("tblcostcentermaster", "Cost Center Master", "intId", clientCode);*/
			lastNo = objGlobalFunctionsService.funGetLastNoModuleWise("tblcostcentermaster", "Cost Center Master","intId",clientCode,"3-WebPMS");
			String strEquipmentCode = "CC" + String.format("%06d", lastNo);
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setIntId(lastNo);
			objModel.setStrClientCode(clientCode);
			objModel.setStrCostCenterCode(strEquipmentCode);
			objModel.setStrCostCenterName(objBean.getStrCostCenterName());
			objModel.setStrOperational(objGlobal.funIfNull(objBean.getStrOperational(), "N", "Y"));
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
		
		}
		else
		{
			objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setIntId(lastNo);
			objModel.setStrClientCode(clientCode);
			objModel.setStrCostCenterCode(objBean.getStrCostCenterCode());
			objModel.setStrCostCenterName(objBean.getStrCostCenterName());
			objModel.setStrOperational(objGlobal.funIfNull(objBean.getStrOperational(), "N", "Y"));
			objModel.setStrUserCreated(userCode);
			objModel.setStrUserEdited(userCode);
		}
		return objModel;

	}

}
