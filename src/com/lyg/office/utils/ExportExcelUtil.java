package com.lyg.office.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.lyg.utils.StringUtil;
/**
 * 导出Excel工具类
 * 使用方法：
 *  1.项目需要引入commons-collections4或以上版本的jar包
 *  2.新建文件夹WEB-INF/FileTemplate
 *  3.在FileTemplate文件夹下放入模板，模板的第一个值放入${listdata}
 *  4.把要导出的数据放到List<String[]>集合中传过来
 * 调用方式：
 *  ExportExcelUtil.comExportExcel(response, request, 导出的名字, 模板名, null, 导出的数据);
 * @author Administrator
 *  
 */
public class ExportExcelUtil {
	
	
	/**
	 * 
	 * @param response
	 * @param request
	 * @param excelFilename  导出文件命名
	 * @param templateFile   模版文件命名面板存放固定位置： WEB-INF/FileTemplate
	 * @param mapValue       填充数据，模版中定义变量格式${key}
	 * @param lists          数据列表
	 * @throws IOException
	 */
	public static void comExportExcel(HttpServletResponse response,
			HttpServletRequest request, String excelFilename,
			String templateFile, Map<String, String> mapValue,
			List<String[]> lists) throws IOException {
		comExportExcel(response, request, excelFilename, templateFile,
				mapValue, lists, 0);
	}
	
	/****
	 * 模版导出Excel 类型为.xls
	 * 
	 * @param response
	 * @param request
	 * @param excelFilename
	 *            导出文件命名
	 * @param templateFile
	 *            模版文件命名面板存放固定位置： WEB-INF/FileTemplate
	 * @param mapValue
	 *            填充数据，模版中定义变量格式${key}
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void exportExcel(HttpServletResponse response,
			HttpServletRequest request, String excelFilename,
			String templateFile, Map<String, String> mapValue) throws Exception {

		List<String[]> lists = new ArrayList();
		int n = 0;
		while (true) {
			String[] strs = request.getParameterValues("data[" + n + "][]");// 此处为页面传人数组
			if (strs == null)
				break;
			lists.add(strs);
			n++;
		}
		comExportExcel(response, request, excelFilename, templateFile,
				mapValue, lists, 0);
	}
	
	/***
	 * 导出Excel
	 * @param response
	 * @param request
	 * @param excelFilename
	 *            导出文件名
	 * @param templateFile
	 *            使用的模板文件 路径\WEB-INF\FileTemplate\XXX.xls
	 * @param mapValue
	 *            分散单项值
	 * @param lists
	 *            数据列表
	 * @param  sheetNum
	 *            sheet数，及表的数量
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static void comExportExcel(HttpServletResponse response,
			HttpServletRequest request, String excelFilename,
			String templateFile, Map<String, String> mapValue,
			List<String[]> lists, int sheetNum) throws IOException {
		FileInputStream fileIn = null;
		HSSFWorkbook wb = null;
		try {
			fileIn = new FileInputStream(request.getServletContext()
					.getRealPath(File.separator)
					+ File.separator+"WEB-INF"+File.separator+"FileTemplate"+File.separator
					+ templateFile + ".xls");
			POIFSFileSystem fs = new POIFSFileSystem(fileIn);
			wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(sheetNum);
			String value;
			HSSFRow row;
			HSSFCell cell;
			String prop;
			int dataRow = -1;
			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				row = sheet.getRow(i);
				if (row != null) {
					for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
						cell = row.getCell(j);
						if (cell != null
								&& cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
							value = cell.getStringCellValue();
							if (value.matches("\\S*\\$\\{\\S+\\}\\S*")) {
								if (mapValue != null) {
									prop = value.substring(
											value.indexOf("${") + 2,
											value.indexOf("}"));
									cell.setCellValue(value.replace(
													"${" + prop + "}",
													StringUtil.NulltoEmpty(mapValue.get(prop))));
								}
								if (value.indexOf("${listdata}") > -1) {
									dataRow = i;
								}
							}

						}
					}
					if (dataRow > -1)
						break;
				}
			}
			if (lists.size() > 0 && dataRow > -1) {
				HSSFRow tmrow = sheet.getRow(dataRow);
				sheet.shiftRows(dataRow + 1, sheet.getLastRowNum(),
						lists.size() - 1, true, true, true);
				HSSFCell tmcell;
				String text;
				for (int i = 0; i < lists.size(); i++) {
					row = sheet.getRow(dataRow + i);
					if (row == null)
						row = sheet.createRow(dataRow + i);
					for (int j = 0; j < lists.get(i).length; j++) {
						cell = row.getCell(j);
						tmcell = tmrow.getCell(j);
						if (cell == null)
							cell = row.createCell(j);
						if (tmcell != null) {
							cell.setCellStyle(tmcell.getCellStyle());
						}
						text = lists.get(i)[j];
						cell.setCellValue(text);
					}
				}
			}
			String filename = excelFilename;
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			wb.write(outputStream);
			StreamWriter(response, outputStream, filename, "application/ms-excel");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileIn != null){
				fileIn.close();
			}
		}
	}
	
	/****
	 * 返回流信息
	 * @param response
	 * @param byteArrayOutputStream
	 * @param filename
	 * @param mime
	 * @throws IOException
	 */
	public static void StreamWriter(HttpServletResponse response,
			ByteArrayOutputStream byteArrayOutputStream, String filename,
			String mime) throws IOException {
		StreamWriter(response, byteArrayOutputStream.toByteArray(), filename,mime);
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
			byte[] byteArray, String filename, String mime) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(mime.toString());
		response.addHeader("Content-Disposition", "attachment;filename=\""
				+ new String(new String(filename.getBytes("GBK"), "ISO8859_1"))
				+ ".xls" + "\"");
		ServletOutputStream outputStream = response.getOutputStream();
		outputStream.write(byteArray);
		outputStream.flush();
		outputStream.close();
		response.flushBuffer();
	}

}
