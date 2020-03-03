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
import android.widget.TextView
import android.widget.Toast
import com.example.deliciascely.LoginActivity
import com.example.deliciascely.Model.Users
import com.example.deliciascely.Prevalent.Prevalent
import com.google.firebase.database.*
import com.rey.material.widget.CheckBox
import io.paperdb.Paper
import org.junit.runner.RunWith

//import android.widget.CheckBox;
//Recuerda que hay que importar los widgets personalizados
class LoginActivity : AppCompatActivity() {
    private var InputPhoneNumber: EditText? = null
    private var InputPassword: EditText? = null
    private var LoginButton: Button? = null
    private var loadingBar: ProgressDialog? = null
    private var AdminLink: TextView? = null
    private var NotAdminLink: TextView? = null
    private var parentDbName: String? = "Users"
    private var chkBoxRememberMe: CheckBox? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        LoginButton = findViewById<View?>(R.id.login_btn) as Button?
        InputPhoneNumber = findViewById<View?>(R.id.login_phone_number_input) as EditText?
        InputPassword = findViewById<View?>(R.id.login_password_input) as EditText?
        loadingBar = ProgressDialog(this)
        AdminLink = findViewById<View?>(R.id.admin_panel_link) as TextView?
        NotAdminLink = findViewById<View?>(R.id.not_admin_panel_link) as TextView?
        chkBoxRememberMe = findViewById<View?>(R.id.remember_me_chkb) as CheckBox?
        Paper.init(this)
        LoginButton.setOnClickListener(View.OnClickListener { LoginUser() })
        AdminLink.setOnClickListener(View.OnClickListener {
            LoginButton.setText("Login Admin")
            AdminLink.setVisibility(View.INVISIBLE)
            NotAdminLink.setVisibility(View.VISIBLE)
            parentDbName = "Admins"
        })
        NotAdminLink.setOnClickListener(View.OnClickListener {
            LoginButton.setText("Login")
            AdminLink.setVisibility(View.VISIBLE)
            NotAdminLink.setVisibility(View.INVISIBLE)
            parentDbName = "Users"
        })
    }

    private fun LoginUser() {
        val phone = InputPhoneNumber.getText().toString()
        val password = InputPassword.getText().toString()
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Favor de Ingresar tu Numero de Telefono", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Favor de Ingresar tu Contrasena elegida", Toast.LENGTH_SHORT).show()
        } else {
            loadingBar.setTitle("Ingresa a tu Cuenta")
            loadingBar.setMessage("Espera Por favor, Chechando tus Credenciales de Acceso")
            loadingBar.setCanceledOnTouchOutside(false)
            loadingBar.show()
            AllowAccessToAccount(phone, password)
        }
    }

    //Aqui se Realiza la validacion de accesos a la base de datos de la app se checa el telefono asi como el password del usuario en el registro
    private fun AllowAccessToAccount(phone: String?, password: String?) {
        if (chkBoxRememberMe.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phone)
            Paper.book().write(Prevalent.UserPasswordKey, password)
        }
        val RootRef: DatabaseReference?
        RootRef = FirebaseDatabase.getInstance().reference
        RootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()) {
                    val usersData: Users = dataSnapshot.child(parentDbName).child(phone).getValue<Users?>(Users::class.java)
                    if (usersData.phone == phone) {
                        if (usersData.password == password) {
                            if (parentDbName == "Admins") {
                                Toast.makeText(this@LoginActivity, "Has Ingresado como Administrador Correctamente", Toast.LENGTH_SHORT).show()
                                loadingBar.dismiss()
                                val intent = Intent(this@LoginActivity, AdminCategoryActivity::class.java)
                                startActivity(intent)
                            } else if (parentDbName == "Users") {
                                Toast.makeText(this@LoginActivity, "Has Ingresado Correctamente", Toast.LENGTH_SHORT).show()
                                loadingBar.dismiss()
                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                Prevalent.currentOnlineUser = usersData
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "La contrasena es incorrecta", Toast.LENGTH_SHORT).show()
                            loadingBar.dismiss()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Cuenta de " + phone + "No esta registrada", Toast.LENGTH_SHORT).show()
                    loadingBar.dismiss()
                    //     Toast.makeText(LoginActivity.this, "Necesitas Crear una Cuenta para Acceder", Toast.LENGTH_SHORT).show();
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}