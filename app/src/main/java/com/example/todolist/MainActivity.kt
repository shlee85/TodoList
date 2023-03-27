package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
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
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
    }
}