package com.example.wonderland.activity

import android.content.DialogInterface
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wonderland.adapter.RequestListAdapter
import com.example.wonderland.databinding.ActivityRequestListBinding
import com.example.wonderland.model.body.ChangeRoleBody
import com.example.wonderland.model.entity.Account
import com.example.wonderland.model.reponse.ChangeRoleResponse
import com.example.wonderland.model.reponse.GetRequestListResponse
import com.example.wonderland.utils.Cache
import com.example.wonderland.utils.MyAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestList : AppCompatActivity() {
    private lateinit var binding: ActivityRequestListBinding
    private lateinit var reqList: ArrayList<Account>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.noReqTxt.isVisible=false

        getReqList()
    }

    private fun getReqList() {
        val token = Cache.getToken(this)
        val response = MyAPI.getAPI().getRequestList(token.toString())

        response.enqueue(object : Callback<GetRequestListResponse> {
            override fun onResponse(
                call: Call<GetRequestListResponse>,
                response: Response<GetRequestListResponse>
            ) {
                if (response.isSuccessful) {

                    val data = response.body()

                    when (data?.exitcode) {
                        0 -> {
                            setUpView(data)
                        }
                        403 -> {
                            Toast.makeText(this@RequestList, "Token expired", Toast.LENGTH_LONG)
                                .show()
                        }
                        104 -> {
                            binding.noReqTxt.isVisible=true
                            binding.lsReq.isVisible=false
                        }
                    }
                }
            }

            override fun onFailure(call: Call<GetRequestListResponse>, t: Throwable) {
                Toast.makeText(this@RequestList, "Fail connection to server", Toast.LENGTH_LONG)
                    .show()
                t.printStackTrace()
            }
        })
    }

    private fun acceptReq(reqList: ArrayList<Account>, adapter: RequestListAdapter, pos: Int) {
        val email = reqList[pos].email
        val newRole = 1

        val token = Cache.getToken(this)!!

        val response = MyAPI.getAPI().postChangeRole(token, ChangeRoleBody(email, newRole))
        response.enqueue(object : Callback<ChangeRoleResponse> {
            override fun onResponse(
                call: Call<ChangeRoleResponse>,
                response: Response<ChangeRoleResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    when (data?.exitcode) {
                        0 -> {
                            reqList.removeAt(pos)
                            adapter.notifyItemRemoved(pos)
                            if(reqList.size<1){
                                binding.noReqTxt.isVisible=true
                                binding.lsReq.isVisible=false
                            }
                        }
                        403 -> {
                            Toast.makeText(
                                this@RequestList,
                                "Token expired",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ChangeRoleResponse>, t: Throwable) {
                Toast.makeText(this@RequestList, "Fail connection to server", Toast.LENGTH_LONG)
                    .show()
                t.printStackTrace();
            }
        })
    }

    private fun declineReq(reqList: ArrayList<Account>, adapter: RequestListAdapter, pos: Int) {
        val email = reqList[pos].email
        val newRole = 0

        val token = Cache.getToken(this)!!
        val response =
            MyAPI.getAPI().postChangeRole(token, ChangeRoleBody(email, newRole))

        response.enqueue(object : Callback<ChangeRoleResponse> {
            override fun onResponse(
                call: Call<ChangeRoleResponse>,
                response: Response<ChangeRoleResponse>
            ) {
                if (response.isSuccessful) {
                    val data = response.body()
                    when (data?.exitcode) {
                        0 -> {
                            reqList.removeAt(pos)
                            adapter.notifyItemRemoved(pos)
                            if(reqList.size<1){
                                binding.noReqTxt.isVisible=true
                                binding.lsReq.isVisible=false
                            }
                        }
                        403 -> {
                            Toast.makeText(
                                this@RequestList,
                                "Token expired",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ChangeRoleResponse>, t: Throwable) {
                Toast.makeText(this@RequestList, "Fail connection to server", Toast.LENGTH_LONG)
                    .show()
                t.printStackTrace();
            }
        })
    }


    fun setUpView(dataTmp: GetRequestListResponse) {
        reqList = dataTmp.reqList
        val adapter = RequestListAdapter(reqList)
        binding.lsReq.adapter = adapter
        binding.lsReq.layoutManager = LinearLayoutManager(this@RequestList)
        val itemDecoration: RecyclerView.ItemDecoration = DividerItemDecoration(
            this@RequestList,
            DividerItemDecoration.VERTICAL
        )
        binding.lsReq.addItemDecoration(itemDecoration)

        adapter.onAccButtonClick = { pos ->
            val alertDialog: AlertDialog? = this.let {
                val builder = AlertDialog.Builder(this@RequestList)
                builder.apply {
                    setPositiveButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        acceptReq(reqList, adapter, pos)
                    })
                    setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        //do sth
                    })
//                                setIcon(android.R.drawable.ic_dialog_alert)
                    setTitle("Do you want to upgrade this customer")
                }
                builder.create()
            }
            alertDialog!!.show()
        }

        adapter.onDecButtonClick = { pos ->
            val alertDialog: AlertDialog? = this.let {
                val builder = AlertDialog.Builder(this@RequestList)
                builder.apply {
                    setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, id ->
                        declineReq(reqList, adapter, pos)
                    })
                    setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->
                        //do sth
                    })
//                                setIcon(android.R.drawable.ic_dialog_alert)
                    setTitle("Do you want to reject this request")
                }
                builder.create()
            }
            alertDialog!!.show()
        }
    }
}