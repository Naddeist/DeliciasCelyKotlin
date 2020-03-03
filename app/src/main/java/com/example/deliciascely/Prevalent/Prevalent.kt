package com.example.deliciascely.Prevalent

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.deliciascely.Model.Users
import org.junit.runner.RunWith

object Prevalent {
    var currentOnlineUser: Users? = null
    val UserPhoneKey: String? = "UserPhone"
    val UserPasswordKey: String? = "UserPassword"
}