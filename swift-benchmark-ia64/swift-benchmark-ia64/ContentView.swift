//
//  ContentView.swift
//  swift-benchmark-ia64
//
//  Created by michaelobrien on 2021-10-03.
//  20210930: 10 million 9232 collatz iterations - single core
//  36.58 sec on i9-9980 MBP16 emulating iphone 13 pro
//  24.56 sec on i9-9980 MBP16 native ia64
//  13.32 sec on ipad pro 2nd gen 11inch
//  11.23 sec on iphone 12 pro max
//  11.10 sec on iphone 12 pro
//  10.48 sec on ipad pro 3nd gen 12.9 inch
//
//

import SwiftUI


struct ContentView: View {
    
    @State var text = "hello world3"
    
    //func onAppear(perform action: (() -> self.process("onAppear")? = nil) -> some View
    
    func process(message: String) -> Double {
        
        let start: Int64 = 27;
        var i: Int64 = start;
        var max: Int64 = 1;
        var path: Int64 = 1;
        
        let time_start = DispatchTime.now()
        for _ in 1...10_000_000 {
        i = start;
        while i > 1 {
            if i % 2 == 0 {
                i = i / 2
            } else {
                i += 2 * i + 1
            }
            if i > max {
                max = i
            }
            path += 1
        }
        }
        let time_end = DispatchTime.now()
        let time_nano = Double(time_end.uptimeNanoseconds - time_start.uptimeNanoseconds) / 1_000_000_000
        print("max: \(max) ns: \(time_nano)");
        return time_nano;
    }

    var body: some View {
        Text(text).padding().onTapGesture {
            self.text = "processing...."
            let max = self.process(message: "test2")
            self.text = String(max)
            
        }

    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
