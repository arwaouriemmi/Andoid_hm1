package com.gl4.hw1

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.view.Menu
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.window.SplashScreen
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {
    lateinit var pizzaTypeSpinner: Spinner
    lateinit var confirmButton: Button
    lateinit var nomEditText: TextInputEditText
    lateinit var prenomEditText: TextInputEditText
    lateinit var adresseEditText: TextInputEditText
    lateinit var checkboxFromage: CheckBox
    lateinit var checkboxChampignon: CheckBox
    lateinit var checkboxOlives: CheckBox

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pizzaTypes = arrayOf("Moyenne", "Mini", "Maxi")
        val adapter = ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, pizzaTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        pizzaTypeSpinner = findViewById(R.id.pizzaTypeSpinner)
        pizzaTypeSpinner.setAdapter(adapter)
        confirmButton = findViewById(R.id.confirmButton)
        checkboxFromage = findViewById(R.id.checkboxFromage)
        checkboxChampignon = findViewById(R.id.checkboxChampignon)
        checkboxOlives = findViewById(R.id.checkboxOlives)
        nomEditText = findViewById(R.id.Editnom)
        prenomEditText = findViewById(R.id.Editprenom)
        adresseEditText = findViewById(R.id.Editadress)
        confirmButton.setOnClickListener {
            val nom = nomEditText.text.toString()
            val prenom = prenomEditText.text.toString()
            val adresse = adresseEditText.text.toString()
            val typePizza = pizzaTypeSpinner.selectedItem.toString()
            var ingredients = ""

            if (checkboxFromage.isChecked) {
                ingredients += "Fromage, "
            }
            if (checkboxChampignon.isChecked) {
                ingredients += "Champignon, "
            }
            if (checkboxOlives.isChecked) {
                ingredients += "Olives, "
            }

            if (ingredients.isNotEmpty()) {
                ingredients = ingredients.substring(0, ingredients.length - 2)
            }
            val message = "Nom: $nom\nPrénom: $prenom\nAdresse: $adresse\nType de Pizza: $typePizza\nIngrédients: $ingredients"
            showChoiceDialog(message)
        }



    }
    private fun showChoiceDialog(commande: String) {
        val choices = arrayOf("Envoyer un SMS", "Envoyer un Email")
        AlertDialog.Builder(this)
            .setTitle("Choisissez une option")
            .setItems(choices) { dialog, which ->
                when (which) {
                    0 -> sendSms("28431054", "Récapitulatif de la commande : $commande")
                    1 -> sendEmail("arwaouriemmi@gmail.com", "Récapitulatif de la commande", "Récapitulatif de la commande : $commande")
                }

                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
                finish()
            }
            .show()
    }


        @SuppressLint("RestrictedApi")
        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            val inflater = menuInflater
            inflater.inflate(R.menu.top_app_bar, menu)
            if (menu is MenuBuilder) {
                menu.setOptionalIconsVisible(true)
            }
            return true
        }
    private fun sendSms(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
          
        } catch (ex: Exception) {

            ex.printStackTrace()

            
        }
    }



    private fun sendEmail(email: String, subject: String, message: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(emailIntent, "Envoyer un email..."))
        } catch (ex: ActivityNotFoundException) {

        }
    }




}