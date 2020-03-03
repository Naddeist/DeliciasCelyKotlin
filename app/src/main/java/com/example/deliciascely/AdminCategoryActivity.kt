package com.example.deliciascely

import android.content.Intent
import android.os.Bundle
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.example.deliciascely.AdminCategoryActivity
import org.junit.runner.RunWith

class AdminCategoryActivity : AppCompatActivity() {
    private var cafe: ImageView? = null
    private var galleta: ImageView? = null
    private var pastel: ImageView? = null
    private var te: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_category)
        cafe = findViewById<View?>(R.id.cafe) as ImageView?
        galleta = findViewById<View?>(R.id.galleta) as ImageView?
        pastel = findViewById<View?>(R.id.pastel) as ImageView?
        te = findViewById<View?>(R.id.te) as ImageView?
        cafe.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
            intent.putExtra("category", "cafe")
            startActivity(intent)
        })
        galleta.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
            intent.putExtra("category", "galleta")
            startActivity(intent)
        })
        pastel.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
            intent.putExtra("category", "pastel")
            startActivity(intent)
        })
        te.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
            intent.putExtra("category", "te")
            startActivity(intent)
        })
    }
}