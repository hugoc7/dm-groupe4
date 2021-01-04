package com.paulzixuanhugo.todo.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import com.paulzixuanhugo.todo.network.TasksRepository
import com.paulzixuanhugo.todo.notification.NotificationUtils
import com.paulzixuanhugo.todo.task.Task
import com.paulzixuanhugo.todo.task.TaskActivity
import com.paulzixuanhugo.todo.task.TaskBroadcastReceiver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class AlarmReceiver : BroadcastReceiver() {
    private val tasksRepository = TasksRepository()
    override fun onReceive(context: Context, intent: Intent) {
        //This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        var taskId = intent.getStringExtra("task_id")

        if (taskId == null)
            return

        GlobalScope.launch{
            var tasks = tasksRepository.refresh()
            if (tasks != null) {
                for (task in tasks) {
                    if (task.title == taskId) {
                        var notificationUtils = NotificationUtils(context)
                        var notification = notificationUtils.getNotificationBuilder(task).build()
                        notificationUtils.getManager().notify(150, notification)
                    }
                }
            }
        }
    }
}