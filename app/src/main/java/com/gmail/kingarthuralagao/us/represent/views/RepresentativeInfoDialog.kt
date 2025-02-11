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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.adapters.getScreenHeight
import com.gmail.kingarthuralagao.us.represent.adapters.getScreenWidth
import com.gmail.kingarthuralagao.us.represent.databinding.DialogCandidateDetailedViewBinding
import com.squareup.picasso.Picasso

private var linkClickListener : RepresentativeInfoDialog.IOnLinkClickListener? = null
class RepresentativeInfoDialog : DialogFragment() {

    interface IOnLinkClickListener {
        fun onLinkClick(tag : String, link : String)
    }

    companion object {
        fun newInstance(value: HashMap<String, String>): RepresentativeInfoDialog {
            val representativeInfoDialog = RepresentativeInfoDialog()
            val args = Bundle()
            args.putSerializable("representativeInfo", value)
            representativeInfoDialog.arguments = args
            return representativeInfoDialog
        }
    }

    private val TAG = javaClass.simpleName
    private lateinit var binding: DialogCandidateDetailedViewBinding
    private lateinit var representativeInfo : HashMap<String, String>
    private lateinit var representativeDetails : RepresentativeDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogCandidateDetailedViewBinding.inflate(layoutInflater)
        representativeInfo = requireArguments().getSerializable("representativeInfo") as HashMap<String, String>
        representativeDetails = RepresentativeDetails(representativeInfo)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Modify image constraints
        val candidateImageLayout = binding.candidateIv.layoutParams as ViewGroup.MarginLayoutParams
        candidateImageLayout.topMargin = getScreenHeight() / 10
        val candidateImageLayoutAsConstraint = binding.candidateIv.layoutParams as ConstraintLayout.LayoutParams
        candidateImageLayoutAsConstraint.width = (getScreenWidth() / 3) + 32
        candidateImageLayoutAsConstraint.height = getScreenHeight() / 5

        // Modify cardview constraints
        val cardViewLayout = binding.candidateDetailsCv.layoutParams as ViewGroup.MarginLayoutParams
        cardViewLayout.topMargin = getScreenHeight() / 10

        val cardViewLayoutAsConstraint = binding.candidateDetailsCv.layoutParams as ConstraintLayout.LayoutParams
        cardViewLayoutAsConstraint.topToTop = binding.candidateIv.id

        // Modify name constraints
        val nameLayout = binding.candidateNameTv.layoutParams as ViewGroup.MarginLayoutParams
        nameLayout.topMargin = 16 + (getScreenHeight() / 10)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            (binding.contentCandidate.root as ViewGroup).removeViewAt(6)
        } else {
            binding.contentCandidate.youtubeTv.text = representativeDetails.youtube
            binding.contentCandidate.youtubeTv.highlightColor = Color.TRANSPARENT
            binding.contentCandidate.youtubeTv.movementMethod = LinkMovementMethod.getInstance()
        }

        if (representativeDetails.twitter.isEmpty()) {
            (binding.contentCandidate.root as ViewGroup).removeViewAt(5)
        } else {
            binding.contentCandidate.twitterTv.text = representativeDetails.twitter
            binding.contentCandidate.twitterTv.highlightColor = Color.TRANSPARENT
            binding.contentCandidate.twitterTv.movementMethod = LinkMovementMethod.getInstance()
        }

        if (representativeDetails.email.isEmpty()) {
            (binding.contentCandidate.root as ViewGroup).removeViewAt(4)
        } else {
            Log.d(TAG, representativeDetails.email.toString())
            binding.contentCandidate.emailTv.text = representativeDetails.email
            binding.contentCandidate.emailTv.highlightColor = Color.TRANSPARENT
            binding.contentCandidate.emailTv.movementMethod = LinkMovementMethod.getInstance()
        }

        if (representativeDetails.photo.isEmpty()) {
            Picasso
                .get()
                .load(R.drawable.default_image)
                .resize((getScreenWidth() / 3) + 32, getScreenHeight() / 5)
                .into(binding.candidateIv)
        } else {
            Picasso
                .get()
                .load(representativeDetails.photo)
                .resize((getScreenWidth() / 3) + 32, getScreenHeight() / 5)
                .into(binding.candidateIv)
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
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawableResource((android.R.color.transparent))
        }
    }

    inner class RepresentativeDetails(details : HashMap<String, String>) {
        var name : String = ""
        var party : String = ""
        var office : String = ""
        var phone : SpannableString
        var website : SpannableString
        var twitter : SpannableString
        var youtube : SpannableString
        var email : SpannableString
        var photo = ""
        var rawWebsite = ""

        init {
            name = details["name"]!!
            party = details["party"]!!
            office = details["office"]!!
            phone = createClickableSpan(details["phone"]!!)
            rawWebsite = details["website"]!!
            website = createClickableSpan(websiteCleaner(details["website"]!!))

            email = if (details["email"].isNullOrEmpty()) {
                SpannableString("")
            } else {
                createClickableSpan(details["email"]!!)
            }

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
                    if (textView.id == binding.contentCandidate.websiteTv.id) {
                        linkClickListener?.onLinkClick(
                            textView.tag.toString(),
                            rawWebsite
                        )
                    } else {
                        linkClickListener?.onLinkClick(
                            textView.tag.toString(),
                            (textView as TextView).text.toString()
                        )
                    }
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
}