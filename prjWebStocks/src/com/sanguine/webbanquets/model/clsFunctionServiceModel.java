package com.sanguine.webbanquets.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
@Embeddable
public class clsFunctionServiceModel implements Serializable
{
private static final long serialVersionUID = 1L;

public clsFunctionServiceModel()
{
	
}
private String strServiceCode;
private String strServiceName;
private String strApplicable;



public String getStrServiceCode() {
	return strServiceCode;
}
public void setStrServiceCode(String strServiceCode) {
	this.strServiceCode = strServiceCode;
}
public String getStrApplicable() {
	return strApplicable;
}
public void setStrApplicable(String strApplicable) {
	this.strApplicable = strApplicable;
}
public String getStrServiceName() {
	return strServiceName;
}
public void setStrServiceName(String strServiceName) {
	this.strServiceName = strServiceName;
} 

}
