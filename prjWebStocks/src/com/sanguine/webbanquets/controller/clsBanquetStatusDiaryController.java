package com.sanguine.webbanquets.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ibm.icu.util.Calendar;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsLocationMasterModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsUserMasterService;
import com.sanguine.webbanquets.bean.clsBanquetStatusDiaryBean;
import com.sanguine.webpms.bean.clsRoomStatusDtlBean;

@Controller
public class clsBanquetStatusDiaryController {
	
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@Autowired
	private clsUserMasterService objUserMasterService;

	// Open Room Status Diary
	@RequestMapping(value = "/frmWebBanquetDiary", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		clsBanquetStatusDiaryBean objBanquetStatusDiaryBean=new clsBanquetStatusDiaryBean(); 
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		
		org.json.simple.JSONArray  jsonArrForLocationButtons =new org.json.simple.JSONArray();
		 
		HashMap<String, String> locMap = new HashMap<String, String>();
		String usercode = request.getSession().getAttribute("usercode").toString();
		String propertyCode= request.getSession().getAttribute("propertyCode").toString();
		try {
			StringBuilder sqlBuilder = new StringBuilder("from clsLocationMasterModel where strClientCode='" + clientCode + "' ");
			List<clsLocationMasterModel> list = objGlobalFunctionsService.funGetList(sqlBuilder.toString(), "hql");
			
			if(list!=null && list.size()>0){
				for(clsLocationMasterModel obj :list){
					jsonArrForLocationButtons.add(obj);	
				}
				
			}
			objBanquetStatusDiaryBean.setJsonArrForLocationButtons(jsonArrForLocationButtons);
			
		} catch (Exception e) {
			e.printStackTrace();
			locMap.put("Invalid", "Invalid");
		}
		 
	
		
		model.put("urlHits", urlHits);
		
		if (urlHits.equalsIgnoreCase("1")) {
			return new ModelAndView("frmWebBanquetDiary", "command", objBanquetStatusDiaryBean);
		} else {
			return new ModelAndView("frmWebBanquetDiary_1", "command", objBanquetStatusDiaryBean);
		}
	}
	
	
	public void funGenarateDiary(){
		List listHrTime=new ArrayList<>();
		try{
			 String myTime = "00:01";
			 SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			 Date d = df.parse(myTime); 
			 Calendar cal = Calendar.getInstance();
			 cal.setTime(d);
			 cal.add(Calendar.HOUR, 1);
			 String newTime = df.format(cal.getTime());
			 
			 //listHrTime
			 
	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "deprecation" })
	@RequestMapping(value ="/getBanquetDiaryHeader", method=RequestMethod.GET)
	public @ResponseBody List funLoadRoomStatus(@RequestParam("viewDate") String viewDate,@RequestParam("viewType") String viewType, HttpServletRequest request) {
		
		List listViewDates = new ArrayList();
		String[] arrViewDate = viewDate.split("-");
		GregorianCalendar cd = new GregorianCalendar();
		cd.set(Integer.parseInt(arrViewDate[2]) - 1900, Integer.parseInt(arrViewDate[1]) - 1, Integer.parseInt(arrViewDate[0]));
		Date dt = new Date(Integer.parseInt(arrViewDate[2]) - 1900, Integer.parseInt(arrViewDate[1]) - 1, Integer.parseInt(arrViewDate[0]));
		cd.setTime(dt);
		if(viewType.equalsIgnoreCase("normal")){
			for (int cnt = 0; cnt < 7; cnt++) {
				String day = funGetDayOfWeek(cd.getTime().getDay());
				String date = day + " " + cd.getTime().getDate() + "-" + (cd.getTime().getMonth() + 1) + "-" + (cd.getTime().getYear() + 1900);
				listViewDates.add(date);
				cd.add(Calendar.DATE, 1);
			}
		}else if(viewType.equalsIgnoreCase("cancel")){
			for (int cnt = 0; cnt < 7; cnt++) {
				String day = funGetDayOfWeek(cd.getTime().getDay());
				String date = day + " " + cd.getTime().getDate() + "-" + (cd.getTime().getMonth() + 1) + "-" + (cd.getTime().getYear() + 1900);
				listViewDates.add(date);
				cd.add(Calendar.DATE, 1);
			}
		}else if(viewType.equalsIgnoreCase("day")){
			for (int cnt = 0; cnt < 7; cnt++) {
				String day = funGetDayOfWeek(cd.getTime().getDay());
				String date = day + " " + cd.getTime().getDate() + "-" + (cd.getTime().getMonth() + 1) + "-" + (cd.getTime().getYear() + 1900);
				listViewDates.add(date);
				cd.add(Calendar.DATE, 1);
			}
		}
		
		
		
		return listViewDates;
	}
	
	@RequestMapping(value ="/getBanquetBookingDetails", method=RequestMethod.GET)
	public @ResponseBody List funGetBanquetBookingDetails(@RequestParam("viewDate")String viewDate,@RequestParam("viewType")String viewType,HttpServletRequest req){
		List listBanquetReservation=new ArrayList<>();
		try{
			
			clsRoomStatusDtlBean objBean=new clsRoomStatusDtlBean();
			
			viewDate=viewDate.split("-")[2] +"-"+viewDate.split("-")[1]+"-"+viewDate.split("-")[0];
			String stratTime="00:00",tableRowTime="";
			for(int i=0;i<24;i++){
				
				 SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				 Date dTimeLimStart = df.parse(stratTime); 
				 Calendar cal = Calendar.getInstance();
				 cal.setTime(dTimeLimStart);
				 if(i==23){
					 cal.add(Calendar.MINUTE, 59);
				 }else{
					 cal.add(Calendar.HOUR, 1);	 
				 }
				 
				 String newTime = df.format(cal.getTime());
				 Date dTimeLimStop = df.parse(newTime); 
				 
				 tableRowTime=stratTime+"-"+newTime;
				
			 
				 objBean=new clsRoomStatusDtlBean();
				 objBean.setStrDay(tableRowTime);
				 
			String sqlDiary="select a.strBookingNo,DATEDIFF(a.dteFromDate,'"+viewDate+"') dayDiff,a.strBookingStatus,"
					+ "a.dteBookingDate,a.dteFromDate,a.dteToDate,a.strCustomerCode from tblbqbookinghd a where " 
				 +" date(a.dteFromDate) >='"+viewDate+"' and date(a.dteToDate) <= DATE_ADD('"+viewDate+"',INTERVAL 7 DAY) "
				 +" and time(a.tmeFromTime) <= '"+stratTime+":00'"
				 +" and time(a.tmeToTime) >= '"+newTime+":00';";

			 stratTime=newTime;
			 
			List listBooking=objGlobalFunctionsService.funGetListModuleWise(sqlDiary, "sql");
			if(listBooking!=null && listBooking.size()>0){
				for(int k=0;k<listBooking.size();k++){
					
					Object obj[]=(Object[])listBooking.get(k);
					
					if(Double.parseDouble(obj[1].toString())==0){
						objBean.setStrDay1(obj[6].toString().toString()+"#"+obj[2].toString().toString()+"#"+obj[0].toString().toString());	
					}else if(Double.parseDouble(obj[1].toString())==1){
						objBean.setStrDay2(obj[6].toString().toString()+"#"+obj[2].toString().toString()+"#"+obj[0].toString().toString());	
					}else if(Double.parseDouble(obj[1].toString())==2){
						objBean.setStrDay3(obj[6].toString().toString()+"#"+obj[2].toString().toString()+"#"+obj[0].toString().toString());	
					}else if(Double.parseDouble(obj[1].toString())==3){
						objBean.setStrDay4(obj[6].toString().toString()+"#"+obj[2].toString().toString()+"#"+obj[0].toString().toString());	
					}else if(Double.parseDouble(obj[1].toString())==4){
						objBean.setStrDay5(obj[6].toString().toString()+"#"+obj[2].toString().toString()+"#"+obj[0].toString().toString());	
					}else if(Double.parseDouble(obj[1].toString())==5){
						objBean.setStrDay6(obj[6].toString().toString()+"#"+obj[2].toString().toString()+"#"+obj[0].toString().toString());	
					}else if(Double.parseDouble(obj[1].toString())==6){
						objBean.setStrDay7(obj[6].toString().toString()+"#"+obj[2].toString().toString()+"#"+obj[0].toString().toString());	
					}
					
				}
			}
			listBanquetReservation.add(objBean);
		}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return listBanquetReservation;
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

	
}
