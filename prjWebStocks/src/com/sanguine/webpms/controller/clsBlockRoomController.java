package com.sanguine.webpms.controller;

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
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsBlockRoomBean;
import com.sanguine.webpms.bean.clsRoomMasterBean;
import com.sanguine.webpms.dao.clsWebPMSDBUtilityDao;
import com.sanguine.webpms.model.clsBlockRoomModel;
import com.sanguine.webpms.model.clsRoomMasterModel;
import com.sanguine.webpms.service.clsBlockRoomMasterService;
import com.sanguine.webpms.service.clsRoomMasterService;

@Controller
public class clsBlockRoomController {
	
	@Autowired
	private clsBlockRoomMasterService objBlockRoomMasterService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@Autowired
	private clsWebPMSDBUtilityDao objWebPMSUtility;
	
	@RequestMapping(value = "/frmBlockRoomMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		String clientCode = request.getSession().getAttribute("clientCode").toString();
		List<String> listRoomType = objBlockRoomMasterService.funGetRoomTypeList(clientCode);
		model.put("listRoomType", listRoomType);
		

		if (urlHits.equalsIgnoreCase("1")) {
			return new ModelAndView("frmBlockRoomMaster", "command", new clsBlockRoomBean());
		} else {
			return new ModelAndView("frmBlockRoomMaster_1", "command", new clsBlockRoomBean());
		}
	}
	
	
	@RequestMapping(value = "/saveBlockRoom", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsBlockRoomBean objBean, BindingResult result, HttpServletRequest req) {
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
			clsBlockRoomModel objModel = funPrepareModel(objBean, userCode, clientCode, propertyCode);
			objBlockRoomMasterService.funAddUpdateRoomMaster(objModel);
			
			String sqlBlock = "UPDATE tblroom a SET a.strStatus='Blocked' WHERE a.strRoomCode='"+objBean.getStrRoomCode()+"' AND a.strClientCode='"+clientCode+"'";
			objWebPMSUtility.funExecuteUpdate(sqlBlock, "sql"); 
			
			
			req.getSession().setAttribute("success", true);
//			req.getSession().setAttribute("successMessage", "Room Code : ".concat(objModel.getStrRoomCode()));

			return new ModelAndView("redirect:/frmBlockRoomMaster.html?saddr=" + urlHits);
		} else {
			return new ModelAndView("redirect:/frmBlockRoomMaster.html?saddr=" + urlHits);
		}
	}
	
	private clsBlockRoomModel funPrepareModel(clsBlockRoomBean objBean, String userCode, String clientCode, String propertyCode)
	{
		clsBlockRoomModel objModel;
		
		objModel = new clsBlockRoomModel(objBean.getStrRoomCode(),clientCode);
		
		objModel.setStrRoomCode(objBean.getStrRoomCode());
		objModel.setStrRoomType(objBean.getStrRoomType());
		objModel.setStrRemarks(objBean.getStrRemarks());
		objModel.setStrReason(objBean.getStrReason());
		objModel.setDteValidFrom(objGlobal.funGetDate("yyyy-MM-dd",objBean.getDteValidFrom()));
		objModel.setDteValidTo(objGlobal.funGetDate("yyyy-MM-dd",objBean.getDteValidTo()));
		objModel.setStrClientCode(clientCode);
		objModel.setDteValidFrom(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteValidFrom()));
		
		return objModel;
		
	}


}
