package com.lyg.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;
/**
 * 生成二维码工具类
 * @author AISINO
 *
 */

public class CreateQrcodeUtil {
	/**
	 * 
	 * @param content  内容
	 * @param imgPath  路径
	 * @return  返回值
	 */
	public static BufferedImage getQrodeImg(String content,String  imgPath) {
		int width = 140;
		int height = 140;
		BufferedImage bufferedImage = new BufferedImage(width, height,BufferedImage.TYPE_INT_BGR);
		try {
			Qrcode qrcode = new Qrcode();
			qrcode.setQrcodeErrorCorrect('M');
			qrcode.setQrcodeEncodeMode('B');
			qrcode.setQrcodeVersion(13);
			Graphics2D grap = bufferedImage.createGraphics();
			grap.setBackground(Color.white);
			grap.clearRect(0, 0, width, height);
			grap.setColor(Color.BLACK);
			
			//获取内容字节数组，设置编码集
			byte[] contentBytes = content.getBytes("UTF-8");
			boolean[][] codeOut = qrcode.calQrcode(contentBytes);
			
			for (int i = 0; i < codeOut.length; i++) {
				for (int j = 0; j < codeOut.length; j++) {
					if(codeOut[j][i]) {
						grap.fillRect(j*2, i*2, 2, 2);
					}
				}
			}
			
			grap.dispose();
			bufferedImage.flush();
			
			//生成二维码
			File imageFile = new File(imgPath);
			ImageIO.write(bufferedImage, "jpg", imageFile);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return bufferedImage;
	}
	

}
