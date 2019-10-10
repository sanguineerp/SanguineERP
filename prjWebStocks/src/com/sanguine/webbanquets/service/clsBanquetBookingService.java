package com.sanguine.webbanquets.service;

import com.sanguine.webbanquets.model.clsBanquetBookingModelHd;


public interface clsBanquetBookingService{

	public void funAddUpdateBanquetBookingHd(clsBanquetBookingModelHd objHdModel);
	//public void funAddUpdateBanquetBookingDtl(clsBanquetBookingModelDtl objDtlModel);
	
	public clsBanquetBookingModelHd funGetBookingData(String strBookingCode,String strClientCode);
	
	public void funDeleteRecord(String query,String queryType);
	

}
