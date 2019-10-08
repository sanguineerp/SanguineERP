package com.sanguine.webbanquets.controller;

import java.util.List;

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
import com.sanguine.webbanquets.bean.clsBanquetWeekendMasterBean;
import com.sanguine.webbanquets.model.clsBanquetWeekendMasterModel;
import com.sanguine.webbanquets.service.clsBanquetWeekendMasterService;

@Controller
public class clsBanquetWeekendMasterController{

	@Autowired
	private clsBanquetWeekendMasterService objBanquetWeekendMasterService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;


//Open BanquetWeekendMaster
	@RequestMapping(value = "/frmBanquetWeekendMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(HttpServletRequest request){
		clsBanquetWeekendMasterModel objModel = new clsBanquetWeekendMasterModel();
		clsBanquetWeekendMasterBean objBean = new clsBanquetWeekendMasterBean();
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String usercode=request.getSession().getAttribute("usercode").toString();
		objBean=objBanquetWeekendMasterService.funGetWeekendMaster(clientCode);
		
		
		return new ModelAndView("frmBanquetWeekendMaster","command", objBean);
	}
//Load Master Data On Form
	@RequestMapping(value = "/frmBanquetWeekendMaster1", method = RequestMethod.POST)
	public @ResponseBody clsBanquetWeekendMasterModel funLoadMasterData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql="";
		String clientCode=request.getSession().getAttribute("clientCode").toString();
		String usercode=request.getSession().getAttribute("usercode").toString();
		clsBanquetWeekendMasterBean objBean=new clsBanquetWeekendMasterBean();
		String docCode=request.getParameter("docCode").toString();
		List listModel=objGlobalFunctionsService.funGetList(sql);
		clsBanquetWeekendMasterModel objBanquetWeekendMaster = new clsBanquetWeekendMasterModel();
		return objBanquetWeekendMaster;
	}

//Save or Update BanquetWeekendMaster
	@RequestMapping(value = "/saveBanquetWeekendMaster", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsBanquetWeekendMasterBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String usercode=req.getSession().getAttribute("usercode").toString();
			objBanquetWeekendMasterService.funDeleteWeekendMaster(clientCode);
			funPrepareModel(objBean,usercode,clientCode);			
			return new ModelAndView("redirect:/frmBanquetWeekendMaster.html");
		}
		else{
			return new ModelAndView("frmBanquetWeekendMaster");
		}
	}

//Convert bean to model function
	private clsBanquetWeekendMasterModel funPrepareModel(clsBanquetWeekendMasterBean objBean,String usercode,String clientCode){
		clsBanquetWeekendMasterModel objModel = new clsBanquetWeekendMasterModel();
		String strAllDayData="";
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
		return objModel;

	}

}
