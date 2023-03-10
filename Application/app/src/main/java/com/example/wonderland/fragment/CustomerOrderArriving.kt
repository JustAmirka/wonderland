package com.example.wonderland.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wonderland.activity.OrderDetail
import com.example.wonderland.adapter.CustomerOrderTabRCAdapter
import com.example.wonderland.databinding.FragmentCustomerOrderArrivingBinding
import com.example.wonderland.model.body.GetHistoryBody
import com.example.wonderland.model.entity.CustomerHistory
import com.example.wonderland.model.reponse.GetCustomerHistoryResponse
import com.example.wonderland.utils.Cache
import com.example.wonderland.utils.MyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerOrderArriving : Fragment() {
    private var _binding: FragmentCustomerOrderArrivingBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerOrderArrivingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.customerOrderArrivingNoInfo.visibility = View.GONE
        loadHistory()
    }

    private fun loadHistory(){
        val token = context?.let { Cache.getToken(it) }
        val response = token?.let { MyAPI.getAPI().getHistory(it, GetHistoryBody("Arriving")) }

        response?.enqueue(object : Callback<GetCustomerHistoryResponse> {
            override fun onResponse(
                call: Call<GetCustomerHistoryResponse>,
                response: Response<GetCustomerHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    val arrHistory: ArrayList<CustomerHistory>? = data?.arrHistory
                    if(arrHistory!!.isNotEmpty()) {
                        val adapter = CustomerOrderTabRCAdapter(arrHistory, false)
                        binding.customerOrderArrivingRC.adapter = adapter
                        binding.customerOrderArrivingRC.layoutManager = LinearLayoutManager( context, LinearLayoutManager.VERTICAL, false)
                        binding.customerOrderArrivingNoInfo.visibility = View.GONE

                        adapter.onItemClick = { customerHistory->
                            OrderDetail(customerHistory,2).show(this@CustomerOrderArriving.childFragmentManager,"bs")
                        }
                    } else {
                        binding.customerOrderArrivingNoInfo.visibility = View.VISIBLE
                    }
                }
            }

            override fun onFailure(call: Call<GetCustomerHistoryResponse>, t: Throwable) {
                if(isAdded){
                    Toast.makeText(context, "Fail connection to server", Toast.LENGTH_LONG).show()
                    t.printStackTrace()
                }
            }
        })
    }
}