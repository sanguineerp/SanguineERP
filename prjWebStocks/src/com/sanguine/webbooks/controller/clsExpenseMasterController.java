package com.sanguine.webbooks.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

import com.sanguine.bean.clsGroupMasterBean;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsGroupMasterModel;
import com.sanguine.model.clsGroupMasterModel_ID;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webbooks.bean.clsExpenseMasterBean;
import com.sanguine.webbooks.model.clsExpenseMasterModel;
import com.sanguine.webbooks.model.clsExpenseMasterModel_ID;
import com.sanguine.webbooks.model.clsWebBooksAccountMasterModel;
import com.sanguine.webbooks.service.clsExpenseMasterService;
@Controller
public class clsExpenseMasterController {
	
	@Autowired
	clsGlobalFunctions objGlobal;
	
	@Autowired
	clsExpenseMasterService objExpenseMasterService;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	
	
	@RequestMapping(value = "/frmExpenseMaster", method = RequestMethod.GET)

	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {

		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmExpenseMaster_1", "command", new clsExpenseMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmExpenseMaster", "command", new clsExpenseMasterBean());
		} else {
			return null;
		}
	}
	
	@RequestMapping(value = "/saveExpenseMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsExpenseMasterBean expBean, BindingResult result, HttpServletRequest req, HttpServletResponse resp) {
		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
			

		} catch (Exception e) {
			e.printStackTrace();
			urlHits = "1";
		}

		if (!result.hasErrors()) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propCode = req.getSession().getAttribute("propertyCode").toString();

			clsExpenseMasterModel objModel = funPrepareModel(expBean, userCode, clientCode);
			objExpenseMasterService.funAddExpense(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Expense Code : ".concat(objModel.getStrExpCode()));
			return new ModelAndView("redirect:/frmExpenseMaster.html?saddr=" + urlHits);
		} else {
			return new ModelAndView("redirect:/frmExpenseMaster.html?saddr=" + urlHits);
		}
	}
	
	private clsExpenseMasterModel funPrepareModel(clsExpenseMasterBean expBean, String userCode, String clientCode) {
		objGlobal = new clsGlobalFunctions();
		long lastNo = 0;
		clsExpenseMasterModel expense;
		if (expBean.getStrExpCode().trim().length() == 0) {
			lastNo = objGlobalFunctionsService.funGetLastNo("tblexpensemaster", "tblexpensemaster", "intEId", clientCode);
			String expCode = "EX" + String.format("%06d", lastNo);
			expense = new clsExpenseMasterModel(new clsExpenseMasterModel_ID(expCode, clientCode));
			expense.setIntEId(lastNo);
			expense.setStrUserCreated(userCode);
			expense.setDtCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		} else {
			clsExpenseMasterModel objExpModel = objExpenseMasterService.funGetExpense(expBean.getStrExpCode(), clientCode);
			if (null == objExpModel) {
				lastNo = objGlobalFunctionsService.funGetLastNo("tblexpensemaster", "tblexpensemaster", "intEId", clientCode);
				String expCode = "EX" + String.format("%06d", lastNo);
				expense = new clsExpenseMasterModel(new clsExpenseMasterModel_ID(expCode, clientCode));
				expense.setIntEId(lastNo);
				expense.setStrUserCreated(userCode);
				expense.setDtCreatedDate(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			} else {
				expense = new clsExpenseMasterModel(new clsExpenseMasterModel_ID(expBean.getStrExpCode(), clientCode));
			}
		}
		expense.setStnExpName(expBean.getStnExpName().toUpperCase());
		expense.setStrExpShortName(expBean.getStrExpShortName());
		expense.setStrUserModified(userCode);
		expense.setDtLastModified(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		expense.setStrGLCode(expBean.getStrGLCode());

		return expense;
	}
	
	
	
	@RequestMapping(value = "/loadExpenseCode", method = RequestMethod.GET)
	public @ResponseBody clsExpenseMasterModel funGetAccountCodeAndName(@RequestParam("expCode") String expCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		clsExpenseMasterModel objModel = objExpenseMasterService.funGetExpense(expCode, clientCode);
		if (null == objModel) {
			objModel = new clsExpenseMasterModel();
			objModel.setStrExpCode("Invalid Code");
		}

		return objModel;
	}

	
}
