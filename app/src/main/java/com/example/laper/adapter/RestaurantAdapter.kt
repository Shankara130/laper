package com.example.laper.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laper.R
import com.example.laper.databinding.ItemRestaurantBinding
import com.example.laper.model.Restaurant
import com.example.laper.ui.RestaurantDetailActivity
import com.example.laper.util.rating

/**
 * Daftar warung. Klik -> RestaurantDetailActivity membawa objek
 * Restaurant (explicit intent + Serializable).
 */
class RestaurantAdapter(private val items: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder =
        RestaurantViewHolder(
            ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class RestaurantViewHolder(private val binding: ItemRestaurantBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(r: Restaurant) {
            binding.tvAvatar.text = r.name.firstOrNull()?.toString().orEmpty()
            binding.tvAvatar.background.setTint(Color.parseColor(r.colorHex))
            binding.tvName.text = r.name
            binding.tvDesc.text = r.description
            binding.tvRating.text = rating(r.rating)
            binding.tvMeta.text = "· ±${r.etaMinutes} menit · ${r.openHours} WIB"
            binding.root.setOnClickListener {
                val context = binding.root.context
                context.startActivity(
                    Intent(context, RestaurantDetailActivity::class.java).putExtra("restaurant", r)
                )
            }
        }
    }
}
