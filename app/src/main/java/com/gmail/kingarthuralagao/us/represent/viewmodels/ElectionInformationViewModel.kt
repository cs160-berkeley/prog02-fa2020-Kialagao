package com.gmail.kingarthuralagao.us.represent.viewmodels

import androidx.lifecycle.ViewModel
import com.gmail.kingarthuralagao.us.represent.repositories.ElectionInformationRepo

class ElectionInformationViewModel : ViewModel() {
    private val electionInformationRepo by lazy {
        ElectionInformationRepo()
    }

    val voterInfoMutableLiveData = electionInformationRepo.voterInfoMutableLiveData
    var representativesMutableLiveData = electionInformationRepo.representativesMutableLiveData

    fun fetchRepresentatives(address : String, key : String) {
        val formattedAddress = address.replace("\\s".toRegex(), "+")
        electionInformationRepo.getRepresentatives(formattedAddress, key)
    }

    fun fetchVoterInformation(address: String, key: String) {
        electionInformationRepo.getVoterInfo(address, key)
    }
}