package com.bb.dd.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.bb.dd.BikeApplication;


public class FileUtil {
	/**
	 * 创建文件夹
	 * @param path:目录
	 */
	public static void createDir(String path) {
		File dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	/**
	 * 验证文件是否存在
	 * @param path
	 * @return
	 */
	public static boolean isValidateFile(String path){
		File f = new File(path);
		return f.exists();
	}
	
	/**
	 * 删除文件
	 * @param path目录
	 * @param filename文件名
	 */
	public static void delFile(String path, String filename) {
		File file = new File(path + "/" + filename);
		if (file.exists() && file.isFile())
			file.delete();
	}

	public static void copyFile(String src, String destPath,String destName) {
		InputStream myInput = null;
		FileOutputStream fos = null;
		try {
			myInput = new FileInputStream(new File(src));
			File file = new File(destPath);
			if(!file.exists())file.mkdir();
			fos = new FileOutputStream(new File(file, destName));
			byte[] buffer = new byte[1024 * 5];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			fos.flush();
			fos.close();
			myInput.close();
		} catch (IOException e) {
			try {
				if (null != myInput ) myInput.close();
				if(null != fos)fos.close();
			} catch (IOException e1) {
			}
		} finally {
			try {
				if (null != myInput ) myInput.close();
				if(null != fos)fos.close();
			} catch (IOException e1) {
			}
		}
	}
	
	public static void copyFile4SD(String src, String destPath,String destName) {
		InputStream myInput = null;
		FileOutputStream fos = null;
		try {
			myInput = BikeApplication.getInstance().getAssets().open(src);
			File file = new File(destPath);
			if(!file.exists())file.mkdir();
			fos = new FileOutputStream(new File(file, destName));
			byte[] buffer = new byte[1024 * 5];
			int length;
			while ((length = myInput.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			fos.flush();
			fos.close();
			myInput.close();
		} catch (IOException e) {
			try {
				if (null != myInput ) myInput.close();
				if(null != fos)fos.close();
			} catch (IOException e1) {
			}
		} finally {
			try {
				if (null != myInput ) myInput.close();
				if(null != fos)fos.close();
			} catch (IOException e1) {
			}
		}
	}
}
