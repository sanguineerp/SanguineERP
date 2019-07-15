package com.sanguine.webpms.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsDayEndBean;
import com.sanguine.webpms.bean.clsPostRoomTerrifBean;
import com.sanguine.webpms.dao.clsWebPMSDBUtilityDao;
import com.sanguine.webpms.model.clsDayEndHdModel;
import com.sanguine.webpms.service.clsDayEndService;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.ResponseBody;

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
		// Check POS Day End Table in PMS
		
		String sqlBlockedRoom = "select a.strRoomCode,DATE(a.dteValidTo) from tblblockroom a ";
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
			String propCode = req.getSession().getAttribute("propertyCode").toString();
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
			
			String sql="SELECT a.strFolioNo,a.strRoomNo,c.dblRoomTerrif,a.strExtraBedCode, IFNULL(a.strReservationNo,''), IFNULL(a.strWalkInNo,''),"
					+ "c.strRoomTypeCode, IFNULL(SUM(d.dblIncomeHeadAmt),0),e.strComplimentry "
					+ "FROM tblfoliohd a "
					+ "LEFT OUTER JOIN tblroompackagedtl d ON a.strCheckInNo=d.strCheckInNo,tblroom b,tblroomtypemaster c,tblcheckinhd e "
					+ "WHERE a.strRoomNo=b.strRoomCode AND b.strRoomTypeCode=c.strRoomTypeCode AND a.strCheckInNo=e.strCheckInNo "
					+ "GROUP BY a.strFolioNo";
			List listRoomInfo = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
             
			for (int cnt = 0; cnt < listRoomInfo.size(); cnt++) 
			{
				clsPostRoomTerrifBean objPostRoomTerrifBean = new clsPostRoomTerrifBean();
				Object[] arrObjRoom = (Object[]) listRoomInfo.get(cnt);
				double dblRoomRate=0.0;
				
				if(!arrObjRoom[4].toString().equals(""))
				{
					 String sqlRoomRate=" select a.dblRoomRate from  tblreservationroomratedtl a "
						        +" where a.strReservationNo='"+arrObjRoom[4].toString()+"' and a.strClientCode='"+clientCode+"' and a.strRoomType='"+arrObjRoom[6].toString()+"' and a.dtDate='"+date+"' ";
					 List listRoomRate = objGlobalFunctionsService.funGetListModuleWise(sqlRoomRate, "sql");
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
				if(arrObjRoom[8].toString().equals("Y"))
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
				String docNo = objPostRoomTerrif.funInsertFolioRecords(folioNo, clientCode, propCode, objPostRoomTerrifBean, objGlobal.funGetDate("yyyy-MM-dd", PMSDate), arrObjRoom[3].toString());
				listRoomTerrifDocNo.add(docNo);
				if(Double.valueOf(arrObjRoom[7].toString())>0)
				{   
					dblRoomRate=Double.valueOf(arrObjRoom[7].toString())/2;
					objPostRoomTerrifBean = new clsPostRoomTerrifBean();
					objPostRoomTerrifBean.setStrFolioNo(arrObjRoom[0].toString());
					objPostRoomTerrifBean.setStrRoomNo(arrObjRoom[1].toString());
					objPostRoomTerrifBean.setDblRoomTerrif(dblRoomRate);
					objPostRoomTerrifBean.setDblOriginalPostingAmt(dblRoomRate);
					objPostRoomTerrifBean.setStrFolioType("Package");
					folioNo = arrObjRoom[0].toString();
					docNo=objPostRoomTerrif.funInsertFolioRecords(folioNo, clientCode, propCode, objPostRoomTerrifBean, objGlobal.funGetDate("yyyy-MM-dd", PMSDate), arrObjRoom[3].toString());	
					listRoomTerrifDocNo.add(docNo);
				}
				
			}

			
			double dayEndAmt = 0;
			for (int cnt = 0; cnt < listRoomTerrifDocNo.size(); cnt++) {
				sql = "select sum(dblDebitAmt) from tblfoliodtl " + " where strDocNo='" + listRoomTerrifDocNo.get(cnt) + "' group by strDocNo";
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
			String[] arrSpDate = objBean.getDtePMSDate().split("-");
			// Date dtNextDate=new
			// Date(Integer.parseInt(arrSpDate[2]),Integer.parseInt(arrSpDate[1]),Integer.parseInt(arrSpDate[0]));
			Date dtNextDate = new Date(Integer.parseInt(arrSpDate[0]), Integer.parseInt(arrSpDate[1]), Integer.parseInt(arrSpDate[2]));
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(dtNextDate);
			cal.add(Calendar.DATE, 1);
			String newStartDate = cal.getTime().getYear() + "-" + (cal.getTime().getMonth()) + "-" + (cal.getTime().getDate());
			clsDayEndHdModel objModel = new clsDayEndHdModel();
			objModel.setStrClientCode(clientCode);
			objModel.setStrPropertyCode(propCode);
			objModel.setStrStartDay("Y");
			objModel.setStrDayEnd("N");
			objModel.setDblDayEndAmt(0);
			objModel.setDtePMSDate(newStartDate);
			objModel.setStrUserCode(userCode);
			objDayEndService.funAddUpdateDayEndHd(objModel);

			String sqlUpdateDepartureDate = "update tblcheckinhd a set a.dteDepartureDate='"+newStartDate+"' where a.strClientCode='"+clientCode+"'  ";
			objWebPMSUtility.funExecuteUpdate(sqlUpdateDepartureDate, "sql"); 
			model= new ModelAndView("redirect:/frmModuleSelection.html");
		}
		return model;
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

}
