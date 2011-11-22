package com.sanguine.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sanguine.bean.clsProductBatchBean;
import com.sanguine.model.clsBatchHdModel;
import com.sanguine.model.clsBatchHdModel_ID;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsProductBatchService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;

@Controller
public class clsManualBatch {

	final static Logger logger = Logger.getLogger(clsManualBatch.class);
	@Autowired
	clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsProductBatchService objBatchProcessService;
	
	@Autowired
	clsGlobalFunctions objGlobal;
	

	@Autowired
	private clsSetupMasterService objSetupMasterService;

	/**
	 * Open Batch Form
	 * 
	 * @param objBean
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/frmManualBatch", method = RequestMethod.GET)
	public ModelAndView funOpenBatchProcessForm(@ModelAttribute("setManualBatch") clsProductBatchBean objBean, HttpServletRequest request, Model model) {
		List<clsBatchHdModel> batchList = new ArrayList<clsBatchHdModel>();
		if (request.getSession().getAttribute("ManualBatchItemList") != null) {
			batchList = (List<clsBatchHdModel>) request.getSession().getAttribute("ManualBatchItemList");
			model.addAttribute("BatchList", batchList);
		}
		return new ModelAndView("frmManualBatch", "command", objBean);
	}

	/**
	 * Load Batch Data
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "finally" })
	@RequestMapping(value = "/loadBatchData", method = RequestMethod.GET)
	public @ResponseBody clsBatchHdModel funLoadBatchData(HttpServletRequest request) {
		List batchList = new ArrayList();
		clsBatchHdModel batchModel = null;
		try {
			String strBatchCode = request.getParameter("BatchCode").toString();
			String clientCode = request.getSession().getAttribute("clientCode").toString();
			StringBuilder sqlBuilder = new StringBuilder("from clsBatchHdModel where strBatchCode='" + strBatchCode + "' and strClientCode='" + clientCode + "'");
			batchList = objGlobalFunctionsService.funGetList(sqlBuilder.toString(), "hql");
			batchModel = (clsBatchHdModel) batchList.get(0);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		} finally {
			return batchModel;
		}
	}

	/**
	 * Save Batch Data
	 * 
	 * @param objBean
	 * @param result
	 * @param request
	 * @return
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/saveManualBatch", method = RequestMethod.POST)
	public @ResponseBody String funSaveBatch(@ModelAttribute("setBatchAttribute") @Valid clsProductBatchBean objBean, BindingResult result, HttpServletRequest request) {
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String propCode = request.getSession().getAttribute("propertyCode").toString();
		String returnvalue = "";
		objGlobal = new clsGlobalFunctions();
		try {
			if (!result.hasErrors()) {
				
				clsPropertySetupModel objSetup = objSetupMasterService
						.funGetObjectPropertySetup(propCode, clientCode);
			
				if(objSetup.getStrFifo().equalsIgnoreCase("N")){
				List<clsBatchHdModel> batchList = objBean.getListBatchDtl();
				for (int i = 0; i < batchList.size(); i++) {
					clsBatchHdModel tempBatchModel = batchList.get(i);
					clsBatchHdModel batchModel = new clsBatchHdModel(new clsBatchHdModel_ID(objBean.getStrMISCode(), tempBatchModel.getStrProdCode(), clientCode,tempBatchModel.getStrBatchCode()));
					batchModel.setStrTransCode(objBean.getStrMISCode());
					batchModel.setStrProdCode(tempBatchModel.getStrProdCode());
					batchModel.setStrClientCode(clientCode);
					batchModel.setStrBatchCode(tempBatchModel.getStrBatchCode());
					batchModel.setDblQty(tempBatchModel.getDblQty());
					batchModel.setDblPendingQty(tempBatchModel.getDblPendingQty());
					batchModel.setDtExpiryDate(tempBatchModel.getDtExpiryDate());
					batchModel.setStrManuBatchCode(tempBatchModel.getStrManuBatchCode());
					batchModel.setStrTransType("MIS");
					batchModel.setStrPropertyCode(propCode);
					batchModel.setStrToLocCode("");
					batchModel.setStrFromLocCode("");
					batchModel.setStrTransCodeforUpdate("");
					objBatchProcessService.funSaveOrUpdateBatch(batchModel);
					StringBuilder sqlBuilder = new StringBuilder("update tblbatchhd set dblPendingQty='" + tempBatchModel.getDblPendingQty() + "' where strBatchCode='" + tempBatchModel.getStrBatchCode() + "' ");
					objGlobalFunctionsService.funUpdate(sqlBuilder.toString(), "sql");
					returnvalue = "Inserted";
					request.getSession().removeAttribute("rptMISCode");
					request.getSession().removeAttribute("ManualBatchItemList");
				}
			}
				
				else
					{
					List<clsBatchHdModel> batchListPending = objBean.getListBatchDtl();
					
					for (int i = 0; i < batchListPending.size(); i++) {
					clsBatchHdModel tempBatchModel = batchListPending.get(i);
					clsBatchHdModel batchModel = new clsBatchHdModel();
					double dblTempBatchQty = tempBatchModel.getDblQty(); 
					String sqlBatchCode = "select * from tblbatchhd a where a.strProdCode='"+tempBatchModel.getStrProdCode()+"' "
							+ "and a.dblPendingQty>0 and a.strClientCode='"+clientCode+"' order by a.strBatchCode asc";
					List listBatch = objGlobalFunctionsService.funGetList(sqlBatchCode, "sql");
					if(listBatch!=null && listBatch.size()>0)
					{
						
						for (int cnt = 0; cnt < listBatch.size(); cnt++) {
							
							Object[] arrObject = (Object[]) listBatch.get(cnt);
							String strClientCode  = arrObject[0].toString();
							String strProdCode  = arrObject[1].toString();
							String strTransCode  = arrObject[2].toString();
							String strBatchCode  = arrObject[3].toString();
							double dblPendingQty= Double.parseDouble(arrObject[4].toString());
							double dblQty  = Double.parseDouble(arrObject[5].toString());
							String strExpiryDate  = arrObject[6].toString();
							int srNo  = Integer.parseInt(arrObject[7].toString());
							String strFormLocCode  = arrObject[8].toString();
							String strManualBatchCode  = arrObject[9].toString();
							String strPropCode  = arrObject[10].toString();
							String strToLocCode  = arrObject[11].toString();
							String strTranssCodeForUpdate  = arrObject[12].toString();
							String strTransType  = arrObject[13].toString();
							if(dblTempBatchQty<=dblPendingQty)
							{
							double dblTempPendingMisQty = dblTempBatchQty;
							dblTempBatchQty = dblPendingQty-dblTempBatchQty;
							
							batchModel.setStrTransCode(objBean.getStrMISCode());
							batchModel.setStrProdCode(tempBatchModel.getStrProdCode());
							batchModel.setStrClientCode(clientCode);
							batchModel.setStrBatchCode(strBatchCode);
							batchModel.setDblQty(dblTempPendingMisQty);
							if(dblTempBatchQty<0)
							{
								dblTempBatchQty=0;
							}
							else
							{
								batchModel.setDblPendingQty(0.0);
							}
							batchModel.setDblPendingQty(dblTempPendingMisQty);
							batchModel.setDtExpiryDate(tempBatchModel.getDtExpiryDate());
							batchModel.setStrManuBatchCode("");
							batchModel.setStrTransType("MIS");
							batchModel.setStrPropertyCode(propCode);
							batchModel.setStrToLocCode("");
							batchModel.setStrFromLocCode("");
							batchModel.setStrTransCodeforUpdate("");
							objBatchProcessService.funSaveOrUpdateBatch(batchModel);
							StringBuilder sqlBuilder = new StringBuilder("update tblbatchhd set dblPendingQty='"+dblTempBatchQty+"' where strBatchCode='" + strBatchCode + "' and strManuBatchCode!='' ");
							objGlobalFunctionsService.funUpdate(sqlBuilder.toString(), "sql");
							break;
							}
							else if (dblTempBatchQty>0)
							{
								dblTempBatchQty = dblTempBatchQty-dblPendingQty;

								batchModel.setStrTransCode(objBean.getStrMISCode());
								batchModel.setStrProdCode(tempBatchModel.getStrProdCode());
								batchModel.setStrClientCode(clientCode);
								batchModel.setStrBatchCode(strBatchCode);
								batchModel.setDblQty(dblPendingQty);
								batchModel.setDblPendingQty(dblPendingQty);
								batchModel.setDtExpiryDate(tempBatchModel.getDtExpiryDate());
								batchModel.setStrManuBatchCode(tempBatchModel.getStrManuBatchCode());
								batchModel.setStrTransType("MIS");
								batchModel.setStrPropertyCode(propCode);
								batchModel.setStrToLocCode("");
								batchModel.setStrFromLocCode("");
								batchModel.setStrTransCodeforUpdate("");
								objBatchProcessService.funSaveOrUpdateBatch(batchModel);
								StringBuilder sqlBuilder = new StringBuilder("update tblbatchhd set dblPendingQty='0' where strBatchCode='" + strBatchCode + "' and strManuBatchCode!=''");
								objGlobalFunctionsService.funUpdate(sqlBuilder.toString(), "sql");
								
							}
					}
					}				
					}			
				}
			}
		} catch (Exception e) {
			returnvalue = "Inserted fail";
			logger.error("Sorry, something wrong!", e);
			e.printStackTrace();
		} finally {
			return returnvalue;
		}

	}
}
