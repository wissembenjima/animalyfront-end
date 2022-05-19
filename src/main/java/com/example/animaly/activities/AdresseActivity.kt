package com.example.animaly.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.animaly.R
import com.example.animaly.activities.util.ApiClient
import com.example.animaly.activities.util.UserSession
import com.google.gson.JsonObject
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.search.ui.view.SearchBottomSheetView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdresseActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var adresseTv: TextView

    private lateinit var searchBottomSheetView: SearchBottomSheetView

    private var coordinates : Point? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_adresse)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);


        mapView = findViewById(R.id.map_view)
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {

        }

        searchBottomSheetView = findViewById(R.id.search_view)
        searchBottomSheetView.initializeSearch(
            savedInstanceState,
            SearchBottomSheetView.Configuration()
        )


        adresseTv = findViewById(R.id.adresse_tv)
        val save_btn = findViewById<Button>(R.id.save_adr_btn)

        searchBottomSheetView.addOnSearchResultClickListener { searchResult, responseInfo ->
            val adrTxt = "${searchResult.name}, ${searchResult.address?.country}"
            adresseTv.text = adrTxt
            coordinates = searchResult.coordinate
            searchBottomSheetView.hide()
            if (save_btn.visibility == View.INVISIBLE)
                save_btn.visibility = View.VISIBLE
        }

        searchBottomSheetView.addOnHistoryClickListener { history ->
            val adrTxt = "${history.name}, ${history.address?.country}"
            adresseTv.text = adrTxt
            coordinates = history.coordinate
            searchBottomSheetView.hide()
            if (save_btn.visibility == View.INVISIBLE)
                save_btn.visibility = View.VISIBLE
        }



        save_btn.setOnClickListener{
            if(intent.getStringExtra("from") != "profil")
            {
                val returnIntent = Intent()
                returnIntent.putExtra("adresse", adresseTv.text.toString())
                returnIntent.putExtra("lat", coordinates?.latitude().toString())
                returnIntent.putExtra("lng", coordinates?.longitude().toString())
                setResult(RESULT_OK, returnIntent)
                finish()
            }else{
                val params = HashMap<String?, String?>()
                params["oldEmail"] = UserSession.email
                params["adress"] = adresseTv.text.toString()
                params["lat"] = coordinates?.latitude().toString()
                params["lng"] = coordinates?.longitude().toString()

                ApiClient.apiService.editProfile(params).enqueue(
                    object : Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                            t.printStackTrace()

                        }
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                            if (response.isSuccessful && response.body() != null) {

                                val content = response.body()

                                if(response.code() == 200)
                                {

                                    UserSession.address = adresseTv.text.toString()
                                    UserSession.lat = coordinates?.latitude().toString()
                                    UserSession.lng = coordinates?.longitude().toString()

                                    finish()

                                }

                                Toast.makeText(this@AdresseActivity,content.get("message").asString,
                                    Toast.LENGTH_LONG).show()


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





        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_LOCATION
            )
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private companion object {

        const val PERMISSIONS_REQUEST_LOCATION = 0

        fun Context.isPermissionGranted(permission: String): Boolean {
            return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }

    }
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}