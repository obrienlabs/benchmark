package org.obrienscience.collatz.model;

import java.math.BigInteger;

/**
 * 20221023: revisit Collatz sequences - software and hardware
 * Base entity + logic
 * java -cp target/classes org.obrienscience.collatz.model.Collatz 24 32 false 665 4799996945368
 * S: 13263350 M: 60342610919632 P: 577 T: 18035
 * S: 16801022 M: 159424614880 P: 686 T: 11747 
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
		start = BigInteger.valueOf(2).pow(aStart).add(BigInteger.ONE);
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
		while(current.compareTo(end) < 0) {
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
	    
	    
	    // powers of 2
	    for(int i=0;i<129;i++) {
	    	System.out.println(i + "," + BigInteger.valueOf(2).pow(i));
	    }
	    
	    System.out.println("p: " + maxPath + " v: " + maxValue);
	    //System.out.println(BigInteger.valueOf(2).pow(aStart));
		Collatz collatz = new Collatz(aStart, aEnd, aDisplayIterations, maxPath, maxValue);
		//collatz.compute();
		collatz.computeRange();//aStart, aEnd);

	}

}

/**
 * java -cp target/classes org.obrienscience.collatz.model.Collatz 2 32 false 1 1 
 * m1max MBP 2021 16 inch
 * 
p: 1 v: 1
S: 5 M: 16 P: 5 T: 0
S: 7 M: 52 P: 16 T: 1
S: 9 M: 52 P: 19 T: 0
S: 15 M: 160 P: 17 T: 0
S: 19 M: 88 P: 20 T: 0
S: 25 M: 88 P: 23 T: 0
S: 27 M: 9232 P: 111 T: 0
S: 55 M: 9232 P: 112 T: 0
S: 73 M: 9232 P: 115 T: 1
S: 97 M: 9232 P: 118 T: 0
S: 129 M: 9232 P: 121 T: 0
S: 171 M: 9232 P: 124 T: 1
S: 231 M: 9232 P: 127 T: 0
S: 255 M: 13120 P: 47 T: 0
S: 313 M: 9232 P: 130 T: 1
S: 327 M: 9232 P: 143 T: 0
S: 447 M: 39364 P: 97 T: 0
S: 639 M: 41524 P: 131 T: 1
S: 649 M: 9232 P: 144 T: 0
S: 703 M: 250504 P: 170 T: 1
S: 871 M: 190996 P: 178 T: 1
S: 1161 M: 190996 P: 181 T: 1
S: 1819 M: 1276936 P: 161 T: 3
S: 2223 M: 250504 P: 182 T: 1
S: 2463 M: 250504 P: 208 T: 0
S: 2919 M: 250504 P: 216 T: 1
S: 3711 M: 481624 P: 237 T: 2
S: 4255 M: 6810136 P: 201 T: 1
S: 4591 M: 8153620 P: 170 T: 0
S: 6171 M: 975400 P: 261 T: 5
S: 9663 M: 27114424 P: 184 T: 7
S: 10971 M: 975400 P: 267 T: 3
S: 13255 M: 497176 P: 275 T: 5
S: 17647 M: 11003416 P: 278 T: 8
S: 20895 M: 50143264 P: 255 T: 5
S: 23529 M: 11003416 P: 281 T: 6
S: 26623 M: 106358020 P: 307 T: 6
S: 31911 M: 121012864 P: 160 T: 11
S: 34239 M: 18976192 P: 310 T: 4
S: 35655 M: 41163712 P: 323 T: 3
S: 52527 M: 106358020 P: 339 T: 26
S: 60975 M: 593279152 P: 334 T: 12
S: 77031 M: 21933016 P: 350 T: 24
S: 77671 M: 1570824736 P: 231 T: 2
S: 106239 M: 104674192 P: 353 T: 43
S: 113383 M: 2482111348 P: 247 T: 11
S: 138367 M: 2798323360 P: 162 T: 41
S: 142587 M: 593279152 P: 374 T: 7
S: 156159 M: 41163712 P: 382 T: 21
S: 159487 M: 17202377752 P: 183 T: 5
S: 216367 M: 11843332 P: 385 T: 116
S: 230631 M: 76778008 P: 442 T: 21
S: 270271 M: 24648077896 P: 406 T: 60
S: 410011 M: 76778008 P: 448 T: 226
S: 511935 M: 76778008 P: 469 T: 162
S: 626331 M: 7222283188 P: 508 T: 184
S: 665215 M: 52483285312 P: 441 T: 63
S: 704511 M: 56991483520 P: 242 T: 64
S: 837799 M: 2974984576 P: 524 T: 221
S: 1042431 M: 90239155648 P: 439 T: 325
S: 1117065 M: 2974984576 P: 527 T: 125
S: 1212415 M: 139646736808 P: 328 T: 156
S: 1441407 M: 151629574372 P: 367 T: 377
S: 1501353 M: 90239155648 P: 530 T: 98
S: 1723519 M: 46571871940 P: 556 T: 357
S: 1875711 M: 155904349696 P: 370 T: 261
S: 1988859 M: 156914378224 P: 427 T: 194
S: 2298025 M: 46571871940 P: 559 T: 507
S: 2643183 M: 190459818484 P: 430 T: 575
S: 2684647 M: 352617812944 P: 399 T: 67
S: 3041127 M: 622717901620 P: 363 T: 613
S: 3064033 M: 46571871940 P: 562 T: 40
S: 3542887 M: 294475592320 P: 583 T: 856
S: 3732423 M: 294475592320 P: 596 T: 345
S: 3873535 M: 858555169576 P: 322 T: 248
S: 4637979 M: 1318802294932 P: 573 T: 1312
S: 5649499 M: 1017886660 P: 612 T: 1760
S: 5656191 M: 2412493616608 P: 400 T: 11
S: 6416623 M: 4799996945368 P: 483 T: 1352
S: 6631675 M: 60342610919632 P: 576 T: 400
S: 6649279 M: 15208728208 P: 664 T: 30
S: 8400511 M: 159424614880 P: 685 T: 3180
S: 11200681 M: 159424614880 P: 688 T: 5109
S: 14934241 M: 159424614880 P: 691 T: 6999
S: 15733191 M: 159424614880 P: 704 T: 1623
S: 19638399 M: 306296925203752 P: 606 T: 7603
S: 31466383 M: 159424614880 P: 705 T: 23013
S: 36791535 M: 159424614880 P: 744 T: 10495
S: 38595583 M: 474637698851092 P: 483 T: 3562
S: 63728127 M: 966616035460 P: 949 T: 50680
S: 80049391 M: 2185143829170100 P: 572 T: 33733
S: 120080895 M: 3277901576118580 P: 438 T: 83334
S: 127456255 M: 966616035460 P: 950 T: 15430
S: 169941673 M: 966616035460 P: 953 T: 89553
S: 210964383 M: 6404797161121264 P: 475 T: 88512
S: 226588897 M: 966616035460 P: 956 T: 34301
S: 268549803 M: 966616035460 P: 964 T: 91938
S: 319804831 M: 1414236446719942480 P: 592 T: 112118
S: 537099607 M: 966616035460 P: 965 T: 481369
S: 670617279 M: 966616035460 P: 986 T: 301577
S: 1341234559 M: 966616035460 P: 987 T: 1545703
S: 1410123943 M: 7125885122794452160 P: 770 T: 160822
S: 1412987847 M: 966616035460 P: 1000 T: 6607
S: 1674652263 M: 966616035460 P: 1008 T: 609045
S: 2610744987 M: 966616035460 P: 1050 T: 2211502
S: 4578853915 M: 966616035460 P: 1087 T: 4792861
S: 4890328815 M: 319497287463520 P: 1131 T: 761004
S: 8528817511 M: 18144594937356598024 P: 726 T: 9245374
S: 9780657631 M: 319497287463520 P: 1132 T: 3180970
past 2^32
32-33
S: 17316457353 M: 20722398914405051728 P: 559 T: 323442
S: 17387835787 M: 319497287463520 P: 1138 T: 161742
S: 17828259369 M: 319497287463520 P: 1213 T: 998919


S: 80049391 M: 2185143829170100 P: 572 T: 29476 21 quadrillion
1 trillion = 2^40 1099511627776


m1max

S: 11371756681 M: 18144594937356598024 P: 729 T: 10632081
S: 12212032815 M: 319497287463520 P: 1153 T: 3523573
S: 12235060455 M: 1037298361093936 P: 1184 T: 110821
S: 12327829503 M: 20722398914405051728 P: 543 T: 441373
S: 13371194527 M: 319497287463520 P: 1210 T: 5538812
Total time: 42248998
michaelobrien@mbp7 org.obrienscience.concurrent.CollatzBeowulfSE % 
michaelobrien@mbp7 org.obrienscience.concurrent.CollatzBeowulfSE % 
michaelobrien@mbp7 org.obrienscience.concurrent.CollatzBeowulfSE % java -cp target/classes org.obrienscience.collatz.model.Collatz 33 34 false 1132 18144584837356598024

m1max
java -cp target/classes org.obrienscience.collatz.model.Collatz 36 37 false 1219 68838156641548227040


p: 1219 v: 68838156641548227040
S: 68807943407 M: 82341648902022834004 P: 553 T: 244566
S: 68985781929 M: 114639617141613998440 P: 773 T: 800968
S: 70141259775 M: 420967113788389829704 P: 1109 T: 5226398
S: 75128138247 M: 319497287463520 P: 1228 T: 23533694
S: 77566362559 M: 916613029076867799856 P: 755 T: 6001903

S: 110243094271 M: 1372453649566268380360 P: 572 T: 83803763

S: 133561134663 M: 319497287463520 P: 1234 T: 62318500
Total time: 191653584
michaelobrien@mbp7 org.obrienscience.concurrent.CollatzBeowulfSE % 
michaelobrien@mbp7 org.obrienscience.concurrent.CollatzBeowulfSE % 
michaelobrien@mbp7 org.obrienscience.concurrent.CollatzBeowulfSE % java -cp target/classes org.obrienscience.collatz.model.Collatz 36 37 false 1219 68838156641548227040

0,1
1,2
2,4
3,8
4,16
5,32
6,64
7,128
8,256
9,512
10,1024
11,2048
12,4096
13,8192
14,16384
15,32768
16,65536
17,131072
18,262144
19,524288
20,1048576
21,2097152
22,4194304
23,8388608
24,16777216
25,33554432
26,67108864
27,134217728
28,268435456
29,536870912
30,1073741824
31,2147483648
32,4294967296 4 billion
33,8589934592
34,17179869184
35,34359738368
36,68719476736
37,137438953472
38,274877906944
39,549755813888
40,1099511627776 1 trillion
41,2199023255552
42,4398046511104
43,8796093022208
44,17592186044416
45,35184372088832
46,70368744177664
47,140737488355328
48,281474976710656
49,562949953421312
50,1125899906842624 1 quadrillion
51,2251799813685248
52,4503599627370496
53,9007199254740992
54,18014398509481984
55,36028797018963968
56,72057594037927936
57,144115188075855872
58,288230376151711744
59,576460752303423488
60,1152921504606846976 1 quintillion
61,2305843009213693952
62,4611686018427387904 4.6 quintillion checked for collatz https://en.wikipedia.org/wiki/Names_of_large_numbers
63,9223372036854775808 
64,18446744073709551616
65,36893488147419103232
66,73786976294838206464
67,147573952589676412928
68,295147905179352825856
69,590295810358705651712
70,1180591620717411303424
71,2361183241434822606848
72,4722366482869645213696
73,9444732965739290427392
74,18889465931478580854784
75,37778931862957161709568
76,75557863725914323419136
77,151115727451828646838272
78,302231454903657293676544
79,604462909807314587353088
80,1208925819614629174706176
81,2417851639229258349412352
82,4835703278458516698824704
83,9671406556917033397649408
84,19342813113834066795298816
85,38685626227668133590597632
86,77371252455336267181195264
87,154742504910672534362390528
88,309485009821345068724781056
89,618970019642690137449562112
90,1237940039285380274899124224
91,2475880078570760549798248448
92,4951760157141521099596496896
93,9903520314283042199192993792
94,19807040628566084398385987584
95,39614081257132168796771975168
96,79228162514264337593543950336
97,158456325028528675187087900672
98,316912650057057350374175801344
99,633825300114114700748351602688
100,1267650600228229401496703205376
101,2535301200456458802993406410752
102,5070602400912917605986812821504
103,10141204801825835211973625643008
104,20282409603651670423947251286016
105,40564819207303340847894502572032
106,81129638414606681695789005144064
107,162259276829213363391578010288128
108,324518553658426726783156020576256
109,649037107316853453566312041152512
110,1298074214633706907132624082305024
111,2596148429267413814265248164610048
112,5192296858534827628530496329220096
113,10384593717069655257060992658440192
114,20769187434139310514121985316880384
115,41538374868278621028243970633760768
116,83076749736557242056487941267521536
117,166153499473114484112975882535043072
118,332306998946228968225951765070086144
119,664613997892457936451903530140172288
120,1329227995784915872903807060280344576
121,2658455991569831745807614120560689152
122,5316911983139663491615228241121378304
123,10633823966279326983230456482242756608
124,21267647932558653966460912964485513216
125,42535295865117307932921825928971026432
126,85070591730234615865843651857942052864
127,170141183460469231731687303715884105728
128,340282366920938463463374607431768211456


*/
