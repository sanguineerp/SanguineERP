package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsBanquetQuotationModelHd;

public interface clsBanquetQuotationDao{

	public void funAddUpdateBanquetQuotationHd(clsBanquetQuotationModelHd objHdModel);
//	public void funAddUpdateBanquetQuotationDtl(clsBanquetQuotationModelDtl objDtlModel);
	
	public clsBanquetQuotationModelHd funGetQuotationData(String strQuotationCode,String strClientCode);
	
	public void funDeleteRecord(String querry,String querryType);
	
	
	
	
}
