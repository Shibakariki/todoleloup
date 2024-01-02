package com.example.todoleloup.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoleloup.detail.ui.theme.TodoleloupTheme
import com.example.todoleloup.list.Task
import java.util.*

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val task = intent.getSerializableExtra("task") as Task?
        setContent {
            TodoleloupTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Detail(task,onValidate = { task ->
                        intent.putExtra("task", task)
                        setResult(RESULT_OK, intent)
                        finish()
                    })
                }
            }
        }
    }
}

@Composable
fun Detail(task : Task?, onValidate: (Task) -> Unit) {
    var task by remember { mutableStateOf(task ?: Task("","","")) }
    var id by remember { mutableStateOf(task.id) }
    var title by remember { mutableStateOf(task.title) }
    var description by remember { mutableStateOf(task.description) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Task Detail",
            style = MaterialTheme.typography.headlineLarge
        )
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") }
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        Button(onClick = {
            onValidate(Task(id = id, title = title, description = description))
        }) {
            Text(text = "Validate")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TodoleloupTheme {
        Detail(Task("0","Quoi ?","Coubéh"),onValidate = {})
    }
}