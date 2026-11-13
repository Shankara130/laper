package com.example.laper.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.laper.R
import com.example.laper.data.OrderRepository
import com.example.laper.databinding.ItemOrderBinding
import com.example.laper.model.Order
import com.example.laper.ui.OrderTrackingActivity
import com.example.laper.util.rupiah

/**
 * Riwayat pesanan. Klik -> OrderTrackingActivity (mode riwayat)
 * membawa objek Order (explicit intent + Serializable).
 */
class OrderAdapter(private val orders: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder =
        OrderViewHolder(
            ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position])
    }

    class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.tvCode.text = order.code
            binding.tvStatus.text = OrderRepository.currentStatus(order).label
            binding.tvTime.text = OrderRepository.formatTime(order.createdAt)
            binding.tvItems.text = order.items.joinToString(", ") {
                "${it.menuItem.name} ×${it.qty}"
            }
            binding.tvTotal.text = rupiah(order.total)

            binding.root.setOnClickListener {
                val context = binding.root.context
                context.startActivity(
                    Intent(context, OrderTrackingActivity::class.java)
                        .putExtra("order", order)
                        .putExtra("isHistory", true)
                )
            }
        }
    }
}
