package com.lyg.office.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.TextPiece;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;


@SuppressWarnings({"unused","deprecation"})
public final class WordExtractor extends POIOLE2TextExtractor {
	
	private POIFSFileSystem fs;
	private HWPFDocument doc;

	public WordExtractor(InputStream is) throws IOException {
		this(HWPFDocument.verifyAndBuildPOIFS(is));
	}

	public WordExtractor(POIFSFileSystem fs) throws IOException {
		this(new HWPFDocument(fs));
		this.fs = fs;
	}


	public WordExtractor(HWPFDocument doc) {
		super(doc);
		this.doc = doc;
	}

	public static  void  getNewWord(String templatePath,String destFile,Map<String,String> contentMap) throws IOException {
	
		FileInputStream fin = new FileInputStream(templatePath);
		WordExtractor extractor = new WordExtractor(fin);
		HWPFDocument document= extractor.doc;
	    Range range=document.getRange();
	    for (Map.Entry<String, String> entry : contentMap.entrySet()) {
	    	range.replaceText("${" + entry.getKey() + "}",entry.getValue());
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			document.write(byteArrayOutputStream);
			OutputStream outputStream = new FileOutputStream(destFile);
			outputStream.write(byteArrayOutputStream.toByteArray());
			outputStream.close();
		} catch (IOException e) {
		}
		extractor.close();
		
	}

	public static  ByteArrayOutputStream  getNewWordStream(String templatePath,Map<String,Object> contentMap) throws IOException {
	
		FileInputStream fin = new FileInputStream(templatePath);
		WordExtractor extractor = new WordExtractor(fin);
		HWPFDocument document= extractor.doc;
	    Range range=document.getRange();
	    for (Map.Entry<String, Object> entry : contentMap.entrySet()) {
	    	range.replaceText("${" + entry.getKey() + "}",entry.getValue()==null?" ":entry.getValue()+"");
		}
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			document.write(byteArrayOutputStream);
			return byteArrayOutputStream;
		} catch (IOException e) {
			return null;
		}finally{
			extractor.close();
		}
	
		
	}

	public String[] getParagraphText() {
		String[] ret;
		try {
			Range r = this.doc.getRange();

			ret = getParagraphText(r);
		} catch (Exception e) {
			ret = new String[1];
			ret[0] = getTextFromPieces();
		}

		return ret;
	}

	public String[] getFootnoteText() {
		Range r = this.doc.getFootnoteRange();

		return getParagraphText(r);
	}

	public String[] getEndnoteText() {
		Range r = this.doc.getEndnoteRange();

		return getParagraphText(r);
	}

	public String[] getCommentsText() {
		Range r = this.doc.getCommentsRange();

		return getParagraphText(r);
	}

	protected static String[] getParagraphText(Range r) {
		String[] ret = new String[r.numParagraphs()];
		for (int i = 0; i < ret.length; ++i) {
			Paragraph p = r.getParagraph(i);
			ret[i] = p.text();

			if (ret[i].endsWith("\r")) {
				ret[i] = ret[i] + "\n";
			}
		}
		return ret;
	}

	private void appendHeaderFooter(String text, StringBuffer out) {
		if ((text == null) || (text.length() == 0)) {
			return;
		}
		text = text.replace('\r', '\n');
		if (!(text.endsWith("\n"))) {
			out.append(text);
			out.append('\n');
			return;
		}
		if (text.endsWith("\n\n")) {
			out.append(text.substring(0, text.length() - 1));
			return;
		}
		out.append(text);
	}

	public String getHeaderText() {
		HeaderStories hs = new HeaderStories(this.doc);

		StringBuffer ret = new StringBuffer();
		if (hs.getFirstHeader() != null) {
			appendHeaderFooter(hs.getFirstHeader(), ret);
		}
		if (hs.getEvenHeader() != null) {
			appendHeaderFooter(hs.getEvenHeader(), ret);
		}
		if (hs.getOddHeader() != null) {
			appendHeaderFooter(hs.getOddHeader(), ret);
		}

		return ret.toString();
	}

	public String getFooterText() {
		HeaderStories hs = new HeaderStories(this.doc);

		StringBuffer ret = new StringBuffer();
		if (hs.getFirstFooter() != null) {
			appendHeaderFooter(hs.getFirstFooter(), ret);
		}
		if (hs.getEvenFooter() != null) {
			appendHeaderFooter(hs.getEvenFooter(), ret);
		}
		if (hs.getOddFooter() != null) {
			appendHeaderFooter(hs.getOddFooter(), ret);
		}

		return ret.toString();
	}

	public String getTextFromPieces() {
		StringBuffer textBuf = new StringBuffer();

		for (TextPiece piece : this.doc.getTextTable().getTextPieces()) {
			String encoding = "Cp1252";
			if (piece.isUnicode())
				encoding = "UTF-16LE";
			try {
				String text = new String(piece.getRawBytes(), encoding);
				textBuf.append(text);
			} catch (UnsupportedEncodingException e) {
				throw new InternalError("Standard Encoding " + encoding
						+ " not found, JVM broken");
			}
		}

		String text = textBuf.toString();

		text = text.replaceAll("\r\r\r", "\r\n\r\n\r\n");
		text = text.replaceAll("\r\r", "\r\n\r\n");

		if (text.endsWith("\r")) {
			text = text + "\n";
		}

		return text;
	}

	public String getText() {
		StringBuffer ret = new StringBuffer();

		ret.append(getHeaderText());

		ArrayList<String> text = new ArrayList<String>();
		text.addAll(Arrays.asList(getParagraphText()));
		text.addAll(Arrays.asList(getFootnoteText()));
		text.addAll(Arrays.asList(getEndnoteText()));

		for (String p : text) {
			ret.append(p);
		}

		ret.append(getFooterText());

		return ret.toString();
	}

	public static String stripFields(String text) {
		return Range.stripFields(text);
	}
}