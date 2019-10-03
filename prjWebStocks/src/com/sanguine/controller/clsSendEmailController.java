package com.sanguine.controller;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.Connection;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsProductMasterService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.util.clsReportBean;
import com.sun.istack.ByteArrayDataSource;

@Controller
public class clsSendEmailController
{

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private clsProductMasterService objProdMasterService;
	@Autowired
	private clsSetupMasterService objSetupMasterService;
	private clsGlobalFunctions objGlobal = null;
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	final static Logger logger = Logger.getLogger(clsSendEmailController.class);

	@Autowired
	private clsPurchaseIndentHdController objPurchaseIndentHdController;
	
	@Autowired
	private clsMISController objMISController;
	
	
	

	
	/**
	 * Open Email Form
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/frmEmailSending", method = RequestMethod.GET)
	public ModelAndView funOpenEmailFrom(HttpServletRequest req)
	{
		return new ModelAndView("frmEmailSending");
	}

	/**
	 * Sending Email
	 * 
	 * @param request
	 * @param attachFile
	 * @return
	 * @throws JRException
	 */
	/*@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sendPOEmail", method = RequestMethod.GET)
	public @ResponseBody String doSendEmail(HttpServletRequest request) throws JRException
	{
		// takes input from e-mail form
		try
		{
			String strPOCode = request.getParameter("strPOCode");
			String subject ="Purchase Order Generated From "+request.getSession().getAttribute("locationName").toString();
			String message = "Purchase Order Slip";
			String strReturnValue = "Email Send SuccessFully";

			String clientCode = request.getSession().getAttribute("clientCode").toString();
			String propertyCode = request.getSession().getAttribute("propertyCode").toString();
			String userCode = request.getSession().getAttribute("usercode").toString();
			
		
			String sql="SELECT CONCAT(ifnull(b.strEmail,''),',',ifnull(c.strEmail,''),',',ifnull(d.strEmail,''))EmailIds "
			+"FROM tblpurchaseorderhd a "
					+"left outer join tblpartymaster b on a.strSuppCode=b.strPCode "
					+"left outer join tbluserhd c on a.strUserModified=c.strUserCode "
					+"left outer join tblpropertysetup d on a.strPropCode=d.strPropertyCode  "
					+"WHERE  a.strPOCode='"+strPOCode+"'  "
					+"AND c.strUserCode='"+userCode+"'  "
					+"AND d.strPropertyCode='"+propertyCode+"'";
			List list = objGlobalFunctionsService.funGetList(sql, "sql");
			if (list != null && !list.isEmpty())
			{
				String recipientAddress = list.get(0).toString();

				String []receipientsArr = recipientAddress.split(",");
                for(int i=0;i<receipientsArr.length;i++)
                {
                	if(receipientsArr[i].toString().trim().length()>0)
                	{
                	
                		logger.info("To: " + receipientsArr[i].toString());
        				logger.info("Subject: " + subject);
        				logger.info("Message: " + message);
        			
        				JasperPrint p = funCallRangePrintReport(strPOCode, request);// Calling
        																			// Attachment
        				ByteArrayOutputStream baos = new ByteArrayOutputStream();
        				JasperExportManager.exportReportToPdfStream(p, baos);
        				DataSource aAttachment = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
        				MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true);

        				helper.setTo(receipientsArr[i].toString());
        				helper.setSubject(subject);
        				helper.addAttachment("PO Slip.pdf", aAttachment);
        				helper.setText(message);
        				mailSender.send(helper.getMimeMessage());
                	}                	
                }
				

			}
			else
			{
				strReturnValue = "Supplier has No Email Id";
			}

			return strReturnValue;
		}
		catch (javax.mail.MessagingException e)
		{
			e.printStackTrace();
			logger.info(e);
			return e.toString();
		}

	}
	*/
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sendPOEmail", method = RequestMethod.GET)
	public @ResponseBody String doSendEmail(HttpServletRequest request) throws JRException
	{
		// takes input from e-mail form
		try
		{
			String strPOCode = request.getParameter("strPOCode");
		    String subject=request.getParameter("strSubject");
		    String message=request.getParameter("strMessage");
			//String subject ="Purchase Order Generated From "+request.getSession().getAttribute("locationName").toString();
			//String message = "Purchase Order Slip";
		    if(subject==null || message==null)
		    {
		    	subject ="Purchase Order Generated From "+request.getSession().getAttribute("locationName").toString();
		    	message = "Purchase Order Slip";
		    }
			String strReturnValue = "Email Send SuccessFully";

			String clientCode = request.getSession().getAttribute("clientCode").toString();
			String propertyCode = request.getSession().getAttribute("propertyCode").toString();
			String userCode = request.getSession().getAttribute("usercode").toString();
			
		
			String sql="SELECT CONCAT(ifnull(b.strEmail,''),',',ifnull(c.strEmail,''),',',ifnull(d.strEmail,''))EmailIds "
			+"FROM tblpurchaseorderhd a "
					+"left outer join tblpartymaster b on a.strSuppCode=b.strPCode "
					+"left outer join tbluserhd c on a.strUserModified=c.strUserCode "
					+"left outer join tblpropertysetup d on a.strPropCode=d.strPropertyCode  "
					+"WHERE  a.strPOCode='"+strPOCode+"'  "
					+"AND c.strUserCode='"+userCode+"'  "
					+"AND d.strPropertyCode='"+propertyCode+"'";
			List list = objGlobalFunctionsService.funGetList(sql, "sql");
			if (list != null && !list.isEmpty())
			{
				String recipientAddress = list.get(0).toString();

				String []receipientsArr = recipientAddress.split(",");
                for(int i=0;i<receipientsArr.length;i++)
                {
                	if(receipientsArr[i].toString().trim().length()>0)
                	{
                	
                		logger.info("To: " + receipientsArr[i].toString());
        				logger.info("Subject: " + subject);
        				logger.info("Message: " + message);
        			
        				JasperPrint p = funCallRangePrintReport(strPOCode, request);// Calling
        																			// Attachment
        				ByteArrayOutputStream baos = new ByteArrayOutputStream();
        				JasperExportManager.exportReportToPdfStream(p, baos);
        				DataSource aAttachment = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
        				MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true);

        				helper.setTo(receipientsArr[i].toString());
        				helper.setSubject(subject);
        				helper.addAttachment("PO Slip.pdf", aAttachment);
        				helper.setText(message);
        				mailSender.send(helper.getMimeMessage());
                	}                	
                }
				

			}
			else
			{
				strReturnValue = "Supplier has No Email Id";
			}

			return strReturnValue;
		}
		catch (javax.mail.MessagingException e)
		{
			e.printStackTrace();
			logger.info(e);
			return e.toString();
		}

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JasperPrint funCallReport(String POcode, HttpServletRequest req) throws MessagingException
	{
		JasperPrint p = null;
		try
		{
			objGlobal = new clsGlobalFunctions();
			Connection con = objGlobal.funGetConnection(req);
			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String companyName = req.getSession().getAttribute("companyName").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();

			clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
			if (objSetup == null)
			{
				objSetup = new clsPropertySetupModel();
			}
			//String reportName = servletContext.getRealPath("/WEB-INF/reports/rptPurchaseOrderSlip.jrxml");
			String reportName=servletContext.getRealPath("/WEB-INF/reports/rptPurchaseOrderSlipForAll.jrxml");
			if(objSetup.getStrPOSlipFormat().equalsIgnoreCase("Format 1"))
			{
				reportName = servletContext.getRealPath("/WEB-INF/reports/rptPurchaseOrderSlip.jrxml");
				if (clientCode.equals("226.001")) {
					reportName = servletContext.getRealPath("/WEB-INF/reports/rptPurchaseOrderSlip2.jrxml");
				}
			}
			else if(objSetup.getStrPOSlipFormat().equalsIgnoreCase("Format 2"))
			{
				reportName = servletContext.getRealPath("/WEB-INF/reports/rptPurchaseOrderSlipWithoutAmtRateAndDisc.jrxml");
			}
			else if(objSetup.getStrPOSlipFormat().equalsIgnoreCase("Format 3")){
				reportName = servletContext.getRealPath("/WEB-INF/reports/rptPurchaseOrderSlipForAll.jrxml");
			}
			
			String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
			String sql = "SELECT po.strPOCode,date(po.dtPODate) as dtPODate,po.strSuppCode,po.strAgainst,po.strSOCode,po.dblFinalAmt," + " po.strVAddress1,po.strVAddress2,po.strVCity,po.strVState,po.strVCountry,po.strVPin,po.strSAddress1," + " po.strSAddress2,po.strSCity,po.strSState,po.strSCountry,po.strSPin,po.strYourRef,po.strPerRef,po.strEOE," + " po.strCode,date(po.dtDelDate) as dtDelDate,po.dblExtra,po.strFinalAmtInWord as AmtInWords,po.dblDiscount," + " po.strPayMode,po.strCurrency,po.strAmedment,po.strAmntNO,po.stredit,po.strUserAmed,date(po.dtPayDate) as dtPayDate," + " po.dblConversion,po.dtLastModified,po.strAuthorise,po.dblTaxAmt,s.strPName,s.strContact,s.strPhone,s.strMobile,s.strFax, s.strMAdd1, s.strMAdd2,s.strMCity," + " s.strMPin,s.strMState,s.strMCountry,u.strUserName,u.strSignatureImg FROM tblpurchaseorderhd AS po " + " left outer JOIN  tblpartymaster AS s ON po.strSuppCode = s.strPCode and s.strClientCode='" + clientCode + "' " + " left outer join  tbluserhd u on po.strUserModified=u.strUserCode and u.strClientCode='" + clientCode + "' " + " left outer join tblcurrencymaster c on po.strCurrency=c.strShortName and c.strClientCode='" + clientCode + "' " + " WHERE (po.strPOCode = '" + POcode + "' and po.strClientCode='" + clientCode + "' ) ";
			JasperDesign jd = JRXmlLoader.load(reportName);
			JRDesignQuery newQuery = new JRDesignQuery();
			newQuery.setText(sql);
			jd.setQuery(newQuery);
			String sql2 = "SELECT  1 slno,   po.strProdCode,p.strProdName as strProdName, p.strUOM, po.dblOrdQty, po.dblPrice," + " po.dbldiscount,po.dblOrdQty*po.dblPrice-po.dbldiscount dblamt,  p.strPartNo,po.dblWeight,p.strCalAmtOn," + " po.strProdChar,p.strSpecification,po.strProcessCode,pr.strProcessName,po.strRemarks,ph.strEOE,po.strUpdate" + " FROM  tblpurchaseorderdtl po  INNER JOIN tblpurchaseorderhd ph ON po.strPOCode = ph.strPOCode and ph.strClientCode='" + clientCode + "' " + " INNER JOIN tblproductmaster p ON po.strProdCode = p.strProdCode and p.strClientCode='" + clientCode + "' " + " left outer JOIN tblprocessmaster pr ON po.strProcessCode = pr.strProcessCode and pr.strClientCode='" + clientCode + "' " + "  where po.strPOCode='" + POcode + "' and po.strClientCode='" + clientCode + "' order by po.intindex";
			JRDesignQuery subQuery = new JRDesignQuery();
			subQuery.setText(sql2);
			Map<String, JRDataset> datasetMap = jd.getDatasetMap();
			JRDesignDataset subDataset = (JRDesignDataset) datasetMap.get("dsPODtl");
			subDataset.setQuery(subQuery);
			String sql3 = "select a.strTCName,if(b.strTCDesc='null','',b.strTCDesc) as strTCDesc  from tbltctransdtl b,tbltcmaster a" + " where b.strTCCode=a.strTCCode and  b.strTransCode='" + POcode + "'" + " and b.strTransType='Purchase Order' and  b.strClientCode='" + clientCode + "'  and a.strClientCode='" + clientCode + "' ";
			JRDesignQuery subQuery1 = new JRDesignQuery();
			subQuery1.setText(sql3);
			JRDesignDataset subDataset1 = (JRDesignDataset) datasetMap.get("dsTC");
			subDataset1.setQuery(subQuery1);

			String taxQuery = " select a.strTaxDesc,a.strTaxAmt from tblpotaxdtl a where strPOCode='" + POcode + "' and strClientCode='" + clientCode + "' ";
			JRDesignQuery subQuery2 = new JRDesignQuery();
			subQuery2.setText(taxQuery);
			JRDesignDataset subDataset2 = (JRDesignDataset) datasetMap.get("dsTax");
			subDataset2.setQuery(subQuery2);
			JasperReport jr = JasperCompileManager.compileReport(jd);
			HashMap hm = new HashMap();
			hm.put("strCompanyName", companyName);
			hm.put("strUserCode", userCode.toUpperCase());
			hm.put("strImagePath", imagePath);
			hm.put("strAddr1", objSetup.getStrAdd1());
			hm.put("strAddr2", objSetup.getStrAdd2());
			hm.put("strCity", objSetup.getStrCity());
			hm.put("strState", objSetup.getStrState());
			hm.put("strCountry", objSetup.getStrCountry());
			hm.put("strPin", objSetup.getStrPin());
			hm.put("strFax", objSetup.getStrFax());
			hm.put("strPhoneNo", objSetup.getStrPhone());
			hm.put("strEmailAddress", objSetup.getStrEmail());
			hm.put("strWebSite", objSetup.getStrWebsite());
			p = JasperFillManager.fillReport(jr, hm, con);		

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return p;
	}

	public JasperPrint funCallRangePrintReport(String POcode, HttpServletRequest req)
	{
		objGlobal = new clsGlobalFunctions();
		Connection con = objGlobal.funGetConnection(req);
		JasperPrint p = null;
		try
		{

			String clientCode = req.getSession().getAttribute("clientCode").toString();
			String companyName = req.getSession().getAttribute("companyName").toString();
			String userCode = req.getSession().getAttribute("usercode").toString();
			String propertyCode = req.getSession().getAttribute("propertyCode").toString();

			clsPropertySetupModel objSetup = objSetupMasterService.funGetObjectPropertySetup(propertyCode, clientCode);
			String billAddress = "";
			String shippingAddress = "";
			if (objSetup == null)
			{
				objSetup = new clsPropertySetupModel();
			}
			else
			{
				billAddress = objSetup.getStrBAdd1() + "," + objSetup.getStrBAdd1() + "," + objSetup.getStrBCity() + "," + objSetup.getStrBState() + "," + objSetup.getStrBPin();
				shippingAddress = objSetup.getStrSAdd1() + "," + objSetup.getStrSAdd1() + "," + objSetup.getStrSCity() + "," + objSetup.getStrSState() + "," + objSetup.getStrSPin();
				;
			}

			String reportName = servletContext.getRealPath("/WEB-INF/reports/rptPurchaseOrderSlip.jrxml");
			String imagePath = servletContext.getRealPath("/resources/images/company_Logo.png");
			String sql = "SELECT po.strPOCode,date(po.dtPODate) as dtPODate,po.strSuppCode,po.strAgainst,po.strSOCode,po.dblFinalAmt," + " po.strVAddress1,po.strVAddress2,po.strVCity,po.strVState,po.strVCountry,po.strVPin,po.strSAddress1," + " po.strSAddress2,po.strSCity,po.strSState,po.strSCountry,po.strSPin,po.strYourRef,po.strPerRef,po.strEOE," + " po.strCode,date(po.dtDelDate) as dtDelDate,po.dblExtra,po.strFinalAmtInWord as AmtInWords,po.dblDiscount," + " po.strPayMode,po.strCurrency,po.strAmedment,po.strAmntNO,po.stredit,po.strUserAmed,date(po.dtPayDate) as dtPayDate," + " po.dblConversion,po.dtLastModified,po.strAuthorise,po.dblTaxAmt,s.strPName,s.strContact,s.strPhone,s.strMobile,s.strFax, s.strMAdd1, s.strMAdd2,s.strMCity," + " s.strMPin,s.strMState,s.strMCountry,u.strUserName,u.strSignatureImg, " + " ((po.dblDiscount*100)/po.dblTotal) as dblDisPer " + " FROM tblpurchaseorderhd AS po " + " left outer JOIN  tblpartymaster AS s ON po.strSuppCode = s.strPCode and s.strClientCode='" + clientCode + "' " + " left outer join  tbluserhd u on po.strUserModified=u.strUserCode and u.strClientCode='" + clientCode + "' " + " left outer join tblcurrencymaster c on po.strCurrency=c.strShortName and c.strClientCode='" + clientCode + "' " + " WHERE (po.strPOCode = '" + POcode + "' and po.strClientCode='" + clientCode + "' ) ";
			System.out.println(sql);
			JasperDesign jd = JRXmlLoader.load(reportName);
			JRDesignQuery newQuery = new JRDesignQuery();
			newQuery.setText(sql);
			jd.setQuery(newQuery);
			String sql2 = "SELECT  1 slno,   po.strProdCode,p.strProdName as strProdName, p.strUOM, po.dblOrdQty, po.dblPrice," + " po.dbldiscount,po.dblOrdQty*po.dblPrice-po.dbldiscount dblamt,  p.strPartNo,po.dblWeight,p.strCalAmtOn," + " po.strProdChar,p.strSpecification,po.strProcessCode,pr.strProcessName,po.strRemarks,ph.strEOE,po.strUpdate,ph.dblExtra" + " FROM  tblpurchaseorderdtl po  INNER JOIN tblpurchaseorderhd ph ON po.strPOCode = ph.strPOCode and ph.strClientCode='" + clientCode + "' " + " INNER JOIN tblproductmaster p ON po.strProdCode = p.strProdCode and p.strClientCode='" + clientCode + "' " + " left outer JOIN tblprocessmaster pr ON po.strProcessCode = pr.strProcessCode and pr.strClientCode='" + clientCode + "' " + "  where po.strPOCode='" + POcode + "' and po.strClientCode='" + clientCode + "' order by po.intindex";
			System.out.println(sql2);
			JRDesignQuery subQuery = new JRDesignQuery();
			subQuery.setText(sql2);
			Map<String, JRDataset> datasetMap = jd.getDatasetMap();
			JRDesignDataset subDataset = (JRDesignDataset) datasetMap.get("dsPODtl");
			subDataset.setQuery(subQuery);
			String sql3 = "select a.strTCName,if(b.strTCDesc='null','',b.strTCDesc) as strTCDesc  from tbltctransdtl b,tbltcmaster a" + " where b.strTCCode=a.strTCCode and  b.strTransCode='" + POcode + "'" + " and b.strTransType='Purchase Order' and  b.strClientCode='" + clientCode + "'  and a.strClientCode='" + clientCode + "' ";
			JRDesignQuery subQuery1 = new JRDesignQuery();
			subQuery1.setText(sql3);
			JRDesignDataset subDataset1 = (JRDesignDataset) datasetMap.get("dsTC");
			subDataset1.setQuery(subQuery1);

			String taxQuery = " select a.strTaxDesc,a.strTaxAmt from tblpotaxdtl a where strPOCode='" + POcode + "' and strClientCode='" + clientCode + "' ";
			JRDesignQuery subQuery2 = new JRDesignQuery();
			subQuery2.setText(taxQuery);
			JRDesignDataset subDataset2 = (JRDesignDataset) datasetMap.get("dsTax");
			subDataset2.setQuery(subQuery2);
			JasperReport jr = JasperCompileManager.compileReport(jd);
			HashMap hm = new HashMap();
			hm.put("strCompanyName", companyName);
			hm.put("strUserCode", userCode.toUpperCase());
			hm.put("strImagePath", imagePath);
			hm.put("strAddr1", objSetup.getStrAdd1());
			hm.put("strAddr2", objSetup.getStrAdd2());
			hm.put("strCity", objSetup.getStrCity());
			hm.put("strState", objSetup.getStrState());
			hm.put("strCountry", objSetup.getStrCountry());
			hm.put("strPin", objSetup.getStrPin());
			hm.put("strFax", objSetup.getStrFax());
			hm.put("strPhoneNo", objSetup.getStrPhone());
			hm.put("strEmailAddress", objSetup.getStrEmail());
			hm.put("strWebSite", objSetup.getStrWebsite());
			hm.put("strVAT", objSetup.getStrVAT());
			hm.put("strCST", objSetup.getStrCST());
			hm.put("strSAdd", shippingAddress);
			hm.put("strBAdd", billAddress);

			p = JasperFillManager.fillReport(jr, hm, con);

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				con.close();
			}
			catch (SQLException e)
			{

				e.printStackTrace();
			}
			return p;
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sendPIEmail", method = RequestMethod.GET)
	public @ResponseBody String doPISendEmail(HttpServletRequest request,HttpServletResponse response) throws JRException
	{
		// takes input from e-mail form
		try
		{
			String strPICode = request.getParameter("strPICode");
			String subject ="Purchase Indent Generated From "+request.getSession().getAttribute("locationName").toString();
			String message = "Purchase Indent Slip";
			String strReturnValue = "Email Send SuccessFully";

			String clientCode = request.getSession().getAttribute("clientCode").toString();
			String propertyCode = request.getSession().getAttribute("propertyCode").toString();
			String userCode = request.getSession().getAttribute("usercode").toString();
			// prints debug info
//			String sql = "select b.strEmail from tblpurchaseorderhd a,tblpartymaster b " + " where a.strSuppCode=b.strPCode and  a.strPOCode='" + strPOCode + "'";
			String sql = "SELECT CONCAT(c.strEmail,',',d.strEmail) "
						+ " FROM tblpurchaseindendhd a,tbluserhd c,tblpropertysetup d "
						+ " WHERE a.strPIcode='"+strPICode+"' AND c.strUserCode='"+userCode+"' AND d.strPropertyCode='"+propertyCode+"';";
			List list = objGlobalFunctionsService.funGetList(sql, "sql");
			if (list != null && !list.isEmpty())
			{
				String recipientAddress = list.get(0).toString();

				String []receipientsArr = recipientAddress.split(",");
                for(int i=0;i<receipientsArr.length;i++)
                {
                	logger.info("To: " + receipientsArr[i].toString());
    				logger.info("Subject: " + subject);
    				logger.info("Message: " + message);
    				JasperPrint p = objPurchaseIndentHdController.funCallRangePrintReport(strPICode,response,request);// Calling
    																			// Attachment
    				ByteArrayOutputStream baos = new ByteArrayOutputStream();
    				JasperExportManager.exportReportToPdfStream(p, baos);
    				DataSource aAttachment = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
    				MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true);

    				helper.setTo(receipientsArr[i].toString());
    				helper.setSubject(subject);
    				helper.addAttachment("PI Slip.pdf", aAttachment);
    				helper.setText(message);
    				mailSender.send(helper.getMimeMessage());
                }
				

			}
			else
			{
				strReturnValue = "USer has No Email Id";
			}

			return strReturnValue;
		}
		catch (javax.mail.MessagingException e)
		{
			e.printStackTrace();
			logger.info(e);
			return e.toString();
		}

	}
	
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/sendMISEmail", method = RequestMethod.GET)
	public @ResponseBody String doMISSendEmail(HttpServletRequest request,HttpServletResponse response) throws JRException
	{
		// takes input from e-mail form
		try
		{  
			clsReportBean objReportBean=new clsReportBean();
			
			String strMISCode = request.getParameter("strMISCode");
			String subject ="Material Issue Generated From "+request.getSession().getAttribute("locationName").toString();
			String message = "Material Issue Slip";
			String strReturnValue = "Email Send SuccessFully";

			String clientCode = request.getSession().getAttribute("clientCode").toString();
			String propertyCode = request.getSession().getAttribute("propertyCode").toString();
			String userCode = request.getSession().getAttribute("usercode").toString();
			// prints debug info

			String sql = "SELECT CONCAT(c.strEmail,',',d.strEmail),a.dtMISDate,a.strLocFrom ,a.strLocTo"
						+ " FROM tblmishd a,tbluserhd c,tblpropertysetup d "
						+ " WHERE a.strMISCode='"+strMISCode+"' AND c.strUserCode='"+userCode+"' AND d.strPropertyCode='"+propertyCode+"';";
			List list = objGlobalFunctionsService.funGetList(sql, "sql");
			if (list != null && !list.isEmpty())
			{   
				
                Object[] arrObj=(Object[]) list.get(0);
				String recipientAddress = arrObj[0].toString();
				
				objReportBean.setDtFromDate(arrObj[1].toString());
				objReportBean.setDtToDate(arrObj[1].toString());
				objReportBean.setStrFromLocCode(arrObj[2].toString());
				objReportBean.setStrToLocCode(arrObj[3].toString());
				String []receipientsArr = recipientAddress.split(",");
				
				
				
                for(int i=0;i<receipientsArr.length;i++)
                {
                	logger.info("To: " + receipientsArr[i].toString());
    				logger.info("Subject: " + subject);
    				logger.info("Message: " + message);
    				JasperPrint p = objMISController.funCallRangePrintReport(strMISCode, response, request, objReportBean);// Calling
    																			// Attachment
    				ByteArrayOutputStream baos = new ByteArrayOutputStream();
    				JasperExportManager.exportReportToPdfStream(p, baos);
    				DataSource aAttachment = new ByteArrayDataSource(baos.toByteArray(), "application/pdf");
    				MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true);

    				helper.setTo(receipientsArr[i].toString());
    				helper.setSubject(subject);
    				helper.addAttachment("MIS Slip.pdf", aAttachment);
    				helper.setText(message);
    				mailSender.send(helper.getMimeMessage());
                }
				

			}
			else
			{
				strReturnValue = "User has No Email Id";
			}

			return strReturnValue;
		}
		catch (javax.mail.MessagingException e)
		{
			e.printStackTrace();
			logger.info(e);
			return e.toString();
		}

	}
	
	public @ResponseBody String doSendReservationEmail(String strReservationNo,String strMessege,String strModuleName,HttpServletRequest request) throws JRException
	{
		// takes input from e-mail form.
		try
		{
			String subject = "";
			if(strModuleName.equalsIgnoreCase("dayEnd"))
			{
				subject ="Check in remainder mail from "+request.getSession().getAttribute("companyName").toString();
			}
			else
			{
				subject ="Reservation Confirmation From "+request.getSession().getAttribute("companyName").toString();
			}
			
			/*String message = "Purchase Order Slip";*/
			String strReturnValue = "Email Send SuccessFully";
			String companyName = request.getSession().getAttribute("companyName").toString();
			String clientCode = request.getSession().getAttribute("clientCode").toString();
			String propertyCode = request.getSession().getAttribute("propertyCode").toString();
			String userCode = request.getSession().getAttribute("usercode").toString();
			
		
			//String strEmailId="sachinm555@gmail.com";
			String sql="select b.strEmailId from tblreservationdtl a ,tblguestmaster b where "
					+ "a.strReservationNo='"+strReservationNo+"' and a.strGuestCode=b.strGuestCode AND a.strClientCode='"+clientCode+"'";/*="SELECT CONCAT(ifnull(b.strEmail,''),',',ifnull(c.strEmail,''),',',ifnull(d.strEmail,''))EmailIds "
			+"FROM tblpurchaseorderhd a "
					+"left outer join tblpartymaster b on a.strSuppCode=b.strPCode "
					+"left outer join tbluserhd c on a.strUserModified=c.strUserCode "
					+"left outer join tblpropertysetup d on a.strPropCode=d.strPropertyCode  "
					+"WHERE  a.strPOCode='"+strPOCode+"'  "
					+"AND c.strUserCode='"+userCode+"'  "
					+"AND d.strPropertyCode='"+propertyCode+"'";*/
			List list = objGlobalFunctionsService.funGetListModuleWise(sql, "sql");
			if (list != null && !list.isEmpty())
			{
				String recipientAddress = list.get(0).toString();

				String []receipientsArr = recipientAddress.split(",");
                for(int i=0;i<receipientsArr.length;i++)
                {
                	if(receipientsArr[i].toString().trim().length()>0)
                	{
                	
                		logger.info("To: " + receipientsArr[i].toString());
        				logger.info("Subject: " + subject);
        				logger.info("Message: " + strMessege);
        			
        				
        				
        				
        				MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true);

        				helper.setTo(receipientsArr[i].toString());
        				helper.setSubject(subject);
        				//helper.addAttachment("PO Slip.pdf", aAttachment);
        				helper.setText(strMessege);
        				mailSender.send(helper.getMimeMessage());
                	}                	
                }
				

			}
			else
			{
				strReturnValue = "Supplier has No Email Id";
			}

			return strReturnValue;
		}
		catch (javax.mail.MessagingException e)
		{
			e.printStackTrace();
			logger.info(e);
			return e.toString();
		}

	}
	
	
}
