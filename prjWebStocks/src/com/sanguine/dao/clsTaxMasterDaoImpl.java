package com.sanguine.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sanguine.model.clsTaxHdModel;
import com.sanguine.model.clsTaxSettlementMasterModel;

@Repository("clsTaxMasterDao")
public class clsTaxMasterDaoImpl implements clsTaxMasterDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void funAddUpdate(clsTaxHdModel object) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(object);
	}

	@Override
	public void funAddUpdateDtl(clsTaxSettlementMasterModel object) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().saveOrUpdate(object);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<clsTaxHdModel> funGetList() {
		// TODO Auto-generated method stub
		return (List<clsTaxHdModel>) sessionFactory.getCurrentSession().createCriteria(clsTaxHdModel.class).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public clsTaxHdModel funGetObject(String code, String clientCode) {
		// TODO Auto-generated method stub
		// return (clsTaxHdModel)
		// sessionFactory.getCurrentSession().get(clsTaxHdModel.class, new
		// clsTaxHdModel_ID(code,clientCode));

		Criteria cr = sessionFactory.getCurrentSession().createCriteria(clsTaxHdModel.class);
		cr.add(Restrictions.eq("strTaxCode", code));
		cr.add(Restrictions.eq("strClientCode", clientCode));
		List list = cr.list();

		clsTaxHdModel objModel = null;
		if (list.size() > 0) {
			objModel = (clsTaxHdModel) list.get(0);
			objModel.getListTaxSGDtl().size();
		}

		return objModel;
	}

	@SuppressWarnings("finally")
	public long funGetLastNo(String tableName, String masterName, String columnName) {
		long lastNo = 0;
		try {
			@SuppressWarnings("rawtypes")
			List listLastNo = sessionFactory.getCurrentSession().createSQLQuery("select max(" + columnName + ") from " + tableName).list();
			if (listLastNo.size() > 1) {
				lastNo = ((BigInteger) listLastNo.get(0)).longValue();
			}
			lastNo++;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return lastNo;
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	public List funGetDtlList(String taxCode, String clientCode) {
		Query query = sessionFactory.getCurrentSession().createQuery("from clsTaxHdModel");
		
		List list = query.list();
		return list;
	}

	@SuppressWarnings("rawtypes")
	public List funGetTaxes(String taxCode, String clientCode) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("select a.strTaxCode,a.strTaxDesc from tbltaxhd a " + " where a.strClientCode='" + clientCode + "' and a.strTaxCode<> '" + taxCode + "'");
		
		List list = query.list();
		return list;

	}
	@SuppressWarnings("rawtypes")
	public List funGetSubGroupList(String clientCode) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("select a.strSGCode,a.strSGName " + " from tblsubgroupmaster a where a.strClientCode='" + clientCode + "' ");
		
		List list = query.list();
		return list;

	}

	@SuppressWarnings("rawtypes")
	public List funGetSettlementList(String clientCode) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("select a.strSettlementCode,a.strSettlementDesc " + " from tblsettlementmaster a where a.strClientCode='" + clientCode + "' and a.strApplicable='true' ");
		List list = query.list();
		return list;

	}
	
	@SuppressWarnings("rawtypes")
	public List funGetTaxSettlement( String taxCode) {
		Query query = sessionFactory.getCurrentSession().createSQLQuery("select a.strSettlementCode, b.strSettlementDesc,a.strApplicable from tbltaxsettlement a,tblsettlementmaster b " + "where  a.strTaxCode= '"+taxCode+"' and a.strSettlementCode=b.strSettlementCode");
		
		List list = query.list();
		return list;
	}

}
