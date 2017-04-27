package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
/**
 * ���룺��־������ʼ�кţ������к�
 * �������־����
 * 
 * */
public class GetTxtUtils {
	public static StringBuffer getLog(String fileName, int startNo, int endNo) throws Exception {
		StringBuffer logBuffer = new StringBuffer();
		String filePath = GetLogPathUtil.getLogPath(fileName);
		File file = new File(filePath);
		BufferedReader reader = null;
		int line = 0;

		reader = new BufferedReader(new FileReader(file));
		String temp = null;
		while ((temp = reader.readLine()) != null) {
			line++;
			if (line >= startNo && line <= endNo) {
				logBuffer.append(line + ":" + temp + "<br/>");
			}
		}

		return logBuffer;
	}
}
