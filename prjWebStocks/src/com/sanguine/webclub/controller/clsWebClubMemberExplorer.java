package com.sanguine.webclub.controller;

import java.io.OutputStream;
import java.sql.Blob;
import java.util.ArrayList;
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
import com.sanguine.model.clsGroupMasterModel;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsGroupMasterService;
import com.sanguine.webclub.bean.clsWebClubGroupMasterBean;
import com.sanguine.webclub.bean.clsWebClubPreMemberProfileBean;
import com.sanguine.webclub.model.clsWebClubGroupMasterModel;
import com.sanguine.webclub.model.clsWebClubMemberPhotoModel;
import com.sanguine.webclub.model.clsWebClubMemberProfileModel;
import com.sanguine.webclub.model.clsWebClubPreMemberProfileModel;
import com.sanguine.webclub.service.clsWebClubMemberPhotoService;
import com.sanguine.webclub.service.clsWebClubMemberProfileService;
import com.sanguine.webclub.service.clsWebClubPreMemberProfileService;
@Controller
public class clsWebClubMemberExplorer {
	
	@Autowired
	private clsGlobalFunctions objGlobal;
	
	@Autowired
	private clsWebClubPreMemberProfileService objPreService;
	
	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;
	
	@Autowired
	private clsWebClubMemberPhotoService objWebClubMemberPhotoService;
	
	@Autowired
	private clsWebClubMemberProfileService objMemberProfileService;
	
	@RequestMapping(value = "/frmMemberExplorer", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmMemberExplorer_1", "command", new clsWebClubPreMemberProfileBean());
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmMemberExplorer", "command", new clsWebClubPreMemberProfileBean());
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/memberExplore", method = RequestMethod.GET)
	public @ResponseBody List funGetGropList(@RequestParam("code") String memCode,HttpServletRequest req) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		//clsWebClubPreMemberProfileModel objModel = objPreService.funGetWebClubPreMemberProfile(custCode, clientCode);
		String sqlMemCode = "select a.strCustomerCode from tblmembermaster a where a.strMemberCode='"+memCode+"' and a.strClientCode='"+clientCode+"'";
		List listMemCode = objGlobalFunctionsService.funGetListModuleWise(sqlMemCode, "sql");
		String custCode = "";
		if(listMemCode!=null && listMemCode.size()>0)
		{
			custCode = listMemCode.get(0).toString();
		}
		List<clsWebClubMemberProfileModel> objModelList = objMemberProfileService.funGetAllMember(custCode, clientCode);
		List list = null;
		for(int i=0;i<objModelList.size();i++)
		{
			if(objModelList.get(i).getStrMemberCode()!=null && objModelList.size()>0 && !objModelList.get(i).getStrMemberCode().equals(""))
			{
				list = new ArrayList<>();
				objModelList.get(i).setDteDateofBirth(objGlobal.funGetDate("yyyy-MM-dd", objModelList.get(i).getDteDateofBirth()));
				objModelList.get(i).setDteMembershipStartDate(objGlobal.funGetDate("yyyy-MM-dd", objModelList.get(i).getDteMembershipStartDate()));
				objModelList.get(i).setDteMembershipExpiryDate(objGlobal.funGetDate("yyyy-MM-dd", objModelList.get(i).getDteMembershipExpiryDate()));

				list.add(objModelList.get(i));
				
			}
		}
		return list;

	}
	
	@RequestMapping(value = "/loadImage", method = RequestMethod.GET)
	public @ResponseBody void funGetImage(@RequestParam("custCode") String custCode,HttpServletRequest req,HttpServletResponse response) {
		String clientCode = req.getSession().getAttribute("clientCode").toString();
		
			clsWebClubMemberPhotoModel obj = objWebClubMemberPhotoService.funGetWebClubMemberPhoto(custCode, clientCode);	
			
			try {
				Blob image = null;
				byte[] imgData = null;
				image = obj.getStrMemberImage();
				if (null != image && image.length() > 0) {
					imgData = image.getBytes(1, (int) image.length());
					response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
					OutputStream o = response.getOutputStream();
					o.write(imgData);
					o.flush();
					o.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
}
