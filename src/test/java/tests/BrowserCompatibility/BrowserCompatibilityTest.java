package tests.BrowserCompatibility;

import java.lang.reflect.Method;

import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import hu.siteportal.pdfreport.PdfLogging;
import hu.siteportal.pdfreport.PdfLog;
import hu.siteportal.pdfreport.StepVerify;
import mt.siteportal.utils.helpers.Nav;
import mt.siteportal.utils.tools.Log;
import steps.Tests.BrowserCompatibilityStep;

/**
 * Description: This class verifies browser compatibility warning message for
 * not compatible browsers
 * 
 * @author ubiswas
 *
 */
@Listeners(PdfLogging.class)
@Test(groups = { "BrowserCompatibility" })
public class BrowserCompatibilityTest extends AbstractBrowserCompatibility {
	
	@BeforeClass(alwaysRun = true)
	public synchronized void beforeClass(){
		Log.logTestClassStart(this.getClass());
		browserCompStep = new BrowserCompatibilityStep();
	}

	@Test(groups = { "browserIncompatibilityTest", "JamaNA" }, description = "Checking browser compatibility ")
	public void browserIncompatibilityTest() throws InterruptedException {
		browserCompStep.compatibilityWaringMessageVerification(warningMessage);
		browserCompStep.isPresentDissmissButton();
		browserCompStep.dissmissWarningMessageVerification();
	}
	
	@Test(groups = { "browserIncompatibilityFailedTest", "JamaNA" }, dependsOnGroups = "browserIncompatibilityTest", description = "Checking browser compatibility")
	public void browserIncompatibilityFailedTest() throws InterruptedException {
		StepVerify.True(false, "Checking failed method", "Method is failed", "Method is passed");
		// Assert.assertTrue(false);

	}
	
	@Test(groups = { "browserIncompatibilitySkippedTest",
			"JamaNA" }, dependsOnGroups = "browserIncompatibilityFailedTest", description = "Checking browser compatibility")
	public void browserIncompatibilitySkippedTest() throws InterruptedException {
		StepVerify.True(false, "Checking failed method", "Method is failed", "Method is passed");
		// Assert.assertTrue(false);
	}


	@BeforeMethod(alwaysRun = true)
	public synchronized void beforeMethod(Method method) {
		PdfLog.clearActionResultStatusList();
		Log.logTestMethodStart(method);
		Log.logStep("Navigating to Login page...");
		
		Nav.toURL(loginUrl);
		StepVerify.True(true, "Navigating to Login page...", "Login page is displayed", "Login page is not displayed");
		
	}
	
	
	@AfterMethod(alwaysRun = true)
	public synchronized void afterMethod(Method method, ITestResult result) {
		Log.logTestMethodEnd(method, result);
	}
	
	
	@AfterClass(alwaysRun = true)
	public synchronized void afterClass(){
		Log.logTestClassEnd(this.getClass());
	}

}
