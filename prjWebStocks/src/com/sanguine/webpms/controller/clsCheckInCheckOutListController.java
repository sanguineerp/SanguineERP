package com.sanguine.webpms.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webpms.bean.clsCheckInChecOutReportListBean;
import com.sanguine.webpms.bean.clsCheckOutListReportBean;
import com.sanguine.webpms.bean.clsFolioPrintingBean;
import com.sanguine.webpms.bean.clsGuestMasterBean;
import com.sanguine.webpms.service.clsFolioService;

@Controller
public class clsCheckInCheckOutListController {
	
	
	@Autowired
	private clsFolioService objFolioService;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	@Autowired
	private clsGlobalFunctions objGlobal;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private clsSetupMasterService objSetupMasterService;
	
	
	// Open Folio Printing
		@RequestMapping(value = "/frmCheckInCheckOutList", method = RequestMethod.GET)
		public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
			String urlHits = "1";
			try {
				urlHits = request.getParameter("saddr").toString();
			} catch (NullPointerException e) {
				urlHits = "1";
			}
			model.put("urlHits", urlHits);
			if ("2".equalsIgnoreCase(urlHits)) {
				return new ModelAndView("frmCheckInCheckOutList_1", "command", new clsCheckInChecOutReportListBean());
			} else if ("1".equalsIgnoreCase(urlHits)) {
				return new ModelAndView("frmCheckInCheckOutList", "command", new clsCheckInChecOutReportListBean());
			} else {
				return null;
			}
		}
		
		
		
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@RequestMapping(value = "/CheckInCheckOutList", method = RequestMethod.GET)
		public ModelAndView funPMSCheckInCheckOutExcelExport(clsCheckInChecOutReportListBean objBean,HttpServletRequest request) {
			String clientCode = request.getSession().getAttribute("clientCode").toString();
			String companyName = request.getSession().getAttribute("companyName").toString();
			//String LocCode = "";
			List list = new ArrayList<>();
			List AllGuestlist= new ArrayList();
			List DataGuestList=null;
			
			
			String dteArrDate = objGlobal.funGetDate("yyyy-MM-dd", objBean.getStrArrivedDate());
			String dteDepDate = objGlobal.funGetDate("yyyy-MM-dd", objBean.getDteDepartureDate());
			String header = "Name Of Hotel / Guest House,Name Of Hotel Owner,Name ,Address of Passenger,Mobile No.,Passenger Passport/visa details,Propose Of visit,Total Days Of stay,Arrived Date,Departure Date";
			List ExportList = new ArrayList();
			
			String repeortfileName = "CheckIn-CheckOutReport";
			repeortfileName = repeortfileName.replaceAll(" ", "");
			ExportList.add(repeortfileName);
			
			String[] ExcelHeader = header.split(",");
			ExportList.add(ExcelHeader);
			try{
			String sql="SELECT CONCAT(a.strFirstName,' ',a.strMiddleName,' ',a.strLastName),"
					+ "CONCAT(a.strAddressLocal,' ',a.strCityLocal,' ',a.strStateLocal,' ',a.strCountryLocal,' ',a.intPinCodeLocal),a.lngMobileNo,a.strPassportNo, "
					+ "a.strVisitingType, DATEDIFF(DATE(c.dteDepartureDate), DATE(c.dteArrivalDate)), DATE(c.dteArrivalDate), DATE(c.dteDepartureDate) "
					+ "FROM tblguestmaster a,tblcheckindtl b,tblcheckinhd c "
					+ "WHERE a.strGuestCode=b.strGuestCode and b.strCheckInNo=c.strCheckInNo "
					+ "and Date(c.dteArrivalDate) between '"+dteArrDate+"' and '"+dteDepDate+"' AND a.strClientCode='"+clientCode+"' AND b.strClientCode='"+clientCode+"' AND c.strClientCode='"+clientCode+"';";        
			
			list=objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			if(!list.isEmpty())
		   {
				for (int i = 0; i < list.size(); i++)
				{
		            Object[] obj = (Object[]) list.get(i);
		            DataGuestList=new ArrayList<>();
		            DataGuestList.add(companyName);
		            DataGuestList.add("Mr.Ramesh Maihar");
		            DataGuestList.add(obj[0].toString());
		            DataGuestList.add(obj[1].toString());
		            DataGuestList.add(obj[2].toString());
		            DataGuestList.add(obj[3].toString());
		            DataGuestList.add(obj[4].toString());
		            DataGuestList.add(obj[5].toString());
		            DataGuestList.add(objGlobal.funGetDate("dd-MM-yyyy", obj[6].toString()));
		            DataGuestList.add(objGlobal.funGetDate("dd-MM-yyyy", obj[7].toString()));
		            
		             
		             
		             
		             AllGuestlist.add(DataGuestList);
				}
			}
			//
			
			}
			catch(Exception ex){
				ex.printStackTrace();
				}
			ExportList.add(AllGuestlist);
			//excelView
			
			return new ModelAndView("excelViewWithReportName", "listWithReportName", ExportList);
		}


}
