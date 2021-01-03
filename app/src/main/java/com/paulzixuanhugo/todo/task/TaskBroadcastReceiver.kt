package com.paulzixuanhugo.todo.task

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.paulzixuanhugo.todo.network.TasksRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class TaskBroadcastReceiver : BroadcastReceiver() {
    private val tasksRepository = TasksRepository()

    override fun onReceive(context: Context, intent: Intent) {
        Log.e("INFO", "INTENT RECU TASK BROADCAST RECEIVER")
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        if(intent.action == MARK_TASK_AS_DONE)
        {
            val taskId = intent.getStringExtra(TASK_ID)
                if(taskId == null){
                    Log.e("Erreur", "taskBroadcatsReceiver = taskid null")
                    return
                }

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
        const val TASK_TITLE = "task_title"
    }
}