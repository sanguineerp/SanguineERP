package com.sanguine.webbooks.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.webbooks.bean.clsAMCFlashBean;

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
		public void funLoadAMCData(Map<String, Object> model, HttpServletRequest request) {
		
			String clientCode = request.getSession().getAttribute("clientCode").toString();
			String sql="select a.strDebtorFullName,a.dteStartDate,a. from tblsundarydebtormaster a where a.strClientCode='"+clientCode+"' ";
			List list = objGlobalService.funGetList(sql);
			List<clsAMCFlashBean>listData=new ArrayList<clsAMCFlashBean>();
			for(int i=0;i<list.size();i++)
			{
				clsAMCFlashBean objBean=new clsAMCFlashBean();
//				objBean.set
			}
			
		}
}
