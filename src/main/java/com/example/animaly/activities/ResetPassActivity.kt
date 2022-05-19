package com.example.animaly.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.animaly.R
import com.example.animaly.activities.util.ApiClient
import com.example.animaly.activities.util.UserSession
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPassActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pass)

        supportActionBar?.hide()


        val backBtn = findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            onBackPressed()

        }

        val ancien_mdp_et = findViewById<EditText>(R.id.ancien_mdp_et)
        val new_mdp_et = findViewById<EditText>(R.id.new_mdp_et)
        val confirm_mdp_et = findViewById<EditText>(R.id.confirm_mdp_et)

        val change_pass_btn = findViewById<Button>(R.id.change_pass_btn)

        change_pass_btn.setOnClickListener{
            if(ancien_mdp_et.text.isNullOrBlank())
            {
                ancien_mdp_et.error = getString(R.string.champ_vide)
                return@setOnClickListener

            }
            if(new_mdp_et.text.isNullOrBlank())
            {
                ancien_mdp_et.error = getString(R.string.champ_vide)
                return@setOnClickListener

            }
            if(confirm_mdp_et.text.isNullOrBlank())
            {
                ancien_mdp_et.error = getString(R.string.champ_vide)
                return@setOnClickListener

            }

            if(new_mdp_et.text.toString() != confirm_mdp_et.text.toString() )
            {
                Toast.makeText(this,getString(R.string.confirm_mdp_incorrect),Toast.LENGTH_LONG).show()
                return@setOnClickListener

            }
            val params = HashMap<String?, String?>()
            params["email"] = UserSession.email
            params["oldPassword"] = ancien_mdp_et.text.toString()
            params["newPassword"] = new_mdp_et.text.toString()

            ApiClient.apiService.changerMdp(params).enqueue(
                object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        t.printStackTrace()

                    }
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {
                            val content = response.body()

                            if(response.code() == 201)
                            {

                                onBackPressed()

                            }

                            Toast.makeText(this@ResetPassActivity,content.get("message").asString, Toast.LENGTH_LONG).show()

                        }
                        else {
                            val content = response.body()

                            println(content)


                        }




                    }
                }
            )

        }

    }
}