package com.paulzixuanhugo.todo.tasklist

import android.content.Intent
import android.content.Intent.getIntent
import android.content.Intent.makeMainActivity
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
import com.paulzixuanhugo.todo.MainActivity


class TaskListFragment : Fragment() {

    private val tasksRepository = TasksRepository()

    private val viewModel by viewModels<TaskListViewModel>()


    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val userInfo = Api.INSTANCE.userService.getInfo().body()!!
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
        viewModel.refreshTasks()
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        val mainActivity = activity as MainActivity


        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val myAdapter = TaskListAdapter()
        recyclerView.adapter = myAdapter


        val addOrEditTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = it.data!!.getSerializableExtra(TaskActivity.TASK_KEY) as Task
            lifecycleScope.launch {
                if(it.resultCode == TaskActivity.ADD_TASK_REQUEST_CODE) {
                    viewModel.addTask(task)
                }
                else if (it.resultCode == TaskActivity.EDIT_TASK_REQUEST_CODE) {
                    viewModel.editTask(task)
                }
            }
        }
        //receive text intent from another application
        if(mainActivity.intent?.action == Intent.ACTION_SEND) {
            if ("text/plain" == mainActivity.intent.type) {
                val text = mainActivity.intent.getStringExtra(Intent.EXTRA_TEXT).toString()
                val intent = Intent(activity, TaskActivity::class.java)
                intent.putExtra(TaskActivity.TEXT_KEY, text)
                addOrEditTask.launch(intent)
            }
        }

        val fab = view.findViewById<FloatingActionButton>(R.id.floatingActionButton2)
        fab.setOnClickListener{
            val intent = Intent(activity, TaskActivity::class.java)
            addOrEditTask.launch(intent)
        }

        viewModel.taskList.observe(viewLifecycleOwner, Observer { newList ->
            myAdapter.taskList = newList.orEmpty()
            myAdapter.notifyDataSetChanged()
        })

        myAdapter.onDeleteClickListener = { task ->
            lifecycleScope.launch {
                viewModel.deleteTask(task)
            }
        }
        myAdapter.onEditClickListener = { task ->
            val intent = Intent(activity, TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_KEY, task)
            addOrEditTask.launch(intent)
        }
        myAdapter.onLongClickListener = { task ->
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, task.title)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }
}