package com.example.groceryapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// Cart me item aur uski quantity yaad rakhne ke liye
data class CartItem(val product: Product, var qty: Int)

// Ye pure app me Cart ka data sambhalega
object CartManager {
    val cartItems = mutableListOf<CartItem>()

    fun addItem(product: Product) {
        val existing = cartItems.find { it.product.id == product.id }
        if (existing != null) {
            existing.qty++
        } else {
            cartItems.add(CartItem(product, 1))
        }
    }
}

data class Product(val id: Int, val name: String, val price: String, val category: String, val emoji: String)

class ProductAdapter(private var productList: List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvImage: TextView = itemView.findViewById(R.id.tvImage)
        val btnAdd: Button = itemView.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.tvName.text = product.name
        holder.tvPrice.text = product.price
        holder.tvImage.text = product.emoji

        holder.btnAdd.setOnClickListener {
            CartManager.addItem(product) // Yahan cart me add ho raha hai
            holder.btnAdd.text = "ADDED"
            Toast.makeText(holder.itemView.context, "${product.name} Cart me gaya!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int = productList.size

    fun updateList(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}

class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    private lateinit var allProducts: List<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val etSearch = findViewById<EditText>(R.id.etSearch)
        val btnAll = findViewById<Button>(R.id.btnAll)
        val btnDairy = findViewById<Button>(R.id.btnDairy)
        val btnVeg = findViewById<Button>(R.id.btnVeg)
        val btnSnacks = findViewById<Button>(R.id.btnSnacks)
        val btnGoToCart = findViewById<Button>(R.id.btnGoToCart)

        // Cart button dabane par Cart screen khulegi
        btnGoToCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        allProducts = listOf(
            Product(1, "Fresh Milk (500ml)", "₹30", "Dairy", "🥛"),
            Product(2, "Brown Bread", "₹45", "Snacks", "🍞"),
            Product(3, "Amul Butter (100g)", "₹55", "Dairy", "🧈"),
            Product(4, "Organic Eggs (6 pcs)", "₹80", "Dairy", "🥚"),
            Product(5, "Paneer (200g)", "₹90", "Dairy", "🧀"),
            Product(6, "Curd (400g)", "₹35", "Dairy", "🥣"),
            Product(7, "Cheese Slices", "₹140", "Dairy", "🧀"),
            Product(8, "Potato (1kg)", "₹40", "Veg", "🥔"),
            Product(9, "Onion (1kg)", "₹50", "Veg", "🧅"),
            Product(10, "Tomato (500g)", "₹30", "Veg", "🍅"),
            Product(11, "Maggi (70g)", "₹14", "Snacks", "🍜"),
            Product(12, "Cooking Oil", "₹160", "Snacks", "🍾"),
            Product(13, "Sugar (1kg)", "₹45", "Snacks", "🍚"),
            Product(14, "Tea Leaves", "₹120", "Snacks", "☕"),
            Product(15, "Biscuits", "₹30", "Snacks", "🍪")
        )

        adapter = ProductAdapter(allProducts)
        recyclerView.adapter = adapter

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filteredList = allProducts.filter { it.name.contains(s.toString(), ignoreCase = true) }
                adapter.updateList(filteredList)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnAll.setOnClickListener { adapter.updateList(allProducts) }
        btnDairy.setOnClickListener { adapter.updateList(allProducts.filter { it.category == "Dairy" }) }
        btnVeg.setOnClickListener { adapter.updateList(allProducts.filter { it.category == "Veg" }) }
        btnSnacks.setOnClickListener { adapter.updateList(allProducts.filter { it.category == "Snacks" }) }
    }
}
