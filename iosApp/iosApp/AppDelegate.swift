//
//  AppDelegate.swift
//  iosApp
//
//  Created by Richard Bajomo on 14/08/2024.
//  Copyright © 2024 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import shared

class AppDelegate: NSObject, UIApplicationDelegate {
    
        func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
            return true
        }
        
        func application(_ application: UIApplication, continue userActivity: NSUserActivity, restorationHandler: @escaping ([UIUserActivityRestoring]?) -> Void) -> Bool {
            if userActivity.activityType == NSUserActivityTypeBrowsingWeb, let url = userActivity.webpageURL {
                let urlString = url.absoluteString
            }
            return true
        }
}
