package com.lyg.office.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import com.lyg.entitys.MIME;


public class StreamWriters {
	
	/****
	 * 返回流信息
	 * 
	 * @param response
	 * @param byteArrayOutputStream
	 * @param filename
	 * @param mime
	 * @throws IOException
	 */
	public static void StreamWriter(HttpServletResponse response,
			ByteArrayOutputStream byteArrayOutputStream, String filename,
			MIME mime) throws IOException {
		StreamWriter(response, byteArrayOutputStream.toByteArray(), filename,
				mime);
	}
	

	/****
	 * 返回流信息
	 * 
	 * @param response
	 * @param byteArray
	 * @param filename
	 * @param mime
	 * @throws IOException
	 */
	public static void StreamWriter(HttpServletResponse response,
			byte[] byteArray, String filename, MIME mime) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(mime.toString());
		response.addHeader("Content-Disposition", "attachment;filename=\""
				+ new String(new String(filename.getBytes("GBK"), "ISO8859_1"))
				+ mime.getSubffix() + "\"");
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(byteArray);
		outputStream.flush();
		outputStream.close();
		response.flushBuffer();
	}

}
