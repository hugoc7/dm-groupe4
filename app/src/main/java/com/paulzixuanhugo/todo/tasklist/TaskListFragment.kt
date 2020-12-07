package com.paulzixuanhugo.todo.tasklist

import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.paulzixuanhugo.todo.R
import com.paulzixuanhugo.todo.network.Api
import com.paulzixuanhugo.todo.network.TasksRepository
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.*
import androidx.lifecycle.Observer

class TaskListFragment : Fragment() {

    private val tasksRepository = TasksRepository()


    private val taskList = mutableListOf(
            Task(id = "id_1", title = "Task 1", description = "no description"),
            Task(id = "id_2", title = "Task 2", description = "no description"),
            Task(id = "id_3", title = "Task 3", description = "no description")
    )

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            val myTitle = view?.findViewById<TextView>(R.id.textView3)
            myTitle?.text = "${userInfo.firstName} ${userInfo.lastName}"
            tasksRepository.refresh()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val myAdapter = TaskListAdapter(taskList)
        recyclerView.adapter = myAdapter

        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        fab.setOnClickListener{
            val newTask = Task(id = UUID.randomUUID().toString(), title = "Task ${taskList.size + 1}", description = "no description")
            lifecycleScope.launch {
                tasksRepository.createTaskOnline(newTask)
                tasksRepository.refresh()
            }
            myAdapter.notifyDataSetChanged()
        }

        // Dans onViewCreated()
        tasksRepository.taskList.observe(viewLifecycleOwner, Observer {
            taskList.clear()
            taskList.addAll(it)
            myAdapter.notifyDataSetChanged()
        })

        // "implémentation" de la lambda dans le fragment:
        myAdapter.onDeleteClickListener = { task ->
            // Supprimer la tâche
            taskList.remove(task)
            myAdapter.notifyDataSetChanged()
        }
    }
}