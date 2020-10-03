package com.gmail.kingarthuralagao.us.represent.views

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.adapters.RepresentativesRecyclerViewAdapter
import com.gmail.kingarthuralagao.us.represent.adapters.getScreenHeight
import com.gmail.kingarthuralagao.us.represent.databinding.ActivityRepresentativesBinding
import com.gmail.kingarthuralagao.us.represent.viewmodels.LocationActivityViewModel
import kotlinx.android.synthetic.main.activity_representatives.*


class RepresentativesActivity : AppCompatActivity() {
    val myDataset = arrayOf(
        arrayOf("Kamala", "Senate", "Democrat"), arrayOf(
            "Kamala",
            "Senate",
            "Democrat"
        ), arrayOf(
            "Kamala",
            "Senate",
            "Democrat"
        ), arrayOf(
            "Kamala",
            "Senate",
            "Democrat"
        ),
        arrayOf(
            "Kamala",
            "Senate",
            "Democrat"
        )
    )

    private lateinit var binding : ActivityRepresentativesBinding
    private lateinit var viewModel : LocationActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRepresentativesBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(LocationActivityViewModel::class.java)
        setContentView(binding.root)

        setSupportActionBar(repToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val viewManager = LinearLayoutManager(this)
        val viewAdapter = RepresentativesRecyclerViewAdapter(myDataset)
        val itemDecorator = VerticalSpaceItemDecoration(96)

        binding.representativesRv.apply {
            addItemDecoration(itemDecorator)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        Toast.makeText(this, "Height: ${getScreenHeight()}", Toast.LENGTH_SHORT).show()
    }
}

class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = verticalSpaceHeight;
    }
}