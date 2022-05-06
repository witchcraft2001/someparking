package com.someparking.androidapp.core.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class SimpleListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), BaseListAdapter<BaseListItem> {
    protected val items: ArrayList<BaseListItem> = ArrayList()

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) = items[position].getLayoutId()

    protected fun inflateByViewType(context: Context?, viewType: Int, parent: ViewGroup) =
        LayoutInflater.from(context).inflate(viewType, parent, false)

    override fun add(newItem: BaseListItem) {
        items.add(newItem)
        notifyDataSetChanged()
    }

    override fun add(newItems: List<BaseListItem>?) {
        items.addAll(newItems?.toList() ?: return)
        notifyDataSetChanged()
    }

    override fun addAtPosition(position: Int, newItem: BaseListItem) {
        items.add(position, newItem)
        notifyItemInserted(position)
    }

    override fun clearAll() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun remove(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }
}