package org.obrienscience.collatz.model;

import java.math.BigInteger;

/**
 * 20221023: revisit Collatz sequences - software and hardware
 * Base entity + logic
 * @author fmichaelobrien@google.com
 *
 */
public class Collatz {

    public static final BigInteger COLLATZ88 = BigInteger.valueOf(1980976057694848447L);     
    public static final BigInteger COLLATZ_2651 = new  BigInteger("2367363789863971985761"); // path of 2651
    private static final BigInteger BIG_INTEGER_TWO = BigInteger.valueOf(2);
	
	private BigInteger start;
	private BigInteger path = BigInteger.ZERO;
	private BigInteger height = BigInteger.ZERO;
	private boolean displayIterations = false;
	
	public Collatz(long aStart) {
		start = BigInteger.valueOf(aStart);
	}

	public Collatz(BigInteger aStart) {
		start = aStart;
	}
	
	public Collatz(long aStart, boolean aDisplayIterations) {
		start = BigInteger.valueOf(aStart);
		displayIterations = aDisplayIterations;
	}

	public Collatz(BigInteger aStart, boolean aDisplayIterations) {
		start = aStart;
		displayIterations = aDisplayIterations;
	}
	public void compute() {
		computeSingleThreaded();
	}


	
	/**
	 * Single threaded compute:
	 * We combine the odd/even operations (shift left*1/right*n)
	 * Odd integers are 
	 * 1 - multiplied by 3 with a carry = 3n+1 or shift left + current + 1
	 * 2 - divided by 2 or shift right until we have an odd number again
	 * We stop when we reach the 4/2/1 loop for positive integers
	 * 
	 */
	private void computeSingleThreaded() {
		BigInteger current = start;
		long startTime = System.currentTimeMillis();
		while (current.compareTo(BigInteger.ONE) > 0) {
			// odd integers follow
			if(current.testBit(0)) {
				current = current.shiftLeft(1).add(current).add(BigInteger.ONE);
			} else {
				current = current.shiftRight(1);
			}
			path = path.add(BigInteger.ONE);
			if(current.compareTo(height) > 0) {
				height = current;
			}
			if(displayIterations) {
				System.out.println(start + "," + current + "," + path + "," + height);
			}
		}
		long time = System.currentTimeMillis() - startTime;
		System.out.println("start,path,max,ms");
		System.out.println(start + "," + path + "," + height + "," + time);
	}

	
	public static void main(String[] args) {

		BigInteger aStart = BigInteger.valueOf(27);
		boolean aDisplayIterations = false;
		System.out.println("collatz start displayIterations(true/false)");
	    if(null != args && args.length > 0) {
        	aStart = new BigInteger(args[0]);
        }   
	    if(null != args && args.length > 1) {
	    	aDisplayIterations = Boolean.parseBoolean(args[1]);
        }  
		Collatz collatz = new Collatz(aStart, aDisplayIterations);
		collatz.compute();

	}

}
