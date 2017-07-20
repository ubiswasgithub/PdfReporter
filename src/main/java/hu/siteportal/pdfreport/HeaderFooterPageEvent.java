package hu.siteportal.pdfreport;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.IOException;
import java.net.MalformedURLException;

public class HeaderFooterPageEvent extends PdfPageEventHelper {
	private PdfTemplate t;
	private Image total;

	public void onOpenDocument(PdfWriter writer, Document document) {
		t = writer.getDirectContent().createTemplate(30, 16);
		try {
			total = Image.getInstance(t);

		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}

	public void onEndPage(PdfWriter writer, Document document) {
		String methodDsc = Result.getResultByMethod();
		addHeader(writer, document, methodDsc);
		addFooter(writer, document);
	}

	private void addHeader(PdfWriter writer, Document doc, String desc) {
		final String IMG = System.getProperty("user.dir") + "\\src\\main\\resources\\medavante-logo.png";
		PdfPTable header = new PdfPTable(new float[] { .2f, .3f });

		try {
			// set defaults
			header.setTotalWidth(527);
			header.setLockedWidth(true);
			header.setWidthPercentage(100);

			// add image
			Image logo = Image.getInstance(IMG);

			PdfPCell cell = new PdfPCell(logo, true);
			cell.setFixedHeight(90);
			cell.setPaddingTop(25);
			cell.setBorder(Rectangle.BOTTOM);
			cell.setBorderColor(BaseColor.LIGHT_GRAY);
			header.addCell(cell);

			// add text
			PdfPCell text = new PdfPCell();
			text.setPaddingTop(32);
			
			text.setPaddingLeft(33);
			text.setBorder(Rectangle.BOTTOM);
			text.setBorderColor(BaseColor.LIGHT_GRAY);
			text.addElement(new Phrase("Item : " + desc, new Font(Font.FontFamily.HELVETICA, 12)));
			text.setHorizontalAlignment(Element.ALIGN_RIGHT);
			header.addCell(text);

			// write content
			header.setSpacingAfter(75f);
			header.writeSelectedRows(0, -1, 34, 849, writer.getDirectContent());

		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		} catch (MalformedURLException e) {
			throw new ExceptionConverter(e);
		} catch (IOException e) {
			throw new ExceptionConverter(e);
		}

	}

	private void addFooter(PdfWriter writer, Document doc) {
		PdfPTable footer = new PdfPTable(3);
		try {
			// set defaults
			footer.setWidths(new int[] { 24, 2, 1 });
			footer.setTotalWidth(527);
			footer.setLockedWidth(true);
			footer.getDefaultCell().setFixedHeight(40);
			footer.getDefaultCell().setBorder(Rectangle.TOP);
			footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

			// add copyright
			footer.addCell(
					new Phrase("\u00A9 www.medavante.com", new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL)));

			// add current page count
			footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
			footer.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()),
					new Font(Font.FontFamily.HELVETICA, 8)));

			// add placeholder for total page count
			PdfPCell totalPageCount = new PdfPCell(total);
			totalPageCount.setBorder(Rectangle.TOP);
			totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
			footer.addCell(totalPageCount);

			// write page
			PdfContentByte canvas = writer.getDirectContent();
			canvas.beginMarkedContentSequence(PdfName.ART);
			footer.writeSelectedRows(0, -1, 34, 50, canvas);
			footer.setSpacingBefore(45f);
			
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}

	}

	public void onCloseDocument(PdfWriter writer, Document document) {
		int totalLength = String.valueOf(writer.getPageNumber()).length();
		int totalWidth = totalLength * 5;
		ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
				new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)), totalWidth,
				6, 0);
	}

}
