package com.example.deliciascely

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.deliciascely.AdminAddNewProductActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.*

class AdminAddNewProductActivity : AppCompatActivity() {
    private var CategoryName: String? = null
    private var Description: String? = null
    private var Price: String? = null
    private var Pname: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var AddNewProductButton: Button? = null
    private var InputProductImage: ImageView? = null
    private var InputProductName: EditText? = null
    private var InputProductDescription: EditText? = null
    private var InputProductPrice: EditText? = null
    private var ImageUri: Uri? = null
    private var productRandomKey // para generar identificadores
            : String? = null
    private var ProductImagesRef // para guardarlos e icluir timestamps
            : StorageReference? = null
    private var downloadImageUrl: String? = null
    private var ProductRef // Nueva referencia en la Base de datos
            : DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_new_product)
        CategoryName = intent.extras["category"].toString()
        ProductImagesRef = FirebaseStorage.getInstance().reference.child("Product Images")
        ProductRef = FirebaseDatabase.getInstance().reference.child("Products")
        AddNewProductButton = findViewById<View?>(R.id.add_new_product) as Button?
        InputProductImage = findViewById<View?>(R.id.select_product_image) as ImageView?
        InputProductName = findViewById<View?>(R.id.product_name) as EditText?
        InputProductDescription = findViewById<View?>(R.id.product_description) as EditText?
        InputProductPrice = findViewById<View?>(R.id.product_price) as EditText?
        loadingBar = ProgressDialog(this)
        InputProductImage.setOnClickListener(View.OnClickListener { OpenGallerty() })
        AddNewProductButton.setOnClickListener(View.OnClickListener { ValidateProductData() })
    }

    //Metodo para abrir la aleria
    private fun OpenGallerty() {
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GalleryPick)
    }

    //para setear la imagen del producto
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GalleryPick && resultCode == Activity.RESULT_OK && data != null) {
            ImageUri = data.data
            InputProductImage.setImageURI(ImageUri)
        }
    }

    private fun ValidateProductData() {
        Description = InputProductDescription.getText().toString()
        Price = InputProductPrice.getText().toString()
        Pname = InputProductName.getText().toString()
        if (ImageUri == null) {
            Toast.makeText(this, "Se Requiere una Imagen", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Por Favor Escribe la description del producto", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Por Favor da un Precio al producto", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Por Favor da un Nombre al producto.", Toast.LENGTH_SHORT).show()
        } else {
            StoreProductInformation()
        }
    }

    private fun StoreProductInformation() {
        loadingBar.setTitle("Agregando Nuevos Productos")
        loadingBar.setMessage("Administrador!, Espera Por favor mientras agregamos lo nuevos productos")
        loadingBar.setCanceledOnTouchOutside(false)
        loadingBar.show()
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("MM dd, yyyy") //Fecha
        saveCurrentDate = currentDate.format(calendar.time)
        val currentTime = SimpleDateFormat("HH:mm:ss a") //Horas
        saveCurrentTime = currentTime.format(calendar.time)
        //Cada imagen usara un identidicador unico en la bd firebase
        productRandomKey = saveCurrentDate + saveCurrentTime
        val filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg")
        val uploadTask = filePath.putFile(ImageUri)
        uploadTask.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(this@AdminAddNewProductActivity, "Error : $message", Toast.LENGTH_SHORT).show()
            loadingBar.dismiss()
        }.addOnSuccessListener {
            Toast.makeText(this@AdminAddNewProductActivity, "Imagen de Producto Cargada!", Toast.LENGTH_SHORT).show()
            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.getException()
                }
                downloadImageUrl = filePath.downloadUrl.toString()
                filePath.downloadUrl
            }.addOnCompleteListener(object : OnCompleteListener<Uri?> {
                override fun onComplete(task: Task<Uri?>) {
                    if (task.isSuccessful) {
                        downloadImageUrl = task.result.toString()
                        Toast.makeText(this@AdminAddNewProductActivity, "Obteniendo Imagen Producto ", Toast.LENGTH_SHORT).show()
                        SaveProductInfoToDatabase()
                    }
                }

                private fun SaveProductInfoToDatabase() {
                    val productMap = HashMap<String?, Any?>()
                    productMap["pid"] = productRandomKey
                    productMap["date"] = saveCurrentDate
                    productMap["time"] = saveCurrentTime
                    productMap["description"] = Description
                    productMap["image"] = downloadImageUrl
                    productMap["category"] = CategoryName
                    productMap["price"] = Price
                    productMap["pname"] = Pname
                    ProductRef.child(productRandomKey).updateChildren(productMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val intent = Intent(this@AdminAddNewProductActivity, AdminCategoryActivity::class.java)
                                    startActivity(intent)
                                    loadingBar.dismiss()
                                    Toast.makeText(this@AdminAddNewProductActivity, "El Producto fue agregado Correctamente!", Toast.LENGTH_SHORT).show()
                                } else {
                                    loadingBar.dismiss()
                                    val message = task.exception.toString()
                                    Toast.makeText(this@AdminAddNewProductActivity, "Error $message", Toast.LENGTH_SHORT).show()
                                }
                            }
                }
            })
        }
    }

    companion object {
        private const val GalleryPick = 1
    }
}