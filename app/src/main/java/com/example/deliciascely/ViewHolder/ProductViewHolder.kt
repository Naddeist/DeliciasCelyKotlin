package com.example.deliciascely.ViewHolder

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.deliciascely.Interface.ItemClickListener
import com.example.deliciascely.R
import org.junit.runner.RunWith

class ProductViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
    var txtProductName: TextView?
    var txtProductPrice: TextView?
    var txtProductDescription: TextView?
    var imageView: ImageView?
    var listener: ItemClickListener? = null
    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    override fun onClick(view: View?) {
        listener.onClick(view, adapterPosition, false)
    }

    init {
        imageView = itemView.findViewById<View?>(R.id.product_image) as ImageView?
        txtProductName = itemView.findViewById<View?>(R.id.product_name) as TextView?
        txtProductDescription = itemView.findViewById<View?>(R.id.product_description) as TextView?
        txtProductPrice = itemView.findViewById<View?>(R.id.product_price) as TextView?
    }
}