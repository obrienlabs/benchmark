package org.obrienscience.collatz.server;

import java.math.BigInteger;
import java.util.concurrent.RecursiveAction;


/**
 * Colatz or Hailstone numbers - proof of concept for the ForkJoin framework in Java 7 from Oracle/SUN
 * 
 * @author Michael.f.obrien@eclipselink.org
 *
 */
public class ForkJoinUnitOfWork extends RecursiveAction {
	private static final long serialVersionUID = 2854227036188716499L;
    public static final BigInteger COLLATZ88 = BigInteger.valueOf(1980976057694848447L);     
    public static final BigInteger COLLATZ_2651 = new  BigInteger("2367363789863971985761"); // path of 2651
    
	private long start;
	private long len;
	private long uowSplit = 1024;
	private BigInteger maximumPath;
	private BigInteger maximumValue;
	
	public ForkJoinUnitOfWork(long split, long start, long len, BigInteger maxPath, BigInteger maxValue) {
		this.uowSplit = split;
		this.start = start;
		this.len = len;
		maximumPath = maxPath;
		maximumValue = maxValue;
	}	

	protected void computeNoFork() {
		BigInteger current = BigInteger.valueOf(start);
		BigInteger stop = BigInteger.valueOf(start + len);
		BigInteger maxValue = stop;
		BigInteger maxPath = BigInteger.ONE;
		long path = 0;
		boolean newMax = false;
		while(current.compareTo(stop) != 0) {
			BigInteger prev = current;
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
			current = current.add(BigInteger.valueOf(2));			
		}	
		if(maxValue.compareTo(maximumValue) > 0) {			
			maximumValue = maxValue;
			newMax = true;
		}
		
		if(maxPath.compareTo(maximumPath) > 0) {			
			maximumPath = maxPath;		
			newMax = true;
		}
		if(newMax) {
			//System.out.println("S: " + start + " N: " + current + " M: " + maxValue + " P: " + maxPath);
		}
	}
	
    @Override
	protected void compute() {
    	// base case
	    if (len <= uowSplit) {
	        computeNoFork();
			//System.out.println("S: " + start + " N: " + current + " M: " + maxValue + " P: " + maxPath);

	        return;
	    }
	    
	    long split = len / 2;
	    // recursive case
	    invokeAll(new ForkJoinUnitOfWork(uowSplit, start, split, maximumPath, maximumValue),
	              new ForkJoinUnitOfWork(uowSplit, start + split, len - split, maximumPath, maximumValue)
	    );		
	}

	public BigInteger getMaximumPath() {		return maximumPath;	}
	public BigInteger getMaximumValue() {		return maximumValue;	}
	public long getUowSplit() {		return uowSplit;	}
	public void setUowSplit(long uowSplit) {		this.uowSplit = uowSplit;	}
}
