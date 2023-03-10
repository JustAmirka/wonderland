package com.example.wonderland.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.wonderland.model.body.LoginBody
import com.example.wonderland.R
import com.example.wonderland.databinding.ActivityLoginBinding
import com.example.wonderland.model.reponse.LoginResponse
import com.example.wonderland.utils.Cache
import com.example.wonderland.utils.MyAPI
import com.example.wonderland.utils.SHA256.Companion.sha256
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //event
        binding.loginNavRegister.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
        val loginBtn: Button = findViewById(R.id.loginBtn)
        loginBtn.setOnClickListener {
            checkAccount(this)
        }
    }

    private fun checkAccount(context: Context) {
        val usernameStr = binding.emailEntry.text.toString()
        val passwordStr = binding.passEntry.text.toString().sha256()

        val response = MyAPI.getAPI().postLogin(LoginBody(usernameStr, passwordStr))

        response.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    when (data?.exitcode) {
                        0 -> {
                            Intent(context, MainActivity::class.java).also {
                                Log.d("Token saved", Cache.saveToken(context, data.token).toString())
                                startActivity(it)
                                finish()
                            }

                        }
                        104 -> {
                            Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_LONG)
                                .show()
                        }
                        708 -> {
                            Toast.makeText(context, "You cannot login now!", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(context, "Fail connection to server", Toast.LENGTH_LONG).show()
                t.printStackTrace();
            }
        })
    }
}