package com.sanguine.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.bean.clsDeleteModuleListBean;
import com.sanguine.model.clsCompanyMasterModel;
import com.sanguine.model.clsTreeMasterModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSecurityShellService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.service.clsStructureUpdateService;
import com.sanguine.util.clsClientDetails;
import com.sanguine.util.clsDatabaseBackup;

@Controller
public class clsStructureUpdateController {

	@Autowired
	private clsStructureUpdateService objStructureUpdateService;

	@Autowired
	private clsSecurityShellService objSecurityShellService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private clsSetupMasterService objSetupMasterService;


	@Autowired
	private clsGlobalFunctions objGlobalFun;
	/**
	 * Open Delete Module form
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/frmDeleteModuleList", method = RequestMethod.GET)
	public ModelAndView funOpenListForm(Map<String, Object> model, HttpServletRequest request) {

		String webStockDB=request.getSession().getAttribute("WebStockDB").toString();
		String urlHits = "1";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		/**
		 * Set header on form
		 */
		model.put("headerName", "Transaction List");

		List<String> listPropertyName = new ArrayList<>();

		String sqlPropertyName = "select strPropertyName from "+webStockDB+".tblpropertymaster where strClientCode='" + clientCode + "' ";
		listPropertyName = objGlobalFunctionsService.funGetDataList(sqlPropertyName, "sql");
		model.put("listPropertyName", listPropertyName);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmDeleteModuleList", "command", new clsDeleteModuleListBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmDeleteModuleList", "command", new clsDeleteModuleListBean());
		} else {
			return null;
		}

	}
	
	//PMS
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/frmPMSDeleteModuleList", method = RequestMethod.GET)
	public ModelAndView funOpenPMSListForm(Map<String, Object> model, HttpServletRequest request) {

		String urlHits = "1";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		/**
		 * Set header on form
		 */
		model.put("headerName", "Transaction List");

		List<String> listPropertyName = new ArrayList<>();

		String sqlPropertyName = request.getSession().getAttribute("propertyName").toString();
		listPropertyName.add(sqlPropertyName);
		model.put("listPropertyName", listPropertyName);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPMSDeleteModuleList", "command", new clsDeleteModuleListBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPMSDeleteModuleList", "command", new clsDeleteModuleListBean());
		} else {
			return null;
		}

	}

	/**
	 * Load Data on Form
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	@RequestMapping(value = "/frmFillActionList", method = RequestMethod.GET)
	public @ResponseBody List funListForm(Map<String, Object> model, HttpServletRequest request) {

		String strModuleNo = request.getSession().getAttribute("moduleNo").toString();
		List<clsTreeMasterModel> objModel = objSecurityShellService.funGetFormList(strModuleNo);

		List<String> objMasters = new ArrayList<String>();
		String strType = request.getParameter("strHeadingType").toString();
		List list = new ArrayList();
		List<clsTreeMasterModel> ListTrans = new ArrayList<clsTreeMasterModel>();
		List<clsTreeMasterModel> ListMaster = new ArrayList<clsTreeMasterModel>();
		List<clsTreeMasterModel> objReports = new ArrayList<clsTreeMasterModel>();
		List<clsTreeMasterModel> objUtilitys = new ArrayList<clsTreeMasterModel>();

		for (Object ob : objModel) {
			List<String> objTransactions = new ArrayList<String>();
			Object[] arrOb = (Object[]) ob;
			String type = arrOb[2].toString();
			clsTreeMasterModel objTree = new clsTreeMasterModel();
			switch (type) {
			// Master
			case "M":

				objTree.setStrFormName(arrOb[0].toString());
				objTree.setStrFormDesc(arrOb[1].toString());
				objTree.setStrDelete("false");
				ListMaster.add(objTree);
				break;
			// Tools
			case "L":
				objTree.setStrFormName(arrOb[0].toString());
				objTree.setStrFormDesc(arrOb[1].toString());
				objTree.setStrDelete("false");
				objUtilitys.add(objTree);
				break;
			// Transaction
			case "T":

				objTree.setStrFormName(arrOb[0].toString());
				objTree.setStrFormDesc(arrOb[1].toString());
				objTree.setStrDelete("false");
				ListTrans.add(objTree);
				break;
			// Report
			case "R":
				objTree.setStrFormName(arrOb[0].toString());
				objTree.setStrFormDesc(arrOb[1].toString());
				objTree.setStrDelete("false");
				objReports.add(objTree);
				break;

			}

		}
		if (strType.equalsIgnoreCase("Transaction")) {
			list = ListTrans;
		} else if (strType.equalsIgnoreCase("Master")) {
			list = ListMaster;
		}
		// Return List
		return list;
	}

	/**
	 * Open Structure Update Form
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/frmStructureUpdate", method = RequestMethod.GET)
	public ModelAndView funOpenStructureUpdateForm(Map<String, Object> model, HttpServletRequest req) {

		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmStructureUpdate_1");
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmStructureUpdate");
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/frmStructureUpdateException_2", method = RequestMethod.GET)
	public ModelAndView funOpenStructur_2(Map<String, Object> model, HttpServletRequest req) {

		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		return new ModelAndView("frmStructureUpdate_2");

	}

	/**
	 * Update Structure in Data base
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateStructure", method = RequestMethod.GET)
	public @ResponseBody String funUpdateStructure(HttpServletRequest req) {
		String clientCode = "";
		if (null != req.getSession().getAttribute("clientCode")) {
			clientCode = req.getSession().getAttribute("clientCode").toString();
		}

		objStructureUpdateService.funUpdateStructure(clientCode,req);
		return "Structure Update Successfully";
	}

	/**
	 * Clear Transaction
	 * 
	 * @param frmName
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/ClearTransaction", method = RequestMethod.GET)
	public @ResponseBody String funClearTransaction(@RequestParam(value = "frmName") String frmName, @RequestParam(value = "propName") String propName, @RequestParam(value = "locName") String locName, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String str[] = frmName.split(",");
		if(propName.equalsIgnoreCase("All"))
		{
			objStructureUpdateService.funClearTransaction(clientCode, str);
		}
		else
		{
			if(propName!=null)
			{
				propName = req.getSession().getAttribute("propertyName").toString();
			}
			objStructureUpdateService.funClearTransactionByProperty(clientCode, str, propName);
		}
		return "Transaction Clear Successfully";
	}

	/**
	 * Clear Master
	 * 
	 * @param frmName
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/ClearMaster", method = RequestMethod.GET)
	public @ResponseBody String funClearMaster(@RequestParam(value = "frmName") String frmName, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String str[] = frmName.split(",");
		objStructureUpdateService.funClearMaster(clientCode, str);
		return "Master Clear Successfully";
	}

	@RequestMapping(value = "/loadPropertyName", method = RequestMethod.GET)
	public @ResponseBody List funLoadPropertyMaster(@RequestParam(value = "propName") String propName, HttpServletRequest req) {

		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String webStockDB=req.getSession().getAttribute("WebStockDB").toString();

		List<String> listPropertyName = new ArrayList<>();
		String sqlPropertyName = "select strPropertyName from "+webStockDB+".tblpropertymaster where strClientCode='" + clientCode + "' ";
		listPropertyName = objGlobalFunctionsService.funGetDataList(sqlPropertyName, "sql");
		listPropertyName.add("All");
		 Collections.sort(listPropertyName);
		return listPropertyName;
	}

	@RequestMapping(value = "/loadLocName", method = RequestMethod.GET)
	public @ResponseBody List funLoadLoctionMaster(@RequestParam(value = "propName") String propName, HttpServletRequest req) {

		String dbName = req.getSession().getAttribute("WebStockDB").toString();
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		List<String> listLocName = new ArrayList<>();
		String sqlLocName = "select a.strLocName from "+dbName+".tbllocationmaster a ,"+dbName+".tblpropertymaster b " + "where a.strPropertyCode=b.strPropertyCode and b.strPropertyName='" + propName + "' and a.strClientCode='" + clientCode + "' ";
		listLocName = objGlobalFunctionsService.funGetDataList(sqlLocName, "sql");
		return listLocName;
	}
//
	@RequestMapping(value = "/takeDBBackUp", method = RequestMethod.GET)
	public @ResponseBody String takeDBBackUp( HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String res="DataBase Backup Done";
		try{
			String OS=System.getProperty("os.name").toLowerCase();
			 String backupFilePathMail="";
			String dbWebmms=new clsGlobalFunctions().funTrimDBNameFromURL(clsGlobalFunctions.conUrl);
	        String dbWebbook=new clsGlobalFunctions().funTrimDBNameFromURL(clsGlobalFunctions.urlwebbooks);
	        String dbWebPMS=new clsGlobalFunctions().funTrimDBNameFromURL(clsGlobalFunctions.urlwebpms);
	        
	        List<clsCompanyMasterModel> listClsCompanyMasterModel = objSetupMasterService.funGetListCompanyMasterModel(clientCode);
	        clsCompanyMasterModel objCompanyMasterModel = null;
			if (listClsCompanyMasterModel.size() > 0) {
				objCompanyMasterModel = listClsCompanyMasterModel.get(0);
				
			}
			 
			Date dtCurrentDate = new Date();
	        String date = dtCurrentDate.getDate() + "-" + (dtCurrentDate.getMonth() + 1) + "-" + (dtCurrentDate.getYear() + 1900);
	        String time = dtCurrentDate.getHours() + "-" + dtCurrentDate.getMinutes();
	        String bckupdate = date+ " " +time;
	        String dteBackup=objGlobalFun.funGetCurrentDateTime("yyyy-MM-dd");
			clsDatabaseBackup obDB=new clsDatabaseBackup();
			if(OS.equals("linux"))
			{
			  backupFilePathMail=obDB.funTakeBackUpDBForLinux(dbWebmms);
			  String insertSql="insert into tbldatabasebckup values('"+dteBackup+"','"+clientCode+"','"+dbWebmms+"')";
			  objGlobalFunctionsService.funExcuteQuery(insertSql);
			  if(null!=objCompanyMasterModel){
				  if(objCompanyMasterModel.getStrWebBookModule().equalsIgnoreCase("Yes")){
					  backupFilePathMail=obDB.funTakeBackUpDBForLinux(dbWebbook);
					  insertSql="insert into tbldatabasebckup values('"+dteBackup+"','"+clientCode+"','"+dbWebmms+"')";
					  objGlobalFunctionsService.funExcuteQuery(insertSql);
					  
					}
				  if(objCompanyMasterModel.getStrWebPMSModule().equalsIgnoreCase("Yes")){
						  backupFilePathMail=obDB.funTakeBackUpDBForLinux(dbWebPMS);
						  insertSql="insert into tbldatabasebckup values('"+dteBackup+"','"+clientCode+"','"+dbWebmms+"')";
						  objGlobalFunctionsService.funExcuteQuery(insertSql);
						  
				  }  
			  }
			  
			}else{
				if(OS.contains("windows")){
					backupFilePathMail=obDB.funTakeBackUpDB(dbWebmms);
					String insertSql="insert into tbldatabasebckup values('"+dteBackup+"','"+clientCode+"','"+dbWebmms+"')";
					objGlobalFunctionsService.funExcuteQuery(insertSql);
					  
					if(null!=objCompanyMasterModel){
						  if(objCompanyMasterModel.getStrWebBookModule().equalsIgnoreCase("Yes")){
							  backupFilePathMail=obDB.funTakeBackUpDBForLinux(dbWebbook);
							  insertSql="insert into tbldatabasebckup values('"+dteBackup+"','"+clientCode+"','"+dbWebmms+"')";
							  objGlobalFunctionsService.funExcuteQuery(insertSql);
							  
							}
						  if(objCompanyMasterModel.getStrWebPMSModule().equalsIgnoreCase("Yes")){
								  backupFilePathMail=obDB.funTakeBackUpDBForLinux(dbWebPMS);
								  insertSql="insert into tbldatabasebckup values('"+dteBackup+"','"+clientCode+"','"+dbWebmms+"')";
								  objGlobalFunctionsService.funExcuteQuery(insertSql);
								  
						  }  
					  }
				}
					
				
			}
			
			
			funSendDBBackupToSanguineAuditiing( backupFilePathMail, OS,bckupdate,req);
			//new clsDatabaseBackup().funTakeBackUpDB(dbWebbook);
		
			System.out.print(OS);
			
			
			 
			
		}catch(Exception e){
			e.printStackTrace();
			res="Failed";
		}
		return res;
	}
	

    private void funSendDBBackupToSanguineAuditiing(String dbBackupFilePath,String os,String bckupdate,HttpServletRequest req)
    {
    if(os.contains("windows")){
	dbBackupFilePath = System.getProperty("user.dir") + "\\DBBackup\\" + dbBackupFilePath + ".sql";
    }else {
    	dbBackupFilePath = dbBackupFilePath + ".sql";
    }
    
    File dbBackupFile = new File(dbBackupFilePath);
	funSendDBBackup( dbBackupFile,bckupdate,req);
    }
    
    
    public void funSendDBBackup( File dbBackupFile,String bckupdate,HttpServletRequest req)
    {

	try
	{

	    //mailed logic
	    int ret = 0;
	    final String from = "sanguineauditing@gmail.com";//change accordingly
	    String to = "";//change accordingly
	    
		to="sanguineauditing@gmail.com";
	    
	    //Get the session object
	    Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.port", "465");

	    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator()
	    {
		protected PasswordAuthentication getPasswordAuthentication()
		{
		    //return new PasswordAuthentication("paritoshkumar112@gmail.com","singhparitosh123");//change accordingly  
		    return new PasswordAuthentication(from, "Sanguine@2017");//change accordingly
		}
	    });
	    MimeMessage message = new MimeMessage(session);
	    //message.setFrom(new InternetAddress("paritoshkumar112@gmail.com"));//change accordingly
	    message.setFrom(new InternetAddress(from));//change accordingly
	

	    String[] arrRecipient = to.split(",");

	    if (to.trim().length() > 0)
	    {
		for (int cnt = 0; cnt < arrRecipient.length; cnt++)
		{
		    System.out.println(arrRecipient[cnt]);
		    message.addRecipient(Message.RecipientType.TO, new InternetAddress(arrRecipient[cnt]));
		}
	    }
	    String clientCode = req.getSession().getAttribute("clientCode").toString();
	     clsClientDetails objClientDtl=clsClientDetails.hmClientDtl.get(clientCode);
	    message.setSubject("DB Backup of MMS '" +clientCode  + "' '" +objClientDtl.Client_Name + "' '" + bckupdate + "' " );

	    String msgBody = "DB Backup of MMS '" +clientCode  + "' '" +objClientDtl.Client_Name + "' '" + bckupdate + "' ";
	    //message.setText(msgBody);

	    // Create the message part 
	    BodyPart messageBodyPart = new MimeBodyPart();

	    // Fill the message
	    messageBodyPart.setText(msgBody);

	    Multipart multipart = new MimeMultipart();
	    // Set text message part
	    multipart.addBodyPart(messageBodyPart);
	    if (dbBackupFile.exists())
	    {
		FileOutputStream fos = null;
		ZipOutputStream zipOut = null;
		FileInputStream fis = null;
		String destZIPFile = System.getProperty("user.dir") + "\\DBBackup\\" + dbBackupFile.getName() + ".zip";
		//clsPosConfigFile.dbBackupPath+File.pathSeparator+dbBackupFile.getName()+".zip";

		try
		{
		    fos = new FileOutputStream(destZIPFile);
		    zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
		    File input = dbBackupFile;
		    fis = new FileInputStream(input);
		    ZipEntry ze = new ZipEntry(input.getName());
		    System.out.println("Zipping the file: " + input.getName());
		    zipOut.putNextEntry(ze);
		    byte[] tmp = new byte[4 * 1024];
		    int size = 0;
		    while ((size = fis.read(tmp)) != -1)
		    {
			zipOut.write(tmp, 0, size);
		    }
		    zipOut.flush();
		    zipOut.close();
		}
		catch (FileNotFoundException e)
		{
		    e.printStackTrace();
		}
		catch (IOException e)
		{
		    e.printStackTrace();
		}
		finally
		{
		    try
		    {
			if (fos != null)
			{
			    fos.close();
			}
			if (fis != null)
			{
			    fis.close();
			}
		    }
		    catch (Exception ex)
		    {

		    }
		}
		File destZipFile = new File(destZIPFile);
		if (destZipFile.exists())
		{
		    DataSource dbBackupSource = new FileDataSource(destZipFile);
		    messageBodyPart = new MimeBodyPart();
		    messageBodyPart.setDataHandler(new DataHandler(dbBackupSource));
		    messageBodyPart.setFileName(dbBackupSource.getName());
		    multipart.addBodyPart(messageBodyPart);
		}
	    }

	    // Send the complete message parts
	    message.setContent(multipart);

	    if (to.length() > 0)
	    {
		//send message  
		Transport.send(message);
		System.out.println("message sent successfully");
	    }
	    else
	    {
		System.out.println("Email has No Recipient");
	    }

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }
	
	
	/**
	 * Open Structure Update Form
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/frmWebBooksStructureUpdate", method = RequestMethod.GET)
	public ModelAndView funOpenWebBooksStructureUpdateForm(Map<String, Object> model, HttpServletRequest req) {

		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmWebBooksStructureUpdate_1");
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmWebBooksStructureUpdate");
		} else {
			return null;
		}

	}

	/**
	 * Update Structure in Data base
	 * 
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateWebBooksStructure", method = RequestMethod.GET)
	public @ResponseBody String funUpdateWebBooksStructure(HttpServletRequest req) {
		String clientCode = "";
		if (null != req.getSession().getAttribute("clientCode")) {
			clientCode = req.getSession().getAttribute("clientCode").toString();
		}

		objStructureUpdateService.funUpdateWebBooksStructure(clientCode,req);
		return "Structure Update Successfully";
	}

	/**
	 * Clear Transaction
	 * 
	 * @param frmName
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/ClearWebBooksTransaction", method = RequestMethod.GET)
	public @ResponseBody String funClearWebBooksTransaction(@RequestParam(value = "frmName") String frmName, @RequestParam(value = "propName") String propName, @RequestParam(value = "locName") String locName, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String str[] = frmName.split(",");
		String property = req.getSession().getAttribute("propertyCode").toString();
		
		if(propName.equalsIgnoreCase("All"))
		{
			objStructureUpdateService.funClearWebBooksTransaction(clientCode, str);
		}
		else
		{
			objStructureUpdateService.funClearWebBooksTransactionByProperty(clientCode, str,property);
		}
		return "Transaction Clear Successfully";
	}

	/**
	 * Clear Master
	 * 
	 * @param frmName
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/ClearWebBooksMaster", method = RequestMethod.GET)
	public @ResponseBody String funClearWebBooksMaster(@RequestParam(value = "frmName") String frmName, HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String str[] = frmName.split(",");
		objStructureUpdateService.funClearWebBooksMaster(clientCode, str);
		return "Master Clear Successfully";
	}

	/**
	 * Open Delete Module form
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/frmWebBooksDeleteModuleList", method = RequestMethod.GET)
	public ModelAndView funOpenWebBooksListForm(Map<String, Object> model, HttpServletRequest request) {

		String urlHits = "1";
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		/**
		 * Set header on form
		 */
		model.put("headerName", "Transaction List");

		List<String> listPropertyName = new ArrayList<>();

		String sqlPropertyName = "select strPropertyName from tblpropertymaster where strClientCode='" + clientCode + "' ";
		listPropertyName = objGlobalFunctionsService.funGetDataList(sqlPropertyName, "sql");
		model.put("listPropertyName", listPropertyName);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmWebBooksDeleteModuleList", "command", new clsDeleteModuleListBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmWebBooksDeleteModuleList", "command", new clsDeleteModuleListBean());
		} else {
			return null;
		}

	}
	
	@RequestMapping(value = "/loadPropertyNameForWebBooks", method = RequestMethod.GET)
	public @ResponseBody List funLoadPropertyMasterForWebBooks(@RequestParam(value = "propName") String propName, HttpServletRequest req) {

		String clientCode = req.getSession().getAttribute("clientCode").toString();
		String dbName = req.getSession().getAttribute("WebStockDB").toString();
		List<String> listPropertyName = new ArrayList<>();
		String sqlPropertyName = "select strPropertyName from "+dbName+".tblpropertymaster where strClientCode='" + clientCode + "' ";
		listPropertyName = objGlobalFunctionsService.funGetDataList(sqlPropertyName, "sql");
		listPropertyName.add("All");
		Collections.sort(listPropertyName);
		return listPropertyName;
	}
	
}
