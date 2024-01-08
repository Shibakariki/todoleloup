package com.example.todoleloup.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoleloup.data.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TasksListViewModel : ViewModel() {
    private val webService = Api.tasksWebService

    public val tasksStateFlow = MutableStateFlow<List<Task>>(emptyList())

    fun refresh() {
        viewModelScope.launch {
            try {
                val response = webService.fetchTasks() // Call HTTP (opération longue)

                if (response.isSuccessful) {
                    // Si la réponse est réussie, utilisez le corps de la réponse
                    val fetchedTasks = response.body()
                    if (fetchedTasks != null) {
                        tasksStateFlow.value = fetchedTasks // Met à jour le flow
                    } else {
                        Log.e("Network", "Réponse réussie mais le corps est null")
                    }
                } else {
                    // Gère le cas où la réponse n'est pas réussie
                    Log.e("Network", "Erreur de réponse: ${response.message()}")
                }
            } catch (e: Exception) {
                // Gérer ici les exceptions qui peuvent se produire lors de l'appel réseau
                Log.e("Network", "Erreur lors de la récupération des tâches", e)
            }
        }
    }

    // à compléter plus tard:
    fun add(task: Task) {
        viewModelScope.launch {
            val response = webService.create(task)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.message()}")
                return@launch
            }
            val createdTask = response.body()!!
            tasksStateFlow.value = tasksStateFlow.value + createdTask
        }
    }
    fun edit(task: Task) {}
    fun remove(task: Task) {}
}