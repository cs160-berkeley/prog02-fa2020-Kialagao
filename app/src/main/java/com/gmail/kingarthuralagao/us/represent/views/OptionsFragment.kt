package com.gmail.kingarthuralagao.us.represent.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.databinding.FragmentOptionsBinding
import java.lang.RuntimeException

class OptionsFragment : Fragment(), View.OnClickListener {

    private val TAG = javaClass.simpleName
    lateinit var binding : FragmentOptionsBinding
    private val doneImage : Bitmap by lazy {
        BitmapFactory.decodeResource(resources, R.drawable.ic_done_white_48dp)
    }

    private var onButtonClick : OnButtonClick? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentOptionsBinding.inflate(layoutInflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.currentLocationBtn.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnButtonClick) {
            onButtonClick = context
        } else {
            throw RuntimeException("Must implement")
        }
    }

    override fun onDetach() {
        super.onDetach()
        onButtonClick = null
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            binding.currentLocationBtn.id -> {
                binding.currentLocationBtn.startAnimation()
                onButtonClick!!.onCurrentLocationBtnClick()
            }
        }
    }

    interface OnButtonClick {
        fun onCurrentLocationBtnClick()
        fun onSearchLocationBtnClick()
    }

    fun onDataFetched() {
        binding.currentLocationBtn.doneLoadingAnimation(R.color.colorAccent, doneImage)
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished <= 1000L) {
                    binding.currentLocationBtn.revertAnimation {
                        binding.currentLocationBtn.background = resources.getDrawable(
                            R.drawable.current_location_btn,
                            null
                        )
                    }
                }
            }

            override fun onFinish() {}
        }.start()
    }
}
