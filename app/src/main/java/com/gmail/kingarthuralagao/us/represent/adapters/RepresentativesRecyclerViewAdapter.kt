package com.gmail.kingarthuralagao.us.represent.adapters

import android.content.res.ColorStateList
import android.content.res.Configuration
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.card.MaterialCardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.kingarthuralagao.us.represent.R
import java.io.Serializable

class RepresentativesRecyclerViewAdapter (private var myDataSet: MutableList<MutableMap<String, String>>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Serializable {

    private val TAG = javaClass.simpleName
    class CandidateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView = view.rootView as MaterialCardView
        var image : ImageView = view.findViewById(R.id.candidateImage)
        var nameTv : TextView = view.findViewById(R.id.nameTv)
        var officeTv : TextView = view.findViewById(R.id.officeTv)
        var partyTv : TextView = view.findViewById(R.id.partyTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_candidate, parent, false) as MaterialCardView
        val candidateImage = v.findViewById<ImageView>(R.id.candidateImage)
        candidateImage.layoutParams = ConstraintLayout.LayoutParams(
            getScreenWidth() / 3,
            getScreenHeight() / 5)
        // set the view's size, margins, paddings and layout parameters

        return CandidateViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CandidateViewHolder).nameTv.text = myDataSet[position]["name"]
        holder.officeTv.text = myDataSet[position]["office"]
        holder.partyTv.text = when (myDataSet[position]["party"]) {
            "Republican Party" -> "Republican"
            "Democratic Party" -> "Democrat"
            else -> "Independent"
        }
        if (myDataSet[position]["photoUrl"].isNullOrEmpty()) {
            Glide.with(holder.image.context).load(holder.image.context.getDrawable(R.drawable.default_image))
                .into(holder.image)
        } else {
            Glide.with(holder.image.context).load(myDataSet[position]["photoUrl"])
                .into(holder.image)
        }

        addAppropriateColors(holder, position)
    }

    override fun getItemCount(): Int {
        return myDataSet.size
    }

    private fun addAppropriateColors(holder: RepresentativesRecyclerViewAdapter.CandidateViewHolder, position: Int ) {
        val colorID = when {
            myDataSet[position]["party"] == "Republican Party" -> {
                holder.cardView.context.resources.getColor(R.color.republicanRed, null)
            }
            myDataSet[position]["party"] == "Democratic Party" -> {
                holder.cardView.context.resources.getColor(R.color.democratBlue, null)
            }
            else -> {
                holder.cardView.context.resources.getColor(R.color.independentPurple, null)
            }
        }
        holder.cardView.strokeColor = colorID
        holder.cardView.strokeWidth = 16
        holder.cardView.rippleColor = ColorStateList.valueOf(colorID)
        holder.partyTv.setTextColor(colorID)
    }

    fun setData(newData : MutableList<MutableMap<String, String>>) {
        this.myDataSet = newData
        notifyDataSetChanged()
    }
}

fun getScreenWidth(): Int {
    return Resources.getSystem().displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return Resources.getSystem().displayMetrics.heightPixels
}

fun isPortrait() : Boolean {
    return Resources.getSystem().configuration.orientation == Configuration.ORIENTATION_PORTRAIT
}