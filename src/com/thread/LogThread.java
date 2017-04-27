package com.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.HashMap;

import com.utils.GetLogPathUtil;
import com.utils.MapUtil;
import com.ws.MyMessageInbound;

/**
 * 日志线程
 * 
 * */
public class LogThread implements Runnable {

	private String logName;// 日志名 每个日志一个线程

	private int logNo = 0;// 日志行号

	private BufferedReader reader = null;

	private HashMap<String, MyMessageInbound> logMap = null;

	private int line = 0;

	private boolean onFlag = true;//线程开关

	private boolean initFlag = true;//初始化标记

	public LogThread(String logName) {
		this.logName = logName;
	}

	@Override
	public void run() {
		logMap = MapUtil.allLogMap.get(logName);
		//
		if (logMap != null && logMap.size() > 0) {
			readFileByLines(line, logMap, reader,  logName, onFlag,initFlag);
		}

	}

	public static void readFileByLines(int line,
			HashMap<String, MyMessageInbound> logMap, BufferedReader reader,
			 String logName, boolean onFlag,boolean initFlag) {
		String filePath = GetLogPathUtil.getLogPath(logName);
		File file = new File(filePath);
		int nullNo = 0;
		try {
			reader = new BufferedReader(new FileReader(file));

			// 一次读入一行，直到读入null为文件结束
			String temp = null;
			String tmpStr = null;
			while (onFlag) {
				//线程第一次启动，将文件读到最新一行
				if (initFlag) {
					while ((temp = reader.readLine()) != null) {
						line++;
						tmpStr = temp;
					}
				}

				if ((((temp = reader.readLine()) != null) && !"".equals(temp))
						|| initFlag) {
					if (initFlag) {
						temp = tmpStr;
						initFlag = false;
					}
					CharBuffer msgCb = CharBuffer.wrap(line + ":" + temp);
					// 重新获取集合
					logMap = MapUtil.allLogMap.get(logName);
					if (logMap != null && logMap.size() > 0) {
						// 遍历集合
						for (String k : logMap.keySet()) {
							// 判断初始标记
							if (logMap.get(k).isFirstFlag()) {
								// 设置起始行号
								logMap.get(k).setLineNo(line);
								logMap.get(k).setFirstFlag(false);
							}
							logMap.get(k).getWsOutbound().writeTextMessage(msgCb);
						}
					}
					// 置空为空次数
					nullNo = 0;
					line++;
				} else {
					if (nullNo < 5) {
						nullNo++;
					}
				}
//				System.out.println(nullNo);
				Thread.sleep(50 * nullNo);
				if(logMap.size()<1){
					MapUtil.allLogMap.remove(logName);
					onFlag = false;
				}
			}
			// reader.close();
		} catch (FileNotFoundException e0) {
			e0.printStackTrace();
			error("日志文件不存在!", logName, logMap);
		} catch (IOException e) {
			error("I/O异常，连接中断", logName, logMap);
			e.printStackTrace();
		} catch (InterruptedException e2) {
			error("未知错误，连接中断", logName, logMap);
			e2.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	// 异常处理
	public static void error(String msg, String logName,
			HashMap<String, MyMessageInbound> logMap) {
		String msgCd = "-----------" + msg + "-----------";
		CharBuffer err = CharBuffer.wrap(msgCd);
		logMap = MapUtil.allLogMap.get(logName);
		try {
			if (logMap != null && logMap.size() > 0) {
				// 遍历集合
				for (String k : logMap.keySet()) {
					logMap.get(k).getWsOutbound().writeTextMessage(err);
					logMap.get(k).onClose(1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isOnFlag() {
		return onFlag;
	}

	public void setOnFlag(boolean onFlag) {
		this.onFlag = onFlag;
	}

}
