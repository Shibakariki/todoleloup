package com.example.todoleloup.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.todoleloup.databinding.FragmentTaskListBinding
import com.example.todoleloup.detail.DetailActivity
class TaskListFragment : Fragment() {
    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

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