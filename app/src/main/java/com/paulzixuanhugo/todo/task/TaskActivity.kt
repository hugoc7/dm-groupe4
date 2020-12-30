package com.paulzixuanhugo.todo.task

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.paulzixuanhugo.todo.R
import androidx.appcompat.app.AppCompatActivity
import com.paulzixuanhugo.todo.task.Task
import java.util.*

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_activity)
        val newTitle = this.findViewById<EditText>(R.id.titleInput)
        val newDesc  = this.findViewById<EditText>(R.id.descriptionInput)

        val taskToEdit = intent.getSerializableExtra(TASK_KEY) as? Task
        val textToAdd = intent.getSerializableExtra(TEXT_KEY) as? String
        var resultCode = ADD_TASK_REQUEST_CODE

        if (textToAdd != null) {
            newDesc.setText(textToAdd)
        }
        if (taskToEdit != null) {
            resultCode = EDIT_TASK_REQUEST_CODE
            newTitle.setText(taskToEdit.title)
            newDesc.setText(taskToEdit.description)
        }


        val valider = this.findViewById<Button>(R.id.valider)
        valider.setOnClickListener {

            val newTask = Task(
                    id = taskToEdit?.id ?: UUID.randomUUID().toString(),
                    title = newTitle.text.toString(),
                    description = newDesc.text.toString())

            intent.putExtra(TASK_KEY, newTask)
            setResult(resultCode, intent)
            finish()
        }

    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 666
        const val EDIT_TASK_REQUEST_CODE = 667
        const val TASK_KEY = "cle"
        const val TEXT_KEY = "text"
    }
}

