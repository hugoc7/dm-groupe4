package com.paulzixuanhugo.todo.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.paulzixuanhugo.todo.tasklist.Task
import retrofit2.Response
import retrofit2.http.*

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()

    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            if (fetchedTasks != null) {
                _taskList.value = fetchedTasks!!
            }
        }
    }

    suspend fun createTaskOnline(task: Task){
        tasksWebService.createTask(task)
    }

    suspend fun updateTask(task:Task) {
        val response = tasksWebService.updateTask(task)
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { task.id == it.id }
        if (response != null)
            editableList[position] = response.body()!!
        _taskList.value = editableList
    }

    suspend fun delete(id: String) {
        val tasksResponse = tasksWebService.deleteTask(id)
        if (tasksResponse.isSuccessful) {
            _taskList.value = _taskList.value?.filter { it.id != id }
        }
    }

/*
    suspend fun updateTask(task: Task) {

        val editedTask = tasksWebService.updateTask(task)
        val editableList = _taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { task.id == it.id }
        editableList[position] = editedTask
        _taskList.value = editableList
    }*/
}