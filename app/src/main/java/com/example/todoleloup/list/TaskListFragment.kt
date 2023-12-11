package com.example.todoleloup.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todoleloup.databinding.FragmentTaskListBinding
import java.util.UUID

// Déclaration de l'interface
interface TaskListListener {
    fun onClickDelete(task: Task)
}

class TaskListFragment : Fragment() {
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: TaskListAdapter

    private val adapterListener: TaskListListener = object : TaskListListener {
        override fun onClickDelete(task: Task) {
            val newList = taskList - task
            taskList = newList
            adapter.submitList(newList)
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
        adapter = TaskListAdapter()
        binding.recyclerview.adapter = adapter
        adapter.submitList(taskList)

        binding.addButton.setOnClickListener {
            val newTask = Task(
                id = UUID.randomUUID().toString(),
                title = "Task ${taskList.size + 1}",
                description = "New task description"
            )
            taskList = taskList + newTask
            refreshAdapter(taskList)
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