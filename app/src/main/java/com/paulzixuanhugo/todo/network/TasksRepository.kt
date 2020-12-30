package com.paulzixuanhugo.todo.network

import com.paulzixuanhugo.todo.task.Task


class TasksRepository {
    private val tasksWebService = Api.INSTANCE.tasksWebService



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

    suspend fun updateTask(task: Task) {
        tasksWebService.updateTask(task)

        // Solution alternative pour eviter de refresh, a mettre a jour avec la nouvelle archi
        /*val editableList = viewModel.taskList.value.orEmpty().toMutableList()
        val position = editableList.indexOfFirst { task.id == it.id }
        if (response != null)
            editableList[position] = response.body()!!
        _taskList.value = editableList*/
    }

    suspend fun delete(id: String) {
        tasksWebService.deleteTask(id)

        // Solution alternative pour eviter de refresh, a mettre a jour avec la nouvelle archi
        /*if (tasksResponse.isSuccessful) {
            _taskList.value = _taskList.value?.filter { it.id != id }
        }*/
    }

}