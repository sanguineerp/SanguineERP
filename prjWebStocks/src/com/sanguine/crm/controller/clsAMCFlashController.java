package com.sanguine.crm.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.crm.bean.clsAMCFlashBean;
import com.sanguine.service.clsGlobalFunctionsService;


@Controller
public class clsAMCFlashController {

	
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
		public List<clsAMCFlashBean> funLoadAMCData(Map<String, Object> model, HttpServletRequest request) {
		
			String clientCode = request.getSession().getAttribute("clientCode").toString();
			String sql="select a.strDebtorFullName,a.dteStartDate,a.strTelNo1,a.longMobileNo ,b.dteInstallation "
					  +" from tblsundarydebtormaster a,tblsundarydebtoritemdetail b where a.strClientCode='226.001' "
					  +" and b.dteInstallation between '2018-06-20' and '2018-08-27' ";
			List list = objGlobalService.funGetList(sql);
			List<clsAMCFlashBean>listData=new ArrayList<clsAMCFlashBean>();
			for(int i=0;i<list.size();i++)
			{
				
				Object[]obj=(Object[])list.get(i);
				clsAMCFlashBean objBean=new clsAMCFlashBean();
				objBean.setStrCustomerName(obj[0].toString());
				objBean.setDteInstallation(obj[1].toString());
				objBean.setDteExpiry(obj[2].toString());
				objBean.setDblAMCAmt(Double.parseDouble(obj[3].toString()));
				listData.add(objBean);
			}
			return listData;
			
		}
}
