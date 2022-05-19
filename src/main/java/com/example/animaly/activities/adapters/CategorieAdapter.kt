package com.example.animaly.activities.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.animaly.R


class CategorieAdapter(private val dataSet: ArrayList<Array<*>>, private val onClickListener: OnClickListener, private val mContext: Context) :
    RecyclerView.Adapter<CategorieAdapter.ViewHolder>() {


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView
        val closeImageView: ImageView
        val card: CardView

        init {
            // Define click listener for the ViewHolder's View.
            titleTextView = view.findViewById(R.id.categorie_title)
            closeImageView = view.findViewById(R.id.categorie_close)
            card = view.findViewById(R.id.categorie_card)

        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.categorie_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val item = dataSet[position]


        viewHolder.titleTextView.text = item[0] as String

        viewHolder.card.setOnClickListener {

            var isSelected = false


            if(viewHolder.card.cardBackgroundColor.defaultColor == ContextCompat.getColor(mContext, R.color.vert))
            {
                viewHolder.card.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.gris_light))
                viewHolder.titleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.noir))
                viewHolder.closeImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.noir))
            }
            else if(viewHolder.card.cardBackgroundColor.defaultColor == ContextCompat.getColor(mContext, R.color.gris_light))
            {
                viewHolder.card.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.vert))
                viewHolder.titleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.blanc))
                viewHolder.closeImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.blanc))

                isSelected = true
            }
            onClickListener.onClick(item[0] as String, isSelected )

        }
        viewHolder.closeImageView.setOnClickListener {


            val sharedPref = mContext.getSharedPreferences(
                mContext.getString(R.string.search_preference_file_key), Context.MODE_PRIVATE)

            with(sharedPref.edit())
            {
                remove(item[0] as String)
                commit()
            }
            onClickListener.onClick(item[0] as String, false)

            dataSet.removeAt(position)

            notifyDataSetChanged()

        }

        if((item[1] as Boolean))
        {

            viewHolder.card.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.vert))
            viewHolder.titleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.blanc))
            viewHolder.closeImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.blanc))

        }else{
            viewHolder.card.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.gris_light))
            viewHolder.titleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.noir))
            viewHolder.closeImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.noir))


        }





    }
    class OnClickListener(val clickListener: (search: String, visibility: Boolean) -> Unit) {
        fun onClick(search: String, visibility: Boolean) = clickListener(search, visibility)
    }
    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


}
