package com.paulzixuanhugo.todo.tasklist

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.paulzixuanhugo.todo.R

class TaskListAdapter(private val taskList: List<Task>) :
    RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task) {
            itemView.apply {
                val textView  = itemView.findViewById<TextView>(R.id.task_title)
                val descriptionView  = itemView.findViewById<TextView>(R.id.task_description)
                val deleteButton = itemView.findViewById<ImageButton>(R.id.delete_button)
                textView.text = task.title
                descriptionView.text = task.description
                deleteButton.setOnClickListener { taskView ->
                    onDeleteClickListener(taskView)
                }
            }
        }
    }
    //var onDeleteClickListener: ((Task) -> Unit)? = null
    fun onDeleteClickListener(taskView: View ) {
        print("\nDeleting task.")
    }
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