package com.paulzixuanhugo.todo.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paulzixuanhugo.todo.network.TasksRepository
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel() {

    // Ces deux variables encapsulent la même donnée:

    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableLiveData<List<Task>>()
    // [taskList] est publique mais non-modifiable: on pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: LiveData<List<Task>> = _taskList

    private val repository = TasksRepository()

    fun refreshTasks() {
        viewModelScope.launch {
            _taskList.value = repository.refresh()
        }
    }
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task.id)
            refreshTasks()
        }
    }
    fun addTask(task: Task) {
        viewModelScope.launch {
            repository.createTaskOnline(task)
            refreshTasks()
        }
    }
    fun editTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            refreshTasks()
        }
    }
}