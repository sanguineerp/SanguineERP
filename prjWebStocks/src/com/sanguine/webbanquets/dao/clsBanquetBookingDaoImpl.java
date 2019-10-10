package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsBanquetBookingModelHd;
import com.sanguine.webbanquets.model.clsBanquetBookingModel_ID;
import com.sanguine.webbanquets.model.clsFunctionMasterModel;
import com.sanguine.webbanquets.model.clsFunctionMasterModel_ID;

@Repository("clsBanquetBookingDao")
public class clsBanquetBookingDaoImpl implements clsBanquetBookingDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateBanquetBookingHd(clsBanquetBookingModelHd objHdModel){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objHdModel);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsBanquetBookingModelHd funGetBookingData(String strBookingNo, String strClientCode)
	{
		return (clsBanquetBookingModelHd) webPMSSessionFactory.getCurrentSession().get(clsBanquetBookingModelHd.class,new clsBanquetBookingModel_ID(strBookingNo,strClientCode));
	}
	

	public void funDeleteRecord(String sql, String queryType) {
		if (queryType.equals("sql")) {
			webPMSSessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
		} else {
			webPMSSessionFactory.getCurrentSession().createQuery(sql).executeUpdate();
			//webPMSSessionFactory.getCurrentSession().delete();
		}
	}
	
}
