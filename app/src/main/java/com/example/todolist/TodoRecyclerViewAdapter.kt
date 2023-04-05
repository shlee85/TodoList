package com.example.todolist

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding
import com.example.todolist.db.ToDoEntity

class TodoRecyclerViewAdapter(private val todoList: ArrayList<ToDoEntity>)
    :RecyclerView.Adapter<TodoRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        val tv_importance = binding.tvImportance
        val tv_title = binding.tvTitle
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TodoRecyclerViewAdapter.MyViewHolder {
        Log.d(TAG, "onCreateViewHolder()")
        //item_todo.xml 뷰 바인딩 객체 생성
        val binding: ItemTodoBinding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoRecyclerViewAdapter.MyViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder()")

        val todoData = todoList[position]
        when (todoData.importance) {
            1 -> {
                holder.tv_importance.setBackgroundResource(R.color.red)
            }
            2 -> {
                holder.tv_importance.setBackgroundResource(R.color.yellow)
            }
            3 -> {
                holder.tv_importance.setBackgroundResource(R.color.green)
            }
        }

        //중요도에 따라 중요도 텍스트(1,2,3) 변경
        holder.tv_importance.text = todoData.importance.toString()

        //할 일의 제목 변경
        holder.tv_title.text = todoData.title
    }

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount(): ${todoList.size}")

        return todoList.size
    }

    companion object {
        val TAG = TodoRecyclerViewAdapter::class.java.simpleName
    }
}