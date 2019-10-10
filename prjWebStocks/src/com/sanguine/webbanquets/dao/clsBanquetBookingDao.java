package com.sanguine.webbanquets.dao;

import com.sanguine.webbanquets.model.clsBanquetBookingModelHd;

public interface clsBanquetBookingDao{

	public void funAddUpdateBanquetBookingHd(clsBanquetBookingModelHd objHdModel);
//	public void funAddUpdateBanquetBookingDtl(clsBanquetBookingModelDtl objDtlModel);
	
	public clsBanquetBookingModelHd funGetBookingData(String strBookingCode,String strClientCode);
	
	public void funDeleteRecord(String querry,String querryType);
	
	
	
	
}
