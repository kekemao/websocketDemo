package com.utils;

import java.util.Calendar;

public class GetLogPathUtil {
	// 获取当前日志path
	public static String getLogPath(String logName) {
		String logPath = null;
		try {
			int dateNum = Calendar.DAY_OF_MONTH;
			String logType = logName.substring(logName.lastIndexOf(".") + 1);
			if(!"trc".equalsIgnoreCase(logType) && !"log".equalsIgnoreCase(logType)&& !" ".equalsIgnoreCase(logType)){
				logType ="trc";
				logPath = "C:/" + logType + "/" + dateNum + "/" + logName+".lst";
			}else{
				logPath = "C:/" + logType + "/" + dateNum + "/" + logName;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logPath;
	}
}


