package com.test;

import java.util.HashMap;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HashMap m1 = new HashMap();
		m1.put("a", new Object());
		HashMap m2 = new HashMap();
		m2.put("a", m1.get("a"));
		
		System.out.println("m1-a:"+m1.get("a"));
		System.out.println("m2-a:"+m2.get("a"));
		m1.remove("a");
		System.out.println("m2-a:"+m2.get("a"));
		System.out.println("m1-a:"+m1.get("a"));
		HashMap m3 = new HashMap();
		m3.put("a", new Object());
		System.out.println(m3);
		
		m2 = m3;
		System.out.println(m2);
		if(m1==null)System.out.println(1);
		else{System.out.println(2);} 
		
		m1.clear();
		if(m1==null)System.out.println(1);
		else{System.out.println(2);}
	}

}
