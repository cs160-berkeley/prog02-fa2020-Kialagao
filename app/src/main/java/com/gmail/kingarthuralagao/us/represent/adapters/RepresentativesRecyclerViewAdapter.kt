package com.gmail.kingarthuralagao.us.represent.adapters

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gmail.kingarthuralagao.us.represent.R
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import me.samlss.broccoli.Broccoli
import me.samlss.broccoli.BroccoliGradientDrawable
import me.samlss.broccoli.PlaceholderParameter
import java.io.Serializable
import java.lang.Exception


class RepresentativesRecyclerViewAdapter(private var myDataSet: MutableList<MutableMap<String, String>>)
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
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_candidate, parent, false) as MaterialCardView
        val candidateImage = v.findViewById<ImageView>(R.id.candidateImage)
        candidateImage.layoutParams = ConstraintLayout.LayoutParams(
            getScreenWidth() / 3,
            getScreenHeight() / 5
        )
        Log.i(TAG, "onBindViewHolder")

        return CandidateViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val broccoli = Broccoli()
        broccoli.addPlaceholder(
            PlaceholderParameter.Builder()
                .setView((holder as CandidateViewHolder).officeTv)
                .setDrawable(
                    BroccoliGradientDrawable(
                        Color.parseColor("#DDDDDD"),
                        Color.parseColor("#CCCCCC"), 0F, 1000, LinearInterpolator()
                    )
                )
                .build()
        )

        broccoli.addPlaceholder(
            PlaceholderParameter.Builder()
                .setView(holder.nameTv)
                .setDrawable(
                    BroccoliGradientDrawable(
                        Color.parseColor("#DDDDDD"),
                        Color.parseColor("#CCCCCC"), 0F, 1000, LinearInterpolator()
                    )
                )
                .build()
        )

        broccoli.addPlaceholder(
            PlaceholderParameter.Builder()
                .setView(holder.partyTv)
                .setDrawable(
                    BroccoliGradientDrawable(
                        Color.parseColor("#DDDDDD"),
                        Color.parseColor("#CCCCCC"), 0F, 1000, LinearInterpolator()
                    )
                )
                .build()
        )

        broccoli.addPlaceholder(
            PlaceholderParameter.Builder()
                .setView(holder.image)
                .setDrawable(
                    BroccoliGradientDrawable(
                        Color.parseColor("#DDDDDD"),
                        Color.parseColor("#CCCCCC"), 0F, 1000, LinearInterpolator()
                    )
                )
                .build()
        )

        broccoli.show()

        Picasso.get().load(R.drawable.default_image).into(holder.image)
        holder.nameTv.text = myDataSet[position]["name"]
        holder.officeTv.text = myDataSet[position]["office"]
        holder.partyTv.text = when (myDataSet[position]["party"]) {
            "Republican Party" -> "Republican"
            "Democratic Party" -> "Democrat"
            else -> "Independent"
        }
        if (myDataSet[position]["photoUrl"].isNullOrEmpty()) {
            broccoli.clearAllPlaceholders()
            Picasso.get().load(R.drawable.default_image).into(holder.image)
            addAppropriateColors(holder, position)
        } else {
            Picasso.get().load(myDataSet[position]["photoUrl"])
                .into((holder as CandidateViewHolder).image, object : Callback {
                    override fun onSuccess() {
                        Log.i(TAG, "HEllooo")
                        broccoli.clearAllPlaceholders()
                        Picasso
                            .get()
                            .load(myDataSet[position]["photoUrl"])
                            .resize(getScreenWidth() / 3, getScreenHeight() / 5)
                            .into(holder.image)
                        addAppropriateColors(holder, position)
                    }

                    override fun onError(e: Exception?) {
                        broccoli.clearAllPlaceholders()
                        Picasso.get().load(R.drawable.default_image).into(holder.image)
                        addAppropriateColors(holder, position)
                    }
                })
        }
        //addAppropriateColors(holder, position)
    }

    override fun getItemCount(): Int {
        return myDataSet.size
    }

    private fun addAppropriateColors(holder: CandidateViewHolder, position: Int) {
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

    fun setData(newData: MutableList<MutableMap<String, String>>) {
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

