//
//  ContentView.swift
//  swift-benchmark
//
//  Created by michaelobrien on 2021-10-02.
//

import SwiftUI


struct ContentView: View {
    
    
    //func onAppear(perform action: (() -> self.process("onAppear")? = nil) -> some View
    
    func process(message: String) {
        let start = 27;
        var i = start;
        var max = 1;
        var path = 1;
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
    }

    var body: some View {
        Text("Hello, world!").padding().onTapGesture {
            self.process(message: "test2")
            
        }

    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
