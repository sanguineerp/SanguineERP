package com.sanguine.webpms.controller;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import javax.validation.Valid;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import sun.nio.cs.ext.Big5;

import com.ibm.icu.math.BigDecimal;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webpms.bean.clsRoomCancellationBean;
import com.sanguine.webpms.bean.clsRoomMasterBean;
import com.sanguine.webpms.dao.clsWebPMSDBUtilityDao;
import com.sanguine.webpms.model.clsReservationDtlModel;
import com.sanguine.webpms.model.clsReservationHdModel;
import com.sanguine.webpms.model.clsRoomCancellationModel;
import com.sanguine.webpms.service.clsRoomCancellationService;



@Controller
public class clsRoomChangeController {
	@Autowired
	private clsRoomCancellationService objRoomCancellationService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@Autowired
	private clsWebPMSDBUtilityDao objWebPMSUtility;
	
	// Open RoomCancellation
	@RequestMapping(value = "/frmChangeRoom", method = RequestMethod.GET)
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
			return new ModelAndView("frmChangeRoom", "command", new clsRoomMasterBean());
		} else {
			return new ModelAndView("frmChangeRoom_1", "command", new clsRoomMasterBean());
		}
	}
	
	
	@RequestMapping(value = "/loadOccupiedRoomData", method = RequestMethod.GET)
	public @ResponseBody Map funGetOccupiedRoomList(HttpServletRequest req) 
	//private Map funGetOccupiedRoomList(HttpServletRequest req) 
	{
		clsRoomMasterBean objRoom = new clsRoomMasterBean();
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		
		List list =null;
		Map hmRoomData=new HashMap<>();
		
		try{
		
			String sql = "select d.strFolioNo,date(b.dteCheckInDate),concat(e.strFirstName,' ',e.strMiddleName,' ',e.strLastName) "
				+ " ,b.strRemarks,b.strReasonCode,a.strRoomDesc,c.strRoomNo,b.strCheckInNo,a.strStatus "
				+ " from tblfoliohd d,tblcheckinhd b left outer join tblcheckindtl c on b.strCheckInNo=c.strCheckInNo "
				+ " left outer join tblroom a on a.strRoomCode=c.strRoomNo , "
				+ " tblguestmaster e,tblroomtypemaster f  "
				+ " where a.strStatus!='Free' and a.strClientCode='"+clientCode+"' "
				+ " and b.strCheckInNo=d.strCheckInNo "
				+ " and c.strGuestCode=e.strGuestCode"
				+ " and f.strRoomTypeCode=c.strRoomType AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' AND c.strClientCode='"+clientCode+"' AND d.strClientCode='"+clientCode+"' AND e.strClientCode='"+clientCode+"' AND f.strClientCode='"+clientCode+"'"
				+ " group by a.strRoomCode order by c.strRoomType,a.strRoomDesc;";
			

			list = objWebPMSUtility.funExecuteQuery(sql, "sql");
			
			List listRoomData=new ArrayList<>();
			 if (list!=null)
				{
					for(int i=0; i<list.size(); i++)
					{
						Object obj=list.get(i);
						Map hmRoom=new HashMap<>();
						hmRoom.put("FolioNo",Array.get(obj, 0));
						hmRoom.put("CheckInDate",Array.get(obj, 1));
						hmRoom.put("GuestName",Array.get(obj, 2));
						hmRoom.put("Reason",Array.get(obj, 4));
						hmRoom.put("Remark",Array.get(obj, 3));
						hmRoom.put("RoomName",Array.get(obj, 5));
						hmRoom.put("RoomNo",Array.get(obj, 6));
						hmRoom.put("CheckinNo",Array.get(obj, 7));
						hmRoom.put("Status",Array.get(obj, 8));
						listRoomData.add(hmRoom);
					}
					hmRoomData.put("hmOccupiedRooms", listRoomData);
			      }
			
			 
//			 sql = "select a.strRoomCode,a.strRoomDesc,a.strStatus,a.strRoomTypeCode,b.strWalkInNo "
//			 		+ " from tblwalkinhd b left outer join tblwalkindtl c on b.strWalkinNo=c.strWalkinNo "
//			 		+ " left outer join tblroom a on a.strRoomCode=c.strRoomNo "
//			 		+ " where a.strStatus='Confirmed' and a.strClientCode='"+clientCode+"' "
//			 		+ " group by a.strRoomCode ";
//
//				list = objWebPMSUtility.funExecuteQuery(sql, "sql");
//				
//				if (list!=null)
//					{
//						for(int i=0; i<list.size(); i++)
//						{
//							Object obj=list.get(i);
//							Map hmRoom=new HashMap<>();
//							hmRoom.put("RoomNo",Array.get(obj, 0));
//							hmRoom.put("RoomName",Array.get(obj, 1));
//							hmRoom.put("RoomType",Array.get(obj, 3));
//							hmRoom.put("WalkinNo",Array.get(obj, 4));
//							hmRoom.put("Type","Walk In");
//							hmRoom.put("Status",Array.get(obj, 2));
//							listRoomData.add(hmRoom);
//						}
//						hmRoomData.put("hmOccupiedRooms", listRoomData);
//				      }
//				 
//				 
//				 sql = "select a.strRoomCode,a.strRoomDesc,a.strStatus,a.strRoomTypeCode,b.strReservationNo "
//				 		+ " from tblreservationhd b left outer join tblreservationdtl c on b.strReservationNo=c.strReservationNo "
//				 		+ " left outer join tblroom a on a.strRoomCode=c.strRoomNo "
//				 		+ " where a.strStatus='Waiting' and a.strClientCode='"+clientCode+"' "
//				 		+ " group by a.strRoomCode ";
//
//						list = objWebPMSUtility.funExecuteQuery(sql, "sql");
//						
//						if (list!=null)
//							{
//								for(int i=0; i<list.size(); i++)
//								{
//									Object obj=list.get(i);
//									Map hmRoom=new HashMap<>();
//									hmRoom.put("RoomNo",Array.get(obj, 0));
//									hmRoom.put("RoomName",Array.get(obj, 1));
//									hmRoom.put("RoomType",Array.get(obj, 3));
//									hmRoom.put("ReservationNo",Array.get(obj, 4));
//									hmRoom.put("Type","Reservation");
//									hmRoom.put("Status",Array.get(obj, 2));
//									listRoomData.add(hmRoom);
//								}
//								hmRoomData.put("hmOccupiedRooms", listRoomData);
//						      }	 
			    
			}catch(Exception ex)
			{
				ex.printStackTrace();
				
			}
			finally
			{
				return hmRoomData;
			}
	}
	
	
	
	@RequestMapping(value = "/loadAllRoomData", method = RequestMethod.GET)
	public @ResponseBody Map LoadAllRoomData(HttpServletRequest req) 
	{
		clsRoomMasterBean objRoom = new clsRoomMasterBean();
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		
		List list =null;
		Map hmRoomData=new HashMap<>();
		
		try{
		
			String sql = "SELECT a.strRoomCode,a.strRoomDesc,a.strStatus,a.strRoomTypeCode,b.strRoomTypeDesc "
						+ " FROM tblroom a,tblroomtypemaster b "
						+ " WHERE a.strRoomTypeCode=b.strRoomTypeCode AND (a.strStatus='Free' or a.strStatus='Blocked') AND a.strClientCode='"+clientCode+"'  AND b.strClientCode='"+clientCode+"' order by b.strRoomTypeCode,a.strRoomDesc";

			list = objWebPMSUtility.funExecuteQuery(sql, "sql");
			
			List listRoomData=new ArrayList<>();
			 if (list!=null)
				{
					for(int i=0; i<list.size(); i++)
					{
						Object obj=list.get(i);
						Map hmRoom=new HashMap<>();
						hmRoom.put("RoomNo",Array.get(obj, 0));
						hmRoom.put("RoomName",Array.get(obj, 1));
						hmRoom.put("RoomType",Array.get(obj, 3));
						hmRoom.put("RoomTypeDesc",Array.get(obj, 4));
						listRoomData.add(hmRoom);
					}
					hmRoomData.put("hmAllRooms", listRoomData);
			      }
			
			    
			}catch(Exception ex)
			{
				ex.printStackTrace();
				
			}
			finally
			{
				return hmRoomData;
			}
	}
	
	

	// Save or Update RoomCancellation
	@RequestMapping(value = "/changeRoom", method = RequestMethod.POST)
	public ModelAndView funAddUpdate(@ModelAttribute("command") @Valid clsRoomMasterBean objBean, BindingResult result, HttpServletRequest req) {
		if (!result.hasErrors()) 
		{
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String pmsDate = req.getSession().getAttribute("PMSDate").toString();
			String[] changedDate = pmsDate.split("-");
			String dteChangedRoom = changedDate[2]+"-"+changedDate[1]+"-"+changedDate[0];
			String strReasonCode = objBean.getStrReasonCode();
			String strRemarks = objBean.getStrRemarks();
			
			String sqlOfChangeRoom = "select a.strRoomDesc as previousRoom,a.strRoomTypeDesc as previousroomType,d.strFolioNo,b.strCheckInNo,date(b.dteCheckInDate),concat(e.strFirstName,' ',e.strMiddleName,' ',e.strLastName) "
						+ " ,b.strRemarks,b.strReasonCode,a.strStatus,e.strGuestCode "
						+ " from tblfoliohd d,tblcheckinhd b left outer join tblcheckindtl c on b.strCheckInNo=c.strCheckInNo " 
						+ " left outer join tblroom a on a.strRoomCode=c.strRoomNo , " 
						+ " tblguestmaster e  "
						+ " where  a.strStatus!='Free' and a.strClientCode='"+clientCode+"' " 
						+ " and b.strCheckInNo=d.strCheckInNo "
						+ " and c.strGuestCode=e.strGuestCode " 
						+ " and a.strRoomCode='"+objBean.getStrRoomCode().split("#")[0]+"' AND b.strClientCode='"+clientCode+"' AND c.strClientCode='"+clientCode+"' AND d.strClientCode='"+clientCode+"' AND e.strClientCode='"+clientCode+"'" ;
			List listData = objWebPMSUtility.funExecuteQuery(sqlOfChangeRoom, "sql"); 
			DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			if(listData.size()>0)
			{
				for(int i=0;i<listData.size();i++)
				{
					Object[] obj = (Object[]) listData.get(i);
					sqlOfChangeRoom = "insert into tblchangeroom (strRoomNo,strRoomTypeCode,strFolioNo,strGuestCode,strReason,strRemark,strUserEdited,dteChangeDate) "
							+ " values('"+obj[0].toString()+"','"+obj[1].toString()+"','"+obj[2].toString()+"','"+obj[9].toString()+"','"+strReasonCode+"','"+strRemarks+"','"+userCode+"','"+dteChangedRoom+" ')";
					objWebPMSUtility.funExecuteUpdate(sqlOfChangeRoom, "sql"); 
				
				}
			}
			

			String sqlNewRoomStatus = "select a.strStatus from tblroom a where a.strRoomCode='"+objBean.getStrRoomDesc()+"' AND a.strClientCode='"+clientCode+"'";
			List listNewRoomStatus = objWebPMSUtility.funExecuteQuery(sqlNewRoomStatus, "sql");
			String status=listNewRoomStatus.get(0).toString();
			if(status.equalsIgnoreCase("Blocked"))
			{
				JOptionPane.showMessageDialog(null, "This Room is Blocked Please select Different Room");
			}
			else
			{
			//strRoomCode means Occupied roomNo and strRoomDesc means Free Room NO
			String sql="update tblcheckindtl a set a.strRoomNo='"+objBean.getStrRoomDesc()+"' "
						+ " where a.strRoomNo='"+objBean.getStrRoomCode().split("#")[0]+"' AND a.strClientCode='"+clientCode+"'";
			objWebPMSUtility.funExecuteUpdate(sql, "sql"); 
				
			String prevCheckiInNo = objBean.getStrRoomType().split("\\.")[0];
			sql="update tblcheckinhd a set "
				+ "  a.strRemarks='"+objBean.getStrRemarks()+"', a.strReasonCode ='"+objBean.getStrReasonCode()+"'"
				+ " where a.strCheckInNo='"+prevCheckiInNo+"'  AND a.strClientCode='"+clientCode+"'";
			objWebPMSUtility.funExecuteUpdate(sql, "sql"); 
				
				
			sql = " select a.strRoomNo from tblfoliohd a where a.strCheckInNo='"+prevCheckiInNo+"' AND a.strClientCode='"+clientCode+"'";
			String selectedRoomNoOfFolio="";
			List list = objWebPMSUtility.funExecuteQuery(sql, "sql");
				
			if (list!=null)
			{
				for(int i=0; i<list.size(); i++)
				{
					Object obj=list.get(0);
					selectedRoomNoOfFolio = obj.toString();
				}
			}
				 
			sql="update tblfoliohd a  set a.strRoomNo='"+objBean.getStrRoomDesc()+"' "
				+ " where a.strRoomNo='"+selectedRoomNoOfFolio+"' and a.strCheckInNo='"+prevCheckiInNo+"' AND a.strClientCode='"+clientCode+"'"; 
			objWebPMSUtility.funExecuteUpdate(sql, "sql"); 
				
			
			
			sql="update tblroom a set a.strStatus='"+objBean.getStrRoomType().split("\\.")[1]+"'  where a.strRoomCode='"+objBean.getStrRoomDesc()+"' AND a.strClientCode='"+clientCode+"'";
			objWebPMSUtility.funExecuteUpdate(sql, "sql"); 
			
			sql="update tblroom a set a.strStatus='Free' where a.strRoomCode='"+objBean.getStrRoomCode().split("#")[0]+"' AND a.strClientCode='"+clientCode+"'";
			objWebPMSUtility.funExecuteUpdate(sql, "sql"); 
			
			sql = "select b.dblRoomTerrif,b.strRoomTypeCode from tblroomtypemaster b where  b.strClientCode='"+clientCode+"' AND b.strRoomTypeCode="
					+ "(select a.strRoomTypeCode from  tblroom a where a.strClientCode='"+clientCode+"' AND  a.strRoomCode='"+objBean.getStrRoomDesc()+"')"; 
			List listDataRate = objWebPMSUtility.funExecuteQuery(sql, "sql");
			double newRate=0.0;
			String newRoomTypeCode="";
			for (int cnt = 0; cnt < listDataRate.size(); cnt++) {
				Object[] arrObjBillTaxDtl = (Object[]) listDataRate.get(cnt);
				newRate =Double.parseDouble(arrObjBillTaxDtl[0].toString());
				newRoomTypeCode =arrObjBillTaxDtl[1].toString();
			}
			
					
					
			
			sql = "update tblwalkinroomratedtl b set b.dblRoomRate='"+newRate+"', b.strRoomType='"+newRoomTypeCode+"' where b.strWalkinNo="
					+ "(select a.strWalkInNo from tblcheckinhd a where a.strCheckInNo='"+prevCheckiInNo+"' AND b.strClientCode='"+clientCode+"')  AND b.strClientCode='"+clientCode+"';;";
			objWebPMSUtility.funExecuteUpdate(sql, "sql");
			
			
			sql = "update tblcheckindtl a set a.strRoomNo='"+objBean.getStrRoomDesc()+"'  where a.strCheckInNo='"+prevCheckiInNo+"'  AND a.strClientCode='"+clientCode+"'";
			objWebPMSUtility.funExecuteUpdate(sql, "sql");
			
            req.getSession().setAttribute("success", true);
			req.getSession().setAttribute("successMessage", "Reservation No. : ".concat(objBean.getStrRoomCode().split("#")[0]));
			
			}
			return new ModelAndView("redirect:/frmChangeRoom.html");
		} else {
			return new ModelAndView("frmChangeRoom");
		}
	}

	
	
	
}
