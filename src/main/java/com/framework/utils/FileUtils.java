package com.framework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.framework.AppConstants;

public class FileUtils {
	public static Logger log = LoggerFactory.getLogger(FileUtils.class);

	public final static long ONE_KB = 1024;
	public final static long ONE_MB = ONE_KB * 1024;
	public final static long ONE_GB = ONE_MB * 1024;
	public final static long ONE_TB = ONE_GB * (long) 1024;
	public final static long ONE_PB = ONE_TB * (long) 1024;

	/**
	 *
	 * 得到文件大小。
	 *
	 * @param fileSize
	 * @return
	 */
	public static String getHumanReadableFileSize(long fileSize) {
		if (fileSize < 0) {
			return String.valueOf(fileSize);
		}
		String result = getHumanReadableFileSize(fileSize, ONE_PB, "PB");
		if (result != null) {
			return result;
		}

		result = getHumanReadableFileSize(fileSize, ONE_TB, "TB");
		if (result != null) {
			return result;
		}
		result = getHumanReadableFileSize(fileSize, ONE_GB, "GB");
		if (result != null) {
			return result;
		}
		result = getHumanReadableFileSize(fileSize, ONE_MB, "MB");
		if (result != null) {
			return result;
		}
		result = getHumanReadableFileSize(fileSize, ONE_KB, "KB");
		if (result != null) {
			return result;
		}
		return String.valueOf(fileSize) + "B";
	}

	private static String getHumanReadableFileSize(long fileSize, long unit, String unitName) {
		if (fileSize == 0)
			return "0";

		if (fileSize / unit >= 1) {
			double value = fileSize / (double) unit;
			DecimalFormat df = new DecimalFormat("######.##" + unitName);
			return df.format(value);
		}
		return null;
	}

	public static String getFileExt(String fileName) {
		if (fileName == null)
			return "";

		String ext = "";
		int lastIndex = fileName.lastIndexOf(".");
		if (lastIndex >= 0) {
			ext = fileName.substring(lastIndex + 1).toLowerCase();
		}

		return ext;
	}

	/**
	 * 得到不包含后缀的文件名字。
	 *
	 * @return
	 */
	public static String getRealName(String name) {
		if (name == null) {
			return "";
		}

		int endIndex = name.lastIndexOf(".");
		if (endIndex == -1) {
			return null;
		}
		return name.substring(0, endIndex);
	}

	/**
	 *
	 * 使用文件通道的方式复制文件
	 *
	 * @param s源文件
	 * @param t复制到的新文件
	 */

	public static boolean fileChannelCopy(File s, File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
	/**
	 * 获取文件目录
	 * @param moduleType 业务模块(在AppConstants取常量)
	 * @param fileType 文件类型 (在AppConstants取常量)
	 * @return
	 */
	public static String getFileDirByCondition(int moduleType,int fileType)
	{
		if(AppConstants.UPLOAD_MODULE_TYPE_CHAT==moduleType)//离线附件
		{
			return "chat";
		}
		else if(AppConstants.UPLOAD_FILE_TYPE_VOICE==fileType)
		{
			return "voice";
		}
		else if(AppConstants.UPLOAD_FILE_TYPE_IMAGE==fileType)
		{
			if(AppConstants.UPLOAD_MODULE_TYPE_USER==moduleType)//用户图片
			{
				return "images/user";
			}
			else if(AppConstants.UPLOAD_MODULE_TYPE_BUSINESSTRIP==moduleType)//商旅行程
			{
				return "images/businesstrip";
			}
			else if(AppConstants.UPLOAD_MODULE_TYPE_PRODUCT==moduleType)//产品信息
			{
				return "images/product";
			}
			else if(AppConstants.UPLOAD_MODULE_TYPE_SERVICE==moduleType)//服务信息
			{
				return "images/service";
			}
		}
		return "temp";
	}
	
	/**
	 * 获取文件访问路径
	 * @param request
	 * @param moduleType
	 * @param fileType
	 * @param fileName
	 * @return
	 */
	public static String getFileAccessPathByCondition(HttpServletRequest request,int moduleType,int fileType,String fileName)
	{
		if(fileName==null||"".equals(fileName.trim()))
		{
			return "";
		}
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+ request.getServerPort()+ request.getContextPath()+"/";
		String fileAccessPath=basePath +AppConstants.UPLOAD_TEMP_PATH  + File.separator
		+ getFileDirByCondition(moduleType,fileType)+ File.separator+fileName;
		return fileAccessPath;
	}
}
