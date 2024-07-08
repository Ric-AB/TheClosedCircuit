import SwiftUI
import FirebaseCore
import shared

@main
struct iOSApp: App {
    init() {
        KoinKt.doInitKoin()
        FirebaseApp.configure()
    }
	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
