package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import Activities.TaskCreatingActivity;
import Models.Task;
import Repository.TaskRepository;


public class MainActivity extends AppCompatActivity implements NumbersAdapter.OnClickListener {

    private androidx.recyclerview.widget.RecyclerView numbersList;
    private NumbersAdapter tasksAdapter;
    private LiveData<List<Task>> tasks;
    private FloatingActionButton addButton;
    private TaskRepository taskRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Задачи");

        numbersList = findViewById(R.id.rv_numbers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        numbersList.setLayoutManager(layoutManager);
        numbersList.setHasFixedSize(true);

        taskRepository = new TaskRepository(getApplication());

        tasksAdapter = new NumbersAdapter();
        numbersList.setAdapter(tasksAdapter);
        addButton = findViewById(R.id.addButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTask();
            }
        });

        taskRepository.getAll().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {

                if (tasks != null) {
                    if (tasksAdapter.getItemCount() != tasks.size()) {
                        tasksAdapter.setTasks(tasks);
                    }

                    tasksAdapter.setOnClickOnCheckBoxListener(sender -> {
                        for (int i = 0; i < tasks.size(); i++) {
                            if (tasks.get(i).getId() == sender) {
                                tasks.get(i).setCompleted(!tasks.get(i).getCompleted());
                                taskRepository.update(tasks.get(i));
                            }
                        }
                    });
                }
            }
        });

        tasksAdapter.setOnClickOnTaskListener(sender -> {
            Intent intent = new Intent(this, TaskCreatingActivity.class);
            intent.putExtra("taskData", sender);
            startActivity(intent);
        });
    };

    public void createTask(){

        Intent intent = new Intent(this, TaskCreatingActivity.class);

        startActivity(intent);
    }

    public void updateTask(String text, int position){
        Task task = tasks.getValue().get(position);

        task.setTaskText(text);

        taskRepository.update(task);

        tasksAdapter.notifyDataSetChanged();
    }

    public void deleteTask(Task task, int position){
        tasks.getValue().remove(position);

        taskRepository.delete(task);

        tasksAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(long id) {
        Intent intent = new Intent(this, TaskCreatingActivity.class);
        intent.putExtra("taskData", id);
        startActivity(intent);
    }
}