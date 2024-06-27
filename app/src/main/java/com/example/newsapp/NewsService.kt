package com.example.newsapp

import layout.News
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// https://newsapi.org/v2/top-headlines?country=in&apiKey=88aa697c44d641e0b4e2863d18d17abd
//https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=88aa697c44d641e0b4e2863d18d17abd
//https://newsapi.org/v2/everything?domains=wsj.com&apiKey=88aa697c44d641e0b4e2863d18d17abd
const val BASE_URL="https://newsapi.org/"
const val API_KEY="3fee3cf7353f479b9450f8ac51e612e5"
interface NewsInterface{
//    define all the methods we need
//    String for the api request and page to store which page is used
    @GET("v2/top-headlines?apiKey=$API_KEY&category=business")
    fun getHeadline(@Query("country")country:String,@Query("page")page:Int): Call<News>
        // url base https://newsapi.org/v2/top-headlines?apiKey=3fee3cf7353f479b9450f8ac51e612e5&country=in&page=1
        // url base https://newsapi.org/v2/top-headlines?apiKey=3fee3cf7353f479b9450f8ac51e612e5&country=in&page=1&category=business

//    creating two class Article and News
}

//retrofit object ->Singleton object
object NewsService{
    val newInstance:NewsInterface
    init{
        val retrofit=Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        newInstance=retrofit.create(NewsInterface::class.java)
    }
}