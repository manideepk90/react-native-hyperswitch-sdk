//
//  ContentView.swift
//  hyperSwitch
//
//  Created by Kuntimaddi Manideep on 19/12/25.
//



import SwiftUI

struct ContentView: View {
    @State private var selectedSegment = 0

    var body: some View {
        VStack {
            Picker(selection: $selectedSegment, label: Text("")) {
                Text("UIKit View").tag(0)
                Text("SwiftUI View").tag(1)
            }.pickerStyle(SegmentedPickerStyle())

            switch selectedSegment {
            case 0:
                UIKitView()
            case 1:
                SwiftUIView()
            default:
                UIKitView()
            }
        }
    }
}

struct UIKitView: UIViewControllerRepresentable {
    typealias UIViewControllerType = ViewController

    func makeUIViewController(context: Context) -> ViewController {
        return ViewController()
    }

    func updateUIViewController(_ uiViewController: ViewController, context: Context) {
    }
}
