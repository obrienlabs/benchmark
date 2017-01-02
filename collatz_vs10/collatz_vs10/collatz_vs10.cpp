// collatz_vs10.cpp : Defines the entry point for the console application.
// http://www.ericr.nl/wondrous/pathrecs.html
/*
29 sec t4400 vs10 24 bit
580k/sec
2.08billion/hr
50 billion/day
18 trillion/yr

 For 3,779,571,220,480 scalar operations (2^35 * 110) we have
 36 sec for 2^35 of 27:111:9232 on 64-bit i7-920 2.8Ghz 32-bit code = 955 million seq/sec = 105 billion iter/sec
 46 sec for 2^35 of 27:111:9232 on 64-bit E8400 3.0Ghz 32-bit code  = 747 million seq/sec =  82 billion iter/sec
 53 sec for 2^35 of 27:111:9232 on 64-bit Q6600 2.6Ghz 32-bit code  = 648 million seq/sec =  71 billion iter/sec
 65 sec for 2^35 of 27:111:9232 on 32-bit T4400 2.1Ghz 32-bit code  = 528 million seq/sec =  58 billion iter/sec
 91 sec for 2^35 of 27:111:9232 on 32-bit P4-630 3 Ghz 32-bit code  = 374 million seq/sec =  41 billion iter/sec

*/
#include "stdafx.h"
//#include <inttypes.h>

unsigned short FIRST_BIT = 127;
unsigned short number[128];
unsigned short temp[128];
unsigned short maxValue[128];
unsigned short max[128];
unsigned __int64 maxPath;
unsigned __int64 path;

// use long long 64 bit word
// http://en.wikipedia.org/wiki/Integer_(computer_science)#Common_integral_data_types
// http://en.wikipedia.org/wiki/Bignum
// http://en.wikipedia.org/wiki/Sch%C3%B6nhage%E2%80%93Strassen_algorithm
// http://clc-wiki.net/wiki/the_C_Standard
// http://www.open-std.org/jtc1/sc22/wg14/www/docs/n1124.pdf

__int64 hailstoneMax(__int64 start) {
	__int64 maxNumber = 0;
    __int64 num = start;
	__int64 path = 0;
    while(num > 4) {
		//printf("%I64d ", maxNumber);
    	if((num % 2) > 0) {
    		//num = (num >> 1) + num + 1; // odd (combined 2-step odd/even = 30% speedup
			num = (num << 1) + num + 1; // odd
    	} else {
    		num >>= 1; // even
    	}
    	if(num > maxNumber) {
    		maxNumber = num;
    	}
		path++;
    }
	return maxNumber;
}

int isPowerOf2(int radix) {
	// check all digits for 0 except radix
	for(int i=0;i<(FIRST_BIT - 1);i++) {
		if(number[i]!=0) {
			return 0;
		}
	}
	// now check radix
	if(number[radix]==1) {
		return 1;
	} else {
		return 0;
	}
}

void shiftLeft(int carry) {
	for(int i=0;i<(FIRST_BIT - 1);i++) {
		number[i] = number[i-1];
		// overflow bit 0 is discarded
	}
	number[FIRST_BIT] = carry;
}

void shiftRight(int borrow) {
	for(int i=0;i<(FIRST_BIT - 1);i++) {
		number[FIRST_BIT- i] = number[FIRST_BIT - 1 - i];
		// underflow bit 63 is discarded
	}
	number[0] = 0;
}

void addSelf() {
	short carry = 0;
	short temp = 0;
	for(int i=0;i<FIRST_BIT;i++) {
		temp = number[i];
		if(number[FIRST_BIT-i] > 0) {
			number[FIRST_BIT-i] = 1;
			carry = 1;
		} else {
			number[FIRST_BIT - i] = 0;
			carry = 0;
		}
	} // overflow
}


void getSequence64() {
  __int64 num = 27;
  //unsigned long long num = 1;
  __int64 maxNumber = 0;
  __int64 newMax = 0;
  unsigned long long path = 0;
  unsigned long long maxPath = 0;
  __int64 MAX = (1 << 30); // dont use long long
  while(1) {//num < MAX) {
	newMax = hailstoneMax(num);	
	if(newMax > maxNumber) {
		printf("\n%I64d,\t%I64d",num, newMax);
		maxNumber = newMax;
		//printf("\n%d,\t%I64d",num,maxNumber); // or I64u, %llu (do not work properly)
	}
	num += 2;
  }
}

void getSequence128Bench() {
        unsigned long long current0 = 0; // no uint64_t typedef
        unsigned long long current1 = 0;
        unsigned long long maxValue0 = 0;
        unsigned long long maxValue1 = 0;
        //unsigned long long MAXBIT = LLONG_MAX + 1;//9223372036854775807llu;//18446744073709551615llu >> 1;//16384;// * 65536;// * 65536;// * 65536;
        unsigned long long MAXBIT = 9223372036854775808;
        int maxPath = 0;//1475 - 2;
        int path = 0;
        unsigned long long max0 = 0;//7073134427238031588 - 2; // 64 bit unsigned integer, like Java's long
        unsigned long long max1 = 0;//470784170169173952 - 2;
        // 1,980,976,057,694,848447 // record 88 61 bits 125 max 64,024,667,322,193,133,530,165,877,294,264,738,020
        //unsigned long long i0 = 534136224795llu;//446559217279;//1410123943;//77031;//27;
        unsigned long long i0 = 3;//1980976057694848447llu;// path 1475 446559217279llu;//1410123943;//77031;//27;
        // 470784170169173952:7073134427238031588: 1475

        unsigned long long i1 = 0;
        unsigned long long temp0_sh = 0;
        unsigned long long temp0_ad = 0;
        unsigned long long temp1 = 0;
        
        printf("%llu: \n", MAXBIT);
        printf("%llu: %llu : %i\n",i0, max0, path);
        //for (int64_t i=27; i < 223372036854775808; i+=2) {
        for (;;) {
            for(int x = 0; x<16777216; x++) {
        current0 = i0;
        current1 = i1;
            max1 = 0;
            max0 = 0;
        path = 0;
        while (!((current0 == 1) && (current1 == 0)) ) {
            if (current0 % 2 == 0) {
                current0 = current0 >> 1;
                // shift high byte first
                if(current1 % 2 != 0) {
                    current0 += MAXBIT;
                    //NSLog(@"u: %llu:%llu %i",current1, current0,path);
                }
                current1 = current1 >> 1;
                //NSLog(@"x: %llu:%llu %i",current1, current0,path);
            } else {
                temp1 = 3 * current1;// + (current1 << 1);
                current1 = temp1;

                // shift first - calc overflow 1
                temp0_sh = 1 + (current0 << 1);
                if(!(current0 < MAXBIT)) {
                    current1 = current1 + 1;
                    //NSLog(@"o1: %llu:%llu %i",current1, temp0_sh,path);

                }
                // add second - calc overflow 2
                temp0_ad = temp0_sh + current0;
                if(temp0_ad < current0) { // overflow
                    current1 = current1 + 1;
                    //NSLog(@"o2: %llu:%llu %i",current1, temp0_ad,path);
                }
                current0 = temp0_ad;
                //NSLog(@"z: %llu:%llu %i",current1, current0,path);
                
            }
            path++;
            if(max1 < current1) {
                max1 = current1;
                max0 = current0;
                //NSLog(@"m: %llu: %llu: %i",max1, max0, path);
            } else {
            if(max1 == current1) {
                if(max0 < current0) {
                    max0 = current0;
                    //NSLog(@"b: %llu: %lld: %i",max1, max0, path);
                }
            }
            }
        }
        //bool maxSet = false;
        if(maxValue1 < max1) {
            maxValue0 = max0;
            maxValue1 = max1;
            printf("m1: %llu:%llu %llu:%llu: %i\n",i1,i0,max1, max0, path);
        } else {
            if(maxValue1 == max1) {
                if(maxValue0 < max0 ) {
                    maxValue0 = max0;
                    printf("m0: %llu:%llu %llu:%llu: %i\n",i1,i0,max1, max0, path);
                }
            }
        }
        if(maxPath < path) {
            maxPath = path;
            printf("mp: %llu:%llu %llu:%llu: %i\n",i1,i0, max1,max0, path);
        }
            i0 += 2;
        }
            printf("%llu:%llu %llu:%llu %i\n",i1,i0,max1,max0,path);
            }
    }
    


void getSequence64Bench() {
  __int64 num = 27;
  //unsigned long long num = 1;
  __int64 maxNumber = 0;
  __int64 newMax = 0;
  unsigned long long path = 0;
  unsigned long long maxPath = 0;
  __int64 MAX = (1 << 4); // dont use long long
  unsigned long  iter1 = (1 << 6);
  unsigned long long iter2;// = (1 << 16);
  unsigned long long iter3;// = (1 << 16);
  while(iter1-- > 0) {
	printf(".");
   iter2 = (1 << 16);
   while(iter2-- > 0) {
    iter3 = (1 << 16);
     while(iter3-- > 0) {
	newMax = hailstoneMax(num);	
	if(newMax > maxNumber) {
		//printf("\n%d,%I64d,\t%I64d",iter,num, newMax);
		//maxNumber = newMax;
		printf("\n%d,\t%I64d",num,maxNumber); // or I64u, %llu (do not work properly)
	}
	//num += 2;
  }
  }
  }
  printf("\nfinished\n");
}

void clear() {
	for(int i=0;i<FIRST_BIT;i++) {
		number[i] = 0;
		temp[i] = 0;
		maxValue[i] = 0;
		max[i] = 0;
		path = 0;
		maxPath = 0;
	}
}

__int64 getLong(unsigned short *bitArray) {
	__int64 number = 0;
	for(int i=0;i<(FIRST_BIT);i++) {
		number += bitArray[i] << (FIRST_BIT - i);
	}
	return number;
}
__int64 hailstoneMax2() {
	while (!isPowerOf2(FIRST_BIT)) {
		if(number[FIRST_BIT]==1) {
			shiftLeft(1);
			addSelf();
		} else {
			shiftRight(0);
		}
	}
	return getLong(number);
}

void getSequence128() {
	// initialize bits
	clear();

  __int64 num = 27;
  __int64 maxNumber = 0;
  __int64 newMax = 0;
  //unsigned long long path = 0;
  //unsigned long long maxPath = 0;
  __int64 MAX = (1 << 30); // dont use long long
  while(1) {//(num < MAX) {
	  printf("%I64d ", maxNumber);
	newMax = hailstoneMax2();	
	if(newMax > maxNumber) {
		printf("\n%I64d,\t%I64d",num, newMax);
		maxNumber = newMax;
		//printf("\n%d,\t%I64d",num,maxNumber); // or I64u, %llu (do not work properly)
	}
	num += 2;
  }
}

int _tmain(int argc, _TCHAR* argv[]) {
	printf("\nCollatz Sequence\n");
	//getSequence128();
	//getSequence64();
	getSequence128Bench();
	return 0;
}


