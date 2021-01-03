package com.paulzixuanhugo.todo.task

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.paulzixuanhugo.todo.network.TasksRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TaskBroadcastReceiver : BroadcastReceiver() {
    private val tasksRepository = TasksRepository()

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if(intent.action == MARK_TASK_AS_DONE)
        {
            val taskId = intent.getSerializableExtra(TASK_ID) as? String ?: return
            deleteTask(taskId)
        }
    }

    private fun deleteTask (taskId: String) {
        GlobalScope.launch {
            tasksRepository.delete(taskId)
        }
    }

    companion object {
        const val MARK_TASK_AS_DONE = "mark_as_done"
        const val DELETE_TASK = "delete_task"
        const val TASK_ID = "task_id"
    }
}