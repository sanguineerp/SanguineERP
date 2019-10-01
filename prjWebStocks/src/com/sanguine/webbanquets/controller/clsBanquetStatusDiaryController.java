package com.sanguine.webbanquets.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	@RequestMapping(value ="/getBanquetBookingDetails", method=RequestMethod.GET)
	public @ResponseBody List funGetBanquetBookingDetails(@RequestParam("viewDate")String viewDate,HttpServletRequest req){
		List listBanquetReservation=new ArrayList<>();
		try{
			
			clsRoomStatusDtlBean objBean=new clsRoomStatusDtlBean();
			
			String stratTime="00:00",tableRowTime="";
			for(int i=0;i<24;i++){
				 
				 objBean=new clsRoomStatusDtlBean();
				
				 SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				 Date d = df.parse(stratTime); 
				 Calendar cal = Calendar.getInstance();
				 cal.setTime(d);
				 cal.add(Calendar.HOUR, 1);
				 String newTime = df.format(cal.getTime());
				 tableRowTime=stratTime+"-"+newTime;
				 stratTime=newTime;
				
				 
				 objBean.setStrDay(tableRowTime);
				 objBean.setStrDay1("");
				 objBean.setStrDay2("");
				 objBean.setStrDay3("");
				 objBean.setStrDay4("");
				 objBean.setStrDay5("");
				 objBean.setStrDay6("");
				 objBean.setStrDay7("");
				 
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
	/*
	@RequestMapping(value = "/getRoomTypeWiseList", method = RequestMethod.GET)
	public @ResponseBody Map funLoadRoomTypeWiseStatus(@RequestParam("viewDate") String viewDate, HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String userCode = request.getSession().getAttribute("usercode").toString();
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
		
		
			
			
			Map objRoomStatusDtlBean = new HashMap<>();
			List listRoomStatus= new ArrayList<>();

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
					listRoomStatus.put(objRoomStatusDtlBean);
					objRoomStatusData.put(rsRoomInfo1.getString(1), strRoomData);
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

*/
}
