package com.utils;

import java.util.HashMap;

import com.thread.LogThread;
import com.ws.MyMessageInbound;

public class MapUtil {
	//public static HashMap<String, MyMessageInbound> map = new HashMap<String, MyMessageInbound>();
	public static HashMap<String,HashMap<String,MyMessageInbound>> allLogMap = new HashMap<String,HashMap<String,MyMessageInbound>>();
	public static HashMap<String,LogThread> threadMap = new HashMap<String,LogThread>();
}
