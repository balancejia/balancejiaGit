package com.bb.dd.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;

import android.content.Context;

/**
 * @author HuangF
 * @version 创建时间：2012-11-1 上午9:28:16 类说明 异常处理
 */
public class BadHandler implements UncaughtExceptionHandler {

	// 是否开启Debug
	public static final boolean DEBUG = true;
	// 默认的UncaughtExceptionHandler处理类
	@SuppressWarnings("unused")
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// 实例
	private static BadHandler INSTANCE;
	@SuppressWarnings("unused")
	private Context context;

	private BadHandler() {
	}

	public static BadHandler getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new BadHandler();
		}
		return INSTANCE;
	}

	public void init(Context ctx) {
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
		this.context = ctx;
	}

	public void uncaughtException(Thread thread, Throwable ex) {
		writerLog(ex);
	}
	/**
	 * 写入SD卡中
	 * @param ex
	 */
	private void writerLog(Throwable ex) {
		if (ex == null) {
			return;
		}
		File errorDir=new File(CommonVariable.SD_ERROR_PATH);
		if(!errorDir.exists())
			errorDir.mkdirs();
		File file = new File(errorDir, "bad_exception.txt");
		PrintStream ps;
		try {
			FileOutputStream fos = new FileOutputStream(file, true);
			ps = new PrintStream(fos);
			ps.append(getDriverInfo());
			System.setErr(ps);
			ex.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 获取时间和手机信息
	private StringBuilder getDriverInfo() {
		StringBuilder sb = new StringBuilder();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日   HH:mm:ss ");
		sb.append("日期:");
		sb.append(formatter.format(System.currentTimeMillis()));
		sb.append("\n 手机型号:");
		sb.append(android.os.Build.MODEL);
		sb.append("\n 手机系统版本:");
		sb.append(android.os.Build.VERSION.RELEASE);
		sb.append("\n");
		return sb;
	}

}
