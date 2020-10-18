package com.gmail.kingarthuralagao.us.represent.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.adapters.DropOffLocationsRvAdapter
import com.gmail.kingarthuralagao.us.represent.adapters.PollingLocationsRvAdapter
import com.gmail.kingarthuralagao.us.represent.databinding.FragmentDropOffLocationsBinding
import com.gmail.kingarthuralagao.us.represent.models.voterinfo.DropOffLocation
import java.io.Serializable

private const val HAS_RESULT = "hasResult"
private const val LIST = "list"
/**
 * A simple [Fragment] subclass.
 * Use the [PollingLocationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DropOffLocationsFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(hasResult: Boolean, list : List<DropOffLocation>) =
            DropOffLocationsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(HAS_RESULT, hasResult)
                    putSerializable(LIST, list as Serializable)
                }
            }
    }

    private var hasResult = false
    private var dropOffLocationsList = listOf<DropOffLocation>()
    private lateinit var binding : FragmentDropOffLocationsBinding
    private var recyclerViewAdapter : DropOffLocationsRvAdapter = DropOffLocationsRvAdapter(listOf())
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var itemDecorator : DividerItemDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hasResult = it.getBoolean(HAS_RESULT)
            dropOffLocationsList = it.getSerializable(LIST) as List<DropOffLocation>
        }
        recyclerViewAdapter = DropOffLocationsRvAdapter(dropOffLocationsList)
        binding = FragmentDropOffLocationsBinding.inflate(layoutInflater)
        viewManager = LinearLayoutManager(requireContext())
        itemDecorator = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(resources.getDrawable(R.drawable.recyclerview_divider, null))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.binding.recyclerView.apply {
            addItemDecoration(itemDecorator)
            layoutManager = viewManager
            adapter = recyclerViewAdapter
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.visibility = View.INVISIBLE
        binding.hasResultTv.visibility = View.VISIBLE
    }

    fun updateView(dropOffLocations: List<DropOffLocation>) {
        if (dropOffLocations.isNullOrEmpty()) {
            hasResult = false
            binding.hasResultTv.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.INVISIBLE
        } else {
            hasResult = true
            binding.hasResultTv.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.VISIBLE
        }
        recyclerViewAdapter.setData(dropOffLocations)
    }
}