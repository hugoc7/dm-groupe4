package com.paulzixuanhugo.todo.tasklist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.paulzixuanhugo.todo.MainActivity
import com.paulzixuanhugo.todo.R
import com.paulzixuanhugo.todo.network.Api
import com.paulzixuanhugo.todo.network.TasksRepository
import com.paulzixuanhugo.todo.tasklist.task.TaskActivity
import com.paulzixuanhugo.todo.userinfo.UserInfoActivity
import kotlinx.coroutines.launch


class TaskListFragment : Fragment() {

    private val tasksRepository = TasksRepository()

    private val viewModel by viewModels<TaskListViewModel>()


    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val userInfo = Api.userService.getInfo().body()!!
            val myTitle = view?.findViewById<TextView>(R.id.textView3)
            myTitle?.text = "${userInfo.firstName} ${userInfo.lastName}"
            tasksRepository.refresh()
            val myImage = view?.findViewById<ImageView>(R.id.imageView)
            if (userInfo.avatar != null) {
                myImage?.load(userInfo.avatar) { transformations(CircleCropTransformation()) }
            } else {
                myImage?.load("https://i.imgur.com/Fy4YqZg.png") { transformations(CircleCropTransformation()) } //Avatar par défaut
            }
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

        val myImage = view?.findViewById<ImageView>(R.id.imageView)
        myImage.setOnClickListener() {
            val intent = Intent(activity, UserInfoActivity::class.java)
            startActivity(intent)
        }
    }
}