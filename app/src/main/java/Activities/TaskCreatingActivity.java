package Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.myapplication.NumbersAdapter;
import com.example.myapplication.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collector;

import Data.TasksAppDatabase;
import Models.Task;
import Repository.TaskRepository;
import Util.ImageConverter;

public class TaskCreatingActivity extends AppCompatActivity {

    public EditText taskName;
    public EditText taskDescription;

    private LiveData<List<Task>> tasks;
    private TaskRepository taskRepository;

    private Button addButton;
    private Button deleteButton;

    private Bundle arguments;

    private final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_creating_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        taskName = findViewById(R.id.task_input);
        taskDescription = findViewById(R.id.task_description_input);

        taskRepository = new TaskRepository(getApplication());

        tasks = taskRepository.getAll();

        tasks.observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                arguments = getIntent().getExtras();
                setTitle("Создание задачи");

                if (arguments != null) {
                    setTitle("Задача");
                    long id = (long) arguments.get("taskData");

                    addButton.setText("Сохранить задачу");

                    addButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            saveChangedTask();
                        }
                    });

                    for (int i = 0; i < tasks.size(); i++) {
                        if (tasks.get(i).getId() == id) {
                            taskName.setText(tasks.get(i).getTaskText());
                            taskDescription.setText(tasks.get(i).getTaskDescription());
                            break;
                        }
                    }

                    deleteButton.setVisibility(View.VISIBLE);
                }
            }
        });

        addButton = findViewById(R.id.addButton);
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.INVISIBLE);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTask();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCurrentTask();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void addNewTask(){
        if (taskName.getText().toString().length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Название задачи не может быть пустым", Toast.LENGTH_SHORT);
            toast.show();
        }
        else{
            Task newTask = new Task();
            newTask.setTaskText(String.valueOf(taskName.getText()));
            newTask.setTaskDescription(String.valueOf(taskDescription.getText()));
            newTask.setCompleted(false);

            taskRepository.insert(newTask);

            onBackPressed();
        }
    }

    public void deleteCurrentTask(){
        arguments = getIntent().getExtras();
        long id = (long) arguments.get("taskData");

        Task newTask = new Task();
        newTask.setId(id);
        newTask.setTaskText(taskName.getText().toString());
        newTask.setTaskDescription(taskDescription.getText().toString());

        taskRepository.delete(newTask);

        onBackPressed();
    }

    public void saveChangedTask(){
        arguments = getIntent().getExtras();
        long id = (long) arguments.get("taskData");

        if (taskName.getText().toString().length() == 0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Название задачи не может быть пустым", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            Task newTask = new Task();
            newTask.setId(id);
            newTask.setTaskText(taskName.getText().toString());
            newTask.setTaskDescription(taskDescription.getText().toString());

            taskRepository.update(newTask);

            onBackPressed();
        }
    }
}