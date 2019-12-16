package com.sanguine.webpms.controller;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.bean.clsSecurityShellBean;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsTreeMasterModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsCheckInDetailsBean;
import com.sanguine.webpms.bean.clsDIrtyRoomBean;
import com.sanguine.webpms.dao.clsWebPMSDBUtilityDao;

@Controller
public class clsCheckDirtyRoom {
	

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal;
	@Autowired
	private clsWebPMSDBUtilityDao objWebPMSUtility;
	
	
	@RequestMapping(value = "/frmCheckDirtyRoom", method = RequestMethod.GET)
	public ModelAndView funOpenForm(@ModelAttribute("command") @Valid clsDIrtyRoomBean objBean,BindingResult result, Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		objGlobal = new clsGlobalFunctions();
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String strRoomNo = request.getParameter("formname");
		String objData = request.getParameter("objData");
		String sqlRoomCode = "";
		List listRoomCode =null;
		String strPMSDate = request.getSession().getAttribute("PMSDate").toString();
		String strFormattedDate  = objGlobal.funGetDate("yyyy-MM-dd", strPMSDate);
		String strRoomForFolio= "";

		/*String sqlHouseKeepName = "select a.strHouseKeepCode,a.strHouseKeepName from tblhousekeepmaster a where a.strClientCode='"+clientCode+"'";
		List listHouseKeepName = objGlobalFunctionsService.funGetListModuleWise(sqlHouseKeepName, "sql");*/
		
		if(strRoomNo.startsWith("F"))
		{
			sqlRoomCode = "select a.strRoomNo from tblfoliohd a where a.strFolioNo='"+strRoomNo+"' and a.strClientCode='"+clientCode+"'";
			listRoomCode = objGlobalFunctionsService.funGetListModuleWise(sqlRoomCode, "sql");
			if(listRoomCode!=null && listRoomCode.size()>0)
			{
				String sqlRoomNo =	"select a.strRoomDesc from tblroom a where a.strRoomCode='"+listRoomCode.get(0).toString()+"' and a.strClientCode='"+clientCode+"'";
				List listRoomNoForFolio = objGlobalFunctionsService.funGetListModuleWise(sqlRoomNo, "sql");
				if(listRoomNoForFolio!=null && listRoomNoForFolio.size()>0)
				{
					strRoomForFolio = listRoomNoForFolio.get(0).toString();
				}
			}
		}
		else
		{
			sqlRoomCode = "SELECT a.strRoomCode FROM tblroom a WHERE A.strRoomDesc='"+strRoomNo+"' and a.strClientCode='"+clientCode+"'";
			listRoomCode = objGlobalFunctionsService.funGetListModuleWise(sqlRoomCode, "sql");
		}
		
		String strRoomCode = "";
		if(listRoomCode!=null && listRoomCode.size()>0)
		{
			strRoomCode = listRoomCode.get(0).toString();
		}
		
		String sqlHouseKeepName ="SELECT a.strHouseKeepCode,a.strHouseKeepName, IFNULL(b.strRoomCodeFlg,'N') "
				+ "FROM tblhousekeepmaster a "
				+ "LEFT OUTER "
				+ "JOIN tblroomhousekeepdtl b ON b.strHouseKeepCode=a.strHouseKeepCode  AND b.strClientCode='"+clientCode+"' and b.strRoomCode='"+strRoomCode+"' and date(b.dteDate)=date('"+strFormattedDate+"')  "
				+ "WHERE a.strClientCode='"+clientCode+"' ";
		List listHouseKeepName = objGlobalFunctionsService.funGetListModuleWise(sqlHouseKeepName, "sql");		
		
		List<clsDIrtyRoomBean> list = new ArrayList<clsDIrtyRoomBean>();
		objBean = new clsDIrtyRoomBean();
		
		if(listHouseKeepName!=null && listHouseKeepName.size()>0)
		{
			for(int i=0;i<listHouseKeepName.size();i++)
			{
				objBean = new clsDIrtyRoomBean();
				Object[] arrObj = (Object[]) listHouseKeepName.get(i);
				
				objBean.setStrHouseKeepCode(arrObj[0].toString());
				objBean.setStrHouseKeepName(arrObj[1].toString());
				objBean.setStrRoomCode(strRoomCode);
				if(arrObj[2].toString().equals("Y"))
				{
					objBean.setStrAdd(true);
				}
				else
				{
					objBean.setStrAdd(false);
				}
				if(!objData.equals(""))
				{
					objBean.setStrGuestName(objData);
				}
				
				list.add(objBean);
				model.put("guestName", objBean.getStrGuestName());
			}
		}
		if(!strRoomForFolio.equals(""))
		{
			model.put("roomNo", strRoomForFolio);
		}
		else
		{
			model.put("roomNo", strRoomNo);
		}
		model.put("houseKeepService", list);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmCheckDirtyRoom_1");
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmCheckDirtyRoom");
		} else {
			return new ModelAndView("frmCheckDirtyRoom");
		}
	}
	
	
	
	
	
	
	
	
	
	@RequestMapping(value = "/checkDirtyRoom", method = RequestMethod.GET)
	public ModelAndView funcheckDirtyRoom(Map<String, Object> model, @ModelAttribute("formname") String value, BindingResult result, @RequestParam(value = "formname") String formName, @RequestParam(value = "searchText") String search_with, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();

		String sqlHouseKeepName = "select a.strHouseKeepCode,a.strHouseKeepName from tblhousekeepmaster a where a.strClientCode='"+clientCode+"'";
		List listHouseKeepName = objGlobalFunctionsService.funGetListModuleWise(sqlHouseKeepName, "sql");

		
		//model.put("houseKeepService", listHouseKeepName);
		return new ModelAndView("frmCheckDirtyRoom","command", new clsDIrtyRoomBean());
	
	}
	
	
	@RequestMapping(value = "/saveCheckDirtyRoom", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsDIrtyRoomBean objBean,  HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String strModuleNo = req.getSession().getAttribute("moduleNo").toString();
		String urlHits = "1";
		String userCode = req.getSession().getAttribute("usercode").toString();

		objGlobal = new clsGlobalFunctions();
		
		String strPMSDate = req.getSession().getAttribute("PMSDate").toString();
		String strFormattedDate  = objGlobal.funGetDate("yyyy-MM-dd", strPMSDate);
		LocalTime time = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String strCurrTime = time.format(formatter);

		boolean flgCheckDirtyRoom = true;
			for (clsDIrtyRoomBean objBeanData : objBean.getListBean())
			{
				if(objBeanData.isStrAdd())
				{
					String sqlDeletePrevData = "delete from tblroomhousekeepdtl  where strRoomCode='"+objBeanData.getStrRoomCode()+"' and strRoomCodeFlg='Y' and date(dteDate) =  date('"+strPMSDate+"') and strHouseKeepCode='"+objBeanData.getStrHouseKeepCode()+"'";
					objWebPMSUtility.funExecuteUpdate(sqlDeletePrevData, "sql");
					
					String sqlInsertData = "INSERT INTO tblroomhousekeepdtl (`strHouseKeepCode`, `strRoomCode`, `strUser`,`dteDate`, `strRemarks`,`strRoomCodeFlg`, `strClientCode`) VALUES ('"+objBeanData.getStrHouseKeepCode()+"', '"+objBeanData.getStrRoomCode()+"', '"+userCode+"','"+strFormattedDate+"''" +strCurrTime+"', '"+objBeanData.getStrRemarks()+"', '"+"Y"+"','"+clientCode+"');";
					
					objWebPMSUtility.funExecuteUpdate(sqlInsertData, "sql");
					
					String sqlUpdateFlg = "update tblroom a set a.strHouseKeepingFlg='Y' where a.strRoomCode='"+objBeanData.getStrRoomCode()+"' and a.strClientCode='"+clientCode+"'";
					objWebPMSUtility.funExecuteUpdate(sqlUpdateFlg, "sql");
				}
				else
				{
					flgCheckDirtyRoom = false;
					
				}
				if(flgCheckDirtyRoom==true)
				{
					String strStatusCheck = "select a.strStatus from tblroom a where a.strRoomCode='"+objBeanData.getStrRoomCode()+"' and a.strClientCode='"+clientCode+"'";
					List listStatusCheck = objGlobalFunctionsService.funGetListModuleWise(strStatusCheck, "sql");		
					
					if(listStatusCheck.get(0).toString().equalsIgnoreCase("Occupied"))
					{
						String sqlUpdateFlg = "update tblroom a set a.strHouseKeepingFlg='Y' ,a.strStatus= 'Occupied'  where a.strRoomCode='"+objBeanData.getStrRoomCode()+"' and a.strClientCode='"+clientCode+"'";
						objWebPMSUtility.funExecuteUpdate(sqlUpdateFlg, "sql");
					}
					else if(listStatusCheck.get(0).toString().equalsIgnoreCase("Dirty"))
					{
						String sqlUpdateFlg = "update tblroom a set a.strHouseKeepingFlg='Y' ,a.strStatus= 'Free'  where a.strRoomCode='"+objBeanData.getStrRoomCode()+"' and a.strClientCode='"+clientCode+"'";
						objWebPMSUtility.funExecuteUpdate(sqlUpdateFlg, "sql");
					}
					else
					{
						
					}
				}
			}
			
		
		
		
		
			// remove comment of report form list :RP
			//req.getSession().setAttribute("success", true);
	
		return new ModelAndView("redirect:/frmRoomStatusDiary.html?saddr=" + urlHits);

	}
}
