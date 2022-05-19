package com.example.animaly.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.animaly.R
import com.example.animaly.activities.models.User
import com.example.animaly.activities.util.ApiClient
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    var LAUNCH_ADRESSE_ACTIVITY = 1
    lateinit var adresse : EditText
    lateinit var latitude : String
    lateinit var longitude : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        val nom = findViewById<EditText>(R.id.nom_et)
        val prenom = findViewById<EditText>(R.id.prenom_et)
        val cin = findViewById<EditText>(R.id.cin_et)
        val email = findViewById<EditText>(R.id.email_et)
        adresse = findViewById<EditText>(R.id.adresse_et)
        val tel = findViewById<EditText>(R.id.tel_et)
        val mdp = findViewById<EditText>(R.id.mdp_et)
        val close_btn = findViewById<ImageView>(R.id.close_btn)
        close_btn.setOnClickListener{
            onBackPressed()
        }

        adresse.setOnClickListener{

            val intent = Intent(this@RegisterActivity, AdresseActivity::class.java)
            startActivityForResult(intent, LAUNCH_ADRESSE_ACTIVITY)
        }


        val inscriptionBtn = findViewById<Button>(R.id.inscription_btn)

        inscriptionBtn.setOnClickListener{

            if(nom.text.isNullOrBlank())
            {
                nom.error = getString(R.string.champ_vide)

                return@setOnClickListener
            }

            if(prenom.text.isNullOrBlank())
            {
                prenom.error = getString(R.string.champ_vide)

                return@setOnClickListener
            }
            if(cin.text.isNullOrBlank())
            {
                cin.error = getString(R.string.champ_vide)

                return@setOnClickListener
            }
            if(email.text.isNullOrBlank())
            {
                email.error = getString(R.string.champ_vide)

                return@setOnClickListener
            }
            if(adresse.text.isNullOrBlank())
            {
                adresse.error = getString(R.string.champ_vide)

                return@setOnClickListener
            }
            if(tel.text.isNullOrBlank())
            {
                tel.error = getString(R.string.champ_vide)

                return@setOnClickListener
            }
            if(mdp.text.isNullOrBlank())
            {
                mdp.error = getString(R.string.champ_vide)

                return@setOnClickListener
            }
            ApiClient.apiService.register(
                User(
                    "",
                    prenom.text.toString(),
                    nom.text.toString(),
                    cin.text.toString(),
                    email.text.toString(),
                    adresse.text.toString(),
                    latitude,
                    longitude,
                    mdp.text.toString(),
                    tel.text.toString(),
                    "",
                    "User"
                )
            ).enqueue(
                object : Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                        t.printStackTrace()

                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val content = response.body()

                            if (response.code() == 201) {

                                println(content)

                                val intent = Intent(
                                    this@RegisterActivity,
                                    LoginActivity::class.java
                                )
                                startActivity(intent)
                                finish()

                            }

                            if (response.code() == 403) {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "Error Occurred: ${response.message()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }


                        } else {

                            Toast.makeText(
                                this@RegisterActivity,
                                "Error Occurred: ${response.message()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            )



            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }


        val connexionId = findViewById<LinearLayout>(R.id.connexion_layout)
        connexionId.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_ADRESSE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                val adr = data?.getStringExtra("adresse")
                val lat = data?.getStringExtra("lat")
                val lng = data?.getStringExtra("lng")

                if (adr != null) {
                    adresse.setText(adr)
                }
                if (lat != null) {
                    latitude = lat
                }
                if (lng != null) {
                    longitude = lng
                }
            }
            if (resultCode == RESULT_CANCELED) {
                if(adresse.text.isNullOrBlank())
                Toast.makeText(this@RegisterActivity,getString(R.string.champ_vide),Toast.LENGTH_LONG).show()
            }
        }
    }

}