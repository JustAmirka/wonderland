package com.example.wonderland.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wonderland.R
import com.example.wonderland.activity.BookDetail
import com.example.wonderland.adapter.HomeCategoryAdapter
import com.example.wonderland.adapter.HomePageViewerAdapter
import com.example.wonderland.adapter.HomeSaleAdapter
import com.example.wonderland.databinding.FragmentHomeBinding
import com.example.wonderland.model.entity.Book
import com.example.wonderland.model.entity.Category
import com.example.wonderland.model.reponse.GetBookOnSaleResponse
import com.example.wonderland.model.reponse.GetCategoryResponse
import com.example.wonderland.utils.MyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null

    lateinit var arrCategory: ArrayList<Category>
    lateinit var arrBookOnSale: ArrayList<Book>

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.homeViewPager.adapter = context?.let { HomePageViewerAdapter(it) }

        loadBookOnSaleList()
        loadCategoryList()
    }

    private fun loadCategoryList() {
        val response = MyAPI.getAPI().getCategory()
        arrCategory = ArrayList()

        response.enqueue(object : Callback<GetCategoryResponse> {
            override fun onResponse(
                call: Call<GetCategoryResponse>,
                response: Response<GetCategoryResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()

                    for(item: Category in data!!.arrCategory) {
                        arrCategory.add(item)
                    }

                    //bind to adapter
                    val adapter=HomeCategoryAdapter(arrCategory)
                    binding.homeCategoryRC.adapter = adapter
                    binding.homeCategoryRC.layoutManager = LinearLayoutManager( context, LinearLayoutManager.HORIZONTAL, false)
                    adapter.onItemClick={item->
                        val nextFrag = HomeCategoryDetails(item.name)
                        activity!!.supportFragmentManager.beginTransaction()
                            .replace(R.id.flFragment, nextFrag, "findThisFragment")
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }

            override fun onFailure(call: Call<GetCategoryResponse>, t: Throwable) {
                if(isAdded){
                    Toast.makeText(context, "Fail connection to server", Toast.LENGTH_LONG).show()
                    t.printStackTrace()
                }
            }
        })
    }

    private fun loadBookOnSaleList() {
        val response = MyAPI.getAPI().getBookOnSale()
        arrBookOnSale = ArrayList()

        response.enqueue(object : Callback<GetBookOnSaleResponse> {
            override fun onResponse(call: Call<GetBookOnSaleResponse>, response: Response<GetBookOnSaleResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()

                    for(item: Book in data?.arrBookOnSale!!) {
                        arrBookOnSale.add(item)
                    }


                    //bind to adapter
                    val adapter=HomeSaleAdapter(arrBookOnSale)
                    binding.homeSaleRC.adapter = adapter
                    binding.homeSaleRC.layoutManager = LinearLayoutManager( context, LinearLayoutManager.HORIZONTAL, false)
                    adapter.onItemClick={ _, position->
                        val intent= Intent(activity, BookDetail::class.java)
                        intent.putExtra("id",arrBookOnSale[position]._id)
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<GetBookOnSaleResponse>, t: Throwable) {
                if (isAdded) {
                    Toast.makeText(context, "Fail connection to server", Toast.LENGTH_LONG).show()
                    t.printStackTrace()
                }
            }
        })
    }

}