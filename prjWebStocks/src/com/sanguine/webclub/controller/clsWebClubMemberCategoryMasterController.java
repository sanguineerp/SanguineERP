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
import com.sanguine.webclub.bean.clsWebClubMemberCategoryMasterBean;
import com.sanguine.webclub.model.clsWebClubCategeoryWiseFacilityModel;
import com.sanguine.webclub.model.clsWebClubFacilityMasterModel;
import com.sanguine.webclub.model.clsWebClubMemberCategoryMasterModel;
import com.sanguine.webclub.model.clsWebClubMemberCategoryMasterModel_ID;
import com.sanguine.webclub.service.clsWebClubMemberCategoryMasterService;

@Controller
public class clsWebClubMemberCategoryMasterController {


	@Autowired
	private clsWebClubMemberCategoryMasterService objWebClubMemberCategoryMasterService;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal = null;

	// Open WebClubMemberCategoryMaster
	@RequestMapping(value = "/frmMemberCategoryMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {

		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmMemberCategoryMaster_1", "command", new clsWebClubMemberCategoryMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmMemberCategoryMaster", "command", new clsWebClubMemberCategoryMasterBean());
		} else {
			return null;
		}

	}

	// Load Master Data On Form
	@RequestMapping(value = "/loadWebClubMemberCategoryMaster", method = RequestMethod.GET)
	public @ResponseBody clsWebClubMemberCategoryMasterModel funLoadMasterData(@RequestParam("catCode") String catCode, HttpServletRequest req) {
		objGlobal = new clsGlobalFunctions();
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		clsWebClubMemberCategoryMasterModel objMemCatModel = objWebClubMemberCategoryMasterService.funGetWebClubMemberCategoryMaster(catCode, clientCode);
		clsWebClubMemberCategoryMasterModel objWebClubMemberCategoryMaster = new clsWebClubMemberCategoryMasterModel();

		if (null == objMemCatModel) {
			objMemCatModel = new clsWebClubMemberCategoryMasterModel();
			objMemCatModel.setStrCatCode("Invalid Code");
		}
		
		return objMemCatModel;
	}
	
	// Load Master Data For Member
		@RequestMapping(value = "/loadWebClubFacilityMaster", method = RequestMethod.GET)
		public @ResponseBody clsWebClubFacilityMasterModel funLoadFacilityMasterData(@RequestParam("catCode") String catCode, HttpServletRequest req) {
			objGlobal = new clsGlobalFunctions();
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String facilityCode="";
			clsWebClubFacilityMasterModel objMemCatModel =  objWebClubMemberCategoryMasterService.funGetWebClubMemberFacilityMaster(catCode,clientCode);
			clsWebClubFacilityMasterModel objWebClubMemberCategoryMaster = new clsWebClubFacilityMasterModel();
			
			if (null == objMemCatModel) {
				objMemCatModel = new clsWebClubFacilityMasterModel();
				objMemCatModel.setStrFacilityCode("Invalid Code");
			}		
			return objMemCatModel;			
		}
	
		
		// Load Master Data For Member List
				@RequestMapping(value = "/loadWebClubFacilityMasterListDtl", method = RequestMethod.GET)
				public @ResponseBody List funLoadFacilityMasterListData(@RequestParam("catCode") String catCode, HttpServletRequest req) {
					objGlobal = new clsGlobalFunctions();
					String clientCode = req.getSession().getAttribute("clientCode").toString();
					String userCode = req.getSession().getAttribute("usercode").toString();
					List<String> list =  objWebClubMemberCategoryMasterService.funGetCategoryWiseFacilityDtlList(catCode,clientCode);
					//clsWebClubCategeoryWiseFacilityModel objWebClubMemberCategoryMaster = new clsWebClubCategeoryWiseFacilityModel();
					
					return list;					
				}
	// Save or Update WebClubMemberCategoryMaster
	@RequestMapping(value = "/saveWebClubMemberCategoryMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsWebClubMemberCategoryMasterBean objBean, BindingResult result, HttpServletRequest req) {

		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}

		if (!result.hasErrors()) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			//clsWebClubMemberCategoryMasterModel objModel = funPrepareModel(objBean, userCode, clientCode);
			
			objGlobal = new clsGlobalFunctions();
			long lastNo = 0;
			clsWebClubMemberCategoryMasterModel objModel=null;
			if (objBean.getStrCatCode().trim().length() == 0) {
				lastNo = objGlobalFunctionsService.funGetLastNo("tblmembertypemaster", "MemberCategoryMaster", "intGId", clientCode);
				String catCode = "MC" + String.format("%06d", lastNo);
			    	 objModel = new clsWebClubMemberCategoryMasterModel(new clsWebClubMemberCategoryMasterModel_ID(catCode, clientCode));
						objModel.setStrCatCode(catCode);
		    		 	objModel.setIntGId(lastNo);
						objModel.setStrUserCreated(userCode);
						objModel.setStrUserModified(userCode);
						objModel.setStrClientCode(clientCode);
						objModel.setStrCatName(objBean.getStrCatName());
						objModel.setStrGroupCategoryCode(objBean.getStrGroupCategoryCode());
						objModel.setStrTenure(objBean.getStrTenure());
						objModel.setStrRuleCode(objBean.getStrRuleCode());
						objModel.setDblCreditAmt(objBean.getDblCreditAmt());
						objModel.setStrVotingRights(objBean.getStrVotingRights());
						objModel.setStrRemarks(objBean.getStrRemarks());
						objModel.setDblCreditAmt(objBean.getDblCreditAmt());
						objModel.setDblDisAmt(objBean.getDblDisAmt());
						objModel.setDtModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
						objModel.setStrcatdesc(objBean.getStrcatdesc());
						objModel.setStrCorporate("N");
						objModel.setStrPropertyCode("");			
						objModel.setDtCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
						objWebClubMemberCategoryMasterService.funAddUpdateWebClubMemberCategoryMaster(objModel); 
			 
			} else {
				 
					objWebClubMemberCategoryMasterService.funGetCategoryWiseFacilityDtl(objBean.getStrCatCode(),clientCode);			    		    	
					objModel = new clsWebClubMemberCategoryMasterModel();
					if(objBean.getListFacilityDtl()!=null)
				     {
				    	 
							
				    	 for(clsWebClubFacilityMasterModel ob:objBean.getListFacilityDtl())
				    	 { 	    		 
				    		
						    	clsWebClubCategeoryWiseFacilityModel obj = new clsWebClubCategeoryWiseFacilityModel();
						      		obj.setStrCatCode(objBean.getStrCatCode());
							    	obj.setStrClientCode(clientCode);
							    	obj.setStrFacilityCode(ob.getStrFacilityCode());
							    	obj.setStrFacilityName(ob.getStrFacilityName());
							    	obj.setStrOperationalYN(ob.getStrOperationalNY());
							    	//objWebClubMemberCategoryMasterService.funAddUpdateWebClubMemberCategoryMaster(obj);	
							    	objWebClubMemberCategoryMasterService.funAddUpdateWebClubCategeoryWiseFacility(obj);	  
				    	 	}
				     }
				    		//objModel.setStrFacilityCode(objBean.getStrFacilityCode()); 
				    	 	objModel.setIntGId(lastNo);
				    	 	objModel.setStrCatCode(objBean.getStrCatCode());
				    	 	objModel.setStrUserCreated(userCode);
				    	 	objModel.setStrUserModified(userCode);
				    	 	objModel.setStrClientCode(clientCode);
				 			objModel.setStrCatName(objBean.getStrCatName());
				 			objModel.setStrGroupCategoryCode(objBean.getStrGroupCategoryCode());
				 			objModel.setStrTenure(objBean.getStrTenure());
				 			objModel.setStrRuleCode(objBean.getStrRuleCode());
				 			objModel.setDblCreditAmt(objBean.getDblCreditAmt());
				 			objModel.setStrVotingRights(objBean.getStrVotingRights());
				 			objModel.setStrRemarks(objBean.getStrRemarks());				 			
				 			objModel.setDblDisAmt(objBean.getDblDisAmt());				 
				 			objModel.setDtModifiedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				 			objModel.setStrcatdesc(objBean.getStrcatdesc());
				 			objModel.setStrCorporate("N");
				 			objModel.setStrPropertyCode("");			
				 			objModel.setDtCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				 			objModel.setStrFacilityCode(objBean.getStrFacilityCode());//optional
				 			objModel.setIntCreditLimit(objBean.getIntCreditLimit());
				    		 objWebClubMemberCategoryMasterService.funAddUpdateWebClubMemberCategoryMaster(objModel);	    	
				    	 			    	
				     
			}
			
			objWebClubMemberCategoryMasterService.funAddUpdateWebClubMemberCategoryMaster(objModel);

			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Member Category Code : ".concat(objModel.getStrCatCode()));
			return new ModelAndView("redirect:/frmMemberCategoryMaster.html?saddr=" + urlHits);
		} else {
			return new ModelAndView("frmMemberCategoryMaster");
		}
	}

	// Convert bean to model function
/*	private clsWebClubMemberCategoryMasterModel funPrepareModel(clsWebClubMemberCategoryMasterBean objBean, String userCode, String clientCode) {
		

		return objModel;*/

	

}
