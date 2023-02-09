package com.example.wonderland.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.wonderland.activity.BookDetail
import com.example.wonderland.adapter.CategoryAdapter
import com.example.wonderland.databinding.FragmentHomeCategoryDetailsBinding
import com.example.wonderland.model.reponse.GetBooksResponse
import com.example.wonderland.utils.MyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeCategoryDetails(val category:String) : Fragment() {
    private var _binding: FragmentHomeCategoryDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeCategoryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textView13.text=category
        val response=MyAPI.getAPI().getCategoryDetails(category)
        response.enqueue(object : Callback<GetBooksResponse> {
            override fun onResponse(
                call: Call<GetBooksResponse>,
                response: Response<GetBooksResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()!!.arrBook
                    val adapter=CategoryAdapter(data!!)
                    binding.rv.adapter=adapter
                    binding.rv.layoutManager=GridLayoutManager(context,2)
                    adapter.setOnItemClickListener(object :CategoryAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent=Intent(activity,BookDetail::class.java)
                            intent.putExtra("id",data[position]._id)
                            startActivity(intent)
                        }
                    })
                }
            }
            override fun onFailure(call: Call<GetBooksResponse>, t: Throwable) {
            }
        })
    }
}