package com.lyg.entitys;

public enum MIME {
	WORD("application/x-msdownload", ".doc"), 
	WORD1("application/msword", ".doc"), 
	EXCEL("application/ms-excel", ".xls"), 
	EXCEL2("application/msexcel", ".xls"), 
	EXCEL1("application/vnd.ms-excel", ".xls"), 
	JSON("application/json", ".json"), 
	XML("text/xml", ".xml"), 
	XWORD("application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"), 
	XEXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",".xlsx"), 
	TEXT("application/html", ".txt"),
	ZIP("application/zip", ".zip");
	
	private String strMime;
	private String subffix;

	private MIME(String mime, String subffix) {
		this.strMime = mime;
		this.subffix = subffix;
	}

	@Override
	public String toString() {
		return this.strMime;
	}

	public String getSubffix() {
		return subffix;
	}
}