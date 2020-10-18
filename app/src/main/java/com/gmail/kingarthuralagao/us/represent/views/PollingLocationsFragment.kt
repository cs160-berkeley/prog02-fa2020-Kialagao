package com.gmail.kingarthuralagao.us.represent.views


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.adapters.PollingLocationsRvAdapter
import com.gmail.kingarthuralagao.us.represent.databinding.FragmentPollingLocationsBinding
import com.gmail.kingarthuralagao.us.represent.models.voterinfo.PollingLocation
import java.io.Serializable

private const val HAS_RESULT = "hasResult"
private const val LIST = "list"
/**
 * A simple [Fragment] subclass.
 * Use the [PollingLocationsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PollingLocationsFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(hasResult: Boolean, list : List<PollingLocation>) =
            PollingLocationsFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(HAS_RESULT, hasResult)
                    putSerializable(LIST, list as Serializable)
                }
            }
    }

    private var hasResult = false
    private var pollingLocationsList = listOf<PollingLocation>()
    private lateinit var binding : FragmentPollingLocationsBinding
    private var recyclerViewAdapter : PollingLocationsRvAdapter = PollingLocationsRvAdapter(listOf())
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var itemDecorator : DividerItemDecoration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hasResult = it.getBoolean(HAS_RESULT)
            pollingLocationsList = it.getSerializable(LIST) as List<PollingLocation>
        }
        recyclerViewAdapter = PollingLocationsRvAdapter(pollingLocationsList)
        binding = FragmentPollingLocationsBinding.inflate(layoutInflater)
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

    fun updateView(pollingLocations: List<PollingLocation>) {
        if (pollingLocations.isNullOrEmpty()) {
            hasResult = false
            recyclerViewAdapter.setData(pollingLocations)
            binding.hasResultTv.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.INVISIBLE
        } else {
            hasResult = true
            recyclerViewAdapter.setData(pollingLocations)
            binding.hasResultTv.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.VISIBLE
        }
    }
}



