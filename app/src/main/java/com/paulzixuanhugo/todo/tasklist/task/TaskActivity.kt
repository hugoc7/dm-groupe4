package com.paulzixuanhugo.todo.tasklist.task

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import com.paulzixuanhugo.todo.R
import androidx.appcompat.app.AppCompatActivity
import com.paulzixuanhugo.todo.tasklist.Task
import java.util.UUID

class TaskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_activity)

        val valider = this.findViewById<Button>(R.id.valider)
        valider.setOnClickListener {
            // Instanciation d'un nouvel objet [Task]
            val newTask = Task(id = UUID.randomUUID().toString(), title = "New Task !")

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

