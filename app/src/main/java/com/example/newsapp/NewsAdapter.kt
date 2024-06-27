package com.example.newsapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ItemLayoutBinding

class NewsAdapter(val articles:List<Article>):RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article=articles[position]
        holder.binding.apply {
            newsTitle.text=article.title
            newsDesc.text=article.description
            Glide.with(root).load(article.urlToImage)
                .into(newsImage)
//            set a onclick listener to the item to display a toast of the title
            root.setOnClickListener {
                val intent=Intent(root.context,DetailActivity::class.java)
                intent.putExtra("URL",article.url)
                root.context.startActivity(intent)
//                Toast.makeText(root.context,article.title,Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun getItemCount(): Int {
        return articles.size
    }

    class ArticleViewHolder(val binding:ItemLayoutBinding):ViewHolder(binding.root){

    }
}