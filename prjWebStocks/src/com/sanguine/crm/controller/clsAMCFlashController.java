package com.sanguine.crm.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.crm.bean.clsAMCFlashBean;
import com.sanguine.crm.bean.clsAMCFlashDtlBean;
import com.sanguine.crm.bean.clsInvoiceBean;
import com.sanguine.crm.bean.clsInvoiceDtlBean;
import com.sanguine.crm.service.clsCRMSettlementMasterService;
import com.sanguine.service.clsGlobalFunctionsService;


@Controller
public class clsAMCFlashController {

	@Autowired
	private clsGlobalFunctions objGlobalFunctions;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalService;
	
	@Autowired 
	private clsProFormaInvoice objProFormaInvController;
	
	@Autowired
	private clsCRMSettlementMasterService objSettlementService;
	
	// Open form
		@RequestMapping(value = "/frmAMCFlash", method = RequestMethod.GET)
		public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
			String urlHits = "1";
			try {
				urlHits = request.getParameter("saddr").toString();
			} catch (NullPointerException e) {
				urlHits = "1";
			}
			model.put("urlHits", urlHits);
			String clientCode = request.getSession().getAttribute("clientCode").toString();
			Map<String, String> settlementList = objSettlementService.funGetSettlementComboBox(clientCode);
			model.put("settlementList", settlementList);

			if (urlHits.equalsIgnoreCase("1")) {
				return new ModelAndView("frmAMCFlash", "command", new clsAMCFlashBean());
			} else {
				return new ModelAndView("frmAMCFlash_1", "command", new clsAMCFlashBean());
			}
		}
		
		
		@RequestMapping(value = "/loadAMCReport", method = RequestMethod.GET)
		public @ResponseBody List<clsAMCFlashBean> funLoadAMCData(@RequestParam("fromDate") String fromDate,@RequestParam("toDate") String toDate, HttpServletRequest request) throws ParseException {
		
			
			fromDate=objGlobalFunctions.funGetDate("yyyy-MM-dd", fromDate);
			toDate=objGlobalFunctions.funGetDate("yyyy-MM-dd", toDate);
			String clientCode = request.getSession().getAttribute("clientCode").toString();
			String sql=" select a.strPName,b.dblLastCost,b.dblAMCAmt,DATE_FORMAT(b.dteInstallation,'%d-%m-%Y'),b.intWarrantyDays ,"
					 + " date(DATE_ADD(DATE_FORMAT(b.dteInstallation,'%Y-%m-%d'), INTERVAL b.intWarrantyDays DAY)) as exp ,a.strPCode"
					 + " FROM tblpartymaster a,tblprodsuppmaster b "
					 + " WHERE a.strPCode=b.strSuppCode AND a.strPType='cust' and date(DATE_ADD(DATE_FORMAT(b.dteInstallation,'%Y-%m-%d'), INTERVAL b.intWarrantyDays DAY)) between "
					 + " '"+fromDate+"' and '"+toDate+"'"
					 +"  group by a.strPCode ";
			
			
			
			List list = objGlobalService.funGetList(sql);
			List<clsAMCFlashBean>listData=new ArrayList<clsAMCFlashBean>();
			if(list.size()>0)
			{
			for(int i=0;i<list.size();i++)
			{
				
				Object[]obj=(Object[])list.get(i);
				clsAMCFlashBean objBean=new clsAMCFlashBean();
				objBean.setStrCustomerName(obj[0].toString());
				objBean.setDblLicenceAmt(Double.parseDouble(obj[1].toString()));
				objBean.setDblAMCAmt(Double.parseDouble(obj[2].toString()));
				objBean.setDteInstallation(obj[3].toString());
				objBean.setDteExpiry(objGlobalFunctions.funGetDate("dd-MM-yyyy", obj[5].toString())); 
				objBean.setStrCustomerCode(obj[6].toString());
				
				listData.add(objBean);
			}
			}
			
			return listData;
			
		}
		
		
		
		@RequestMapping(value = "/saveAMCInvoice", method = RequestMethod.POST)
		public ModelAndView funSaveAMCInvoice(@ModelAttribute("command") @Valid clsAMCFlashBean objAMCBean, BindingResult result, HttpServletRequest request,HttpServletResponse resp) throws ParseException {
		
			
			String clientCode = request.getSession().getAttribute("clientCode").toString();
		   StringBuilder invCode=new StringBuilder();
			for(clsAMCFlashDtlBean objAmc: objAMCBean.getListAMCDtl())
			{
				if(objAmc.getStrselect()!=null)
				{
				clsInvoiceBean objInvBean=new clsInvoiceBean();
				objInvBean.setStrCustCode(objAmc.getStrCustCode());
				objInvBean.setStrCustName(objAmc.getStrCustomerName());
				String locationCode = request.getSession().getAttribute("locationCode").toString();
				objInvBean.setStrAgainst("Direct");
				objInvBean.setStrLocCode(locationCode);
				objInvBean.setDteInvDate(objGlobalFunctions.funGetCurrentDate("yyyy-MM-dd"));
				objInvBean.setStrSettlementCode(objAMCBean.getStrSettlementCode());
				objInvBean.setStrCurrencyCode("");
				objInvBean.setStrClientCode(clientCode);
				objInvBean.setStrWarrPeriod(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
	            objInvBean.setStrWarraValidity(objGlobalFunctions.funGetCurrentDateTime("yyyy-MM-dd"));
	            objInvBean.setDblDiscount(0.0);
	            objInvBean.setDblDiscountAmt(0.00);
	            objInvBean.setStrMobileNoForSettlement("");
	            List <clsInvoiceDtlBean> listInvoiceDtl=new ArrayList<clsInvoiceDtlBean>();
	            String sql=" select b.strProdCode,b.dblAMCAmt,b.dblStandingOrder from tblprodsuppmaster b where b.strSuppCode='"+objAmc.getStrCustCode()+"' ";
	            List list = objGlobalService.funGetList(sql);
	         
	            if(list.size()>0)
	            {
	            	for(int i=0;i<list.size();i++)
	            	{
		            	 Object obj[]=(Object[])list.get(i);
		            	 clsInvoiceDtlBean objDtlBean=new clsInvoiceDtlBean();
						 objDtlBean.setStrProdCode(obj[0].toString());
						 objDtlBean.setDblUnitPrice(Double.parseDouble(obj[1].toString()));
						 objDtlBean.setStrCustCode(objAmc.getStrCustCode());
						 objDtlBean.setStrClientCode(clientCode);
						
						 objDtlBean.setDblQty(Double.parseDouble(obj[2].toString()));
						 
							objDtlBean.setDblDisAmt(0.00);
							
							
						 listInvoiceDtl.add(objDtlBean);
	            	}
	            	objInvBean.setListclsInvoiceModelDtl(listInvoiceDtl);
	            	objProFormaInvController.funAddUpdate(objInvBean,  result, request);
	            	 
	            	    invCode.append(request.getSession().getAttribute("rptInvCode").toString());
	            	    request.getSession().removeAttribute("rptInvCode");
	            	   
//						
//						objProFormaInvController.funOpenInvoiceFormat5Report(invCode, invDate, resp,request) ;
	            }
	           
				}
			}
			 String invDate=request.getSession().getAttribute("rptInvDate").toString();
			clsAMCFlashBean objBean=new clsAMCFlashBean();
			request.getSession().setAttribute("rptInvCode", invCode.toString().substring(0,invCode.length()-1));

			request.getSession().setAttribute("success", true);
			request.getSession().setAttribute("successMessage", "Invoice Code : "+invCode);
			objBean.setStrInvCode(invCode.toString().substring(0,invCode.length()-1));
			Map<String, Object> model=new HashMap<String, Object>();
		
			
			Map<String, String> settlementList = objSettlementService.funGetSettlementComboBox(clientCode);
			model.put("settlementList", settlementList);
			return new ModelAndView("frmAMCFlash", "command", new clsAMCFlashBean());
			
			
			
			
		}
}
