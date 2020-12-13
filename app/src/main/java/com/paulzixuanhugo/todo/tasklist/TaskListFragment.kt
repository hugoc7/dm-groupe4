package com.paulzixuanhugo.todo.tasklist

import android.content.Intent
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
import com.paulzixuanhugo.todo.tasklist.task.TaskActivity
import androidx.fragment.app.viewModels


class TaskListFragment : Fragment() {

    private val tasksRepository = TasksRepository()

    private val viewModel by viewModels<TaskListViewModel>()

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            val myTitle = view?.findViewById<TextView>(R.id.textView3)
            myTitle?.text = "${userInfo.firstName} ${userInfo.lastName}"
            //tasksRepository.refresh()
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
        val myAdapter = TaskListAdapter()
        recyclerView.adapter = myAdapter

        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        fab.setOnClickListener{
            viewModel.refreshTasks()
            //val intent = Intent(activity, TaskActivity::class.java)
            //startActivityForResult(intent, TaskActivity.ADD_TASK_REQUEST_CODE)
        }

        viewModel.taskList.observe(viewLifecycleOwner, Observer { newList ->
            myAdapter.taskList = newList.orEmpty()
            myAdapter.notifyDataSetChanged()
        })

        myAdapter.onDeleteClickListener = { task ->
            lifecycleScope.launch {
                tasksRepository.delete(task.id)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val newTask = data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
        lifecycleScope.launch {
            tasksRepository.createTaskOnline(newTask)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}