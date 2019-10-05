package com.sanguine.webpms.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.SessionFactory;
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
import com.sanguine.webbanquets.model.clsBanquetBookingModelHd;
import com.sanguine.webpms.bean.clsRoomCancellationBean;
import com.sanguine.webpms.model.clsBookingTypeHdModel;
import com.sanguine.webpms.model.clsReservationDtlModel;
import com.sanguine.webpms.model.clsReservationHdModel;
import com.sanguine.webpms.model.clsRoomCancellationModel;
import com.sanguine.webpms.service.clsRoomCancellationService;

@Controller
public class clsRoomCancellationController {
	@Autowired
	private clsRoomCancellationService objRoomCancellationService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@Autowired
	private SessionFactory webPMSSessionFactory;

	// Open RoomCancellation
	@RequestMapping(value = "/frmRoomCancellation", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String webStockDB=request.getSession().getAttribute("WebStockDB").toString();
		List listOfProperty = objGlobalFunctionsService.funGetList("select strPropertyName from "+webStockDB+".tblpropertymaster");
		model.put("listOfProperty", listOfProperty);
		List listOfReservationType = new ArrayList<String>();
		listOfReservationType.add("All");
		model.put("listOfReservationType", listOfReservationType);

		if (urlHits.equalsIgnoreCase("1")) {
			return new ModelAndView("frmRoomCancellation", "command", new clsRoomCancellationBean());
		} else {
			return new ModelAndView("frmRoomCancellation_1", "command", new clsRoomCancellationBean());
		}
	}

	// Save or Update RoomCancellation
	@RequestMapping(value = "/saveRoomCancellation", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsRoomCancellationBean objBean, BindingResult result, HttpServletRequest req) {
		if (!result.hasErrors()) {
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String strModuleName = req.getSession().getAttribute("selectedModuleName").toString();
			if(strModuleName.equalsIgnoreCase("7-WebBanquet"))
			{
				
				
				clsRoomCancellationModel objModel = funPrepardBeanForBanquetBooking(objBean, req);
				objRoomCancellationService.funAddUpdateRoomCancellation(objModel);
				
				String strUpdate = "update tblbqbookinghd a set a.strBookingStatus='Cancel'  where a.strBookingNo='"+objBean.getStrReservationNo()+"' and a.strClientCode='"+clientCode+"'";
				objGlobalFunctionsService.funUpdateAllModule(strUpdate, "sql");
				
				req.getSession().setAttribute("success", true);
				req.getSession().setAttribute("successMessage", "Booking No. : ".concat(objBean.getStrReservationNo()));


			}
			
			else
			{
				String PMSDate = objGlobal.funGetDate("yyyy-MM-dd", req.getSession().getAttribute("PMSDate").toString());
				clsReservationHdModel objHdModel = objRoomCancellationService.funGetReservationModel(objBean.getStrReservationNo(), clientCode);
				objHdModel.setStrCancelReservation("Y");
				objHdModel.setStrUserEdited(userCode);
				objHdModel.setDteCancelDate(PMSDate);
				objHdModel.setDteDateEdited(PMSDate);

				clsRoomCancellationModel objModel = funPrepardBean(objBean, req, objHdModel);
				objRoomCancellationService.funAddUpdateRoomCancellation(objModel);

				List<clsReservationDtlModel> list = objHdModel.getListReservationDtlModel();

				objRoomCancellationService.funAddUpdateRoomCancellationReservationTable(objHdModel);
				
				String sql = "update tblroom set strStatus='Free' " + " where strRoomCode='" + objBean.getStrRoomCode()+ "' and strClientCode='" + objHdModel.getStrClientCode() + "'";
				//webPMSSessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
				objRoomCancellationService.funUpdateRoomStatus(sql);
				
				req.getSession().setAttribute("success", true);
				req.getSession().setAttribute("successMessage", "Reservation No. : ".concat(objBean.getStrReservationNo()));

			}
			
			
			
			return new ModelAndView("redirect:/frmRoomCancellation.html");
		} else {
			return new ModelAndView("frmRoomCancellation");
		}
	}

	
	private clsRoomCancellationModel funPrepardBeanForBanquetBooking(
			clsRoomCancellationBean objBean, HttpServletRequest req) {
		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
		clsRoomCancellationModel objModel = new clsRoomCancellationModel();
		List listProperty = objGlobalFunctionsService.funGetDataList("select strPropertyCode from "+webStockDB+".tblpropertymaster where strPropertyName='" + objBean.getStrPropertyCode() + "' ", "sql");
		String strPropertyCode = listProperty.get(0).toString();

		objModel.setStrReservationNo(objBean.getStrReservationNo());
		objModel.setStrGuestCode(objBean.getStrGuestCode());
		objModel.setStrPropertyCode(strPropertyCode);
		objModel.setDteArrivalFromDate(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteArrivalFromDate()));
		objModel.setDteArrivalToDate(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteArrivalToDate()));
		objModel.setDteCancelDate(objGlobal.funGetCurrentDate("yyyy-MM-dd"));
		objModel.setStrUserCreated(userCode);
		objModel.setStrUserEdited(userCode);
		objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrClientCode(clientCode);
		objModel.setStrRemarks(objBean.getStrRemarks());
		objModel.setStrReasonCode(objBean.getStrReasonCode());
		
		
		return objModel;
	}

	@RequestMapping(value = "/loadReservationDataForCheckIn", method = RequestMethod.GET)
	public @ResponseBody List funLoadHdData(HttpServletRequest request) {

		String strModuleName = request.getSession().getAttribute("selectedModuleName").toString();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		String webStockDB=request.getSession().getAttribute("WebStockDB").toString();
		List list = new ArrayList<>();
		if(strModuleName.equalsIgnoreCase("7-WebBanquet"))
		{
			String reservationNo = request.getParameter("reservationNo").toString();
			String sql="select a.strBookingNo, b.strPName,b.strPCode "
					+ "from tblbqbookinghd a ,"+webStockDB+".tblpartymaster b where a.strBookingNo='"+reservationNo+"' AND a.strCustomerCode=b.strPCode and a.strClientCode='"+clientCode+"'";
			
			list = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		}
		else
		{
			String reservationNo = request.getParameter("reservationNo").toString();
			String arrivalFromDate = request.getParameter("arrivalFromDate").toString();
			String arrivalToDate = request.getParameter("arrivalToDate").toString();

			String sql = "select a.strReservationNo,ifnull(c.strCorporateCode,'NA'),ifnull(c.strCorporateDesc,'NA' ) " + " ,concat(d.strFirstName,' ',d.strMiddleName,' ',d.strLastName),f.strRoomTypeDesc,b.strGuestCode,e.strRoomCode "
					+ " from tblreservationhd a left outer join tblreservationdtl b on a.strReservationNo=b.strReservationNo AND b.strClientCode='"+clientCode+"'" + " left outer join tblcorporatemaster c on a.strCorporateCode=c.strCorporateCode AND c.strClientCode='"+clientCode+"'" + " left outer join tblguestmaster d on b.strGuestCode=d.strGuestCode AND d.strClientCode='"+clientCode+"'"
					+ " left outer join tblroom e on b.strRoomNo=e.strRoomCode AND e.strClientCode='"+clientCode+"'"
					+ " left outer join tblroomtypemaster f on f.strRoomTypeCode=b.strRoomType " 
					+ " where a.strReservationNo='" + reservationNo + "' and a.strClientCode='" + clientCode + "' ";
			list = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		}
		

		return list;
	}

	private clsRoomCancellationModel funPrepardBean(clsRoomCancellationBean objBean, HttpServletRequest req, clsReservationHdModel objHdModel) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String webStockDB=req.getSession().getAttribute("WebStockDB").toString();
		clsRoomCancellationModel objModel = new clsRoomCancellationModel();
		List listProperty = objGlobalFunctionsService.funGetDataList("select strPropertyCode from "+webStockDB+".tblpropertymaster where strPropertyName='" + objBean.getStrPropertyCode() + "' ", "sql");
		String strPropertyCode = listProperty.get(0).toString();

		objModel.setStrReservationNo(objBean.getStrReservationNo());
		objModel.setStrGuestCode(objBean.getStrGuestCode());
		objModel.setStrPropertyCode(strPropertyCode);
		objModel.setDteArrivalFromDate(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteArrivalFromDate()));
		objModel.setDteArrivalToDate(objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteArrivalToDate()));
		objModel.setDteCancelDate(objHdModel.getDteCancelDate());
		objModel.setStrUserCreated(userCode);
		objModel.setStrUserEdited(userCode);
		objModel.setDteDateCreated(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setDteDateEdited(objGlobal.funGetCurrentDateTime("yyyy-MM-dd"));
		objModel.setStrClientCode(clientCode);
		objModel.setStrRemarks(objBean.getStrRemarks());
		objModel.setStrReasonCode(objBean.getStrReasonCode());


		return objModel;
	}
}
