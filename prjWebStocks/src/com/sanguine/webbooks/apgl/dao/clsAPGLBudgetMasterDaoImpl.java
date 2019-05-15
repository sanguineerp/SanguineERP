package com.sanguine.webbooks.apgl.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sanguine.webbooks.apgl.model.clsAPGLBudgetModel;

@Repository("clsAPGLBudgetMasterDao")
public class clsAPGLBudgetMasterDaoImpl implements clsAPGLBudgetMasterDao {

	@Autowired
	private SessionFactory webBooksSessionFactory;

	public List funGetBudgetTableData(String strYear, String strClientCode) {

		String sql = " select a.strAccountCode,a.strAccountName,IFNULL(b.dblBudgetAmt,0.0), IFNULL(b.intID,' ') from tblacmaster a " + " left outer join tblbudget b on a.strAccountCode=b.strAccCode and a.strClientCode=b.strClientCode "
				+ " where  ";
				if(!strYear.isEmpty()){
					sql+=" b.strYear='" + strYear + "' and ";
				}
				sql+= " a.strClientCode='" + strClientCode + "'";
				if(!strYear.isEmpty()){
					sql+=" and b.strClientCode=a.strClientCode";
				}
		List list = webBooksSessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return list;
	}

	public void funSaveBudgetTableData(clsAPGLBudgetModel objBudgetModel) {

		webBooksSessionFactory.getCurrentSession().saveOrUpdate(objBudgetModel);
	}

}
