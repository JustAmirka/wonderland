package com.example.wonderland.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wonderland.adapter.CartAdapter
import com.example.wonderland.databinding.ActivityCartBinding
import com.example.wonderland.model.body.DeleteCartBody
import com.example.wonderland.model.entity.BooksInCart
import com.example.wonderland.model.reponse.GetCartResponse
import com.example.wonderland.model.reponse.GetUpdateResponse
import com.example.wonderland.utils.Cache
import com.example.wonderland.utils.MyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Integer.parseInt
import java.text.DecimalFormat
import java.text.NumberFormat


class Cart : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    lateinit var arrBooks: ArrayList<BooksInCart>
    lateinit var adapter: CartAdapter
    var choosen: HashMap<String, BooksInCart> = HashMap()
    private var total: Int = 0

    private val formatter: NumberFormat = DecimalFormat("#,###")
    var tmp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cartActive.isVisible = false
        binding.noCart.isVisible = false
        binding.goShopping.setOnClickListener {
            finish()
        }
        loadListCart()
    }

    private fun loadListCart() {
        val token = Cache.getToken(this)
        if (token === null) {
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            val response = MyAPI.getAPI().getCart(token.toString())
            arrBooks = ArrayList()

            response.enqueue(object : Callback<GetCartResponse> {
                override fun onResponse(
                    call: Call<GetCartResponse>,
                    response: Response<GetCartResponse>
                ) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        if (data?.arrBooks.isNullOrEmpty()) {
                            binding.cartActive.isVisible = false
                            binding.noCart.isVisible = true
                        } else {
                            if (data != null) {
                                setUpCart(data)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<GetCartResponse>, t: Throwable) {
                    Toast.makeText(this@Cart, "Fail connection to server", Toast.LENGTH_LONG).show()
                    t.printStackTrace()
                }
            })
            binding.checkout.setOnClickListener() {
                val intent = Intent(this, Checkout::class.java)
                if (choosen.size != 0) {
                    intent.putExtra("map", choosen)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Choose at least one to checkout", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun setUpCart(data: GetCartResponse) {
        binding.cartActive.isVisible = true
        binding.noCart.isVisible = false

        for (item: BooksInCart in data.arrBooks!!) {
            arrBooks.add(item)
        }

        var seller = ""

        val TotalView: TextView = binding.total
        val hiddenView: TextView = binding.tempTotalView
        tmp = total.toString()
        TotalView.text = formatter.format(parseInt(tmp))

        hiddenView.text = total.toString()
        adapter = CartAdapter(arrBooks, hiddenView, TotalView)
        val listCartitem: RecyclerView = binding.listCartitem
        listCartitem.adapter = adapter
        listCartitem.layoutManager = LinearLayoutManager(this@Cart)
        adapter.onItemClick = { s, position ->
            if (choosen[s._id] != null) {
                total -= s.price * s.quantity
                hiddenView.text =
                    (parseInt(hiddenView.text.toString()) - s.price * s.quantity).toString()
                //total.toString()
                tmp = hiddenView.text as String
                TotalView.text = formatter.format(parseInt(tmp))

                choosen.remove(s._id)
                seller = ""
            }
            val alertDialog: AlertDialog? = this.let {
                val builder = AlertDialog.Builder(this@Cart!!)
                builder.apply {
                    setPositiveButton("Remove", DialogInterface.OnClickListener { dialog, id ->
                        deleteData(s, position)

                    })
                    setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        //do sth
                    })
//                                setIcon(android.R.drawable.ic_dialog_alert)
                    setTitle("Remove this item?")
                }
                builder.create()
            }
            alertDialog!!.show()


        }
        adapter.onCheckClick = { s, _, check_btn ->
            if (choosen.size == 0) {
                choosen[s._id] = s
                seller = s.seller
                total += s.price * s.quantity
                hiddenView.text =
                    (parseInt(hiddenView.text.toString()) + s.price * s.quantity).toString()
                //total.toString()
                tmp = hiddenView.text as String
                TotalView.text = formatter.format(parseInt(tmp))
            } else {
                if (s.seller == seller) {
                    choosen[s._id] = s
                    hiddenView.text =
                        (parseInt(hiddenView.text.toString()) + s.price * s.quantity).toString()
                    //(total+ s.price * s.quantity).toString()
                    tmp = hiddenView.text as String
                    TotalView.text = formatter.format(parseInt(tmp))
                    //(total+s.price*s.quantity).toString()
                } else {
                    check_btn.isChecked = false
                    Toast.makeText(
                        this@Cart,
                        "Different Seller, choose again",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }
        adapter.removeCheck = { s, position ->
            if (choosen[s._id]?.seller == seller) {
                choosen.remove(s._id)
                if (choosen.size == 0)
                    seller = ""
                total -= s.price * s.quantity
                hiddenView.text =
                    (parseInt(hiddenView.text.toString()) - s.price * s.quantity).toString()
                //total.toString()
//                                Log.d("Test",hiddenView.text.toString())
                tmp = hiddenView.text as String
                TotalView.text = formatter.format(parseInt(tmp))
                //(total-s.price*s.quantity).toString()
            }
        }
    }

    private fun deleteData(book: BooksInCart, position: Int) {
        val token = Cache.getToken(this)
        val response = MyAPI.getAPI().deleteCart(token.toString(), DeleteCartBody(book._id))
        response.enqueue(object : Callback<GetUpdateResponse> {
            override fun onResponse(
                call: Call<GetUpdateResponse>,
                response: Response<GetUpdateResponse>
            ) {
                if (response.isSuccessful) {
                    arrBooks.removeAt(position)
                    adapter.notifyItemRemoved(position)

                    if (arrBooks.isEmpty()) {
                        binding.cartActive.isVisible = false
                        binding.noCart.isVisible = true
                    }
                }

            }

            override fun onFailure(call: Call<GetUpdateResponse>, t: Throwable) {
                Toast.makeText(this@Cart, "Fail connection to server", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }
        })
    }



}