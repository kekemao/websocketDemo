package com.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TestReadLine {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		String fileName = "log/a/";
		File file = new File(fileName);
		String tempString = null;
		BufferedReader reader = null;
		int nullNo = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			int line = 0;
			// 一次读入一行，直到读入null为文件结束
			String temp = null;
			boolean initFlag = true;
			String tmpStr = null;
			while (true) {
				if (initFlag) {
					while ((temp = reader.readLine()) != null) {
						tmpStr = temp;
						line++;	
						System.out.println(line+"---->"+tmpStr);
					}
					
				}
				
				if ((((temp = reader.readLine()) != null) && !"".equals(temp))||initFlag) {
					if (initFlag) {
						temp = tmpStr;
						initFlag = false;
//						if("".equals(temp))continue;
					}
					
					System.out.println(line + ":" + temp);
					nullNo = 0;
					line++;
				}else{
					if(nullNo<5){
						nullNo++;
					}
				}
//				System.out.println(nullNo);
				Thread.sleep(1000*0);
			}
			// reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	public static void readFileByLines(String fileName) {
		File file = new File(fileName);
		String tempString = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			int line = 1;
			// 一次读入一行，直到读入null为文件结束
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				// tempString = temp;
				System.out.println(line + ":" + temp);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		// return tempString;
	}
}
