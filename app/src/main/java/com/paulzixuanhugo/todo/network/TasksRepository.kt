package com.paulzixuanhugo.todo.network

import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.paulzixuanhugo.todo.tasklist.Task
import com.paulzixuanhugo.todo.tasklist.TaskListViewModel
import androidx.fragment.app.viewModels

import retrofit2.Response
import retrofit2.http.*


class TasksRepository {
    private val tasksWebService = Api.tasksWebService



    suspend fun refresh(): List<Task>? {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            return tasksResponse.body()
        }
        return null
    }

    suspend fun createTaskOnline(task: Task){
        tasksWebService.createTask(task)
    }

    suspend fun updateTask(task:Task) {
        val response = tasksWebService.updateTask(task)
        //val editableList = viewModel.taskList.value.orEmpty().toMutableList()
        //val position = editableList.indexOfFirst { task.id == it.id }
        //if (response != null)
            //editableList[position] = response.body()!!
        //_taskList.value = editableList
    }

    suspend fun delete(id: String) {
        val tasksResponse = tasksWebService.deleteTask(id)

        // Solution alternative pour eviter de refresh, a mettre a jour avec la nouvelle archi
        /*if (tasksResponse.isSuccessful) {
            _taskList.value = _taskList.value?.filter { it.id != id }
        }*/
    }
}