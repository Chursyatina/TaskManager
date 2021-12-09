package Data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Models.Task;

@Database(entities = {Task.class}, version = 2)
public abstract class TasksAppDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 8;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newScheduledThreadPool(NUMBER_OF_THREADS);

    private static volatile TasksAppDatabase INSTANCE;

    public static TasksAppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TasksAppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TasksAppDatabase.class, "task_app_database")
                            .fallbackToDestructiveMigration() //todo delete
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TaskDAO getTaskDAO();
}
