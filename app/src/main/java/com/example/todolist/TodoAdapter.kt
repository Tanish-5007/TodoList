package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding
import java.text.SimpleDateFormat
import java.util.*

class TodoAdapter(private val list: List<TodoModel>): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context))
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    class TodoViewHolder(private val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(todoModel: TodoModel){
            with(itemView){
                val color = resources.getIntArray(R.array.random_color)
                val randomColor = color[Random().nextInt(color.size)]
                binding.viewColorTag.setBackgroundColor(randomColor)
                binding.txtShowTitle.text = todoModel.title
                binding.txtShowTask.text = todoModel.description
                binding.txtShowCategory.text = todoModel.category
                updateTime(todoModel.time)
                updateDate(todoModel.date)
            }
        }

        private fun updateTime(time: Long) {
            val myFormat = "h:mm a"
            val sdf = SimpleDateFormat(myFormat)
            binding.txtShowTime.text = sdf.format(Date(time))
        }
        private fun updateDate(date: Long) {
            val myFormat = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(myFormat)
            binding.txtShowDate.text = sdf.format(Date(date))
        }

    }
}