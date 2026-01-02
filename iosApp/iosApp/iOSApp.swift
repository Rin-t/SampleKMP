import SwiftUI
import shared

@main
struct iOSApp: App {
    init() {
        KoinProvider.shared.doInit()
    }
    
    var body: some Scene {
        WindowGroup {
            TabPage()
        }
    }
}
