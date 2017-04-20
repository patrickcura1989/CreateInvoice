//WARNING!
//This file was created by Oracle OpenScript.
//Don't change it.

package lib.ebsqafwk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import oracle.oats.scripting.modules.basic.api.FunctionLibrary;
import oracle.oats.scripting.modules.basic.api.IteratingVUserScript;
import oracle.oats.scripting.modules.basic.api.ScriptService;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;
import oracle.oats.scripting.modules.functionalTest.api.PropertyTestList;
import oracle.oats.scripting.modules.functionalTest.api.TestOperator;
import oracle.oats.scripting.modules.webdom.api.WebDomService;
import oracle.oats.scripting.modules.webdom.api.elements.DOMCheckbox;
import oracle.oats.scripting.modules.webdom.api.elements.DOMDocument;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.utilities.FileUtility;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.basic.api.*;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.applet.api.*;
import lib.*;
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;

public class PROCLIB extends FuncLibraryWrapper
{

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

	/**
	 * @param sWhatYouWantToDo  Add,Search And Select
	 */
	public void finish() throws AbstractScriptException {
		checkInit();
		callFunction("finish");
	}

	public void verifyDialogMessage(String msgToVerify, String action) throws AbstractScriptException {
		checkInit();
		callFunction("verifyDialogMessage", msgToVerify, action);
	}

	public void setNextRandomNumber(String window, String field) throws AbstractScriptException {
		checkInit();
		callFunction("setNextRandomNumber", window, field);
	}

	public void awardTableAction(String supplier, String awardOption, String objectType, String value) throws AbstractScriptException {
		checkInit();
		callFunction("awardTableAction", supplier, awardOption, objectType, value);
	}

	public String getAwardOption(String supplier, String awardOption) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getAwardOption", supplier, awardOption);
	}

	public void verifyPaymentProcessRequest(String reqName, String status) throws AbstractScriptException {
		checkInit();
		callFunction("verifyPaymentProcessRequest", reqName, status);
	}

	public void verifyAwardOption(String supplier, String awardOption, String expectedVal) throws AbstractScriptException {
		checkInit();
		callFunction("verifyAwardOption", supplier, awardOption, expectedVal);
	}

	public void launchCustomURL(String url) throws AbstractScriptException {
		checkInit();
		callFunction("launchCustomURL", url);
	}

	/**
	 * carWebSelectLOV("Funding or Requesting Agency ID Lookup","Agency ID","9543","Agency ID","9543");
	 * @param lovName
	 * @param searchByOption
	 * @param searchValue
	 * @param colName
	 * @param rowValue
	 * @throws Exception
	 */
	public void carWebSelectLOV(String lovName, String searchByOption, String searchValue, String colName, String rowValue) throws AbstractScriptException {
		checkInit();
		callFunction("carWebSelectLOV", lovName, searchByOption, searchValue, colName, rowValue);
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
	public void carWebSelectLOV(String lovName, String searchByOption, String searchValue, String[] colNames, String[] rowValues) throws AbstractScriptException {
		checkInit();
		callFunction("carWebSelectLOV", lovName, searchByOption, searchValue, (Object) colNames, (Object) rowValues);
	}

	public void verifyCheckBoxImageBasedOnLabel(String labelName, String status) throws AbstractScriptException {
		checkInit();
		callFunction("verifyCheckBoxImageBasedOnLabel", labelName, status);
	}

	public DOMElement getParentRow(DOMElement cell) throws AbstractScriptException {
		checkInit();
		return (DOMElement) callFunction("getParentRow", cell);
	}

	public String getOfferReceiveTime(String dateFormatType, String dateValue) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getOfferReceiveTime", dateFormatType, dateValue);
	}

	public String generateofferReceiveTime(String dateFormatType, String dateValue, int incrementValue) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("generateofferReceiveTime", dateFormatType, dateValue, incrementValue);
	}

	public void formsSetChargeAccount(String ChargeAccount, String distributionLineNumber) throws AbstractScriptException {
		checkInit();
		callFunction("formsSetChargeAccount", ChargeAccount, distributionLineNumber);
	}

	public void clearShopppingCart() throws AbstractScriptException {
		checkInit();
		callFunction("clearShopppingCart");
	}

	public void Award_Bid_To_Supplier(String supplierName, String award, String awardOption, String ValueToEnter, String InternalNote, String NotetoSupplier) throws AbstractScriptException {
		checkInit();
		callFunction("Award_Bid_To_Supplier", supplierName, award, awardOption, ValueToEnter, InternalNote, NotetoSupplier);
	}

	public void Verify_AwardBid_Details_Supplier(String supplierName, String awardOption, String valueToVerifY) throws AbstractScriptException {
		checkInit();
		callFunction("Verify_AwardBid_Details_Supplier", supplierName, awardOption, valueToVerifY);
	}

	public void clickBidinAwardBid(String supplierName) throws AbstractScriptException {
		checkInit();
		callFunction("clickBidinAwardBid", supplierName);
	}

	public void selectRadiobuttonBasedonLabel(String labelNameWithIndex) throws AbstractScriptException {
		checkInit();
		callFunction("selectRadiobuttonBasedonLabel", labelNameWithIndex);
	}

	public String encryptURL(String ou) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("encryptURL", ou);
	}

	public void addAttachments(String[] fieldLabels, String[] fieldValues, String action) throws AbstractScriptException {
		checkInit();
		callFunction("addAttachments", (Object) fieldLabels, (Object) fieldValues, action);
	}

	public void verifyDocumentNumberDetails(String documentNumberDetail, String value) throws AbstractScriptException {
		checkInit();
		callFunction("verifyDocumentNumberDetails", documentNumberDetail, value);
	}

	public void verifyItemPricingDetails(String pricingDetail, String pricingValue) throws AbstractScriptException {
		checkInit();
		callFunction("verifyItemPricingDetails", pricingDetail, pricingValue);
	}

	public void addItemPricingDetails(String pricingDetail, String pricingValue) throws AbstractScriptException {
		checkInit();
		callFunction("addItemPricingDetails", pricingDetail, pricingValue);
	}

	@SuppressWarnings("unchecked")
	public String getCellCompXPath(String itemName, int expIndex, String compType, int compSeq) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCellCompXPath", itemName, expIndex, compType, compSeq);
	}

	public String getCompXPath(String component, HashMap<String, String> tagAtttirbutes) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCompXPath", component, tagAtttirbutes);
	}

	public HashMap<String, String> getTagAttributes(DOMElement element) throws AbstractScriptException {
		checkInit();
		return (HashMap<String, String>) callFunction("getTagAttributes", element);
	}

	public HashMap<String, String> getTagAttributes(DOMElement element, String[] expAttributes) throws AbstractScriptException {
		checkInit();
		return (HashMap<String, String>) callFunction("getTagAttributes", element, (Object) expAttributes);
	}

	public void selectFromLOV(String lovName, String searchByOption, String searchValue, String colName, String rowValue) throws AbstractScriptException {
		checkInit();
		callFunction("selectFromLOV", lovName, searchByOption, searchValue, colName, rowValue);
	}

	public void selectFromLOV(String lovName, String searchByOption, String searchValue, String[] colNames, String[] rowValues) throws AbstractScriptException {
		checkInit();
		callFunction("selectFromLOV", lovName, searchByOption, searchValue, (Object) colNames, (Object) rowValues);
	}

	@SuppressWarnings("unchecked")
	public String getCellCompXPath(String itemName, int expIndex, String compType, String value) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCellCompXPath", itemName, expIndex, compType, value);
	}

	public String getCurrentWindowXPath() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCurrentWindowXPath");
	}

	public String getCurrentWindowTitle() throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCurrentWindowTitle");
	}

	public boolean isEmpty(String input) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("isEmpty", input);
	}

	/**
	 * @param labelName  
	 * @param value  
	 */
	public void selectFirstOptionInSelectBoxBasedOnLabel(String labelName) throws AbstractScriptException {
		checkInit();
		callFunction("selectFirstOptionInSelectBoxBasedOnLabel", labelName);
	}

	public void setValueBasedOnLabel(String labelName, String compType, String valueToSet) throws AbstractScriptException {
		checkInit();
		callFunction("setValueBasedOnLabel", labelName, compType, valueToSet);
	}

	/**
	 * Wrapper functions for setValueBasedOnLabel 
	 */
	public void setSelectValueBasedOnLabel(String labelName, String value) throws AbstractScriptException {
		checkInit();
		callFunction("setSelectValueBasedOnLabel", labelName, value);
	}

	public void setTextAreaValueBasedOnLabel(String labelName, String value) throws AbstractScriptException {
		checkInit();
		callFunction("setTextAreaValueBasedOnLabel", labelName, value);
	}

	public void setEditValueBasedOnLabel(String labelName, String value) throws AbstractScriptException {
		checkInit();
		callFunction("setEditValueBasedOnLabel", labelName, value);
	}

	public void setCheckboxValueBasedOnLabel(String labelName, String value) throws AbstractScriptException {
		checkInit();
		callFunction("setCheckboxValueBasedOnLabel", labelName, value);
	}

	public String getValueBasedOnLabel(String labelName, String compType) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getValueBasedOnLabel", labelName, compType);
	}

	/**
	 * Wrapper functions for getValueBasedOnLabel 
	 */
	public String getSelectValueBasedOnLabel(String labelName) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getSelectValueBasedOnLabel", labelName);
	}

	public String getTextAreaValueBasedOnLabel(String labelName) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getTextAreaValueBasedOnLabel", labelName);
	}

	public String getEditValueBasedOnLabel(String labelName) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getEditValueBasedOnLabel", labelName);
	}

	public String getCheckboxValueBasedOnLabel(String labelName) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("getCheckboxValueBasedOnLabel", labelName);
	}

	/**
	 * Wrapper functions for VerifyValueBasedOnLabel 
	 */
	public boolean verifySelectValueBasedOnLabel(String labelName, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifySelectValueBasedOnLabel", labelName, expectedValue);
	}

	public boolean verifyTextAreaValueBasedOnLabel(String labelName, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyTextAreaValueBasedOnLabel", labelName, expectedValue);
	}

	public boolean verifyEditValueBasedOnLabel(String labelName, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyEditValueBasedOnLabel", labelName, expectedValue);
	}

	public boolean verifyCheckboxValueBasedOnLabel(String labelName, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyCheckboxValueBasedOnLabel", labelName, expectedValue);
	}

	public boolean verifyValueBasedOnLabel(String labelName, String compType, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("verifyValueBasedOnLabel", labelName, compType, expectedValue);
	}

	@SuppressWarnings("unchecked")
	public List<DOMElement> getCompsBasedOnLabel(String label, String compType) throws AbstractScriptException {
		checkInit();
		return (List<DOMElement>) callFunction("getCompsBasedOnLabel", label, compType);
	}

	public List<DOMElement> getCompsBasedOnLabel2(String label, String compType) throws AbstractScriptException {
		checkInit();
		return (List<DOMElement>) callFunction("getCompsBasedOnLabel2", label, compType);
	}

	public void handleEditDocumentNumber(String DoDAAC, String InstrumentType, String AllowedRange, String SerialNum) throws AbstractScriptException {
		checkInit();
		callFunction("handleEditDocumentNumber", DoDAAC, InstrumentType, AllowedRange, SerialNum);
	}

	public void editRequisitionNumber(String prefix, String agencyIdentifier, String allowedRange, String serialNumber) throws AbstractScriptException {
		checkInit();
		callFunction("editRequisitionNumber", prefix, agencyIdentifier, allowedRange, serialNumber);
	}

	public void verifyRequisitionNumber(String Prefix, String agencyIdentifier, String allowedRange, String delimiter) throws AbstractScriptException {
		checkInit();
		callFunction("verifyRequisitionNumber", Prefix, agencyIdentifier, allowedRange, delimiter);
	}

	public void selectSearchRadioOption(String radioOption) throws AbstractScriptException {
		checkInit();
		callFunction("selectSearchRadioOption", radioOption);
	}

	public void handleWebTermsWindow(String Action) throws AbstractScriptException {
		checkInit();
		callFunction("handleWebTermsWindow", Action);
	}

	public void viewContractFileNavigation(String mainResp, String subResp, String lastresp) throws AbstractScriptException {
		checkInit();
		callFunction("viewContractFileNavigation", mainResp, subResp, lastresp);
	}

	public void addToCartBasedOnSource(String itemName, String source) throws AbstractScriptException {
		checkInit();
		callFunction("addToCartBasedOnSource", itemName, source);
	}

	public void addIDVStructuretoCart(String idvItem, String idvNum, String price) throws AbstractScriptException {
		checkInit();
		callFunction("addIDVStructuretoCart", idvItem, idvNum, price);
	}

	public List<DOMElement> getLinksInPage(String itemName) throws AbstractScriptException {
		checkInit();
		return (List<DOMElement>) callFunction("getLinksInPage", itemName);
	}

	public void addItemFromFavToDocument(String itemName, String idvNum, String quantity) throws AbstractScriptException {
		checkInit();
		callFunction("addItemFromFavToDocument", itemName, idvNum, quantity);
	}

	public void selectApproverInManageApprovals(String approver) throws AbstractScriptException {
		checkInit();
		callFunction("selectApproverInManageApprovals", approver);
	}

	public void selectFirstValueFromLOV(String lovName) throws AbstractScriptException {
		checkInit();
		callFunction("selectFirstValueFromLOV", lovName);
	}

}

