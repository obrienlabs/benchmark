package org.obrienscience.collatz.server;

import java.util.ArrayList;
import java.util.List;

// 20160730
public class Memory {

	private List<List<String>> stringList = new ArrayList<>();
	
	// force an out of memory error to test -Xmx limits - inherently parallel at the JVM level
	public void forceOutOfMemoryError() {
		long count = 0;
		for(;;) {
			List<String> strings = new ArrayList<>();
			count++;
			System.out.println(count + " : " + Runtime.getRuntime().freeMemory());
			for(int i=0;i<1024;i++) {
				strings.add(String.valueOf(i));
			}
			stringList.add(strings);
		}

	}
	
	
	public static void main(String[] args) {
		Memory memory = new Memory();
		memory.forceOutOfMemoryError();
	}

}
