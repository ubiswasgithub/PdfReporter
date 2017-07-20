package hu.siteportal.pdfreport;

import java.util.List;

public class PdfLogList {

	public static void setLogStatus(String msg, List<String> list) {
		list.add(msg);
	}
}
