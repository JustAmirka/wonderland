package com.example.wonderland.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wonderland.activity.SellerBookDetails
import com.example.wonderland.adapter.CategoryAdapter
import com.example.wonderland.databinding.FragmentSellerViewBookBinding
import com.example.wonderland.model.entity.Book
import com.example.wonderland.model.reponse.GetBooksResponse
import com.example.wonderland.utils.MyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SellerViewBookFrag(private val user: String) : Fragment() {
    private lateinit var _binding: FragmentSellerViewBookBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentSellerViewBookBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadListBook(user)
    }

    override fun onResume() {
        super.onResume()
        loadListBook(user)
    }

    private fun loadListBook(user:String){
        val response = MyAPI.getAPI().getBooks()
        val arrBooks = arrayListOf<Book>()

        response.enqueue(object : Callback<GetBooksResponse> {
            override fun onResponse(call: Call<GetBooksResponse>, response: Response<GetBooksResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    for(item: Book in data!!.arrBook!!) {
                        if(item.seller==user && item.quantity>0)
                            arrBooks.add(item)
                    }

                    val adapter= CategoryAdapter(arrBooks)
                    binding.rv.adapter = adapter
                    binding.rv.layoutManager = GridLayoutManager(context,2)
                    adapter.setOnItemClickListener(object : CategoryAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent= Intent(context, SellerBookDetails::class.java)
                            intent.putExtra("id",arrBooks[position]._id)
                            intent.putExtra("user",user)
                            startActivity(intent)
                        }
                    })
                }
            }
            override fun onFailure(call: Call<GetBooksResponse>, t: Throwable) {
                Toast.makeText(context, "Fail connection to server", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }
        })
    }
}