package com.example.animaly.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.animaly.R
import com.example.animaly.activities.models.User
import com.example.animaly.activities.util.ApiClient
import com.example.animaly.activities.util.UserSession
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        val inscrireId = findViewById<LinearLayout>(R.id.s_inscrire_layout)
        inscrireId.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        val email_et = findViewById<EditText>(R.id.email_et)
        val mdp_et = findViewById<EditText>(R.id.mdp_et)

        mdp_et.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                val DRAWABLE_RIGHT = 2

                if (event.action == MotionEvent.ACTION_DOWN) {
                    if (event.rawX >= mdp_et.right - mdp_et.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        // your action here
                        mdp_et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
                        return true
                    }

                }
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= mdp_et.right - mdp_et.compoundDrawables[DRAWABLE_RIGHT].bounds.width()
                    ) {
                        // your action here
                        mdp_et.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                        return true
                    }
                }

                return false
            }
        })

        val close_btn = findViewById<ImageView>(R.id.close_btn)
        close_btn.setOnClickListener{
            onBackPressed()
        }

        val cnx = findViewById<Button>(R.id.connexion_btn)
        cnx.setOnClickListener {

            if(email_et.text.isNullOrBlank())
            {
                email_et.error = getString(R.string.champ_vide)

                return@setOnClickListener
            }

            if(mdp_et.text.isNullOrBlank())
            {
                mdp_et.error = getString(R.string.champ_vide)

                return@setOnClickListener
            }


                ApiClient.apiService.login(
                    User(
                        email = email_et.text.toString(),
                        password = mdp_et.text.toString()
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

                                if (response.code() == 200) {
                                    val sharedPref = getSharedPreferences(
                                        getString(R.string.user), Context.MODE_PRIVATE
                                    )



                                    with(sharedPref.edit()) {
                                        putString(
                                            getString(R.string.token),
                                            content.get("token").asString
                                        )
                                        commit()
                                    }

                                    val user = content.getAsJsonObject("user")

                                    UserSession.id =
                                        user.get("_id").asString


                                    UserSession.prenom =
                                        user.get("firstName").asString

                                    UserSession.nom =
                                        user.get("lastName").asString

                                    UserSession.email =
                                        user.get("email").asString

                                    UserSession.address =
                                        user.get("address").asString

                                    UserSession.lat =
                                        user.get("lat").asString

                                    UserSession.lng =
                                        user.get("lng").asString

                                    val profilePicture = user.get("profilePicture").asString

                                    if (!profilePicture.isEmpty())
                                        UserSession.image = profilePicture


                                    val intent = Intent(
                                        this@LoginActivity,
                                        HomeActivity::class.java
                                    )
                                    startActivity(intent)
                                    finish()

                                }
                                if (response.code() == 201) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Error Occurred: ${response.message()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                if (response.code() == 403) {
                                    Toast.makeText(
                                        this@LoginActivity,
                                        "Error Occurred: ${response.message()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }


                            } else {

                                Toast.makeText(
                                    this@LoginActivity,
                                    "Error Occurred: ${response.message()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                )



        }

    }

}



