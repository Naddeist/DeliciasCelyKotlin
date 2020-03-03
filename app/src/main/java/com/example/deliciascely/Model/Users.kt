package com.example.deliciascely.Model

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith

class Users {
    private var name: String? = null
    var phone: String? = null
    var password: String? = null

    constructor() {}
    constructor(name: String?, phone: String?, password: String?) {
        this.name = name
        this.phone = phone
        this.password = password
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?) {
        this.name = name
    }

    fun getPhone(): String? {
        return phone
    }

    fun setPhone(phone: String?) {
        this.phone = phone
    }

    fun getPassword(): String? {
        return password
    }

    fun setPassword(password: String?) {
        this.password = password
    }
}