package com.someparking.androidapp.fragments.bookings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.someparking.androidapp.R
import com.someparking.androidapp.core.base.AdapterOnClickListener
import com.someparking.androidapp.core.base.SimpleListAdapter
import com.someparking.androidapp.databinding.LayoutBookingListItemBinding
import com.someparking.androidapp.domain.models.BookingModel
import java.lang.StringBuilder
import java.util.*

class BookingListAdapter(
    private val clickListener: AdapterOnClickListener<BookingModel>
) : SimpleListAdapter() {
    private lateinit var binding: LayoutBookingListItemBinding

    private val bookings = items as List<BookingModel>

    private lateinit var dataFormat: String
    private lateinit var timeFormat: String

    fun updateList(list: List<BookingModel>) {
        val diffUtilCallback = BookingListDiffUtilCallback(bookings, list)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        items.clear()
        items.addAll(list)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        binding =
            LayoutBookingListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        dataFormat = parent.context.getString(R.string.text_date_format)
        timeFormat = parent.context.getString(R.string.text_time_format)
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is BookingViewHolder -> {
                val model = items[position] as BookingModel
                with(holder) {
                    id.text = String.format("ID %d", model.id)
                    with(model.start) {
                        date.text = String.format(
                            dataFormat,
                            get(Calendar.DAY_OF_MONTH),
                            get(Calendar.MONTH) + 1,
                            get(Calendar.YEAR)
                        )
                    }
                    val start = getHoursMinutes(model.start)
                    val end = getHoursMinutes(model.end)
                    time.text = String.format("%s - %s", start, end)
                    status.text = model.status
                    itemView.setOnClickListener { clickListener.onClickItem(model) }
                    button.setOnClickListener { clickListener.onClickItem(model) }
                }
            }
            else -> throw IllegalStateException("There is no match with current holder instance")
        }
    }

    private fun getHoursMinutes(calendar: Calendar): String =
        String.format(
            timeFormat,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE)
        )

    class BookingViewHolder(binding: LayoutBookingListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val id = binding.tvId
        val date = binding.tvDate
        val time = binding.tvTime
        val status = binding.tvStatus
        val button = binding.buttonEdit
    }

    class BookingListDiffUtilCallback(
        private val oldList: List<BookingModel>,
        private val newList: List<BookingModel>,
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