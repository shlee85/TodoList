package com.example.todolist

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.ToDoDao
import com.example.todolist.db.ToDoEntity
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), OnItemLongClickListener {

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
            adapter = TodoRecyclerViewAdapter(todoList, this)
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

    //interface 구현부.
    override fun onLongClick(position: Int) {
        Log.d(TAG, "onLongClick")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("할 일 삭제")
        builder.setMessage("정말 삭제하겠습니까?")
        builder.setNegativeButton("취소", null)
        builder.setPositiveButton("네",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    Log.d(TAG, "OnClick.")
                    deleteTodo(position)
                }
            }
        )
        builder.show()
    }

    private fun deleteTodo(position: Int) {
        //동작확인 후 코루틴을 적용해보자.
//        Thread {
//            todoDao.deleteTodo(todoList[position])
//            todoList.removeAt(position)
//            runOnUiThread {
//                adapter.notifyDataSetChanged()
//                Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
//            }
//        }.start()

        GlobalScope.launch {
            todoDao.deleteTodo(todoList[position])
            todoList.removeAt(position)
            showToast()
        }
    }

    private fun showToast() = runBlocking {
        withContext(Dispatchers.Main){
            adapter.notifyDataSetChanged()
            Toast.makeText(this@MainActivity, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}