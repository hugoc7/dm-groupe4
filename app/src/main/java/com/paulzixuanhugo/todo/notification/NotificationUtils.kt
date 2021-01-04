package com.paulzixuanhugo.todo.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.paulzixuanhugo.todo.MainActivity
import com.paulzixuanhugo.todo.R
import com.paulzixuanhugo.todo.task.Task
import com.paulzixuanhugo.todo.task.TaskBroadcastReceiver

class  NotificationUtils(base: Context) : ContextWrapper(base) {

    val MYCHANNEL_ID = "App Alert Notification ID"
    val MYCHANNEL_NAME = "App Alert Notification"

    private var manager: NotificationManager? = null

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels()
        }
    }

    // Create channel for Android version 26+
    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannels() {
        val channel = NotificationChannel(MYCHANNEL_ID, MYCHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        channel.enableVibration(true)

        getManager().createNotificationChannel(channel)
    }

    // Get Manager
    fun getManager() : NotificationManager {
        if (manager == null) manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return manager as NotificationManager
    }

    fun getNotificationBuilder(task: Task): NotificationCompat.Builder {

        Log.e("TASK ID 3", task.id)
        Log.e("TASK ID 3", task.title)

        var editIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        var editPendingIntent = PendingIntent.getActivity(this, 0, editIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        var deleteIntent = Intent(this, TaskBroadcastReceiver::class.java).apply {
            action = TaskBroadcastReceiver.MARK_TASK_AS_DONE
            putExtra(TaskBroadcastReceiver.TASK_ID, task.id)
        }
        var deletePendingIntent: PendingIntent =
                PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        return NotificationCompat.Builder(applicationContext, MYCHANNEL_ID)
                .setContentTitle("Alarm!")
                .setContentText(task.title)
                .setSmallIcon(R.drawable.material_ic_calendar_black_24dp)
                .setColor(Color.YELLOW)
                .setContentIntent(editPendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true)
                .addAction(android.R.drawable.ic_input_delete, "Mark as Done", deletePendingIntent)
    }
}