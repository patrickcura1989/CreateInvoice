//WARNING!
//This file was created by Oracle OpenScript.
//Don't change it.

package lib.ebsqafwk;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import oracle.oats.scripting.modules.basic.api.Arg;
import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.formsFT.common.api.elements.TextField;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.applet.api.*;
import lib.*;
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;

public class SCMLIB extends FuncLibraryWrapper
{

	public void initialize() throws AbstractScriptException {
		checkInit();
		callFunction("initialize");
	}

	/**
	 * Add code to be executed each iteration for this virtual user.
	 */
	public void run() throws AbstractScriptException {
		checkInit();
		callFunction("run");
	}

	public void finish() throws AbstractScriptException {
		checkInit();
		callFunction("finish");
	}

	public void ProcessingConstraints(String Entity, String Processing_Constraints, String Validation, String isEnable) throws AbstractScriptException {
		checkInit();
		callFunction("ProcessingConstraints", Entity, Processing_Constraints, Validation, isEnable);
	}

	public void updateProcessingConstraints(String Entity, String Processing_Constraints, String Validation, String isEnable) throws AbstractScriptException {
		checkInit();
		callFunction("updateProcessingConstraints", Entity, Processing_Constraints, Validation, isEnable);
	}

	public void function1(String Entity, String Processing_Constraints, String Validation, String isEnable) throws AbstractScriptException {
		checkInit();
		callFunction("function1", Entity, Processing_Constraints, Validation, isEnable);
	}

	public void unexpectedPopUp() throws AbstractScriptException {
		checkInit();
		callFunction("unexpectedPopUp");
	}

	/**
	 * By Leo, Given on: 8Apr2013
	 * @param frmOrg
	 * @param endOrg
	 * @param flowType
	 * @param endDate
	 * @throws Exception
	 */
	public void disableInterCompanyRecord(String frmOrg, String endOrg, String flowType, String endDate) throws AbstractScriptException {
		checkInit();
		callFunction("disableInterCompanyRecord", frmOrg, endOrg, flowType, endDate);
	}

	public void setTextInDualField(String atti, String label, String value) throws AbstractScriptException {
		checkInit();
		callFunction("setTextInDualField", atti, label, value);
	}

	public void selectDayInMonth(String day) throws AbstractScriptException {
		checkInit();
		callFunction("selectDayInMonth", day);
	}

	public String getInstancesCount(String instanceString) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getInstancesCount", instanceString);
	}

	public List<String> getInstances(String instanceString) throws AbstractScriptException {
		checkInit();
		return (List<String>) callFunction("getInstances", instanceString);
	}

	public String getExpenditureGroup(String index) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getExpenditureGroup", index);
	}

	public String extractRequestID() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("extractRequestID");
	}

	/**
	 * Clear current record, select clear record option from menu
	 * @throws Exception
	 * @author  fsui
	 */
	public void clearRecord() throws AbstractScriptException {
		checkInit();
		callFunction("clearRecord");
	}

	/**
	 * Delete current record, select delete option from menu
	 * @throws Exception
	 * @author  fsui
	 */
	public void deleteRecord() throws AbstractScriptException {
		checkInit();
		callFunction("deleteRecord");
	}

	/**
	 * Select all records in current form
	 * @throws Exception
	 * @author  fsui
	 */
	public void selectAllRecords() throws AbstractScriptException {
		checkInit();
		callFunction("selectAllRecords");
	}

	/**
	 * Trim the string's space and return it
	 * @param value
	 * @return
	 * @throws Exception
	 * @author  fsui
	 */
	public String strTrim(String value) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("strTrim", value);
	}

	/**
	 * logout application and close all browsers
	 * @throws Exception
	 * @author  fsui
	 */
	public void logOut() throws AbstractScriptException {
		checkInit();
		callFunction("logOut");
	}

	/**
	 * Shows all the fields passed into it in the form of an array. 
	 * @param fieldNames  An array containing all the fields to be displayed.
	 * @throws Exception
	 */
	public void showAllFields(String[] fieldNames) throws AbstractScriptException {
		checkInit();
		callFunction("showAllFields", (Object) fieldNames);
	}

	/**
	 * Shows a field
	 * @param fieldName
	 * @throws Exception
	 * @return  true if successful and false for fails
	 */
	public boolean showField(String fieldName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("showField", fieldName);
	}

	/**
	 * Open Accouting Periods up to current month for an org Change organization must be done before using this function
	 * @throws Exception
	 */
	public void openInvAccoutingPeroids() throws AbstractScriptException {
		checkInit();
		callFunction("openInvAccoutingPeroids");
	}

	/**
	 * Collapse trees within navigator
	 * @throws Exception
	 */
	public void collapseNavigator() throws AbstractScriptException {
		checkInit();
		callFunction("collapseNavigator");
	}

	/**
	 * Set component value given the label
	 * @param labelName  label of the component
	 * @param compType  component type
	 * @param valueToSet  value to set
	 * @throws Exception
	 */
	public void setValueBasedOnLabel(String labelName, String compType, String valueToSet) throws AbstractScriptException {
		checkInit();
		callFunction("setValueBasedOnLabel", labelName, compType, valueToSet);
	}

	/**
	 * Get component belongs to a label
	 * @param label  the component's label
	 * @param compType  component type
	 * @return  the component list with the label
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DOMElement> getCompsBasedOnLabel(String label, String compType) throws AbstractScriptException {
		checkInit();
		return (List<DOMElement>) callFunction("getCompsBasedOnLabel", label, compType);
	}

	/**
	 * Format given date string into the formated date according to the new format
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public String formatDate(String date, String format) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("formatDate", date, format);
	}

	/**
	 * Replace oldStr with newStr in str
	 * @param str  
	 * @param oldStr  
	 * @param newStr
	 * @return  replaced string
	 * @throws Exception
	 */
	public String replaceString(String str, String oldStr, String newStr) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("replaceString", str, oldStr, newStr);
	}

	/**
	 * Get substring by giving the start index and end index
	 * @str  parent string
	 * @param start
	 * @param end
	 * @return
	 */
	public String getSubStringByIndex(String str, int start, int end) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getSubStringByIndex", str, start, end);
	}

	/**
	 * Get substring by giving the left and right string
	 * @param str  parent string
	 * @param left  left string besides the substring
	 * @param right  right string besides the substring
	 * @return
	 */
	public String getSubStringByText(String str, String left, String right) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getSubStringByText", str, left, right);
	}

	/**
	 * Select 'more...' option when select org, find the org and select
	 * @param org
	 * @author  cchao
	 */
	public void selectOrgMore(@Arg("org") String org) throws AbstractScriptException {
		checkInit();
		callFunction("selectOrgMore", org);
	}

	/**
	 * Verify choice box message againgst expectedValue
	 * @param expectedValue  expected message against which to be verifyed
	 * @return  true if matched, false not matched
	 * @author  lehan
	 * @throws Exception
	 */
	public boolean formVerifyChoiceBox(String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyChoiceBox", expectedValue);
	}

	/**
	 * keep performing action on actionObj util verifyObj match expectedValue
	 * @param actionObj  form object type
	 * @param actionXPath  action object xpath
	 * @param verifyObject  verify object type
	 * @param verifyXpath  verify object xpath
	 * @param expectedValue  expected value to wait
	 * @throws Exception Supported object types: button, menu, statusBar, edit
	 */
	public void formWaitForRecord(String actionObj, String actionXPath, String verifyObj, String verifyXpath, String expectedValue) throws AbstractScriptException {
		checkInit();
		callFunction("formWaitForRecord", actionObj, actionXPath, verifyObj, verifyXpath, expectedValue);
	}

	/**
	 * Copy all text from focused window, and check if exepctedValue existes in the text copied
	 * @param expectedValue  value to check
	 * @return  true if contains the expectedValue
	 * @throws Exception
	 */
	public boolean formVerifyWindowText(String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyWindowText", expectedValue);
	}

	/**
	 * Get table cell value by given first cell value and row, column number 
	 * @param tableName  first cell value of the table
	 * @param row  The row of the table cell, starting from 1.
	 * @param col  The column of the table cell, starting from 1.
	 * @return  the value of the table cell.
	 * @throws Exception
	 */
	public String webGetCellData(String tableName, int row, int col) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetCellData", tableName, row, col);
	}

	/**
	 * Verify element's attribute value against expected attribute value
	 * @param xpath  in the format: web:a[@text='Logout']
	 * @param attr  attribute name of the element
	 * @param expectedValue  expectd attribute value of the element
	 * @return  true is attribute value is matched
	 * @throws Exception
	 */
	public boolean webVerifyAttributes(String xpath, String attr, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyAttributes", xpath, attr, expectedValue);
	}

	/**
	 * Verify element's attribute value against expected attribute value
	 * @param tag  element tag type
	 * @param name  name of the element
	 * @param attr  attribute name
	 * @param expectedValue  expected attribute value
	 * @return  true if attribute value is matched
	 * @throws Exception
	 */
	public boolean formVerifyAttributes(String tag, String name, String attr, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyAttributes", tag, name, attr, expectedValue);
	}

	/**
	 * Verify form menu status, enabled and existed
	 * @param menu  menu path
	 * @param attr  existed or enabled
	 * @param expectedValue  true or false
	 * @return  true if status matched
	 * @throws Exception
	 */
	public boolean formVerifyMenu(String menu, String attr, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyMenu", menu, attr, expectedValue);
	}

	/**
	 * sign E-signatures
	 * @param signer
	 * @param password
	 * @throws AbstractScriptException
	 */
	public void signESignatures(String signer, String password) throws AbstractScriptException {
		checkInit();
		callFunction("signESignatures", signer, password);
	}

	public String getShipConfirmReqIds(String content, String before, String after) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getShipConfirmReqIds", content, before, after);
	}

	public String getShipConfirmReqIds(String content) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getShipConfirmReqIds", content);
	}

	public String getDeliveryNumber(String content) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getDeliveryNumber", content);
	}

	public String getTripStopReqId(String content) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getTripStopReqId", content);
	}

	/**
	 * Adding a number to the integer part available in "From" field Ex:  1) From : str0000, index: 5  then return : str0005 2) From : str026, index: 5  then return : str031 3) From : str526, index: 5  then return : str531
	 * @param From
	 * @param Index
	 * @return
	 */
	public String getLPNNumber(String From, String Index) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getLPNNumber", From, Index);
	}

	public String getLPNNameFromLog() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getLPNNameFromLog");
	}

	public String verifyLabelContextXMLData(String textFieldAttributeName, String variableNames, String variableValues) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("verifyLabelContextXMLData", textFieldAttributeName, variableNames, variableValues);
	}

	public boolean isNumber(String string) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("isNumber", string);
	}

}

