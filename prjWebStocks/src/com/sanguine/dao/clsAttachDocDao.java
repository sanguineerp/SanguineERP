package com.sanguine.dao;

import java.util.List;

import com.sanguine.model.clsAttachDocModel;

public interface clsAttachDocDao {
	public void funSaveDoc(clsAttachDocModel objModel);

	public List<clsAttachDocModel> funListDocs(String docCode, String clientCode);

	public List funGetDoc(String code, String fileNo, String clientCode);

	public void funDeleteDoc(Long id);

	public void funDeleteAttachment(String docName, String dcode, String clientCode);
}
