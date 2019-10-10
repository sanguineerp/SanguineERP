package com.sanguine.webpms.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.Calendar;
import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsGuestListReportBean;
import com.sanguine.webpms.bean.clsGuestMasterBean;
import com.sanguine.webpms.bean.clsRoomStatusDiaryBean;
import com.sanguine.webpms.bean.clsRoomStatusDtlBean;
import com.sanguine.webpms.model.clsFolioDtlModel;
import com.sanguine.webpms.model.clsFolioHdModel;
import com.sanguine.webpms.service.clsRoomMasterService;

@Controller
public class clsRoomStatusDiaryController {
	@Autowired
	private clsRoomMasterService objRoomMasterService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;

	// Open Room Status Diary
	@RequestMapping(value = "/frmRoomStatusDiary", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		List listRoom = new ArrayList<>();
		
		String sqlRoomType = "select a.strRoomTypeDesc from tblroomtypemaster a where a.strClientCode='"+clientCode+"'";
		listRoom = objGlobalFunctionsService.funGetListModuleWise(sqlRoomType, "sql");
		//listRoom.add("Select room type");
		listRoom.add(0, "SELECT ROOM TYPE");
		/*for (int cnt1 = 0; cnt1 < listRoom.size(); cnt1++) 
		{
			Object[] arrObjRooms = (Object[]) listRoom.get(cnt1);
			model.put("prefix", arrObjRooms[cnt1]).toString();
		}*/
		model.put("prefix", listRoom);
		model.put("urlHits", urlHits);

		

		if (urlHits.equalsIgnoreCase("1")) {
			return new ModelAndView("frmRoomStatusDiary", "command", new clsRoomStatusDiaryBean());
		} else {
			return new ModelAndView("frmRoomStatusDiary_1", "command", new clsRoomStatusDiaryBean());
		}
	}

	// get Room Status Data
	@RequestMapping(value = "/getRoomStatusList", method = RequestMethod.GET)
	public @ResponseBody List funLoadRoomStatus(@RequestParam("viewDate") String viewDate, HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		
		List listViewDates = new ArrayList();
		String[] arrViewDate = viewDate.split("-");
		GregorianCalendar cd = new GregorianCalendar();
		cd.set(Integer.parseInt(arrViewDate[2]) - 1900, Integer.parseInt(arrViewDate[1]) - 1, Integer.parseInt(arrViewDate[0]));

		Date dt = new Date(Integer.parseInt(arrViewDate[2]) - 1900, Integer.parseInt(arrViewDate[1]) - 1, Integer.parseInt(arrViewDate[0]));
		// System.out.println(dt.getDay());
		// System.out.println(dt);
		cd.setTime(dt);
		for (int cnt = 0; cnt < 7; cnt++) {
			String day = funGetDayOfWeek(cd.getTime().getDay());
			String transDate = (cd.getTime().getYear() + 1900) + "-" + (cd.getTime().getMonth() + 1) + "-" + cd.getTime().getDate();
			String date = day + " " + cd.getTime().getDate() + "-" + (cd.getTime().getMonth() + 1) + "-" + (cd.getTime().getYear() + 1900);
			System.out.println(date);
			listViewDates.add(date);
			cd.add(Calendar.DATE, 1);
		}

		System.out.println(listViewDates);
		return listViewDates;
	}

	// get Room Status Data
	@RequestMapping(value = "/getRoomStatusDtlList", method = RequestMethod.GET)
	public @ResponseBody List funLoadRoomStatusDetails(@RequestParam("viewDate") String viewDate, HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String PMSDate=objGlobal.funGetDate("yyyy-MM-dd",request.getSession().getAttribute("PMSDate").toString());
		String date1 = objGlobal.funGetDate("yyyy-MM-dd", viewDate);
		String[] arrViewDate = viewDate.split("-");
		viewDate = objGlobal.funGetDate("yyyy-MM-dd", viewDate);
		clsRoomStatusDtlBean objRoomStatusDtl=null;
		clsGuestMasterBean objGuestDtl = null;
		List objTemp = null;
		List listRoomStatusBeanDtl = new ArrayList<>();
		Map objRoomTypeWise = new HashMap<>();
		Map returnObject = new HashMap<>();
		String sql = "select a.strRoomCode,a.strRoomDesc,b.strRoomTypeDesc,a.strStatus from tblroom a,tblroomtypemaster b where a.strRoomTypeCode=b.strRoomTypeCode AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"'"
				+ " order by b.strRoomTypeCode,a.strRoomDesc; ";
		List listRoom = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		objTemp=new ArrayList<>();
		for (int cnt1 = 0; cnt1 < listRoom.size(); cnt1++) 
		{
			objRoomStatusDtl = new clsRoomStatusDtlBean();
			Object[] arrObjRooms = (Object[]) listRoom.get(cnt1);
			objRoomStatusDtl.setStrRoomNo(arrObjRooms[1].toString());
			objRoomStatusDtl.setStrRoomType(arrObjRooms[2].toString());
			objRoomStatusDtl.setStrRoomStatus(arrObjRooms[3].toString());
			TreeMap<Integer, List<clsGuestListReportBean>> mapGuestListPerDay=new TreeMap<>();
			List<clsGuestListReportBean> listMainGuestDetailsBean=new ArrayList<>();
			
			sql=" SELECT IF(a.strReservationNo='',a.strCheckInNo,''),d.strRoomCode,d.strRoomDesc, CONCAT(c.strFirstName,' ',c.strMiddleName,' ',c.strLastName),d.strStatus, "
					+ "DATE_FORMAT(DATE(a.dteArrivalDate),'%d-%m-%Y'), DATE_FORMAT(DATE(a.dteDepartureDate),'%d-%m-%Y'), "
					+ "DATEDIFF('"+PMSDate+"',DATE(a.dteDepartureDate)),LEFT(TIMEDIFF(a.tmeDepartureTime,(select a.tmeCheckOutTime from tblpropertysetup a )),6), "
					+ "LEFT(TIMEDIFF(a.tmeArrivalTime,(select a.tmeCheckInTime from tblpropertysetup a )),6),a.tmeArrivalTime,a.tmeDepartureTime , DATEDIFF(DATE(a.dteArrivalDate),'"+PMSDate+"'), DATEDIFF(DATE(a.dteDepartureDate),'"+PMSDate+"')"
					+ "FROM tblcheckinhd a,tblcheckindtl b,tblguestmaster c,tblroom d,tblfoliohd e "
					+ "WHERE a.strCheckInNo=b.strCheckInNo AND b.strGuestCode=c.strGuestCode AND b.strRoomNo=d.strRoomCode "
					+ "AND DATE(a.dteDepartureDate) BETWEEN '"+viewDate+"' AND DATE_ADD('"+viewDate+"',INTERVAL 7 DAY) AND b.strRoomNo='"+arrObjRooms[0].toString()+"' AND a.strCheckInNo=e.strCheckInNo "
					+ "AND a.strCheckInNo NOT IN (SELECT strCheckInNo FROM tblbillhd) AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' AND c.strClientCode='"+clientCode+"' AND d.strClientCode='"+clientCode+"' AND e.strClientCode='"+clientCode+"' "
					+ "UNION "
					+ "SELECT a.strReservationNo,d.strRoomCode,d.strRoomDesc, CONCAT(c.strFirstName,' ',c.strMiddleName,' ',c.strLastName), "
					+ "IFNULL(e.strBookingTypeDesc,''), DATE_FORMAT(DATE(a.dteArrivalDate),'%d-%m-%Y'), DATE_FORMAT(DATE(a.dteDepartureDate),'%d-%m-%Y'), "
					+ "DATEDIFF(DATE(a.dteDepartureDate),DATE(a.dteArrivalDate)),LEFT(TIMEDIFF(a.tmeDepartureTime,(select a.tmeCheckOutTime from tblpropertysetup a )),6), "
					+ "LEFT(TIMEDIFF(a.tmeArrivalTime,(select a.tmeCheckInTime from tblpropertysetup a )),6),a.tmeArrivalTime,a.tmeDepartureTime , DATEDIFF(DATE(a.dteArrivalDate),'"+viewDate+"'),DATEDIFF(DATE(a.dteDepartureDate),'"+viewDate+"')"
					+ "FROM tblreservationhd a,tblreservationdtl b,tblguestmaster c,tblroom d,tblbookingtype e "
					+ "WHERE a.strReservationNo=b.strReservationNo AND b.strGuestCode=c.strGuestCode AND b.strRoomNo=d.strRoomCode "
					+ "AND a.strBookingTypeCode=e.strBookingTypeCode AND DATE(a.dteDepartureDate) BETWEEN '"+viewDate+"' AND DATE_ADD('"+viewDate+"',INTERVAL 7 DAY) AND b.strRoomNo='"+arrObjRooms[0].toString()+"' "
					+ "AND a.strReservationNo NOT IN (SELECT strReservationNo FROM tblcheckinhd) AND a.strCancelReservation='N' AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' AND c.strClientCode='"+clientCode+"' AND d.strClientCode='"+clientCode+"' AND e.strClientCode='"+clientCode+"'"
					+ "UNION "
					+ "SELECT a.strWalkinNo,d.strRoomCode,d.strRoomDesc, CONCAT(c.strFirstName,' ',c.strMiddleName,' ',c.strLastName),'Waiting', "
					+ "DATE_FORMAT(DATE(a.dteWalkinDate),'%d-%m-%Y'), DATE_FORMAT(DATE(a.dteCheckOutDate),'%d-%m-%Y'), "
					+ "DATEDIFF('"+viewDate+"',DATE(a.dteCheckOutDate)),LEFT(TIMEDIFF(a.tmeCheckOutTime,(select a.tmeCheckOutTime from tblpropertysetup a )),6), "
					+ "LEFT(TIMEDIFF(a.tmeWalkInTime,(select a.tmeCheckInTime from tblpropertysetup a )),6),a.tmeWalkInTime,a.tmeCheckOutTime , DATEDIFF(DATE(a.dteWalkinDate),'"+viewDate+"'),DATEDIFF(DATE(a.dteCheckOutDate),'"+viewDate+"')"
					+ "FROM tblwalkinhd a,tblwalkindtl b,tblguestmaster c,tblroom d "
					+ "WHERE a.strWalkinNo=b.strWalkinNo AND b.strGuestCode=c.strGuestCode AND b.strRoomNo=d.strRoomCode "
					+ "AND DATE(a.dteCheckOutDate) BETWEEN '"+viewDate+"' AND DATE_ADD('"+viewDate+"',INTERVAL 7 DAY) AND b.strRoomNo='"+arrObjRooms[0].toString()+"' AND a.strWalkinNo NOT IN (SELECT strWalkinNo FROM tblcheckinhd) AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' AND c.strClientCode='"+clientCode+"' AND d.strClientCode='"+clientCode+"'"
					+ "UNION "
					+ "SELECT e.strBillNo,d.strRoomCode,d.strRoomDesc, CONCAT(c.strFirstName,' ',c.strMiddleName,' ',c.strLastName),'Checked Out', DATE_FORMAT(DATE(a.dteArrivalDate),'%d-%m-%Y'), DATE_FORMAT(DATE(a.dteDepartureDate),'%d-%m-%Y'), DATEDIFF('"+PMSDate+"', DATE(a.dteDepartureDate)),"
					+ "LEFT(TIMEDIFF(a.tmeDepartureTime,(SELECT a.tmeCheckOutTime "
					+ "FROM tblpropertysetup a)),6),"
					+ "LEFT(TIMEDIFF(a.tmeArrivalTime,("
					+ "SELECT a.tmeCheckInTime "
					+ "FROM tblpropertysetup a)),6),a.tmeArrivalTime,a.tmeDepartureTime,"
					+ "DATEDIFF(DATE(a.dteArrivalDate),'"+viewDate+"'), DATEDIFF(DATE(a.dteDepartureDate),'"+viewDate+"') "
					+ "FROM tblcheckinhd a,tblcheckindtl b,tblguestmaster c,tblroom d,tblbillhd e "
					+ "WHERE a.strCheckInNo=b.strCheckInNo AND b.strGuestCode=c.strGuestCode AND b.strRoomNo=d.strRoomCode AND DATE(a.dteDepartureDate) BETWEEN '"+viewDate+"' AND DATE_ADD('"+viewDate+"', INTERVAL 7 DAY)"
					+ "	AND b.strRoomNo='"+arrObjRooms[0].toString()+"' AND a.dteDepartureDate NOT IN ('"+PMSDate+"') AND a.strCheckInNo=e.strCheckInNo  AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' "
					+ "AND c.strClientCode='"+clientCode+"' AND d.strClientCode='"+clientCode+"' group by d.strRoomDesc ;";
				List listRoomDtl = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
				if (listRoomDtl.size() > 0) 
				{
					for(int i=0;i<listRoomDtl.size();i++)
					{
						
						int intArrivalCnt = 0;
						int intDepartureCnt = 0;
						objGuestDtl = new clsGuestMasterBean();
						Object[] arrObjRoomDtl = (Object[]) listRoomDtl.get(i);
						objGuestDtl.setStrFirstName(arrObjRoomDtl[3].toString());
						objGuestDtl.setDteArrivalDate(arrObjRoomDtl[5].toString());
						objGuestDtl.setDteDepartureDate(arrObjRoomDtl[6].toString());
						objGuestDtl.setStRoomNo(arrObjRoomDtl[2].toString());
						objGuestDtl.setStrNoOfNights(arrObjRoomDtl[7].toString());
						objGuestDtl.setTmeArrivalTime(arrObjRoomDtl[10].toString());
						objGuestDtl.setTmeDepartureTime(arrObjRoomDtl[11].toString());
						
						/*if(objRoomTypeWise.containsKey(arrObjRooms[2].toString()))
						{
							objTemp=(List)objRoomTypeWise.get(arrObjRooms[2].toString());
							objRoomStatusDtl=new clsRoomStatusDtlBean();
							objRoomStatusDtl.setStrRoomNo(arrObjRooms[1].toString());
							objRoomStatusDtl.setStrRoomType(arrObjRooms[2].toString());
							objRoomStatusDtl.setStrRoomStatus(arrObjRooms[3].toString());
							objRoomStatusDtl.setStrReservationNo(arrObjRoomDtl[0].toString());
							objRoomStatusDtl.setStrGuestName(arrObjRoomDtl[3].toString());
							objRoomStatusDtl.setDteArrivalDate(arrObjRoomDtl[5].toString());
							objRoomStatusDtl.setDteDepartureDate(arrObjRoomDtl[6].toString());
							objRoomStatusDtl.setStrNoOfDays(arrObjRoomDtl[7].toString());
							objRoomStatusDtl.setTmeArrivalTime(arrObjRoomDtl[10].toString());
							objRoomStatusDtl.setTmeDepartureTime(arrObjRoomDtl[11].toString());
							objRoomStatusDtl.setStrRoomStatus(arrObjRoomDtl[4].toString());
							if(arrObjRoomDtl[8].toString().contains("-"))
							{
								if(arrObjRoomDtl[11].toString().contains("PM") || arrObjRoomDtl[11].toString().contains("pm"))
								{
									objRoomStatusDtl.setTmeCheckOutAMPM("PM");
								}
								else
								{
									objRoomStatusDtl.setTmeCheckInAMPM("AM");
								}
							}
							else
							{
								if(arrObjRoomDtl[8].toString().equals("00:00:"))
								{
									if(arrObjRoomDtl[11].toString().contains("PM") || arrObjRoomDtl[11].toString().contains("pm"))
									{
										objRoomStatusDtl.setTmeCheckOutAMPM("PM");
									}
									else
									{
										objRoomStatusDtl.setTmeCheckOutAMPM("AM");
									}
								}
								else
								{
									if(arrObjRoomDtl[11].toString().contains("PM") || arrObjRoomDtl[11].toString().contains("pm"))
									{
										objRoomStatusDtl.setTmeCheckOutAMPM("PM");
									}
									else
									{
										objRoomStatusDtl.setTmeCheckOutAMPM("AM");
									}
								}
							}
							
							if(arrObjRoomDtl[9].toString().contains("-"))
							{
								if(arrObjRoomDtl[10].toString().contains("PM") || arrObjRoomDtl[10].toString().contains("pm"))
								{
									objRoomStatusDtl.setTmeCheckInAMPM("PM");
								}
								else
								{
									objRoomStatusDtl.setTmeCheckInAMPM("AM");
								}
							}
							else
							{
								if(arrObjRoomDtl[8].toString().equals("00:00:"))
								{
									if(arrObjRoomDtl[10].toString().contains("PM") || arrObjRoomDtl[10].toString().contains("pm"))
									{
										objRoomStatusDtl.setTmeCheckInAMPM("PM");
									}
									else
									{
										objRoomStatusDtl.setTmeCheckInAMPM("AM");
									}
								}
								else
								{
									if(arrObjRoomDtl[10].toString().contains("PM") || arrObjRoomDtl[10].toString().contains("pm"))
									{
										objRoomStatusDtl.setTmeCheckInAMPM("PM");
									}
									else
									{
										objRoomStatusDtl.setTmeCheckInAMPM("AM");
									}
								}
							}
							objTemp.add(objRoomStatusDtl);
							objRoomTypeWise.put(arrObjRooms[2].toString(),objTemp);
						}
						else
						{*/
							String sqlFolioNo = "select a.strFolioNo from tblfoliohd a where a.strCheckInNo='"+arrObjRoomDtl[0].toString()+"' AND a.strRoomNo='"+arrObjRoomDtl[1].toString()+"' AND a.strClientCode='"+clientCode+"'";
							List listFolioNo = objGlobalFunctionsService.funGetListModuleWise(sqlFolioNo, "sql");
							String strFolioNo = "";
							if(listFolioNo!=null && listFolioNo.size()>0)
							{
								strFolioNo = listFolioNo.get(0).toString();
							}
							
							objRoomStatusDtl=new clsRoomStatusDtlBean();
							objRoomStatusDtl.setStrRoomNo(arrObjRooms[1].toString());
							objRoomStatusDtl.setStrRoomType(arrObjRooms[2].toString());
							objRoomStatusDtl.setStrRoomStatus(arrObjRooms[3].toString());
							if(arrObjRoomDtl[4].toString().equalsIgnoreCase("Occupied"))
							{
								objRoomStatusDtl.setStrReservationNo(strFolioNo);
							}
							else
							{
								objRoomStatusDtl.setStrReservationNo(arrObjRoomDtl[0].toString());
							}
							objRoomStatusDtl.setStrGuestName(arrObjRoomDtl[3].toString());
							objRoomStatusDtl.setDteArrivalDate(arrObjRoomDtl[5].toString()+" "+ arrObjRoomDtl[10].toString());
							objRoomStatusDtl.setDteDepartureDate(arrObjRoomDtl[6].toString()+" "+ arrObjRoomDtl[11].toString());
							objRoomStatusDtl.setStrNoOfDays(arrObjRoomDtl[7].toString());
							objRoomStatusDtl.setTmeArrivalTime(arrObjRoomDtl[10].toString());
							objRoomStatusDtl.setTmeDepartureTime(arrObjRoomDtl[11].toString());
							objRoomStatusDtl.setStrRoomStatus(arrObjRoomDtl[4].toString());
							if(arrObjRoomDtl[4].toString().equals("Occupied")){
							objRoomStatusDtl.setDblRemainingAmt(funGetDblRemainingAmt(strFolioNo,clientCode,arrObjRoomDtl[0].toString()));
							}
							intArrivalCnt=Integer.parseInt(arrObjRoomDtl[12].toString());
							intDepartureCnt=Integer.parseInt(arrObjRoomDtl[13].toString());
							
							if (intArrivalCnt<=0 && 0<=intDepartureCnt) 
								
							{
								
								 objRoomStatusDtl.setStrDay1(" "+objRoomStatusDtl.getStrGuestName());
							} 
							if (intArrivalCnt<=1 && 1<=intDepartureCnt) 
							{
								
								 objRoomStatusDtl.setStrDay2(" "+objRoomStatusDtl.getStrGuestName());
							} 
							if (intArrivalCnt<=2 && 2<=intDepartureCnt) 
							{
								
								 objRoomStatusDtl.setStrDay3(" "+objRoomStatusDtl.getStrGuestName());
							} if (intArrivalCnt<=3 && 3<=intDepartureCnt) {
								
								 objRoomStatusDtl.setStrDay4(" "+objRoomStatusDtl.getStrGuestName());
							} if (intArrivalCnt<=4 && 4<=intDepartureCnt) {
								
								 objRoomStatusDtl.setStrDay5(" "+objRoomStatusDtl.getStrGuestName());
							} if (intArrivalCnt<=5 && 5<=intDepartureCnt) {
								
								 objRoomStatusDtl.setStrDay6(" "+objRoomStatusDtl.getStrGuestName());
							} if (intArrivalCnt<=6 && 6<=intDepartureCnt) {
								
								 objRoomStatusDtl.setStrDay7(" "+objRoomStatusDtl.getStrGuestName());
							}
							
							if(arrObjRoomDtl[8].toString().contains("-"))
							{
								if(arrObjRoomDtl[11].toString().contains("PM") || arrObjRoomDtl[11].toString().contains("pm"))
								{
									objRoomStatusDtl.setTmeCheckOutAMPM("PM");
								}
								else
								{
									objRoomStatusDtl.setTmeCheckOutAMPM("AM");
								}
							}
							else
							{
								if(arrObjRoomDtl[8].toString().equals("00:00:"))
								{
									if(arrObjRoomDtl[11].toString().contains("PM") || arrObjRoomDtl[11].toString().contains("pm"))
									{
										objRoomStatusDtl.setTmeCheckOutAMPM("PM");
									}
									else
									{
										objRoomStatusDtl.setTmeCheckOutAMPM("AM");
									}
								}
								else
								{
									if(arrObjRoomDtl[11].toString().contains("PM") || arrObjRoomDtl[11].toString().contains("pm"))
									{
										objRoomStatusDtl.setTmeCheckOutAMPM("PM");
									}
									else
									{
										objRoomStatusDtl.setTmeCheckOutAMPM("AM");
									}
								}
							}
							
							if(arrObjRoomDtl[9].toString().contains("-"))
							{
								if(arrObjRoomDtl[10].toString().contains("PM") || arrObjRoomDtl[10].toString().contains("pm"))
								{
									objRoomStatusDtl.setTmeCheckInAMPM("PM");
								}
								else
								{
									objRoomStatusDtl.setTmeCheckInAMPM("AM");
								}
							}
							else
							{
								if(arrObjRoomDtl[8].toString().equals("00:00:"))
								{
									if(arrObjRoomDtl[10].toString().contains("PM") || arrObjRoomDtl[10].toString().contains("pm"))
									{
										objRoomStatusDtl.setTmeCheckInAMPM("PM");
									}
									else
									{
										objRoomStatusDtl.setTmeCheckInAMPM("AM");
									}
								}
								else
								{
									if(arrObjRoomDtl[10].toString().contains("PM") || arrObjRoomDtl[10].toString().contains("pm"))
									{
										objRoomStatusDtl.setTmeCheckInAMPM("PM");
									}
									else
									{
										objRoomStatusDtl.setTmeCheckInAMPM("AM");
									}
								}
							}
							objTemp.add(objRoomStatusDtl);
							//objRoomTypeWise.put(arrObjRooms[2].toString(),objTemp);
						//}
					}
				}
				else
				{
					/*if(objRoomTypeWise.containsKey(arrObjRooms[2].toString()))
					{
						objTemp=(List)objRoomTypeWise.get(arrObjRooms[2].toString());
						objRoomStatusDtl=new clsRoomStatusDtlBean();
						objRoomStatusDtl.setStrRoomNo(arrObjRooms[1].toString());
						objRoomStatusDtl.setStrRoomType(arrObjRooms[2].toString());
						objRoomStatusDtl.setStrRoomStatus(arrObjRooms[3].toString());
						
						objTemp.add(objRoomStatusDtl);
						objRoomTypeWise.put(arrObjRooms[2].toString(),objTemp);
					}
					else
					{*/
						//objTemp=new ArrayList<>();
						objRoomStatusDtl=new clsRoomStatusDtlBean();
						objRoomStatusDtl.setStrRoomNo(arrObjRooms[1].toString());
						objRoomStatusDtl.setStrRoomType(arrObjRooms[2].toString());
						objRoomStatusDtl.setStrRoomStatus(arrObjRooms[3].toString());
						
						objTemp.add(objRoomStatusDtl);
						//objRoomTypeWise.put(arrObjRooms[2].toString(),objTemp);
					//}	
				}
				
				if(objRoomStatusDtl.getStrRoomStatus().equalsIgnoreCase("Blocked"))
				{
					String sqlBlock = "SELECT DATEDIFF('"+PMSDate+"',b.dteValidTo) FROM tblroom a,tblblockroom b "
							+ "WHERE a.strRoomCode=b.strRoomCode AND a.strRoomDesc='"+objRoomStatusDtl.getStrRoomNo()+"' AND a.strClientCode='"+clientCode+"' ";
					List listBlockRoom = objGlobalFunctionsService.funGetListModuleWise(sqlBlock, "sql");
					if (listBlockRoom.size() > 0) 
					{
						BigInteger diff = (BigInteger) listBlockRoom.get(0);
						String strBlockRoomDiff=diff.toString();
						if(strBlockRoomDiff.startsWith("-"))
						{
							if(Integer.parseInt(strBlockRoomDiff.substring(1))==0)
							{
								objRoomStatusDtl.setStrDay1("Blocked Room");
							}
							else if(Integer.parseInt(strBlockRoomDiff.substring(1))>0)
							{
								for(int i=0;i<=Integer.parseInt(strBlockRoomDiff.substring(1));i++)
								{
									if(i==0)
									{
										i=i+1;
									}
									objRoomStatusDtl.setStrDay("Day"+i+"Blocked Room");
								}
							}
						}
						else
						{
							if(Integer.parseInt(strBlockRoomDiff)==0)
							{
								objRoomStatusDtl.setStrDay1("Blocked Room");
							}
							else if(Integer.parseInt(strBlockRoomDiff)>0)
							{
								for(int i=0;i<=Integer.parseInt(strBlockRoomDiff);i++)
								{
									if(i==0)
									{
										i=i+1;
									}
									objRoomStatusDtl.setStrDay("Day"+i+"Blocked Room");
								}
							}
						}
					}
				}
				//objRoomTypeWise.put(objRoomStatusDtl);
				
		}
		listRoomStatusBeanDtl.add(objRoomTypeWise);	
		//returnObject.put("RoomData", listRoomStatusBeanDtl);
			
		return objTemp;
			
		

		
	}

	private double funGetDblRemainingAmt(String strFolioNo,String clintCode,String strCheckInNo) {
		
		NumberFormat decformat = new DecimalFormat("#0.00");
		double dblTotalRemainingAmt = 0;
	//for Folio Amt
		String sqlFolioAmt = "select sum(a.dblDebitAmt) from tblfoliodtl a where a.strFolioNo='"+strFolioNo+"' and a.strClientCode='"+clintCode+"' ";
		List listFolioAmt = objGlobalFunctionsService.funGetListModuleWise(sqlFolioAmt, "sql");

		if (!listFolioAmt.isEmpty() && listFolioAmt!=null) {
			for(int i = 0; i<listFolioAmt.size(); i++)
			{
				double dblFolioAmt = 0;
				dblFolioAmt = Double.parseDouble(listFolioAmt.get(i).toString());
				dblTotalRemainingAmt = dblTotalRemainingAmt + dblFolioAmt;

			}
		}
		//For Walkin Discount
		String sqlWalkinNo = "select a.strWalkInNo from tblcheckinhd a where a.strCheckInNo='"+strCheckInNo+"' and a.strClientCode='"+clintCode+"'";
		List listWalkinNo = objGlobalFunctionsService.funGetListModuleWise(sqlWalkinNo, "sql");
		if(listWalkinNo.size()>0 && listWalkinNo!=null)
		{
			String strWalkinNo = listWalkinNo.get(0).toString();
			String sqlWalkinDiscount = "select a.dblDiscount from tblwalkinroomratedtl a where a.strWalkinNo='"+strWalkinNo+"' and a.strClientCode='"+clintCode+"'";
			List listWalkinDisc = objGlobalFunctionsService.funGetListModuleWise(sqlWalkinDiscount, "sql");
			if(listWalkinDisc.size()>0 && listWalkinDisc!=null)
			{
				double dblWalkinDisc = Double.parseDouble(listWalkinDisc.get(0).toString());
				dblTotalRemainingAmt = dblTotalRemainingAmt-((dblTotalRemainingAmt*dblWalkinDisc)/100);
			}
		}
		
		//For Folio Tax Amt
		String sqlFolioTaxAmt = "select sum(a.dblTaxAmt) from tblfoliotaxdtl a where a.strFolioNo='"+strFolioNo+"' and a.strDocNo like 'RM%' and a.strClientCode='"+clintCode+"';";
		List listFolioTaxAmt = objGlobalFunctionsService.funGetListModuleWise(sqlFolioTaxAmt, "sql");

		if (!listFolioTaxAmt.isEmpty() && listFolioTaxAmt!=null) {
			for(int i = 0; i<listFolioTaxAmt.size(); i++)
			{
				double dblFolioTaxAmt = 0;
				dblFolioTaxAmt = Double.parseDouble(listFolioTaxAmt.get(i).toString());
				dblTotalRemainingAmt = dblTotalRemainingAmt + dblFolioTaxAmt;

			}
		}
		
		//For Advance Amt 
				String sqlAdvanceAmt = "select a.dblPaidAmt from tblreceipthd a where a.strCheckInNo='"+strCheckInNo+"' and a.strAgainst='Check-In' and a.strClientCode='"+clintCode+"'";
				List listAdvanceAmt = objGlobalFunctionsService.funGetListModuleWise(sqlAdvanceAmt, "sql");

				if (!listAdvanceAmt.isEmpty() && listAdvanceAmt!=null) {
					for(int i = 0; i<listAdvanceAmt.size(); i++)
					{
						double dblAdvanceAmt = 0;
						dblAdvanceAmt = Double.parseDouble(listAdvanceAmt.get(i).toString());
						dblTotalRemainingAmt = dblTotalRemainingAmt - dblAdvanceAmt;

					}
				}
		
		
		dblTotalRemainingAmt=Double.parseDouble(decformat.format(dblTotalRemainingAmt));
		return dblTotalRemainingAmt;
	}

	private String funGetDayOfWeek(int day) {
		String dayOfWeek = "Sun";

		switch (day) {
		case 0:
			dayOfWeek = "Sun";
			break;

		case 1:
			dayOfWeek = "Mon";
			break;

		case 2:
			dayOfWeek = "Tue";
			break;

		case 3:
			dayOfWeek = "Wed";
			break;

		case 4:
			dayOfWeek = "Thu";
			break;

		case 5:
			dayOfWeek = "Fri";
			break;

		case 6:
			dayOfWeek = "Sat";
			break;
		}

		return dayOfWeek;
	}
	
	@RequestMapping(value = "/getRoomTypeWiseList", method = RequestMethod.GET)
	public @ResponseBody Map funLoadRoomTypeWiseStatus(@RequestParam("viewDate") String viewDate, HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
		String PMSDate=objGlobal.funGetDate("yyyy-MM-dd",request.getSession().getAttribute("PMSDate").toString());
		Map returnObject=new HashMap<>();

		try
		{
		Map jObjStayViewData=new HashMap<>(); 
		List mainArrObj = new ArrayList<>();
		NumberFormat formatter = new DecimalFormat("#0");
		NumberFormat decformat = new DecimalFormat("#0.00");
		String dd = viewDate.split("-")[0]; 
		String mm=	 viewDate.split("-")[1] ;
		String yy= viewDate.split("-")[2];
		String strCompDate = objGlobal.funGetDate("yyyy-MM-dd", viewDate);
		
			
			
			Map objRoomStatusDtlBean = new HashMap<>();
			List listRoomStatus= new ArrayList<>();

			if(PMSDate.equalsIgnoreCase(strCompDate))
			{
				String sql="select a.strRoomTypeDesc from tblroom a where a.strClientCode='"+clientCode+"' group by  a.strRoomTypeDesc ";
				List listRoomDesc = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
				//while(listRoomDesc.size()>0)
				for(int j=0;j<listRoomDesc.size();j++)
				{
					String tempPMSDate=objGlobal.funGetDate("yyyy-MM-dd", viewDate);
					String strRoomData="";
					sql="select count(*) from tblroom a where a.strRoomTypeDesc='"+listRoomDesc.get(j)+"' AND a.strClientCode='"+clientCode+"'";
					
					List listRoomData = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
					if(listRoomData.size()>0)
					{
						int intRoomAccupied=0;
						for(int i=1;i<=7;i++)
						{
							sql=" select count(*) "
									+ " from  tblcheckindtl a,tblroom b,tblcheckinhd c where a.strCheckInNo=c.strCheckInNo and"
									+ " a.strRoomNo=b.strRoomCode and date(c.dteCheckInDate) <= '"+tempPMSDate+"'  "
									+ "  and date(c.dteDepartureDate)>='"+tempPMSDate+"' and b.strRoomTypeDesc='"+listRoomDesc.get(j)+"' and b.strStatus='Occupied' AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' AND c.strClientCode='"+clientCode+"'";
							
							List listCheckInData = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
							String dd1=String.valueOf((Integer.parseInt(dd)+i));

							if(listCheckInData.size()>0)
							{
								if(strRoomData.isEmpty())
								{
									strRoomData=listRoomDesc.get(j)+"/"+listCheckInData.get(0)+"-"+listRoomData.get(0);
								}
								else
								{
									strRoomData=strRoomData+"/"+listCheckInData.get(0)+"-"+listRoomData.get(0);	
								}
							}
							tempPMSDate=yy+"-"+mm+"-"+dd1;
						}
						objRoomStatusDtlBean.put(listRoomDesc.get(j),strRoomData);
						/*listRoomStatus.put(objRoomStatusDtlBean);*/
						/*objRoomStatusData.put(rsRoomInfo1.getString(1), strRoomData);*/
					}
				}
			}
			else
			{
			
			String sql="select a.strRoomTypeDesc from tblroom a where a.strClientCode='"+clientCode+"' group by strBedType ";
			List listRoomDesc = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			//while(listRoomDesc.size()>0)
			for(int j=0;j<listRoomDesc.size();j++)
			{
				String tempPMSDate=objGlobal.funGetDate("yyyy-MM-dd", viewDate);
				String strRoomData="";
				sql="select count(*) from tblroom a where a.strRoomTypeDesc='"+listRoomDesc.get(j)+"' AND a.strClientCode='"+clientCode+"'";
				
				List listRoomData = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
				if(listRoomData.size()>0)
				{
					int intRoomAccupied=0;
					for(int i=1;i<=7;i++)
					{
						sql=" select count(*) "
								+ " from  tblcheckindtl a,tblroom b,tblcheckinhd c where a.strCheckInNo=c.strCheckInNo and"
								+ " a.strRoomNo=b.strRoomCode and date(c.dteCheckInDate) <= '"+tempPMSDate+"'  "
								+ "  and date(c.dteDepartureDate)>='"+tempPMSDate+"' and b.strRoomTypeDesc='"+listRoomDesc.get(j)+"' AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' AND c.strClientCode='"+clientCode+"' group by b.strRoomDesc";
						
						List listCheckInData = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
						String dd1=String.valueOf((Integer.parseInt(dd)+i));

						if(listCheckInData.size()>0)
						{
							if(strRoomData.isEmpty())
							{
								strRoomData=listRoomDesc.get(j)+"/"+listCheckInData.size()+"-"+listRoomData.get(0);
							}
							else
							{
								strRoomData=strRoomData+"/"+listCheckInData.size()+"-"+listRoomData.get(0);	
							}
						}
						else
						{
							strRoomData=strRoomData+"/"+0+"-"+listRoomData.get(0);
						}
						tempPMSDate=yy+"-"+mm+"-"+dd1;
					}
					objRoomStatusDtlBean.put(listRoomDesc.get(j),strRoomData);
					/*listRoomStatus.put(objRoomStatusDtlBean);*/
					/*objRoomStatusData.put(rsRoomInfo1.getString(1), strRoomData);*/
				}
			}
			}
			returnObject.put("RoomTypeCount", objRoomStatusDtlBean);
					}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return returnObject;
		
	}
}
