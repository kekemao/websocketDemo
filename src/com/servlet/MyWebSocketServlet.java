package com.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;   
import org.apache.catalina.websocket.WebSocketServlet;
import com.ws.*;

public class MyWebSocketServlet extends WebSocketServlet {  
	


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected StreamInbound createWebSocketInbound(String arg0, HttpServletRequest request) {
		// TODO Auto-generated method stub
		String ip = request.getRemoteAddr().toString();
		String port = ((Integer)request.getServerPort()).toString();
		System.out.println("连接成功，ip地址为："+ip);
		System.out.println("request.getServerPort()："+request.getServerPort());
		System.out.println("request.getServerName()："+request.getServerName());
		System.out.println("request.getRequestURL()："+request.getRequestURL());
		System.out.println("request.getRemotePort()："+request.getRemotePort());
		System.out.println("request.getProtocol()："+request.getProtocol());
		
		try {
			request.getReader();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new MyMessageInbound(ip,port);
	}

}




















