package com.sanguine.webclub.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sanguine.webclub.bean.clsWebClubPDCBean;
import com.sanguine.webclub.model.clsWebClubPDCModel;
import com.sanguine.webclub.model.clsWebClubPDCModel_ID;

@Repository("clsWebClubPDCDao")
public class clsWebClubPDCDaoImpl implements clsWebClubPDCDao{

	@Autowired
	private SessionFactory WebClubSessionFactory;
	
	@Override
	public void funAddUpdateWebClubPDC(clsWebClubPDCModel objMaster){
		WebClubSessionFactory.getCurrentSession().save(objMaster);
	}
	@Override
	public void funUpdateWebClubPDC(clsWebClubPDCModel objMaster){
		WebClubSessionFactory.getCurrentSession().update(objMaster);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List funGetWebClubPDC(String memCode, String clientCode) {
		String hql = "from clsWebClubPDCModel a WHERE a.strMemCode=:memCode and a.strClientCode= :clientCode ";
		
		Query query = WebClubSessionFactory.getCurrentSession().createQuery(hql);
		query.setParameter("memCode", memCode);
		query.setParameter("clientCode", clientCode);
		@SuppressWarnings("rawtypes")
		List list = query.list();
		return list;
	}
	
	
	@Override
	public void funDeleteDtlRecieved(String memCode, String clientCode) {
		
		String sql= "delete from clsWebClubPDCModel where strMemCode='"+memCode+"' and strType='Received' and strClientCode='"+clientCode+"'  ";
		try {			
			 WebClubSessionFactory.getCurrentSession().createQuery(sql).executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void funDeleteDtlIssued(String memCode, String clientCode) {
		
		String sql= "delete from clsWebClubPDCModel where strMemCode='"+memCode+"' and strType='Issued' and strClientCode='"+clientCode+"'  ";
		try {			
			 WebClubSessionFactory.getCurrentSession().createQuery(sql).executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	
	

}
