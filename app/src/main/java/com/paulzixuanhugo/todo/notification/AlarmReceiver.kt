package com.paulzixuanhugo.todo.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Debug
import com.paulzixuanhugo.todo.notification.NotificationUtils
import com.paulzixuanhugo.todo.task.Task
import com.paulzixuanhugo.todo.task.TaskActivity
import com.paulzixuanhugo.todo.task.TaskBroadcastReceiver

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        val task = intent.getSerializableExtra(TaskActivity.TASK_KEY) as? Task
        if (task == null) {
            print("[HUGO] alarm receiver ne recoit pas de task !")
            return
        }
        print("Alarm !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        val notificationUtils = NotificationUtils(context)
        val notification = notificationUtils.getNotificationBuilder(task).build()
        notificationUtils.getManager().notify(150, notification)
    }
}