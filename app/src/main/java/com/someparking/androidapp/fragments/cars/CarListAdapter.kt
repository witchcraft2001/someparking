package com.someparking.androidapp.fragments.cars

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.someparking.androidapp.core.base.AdapterOnClickListener
import com.someparking.androidapp.core.base.SimpleListAdapter
import com.someparking.androidapp.databinding.LayoutCarListItemBinding
import com.someparking.androidapp.domain.models.CarModel

class CarListAdapter(
    private val clickListener: AdapterOnClickListener<CarModel>
): SimpleListAdapter()  {
    private lateinit var binding: LayoutCarListItemBinding

    val cars = items as List<CarModel>

    fun updateList(list: List<CarModel>) {
        val diffUtilCallback = OrderListDiffUtilCallback(cars, list)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        items.clear()
        items.addAll(list)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding = LayoutCarListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CarViewHolder -> {
                val model = items[position] as CarModel
                holder.model.text = String.format("%s %s", model.brand, model.model)
                holder.number.text = model.number
                holder.itemView.setOnClickListener { clickListener.onClickItem(model) }
                holder.button.setOnClickListener { clickListener.onClickItem(model) }
            }
            else -> throw IllegalStateException("There is no match with current holder instance")
        }
    }

    class CarViewHolder(binding: LayoutCarListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val model = binding.tvBrand
        val number = binding.tvNumber
        val button = binding.buttonEdit
    }

    class OrderListDiffUtilCallback(
        private val oldList: List<CarModel>,
        private val newList: List<CarModel>,
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}