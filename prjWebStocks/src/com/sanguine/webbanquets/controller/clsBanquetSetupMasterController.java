package com.sanguine.webbanquets.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webbanquets.bean.clsBanquetStaffCategeoryMasterBean;
import com.sanguine.webbanquets.bean.clsBanquetSetupMasterBean;
import com.sanguine.webbanquets.model.clsBanquetStaffCategeoryMasterModel;
import com.sanguine.webbanquets.model.clsBanquetWeekendMasterModel;
import com.sanguine.webbanquets.service.clsBanquetStaffCategeoryMasterService;
import com.sanguine.webbanquets.service.clsBanquetWeekendMasterService;

@Controller
public class clsBanquetSetupMasterController{

	@Autowired
	private clsBanquetWeekendMasterService objBanquetWeekendMasterService;
	@Autowired	
	private clsBanquetStaffCategeoryMasterService objBanquetStaffCategeoryMasterService;
	@Autowired
	private clsGlobalFunctions objGlobal;


//Open BanquetStaffCategeoryMaster
	@RequestMapping(value = "/frmBanquetSetup", method = RequestMethod.GET)
	public ModelAndView funOpenForm(HttpServletRequest request){
		clsBanquetWeekendMasterModel objModel = new clsBanquetWeekendMasterModel();
		clsBanquetSetupMasterBean objBean = new clsBanquetSetupMasterBean();
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String usercode=request.getSession().getAttribute("usercode").toString();
		objBean=objBanquetWeekendMasterService.funGetWeekendMaster(clientCode);	
		return new ModelAndView("frmBanquetSetup","command", objBean);
	}
//Load Master Data On Form
	@RequestMapping(value = "/frmBanquetSetup1", method = RequestMethod.POST)
	public @ResponseBody clsBanquetSetupMasterBean funLoadMasterData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql="";
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String userCode=request.getSession().getAttribute("usercode").toString();
		clsBanquetStaffCategeoryMasterBean objBean=new clsBanquetStaffCategeoryMasterBean();
		String docCode=request.getParameter("docCode").toString();
		//List listModel=objGlobalFunctionsService.funGetList(sql);
		clsBanquetSetupMasterBean objBanquetStaffCategeoryMaster = new clsBanquetSetupMasterBean();
		return objBanquetStaffCategeoryMaster;
	}

//Save or Update BanquetStaffCategeoryMaster
	@RequestMapping(value = "/saveBanquetSetupMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsBanquetSetupMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String usercode=req.getSession().getAttribute("usercode").toString();
			objBanquetWeekendMasterService.funDeleteWeekendMaster(clientCode);
			funPrepareModel(objBean,usercode,clientCode);
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Updated Successfully");
			return new ModelAndView("redirect:/frmBanquetSetup.html");
		}
		else{
			return new ModelAndView("frmBanquetSetup");
		}
	}
	
		
	/*@RequestMapping(value = "/loadStaffCategeoryMasterData", method = RequestMethod.GET)
	public @ResponseBody clsBanquetStaffCategeoryMasterModel funAssignFields(@RequestParam("staffCatCode") String staffCatCode, HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode=request.getSession().getAttribute("usercode").toString();
		clsBanquetStaffCategeoryMasterModel objModel = objBanquetStaffCategeoryMasterService.funGetBanquetStaffCategeoryMaster(staffCatCode, clientCode);
		if (null == objModel) {
			objModel = new clsBanquetStaffCategeoryMasterModel();
			//objLocCode.setStrLocCode("Invalid Code");
		}

		return objModel;
	}*/
		
	private void funPrepareModel(clsBanquetSetupMasterBean objBean,String usercode,String clientCode){
		clsBanquetWeekendMasterModel objModel = new clsBanquetWeekendMasterModel();
		String strAllDayData="";
		//weekend master tab 
		if(objBean.getStrSunday()!=null)			//1
		{		
			strAllDayData=objBean.getStrSunday();
		}	
		if(objBean.getStrMonday()!=null)		//2
		{
			if(strAllDayData.length()>0)
			{
				strAllDayData+="!"+objBean.getStrMonday();
			}
			else
			{
				strAllDayData=objBean.getStrMonday();
			}
		}		
		if(objBean.getStrTuesday()!=null)		//3	
		{
			if(strAllDayData.length()>0)
			{
				strAllDayData+="!"+objBean.getStrTuesday();
			}
			else
			{
				strAllDayData=objBean.getStrTuesday();
			}			
		}
		
		if(objBean.getStrWednesday()!=null)		//4
		{
			if(strAllDayData.length()>0)
			{
				strAllDayData+="!"+objBean.getStrWednesday();
			}
			else
			{
				strAllDayData=objBean.getStrWednesday();
			}			
		}		
		if(objBean.getStrThursday()!=null)		//5
		{
			if(strAllDayData.length()>0)
			{
				strAllDayData+="!"+objBean.getStrThursday();
			}
			else
			{
				strAllDayData=objBean.getStrThursday();
			}
		}		
		if(objBean.getStrFriday()!=null)		//6
		{
			if(strAllDayData.length()>0)
			{
				strAllDayData+="!"+objBean.getStrFriday();
			}
			else
			{
				strAllDayData=objBean.getStrFriday();
			}
		}		
		if(objBean.getStrSaturday()!=null)		//7
		{
			if(strAllDayData.length()>0)
			{
				strAllDayData+="!"+objBean.getStrSaturday();
			}
			else
			{
				strAllDayData=objBean.getStrSaturday();
			}
		}					
		String list[] = strAllDayData.split("!");			
		for(int i=0;i<list.length;i++)
		{
			objModel.setStrDayNo(String.valueOf(i+1));
			objModel.setStrDay(list[i]);
			objModel.setStrClientCode(clientCode);
			objModel.setStrUserCreated(usercode);
			objModel.setStrUserEdited(usercode);
			objModel.setDtDteCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));		
			objModel.setDtDteEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));		
					
			objBanquetWeekendMasterService.funAddUpdateBanquetWeekendMaster(objModel);
			
		}


	}

}

	
	
	
	
	
	
	
	
	

