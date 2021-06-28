package com.example.booksworld.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.booksworld.R
import com.example.booksworld.adapters.FavouriteRecyclerAdapter
import com.example.booksworld.database.BookDatabase
import com.example.booksworld.database.BookEntity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FavouriteFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    var bookList = listOf<BookEntity>()
    lateinit var favouriteRecyclerView: RecyclerView
    lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var recyclerAdapter : FavouriteRecyclerAdapter
    lateinit var progressBarLayout : RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        favouriteRecyclerView = view.findViewById(R.id.favRecyclerView)
        progressBarLayout = view.findViewById(R.id.favLayoutProgressBar)
        progressBarLayout.visibility = View.VISIBLE

        layoutManager = GridLayoutManager(activity as Context, 2)

        bookList = RetrieveBookList(activity as Context).execute().get()

        if(activity != null) {
            progressBarLayout.visibility = View.GONE
            recyclerAdapter = FavouriteRecyclerAdapter(activity as Context, bookList)
            favouriteRecyclerView.adapter = recyclerAdapter
            favouriteRecyclerView.layoutManager = layoutManager
        }

        return view
    }
    class RetrieveBookList(val context: Context) : AsyncTask<Void,Void, List<BookEntity>>() {

        override fun doInBackground(vararg params: Void?): List<BookEntity> {
            val db = Room.databaseBuilder(context, BookDatabase::class.java, "book-db").build()

            return db.bookDao().getAllBooks()
        }

    }
}