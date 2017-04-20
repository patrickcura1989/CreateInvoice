//WARNING!
//This file was created by Oracle OpenScript.
//Don't change it.

package lib.ebsqafwk;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import oracle.oats.scripting.modules.basic.api.FunctionLibrary;
import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;
import oracle.oats.scripting.modules.formsFT.common.api.elements.AbstractWindow;
import oracle.oats.scripting.modules.functionalTest.api.PropertyTestList;
import oracle.oats.scripting.modules.functionalTest.api.TestOperator;
import oracle.oats.scripting.modules.webdom.api.WebDomService;
import oracle.oats.scripting.modules.webdom.api.elements.DOMDocument;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.webdom.api.elements.DOMRadioButton;
import oracle.oats.scripting.modules.webdom.api.elements.DOMSelect;
import oracle.oats.scripting.modules.webdom.api.elements.DOMText;
import oracle.oats.utilities.FileUtility;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.applet.api.*;
import lib.*;
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;

public class GENLIB extends FuncLibraryWrapper
{

	public void alterEffectiveDate(String sNewdate) throws AbstractScriptException {
		checkInit();
		callFunction("alterEffectiveDate", sNewdate);
	}

	public String getLPNname() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getLPNname");
	}

	public void sop(String rs) throws AbstractScriptException {
		checkInit();
		callFunction("sop", rs);
	}

	public void initialize() throws AbstractScriptException {
		checkInit();
		callFunction("initialize");
	}

	public void initializeWebTable() throws AbstractScriptException {
		checkInit();
		callFunction("initializeWebTable");
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

	/**
	 * Reusable Functions code
	 */
	public void actOnAssignment(String type) throws AbstractScriptException {
		checkInit();
		callFunction("actOnAssignment", type);
	}

	public void clickHide(String hideAltAttribute) throws AbstractScriptException {
		checkInit();
		callFunction("clickHide", hideAltAttribute);
	}

	/**
	 * Code to verify checkbox
	 */
	public boolean formVerifyChoiceBox(String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyChoiceBox", expectedValue);
	}

	public void expandAndSelectNode(String nameAttribute, String navPath) throws AbstractScriptException {
		checkInit();
		callFunction("expandAndSelectNode", nameAttribute, navPath);
	}

	public void expandNodes(String nameAttribute, String value) throws AbstractScriptException {
		checkInit();
		callFunction("expandNodes", nameAttribute, value);
	}

	public void oracle_prompt_jtt_url() throws AbstractScriptException {
		checkInit();
		callFunction("oracle_prompt_jtt_url");
	}

	public String getSysDate(String zone, String dateFormat, int noOfDays) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getSysDate", zone, dateFormat, noOfDays);
	}

	public String getSysTime(String zone, String dateFormat, int... param) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getSysTime", zone, dateFormat, param);
	}

	public String getRandom(String format, int number) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getRandom", format, number);
	}

	public void clickAlertButton(String buttonName) throws AbstractScriptException {
		checkInit();
		callFunction("clickAlertButton", buttonName);
	}

	public String extractNumber(String text) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("extractNumber", text);
	}

	public String getNumbersFromStr(String msg, String before, String after, String ind) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getNumbersFromStr", msg, before, after, ind);
	}

	public String getNumberAt(String commaStr, String index) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getNumberAt", commaStr, index);
	}

	public void setProperty(String key, String value) throws AbstractScriptException {
		checkInit();
		callFunction("setProperty", key, value);
	}

	public String getProperty(String key) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getProperty", key);
	}

	public boolean webVerifyLinkBasedOnLabel(String labelWithIndex, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyLinkBasedOnLabel", labelWithIndex, expectedValue);
	}

	public boolean webVerifyTextBasedOnLabel(String labelWithIndex, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyTextBasedOnLabel", labelWithIndex, expectedValue);
	}

	public String webGetLinkBasedOnLabel(String labelWithIndex) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetLinkBasedOnLabel", labelWithIndex);
	}

	public String webGetTextBasedOnLabel(String labelWithIndex) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetTextBasedOnLabel", labelWithIndex);
	}

	public void webSetTextBasedOnLabel(String labelWithIndex, String valueToSet) throws AbstractScriptException {
		checkInit();
		callFunction("webSetTextBasedOnLabel", labelWithIndex, valueToSet);
	}

	public String getListboxSelectedValue(String xpath) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getListboxSelectedValue", xpath);
	}

	public void selectTableRadioButton(String tblName, String srcColNames[], String srcColVals[], String targetCol) throws AbstractScriptException {
		checkInit();
		callFunction("selectTableRadioButton", tblName, srcColNames, srcColVals, targetCol);
	}

	public void wait(String globalWaitTime, String comLevelWaitTime) throws AbstractScriptException {
		checkInit();
		callFunction("wait", globalWaitTime, comLevelWaitTime);
	}

	public void closeWebPage(String title) throws AbstractScriptException {
		checkInit();
		callFunction("closeWebPage", title);
	}

	public void formsSendKey(String xpath, String value) throws AbstractScriptException {
		checkInit();
		callFunction("formsSendKey", xpath, value);
	}

	public void webSendKey(String xpath, String value) throws AbstractScriptException {
		checkInit();
		callFunction("webSendKey", xpath, value);
	}

	public void formSelectLOV5(String editLogicalName, String valueToSelect) throws AbstractScriptException {
		checkInit();
		callFunction("formSelectLOV5", editLogicalName, valueToSelect);
	}

	public void formSelectLOV(String editLogicalName, String valueToSelect) throws AbstractScriptException {
		checkInit();
		callFunction("formSelectLOV", editLogicalName, valueToSelect);
	}

	public void formSelectLOV1(String valueToSelect) throws AbstractScriptException {
		checkInit();
		callFunction("formSelectLOV1", valueToSelect);
	}

	public void formSelectDate(String xpath, String dateValue) throws AbstractScriptException {
		checkInit();
		callFunction("formSelectDate", xpath, dateValue);
	}

	public boolean formVerifyEdit(String editLogicalNameXPath, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyEdit", editLogicalNameXPath, expectedValue);
	}

	/**
	 * Note: Not using this.formSelectLOV(null, fieldValue); because it is returning value as  [Encumbered Amount                                        *ENCUMBERED_AMOUNT_DSP] hence handling logic in this function
	 * @param windowName
	 * @param fieldValue
	 * @throws Exception
	 */
	public void formShowField(String windowName, String fieldValue) throws AbstractScriptException {
		checkInit();
		callFunction("formShowField", windowName, fieldValue);
	}

	public void formHideField(String fieldName, String editFieldName) throws AbstractScriptException {
		checkInit();
		callFunction("formHideField", fieldName, editFieldName);
	}

	public void formHideField(String fieldName) throws AbstractScriptException {
		checkInit();
		callFunction("formHideField", fieldName);
	}

	public void webVerifyText(String text) throws AbstractScriptException {
		checkInit();
		callFunction("webVerifyText", text);
	}

	public void webClickImage(String imageName) throws AbstractScriptException {
		checkInit();
		callFunction("webClickImage", imageName);
	}

	public void webVerifyList(String path, String expectedValue) throws AbstractScriptException {
		checkInit();
		callFunction("webVerifyList", path, expectedValue);
	}

	public void webVerifyListBox(String path, String expectedValue) throws AbstractScriptException {
		checkInit();
		callFunction("webVerifyListBox", path, expectedValue);
	}

	public void webVerifyListBoxValues(String path, String expectedValue) throws AbstractScriptException {
		checkInit();
		callFunction("webVerifyListBoxValues", path, expectedValue);
	}

	public void webVerifyListValues(String path, String expectedValue) throws AbstractScriptException {
		checkInit();
		callFunction("webVerifyListValues", path, expectedValue);
	}

	public void clickLink(String linkName, int index) throws AbstractScriptException {
		checkInit();
		callFunction("clickLink", linkName, index);
	}

	public void clickLink(String linkName, String windowName, int compSequence, boolean isFormPathExists) throws AbstractScriptException {
		checkInit();
		callFunction("clickLink", linkName, windowName, compSequence, isFormPathExists);
	}

	public void webClickLink(String linkName) throws AbstractScriptException {
		checkInit();
		callFunction("webClickLink", linkName);
	}

	public void webClickButton(String buttonName) throws AbstractScriptException {
		checkInit();
		callFunction("webClickButton", buttonName);
	}

	public void webSelectLOV(String lovName, String searchByOption, String searchValue, String colName, String rowValue) throws AbstractScriptException {
		checkInit();
		callFunction("webSelectLOV", lovName, searchByOption, searchValue, colName, rowValue);
	}

	/**
	 * Select From LOV
	 * @param lovName
	 * @param searchByOption
	 * @param searchValue
	 * @param colNames
	 * @param rowValues
	 * @throws Exception
	 */
	public void webSelectFromLOV(String lovName, String searchByOption, String searchValue, String[] colNames, String[] rowValues) throws AbstractScriptException {
		checkInit();
		callFunction("webSelectFromLOV", lovName, searchByOption, searchValue, (Object) colNames, (Object) rowValues);
	}

	public void webVerifyLink(String linkPath, String expectedValue) throws AbstractScriptException {
		checkInit();
		callFunction("webVerifyLink", linkPath, expectedValue);
	}

	public void webVerifyEdit(String path, String expectedValue) throws AbstractScriptException {
		checkInit();
		callFunction("webVerifyEdit", path, expectedValue);
	}

	public void webVerifyTextArea(String path, String expectedValue) throws AbstractScriptException {
		checkInit();
		callFunction("webVerifyTextArea", path, expectedValue);
	}

	public void webVerifyCheckBox(String path, String expectedState) throws AbstractScriptException {
		checkInit();
		callFunction("webVerifyCheckBox", path, expectedState);
	}

	public void webVerifyRadioButton(String path, String expectedState) throws AbstractScriptException {
		checkInit();
		callFunction("webVerifyRadioButton", path, expectedState);
	}

	public void webSelectDate(String xpath, String date) throws AbstractScriptException {
		checkInit();
		callFunction("webSelectDate", xpath, date);
	}

	public String formTableSearch(HashMap<String, String> searchCol, int visiblelines) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("formTableSearch", searchCol, visiblelines);
	}

	public boolean verifySpreadCell(String spreadTableAttribute, String rowNum, String colNum, String expectedValToVerify) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifySpreadCell", spreadTableAttribute, rowNum, colNum, expectedValToVerify);
	}

	public String formSpreadTableSearch(HashMap<String, String> searchCol, String attrValue) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("formSpreadTableSearch", searchCol, attrValue);
	}

	public void formsConfirmDialog() throws AbstractScriptException {
		checkInit();
		callFunction("formsConfirmDialog");
	}

	public void formsChoiceWindow() throws AbstractScriptException {
		checkInit();
		callFunction("formsChoiceWindow");
	}

	public boolean formVerifyTextArea(String path, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyTextArea", path, expectedValue);
	}

	public boolean formVerifyRadioButton(String path, String radioButtonStatus) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyRadioButton", path, radioButtonStatus);
	}

	public boolean formVerifyCheckBox(String path, String checkBoxstatus) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyCheckBox", path, checkBoxstatus);
	}

	public boolean formVerifyListBoxValues(String path, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyListBoxValues", path, expectedValue);
	}

	public boolean formVerifyStatus(String message) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("formVerifyStatus", message);
	}

	public void switchResponsibility(String valueToSelect) throws AbstractScriptException {
		checkInit();
		callFunction("switchResponsibility", valueToSelect);
	}

	public String removeSpecialCharacters(String input) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("removeSpecialCharacters", input);
	}

	public void waitForText(String text) throws AbstractScriptException {
		checkInit();
		callFunction("waitForText", text);
	}

	public void waitForText(String text, String compType) throws AbstractScriptException {
		checkInit();
		callFunction("waitForText", text, compType);
	}

	public void verifyAndClosePopup(String msg, String action) throws AbstractScriptException {
		checkInit();
		callFunction("verifyAndClosePopup", msg, action);
	}

	public String getLatestWindowXPath() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getLatestWindowXPath");
	}

	public String getCurrentWindowXPath() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCurrentWindowXPath");
	}

	public boolean compareStrings(String expectedString, String actualString) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("compareStrings", expectedString, actualString);
	}

	public String[] getKeysFromHashMap(HashMap<String, String> searchColumns) throws AbstractScriptException {
		checkInit();
		return (String[]) callFunction("getKeysFromHashMap", searchColumns);
	}

	public String[] getValuesFromHashMap(HashMap<String, String> searchColumns) throws AbstractScriptException {
		checkInit();
		return (String[]) callFunction("getValuesFromHashMap", searchColumns);
	}

	public String getRandomString() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getRandomString");
	}

	public int getRandomNumber(String maxRange) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("getRandomNumber", maxRange);
	}

	/**
	 * Gives a Random Number within a Specified Ranges
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	public int getRandomNumber(int startRange, int endRange) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("getRandomNumber", startRange, endRange);
	}

	public String getSysDateTime(String dateFormat, int noOfDays) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getSysDateTime", dateFormat, noOfDays);
	}

	public String getSysDate(String dateFormat, int noOfDays) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getSysDate", dateFormat, noOfDays);
	}

	public String addSysDate(String dateFormatType, String dateValue) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("addSysDate", dateFormatType, dateValue);
	}

	public String subSysDate(String dateFormatType, String dateValue) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("subSysDate", dateFormatType, dateValue);
	}

	public boolean webVerifyText(String expectedText, String beforeWithIndex, String afterWithIndex) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyText", expectedText, beforeWithIndex, afterWithIndex);
	}

	public boolean webVerifyText(String expectedText, String beforeText, String afterText, String beforeIndex, String afterIndex) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyText", expectedText, beforeText, afterText, beforeIndex, afterIndex);
	}

	public boolean webVerifyBetweenText(String expectedText, String beforeText, String afterText, String beforeIndex, String afterIndex) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyBetweenText", expectedText, beforeText, afterText, beforeIndex, afterIndex);
	}

	public boolean webVerifyBeforeText(String expectedText, String afterText, String sWordsRequired, String sIndex) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyBeforeText", expectedText, afterText, sWordsRequired, sIndex);
	}

	public boolean webVerifyAfterText(String expectedText, String beforeText, String sWordsRequired, String sIndex) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyAfterText", expectedText, beforeText, sWordsRequired, sIndex);
	}

	public String getWordCount(String text) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getWordCount", text);
	}

	public boolean webVerifyTextWithBefore(String before, String textToVerify) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyTextWithBefore", before, textToVerify);
	}

	public boolean webVerifyTextWithBefore(String before, String textToVerify, Integer index) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyTextWithBefore", before, textToVerify, index);
	}

	public boolean webVerifyTextWithAfter(String after, String textToVerify) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyTextWithAfter", after, textToVerify);
	}

	public boolean webVerifyTextWithAfter(String after, String textToVerify, Integer index) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyTextWithAfter", after, textToVerify, index);
	}

	public boolean webVerifyTextWithBeforeAfter(String before, String after, String textToVerify) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyTextWithBeforeAfter", before, after, textToVerify);
	}

	public boolean webVerifyTextWithBeforeAfter(String before, String after, String textToVerify, Integer index) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyTextWithBeforeAfter", before, after, textToVerify, index);
	}

	/**
	 * @param before
	 * @param after
	 * @param textToVerify
	 * @param index
	 */
	public String webGetTextInTable(String text, String sWordsRequired, String sIndex, String action) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetTextInTable", text, sWordsRequired, sIndex, action);
	}

	public String webGetText(String beforeWithIndex, String afterWithIndex) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetText", beforeWithIndex, afterWithIndex);
	}

	public String webGetText(String beforeText, String afterText, String beforeIndex, String afterIndex) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetText", beforeText, afterText, beforeIndex, afterIndex);
	}

	public String webGetBetweenText(String before, String after, String beforeIndex, String afterIndex) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetBetweenText", before, after, beforeIndex, afterIndex);
	}

	public String webGetBeforeText(String afterText, String sWordsRequired, String sIndex) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetBeforeText", afterText, sWordsRequired, sIndex);
	}

	public String webGetAfterText(String beforeText, String sWordsRequired, String sIndex) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetAfterText", beforeText, sWordsRequired, sIndex);
	}

	public void webSetText(String name, String valueToSet) throws AbstractScriptException {
		checkInit();
		callFunction("webSetText", name, valueToSet);
	}

	public void extractZipFile(String zipFilePath, String directoryToExtractTo) throws AbstractScriptException {
		checkInit();
		callFunction("extractZipFile", zipFilePath, directoryToExtractTo);
	}

	/**
	 * ****************CDC code
	 */
	public void setFlexText(String label, String value) throws AbstractScriptException {
		checkInit();
		callFunction("setFlexText", label, value);
	}

	/**
	 * Click the ok button within the flexwindow
	 * @throws Exception
	 */
	public void clickFlexOK() throws AbstractScriptException {
		checkInit();
		callFunction("clickFlexOK");
	}

	/**
	 * verify request status
	 * @param expectedValue
	 * @throws Exception
	 */
	public void verifyParenetChildReqs(String Parent, String ReqIndex, String ParentStatus, String commaReqNames, String reqStatuses) throws AbstractScriptException {
		checkInit();
		callFunction("verifyParenetChildReqs", Parent, ReqIndex, ParentStatus, commaReqNames, reqStatuses);
	}

	public void verifyParenetChildReqs(String Parent, String ParentStatus, String commaReqNames, String reqStatuses) throws AbstractScriptException {
		checkInit();
		callFunction("verifyParenetChildReqs", Parent, ParentStatus, commaReqNames, reqStatuses);
	}

	public String findChildRequest(String reqName, String par) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("findChildRequest", reqName, par);
	}

	public void openRequest(String ReqID, String ReqName) throws AbstractScriptException {
		checkInit();
		callFunction("openRequest", ReqID, ReqName);
	}

	public boolean verifyRequestStatus(String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyRequestStatus", expectedValue);
	}

	@SuppressWarnings("unchecked")
	public void setValueBasedonLabelAfterUIComponent(String labelName, String compType, String valueToSet) throws AbstractScriptException {
		checkInit();
		callFunction("setValueBasedonLabelAfterUIComponent", labelName, compType, valueToSet);
	}

	@SuppressWarnings("unchecked")
	public void setValueBasedonLabelBeforeUIComponent(String labelName, String compType, String valueToSet) throws AbstractScriptException {
		checkInit();
		callFunction("setValueBasedonLabelBeforeUIComponent", labelName, compType, valueToSet);
	}

	@SuppressWarnings("unchecked")
	public String getValueBasedonLabelBeforeUIComponent(String labelName, String compType) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getValueBasedonLabelBeforeUIComponent", labelName, compType);
	}

	@SuppressWarnings("unchecked")
	public String getValueBasedonLabelAfterUIComponent(String labelName, String compType) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getValueBasedonLabelAfterUIComponent", labelName, compType);
	}

	@SuppressWarnings("unchecked")
	public boolean verifyValueBasedonLabelBeforeUIComponent(String labelName, String compType, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyValueBasedonLabelBeforeUIComponent", labelName, compType, expectedValue);
	}

	@SuppressWarnings("unchecked")
	public boolean verifyValueBasedonLabelAfterUIComponent(String labelName, String compType, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyValueBasedonLabelAfterUIComponent", labelName, compType, expectedValue);
	}

	public void selectRadiobuttonBasedonLabel(String labelNameWithIndex) throws AbstractScriptException {
		checkInit();
		callFunction("selectRadiobuttonBasedonLabel", labelNameWithIndex);
	}

	public void setRadioValueBasedonLabelAfterUIComponent(String labelNameWithIndex) throws AbstractScriptException {
		checkInit();
		callFunction("setRadioValueBasedonLabelAfterUIComponent", labelNameWithIndex);
	}

	public void setRadioValueBasedonLabelAfterUIComponent1(String labelNameWithIndex) throws AbstractScriptException {
		checkInit();
		callFunction("setRadioValueBasedonLabelAfterUIComponent1", labelNameWithIndex);
	}

	public void setRadioValueBasedonLabelBeforeUIComponent(String labelNameWithIndex) throws AbstractScriptException {
		checkInit();
		callFunction("setRadioValueBasedonLabelBeforeUIComponent", labelNameWithIndex);
	}

	public String getRand(int pattern) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getRand", pattern);
	}

	public String getRand(String pattern) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getRand", pattern);
	}

	/**
	 * Setting Data in the Dynamic Column Table
	 * @param columnName -- Column Name
	 * @param rowNum -- Row Number
	 * @param valueToSet -- Value to Set
	 * @throws Exception
	 */
	public void formSetValueInDynamicColumn(String columnName, String rowNumStr, String valueToSet) throws AbstractScriptException {
		checkInit();
		callFunction("formSetValueInDynamicColumn", columnName, rowNumStr, valueToSet);
	}

	public boolean verifyValueInDynamicColumn(String columnName, String rowNumStr, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyValueInDynamicColumn", columnName, rowNumStr, expectedValue);
	}

	/**
	 * Getting Value from Dynamic Column Table
	 * @param columnName -- Column Name
	 * @param rowNum -- Row Number
	 * @throws Exception
	 */
	public String getValueInDynamicColumn(String columnName, String rowNumStr) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getValueInDynamicColumn", columnName, rowNumStr);
	}

	/**
	 * Allows to set required line to top
	 * @param maxLine
	 * @param setLineVal
	 * @return
	 */
	public String setLine(String editField, String maxLine, String setLineVal) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("setLine", editField, maxLine, setLineVal);
	}

	@SuppressWarnings({ "unchecked" })
	public List<DOMElement> getCompsBasedOnLabel(String label, String compType) throws AbstractScriptException {
		checkInit();
		return (List<DOMElement>) callFunction("getCompsBasedOnLabel", label, compType);
	}

	@SuppressWarnings({ "unchecked" })
	public List<DOMElement> getCompsBasedOnLabel2(String label, String compType) throws AbstractScriptException {
		checkInit();
		return (List<DOMElement>) callFunction("getCompsBasedOnLabel2", label, compType);
	}

	public void selectFromLov(String windowId, String img, String srcVal) throws AbstractScriptException {
		checkInit();
		callFunction("selectFromLov", windowId, img, srcVal);
	}

	public void clickTableImage(String WebTableName, int columnNumberToVerify, String Record, String ColumnNameToSelect, int ColumnNumberToSelect) throws AbstractScriptException {
		checkInit();
		callFunction("clickTableImage", WebTableName, columnNumberToVerify, Record, ColumnNameToSelect, ColumnNumberToSelect);
	}

	public void searchObjectByLocation(String objtype, int loc, String name) throws AbstractScriptException {
		checkInit();
		callFunction("searchObjectByLocation", objtype, loc, name);
	}

	public String[] getObjectType(String objType) throws AbstractScriptException {
		checkInit();
		return (String[]) callFunction("getObjectType", objType);
	}

	public String[] getObjectType(String objType, boolean isTable) throws AbstractScriptException {
		checkInit();
		return (String[]) callFunction("getObjectType", objType, isTable);
	}

	public boolean isFound() throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("isFound");
	}

	public void click() throws AbstractScriptException {
		checkInit();
		callFunction("click");
	}

	public void setDocPath(String str) throws AbstractScriptException {
		checkInit();
		callFunction("setDocPath", str);
	}

	public void restore() throws AbstractScriptException {
		checkInit();
		callFunction("restore");
	}

	public void setRadioAttribute(String radioAttr) throws AbstractScriptException {
		checkInit();
		callFunction("setRadioAttribute", radioAttr);
	}

	public String getRadioXpath() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getRadioXpath");
	}

	public String getImgXpath() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getImgXpath");
	}

	public void searchTableObject(String tablename, String SearchCol, String SearchText, String TargetCol, String TargetObjType, int objloc) throws AbstractScriptException {
		checkInit();
		callFunction("searchTableObject", tablename, SearchCol, SearchText, TargetCol, TargetObjType, objloc);
	}

	@SuppressWarnings("unchecked")
	public void searchTableObject(int tblloc, String tablename, String SearchCol, String SearchText, String TargetCol, String TargetObjType, int objloc) throws AbstractScriptException {
		checkInit();
		callFunction("searchTableObject", tblloc, tablename, SearchCol, SearchText, TargetCol, TargetObjType, objloc);
	}

	public String handleDialog(String action) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("handleDialog", action);
	}

	public void webLogout() throws AbstractScriptException {
		checkInit();
		callFunction("webLogout");
	}

	public void closeForms() throws AbstractScriptException {
		checkInit();
		callFunction("closeForms");
	}

	public void closeForm() throws AbstractScriptException {
		checkInit();
		callFunction("closeForm");
	}

	public void closeForm(String windowName) throws AbstractScriptException {
		checkInit();
		callFunction("closeForm", windowName);
	}

	public String getActiveFormWindowName() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getActiveFormWindowName");
	}

	public void navigateToHome() throws AbstractScriptException {
		checkInit();
		callFunction("navigateToHome");
	}

	public void formMenuSelect(String menuPath) throws AbstractScriptException {
		checkInit();
		callFunction("formMenuSelect", menuPath);
	}

	public void webClickDynamicLink(String linkName) throws AbstractScriptException {
		checkInit();
		callFunction("webClickDynamicLink", linkName);
	}

	public void clickFlexButton(String button) throws AbstractScriptException {
		checkInit();
		callFunction("clickFlexButton", button);
	}

	public void saveDialog(String location) throws AbstractScriptException {
		checkInit();
		callFunction("saveDialog", location);
	}

	public void setFormsText(String editLogicalName, String value) throws AbstractScriptException {
		checkInit();
		callFunction("setFormsText", editLogicalName, value);
	}

	public void formsSelectColor(String nameAttrb, String color) throws AbstractScriptException {
		checkInit();
		callFunction("formsSelectColor", nameAttrb, color);
	}

	public boolean setPurchasingPeriod(String[] period, String[] status) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("setPurchasingPeriod", (Object) period, (Object) status);
	}

	public boolean openInventoryPeriod(String sPeriod) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("openInventoryPeriod", sPeriod);
	}

	/**
	 * GetMessage
	 * @param Source
	 * @param Request
	 * @return
	 * @throws Exception
	 */
	public String GetMessage(int Source, int Request) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("GetMessage", Source, Request);
	}

	public void setPayablePeriods(String Ledger, String OU, String[] Period, String[] Status) throws AbstractScriptException {
		checkInit();
		callFunction("setPayablePeriods", Ledger, OU, (Object) Period, (Object) Status);
	}

	public void RunQuery() throws AbstractScriptException {
		checkInit();
		callFunction("RunQuery");
	}

	public void EnterQuery() throws AbstractScriptException {
		checkInit();
		callFunction("EnterQuery");
	}

	/**
	 * Cancels the query
	 * @param Win_Name
	 * @throws Exception
	 */
	public void CancelQuery() throws AbstractScriptException {
		checkInit();
		callFunction("CancelQuery");
	}

	/**
	 * Select List Item if "" is passsed for valueToselect , then it will select first item in the list
	 * @param xPath
	 * @param valueToselect
	 * @throws AbstractScriptException
	 */
	public void webSelectListBoxWithXPath(String xPath, String valueToselect) throws AbstractScriptException {
		checkInit();
		callFunction("webSelectListBoxWithXPath", xPath, valueToselect);
	}

	public void webSelectListBox(String selectLabel, String valueToselect) throws AbstractScriptException {
		checkInit();
		callFunction("webSelectListBox", selectLabel, valueToselect);
	}

	public void webSelectListBox1(String selectLabel, String valueToselect) throws AbstractScriptException {
		checkInit();
		callFunction("webSelectListBox1", selectLabel, valueToselect);
	}

	public String getSysTime(String dateFormat, int... param) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getSysTime", dateFormat, param);
	}

	public void selectFile(String buttonName, String fileName) throws AbstractScriptException {
		checkInit();
		callFunction("selectFile", buttonName, fileName);
	}

	public void uploadFile(String fileName) throws AbstractScriptException {
		checkInit();
		callFunction("uploadFile", fileName);
	}

	public List<DOMElement> getSelectBoxBasedOnAttributes(String attrbs) throws AbstractScriptException {
		checkInit();
		return (List<DOMElement>) callFunction("getSelectBoxBasedOnAttributes", attrbs);
	}

	public String getCurrentDocPath() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCurrentDocPath");
	}

	public List<DOMElement> getListBoxBasedOnLabel(String label) throws AbstractScriptException {
		checkInit();
		return (List<DOMElement>) callFunction("getListBoxBasedOnLabel", label);
	}

	public void selectListMultiValues(String labelName, String options) throws AbstractScriptException {
		checkInit();
		callFunction("selectListMultiValues", labelName, options);
	}

	public void setEditValueBasedOnLabel(String labelName, String value) throws AbstractScriptException {
		checkInit();
		callFunction("setEditValueBasedOnLabel", labelName, value);
	}

	public void setValueBasedOnLabel(String labelName, String compType, String valueToSet) throws AbstractScriptException {
		checkInit();
		callFunction("setValueBasedOnLabel", labelName, compType, valueToSet);
	}

	public void maximizeWindow(String title) throws AbstractScriptException {
		checkInit();
		callFunction("maximizeWindow", title);
	}

	public String searchEmptyRow(String tableName, String colName) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("searchEmptyRow", tableName, colName);
	}

}

