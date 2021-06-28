package com.example.booksworld.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.booksworld.R
import com.example.booksworld.adapters.DashboardRecyclerAdapter
import com.example.booksworld.module.Book
import com.example.booksworld.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FragmentDashboard : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var dashboardRecyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager

    /* private var bookList = arrayListOf("Harry Potter","The Lord of the Rings","The Great Gatsby","Pride and Prejudice",
         "The Destination","The Diary Of A Young Girl","The Book Thief",
         "The Jungle Book","India of my dreams","To Kill a Mockingbird")*/

    /*private var bookNewList = arrayListOf(
        Book("Harry Potter","J.K. Rowling","800","4.8",R.drawable.harry_potter),
        Book("The Lord of the Rings","J.R.R. Tolkien","570","4.8",R.drawable.jrr_tolkien),
        Book("The Great Gatsby","F. Scott Fitzgerald","200","4.2",R.drawable.fscott_fitzgerald),
        Book("Pride and Prejudice","Jane Austen","600","4.7",R.drawable.jane_austen),
        Book("The Destination","Robert L. Moore","776","4.8",R.drawable.the_destination),
        Book("The Diary Of A Young Girl","Anne Frank","400","4.6",R.drawable.anne_frank),
        Book("The Book Thief","Markus Zusak","799","4.7",R.drawable.markus_zusak),
        Book("The Hobbit","J.R.R. Tolkien","735","4.6",R.drawable.the_hobbit),
        Book("In Search of Lost Time","Marcel Proust","577","4.0",R.drawable.marcel_proust),
        Book("To Kill a Mockingbird","Harper Lee","780","4.0",R.drawable.harper_lee)

    )*/

    private var bookNewList = arrayListOf<Book>()

    private lateinit var recyclerAdapter: DashboardRecyclerAdapter

//    private lateinit var btnNetwork: Button

    private lateinit var progressBarLayout : ProgressBar

    var ratingComparator = Comparator<Book>{ book1, book2 ->
        if(book1.bookRating.compareTo(book2.bookRating, true) == 0) {
            book1.bookName.compareTo(book2.bookName, true)
        }else {
            book1.bookRating.compareTo(book2.bookRating, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)

        dashboardRecyclerView = view.findViewById(R.id.recyclerViewDashboard)
        layoutManager = LinearLayoutManager(activity)

        progressBarLayout = view.findViewById(R.id.progressLayout)
        progressBarLayout.visibility =  View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v1/book/fetch_books/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {
            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    //code to handle response

                    try {
                        progressBarLayout.visibility = View.GONE
                    val success = it.getBoolean("success")
                    if (success) {
                        val data = it.getJSONArray("data")

                        for (i in 0 until data.length()) {
                            val bookJsonObject = data.getJSONObject(i)

                            val bookObject = Book(
                                bookJsonObject.getString("book_id"),
                                bookJsonObject.getString("name"),
                                bookJsonObject.getString("author"),
                                bookJsonObject.getString("price"),
                                bookJsonObject.getString("rating"),
                                bookJsonObject.getString("image")
                            )
                            bookNewList.add(bookObject)

                            recyclerAdapter = DashboardRecyclerAdapter(activity as Context, bookNewList)

                            dashboardRecyclerView.adapter = recyclerAdapter
                            dashboardRecyclerView.layoutManager = layoutManager

                        }
                    } else {
                        Toast.makeText(activity as Context, "Some Error Occurred !!", Toast.LENGTH_SHORT).show()
                    }
                    }
                    catch(e : JSONException) {
                        Toast.makeText(activity as Context, "Some Unexpected Error Occurred", Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {

                    // code to handle error
                    if(activity != null) {
                        Toast.makeText(activity as Context, "Volley Error Occurred!!",Toast.LENGTH_SHORT).show()
                    }

                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "5a411ee382252c"
                        //headers["token"] = "9bf534118365f1"

                        return headers
                    }
                }
            queue.add(jsonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection Found!")
            dialog.setPositiveButton("Open Settings"){text, listener->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()
        }


        /*btnNetwork = view.findViewById(R.id.btnCheckNetwork)
        btnNetwork.setOnClickListener {
            if (ConnectionManager().checkConnectivity(activity as Context)) {
                val dialog = android.app.AlertDialog.Builder(activity as Context)
                dialog.setTitle("Success!!")
                dialog.setMessage("You are Connected to Internet")
                dialog.setPositiveButton("OK") { text, Listener ->
                    // nothing
                }
                dialog.setNegativeButton("Cancel") { text, Listener ->
                    //nothing
                }
                dialog.create()
                dialog.show()
            } else {
                val dialog = android.app.AlertDialog.Builder(activity as Context)
                dialog.setTitle("Error!!")
                dialog.setMessage("You are NOT Connected to Internet")
                dialog.setPositiveButton("OK") { text, Listener ->
                    // nothing
                }
                dialog.setNegativeButton("Cancel") { text, Listener ->
                    //nothing
                }
                dialog.create()
                dialog.show()
            }
        }*/

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item?.itemId
        if(id == R.id.sorting) {

            Collections.sort(bookNewList,ratingComparator)
            bookNewList.reverse()
            recyclerAdapter.notifyDataSetChanged()

        }
        return super.onOptionsItemSelected(item)
    }

}