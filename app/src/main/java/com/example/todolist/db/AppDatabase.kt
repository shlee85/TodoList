package com.example.todolist.db

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(entities = arrayOf(ToDoEntity::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getTodoDao() : ToDoDao

    companion object {
        val TAG : String = AppDatabase::class.java.simpleName

        val databaseName = "db_todo"
        var appDatabase : AppDatabase ?= null

        fun getInstance(context: Context) : AppDatabase? {
            if(appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, databaseName).build()
            }

            return appDatabase
        }
    }
}