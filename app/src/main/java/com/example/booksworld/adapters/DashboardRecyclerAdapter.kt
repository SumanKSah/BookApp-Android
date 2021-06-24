package com.example.booksworld.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.booksworld.R
import com.example.booksworld.activity.DescriptionActivity
import com.example.booksworld.module.Book
import com.squareup.picasso.Picasso

class DashboardRecyclerAdapter(val context: Context, private var itemList: ArrayList<Book>) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    class DashboardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var txtBookName: TextView = view.findViewById(R.id.txtDashBoardRecycler)
        val txtAuthor: TextView = view.findViewById(R.id.txtAuthorRecyclerDashboard)
        val txtBookPrice: TextView = view.findViewById(R.id.txtDashBoardRecyclerPrice)
        val txtRating: TextView = view.findViewById(R.id.txtDashBoardRecyclerRating)
        val imgBook: ImageView = view.findViewById(R.id.imgDashboardRecycler)
        val rowDashboard: LinearLayout = view.findViewById(R.id.dashboardRecyclerRow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_dashboard_row, parent, false)

        return DashboardViewHolder(view)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val book = itemList[position]
        holder.txtBookName.text = book.bookName
        holder.txtAuthor.text = book.bookAuthor
        holder.txtBookPrice.text = book.bookPrice
        holder.txtRating.text = book.bookRating
//        holder.imgBook.setImageResource(book.bookImage)

        Picasso.get().load(book.bookImage).error(R.drawable.default_book).into(holder.imgBook)

        holder.rowDashboard.setOnClickListener {
//            Toast.makeText(context, "Clicked on "+holder.txtBookName.text,Toast.LENGTH_SHORT).show()
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("book_id",book.bookId)
            context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}