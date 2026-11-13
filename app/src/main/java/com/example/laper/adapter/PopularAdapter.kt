package com.example.laper.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laper.databinding.ItemMenuHorizontalBinding
import com.example.laper.model.MenuItem
import com.example.laper.ui.MenuDetailActivity
import com.example.laper.util.rating

/**
 * Kartu menu horizontal di dashboard ("Menu Terlaris").
 * Klik item -> MenuDetailActivity membawa objek MenuItem (explicit intent + data).
 */
class PopularAdapter(private val items: List<MenuItem>) :
    RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder =
        PopularViewHolder(
            ItemMenuHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class PopularViewHolder(private val binding: ItemMenuHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MenuItem) {
            binding.ivMenu.setImageResource(item.imageRes)
            binding.tvName.text = item.name
            binding.tvPrice.text = com.example.laper.util.rupiah(item.price)
            binding.tvRatingBadge.text = rating(item.rating)
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
