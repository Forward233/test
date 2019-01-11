package com.test.hello;

import com.opslab.util.CharsetUtil;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println(Runtime.getRuntime().availableProcessors());
		System.out.println("a".hashCode());
		String string = "s";
		String gbk = CharsetUtil.GBK;
		
	}
}
