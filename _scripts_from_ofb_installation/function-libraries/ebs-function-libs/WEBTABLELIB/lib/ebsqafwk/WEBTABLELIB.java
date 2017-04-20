//WARNING!
//This file was created by Oracle OpenScript.
//Don't change it.

package lib.ebsqafwk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.applet.api.*;
import lib.*;
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;

public class WEBTABLELIB extends FuncLibraryWrapper
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

	public void sop(String rs) throws AbstractScriptException {
		checkInit();
		callFunction("sop", rs);
	}

	public String findEmptyRowStart(String tableName, String colName) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("findEmptyRowStart", tableName, colName);
	}

	/**
	 * This is for finding table row, which is empty Example1: 
	 * @param tableName
	 * @param colName
	 * @param compType
	 * @return
	 * @throws Exception
	 */
	public String findDynamicEmptyRowStart(String tableName, String colName, String compType) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("findDynamicEmptyRowStart", tableName, colName, compType);
	}

	public String findDynamicEmptyRowStart(String tableName, String colName, String compType, String compSequence) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("findDynamicEmptyRowStart", tableName, colName, compType, compSequence);
	}

	public String tableactions(String tableName, List<String[]> actions, HashMap<String, String> searchColumns) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("tableactions", tableName, actions, searchColumns);
	}

	public String tableactions(String tableName, HashMap<String, String> searchColumns, List<String[]> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("tableactions", tableName, searchColumns, actions);
	}

	public String tableactions(String tableName, String rowNumber, List<String[]> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("tableactions", tableName, rowNumber, actions);
	}

	public void clearWebTableObject() throws AbstractScriptException {
		checkInit();
		callFunction("clearWebTableObject");
	}

	public DOMElement getParentRow(DOMElement cell) throws AbstractScriptException {
		checkInit();
		return (DOMElement) callFunction("getParentRow", cell);
	}

	public void crmWebSelectLOV(String label, String searchByOption, String searchValue, String colName, String rowValue) throws AbstractScriptException {
		checkInit();
		callFunction("crmWebSelectLOV", label, searchByOption, searchValue, colName, rowValue);
	}

	/**
	 * Get Columns 
	 * @param tableName
	 * @throws Exception
	 */
	public void getColumns(String tableName) throws AbstractScriptException {
		checkInit();
		callFunction("getColumns", tableName);
	}

	public int getRowCount(String tableName) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("getRowCount", tableName);
	}

	public boolean verifyTableWithExactRowData(String tableName, String[] colNames, HashMap<String, List<String>> expectedTableData, String[] sExpComponents) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyTableWithExactRowData", tableName, (Object) colNames, expectedTableData, (Object) sExpComponents);
	}

	public List<String> getCellData(String tableName, HashMap<String, String> searchColumns, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (List<String>) callFunction("getCellData", tableName, searchColumns, actions);
	}

	public List<String> getCellData(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (List<String>) callFunction("getCellData", tableName, rowNum, actions);
	}

	public void checkOn(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("checkOn", tableName, rowNum, actions);
	}

	public void checkOFF(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("checkOFF", tableName, rowNum, actions);
	}

	public void radioOn(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("radioOn", tableName, rowNum, actions);
	}

	public String getEditValue(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getEditValue", tableName, rowNum, actions);
	}

	public String getLinkValue(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getLinkValue", tableName, rowNum, actions);
	}

	public String getImageValue(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getImageValue", tableName, rowNum, actions);
	}

	public String getCheckBoxOption(String tableName, HashMap<String, String> searchColumns, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCheckBoxOption", tableName, searchColumns, actions);
	}

	public String getCheckBoxOption(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCheckBoxOption", tableName, rowNum, actions);
	}

	public String getTextAreaValue(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getTextAreaValue", tableName, rowNum, actions);
	}

	public void setEditValue(String tableName, HashMap<String, String> searchColumns, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("setEditValue", tableName, searchColumns, actions);
	}

	public void setEditValue(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("setEditValue", tableName, rowNum, actions);
	}

	public void setTextArea(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("setTextArea", tableName, rowNum, actions);
	}

	public void clickImage(String tableName, HashMap<String, String> searchColumns, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("clickImage", tableName, searchColumns, actions);
	}

	public void clickImage(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("clickImage", tableName, rowNum, actions);
	}

	public void clickImageByName(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("clickImageByName", tableName, rowNum, actions);
	}

	public void clickLink(String tableName, HashMap<String, String> searchColumns, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("clickLink", tableName, searchColumns, actions);
	}

	public void clickLink(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("clickLink", tableName, rowNum, actions);
	}

	public void clickButton(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("clickButton", tableName, rowNum, actions);
	}

	public void selectList(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("selectList", tableName, rowNum, actions);
	}

	public String getListValue(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getListValue", tableName, rowNum, actions);
	}

	public boolean verifyRowData(String tableName, HashMap<String, String> searchColumns, String[] actions) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyRowData", tableName, searchColumns, (Object) actions);
	}

	public int findRowNumber(String tableName, HashMap<String, String> searchColumns) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("findRowNumber", tableName, searchColumns);
	}

	public String[] getKeysFromHashMapValuesAreList(HashMap<String, List<String>> searchColumns) throws AbstractScriptException {
		checkInit();
		return (String[]) callFunction("getKeysFromHashMapValuesAreList", searchColumns);
	}

	public String[] getKeysFromHashMap(HashMap<String, String> searchColumns) throws AbstractScriptException {
		checkInit();
		return (String[]) callFunction("getKeysFromHashMap", searchColumns);
	}

	public String[] getValuesFromHashMap(HashMap<String, String> searchColumns) throws AbstractScriptException {
		checkInit();
		return (String[]) callFunction("getValuesFromHashMap", searchColumns);
	}

	@SuppressWarnings("unchecked")
	public List<String>[] getValuesFromHashMapValuesAreList(HashMap<String, List<String>> searchColumns) throws AbstractScriptException {
		checkInit();
		return (List<String>[]) callFunction("getValuesFromHashMapValuesAreList", searchColumns);
	}

	public boolean compareStrings(String expectedString, String actualString) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("compareStrings", expectedString, actualString);
	}

}

