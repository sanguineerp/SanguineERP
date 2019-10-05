package com.sanguine.webbanquets.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webbanquets.bean.clsBanquetBookingBean;
import com.sanguine.webbanquets.model.clsBanquetBookingModelHd;
import com.sanguine.webbanquets.service.clsBanquetBookingService;
import com.sanguine.webpms.bean.clsReservationBean;
import com.sanguine.webpms.model.clsPropertySetupHdModel;

@Controller
public class clsBanquetBookingController{

	@Autowired
	private clsBanquetBookingService objBanquetBookingService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	private clsGlobalFunctions objGlobal=null;

//Open BanquetBooking
	@RequestMapping(value = "/frmBanquetBooking", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request){

		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		String webStockDB=request.getSession().getAttribute("WebStockDB").toString();
		List listOfProperty = objGlobalFunctionsService.funGetList("select strPropertyName from "+webStockDB+".tblpropertymaster where strPropertyCode='" + propCode + "' ");
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		model.put("listOfProperty", listOfProperty);

		model.put("urlHits", urlHits);
	

//		clsPropertySetupHdModel objPropertySetupModel = objPropertySetupService.funGetPropertySetup(propCode, clientCode);
//		String noOfRoom = objPropertySetupModel.getStrRoomLimit();
//		
//		model.put("noOfRoom", noOfRoom);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBanquetBooking_1", "command", new clsBanquetBookingBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmBanquetBooking", "command", new clsBanquetBookingBean());
		} else {
			return null;
		
		}
		
	}
//Load Header Table Data On Form
	@RequestMapping(value = "/loadBanquetBookingHd", method = RequestMethod.POST)
	public @ResponseBody clsBanquetBookingModelHd funLoadHdData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql="";
			String clientCode=request.getSession().getAttribute("clientCode").toString();
			String userCode=request.getSession().getAttribute("userCode").toString();
			clsBanquetBookingBean objBean=new clsBanquetBookingBean();
			String docCode=request.getParameter("docCode").toString();
			List listHdModel=objGlobalFunctionsService.funGetList(sql);
			clsBanquetBookingModelHd objBanquetBooking = new clsBanquetBookingModelHd();
			return objBanquetBooking;
	}

//Load Dtl Table Data On Form
	/*@RequestMapping(value = "/loadBanquetBookingDtl", method = RequestMethod.POST)
	public @ResponseBody clsBanquetBookingDtlModelHd funLoadDtlData(HttpServletRequest request){
		objGlobal=new clsGlobalFunctions();
		String sql=""
			String clientCode=request.getSession().getAttribute("clientCode").toString();
			String userCode=request.getSession().getAttribute("userCode").toString();
		clsBanquetBookingBean objBean=new clsBanquetBookingBean();
		String docCode=req.getParameter("docCode").toString();
		List listDtlModel=objGlobalFunctionsService.funGetList(sql);
	clsBanquetBookingDtlModel objBanquetBookingDtl = new clsBanquetBookingDtlModel();
	return objBanquetBookingDtl;
	}*/

//Save or Update BanquetBooking
	@RequestMapping(value = "/saveBanquetBooking", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsBanquetBookingBean objBean ,BindingResult result,HttpServletRequest req){
		if(!result.hasErrors()){
			String clientCode=req.getSession().getAttribute("clientCode").toString();
			String userCode=req.getSession().getAttribute("userCode").toString();
			String propCode=req.getSession().getAttribute("propertyCode").toString();
			String startDate=req.getSession().getAttribute("startDate").toString();
			//clsBanquetBookingModelHd objHdModel = funPrepareHdModel(objBean,userCode,clientCode);
			//objBanquetBookingService.funAddUpdateBanquetBookingHd(objModel);
			return new ModelAndView("redirect:/frmBanquetBooking.html");
		}
		else{
			return new ModelAndView("frmBanquetBooking");
		}
	}

//Convert bean to model function
	/*private clsBanquetBookingHdModel funPrepareModel(clsBanquetBookingBean objBean,String userCode,String clientCode){
		objGlobal=new clsGlobalFunctions();
		long lastNo=0;
		clsBanquetBookingHdModel objModel;
		return objModel;

	}
*/
}
