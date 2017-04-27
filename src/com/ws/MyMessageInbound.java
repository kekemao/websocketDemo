package com.ws;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.HashMap;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.thread.LogThread;
import com.utils.GetTxtUtils;
import com.utils.MapUtil;

public class MyMessageInbound extends MessageInbound implements Runnable {

	private String ip = "";
	private String port = "";
	private String key = "";			//IP+PORT
	private boolean firstFlag = true;	//初始连接标志
	private int lineNo = 0;				//起始行号
	private String logName = "";		//当前连接日志名

	public MyMessageInbound(String ip, String port) {
		this.ip = ip;
		this.port = port;
	}

	@Override
	protected void onOpen(WsOutbound outbound) {
		key = this.ip + this.port;
		//MapUtil.map.put(key, this);
		//System.out.println(MapUtil.map.size());
		CharBuffer buffer = CharBuffer.wrap("----连接成功----");

		try {
			this.getWsOutbound().writeTextMessage(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onTextMessage(CharBuffer msg) throws IOException {
		this.firstFlag = true;
		String message = msg.toString();
		System.out.println("message="+message);
		// 判断协议
		if (message.contains("to")) {
			String[] logStr = message.split("to");
			System.out.println(logStr[1]);
			if ("".equals(logStr[0]) || logStr[0] == null) {
				HashMap<String, MyMessageInbound> lMap = MapUtil.allLogMap
						.get(logStr[1]);
				if (lMap == null) {
					MapUtil.allLogMap.put(logStr[1],
							new HashMap<String, MyMessageInbound>());
					MapUtil.allLogMap.get(logStr[1]).put(key, this);
					LogThread logThread = new LogThread(logStr[1]);
					Thread t = new Thread(logThread);
					MapUtil.threadMap.put(logStr[1], logThread);
					t.start();
				}else {
					MapUtil.allLogMap.get(logStr[1]).put(key, this);
				}
			} else {
				HashMap<String, MyMessageInbound> lMap = MapUtil.allLogMap
						.get(logStr[0]);
				if (lMap != null) {
					lMap.remove(key);
				}
				lMap = MapUtil.allLogMap.get(logStr[1]);
				if (lMap == null) {
					MapUtil.allLogMap.put(logStr[1],
					new HashMap<String, MyMessageInbound>());
					MapUtil.allLogMap.get(logStr[1]).put(key, this);
					LogThread logThread = new LogThread(logStr[1]);
					Thread t = new Thread(logThread);
					MapUtil.threadMap.put(logStr[1], logThread);
					t.start();

				} else {
					MapUtil.allLogMap.get(logStr[1]).put(key, this);
				}

			}
			this.setLogName(logStr[1]);// 设置当前日志名
		}
		if(message.contains("%%")){
			try {
				String toLine = message.split("%%")[1];
				if(toLine!=null && !"".equals(toLine)){
					int endNo = this.lineNo;
					int toNum = Integer.parseInt(toLine);
					int startNo = endNo - toNum;
					if(toNum < 0){
						startNo=0;
					}
					String fileName = this.getLogName();
					
					StringBuffer sf = GetTxtUtils.getLog(fileName, startNo, endNo);
					CharBuffer info = CharBuffer.wrap(sf);
					this.getWsOutbound().writeTextMessage(info);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onClose(int status) {
		if (this.getLogName() != null && !"".equals(this.getLogName())) {
			if (MapUtil.allLogMap.get(this.getLogName()) != null) {
				MapUtil.allLogMap.get(this.getLogName()).remove(this.key);
			}
			if (MapUtil.allLogMap.get(this.getLogName()) != null
					&& MapUtil.allLogMap.get(this.getLogName()).size() < 1) {
				MapUtil.allLogMap.remove(this.getLogName());// 移除连接集合
				if (MapUtil.threadMap.get(this.getLogName()) != null) {
					MapUtil.threadMap.get(this.getLogName()).setOnFlag(false);// 关闭线程
				}
				MapUtil.threadMap.remove(this.getLogName());// 移除线程集合
			} else if (MapUtil.allLogMap.get(this.getLogName()) == null) {
				if (MapUtil.threadMap.get(this.getLogName()) != null) {
					MapUtil.threadMap.get(this.getLogName()).setOnFlag(false);// 关闭线程
				}
				MapUtil.threadMap.remove(this.getLogName());// 移除线程集合
			}
		}
		super.onClose(status);
	}

	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public boolean isFirstFlag() {
		return firstFlag;
	}

	public void setFirstFlag(boolean firstFlag) {
		this.firstFlag = firstFlag;
	}

	public int getLineNo() {
		return lineNo;
	}

	public void setLineNo(int lineNo) {
		this.lineNo = lineNo;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

}
