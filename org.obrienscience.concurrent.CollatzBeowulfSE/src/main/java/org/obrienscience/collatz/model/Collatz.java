package org.obrienscience.collatz.model;

import java.math.BigInteger;

/**
 * 20221023: revisit Collatz sequences - software and hardware
 * Base entity + logic
 * java -cp target/classes org.obrienscience.collatz.model.Collatz 24 32 false 665 4799996945368
 *  
 * 
 * @author fmichaelobrien@google.com
 *
 */
public class Collatz {

    public static final BigInteger COLLATZ88 = BigInteger.valueOf(1980976057694848447L); 
    // path of 2651, height 6853114284254907107011432 or 2^
    public static final BigInteger COLLATZ_2651 = new  BigInteger("2367363789863971985761"); 
    private static final BigInteger BIG_INTEGER_TWO = BigInteger.valueOf(2);
	
	private BigInteger start;
	private BigInteger end;
	private BigInteger path = BigInteger.ZERO;
	private BigInteger height = BigInteger.ZERO;
	private boolean displayIterations = false;
	private BigInteger prevMaxPath = BigInteger.ZERO;
	private BigInteger prevMaxValue = BigInteger.ZERO;
	
	
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
	
	public Collatz(BigInteger aStart, BigInteger aEnd, boolean aDisplayIterations) {
		start = aStart;
		end = aEnd;
		displayIterations = aDisplayIterations;
	}
	
	public Collatz(int aStart, int aEnd, boolean aDisplayIterations) {
		start = BigInteger.valueOf(2).pow(aStart);
		end = BigInteger.valueOf(2).pow(aEnd);
		displayIterations = aDisplayIterations;
	}
	
	public Collatz(int aStart, int aEnd, boolean aDisplayIterations, BigInteger maxPath, BigInteger maxValue) {
		start = BigInteger.valueOf(2).pow(aStart);
		end = BigInteger.valueOf(2).pow(aEnd);
		displayIterations = aDisplayIterations;
		prevMaxPath = maxPath;
		prevMaxValue = maxValue;
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

	
	public void computeRange() {
		BigInteger maxValue = start;
		BigInteger maxPath = BigInteger.ONE;
		BigInteger gmaxValue = prevMaxValue;
		BigInteger gmaxPath = prevMaxPath;
		long lastMaxTime = System.currentTimeMillis();	
		long totalStartTime = lastMaxTime;
		
		long path = 0;
		boolean newMax = false;
		BigInteger current = start;
		while(current.compareTo(end) != 0) {
			BigInteger prev = current;
			path = 0;
			maxValue = BigInteger.ZERO;
			maxPath = BigInteger.ZERO;
			newMax = false;
			
			while (prev.compareTo(BigInteger.ONE) > 0) {
				if(prev.testBit(0)) {
					prev = prev.shiftLeft(1).add(prev).add(BigInteger.ONE);
				} else {
					prev = prev.shiftRight(1);
				}
				path++;
				if(prev.compareTo(maxValue) > 0) {
					maxValue = prev;
				}
			}
			if(path > maxPath.longValue()) {
				maxPath = BigInteger.valueOf(path);
			}			
		
			if(maxValue.compareTo(gmaxValue) > 0) {			
				gmaxValue = maxValue;
				newMax = true;
			}
		
			if(maxPath.compareTo(gmaxPath) > 0) {			
				gmaxPath = maxPath;		
				newMax = true;
			}
			if(newMax) {

				System.out.println("S: " + current + " M: " + maxValue + " P: " + maxPath 
						+ " T: " + (System.currentTimeMillis() - lastMaxTime));
				lastMaxTime = System.currentTimeMillis();
			}
			current = current.add(BIG_INTEGER_TWO);	
		}
		System.out.println("Total time: " + (System.currentTimeMillis() - totalStartTime));
	}
	
	
	
	public static void main(String[] args) {

		//BigInteger aStart = BigInteger.valueOf(27);
		//BigInteger aEnd = BigInteger.valueOf(27);
		int aStart = 2;
		int aEnd = 20;
		BigInteger maxPath = BigInteger.valueOf(1);
		BigInteger maxValue = BigInteger.valueOf(1);
		
		boolean aDisplayIterations = false;
		System.out.println("collatz (power of 2 exponent) start stop displayIterations(true/false) maxPath maxValue");
	    if(null != args && args.length > 0) {
        	aStart = Integer.parseInt(args[0]);// BigInteger(args[0]);
        }   
	    if(null != args && args.length > 1) {
        	aEnd = Integer.parseInt(args[1]);//new BigInteger(args[1]);
        }  
	    if(null != args && args.length > 2) {
	    	aDisplayIterations = Boolean.parseBoolean(args[1]);
        }  
	    if(null != args && args.length > 3) {
        	maxPath = new BigInteger(args[3]);
        }
	    if(null != args && args.length > 4) {
        	maxValue = new BigInteger(args[4]);
        }
	    System.out.println("p: " + maxPath + " v: " + maxValue);
	    //System.out.println(BigInteger.valueOf(2).pow(aStart));
		Collatz collatz = new Collatz(aStart, aEnd, aDisplayIterations, maxPath, maxValue);
		//collatz.compute();
		collatz.computeRange();//aStart, aEnd);

	}

}
