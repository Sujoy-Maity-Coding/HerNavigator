package com.sujoy.hernavigator.Ads

import android.app.Activity
import android.widget.Toast
import androidx.navigation.NavHostController
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.sujoy.hernavigator.Presentation.NavigationGraph.Routes

fun rewaredAds(activity: Activity, navController: NavHostController,onAdLoaded: () -> Unit,
               onAdFailed: () -> Unit) {
    RewardedAd.load(
        activity,
        "ca-app-pub-3940256099942544/5224354917",
        AdRequest.Builder().build(),
        object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)
                // Handle ad load failure if necessary
                onAdFailed()
            }

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                super.onAdLoaded(rewardedAd)

                rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        onAdLoaded()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        super.onAdFailedToShowFullScreenContent(p0)
                        // Handle ad show failure if necessary
                        onAdFailed()
                    }
                }

                rewardedAd.show(activity) { rewardItem ->
                    navController.navigate(Routes.SkinCare) // Navigate to your target screen on reward earned
                }
            }
        }
    )
}
