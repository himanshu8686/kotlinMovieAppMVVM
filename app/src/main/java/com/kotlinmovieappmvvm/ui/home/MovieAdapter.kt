package com.kotlinmovieappmvvm.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kotlinmovieappmvvm.R
import com.kotlinmovieappmvvm.models.PopularMovieResponse
import com.kotlinmovieappmvvm.utils.toast

/**
 * Project Name :KotlinMovieAppMVVM
 * Created By :Himanshu sharma on 18-03-2021
 * Package : com.kotlinmovieappmvvm.ui.home
 */
class MovieAdapter(
    private val context: Context,
    private val popularMoviesList: List<PopularMovieResponse.PopularMovieItem>,
    private val onMovieItemClickListener: OnMovieItemClickListener
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_list_item,
            parent,
            false
        )
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val imageString: String = popularMoviesList[position].posterPath
        val movieTitle: String = popularMoviesList[position].title
        val movieReleaseDate: String = popularMoviesList[position].releaseDate
        val movieLanguage: String = popularMoviesList[position].originalLanguage
        val movieRating: Float = popularMoviesList[position].voteAverage / 3

        holder.setMovieData(imageString, movieTitle, movieReleaseDate, movieLanguage, movieRating)

        holder.setClickOnDetailsButton(position,popularMoviesList,onMovieItemClickListener)
        //get listener callback on Homefragment
//        holder.recyclerviewMovieBinding.layoutLike.setOnClickListener {
//            listener.onItemClick(holder.recyclerviewMovieBinding.layoutLike,movies[position])
//        }
    }

    override fun getItemCount(): Int {
        return popularMoviesList?.size ?: 0
    }


    /**
     *  MovieViewHolder class
     */
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movie_img: ImageView = itemView.findViewById(R.id.movie_img)
        var movie_title: TextView = itemView.findViewById(R.id.movie_title)
        var movie_release_date: TextView = itemView.findViewById(R.id.movie_release_date)
        var movie_language: TextView = itemView.findViewById(R.id.movie_language)
        var rating_bar: RatingBar = itemView.findViewById(R.id.rating_bar)
        var btn_details:Button = itemView.findViewById(R.id.btn_details)

        fun setMovieData(
            imageString: String,
            movieTitle: String,
            movieReleaseDate: String,
            movieLanguage: String,
            movieRating: Float
        ) {

            Glide.with(itemView.context).load("https://image.tmdb.org/t/p/w500/" + imageString)
                .placeholder(R.drawable.spinner)
                .into(movie_img)
            //Glide.with(itemView.context).load(R.drawable.spinner).into(movie_img)

            movie_title.text = movieTitle
            movie_release_date.text = movieReleaseDate
            movie_language.text = movieLanguage
            rating_bar.rating = movieRating
        }

        fun setClickOnDetailsButton(position: Int, popularMoviesList: List<PopularMovieResponse.PopularMovieItem>, onMovieItemClickListener: OnMovieItemClickListener) {
            btn_details.setOnClickListener {
                onMovieItemClickListener.onDetailsButtonClick(it,popularMoviesList[position])
            }
        }
    }
}