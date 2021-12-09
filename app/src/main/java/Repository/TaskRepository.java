package Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import java.util.List;

import Data.TaskDAO;
import Data.TasksAppDatabase;
import Models.Task;

public class TaskRepository implements Repository<Task>{

    private final TaskDAO taskDAO;
    private final LiveData<List<Task>> allTasks;

    public TaskRepository(Application application){
        TasksAppDatabase db = TasksAppDatabase.getDatabase(application);
        taskDAO = db.getTaskDAO();
        allTasks = taskDAO.getAllTasks();
    }

    @Override
    public LiveData<List<Task>> getAll() {
        return allTasks;
    }

    @Override
    public LiveData<Task> getById(long id) {
        return Transformations.map(allTasks, input -> input.stream()
                .filter(m -> m.getId() == id)
                .findAny()
                .orElse(null)
        );
    }

    @Override
    public void insert(Task task) {
        TasksAppDatabase.databaseWriteExecutor.execute(() -> taskDAO.addTask(task));
    }

    @Override
    public void update(Task task) {
        TasksAppDatabase.databaseWriteExecutor.execute(() -> taskDAO.updateTask(task));
    }

    @Override
    public void delete(Task task) {
        TasksAppDatabase.databaseWriteExecutor.execute(() -> taskDAO.deleteTask(task));
    }
}
