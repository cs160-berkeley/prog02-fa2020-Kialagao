package com.gmail.kingarthuralagao.us.represent.views

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.databinding.FragmentElectionInformationBinding
import com.gmail.kingarthuralagao.us.represent.models.voterinfo.PollingLocation
import com.gmail.kingarthuralagao.us.represent.models.voterinfo.VoterInfoResult
import com.gmail.kingarthuralagao.us.represent.viewmodels.ElectionInformationViewModel
import com.gmail.kingarthuralagao.us.represent.viewmodels.Resource
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_election_information.*


private const val ADDRESS = "Address"
/**
 * A simple [Fragment] subclass.
 * Use the [ElectionInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ElectionInformationFragment : Fragment() {

    interface IIconClickListener {
        fun onIconClick(tag: String)
    }

    companion object {
        fun newInstance(address: String) = ElectionInformationFragment().apply {
            arguments = Bundle().apply {
                putString(ADDRESS, address)
            }
        }
    }

    private val TAG = javaClass.simpleName
    private var address = ""
    private var pagerAdapter : PagerAdapter? = null
    private lateinit var tabTitles : MutableList<String>
    private lateinit var binding: FragmentElectionInformationBinding
    private var iconClickListener: IIconClickListener? = null
    private lateinit var viewModel : ElectionInformationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        address = requireArguments().getString(ADDRESS).toString()
        binding = FragmentElectionInformationBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(ElectionInformationViewModel::class.java)
        viewModel.representativesMutableLiveData.observe(this, representativesObserver)
        viewModel.voterInfoMutableLiveData.observe(this, voterInfoObserver)
        tabTitles = mutableListOf(
            resources.getString(R.string.representatives),
            resources.getString(R.string.polling_locations),
            resources.getString(R.string.drop_off_locations)
        )
        setViewPager()
        getElectionInformation(address)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.addressTv.text = address
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myLocationBtn.setOnClickListener {iconClickListener?.onIconClick(
            binding.myLocationBtn.tag.toString()
        )}
        binding.searchBtn.setOnClickListener {iconClickListener?.onIconClick(
            binding.searchBtn.tag.toString()
        )}

        binding.randomLocationBtn.setOnClickListener {iconClickListener?.onIconClick(
            binding.randomLocationBtn.tag.toString()
        )}

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                try {
                    val pollingLocationsFragment = pagerAdapter?.getFragment(1) as PollingLocationsFragment
                } catch (e : Exception) {
                    if (viewModel.voterInfoMutableLiveData.value?.data != null) {
                        viewModel.fetchVoterInformation(
                            address,
                            resources.getString(R.string.api_key)
                        )
                    }
                }

                try {
                    val dropOffLocationsFragment = pagerAdapter?.getFragment(2) as DropOffLocationsFragment
                } catch (e: Exception) {
                    if (viewModel.voterInfoMutableLiveData.value?.data != null) {
                        viewModel.fetchVoterInformation(address, resources.getString(R.string.api_key))
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IIconClickListener) {
            iconClickListener = context
        } else {
            throw RuntimeException("Must implement IconClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        pagerAdapter = null
        iconClickListener = null
    }

    fun showErrorMsg(s: String) {
        Toasty.error(requireContext(), s, Toast.LENGTH_LONG, true).show()
    }

    private fun setViewPager() {
        pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    fun updateVoterInfo(a: String) {
        address = a
        getElectionInformation(address)
    }

    fun cleanAdapter() {
        pagerAdapter = null
    }

    private fun getElectionInformation(address: String) {
        viewModel.fetchRepresentatives(address, resources.getString(R.string.api_key))
        if (pagerAdapter!!.arrayList.size >=2) {
            viewModel.fetchVoterInformation(address, resources.getString(R.string.api_key))
        }
    }

    private val representativesObserver = Observer<Resource<MutableList<MutableMap<String, String>>>> {
        if (!it.data.isNullOrEmpty() && it.message!!.isEmpty()) {
            for (i in it.data!!) {
                Log.i(TAG, i["name"]!!)
            }

            val representativeFragment = pagerAdapter?.getFragment(0) as RepresentativesFragment
            representativeFragment.updateView(it.data!!)
            binding.addressTv.text = address
        } else {
            showErrorMsg(it.message!!)
        }
    }

    private val voterInfoObserver = Observer<Resource<VoterInfoResult>> {
        Log.d(TAG, "inVoterInfoObserver")

        if (pagerAdapter!!.arrayList.size >= 2) {
            val pollingLocationsFragment = pagerAdapter?.getFragment(1) as PollingLocationsFragment
            if (it.data != null && !it.data!!.pollingLocations.isNullOrEmpty()) {
                it.data!!.pollingLocations?.let { pollingLocationsList ->
                    pollingLocationsFragment.updateView(pollingLocationsList)
                }
            } else {
                pollingLocationsFragment.updateView(listOf())
            }


            if (pagerAdapter!!.arrayList.size >= 3) {
                val dropOffLocationsFragment = pagerAdapter?.getFragment(2) as DropOffLocationsFragment
                if (it.data != null && !it.data!!.dropOffLocations.isNullOrEmpty()) {
                    it.data!!.dropOffLocations?.let { dropOffLocationsList ->
                        dropOffLocationsFragment.updateView(dropOffLocationsList)
                    }
                } else {
                    dropOffLocationsFragment.updateView(listOf())
                }
            }
        }
    }

    private inner class PagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {
        val arrayList: ArrayList<Fragment> = ArrayList()
        override fun createFragment(position: Int): Fragment {
            return when(position) {
                0 -> {
                    val representativesFragment = RepresentativesFragment.newInstance(address)
                    addFragment(position, representativesFragment)
                    representativesFragment
                }

                1 -> {
                    Log.d(javaClass.simpleName, "Creating Polling Locatuon Frag")

                    val pollingLocationsList =
                        if (viewModel.voterInfoMutableLiveData.value?.data?.pollingLocations != null) {
                            viewModel.voterInfoMutableLiveData.value?.data?.pollingLocations
                        } else {
                            mutableListOf()
                        }
                    val pollingLocationsFragment = PollingLocationsFragment.newInstance(false, pollingLocationsList!!)
                    addFragment(position, pollingLocationsFragment)
                    pollingLocationsFragment
                }

                else -> {
                    val dropOffLocationsList =
                        if (viewModel.voterInfoMutableLiveData.value?.data?.dropOffLocations != null) {
                            viewModel.voterInfoMutableLiveData.value?.data?.dropOffLocations
                        } else {
                            mutableListOf()
                        }
                    val dropOffLocationsFragment = DropOffLocationsFragment.newInstance(false, dropOffLocationsList!!)
                    addFragment(position, dropOffLocationsFragment)
                    dropOffLocationsFragment
                }
            }
        }

        override fun getItemCount(): Int {
            return tabTitles.size
        }

        fun addFragment(position: Int, fragment: Fragment) {
            arrayList.add(position, fragment)
        }

        fun getFragment(position: Int) : Fragment{
            return arrayList[position]
        }
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