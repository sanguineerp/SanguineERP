package com.sanguine.webpms.service;

import java.util.List;

import com.sanguine.webpms.model.clsPMSSettlementTaxMasterModel;
import com.sanguine.webpms.model.clsPMSTaxMasterModel;

public interface clsPMSTaxMasterService {

	public void funAddUpdatePMSTaxMaster(clsPMSTaxMasterModel objMaster);

	public clsPMSTaxMasterModel funGetPMSTaxMaster(String taxCode, String clientCode);

	public List<String> funGetPMSDepartments(String clientCode);

	public List<String> funGetIncomeHead(String clientCode);

	public List<String> funGetPMSTaxes(String clientCode);

	public List<String> funGetPMSTaxGroup(String clientCode);

	public String funGetCodeFromName(String fieldToBeSeleted, String fieldName, String fromFieldNameValue, String tableName, String clientCode);

	public String funGetMasterName(String query);
	
	public void funAddUpdatePMSSettlementTaxMaster(clsPMSSettlementTaxMasterModel objMaster);

}
