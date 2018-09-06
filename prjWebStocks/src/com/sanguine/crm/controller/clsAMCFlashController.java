package com.sanguine.crm.controller;

import java.text.ParseException;
import java.util.ArrayList;
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

import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.crm.bean.clsAMCFlashBean;
import com.sanguine.service.clsGlobalFunctionsService;


@Controller
public class clsAMCFlashController {

	@Autowired
	private clsGlobalFunctions objGlobalFunctions;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalService;
	
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
					 + " date(DATE_ADD(DATE_FORMAT(b.dteInstallation,'%Y-%m-%d'), INTERVAL b.intWarrantyDays DAY)) as exp "
					 + " FROM tblpartymaster a,tblprodsuppmaster b "
					 + " WHERE a.strPCode=b.strSuppCode AND a.strPType='cust' and date(DATE_ADD(DATE_FORMAT(b.dteInstallation,'%Y-%m-%d'), INTERVAL b.intWarrantyDays DAY)) between "
					 + " '"+fromDate+"' and '"+toDate+"'";
			
			
			
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
				
				listData.add(objBean);
			}
			}
			return listData;
			
		}
}
