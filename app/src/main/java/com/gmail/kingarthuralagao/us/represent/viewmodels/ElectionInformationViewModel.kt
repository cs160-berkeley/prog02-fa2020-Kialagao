package com.gmail.kingarthuralagao.us.represent.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.gmail.kingarthuralagao.us.represent.repositories.ElectionInformationRepo

class ElectionInformationViewModel : ViewModel() {
    private val electionInformationRepo by lazy {
        ElectionInformationRepo()
    }

    val voterInfoMutableLiveData = electionInformationRepo.voterInfoMutableLiveData
    var representativesMutableLiveData = electionInformationRepo.representativesMutableLiveData

    fun fetchRepresentatives(address : String, key : String) {
        var formattedAddress = ""
        if (address.contains("-")) {
            val index = address.indexOf("-")
            formattedAddress = address.substring(index + 1)
            formattedAddress = formattedAddress.replace("\\s".toRegex(), "+")
        } else {
            formattedAddress = address.replace("\\s".toRegex(), "+")
        }
        Log.d(javaClass.simpleName, formattedAddress)
        electionInformationRepo.getRepresentatives(formattedAddress, key)
    }

    fun fetchVoterInformation(address: String, key: String) {
        electionInformationRepo.getVoterInfo(address, key)
    }
}