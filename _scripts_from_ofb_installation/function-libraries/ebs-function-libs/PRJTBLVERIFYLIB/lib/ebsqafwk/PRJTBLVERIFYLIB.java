//WARNING!
//This file was created by Oracle OpenScript.
//Don't change it.

package lib.ebsqafwk;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.applet.api.*;
import lib.*;
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;

public class PRJTBLVERIFYLIB extends FuncLibraryWrapper
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

	public boolean spreadVerifyTable(String excelName, String sheetName, String spreadTableAttribute, List<String[]> verifycolumnsNames) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("spreadVerifyTable", excelName, sheetName, spreadTableAttribute, verifycolumnsNames);
	}

	/**
	 * Verification of Data in Forms
	 * @param componentType -- Type of Component : compType: {"textfield", "password","textarea","link","checkbox","radio","select","button","text"}
	 * @param attributes -- Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName -- Excel Path Name or File Name  (i.e: D:\abc.xls or abx.xls)
	 * @param sheetName -- Sheet Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean webVerifyWithExcelInput(String excelName, String sheetName, String keyword, String componentType, String columnName, String attributes) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyWithExcelInput", excelName, sheetName, keyword, componentType, columnName, attributes);
	}

	/**
	 * Verification of data in forms
	 * @param componentType - Type of Component : {"textfield", "textarea","checkbox","radio","select","button"}
	 * @param attributes - Attributes :name:def
	 * @param excelName - Excel Path Name or File Name  (i.e: D:\abc.xls or abx.xls)
	 * @param sheetName - Sheet Name
	 * @param columnName - Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean formVerifywithExcelData(String componentType, String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifywithExcelData", componentType, attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Text Field Data in Forms
	 * @param attributes -- Text Field Attributes i.e (name:def)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean formsVerifyEditText(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formsVerifyEditText", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Text Area Data in Forms
	 * @param attributes -- Text Area Attributes i.e (name:def)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean formsVerifyTextAreaValue(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formsVerifyTextAreaValue", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Checkbox status in Forms
	 * @param attributes -- Checkbox Field Attributes i.e (name:def)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean formsVerifyCheckBoxStatus(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formsVerifyCheckBoxStatus", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Radio button status in Forms
	 * @param attributes -- Radio button Attributes i.e (name:def)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean formsVerifyRadioStatus(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formsVerifyRadioStatus", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of List Value in Forms
	 * @param attributes -- List Attributes i.e (name:def)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean formsVerifyListBoxSelectedValue(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formsVerifyListBoxSelectedValue", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Button status in Forms
	 * @param attributes -- Button Attributes i.e (name:def)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean formsVerifyButtonName(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formsVerifyButtonName", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Text
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean verifyText(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyText", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Text Field Data
	 * @param attributes -- Text Field Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean verifyEditText(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyEditText", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Password Field Data
	 * @param attributes -- Password Field Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean verifyPasswordText(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyPasswordText", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Button
	 * @param attributes -- Button Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean verifyButtonName(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyButtonName", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Selected Field Value
	 * @param attributes -- Select Field Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean verifyListBoxSelectedValue(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyListBoxSelectedValue", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Checkbox Status Value
	 * @param attributes -- Checkbox Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean verifyCheckBoxStatus(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyCheckBoxStatus", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Radio button Status
	 * @param attributes -- Radio button Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean verifyRadioStatus(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyRadioStatus", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Text Area value
	 * @param attributes -- Text Area Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean verifyTextAreaValue(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyTextAreaValue", attributes, excelName, sheetName, columnName);
	}

	/**
	 * Verification of Link
	 * @param attributes -- Link Attributes i.e (id:abc,name:def,index:ghi)
	 * @param excelName -- Complete Excel Path
	 * @param sheetName -- Excel Name
	 * @param columnName -- Column Name
	 * @return
	 * @throws Exception
	 */
	public boolean verifyLinkText(String attributes, String excelName, String sheetName, String columnName) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyLinkText", attributes, excelName, sheetName, columnName);
	}

	public boolean webVerifyTable(String tableName, String excelName, String sheetName, List<String[]> verifycolumnsNames) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyTable", tableName, excelName, sheetName, verifycolumnsNames);
	}

	public String getCellData(DOMElement element) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCellData", element);
	}

	public boolean formVerifyTable(String excelName, String sheetName, String maxVisibleLines, List<String[]> verifycolumnsNames) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyTable", excelName, sheetName, maxVisibleLines, verifycolumnsNames);
	}

	/**
	 * @param before  
	 * @param after  
	 * @param textToVerify  
	 * @param index  
	 */
	public String getText(String before, String after) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getText", before, after);
	}

	/**
	 * Example:  String path = "O:\\EBS QA\\GUIDS\\Ganesh\\LibraryTest\\Data.xls"; List <String[]> act    =    new ArrayList<String[]>(); act.add(new String[]{"VERIFY","TEXT","Date;Date",""}); act.add(new String[]{"VERIFY","TEXT","Currency Code;Currency Code",""}); act.add(new String[]{"VERIFY","TEXT","Total Cash;Total Cash",""}); verifyRequestOutput(path, "Report Data", act);
	 */
	public boolean verifyRequestOutput(String excelName, String sheetName, List<String[]> verifyColumnsNames) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyRequestOutput", excelName, sheetName, verifyColumnsNames);
	}

	public HashMap<String, HashMap<String, String>> getReportData() throws AbstractScriptException {
		checkInit();
		return (HashMap<String, HashMap<String, String>>) callFunction("getReportData");
	}

}

