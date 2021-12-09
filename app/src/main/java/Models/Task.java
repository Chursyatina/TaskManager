package Models;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Base64;

import Util.ImageConverter;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private long id;

    @ColumnInfo(name = "task_text")
    private String taskText;

    @ColumnInfo(name = "task_description")
    private String taskDescription;

    @ColumnInfo(name = "is_task_completed")
    private boolean isCompleted;

    public Task(){

    }

    public Task(long Id, String text, String description, Bitmap taskImage){
        taskText = text;
        taskDescription = description;
        id = Id;
        isCompleted = false;
    }


    public Task(String text, boolean completed){
        taskText = text;
        isCompleted = completed;
    }

    public Task(String text){
        taskText = text;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }
}
