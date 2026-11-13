package com.example.laper.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laper.R
import com.example.laper.databinding.ItemMenuBinding
import com.example.laper.model.MenuItem
import com.example.laper.ui.MenuDetailActivity
import com.example.laper.util.rating
import com.example.laper.util.rupiah

/**
 * RecyclerView utama daftar menu (syarat tugas: >= 10 item,
 * klik item membuka detail dengan membawa data).
 */
class MenuAdapter(private val items: List<MenuItem>) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder =
        MenuViewHolder(
            ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class MenuViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MenuItem) {
            binding.ivMenu.setImageResource(item.imageRes)
            binding.tvName.text = item.name
            binding.tvDesc.text = item.description
            binding.tvPrice.text = rupiah(item.price)
            binding.tvRating.text = rating(item.rating)
            binding.tvSold.text = binding.root.context.getString(
                R.string.sold_count, rupiah(item.sold).removePrefix("Rp")
            )
            binding.ivSpicy.visibility = if (item.spicy) View.VISIBLE else View.GONE

            // Syarat inti: klik item RecyclerView -> detail dengan data (explicit intent).
            binding.root.setOnClickListener {
                val context = binding.root.context
                context.startActivity(
                    Intent(context, MenuDetailActivity::class.java)
                        .putExtra("menu", item)
                        .putExtra("fromList", true)
                )
            }
        }
    }
}
