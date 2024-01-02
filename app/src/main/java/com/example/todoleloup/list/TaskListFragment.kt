package com.example.todoleloup.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.todoleloup.data.Api
import com.example.todoleloup.databinding.FragmentTaskListBinding
import com.example.todoleloup.detail.DetailActivity
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {
    private var taskList = listOf(
        Task(id = "id_1", content = "Task 1", description = "description 1"),
        Task(id = "id_2", content = "Task 2"),
        Task(id = "id_3", content = "Task 3")
    )
    private val viewModel: TasksListViewModel by viewModels()
    override fun onResume() {
        super.onResume()
        // TODO: ça crash ici => viewModel.refresh()
        /*lifecycleScope.launch {
            try {
                // Supposons que Api.userWebService.fetchUser() est une méthode suspendue qui renvoie une réponse
                val user = Api.userWebService.fetchUser().body()!!

                // Mise à jour de la TextView sur le thread UI
                binding.testApi.text = user.name
            } catch (e: Exception) {
                // Gérez l'erreur ici, par exemple en affichant un message d'erreur
                // Pour cet exemple, nous allons simplement imprimer l'erreur
                e.printStackTrace()
            }
        }*/
    }

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TaskListAdapter

    val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val newTask = result.data?.getSerializableExtra("task") as Task?
        if (newTask != null){
            taskList = taskList + newTask
            refreshAdapter(taskList)
        }
    }
    val editTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task?
        if (task != null){
            taskList = taskList.map { if (it.id == task.id) task else it }
            refreshAdapter(taskList)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            /*viewModel.tasksStateFlow.collect { newList ->
                // cette lambda est exécutée à chaque fois que la liste est mise à jour dans le VM
                // -> ici, on met à jour la liste dans l'adapter
            }*/
        }


            adapter = TaskListAdapter()
        adapter.onClickDelete = { task ->
            taskList = taskList.filter { it.id != task.id }
            adapter.submitList(taskList)
        }
        adapter.onClickEdit = { task ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)
        }

        binding.recyclerview.adapter = adapter
        adapter.submitList(taskList)

        binding.addButton.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            createTask.launch(intent)
        }
    }
    private fun refreshAdapter(newList: List<Task>) {
        adapter.submitList(newList.toList()) // Créez une copie de la liste pour garantir la mise à jour de l'adaptateur
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}