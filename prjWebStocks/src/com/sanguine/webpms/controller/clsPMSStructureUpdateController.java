package com.sanguine.webpms.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class clsPMSStructureUpdateController {
	
	@RequestMapping(value = "/frmPMSStructureUpdate", method = RequestMethod.GET)
	public ModelAndView funOpenStructureUpdateForm(Map<String, Object> model, HttpServletRequest req) {

		String urlHits = "1";
		try {
			urlHits = req.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPMSStructureUpdate_1");
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmPMSStructureUpdate");
		} else {
			return null;
		}

	}

}
