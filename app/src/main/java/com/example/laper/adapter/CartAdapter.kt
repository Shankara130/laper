package com.example.laper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laper.databinding.ItemCartBinding
import com.example.laper.model.CartItem
import com.example.laper.util.rupiah

/** Item keranjang: ubah jumlah, hapus. Callback ke CartActivity. */
class CartAdapter(
    private val items: List<CartItem>,
    private val onQtyChanged: (Int, Int) -> Unit,
    private val onRemoved: (Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder =
        CartViewHolder(
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {
            val pos = adapterPosition
            binding.ivMenu.setImageResource(item.menuItem.imageRes)
            binding.tvName.text = item.menuItem.name
            binding.tvQty.text = item.qty.toString()
            binding.tvLineTotal.text = rupiah(item.lineTotal)
            binding.tvNote.visibility = if (item.note.isNotBlank()) View.VISIBLE else View.GONE
            binding.tvNote.text = "“${item.note}”"

            binding.btnMinus.setOnClickListener { if (pos != RecyclerView.NO_POSITION) onQtyChanged(pos, item.qty - 1) }
            binding.btnPlus.setOnClickListener { if (pos != RecyclerView.NO_POSITION) onQtyChanged(pos, item.qty + 1) }
            binding.btnDelete.setOnClickListener { if (pos != RecyclerView.NO_POSITION) onRemoved(pos) }
        }
    }
}
