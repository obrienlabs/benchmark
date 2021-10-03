//
//  ContentView.swift
//  swift-benchmark
//
//  Created by michaelobrien on 2021-10-02.
//

import SwiftUI


struct ContentView: View {
    
    @State var text = "hello world"
    
    //func onAppear(perform action: (() -> self.process("onAppear")? = nil) -> some View
    
    func process(message: String) -> Double {
        
        let start: Int64 = 27;
        var i: Int64 = start;
        var max: Int64 = 1;
        var path: Int64 = 1;
        
        let time_start = DispatchTime.now()
        for _ in 1...10000000 {
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
