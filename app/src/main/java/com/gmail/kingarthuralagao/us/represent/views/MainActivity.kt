package com.gmail.kingarthuralagao.us.represent.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gmail.kingarthuralagao.us.represent.R
import com.gmail.kingarthuralagao.us.represent.repositories.ElectionInformationRepo

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.splashScreenTheme);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val voterInfoRepo = ElectionInformationRepo()
        voterInfoRepo.getVoterInfo("601 Biscayne Blvd, Miami, FL 33132", resources.getString(R.string.api_key))
        /*
        var intent = Intent(this, MasterActivity::class.java)
        startActivity(intent)*/
    }
}
