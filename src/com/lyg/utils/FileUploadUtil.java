package com.lyg.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传工具类
 * 页面表单需要加上： 
 *      enctype="multipart/form-data" 
 *      method="post"
 * @author Administrator
 *
 */
public class FileUploadUtil {
	
	/**
	 * 获取文件的后缀名
	 * @param filename  文件名
	 * @return
	 */
	 public static String getExtensionName(String filename) {   
	        if ((filename != null) && (filename.length() > 0)) {   
	            int dot = filename.lastIndexOf('.');   
	            if ((dot >-1) && (dot < (filename.length() - 1))) {   
	                return filename.substring(dot + 1);   
	            }   
	        }   
	        return filename;   
	    }   
	 
	 /**
	  * 文件上传
	  * @param request
	  * @param file    上传的文件
	  * @param uploadDir 上传的文件路径
	  * @return
	  */
	 public static String uploadFile(HttpServletRequest request,MultipartFile file,String uploadDir){
	        String path = request.getSession().getServletContext().getRealPath(uploadDir);  
	        String ylfileName = file.getOriginalFilename(); 
	        String  fileName = UUID.randomUUID().toString()+"."+getExtensionName(ylfileName);
	        System.out.println(path);  
	        File targetFile = new File(path, fileName);  
	        if(!targetFile.exists()){  
	            targetFile.mkdirs();  
	        }  
	        //保存  
	        try {  
	            file.transferTo(targetFile);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        //
		 return request.getContextPath()+"/"+uploadDir+"/"+fileName;
	 }
	 
	 
	/**
	 * 使用发法
	 * 如上传图片资源
	 * @param response
	 * @param file   这个名字必须是页面上传文本框name的名字
	 * @param request
	 * @throws IOException
	 */
	//@RequestMapping("saveImgFile")
	public void saveImg(HttpServletResponse response,MultipartFile file,HttpServletRequest request) throws IOException{
		String url="../test/test/img";
		String fileName=uploadFile(request, file,url);
		//返回文件上传后的一个相对路径+文件
		//如 SSM/../ssm/test/img/d3e2d3eb-bf79-4cc8-8246-842c612bccef.jpg
		// SSM-项目名  ssm/test/img文件夹
		System.out.println("fileName"+fileName);
	}

}
