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
    
    func process(message: String) -> Int64 {
        let start: Int64 = 27;
        var i: Int64 = start;
        var max: Int64 = 1;
        var path: Int64 = 1;
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
        print("max: \(max)");
        return max;
    }

    var body: some View {
        Text(text).padding().onTapGesture {
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
