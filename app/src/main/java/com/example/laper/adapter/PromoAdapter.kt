package com.example.laper.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laper.data.DummyData
import com.example.laper.databinding.ItemPromoBinding
import com.example.laper.model.Promo

/** Banner promo di dashboard (ViewPager2). */
class PromoAdapter : RecyclerView.Adapter<PromoAdapter.PromoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder =
        PromoViewHolder(
            ItemPromoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = DummyData.promos.size

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        holder.bind(DummyData.promos[position])
    }

    class PromoViewHolder(private val binding: ItemPromoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(promo: Promo) {
            binding.tvTitle.text = promo.title
            binding.tvSubtitle.text = promo.subtitle
            binding.tvCode.text = "Kode: ${promo.code}"
            binding.promoCard.background.setTint(Color.parseColor(promo.colorHex))
        }
    }
}
