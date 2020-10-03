package com.gmail.kingarthuralagao.us.represent.views

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.kingarthuralagao.us.represent.adapters.RepresentativesRecyclerViewAdapter
import com.gmail.kingarthuralagao.us.represent.adapters.getScreenHeight
import com.gmail.kingarthuralagao.us.represent.databinding.FragmentRepresentativesBinding
import java.lang.RuntimeException

class RepresentativesFragment : Fragment() {

    private val TAG = javaClass.simpleName
    lateinit var binding : FragmentRepresentativesBinding
    private lateinit var viewManager: LinearLayoutManager
    lateinit var viewAdapter : RepresentativesRecyclerViewAdapter
    private lateinit var itemDecorator : VerticalSpaceItemDecoration
    private var iRepresentativesFragmentListener: IRepresentativesFragmentListener? = null
    private lateinit var address : String

    companion object {
        fun newInstance(viewAdapter: RepresentativesRecyclerViewAdapter, address: String?): RepresentativesFragment {
            val fragmentDemo = RepresentativesFragment()
            val args = Bundle()
            args.putSerializable("adapter", viewAdapter)
            args.putString("address", address)
            fragmentDemo.arguments = args
            return fragmentDemo
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentRepresentativesBinding.inflate(layoutInflater)
        viewManager = LinearLayoutManager(requireContext())
        viewAdapter = requireArguments().getSerializable("adapter") as RepresentativesRecyclerViewAdapter
        address = requireArguments().getString("address") as String
        itemDecorator = VerticalSpaceItemDecoration(96)

        Toast.makeText(requireContext(), "Height: ${getScreenHeight()}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.representativesRv.apply {
            addItemDecoration(itemDecorator)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        binding.addressTv.text = address
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myLocationBtn.setOnClickListener {iRepresentativesFragmentListener?.onIconClick(binding.myLocationBtn.tag.toString())}
        binding.searchBtn.setOnClickListener {iRepresentativesFragmentListener?.onIconClick(binding.searchBtn.tag.toString())}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IRepresentativesFragmentListener) {
            iRepresentativesFragmentListener = context
        } else {
            throw RuntimeException("must implement")
        }
    }

    override fun onDetach() {
        super.onDetach()
        iRepresentativesFragmentListener = null
    }

    interface IRepresentativesFragmentListener {
        fun onCardViewItemClick(position : Int)
        fun onIconClick(tag : String)
    }
}

class VerticalSpaceItemDecoration(private val verticalSpaceHeight: Int) : RecyclerView.ItemDecoration() {
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