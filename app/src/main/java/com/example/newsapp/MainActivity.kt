package com.example.newsapp

import android.net.DnsResolver
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsService.newsInstance
import com.littlemango.stacklayoutmanager.StackLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var  adapter: NewsAdapter
    private var articles = mutableListOf<Article>()
    var totalResults = 0
    var pageNumber = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = NewsAdapter(this@MainActivity, articles)
        newsList.adapter = adapter
        newsList.layoutManager = LinearLayoutManager(this@MainActivity)

        /*val layoutManager = StackLayoutManager(StackLayoutManager.ScrollOrientation.BOTTOM_TO_TOP)
        layoutManager.setPagerMode(true)
        layoutManager.setPagerFlingVelocity(2500)
        newsList.layoutManager = layoutManager
        layoutManager.setItemChangedListener(object : StackLayoutManager.ItemChangedListener {
            override fun onItemChanged(position: Int) {
               // Log.d("ON_CLICK_ITEM", "item - ${layoutManager.getFirstVisibleItemPosition()}")
               // Log.d("ON_CLICK_ITEM", "totalcount - ${layoutManager.itemCount}")

                if (totalResults > layoutManager.itemCount && layoutManager.getFirstVisibleItemPosition() >= layoutManager.itemCount - 5) {
                    pageNumber++
                    getNews()
                }
            }
        })*/
        getNews()
        pageNumber++
        getNews()
    }

    private fun getNews() {
        Log.d("CHECK", "GetNews()")
        val news = NewsService.newsInstance.getHeadLines("in", pageNumber)
        news.enqueue(object: Callback<News> {
            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d("TAG", "Error in fetching News", t)
            }

            override fun onResponse(call: Call<News>, response: Response<News>) {
                val newNews: News? = response.body()
                if (newNews != null) {
                    //Log.d("TAG", newNews.toString())
                    totalResults = newNews.totalResults
                    articles.addAll(newNews.articles)
                    adapter.notifyDataSetChanged()
                }
            }
        })
    }
}