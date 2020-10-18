package com.gmail.kingarthuralagao.us.represent.views

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.adapters.RepresentativesRecyclerViewAdapter
import com.gmail.kingarthuralagao.us.represent.adapters.getScreenHeight
import com.gmail.kingarthuralagao.us.represent.databinding.FragmentRepresentativesBinding
import es.dmoral.toasty.Toasty

class RepresentativesFragment : Fragment() {

    interface ClickListener {
        fun onClick(view: View?, position: Int)
    }

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

    private val TAG = javaClass.simpleName
    lateinit var representativesBinding: FragmentRepresentativesBinding
    private lateinit var viewManager: LinearLayoutManager
    lateinit var viewAdapter : RepresentativesRecyclerViewAdapter
    private lateinit var itemDecorator : VerticalSpaceItemDecoration
    private var iRepresentativesFragmentListener: IRepresentativesFragmentListener? = null
    private lateinit var address : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        representativesBinding = FragmentRepresentativesBinding.inflate(layoutInflater)
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
        Log.i(TAG, "onCreateView")
        representativesBinding.representativesRv.apply {
            addItemDecoration(itemDecorator)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        representativesBinding.addressTv.text = address
        return representativesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        representativesBinding.myLocationBtn.setOnClickListener {iRepresentativesFragmentListener?.onIconClick(
            representativesBinding.myLocationBtn.tag.toString()
        )}
        representativesBinding.searchBtn.setOnClickListener {iRepresentativesFragmentListener?.onIconClick(
            representativesBinding.searchBtn.tag.toString()
        )}

        representativesBinding.randomLocationBtn.setOnClickListener {iRepresentativesFragmentListener?.onIconClick(
            representativesBinding.randomLocationBtn.tag.toString())}

        representativesBinding.representativesRv.addOnItemTouchListener(
            RecyclerTouchListener(requireContext(), object : ClickListener {
                override fun onClick(view: View?, position: Int) {
                    iRepresentativesFragmentListener?.onCardViewItemClick(position)
                }
            })
        )
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
        fun onCardViewItemClick(position: Int)
        fun onIconClick(tag: String)
    }

    fun showErrorMsg(s : String) {
        Toasty.error(requireContext(), s, Toast.LENGTH_LONG, true).show()
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

internal class RecyclerTouchListener(
    context: Context?,
    private val clickListener: RepresentativesFragment.ClickListener?
) :
    RecyclerView.OnItemTouchListener {
    private val gestureDetector: GestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }
        })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildAdapterPosition(child))
        }
        return false
    }
    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}