package com.example.wonderland.activity


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wonderland.adapter.OrderDetailAdapter
import com.example.wonderland.databinding.FragmentOrderDetailBinding
import com.example.wonderland.model.body.CommentBody
import com.example.wonderland.model.entity.CustomerBookHistory
import com.example.wonderland.model.entity.CustomerHistory
import com.example.wonderland.model.reponse.CommentResponse
import com.example.wonderland.utils.Cache
import com.example.wonderland.utils.MyAPI


import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Double.parseDouble
import java.text.DecimalFormat
import java.text.NumberFormat


class OrderDetail(private var arrHistory: CustomerHistory,private var type:Int): BottomSheetDialogFragment(){
    private lateinit var _binding: FragmentOrderDetailBinding
    private val binding get() = _binding
    lateinit var adapter: OrderDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        adapter=OrderDetailAdapter(arrHistory.product,type)
        _binding.orderDetailItem.adapter=adapter
        val context_ = container?.context
        _binding.orderDetailItem.layoutManager=LinearLayoutManager(context_)

        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            context,
            DividerItemDecoration.VERTICAL
        )
        _binding.orderDetailItem.addItemDecoration(itemDecoration)
        val formatter: NumberFormat = DecimalFormat("#,###")

        val tmp = arrHistory.total
        println(tmp)

        _binding.customerOrderTotalMoney.text=formatter.format(tmp)

        adapter.onItemClick={ s, _, ratingBar, comment, reviewPanel, notification->
            val rating:Double = parseDouble(ratingBar.rating.toString())
            val comment_:String =comment.text.toString()
            if(comment_==""){
                Toast.makeText(context, "You need to comment in order to complete review", Toast.LENGTH_LONG).show()
            }else{
                //Toast.makeText(context, rating.toString(), Toast.LENGTH_LONG).show()
                postComment(s,rating,comment_,reviewPanel,notification)
            }

        }
        return binding.root
    }
    private fun postComment (book: CustomerBookHistory, rating:Double, comment_:String, reviewPanel: LinearLayout, notification:TextView){

        val token = context?.let { Cache.getToken(it) }
        val response = MyAPI.getAPI().postComment(token.toString(), CommentBody(book._id,rating,comment_,arrHistory._id))

        response.enqueue(object : Callback<CommentResponse> {
            override fun onResponse(
                call: Call<CommentResponse>,
                response: Response<CommentResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    if(data?.exitcode==0){
                        reviewPanel.visibility= View.GONE
                        book.isReviewed=true
                        Toast.makeText(context, "Thanks for your review", Toast.LENGTH_LONG).show()
                        notification.visibility=View.VISIBLE
                    }
                }

            }

            override fun onFailure(call: Call<CommentResponse>, t: Throwable) {
                Toast.makeText(context, "Fail connection to server", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }
        })
    }
}