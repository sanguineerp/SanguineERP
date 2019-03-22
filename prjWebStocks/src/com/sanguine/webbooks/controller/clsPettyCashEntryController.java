package com.sanguine.webbooks.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.sanguine.model.clsCurrencyMasterModel;
import com.sanguine.webbooks.bean.clsJVBean;
import com.sanguine.webbooks.bean.clsJVDetailsBean;
import com.sanguine.webbooks.bean.clsPaymentBean;
import com.sanguine.webbooks.bean.clsPettyCashEntryBean;
import com.sanguine.webbooks.model.clsExpenseMasterModel;
import com.sanguine.webbooks.model.clsJVDebtorDtlModel;
import com.sanguine.webbooks.model.clsJVDtlModel;
import com.sanguine.webbooks.model.clsJVHdModel;
import com.sanguine.webbooks.model.clsPettyCashEntryDtlModel;
import com.sanguine.webbooks.model.clsPettyCashEntryHdModel;
import com.sanguine.webbooks.service.clsPettyCashEntryService;


@Controller
public class clsPettyCashEntryController {
	
	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@Autowired 
	private clsPettyCashEntryService objService;
	
	@Autowired 
	private clsPaymentController objPaymentController;
	
	
	// Open AccountHolderMaster
		@RequestMapping(value = "/frmPettyCashEntry", method = RequestMethod.GET)
		public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
			String urlHits = "1";
			try {
				urlHits = request.getParameter("saddr").toString();
			} catch (NullPointerException e) {
				urlHits = "1";
			}
			model.put("urlHits", urlHits);

			if (urlHits.equalsIgnoreCase("1")) {
				return new ModelAndView("frmPettyCashEntry", "command", new clsPettyCashEntryBean());
			} else {
				return new ModelAndView("frmPettyCashEntry_1", "command", new clsPettyCashEntryBean());
			}
		}



		// Save or Update JV
		@RequestMapping(value = "/savePettyCash", method = RequestMethod.POST)
		public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsPettyCashEntryBean objBean, BindingResult result, HttpServletRequest req) {
			if (!result.hasErrors()) {
				String clientCode = req.getSession().getAttribute("clientCode").toString();
				String userCode = req.getSession().getAttribute("usercode").toString();
				
				
				clsPettyCashEntryHdModel objHdModel= funPrepareHdModel(objBean,userCode,clientCode,req);
				objService.funAddUpdatePettyHd(objHdModel);
				clsPaymentBean objPayBean=new clsPaymentBean();
				
				
				//objPaymentController.funAddUpdate(objPayBean, result, req);
				
				req.getSession().setAttribute("success", true);
				req.getSession().setAttribute("successMessage", " Petty Cash Entry : ".concat(objHdModel.getStrVouchNo()));
				req.getSession().setAttribute("rptVoucherNo", objHdModel.getStrVouchNo());
				return new ModelAndView("redirect:/frmPettyCashEntry.html");
			} else {
				return new ModelAndView("frmPettyCashEntry");
			}
		}
		
		
		
		private clsPettyCashEntryHdModel funPrepareHdModel(clsPettyCashEntryBean objBean, String userCode, String clientCode, HttpServletRequest request) {

			
			clsPettyCashEntryHdModel objModel = new clsPettyCashEntryHdModel();

			if (objBean.getStrVouchNo().isEmpty()) // New Entry
			{
				String documentNo = objGlobal.funGenerateDocumentCodeWebBook("frmPettyCashEntry", objBean.getDteVouchDate(), request);
				objModel.setStrVouchNo(documentNo);
				objModel.setStrUserCreated(userCode);
				objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			} 
			else // Update
			{
				objModel.setStrUserCreated(userCode);
				objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				objModel.setStrVouchNo(objBean.getStrVouchNo());
			}

			
			objModel.setIntId(0);
			objModel.setStrNarration(objGlobal.funIfNull(objBean.getStrNarration(), "NA", objBean.getStrNarration()));
			objModel.setDteVouchDate(objGlobal.funGetDateAndTime("yyyy-MM-dd", objBean.getDteVouchDate()));
			objModel.setStrUserEdited(userCode);
			objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			objModel.setStrClientCode(clientCode);
			objModel.setDblGrandTotal(objBean.getDblGrandTotal());
			List <clsPettyCashEntryDtlModel>listPettyCashEntryDtlModel=new ArrayList<clsPettyCashEntryDtlModel>();
			for(clsPettyCashEntryDtlModel objDtlModel:objBean.getListPettyCashDtl())
			{
				listPettyCashEntryDtlModel.add(objDtlModel);
				
			}
			objModel.setListDtlModel(listPettyCashEntryDtlModel);
			return objModel;
		}
		
		
		@RequestMapping(value = "/loadPettyCash", method = RequestMethod.GET)
		public @ResponseBody clsPettyCashEntryHdModel funGetPettyCash(@RequestParam("vouchNo") String vouchNo, HttpServletRequest req) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			clsPettyCashEntryHdModel objModel = objService.funGetPettyList(vouchNo, clientCode,propertyCode);
			if (null == objModel) {
				objModel = new clsPettyCashEntryHdModel();
				objModel.setStrVouchNo("Invalid Code");
			}else{
			objModel.setDteVouchDate(objGlobal.funGetDateAndTime("yyyy-MM-dd", objModel.getDteVouchDate()));
			}
			return objModel;
		}

		
}
