package com.example.newsapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.customview.widget.ViewDragHelper.Callback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.databinding.ActivityMainBinding
import layout.News
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NewsAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        interface of all the methods that can be called
//        convertors
//        retrofit object
        getNews()


    }

    private fun getNews() {
        val news=NewsService.newInstance.getHeadline("in",1)
        news.enqueue(object:retrofit2.Callback<News>{
            override fun onResponse(p0: Call<News>, p1: Response<News>) {
                val news=p1.body()
                if(news!=null){
                    Log.d("SUCCESS",news.toString())
                    adapter= NewsAdapter(news.articles.filter {!it.urlToImage.isNullOrEmpty() })
                    binding.rvnewsList.adapter=adapter
                    binding.rvnewsList.layoutManager=LinearLayoutManager(parent)
                }
            }

            override fun onFailure(p0: Call<News>, p1: Throwable) {
                Log.d("FAILURE","Error in Fetching news",p1)
            }
        })
    }
}