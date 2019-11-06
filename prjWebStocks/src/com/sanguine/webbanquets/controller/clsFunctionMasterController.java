package com.sanguine.webbanquets.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.sanguine.bean.clsProductMasterBean;
import com.sanguine.bean.clsTaxMasterBean;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsGroupMasterModel;
import com.sanguine.model.clsGroupMasterModel_ID;
import com.sanguine.model.clsTaxSettlementMasterModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webbanquets.bean.clsFunctionMasterBean;
import com.sanguine.webbanquets.model.clsFunctionMasterModel;
import com.sanguine.webbanquets.model.clsFunctionMasterModel_ID;
import com.sanguine.webbanquets.model.clsFunctionServiceModel;
import com.sanguine.webbanquets.service.clsFunctionMasterService;
import com.sanguine.webpms.model.clsAgentMasterHdModel;
import com.sanguine.webpms.model.clsPMSSettlementTaxMasterModel;

@Controller
public class clsFunctionMasterController{

	@Autowired
	private clsFunctionMasterService objFunctionMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;
	

//Open FunctionMaster
	@RequestMapping(value = "/frmFunctionMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request){
		
		
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmFunctionMaster_1", "command", new clsFunctionMasterBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmFunctionMaster", "command", new clsFunctionMasterBean());
		} else {
			return null;
		}
		
	}
//Load Master Data On Form
	@RequestMapping(value = "/loadFunctionMasterData", method = RequestMethod.GET)
	public @ResponseBody clsFunctionMasterModel funLoadMasterData(@RequestParam("functionCode") String funCode,HttpServletRequest request){
		
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		clsFunctionMasterModel objModel=objFunctionMasterService.funGetFunctionMaster(funCode, clientCode);
		
		if (null == objModel) {
			objModel = new clsFunctionMasterModel();
			objModel.setStrFunctionCode("Invalid Code");
		}
		
		return objModel;
	}

//Save or Update FunctionMaster
	@RequestMapping(value = "/saveFunctionMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsFunctionMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("usercode").toString();
			String propertyCode=req.getSession().getAttribute("propertyCode").toString();
			clsFunctionMasterModel objModel = funPrepareModel(objBean,userCode,clientCode,propertyCode);
			objFunctionMasterService.funAddUpdateFunctionMaster(objModel);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Function Code : ".concat(objModel.getStrFunctionCode()));
			return new ModelAndView("redirect:/frmFunctionMaster.html");
		}
		else{
			return new ModelAndView("frmFunctionMaster");
		}
	}

//Convert bean to model function
	private clsFunctionMasterModel funPrepareModel(clsFunctionMasterBean objBean,String userCode,String clientCode,String propertyCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsFunctionMasterModel objFunModel;
	   
		if(objBean.getStrFunctionCode().trim().length() == 0)
		{
			lastNo=objGlobalFunctionsService.funGetLastNo("tblfunctionmaster", "FunctionMaster", "intFId", clientCode);
			String functionCode = "FM" + String.format("%06d", lastNo);
			objFunModel=new clsFunctionMasterModel(new clsFunctionMasterModel_ID(functionCode, clientCode));
			objFunModel.setIntFId(lastNo);
		    objFunModel.setStrUserCreated(userCode);
			objFunModel.setStrDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			
		}
		else
		{
			clsFunctionMasterModel objModel=objFunctionMasterService.funGetFunctionMaster(objBean.getStrFunctionCode(), clientCode);
			if (null == objModel) {
				lastNo=objGlobalFunctionsService.funGetLastNo("tblfunctionmaster", "FunctionMaster", "intFId", clientCode);
				String functionCode = "FM" + String.format("%06d", lastNo);
				objFunModel=new clsFunctionMasterModel(new clsFunctionMasterModel_ID(functionCode, clientCode));
				objFunModel.setIntFId(lastNo);
				objFunModel.setStrUserCreated(userCode);
				objFunModel.setStrDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
			} else {
				objFunModel = new clsFunctionMasterModel(new clsFunctionMasterModel_ID(objBean.getStrFunctionCode(), clientCode));
			}
			
	     }
	    
	    objFunModel.setStrClientCode(clientCode);
		objFunModel.setStrFunctionName(objBean.getStrFunctionName());
		objFunModel.setStrOperationalYN(objBean.getStrOperationalYN());
		objFunModel.setStrUserEdited(userCode);
		objFunModel.setStrDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objFunModel.setStrPropertyCode(propertyCode);
		
		
		
		
        List<clsFunctionServiceModel> listSetFunModel =new ArrayList();
		
		if(objBean.getListService()!=null && objBean.getListService().size()>0){
			for(clsFunctionServiceModel objFunService :objBean.getListService())
			{
				if(objFunService.getStrApplicable()!=null ){
					objFunService.setStrApplicable("Y");
				}else{
					objFunService.setStrApplicable("N");
				}
				listSetFunModel.add(objFunService);
				
			}
		}
		objFunModel.setListService(listSetFunModel);

		return objFunModel;
	     
	}
	
	
	@RequestMapping(value = "/loadServiceData", method = RequestMethod.GET)
	public @ResponseBody List funLoadServiceMasterData(HttpServletRequest req)
	{
		List list =null;
		try{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String 	 sql="select a.strServiceCode,a.strServiceName  from tblservicemaster a where a.strClientCode='"+clientCode+"' ";
			list= objGlobalFunctionsService.funGetDataList(sql, "sql");		
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return list;
	}

	/*@RequestMapping(value = "/loadFunctionServiceData", method = RequestMethod.GET)
	public @ResponseBody List funLoadFunctionServiceMasterData(@RequestParam("functionCode") String funCode,HttpServletRequest req)
	{
		List list =null;
		try{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String 	 sql="select a.strServiceCode,a.strServiceName,b.dblRate from tblfunctionservice a,tblservicemaster b where "
					+ " a.strServiceCode=b.strServiceCode and   a.strFunctionCode='"+funCode+"' and a.strClientCode='"+clientCode+"' ";
			list= objGlobalFunctionsService.funGetDataList(sql, "sql");		
			}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return list;
	}
*/


@RequestMapping(value = "/loadFunctionServiceData", method = RequestMethod.GET)
	public @ResponseBody List funLoadFunctionServiceMasterData(@RequestParam("functionCode") String funCode,HttpServletRequest req)
	{
		List list =null;
		try{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			
			String sql="select a.strServiceCode,a.strServiceName,if(ifnull(b.strFunctionCode,'')='','N',b.strApplicable)"
                      +" from tblservicemaster a left outer join tblfunctionservice b  on "
                      +" a.strServiceCode=b.strServiceCode "
                      +" and   b.strFunctionCode='"+funCode+"' and a.strClientCode=b.strClientCode 	";
			
			/*String sql="select a.strServiceCode,a.strServiceName,ifnull(b.strApplicable,'N') "
                      +" from tblservicemaster a left outer join tblfunctionservice b  on  "
                      +" a.strServiceCode=b.strServiceCode and b.strFunctionCode='"+funCode+"'  "
                      +" where a.strClientCode=b.strClientCode  " ;*/

			list= objGlobalFunctionsService.funGetDataList(sql, "sql");		
			
		}
		catch(Exception e)
			{
				e.printStackTrace();
			}
		return list;
	}



}
