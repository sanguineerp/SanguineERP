package com.sanguine.webpms.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
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

import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsCompanyMasterModel;
import com.sanguine.model.clsPropertyMaster;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsPropertyMasterService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webpms.bean.clsCheckInBean;
import com.sanguine.webpms.bean.clsCheckInDetailsBean;
import com.sanguine.webpms.bean.clsFolioHdBean;
import com.sanguine.webpms.dao.clsExtraBedMasterDao;
import com.sanguine.webpms.dao.clsGuestMasterDao;
import com.sanguine.webpms.dao.clsWalkinDao;
import com.sanguine.webpms.dao.clsWebPMSDBUtilityDao;
import com.sanguine.webpms.model.clsCheckInDtl;
import com.sanguine.webpms.model.clsCheckInHdModel;
import com.sanguine.webpms.model.clsExtraBedMasterModel;
import com.sanguine.webpms.model.clsFolioDtlModel;
import com.sanguine.webpms.model.clsFolioHdModel;
import com.sanguine.webpms.model.clsGuestMasterHdModel;
import com.sanguine.webpms.model.clsPackageMasterDtl;
import com.sanguine.webpms.model.clsPackageMasterHdModel;
import com.sanguine.webpms.model.clsPropertySetupHdModel;
import com.sanguine.webpms.model.clsReservationHdModel;
import com.sanguine.webpms.model.clsReservationRoomRateModelDtl;
import com.sanguine.webpms.model.clsRoomMasterModel;
import com.sanguine.webpms.model.clsRoomPackageDtl;
import com.sanguine.webpms.model.clsWalkinHdModel;
import com.sanguine.webpms.model.clsWalkinRoomRateDtlModel;
import com.sanguine.webpms.service.clsCheckInService;
import com.sanguine.webpms.service.clsFolioService;
import com.sanguine.webpms.service.clsGuestMasterService;
import com.sanguine.webpms.service.clsPropertySetupService;
import com.sanguine.webpms.service.clsReservationService;
import com.sanguine.webpms.service.clsRoomMasterService;
@Controller
public class clsAddExtraBedController {
	
	@Autowired
	private clsCheckInService objCheckInService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;

	@Autowired
	private clsGuestMasterService objGuestMasterService;

	@Autowired
	private clsGuestMasterDao objGuestMasterDao;

	@Autowired
	private clsFolioController objFolioController;

	@Autowired
	private clsFolioService objFolioService;

	@Autowired
	private clsExtraBedMasterDao objExtraBedMasterDao;

	@Autowired
	private clsPropertySetupService objPropertySetupService;

	@Autowired
	private clsGuestMasterService objGuestService;

	@Autowired
	private clsPropertyMasterService objPropertyMasterService;

	@Autowired
	clsRoomMasterService objRoomMaster;
	
	@Autowired
	clsPMSUtilityFunctions objPMSUtility;
	
	@Autowired 
	clsReservationService objReservationService;
	
	@Autowired
	private clsWebPMSDBUtilityDao objWebPMSUtility;

	@Autowired
	private intfBaseService objBaseService;
	
	
	@Autowired
	private clsWalkinDao objWalkinDao;
	
	@Autowired
	private ServletContext servletContext;

	@Autowired
	private clsSetupMasterService objSetupMasterService;
	

	@Autowired
	private clsGlobalFunctionsService objGlobalFunService;
	
	
	@RequestMapping(value = "/frmAddExtraBed", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmAddExtraBed_1", "command", new clsCheckInBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmAddExtraBed", "command", new clsCheckInBean());
		} else {
			return null;
		}
	}
	
	
	@RequestMapping(value = "/frmAddExtraBed1", method = RequestMethod.GET)
	public ModelAndView funOpenForm1(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		String checkInNo = request.getParameter("docCode").toString();

		try {
			urlHits = request.getParameter("saddr").toString();

		} catch (NullPointerException e) {
			urlHits = "1";
		}

		model.put("urlHits", urlHits);

		request.getSession().setAttribute("checkInNo", checkInNo);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmAddExtraBed_1", "command", new clsCheckInBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmAddExtraBed", "command", new clsCheckInBean());
		} else {
			return null;
		}
	}
	
	
	@RequestMapping(value = "/loadExtraBedData", method = RequestMethod.GET)
	public @ResponseBody clsCheckInBean funLoadHdData(HttpServletRequest request) {

		String sql = "";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		clsCheckInBean objBean = new clsCheckInBean();
		String docCode = request.getParameter("docCode").toString();
		clsCheckInHdModel objCheckIn = objCheckInService.funGetCheckInData(docCode, clientCode);
		objBean.setStrCheckInNo(objCheckIn.getStrCheckInNo());
		objBean.setStrRegistrationNo(objCheckIn.getStrRegistrationNo());
		objBean.setStrType(objCheckIn.getStrType());
		if (objCheckIn.getStrType().equals("Reservation")) {
			objBean.setStrAgainstDocNo(objCheckIn.getStrReservationNo());
		} else {
			objBean.setStrAgainstDocNo(objCheckIn.getStrWalkInNo());
		}
		objBean.setDteArrivalDate(objGlobal.funGetDate("yyyy/MM/dd", objCheckIn.getDteArrivalDate()));
		objBean.setDteDepartureDate(objGlobal.funGetDate("yyyy/MM/dd", objCheckIn.getDteDepartureDate()));
		objBean.setTmeArrivalTime(objCheckIn.getTmeArrivalTime());
		objBean.setTmeDepartureTime(objCheckIn.getTmeDepartureTime());

		sql = "select a.strRoomDesc,b.strRoomTypeDesc from tblroom a,tblroomtypemaster b " + " where a.strRoomTypeCode=b.strRoomTypeCode and a.strRoomCode='" + objCheckIn.getStrRoomNo() + "' ";
		List listRoomData = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");

		if (!listRoomData.isEmpty()) {
			Object[] arrObjRoomData = (Object[]) listRoomData.get(0);
			objBean.setStrRoomNo(objCheckIn.getStrRoomNo());
			objBean.setStrRoomDesc(arrObjRoomData[0].toString());
		} else {
			objBean.setStrRoomNo("");
			objBean.setStrRoomDesc("");
		}

		if (!objCheckIn.getStrExtraBedCode().equals("")) {
			List listExtraBedData = objExtraBedMasterDao.funGetExtraBedMaster(objCheckIn.getStrExtraBedCode(), clientCode);
			clsExtraBedMasterModel objExtraBedMasterModel = (clsExtraBedMasterModel) listExtraBedData.get(0);
			objBean.setStrExtraBedCode(objCheckIn.getStrExtraBedCode());
			objBean.setStrExtraBedDesc(objExtraBedMasterModel.getStrExtraBedTypeDesc());
		} else {
			objBean.setStrExtraBedCode("");
			objBean.setStrExtraBedDesc("");
		}
		objBean.setIntNoOfAdults(objCheckIn.getIntNoOfAdults());
		objBean.setIntNoOfChild(objCheckIn.getIntNoOfChild());
		objBean.setStrNoPostFolio(objCheckIn.getStrNoPostFolio());
		
		List<clsCheckInDetailsBean> listCheckInDtlBean = new ArrayList<clsCheckInDetailsBean>();
		for (clsCheckInDtl objCheckInDtlModel : objCheckIn.getListCheckInDtl()) {
			clsCheckInDetailsBean objCheckInDtlBean = new clsCheckInDetailsBean();
			objCheckInDtlBean.setStrGuestCode(objCheckInDtlModel.getStrGuestCode());

			sql = "select strFirstName,strMiddleName,strLastName,lngMobileNo from tblguestmaster " + " where strGuestCode='" + objCheckInDtlModel.getStrGuestCode() + "' and strClientCode='" + clientCode + "' ";
			List listGuestMaster = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");

			// listGuestMaster.forEach(obj-> {});

			for (int cnt = 0; cnt < listGuestMaster.size(); cnt++) {
				Object[] arrObjGuest = (Object[]) listGuestMaster.get(cnt);
				String guestName = arrObjGuest[0] + " " + arrObjGuest[1] + " " + arrObjGuest[2];
				objCheckInDtlBean.setStrGuestName(guestName);
				objCheckInDtlBean.setLngMobileNo(Long.parseLong(arrObjGuest[3].toString()));
			}

			sql = "select a.strRoomDesc,b.strRoomTypeDesc,b.strRoomTypeCode from tblroom a,tblroomtypemaster b " + " where a.strRoomTypeCode=b.strRoomTypeCode and a.strRoomCode='" + objCheckInDtlModel.getStrRoomNo() + "' ";
			List listRoomData1 = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			Object[] arrObjRoomData1 = (Object[]) listRoomData1.get(0);
			objCheckInDtlBean.setStrRoomNo(objCheckInDtlModel.getStrRoomNo());
			objCheckInDtlBean.setStrRoomDesc(arrObjRoomData1[0].toString());

			if (!objCheckInDtlModel.getStrExtraBedCode().equals("")) {
				List listExtraBedData = objExtraBedMasterDao.funGetExtraBedMaster(objCheckInDtlModel.getStrExtraBedCode(), clientCode);
				clsExtraBedMasterModel objExtraBedMasterModel = (clsExtraBedMasterModel) listExtraBedData.get(0);
				objCheckInDtlBean.setStrExtraBedCode(objCheckInDtlModel.getStrExtraBedCode());
				objCheckInDtlBean.setStrExtraBedDesc(objExtraBedMasterModel.getStrExtraBedTypeDesc());
			} else {
				objCheckInDtlBean.setStrExtraBedCode("");
				objCheckInDtlBean.setStrExtraBedDesc("");
			}
			objCheckInDtlBean.setStrPayee(objCheckInDtlModel.getStrPayee());
			objCheckInDtlBean.setStrRoomType(objCheckInDtlModel.getStrRoomType());
			listCheckInDtlBean.add(objCheckInDtlBean);
		}
		objBean.setListCheckInDetailsBean(listCheckInDtlBean);
		
		
		if (objCheckIn.getStrType().equals("Reservation")) 
		{
			clsReservationHdModel objReservationModel = objReservationService.funGetReservationList(objBean.getStrAgainstDocNo(), clientCode, propCode);
			objBean.setlistReservationRoomRateDtl(objReservationModel.getListReservationRoomRateDtl());
		}
		else
		{
			List listWalkinData = objWalkinDao.funGetWalkinDataDtl(objBean.getStrAgainstDocNo(), clientCode);
			clsWalkinHdModel objWalkinHdModel = (clsWalkinHdModel) listWalkinData.get(0);
			objBean.setListWalkinRoomRateDtl(objWalkinHdModel.getListWalkinRoomRateDtlModel());
		}
		objBean.setListRoomPackageDtl(objCheckInService.funGetCheckInIncomeList(docCode, clientCode));
		for (clsRoomPackageDtl objPkgDtlModel : objBean.getListRoomPackageDtl()) 
		{
			objBean.setStrPackageCode(objPkgDtlModel.getStrPackageCode());
			objBean.setStrPackageName(objPkgDtlModel.getStrPackageName());
			break;
		}

		return objBean;
	}

	
	@RequestMapping(value = "/saveExtraBed", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsCheckInBean objBean, BindingResult result, HttpServletRequest req) {
		if (!result.hasErrors()) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propCode = req.getSession().getAttribute("propertyCode").toString();
			String startDate = req.getSession().getAttribute("startDate").toString();
			String PMSDate = objGlobal.funGetDate("yyyy-MM-dd", req.getSession().getAttribute("PMSDate").toString());
 
			
			
			String sqlFolioNo = "select a.strFolioNo from tblfoliohd a where a.strCheckInNo='"+objBean.getStrCheckInNo()+"'";
			List listFolioData1 = objGlobalFunctionsService.funGetListModuleWise(sqlFolioNo, "sql");
			if(listFolioData1.size()>0)
			{
				String folioNo = listFolioData1.get(0).toString();
				List list = (List)objBean.getListCheckInDetailsBean();
				for(int i=0;i<list.size();i++)
				{
					clsCheckInDetailsBean  obj=(clsCheckInDetailsBean )list.get(i);
					String strExtraBed= obj.getStrExtraBedCode();
					
					String sqlAddExtraBed = "update tblfoliohd a set a.strExtraBedCode='"+strExtraBed+"'  where a.strFolioNo='"+folioNo+"' ";
					
					objWebPMSUtility.funExecuteUpdate(sqlAddExtraBed, "sql");
				}
				
			
			}
			
			
			
			req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Check In No : ".concat(objBean.getStrCheckInNo()));
			return new ModelAndView("redirect:/frmAddExtraBed.html");
		} else {
			return new ModelAndView("frmAddExtraBed");
		}
	}
	
}
