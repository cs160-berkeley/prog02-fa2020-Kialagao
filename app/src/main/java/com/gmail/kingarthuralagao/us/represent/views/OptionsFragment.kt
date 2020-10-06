package com.gmail.kingarthuralagao.us.represent.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.databinding.FragmentOptionsBinding
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_options.*
import java.lang.RuntimeException

class OptionsFragment : Fragment(), View.OnClickListener {

    interface IButtonClickListener {
        fun onCurrentLocationBtnClick()
        fun onSearchLocationBtnClick()
        fun onRandomizeLocation()
    }

    private val TAG = javaClass.simpleName
    lateinit var binding : FragmentOptionsBinding
    private lateinit var activeButton : CircularProgressButton
    private var onButtonClick : IButtonClickListener? = null

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
        binding.searchLocationBtn.setOnClickListener(this)
        binding.randomizeLocationBtn.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IButtonClickListener) {
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
                activeButton = binding.currentLocationBtn
                binding.currentLocationBtn.startAnimation()
                onButtonClick!!.onCurrentLocationBtnClick()
            }
            binding.searchLocationBtn.id -> {
                activeButton = binding.searchLocationBtn
                onButtonClick!!.onSearchLocationBtnClick()
            }

            else -> {
                onButtonClick!!.onRandomizeLocation()
            }
        }
    }

    fun startTimer(resourceID : Int, errorMsg : String) {
        binding.currentLocationBtn.doneLoadingAnimation(R.color.colorAccent,
            BitmapFactory.decodeResource(resources, resourceID))
        MyCountDownTimer(2000, 1000, activeButton, errorMsg).start()
    }

    private class MyCountDownTimer(millisInFuture : Long, countDownInterval : Long,
                                   button : CircularProgressButton, errorMsg : String)
        : CountDownTimer(millisInFuture, countDownInterval) {
        val b = button
        val msg = errorMsg
        override fun onTick(millisUntilFinished: Long) {
            if (millisUntilFinished <= 1000L) {
                b.revertAnimation {
                    if (b.tag == "currentLocationBtn") {
                        b.background = b.context.resources.getDrawable(R.drawable.current_location_btn, null)
                    } else {
                        b.background = b.context.resources.getDrawable(R.drawable.search_location_btn, null)
                    }
                }
            }
        }

        override fun onFinish() {
            showErrorMessage(msg)
        }

        private fun showErrorMessage(s: String) {
            Toasty.error(b.context, s, Toast.LENGTH_LONG, true).show()
        }
    }
}
