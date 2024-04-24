package com.closedcircuit.closedcircuitapplication.common.presentation.feature.onboarding

import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.StringResource

internal sealed class OnboardingPage(
    val title: StringResource,
    val description: StringResource,
    val image: ImageResource
) {
    object First : OnboardingPage(
        title = SharedRes.strings.onboarding_one_title,
        description = SharedRes.strings.onboarding_one_description,
        image = SharedRes.images.onboarding_one_illustration
    )

    object Second : OnboardingPage(
        title = SharedRes.strings.onboarding_two_title,
        description = SharedRes.strings.onboarding_two_description,
        image = SharedRes.images.onboarding_two_illustration
    )

    object Third : OnboardingPage(
        title = SharedRes.strings.onboarding_three_title,
        description = SharedRes.strings.onboarding_three_description,
        image = SharedRes.images.onboarding_three_illustration
    )

    object Fourth : OnboardingPage(
        title = SharedRes.strings.onboarding_three_title,
        description = SharedRes.strings.onboarding_four_description,
        image = SharedRes.images.onboarding_four_illustration
    )
}
