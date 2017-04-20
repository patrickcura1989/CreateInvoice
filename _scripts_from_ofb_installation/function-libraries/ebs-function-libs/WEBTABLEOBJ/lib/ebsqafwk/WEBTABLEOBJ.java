//WARNING!
//This file was created by Oracle OpenScript.
//Don't change it.

package lib.ebsqafwk;

import java.util.ArrayList;
import java.util.Arrays;
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
import oracle.oats.scripting.modules.webdom.api.elements.DOMDocument;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.applet.api.*;
import oracle.oats.utilities.FileUtility;
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;

public class WEBTABLEOBJ<WebTable> extends FuncLibraryWrapper
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

	public void initializeWebTable() throws AbstractScriptException {
		checkInit();
		callFunction("initializeWebTable");
	}

	public String findDynamicEmptyRowStart(String tableName, String colName, String compType, String compSequence) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("findDynamicEmptyRowStart", tableName, colName, compType, compSequence);
	}

	public String findEmptyRowStart(String tableName, String colName) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("findEmptyRowStart", tableName, colName);
	}

	public void clearWebTableObject() throws AbstractScriptException {
		checkInit();
		callFunction("clearWebTableObject");
	}

	/**
	 * Gets Table Object
	 * @param tableName -- It looks like "Select" or "Select;1" or "Select;1,document=0,form=0"
	 * @return
	 * @throws Exception
	 */
	public WebTable getWebTableObject(String tableName) throws AbstractScriptException {
		checkInit();
		return (WebTable) callFunction("getWebTableObject", tableName);
	}

	public void waitForColumn(String tableName) throws AbstractScriptException {
		checkInit();
		callFunction("waitForColumn", tableName);
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

	public HashMap<String, String> getColumnDetails(HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (HashMap<String, String>) callFunction("getColumnDetails", actions);
	}

	public HashMap<String, String> getColumnDetails(String columnName) throws AbstractScriptException {
		checkInit();
		return (HashMap<String, String>) callFunction("getColumnDetails", columnName);
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

	public String getListValue(String tableName, HashMap<String, String> searchColumns, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getListValue", tableName, searchColumns, actions);
	}

	public String getListValue(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getListValue", tableName, rowNum, actions);
	}

	public void setEditValue(String tableName, HashMap<String, String> searchColumns, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("setEditValue", tableName, searchColumns, actions);
	}

	public void setEditValue(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("setEditValue", tableName, rowNum, actions);
	}

	public void setTextArea(String tableName, HashMap<String, String> searchColumns, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("setTextArea", tableName, searchColumns, actions);
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

	public void clickImageByName(String tableName, HashMap<String, String> searchColumns, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("clickImageByName", tableName, searchColumns, actions);
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

	public void selectList(String tableName, HashMap<String, String> searchColumns, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("selectList", tableName, searchColumns, actions);
	}

	public void selectList(String tableName, int rowNum, HashMap<String, String> actions) throws AbstractScriptException {
		checkInit();
		callFunction("selectList", tableName, rowNum, actions);
	}

	public boolean verifyRowData(String tableName, HashMap<String, String> searchColumns, String[] actions) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyRowData", tableName, searchColumns, (Object) actions);
	}

	public int findRowNumber(HashMap<String, String> searchColumns, String tableName) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("findRowNumber", searchColumns, tableName);
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

}

