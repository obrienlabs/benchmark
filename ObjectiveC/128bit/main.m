//
//  main.m
//  FirstConsole
//
//  Created by Michael O'Brien on 2013-02-21.
//  Copyright (c) 2013 Michael O'Brien. All rights reserved.
//
// cli compile, linking
// biometric:128bit michaelobrien$ gcc -framework Foundation main.m
// biometric:128bit michaelobrien$ ./a.out 
// MBP16 i9-9980
//2021-10-03 20:24:19.710 a.out[31970:1041645] SRT: 1633307059.710497: ... 60 sec for 1million iter
//2021-10-03 20:25:21.194 a.out[31970:1041645] END: 1633307121.19462: 
//2021-10-03 20:25:21.194 a.out[31970:1041645] Duration: 61.48412322998047:

#import <Foundation/Foundation.h>
#include <stdint.h>

/*char[30] toString(int64_t x1, int64_t x0) {
    char aString[128];
    return aString;
}*/

int main(int argc, const char * argv[]) {
    
    
    
    
    @autoreleasepool {
        NSTimeInterval timeStamp = [[NSDate date] timeIntervalSince1970];
        // NSTimeInterval is defined as double
        NSNumber *timeStampObj = [NSNumber numberWithDouble: timeStamp];
        NSLog(@"SRT: %@: ... 60 sec for 1million iter", timeStampObj);
        for (long i = 0; i <= 10000000; i++) {
        /*
         The Long datatype in Java and the corresponding __int64 datatype in C/C++ (Visual Studio 10) and the BIGINT datatype in SQL - all overflow at 64 bits which can address an Exabyte or represent the unsigned scalar 10^18 which is 18,446744,073709,551616 or 18 Quintillion.
         63 bits = start 141023943
         */
        
        unsigned long long current0 = 0; // no uint64_t typedef
        unsigned long long current1 = 0;
        unsigned long long maxValue0 = 0;
        unsigned long long maxValue1 = 0;
        //unsigned long long MAXBIT = LLONG_MAX + 1;//9223372036854775807llu;//18446744073709551615llu >> 1;//16384;// * 65536;// * 65536;// * 65536;
        unsigned long long MAXBIT = 9223372036854775808llu;
        int maxPath = 0;
        int path = 0;
        unsigned long long max0 = 0; // 64 bit unsigned integer, like Java's long
        unsigned long long max1 = 0;
        // 1,980,976,057,694,848447 // record 88 61 bits 125 max 64,024,667,322,193,133,530,165,877,294,264,738,020
        //unsigned long long i0 = 534136224795llu;//446559217279;//1410123943;//77031;//27;
        unsigned long long i0 = 1980976057694848447llu;//446559217279llu;//1410123943;//77031;//27;
        unsigned long long i1 = 0;
        unsigned long long temp0_sh = 0;
        unsigned long long temp0_ad = 0;
        unsigned long long temp1 = 0;
        
        //NSLog(@"%llu: ", MAXBIT);
        //NSLog(@"%llu: %llu : %i",i0, max0, path);
        //for (int64_t i=27; i < 223372036854775808; i+=2) {
        current0 = i0;
        current1 = i1;
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
                //NSLog(@"a: %llu: %llu: %i",max1, max0, path);
            }
            if(max1 == current1) {
                if(max0 < current0) {
                    max0 = current0;
                    ////NSLog(@"b: %llu: %lld: %i",max1, max0, path);
                }
            }
        }
        bool maxSet = false;
        if(maxValue0 < max0) {
            maxValue0 = max0;
            maxSet = true;
        }
        if(maxPath < path) {
            maxPath = path;
            maxSet = true;
        }
        if(maxSet) {
            //NSLog(@"%llu:%llu %llu:%llu: %i",i1,i0, max1,max0, path);
        }
        //}


        }
        NSTimeInterval timeStamp2 = [[NSDate date] timeIntervalSince1970];
        // NSTimeInterval is defined as double
        NSNumber *timeStampObj2 = [NSNumber numberWithDouble: timeStamp2];
        NSLog(@"END: %@: ", timeStampObj2);
        NSNumber *duration = [NSNumber numberWithDouble: ([timeStampObj2 doubleValue] - [timeStampObj doubleValue])];
        NSLog(@"Duration: %@: ", duration);

    }
    //printf('\007');
    return 0;
}

int main2(int argc, const char * argv[]) {
    @autoreleasepool {
        
        /* connect externally */
        NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL
                                        URLWithString:@"http://obrienlabs.com/gps/FrontController"]];
        
        [request setHTTPMethod:@"POST"];
        [request setValue:@"text/xml" forHTTPHeaderField:@"Content-type"];
        
        NSString *xmlString = @"<cell>2</cell>";
        
        [request setValue:[NSString stringWithFormat:@"%d", 100]
       forHTTPHeaderField:@"Content-length"];
        
        [request setHTTPBody:[xmlString dataUsingEncoding:NSUTF8StringEncoding]];
        
        //[[NSURLConnection alloc] initWithRequest:request delegate:self];
        
        
        /*
         The Long datatype in Java and the corresponding __int64 datatype in C/C++ (Visual Studio 10) and the BIGINT datatype in SQL - all overflow at 64 bits which can address an Exabyte or represent the unsigned scalar 10^18 which is 18,446744,073709,551616 or 18 Quintillion.
         63 bits = start 141023943
        */
        int64_t current = 0;
        int64_t maxValue = 0;
        int maxPath = 0;
        int path = 0;
        int64_t max = 0; // 64 bit signed integer, like Java's long
        int64_t i = 1410123943;//77031;//27;
        //NSLog(@"%llu: %llu: %i",i, max, path);
        //for (int64_t i=27; i < 223372036854775808; i+=2) {
            current = i;
            path = 0;
            while (current > 1) {
                 if (current % 2 == 0) {
                    current = current >> 1;
                    
                } else {
                    current = 1 + current + (current << 1);
                }
                path++;
                if(max < current) {
                    max = current;
                    //NSLog(@"%llu: %llu: %i",i, max, path);
                }
            }
            bool maxSet = false;
            if(maxValue < max) {
                maxValue = max;
                maxSet = true;
            }
            if(maxPath < path) {
                maxPath = path;
                maxSet = true;
            }
            if(maxSet) {
                //NSLog(@"%llu: %llu: %i",i, max, path);
            }
        //}
    }
    return 0;
}

