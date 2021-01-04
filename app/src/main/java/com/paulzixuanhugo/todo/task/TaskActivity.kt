package com.paulzixuanhugo.todo.task

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.paulzixuanhugo.todo.R
import com.paulzixuanhugo.todo.notification.AlarmReceiver
import java.text.SimpleDateFormat
import java.util.*

class TaskActivity : AppCompatActivity() {

    private val timeFormatter = SimpleDateFormat("HH:mm", Locale.US)
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    private fun createAlarm(calendar: Calendar, taskid : String) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notifyIntent = Intent(this, AlarmReceiver::class.java)
        notifyIntent.putExtra(TaskActivity.TASK_ID, taskid)
        Log.e("TASK ID", taskid)
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingNotifyIntent = PendingIntent.getBroadcast(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, 2000 , pendingNotifyIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_activity)
        val newTitle = this.findViewById<EditText>(R.id.titleInput)
        val newDesc = this.findViewById<EditText>(R.id.descriptionInput)
        val newTime = this.findViewById<TextView>(R.id.timeInput)
        val newDate = this.findViewById<TextView>(R.id.dateInput)
        val editTimeButton = this.findViewById<ImageButton>(R.id.editTimeButton)
        val editDateButton = this.findViewById<ImageButton>(R.id.editDateButton)


        val taskToEdit = intent.getSerializableExtra(TASK_KEY) as? Task
        val textToAdd = intent.getSerializableExtra(TEXT_KEY) as? String
        var resultCode = ADD_TASK_REQUEST_CODE

        //déprécié au profit de java.time, mais dispo seulement dans api 26
        val currentDueDate = Calendar.getInstance()
        val timePickerFragment = TimePickerFragment()
        val datePickerFragment = DatePickerFragment()

        if (textToAdd != null) {
            newDesc.setText(textToAdd)
        }
        if (taskToEdit != null) {
            resultCode = EDIT_TASK_REQUEST_CODE
            newTitle.setText(taskToEdit.title)
            newDesc.setText(taskToEdit.description)
            currentDueDate.time = taskToEdit.dueDate
            newTime.setText(timeFormatter.format(taskToEdit.dueDate))
            newDate.setText(dateFormatter.format(taskToEdit.dueDate))
        }
        editTimeButton.setOnClickListener {
            timePickerFragment.onTimeSetListener = { hours, minutes ->
                currentDueDate.set(Calendar.HOUR_OF_DAY, hours)
                currentDueDate.set(Calendar.MINUTE, minutes)
                newTime.setText(timeFormatter.format(currentDueDate.time))
            }
            timePickerFragment.hours = currentDueDate.get(Calendar.HOUR_OF_DAY)
            timePickerFragment.minutes = currentDueDate.get(Calendar.MINUTE)
            timePickerFragment.show(supportFragmentManager, "timePicker")
        }
        editDateButton.setOnClickListener {
            datePickerFragment.onDateSetListener = { day, month, year ->
                currentDueDate.set(Calendar.DAY_OF_MONTH, day)
                currentDueDate.set(Calendar.MONTH, month)
                currentDueDate.set(Calendar.YEAR, year)
                newDate.setText(dateFormatter.format(currentDueDate.time))
            }
            datePickerFragment.day = currentDueDate.get(Calendar.DAY_OF_MONTH)
            datePickerFragment.month = currentDueDate.get(Calendar.MONTH)
            datePickerFragment.year = currentDueDate.get(Calendar.YEAR)
            datePickerFragment.show(supportFragmentManager, "datePicker")
        }

        val valider = this.findViewById<Button>(R.id.valider)
        valider.setOnClickListener {

            val newTask = Task(
                    id = taskToEdit?.id ?: UUID.randomUUID().toString(),
                    title = newTitle.text.toString(),
                    description = newDesc.text.toString(),
                    dueDate = currentDueDate.time)

            //Setup alarme ici
            Log.e("ALARM CREATION",newTask.id)
            createAlarm(currentDueDate, newTask.title)

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
        const val TASK_ID = "task_id"
    }
}

