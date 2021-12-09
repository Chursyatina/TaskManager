package Repository;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface Repository<M> {
    LiveData<List<M>> getAll();

    LiveData<M> getById(long id);

    void insert(M model);
    void update(M model);
    void delete(M model);
}
