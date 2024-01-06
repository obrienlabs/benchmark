package org.obrienscience.collatz.server;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;

/**
 * @author fmichaelobrien@google.com
 */
public class ForkJoinCollatzServerStatic8 extends ForkJoinCollatzServer {

	@Override
	public void compute(long pool, long threads, long uowSplit, long extent) {
		BigInteger maxPath = BigInteger.ONE;
		BigInteger maxValue = BigInteger.ONE;
		ForkJoinUnitOfWork forkJoinUOW = new ForkJoinUnitOfWork(uowSplit, 0, extent, maxPath, maxValue);
		long startTime = System.currentTimeMillis();
		// create a pool of threads to the power of pool * # of (proc + ht)
		mapReducePool = new ForkJoinPool(1 << pool);//Runtime.getRuntime().availableProcessors() << pool);
		mapReducePool.invoke(forkJoinUOW);
		long endTime = System.currentTimeMillis();
		System.out.println(new StringBuffer(
				String.valueOf(endTime - startTime)).append(",")
				.append(pool).append(",")
				.append(threads).append(",")
				.append(String.valueOf((endTime - startTime) / 1000))
				.append(threads).append(",").append(uowSplit).toString());
	}
    
	public static void main(String[] args) {
	    System.out.println("ForkJoinUnitOfWork8 (v 20240101)");
		long poolStart = 1;
		long poolEnd = 1;
		long runs = 1;
		/*
	    if(null != args && args.length > 0) {
        	poolStart = Long.parseLong(args[0]);
        }   
	    if(null != args && args.length > 1) {
        	poolEnd = Long.parseLong(args[1]);
        }   
	    if(null != args && args.length > 2) {
        	runs = Long.parseLong(args[2]);
        } */
		System.out.println("availableProc\t: " + Runtime.getRuntime().availableProcessors());
		//System.out.println("fjps threads\t: " + poolStart + "," + poolEnd);
		System.out.println("freeMemory()\t: " + Runtime.getRuntime().freeMemory());
		System.out.println("maxMemory()\t: " + Runtime.getRuntime().maxMemory());
		System.out.println("totalMemory()\t: " + Runtime.getRuntime().totalMemory());
		System.out.println("System.getEnv()\t: " + System.getenv().toString());
		ForkJoinCollatzServerStatic8 server = new ForkJoinCollatzServerStatic8();
		long extent = 4;//25;
		 

		System.out.println("Range: bits\t: " + extent);
		//for(long r=0;r<runs;r++) {
			//for(long p=poolStart;p<poolEnd + 1;p++) {
				//for(long i=3;i<extent + 1;i++) {
					server.compute(3, 3, 3, 3);//poolStart, extent - i, 1 << i, 1 << extent);
			
				//}
			//}
		//}
	}
}
