package com.example.deliciascely

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import org.junit.runner.RunWith
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private var CreateAccountButton: Button? = null
    private var InputName: EditText? = null
    private var InputPhoneNumber: EditText? = null
    private var InputPassword: EditText? = null
    private var loadingBar: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        CreateAccountButton = findViewById<View?>(R.id.register_btn) as Button?
        InputName = findViewById<View?>(R.id.register_username_input) as EditText?
        InputPhoneNumber = findViewById<View?>(R.id.register_phone_number_input) as EditText?
        InputPassword = findViewById<View?>(R.id.register_password_input) as EditText?
        loadingBar = ProgressDialog(this)
        CreateAccountButton.setOnClickListener(View.OnClickListener { CreateAccount() })
    }

    private fun CreateAccount() {
        val name = InputName.getText().toString()
        val phone = InputPhoneNumber.getText().toString()
        val password = InputPassword.getText().toString()
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Favor de Ingresar tu Nombre", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Favor de Ingresar tu Numero de Telefono", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Favor de Ingresar tu Contrasena elegida", Toast.LENGTH_SHORT).show()
        } else {
            loadingBar.setTitle("Crear Cuenta")
            loadingBar.setMessage("Espera Por favor, Chechando tus Credenciales de Acceso")
            loadingBar.setCanceledOnTouchOutside(false)
            loadingBar.show()
            ValidatephoneNumber(name, phone, password)
        }
    }

    private fun ValidatephoneNumber(name: String?, phone: String?, password: String?) {
        val RootRef: DatabaseReference?
        RootRef = FirebaseDatabase.getInstance().reference
        RootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.child("Users").child(phone).exists()) {
                    val userdataMap = HashMap<String?, Any?>()
                    userdataMap["phone"] = phone
                    userdataMap["password"] = password
                    userdataMap["name"] = name
                    RootRef.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this@RegisterActivity, "Bien Hecho Tu Cuenta fue Creada Con Exito!", Toast.LENGTH_SHORT).show()
                                    loadingBar.dismiss()
                                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    loadingBar.dismiss()
                                    Toast.makeText(this@RegisterActivity, "Error de Red : Verifica tu Conexion  ", Toast.LENGTH_SHORT).show()
                                }
                            }
                } else {
                    Toast.makeText(this@RegisterActivity, "El numero " + phone + "ya esta registrado", Toast.LENGTH_SHORT).show()
                    loadingBar.dismiss()
                    Toast.makeText(this@RegisterActivity, "Intentalo de nuevo usando otro numero de telefono", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}