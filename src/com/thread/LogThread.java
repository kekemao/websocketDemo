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
 * ��־�߳�
 * 
 * */
public class LogThread implements Runnable {

	private String logName;// ��־�� ÿ����־һ���߳�

	private int logNo = 0;// ��־�к�

	private BufferedReader reader = null;

	private HashMap<String, MyMessageInbound> logMap = null;

	private int line = 0;

	private boolean onFlag = true;//�߳̿���

	private boolean initFlag = true;//��ʼ�����

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

			// һ�ζ���һ�У�ֱ������nullΪ�ļ�����
			String temp = null;
			String tmpStr = null;
			while (onFlag) {
				//�̵߳�һ�����������ļ���������һ��
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
					// ���»�ȡ����
					logMap = MapUtil.allLogMap.get(logName);
					if (logMap != null && logMap.size() > 0) {
						// ��������
						for (String k : logMap.keySet()) {
							// �жϳ�ʼ���
							if (logMap.get(k).isFirstFlag()) {
								// ������ʼ�к�
								logMap.get(k).setLineNo(line);
								logMap.get(k).setFirstFlag(false);
							}
							logMap.get(k).getWsOutbound().writeTextMessage(msgCb);
						}
					}
					// �ÿ�Ϊ�մ���
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
			error("��־�ļ�������!", logName, logMap);
		} catch (IOException e) {
			error("I/O�쳣�������ж�", logName, logMap);
			e.printStackTrace();
		} catch (InterruptedException e2) {
			error("δ֪���������ж�", logName, logMap);
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

	// �쳣����
	public static void error(String msg, String logName,
			HashMap<String, MyMessageInbound> logMap) {
		String msgCd = "-----------" + msg + "-----------";
		CharBuffer err = CharBuffer.wrap(msgCd);
		logMap = MapUtil.allLogMap.get(logName);
		try {
			if (logMap != null && logMap.size() > 0) {
				// ��������
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
