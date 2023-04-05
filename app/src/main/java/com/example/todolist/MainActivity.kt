package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.ToDoDao
import com.example.todolist.db.ToDoEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var db: AppDatabase
    lateinit var todoDao: ToDoDao
    lateinit var todoList: ArrayList<ToDoEntity>

    lateinit var adapter: TodoRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //할 일 추가하기 버튼을 누르면 AddTodoActivity로 이동한다.
        binding.btnAdd.setOnClickListener {
            Log.d(TAG, "할 일 추가하기 클릭.")
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivity(intent)
        }

        //DB인스턴스를 가져오고 DB작업을 할 수 있는 DAO를 가져온다.
        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()

        getAllTodoList()
    }

    private fun getAllTodoList(){
        GlobalScope.launch {
            Log.d(TAG, "6코루틴 실행.")
            todoList = ArrayList(todoDao.getAll())
            setRecyclerView()
        }
    }

    //리사이클러뷰 설정.
    private fun setRecyclerView() {
        Log.d(TAG, "setRecyclerView")
        runOnUiThread {
            adapter = TodoRecyclerViewAdapter(todoList)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart()")
        getAllTodoList()
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}