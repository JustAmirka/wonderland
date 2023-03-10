package com.example.wonderland.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.wonderland.R
import com.example.wonderland.activity.OrderDetail
import com.example.wonderland.adapter.CustomerOrderTabRCAdapter
import com.example.wonderland.databinding.FragmentCustomerOrderPendingBinding
import com.example.wonderland.model.body.CancelOrderBody
import com.example.wonderland.model.body.GetHistoryBody
import com.example.wonderland.model.entity.CustomerHistory
import com.example.wonderland.model.reponse.CancelOrderResponse
import com.example.wonderland.model.reponse.GetCustomerHistoryResponse
import com.example.wonderland.utils.Cache
import com.example.wonderland.utils.MyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerOrderPending : Fragment() {
    private var _binding: FragmentCustomerOrderPendingBinding? = null


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
    ): View? {
        _binding = FragmentCustomerOrderPendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.customerOrderPendingNoInfo.visibility = View.GONE
        loadHistory()
    }

    override fun onResume() {
        super.onResume()
        binding.customerOrderPendingNoInfo.visibility = View.GONE
        loadHistory()
    }

    private fun loadHistory(){
        val token = context?.let { Cache.getToken(it) }
        val response = token?.let { MyAPI.getAPI().getHistory(it, GetHistoryBody("Pending")) }

        response?.enqueue(object : Callback<GetCustomerHistoryResponse> {
            override fun onResponse(
                call: Call<GetCustomerHistoryResponse>,
                response: Response<GetCustomerHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    val arrHistory: ArrayList<CustomerHistory>? = data?.arrHistory
                    var adapter = CustomerOrderTabRCAdapter(arrHistory!!, true)
                    adapter.onCancelClick = { customerHistory ->
                        val alertDialog: AlertDialog? = this.let {
                            val builder = AlertDialog.Builder(context!!)
                            builder.apply {
                                setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                                    cancelOrder(customerHistory)
                                })
                                setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                                    //do sth
                                })
//                                setIcon(android.R.drawable.ic_dialog_alert)
                                setTitle("Do you really want to cancel this order?")
                            }
                            builder.create()
                        }
                        alertDialog!!.show()
                    }

                    if(arrHistory.isNotEmpty()) {
                        binding.customerOrderPendingRC.adapter = adapter
                        binding.customerOrderPendingRC.layoutManager = LinearLayoutManager( context, LinearLayoutManager.VERTICAL, false)
                        binding.customerOrderPendingNoInfo.visibility = View.GONE
                    } else {
                        binding.customerOrderPendingRC.adapter = adapter
                        binding.customerOrderPendingRC.layoutManager = LinearLayoutManager( context, LinearLayoutManager.VERTICAL, false)
                        binding.customerOrderPendingNoInfo.visibility = View.VISIBLE
                    }
                    adapter.onItemClick = { customerHistory->
                        OrderDetail(customerHistory,0).show(this@CustomerOrderPending.childFragmentManager,"bs")
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

    private fun cancelOrder(customerHistory: CustomerHistory) {
        val token = context?.let { Cache.getToken(it) }
        val response = token?.let { MyAPI.getAPI().cancelOrder(it, CancelOrderBody(customerHistory._id)) }

        response?.enqueue(object : Callback<CancelOrderResponse> {
            override fun onResponse(
                call: Call<CancelOrderResponse>,
                response: Response<CancelOrderResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()

                    if(data?.exitcode == 0) {
                        loadHistory()
                        val viewPaper: ViewPager2 = activity!!.findViewById(R.id.customerOrderViewPager2)
                        viewPaper.currentItem = 4
                        Toast.makeText(context, "The order is cancelled", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Cannot cancel the order", Toast.LENGTH_LONG).show()
                    }
                }
            }

            override fun onFailure(call: Call<CancelOrderResponse>, t: Throwable) {
                if(isAdded){
                    Toast.makeText(context, "Fail connection to server", Toast.LENGTH_LONG).show()
                    t.printStackTrace()
                }
            }
        })
    }
}