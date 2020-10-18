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
import com.gmail.kingarthuralagao.us.represent.models.voterinfo.Address
import com.gmail.kingarthuralagao.us.represent.models.voterinfo.PollingLocation
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import me.samlss.broccoli.Broccoli
import me.samlss.broccoli.BroccoliGradientDrawable
import me.samlss.broccoli.PlaceholderParameter
import java.io.Serializable
import java.lang.Exception


class PollingLocationsRvAdapter(private var myDataSet: List<PollingLocation>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Serializable {

    private val TAG = javaClass.simpleName
    class PollingLocationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var locationTv: TextView = view.findViewById(R.id.locationTv)
        var addressTv: TextView = view.findViewById(R.id.addressTv)
        var pollingHrsTv : TextView = view.findViewById(R.id.pollingHrsTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_polling_location, parent, false)
        Log.i(TAG, "onBindViewHolder")

        return PollingLocationViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val h = holder as PollingLocationViewHolder
        h.locationTv.text = myDataSet[position].address?.locationName ?: "Location name not available"
        h.addressTv.text = buildAddress(myDataSet[position]?.address)
        h.pollingHrsTv.text = myDataSet[position].pollingHours ?: "Polling hours not available"
    }

    override fun getItemCount(): Int {
        return myDataSet.size
    }

    private fun buildAddress(address: Address?) : String {
        if (address != null) {
            val line1 = address.line1
            val city = address.city
            val state = address.state
            val zip = address.zip
            return "$line1 $city, $state $zip"
        }
        return "Address not available"
    }

    fun setData(newData: List<PollingLocation>) {
        this.myDataSet = newData
        notifyDataSetChanged()
    }
}

