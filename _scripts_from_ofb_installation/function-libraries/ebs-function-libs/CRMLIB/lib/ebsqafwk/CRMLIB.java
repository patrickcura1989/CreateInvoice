//WARNING!
//This file was created by Oracle OpenScript.
//Don't change it.

package lib.ebsqafwk;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import oracle.oats.scripting.modules.webdom.api.elements.DOMSelect;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.applet.api.*;
import lib.*;
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;

public class CRMLIB extends FuncLibraryWrapper
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

	public boolean verifyDateBasedOnMonday(String logicalName, String time, String dateFormat, String minutesToBeAdded) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyDateBasedOnMonday", logicalName, time, dateFormat, minutesToBeAdded);
	}

	public boolean refreshWebItem(String buttonName, String tableName, String sourceColName, String sourceColValue, String targetColName, String targetColValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("refreshWebItem", buttonName, tableName, sourceColName, sourceColValue, targetColName, targetColValue);
	}

	/**
	 * Clics on Select button under Specific Address
	 * @param address
	 * @throws Exception
	 */
	public void selectAddress(String address) throws AbstractScriptException {
		checkInit();
		callFunction("selectAddress", address);
	}

	/**
	 * Clicks on Checkout butoon
	 * @param cartType - Type of Cart i.e. "Saved Carts","Carts Shared With You",  "Carts Shared By You"
	 * @throws Exception
	 */
	public void cartCheckout(String cartType) throws AbstractScriptException {
		checkInit();
		callFunction("cartCheckout", cartType);
	}

	/**
	 * Select the list box and clicks on Go button
	 * @param cartType - Type of Cart i.e. "Saved Carts","Carts Shared With You",  "Carts Shared By You"
	 * @param itemToSelect - Item to Select in the listbox
	 * @throws Exception
	 */
	public void selectCartAction(String cartType, String itemToSelect) throws AbstractScriptException {
		checkInit();
		callFunction("selectCartAction", cartType, itemToSelect);
	}

	public void actionOnCart(String cartName, String compType, String itemToSelect) throws AbstractScriptException {
		checkInit();
		callFunction("actionOnCart", cartName, compType, itemToSelect);
	}

	@SuppressWarnings("unchecked")
	public void clickLOVBasedOnLabel(String labelName) throws AbstractScriptException {
		checkInit();
		callFunction("clickLOVBasedOnLabel", labelName);
	}

	@SuppressWarnings("unchecked")
	public int searchEditableRow() throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("searchEditableRow");
	}

	@SuppressWarnings("unchecked")
	public void addToCartItemDetails(String Itemlabel, String Quantity) throws AbstractScriptException {
		checkInit();
		callFunction("addToCartItemDetails", Itemlabel, Quantity);
	}

	public void expandCollapseBasedOnLabel(String labelNameWithIndex) throws AbstractScriptException {
		checkInit();
		callFunction("expandCollapseBasedOnLabel", labelNameWithIndex);
	}

	public void expandBasedOnLabel(String labelNameWithIndex) throws AbstractScriptException {
		checkInit();
		callFunction("expandBasedOnLabel", labelNameWithIndex);
	}

	public void setSearchParams(String searchCriteria) throws AbstractScriptException {
		checkInit();
		callFunction("setSearchParams", searchCriteria);
	}

	public void setSearchParams_old(String searchCriteria) throws AbstractScriptException {
		checkInit();
		callFunction("setSearchParams_old", searchCriteria);
	}

	public void crm_WebSelectLOV(String lovName, String searchByOption, String searchValue, String colName, String rowValue) throws AbstractScriptException {
		checkInit();
		callFunction("crm_WebSelectLOV", lovName, searchByOption, searchValue, colName, rowValue);
	}

	public void crm_WebSelectLOV(String lovName, String searchByOption, String searchValue, String[] colNames, String[] rowValues) throws AbstractScriptException {
		checkInit();
		callFunction("crm_WebSelectLOV", lovName, searchByOption, searchValue, (Object) colNames, (Object) rowValues);
	}

	@SuppressWarnings("unchecked")
	public void getRequestStatus(String requestID) throws AbstractScriptException {
		checkInit();
		callFunction("getRequestStatus", requestID);
	}

	public void crmWebSelectLOV(String label, String searchByOption, String searchValue, String colName, String rowValue) throws AbstractScriptException {
		checkInit();
		callFunction("crmWebSelectLOV", label, searchByOption, searchValue, colName, rowValue);
	}

	public DOMElement getParentRow(DOMElement cell) throws AbstractScriptException {
		checkInit();
		return (DOMElement) callFunction("getParentRow", cell);
	}

	public String getLatestWindowXPath() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getLatestWindowXPath");
	}

	public String getCurrentWindowXPath() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCurrentWindowXPath");
	}

	public void webClickDynamicLink(String linkName) throws AbstractScriptException {
		checkInit();
		callFunction("webClickDynamicLink", linkName);
	}

	public void clickSiteLink(String sitename) throws AbstractScriptException {
		checkInit();
		callFunction("clickSiteLink", sitename);
	}

	public void promptAndLaunchJTTUrl() throws AbstractScriptException {
		checkInit();
		callFunction("promptAndLaunchJTTUrl");
	}

	public void clickJTTLink(String link) throws AbstractScriptException {
		checkInit();
		callFunction("clickJTTLink", link);
	}

	public void selectMediaContent(String searchFor, String searchForValue) throws AbstractScriptException {
		checkInit();
		callFunction("selectMediaContent", searchFor, searchForValue);
	}

	public String getTableActionsRow(String tableName, List<String[]> searchList) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getTableActionsRow", tableName, searchList);
	}

	public void actionsOnJTTTable(String tblString, List<String[]> actionList) throws AbstractScriptException {
		checkInit();
		callFunction("actionsOnJTTTable", tblString, actionList);
	}

	public void jttLogin(String username, String password) throws AbstractScriptException {
		checkInit();
		callFunction("jttLogin", username, password);
	}

	public void clickAddToCart(String itemName) throws AbstractScriptException {
		checkInit();
		callFunction("clickAddToCart", itemName);
	}

	public void clickExpressCheckOut(String itemName) throws AbstractScriptException {
		checkInit();
		callFunction("clickExpressCheckOut", itemName);
	}

	public void setCartQuantity(String itemName, String quantity) throws AbstractScriptException {
		checkInit();
		callFunction("setCartQuantity", itemName, quantity);
	}

	public void clickConfigure(String itemName) throws AbstractScriptException {
		checkInit();
		callFunction("clickConfigure", itemName);
	}

	public void selectDisplayTemplate(String label) throws AbstractScriptException {
		checkInit();
		callFunction("selectDisplayTemplate", label);
	}

	/**
	 * Check or Uncheck a checkbox which is identified as Image
	 * @param xPath
	 * @param checkValue
	 * @throws Exception
	 */
	public void checkImageCheckBox(String xPath, String checkValueString) throws AbstractScriptException {
		checkInit();
		callFunction("checkImageCheckBox", xPath, checkValueString);
	}

	public void selectImageRadiobutton(String xPath, String selectValueString) throws AbstractScriptException {
		checkInit();
		callFunction("selectImageRadiobutton", xPath, selectValueString);
	}

	/**
	 * Click an Image in Inner Navigation Table (here Navigation is available in the table row)
	 * @param tableName Table Name
	 * @param navigationColumn Column in which navigation is available
	 * @param searchColumn Search Column
	 * @param searchValue Search Value
	 * @param targetColumn Target Column Ex: clickImageInInnerNavigationTable("Focus", "Component","Component","TRANSACTION_AMOUNT","");
	 * @throws Exception  
	 */
	public void clickImageInInnerNavigationTable(String tableName, String navigationColumn, String searchColumn, String searchValue, String targetColumn) throws AbstractScriptException {
		checkInit();
		callFunction("clickImageInInnerNavigationTable", tableName, navigationColumn, searchColumn, searchValue, targetColumn);
	}

	public int getRowNumberFromInnerNavigationTable(String tableName, String navigationColumn, String searchColumn, String searchValue) throws AbstractScriptException {
		checkInit();
		return (Integer) callFunction("getRowNumberFromInnerNavigationTable", tableName, navigationColumn, searchColumn, searchValue);
	}

	public void selectCustomer(String searchFor, String searchForValue, String accountNumber) throws AbstractScriptException {
		checkInit();
		callFunction("selectCustomer", searchFor, searchForValue, accountNumber);
	}

	public void selectResources(String resources) throws AbstractScriptException {
		checkInit();
		callFunction("selectResources", resources);
	}

	public void selectFormsSingleColValues(String xpaths, String srcValues) throws AbstractScriptException {
		checkInit();
		callFunction("selectFormsSingleColValues", xpaths, srcValues);
	}

}

