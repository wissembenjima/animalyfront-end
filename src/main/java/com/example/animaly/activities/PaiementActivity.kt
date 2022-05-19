package com.example.animaly.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.animaly.R
import com.example.animaly.activities.models.User
import com.example.animaly.activities.util.ApiClient
import com.example.animaly.activities.util.UserSession
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaiementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paiement)

        supportActionBar?.hide()

        val backBtn = findViewById<ImageButton>(R.id.close_btn)
        backBtn.setOnClickListener {
            onBackPressed()

        }

        val num_carte_et = findViewById<EditText>(R.id.num_carte_et)
        val data_exp = findViewById<EditText>(R.id.date_exp_et)
        val cvv = findViewById<EditText>(R.id.cvv_et)

        ApiClient.apiService.getCarte(UserSession.id).enqueue(
            object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    t.printStackTrace()

                }
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {

                        val content = response.body()

                        if(response.code() == 200)
                        {
                            println(content)
                            if(!content.get("card").isJsonNull)
                            {
                                val carte = content.getAsJsonObject("card")
                                num_carte_et.setText(carte.get("numCard").asString)
                                data_exp.setText(carte.get("expDate").asString)

                            }


                        }



                    }
                    else {
                        val content = response.body()
                        println(content)

                    }
                }
            }
        )

        val ajouter_carte_btn = findViewById<Button>(R.id.ajouter_carte_btn)
        ajouter_carte_btn.setOnClickListener{
            if(num_carte_et.text.isNullOrBlank())
            {
                num_carte_et.error = getString(R.string.champ_vide)
                return@setOnClickListener
            }
            if(data_exp.text.isNullOrBlank())
            {
                data_exp.error = getString(R.string.champ_vide)
                return@setOnClickListener
            }
            if(cvv.text.isNullOrBlank())
            {
                cvv.error = getString(R.string.champ_vide)
                return@setOnClickListener
            }


            val params = HashMap<String?, String?>()
            params["user"] = UserSession.id
            params["numCard"] = num_carte_et.text.toString()
            params["expDate"] = data_exp.text.toString()
            params["cvv"] = cvv.text.toString()

            ApiClient.apiService.ajouterCarte(params).enqueue(
                object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        t.printStackTrace()

                    }
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                        if (response.isSuccessful && response.body() != null) {

                            val content = response.body()

                            if(response.code() == 201)
                            {

                                Toast.makeText(this@PaiementActivity,content.get("message").asString,
                                    Toast.LENGTH_LONG).show()

                                finish()
                            }




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