package com.sanguine.webbooks.controller;

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
import com.sanguine.webbooks.bean.clsACSubGroupMasterBean;
import com.sanguine.webbooks.model.clsACGroupMasterModel;
import com.sanguine.webbooks.model.clsACSubGroupMasterModel;
import com.sanguine.webbooks.model.clsACSubGroupMasterModel_ID;
import com.sanguine.webbooks.model.clsWebBooksAccountMasterModel;
import com.sanguine.webbooks.service.clsACSubGroupMasterService;

@Controller
public class clsACSubGroupMasterController {

	@Autowired
	private clsACSubGroupMasterService objACSubGroupMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal = null;

	// Open ACGroupMaster
		@RequestMapping(value = "/frmACSubGroupMaster", method = RequestMethod.GET)
		public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
			String urlHits = "1";
			try {
				urlHits = request.getParameter("saddr").toString();
			} catch (NullPointerException e) {
				urlHits = "1";
			}
			//String clientCode = request.getSession().getAttribute("clientCode").toString();
			model.put("urlHits", urlHits);

			if (urlHits.equalsIgnoreCase("1")) {
				return new ModelAndView("frmACSubGroupMaster", "command", new clsACSubGroupMasterBean());
			} else {
				return new ModelAndView("frmACSubGroupMaster_1", "command", new clsACSubGroupMasterBean());
			}
		}
		
		

		// Load Master Data On Form
		@RequestMapping(value = "/loadACSubGroupMasterData", method = RequestMethod.GET)
		public @ResponseBody clsACSubGroupMasterModel funAssignFields(@RequestParam("acSubGroupCode") String acSubGroupCode, HttpServletRequest req) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			List listModel=objACSubGroupMasterService.funGetWebBooksSubGroupMaster(acSubGroupCode, clientCode);
			clsACSubGroupMasterModel acSubGroupMasterModel =null;
			if (null == listModel || listModel.size()==0) {
				acSubGroupMasterModel = new clsACSubGroupMasterModel();
				acSubGroupMasterModel.setStrGroupCode("Invalid Code");
			}else{
				Object objects[] = (Object[]) listModel.get(0);

				acSubGroupMasterModel = (clsACSubGroupMasterModel) objects[0];
				
				clsACGroupMasterModel objGroupModel = (clsACGroupMasterModel) objects[1];
				acSubGroupMasterModel.setStrGroupName(objGroupModel.getStrGroupName());
				
			}
			
			return acSubGroupMasterModel;
		}
		
		@RequestMapping(value = "/loadUnderSubGroupMasterData", method = RequestMethod.GET)
		public @ResponseBody String funloadUnderSubGroupMasterData(@RequestParam("acSubGroupCode") String acSubGroupCode, HttpServletRequest req) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			clsACSubGroupMasterModel acGroupMasterModel = objACSubGroupMasterService.funGetACSubGroupMaster(acSubGroupCode, clientCode);
			if (null == acGroupMasterModel) {
				acGroupMasterModel = new clsACSubGroupMasterModel();
				acGroupMasterModel.setStrGroupCode("Invalid Code");
			}
			String sgName= "";
			if(acGroupMasterModel.getStrSubGroupName()!=null){
				sgName=acGroupMasterModel.getStrSubGroupName();
			}
			return sgName;
		}

		// Save or Update ACGroupMaster
		@RequestMapping(value = "/saveACSubGroupMaster", method = RequestMethod.POST)
		public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsACSubGroupMasterBean objBean, BindingResult result, HttpServletRequest req) {
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
				clsACSubGroupMasterModel objModel = funPrepareModel(objBean, userCode, clientCode, propCode);
				objACSubGroupMasterService.funAddUpdateACSubGroupMaster(objModel);

				req.getSession().setAttribute("success", true);
				req.getSession().setAttribute("successMessage", "Sub Group Code : ".concat(objModel.getStrSubGroupCode()));

				return new ModelAndView("redirect:/frmACSubGroupMaster.html?saddr=" + urlHits);
			} else {
				return new ModelAndView("redirect:/frmACSubGroupMaster.html?saddr=" + urlHits);
			}
		}

		// Convert bean to model function
		private clsACSubGroupMasterModel funPrepareModel(clsACSubGroupMasterBean objBean, String userCode, String clientCode, String propCode) {
			objGlobal = new clsGlobalFunctions();
			long lastNo = 0;
			clsACSubGroupMasterModel objModel;
			if (objBean.getStrSubGroupCode().trim().length() == 0) {
				lastNo = objGlobalFunctionsService.funGetLastNo("tblsubgroupmaster", "ACSubGroupMaster", "intSGCode", clientCode);
				String acSubGroupCode =String.format("%04d", lastNo);
				objModel = new clsACSubGroupMasterModel(new clsACSubGroupMasterModel_ID(acSubGroupCode, clientCode));
				objModel.setIntSGCode(lastNo);

			} else {
				objModel = new clsACSubGroupMasterModel(new clsACSubGroupMasterModel_ID(objBean.getStrSubGroupCode(), clientCode));
			}
			objModel.setStrSubGroupName(objBean.getStrSubGroupName().toUpperCase());
			objModel.setStrUnderSubGroup(objBean.getStrUnderSubGroup());
			objModel.setStrGroupCode(objBean.getStrGroupCode());
			objModel.setStrClientCode(clientCode);
			
			objModel.setStrUserCreated(userCode);
			objModel.setDteCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrUserModified(userCode);
			objModel.setDteLastModified(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			
			objModel.setStrPropertyCode(propCode);
			return objModel;

		}

}
