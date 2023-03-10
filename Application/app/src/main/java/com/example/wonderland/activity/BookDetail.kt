package com.example.wonderland.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.graphics.text.LineBreaker.JUSTIFICATION_MODE_INTER_WORD
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wonderland.adapter.CategoryAdapter
import com.example.wonderland.adapter.CommentAdapter
import com.example.wonderland.model.body.AddCartBody
import com.example.wonderland.model.body.GetDetailsBody
import com.example.wonderland.model.entity.Book
import com.example.wonderland.model.reponse.GetAccountResponse
import com.example.wonderland.model.reponse.GetBookDetailResponse
import com.example.wonderland.model.reponse.GetUpdateResponse
import com.example.wonderland.utils.Cache
import com.example.wonderland.utils.MyAPI
import com.example.wonderland.databinding.ActivityBookDetailBinding
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class BookDetail : AppCompatActivity() {
    private lateinit var binding: ActivityBookDetailBinding
    lateinit var arrRelatedBooks: ArrayList<Book>
    lateinit var adapter: CategoryAdapter
    var commentAdapter: CommentAdapter? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val id = intent.getStringExtra("id")
        loadDetail(id!!)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.noReview.isVisible=false
        binding.comments.isVisible=false
    }

    private fun loadDetail(id: String) {
        val response = MyAPI.getAPI().getBookDetail(GetDetailsBody(id))
        val token = Cache.getToken(this)
        val intent = Intent(this, Login::class.java)
        response.enqueue(object : Callback<GetBookDetailResponse> {
            @RequiresApi(Build.VERSION_CODES.Q)
            @SuppressLint("SimpleDateFormat")
            override fun onResponse(
                call: Call<GetBookDetailResponse>,
                response: Response<GetBookDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()!!

                    val formatter: NumberFormat = DecimalFormat("#,###")
                    binding.bookPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    binding.bookTitle.text = data.name
                    Picasso.get().load(data.picture).into(binding.bookImage)
                    Picasso.get().load(data.picture).into(binding.animation)
                    binding.bookDescription.text =
                        HtmlCompat.fromHtml(data.description, HtmlCompat.FROM_HTML_MODE_COMPACT)

                    if(data.comments!!.isNotEmpty()){
                        binding.comments.isVisible=true
                        commentAdapter = CommentAdapter(data.comments)

                        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
                            this@BookDetail,
                            DividerItemDecoration.VERTICAL
                        )
                        binding.comments.addItemDecoration(itemDecoration)
                        binding.sumRev.text= "(${data.comments.size} reviews)"
                        binding.star.text=data.star.toString()
                    } else{
                        binding.noReview.isVisible=true
                        binding.sumRev.isVisible=false
                        binding.starIcon.isVisible=false
                        binding.star.isVisible=false
                    }

//                    binding.bookDescription.setOnClickListener {
//                        binding.bookDescription.toggle()
//                    }

                    binding.bookDescription.justificationMode = JUSTIFICATION_MODE_INTER_WORD

                    binding.bookAuthor.text = data.author

                    if (data.saleprice.toString() == "0") {
                        binding.bookSalePrice.text = formatter.format(data.price)
                        binding.bookPrice.isVisible=false
                    } else {
                        binding.bookPrice.text = formatter.format(data.price)
                        binding.bookSalePrice.text = formatter.format(data.saleprice)
                    }

                    val simpleDate = SimpleDateFormat("yyyy")
                    data.release_year = simpleDate.format(Date())

                    binding.bookRelease.text = data.release_year
                    binding.bookQuantity.text = data.quantity.toString()
                    binding.bookSeller.text = data.seller
                    binding.bookCate.text = data.category

                    arrRelatedBooks= data.relatedBooks!!

                    binding.comments.adapter = commentAdapter
                    binding.comments.layoutManager = LinearLayoutManager(this@BookDetail)


                    adapter=CategoryAdapter(arrRelatedBooks)
                    binding.relatedBooks.adapter = adapter
                    binding.relatedBooks.layoutManager = LinearLayoutManager(this@BookDetail,LinearLayoutManager.HORIZONTAL, false)
                    adapter.setOnItemClickListener(object :CategoryAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val intent=Intent(this@BookDetail,BookDetail::class.java)
                            intent.putExtra("id",arrRelatedBooks[position]._id)
                            startActivity(intent)
                        }
                    })


                    binding.addCartBtn.setOnClickListener {
                        if (token === null) {
                            startActivity(intent)
                        } else {
                            val response_ = MyAPI.getAPI().getAccount(token.toString())

                            response_.enqueue(object : Callback<GetAccountResponse> {
                                override fun onResponse(
                                    call: Call<GetAccountResponse>,
                                    response_: Response<GetAccountResponse>
                                ) {
                                    if (response_.isSuccessful) {
                                        val data_ = response_.body()
                                        Log.d("data", data_?.exitcode.toString())
                                        if (data_?.exitcode == 0) {
                                            if(data_.email == data.seller)
                                            {
                                                Toast.makeText(
                                                    this@BookDetail,
                                                    "You cannot buy your books",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }else{
                                                addCart(token.toString(), data._id)

                                                binding.animation.animate().apply {
                                                    duration = 500
                                                    alpha(0f)
                                                    yBy(1500f)
                                                    scaleYBy(-1f)
                                                    scaleXBy(-1f)
                                                    zBy(90f)
                                                }.withEndAction {
                                                    binding.animation.animate().apply {
                                                        duration = 1
                                                        z(0f)
                                                        alpha(0.5f)
                                                        yBy(-1500f)
                                                        scaleYBy(1f)
                                                        scaleXBy(1f)
                                                    }.start()
                                                }
                                            }

                                        }
                                    } else {
                                        startActivity(intent)
                                    }
                                }

                                override fun onFailure(
                                    call: Call<GetAccountResponse>,
                                    t: Throwable
                                ) {
                                    Toast.makeText(
                                        this@BookDetail,
                                        "Fail connection to server",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    t.printStackTrace()
                                }
                            })
                        }
                    }
                } else {
                    Log.i("test", "fail")
                }
            }

            override fun onFailure(call: Call<GetBookDetailResponse>, t: Throwable) {
                Toast.makeText(this@BookDetail, "Fail connection to server", Toast.LENGTH_LONG)
                    .show()
                t.printStackTrace()
            }

        })
    }

    fun addCart(token: String, id: String) {
        val response1 = MyAPI.getAPI().addCart(token, AddCartBody(id, 1))
        response1.enqueue(object : Callback<GetUpdateResponse> {
            override fun onResponse(
                call: Call<GetUpdateResponse>,
                response: Response<GetUpdateResponse>
            ) {
//                if (response.isSuccessful) {
//                    //Toast.makeText(this@BookDetail, "Add to cart successfully", Toast.LENGTH_LONG).show()
//                }
            }

            override fun onFailure(call: Call<GetUpdateResponse>, t: Throwable) {
                Toast.makeText(this@BookDetail, "Fail connection to server", Toast.LENGTH_LONG)
                    .show()
                t.printStackTrace()
            }


        })
    }
}