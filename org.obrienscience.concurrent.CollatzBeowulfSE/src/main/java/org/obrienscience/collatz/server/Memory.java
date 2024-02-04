package org.obrienscience.collatz.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

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
	
	
	
	
	public Optional<String> random(String input) {
		List<String> strings = Collections.list(new StringTokenizer(input, ",")).stream()
	      .map(token -> (String) token)
	      .collect(Collectors.toList());
		return Optional.of(strings.get((int)(Math.random() * strings.size())));
	}
	

// print first 10 random numbers
	



	public static void main(String[] args) {
		Memory memory = new Memory();
		//memory.forceOutOfMemoryError();
		String input = "first,second,third,forth,fifth";
		System.out.println(memory.random(input).get());
	}

}
