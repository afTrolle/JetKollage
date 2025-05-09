import SwiftUI
import shared

@main
struct iOSApp: App {
    
    init() {
        App_appleKt.doInitKoin()
    }
    
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
