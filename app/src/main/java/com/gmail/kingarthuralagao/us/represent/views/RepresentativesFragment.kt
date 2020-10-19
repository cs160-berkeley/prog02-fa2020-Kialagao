package com.gmail.kingarthuralagao.us.represent.views

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmail.kingarthuralagao.us.represent.adapters.RepresentativesRecyclerViewAdapter
import com.gmail.kingarthuralagao.us.represent.adapters.getScreenHeight
import com.gmail.kingarthuralagao.us.represent.databinding.FragmentRepresentativesBinding
import es.dmoral.toasty.Toasty


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ADDRESS = "address"
private const val ADAPTER = "adapter"

/**
 * A simple [Fragment] subclass.
 * Use the [Representatives2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RepresentativesFragment : Fragment() {

    interface IClickListener {
        fun onClick(view: View?, position: Int)
    }

    interface IRepresentativesFragmentListener {
        fun onCardViewItemClick(representativeMap: MutableMap<String, String>)
    }

    companion object {
        @JvmStatic
        fun newInstance(address: String) =
            RepresentativesFragment().apply {
                arguments = Bundle().apply {
                    putString(ADDRESS, address)
                }
            }
    }

    private val TAG = javaClass.simpleName
    private var iRepresentativesFragmentListener: IRepresentativesFragmentListener? = null
    private var address: String = ""
    private var viewAdapter = RepresentativesRecyclerViewAdapter(mutableListOf(mutableMapOf()))
    private lateinit var representativesBinding: FragmentRepresentativesBinding
    private lateinit var viewManager: LinearLayoutManager
    private lateinit var itemDecorator : VerticalSpaceItemDecoration
    private lateinit var representativesList : MutableList<MutableMap<String, String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            address = it.getString(ADDRESS).toString()
        }

        val space = (getScreenHeight() - 370 - (3 * (getScreenHeight() / 5)) - (16 * 6)) / 6
        representativesBinding = FragmentRepresentativesBinding.inflate(layoutInflater)
        viewManager = LinearLayoutManager(requireContext())
        itemDecorator = VerticalSpaceItemDecoration(space)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        this.representativesBinding.representativesRv.apply {
            addItemDecoration(itemDecorator)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        representativesBinding.representativesRv.visibility = View.INVISIBLE
        Log.i(TAG, "onCreateView")

        return representativesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        representativesBinding.representativesRv.addOnItemTouchListener(
            RecyclerTouchListener(requireContext(), object : IClickListener {
                override fun onClick(view: View?, position: Int) {
                    val representative = representativesList[position]
                    iRepresentativesFragmentListener?.onCardViewItemClick(representative!!)
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

    private fun showErrorMsg(s: String) {
        Toasty.error(requireContext(), s, Toast.LENGTH_LONG, true).show()
    }

    fun updateView(data: MutableList<MutableMap<String, String>>) {
        representativesList = data
        viewAdapter.setData(data)
        representativesBinding.representativesRv.visibility = View.VISIBLE
    }

    internal class RecyclerTouchListener(
        context: Context?,
        private val clickListener: IClickListener
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
}


