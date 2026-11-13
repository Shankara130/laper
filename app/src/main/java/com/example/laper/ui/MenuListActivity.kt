package com.example.laper.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import com.example.laper.adapter.MenuAdapter
import com.example.laper.data.DummyData
import com.example.laper.databinding.ActivityMenuListBinding
import com.google.android.material.chip.Chip

/**
 * Daftar menu lengkap: 16 item di RecyclerView, dengan pencarian
 * dan filter kategori. Menerima extra "category" (explicit intent + data).
 */
class MenuListActivity : BaseActivity() {

    private lateinit var binding: ActivityMenuListBinding
    private lateinit var adapter: MenuAdapter

    private var currentCategory = DummyData.CAT_ALL
    private var currentQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applySystemBarInsets(binding.root)

        currentCategory = intent.getStringExtra("category") ?: DummyData.CAT_ALL

        binding.btnBack.setOnClickListener { finish() }
        binding.btnCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        setupChips()
        setupSearch()
        setupRecyclerView()
    }

    private fun setupChips() {
        DummyData.categories.forEachIndexed { index, category ->
            val chip = LayoutInflater.from(this)
                .inflate(com.example.laper.R.layout.item_chip_filter, binding.chipCategories, false) as Chip
            chip.text = category
            chip.id = android.view.View.generateViewId()
            binding.chipCategories.addView(chip)
            if (category == currentCategory) {
                binding.chipCategories.check(chip.id)
            } else if (currentCategory == DummyData.CAT_ALL && index == 0) {
                binding.chipCategories.check(chip.id)
            }
        }
        binding.chipCategories.setOnCheckedStateChangeListener { _, checkedIds ->
            val chip = binding.chipCategories.findViewById<Chip>(checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener)
            currentCategory = chip.text.toString()
            applyFilter()
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                currentQuery = s?.toString()?.trim().orEmpty()
                applyFilter()
            }
        })
    }

    private fun setupRecyclerView() {
        adapter = MenuAdapter(DummyData.menu)
        binding.rvMenu.adapter = adapter
        binding.rvMenu.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        applyFilter()
    }

    private fun applyFilter() {
        val filtered = DummyData.menuByCategory(currentCategory).filter {
            currentQuery.isEmpty() ||
                it.name.contains(currentQuery, ignoreCase = true) ||
                it.description.contains(currentQuery, ignoreCase = true) ||
                it.category.contains(currentQuery, ignoreCase = true)
        }
        adapter = MenuAdapter(filtered)
        binding.rvMenu.adapter = adapter
        binding.layoutEmpty.visibility =
            if (filtered.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        binding.rvMenu.visibility =
            if (filtered.isEmpty()) android.view.View.GONE else android.view.View.VISIBLE
    }
}
