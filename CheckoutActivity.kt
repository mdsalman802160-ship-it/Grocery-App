package com.example.groceryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        val etAddress = findViewById<EditText>(R.id.etAddress)
        val btnPlaceOrder = findViewById<Button>(R.id.btnPlaceOrder)

        btnPlaceOrder.setOnClickListener {
            val address = etAddress.text.toString()
            
            if (address.isEmpty()) {
                Toast.makeText(this, "Pehle address daal bhai!", Toast.LENGTH_SHORT).show()
            } else {
                
                // Order Summary Calculate karna
                var totalAmount = 0
                var totalItems = 0
                for (item in CartManager.cartItems) {
                    val priceInt = item.product.price.replace("₹", "").trim().toInt()
                    totalAmount += priceInt * item.qty
                    totalItems += item.qty
                }

                // Success Popup (Dialog)
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Order Successful! ✅")
                builder.setMessage("Order Summary: $totalItems Items (Total Bill: ₹$totalAmount)\n\nOrder ID: #OX${(1000..9999).random()}\nEstimated Delivery: 15-20 Mins")
                
                builder.setPositiveButton("OK") { _, _ -> 
                    
                    // --- YAHAN BADLAV KIYA HAI ---
                    // 1. Cart khali kar do taaki naya order ho sake
                    CartManager.cartItems.clear()
                    
                    // 2. Wapas HomeActivity (Home Screen) par bhej do
                    val intent = Intent(this, HomeActivity::class.java)
                    // Ye line isliye hai taaki back dabane par wapas success page na aaye
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish() 
                }
                builder.setCancelable(false) // Bahar click karne par dialog hategi nahi
                builder.show()
            }
        }
    }
}
