package com.kotlinmovieappmvvm.ui.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.kotlinmovieappmvvm.MovieDetailsActivity
import com.kotlinmovieappmvvm.R
import com.kotlinmovieappmvvm.models.PopularMovieResponse
import com.kotlinmovieappmvvm.network.MovieApi
import com.kotlinmovieappmvvm.repositories.MovieRepository
import com.kotlinmovieappmvvm.utils.Constants
import com.kotlinmovieappmvvm.utils.toast

class MovieFragment : Fragment(), OnMovieItemClickListener {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieViewModelFactory: MovieViewModelFactory
    private lateinit var movies_recycler_view:RecyclerView
    private lateinit var viewOfLayout: View

    /******** shimmer *********/
    private lateinit var nested_scrollview:NestedScrollView
    private lateinit var shimmer_layout : ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       viewOfLayout= inflater!!.inflate(R.layout.fragment_movie, container, false)
        return viewOfLayout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val movieApi = MovieApi()
        val moviesRepository= MovieRepository(movieApi)

        movieViewModelFactory = MovieViewModelFactory(moviesRepository)
        movieViewModel = ViewModelProvider(this,movieViewModelFactory).get(MovieViewModel::class.java)

        movies_recycler_view = viewOfLayout.findViewById(R.id.movies_recycler_view)

        //shimmer
        nested_scrollview = viewOfLayout.findViewById(R.id.nested_scrollview)
        shimmer_layout = viewOfLayout.findViewById(R.id.shimmer_layout)

        shimmer_layout.startShimmer()

        if (Constants.isInternetAvailable(requireActivity())){
            movieViewModel.setPopularMovieListToLiveData()
            // set the data & observe the data
            requireActivity().toast("internet available")
            movieViewModel.getPopularMovies.observe(viewLifecycleOwner, { movies ->

                movies_recycler_view.visibility=View.VISIBLE
                shimmer_layout.stopShimmer()
                shimmer_layout.visibility=View.GONE

                movies_recycler_view.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.setHasFixedSize(true)
                    it.adapter = MovieAdapter(requireContext(), movies, this)
                }
            })
        }else{
            //display toast
            requireActivity().toast("No internet")
        }


    }

    override fun onDetailsButtonClick(view: View, movie: PopularMovieResponse.PopularMovieItem) {
        when(view.id){
            R.id.btn_details -> {
                val intent = Intent(context?.applicationContext,MovieDetailsActivity::class.java)

                intent.putExtra("DETAILS",movie)
                startActivity(intent)
            }
        }
    }
}