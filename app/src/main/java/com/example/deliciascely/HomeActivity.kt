package com.example.deliciascely

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.TextView
import com.example.deliciascely.HomeActivity
import com.example.deliciascely.Model.Products
import com.example.deliciascely.ViewHolder.ProductViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import io.paperdb.Paper
import org.junit.runner.RunWith

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var ProductsRef: DatabaseReference? = null
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Paper.init(this)
        ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")
        val toolbar = findViewById<Toolbar?>(R.id.toolbar)
        toolbar.setTitle("Home")
        setSupportActionBar(toolbar)
        val fab = findViewById<FloatingActionButton?>(R.id.fab)
        fab.setOnClickListener(View.OnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        })
        val drawer = findViewById<DrawerLayout?>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        val navigationView = findViewById<NavigationView?>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val headerView = navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView?>(R.id.user_profile_name)
        val profileImageView: CircleImageView = headerView.findViewById(R.id.user_profile_image)
        //  userNameTextView.setText(Prevalent.currentOnlineUser.getName());
        recyclerView = findViewById<RecyclerView?>(R.id.recycler_menu)
        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
    }

    override fun onStart() {
        super.onStart()
        val options: FirebaseRecyclerOptions<Products?> = FirebaseRecyclerOptions.Builder<Products?>().setQuery(ProductsRef, Products::class.java).build()
        val adapter: FirebaseRecyclerAdapter<Products?, ProductViewHolder?> = object : FirebaseRecyclerAdapter<Products?, ProductViewHolder?>(options) {
            override fun onBindViewHolder(holder: ProductViewHolder, position: Int, model: Products) {
                holder.txtProductName.text = model.pname
                holder.txtProductDescription.text = model.description
                holder.txtProductPrice.text = "El Precio es = " + model.price + "$"
                //Imagenes :D organizables
                Picasso.get().load(model.image).into(holder.imageView)
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.product_items_layout, parent, false)
                return ProductViewHolder(view)
            }
        }
        recyclerView.setAdapter(adapter)
        adapter.startListening()
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout?>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item.getItemId()
        //  if (id == R.id.action_settings) {
//     return true;
//  }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem?): Boolean { // Handle navigation view item clicks here.
        val id = item.getItemId()
        if (id == R.id.nav_cart) { // Handle the camera action
        } else if (id == R.id.nav_orders) {
        } else if (id == R.id.nav_categories) {
        } else if (id == R.id.nav_settings) {
        } else if (id == R.id.nav_logout) {
            Paper.book().destroy()
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
        val drawer = findViewById<DrawerLayout?>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}