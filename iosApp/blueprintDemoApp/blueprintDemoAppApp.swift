//
//  blueprintDemoAppApp.swift
//  blueprintDemoApp
//
//  Created by Aleksey Rogachevskiy on 10.05.2025.
//

import UIKit
import SwiftUI
import blueprintDemoCompose

@main
struct blueprintDemoAppApp: App {
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea()
    }
}

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> some UIViewController {
        MainKt.MainViewController()
    }
    
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {}
}
