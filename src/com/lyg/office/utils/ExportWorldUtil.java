package com.lyg.office.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lyg.entitys.MIME;

/**
 * 导出world工具类
 * @author xiaoguizi
 *
 */
public class ExportWorldUtil {
	
	/****
	 * 请求模式：普通请求 导出word文件 类型为.doc
	 * 
	 * @param response
	 * @param request
	 * @param filename
	 *            导出文件名
	 * @param templateFile
	 *            模版文件名
	 * @param mapValue
	 *            Map数据填充对象
	 * @throws IOException
	 */
	public static void exportWord(HttpServletResponse response,
			HttpServletRequest request, String filename, String templateFile,
			Map<String, Object> mapValue) throws IOException {
		String path = request.getServletContext().getRealPath(File.separator)
				+ File.separator+"WEB-INF"+File.separator+"FileTemplate"+File.separator;
		String inputPath = path + templateFile + ".doc";
		ByteArrayOutputStream outputStream = WordExtractor.getNewWordStream(
				inputPath, mapValue);
		StreamWriters.StreamWriter(response, outputStream, filename, MIME.WORD);
	}
	

}
