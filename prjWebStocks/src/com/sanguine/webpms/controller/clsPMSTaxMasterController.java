package com.sanguine.webpms.controller;

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
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsPMSTaxMasterBean;
import com.sanguine.webpms.dao.clsWebPMSDBUtilityDao;
import com.sanguine.webpms.model.clsPMSSettlementTaxMasterModel;
import com.sanguine.webpms.model.clsPMSTaxMasterModel;
import com.sanguine.webpms.model.clsPMSTaxMasterModel_ID;
import com.sanguine.webpms.service.clsPMSTaxMasterService;

@Controller
public class clsPMSTaxMasterController {

	@Autowired
	private clsPMSTaxMasterService objPMSTaxMasterService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@Autowired
	private clsWebPMSDBUtilityDao objWebPMSUtility;

	// Open PMSTaxMaster
	@RequestMapping(value = "/frmPMSTaxMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		String clientCode = request.getSession().getAttribute("clientCode").toString();
		List<String> listDepartment = objPMSTaxMasterService.funGetPMSDepartments(clientCode);
		model.put("listDepartment", listDepartment);

		List<String> listIncomeHead = objPMSTaxMasterService.funGetIncomeHead(clientCode);
		model.put("listIncomeHead", listIncomeHead);

		List<String> listTaxType = new ArrayList<String>();
		listTaxType.add("Percentage");
		listTaxType.add("Amount");
		model.put("listTaxType", listTaxType);

		List<String> listTaxType2 = new ArrayList<String>();
		listTaxType2.add("External");
		listTaxType2.add("Internal");
		model.put("listTaxType2", listTaxType2);

		List<String> listPerOrAmt = new ArrayList<String>();
		listPerOrAmt.add("Room Night");
		listPerOrAmt.add("Income Head");
		model.put("listPerOrAmt", listPerOrAmt);

		List<String> listTaxOn = new ArrayList<String>();
		listTaxOn.add("Gross Amount");
		listTaxOn.add("Discount Amount");
		model.put("listTaxOn", listTaxOn);

		List<String> listDiplomat = new ArrayList<String>();
		listDiplomat.add("Yes");
		listDiplomat.add("No");
		model.put("listDiplomat", listDiplomat);

		List<String> listLocalForeigner = new ArrayList<String>();
		listLocalForeigner.add("Local");
		listLocalForeigner.add("Foreigner");
		model.put("listLocalForeigner", listLocalForeigner);

		List<String> listTaxOnTax = new ArrayList<>();
		listTaxOnTax.add("");
		List<String> list = objPMSTaxMasterService.funGetPMSTaxes(clientCode);
		listTaxOnTax.addAll(list);
		model.put("listTaxOnTax", listTaxOnTax);

		List<String> listTaxOnTaxable = new ArrayList<String>();
		listTaxOnTaxable.add("");
		model.put("listTaxOnTaxable", listTaxOnTaxable);

		List<String> listTaxGroup = objPMSTaxMasterService.funGetPMSTaxGroup(clientCode);
		model.put("listTaxGroup", listTaxGroup);

		if (urlHits.equalsIgnoreCase("1")) {
			return new ModelAndView("frmPMSTaxMaster", "command", new clsPMSTaxMasterBean());
		} else {
			return new ModelAndView("frmPMSTaxMaster_1", "command", new clsPMSTaxMasterBean());
		}
	}

	// Load Master Data On Form
	@RequestMapping(value = "/loadPMSTaxMasterData", method = RequestMethod.GET)
	public @ResponseBody clsPMSTaxMasterModel funLoadMasterData(@RequestParam("taxCode") String taxCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		clsPMSTaxMasterModel objModel = objPMSTaxMasterService.funGetPMSTaxMaster(taxCode, clientCode);
		if (objModel == null) {
			objModel = new clsPMSTaxMasterModel();
			objModel.setStrTaxCode("Invalid Code");
		}

		return objModel;
	}

	// get master name from query + code //
	@RequestMapping(value = "/getMasterNameFromCode", method = RequestMethod.GET)
	public @ResponseBody Map<String, String> funGetMasterNameFromCode(@RequestParam("masterName") String masterName, @RequestParam("masterCode") String masterCode, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String query = null;
		switch (masterName) {
		case "DepartmentMaster":
			query = "select strDeptDesc  from tbldepartmentmaster where strDeptCode='" + masterCode + "' and strClientCode='" + clientCode + "' ";
			break;
		case "IncomeHeadMaster":
			query = "select a.strIncomeHeadDesc  from tblincomehead  a where a.strIncomeHeadCode='" + masterCode + "' and strClientCode='" + clientCode + "' ";
			break;
		case "TaxOnTaxCode":
			query = "select a.strTaxDesc  from tbltaxmaster  a where a.strTaxCode='" + masterCode + "' and strClientCode='" + clientCode + "' ";
			break;
		case "TaxGroupMaster":
			query = "select a.strTaxGroupDesc  from tbltaxgroup  a where a.strTaxGroupCode='" + masterCode + "' and strClientCode='" + clientCode + "' ";
			break;
		}
		String name = objPMSTaxMasterService.funGetMasterName(query);
		Map<String, String> mapName = new HashMap<>();
		mapName.put("name", name);
		return mapName;
	}

	// Save or Update PMSTaxMaster
	@RequestMapping(value = "/savePMSTaxMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsPMSTaxMasterBean objBean, BindingResult result, HttpServletRequest req) {
		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		if (!result.hasErrors()) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			
			clsPMSTaxMasterModel objModel = funPrepareModel(objBean, userCode, clientCode, propertyCode);
				objPMSTaxMasterService.funAddUpdatePMSTaxMaster(objModel);
			
			//clsPMSSettlementMasterModel objModel1 = funPrepareModelSettlemet(objBean, userCode, clientCode, propertyCode);
			//objPMSTaxMasterService.funAddUpdatePMSSettlementTaxMaster(objModel1);
			
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Tax Code : ".concat(objModel.getStrTaxCode()));

			return new ModelAndView("redirect:/frmPMSTaxMaster.html?saddr=" + urlHits);
		} else {
			return new ModelAndView("redirect:/frmPMSTaxMaster.html?saddr=" + urlHits);
		}
	}
	
	/*@RequestMapping(value = "/savePMSTaxMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsPMSTaxMasterBean objBean, BindingResult result, HttpServletRequest req) {
		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		if (!result.hasErrors()) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();
			
			clsPMSTaxMasterModel objModel = funPrepareModel(objBean, userCode, clientCode, propertyCode);
			String sql = null;
			
			String delQuery = "delete from tbltaxmaster  where strDeptCode='"+objModel.getStrDeptCode()+"'";
			objWebPMSUtility.funExecuteUpdate(delQuery, "sql");
			
			String sql1= "select a.strIncomeHeadCode from tblincomehead a where a.strDeptCode='"+objModel.getStrDeptCode()+"' ;";
			List list  = objWebPMSUtility.funExecuteQuery(sql1, "sql");
			for(int i=0;i<list.size();i++)
			{
				String incomHeadCode = list.get(i).toString();
				objBean.setStrIncomeHeadCode(incomHeadCode);
				objModel=funPrepareModel(objBean, userCode, clientCode, propertyCode);
				objPMSTaxMasterService.funAddUpdatePMSTaxMaster(objModel);
			}
			if(objModel.getStrDeptCode()=="")
			{
				 sql = "UPDATE tbltaxmaster a set a.dblTaxValue='"+objModel.getDblTaxValue()+"' WHERE a.strIncomeHeadCode='"+objModel.getStrIncomeHeadCode()+"'";
				objPMSTaxMasterService.funAddUpdatePMSTaxMaster(objModel);
			}
			else
			{
				 sql = "UPDATE tbltaxmaster a set a.dblTaxValue='"+objModel.getDblTaxValue()+"' WHERE a.strDeptCode='"+objModel.getStrDeptCode()+"' and a.strTaxCode = '"+objModel.getStrTaxCode()+"' ";
				 objWebPMSUtility.funExecuteUpdate(sql, "sql");
			}
			
						req.getSession().setAttribute("success", true);
			
			req.getSession().setAttribute("successMessage", "Tax Code : ".concat(objModel.getStrTaxCode()));

			return new ModelAndView("redirect:/frmPMSTaxMaster.html?saddr=" + urlHits);
		} else {
			return new ModelAndView("redirect:/frmPMSTaxMaster.html?saddr=" + urlHits);
		}
	}*/

	/*private clsPMSSettlementMasterModel funPrepareModelSettlemet(clsPMSTaxMasterBean objBean, String userCode, String clientCode,
			String propertyCode) {
	
		
		 
		for (clsPMSSettlementMasterModel objModel : objBean.getListSettlement()) 
		{
			objBean.setStrSettlementCode(objModel.getStrSettlementCode());
			objBean.setStrSettlementDesc(objModel.getStrSettlementName());
			break;
		}
			
		
		
		
		return objModel;
	}*/

	// Convert bean to model function
	private clsPMSTaxMasterModel funPrepareModel(clsPMSTaxMasterBean objBean, String userCode, String clientCode, String propertyCode) {
		long lastNo = 0;
		clsPMSTaxMasterModel objModel = null;
		if (objBean.getStrTaxCode().trim().length() == 0) {
			lastNo = objGlobalFunctionsService.funGetPMSMasterLastNo("tbltaxmaster", "TaxMaster", "strTaxCode", clientCode);
			String taxCode = "TC" + String.format("%06d", lastNo);
			objModel = new clsPMSTaxMasterModel(new clsPMSTaxMasterModel_ID(taxCode, clientCode));
		} else {
			objModel = new clsPMSTaxMasterModel(new clsPMSTaxMasterModel_ID(objBean.getStrTaxCode(), clientCode));
		}

		objModel.setStrTaxDesc(objBean.getStrTaxDesc());
		objModel.setStrDeptCode(objBean.getStrDeptCode());
		objModel.setStrIncomeHeadCode(objBean.getStrIncomeHeadCode());
		// objModel.setStrDeptCode(funGetCodeFromName("strDeptCode",
		// "strDeptDesc", objBean.getStrDeptCode(), "tbldepartmentmaster",
		// clientCode));
		// objModel.setStrIncomeHeadCode(funGetCodeFromName("strIncomeHeadCode",
		// "strIncomeHeadDesc", objBean.getStrIncomeHeadCode(), "tblincomehead",
		// clientCode));
		objModel.setStrTaxType(objBean.getStrTaxType());
		objModel.setStrTaxOnType(objBean.getStrTaxOnType());
		objModel.setDblTaxValue(objBean.getDblTaxValue());
		objModel.setStrTaxOn(objBean.getStrTaxOn());
		objModel.setStrDeplomat(objBean.getStrDeplomat());
		objModel.setStrLocalOrForeigner(objBean.getStrLocalOrForeigner());
		objModel.setDteValidFrom(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteValidFrom()));
		objModel.setDteValidTo(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteValidTo()));
		objModel.setDblFromRate(objBean.getDblFromRate());
		objModel.setDblToRate(objBean.getDblToRate());

		if (objBean.getStrTaxOnTaxCode() != null && objBean.getStrTaxOnTaxCode().trim().length() > 0) {
			objModel.setStrTaxOnTaxCode(funGetCodeFromName("strTaxCode", "strTaxDesc", objBean.getStrTaxOnTaxCode(), "tbltaxmaster", clientCode));
		} else {
			objModel.setStrTaxOnTaxCode("");
		}

		if (null == objBean.getStrTaxOnTaxable()) {
			objModel.setStrTaxOnTaxable("");
		} else {
			objModel.setStrTaxOnTaxable(objBean.getStrTaxOnTaxable());
		}

		if (objBean.getStrTaxGroupCode() != null && objBean.getStrTaxGroupCode().trim().length() > 0) {
			objModel.setStrTaxGroupCode(funGetCodeFromName("strTaxGroupCode", "strTaxGroupDesc", objBean.getStrTaxGroupCode(), "tbltaxgroup", clientCode));
		} else {
			objModel.setStrTaxGroupCode("");
		}
		objModel.setStrUserCreated(userCode);
		objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrUserEdited(userCode);
		objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrAccountCode(objBean.getStrAccountCode());
		
		List<clsPMSSettlementTaxMasterModel> listSetTAxModel =new ArrayList();
		
		if(objBean.getListSettlement()!=null && objBean.getListSettlement().size()>0){
			for(clsPMSSettlementTaxMasterModel objSettlmentTax :objBean.getListSettlement())
			{
				if(objSettlmentTax.getStrApplicable()!=null ){
					objSettlmentTax.setStrApplicable("Y");
				}else{
					objSettlmentTax.setStrApplicable("N");
				}
				objSettlmentTax.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				objSettlmentTax.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
				objSettlmentTax.setStrUserCreated(userCode);
				objSettlmentTax.setStrUserEdited(userCode);
				objSettlmentTax.setDteFrom(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteValidFrom()));
				objSettlmentTax.setDteTo(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteValidTo()));
				listSetTAxModel.add(objSettlmentTax);
			}
		}
		objModel.setListSettlementTaxModels(listSetTAxModel);
		
		
		
		/*if(objBean.getListSettlement()!=null && objBean.getListSettlement().size()>0){
			for(int k=0;k<objBean.getListSettlement().size();k++){
				
				
			}
		}
		*/
		return objModel;
	}

	// get code from name
	public String funGetCodeFromName(String fieldToBeSeleted, String fromFieldName, String fromFieldNameValue, String fromTableName, String clientCode) {
		String code = null;
		code = objPMSTaxMasterService.funGetCodeFromName(fieldToBeSeleted, fromFieldName, fromFieldNameValue, fromTableName, clientCode);
		return code;
	}
	
	@RequestMapping(value = "/loadSettlementData", method = RequestMethod.GET)
	public @ResponseBody List funLoadSettlementMasterData(HttpServletRequest req) {
		List list =null;
		try{
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		
		String sqlSettlement = "select a.strSettlementCode,a.strSettlementDesc from tblsettlementmaster a where a.strClientCode='"+clientCode+"'";
		list= objGlobalFunctionsService.funGetDataList(sqlSettlement, "sql");
			
				
			}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return list;
	}

}
