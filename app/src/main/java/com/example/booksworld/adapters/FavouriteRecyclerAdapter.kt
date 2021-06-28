package com.example.booksworld.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.booksworld.R
import com.example.booksworld.database.BookEntity
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context,val bookList : List<BookEntity>)
    :RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>()  {

    class FavouriteViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val llFavLayout: LinearLayout = view.findViewById(R.id.llFavContent)
        val txtName : TextView = view.findViewById(R.id.txtFavBookTitle)
        val txtAuthor : TextView = view.findViewById(R.id.txtFavBookAuthor)
        val txtPrice : TextView = view.findViewById(R.id.txtFavBookPrice)
        val txtRating : TextView = view.findViewById(R.id.txtFavBookRating)
        val imgBook : ImageView = view.findViewById(R.id.imgFavBookImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favourites_fragment_row,parent,false)
        return FavouriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val book : BookEntity = bookList[position]
        holder.txtName.text = book.bookName.toString()
        holder.txtAuthor.text = book.bookAuthor.toString()
        holder.txtPrice.text = book.bookPrice.toString()
        holder.txtRating.text = book.bookRating.toString()

        Picasso.get().load(book.bookImage).error(R.drawable.default_book).into(holder.imgBook)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}