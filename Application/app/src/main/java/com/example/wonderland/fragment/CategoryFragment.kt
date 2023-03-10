package com.example.wonderland.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wonderland.R
import com.example.wonderland.activity.BookDetail
import com.example.wonderland.adapter.CategoryAdapter
import com.example.wonderland.databinding.FragmentCategoryBinding
import com.example.wonderland.model.entity.Book
import com.example.wonderland.model.reponse.GetBooksResponse
import com.example.wonderland.utils.MyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryFragment : Fragment(R.layout.fragment_category) {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    lateinit var arrBooks:ArrayList<Book>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadListBook()
    }

    private fun loadListBook(){
        val response = MyAPI.getAPI().getBooks()
        arrBooks = ArrayList()

        response.enqueue(object : Callback<GetBooksResponse> {
            override fun onResponse(call: Call<GetBooksResponse>, response: Response<GetBooksResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    for(item: Book in data!!.arrBook!!) {
                        if(item.quantity>0) {
                            arrBooks.add(item)
                        }
                    }

                    Log.i("?","123")
                    val adapter=CategoryAdapter(arrBooks)
                    binding.rvCategoryListItem.adapter = adapter
                    binding.rvCategoryListItem.layoutManager = GridLayoutManager(context,2)
                    adapter.setOnItemClickListener(object :CategoryAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent=Intent(activity,BookDetail::class.java)
                            intent.putExtra("id",arrBooks[position]._id)
                            startActivity(intent)
                        }
                    })
                }
            }
            override fun onFailure(call: Call<GetBooksResponse>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(context, "Fail connection to server", Toast.LENGTH_LONG).show()
                    t.printStackTrace()
                }
            }
        })
    }
}