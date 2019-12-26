package com.sanguine.webbanquets.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sanguine.webbanquets.model.clsBanquetQuotationModelHd;
import com.sanguine.webbanquets.model.clsBanquetQuotationModel_ID;
import com.sanguine.webbanquets.model.clsFunctionMasterModel;
import com.sanguine.webbanquets.model.clsFunctionMasterModel_ID;

@Repository("clsBanquetQuotationDao")
public class clsBanquetQuotationDaoImpl implements clsBanquetQuotationDao{

	@Autowired
	private SessionFactory webPMSSessionFactory;

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public void funAddUpdateBanquetQuotationHd(clsBanquetQuotationModelHd objHdModel){
		webPMSSessionFactory.getCurrentSession().saveOrUpdate(objHdModel);
	}

	@Override
	@Transactional(value = "WebPMSTransactionManager")
	public clsBanquetQuotationModelHd funGetQuotationData(String strQuotationNo, String strClientCode)
	{
		return (clsBanquetQuotationModelHd) webPMSSessionFactory.getCurrentSession().get(clsBanquetQuotationModelHd.class,new clsBanquetQuotationModel_ID(strQuotationNo,strClientCode));
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
