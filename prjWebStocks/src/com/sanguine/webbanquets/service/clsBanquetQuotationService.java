package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsBanquetQuotationModelHd;


public interface clsBanquetQuotationService{

	public void funAddUpdateBanquetQuotationHd(clsBanquetQuotationModelHd objHdModel);
	//public void funAddUpdateBanquetQuotationDtl(clsBanquetQuotationModelDtl objDtlModel);
	
	public clsBanquetQuotationModelHd funGetQuotationData(String strQuotationCode,String strClientCode);
	
	public void funDeleteRecord(String query,String queryType);
	

}
