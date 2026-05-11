package com.example.groceryapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val cartList: MutableList<CartItem>,
    private val updateBill: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmoji: TextView = itemView.findViewById(R.id.tvCartEmoji)
        val tvName: TextView = itemView.findViewById(R.id.tvCartName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCartPrice)
        val tvQty: TextView = itemView.findViewById(R.id.tvQty)
        val btnPlus: Button = itemView.findViewById(R.id.btnPlus)
        val btnMinus: Button = itemView.findViewById(R.id.btnMinus)
        val btnRemove: Button = itemView.findViewById(R.id.btnRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartList[position]
        holder.tvEmoji.text = item.product.emoji
        holder.tvName.text = item.product.name
        holder.tvPrice.text = item.product.price
        holder.tvQty.text = item.qty.toString()

        holder.btnPlus.setOnClickListener {
            item.qty++
            notifyItemChanged(position)
            updateBill()
        }

        holder.btnMinus.setOnClickListener {
            if (item.qty > 1) {
                item.qty--
                notifyItemChanged(position)
                updateBill()
            }
        }

        holder.btnRemove.setOnClickListener {
            cartList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartList.size)
            updateBill()
        }
    }

    override fun getItemCount(): Int = cartList.size
}

class CartActivity : AppCompatActivity() {

    private lateinit var tvTotalBill: TextView
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        tvTotalBill = findViewById(R.id.tvTotalBill)
        val rvCart = findViewById<RecyclerView>(R.id.rvCart)

        rvCart.layoutManager = LinearLayoutManager(this)
        adapter = CartAdapter(CartManager.cartItems) { calculateTotal() }
        rvCart.adapter = adapter

        calculateTotal()
        
        // Yahan tera naya Checkout button ka code hai
        val btnCheck = findViewById<Button>(R.id.btnCheckout)
        btnCheck.setOnClickListener {
            val intent = Intent(this, CheckoutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun calculateTotal() {
        var total = 0
        for (item in CartManager.cartItems) {
            val priceInt = item.product.price.replace("₹", "").trim().toInt()
            total += priceInt * item.qty
        }
        tvTotalBill.text = "Total Bill: Rs $total"
    }
}
