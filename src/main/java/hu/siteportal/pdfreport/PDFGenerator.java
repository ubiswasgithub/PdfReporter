package hu.siteportal.pdfreport;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFGenerator {

	private Document doc = null;
	PdfPTable successTable = null, failTable = null, skippTable = null;
	PdfWriter writer = null;
	public PdfPTable createTable() {
		PdfPTable table = new PdfPTable(new float[] { .1f, .3f, .3f,.3f, .2f, .2f });
		table.setTotalWidth(500);
		table.setWidthPercentage(100);
		return table;

	}

	public Paragraph createParagraph(PdfPTable table, String pragraphTitle) {
		Paragraph p = new Paragraph(pragraphTitle,
				new Font(Font.getFamily("TIMES_ROMAN"), Font.DEFAULTSIZE, Font.NORMAL));
		p.setAlignment(Element.ALIGN_LEFT);
		return p;

	}
	
	public Paragraph createParagraphForHeader(PdfPTable table, String pragraphTitle) {
		Paragraph p = new Paragraph(pragraphTitle,
				new Font(Font.getFamily("TIMES_ROMAN"), Font.DEFAULTSIZE, Font.BOLD));
		p.setAlignment(Element.ALIGN_LEFT);
		return p;

	}


	public PdfPCell createCell(Paragraph p, BaseColor b) {

		PdfPCell pCell = new PdfPCell(p);
		pCell.setBackgroundColor(b);

		return pCell;

	}

	public void pdfClose() {
		this.doc.close();
	}

	public void pdfOpen() {
		this.doc.open();
	}

	public void comonStructure(PdfPTable t, String paragraphTitle, BaseColor b) {

		Paragraph p;
		PdfPCell c;

		p = this.createParagraph(t, paragraphTitle);
		c = this.createCell(p, b);
		t.addCell(c);

	}
	
	public void comonStructure(PdfPTable t, Paragraph paragraphTitle, BaseColor b) {

		
		PdfPCell c;
		
		c = this.createCell(paragraphTitle, b);
		t.addCell(c);

	}
	
	

	public PdfPTable tableHeaderFormat(String methodname) throws DocumentException {
		
		PdfPTable t = null;	
		
		t = this.createTable();

	/*	p = this.createParagraphForHeader(t, methodname);
		c = this.createCell(p, BaseColor.WHITE);
		c.setColspan(6);
		t.addCell(c);
*/
		this.comonStructure(t, "Step#", BaseColor.LIGHT_GRAY);
		this.comonStructure(t, "Action", BaseColor.LIGHT_GRAY);
		this.comonStructure(t, "Expected Result", BaseColor.LIGHT_GRAY);
		this.comonStructure(t, "Actual Result", BaseColor.LIGHT_GRAY);
//		this.comonStructure(t, "Notes", BaseColor.LIGHT_GRAY);
		this.comonStructure(t, "Status", BaseColor.LIGHT_GRAY);
		this.comonStructure(t, "Notes", BaseColor.LIGHT_GRAY);
	

		return t;
	}
	
	
	public PdfPTable executionHeaderFormat() throws DocumentException {
		
		PdfPTable t = null;
	
		
		t = new PdfPTable(new float[] { .3f, .3f, .3f });
		t.setTotalWidth(500);
		t.setWidthPercentage(100);

	/*	p = this.createParagraph(t, "Execution Information: ");
		c = this.createCell(p, BaseColor.WHITE);
		c.setColspan(3);
		t.addCell(c);*/

		this.comonStructure(t, "Execution Date", BaseColor.LIGHT_GRAY);
		this.comonStructure(t, "Execution Environment", BaseColor.LIGHT_GRAY);
		this.comonStructure(t, "Browser", BaseColor.LIGHT_GRAY);

		return t;

	}

	public void startPdf(String dir, String pdfName) throws FileNotFoundException, DocumentException {
		this.doc = new Document(PageSize.A4, 36, 36, 90, 36);
		writer = PdfWriter.getInstance(this.doc, new FileOutputStream(dir+pdfName + ".pdf"));
		// add header and footer
        HeaderFooterPageEvent event = new HeaderFooterPageEvent();
        writer.setPageEvent(event);
        
		this.pdfOpen();
	}
	
	private PdfPTable createHeader(String desc) throws BadElementException, MalformedURLException, IOException {

		PdfPTable table = new PdfPTable(new float[] { .3f, .3f });
		final String IMG = System.getProperty("user.dir") + "\\src\\main\\resources\\MedavanteLogo.png";

		Image img = Image.getInstance(IMG);

		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.setTotalWidth(170);
		PdfPCell cell = new PdfPCell(img, true);
		cell.setBorder(Rectangle.NO_BORDER);

		cell.setFixedHeight(90);

		table.addCell(cell);

		table.addCell("Item: " + desc);

		table.setSpacingAfter(75f);
		return table;
	}
	private  PdfPTable createFooter(){
		PdfPTable table = new PdfPTable(1);
        table.setTotalWidth(500);
        table.setLockedWidth(true);
   
        table.setWidthPercentage(100);
    
        Paragraph p = new Paragraph();
        PdfPCell cell = new PdfPCell(p);
        cell.setBorder(Rectangle.NO_BORDER);
        
        table.addCell("Footer");
        table.setSpacingBefore(45f);		
        return table;
	}
	
	private  PdfPTable createTestDescription(String desc,String suiteName, String date, String passorfail){
		PdfPTable table = new PdfPTable(new float[] { .2f, .3f});
	
        table.setTotalWidth(500);
        table.setLockedWidth(true);
   
        table.setWidthPercentage(100);
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
        table.getDefaultCell().setPaddingLeft(0);
    
        table.addCell("Test Plan: ");
        table.addCell(suiteName);
        
        table.addCell("Test Script Description: ");
        table.addCell(desc);

        table.addCell("Executed by: ");
        table.addCell("System");

        table.addCell("Test Execution date: ");
        table.addCell(date);
        

        table.addCell("Test Run Status: ");
        table.addCell(passorfail);
            
        table.setSpacingAfter(45f);
        
        return table;
       
	}

	public void generatePDF(PdfPTable t, Paragraph note, String pdfName, List<String> actionList,
			List<String> expectedResultList, List<String> actualResultList, String host, String exedate, String desc, String suiteName,
			List<String> statusList, String methodStatus) throws DocumentException, MalformedURLException, IOException {
		int step = 1;
		PdfPTable dt = this.createTestDescription(desc, suiteName, exedate, methodStatus);

		addedToDocument(dt);

		if (!actionList.isEmpty()) {
			for (int i = 0; i < actionList.size(); i++) {
				this.comonStructure(t, "#" + String.valueOf(step), BaseColor.WHITE);
				this.comonStructure(t, actionList.get(i).toString(), BaseColor.WHITE);
				this.comonStructure(t, expectedResultList.get(i).toString(), BaseColor.WHITE);
				this.comonStructure(t, actualResultList.get(i).toString(), BaseColor.WHITE);
				// this.comonStructure(t, resultList.get(i).toString(),
				// BaseColor.WHITE);
				this.comonStructure(t, statusList.get(i), BaseColor.WHITE);
				if (statusList.get(i).equalsIgnoreCase("Failed")) {
					this.comonStructure(t, note, BaseColor.WHITE);
				} else
					this.comonStructure(t, "", BaseColor.WHITE);

				step++;
			}
		}

		t.setSpacingAfter(45f);

		addedToDocument(t);

		try {
			PdfPTable exeTable = this.executionHeaderFormat();

			this.comonStructure(exeTable, exedate, BaseColor.WHITE);
			this.comonStructure(exeTable, host, BaseColor.WHITE);
			this.comonStructure(exeTable, "Google Chrome", BaseColor.WHITE);
			this.addedToDocument(exeTable);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.pdfClose();
	}

	public void addedToDocument(PdfPTable t) throws DocumentException{

		this.doc.add(t);
	}

}
