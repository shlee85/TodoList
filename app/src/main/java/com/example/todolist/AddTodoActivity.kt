package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.todolist.databinding.ActivityAddTodoBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.ToDoDao
import com.example.todolist.db.ToDoEntity
import kotlinx.coroutines.*

class AddTodoActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddTodoBinding
    lateinit var db: AppDatabase
    lateinit var todoDao: ToDoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()

        binding.btnCompletion.setOnClickListener {
            Log.d(TAG, "할 일 추가하기 완료 버튼 클릭.")
            insertTodo()

        }
    }

    //할 일 추가 함수.
    private fun insertTodo() {
        Log.d(TAG, "할 일 추가 함수 실행.")

        val todoTitle = binding.edtTitle.text.toString() //할 일 제목.
        var todoImportance = binding.radioGroup.checkedRadioButtonId ///할 일 중요도

        //어떤 버튼 눌렀는지 확인 후 값을 지정.
        when(todoImportance) {
            R.id.btn_high -> {
                Log.d(TAG, "높음 선택.")
                todoImportance = 1
            }
            R.id.btn_middle -> {
                Log.d(TAG, "중간 선택.")
                todoImportance = 2
            }
            R.id.btn_low -> {
                Log.d(TAG, "낮음 선택.")
                todoImportance = 3
            }
            else -> {
                todoImportance = -1
            }
        }

        //중요도가 선택되지 않거나, 제목이 작성되지 않았는지 체크.
        if(todoImportance == -1 || todoTitle.isBlank()) {
            Log.d(TAG, "항목이 채워지지 않음.")
            Toast.makeText(this, "항목이 채워지지 않았음.",Toast.LENGTH_SHORT).show()
        } else {
//            Thread { //백그라운드 쓰레드를 실행. 실무에서는 코루틴 or RxJava, RxKotlin같은 라이브러리를 사용. 코루틴 해볼 것.!!
//                todoDao.insertTodo(ToDoEntity(null, todoTitle, todoImportance))
//                runOnUiThread {
//                    Log.d(TAG, "UI Thread실행!!!!")
//                    Toast.makeText(this, "추가되었습니다.!", Toast.LENGTH_SHORT).show()
//                    finish() //AddTodoActivity 종료, 다시 Main Activity로 돌아간다.
//                }
//            }.start()

            //코루틴
            GlobalScope.launch {
                Log.d(TAG, "코루틴 시작")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddTodoActivity, "추가되었습니다.!", Toast.LENGTH_SHORT).show()
                }
                finish() //AddTodoActivity 종료, 다시 Main Activity로 돌아간다.
            }
        }
    }

    companion object {
        val TAG = AddTodoActivity::class.java.simpleName
    }
}