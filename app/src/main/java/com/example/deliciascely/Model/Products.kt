package com.example.deliciascely.Model

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith

class Products {
    private var pid: String? = null
    private var pname: String? = null
    private var price: String? = null
    private var description: String? = null
    private var image: String? = null
    private var date: String? = null
    private var time: String? = null
    private var category: String? = null

    constructor() {}
    constructor(pid: String?, pname: String?, price: String?, description: String?, image: String?, date: String?, time: String?, category: String?) {
        this.pid = pid
        this.pname = pname
        this.price = price
        this.description = description
        this.image = image
        this.date = date
        this.time = time
        this.category = category
    }

    fun getPid(): String? {
        return pid
    }

    fun setPid(pid: String?) {
        this.pid = pid
    }

    fun getPname(): String? {
        return pname
    }

    fun setPname(pname: String?) {
        this.pname = pname
    }

    fun getPrice(): String? {
        return price
    }

    fun setPrice(price: String?) {
        this.price = price
    }

    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String?) {
        this.description = description
    }

    fun getImage(): String? {
        return image
    }

    fun setImage(image: String?) {
        this.image = image
    }

    fun getDate(): String? {
        return date
    }

    fun setDate(date: String?) {
        this.date = date
    }

    fun getTime(): String? {
        return time
    }

    fun setTime(time: String?) {
        this.time = time
    }

    fun getCategory(): String? {
        return category
    }

    fun setCategory(category: String?) {
        this.category = category
    }
}