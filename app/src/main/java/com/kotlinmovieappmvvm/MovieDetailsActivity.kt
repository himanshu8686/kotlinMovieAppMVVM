package com.kotlinmovieappmvvm

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.kotlinmovieappmvvm.models.PopularMovieResponse

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var iv_movie_image:ImageView
    private lateinit var tv_movie_title:TextView
    private lateinit var tv_movie_overview:TextView
    private lateinit var rb_movie_rating:RatingBar
    private lateinit var movie:PopularMovieResponse.PopularMovieItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)
        val toolbar: Toolbar = findViewById(R.id.title_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Movie Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // back arrow in action bar
         movie=intent.getParcelableExtra<PopularMovieResponse.PopularMovieItem>("DETAILS")

        iv_movie_image=findViewById(R.id.iv_movie_image)
        tv_movie_title=findViewById(R.id.tv_movie_title)
        tv_movie_overview=findViewById(R.id.tv_movie_overview)
        rb_movie_rating=findViewById(R.id.rb_movie_rating)

        setData()

    }

    private fun setData() {
        Glide.with(this).load("https://image.tmdb.org/t/p/w500/" + movie.backdropPath)
            .placeholder(R.drawable.spinner)
            .into(iv_movie_image)

        tv_movie_title.text=movie.title
        tv_movie_overview.text=movie.overview
        rb_movie_rating.rating=movie.voteAverage/2
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id= item.itemId
        when(id){
            android.R.id.home->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}