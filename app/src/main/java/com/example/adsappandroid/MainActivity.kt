package com.example.adsappandroid
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.adsappandroid.ui.theme.AdsAppAndroidTheme
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : ComponentActivity() {
    var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        MobileAds.initialize(this@MainActivity) {}

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {

                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {

                    mInterstitialAd = interstitialAd
                }
            })


        setContent {
            AdsAppAndroidTheme {
                Scaffold { innerPadding ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ) {
                        val context = LocalContext.current
                        val bannerAd = remember {
                            AdView(context).apply {
                                adUnitId = "ca-app-pub-3940256099942544/9214589741"
                                setAdSize(AdSize.BANNER)
                                val adRequest = AdRequest.Builder().build()
                                loadAd(adRequest)
                            }
                        }

                        AndroidView(
                            factory = { bannerAd }, modifier = Modifier
                                .fillMaxWidth()
                        )
                        Button(onClick = {
                            if (mInterstitialAd != null) {
                                mInterstitialAd?.show(this@MainActivity)


                                mInterstitialAd?.fullScreenContentCallback =
                                    object : FullScreenContentCallback() {

                                        override fun onAdDismissedFullScreenContent() {
                                            // Called when ad is dismissed.
                                            Log.d(TAG, "Ad dismissed fullscreen content.")
                                            mInterstitialAd = null
                                            startActivity(
                                                Intent(
                                                    this@MainActivity,
                                                    HomeActivity::class.java
                                                )
                                            )
                                        }

                                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                            // Called when ad fails to show.
                                            Log.e(TAG, "Ad failed to show fullscreen content.")
                                            mInterstitialAd = null
                                            startActivity(
                                                Intent(
                                                    this@MainActivity,
                                                    HomeActivity::class.java
                                                )
                                            )
                                        }


                                    }


                            } else
                                startActivity(Intent(this@MainActivity, HomeActivity::class.java))

                        },


                        ) {
                            Text(text = "Home")
                        }


                    }


                }
            }
        }
    }
}