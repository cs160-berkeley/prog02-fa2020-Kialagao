package com.gmail.kingarthuralagao.us.represent.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.databinding.DialogCandidateDetailedViewBinding
import com.squareup.picasso.Picasso

private var linkClickListener : RepresentativeInfoDialog.IOnLinkClickListener? = null
class RepresentativeInfoDialog : DialogFragment() {

    private lateinit var binding: DialogCandidateDetailedViewBinding
    private lateinit var representativeInfo : HashMap<String, String>
    private lateinit var representativeDetails : RepresentativeDetails

    companion object {
        fun newInstance(value: HashMap<String, String>): RepresentativeInfoDialog {
            val representativeInfoDialog = RepresentativeInfoDialog()
            val args = Bundle()
            args.putSerializable("representativeInfo", value)
            representativeInfoDialog.arguments = args
            return representativeInfoDialog
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogCandidateDetailedViewBinding.inflate(layoutInflater)
        representativeInfo = requireArguments().getSerializable("representativeInfo") as HashMap<String, String>
        representativeDetails = RepresentativeDetails(representativeInfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding.candidateNameTv.text = representativeDetails.name
        binding.contentCandidate.detailedOfficeTv.text = representativeDetails.office

        binding.contentCandidate.phoneTv.text = representativeDetails.phone
        binding.contentCandidate.phoneTv.highlightColor = Color.TRANSPARENT
        binding.contentCandidate.phoneTv.movementMethod = LinkMovementMethod.getInstance()

        binding.contentCandidate.websiteTv.text = representativeDetails.website
        binding.contentCandidate.websiteTv.highlightColor = Color.TRANSPARENT
        binding.contentCandidate.websiteTv.movementMethod = LinkMovementMethod.getInstance()

        binding.contentCandidate.detailedPartyTv.text = representativeDetails.party
        if (representativeDetails.youtube.isEmpty()) {
            (binding.contentCandidate.root as ViewGroup).removeViewAt(5)
        } else {
            binding.contentCandidate.youtubeTv.text = representativeDetails.youtube
            binding.contentCandidate.youtubeTv.highlightColor = Color.TRANSPARENT
            binding.contentCandidate.youtubeTv.movementMethod = LinkMovementMethod.getInstance()
        }

        if (representativeDetails.twitter.isEmpty()) {
            (binding.contentCandidate.root as ViewGroup).removeViewAt(4)
        } else {
            binding.contentCandidate.twitterTv.text = representativeDetails.twitter
            binding.contentCandidate.twitterTv.highlightColor = Color.TRANSPARENT
            binding.contentCandidate.twitterTv.movementMethod = LinkMovementMethod.getInstance()
        }

        if (representativeDetails.photo.isEmpty()) {
            Picasso.get().load(R.drawable.default_image).into(binding.candidateIv)
        } else {
            Picasso.get().load(representativeDetails.photo).into(binding.candidateIv)
        }

        when (representativeDetails.party) {
            "Republican Party" -> {
                binding.candidateDetailsCv.setCardBackgroundColor(binding.candidateDetailsCv.context.resources.getColor(R.color.republicanRedWithAlpha, null))
            }
            "Democratic Party" -> {
                binding.candidateDetailsCv.setCardBackgroundColor(binding.candidateDetailsCv.context.resources.getColor(R.color.democraticBlueWithAlpha, null))
            }
            else -> {
                binding.candidateDetailsCv.setCardBackgroundColor(binding.candidateDetailsCv.context.resources.getColor(R.color.independentPurpleWithAlpha, null))
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IOnLinkClickListener) {
            linkClickListener = context
        } else {
            throw RuntimeException("Must implement")
        }
    }

    override fun onDetach() {
        super.onDetach()
        linkClickListener = null
    }

    override fun onStart() {
        super.onStart()
        val dialog: Dialog? = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawableResource((android.R.color.transparent))
        }
    }

    class RepresentativeDetails(details : HashMap<String, String>) {
        var name : String = ""
        var party : String = ""
        var office : String = ""
        var phone : SpannableString
        var website : SpannableString
        var twitter : SpannableString
        var youtube : SpannableString
        var photo = ""

        init {
            name = details["name"]!!
            party = details["party"]!!
            office = details["office"]!!
            phone = createClickableSpan(details["phone"]!!)
            website = createClickableSpan(websiteCleaner(details["website"]!!))
            twitter = if (details["twitter"].isNullOrEmpty()) {
                SpannableString("")
            } else {
                createClickableSpan(details["twitter"]!!)
            }

            youtube = if (details["youtube"].isNullOrEmpty()) {
                SpannableString("")
            } else {
                createClickableSpan(details["youtube"]!!)
            }

            photo = if (details["photoUrl"].isNullOrEmpty()) {
                ""
            } else {
                details["photoUrl"]!!
            }
        }

        private fun websiteCleaner(website : String) : String {
            var cleanUrl = website.replace("https://", "")
            cleanUrl = cleanUrl.replace("www.", "")
            return cleanUrl
        }

        private fun createClickableSpan(s : String) : SpannableString{
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(textView: View) {
                    linkClickListener?.onLinkClick(textView.tag.toString(), (textView as TextView).text.toString())
                }
            }
            val startIndex = 0
            val endIndex = s.length
            val spannableString = SpannableString(s)
            spannableString.setSpan(
                clickableSpan,
                startIndex,
                endIndex,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            /*
            val spannableString = SpannableString(s)
            spannableString.setSpan(ClickableSpan(), 0, spannableString.length, 0)*/
            val fcs = ForegroundColorSpan(Color.WHITE)
            spannableString.setSpan(fcs, startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
            return spannableString
        }
    }
    interface IOnLinkClickListener {
        fun onLinkClick(tag : String, link : String)
    }
}