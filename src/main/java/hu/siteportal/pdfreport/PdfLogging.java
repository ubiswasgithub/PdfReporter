package hu.siteportal.pdfreport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfPTable;

import mt.siteportal.utils.Browser;

public class PdfLogging implements ITestListener {

	private static PDFGenerator pdfG = new PDFGenerator();
	PdfPTable successTable = null, failTable = null, skippTable = null;
	 
	private HashMap<Integer, Throwable> throwableMap = null;
	private String hostName = null, suiteName = null;Paragraph  p = null;
	
	// PDF output directories
	String parentDir, passedItems, failedItems, skippedItems, failedScreenShot;
	private static WebDriver driver = null;

	// Helper method 
	private String getFormattedDate(Date date){
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
		String formattedDate = formatter.format(date);
		return formattedDate;
		
	}
	
	private void checkDirectory(String folder){
		File theDir = new File (folder);
		if(!theDir.exists()){
			try {
				theDir.mkdir();
			}catch (Exception e){
				e.getMessage();
			}
		}
		
	}
	
	private Paragraph captureFailedScreen(Throwable throwable) {
		String file = failedScreenShot + (new Random().nextInt()) + ".png";
		Paragraph excep = null;
		try {
			TakeScreenShot.takeScreenShot(Browser.getDriver(), file);
			if (throwable != null) {
				this.throwableMap.put(new Integer(throwable.hashCode()), throwable);
				Chunk imdb = new Chunk("[Screen Shot]",
						new Font(Font.getFamily("TIMES_ROMAN"), Font.DEFAULTSIZE, Font.UNDERLINE));
				imdb.setAction(new PdfAction("file:///" + file));
				excep = new Paragraph();
				excep.add(imdb);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return excep;
	}

	public void onTestStart(ITestResult result) {
		
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		p = null;
		successTable = null;

		// getting result from itestResult
		String exedate = getFormattedDate(result.getTestContext().getStartDate()).toString();
		ITestNGMethod method = result.getMethod();
		String methodDes = method.getDescription();
		Result.setResultByMethod(methodDes);

		// open PDF document
		try {
			pdfG.startPdf(passedItems, result.getMethod().getMethodName().toString());
		} catch (FileNotFoundException | DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// design PDF table header and rows with test data
		try {
			if (successTable == null) {
				successTable = pdfG.tableHeaderFormat(method.getMethodName() + " results");
			}
			pdfG.generatePDF(successTable, p, method.getMethodName(), PdfLog.getActionList(), PdfLog.getExpectedResultList(),PdfLog.getActualResultList(),
					hostName, exedate, methodDes, suiteName, PdfLog.getStatusList(), "Passed");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onTestFailure(ITestResult result) {
		failTable = null;

		// getting result from itestResult
		ITestNGMethod method = result.getMethod();
		String methodDes = method.getDescription();
		String exedate = getFormattedDate(result.getTestContext().getStartDate()).toString();
		Result.setResultByMethod(methodDes);

		// throw exception
		Throwable throwable = result.getThrowable();
		p = captureFailedScreen(throwable);

		// open PDF document
		try {
			pdfG.startPdf(failedItems, result.getMethod().getMethodName().toString());
		} catch (FileNotFoundException | DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// design PDF table header and rows with test data
		try {
			if (failTable == null) {
				failTable = pdfG.tableHeaderFormat(method.getMethodName() + " results");
			}
			pdfG.generatePDF(failTable, p, method.getMethodName(), PdfLog.getActionList(), PdfLog.getExpectedResultList(),PdfLog.getActualResultList(),
					hostName, exedate, methodDes, suiteName, PdfLog.getStatusList(), "Failed");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (DocumentException e) {

			e.printStackTrace();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		p = null;
		skippTable = null;

		// getting result from itestResult
		ITestNGMethod method = result.getMethod();
		String methodDes = method.getDescription();
		Result.setResultByMethod(methodDes);
		String exedate = getFormattedDate(result.getTestContext().getStartDate()).toString();
		PdfLog.getActionList().clear();

		// open PDF document
		try {
			pdfG.startPdf(skippedItems, result.getMethod().getMethodName().toString());
		} catch (FileNotFoundException | DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// design PDF table header and rows with test data
		try {
			if (skippTable == null) {
				skippTable = pdfG.tableHeaderFormat(method.getMethodName() + " results");
			}
			pdfG.generatePDF(skippTable, p, method.getMethodName(), PdfLog.getActionList(), PdfLog.getExpectedResultList(),PdfLog.getActualResultList(),
					hostName, exedate, methodDes, suiteName, PdfLog.getStatusList(), "Skipped");
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (DocumentException e) {

			e.printStackTrace();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {	
	}

	@Override
	public void onStart(ITestContext context) {
		this.throwableMap = new HashMap<Integer, Throwable>();
		suiteName = context.getCurrentXmlTest().getSuite().getName();

		try {
			hostName = context.getHost().toString();
		} catch (Exception e) {
			hostName = "10.21.64.110";
		}

		// initiate PDF output directories
		parentDir = System.getProperty("user.dir") + "\\Pdf-output\\";
		passedItems = System.getProperty("user.dir") + "\\Pdf-output\\PassedTestCases\\";
		failedItems = System.getProperty("user.dir") + "\\Pdf-output\\FailedTestCases\\";
		skippedItems = System.getProperty("user.dir") + "\\Pdf-output\\SkippedTestCases\\";
		failedScreenShot = System.getProperty("user.dir") + "\\Pdf-output\\FailedTestCases\\Screenshot\\";

		checkDirectory(parentDir);
		checkDirectory(passedItems);
		checkDirectory(failedItems);
		checkDirectory(skippedItems);
		checkDirectory(failedScreenShot);
	}

	@Override
	public void onFinish(ITestContext context) {
	}
	
	/**
	 * Used for setting driver from another project
	 * 
	 * @param drv
	 */
	public static void setDriver(WebDriver drv) {
		driver = drv;	
	}

}
