package com.sanguine.webbanquets.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.webbanquets.bean.clsSampleBean;
import com.sanguine.webbanquets.model.clsSampleModel;
import com.sanguine.webbanquets.service.clsSampleService;

@Controller
public class clsSampleController {

	@Autowired
	clsSampleService objSampleService;
	
	@RequestMapping(value = "/frmSampleMaster", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {

		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		
		
		objSampleService.funSaveData("WebBAnquet");
		
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmSampleMaster_1", "command", new clsSampleBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmSampleMaster", "command", new clsSampleBean());
		} else {
			return null;
		}
	}
}
