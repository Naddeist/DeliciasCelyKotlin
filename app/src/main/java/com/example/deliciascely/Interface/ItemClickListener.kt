package com.example.deliciascely.Interface

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.view.View
import org.junit.runner.RunWith

interface ItemClickListener {
    open fun onClick(view: View?, position: Int, isLongClick: Boolean)
}