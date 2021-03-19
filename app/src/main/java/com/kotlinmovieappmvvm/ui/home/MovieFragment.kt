package com.kotlinmovieappmvvm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlinmovieappmvvm.R
import com.kotlinmovieappmvvm.network.MovieApi
import com.kotlinmovieappmvvm.repositories.MovieRepository
import kotlinx.android.synthetic.main.fragment_movie.*

class MovieFragment : Fragment(), OnMovieItemClickListener {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieViewModelFactory: MovieViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val movieApi = MovieApi()
        val moviesRepository= MovieRepository(movieApi)

        movieViewModelFactory = MovieViewModelFactory(moviesRepository)
        movieViewModel = ViewModelProvider(this,movieViewModelFactory).get(MovieViewModel::class.java)
        movieViewModel.setPopularMovieListToLiveData()

        movieViewModel.getPopularMovies.observe(viewLifecycleOwner,{
                movies ->
            movies_recycler_view.also {
                it.layoutManager= LinearLayoutManager(requireContext())
                it.setHasFixedSize(true)
                it.adapter = MovieAdapter(requireContext(),movies,this)
            }
        })

        /********* Testing *********/
//        val networkConnectionInterceptor = NetworkConnectionInterceptor(requireActivity())
//        val movieApi = MovieApi(networkConnectionInterceptor)
//        val movieRepository = MovieRepository()
//
//
//        CoroutineScope(Dispatchers.IO).launch {
//            //fetchMovies(movieApi)
//            val mutablelist: LiveData<List<PopularMovieResponse.PopularMovieItem>> =
//                movieRepository.getPopularMovies()
//            Log.d("TAG", "" + mutablelist.value?.get(0))
//
//        }
    }
}