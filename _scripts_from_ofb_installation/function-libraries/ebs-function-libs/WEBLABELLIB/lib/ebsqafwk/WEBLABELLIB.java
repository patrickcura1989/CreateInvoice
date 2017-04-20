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
import oracle.oats.scripting.modules.browser.api.*;
import oracle.oats.scripting.modules.functionalTest.api.*;
import oracle.oats.scripting.modules.utilities.api.*;
import oracle.oats.scripting.modules.utilities.api.sql.*;
import oracle.oats.scripting.modules.utilities.api.xml.*;
import oracle.oats.scripting.modules.utilities.api.file.*;
import oracle.oats.scripting.modules.webdom.api.*;
import oracle.oats.scripting.modules.webdom.api.elements.DOMElement;
import oracle.oats.scripting.modules.webdom.api.elements.DOMSelect;
import oracle.oats.scripting.modules.webdom.api.elements.DOMText;
import oracle.oats.scripting.modules.formsFT.api.*;
import oracle.oats.scripting.modules.applet.api.*;
import oracle.oats.scripting.modules.basic.api.internal.FuncLibraryWrapper;
import oracle.oats.scripting.modules.basic.api.exceptions.AbstractScriptException;

public class WEBLABELLIB extends FuncLibraryWrapper
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

	/**
	 * @param labelWithIndex
	 * @param valueToEnter
	 * @throws Exception
	 */
	public void webSetTextBasedOnLabel(String labelWithIndex, String valueToEnter) throws AbstractScriptException {
		checkInit();
		callFunction("webSetTextBasedOnLabel", labelWithIndex, valueToEnter);
	}

	/**
	 * Select List Box based on Label -- labelWithIndex (label;1,document=1,form=1) and label should be passed based on OATS recordings
	 * @param labelWithIndex
	 * @param valueToselect
	 * @throws Exception
	 */
	public void webSelectListBoxBasedOnLabel(String labelWithIndex, String valueToselect) throws AbstractScriptException {
		checkInit();
		callFunction("webSelectListBoxBasedOnLabel", labelWithIndex, valueToselect);
	}

	/**
	 * @param labelWithIndex -- labelWithIndex (label;1,document=1,form=1) and label should be passed based on OATS recordings
	 * @return
	 * @throws Exception
	 */
	public String webGetLinkBasedOnLabel(String labelWithIndex) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetLinkBasedOnLabel", labelWithIndex);
	}

	/**
	 * @param labelWithIndex --  labelWithIndex (label;1,document=1,form=1) and label should be passed based on OATS recordings
	 * @return
	 * @throws Exception
	 */
	public String webGetTextBasedOnLabel(String labelWithIndex) throws AbstractScriptException {
		checkInit();
		return (String) callFunction("webGetTextBasedOnLabel", labelWithIndex);
	}

	public boolean webVerifyLinkBasedOnLabel(String labelWithIndex, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyLinkBasedOnLabel", labelWithIndex, expectedValue);
	}

	public boolean webVerifyTextBasedOnLabel(String labelWithIndex, String expectedValue) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("webVerifyTextBasedOnLabel", labelWithIndex, expectedValue);
	}

	public boolean compareStrings(String expectedString, String actualString) throws AbstractScriptException {
		checkInit();
		return (Boolean) callFunction("compareStrings", expectedString, actualString);
	}

	public HashMap<String, String> getComponentDetails(String compTypeWithIndex) throws AbstractScriptException {
		checkInit();
		return (HashMap<String, String>) callFunction("getComponentDetails", compTypeWithIndex);
	}

	public List<DOMElement> getComponents(String docPath, String tag, String expectedData) throws AbstractScriptException {
		checkInit();
		return (List<DOMElement>) callFunction("getComponents", docPath, tag, expectedData);
	}

	public DOMElement webGetCompBasedOnLabel(String labelWithIndex, String compType) throws AbstractScriptException {
		checkInit();
		return (DOMElement) callFunction("webGetCompBasedOnLabel", labelWithIndex, compType);
	}

	public DOMElement getNextElement(DOMElement component, String compType) throws AbstractScriptException {
		checkInit();
		return (DOMElement) callFunction("getNextElement", component, compType);
	}

}

