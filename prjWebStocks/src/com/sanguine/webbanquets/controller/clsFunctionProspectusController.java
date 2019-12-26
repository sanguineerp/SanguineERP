package com.sanguine.webbanquets.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.crm.bean.clsInvoiceDtlBean;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webbanquets.bean.clsFunctionProspectusBean;
import com.sanguine.webbanquets.model.clsMenuHeadMasterModel;
import com.sanguine.webpms.bean.clsFolioPrintingBean;

@Controller
public class clsFunctionProspectusController
{
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	@Autowired
	private clsSetupMasterService objSetupMasterService;
	@Autowired
	private ServletContext servletContext;
	
	@RequestMapping(value = "/frmFunctionProspectus", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request)
	{
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmFunctionProspectus_1", "command", new clsFunctionProspectusBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmFunctionProspectus", "command", new clsFunctionProspectusBean());
		} else {
			return null;
		}
	}
	
	@RequestMapping(value = "/rptFunctionProspectus", method = RequestMethod.POST)
	public void funGenerateFunctionProspectus(clsFunctionProspectusBean objFunctionProspectusBean, HttpServletRequest req, HttpServletResponse resp) 
	{
		try 
		{
			funGenarateFunctionProspectus(objFunctionProspectusBean.getStrBookingNo(),req, resp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
		 
	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	public void funGenarateFunctionProspectus(String strBookingNo,HttpServletRequest req,HttpServletResponse resp) throws Exception 
	{
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String userCode = req.getSession().getAttribute("usercode").toString();
		String propertyCode = req.getSession().getAttribute("propertyCode").toString();
		String companyName = req.getSession().getAttribute("companyName").toString();
		String webStockDB = req.getSession().getAttribute("WebStockDB").toString();
		String sql="";
		String fromDate="",toDate="";
		String strServiceType="";
		HashMap reportParams = new HashMap();
		clsFunctionProspectusBean objBean=null;
		List listStaff = new ArrayList<>();
		List listEquipment = new ArrayList<>();
		List listService = new ArrayList<>();
		List listMenu = new ArrayList<>();
		List listExtService = new ArrayList<>();
		List listIntService = new ArrayList<>();
		
		String reportName = servletContext.getRealPath("/WEB-INF/reports/webbanquet/rptFunctionProspectus.jrxml");
		String imagePath = servletContext.getRealPath("/resources/images/Sanguine_Logo_Icon.png");
		clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
		if (objSetup == null) {
			objSetup = new clsPropertySetupModel();
		}		
		
		sql="SELECT a.strBookingNo,b.strType,a.strCustomerCode,a.dteBookingDate,c.strLocName,a.strFunctionCode,a.intMinPaxNo,"
				+ "a.intMaxPaxNo,a.tmeFromTime,a.tmeToTime,ifnull(d.strStaffName,''),ifnull(d.strMobile,''),ifnull(d.strEmail,''),ifnull(g.strBanquetName,''),DATE(a.dteToDate)- DATE(a.dteFromDate) FROM tblbqbookinghd a "
				+ "LEFT OUTER JOIN tblbqbookingdtl b ON b.strBookingNo=a.strBookingNo LEFT OUTER "
				+ "JOIN "+webStockDB+".tbllocationmaster c ON c.strLocCode=a.strAreaCode LEFT OUTER JOIN "
				+ "tblstaffmaster d ON d.strStaffCode=a.strEventCoordinatorCode LEFT OUTER "
				+ "JOIN "+webStockDB+".tblpartymaster e ON e.strPCode=a.strCustomerCode LEFT OUTER JOIN  "
				+ "tblfunctionmaster f ON f.strFunctionCode=a.strFunctionCode LEFT OUTER  "
                + "JOIN tblbanquetmaster g ON a.strBanquetCode=g.strBanquetCode WHERE a.strBookingNo=b.strBookingNo "
				+ "AND a.strBookingNo='"+strBookingNo+"' AND a.strClientCode='"+clientCode+"' "
				+ "GROUP BY b.strType";
		
		List listData = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
		
		for(int i=0;i<listData.size();i++)
		{
			Object[] obj = (Object[])listData.get(i);
			strServiceType=obj[1].toString();
			reportParams.put("pArea", obj[4].toString());
			reportParams.put("pEventCoordinator", obj[10].toString());
			reportParams.put("pMobile", obj[11].toString());
			reportParams.put("pEmail", obj[12].toString());
			reportParams.put("pBanquetName", obj[13].toString());
			if(obj[2].toString().startsWith("C"))
			{
				sql="SELECT a.strPCode,a.strPName,a.strOperational FROM tblpartymaster a WHERE a.strPCode='"+obj[2].toString()+"' "
					+ "AND a.strPropCode='"+propertyCode+"' AND a.strOperational='Y' AND a.strClientCode='"+clientCode+"'";
				List listCustDetails = objGlobalFunctionsService.funGetList(sql, "sql");
				for(int m=0;m<listCustDetails.size();m++)
				{
					Object[] objCust = (Object[])listCustDetails.get(m);
					reportParams.put("pCustomerName", objCust[1].toString());
				}
			}
			if(obj[5].toString().startsWith("FM"))
			{
				sql="SELECT b.strType,b.strDocName,c.strFunctionName,b.dblDocQty FROM tblbqbookinghd a LEFT OUTER JOIN tblbqbookingdtl b "
						+ "ON b.strBookingNo=a.strBookingNo LEFT OUTER JOIN tblfunctionmaster c ON "
						+ "c.strFunctionCode=a.strFunctionCode WHERE a.strBookingNo='"+strBookingNo+"' "
						+ "AND c.strFunctionCode='"+obj[5].toString()+"' AND c.strOperationalYN='Y' AND b.strType='"+strServiceType+"' AND a.strPropertyCode='"+propertyCode+"' "
						+ "AND a.strClientCode='"+clientCode+"';";
					List listFunction = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
					for(int k=0;k<listFunction.size();k++)
					{
						Object[] objFunction = (Object[])listFunction.get(k);
						reportParams.put("pFunction", objFunction[2].toString());
						if(strServiceType.equals("Staff"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
					        objBean.setStrQty(objFunction[3].toString());
							listStaff.add(objBean);
						}
						else if(strServiceType.equals("Service"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							listService.add(objBean);
						}
						else if(strServiceType.equals("Equipment"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							 objBean.setStrQty(objFunction[3].toString());
							listEquipment.add(objBean);
						}
						else if(strServiceType.equals("Menu"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							objBean.setStrQty(objFunction[3].toString());
						    listMenu.add(objBean);
						}
						else if(strServiceType.equals("Ex Service"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							objBean.setStrQty(objFunction[3].toString());
						    listExtService.add(objBean);
						}
						else if(strServiceType.equals("In Service"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							objBean.setStrQty(objFunction[3].toString());
						    listIntService.add(objBean);
						}
						/*else if(strServiceType.equals("Banquet"))
						{
							objBean = new clsFunctionProspectusBean();
							objBean.setStrServiceType(strServiceType);
							objBean.setStrService(objFunction[1].toString());
							objBean.setStrQty(objFunction[3].toString());
						    listMenu.add(objBean);
						}*/
					}
			}
			
			reportParams.put("pBookingNo", obj[0].toString());
			reportParams.put("pDate", obj[3].toString().substring(0,obj[3].toString().indexOf(" ")).split("-")[2]+"-"+obj[3].toString().substring(0,obj[3].toString().indexOf(" ")).split("-")[1]+"-"+obj[3].toString().substring(0,obj[3].toString().indexOf(" ")).split("-")[0]);
			reportParams.put("pTime", obj[3].toString().substring(obj[3].toString().indexOf(" "),obj[3].toString().length()-3));
			reportParams.put("pPAX", obj[6].toString()+" - "+obj[7].toString());
			int k=(int) Math.round(Double.parseDouble(obj[14].toString()));			
			String frTime[]=obj[8].toString().split(":");
			String toTime[]=obj[9].toString().split(":");
			int hrsCnt=Integer.parseInt(frTime[0])-Integer.parseInt(toTime[0]);
			int minCnt=Integer.parseInt(frTime[1])-Integer.parseInt(toTime[1]);
			
			String hrsCount=null;
			if(String.valueOf(hrsCnt).startsWith("-"))
			{
				String hrsValue[]=String.valueOf(hrsCnt).split("-");
				hrsCount=hrsValue[1];
			}
			else
			{
				hrsCount=String.valueOf(hrsCnt);
			}
			String minCount=null;
			if(String.valueOf(minCnt).startsWith("-"))
			{
				String hrsValue[]=String.valueOf(minCnt).split("-");
				minCount=hrsValue[1];
			}
			else
			{
				minCount=String.valueOf(minCnt);
			}
			reportParams.put("pDuration",k+" Day"+ " "+hrsCount+" Hours "+minCount+" Minutes");
			
			
			//reportParams.put("pDuration",obj[8].toString()+" - "+obj[9].toString());
			
		}
		reportParams.put("pCompanyName", companyName);
		reportParams.put("pAddress1", objSetup.getStrAdd1() + "," + objSetup.getStrAdd2() + "," + objSetup.getStrCity());
		reportParams.put("pAddress2", objSetup.getStrState() + "," + objSetup.getStrCountry() + "," + objSetup.getStrPin());
		reportParams.put("strUserCode", userCode);
		reportParams.put("strImagePath", imagePath);
		reportParams.put("listStaff", listStaff);
		reportParams.put("listEquipment", listEquipment);
		reportParams.put("listService", listService);
		reportParams.put("listMenu", listMenu);
		reportParams.put("listExtService", listExtService);
		reportParams.put("listIntService", listIntService);
		
		
		JasperDesign jd = JRXmlLoader.load(reportName);
		JasperReport jr = JasperCompileManager.compileReport(jd);
		JasperPrint jp = JasperFillManager.fillReport(jr, reportParams,new JREmptyDataSource());
		List<JasperPrint> jprintlist = new ArrayList<JasperPrint>();
		if (jp != null) 
		{
			jprintlist.add(jp);
			ServletOutputStream servletOutputStream = resp.getOutputStream();
			JRExporter exporter = new JRPdfExporter();
			resp.setContentType("application/pdf");
			exporter.setParameter(JRPdfExporterParameter.JASPER_PRINT_LIST, jprintlist);
			exporter.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, servletOutputStream);
			exporter.setParameter(JRPdfExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
			resp.setHeader("Content-Disposition", "inline;filename=FunctionProspectus_" + fromDate + "_To_" + toDate + "_" + userCode + ".pdf");
			exporter.exportReport();
			servletOutputStream.flush();
			servletOutputStream.close();
		}
	
		
	}
	

}
