package com.gmail.kingarthuralagao.us.represent.adapters

import android.content.res.Configuration
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gmail.kingarthuralagao.us.represent.R

class RepresentativesRecyclerViewAdapter (private val myDataSet: Array<Array<String>>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class CandidateViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var nameTv : TextView = view.findViewById(R.id.nameTv)
        var officeTv : TextView = view.findViewById(R.id.officeTv)
        var partyTv : TextView = view.findViewById(R.id.partyTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // create a new view
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_candidate, parent, false) as CardView
        val candidateImage = v.findViewById<ImageView>(R.id.candidateImage)
        candidateImage.layoutParams = ConstraintLayout.LayoutParams(
            getScreenWidth() / 3,
            getScreenWidth() / 3)
        // set the view's size, margins, paddings and layout parameters

        return CandidateViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CandidateViewHolder).nameTv.text = myDataSet[position][0]
        holder.officeTv.text = myDataSet[position][1]
        holder.partyTv.text = myDataSet[position][1]
    }

    override fun getItemCount(): Int {
        return myDataSet.size
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