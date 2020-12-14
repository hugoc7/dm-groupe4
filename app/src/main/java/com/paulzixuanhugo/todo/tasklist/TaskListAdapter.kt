package com.paulzixuanhugo.todo.tasklist

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paulzixuanhugo.todo.R
import kotlin.properties.Delegates

class TaskListAdapter() :
    RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    // On initialise la tasklist de façon à ce que l'adapter se notifie automatiquement lui même à chaque fois qu'on modifie la liste:
    var taskList: List<Task> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            itemView.apply {
                val textView  = itemView.findViewById<TextView>(R.id.task_title)
                val descriptionView  = itemView.findViewById<TextView>(R.id.task_description)
                val deleteButton = itemView.findViewById<ImageButton>(R.id.delete_button)
                val editButton = itemView.findViewById<ImageButton>(R.id.edit_button)
                textView.text = task.title
                descriptionView.text = task.description
                deleteButton.setOnClickListener {
                    onDeleteClickListener?.invoke(task)
                }
                editButton.setOnClickListener {
                    onEditClickListener?.invoke(task)
                }
                textView.setOnLongClickListener {
                    onLongClickListener?.invoke(task)
                    return@setOnLongClickListener true
                }
            }
        }
    }
    var onDeleteClickListener: ((Task) -> Unit)? = null
    var onEditClickListener: ((Task) -> Unit)? = null
    var onLongClickListener: ((Task) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}