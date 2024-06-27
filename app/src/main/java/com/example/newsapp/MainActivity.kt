package com.example.newsapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.littlemango.stacklayoutmanager.StackLayoutManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import layout.News
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NewsAdapter
    private lateinit var binding: ActivityMainBinding
    private var articles = mutableListOf<Article>()
    private var dataFinished = false
    var pageNumber = 1
    var totalResult = -1
    val m = "Hello"
    private var mInterstitialAd: InterstitialAd? = null
    private final val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this@MainActivity)
        loadAd()
//        loadAd()
//        var adRequest = AdRequest.Builder().build()


//        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.d("MAIN", adError.toString())
//                mInterstitialAd = null
//            }
//
//            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                Log.d("MAIN", "Ad was loaded.")
//                mInterstitialAd = interstitialAd
//            }
//        })
//        interface of all the methods that can be called
//        convertors
//        retrofit object
        // Binding recycler view with empty view
        adapter = NewsAdapter(articles)
        binding.rvnewsList.adapter = adapter
//        binding.rvnewsList.layoutManager=LinearLayoutManager(parent)
        val layoutManager = StackLayoutManager(StackLayoutManager.ScrollOrientation.BOTTOM_TO_TOP)
        layoutManager.setPagerMode(true);
        layoutManager.setPagerFlingVelocity(3000);
        layoutManager.setItemChangedListener(object : StackLayoutManager.ItemChangedListener {
            override fun onItemChanged(position: Int) {
//                Log.d(m,"First item ${layoutManager.getFirstVisibleItemPosition()}")
//                Log.d(m,"Total items ${layoutManager.itemCount}")
                if (!dataFinished && layoutManager.getFirstVisibleItemPosition() >= layoutManager.itemCount - 5) {
                    pageNumber++;
                    getNews()
                }
                if(position%10==0){
                        showAd()
                }
            }
        })
        binding.rvnewsList.layoutManager = layoutManager
        getNews()
    }
    private fun loadAd(){
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d(TAG, adError.toString())
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
    }
    private fun showAd(){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
            loadAd()
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }
    //    private fun showAd(){
//        if (mInterstitialAd != null) {
//            mInterstitialAd?.show(this@MainActivity)
//            loadAd()
//        } else {
//            Log.d("TAG", "The interstitial ad wasn't ready yet.")
//        }
//    }
//    private fun loadAd(){
//        var adRequest = AdRequest.Builder().build()
//
//        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.d("MAIN", adError.toString())
//                mInterstitialAd = null
//            }
//
//            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                Log.d("MAIN", "Ad was loaded.")
//                mInterstitialAd = interstitialAd
//            }
//        })
//    }
    private fun getNews() {
        Log.d(m, "Request send for $pageNumber")
        val news = NewsService.newInstance.getHeadline("in", pageNumber)
        news.enqueue(object : retrofit2.Callback<News> {
            override fun onResponse(p0: Call<News>, p1: Response<News>) {
                val storenews = p1.body()
                if (storenews?.articles.isNullOrEmpty()) {
                    dataFinished = true
                    return
                }

                if (storenews != null) {
//                    Log.d("SUCCESS",storenews.toString())
                    totalResult = storenews.totalResults
//                    Log.d(m,"$totalResult")
                    articles.addAll(storenews.articles.filter { !it.urlToImage.isNullOrEmpty() })
                    // upgrading recycler view with the updated item list
                    adapter.notifyDataSetChanged()

                }
            }

            override fun onFailure(p0: Call<News>, p1: Throwable) {
                Log.d("FAILURE", "Error in Fetching news", p1)
            }
        })
    }
}