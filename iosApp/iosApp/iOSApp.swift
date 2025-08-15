import SwiftUI
import FirebaseCore
import shared

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    init() {
        KoinKt.doInitKoin()
        FirebaseApp.configure()
    }
	var body: some Scene {
		WindowGroup {
            ContentView().onOpenURL { url in
                ExternalUriHandler.shared.onNewUri(uri: url.absoluteString)
            }
		}
	}
}
