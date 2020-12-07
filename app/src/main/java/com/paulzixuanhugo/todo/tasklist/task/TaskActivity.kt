package com.paulzixuanhugo.todo.tasklist.task

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.paulzixuanhugo.todo.R
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.paulzixuanhugo.todo.tasklist.Task
import java.util.UUID

class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_activity)

        val valider = this.findViewById<Button>(R.id.valider)
        valider.setOnClickListener {

            val newTitle = this.findViewById<TextInputEditText>(R.id.titleInput).text
            val newDesc = this.findViewById<TextInputEditText>(R.id.descriptionInput).text

            val newTask = Task(id = UUID.randomUUID().toString(), title = newTitle.toString(), description = newDesc.toString())

            intent.putExtra(TASK_KEY,newTask)

            setResult(Activity.RESULT_OK,intent)
            finish()
        }

    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
        const val TASK_KEY = "cle"
    }
}

