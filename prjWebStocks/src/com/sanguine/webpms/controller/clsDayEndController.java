package com.sanguine.webpms.controller;

import net.sf.jasperreports.engine.JRException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ibm.icu.text.SimpleDateFormat;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.controller.clsSendEmailController;
import com.sanguine.model.clsCompanyMasterModel;
import com.sanguine.model.clsPropertyMaster;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsPropertyMasterService;
import com.sanguine.webpms.bean.clsDayEndBean;
import com.sanguine.webpms.bean.clsPostRoomTerrifBean;
import com.sanguine.webpms.dao.clsWebPMSDBUtilityDao;
import com.sanguine.webpms.model.clsDayEndHdModel;
import com.sanguine.webpms.model.clsGuestMasterHdModel;
import com.sanguine.webpms.model.clsPropertySetupHdModel;
import com.sanguine.webpms.model.clsReservationDtlModel;
import com.sanguine.webpms.model.clsReservationHdModel;
import com.sanguine.webpms.model.clsRoomMasterModel;
import com.sanguine.webpms.service.clsDayEndService;
import com.sanguine.webpms.service.clsGuestMasterService;
import com.sanguine.webpms.service.clsPropertySetupService;
import com.sanguine.webpms.service.clsReservationService;
import com.sanguine.webpms.service.clsRoomMasterService;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@Controller
public class clsDayEndController {

	@Autowired
	private clsDayEndService objDayEndService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;

	@Autowired
	clsPostRoomTerrifController objPostRoomTerrif;
	
	@Autowired
	private clsWebPMSDBUtilityDao objWebPMSUtility;
	
	@Autowired
	private clsGuestMasterService objGuestService;
	
	@Autowired
	private clsPropertySetupService objPropertySetupService;
	
	@Autowired
	private clsReservationService objReservationService;
	
	@Autowired
	clsRoomMasterService objRoomMaster;
	
	@Autowired
	private clsPropertyMasterService objPropertyMasterService;
	
	@Autowired
	private clsSendEmailController objSendEmail;

	// Open DayEnd
	@RequestMapping(value = "/frmDayEnd", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		Date dt = new Date();
		String PMSDate = request.getSession().getAttribute("PMSDate").toString();
		// String
		// PMSStartDay=request.getSession().getAttribute("PMSStartDay").toString();
		model.put("PMSDate", objGlobal.funGetDate("yyyy-MM-dd", PMSDate));
		model.put("POSData", "");
		model.put("POSDayStart", "");
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmDayEnd_1", "command", new clsDayEndHdModel());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmDayEnd", "command", new clsDayEndHdModel());
		} else {
			return null;
		}
	}

	// Save or Update DayEnd
	@RequestMapping(value = "/dayEndProcess", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsDayEndBean objBean, BindingResult result, HttpServletRequest req)
	{
		ModelAndView model=new ModelAndView("frmDayEnd");
		
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String PMSDate = req.getSession().getAttribute("PMSDate").toString();
		String sqlStart="";
		String[] newDate=PMSDate.split("-");
		String date=newDate[2]+"-"+newDate[1]+"-"+newDate[0];
		String propCode = req.getSession().getAttribute("propertyCode").toString();
		String strTransactionType = "Day End";
		// Check POS Day End Table in PMS
		
		String[] arrSpDate = objBean.getDtePMSDate().split("-");
		// Date dtNextDate=new
		// Date(Integer.parseInt(arrSpDate[2]),Integer.parseInt(arrSpDate[1]),Integer.parseInt(arrSpDate[0]));
		//Date dtNextDate = new Date(Integer.parseInt(arrSpDate[2]), Integer.parseInt(arrSpDate[1]), Integer.parseInt(arrSpDate[0]));
		Date dtNextDate = new Date(Integer.parseInt(arrSpDate[0]), Integer.parseInt(arrSpDate[1]), Integer.parseInt(arrSpDate[2]));
		
		String strNewDate="";
		GregorianCalendar cal = new GregorianCalendar();
		try {
			strNewDate = getNextDate(objBean.getDtePMSDate());
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} 
		cal.setTime(dtNextDate);
		cal.add(Calendar.DATE, 1);
		/*String newStartDate = cal.getTime().getYear()+ "-" + (cal.getTime().getMonth()) + "-" + (cal.getTime().getDate());*/

		try {
			funSendCheckInMail(strNewDate,clientCode,propCode,req);
		} catch (JRException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String sqlBlockedRoom = "select a.strRoomCode,DATE(a.dteValidTo) from tblblockroom a where a.strClientCode='"+clientCode+"'";
		List listOfBlockedRoom = objGlobalFunctionsService.funGetListModuleWise(sqlBlockedRoom, "sql");
		{
			if(listOfBlockedRoom.size()>0)
			{
				for(int i=0;i<listOfBlockedRoom.size();i++)
				{
					Object[] arrObjBlock = (Object[]) listOfBlockedRoom.get(i);
					String roomCode = arrObjBlock[0].toString();
					String dteValidTo = objGlobal.funGetDate("yyyy-MM-dd",arrObjBlock[1].toString());
					if(PMSDate.equalsIgnoreCase(dteValidTo))
					{
						String sqlBlock = "UPDATE tblroom a SET a.strStatus='Free' WHERE a.strRoomCode='"+roomCode+"' AND a.strClientCode='"+clientCode+"'";
						objWebPMSUtility.funExecuteUpdate(sqlBlock, "sql"); 
						
					}
					
				}
			}
			else
			{
				
			}
		}
		sqlStart=" SELECT a.strPOSCode,a.strPOSName FROM tblposdayend a WHERE a.strStatus='N' AND DATE(a.dteDayEndDate)='"+date+"' AND a.strClientCode='"+clientCode+"' ";
		List listOfPOS = objGlobalFunctionsService.funGetListModuleWise(sqlStart, "sql");
		if(listOfPOS.size()>0)
		{
			String posName="No Data Found";
			for(int index=0;index<listOfPOS.size();index++)
			{
				try
				{
					JSONObject json = new JSONObject(); 
					Object[] obj = (Object[]) listOfPOS.get(index);
					posName=obj[1].toString()+" POS Day End Not Done.. ";
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			model.addObject("POSData",posName);
		}
		/*else if(listOfPOS.size()==0)
		{
			model.addObject("POSDayStart","POS Data Not Posted !!");
		}*/
		else
		{
			String userCode = req.getSession().getAttribute("usercode").toString();
			
			String startDate = req.getSession().getAttribute("startDate").toString();
			List<String> listRoomTerrifDocNo = new ArrayList<String>();
			/*String sql = "select a.strFolioNo,a.strRoomNo,c.dblRoomTerrif,a.strExtraBedCode,ifnull(a.strReservationNo,''),ifnull(a.strWalkInNo,''),c.strRoomTypeCode " 
					   + " from tblfoliohd a,tblroom b,tblroomtypemaster c " 
					   + " where a.strRoomNo=b.strRoomCode and b.strRoomTypeCode=c.strRoomTypeCode";
			*/
			/*String sql= "SELECT a.strFolioNo,a.strRoomNo,c.dblRoomTerrif,a.strExtraBedCode, IFNULL(a.strReservationNo,''), "
					+ " IFNULL(a.strWalkInNo,''),c.strRoomTypeCode,ifnull(sum(d.dblIncomeHeadAmt),0)"
					+ " FROM tblfoliohd a left outer join tblroompackagedtl d on a.strCheckInNo=d.strCheckInNo,tblroom b,tblroomtypemaster c "
					+ " WHERE a.strRoomNo=b.strRoomCode AND b.strRoomTypeCode=c.strRoomTypeCode"
					+ " group by a.strFolioNo";*/
			
			
			
			
			String sql="SELECT a.strFolioNo,a.strRoomNo,c.dblRoomTerrif,a.strExtraBedCode, "
					+ "IFNULL(a.strReservationNo,''), IFNULL(a.strWalkInNo,''),c.strRoomTypeCode,"
					+ "IFNULL(e.strComplimentry,'N') "
					+ "FROM tblfoliohd a ,tblroom b,tblroomtypemaster c,tblcheckinhd e "
					+ "WHERE a.strRoomNo=b.strRoomCode AND b.strRoomTypeCode=c.strRoomTypeCode AND a.strCheckInNo=e.strCheckInNo AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' AND c.strClientCode='"+clientCode+"' AND e.strClientCode='"+clientCode+"'"
					+ "GROUP BY a.strFolioNo";
			List listRoomInfo = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
             
			for (int cnt = 0; cnt < listRoomInfo.size(); cnt++) 
			{
				clsPostRoomTerrifBean objPostRoomTerrifBean = new clsPostRoomTerrifBean();
				Object[] arrObjRoom = (Object[]) listRoomInfo.get(cnt);
				double dblRoomRate=0.0;
				
				String sqlFolioENtryCheck = "select * from tblfoliodtl a where a.strFolioNo='"+arrObjRoom[0].toString()+"' and date(a.dteDocDate)='"+objGlobal.funGetDate("yyyy-MM-dd", PMSDate)+"' AND a.strDocNo like 'RM%' and a.strClientCode='"+clientCode+"'";
				List listFolioCHeck = objGlobalFunctionsService.funGetListModuleWise(sqlFolioENtryCheck, "sql");
				if(listFolioCHeck!=null && listFolioCHeck.size()>0)
				{
					String sqlUpdateDepartureDate = "update tblcheckinhd a set a.dteDepartureDate='"+strNewDate+"' where a.strClientCode='"+clientCode+"' AND a.strWalkInNo='"+arrObjRoom[5].toString()+"' ";
					objWebPMSUtility.funExecuteUpdate(sqlUpdateDepartureDate, "sql");
				}
				else
				{
				if(!arrObjRoom[4].toString().equals(""))
				{
					 String sqlRoomRate=" select a.dblRoomRate from  tblreservationroomratedtl a "
						        +" where a.strReservationNo='"+arrObjRoom[4].toString()+"' and a.strClientCode='"+clientCode+"' and a.strRoomType='"+arrObjRoom[6].toString()+"' and a.dtDate='"+date+"' ";
					 List listRoomRate = objGlobalFunctionsService.funGetListModuleWise(sqlRoomRate, "sql");
					 
					 	String sqlUpdateDepartureDate = "update tblcheckinhd a set a.dteDepartureDate='"+strNewDate+"' where a.strClientCode='"+clientCode+"' AND a.strWalkInNo='"+arrObjRoom[5].toString()+"' ";
						objWebPMSUtility.funExecuteUpdate(sqlUpdateDepartureDate, "sql"); 

					 
					 if(listRoomRate.size()>0)
					 {
						 dblRoomRate=Double.parseDouble(listRoomRate.get(0).toString());
					 }
					 else
					 {
						  sqlRoomRate=" select a.dblRoomRate from  tblreservationroomratedtl a "
							        +" where a.strReservationNo='"+arrObjRoom[4].toString()+"' and a.strClientCode='"+clientCode+"' and a.strRoomType='"+arrObjRoom[6].toString()+"' order by date(a.dtDate) desc ";
						  listRoomRate = objGlobalFunctionsService.funGetListModuleWise(sqlRoomRate, "sql");
						 if(listRoomRate.size()>0)
						 {
							 dblRoomRate=Double.parseDouble(listRoomRate.get(0).toString());
						 }
					 }
				}
				if(!arrObjRoom[5].toString().equals(""))
				{
					String sqlRoomRate=" select a.dblRoomRate from  tblwalkinroomratedtl a "
						        +" where a.strWalkinNo='"+arrObjRoom[5].toString()+"' and a.strClientCode='"+clientCode+"' and a.strRoomType='"+arrObjRoom[6].toString()+"' and a.dtDate='"+date+"' ";
					 List listRoomRate = objGlobalFunctionsService.funGetListModuleWise(sqlRoomRate, "sql");
					 
					 	String sqlUpdateDepartureDate = "update tblcheckinhd a set a.dteDepartureDate='"+strNewDate+"' where a.strClientCode='"+clientCode+"' AND a.strWalkInNo='"+arrObjRoom[5].toString()+"' ";
						objWebPMSUtility.funExecuteUpdate(sqlUpdateDepartureDate, "sql"); 

					 
					 if(listRoomRate.size()>0)
					 {
					 dblRoomRate=Double.parseDouble(listRoomRate.get(0).toString());
					 }
					 else
					 {
						sqlRoomRate=" select a.dblRoomRate from  tblwalkinroomratedtl a "
							        +" where a.strWalkinNo='"+arrObjRoom[5].toString()+"' and a.strClientCode='"+clientCode+"' and a.strRoomType='"+arrObjRoom[6].toString()+"'  order by date(a.dtDate) desc ";
						  listRoomRate = objGlobalFunctionsService.funGetListModuleWise(sqlRoomRate, "sql");
						 if(listRoomRate.size()>0)
						 {
						 dblRoomRate=Double.parseDouble(listRoomRate.get(0).toString());
						 }
					 }
				}
				objPostRoomTerrifBean = new clsPostRoomTerrifBean();
				objPostRoomTerrifBean.setStrFolioNo(arrObjRoom[0].toString());
				objPostRoomTerrifBean.setStrRoomNo(arrObjRoom[1].toString());
				if(arrObjRoom[7].toString().equals("Y"))
				{
					objPostRoomTerrifBean.setDblRoomTerrif(0.0);
					objPostRoomTerrifBean.setDblOriginalPostingAmt(0.0);
				}
				else
				{
					objPostRoomTerrifBean.setDblRoomTerrif(dblRoomRate);
					objPostRoomTerrifBean.setDblOriginalPostingAmt(dblRoomRate);
				}
				objPostRoomTerrifBean.setStrFolioType("Room");
				String folioNo = arrObjRoom[0].toString();
				String docNo = objPostRoomTerrif.funInsertFolioRecords(folioNo, clientCode, propCode, objPostRoomTerrifBean, objGlobal.funGetDate("yyyy-MM-dd", PMSDate), arrObjRoom[3].toString(),strTransactionType,userCode);
				listRoomTerrifDocNo.add(docNo);
				/*if(Double.valueOf(arrObjRoom[7].toString())>0)
				{   
					dblRoomRate=Double.valueOf(arrObjRoom[7].toString())/2;
					objPostRoomTerrifBean = new clsPostRoomTerrifBean();
					objPostRoomTerrifBean.setStrFolioNo(arrObjRoom[0].toString());
					objPostRoomTerrifBean.setStrRoomNo(arrObjRoom[1].toString());
					objPostRoomTerrifBean.setDblRoomTerrif(dblRoomRate);
					objPostRoomTerrifBean.setDblOriginalPostingAmt(dblRoomRate);
					objPostRoomTerrifBean.setStrFolioType("Package");
					folioNo = arrObjRoom[0].toString();
					docNo=objPostRoomTerrif.funInsertFolioRecords(folioNo, clientCode, propCode, objPostRoomTerrifBean, objGlobal.funGetDate("yyyy-MM-dd", PMSDate), arrObjRoom[3].toString(),strTransactionType,userCode);	
					listRoomTerrifDocNo.add(docNo);
				}*/
				
				
				
			
			}
		}

			
			double dayEndAmt = 0;
			for (int cnt = 0; cnt < listRoomTerrifDocNo.size(); cnt++) {
				sql = "select sum(dblDebitAmt) from tblfoliodtl " + " where strDocNo='" + listRoomTerrifDocNo.get(cnt) + "'  AND strClientCode='"+clientCode+"' group by strDocNo";
				List listFolioAmt = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
				if(listFolioAmt.size()>0)
				{
					dayEndAmt += Double.parseDouble(listFolioAmt.get(0).toString());
				}
			}

			objBean.setDblDayEndAmt(dayEndAmt);
			objBean.setStrDayEnd("Y");
			objBean.setStrPropertyCode(propCode);
			objBean.setStrClientCode(clientCode);
			
			
			clsDayEndHdModel objHdModel = funPrepareHdModel(objBean, userCode);
			objDayEndService.funAddUpdateDayEndHd(objHdModel);

			// Insert row in tbldayendprocess for next date.
			clsDayEndHdModel objModel = new clsDayEndHdModel();
			objModel.setStrClientCode(clientCode);
			objModel.setStrPropertyCode(propCode);
			objModel.setStrStartDay("Y");
			objModel.setStrDayEnd("N");
			objModel.setDblDayEndAmt(0);
			objModel.setDtePMSDate(strNewDate);
			objModel.setStrUserCode(userCode);
			objDayEndService.funAddUpdateDayEndHd(objModel);

			model= new ModelAndView("redirect:/frmModuleSelection.html");
		}
		return model;
	}

	private void funSendCheckInMail(String newStartDate,String clientCode, String propCode,HttpServletRequest req) throws JRException {
		
		String strModuleName = "dayEnd";
		
		String sqlCheckInForNextDate="SELECT a.strReservationNo,a.strGuestcode "
				+ "FROM tblreservationhd a "
				+ "WHERE Date(a.dteArrivalDate)='"+newStartDate+"' AND a.strClientCode='"+clientCode+"'";
		
		List listNextDateCheckIn = objGlobalFunctionsService.funGetListModuleWise(sqlCheckInForNextDate, "sql");
		{
			if(listNextDateCheckIn!=null && listNextDateCheckIn.size()>0)
			{
				for(int k=0;k<listNextDateCheckIn.size();k++)
				{
					Object[] arrObjBlock = (Object[]) listNextDateCheckIn.get(k);
					
					String strReservationNo = arrObjBlock[0].toString();
					String strGuestCode = arrObjBlock[1].toString();
					
					if(strGuestCode!=null){
						
						
						clsReservationHdModel objModel = objReservationService.funGetReservationList(strReservationNo, clientCode, propCode);
						
						clsPropertySetupHdModel objPropertySetupModel= objPropertySetupService.funGetPropertySetup(propCode, clientCode);
						
						String strReservationMessege = objPropertySetupModel.getStrCheckInEmailContent();
						
						List<clsReservationDtlModel> listReservationmodel = objModel.getListReservationDtlModel();

						if (listReservationmodel.size() > 0) {
							for (int i = 0; i < listReservationmodel.size(); i++) {
								clsReservationDtlModel objDtl = listReservationmodel.get(i);
								if (objDtl.getStrPayee().equals("Y")) {

									List list = objGuestService.funGetGuestMaster(objDtl.getStrGuestCode(), clientCode);
									clsGuestMasterHdModel objGuestModel = null;
									if (list.size() > 0) {
										objGuestModel = (clsGuestMasterHdModel) list.get(0);
						
						
						
							if(strReservationMessege!=null)
								{
							if (strReservationMessege.contains("%%CompanyName")) {
							List<clsCompanyMasterModel> listCompanyModel = objPropertySetupService.funGetListCompanyMasterModel(clientCode);
							strReservationMessege = strReservationMessege.replace("%%CompanyName", listCompanyModel.get(0).getStrCompanyName()+" ");
							
						}
						if (strReservationMessege.contains("%%PropertyName")) {
							clsPropertyMaster objProperty = objPropertyMasterService.funGetProperty(propCode, clientCode);
							strReservationMessege = strReservationMessege.replace("%%PropertyName", objProperty.getPropertyName()+" ");
							
						}

						if (strReservationMessege.contains("%%RNo")) {
							strReservationMessege = strReservationMessege.replace("%%RNo", strReservationNo+" ");
							
						}

						if (strReservationMessege.contains("%%RDate")) {
							strReservationMessege = strReservationMessege.replace("%%RDate", objGlobal.funGetDate("dd-MM-yyyy", objModel.getDteReservationDate()+" "));
							
						}

						if (strReservationMessege.contains("%%NoNights")) {
							strReservationMessege = strReservationMessege.replace("%%NoNights", String.valueOf(objModel.getIntNoOfNights())+" ");
							
						}
						
						if (strReservationMessege.contains("%%GuestName")) {
							strReservationMessege = strReservationMessege.replace("%%GuestName", objGuestModel.getStrFirstName() + " " + objGuestModel.getStrMiddleName() + " " + objGuestModel.getStrLastName());
						}

						if (strReservationMessege.contains("%%RoomNo")) {
							clsRoomMasterModel roomNo = objRoomMaster.funGetRoomMaster(objDtl.getStrRoomNo(), clientCode);
							strReservationMessege = strReservationMessege.replace("%%RoomNo", roomNo.getStrRoomDesc());
						}
						
									}
								}
							}
						}
					}
						objSendEmail.doSendReservationEmail(strReservationNo,strReservationMessege,strModuleName,req);
						
						
					}
				}
			}
		}
	}

	// Convert bean to model function
	private clsDayEndHdModel funPrepareHdModel(clsDayEndBean objBean, String userCode) {

		clsDayEndHdModel objModel = new clsDayEndHdModel();
		objModel.setStrStartDay("Y");
		//objModel.setDtePMSDate(objBean.getDtePMSDate());
		objModel.setDtePMSDate(objBean.getDtePMSDate());
		objModel.setStrDayEnd(objBean.getStrDayEnd());
		objModel.setDblDayEndAmt(objBean.getDblDayEndAmt());
		objModel.setStrUserCode(userCode);
		objModel.setStrPropertyCode(objBean.getStrPropertyCode());
		objModel.setStrClientCode(objBean.getStrClientCode());
		return objModel;
	}

	// Start Day function
	@RequestMapping(value = "/startPMSDay", method = RequestMethod.GET)
	public @ResponseBody int funLoadMasterData(@RequestParam("PMSDate") String PMSDate, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String propCode = req.getSession().getAttribute("propertyCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();

		/*
		 * String sql="update tbldayendprocess set strStartDay='Y' " +
		 * " where strPropertyCode='" + propCode +
		 * "' and strClientCode='"+clientCode+"' " +
		 * " and strDayEnd='N' and date(dtePMSDate)='"+PMSDate+"'";
		 */

		clsDayEndHdModel objModel = new clsDayEndHdModel();
		objModel.setDtePMSDate(PMSDate);
		objModel.setStrStartDay("Y");
		objModel.setStrDayEnd("N");
		objModel.setDblDayEndAmt(0);
		objModel.setStrUserCode(userCode);
		objModel.setStrPropertyCode(propCode);
		objModel.setStrClientCode(clientCode);
		objDayEndService.funAddUpdateDayEndHd(objModel);
		return 1;
	}

	public static String getNextDate(String  curDate) throws ParseException {
		  final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		  final Date date = format1.parse(curDate);
		  final Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date);
		  calendar.add(Calendar.DAY_OF_YEAR, 1);
		  return format1.format(calendar.getTime()); 
		}

}
