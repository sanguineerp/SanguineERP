package com.sanguine.webbooks.apgl.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.jdbc.Connection;
import com.sanguine.base.service.intfBaseService;
import com.sanguine.controller.clsGlobalFunctions;
import com.sanguine.model.clsPropertySetupModel;
import com.sanguine.service.clsCurrencyMasterService;
import com.sanguine.service.clsGlobalFunctionsService;
import com.sanguine.service.clsSetupMasterService;
import com.sanguine.webbooks.bean.clsCreditorOutStandingReportBean;
import com.sanguine.webbooks.bean.clsDebtorLedgerBean;
import com.sanguine.webbooks.bean.clsWebBooksAgeingReportBean;
import com.sanguine.webbooks.model.clsLedgerSummaryModel;

@Controller
public class clsCreditorAgeingReportController {

	private clsGlobalFunctions objGlobal = null;

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private clsSetupMasterService objSetupMasterService;

	@Autowired
	private clsGlobalFunctionsService objGlobalFunctionsService;

	@Autowired
	private clsGlobalFunctions objGlobalFunctions;

	@Autowired
	private clsCurrencyMasterService objCurrencyMasterService;

	@Autowired
	intfBaseService objBaseService;

	@RequestMapping(value = "/frmCreditorAgeingReport", method = RequestMethod.GET)
	public ModelAndView funOpenForm(Map<String, Object> model, HttpServletRequest request) {
		String urlHits = "1";
		try {
			urlHits = request.getParameter("saddr").toString();
		} catch (NullPointerException e) {
			urlHits = "1";
		}
		model.put("urlHits", urlHits);
		clsWebBooksAgeingReportBean objBean = new clsWebBooksAgeingReportBean();
		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String propertyCode = request.getSession().getAttribute("propertyCode").toString();
		Map<String, String> hmCurrency = objCurrencyMasterService.funCurrencyListToDisplay(clientCode);
		if (hmCurrency.isEmpty()) {
			hmCurrency.put("", "");
		}
		model.put("currencyList", hmCurrency);

		if ("2".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmCreditorAgeingReport_1", "command", objBean);
		} else if ("1".equalsIgnoreCase(urlHits)) {
			return new ModelAndView("frmCreditorAgeingReport", "command", objBean);
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/getCreditorAgeingData", method = RequestMethod.GET)
	public @ResponseBody Map<String, List<Object>> funGetAgeingData(HttpServletRequest request,HttpServletResponse response) {
		return funGetAgeingMapData(request);
	}

	@RequestMapping(value = "/exportCreditorAgeingData", method = RequestMethod.GET)
	private ModelAndView funExportCreditorAgeingData(HttpServletRequest request, HttpServletResponse response) {
		Map<String, List<Object>> mapOSandAgeing = funGetAgeingMapData(request);

		String clientCode = request.getSession().getAttribute("clientCode").toString();
		String propertyCode = request.getSession().getAttribute("propertyCode").toString();

		String glCode = request.getParameter("glCode").toString();
		String fromDate = request.getParameter("fromDate").toString();
		String toDate = request.getParameter("toDate").toString();
		String toDateInddMMyyyy = toDate;

		String currency = request.getParameter("currency").toString();
		String DCType = "Creditor";

		String webStockDB = request.getSession().getAttribute("WebStockDB").toString();

		List listAgeingData = new ArrayList();

		try {
			StringBuilder sbSql = new StringBuilder();
			sbSql.append("select dblConvToBaseCurr from " + webStockDB + ".tblcurrencymaster where strCurrencyCode='"
					+ currency + "' and strClientCode='" + clientCode + "' ");
			List list = objBaseService.funGetList(sbSql, "sql");
			double conversionRate = 1;
			if (list != null && list.size() > 0) {
				Double.parseDouble(list.get(0).toString());
			}

			double totalTaxableAmt = 0, totalTaxAmt = 0;

			List listOfColumns = mapOSandAgeing.get("header");

			String[] columnNames = new String[listOfColumns.size()];

			for (int i = 0; i < listOfColumns.size(); i++) {
				columnNames[i] = listOfColumns.get(i).toString();
			}

			listAgeingData.add(columnNames);

			List<List> listDebtorAgeingData = new ArrayList<List>();

			for (Map.Entry<String, List<Object>> entry : mapOSandAgeing.entrySet()) {
				String debtorCode = entry.getKey();
				List<Object> listOfOSandAgeing = entry.getValue();

				listDebtorAgeingData.add(listOfOSandAgeing);

			}

			List<String> listTotals = new ArrayList<String>();
			listTotals.add("");
			listTotals.add("");
			listTotals.add("");
			listTotals.add("");
			listTotals.add("");
			listDebtorAgeingData.add(listTotals);

			listAgeingData.add(listDebtorAgeingData);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("excelView", "stocklist", listAgeingData);

	}

	private Map<String, List<Object>> funGetAgeingMapData(HttpServletRequest request) {

		Map<String, List<Object>> mapOSandAgeing = new HashMap<String, List<Object>>();

		try {

			String clientCode = request.getSession().getAttribute("clientCode").toString();
			String propertyCode = request.getSession().getAttribute("propertyCode").toString();

			String glCode = request.getParameter("glCode").toString();
			String fromDate = request.getParameter("fromDate").toString();
			String toDate = request.getParameter("toDate").toString();
			String toDateInddMMyyyy = toDate;

			String currency = request.getParameter("currency").toString();
			String DCType = "Creditor";

			SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

			Date dteFromDateForReport = objSimpleDateFormat.parse(fromDate);
			int intFromDateYear = dteFromDateForReport.getYear() + 1900;
			int intFromDateMonth = dteFromDateForReport.getMonth() + 1;
			int intFromDateDay = dteFromDateForReport.getDate();

			LocalDate dteFromDateForOutStanding = LocalDate.of(intFromDateYear, intFromDateMonth, intFromDateDay);
			dteFromDateForOutStanding = dteFromDateForOutStanding.plusDays(1);

			int intFromDateYearForOutStanding = dteFromDateForOutStanding.getYear();
			int intFromDateMonthForOutStanding = dteFromDateForOutStanding.getMonthValue();
			int intFromDateDayForOutStanding = dteFromDateForOutStanding.getDayOfMonth();

			String fromDateToCalculateOutStanding = intFromDateYearForOutStanding + "-" + intFromDateMonthForOutStanding
					+ "-" + intFromDateDayForOutStanding;

			Date dteToDateForReport = objSimpleDateFormat.parse(toDate);
			int intToDateYear = dteToDateForReport.getYear() + 1900;
			int intToDateMonth = dteToDateForReport.getMonth() + 1;
			int intToDateDay = dteToDateForReport.getDate();

			LocalDate dteToDateForOutStanding = LocalDate.of(intToDateYear, intToDateMonth, intToDateDay);

			int intToDateYearForOutStanding = dteToDateForOutStanding.getYear();
			int intToDateMonthForOutStanding = dteToDateForOutStanding.getMonthValue();
			int intToDateDayForOutStanding = dteToDateForOutStanding.getDayOfMonth();

			String toDateToCalculateuteOutStanding = intToDateYearForOutStanding + "-" + intToDateMonthForOutStanding
					+ "-" + intToDateDayForOutStanding;

			fromDate = objGlobalFunctions.funGetDate("yyyy-MM-dd", fromDate);
			toDate = objGlobalFunctions.funGetDate("yyyy-MM-dd", toDate);

			String startDateInddMMyyyy = request.getSession().getAttribute("startDate").toString();
			String startDate = startDateInddMMyyyy.split("/")[2] + "-" + startDateInddMMyyyy.split("/")[1] + "-"
					+ startDateInddMMyyyy.split("/")[0];

			String reportFromDate = fromDate;
			String reportToDate = toDate;
			String fromDateToCalculateBal = startDate;
			String toDateToCalculateBal = fromDate;

			StringBuilder sbSql = new StringBuilder();

			/**
			 * start for calculating outstanding
			 */
			Map<String, clsCreditorOutStandingReportBean> hmOutstanding = objGlobalFunctions
					.funCalCreditorDebtorOPBalance(glCode, clientCode, fromDateToCalculateBal, toDateToCalculateBal,
							DCType, request);

			sbSql.setLength(0);
			sbSql.append("select debtor.strDebtorCode,'',debtor.strCrDr, sum(ifnull(debtor.dblAmt,0.00)) "
					+ " from tbljvdebtordtl debtor inner join tbljvhd hd on hd.strVouchNo=debtor.strVouchNo "
					+ " where debtor.strAccountCode='" + glCode
					+ "' and debtor.strCrDr='Dr' and date(hd.dteVouchDate) between '" + fromDateToCalculateOutStanding
					+ "' AND '" + toDateToCalculateuteOutStanding + "' " + " and hd.strClientCode='" + clientCode + "' "
					+ " group by debtor.strCrDr,debtor.strDebtorCode ");
			List listJVAmt = objBaseService.funGetListModuleWise(sbSql, "sql", "WebBooks");
			if (listJVAmt != null && listJVAmt.size() > 0) {
				for (int j = 0; j < listJVAmt.size(); j++) {
					Object[] arrObj = (Object[]) listJVAmt.get(j);
					clsCreditorOutStandingReportBean objOutStBean = new clsCreditorOutStandingReportBean();

					if (hmOutstanding.containsKey(arrObj[0].toString())) {
						objOutStBean = hmOutstanding.get(arrObj[0].toString());
						double debitAmt = objOutStBean.getDblDrAmt() + Double.parseDouble(arrObj[3].toString());
						objOutStBean.setDblDrAmt(debitAmt);
						// objOutStBean.setDblCrAmt(objOutStBean.getDblBalAmt());
					} else {
						objOutStBean.setStrDebtorCode(arrObj[0].toString());
						objOutStBean.setStrDebtorName(arrObj[1].toString());
						objOutStBean.setDblBalAmt(0.00);
						objOutStBean.setDblDrAmt(Double.parseDouble(arrObj[3].toString()));
						objOutStBean.setDblCrAmt(0.00);
						objOutStBean.setDblOpnAmt(0.00);
					}
					objOutStBean.setDblBalAmt(objOutStBean.getDblBalAmt() - objOutStBean.getDblDrAmt());

					hmOutstanding.put(arrObj[0].toString(), objOutStBean);
				}
			}

			sbSql.setLength(0);
			sbSql.append("select debtor.strDebtorCode,'',debtor.strCrDr, sum(ifnull(debtor.dblAmt,0.00)) "
					+ " from tblpaymentdebtordtl debtor inner join tblpaymenthd hd on hd.strVouchNo=debtor.strVouchNo "
					+ " where debtor.strAccountCode='" + glCode
					+ "' and debtor.strCrDr='Dr' and date(hd.dteVouchDate) between '" + fromDateToCalculateOutStanding
					+ "' AND '" + toDateToCalculateuteOutStanding + "' " + " and hd.strClientCode='" + clientCode + "' "
					+ " group by debtor.strCrDr,debtor.strDebtorCode ");
			List listPaymentAmt = objBaseService.funGetListModuleWise(sbSql, "sql", "WebBooks");
			if (listPaymentAmt != null && listPaymentAmt.size() > 0) {
				for (int j = 0; j < listPaymentAmt.size(); j++) {
					Object[] arrObj = (Object[]) listPaymentAmt.get(j);
					clsCreditorOutStandingReportBean objOutStBean = new clsCreditorOutStandingReportBean();

					if (hmOutstanding.containsKey(arrObj[0].toString())) {
						objOutStBean = hmOutstanding.get(arrObj[0].toString());
						double debitAmt = objOutStBean.getDblDrAmt() + Double.parseDouble(arrObj[3].toString());
						objOutStBean.setDblDrAmt(debitAmt);
						// objOutStBean.setDblCrAmt(objOutStBean.getDblBalAmt());
					} else {
						objOutStBean.setStrDebtorCode(arrObj[0].toString());
						objOutStBean.setStrDebtorName(arrObj[1].toString());
						objOutStBean.setDblBalAmt(0.00);
						objOutStBean.setDblDrAmt(Double.parseDouble(arrObj[3].toString()));
						objOutStBean.setDblCrAmt(0.00);
						objOutStBean.setDblOpnAmt(0.00);
					}
					objOutStBean.setDblBalAmt(objOutStBean.getDblBalAmt() - objOutStBean.getDblDrAmt());

					hmOutstanding.put(arrObj[0].toString(), objOutStBean);
				}
			}

			sbSql.setLength(0);
			sbSql.append("select debtor.strDebtorCode,'',debtor.strCrDr, sum(ifnull(debtor.dblAmt,0.00)) "
					+ " from tblreceiptdebtordtl debtor inner join tblreceipthd hd on hd.strVouchNo=debtor.strVouchNo "
					+ " where debtor.strAccountCode='" + glCode
					+ "' and debtor.strCrDr='Dr' and date(hd.dteVouchDate) between '" + fromDateToCalculateOutStanding
					+ "' AND '" + toDateToCalculateuteOutStanding + "' " + " and hd.strClientCode='" + clientCode + "' "
					+ " group by debtor.strCrDr,debtor.strDebtorCode ");
			List listReceiptAmt = objBaseService.funGetListModuleWise(sbSql, "sql", "WebBooks");
			if (listReceiptAmt != null && listReceiptAmt.size() > 0) {
				for (int j = 0; j < listReceiptAmt.size(); j++) {
					Object[] arrObj = (Object[]) listReceiptAmt.get(j);
					clsCreditorOutStandingReportBean objOutStBean = new clsCreditorOutStandingReportBean();
					if (hmOutstanding.containsKey(arrObj[0].toString())) {
						objOutStBean = hmOutstanding.get(arrObj[0].toString());
						double debitAmt = objOutStBean.getDblDrAmt() + Double.parseDouble(arrObj[3].toString());
						objOutStBean.setDblDrAmt(debitAmt);
						// objOutStBean.setDblCrAmt(objOutStBean.getDblBalAmt());
					} else {
						objOutStBean.setStrDebtorCode(arrObj[0].toString());
						objOutStBean.setStrDebtorName(arrObj[1].toString());
						objOutStBean.setDblBalAmt(0.00);
						objOutStBean.setDblDrAmt(Double.parseDouble(arrObj[3].toString()));
						objOutStBean.setDblCrAmt(0.00);
						objOutStBean.setDblOpnAmt(0.00);
					}
					objOutStBean.setDblBalAmt(objOutStBean.getDblBalAmt() - objOutStBean.getDblDrAmt());

					hmOutstanding.put(arrObj[0].toString(), objOutStBean);
				}
			}

			/**
			 * end for calculating outstanding
			 */

			/**
			 * start of ageing calculation
			 */

			for (Map.Entry<String, clsCreditorOutStandingReportBean> entry : hmOutstanding.entrySet()) {

				String debtorCode = entry.getKey();

				clsCreditorOutStandingReportBean objCreditorOutStandingReportBean = entry.getValue();
				if (objCreditorOutStandingReportBean.getDblBalAmt() > 0) {
					if (mapOSandAgeing.containsKey(debtorCode)) {
						mapOSandAgeing.get(debtorCode).add(objCreditorOutStandingReportBean.getDblBalAmt());
					} else {
						List<Object> listOfOSandAgeing = new LinkedList<>();

						listOfOSandAgeing.add(objCreditorOutStandingReportBean.getStrDebtorName());
						listOfOSandAgeing.add(objCreditorOutStandingReportBean.getDblBalAmt());

						mapOSandAgeing.put(debtorCode, listOfOSandAgeing);
					}
				}
			}

			LocalDate dteToDateForAgeing = LocalDate.of(intToDateYear, intToDateMonth, intToDateDay);

			int intToDateYearForAgeing = dteToDateForOutStanding.getYear();
			int intToDateMonthForAgeing = dteToDateForOutStanding.getMonthValue();
			int intToDateDayForAgeing = dteToDateForOutStanding.getDayOfMonth();

			String toDateForAgeing = intToDateYearForAgeing + "-" + intToDateMonthForAgeing + "-"
					+ intToDateDayForAgeing;

			LocalDate dteFromDateForAgeing = dteToDateForAgeing.minusDays(30);

			int intFromDateYearForAgeing = dteFromDateForAgeing.getYear();
			int intFromDateMonthForAgeing = dteFromDateForAgeing.getMonthValue();
			int intFromDateDayForAgeing = dteFromDateForAgeing.getDayOfMonth();

			String fromDateForAgeing = intFromDateYearForAgeing + "-" + intFromDateMonthForAgeing + "-"
					+ intFromDateDayForAgeing;
			;

			int terminateConditionValue = 0;
			while (true) {
				if (terminateConditionValue == 2) {
					break;
				}

				/*
				 * debit calculation logic start
				 * 
				 * 
				 */

				sbSql.setLength(0);

				sbSql.append(
						"SELECT cm.strCreditorCode,cm.strCreditorFullName,ifnull(c.CrDr,'Dr'), ifnull(SUM(c.DebirAmt),0)  ");
				sbSql.append("FROM tblsundarycreditormaster cm " + "left Outer join   ");
				sbSql.append("(  ");
				sbSql.append(
						"SELECT debtor.strDebtorCode DebtorCode,' ' blank,debtor.strCrDr CrDr, SUM(IFNULL(debtor.dblAmt,0.00)) DebirAmt   ");
				sbSql.append("FROM tbljvdebtordtl debtor   ");
				sbSql.append("INNER JOIN tbljvhd hd ON hd.strVouchNo=debtor.strVouchNo   ");
				sbSql.append("WHERE debtor.strAccountCode='" + glCode
						+ "' AND debtor.strCrDr='Cr' AND DATE(hd.dteVouchDate) BETWEEN '" + fromDateForAgeing
						+ "' AND '" + toDateForAgeing + "' AND hd.strClientCode='" + clientCode + "'   ");
				// sbSql.append("and debtor.strDebtorCode='D00000355' ");
				sbSql.append("GROUP BY debtor.strCrDr,debtor.strDebtorCode    ");
				sbSql.append("union all   ");
				sbSql.append(
						"SELECT debtor.strDebtorCode DebtorCode,' ' blank,debtor.strCrDr CrDr, SUM(IFNULL(debtor.dblAmt,0.00)) DebirAmt   ");
				sbSql.append("FROM tblpaymentdebtordtl debtor   ");
				sbSql.append("INNER JOIN tblpaymenthd hd ON hd.strVouchNo=debtor.strVouchNo   ");
				sbSql.append("WHERE debtor.strAccountCode='" + glCode
						+ "' AND debtor.strCrDr='Cr' AND DATE(hd.dteVouchDate) BETWEEN '" + fromDateForAgeing
						+ "' AND '" + toDateForAgeing + "' AND hd.strClientCode='" + clientCode + "'   ");
				// sbSql.append("and debtor.strDebtorCode='D00000355' ");
				sbSql.append("GROUP BY debtor.strCrDr,debtor.strDebtorCode    ");
				sbSql.append("union all   ");
				sbSql.append(
						"SELECT debtor.strDebtorCode DebtorCode,' ' blank,debtor.strCrDr CrDr, SUM(IFNULL(debtor.dblAmt,0.00)) DebirAmt   ");
				sbSql.append("FROM tblreceiptdebtordtl debtor   ");
				sbSql.append("INNER JOIN tblreceipthd hd ON hd.strVouchNo=debtor.strVouchNo   ");
				sbSql.append("WHERE debtor.strAccountCode='" + glCode
						+ "' AND debtor.strCrDr='Cr' AND DATE(hd.dteVouchDate) BETWEEN '" + fromDateForAgeing
						+ "' AND '" + toDateForAgeing + "' AND hd.strClientCode='" + clientCode + "'   ");
				// sbSql.append("and debtor.strDebtorCode='D00000355' ");
				sbSql.append("GROUP BY debtor.strCrDr,debtor.strDebtorCode    ");
				sbSql.append(") c  on cm.strCreditorCode= c.DebtorCode ");
				sbSql.append("GROUP BY cm.strCreditorCode  ");

				int listSise = -1;
				List listDrJVAmt = objBaseService.funGetListModuleWise(sbSql, "sql", "WebBooks");
				if (listDrJVAmt != null && listDrJVAmt.size() > 0) {
					for (int j = 0; j < listDrJVAmt.size(); j++) {
						Object[] arrObj = (Object[]) listDrJVAmt.get(j);

						String debtorCode = arrObj[0].toString();
						double creditAmt = Double.parseDouble(arrObj[3].toString());

						if (mapOSandAgeing.containsKey(debtorCode) && hmOutstanding.containsKey(debtorCode)) {

							clsCreditorOutStandingReportBean objDebtorOutStanding = hmOutstanding.get(debtorCode);
							double oldOutStandingAmt = objDebtorOutStanding.getDblBalAmt();
							double newOutStandingAmt = 0;
							if (creditAmt < oldOutStandingAmt) {
								newOutStandingAmt = oldOutStandingAmt - creditAmt;
							} else// oldOutStandingAmt < debitAmt
							{
								creditAmt = oldOutStandingAmt;
							}
							objDebtorOutStanding.setDblBalAmt(newOutStandingAmt);
							List<Object> listOfOSandAgeing = mapOSandAgeing.get(debtorCode);
							listOfOSandAgeing.add(creditAmt);
							if (listOfOSandAgeing.size() > listSise) {
								listSise = listOfOSandAgeing.size();
							}
						}
						// else
						// {
						// List<Double>listOfOSandAgeing=new LinkedList<>();
						// listOfOSandAgeing.add(debitAmt);
						//
						// mapOSandAgeing.put(debtorCode, listOfOSandAgeing);
						// }
					}
				}

				/*
				 * debit calculation logic end
				 * 
				 * 
				 */

				if (terminateConditionValue == 1) {
					terminateConditionValue = 2;
				}

				dteToDateForAgeing = dteFromDateForAgeing.minusDays(1);
				dteFromDateForAgeing = dteFromDateForAgeing.minusDays(30);
				if (dteFromDateForAgeing.isBefore(dteFromDateForOutStanding) && terminateConditionValue < 2) {
					terminateConditionValue = 1;

					dteFromDateForAgeing = dteFromDateForOutStanding;
				}
				
				if(dteToDateForAgeing.isBefore(dteFromDateForAgeing))
				{
					terminateConditionValue=2;
				}

				intToDateYearForAgeing = dteToDateForAgeing.getYear();
				intToDateMonthForAgeing = dteToDateForAgeing.getMonthValue();
				intToDateDayForAgeing = dteToDateForAgeing.getDayOfMonth();

				toDateForAgeing = intToDateYearForAgeing + "-" + intToDateMonthForAgeing + "-" + intToDateDayForAgeing;

				intFromDateYearForAgeing = dteFromDateForAgeing.getYear();
				intFromDateMonthForAgeing = dteFromDateForAgeing.getMonthValue();
				intFromDateDayForAgeing = dteFromDateForAgeing.getDayOfMonth();

				fromDateForAgeing = intFromDateYearForAgeing + "-" + intFromDateMonthForAgeing + "-"
						+ intFromDateDayForAgeing;
				;

			}

			int MAXSIZE = 0;
			for (Map.Entry<String, List<Object>> entry : mapOSandAgeing.entrySet()) {

				String debtorCode = entry.getKey();
				List<Object> listOfOSandAgeing = entry.getValue();

				clsCreditorOutStandingReportBean objDebtorOutStanding = hmOutstanding.get(debtorCode);
				double oldOutStandingAmt = objDebtorOutStanding.getDblBalAmt();

				if (oldOutStandingAmt >= 0) {
					listOfOSandAgeing.add(oldOutStandingAmt);
				} else {
					listOfOSandAgeing.add(0.00);
				}

				if (listOfOSandAgeing.size() > MAXSIZE) {
					MAXSIZE = listOfOSandAgeing.size();
				}

			}

			int from = 0, to = 30;
			List<Object> listOfHeader = new ArrayList<>();
			listOfHeader.add("Creditor Name");
			listOfHeader.add("Outstanding");
			listOfHeader.add(from + "-" + to);

			from = to + 1;
			to = to + 30;

			for (int i = 0; i < MAXSIZE - 3; i++) {
				listOfHeader.add(from + "-" + to);

				from = to + 1;
				to = to + 30;
			}
			mapOSandAgeing.put("header", listOfHeader);

			/*
			 * Comparator<clsCreditorOutStandingReportBean> debtorNameComparator = new
			 * Comparator<clsCreditorOutStandingReportBean>() {
			 * 
			 * @Override public int compare(clsCreditorOutStandingReportBean o1,
			 * clsCreditorOutStandingReportBean o2) { return
			 * (o1.getStrDebtorName().compareToIgnoreCase(o2.getStrDebtorName())); } };
			 * Collections.sort(listOfAgeingData, debtorNameComparator);
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			return mapOSandAgeing;
		}
	}

}