package com.example.animaly.activities.ui.panier

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.animaly.R
import com.example.animaly.activities.adapters.HistoriqueAdapter
import com.example.animaly.activities.models.PanierItem
import com.example.animaly.activities.ui.home.PanierViewModel
import com.example.animaly.activities.util.ApiClient
import com.example.animaly.activities.util.UserSession
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PanierFragment : Fragment() {

    private lateinit var panierViewModel: PanierViewModel

    var total : Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        panierViewModel =
            ViewModelProvider(this).get(PanierViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_panier, container, false)

        val backBtn = root.findViewById<ImageButton>(R.id.back_btn)
        backBtn.setOnClickListener {
            activity?.onBackPressed()

        }
        val historiqueArr =  arrayListOf<PanierItem>()
        val historiqueRv = root.findViewById<RecyclerView>(R.id.panier_rv)
        val historiqueAdapter = HistoriqueAdapter(historiqueArr)

        val params = HashMap<String?, String?>()
        params["user"] = UserSession.id

        ApiClient.apiService.getPanier(params).enqueue(
            object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    t.printStackTrace()

                }
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        val content = response.body()

                        if(response.code() == 200)
                        {

                            if(!content.get("panier").isJsonNull)
                            {
                                val produits = content.getAsJsonObject("panier").getAsJsonArray("produits")

                                produits.forEach {
                                    val item = it.asJsonObject

                                    total += item.get("price").asDouble

                                    val produit = PanierItem(item.get("_id").asString,item.get("name").asString, 1)


                                    historiqueArr.add(produit)
                                }

                                historiqueAdapter.notifyDataSetChanged()


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


        val layoutManager = LinearLayoutManager(root.context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL

        historiqueRv.layoutManager = layoutManager
        historiqueRv.adapter = historiqueAdapter

        val editText = EditText(root.context)
        editText.inputType = InputType.TYPE_CLASS_NUMBER

        val paiement_btn = root.findViewById<Button>(R.id.paiement_panier_btn)
        val builder = AlertDialog.Builder(root.context)

        builder.setMessage(root.context.getString(R.string.entrer_code_securite))
            .setView(editText)
            .setCancelable(false)
            .setNegativeButton(root.context.getString(R.string.confirmer)) { dialog, id ->

                val cvv = editText.text.toString();

                ApiClient.apiService.verifierCarte(UserSession.id, cvv).enqueue(
                    object : Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                            t.printStackTrace()

                        }
                        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                            if (response.isSuccessful && response.body() != null) {

                                val content = response.body()

                                if(response.code() == 200)
                                {

                                    if(content.get("correct").asBoolean)
                                    {
                                        ApiClient.apiService.ajoutercommande(UserSession.id, total.toString()).enqueue(
                                            object : Callback<JsonObject> {
                                                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                                                    t.printStackTrace()

                                                }
                                                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                                    if (response.isSuccessful && response.body() != null) {

                                                        val content = response.body()

                                                        if(response.code() == 200)
                                                        {

                                                            historiqueArr.clear()
                                                            historiqueAdapter.notifyDataSetChanged()


                                                            Toast.makeText(context,content.get("message").asString,
                                                                Toast.LENGTH_LONG).show()

                                                        }




                                                    }
                                                    else {
                                                        val content = response.body()
                                                        println(content)

                                                    }
                                                }
                                            }
                                        )


                                    }else{
                                        Toast.makeText(context,getString(R.string.code_cvv_incorrect),
                                            Toast.LENGTH_LONG).show()

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
                dialog.dismiss()


            }
            .setPositiveButton(root.context.getString(R.string.non)) { dialog, id ->
                // Dismiss the dialog
                dialog.dismiss()
            }
        val alert = builder.create()



        paiement_btn.setOnClickListener{

            alert.show()

            val nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(ContextCompat.getColor(root.context, R.color.vert))
            val pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.BLACK)
        }



        return root
    }
}