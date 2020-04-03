package com.example.todonew

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.android.synthetic.main.item_todo.view.*
import kotlinx.android.synthetic.main.item_todo.view.txtShowTime
import java.text.SimpleDateFormat
import java.util.*
import com.example.todonew.HistoryAdapter.HistoryViewHolder as HistoryViewHolder1

class HistoryAdapter(val list: List<TodoModel>) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_history, parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun getItemId(position: Int): Long {
        return list[position].id
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("ResourceAsColor")
        fun bind(todoModel: TodoModel) {
            with(itemView) {
//                val colors = resources.getIntArray(R.array.random_color)
//                val randomColor = colors[Random().nextInt(colors.size)]
                viewColorTagHis.setBackgroundColor(R.color.LightBlue)
                txtShowTitleHis.text = todoModel.title
                txtShowTaskHis.text = todoModel.description
                txtShowCategoryHis.text = todoModel.category
                updateTime(todoModel.time)
                updateDate(todoModel.date)

            }
        }
        private fun updateTime(time: Long) {
            //Mon, 5 Jan 2020
            val myformat = "h:mm a"
            val sdf = SimpleDateFormat(myformat)
            itemView.txtShowTimeHis.text = sdf.format(Date(time))

        }

        private fun updateDate(time: Long) {
            //Mon, 5 Jan 2020
            val myformat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myformat)
            itemView.txtShowDateHis.text = sdf.format(Date(time))

        }
    }


    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(list[position])
    }

}


