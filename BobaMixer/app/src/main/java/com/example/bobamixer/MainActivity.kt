package com.example.bobamixer

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var baseSpinner: Spinner
    private lateinit var toppingBoba: CheckBox
    private lateinit var toppingJelly: CheckBox
    private lateinit var toppingPudding: CheckBox
    private lateinit var flavorGroup: RadioGroup
    private lateinit var mixButton: Button
    private lateinit var resultText: TextView // Optional if you want to keep it

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Views
        baseSpinner = findViewById(R.id.baseSpinner)
        toppingBoba = findViewById(R.id.toppingBoba)
        toppingJelly = findViewById(R.id.toppingJelly)
        toppingPudding = findViewById(R.id.toppingPudding)
        flavorGroup = findViewById(R.id.flavorGroup)
        mixButton = findViewById(R.id.mixButton)
        resultText = findViewById(R.id.resultText)

        // Spinner items
        val bases = arrayOf("Black Tea", "Green Tea", "Oolong", "Milk Tea")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bases)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        baseSpinner.adapter = adapter

        // Button click
        mixButton.setOnClickListener {
            val base = baseSpinner.selectedItem.toString()

            val toppings = mutableListOf<String>()
            if (toppingBoba.isChecked) toppings.add("Boba Pearls")
            if (toppingJelly.isChecked) toppings.add("Fruit Jelly")
            if (toppingPudding.isChecked) toppings.add("Egg Pudding")

            val selectedFlavorId = flavorGroup.checkedRadioButtonId
            val flavor = if (selectedFlavorId != -1) {
                findViewById<RadioButton>(selectedFlavorId).text.toString()
            } else {
                "No Flavor"
            }

            val result = "🍹 Your Custom Boba Tea:\n\n" +
                    "🧋 Base: $base\n" +
                    "🍬 Toppings: ${if (toppings.isEmpty()) "None" else toppings.joinToString(", ")}\n" +
                    "🍓 Flavor: $flavor"

            // Show modal pop-up
            AlertDialog.Builder(this)
                .setTitle("Your Boba Mix!")
                .setMessage(result)
                .setPositiveButton("Yum!") { dialog, _ -> dialog.dismiss() }
                .setCancelable(true)
                .show()

            // Optional: Also display in resultText view
            resultText.text = result
        }
    }
}
